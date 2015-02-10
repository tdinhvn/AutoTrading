package business.dataUpdate;

public class DataUpdateAPI {
	private static final String COPHIEU68 = "Cophieu68";
	private static final String YAHOO_STOCK = "YahooStockDataUpdate";
	
	public static final String[] ONLINE_RESOURCES = { COPHIEU68 };

	public static AbstractDataUpdate getDataUpdate(String str) {
		if (str.equals(COPHIEU68)) {
			return new Cophieu68DataUpdate();
		} else if (str.equals(YAHOO_STOCK)) {
			return new YahooStockDataUpdate();
		}
		return null;
	}

//	public static void main(String args[]) {
//
//		Cophieu68DataUpdate.initExchangeMarketsAndAssets("company.csv");
//		Cophieu68DataUpdate cophieu68 = new Cophieu68DataUpdate();
//		cophieu68.updateHistoricalData();
//
//		YahooStockDataUpdate
//				.initExchangeMarketsAndAssets("NASDAQ_companylist.csv");
//		YahooStockDataUpdate yahoo = new YahooStockDataUpdate();
//		yahoo.updateHistoricalData();
//
//		/*
//		 * try { DateFormat df = new SimpleDateFormat("dd-MM-yyyy"); Date
//		 * fromDate = df.parse("1-1-2010"); Date toDate = df.parse("6-6-2010");
//		 * AssetManager assetManager = new AssetManager(); Cophieu68DataUpdate
//		 * cophieu68 = new Cophieu68DataUpdate();
//		 * cophieu68.updateDateFromDateToDate
//		 * (assetManager.getAssetBySymbolAndExchange("AAM", "HOSE"), fromDate,
//		 * toDate); } catch (Exception e) { e.printStackTrace(); }
//		 */
//	}
}
