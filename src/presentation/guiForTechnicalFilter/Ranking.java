/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.guiForTechnicalFilter;

import business.algorithm.predictAlgorithm.AbstractPredictAlgorithm;
import business.algorithm.predictAlgorithm.OutputForPredictionAlgorithm;
import business.algorithm.predictAlgorithm.PriceEntry;
import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.ExchangeEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.AssetManager;
import dataAccess.databaseManagement.manager.PriceManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

/**
 *
 * @author TDINH
 */
public class Ranking {
    public static final String PERCENTAGE_INCREASE = "Percentage Increase";
    public static final String SHARPE_RATIO = "Sharpe Ratio";
    public static final String PERINC_OVER_RISK = "Per_Inc Over Risk";
    public static final double RISK_FREE_RETURN = 0.09/260;
    
    public static final String[] RANKING_METHOD_LIST = {PERCENTAGE_INCREASE, SHARPE_RATIO, PERINC_OVER_RISK};
    public ExchangeEntity exchangeEntity;
    public TreeMap<Double, ArrayList<Object>> output;
    public AbstractPredictAlgorithm preAlg;
    public Date startPredictDate;
    public int noTraining = 1000;
    public String completePercentage = "0.0%";
    public int period = 4;
    public int noSam = 40;
    private String type = "";
    
    public Ranking(String type) {
        this.type = type;
    }
    
    public void rank() throws IOException, Exception {
        if (this.type.equals(PERCENTAGE_INCREASE)) {
            rankPerInc();
        } else if (this.type.equals(SHARPE_RATIO)) {
            rankSharpeRatio();
        } else if (this.type.equals(PERINC_OVER_RISK)) {
            rankPerIncOverRisk();
        }
    }
    
    /**
     * @param args
     * @throws IOException
     */
    public void rankPerInc() throws IOException, Exception {
        
        BufferedWriter bufferedWriterResult = new BufferedWriter(
                new FileWriter(new File(exchangeEntity.getName() + "-" + PERCENTAGE_INCREASE + "-" +  startPredictDate + ".csv")));

        // choose testing asset and reference symbols
        AssetManager assetManager = new AssetManager();
        PriceManager priceManager = new PriceManager();

        if (exchangeEntity == null) {
            return;
        }
        
        ArrayList<AssetEntity> assetEntities = assetManager.getAssetsByExchange(exchangeEntity.getExchangeID());
        ArrayList<PriceEntity> vnIndexList = priceManager.getPriceByAssetID(assetManager.getAssetBySymbolAndExchange("^VNINDEX", "INDEX").getAssetID());

        output = new TreeMap<Double, ArrayList<Object>>(Collections.reverseOrder());
        {
            double percent = 0;
            DecimalFormat decimalf = new DecimalFormat("#.##");
            
            for (AssetEntity assetEntity : assetEntities) {
                percent += 100.0/assetEntities.size();
                
                completePercentage = decimalf.format(percent) + "%";
                
                ArrayList<PriceEntity> priceEntityList = priceManager.getPriceByAssetID(assetEntity.getAssetID());
                                
                if (priceEntityList.get(priceEntityList.size()-1).getDate().before(vnIndexList.get(vnIndexList.size()-1).getDate())) {
                    continue;
                }

                ArrayList<PriceEntity> priceEntitys = new ArrayList<PriceEntity>();
                for (int i = 0; i < priceEntityList.size() && priceEntityList.get(i).getDate().before(startPredictDate); ++i) {
                    priceEntitys.add(priceEntityList.get(i));
                }
                
                while (priceEntitys.size() > noTraining) {
                    priceEntitys.remove(0);
                }
                
                if (priceEntitys.size() < 100) {
                    continue;
                }                
                
                System.out.println(assetEntity.getSymbol());

                String date = "Date";
                String real = "Real";

                for (int i = -10; i < 0; ++i) {
                    date += ","
                            + priceEntitys.get(i + priceEntitys.size()).getDate();
                    real += ","
                            + priceEntitys.get(i + priceEntitys.size()).getClose();
                }

                ArrayList<Object> listOut = new ArrayList<Object>();
                listOut.add(assetEntity);
                listOut.add(priceEntityList);
                listOut.add(date);
                listOut.add(real);

                {                    
                    TreeMap<AssetEntity, ArrayList<PriceEntity>> map = new TreeMap<AssetEntity, ArrayList<PriceEntity>>();
                    map.put(assetEntity, priceEntitys);
                    preAlg.setPriceEntityList(map);
                    OutputForPredictionAlgorithm outputPre = preAlg.runAlgorithm();
                    
                    ArrayList<PriceEntry> prediction = outputPre.getPredictionPriceList().firstEntry().getValue();
                    double rate = new Double(prediction.get(prediction.size()-1).getPrice() / priceEntitys.get(priceEntitys.size() - 1).getClose() - 1);

                    String predict = "";
                    for (int j = 1; j < prediction.size(); ++j) {
                        predict += "," + prediction.get(j).getPrice();
                    }
                    listOut.add(predict);

                    System.out.println(rate);

                    output.put(rate, listOut);

                }

                completePercentage = "0.0%";
            }
        }

        for (Double roc : output.keySet()) {
            bufferedWriterResult.write(((AssetEntity)output.get(roc).get(0)).getSymbol() + "," + roc);
            bufferedWriterResult.newLine();
//            for (int i = 2; i < output.get(roc).size() - 2; ++i) {
//                bufferedWriterResult.write((String)output.get(roc).get(i));
//                bufferedWriterResult.newLine();
//            }
//            bufferedWriterResult.write((String)output.get(roc).get(output.get(roc).size() - 2));
//            bufferedWriterResult.write((String)output.get(roc).get(output.get(roc).size() - 1));
//            bufferedWriterResult.newLine();
        }

        bufferedWriterResult.close();
    }
    
