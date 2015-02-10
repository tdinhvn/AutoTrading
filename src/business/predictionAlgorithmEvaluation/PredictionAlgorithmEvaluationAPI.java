package business.predictionAlgorithmEvaluation;

public class PredictionAlgorithmEvaluationAPI {
	private static final String RESIDUAL_SUM_SQUARES = "Residual Sum of Squares";
	private static final String AVARAGE_PERCENTAGE_ERROR = "Average Percentage Error";

	public static final String[] PREDICTION_CRITERIA_LIST = { RESIDUAL_SUM_SQUARES, AVARAGE_PERCENTAGE_ERROR };

	public static PredictionCriteria getPredictionAlgorithm(String str) {
		if (str.equals(RESIDUAL_SUM_SQUARES)) {
			return (new SumSquare());
		}
		
		if (str.equals(AVARAGE_PERCENTAGE_ERROR)) {
			return (new AveragePercentage());
		}
		return null;
	}
}
