package business.algorithm.decisionAlgorithm;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;

public class MovingAverage extends AbstractDecisionAlgorithm {

	private Integer MA_period;

	public MovingAverage(
			TreeMap<AssetEntity, ArrayList<PriceEntity>> priceList,
			Integer MA_period) {
		// TODO Auto-generated constructor stub
		super(priceList);
		this.MA_period = MA_period;
	}

	public MovingAverage() {
		super(null);
		this.MA_period = null;
	}

	public Integer getMA_period() {
		return MA_period;
	}

	public void setMA_period(Integer mA_period) {
		MA_period = mA_period;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public TreeMap<String, Class> getParameterList() {
		// TODO Auto-generated method stub
		TreeMap<String, Class> map = super.getParameterList();
		map.put("MA period", Integer.class);
		return map;
	}

	@Override
	public void setParameterValue(TreeMap<String, Object> map) {
		// TODO Auto-generated method stub
		super.setParameterValue(map);
		this.MA_period = (Integer) map.get("MA period");
	}

	@Override
	public OutputForDecisionAlgorithm runAlgorithm() {
		// TODO Auto-generated method stub
		ArrayList<Order> orderList = new ArrayList<Order>();
		Set<AssetEntity> assetSet = priceList.keySet();
		double alpha = 2.0/(MA_period + 1);		

		for (AssetEntity asset : assetSet) {
			ArrayList<PriceEntity> priceEntityList = priceList.get(asset);
			ArrayList<Double> EMA = new ArrayList<Double>();

			EMA.add(priceEntityList.get(0).getClose());
			for (int i = 1; i < priceEntityList.size(); ++i) {
				EMA.add(alpha*priceEntityList.get(i).getClose() + (1-alpha)*EMA.get(EMA.size()-1));
			}

			ArrayList<PriceEntity> tempArray = new ArrayList<PriceEntity>();
			for (PriceEntity price : priceEntityList) {
				tempArray.add(price);
			}

			priceEntityList = tempArray;
			
			double todayPrice, todayMA, yesterdayMA;

			boolean previous_buy_order = false;
			boolean previous_sell_order = false;
			for (int i = 1; i < priceEntityList.size(); ++i) {
				todayPrice = priceEntityList.get(i).getClose();
				yesterdayMA = EMA.get(i - 1);
				todayMA = EMA.get(i);

				if (todayMA > yesterdayMA) {
					if (todayPrice > todayMA) {
						if (previous_buy_order == false) {
							Order order = new Order(asset, Order.ORDER_BUY,
									todayPrice, priceEntityList.get(i)
											.getDate());
							orderList.add(order);
							previous_buy_order = true;
							previous_sell_order = false;
						}
					}
				} else {
					if (todayPrice < todayMA) {
						if (previous_sell_order == false) {
							Order order = new Order(asset, Order.ORDER_SELL,
									todayPrice, priceEntityList.get(i)
											.getDate());
							orderList.add(order);
							previous_buy_order = false;
							previous_sell_order = true;
						}
					}
				}
			}

		}
		return new OutputForDecisionAlgorithm(orderList);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Moving Average - " + MA_period;
	}

}
