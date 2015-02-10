package business.dataUpdate;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.AssetManager;
import dataAccess.databaseManagement.manager.ExchangeManager;
import dataAccess.databaseManagement.manager.PriceManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

public class Cophieu68DataUpdate extends AbstractDataUpdate {

    //private Date latestDate;
    private static HttpContext localContext = null;
    private final String username = "trandinh487@gmail.com";
    private final String pass = "optimusprime";

    public Cophieu68DataUpdate() {
        this.description = "cophieu68.vn";
        this.exchangeNameList = new ArrayList<String>();
        this.exchangeNameList.add("HOSE");
        this.exchangeNameList.add("HASTC");
        this.exchangeNameList.add("INDEX");

        // get lastest date
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//				ExchangeManager exchangeManager = new ExchangeManager();
//				ExchangeEntity exchangeEntity = exchangeManager
//						.getExchangeByName(exchangeNameList.get(0));
//				PriceManager priceManager = new PriceManager();
//
//				java.sql.Date date = priceManager
//						.getLatestDateOfExchange(exchangeEntity.getExchangeID());
//				if (date != null) {
//					latestDate = new java.util.Date(date.getTime());
//				} else {
//					try {
//						latestDate = dateFormat.parse("1-1-2000");
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						latestDate = null;
//					}
//				}
//
//			}
//		}).start();

//		// set filename list
//		this.fileNameList = new ArrayList<String>();
//		this.fileNameList.add("excel_all_data.csv");
    }

//	public static boolean initExchangeMarketsAndAssets(String fileName) {
//		// TODO Auto-generated method stub
//		String symbol, exchangemarket, companyname, assetInfo;
//
//		// create BufferedReader to read csv file
//		BufferedReader br;
//		try {
//			br = new BufferedReader(new FileReader(fileName));
//
//			// TODO Auto-generated catch block
//			String strLine = "";
//			StringTokenizer st = null;
//			// read comma separated file line by line
//			AssetManager assetManager = new AssetManager();
//			ExchangeManager exchangeManager = new ExchangeManager();
//			exchangeManager.add(new ExchangeEntity("HOSE", 0.07));
//			exchangeManager.add(new ExchangeEntity("HASTC", 0.10));
//			exchangeManager.add(new ExchangeEntity("INDEX", -1));
//			ExchangeEntity hose = exchangeManager.getExchangeByName("HOSE");
//			ExchangeEntity hastc = exchangeManager.getExchangeByName("HASTC");
//			ExchangeEntity index = exchangeManager.getExchangeByName("INDEX");
//
//			strLine = br.readLine();
//			while ((strLine = br.readLine()) != null) {
//				// break comma separated line using ","
//				st = new StringTokenizer(strLine, ",");
//				symbol = "";
//				exchangemarket = "";
//				companyname = "";
//				assetInfo = "";
//				int numOfTokens = st.countTokens();
//				for (int i = 0; i < numOfTokens; ++i) {
//					if (i == 0) {
//						symbol = st.nextToken();
//					} else if (i == 1) {
//						exchangemarket = st.nextToken();
//					} else if (i == 2) {
//						companyname = st.nextToken();
//					} else if (i == 3) {
//						assetInfo = st.nextToken();
//					}
//				}
//				System.out.println(symbol + " " + exchangemarket + " "
//						+ companyname + " " + assetInfo);
//				if (exchangemarket.equals("HOSE")) {
//					assetManager.add(new AssetEntity(companyname, symbol, hose
//							.getExchangeID(), assetInfo, 0.07));
//				} else if (exchangemarket.equals("HASTC")) {
//					assetManager.add(new AssetEntity(companyname, symbol, hastc
//							.getExchangeID(), assetInfo, 0.10));
//				} else if (exchangemarket.equals("INDEX")) {
//					assetManager.add(new AssetEntity(companyname, symbol, index
//							.getExchangeID(), assetInfo, -1));
//				}
//			}
//			br.close();
//			/*
//			 * add VNINDEX and HASTC INDEX to asset table
//			 */
//			// AssetEntity vnIndex = new AssetEntity("VNINDEX", "VNINDEX",
//			// hose.getExchangeID(), "VNINDEX", 0.07);
//			// AssetEntity HASTC_index = new AssetEntity("HASTCIndex",
//			// "HASTCINDEX", hastc.getExchangeID(), "HASTCINDEX", 0.10);
//			// assetManager.add(vnIndex);
//			// assetManager.add(HASTC_index);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//	@Override
//	public boolean updateHistoricalData() {
//		// TODO Auto-generated method stub
//		String strLine = "";
//		StringTokenizer st = null;
//		DateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
//		String symbol = "";
//		String strDate = "";
//		Date date = new Date();
//		double open = 0;
//		double close = 0;
//		double high = 0;
//		double low = 0;
//		double volume = 0;
//
//		PriceManager priceManager = new PriceManager();
//		AssetManager assetManager = new AssetManager();
//		AssetEntity assetList = null;
//		for (int k = 0; k < this.fileNameList.size(); ++k) {
//			String fileName = this.fileNameList.get(k);
//			try {
//				// read comma separated file line by line
//				BufferedReader br = new BufferedReader(new FileReader(fileName));
//				int count = 0;
//				strLine = br.readLine();
//				while ((strLine = br.readLine()) != null) {
//					count++;
//					System.out.println(count);
//					// if (count > 1000) {
//					// break;
//					// }
//					// break comma separated line using ","
//					st = new StringTokenizer(strLine, ",");
//					int numOfTokens = st.countTokens();
//					for (int i = 0; i < numOfTokens; ++i) {
//						if (i == 0) {
//							symbol = st.nextToken();
//							assetList = assetManager
//									.getAssetBySymbolAndExchange(symbol, "HOSE");
//							if (assetList == null) {
//								assetList = assetManager
//										.getAssetBySymbolAndExchange(symbol,
//												"HASTC");
//								if (assetList == null) {
//									assetList = assetManager
//											.getAssetBySymbolAndExchange(
//													symbol, "INDEX");
//								}
//							}
//						} else if (i == 1) {
//							strDate = st.nextToken();
//							date = dateformat.parse(strDate);
//						} else if (i == 2) {
//							open = Double.parseDouble(st.nextToken());
//						} else if (i == 3) {
//							high = Double.parseDouble(st.nextToken());
//						} else if (i == 4) {
//							low = Double.parseDouble(st.nextToken());
//						} else if (i == 5) {
//							close = Double.parseDouble(st.nextToken());
//						} else if (i == 6) {
//							volume = Double.parseDouble(st.nextToken());
//						}
//					}
//					// System.out.println(symbol + " " + String.valueOf(date) +
//					// " " + String.valueOf(open) + " " + String.valueOf(high) +
//					// " " + String.valueOf(low) + " " + String.valueOf(close) +
//					// " " + String.valueOf(volume));
//					PriceEntity priceEntity = new PriceEntity(
//							assetList.getAssetID(), new java.sql.Date(
//									date.getTime()), null, volume, close, open,
//							high, low);
//					priceManager.add(priceEntity);
//				}
//
//				br.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
//		return true;
//	}
//	@Override
//	public boolean updateData() {
//		while (latestDate == null) {
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException ex) {
//				ex.printStackTrace();
//			}
//		}
//
//		AssetManager assetManager = new AssetManager();
//		PriceManager priceManager = new PriceManager();
//
//		Date currentDate = new Date();
//		if (this.latestDate.after(currentDate)) {
//			return false;
//		}
//		this.latestDate = utility.Utility.increaseDate(this.latestDate);
//		while (this.latestDate.before(currentDate)) {
//			for (String exchangeName : this.exchangeNameList) {
//				HttpURLConnection uc = initConnection(exchangeName,
//						this.latestDate);
//				try {
//					BufferedReader br = new BufferedReader(
//							new InputStreamReader(uc.getInputStream()));
//
//					double open, high, low, close;
//					double volume;
//					DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//					String strLine, symbol;
//
//					String strDate = dateFormat.format(this.latestDate);
//					String[] splitString;
//					// Open an output stream
//					br.readLine();
//					while ((strLine = br.readLine()) != null) {
//						// strLine is one line of text; readLine() strips the
//						// newline
//						// character(s)
//
//						// Print a line of text
//						splitString = strLine.split(",");
//						if (splitString[1].contentEquals(strDate)) {
//							symbol = splitString[0];
//							open = Double.valueOf(splitString[2]);
//							high = Double.valueOf(splitString[3]);
//							low = Double.valueOf(splitString[4]);
//							close = Double.valueOf(splitString[5]);
//							volume = Integer.valueOf(splitString[6]);
//							AssetEntity assetList = assetManager
//									.getAssetBySymbolAndExchange(symbol,
//											exchangeName);
//							if (assetList != null) {
//								PriceEntity priceEntity = new PriceEntity(
//										assetList.getAssetID(),
//										new java.sql.Date(this.latestDate
//												.getTime()),
//										null, volume, close, open, high, low);
//
//								priceManager.add(priceEntity);
//							}
//						}
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			this.latestDate = utility.Utility.increaseDate(this.latestDate);
//		}
//		String assetName[] = { "VNINDEX", "HASTCINDEX" };
//		String exchangeName[] = { "HOSE", "HASTC" };
//		return updateIndexData(assetName, exchangeName);
//	}
//	public boolean updateIndexData(String assetName[], String exchangeName[]) {
//		AssetManager assetManager = new AssetManager();
//		PriceManager priceManager = new PriceManager();
//		for (int i = 0; i < assetName.length; ++i) {
//			AssetEntity assetList = assetManager.getAssetBySymbolAndExchange(
//					assetName[i], exchangeName[i]);
//			Date lastDate = priceManager.getLatestDateOfAsset((int) assetList
//					.getAssetID());
//			Date currentDate = new Date();
//			Date tempDate;
//			Double open, high, close, low, volume;
//			DateFormat df = new SimpleDateFormat("yyyyMMdd");
//			String splitString[];
//
//			String fileLink;
//			if (assetName[i].equals("VNINDEX"))
//				fileLink = "http://www.cophieu68.com/export/metastock.php?id=^vnindex";
//			else
//				fileLink = "http://www.cophieu68.com/export/metastock.php?id=^hastc";
//
//			try {
//	        	HttpClient httpclient = new DefaultHttpClient();
//	            HttpGet httpGet = new HttpGet(fileLink);
//
//	            HttpResponse response = httpclient.execute(httpGet,localContext);
//				BufferedReader br = new BufferedReader(new InputStreamReader(
//	                    response.getEntity().getContent()));
//
//				String strLine = br.readLine();
//				while ((strLine = br.readLine()) != null) {
//					splitString = strLine.split(",");
//					tempDate = df.parse(splitString[1]);
//					if ((tempDate.after(currentDate))
//							|| (tempDate.before(lastDate))) {
//						break;
//					}
//					open = Double.valueOf(splitString[2]);
//					high = Double.valueOf(splitString[3]);
//					low = Double.valueOf(splitString[4]);
//					close = Double.valueOf(splitString[5]);
//					volume = Double.valueOf(splitString[6]);
//					PriceEntity priceEntity = new PriceEntity(
//							(int) assetList.getAssetID(), new java.sql.Date(
//									tempDate.getTime()), null, volume, close,
//							open, high, low);
//					priceManager.add(priceEntity);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return true;
//	}
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
                HttpResponse response = httpclient.execute(post, localContext);
                httpclient.getConnectionManager().shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean updateDataFromDateToDate(String exchangeName, Date fromDate, Date toDate) {
        // TODO Auto-generated method stub

        PriceManager priceManager = new PriceManager();
        AssetManager assetManager = new AssetManager();

        DecimalFormat decimalf = new DecimalFormat("#.##");

        ArrayList<Date> listDate = new ArrayList<Date>();
        for (Date date = fromDate; !date.after(toDate); date = new Date(date.getTime() + 24 * 60 * 60 * 1000)) {
            listDate.add(date);
        }
        
        Double open, high, close, low, volume;
        String splitString[];
        String assetSymbol;
        HttpResponse response;
        
        int size = listDate.size();
        if (exchangeName.equals("All")) {
            size *= assetManager.getAllAssets().size();
        } else {
            size *= assetManager.getAssetsByExchange(new ExchangeManager().getExchangeByName(exchangeName).getExchangeID()).size();
        }
        
        double i = 0;
        completePercentage = "0.0%";
        for (Date date : listDate) {
            response = getHttpResponse(exchangeName, date);
            if (response != null) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String strLine = br.readLine();
                    while ((strLine = br.readLine()) != null) {
                        completePercentage = decimalf.format(100.0 *( i / size)) + "%";
                        splitString = strLine.split(",");
                        
                        assetSymbol = splitString[0];
                        if (assetSymbol.charAt(0) != '^' && exchangeName.equals("INDEX")) {
                            continue;
                        }
                        
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
                                new java.sql.Date(date.getTime()));
                        
                        if (priceEntity == null) {
                            priceEntity = new PriceEntity(
                                    assetList.get(0).getAssetID(),
                                    new java.sql.Date(date.getTime()), null,
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public HttpResponse getHttpResponse(String exchangeName, Date date) {
        try {
            initLocalContext();

            String fileLink;
            if (exchangeName.equals("HASTC")) {
                fileLink = "http://www.cophieu68.vn/export/dailymetastock.php?stcid=2&date=";
            } else if (exchangeName.equals("HOSE")) {
                fileLink = "http://www.cophieu68.vn/export/dailymetastock.php?stcid=1&date=";
            } else {
                fileLink = "http://www.cophieu68.vn/export/dailymetastock.php?stcid=0&date=";
            }
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String fileName = dateFormat.format(date);
            fileLink = fileLink.concat(fileName);

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(fileLink);

            return httpclient.execute(httpGet, localContext);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateDataFromDateToDate(AssetEntity assetEntity,
            Date fromDate, Date toDate) {
        // TODO Auto-generated method stub

        initLocalContext();

        PriceManager priceManager = new PriceManager();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        Date tempDate;
        Double open, high, close, low, volume;
        String splitString[];

        String fileLink = "http://www.cophieu68.vn/export/excel.php?id=" + assetEntity.getSymbol();
        fileLink = fileLink.replace("^", "%5E");

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(fileLink);

            HttpResponse response = httpclient.execute(httpGet, localContext);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String strLine = br.readLine();
            while ((strLine = br.readLine()) != null) {
                splitString = strLine.split(",");
                tempDate = df.parse(splitString[1]);
                if ((tempDate.after(toDate)) || (tempDate.before(fromDate))) {
                    continue;
                }
                open = Double.valueOf(splitString[2]);
                high = Double.valueOf(splitString[3]);
                low = Double.valueOf(splitString[4]);
                close = Double.valueOf(splitString[5]);
                volume = Double.valueOf(splitString[6]);

                PriceEntity priceEntity = priceManager.getPriceByAssetIDAndDate(
                        (int) assetEntity.getAssetID(),
                        new java.sql.Date(tempDate.getTime()));
                if (priceEntity == null) {
                    priceEntity = new PriceEntity(
                            (int) assetEntity.getAssetID(), new java.sql.Date(
                            tempDate.getTime()), null, volume, close,
                            open, high, low);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean updateData(AssetEntity assetEntity) {
        // TODO Auto-generated method stub

        initLocalContext();

        PriceManager priceManager = new PriceManager();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        Date tempDate;
        Double open, high, close, low, volume;
        String splitString[];

        String fileLink = "http://www.cophieu68.vn/export/excel.php?id=" + assetEntity.getSymbol();
        fileLink = fileLink.replace("^", "%5E");

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(fileLink);

            HttpResponse response = httpclient.execute(httpGet, localContext);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String strLine = br.readLine();
            while ((strLine = br.readLine()) != null) {
                splitString = strLine.split(",");
                tempDate = df.parse(splitString[1]);
                open = Double.valueOf(splitString[2]);
                high = Double.valueOf(splitString[3]);
                low = Double.valueOf(splitString[4]);
                close = Double.valueOf(splitString[5]);
                volume = Double.valueOf(splitString[6]);

                PriceEntity priceEntity = priceManager.getPriceByAssetIDAndDate(
                        (int) assetEntity.getAssetID(),
                        new java.sql.Date(tempDate.getTime()));
                if (priceEntity == null) {
                    priceEntity = new PriceEntity(
                            (int) assetEntity.getAssetID(), new java.sql.Date(
                            tempDate.getTime()), null, volume, close,
                            open, high, low);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
//	public static void main(String[] args) {
//		// initExchangeMarketsAndAssets("company_list.csv");
//		// (new Cophieu68DataUpdate()).updateHistoricalData();
//
//		// ExchangeManager exchangeManager = new ExchangeManager();
//		AssetManager assetManager = new AssetManager();
//		// PriceManager priceManager = new PriceManager();
//		//
//		// ArrayList<PriceEntity> priceEntitys = priceManager
//		// .getPriceByAssetID(assetManager.getAssetBySymbolAndExchange("CLW",
//		// "HOSE").getAssetID());
//		//
//		// for (int i = 1; i < priceEntitys.size(); ++i) {
//		// if (priceEntitys.get(i).getVolume() == 0) {
//		// priceEntitys.get(i).setClose(priceEntitys.get(i - 1).getClose());
//		// priceManager.update(priceEntitys.get(i));
//		// }
//		// }
//
//		Calendar calendar = Calendar.getInstance();
//
//		calendar.set(2000, 0, 1);
//		Date startDate = calendar.getTime();
//		System.out.println(startDate.toString());
//
//		calendar.set(2013, 1, 15);
//		Date endDate = calendar.getTime();
//		System.out.println(endDate.toString());
//
//		Cophieu68DataUpdate cophieu68DataUpdate = new Cophieu68DataUpdate();
//
//		cophieu68DataUpdate.updateDataFromDateToDate(
//				assetManager.getAssetBySymbolAndExchange("VNM", "HOSE"),
//				startDate, endDate);
//		// cophieu68DataUpdate.updateDataFromDateToDate(assetManager.getAssetBySymbolAndExchange("CLW",
//		// "HOSE"), startDate,
//		// endDate);
//		// cophieu68DataUpdate.updateDataFromDateToDate(assetManager.getAssetBySymbolAndExchange("DRC",
//		// "HOSE"), startDate,
//		// endDate);
//		// cophieu68DataUpdate.updateDataFromDateToDate(assetManager.getAssetBySymbolAndExchange("HHS",
//		// "HOSE"), startDate,
//		// endDate);
//		// cophieu68DataUpdate.updateDataFromDateToDate(assetManager.getAssetBySymbolAndExchange("DVD",
//		// "HOSE"), startDate,
//		// endDate);
//
//	}
}
