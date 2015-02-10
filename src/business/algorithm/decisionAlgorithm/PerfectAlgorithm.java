package business.algorithm.decisionAlgorithm;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;

public class PerfectAlgorithm extends AbstractDecisionAlgorithm {

	public PerfectAlgorithm(
			TreeMap<AssetEntity, ArrayList<PriceEntity>> priceList) {
		// TODO Auto-generated constructor stub
		super(priceList);
	}

	public PerfectAlgorithm() {
		super(null);
	}

	@Override
	public OutputForDecisionAlgorithm runAlgorithm() {
		// TODO Auto-generated method stub
		ArrayList<Order> orderList = new ArrayList<Order>();

		Set<AssetEntity> assetSet = priceList.keySet();

		for (AssetEntity asset : assetSet) {
			ArrayList<PriceEntity> priceEntityList = priceList.get(asset);

			ArrayList<Integer> peakIndexList = new ArrayList<Integer>();
			ArrayList<Integer> bottomIndexList = new ArrayList<Integer>();

			double yesterdayPrice, todayPrice, tomorrowPrice;

			for (int i = 1; i < priceEntityList.size() - 1; ++i) {
				yesterdayPrice = priceEntityList.get(i - 1).getClose();
				todayPrice = priceEntityList.get(i).getClose();
				tomorrowPrice = priceEntityList.get(i + 1).getClose();

				if ((yesterdayPrice < todayPrice)
						&& (tomorrowPrice < todayPrice))
					peakIndexList.add(i);
				else if ((yesterdayPrice > todayPrice)
						&& (tomorrowPrice > todayPrice))
					bottomIndexList.add(i);
			}

			for (int index : peakIndexList) {

				orderList.add(new Order(asset, Order.ORDER_SELL, priceEntityList.get(
						index).getClose(), priceEntityList.get(index)
						.getDate()));
			}

			for (int index : bottomIndexList) {

				orderList.add(new Order(asset, Order.ORDER_BUY, priceEntityList.get(
						index).getClose(), priceEntityList.get(index)
						.getDate()));
			}

		}
		return new OutputForDecisionAlgorithm(orderList);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Perfect Algorithm";
	}

}
