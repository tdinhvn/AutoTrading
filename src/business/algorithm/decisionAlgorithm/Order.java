package business.algorithm.decisionAlgorithm;

import java.util.Date;

import dataAccess.databaseManagement.entity.AssetEntity;

public class Order {

	public static final boolean ORDER_BUY = true;
	public static final boolean ORDER_SELL = false;

	private AssetEntity asset;
	private boolean orderType;
	private double price;
	private Date date;

	public Order(AssetEntity asset, boolean orderType, double price, Date date) {
		super();
		this.setAsset(asset);
		this.orderType = orderType;
		this.price = price;
		this.date = date;
	}

	public boolean isOrderType() {
		return orderType;
	}

	public void setOrderType(boolean orderType) {
		this.orderType = orderType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setAsset(AssetEntity asset) {
		this.asset = asset;
	}

	public AssetEntity getAsset() {
		return asset;
	}
	 
	public business.virtualTrading.Order toOrder() {
		return new business.virtualTrading.Order(asset, orderType, price,
				business.virtualTrading.Order.USE_ALL_CASH);
	}
}
