package dataAccess.databaseManagement.entity;

import java.io.Serializable;

public class ExchangeEntity implements Serializable, Comparable<ExchangeEntity> {

	/**
     *
     */
	private static final long serialVersionUID = -5854103809456038716L;
	private long exchangeID;
	private String name;
	private double fluctuationRange;

	public ExchangeEntity() {
	}

	public ExchangeEntity(String name, double fluctuationRange) {
		this.name = name;
		this.fluctuationRange = fluctuationRange;
	}

	public void setExchangeID(long exchangeID) {
		this.exchangeID = exchangeID;
	}

	public long getExchangeID() {
		return exchangeID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setFluctuationRange(double fluctuationRange) {
		this.fluctuationRange = fluctuationRange;
	}

	public double getFluctuationRange() {
		return fluctuationRange;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(ExchangeEntity o) {
		return new Long(this.exchangeID).compareTo(o.exchangeID);
	}
}
