package dataAccess.databaseManagement.entity;

import java.io.Serializable;

public class AssetEntity implements Serializable, Comparable<AssetEntity> {

	/**
     *
     */
	private static final long serialVersionUID = -1106350892914906725L;

	private long assetID;
	private String name;
	private String symbol;
	private long exchangeID;
	private String assetInfo;
	private double fluctuationRange;

	public AssetEntity() {
	}

	public AssetEntity(String name, String symbol, long exchangeID,
			String assetInfo, double fluctuationRange) {
		this.name = name;
		this.symbol = symbol;
		this.exchangeID = exchangeID;
		this.assetInfo = assetInfo;
		this.fluctuationRange = fluctuationRange;
	}

	public void setAssetID(long assetID) {
		this.assetID = assetID;
	}

	public long getAssetID() {
		return assetID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setExchangeID(long exchangeID) {
		this.exchangeID = exchangeID;
	}

	public long getExchangeID() {
		return exchangeID;
	}

	public void setAssetInfo(String assetInfo) {
		this.assetInfo = assetInfo;
	}

	public String getAssetInfo() {
		return assetInfo;
	}

	public void setFluctuationRange(double fluctuationRange) {
		this.fluctuationRange = fluctuationRange;
	}

	public double getFluctuationRange() {
		return fluctuationRange;
	}

	@Override
	public String toString() {
		return symbol;
	}

	@Override
	public int compareTo(AssetEntity o) {
		return new Long(assetID).compareTo(o.assetID);
	}
}
