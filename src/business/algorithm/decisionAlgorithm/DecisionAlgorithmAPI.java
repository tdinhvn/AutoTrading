package business.algorithm.decisionAlgorithm;

public class DecisionAlgorithmAPI {
	private static final String MOVING_AVERAGE = "Moving Average";
	private static final String PERFECT_ALGORITHM = "Perfect Algorithm";

	public static final String[] DECISION_ALGORITHM_LIST = { MOVING_AVERAGE,
			PERFECT_ALGORITHM };

	public static AbstractDecisionAlgorithm getDecisionAlgorithm(
			String algorithmName) {
		if (algorithmName.equals(MOVING_AVERAGE))
			return new MovingAverage();
		else if (algorithmName.equals(PERFECT_ALGORITHM))
			return new PerfectAlgorithm();
		return null;
	}
}
