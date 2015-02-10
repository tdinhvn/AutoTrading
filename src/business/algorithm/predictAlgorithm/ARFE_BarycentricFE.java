package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;

public class ARFE_BarycentricFE extends AbstractPredictAlgorithm{

	private int orderD;
	private double confidenceLevel;
	private int AR_period;
	
	public ARFE_BarycentricFE() {
		super(null, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public OutputForPredictionAlgorithm runAlgorithm() throws Exception {
		TreeMap<AssetEntity, ArrayList<PriceEntity>> map = new TreeMap<AssetEntity, ArrayList<PriceEntity>>();
		
		for (AssetEntity assetEntity : priceList.keySet()) {
			int n = priceList.get(assetEntity).size();
			ArrayList<PriceEntity> prices = new ArrayList<PriceEntity>();
			
			for (int i = 0; i < n-1; ++i) {
				prices.add(priceList.get(assetEntity).get(i));
			}
			
			map.put(assetEntity, prices);
		}
		
		AutoRegressionFE autoRegressionFE = new AutoRegressionFE();
		autoRegressionFE.setAR_period(AR_period);
		autoRegressionFE.setConfidenceLevel(confidenceLevel);
		autoRegressionFE.setFutureInterval(1);
		autoRegressionFE.setPriceEntityList(map);
		
		OutputForPredictionAlgorithm outputARFE = autoRegressionFE.runAlgorithm();
		
		for (AssetEntity assetEntity : map.keySet()) {
			while (map.get(assetEntity).size() > AR_period) {
				map.get(assetEntity).remove(0);
			}
		}		
		
		BarycentricRationalFE barycentricRationalFE = new BarycentricRationalFE();
		barycentricRationalFE.setFutureInterval(1);
		barycentricRationalFE.setOrderD(orderD);
		barycentricRationalFE.setPriceEntityList(map);
		
		OutputForPredictionAlgorithm outputBarycentricFE = barycentricRationalFE.runAlgorithm();
		
		AssetEntity assetEntity = priceList.firstKey();
		int n = priceList.get(assetEntity).size();
		double arfeError = Math.abs(outputARFE.getPredictionPriceList().get(assetEntity).get(1).getPrice() - priceList.get(assetEntity).get(n-1).getClose());
		double barycentricFEError = Math.abs(outputBarycentricFE.getPredictionPriceList().get(assetEntity).get(1).getPrice() - priceList.get(assetEntity).get(n-1).getClose());
	
		if (arfeError > barycentricFEError) {
			barycentricRationalFE = new BarycentricRationalFE();
			barycentricRationalFE.setFutureInterval(futureInterval);
			barycentricRationalFE.setOrderD(orderD);
			
			map = new TreeMap<AssetEntity, ArrayList<PriceEntity>>();
			
			n = priceList.get(assetEntity).size();
			ArrayList<PriceEntity> prices = new ArrayList<PriceEntity>();
			
			for (int i = n-AR_period; i < n; ++i) {
				prices.add(priceList.get(assetEntity).get(i));
			}
			
			map.put(assetEntity, prices);
			
			barycentricRationalFE.setPriceEntityList(map);
			return barycentricRationalFE.runAlgorithm();
		}
		
		autoRegressionFE = new AutoRegressionFE();
		autoRegressionFE.setAR_period(AR_period);
		autoRegressionFE.setConfidenceLevel(confidenceLevel);
		autoRegressionFE.setFutureInterval(futureInterval);
		autoRegressionFE.setPriceEntityList(priceList);
		
		return autoRegressionFE.runAlgorithm();

//		AssetEntity assetEntity = priceList.firstKey();
//		int n = priceList.get(assetEntity).size();		
//		ArrayList<PriceEntity> prices = priceList.get(assetEntity);
//		
//		if ((prices.get(n-1).getClose()-prices.get(n-2).getClose())*(prices.get(n-2).getClose()-prices.get(n-3).getClose()) > 0) {
//			AutoRegressionFE autoRegressionFE = new AutoRegressionFE();
//			autoRegressionFE.setAR_period(AR_period);
//			autoRegressionFE.setConfidenceLevel(confidenceLevel);
//			autoRegressionFE.setFutureInterval(futureInterval);
//			autoRegressionFE.setPriceEntityList(priceList);
//			
//			return autoRegressionFE.runAlgorithm();			
//		}
//		
//		BarycentricRationalFE barycentricRationalFE = new BarycentricRationalFE();
//		barycentricRationalFE.setFutureInterval(futureInterval);
//		barycentricRationalFE.setOrderD(orderD);
//		
//		TreeMap<AssetEntity, ArrayList<PriceEntity>> map = new TreeMap<AssetEntity, ArrayList<PriceEntity>>();
//		
//		n = priceList.get(assetEntity).size();
//		ArrayList<PriceEntity> priceListForBarycentric = new ArrayList<PriceEntity>();
//		
//		for (int i = n-AR_period; i < n; ++i) {
//			priceListForBarycentric.add(priceList.get(assetEntity).get(i));
//		}
//		
//		map.put(assetEntity, priceListForBarycentric);
//		
//		barycentricRationalFE.setPriceEntityList(map);
//		return barycentricRationalFE.runAlgorithm();		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public TreeMap<String, Class> getParametersList() {
		// TODO Auto-generated method stub
		TreeMap<String, Class> map = super.getParametersList();
		map.put("Confidence level", Double.class);
		map.put("AR period", Integer.class);
		map.put("Approximation Order", Integer.class);
		return map;
	}
	
	@Override
	public void setParametersValue(TreeMap<String, Object> map) {
		// TODO Auto-generated method stub
		super.setParametersValue(map);
		this.confidenceLevel = (Double) map.get("Confidence level");
		this.AR_period = (Integer) map.get("AR period");
		this.orderD = (Integer) map.get("Approximation Order");
	}
        
        @Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ARFE_BarycentricFE";
	}


	public int getOrderD() {
		return orderD;
	}

	public void setOrderD(int orderD) {
		this.orderD = orderD;
	}

	public double getConfidenceLevel() {
		return confidenceLevel;
	}

	public void setConfidenceLevel(double confidenceLevel) {
		this.confidenceLevel = confidenceLevel;
	}

	public int getAR_period() {
		return AR_period;
	}

	public void setAR_period(int aR_period) {
		AR_period = aR_period;
	}

}
