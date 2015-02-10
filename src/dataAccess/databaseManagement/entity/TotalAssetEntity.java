package dataAccess.databaseManagement.entity;

import java.io.Serializable;
import java.sql.Date;

public class TotalAssetEntity implements Serializable, Comparable<TotalAssetEntity> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5374209810328051221L;

	private long totalAssetID;
	private long userID;
	private Date date;
	private double totalAsset;

	public TotalAssetEntity() {
	}

	public TotalAssetEntity(long userID, Date date, double totalAsset) {
		this.userID = userID;
		this.date = date;
		this.totalAsset = totalAsset;
	}

	public long getTotalAssetID() {
		return totalAssetID;
	}

	public void setTotalAssetID(long totalAssetID) {
		this.totalAssetID = totalAssetID;
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public double getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(double totalAsset) {
		this.totalAsset = totalAsset;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public int compareTo(TotalAssetEntity o) {
		// TODO Auto-generated method stub
		return this.date.compareTo(o.date);
	}
}
