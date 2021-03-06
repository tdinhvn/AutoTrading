package business.dataUpdate;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.ExchangeEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.AssetManager;
import dataAccess.databaseManagement.manager.ExchangeManager;
import dataAccess.databaseManagement.manager.PriceManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utility.Utility;

public class Cophieu68DataUpdate extends AbstractDataUpdate {

    private static HttpContext localContext = null;
    private final String username = "trandinh487@gmail.com";
    private final String pass = "optimusprime";

    public Cophieu68DataUpdate() {
        this.description = "cophieu68.vn";
        this.exchangeNameList = new ArrayList<>();
        this.exchangeNameList.add("HOSE");
        this.exchangeNameList.add("HASTC");
        this.exchangeNameList.add("UPCOM");
        this.exchangeNameList.add("INDEX");
    }

    private String getUrlOfDailyPriceDataFile(LocalDate date) {
        String url = "http://www.cophieu68.vn/export/dailyexcel.php?date=";
        url += date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return url;
    }

    private String getUrlOfCompanyInformationByAssetSymbol(String symbol) {
        return "http://www.cophieu68.vn/profilesymbol.php?id=" + symbol;
    }

    private String getUrlOfPriceDataFileByAssetSymbol(String symbol) {
        return "http://www.cophieu68.vn/export/excel.php?id=" + symbol.replace("^", "%5E");
    }

