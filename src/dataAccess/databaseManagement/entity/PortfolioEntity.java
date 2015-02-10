package dataAccess.databaseManagement.entity;

import java.io.Serializable;
import java.sql.Date;

public class PortfolioEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7320439639218180861L;

	private long portfolioID;
	private long userID;
	private long assetID;
	private double price;
	private double volume;
	private Date date;

	public PortfolioEntity() {
	}

	public PortfolioEntity(long userID, long assetID, double price,
			double volume, Date date) {
		this.userID = userID;
		this.assetID = assetID;
		this.price = price;
		this.volume = volume;
		this.date = date;
	}

	public void setPortfolioID(long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public long getPortfolioID() {
		return portfolioID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public long getUserID() {
		return userID;
	}

	public void setAssetID(long assetID) {
		this.assetID = assetID;
	}

	public long getAssetID() {
		return assetID;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return price;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getVolume() {
		return volume;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

}
