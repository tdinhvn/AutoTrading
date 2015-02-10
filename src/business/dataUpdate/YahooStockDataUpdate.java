package business.dataUpdate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.ExchangeEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.AssetManager;
import dataAccess.databaseManagement.manager.ExchangeManager;
import dataAccess.databaseManagement.manager.PriceManager;

public class YahooStockDataUpdate extends AbstractDataUpdate {

	private Date oldestDate = null;

	public YahooStockDataUpdate() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		try {
			this.oldestDate = dateFormat.parse("1-1-2011");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.exchangeNameList = new ArrayList<String>();
		this.exchangeNameList.add("NASDAQ");

		//this.fileNameList = null;
		this.description = null;
	}

	public boolean updateHistoricalData() {
		AssetManager assetManager = new AssetManager();
		ExchangeManager exchangeManager = new ExchangeManager();

		ExchangeEntity exchangeEntity = exchangeManager
				.getExchangeByName(this.exchangeNameList.get(0));

		ArrayList<AssetEntity> assetEntityList = assetManager
				.getAssetsByExchange(exchangeEntity.getExchangeID());
		for (AssetEntity assetEntity : assetEntityList)
			updateDataFromDateToDate(assetEntity, oldestDate, new Date());
		return true;
	}

	public boolean updateData() {
		// TODO Auto-generated method stub
		AssetManager assetManager = new AssetManager();
		PriceManager priceManager = new PriceManager();
		ExchangeManager exchangeManager = new ExchangeManager();

		ExchangeEntity exchangeEntity = exchangeManager
				.getExchangeByName(this.exchangeNameList.get(0));

		ArrayList<AssetEntity> assetEntityList = assetManager
				.getAssetsByExchange(exchangeEntity.getExchangeID());
		Date latestDate;
		for (AssetEntity assetEntity : assetEntityList) {
			latestDate = priceManager.getLatestDateOfAsset((int) assetEntity
					.getAssetID());
			if (latestDate == null)
				latestDate = oldestDate;
			updateDataFromDateToDate(assetEntity,
					utility.Utility.increaseDate(latestDate), new Date());
		}
		return true;
	}

	public static boolean initExchangeMarketsAndAssets(String fileName) {
		// TODO Auto-generated method stub
		String symbol, companyname;

		// create BufferedReader to read csv file
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileName));

			// TODO Auto-generated catch block
			String strLine = "";
			StringTokenizer st = null;
			// read comma separated file line by line
			AssetManager assetManager = new AssetManager();
			ExchangeManager exchangeManager = new ExchangeManager();
			exchangeManager.add(new ExchangeEntity("NASDAQ", -1));
			ExchangeEntity nasdaq = null;
			nasdaq = exchangeManager.getExchangeByName("NASDAQ");
			strLine = br.readLine();
			while ((strLine = br.readLine()) != null) {
				// break comma separated line using ","
				st = new StringTokenizer(strLine, ",");
				symbol = st.nextToken();
				symbol = symbol.split(" ")[0];
				symbol = symbol.split("\"")[1];
				companyname = st.nextToken();
				System.out.println(symbol + " " + companyname);
				assetManager.add(new AssetEntity(companyname, symbol, nasdaq
						.getExchangeID(), "", -1));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean updateDataFromDateToDate(AssetEntity assetEntity,
			Date fromDate, Date toDate) {
		PriceManager priceManager = new PriceManager();

		HttpURLConnection uc = initConnection(assetEntity, fromDate, toDate);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					uc.getInputStream()));

			double open, high, low, close;
			double volume;
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date date;
			String strLine, strDate;
			String[] splitString;
			String[] str;
			br.readLine();

			while ((strLine = br.readLine()) != null) {
				splitString = strLine.split(",");
				strDate = splitString[0];
				str = strDate.split("-");
				strDate = str[2] + "-" + str[1] + "-" + str[0];
				date = df.parse(strDate);
				if ((date.compareTo(fromDate) >= 0) && (date.compareTo(toDate) <= 0))
				{
					open = Double.valueOf(splitString[1]);
					high = Double.valueOf(splitString[2]);
					low = Double.valueOf(splitString[3]);
					close = Double.valueOf(splitString[4]);
					volume = Double.valueOf(splitString[5]);

					PriceEntity priceEntity = new PriceEntity(
							assetEntity.getAssetID(), new java.sql.Date(
									date.getTime()), null, volume, close, open,
							high, low);
					priceManager.add(priceEntity);
				}
			}
		} 
		catch (FileNotFoundException e) {
			// TODO: handle exception
			return false;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean updateDataFromDateToDate(String exchangeName,
			Date fromDate, Date toDate) {
		AssetManager assetManager = new AssetManager();

		ArrayList<AssetEntity> assetEntities = assetManager
				.getAssetsByExchange(new ExchangeManager().getExchangeByName(exchangeName).getExchangeID());

		for (AssetEntity assetEntity : assetEntities) {
			updateDataFromDateToDate(assetEntity, fromDate, toDate);
		}

		return true;
	}

	public HttpURLConnection initConnection(AssetEntity assetEntity,
			Date fromDate, Date toDate) {
		try {
			String link = "http://ichart.finance.yahoo.com/table.csv?s=";
			Calendar cal = Calendar.getInstance();

			link = link.concat(assetEntity.getSymbol() + "&d=");

			cal.setTime(toDate);
			
			link = link.concat(String.valueOf(cal.get(Calendar.MONTH))
					+ "&e=");
			link = link.concat(String.valueOf(cal.get(Calendar.DATE)) + "&f=");
			link = link.concat(String.valueOf(cal.get(Calendar.YEAR))
					+ "&g=d&a=");

			cal.setTime(fromDate);

			link = link.concat(String.valueOf(cal.get(Calendar.MONTH))
					+ "&b=");
			link = link.concat(String.valueOf(cal.get(Calendar.DATE)) + "&c=");
			link = link.concat(String.valueOf(cal.get(Calendar.YEAR))
					+ "&ignore=.csv");

			URL url = new URL(link);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setRequestProperty("User-Agent", "");
			return uc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String args[])
	{
		/*
		YahooStockDataUpdate yahoo = new YahooStockDataUpdate();
		
		ExchangeManager exchangeManager = new ExchangeManager();
		ExchangeEntity exchangeEntity = exchangeManager.getExchangeByName("NASDAQ");
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		try
		{
			Date fromDate = df.parse("01-01-1990");
			Date toDate = df.parse("31-12-1990");
			yahoo.updateDataFromDateToDate(exchangeEntity, fromDate, toDate);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}*/
	}

    @Override
    public boolean updateData(AssetEntity assetEntity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
