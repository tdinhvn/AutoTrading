package business.algorithm.decisionAlgorithm;

import java.util.ArrayList;
import java.util.TreeMap;

public class OutputForDecisionAlgorithm {
	private ArrayList<Order> orderList;

	public OutputForDecisionAlgorithm(ArrayList<Order> orderList) {
		this.setOrderList(orderList);
	}

	public ArrayList<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(ArrayList<Order> orderList) {
		this.orderList = orderList;
	}
	
	public TreeMap<String, Object> getOutputList() {
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("OrderList", orderList);
		return map;
	}

}
