package business.algorithm.predictAlgorithm;

import java.util.Date;

public class PriceEntry {
	private Date date;
	private Double price;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public PriceEntry(Date date, Double price) {
		super();
		this.date = date;
		this.price = price;
	}

}