    public void rankSharpeRatio() throws IOException, Exception {
        
        BufferedWriter bufferedWriterResult = new BufferedWriter(
                new FileWriter(new File(exchangeEntity.getName() + "-" + SHARPE_RATIO + "-" + startPredictDate + ".csv")));

        // choose testing asset and reference symbols
        AssetManager assetManager = new AssetManager();
        PriceManager priceManager = new PriceManager();

        if (exchangeEntity == null) {
            return;
        }
        
        ArrayList<AssetEntity> assetEntities = assetManager.getAssetsByExchange(exchangeEntity.getExchangeID());
        ArrayList<PriceEntity> vnIndexList = priceManager.getPriceByAssetID(assetManager.getAssetBySymbolAndExchange("^VNINDEX", "INDEX").getAssetID());

        output = new TreeMap<Double, ArrayList<Object>>(Collections.reverseOrder());
        {
            double percent = 0;
            DecimalFormat decimalf = new DecimalFormat("#.##");
            
            for (AssetEntity assetEntity : assetEntities) {
                percent += 100.0/assetEntities.size();
                
                completePercentage = decimalf.format(percent) + "%";
                
                ArrayList<PriceEntity> priceEntityList = priceManager.getPriceByAssetID(assetEntity.getAssetID());
                                
                if (priceEntityList.get(priceEntityList.size()-1).getDate().before(vnIndexList.get(vnIndexList.size()-1).getDate())) {
                    continue;
                }

                ArrayList<PriceEntity> priceEntitys = new ArrayList<PriceEntity>();
                for (int i = 0; i < priceEntityList.size() && priceEntityList.get(i).getDate().before(startPredictDate); ++i) {
                    priceEntitys.add(priceEntityList.get(i));
                }
                
                while (priceEntitys.size() > noSam+period) {
                    priceEntitys.remove(0);
                }
                                
                System.out.println(assetEntity.getSymbol());

                ArrayList<Object> listOut = new ArrayList<Object>();
                listOut.add(assetEntity);

                {   
                    ArrayList<Double> returns = new ArrayList<Double>();
                    for (int i = period; i < priceEntitys.size(); ++i) {
                        returns.add(priceEntitys.get(i).getClose()/priceEntitys.get(i-period).getClose()-1);
                    }
                    
                    double averageReturn = 0;
                    for (int i = 0; i < returns.size(); ++i) {
                        averageReturn += returns.get(i);
                    }
                    averageReturn /= returns.size();
                    
                    double standardDeviation = 0;
                    for (int i = 0; i < returns.size(); ++i) {
                        standardDeviation += (averageReturn-returns.get(i))*(averageReturn-returns.get(i));
                    }
                    standardDeviation /= returns.size()-1;
                    standardDeviation = Math.sqrt(standardDeviation);
                    
                    double sharpeRatio = (averageReturn-RISK_FREE_RETURN*period)/standardDeviation;
                    System.out.println(sharpeRatio);
                    output.put(sharpeRatio, listOut);
                }

                completePercentage = "0.0%";
            }
        }

        for (Double roc : output.keySet()) {
            bufferedWriterResult.write(((AssetEntity)output.get(roc).get(0)).getSymbol() + "," + roc);
            bufferedWriterResult.newLine();
//            for (int i = 2; i < output.get(roc).size() - 2; ++i) {
//                bufferedWriterResult.write((String)output.get(roc).get(i));
//                bufferedWriterResult.newLine();
//            }
//            bufferedWriterResult.write((String)output.get(roc).get(output.get(roc).size() - 2));
//            bufferedWriterResult.write((String)output.get(roc).get(output.get(roc).size() - 1));
//            bufferedWriterResult.newLine();
        }

        bufferedWriterResult.close();
    }
    
