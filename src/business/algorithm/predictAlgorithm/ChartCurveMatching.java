package business.algorithm.predictAlgorithm;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class ChartCurveMatching extends AbstractPredictAlgorithm {

    private ArrayList<Double> parameterList = new ArrayList<Double>();
    private Integer interval;
    private Integer noSample;

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getNoSample() {
        return noSample;
    }

    public void setNoSample(Integer noSample) {
        this.noSample = noSample;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public TreeMap<String, Class> getParametersList() {
        // TODO Auto-generated method stub
        TreeMap<String, Class> map = super.getParametersList();
        map.put("Interval", Integer.class);
        map.put("NoSamples", Integer.class);
        return map;
    }
    
    @Override
    public TreeMap<String, Object> getDefaultValuesList() {
        // TODO Auto-generated method stub
        TreeMap<String, Object> map = super.getDefaultValuesList();
        map.put("Interval", new Integer(10));
        map.put("NoSamples", new Integer(1));
        return map;
    }    

    @Override
    public void setParametersValue(TreeMap<String, Object> map) {
        // TODO Auto-generated method stub
        super.setParametersValue(map);
        this.interval = (Integer) map.get("Interval");
        this.noSample = (Integer) map.get("NoSamples");
    }

    public ChartCurveMatching(
            TreeMap<AssetEntity, ArrayList<PriceEntity>> priceList,
            Integer futureInterval, Integer interval, Integer noSamples) {
        super(priceList, futureInterval);
        this.interval = interval;
        this.noSample = noSamples;
    }

    public ChartCurveMatching() {
        super(null, null);
        this.interval = null;
        this.noSample = null;
    }

    @Override
    public OutputForPredictionAlgorithm runAlgorithm() throws Exception {
        // TODO Auto-generated method stub
        AssetEntity asset = priceList.firstKey();
        ArrayList<PriceEntity> priceEntityList = priceList.get(priceList.firstKey());

        ArrayList<Double> predictionPriceList = new ArrayList<Double>();

        double[] result = predict(priceEntityList, priceEntityList.size(), futureInterval);
        for (int i = 0; i < result.length; ++i) {
            predictionPriceList.add(result[i]);
        }
        
        predictionPriceList.add(0, priceEntityList.get(priceEntityList.size() - 1).getClose());

        Date startPredictionDate = priceEntityList.get(
                priceEntityList.size() - 1).getDate();

        // startPredictionDate = new
        // java.sql.Date(utility.Utility.increaseDate(startPredictionDate).getTime());

        ArrayList<PriceEntry> priceEntryList = Utility.constructPriceList(
                asset, predictionPriceList, startPredictionDate);
        TreeMap<AssetEntity, ArrayList<PriceEntry>> predictionPriceMap = new TreeMap<AssetEntity, ArrayList<PriceEntry>>();
        predictionPriceMap.put(asset, priceEntryList);
        return new CommonOutputForPredictionAlgorithm(predictionPriceMap);
    }

    public double[] predict(ArrayList<PriceEntity> priceEntityList, int startPredictedDate, int noPredictedDates) {

        double[] results = new double[noPredictedDates];

        for (int i = startPredictedDate; i < startPredictedDate + noPredictedDates; ++i) {
            predictOneDay(priceEntityList, i, startPredictedDate, results);
        }

        return results;
    }

    private void predictOneDay(ArrayList<PriceEntity> priceEntities, int i, int startPredictedDate, double[] results) {
        ArrayList<Double> matchingObject = new ArrayList<Double>();

        for (int j = i - interval; j < startPredictedDate; ++j) {
            matchingObject.add(priceEntities.get(j).getClose());
        }

        for (int j = i - interval; matchingObject.size() < interval; ++j) {
            if (j < startPredictedDate) {
                continue;
            }
            if (results[j - startPredictedDate] == 0) {
                results[i - startPredictedDate] = 0;
            }
            matchingObject.add(results[j - startPredictedDate]);
        }

        ArrayList<Double> closePrices = normalize(matchingObject);

        ArrayList<ArrayList<Double>> theBestMatchings = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> theBestPredicts = new ArrayList<Double>();
        ArrayList<Double> mins = new ArrayList<Double>();

        for (int j = 0; j < noSample; ++j) {
            mins.add(Double.MAX_VALUE);
            theBestMatchings.add(new ArrayList<Double>());
            theBestPredicts.add(new Double(0));
        }

        for (int j = 0; j < startPredictedDate - interval; ++j) {
            ArrayList<Double> matchingResult = new ArrayList<Double>();
            for (int k = j; k < j + interval; ++k) {
                matchingResult.add(priceEntities.get(k).getClose());
            }

            ArrayList<Double> closePricesMatching = normalize(matchingResult);

            double distance = distance(closePrices, closePricesMatching);

            int insert;
            for (insert = 0; insert < noSample
                    && distance > mins.get(insert); ++insert)
					;

            if (insert < noSample) {
                for (int k = noSample - 1; k > insert; --k) {
                    mins.set(k, mins.get(k - 1));
                    theBestMatchings.set(k, theBestMatchings.get(k - 1));
                    theBestPredicts.set(k, theBestPredicts.get(k - 1));
                }

                mins.set(insert, distance);
                theBestMatchings.set(insert, matchingResult);
                theBestPredicts.set(insert, priceEntities.get(j + interval).getClose());
            }
        }

        double rateOfChange = 0;
        double total = 0;

        for (int j = 0; j < noSample; ++j) {
            if (theBestMatchings.get(j).isEmpty()) {
                break;
            }
            double rate = theBestPredicts.get(j) / theBestMatchings.get(j).get(theBestMatchings.get(j).size() - 1) - 1;
            rateOfChange += rate / mins.get(j);
            total += 1.0 / mins.get(j);
        }

        rateOfChange /= total;

        if (i - startPredictedDate == 0) {
            results[i - startPredictedDate] = (priceEntities.get(i - 1).getClose() + priceEntities.get(i - 1).getClose()
                    * rateOfChange);
        } else {
            results[i - startPredictedDate] = (results[i - startPredictedDate - 1] + results[i - startPredictedDate - 1] * rateOfChange);
        }
    }

    public static ArrayList<Double> normalize(ArrayList<Double> x) {
        ArrayList<Double> result = new ArrayList<Double>();

        for (int i = 0; i < x.size() - 1; ++i) {
            result.add((x.get(i + 1) - x.get(i)) / x.get(i));
        }


        return result;
    }

    public static double distance(ArrayList<Double> x1, ArrayList<Double> x2) {
        if (x1.size() != x2.size()) {
            return 0;
        }

        double result = 10e-5;

        for (int i = 0; i < x1.size(); ++i) {
            result += Math.pow(x1.get(i) - x2.get(i), 2);
        }

        return Math.sqrt(result);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Chart-Curve Matching";
    }

    public ArrayList<Double> getParameterList() {
        return parameterList;
    }

    public void setParameterList(ArrayList<Double> parameterList) {
        this.parameterList = parameterList;
    }
}
