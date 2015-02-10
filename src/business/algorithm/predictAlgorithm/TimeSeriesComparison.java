package business.algorithm.predictAlgorithm;

import java.util.ArrayList;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.AssetManager;
import dataAccess.databaseManagement.manager.PriceManager;

public class TimeSeriesComparison {
	
	public static double Norm2OfDifference(ArrayList<Double> series1, ArrayList<Double> series2)
	{
		ArrayList<Double> diff1 = Utility.firstDifference(series1);
		ArrayList<Double> diff2 = Utility.firstDifference(series2);
		ArrayList<Double> diff = Utility.subtract(diff1, diff2);
		return Utility.norm2(diff);
	}
	
	public static double CosineOfDifference(ArrayList<Double> series1, ArrayList<Double> series2)
	{
		ArrayList<Double> diff1 = Utility.firstDifference(series1);
		ArrayList<Double> diff2 = Utility.firstDifference(series2);
		
		return Utility.cosine(diff1, diff2);
	}
	
	public static double CosineOfSeries(ArrayList<Double> series1, ArrayList<Double> series2)
	{
		return Utility.cosine(series1, series2);
	}
	
	public static double SMAandCosine(ArrayList<Double> series1, ArrayList<Double> series2, int MA_period)
	{
		ArrayList<Double> sma1 = Utility.SMA(series1, MA_period);
		ArrayList<Double> sma2 = Utility.SMA(series2, MA_period);
		return Utility.cosine(sma1, sma2);
	}
	
	public static double SMAandNorm2(ArrayList<Double> series1, ArrayList<Double> series2, int MA_period)
	{
		ArrayList<Double> sma1 = Utility.SMA(series1, MA_period);
		ArrayList<Double> sma2 = Utility.SMA(series2, MA_period);
		return Utility.norm2(Utility.subtract(sma1, sma2));
	}
	
	public static double EMAandNorm2(ArrayList<Double> series1, ArrayList<Double> series2, int MA_period)
	{
		ArrayList<Double> ema1 = Utility.EMA(series1, MA_period);
		ArrayList<Double> ema2 = Utility.EMA(series2, MA_period);
		return Utility.norm2(Utility.subtract(ema1, ema2));
	}
	
	public static double EMAandCosine(ArrayList<Double> series1, ArrayList<Double> series2, int MA_period)
	{
		ArrayList<Double> ema1 = Utility.EMA(series1, MA_period);
		ArrayList<Double> ema2 = Utility.EMA(series2, MA_period);
		return Utility.cosine(ema1, ema2);
	}
	
	public static double FEandNorm2(ArrayList<Double> series1, ArrayList<Double> series2)
	{
		ArrayList<Double> smooth1 = FiniteElement.finiteElement(series1);
		ArrayList<Double> smooth2 = FiniteElement.finiteElement(series2);
		return Utility.norm2(Utility.subtract(smooth1, smooth2));
	}
	
	public static double FEandCosine(ArrayList<Double> series1, ArrayList<Double> series2)
	{
		ArrayList<Double> smooth1 = FiniteElement.finiteElement(series1);
		ArrayList<Double> smooth2 = FiniteElement.finiteElement(series2);
		return Utility.cosine(smooth1, smooth2);
	} 
	
	public static double DiffofSMAandNorm2(ArrayList<Double> series1, ArrayList<Double> series2, int MA_period)
	{
		ArrayList<Double> smooth1 = Utility.SMA(series1, MA_period);
		ArrayList<Double> smooth2 = Utility.SMA(series1, MA_period);
		smooth1 = Utility.firstDifference(smooth1);
		smooth2 = Utility.firstDifference(smooth2);
		return Utility.norm2(Utility.subtract(smooth1, smooth2));
	}
	
	public static double LogofDiffandNorm2(ArrayList<Double> series1, ArrayList<Double> series2)
	{
		series1 = Utility.log(series1);
		series2 = Utility.log(series2);
		return Utility.norm2(Utility.subtract(series1, series2));
	}
	
	public static double SMAofDiffandNorm2(ArrayList<Double> series1, ArrayList<Double> series2, int MA_period)
	{
		series1 = Utility.SMA(Utility.firstDifference(series1), MA_period);
		series2 = Utility.SMA(Utility.firstDifference(series2), MA_period);
		return Utility.norm2(Utility.subtract(series1, series2));
	}
	
	public static double EMAofDiffandNorm2(ArrayList<Double> series1, ArrayList<Double> series2, int MA_period)
	{
		series1 = Utility.EMA(Utility.firstDifference(series1), MA_period);
		series2 = Utility.EMA(Utility.firstDifference(series2), MA_period);
		return Utility.norm2(Utility.subtract(series1, series2));
	}
	
