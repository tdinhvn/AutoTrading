package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;

public class OutputForAutoRegression extends OutputForPredictionAlgorithm {
	private double lambda;

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public OutputForAutoRegression(
			TreeMap<AssetEntity, ArrayList<PriceEntry>> predictionPriceList,
			double lambda) {
		super(predictionPriceList);
		this.lambda = lambda;
	}

	@Override
	public TreeMap<String, Object> getOutputValue() {
		// TODO Auto-generated method stub
		TreeMap<String, Object> map = super.getOutputValue();
		map.put("Lambda", lambda);
		return map;
	}

	/*
	 * public PredictionAlgorithmEntity convertThis(Date startPredictingDate) {
	 * PredictionAlgorithmEntity entity = new PredictionAlgorithmEntity();
	 * entity.list = new ArrayList<PredictionAlgorithmEntity.Entry>(); Date
	 * currentDate = startPredictingDate; for (double d : predictionPrice) {
	 * currentDate = utility.Utility.increaseDate(currentDate); Entry entry =
	 * null; { entry = new Entry(); entry.date = currentDate; entry.midValue =
	 * d; entry.lowValue = d - Math.abs(lambda); entry.highValue = d +
	 * Math.abs(lambda); } entry.date = currentDate; entity.list.add(entry); }
	 * return entity; }
	 */

	/*
	 * @Override public TreeMap<String, Object> ToParamOfPredictionCriteria(
	 * AssetEntity assetEntity, Date startPredictingDate) { // TODO
	 * Auto-generated method stub return
	 * super.ToParamOfPredictionCriteria(assetEntity, startPredictingDate); }
	 */

}
