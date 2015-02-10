package business.decisionAlgorithmEvaluation;

public class DecisionAlgorithmEvaluationAPI {
	private static final String VNINDEX = "VNIndex Comparision";
	
	public static final String[] DECISION_CRITERIA_LIST = { VNINDEX };

	public static DecisionCriteria getPredictionAlgorithm(String str) {
		if (str.equals(VNINDEX)) {
			return (new VsVNIndex());
		}
		return null;
	}

}
