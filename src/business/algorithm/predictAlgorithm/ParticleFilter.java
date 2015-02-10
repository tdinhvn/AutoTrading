package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import umontreal.iro.lecuyer.probdist.NormalDist;
import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;

public class ParticleFilter extends AbstractPredictAlgorithm {

	private int numTrial;
	private int numPar;

	private ArrayList<Double> particleList;
	private ArrayList<Double> weight;

	public ParticleFilter() {
		super(null, null);
		particleList = new ArrayList<Double>();
		weight = new ArrayList<Double>();
	}

	private void particleGenerator(double mean, double sigma, int numPar) {
		double particle = 0;
		for (int i = 0; i < numPar; i++) {
			particle = NormalDist.inverseF(mean, sigma, Math.random());
			particleList.add(particle);
		}
	}

	private void weightByDistance(double predictedPoint) {
		double distance = 0;
		for (int i = 0; i < particleList.size(); i++) {
			distance = Math.abs(predictedPoint - particleList.get(i));
			weight.add(1 / (distance / predictedPoint));
		}

		weightNormalize();
	}

	private void weightNormalize() {
		double sum = 0;
		for (int i = 0; i < weight.size(); i++) {
			sum += weight.get(i);
		}

		for (int i = 0; i < weight.size(); i++) {
			weight.set(i, weight.get(i) / sum);
		}
	}

	public double doPrediction(double predictedPoint, double mean,
			double sigma, int numPar) {
		particleList.clear();
		weight.clear();
		particleGenerator(mean, sigma, numPar);
		weightByDistance(predictedPoint);
		weightNormalize();

		double sum = 0;
		for (int i = 0; i < particleList.size(); i++) {
			sum += particleList.get(i) * weight.get(i);
		}

		return sum;
	}

	private static double mean(ArrayList<Double> list) {
		double mean = 0;

		for (Double x : list) {
			mean += x;
		}

		return mean / list.size();
	}

	private static double variance(ArrayList<Double> list) {
		double var = 0;
		double mean = mean(list);

		for (Double x : list) {
			var += Math.pow(x - mean, 2);
		}

		var = var / (list.size() - 1);
		
		if (var == 0) {
			return 10e-5;
		}
		
		return var;
	}

	@Override
	public OutputForPredictionAlgorithm runAlgorithm() throws Exception {
		AssetEntity asset = priceList.firstKey();
		ArrayList<PriceEntity> priceEntityList = priceList.get(priceList
				.firstKey());

		ArrayList<Double> predictionPriceList = new ArrayList<Double>();

		for (int i = 0; i < futureInterval; i++) {
			predictionPriceList.add(0.0);
		}

		for (int i = 0; i < numTrial; i++) {
			ArrayList<Double> dataList = new ArrayList<Double>();
			for (PriceEntity priceEntity : priceEntityList) {
				dataList.add(priceEntity.getClose());
			}

			for (int j = 0; j < futureInterval; j++) {
				double newPredictedPrice = this.doPrediction(
						dataList.get(dataList.size() - 1), mean(dataList),
						variance(dataList), numPar);

				predictionPriceList.set(j, predictionPriceList.get(j)
						+ newPredictedPrice);
				
				dataList.add(newPredictedPrice);
				dataList.remove(0);
			}
		}

		for (int i = 0; i < predictionPriceList.size(); i++) {
			predictionPriceList.set(i, predictionPriceList.get(i) / numTrial);
		}

		predictionPriceList.add(0,
				priceEntityList.get(priceEntityList.size() - 1).getClose());
		
		Date startPredictionDate = priceEntityList.get(priceEntityList.size() - 1).getDate();
		ArrayList<PriceEntry> priceEntryList = Utility.constructPriceList(
				asset, predictionPriceList, startPredictionDate);
		TreeMap<AssetEntity, ArrayList<PriceEntry>> predictionPriceMap = new TreeMap<AssetEntity, ArrayList<PriceEntry>>();
		predictionPriceMap.put(asset, priceEntryList);
		return new CommonOutputForPredictionAlgorithm(predictionPriceMap);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public TreeMap<String, Class> getParametersList() {
		// TODO Auto-generated method stub
		TreeMap<String, Class> map = super.getParametersList();
		map.put("Number of Trial", Integer.class);
		map.put("Number of Particle", Integer.class);
		return map;
	}

	@Override
	public void setParametersValue(TreeMap<String, Object> map) {
		// TODO Auto-generated method stub
		super.setParametersValue(map);
		this.numTrial = (Integer) map.get("Number of Trial");
		this.numPar = (Integer) map.get("Number of Particle");
	}

	public int getNoTrial() {
		return numTrial;
	}

	public void setNoTrial(int noTrial) {
		this.numTrial = noTrial;
	}

	public int getNumPar() {
		return numPar;
	}

	public void setNumPar(int numPar) {
		this.numPar = numPar;
	}
	
	@Override
	public String toString() {
		return "Particle Filter";
	}

}