    public void rankPerIncOverRisk() throws IOException, Exception {
        
        BufferedWriter bufferedWriterResult = new BufferedWriter(
                new FileWriter(new File(exchangeEntity.getName() + "-" + PERINC_OVER_RISK + "-" +  startPredictDate + ".csv")));

        // choose testing asset and reference symbols
        AssetManager assetManager = new AssetManager();
        PriceManager priceManager = new PriceManager();

        if (exchangeEntity == null) {
            return;
        }
        
        ArrayList<AssetEntity> assetEntities = assetManager.getAssetsByExchange(exchangeEntity.getExchangeID());
        ArrayList<PriceEntity> vnIndexList = priceManager.getPriceByAssetID(assetManager.getAssetBySymbolAndExchange("^VNINDEX", "INDEX").getAssetID());

        output = new TreeMap<Double, ArrayList<Object>>(Collections.reverseOrder());
        {
            double percent = 0;
            DecimalFormat decimalf = new DecimalFormat("#.##");
            
            for (AssetEntity assetEntity : assetEntities) {
                percent += 100.0/assetEntities.size();
                
                completePercentage = decimalf.format(percent) + "%";
                
                ArrayList<PriceEntity> priceEntityList = priceManager.getPriceByAssetID(assetEntity.getAssetID());
                                
                if (priceEntityList.get(priceEntityList.size()-1).getDate().before(vnIndexList.get(vnIndexList.size()-1).getDate())) {
                    continue;
                }

                ArrayList<PriceEntity> priceEntitys = new ArrayList<PriceEntity>();
                for (int i = 0; i < priceEntityList.size() && priceEntityList.get(i).getDate().before(startPredictDate); ++i) {
                    priceEntitys.add(priceEntityList.get(i));
                }
                
                while (priceEntitys.size() > noTraining) {
                    priceEntitys.remove(0);
                }
                
                if (priceEntitys.size() < 100) {
                    continue;
                }                
                
                System.out.println(assetEntity.getSymbol());

//                String date = "Date";
//                String real = "Real";
//
//                for (int i = -10; i < 0; ++i) {
//                    date += ","
//                            + priceEntitys.get(i + priceEntitys.size()).getDate();
//                    real += ","
//                            + priceEntitys.get(i + priceEntitys.size()).getClose();
//                }

                ArrayList<Object> listOut = new ArrayList<Object>();
                listOut.add(assetEntity);
                //listOut.add(priceEntityList);
                //listOut.add(date);
                //listOut.add(real);

                {                    
                    TreeMap<AssetEntity, ArrayList<PriceEntity>> map = new TreeMap<AssetEntity, ArrayList<PriceEntity>>();
                    map.put(assetEntity, priceEntitys);
                    preAlg.setPriceEntityList(map);
                    OutputForPredictionAlgorithm outputPre = preAlg.runAlgorithm();
                    
                    ArrayList<PriceEntry> prediction = outputPre.getPredictionPriceList().firstEntry().getValue();
                    double rate = new Double(prediction.get(prediction.size()-1).getPrice() / priceEntitys.get(priceEntitys.size() - 1).getClose() - 1);

//                    String predict = "";
//                    for (int j = 1; j < prediction.size(); ++j) {
//                        predict += "," + prediction.get(j).getPrice();
//                    }
                    //listOut.add(predict);

                    while (priceEntitys.size() > noSam+period) {
                        priceEntitys.remove(0);
                    }                    
                    
                    ArrayList<Double> returns = new ArrayList<Double>();
                    for (int i = period; i < priceEntitys.size(); ++i) {
                        returns.add(priceEntitys.get(i).getClose()/priceEntitys.get(i-period).getClose()-1);
                    }
                    
                    double averageReturn = 0;
                    for (int i = 0; i < returns.size(); ++i) {
                        averageReturn += returns.get(i);
                    }
                    averageReturn /= returns.size();
                    
                    double standardDeviation = 0;
                    for (int i = 0; i < returns.size(); ++i) {
                        standardDeviation += (averageReturn-returns.get(i))*(averageReturn-returns.get(i));
                    }
                    standardDeviation /= returns.size()-1;
                    standardDeviation = Math.sqrt(standardDeviation);
                    
                    System.out.println(rate/standardDeviation);

                    output.put(rate/standardDeviation, listOut);

                }

                completePercentage = "0.0%";
            }
        }

        for (Double roc : output.keySet()) {
            bufferedWriterResult.write(((AssetEntity)output.get(roc).get(0)).getSymbol() + "," + roc);
            bufferedWriterResult.newLine();
//            for (int i = 2; i < output.get(roc).size() - 2; ++i) {
//                bufferedWriterResult.write((String)output.get(roc).get(i));
//                bufferedWriterResult.newLine();
//            }
//            bufferedWriterResult.write((String)output.get(roc).get(output.get(roc).size() - 2));
//            bufferedWriterResult.write((String)output.get(roc).get(output.get(roc).size() - 1));
//            bufferedWriterResult.newLine();
        }

        bufferedWriterResult.close();
    }

}

