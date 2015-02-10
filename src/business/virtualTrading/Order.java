package business.virtualTrading;

import dataAccess.databaseManagement.entity.AssetEntity;

/**
 * Class Name: Order
 * 
 * @version 1.5
 * @date June 1, 2011
 * @author Xuan Ngoc
 */

/*
 * TODO : Assume that names of all symbols are unique
 */

public class Order {

	private Asset asset;
	private boolean orderType;
	private double price;
	private double volume;
	private double value;
	private boolean matched;
        
        public static final double tradingFeeRate = 0.0015;
        public static final double taxRate = 0.001;
	public static final int USE_ALL_CASH = -1;

	public Order() {

	}

	public Order(Asset asset, boolean orderType, double price, double volume) {
		this.setAsset(asset);
		this.setOrderType(orderType);
		this.setPrice(price);
		this.setVolume(volume);
		this.setValue(price * volume);
		this.setMatched(false);
	}

	public Order(AssetEntity asset, boolean orderType, double price,
			double volume) {
		this.asset = new Asset(asset);
		this.setOrderType(orderType);
		this.setPrice(price);
		this.setVolume(volume);
		this.setValue(price * volume);
		this.setMatched(false);
	}

	public void setOrderType(boolean orderType) {
		this.orderType = orderType;
	}

	public boolean isOrderType() {
		return orderType;
	}

	public boolean getOrderType() {
		return orderType;
	}

	public void setPrice(double price) {
		this.price = price;
		this.value = price * volume;
	}

	public double getPrice() {
		return price;
	}

	public void setVolume(double volume) {
		this.volume =(long) volume;
		this.value = this.volume * price;
	}

	public double getVolume() {
		return volume;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getValue() {
            if (this.orderType) {
                return value + tradingFeeRate*value;
            } else {
                return value - tradingFeeRate*value-taxRate*value;
            }
	}

	public void setAsset(Asset asset) {
		this.asset = new Asset(asset);
	}

	public Asset getAsset() {
		return asset;
	}

	public void setMatched(boolean matched) {
		this.matched = matched;
	}

	public boolean getMatched() {
		return matched;
	}

}