	public static double FEofDiffandNorm2(ArrayList<Double> series1, ArrayList<Double> series2)
	{
		series1 = FiniteElement.finiteElement(Utility.firstDifference(series1));
		series2 = FiniteElement.finiteElement(Utility.firstDifference(series2));
		return Utility.norm2(Utility.subtract(series1, series2));
	}
	
	public static void main(String args[])
	{
		AssetManager assetManager = new AssetManager();
		PriceManager priceManager = new PriceManager();
		//AssetEntity vnIndexAsset = assetManager.getAssetBySymbolAndExchange("^VNINDEX","INDEX");
		AssetEntity vnIndexAsset = assetManager.getAssetBySymbolAndExchange("SAM","HOSE");
		ArrayList<PriceEntity> vnIndex = priceManager.getPriceByAssetID(vnIndexAsset.getAssetID());
		ArrayList<Double> priceList = Utility.convertPriceEntityListToPriceList(vnIndex);
		
		int n = 10;
		int MA_period = 3;
		
		ArrayList<Double> series2 = Utility.subList(priceList, priceList.size() - n, priceList.size() - 1);
		
		ArrayList<Double> score1 = new ArrayList<Double>();
		ArrayList<Double> score2 = new ArrayList<Double>();
		ArrayList<Double> score3 = new ArrayList<Double>();
		ArrayList<Double> score4 = new ArrayList<Double>();
//		ArrayList<Double> score5 = new ArrayList<Double>();
//		ArrayList<Double> score6 = new ArrayList<Double>();
//		ArrayList<Double> score7 = new ArrayList<Double>();
//		ArrayList<Double> score8 = new ArrayList<Double>();
//		ArrayList<Double> score9 = new ArrayList<Double>();
		
		for (int i = 0; i < priceList.size() - n; ++i)
		{
			ArrayList<Double> series1 = Utility.subList(priceList, i, i + n - 1); 
								
			score1.add(Norm2OfDifference(series1, series2));
			score2.add(SMAofDiffandNorm2(series1, series2, MA_period));
			score3.add(EMAofDiffandNorm2(series1, series2, MA_period));
			score4.add(FEofDiffandNorm2(series1, series2));
//			score2.add(CosineOfDifference(series1, series2));
//			score3.add(CosineOfSeries(series1, series2));
//			score4.add(SMAandCosine(series1, series2, MA_period));
//			score5.add(SMAandNorm2(series1, series2, MA_period));
//			score6.add(EMAandCosine(series1, series2, MA_period));
//			score7.add(EMAandNorm2(series1, series2, MA_period));
//			score8.add(FEandCosine(series1, series2));
//			score9.add(FEandNorm2(series1, series2));			
		}
		int index1 = Utility.minIndex(score1);
		ArrayList<Double> t1 = Utility.subList(priceList, index1, index1 + n - 1);
		int index2 = Utility.minIndex(score2);
		ArrayList<Double> t2 = Utility.subList(priceList, index2, index2 + n - 1);
		int index3 = Utility.minIndex(score3);
		ArrayList<Double> t3 = Utility.subList(priceList, index3, index3 + n - 1);
		int index4 = Utility.minIndex(score4);
		ArrayList<Double> t4 = Utility.subList(priceList, index4, index4 + n - 1);
//		int index2 = Utility.maxIndex(score2);
//		ArrayList<Double> t2 = Utility.subList(priceList, index2, index2 + n - 1);
//		int index3 = Utility.maxIndex(score3);
//		ArrayList<Double> t3 = Utility.subList(priceList, index3, index3 + n - 1);
//		int index4 = Utility.maxIndex(score4);
//		ArrayList<Double> t4 = Utility.subList(priceList, index4, index4 + n - 1);
//		int index5 = Utility.minIndex(score5);
//		ArrayList<Double> t5 = Utility.subList(priceList, index5, index5 + n - 1);
//		int index6 = Utility.maxIndex(score6);
//		ArrayList<Double> t6 = Utility.subList(priceList, index6, index6 + n - 1);
//		int index7 = Utility.minIndex(score7);
//		ArrayList<Double> t7 = Utility.subList(priceList, index7, index7 + n - 1);
//		int index8 = Utility.maxIndex(score8);
//		ArrayList<Double> t8 = Utility.subList(priceList, index8, index8 + n - 1);
//		int index9 = Utility.minIndex(score9);
//		ArrayList<Double> t9 = Utility.subList(priceList, index9, index9 + n - 1);
		
		System.out.println(series2.toString());
		System.out.println(t1.toString());
		System.out.println(t2.toString());
		System.out.println(t3.toString());
		System.out.println(t4.toString());
//		System.out.println(t5.toString());
//		System.out.println(t6.toString());
//		System.out.println(t7.toString());
//		System.out.println(t8.toString());
//		System.out.println(t9.toString());
	}
}
