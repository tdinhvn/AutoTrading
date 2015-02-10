package business.virtualTrading;

import java.util.Date;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PortfolioEntity;
import dataAccess.databaseManagement.manager.AssetManager;
import dataAccess.databaseManagement.manager.PortfolioManager;
import dataAccess.databaseManagement.manager.PriceManager;

/**
 * Class Name: PortfolioEntry
 * 
 * @version 1.5
 * @date June 1, 2011
 * @author Xuan Ngoc
 */
public class PortfolioEntry {

	private Asset asset;
	private double buyPrice;
	private double currentPrice;
	private double volume;
	private double profit;

	public PortfolioEntry() {

	}

	public PortfolioEntry(Asset asset, double buyPrice, double volume) {
//		PriceManager priceManager = new PriceManager();
		this.setAsset(new Asset(asset));
		this.setBuyPrice(buyPrice);
		this.setVolume(volume);
//		this.setCurrentPrice(priceManager.getPriceByAssetIDAndDate(
//				asset.getAssetID(), priceManager.getLatestDate()).getClose()); // TODO
//																				// getLatestPriceByAssetID(assetID)
//		profit = (this.currentPrice - this.buyPrice) / this.buyPrice * 100;
	}

	public PortfolioEntry(AssetEntity asset, double buyPrice, double volume) {
//		PriceManager priceManager = new PriceManager();
		this.setAsset(new Asset(asset));
		this.setBuyPrice(buyPrice);
		this.setVolume(volume);
//		this.setCurrentPrice(priceManager.getPriceByAssetIDAndDate(
//				asset.getAssetID(), priceManager.getLatestDate()).getClose());
//		profit = (this.currentPrice - this.buyPrice) / this.buyPrice;
	}

	public PortfolioEntry(PortfolioEntity portfolioEntity) {
//		PriceManager priceManager = new PriceManager();
		AssetManager assetManager = new AssetManager();
		this.asset = new Asset(assetManager.getAssetByID(portfolioEntity
				.getAssetID()));
		this.buyPrice = portfolioEntity.getPrice();
		this.volume = portfolioEntity.getVolume();
//		this.setCurrentPrice(priceManager.getPriceByAssetIDAndDate(
//				asset.getAssetID(), priceManager.getLatestDateOfAsset(asset.getAssetID())).getClose());
//		profit = (this.currentPrice - this.buyPrice) / this.buyPrice;
	}

	/**
	 * 
	 * @param volume
	 *            > 0 if buyOrderType, volume < 0 if sellOrderType
	 * @param price
	 * @return > 0 if sell all
	 */
	public double updatePortfolio(double volume, double price) {
		double validVolume = this.volume + volume;
		if (volume > 0) { // buy OrderType
			this.buyPrice = (price * volume + this.buyPrice * this.volume)
				/ validVolume;
			this.volume += volume;
			return -1;
		}
		if (validVolume > 0) { // sell Order Type
			this.volume = validVolume;
			return -1;
		}
		return this.volume; // sell all
	}

	public double getPortfolioValue() {
		return buyPrice * volume;
	}

	/**
	 * Delete portfolio by ID from database <li>Note: this method update
	 * database
	 */
	public void delete(long portfolioID) {
		PortfolioManager portfolioManager = new PortfolioManager();
		portfolioManager.delete(portfolioID);
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setVolume(double volume) {
		this.volume = (long) volume;
	}

	public double getVolume() {
		return volume;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public double getProfit() {
		return profit;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public void updateCurrentPriceToDate(Date date) {
            if (date == null) {
                return;
            }

		PriceManager priceManager = new PriceManager();
		this.setCurrentPrice(priceManager.getPriceByAssetIDAndDate(
				asset.getAssetID(), new java.sql.Date(date.getTime()))
				.getClose());
		//System.out.println(date + " --- " + currentPrice);
		profit = (currentPrice - buyPrice)/buyPrice *100;
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

}
