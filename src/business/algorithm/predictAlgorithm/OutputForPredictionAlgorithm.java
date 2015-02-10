package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;

public abstract class OutputForPredictionAlgorithm {
	protected TreeMap<AssetEntity, ArrayList<PriceEntry>> predictionPriceList;

	public TreeMap<AssetEntity, ArrayList<PriceEntry>> getPredictionPriceList() {
		return predictionPriceList;
	}

	public void setPredictionPriceList(
			TreeMap<AssetEntity, ArrayList<PriceEntry>> predictionPriceList) {
		this.predictionPriceList = predictionPriceList;
	}

	public OutputForPredictionAlgorithm(
			TreeMap<AssetEntity, ArrayList<PriceEntry>> predictionPriceList) {
		super();
		this.predictionPriceList = predictionPriceList;
	}

	public TreeMap<String, Object> getOutputValue() {
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("Prediction price list", predictionPriceList);
		return map;
	}

}
