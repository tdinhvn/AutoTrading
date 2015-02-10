package business.fundamentalModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

import business.algorithm.decisionAlgorithm.AbstractDecisionAlgorithm;
import business.algorithm.decisionAlgorithm.MovingAverage;
import business.algorithm.decisionAlgorithm.Order;
import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.ExchangeEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.AssetManager;
import dataAccess.databaseManagement.manager.ExchangeManager;
import dataAccess.databaseManagement.manager.PriceManager;

public class FundamentalEvaluation {
	public static final int MA_PERIOD = 30;
	public static final double INITIAL_CASH = 100000;
	public static final String PHILTOWN = "PhilTown";
	public static final String PFG = "PFG";

	
	/*
	 * 
	 */

	public static void toFile(String modelName, ArrayList<AssetStat> asStatList, String index, String exchange, Date sDate, Date eDate) {
		PriceManager priceManager = new PriceManager();
		double initialCapital = INITIAL_CASH * asStatList.size();
			
		java.sql.Date beginDate = new java.sql.Date(sDate.getTime());
		java.sql.Date endDate = new java.sql.Date(eDate.getTime());

                if ("HASTC".equals(exchange)) {
                    index = "^HASTC";
                } else {
                    index = "^VNINDEX";
                }
                
		ArrayList<PriceEntity> indexList = priceManager.getPriceInInterval(
				(new AssetManager()).getAssetBySymbolAndExchange(index, "INDEX").getAssetID(), beginDate, endDate);
		
		TreeMap<Date, Double> hisPortfolio = new TreeMap<Date, Double>();
		for (PriceEntity curDate :indexList ) {
			hisPortfolio.put(curDate.getDate(), 0.0);
		}
		ArrayList<Date> dateList = new ArrayList<Date>();
		
		
		Calendar year = Calendar.getInstance();
		year.setTime(sDate);
		
		try {
			// Create file
//			FileWriter fstream = new FileWriter("out.csv");
			FileWriter fstream = new FileWriter(modelName + "_" + exchange + "_" + year.get(Calendar.YEAR) + ".csv");
			BufferedWriter out = new BufferedWriter(fstream);
			
			for(AssetStat curAssetStat : asStatList)
				out.write(curAssetStat.getAsset().getSymbol() + "\n");
			out.write("\n");

			
			for (AssetStat curAssetStat : asStatList) {
				
				out.write(curAssetStat.getAsset().getSymbol() + "\n" );

				AbstractDecisionAlgorithm curAlgorithm = new MovingAverage();
				TreeMap<String, Object> map = new TreeMap<String, Object>();

				map.put("MA period", MA_PERIOD);

				TreeMap<AssetEntity, ArrayList<PriceEntity>> priceMap = new TreeMap<AssetEntity, ArrayList<PriceEntity>>();

				ArrayList<PriceEntity> priceList = priceManager.getPriceByAssetID(curAssetStat.getAsset().getAssetID());
				
				priceMap.put(curAssetStat.getAsset(), priceList);
				map.put("Price list", priceMap);
				curAlgorithm.setParameterValue(map);


				/*
				 * update portfolio for each asset
				 */
				
				TreeMap<Date, Order> orderMap = new TreeMap<Date, Order>();
				Order orderIndicator = null;
				for (Order curOrder : curAlgorithm.runAlgorithm().getOrderList()) {
					if ((endDate.compareTo(curOrder.getDate()) > 0) && (beginDate.compareTo(curOrder.getDate()) < 0)) {
						if (orderMap.isEmpty()) 
							orderMap.put(curOrder.getDate(), curOrder);
						
						else {
							if (orderIndicator == null) {
								if (daysBetweenDates(orderMap.lastKey(), curOrder.getDate(), priceList) <= 4)
									orderIndicator = curOrder;
								else
									orderMap.put(curOrder.getDate(), curOrder);
								
							} else {
								if (daysBetweenDates(orderMap.lastKey(), curOrder.getDate(), priceList) <= 4)
									orderIndicator = null;
								else {
									PriceEntity priceT4 = priceT4(orderMap.lastKey(), priceList);
									orderIndicator = new Order(orderIndicator.getAsset(), orderIndicator.isOrderType(), priceT4.getClose(), priceT4.getDate());
									orderMap.put(orderIndicator.getDate(), orderIndicator);
									System.out.println(orderIndicator.getDate());
									
									if (daysBetweenDates(orderMap.lastKey(), curOrder.getDate(), priceList) <= 4)
										orderIndicator = curOrder;
									else {
										orderIndicator = null;
										orderMap.put(curOrder.getDate(), curOrder);
									}
								}
							}
						}
						
						System.out.println(curOrder.getDate() + "\t"
								+ curOrder.getAsset().getSymbol()
								+ curOrder.getAsset().getAssetID() + "\t" + curOrder.isOrderType());
					}
					
				}
				
				if (orderIndicator != null) { // add remaining order
					PriceEntity priceT4 = priceT4(orderMap.lastKey(), priceList);
					orderIndicator = new Order(orderIndicator.getAsset(), orderIndicator.isOrderType(), priceT4.getClose(), priceT4.getDate());
					orderMap.put(orderIndicator.getDate(), orderIndicator);
					System.out.println(orderIndicator.getDate());
				}
				
				
				
				if (!orderMap.firstEntry().getValue().isOrderType()) // remove first order if it is sellType
					orderMap.remove(orderMap.firstKey());
				
				TreeMap<Date, Double> priceEntityMap = new TreeMap<Date, Double>();
				for (PriceEntity curPrice : priceList) {
					if ((endDate.compareTo(curPrice.getDate()) > 0) && (beginDate.compareTo(curPrice.getDate()) < 0)) {
						priceEntityMap.put(curPrice.getDate(), curPrice.getClose());
					}
				}

				double cash = INITIAL_CASH;
				double volume = 0;
				double initialPrice = priceEntityMap.firstEntry().getValue();

				
				out.write("Date,Price Rate,Profit Rate,Price,Order\n");
				for (Date curDate : priceEntityMap.keySet()){
//					System.out.println(curDate);
					
					double priceRate = priceEntityMap.get(curDate)/initialPrice - 1;
					String orderType = "";
					
					if (orderMap.containsKey(curDate) ) {
						business.algorithm.decisionAlgorithm.Order curOrder = orderMap.get(curDate);
						
						if (curOrder.isOrderType()) { // buyType
							volume = (long) cash / curOrder.getPrice();
							cash -= curOrder.getPrice() * volume;
							orderType = "Buy";

						} else { // sellType
							cash += curOrder.getPrice() * volume;
							volume = 0;
							orderType = "Sell";
						}
					} 

					double totalCash = priceEntityMap.get(curDate) * volume + cash;
					double rate = totalCash/INITIAL_CASH - 1; 
					

					out.write(curDate + "," + priceRate + "," + rate + "," + priceEntityMap.get(curDate) + "," + orderType + "\n");
					if (hisPortfolio.containsKey(curDate)) {
						hisPortfolio.put(curDate, hisPortfolio.get(curDate) + priceEntityMap.get(curDate) * volume + cash);
						dateList.add(curDate);
					}
				}
				
				out.write("\n");
			}

			double initialPrice = indexList.get(0).getClose();
			
			out.write(index + "\n");
			out.write("Date,Price Rate,Profit Rate,Price,Portfolio\n");
			for (PriceEntity curDate :indexList ) {
				double rate = hisPortfolio.get(curDate.getDate())/initialCapital - 1;
				double priceRate = curDate.getClose() / initialPrice - 1 ;
				
				if ((dateList.contains(curDate.getDate())) 
						) {
					out.write(curDate.getDate() + "," + priceRate + ","  + rate + "," + curDate.getClose() + "," + hisPortfolio.get(curDate.getDate()) +"\n");
				}
				
			}
			
			
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	
	public static int daysBetweenDates(Date startDate, Date endDate, ArrayList<PriceEntity> priceList) {
		int i = 0;
		for(PriceEntity curPrice : priceList) {
			if ((curPrice.getDate().compareTo(startDate) > 0) && (curPrice.getDate().compareTo(endDate) <= 0))
				i++;
		}
		if (i == 0)
			return 5;
		return i;
	}
	
	public static PriceEntity priceT4(Date date, ArrayList<PriceEntity> priceList) {
		PriceEntity priceT4 = null;
		for(PriceEntity curPrice : priceList) {
			if (curPrice.getDate().compareTo(date) == 0) {
				priceT4 = curPrice;
				break;
			}
		}
		
		Collections.sort(priceList);

		return priceList.get(priceList.indexOf(priceT4) + 4);
	}
	
	
	
	
	public static AbstractFundamentalModel modelHist(String modelName, String exchange, Date date, TreeMap<String, Object> map) {
		ExchangeManager exchangeManager = new ExchangeManager();
		AssetManager assetManager= new AssetManager();
	
		ExchangeEntity exchangeEntity = exchangeManager.getExchangeByName(exchange);
		ArrayList<AssetEntity> assetListTemp = assetManager.getAssetsByExchange(exchangeEntity.getExchangeID());
		ArrayList<AssetEntity> assetList = new ArrayList<AssetEntity>();
		
		
		for(AssetEntity curAsset : assetListTemp) {
			if (curAsset.getSymbol().equals("VNINDEX") 
					|| curAsset.getSymbol().equals("HASTCINDEX") 
					|| curAsset.getSymbol().equals("MAFPF1") 
					|| curAsset.getSymbol().equals("PRUBF1") 
					|| curAsset.getSymbol().equals("VFMVF1") 
					|| curAsset.getSymbol().equals("VFMVF4") 
					|| curAsset.getSymbol().equals("VFMVFA") 
					|| curAsset.getSymbol().equals("ASA") 
//					|| curAsset.getSymbol().equals("BBT") 
//					|| curAsset.getSymbol().equals("FPC") 
//					|| curAsset.getSymbol().equals("HT2") 
//					|| curAsset.getSymbol().equals("NKD") 
//					|| curAsset.getSymbol().equals("NTP") 
//					|| curAsset.getSymbol().equals("S96") 
//					|| curAsset.getSymbol().equals("BTC")
					){
			}
			else {
					assetList.add(curAsset);

			}
		}

		AbstractFundamentalModel abstractFModel = FundamentalModelAPI.getFundamentalModel(modelName);
        TreeMap<String, Object> valueMap = new TreeMap<String, Object>();

        valueMap.put("Asset List", assetList);
        valueMap.put("Date", date);
        valueMap.putAll(map);

    	abstractFModel.setParameterValue(valueMap);

        
//      Calendar bDate = Calendar.getInstance();
//		bDate.set(2010,0, 1);
//		Calendar eDate = Calendar.getInstance();
//		eDate.set(2011,0, 1);
//		ArrayList<AssetStat> asStatList = abstractFModel.getAssetStatList();
//		toFile(asStatList,"VNINDEX" ,"HOSE", bDate.getTime(), eDate.getTime());
//		toFile(asStatList,"HASTCINDEX" ,"HASTC", bDate.getTime(), eDate.getTime());
    	
		return abstractFModel;
	}
	
	
	public static void toFileModelHist(String modelName, String exchange) {
		Calendar ca = Calendar.getInstance();
		ArrayList<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
		ca.set(2009, 2, 31);
		dateList.add(new java.sql.Date(ca.getTime().getTime()));
		ca.set(2010, 2, 31);
		dateList.add(new java.sql.Date(ca.getTime().getTime()));
		ca.set(2011, 2, 31);
		dateList.add(new java.sql.Date(ca.getTime().getTime()));
		ca.set(2012, 1, 15);
		dateList.add(new java.sql.Date(ca.getTime().getTime()));
		
        TreeMap<String, Object> valueMap = new TreeMap<String, Object>();
        valueMap.put("P/E", 5.0);
        valueMap.put("P/B", 1.0);
        valueMap.put("ROE", 0.27);
        valueMap.put("RevenueGrowth", 0.3);

		try {
			// Create file
			FileWriter fstream = new FileWriter(modelName + "_"+ exchange + ".csv");
			BufferedWriter out = new BufferedWriter(fstream);

			for (java.sql.Date curDate : dateList) {
				AbstractFundamentalModel abstractFModel = modelHist(modelName,
						exchange, curDate, valueMap);
				System.out.println(curDate);
				
				out.write(curDate + "\n");
				if (modelName.equals("PhilTown")) 
					out.write("Symbol,Current Price,Predict Price,ROE,EPS,Year\n" );
					
				if (modelName.equals("PFG")) 
					out.write("Symbol,Current Price,ROE,P/E,P/B,Year\n" );
				
				out.write(abstractFModel.toString());
				System.out.println(abstractFModel.toString());
			}
			
			
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public static void toFile(String modelName, ArrayList<AssetStat> asStatList, String exchange, Date sDate, Date eDate) {
		String index = null;
		if (exchange.equals("HOSE"))
			index = "^VNINDEX";
		if (exchange.equals("HASTC"))
			index = "^HASTC";
		toFile(modelName, asStatList,index ,exchange, sDate, eDate);
	}
	
	
	//TODO : missing ACC PTB
	public static void main(String[] args) {
		
		toFileModelHist(PHILTOWN, "HOSE");
		
//		ExchangeManager exchangeManager = new ExchangeManager();
//		AssetManager assetManager= new AssetManager();
//	
//		ExchangeEntity exchangeEntity = exchangeManager.getExchangeByName("HASTC");
//		ArrayList<AssetEntity> assetListTemp = assetManager.getAssetsByExchange(exchangeEntity.getExchangeID());
//		ArrayList<AssetEntity> assetList = new ArrayList<AssetEntity>();
//		
////		assetListTemp = assetManager.getAssetsBySymbol("TV3");
////		assetListTemp.addAll(assetManager.getAssetsBySymbol("PGS"));
////		assetListTemp.addAll(assetManager.getAssetsBySymbol("LCG"));
////		assetListTemp.addAll(assetManager.getAssetsBySymbol("SMC"));
////		assetListTemp.addAll(assetManager.getAssetsBySymbol("ST8"));
//		
////		assetListTemp.addAll(assetManager.getAssetsBySymbol("VNA"));
////		assetListTemp.addAll(assetManager.getAssetsBySymbol("VSC"));
////		assetListTemp.addAll(assetManager.getAssetsBySymbol("VST"));
//		
////		assetListTemp.addAll(assetManager.getAssetsBySymbol("LAF"));
////		assetListTemp.addAll(assetManager.getAssetsBySymbol("LHG"));
////		assetListTemp.addAll(assetManager.getAssetsBySymbol("TSC"));
////		assetListTemp.addAll(assetManager.getAssetsBySymbol("VTF"));
//		
//		
//		/*
//		 * NTP : not full report
//		 * S96 : not full report
//		 */
//		for(AssetEntity curAsset : assetListTemp) {
//			if (curAsset.getSymbol().equals("VNINDEX") 
//					|| curAsset.getSymbol().equals("HASTCINDEX") 
//					|| curAsset.getSymbol().equals("MAFPF1") 
//					|| curAsset.getSymbol().equals("PRUBF1") 
//					|| curAsset.getSymbol().equals("VFMVF1") 
//					|| curAsset.getSymbol().equals("VFMVF4") 
//					|| curAsset.getSymbol().equals("VFMVFA") 
//					|| curAsset.getSymbol().equals("ASA") 
//					|| curAsset.getSymbol().equals("TV3") 
////					|| curAsset.getSymbol().equals("FPC") 
////					|| curAsset.getSymbol().equals("HT2") 
////					|| curAsset.getSymbol().equals("NKD") 
////					|| curAsset.getSymbol().equals("NTP") 
////					|| curAsset.getSymbol().equals("S96") 
////					|| curAsset.getSymbol().equals("BTC")
//					){
//			}
//			else {
//					assetList.add(curAsset);
//			}
//		}
		
		Calendar ca = Calendar.getInstance();
		ca.set(2011, 2, 31);
		String model = PFG;
		
		AbstractFundamentalModel abstractFModel = FundamentalModelAPI.getFundamentalModel(model);
        TreeMap<String, Object> valueMap = new TreeMap<String, Object>();
        
        valueMap.put("Asset List", AbstractFundamentalModel.assList(new ExchangeManager().getExchangeByName("HASTC")));
        valueMap.put("Date", ca.getTime());
        valueMap.put("P/E", 5.0);
        valueMap.put("P/B", 1.0);
        valueMap.put("ROE", 0.27);
        valueMap.put("RevenueGrowth", 0.3);
              
        abstractFModel.setParameterValue(valueMap);
        System.out.println(abstractFModel.toString());
        
		
		Calendar bDate = Calendar.getInstance();
		Calendar eDate = Calendar.getInstance();
		//ArrayList<AssetStat> asStatList = abstractFModel.getAssetStatList();
		
		if (ca.get(Calendar.YEAR) == 2009) {
			bDate.set(2009, 1, 31);
			eDate.set(2010,2, 30);
		}
		
		if (ca.get(Calendar.YEAR) == 2010) {
			bDate.set(2010,2, 31);
			eDate.set(2011,2, 30);
		}
		
		if (ca.get(Calendar.YEAR) == 2011) {
			bDate.set(2011,2, 31);
			eDate.set(2012,2, 30);	
		}

		
		
//		toFile(model, asStatList,"^VNINDEX" ,"HOSE", bDate.getTime(), eDate.getTime());
		
//		toFile(model, asStatList,"^HASTC" ,"HASTC", bDate.getTime(), eDate.getTime());
	}

}