    private void initLocalContext() {
        if (localContext == null) {
            HttpClient httpclient = new DefaultHttpClient();
            localContext = new BasicHttpContext();
            CookieStore cookieStore = new BasicCookieStore();

            HttpPost post = new HttpPost("http://www.cophieu68.vn/account/login.php");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("tpassword", pass));
                nameValuePairs.add(new BasicNameValuePair("rememberme", "0"));
                nameValuePairs.add(new BasicNameValuePair("login", "ĐĂNG NHẬP"));

                localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpclient.execute(post, localContext);
                httpclient.getConnectionManager().shutdown();
            } catch (IOException ex) {
                Logger.getLogger(Cophieu68DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public HttpResponse getHttpResponse(String fileLink) {
        try {
            initLocalContext();

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(fileLink);

            return httpclient.execute(httpGet, localContext);
        } catch (IOException ex) {
            Logger.getLogger(Cophieu68DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Get information of all assets on HOSE and HASTC exchange market from
     * Cophieu68 web-site. Then update the information of these assets in Auto-
     * trading database.
     *
     * @return whether update process is successful.
     */
    public boolean updateAssetList() {
        ExchangeManager exchangeManager = new ExchangeManager();
        ExchangeEntity hose = exchangeManager.getExchangeByName("HOSE");
        if (hose == null) {
            hose = new ExchangeEntity("HOSE", 0.07);
            exchangeManager.add(hose);
        }

        ExchangeEntity hastc = exchangeManager.getExchangeByName("HASTC");
        if (hastc == null) {
            hastc = new ExchangeEntity("HASTC", 0.10);
            exchangeManager.add(hastc);
        }

        ExchangeEntity upcom = exchangeManager.getExchangeByName("UPCOM");
        if (upcom == null) {
            upcom = new ExchangeEntity("UPCOM", 0.15);
            exchangeManager.add(upcom);
        }

        ExchangeEntity index = exchangeManager.getExchangeByName("INDEX");
        if (index == null) {
            index = new ExchangeEntity("INDEX", -1);
            exchangeManager.add(index);
        }

        // get list of all assets on HOSE, HASTC, and UPCOM exchange market
        LocalDate latestWorkingDate = Utility.getLatestWorkingDate();
        HttpResponse httpResponse = getHttpResponse(getUrlOfDailyPriceDataFile(latestWorkingDate));
        List<String> assetSymbols = new ArrayList<>();
        if (httpResponse != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))) {
                String strLine = br.readLine();
                while ((strLine = br.readLine()) != null) {
                    assetSymbols.add(strLine.split(",")[0]);
                }
            } catch (IOException ex) {
                Logger.getLogger(Cophieu68DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }

        // get name and exchange market of each asset
        // then add each asset to database
        AssetManager assetManager = new AssetManager();
        for (String assetSymbol : assetSymbols) {
            if (assetManager.getAssetsBySymbol(assetSymbol).isEmpty()) {
                try {
                    Document doc = Jsoup.connect(getUrlOfCompanyInformationByAssetSymbol(assetSymbol)).get();
                    String exchangemarket = doc.getElementsContainingOwnText("Sàn Giao Dịch").get(0).parent().child(1).text();
                    String companyname = doc.getElementById("begin_header").getElementsByTag("h1").text();
                    String assetInfo = "";
                    AssetEntity assetEntity = null;
                    switch (exchangemarket.toLowerCase()) {
                        case "hose":
                            assetEntity = new AssetEntity(companyname, assetSymbol, hose.getExchangeID(), assetInfo, 0.07);
                            break;
                        case "hnx":
                            assetEntity = new AssetEntity(companyname, assetSymbol, hastc.getExchangeID(), assetInfo, 0.10);
                            break;
                        case "":
                            if (assetSymbol.charAt(0) == '^') {
                                assetEntity = new AssetEntity(companyname, assetSymbol, index.getExchangeID(), assetInfo, -1);
                            } else {
                                assetEntity = new AssetEntity(companyname, assetSymbol, upcom.getExchangeID(), assetInfo, 0.15);
                            }
                            break;
                    }
                    assetManager.add(assetEntity);
                    System.out.println("Added asset " + assetSymbol + ".");
                } catch (IOException ex) {
                    Logger.getLogger(Cophieu68DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
            }
        }
        
        assetManager.closeConnection();
        return true;
    }

    @Override
    public boolean updateDataFromDateToDate(LocalDate fromDate, LocalDate toDate) {
        PriceManager priceManager = new PriceManager();
        AssetManager assetManager = new AssetManager();

        DecimalFormat decimalf = new DecimalFormat("#.##");

        Double open, high, close, low, volume;
        String splitString[];
        String assetSymbol;
        HttpResponse response;

        int size = (fromDate.until(toDate).getDays() + 1) * assetManager.getAllAssets().size();

        double i = 0;
        completePercentage = "0.0%";
        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            response = getHttpResponse(getUrlOfDailyPriceDataFile(date));
            if (response != null) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String strLine = br.readLine();
                    while ((strLine = br.readLine()) != null) {
                        completePercentage = decimalf.format(100.0 * (i / size)) + "%";
                        splitString = strLine.split(",");

                        assetSymbol = splitString[0];
                        open = Double.valueOf(splitString[2]);
                        high = Double.valueOf(splitString[3]);
                        low = Double.valueOf(splitString[4]);
                        close = Double.valueOf(splitString[5]);
                        volume = Double.valueOf(splitString[6]);

                        ArrayList<AssetEntity> assetList = assetManager.getAssetsBySymbol(assetSymbol);
                        if (assetList.isEmpty()) {
                            continue;
                        }

                        PriceEntity priceEntity = priceManager.getPriceByAssetIDAndDate(
                                assetList.get(0).getAssetID(),
                                java.sql.Date.valueOf(date));

                        if (priceEntity == null) {
                            priceEntity = new PriceEntity(
                                    assetList.get(0).getAssetID(),
                                    java.sql.Date.valueOf(date), null,
                                    volume, close, open, high, low);
                            priceManager.add(priceEntity);
                        } else {
                            priceEntity.setVolume(volume);
                            priceEntity.setClose(close);
                            priceEntity.setOpen(open);
                            priceEntity.setHigh(high);
                            priceEntity.setLow(low);
                            priceManager.update(priceEntity);
                        }
                        i++;
                    }
                    br.close();

                } catch (IOException | IllegalStateException | NumberFormatException ex) {
                    Logger.getLogger(Cophieu68DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
            }
        }

        priceManager.closeConnection();
        assetManager.closeConnection();
        return true;
    }

    @Override
    public boolean updateDataFromDateToDate(AssetEntity assetEntity, LocalDate fromDate, LocalDate toDate) {
        initLocalContext();

        PriceManager priceManager = new PriceManager();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate tempDate;
        Double open, high, close, low, volume;
        String splitString[];

        try {
            HttpResponse response = getHttpResponse(getUrlOfPriceDataFileByAssetSymbol(assetEntity.getSymbol()));
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String strLine = br.readLine();
            while ((strLine = br.readLine()) != null) {
                splitString = strLine.split(",");
                tempDate = LocalDate.parse(splitString[1], df);
                if ((tempDate.isAfter(toDate)) || (tempDate.isBefore(fromDate))) {
                    continue;
                }
                open = Double.valueOf(splitString[2]);
                high = Double.valueOf(splitString[3]);
                low = Double.valueOf(splitString[4]);
                close = Double.valueOf(splitString[5]);
                volume = Double.valueOf(splitString[6]);

                PriceEntity priceEntity = priceManager.getPriceByAssetIDAndDate(
                        (int) assetEntity.getAssetID(),
                        Date.valueOf(tempDate));
                if (priceEntity == null) {
                    priceEntity = new PriceEntity(
                            (int) assetEntity.getAssetID(), Date.valueOf(tempDate),
                            null, volume, close, open, high, low);
                    priceManager.add(priceEntity);
                } else {
                    priceEntity.setVolume(volume);
                    priceEntity.setClose(close);
                    priceEntity.setOpen(open);
                    priceEntity.setHigh(high);
                    priceEntity.setLow(low);
                    priceManager.update(priceEntity);
                }
            }
        } catch (IOException | IllegalStateException | NumberFormatException ex) {
            Logger.getLogger(Cophieu68DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        priceManager.closeConnection();
        return true;
    }

    @Override
    public boolean updateData(AssetEntity assetEntity) {
        PriceManager priceManager = new PriceManager();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate tempDate;
        Double open, high, close, low, volume;
        String splitString[];

        try {
            HttpResponse response = getHttpResponse(getUrlOfPriceDataFileByAssetSymbol(assetEntity.getSymbol()));
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String strLine = br.readLine();
            while ((strLine = br.readLine()) != null) {
                splitString = strLine.split(",");
                tempDate = LocalDate.parse(splitString[1], df);
                open = Double.valueOf(splitString[2]);
                high = Double.valueOf(splitString[3]);
                low = Double.valueOf(splitString[4]);
                close = Double.valueOf(splitString[5]);
                volume = Double.valueOf(splitString[6]);

                PriceEntity priceEntity = priceManager.getPriceByAssetIDAndDate(
                        (int) assetEntity.getAssetID(),
                        Date.valueOf(tempDate));
                if (priceEntity == null) {
                    priceEntity = new PriceEntity(
                            (int) assetEntity.getAssetID(), Date.valueOf(tempDate), 
                            null, volume, close, open, high, low);
                    priceManager.add(priceEntity);
                } else {
                    priceEntity.setVolume(volume);
                    priceEntity.setClose(close);
                    priceEntity.setOpen(open);
                    priceEntity.setHigh(high);
                    priceEntity.setLow(low);
                    priceManager.update(priceEntity);
                }
            }
            priceManager.closeConnection();
        } catch (IOException ex) {
            Logger.getLogger(Cophieu68DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    public static void main(String[] args) throws IOException {
        Cophieu68DataUpdate updater = new Cophieu68DataUpdate();
        //updater.updateAssetList();

        AssetManager assetManager = new AssetManager();
        for (AssetEntity assetEntity : assetManager.getAllAssets()) {
            System.out.println(assetEntity.toString());
            updater.updateData(assetEntity);
        }
    }
}
