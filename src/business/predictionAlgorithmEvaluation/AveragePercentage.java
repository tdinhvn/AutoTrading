package business.predictionAlgorithmEvaluation;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import business.algorithm.predictAlgorithm.PriceEntry;
import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.PriceManager;

public class AveragePercentage extends PredictionCriteria {

	public AveragePercentage() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Average Percentage Error (AP) is the average of error. It is a
	 * measure of the discrepancy between the data and an estimation model. A
	 * small AP indicates a tight fit of the model to the data.
	 */
	@Override
	public TreeMap<String, Double> evaluate() {
		PriceManager priceManager = new PriceManager();

		@SuppressWarnings("unchecked")
		TreeMap<AssetEntity, ArrayList<PriceEntry>> assetList = (TreeMap<AssetEntity, ArrayList<PriceEntry>>) paramOfPreditionCriteria
				.get("Prediction price list");

		double average = 0;
		double t;

		for (AssetEntity assetEntity : assetList.keySet()) {

			ArrayList<PriceEntry> priceEntryList = assetList.get(assetEntity);
			TreeMap<Date, Double> priceList = new TreeMap<Date, Double>();

			// convert PriceEntry to TreeMap<Date, Double> for ease of
			// comparison
			for (PriceEntry curPriceEntry : priceEntryList) {
				priceList
						.put(curPriceEntry.getDate(), curPriceEntry.getPrice());
			}

			// get asset from database
			java.sql.Date beginDate = new java.sql.Date(priceList.firstEntry()
					.getKey().getTime());
			java.sql.Date endDate = new java.sql.Date(priceList.lastEntry()
					.getKey().getTime());
			ArrayList<PriceEntity> realPriceList = priceManager
					.getPriceInInterval(assetEntity.getAssetID(), beginDate,
							endDate);
			
			double numberOfEntries = realPriceList.size();
			for (PriceEntity curEntity : realPriceList) {
				t = java.lang.Math.abs(priceList.get(curEntity.getDate()) - curEntity.getClose()) / curEntity.getClose()  * 100;
				average += t / numberOfEntries ;
			}
		}

		TreeMap<String, Double> map = (new TreeMap<String, Double>());
		map.put("AP", average / assetList.size());
		return map;

	}

	@Override
	public String toString() {
		return "Average Percentage Error";
	}

	@Override
	public void setParametersValue(TreeMap<String, Object> map) {
		paramOfPreditionCriteria.put("Prediction price list", map
				.get("Prediction price list"));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public TreeMap<String, Class> getParametersList() {
		// TODO Auto-generated method stub
		return null;
	}

}