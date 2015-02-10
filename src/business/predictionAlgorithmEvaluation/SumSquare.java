package business.predictionAlgorithmEvaluation;

import java.util.Date;
import java.util.ArrayList;
import java.util.TreeMap;

import business.algorithm.predictAlgorithm.PriceEntry;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.PriceManager;

public class SumSquare extends PredictionCriteria {

	public SumSquare() {

	}

	/**
	 * Residual Sum of Squares (RSS) is the sum of squares of residuals. It is a
	 * measure of the discrepancy between the data and an estimation model. A
	 * small RSS indicates a tight fit of the model to the data.
	 */
	@Override
	public TreeMap<String, Double> evaluate() {
		PriceManager priceManager = new PriceManager();
		
		@SuppressWarnings("unchecked")
		TreeMap<AssetEntity, ArrayList<PriceEntry>> assetList = (TreeMap<AssetEntity, ArrayList<PriceEntry>>) paramOfPreditionCriteria
		.get("Prediction price list");
		
		double sum = 0;
		double t;
		
		for (AssetEntity assetEntity : assetList.keySet()) {
			
			ArrayList<PriceEntry> priceEntryList = assetList.get(assetEntity);
			TreeMap<Date, Double> priceList = new TreeMap<Date, Double>();
			
			// convert PriceEntry to TreeMap<Date, Double> for ease of comparison
			for (PriceEntry curPriceEntry : priceEntryList) {
				priceList.put(curPriceEntry.getDate(), curPriceEntry.getPrice());
			}
			
			// get asset from database
			java.sql.Date beginDate = new java.sql.Date(priceList.firstEntry()
					.getKey().getTime());
			java.sql.Date endDate = new java.sql.Date(priceList.lastEntry()
					.getKey().getTime());
			ArrayList<PriceEntity> realPriceList = priceManager
					.getPriceInInterval(assetEntity.getAssetID(), beginDate,
							endDate);		
			
			for (PriceEntity curEntity : realPriceList) {
				t = (priceList.get(curEntity.getDate()) - curEntity.getClose());
				sum += t * t;
			}
			
			
//		AssetEntity assetEntity = (AssetEntity) paramOfPreditionCriteria.get("Asset");
//		@SuppressWarnings("unchecked");		
//		TreeMap<Date, Double> priceList = (TreeMap<Date, Double>) paramOfPreditionCriteria.get("PriceList");
			
		}

		TreeMap<String, Double> map = (new TreeMap<String, Double>());
		map.put("RSS", sum);
		return map;
	}

	@Override
	public String toString() {
		return "Residual Sum of Squares";
	}

	@Override
	public void setParametersValue(TreeMap<String, Object> map) {
		paramOfPreditionCriteria.put("Prediction price list", map.get("Prediction price list"));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public TreeMap<String, Class> getParametersList() {
		// TODO Auto-generated method stub
		return null;
	}

}
