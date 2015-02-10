package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;

public class CommonOutputForPredictionAlgorithm extends OutputForPredictionAlgorithm{

	public CommonOutputForPredictionAlgorithm(
			TreeMap<AssetEntity, ArrayList<PriceEntry>> predictionPriceList) {
		super(predictionPriceList);
	}

}
