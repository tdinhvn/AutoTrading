package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;

public class BarycentricRationalMA extends AbstractPredictAlgorithm{
	private int maPeriod;
	private int orderD;
	
	public BarycentricRationalMA() {
		super(null, null);
	}

	public int getMaPeriod() {
		return maPeriod;
	}

	public void setMaPeriod(int maPeriod) {
		this.maPeriod = maPeriod;
	}

	@Override
	public OutputForPredictionAlgorithm runAlgorithm() throws Exception {
		AssetEntity asset = priceList.firstKey();
		ArrayList<PriceEntity> priceEntityList = priceList.get(priceList
				.firstKey());
		ArrayList<Double> priceArrayList = Utility
				.convertPriceEntityListToPriceList(priceEntityList);

		/*
		 * Moving Average Step
		 */
		int nSample = priceArrayList.size();
		double alpha = 2.0/(maPeriod + 1);
		ArrayList<Double> movingAverage = new ArrayList<Double>();
		movingAverage.add(priceArrayList.get(0));
		for (int i = 1; i < nSample; ++i) {
			movingAverage.add(alpha*priceArrayList.get(i) + (1-alpha)*movingAverage.get(movingAverage.size()-1));
		}
				
		int n = movingAverage.size();
		double[] w = new double[n];
		
		//calculate weights
		for (int k = 0; k < n; ++k) {
			w[k] = 0;
			int imin = Math.max(k - getOrderD(), 0);
			int imax = Math.min(k, n - getOrderD() - 1);
			for (int i = imin; i <= imax; ++i) {
				double temp = Math.pow(-1, k);
				for (int j = i; j <= i + getOrderD(); ++j) {
					if (j != k) {
						temp *= 1.0 / (k - j);
					}
				}
				w[k] += temp;
			}
		}
				
		ArrayList<Double> predictionPriceList = new ArrayList<Double>();
		
		//calculate predicted price
		for (int i = n; i < n + futureInterval; ++i) {
			double numerator = 0;
			double denominator = 0;
			
			for (int j = 0; j < n; ++j) {
				numerator += w[j]*movingAverage.get(j)/(i-j);
				denominator += w[j]/(i-j);
			}
			
			predictionPriceList.add(numerator / denominator);
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

	public int getOrderD() {
		return orderD;
	}

	public void setOrderD(int orderD) {
		this.orderD = orderD;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public TreeMap<String, Class> getParametersList() {
		// TODO Auto-generated method stub
		TreeMap<String, Class> map = super.getParametersList();
		map.put("Approximation Order", Integer.class);
		map.put("MA Period", Integer.class);
		return map;
	}

	@Override
	public void setParametersValue(TreeMap<String, Object> map) {
		// TODO Auto-generated method stub
		super.setParametersValue(map);
		this.orderD = (Integer) map.get("Approximation Order");
		this.maPeriod = (Integer) map.get("MA Period");
	}	

        	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Barycentric (Moving Average)";
	}

	
}
