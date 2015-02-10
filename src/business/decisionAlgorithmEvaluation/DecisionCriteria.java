package business.decisionAlgorithmEvaluation;

import java.util.ArrayList;
import java.util.TreeMap;

import business.algorithm.decisionAlgorithm.Order;
import business.virtualTrading.User;

/*
 * 	// Add Algorithm
 *  ArrayList <AbstractDecisionAlgorithm> algorithmList;
 *  algorithmList.add(...);
 *  
 *  // Add Criteria
 *  ArrayList<DecisionCriteria> criteriaList;
 *  criteriaList.add(...);
 *  
 *  // Import Initial State
 *  User algoUser = new User ("Algorithm", cash);
 *  PortfoliEntry curPortfolio = new PortfolioEntry(assetEntity, buyPrice, Volume);
 *  algoUser.addPortfolio(curPortfolio);
 *  
 *  // Refresh
 *  for (AbstractDecisionAlgorithm curAlgorithm :  algorithmList) {
 *  	User curUser = algoUser.clone(); // different user for each algorithm
 *  	for (DecisionCriteria curCriteria : criteriaList) {
 *  		curCriteria.setParametersValue(curUser, curAlgorithm.runAlgorithm(), assetEntity);
 *  
 * 			table[curAlgorithm][curCriteria] = curCriteria.evaluate();
 * 		}
 * 	}
 * 
 * 	// View oders
 * 	selectedUser.
 * 
 * 	// View Portfolio
 *  selectedUser.getCurPortfolioList();
 *      
 */

/**
 * Use the following method to convert an order of OutputOfDecisionAlgorithm to
 * parameter of DecisionCiriteria <li>// if the order does not associate with a
 * specific asset then it will use this AssetEntity <li>TreeMap<String, Object>
 * toParamOfDecisionCriteria(AssetEntity)
 * 
 */
public abstract class DecisionCriteria {
	protected User user;
	protected TreeMap<String, Object> paramList;

	public abstract TreeMap<String, Double> evaluate();
	
	public abstract void setParametersValue(TreeMap<String, Object> map);

	@SuppressWarnings("rawtypes")
	public abstract TreeMap<String, Class> getParametersList();

	public abstract void setParametersValue(User user,
			ArrayList<Order> orderList);

	@Override
	public abstract String toString();
}
