package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;

public class BarycentricRationalFE_ParticleFilter extends AbstractPredictAlgorithm{

	private int numTrial;
	private int numPar;
	private int numTrainingPF;
	private int orderD;
	
	public BarycentricRationalFE_ParticleFilter() {
		super(null, null);
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
		ParticleFilter particleFilter = new ParticleFilter();
		
		//BarycentricFE part
		BarycentricRationalFE barycentricFE = new BarycentricRationalFE();
		barycentricFE.setOrderD(orderD);
		barycentricFE.setFutureInterval(futureInterval);
		barycentricFE.setPriceEntityList(priceList);
		ArrayList<PriceEntry> barycentricFEResult = barycentricFE.runAlgorithm().getPredictionPriceList().firstEntry().getValue();
		ArrayList<Double> barycentricFEPredictedPriceList = new ArrayList<Double>();
		for (PriceEntry priceEntry : barycentricFEResult) {
			barycentricFEPredictedPriceList.add(priceEntry.getPrice());
		}
		
		//Particle Filter part
		ArrayList<Double> predictionPriceList = new ArrayList<Double>();

		for (int i = 0; i < futureInterval; i++) {
			predictionPriceList.add(0.0);
		}

		for (int i = 0; i < numTrial; i++) {
			ArrayList<Double> dataList = new ArrayList<Double>();
			int n = priceEntityList.size();
			for (int j = 0; j < numTrainingPF; ++j) {
				dataList.add(0,priceEntityList.get(n-j-1).getClose());
			}

			for (int j = 0; j < futureInterval; j++) {
				double newPredictedPrice = particleFilter.doPrediction(
						barycentricFEPredictedPriceList.get(j), mean(dataList),
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
		map.put("NoTrainingSamples For ParticleFilter", Integer.class);
		map.put("Approximation Order", Integer.class);
		return map;
	}

	@Override
	public void setParametersValue(TreeMap<String, Object> map) {
		// TODO Auto-generated method stub
		super.setParametersValue(map);
		this.numTrial = (Integer) map.get("Number of Trial");
		this.numPar = (Integer) map.get("Number of Particle");
		this.numTrainingPF = (Integer) map.get("NoTrainingSamples For ParticleFilter");
		this.orderD = (Integer) map.get("Approximation Order");
	}


	public int getNumTrial() {
		return numTrial;
	}

	public void setNumTrial(int numTrial) {
		this.numTrial = numTrial;
	}

	public int getNumPar() {
		return numPar;
	}

	public void setNumPar(int numPar) {
		this.numPar = numPar;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "BarycentricFE + Particle Filter";
	}

	public int getNumTrainingPF() {
		return numTrainingPF;
	}

	public void setNumTrainingPF(int numTrainingPF) {
		this.numTrainingPF = numTrainingPF;
	}

	public int getOrderD() {
		return orderD;
	}

	public void setOrderD(int orderD) {
		this.orderD = orderD;
	}

}
