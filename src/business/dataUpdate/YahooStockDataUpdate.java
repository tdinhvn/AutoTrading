package business.dataUpdate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.ExchangeEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.AssetManager;
import dataAccess.databaseManagement.manager.ExchangeManager;
import dataAccess.databaseManagement.manager.PriceManager;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class YahooStockDataUpdate extends AbstractDataUpdate {

    private LocalDate oldestDate = null;

    public YahooStockDataUpdate() {
        this.oldestDate = LocalDate.parse("2011-01-01");

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
        for (AssetEntity assetEntity : assetEntityList) {
            updateDataFromDateToDate(assetEntity, oldestDate, LocalDate.now());
        }
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
        LocalDate latestDate;
        for (AssetEntity assetEntity : assetEntityList) {
            latestDate = priceManager.getLatestDateOfAsset((int) assetEntity
                    .getAssetID()).toLocalDate();
            if (latestDate == null) {
                latestDate = oldestDate;
            }
            updateDataFromDateToDate(assetEntity,
                    latestDate, LocalDate.now());
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
            LocalDate fromDate, LocalDate toDate) {
        PriceManager priceManager = new PriceManager();

        HttpURLConnection uc = initConnection(assetEntity, fromDate, toDate);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    uc.getInputStream()));

            double open, high, low, close;
            double volume;
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date;
            String strLine, strDate;
            String[] splitString;
            String[] str;
            br.readLine();

            while ((strLine = br.readLine()) != null) {
                splitString = strLine.split(",");
                strDate = splitString[0];
                str = strDate.split("-");
                strDate = str[2] + "-" + str[1] + "-" + str[0];
                date = LocalDate.parse(strDate, df);
                if ((date.compareTo(fromDate) >= 0) && (date.compareTo(toDate) <= 0)) {
                    open = Double.valueOf(splitString[1]);
                    high = Double.valueOf(splitString[2]);
                    low = Double.valueOf(splitString[3]);
                    close = Double.valueOf(splitString[4]);
                    volume = Double.valueOf(splitString[5]);

                    PriceEntity priceEntity = new PriceEntity(
                            assetEntity.getAssetID(), Date.valueOf(date), 
                            null, volume, close, open,
                            high, low);
                    priceManager.add(priceEntity);
                }
            }
        } catch (FileNotFoundException e) {
            // TODO: handle exception
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateDataFromDateToDate(LocalDate fromDate, LocalDate toDate) {
        AssetManager assetManager = new AssetManager();

        ArrayList<AssetEntity> assetEntities = assetManager.getAllAssets();

        for (AssetEntity assetEntity : assetEntities) {
            updateDataFromDateToDate(assetEntity, fromDate, toDate);
        }

        return true;
    }

    public HttpURLConnection initConnection(AssetEntity assetEntity,
            LocalDate fromDate, LocalDate toDate) {
        try {
            String link = "http://ichart.finance.yahoo.com/table.csv?s=";

            link = link.concat(assetEntity.getSymbol() + "&d=");

            link = link.concat(String.valueOf(toDate.getMonthValue()) + "&e=");
            link = link.concat(String.valueOf(toDate.getDayOfMonth()) + "&f=");
            link = link.concat(String.valueOf(toDate.getYear()) + "&g=d&a=");

            link = link.concat(String.valueOf(fromDate.getMonthValue()) + "&b=");
            link = link.concat(String.valueOf(fromDate.getDayOfMonth()) + "&c=");
            link = link.concat(String.valueOf(fromDate.getYear()) + "&ignore=.csv");

            URL url = new URL(link);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setRequestProperty("User-Agent", "");
            return uc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String args[]) {
//        YahooStockDataUpdate yahoo = new YahooStockDataUpdate();
//
//        ExchangeManager exchangeManager = new ExchangeManager();
//        ExchangeEntity exchangeEntity = exchangeManager.getExchangeByName("NASDAQ");
//        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        try {
//            Date fromDate = df.parse("01-01-1990");
//            Date toDate = df.parse("31-12-1990");
//            yahoo.updateDataFromDateToDate(exchangeEntity, fromDate, toDate);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public boolean updateData(AssetEntity assetEntity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
