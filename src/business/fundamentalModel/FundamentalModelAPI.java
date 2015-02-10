package business.fundamentalModel;

public class FundamentalModelAPI {
public static final String PFG = "PFG";
public static final String PhT = "PhilTown";
public static final String Custom = "Custom";
	
	public static final String[] FUNDAMENTAL_MODEL_LIST = { PFG, PhT, Custom };

	public static AbstractFundamentalModel getFundamentalModel(String str) {
		if (str.equals(PFG)) {
			return (new PFG());
		} else if (str.equals(PhT)) {
			return (new PhilTown());
		} else if (str.equals(Custom)) {
			return (new Custom());
		}
		return null;
	}

}
