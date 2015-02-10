package dataAccess.databaseManagement.entity;

import java.io.Serializable;
import java.sql.Date;

public class PriceEntity implements Serializable, Comparable<PriceEntity> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5374209810328051221L;

	private long priceID;
	private long assetID;
	private Date date;
	private Date deliveryDate;
	private double volume;
	private double close;
	private double open;
	private double high;
	private double low;

	public PriceEntity() {
	}

	public PriceEntity(long assetID, Date date, Date deliveryDate,
			double volume, double close, double open, double high, double low) {
		this.assetID = assetID;
		this.date = date;
		this.deliveryDate = deliveryDate;
		this.volume = volume;
		this.close = close;
		this.open = open;
		this.high = high;
		this.low = low;
	}

	public void setPriceID(long priceID) {
		this.priceID = priceID;
	}

	public long getPriceID() {
		return priceID;
	}

	public void setAssetID(long assetID) {
		this.assetID = assetID;
	}

	public long getAssetID() {
		return assetID;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getVolume() {
		return volume;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getClose() {
		return close;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getOpen() {
		return open;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getHigh() {
		return high;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getLow() {
		return low;
	}

	@Override
	public int compareTo(PriceEntity o) {
		// TODO Auto-generated method stub
		return this.date.compareTo(o.date);
	}
}
