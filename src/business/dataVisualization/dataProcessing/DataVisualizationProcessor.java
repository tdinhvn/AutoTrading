/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.dataVisualization.dataProcessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeMap;

import org.jfree.chart.JFreeChart;

import business.algorithm.decisionAlgorithm.AbstractDecisionAlgorithm;
import business.algorithm.predictAlgorithm.AbstractPredictAlgorithm;
import business.dataVisualization.chart.ChartStyle;
import business.dataVisualization.chart.VisulizationChart;
import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.PriceManager;
import java.util.Calendar;

/**
 * 
 * @author Dinh
 */
public class DataVisualizationProcessor {

    private static PriceManager priceManager = new PriceManager();
    private AssetEntity asset;
    private Date fromDate;
    private Date toDate;
    private Date startPredictionDate;
    private int trainingSamplesNumber;
    private ArrayList<AbstractPredictAlgorithm> preAlgList = new ArrayList<AbstractPredictAlgorithm>();
    private ArrayList<AbstractDecisionAlgorithm> decAlgList = new ArrayList<AbstractDecisionAlgorithm>();
    private VisulizationChart visualizationChart;
    private ArrayList<PriceEntity> prices;

    public DataVisualizationProcessor(AssetEntity asset, Date fromDate,
            Date toDate, ChartStyle chartStyle) {
        try {
            this.asset = asset;
            this.fromDate = fromDate;
            this.toDate = toDate;

            updatePricesList();

            visualizationChart = (VisulizationChart) chartStyle.getChartClass().newInstance();
            visualizationChart.initalChart();
            visualizationChart.setPrices(prices);
            visualizationChart.updateChart();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    private void updatePricesList() {
        prices = priceManager.getPriceInInterval(asset.getAssetID(),
                new java.sql.Date(fromDate.getTime()),
                new java.sql.Date(toDate.getTime()));
        Collections.sort(prices, new Comparator<PriceEntity>() {

            @Override
            public int compare(PriceEntity o1, PriceEntity o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }

    public void updateChartData() {
        updatePricesList();
        visualizationChart.setPrices(prices);

        // update prediction algorithms
        {

            visualizationChart.removeAllPredictionPrice();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startPredictionDate);
            calendar.add(Calendar.DATE, -1);
            java.sql.Date endTrainingDate = new java.sql.Date(calendar.getTimeInMillis());

            calendar.add(Calendar.DATE, -trainingSamplesNumber);
            java.sql.Date startTrainingDate = new java.sql.Date(calendar.getTimeInMillis());

            ArrayList<PriceEntity> priceEntityList = priceManager.getPriceInInterval(asset.getAssetID(), startTrainingDate, endTrainingDate);

            TreeMap<AssetEntity, ArrayList<PriceEntity>> map = new TreeMap<AssetEntity, ArrayList<PriceEntity>>();
            map.put(asset, priceEntityList);

            // add new results of Algorithms
            for (AbstractPredictAlgorithm preAlgo : preAlgList) {
                preAlgo.setPriceEntityList(map);
                try {
                    visualizationChart.addPredictionPrices(preAlgo, preAlgo.runAlgorithm().getPredictionPriceList().get(asset));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        // update decision algorithms
        {
            visualizationChart.removeAllOrders();

            TreeMap<AssetEntity, ArrayList<PriceEntity>> map = new TreeMap<AssetEntity, ArrayList<PriceEntity>>();
            map.put(asset, prices);

            for (AbstractDecisionAlgorithm decAlgo : decAlgList) {
                decAlgo.setPriceList(map);
                visualizationChart.addOrders(decAlgo, decAlgo.runAlgorithm().getOrderList());
            }

        }

        visualizationChart.updateChart();
    }

    public void changeChartType(ChartStyle chartStyle) {
        try {
            visualizationChart = (VisulizationChart) chartStyle.getChartClass().newInstance();
            visualizationChart.initalChart();
            this.updateChartData();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void addDecAlg(AbstractDecisionAlgorithm abstractDecisionAlgorithm) {
        decAlgList.add(abstractDecisionAlgorithm);

        TreeMap<AssetEntity, ArrayList<PriceEntity>> map = new TreeMap<AssetEntity, ArrayList<PriceEntity>>();
        map.put(asset, prices);

        abstractDecisionAlgorithm.setPriceList(map);

        visualizationChart.addOrders(abstractDecisionAlgorithm,
                abstractDecisionAlgorithm.runAlgorithm().getOrderList());

        visualizationChart.updateChart();
    }

    public void removeDecAlg(AbstractDecisionAlgorithm abstractDecisionAlgorithm) {
        decAlgList.remove(abstractDecisionAlgorithm);
        visualizationChart.removeOrder(abstractDecisionAlgorithm);
        visualizationChart.updateChart();
    }

    public void removeAllDecAlg() {
        decAlgList.clear();
        visualizationChart.removeAllOrders();
        visualizationChart.updateChart();
    }

    public void addPreAlg(AbstractPredictAlgorithm abstractPredictAlgorithm) {
        preAlgList.add(abstractPredictAlgorithm);
        TreeMap<AssetEntity, ArrayList<PriceEntity>> map = new TreeMap<AssetEntity, ArrayList<PriceEntity>>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startPredictionDate);
        calendar.add(Calendar.DATE, -1);
        java.sql.Date endTrainingDate = new java.sql.Date(calendar.getTimeInMillis());

        calendar.add(Calendar.DATE, -trainingSamplesNumber);
        java.sql.Date startTrainingDate = new java.sql.Date(calendar.getTimeInMillis());

        ArrayList<PriceEntity> priceEntityList = priceManager.getPriceInInterval(asset.getAssetID(), startTrainingDate, endTrainingDate);
        System.out.println(priceEntityList.size());
        map.put(asset, priceEntityList);
        abstractPredictAlgorithm.setPriceEntityList(map);
        try {
            visualizationChart.addPredictionPrices(abstractPredictAlgorithm,
                    abstractPredictAlgorithm.runAlgorithm().getPredictionPriceList().get(asset));
        } catch (Exception e) {
            e.printStackTrace();
        }
        visualizationChart.updateChart();
    }

    public void removePreAlg(AbstractPredictAlgorithm abstractPredictAlgorithm) {
        preAlgList.remove(abstractPredictAlgorithm);
        visualizationChart.removePredictionPrice(abstractPredictAlgorithm);
        visualizationChart.updateChart();
    }

    public void removeAllPreAlg() {
        preAlgList.clear();
        visualizationChart.removeAllPredictionPrice();
        visualizationChart.updateChart();
    }

    public ArrayList<AbstractDecisionAlgorithm> getDecAlgList() {
        return decAlgList;
    }

    public void setDecAlgList(ArrayList<AbstractDecisionAlgorithm> decAlg) {
        this.decAlgList = decAlg;
    }

    public ArrayList<AbstractPredictAlgorithm> getPreAlgList() {
        return preAlgList;
    }

    public void setPreAlgList(ArrayList<AbstractPredictAlgorithm> preAlg) {
        this.preAlgList = preAlg;
    }

    public AssetEntity getAsset() {
        return asset;
    }

    public void setAsset(AssetEntity asset) {
        this.asset = asset;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getStartPredictionDate() {
        return startPredictionDate;
    }

    public void setStartPredictionDate(Date startPredictionDate) {
        this.startPredictionDate = startPredictionDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public JFreeChart getChart() {
        return visualizationChart.getChart();
    }

    public int getTrainingSamplesNumber() {
        return trainingSamplesNumber;
    }

    public void setTrainingSamplesNumber(int trainingSamplesNumber) {
        this.trainingSamplesNumber = trainingSamplesNumber;
    }
}
