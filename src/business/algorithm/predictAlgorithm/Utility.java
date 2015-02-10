package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.Date;


import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.PriceManager;

public class Utility {
	public static final PriceManager priceManager = new PriceManager();
	
	public static ArrayList<PriceEntry> constructPriceList(AssetEntity asset,
			ArrayList<Double> priceList, Date startPredictingDate) {
		ArrayList<PriceEntity> priceEntityList = priceManager
				.getPriceInInterval(asset.getAssetID(), new java.sql.Date(
						startPredictingDate.getTime()), new java.sql.Date(
						priceManager.getLatestDate().getTime()));

		ArrayList<PriceEntry> priceEntryList = new ArrayList<PriceEntry>();
		ArrayList<Date> dateList = new ArrayList<Date>();
		for (PriceEntity priceEntity : priceEntityList) {
			dateList.add(priceEntity.getDate());
		}

		if (dateList.size() == 0)
			dateList.add(startPredictingDate);

		while (dateList.size() < priceList.size())
			dateList.add(utility.Utility.increaseDate(dateList.get(dateList
					.size() - 1)));
		
		int i = 0;
		for (double d : priceList) {
			priceEntryList.add(new PriceEntry(dateList.get(i), d));
			i++;
		}
		return priceEntryList;
		
//		ArrayList<PriceEntry> priceEntryList = new ArrayList<PriceEntry>();		
//		for (double d : priceList) {
//			priceEntryList.add(new PriceEntry(null, d));
//		}
//		return priceEntryList;		
	}

	public static ArrayList<Double> convertPriceEntityListToPriceList(
			ArrayList<PriceEntity> priceEntityList) {
		ArrayList<Double> priceList = new ArrayList<Double>();
		for (PriceEntity entity : priceEntityList) {
			priceList.add(entity.getClose());
		}
		return priceList;
	}
	
	public static ArrayList<Double> log(ArrayList<Double> series)
	{
		ArrayList<Double> result = new ArrayList<Double>();
		for (Double d : series)
			result.add(Math.log(d));
		return result;
	}
	
	public static double norm2(ArrayList<Double> list)
	{
		double result = 0;
		for (Double d : list)
		{
			result += d * d;
		}
		result = Math.sqrt(result);
		return result;
	}
	
	public static double dotProduct(ArrayList<Double> vector1, ArrayList<Double> vector2)
	{
		double result = 0;
		for (int i = 0; i < vector1.size(); ++i)
		{
			result += vector1.get(i) + vector2.get(i);
		}
		return result;
	}
	
	public static double cosine(ArrayList<Double> vector1, ArrayList<Double> vector2)
	{
		return dotProduct(vector1, vector2) / (norm2(vector1) * norm2(vector2));
	}
	
	public static ArrayList<Double> subtract(ArrayList<Double> vector1, ArrayList<Double> vector2)
	{
		ArrayList<Double> result = new ArrayList<Double>();
		for (int i = 0; i < vector1.size(); ++i)
		{
			result.add(vector1.get(i) - vector2.get(i));
		}
		return result;
	}
	
	public static ArrayList<Double> firstDifference(ArrayList<Double> vector)
	{
		ArrayList<Double> result = new ArrayList<Double>();
		for (int i = 0; i < vector.size() - 1; ++i)
			result.add(vector.get(i+1)/vector.get(i) - 1);
		return result;
	}
	
	public static ArrayList<Double> EMA(ArrayList<Double> series, int MA_period)
	{
		/*
		* Moving Average Step
		*/
		int nSample = series.size();
		double alpha = 2.0/(MA_period + 1);
		ArrayList<Double> movingAverage = new ArrayList<Double>();
		movingAverage.add(series.get(0));
		for (int i = 1; i < nSample; ++i)
			movingAverage.add(alpha*series.get(i) + (1-alpha)*movingAverage.get(movingAverage.size()-1));
		return movingAverage;
	}
	
	public static ArrayList<Double> SMA(ArrayList<Double> series, int MA_period)
	{
		ArrayList<Double> result = new ArrayList<Double>();
		for (int i = 0; i < series.size() - MA_period + 1; ++i)
		{
			double temp = 0;
			for (int j = i; j < (i + MA_period); ++j)
			{
				temp += series.get(j);
			}
			temp = temp / MA_period;
			result.add(temp);
		}
		return result;
	}
	
	public static int maxIndex(ArrayList<Double> series)
	{
		double max = series.get(0);
		int index = 0;
		for (int i = 1; i < series.size(); ++i)
		{
			if (series.get(i) > max)
			{
				max = series.get(i);
				index = i;
			}
		}
		return index;
	}
	
	public static int minIndex(ArrayList<Double> series)
	{
		double min = series.get(0);
		int index = 0;
		for (int i = 1; i < series.size(); ++i)
		{
			if (series.get(i) < min)
			{
				min = series.get(i);
				index = i;
			}
		}
		return index;
	}
	
	public static ArrayList<Double> subList(ArrayList<Double> series, int fromIndex, int toIndex)
	{
		ArrayList<Double> result = new ArrayList<Double>();
		for (int i = fromIndex; i <= toIndex; ++i)
		{
			result.add(series.get(i));
		}
		return result;
	}
}
