package business.predictionAlgorithmEvaluation;

import java.util.Date;
import java.util.ArrayList;
import java.util.TreeMap;

import business.algorithm.predictAlgorithm.OutputForPredictionAlgorithm;
import business.algorithm.predictAlgorithm.PriceEntry;
import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.PriceManager;

/*
 *  ArrayList <AbstractPredictAlgorithm> algorithmList;
 *  algorithmList.add(...);
 *  ArrayList<PredictionCriteria> criteriaList;
 *  criteriaList.add();
 *  
 *  for (AbstractPredictAlgorithm curAlgorithm :  algorithmList) 
 *  	for (PredictionCriteria curCriteria : criteriaList) {
 *  		curCriteria.setParamater(curAlgorithm.runAlgorithm().toParamOfPredictionCriteria(assetEntity, startDate));
 * 			table[curAlgorithm][curCriteria] = curCriteria.evaluate();
 * 		}	     
 * 
 */
public abstract class PredictionCriteria {

	protected TreeMap<String, Object> paramOfPreditionCriteria = new TreeMap<String, Object>();

	public static TreeMap<String, Object> toParamOfPredictionCriteria(
			AssetEntity assetEntity, Date startPredictingDate,
			OutputForPredictionAlgorithm output) {
		PriceManager priceManager = new PriceManager();
		ArrayList<PriceEntity> priceEntityList = priceManager
				.getPriceInInterval(assetEntity.getAssetID(),
						(java.sql.Date) startPredictingDate,
						priceManager.getLatestDate());
		TreeMap<Date, Double> priceList = new TreeMap<Date, Double>();
		int i = 0;
		for (PriceEntry priceEntry : output.getPredictionPriceList()
				.firstEntry().getValue()) {
			priceList.put(new Date(priceEntityList.get(i).getDate().getTime()),
					priceEntry.getPrice());
			i++;
		}
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("PriceList", priceList);
		map.put("Asset", assetEntity);
		return map;
	}

	public abstract TreeMap<String, Double> evaluate();

	public abstract void setParametersValue(TreeMap<String, Object> map);

	@SuppressWarnings("rawtypes")
	public abstract TreeMap<String, Class> getParametersList();

	@Override
	public abstract String toString();
}
