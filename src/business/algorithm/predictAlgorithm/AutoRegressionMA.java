package business.algorithm.predictAlgorithm;

import java.util.Date;
import java.util.ArrayList;
import java.util.TreeMap;

import JSci.maths.statistics.TDistribution;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;

public class AutoRegressionMA extends AbstractPredictAlgorithm {

	private Double confidenceLevel;
	private Integer MA_period;
	private Integer AR_period;
	private ArrayList<Double> parameterList = new ArrayList<Double>();

	public Double getConfidenceLevel() {
		return confidenceLevel;
	}

	public void setConfidenceLevel(Double confidenceLevel) {
		this.confidenceLevel = confidenceLevel;
	}

	public Integer getMA_period() {
		return MA_period;
	}

	public void setMA_period(Integer mA_period) {
		MA_period = mA_period;
	}

	public int getAR_period() {
		return AR_period;
	}

	public void setAR_period(Integer aR_period) {
		AR_period = aR_period;
	}
	
	public ArrayList<Double> getParameterList() {
		return parameterList;
	}

	public void setParameterList(ArrayList<Double> parameterList) {
		this.parameterList = parameterList;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public TreeMap<String, Class> getParametersList() {
		TreeMap<String, Class> map = super.getParametersList();
		map.put("Confidence level", Double.class);
		map.put("MA period", Integer.class);
		map.put("AR period", Integer.class);
		return map;
	}

	@Override
	public void setParametersValue(TreeMap<String, Object> map) {
		super.setParametersValue(map);
		this.confidenceLevel = (Double) map.get("Confidence level");
		this.MA_period = (Integer) map.get("MA period");
		this.AR_period = (Integer) map.get("AR period");
	}

	public AutoRegressionMA(
			TreeMap<AssetEntity, ArrayList<PriceEntity>> priceList,
			Integer futureInterval, Double confidenceLevel, Integer MA_period,
			Integer AR_period) {
		super(priceList, futureInterval);
		this.confidenceLevel = confidenceLevel;
		this.MA_period = MA_period;
		this.AR_period = AR_period;
	}

	public AutoRegressionMA() {
		super(null, null);
		this.confidenceLevel = null;
		this.MA_period = null;
		this.AR_period = null;
	}

	@Override
	public OutputForPredictionAlgorithm runAlgorithm() throws Exception {
		// TODO Auto-generated method stub
		AssetEntity asset = priceList.firstKey();
		ArrayList<PriceEntity> priceEntityList = priceList.get(priceList
				.firstKey());
		ArrayList<Double> priceArrayList = Utility
				.convertPriceEntityListToPriceList(priceEntityList);

		/*
		 * Moving Average Step
		 */
		int nSample = priceArrayList.size();
		double alpha = 2.0/(MA_period + 1);
		ArrayList<Double> movingAverage = new ArrayList<Double>();
		movingAverage.add(priceArrayList.get(0));
		for (int i = 1; i < nSample; ++i) {
			movingAverage.add(alpha*priceArrayList.get(i) + (1-alpha)*movingAverage.get(movingAverage.size()-1));
		}

		/*
		 * Auto Regression step
		 */
		int nMovingAverage = movingAverage.size();

		if (nMovingAverage - AR_period < 0)
			throw new Exception("There are too few samples");

		double[][] x = new double[nMovingAverage - AR_period][AR_period + 1];
		double[] y = new double[nMovingAverage - AR_period];
		for (int i = 0; i < (nMovingAverage - AR_period); ++i) {
			for (int j = 0; j <= AR_period; ++j) {
				if (j != AR_period) {
					x[i][j] = movingAverage.get(i + j);
				} else {
					x[i][j] = 1;
				}
			}
			y[i] = movingAverage.get(i + AR_period);
		}
		Matrix matrixX = new Matrix(x);
		Matrix matrixY = new Matrix(y, nMovingAverage - AR_period);
		Matrix matrixB = matrixX.transpose();
		matrixB = matrixB.times(matrixX);

		//matrixB = pseudo inverse of matrixB
		{
			SingularValueDecomposition svdMatrixB = matrixB.svd();
			Matrix pseudoInverseSigma = svdMatrixB.getS();
			int sizePseudoInverseSigma = Math.min(pseudoInverseSigma.getColumnDimension(), pseudoInverseSigma.getRowDimension());
			for (int i = 0; i < sizePseudoInverseSigma; ++i) {
				pseudoInverseSigma.set(i, i, 1/pseudoInverseSigma.get(i, i));
			}
			matrixB = (svdMatrixB.getV().times(pseudoInverseSigma)).times(svdMatrixB.getU().transpose());			
		}
		
		matrixB = matrixB.times(matrixX.transpose());
		matrixB = matrixB.times(matrixY);

		parameterList.clear();
		for (int i = 0; i < matrixB.getRowDimension(); ++i) {
			parameterList.add(matrixB.get(i, 0));
		}		
		
		ArrayList<Double> predictionPriceList = new ArrayList<Double>();
		for (int i = AR_period; i > 0; --i) {
			predictionPriceList
					.add(movingAverage.get(movingAverage.size() - i));
			parameterList.add(movingAverage.get(movingAverage.size() - i));
		}

		for (int i = 0; i < futureInterval; ++i) {
			Double temp = 0.0;
			for (int j = AR_period; j >= 0; --j) {
				if (j != 0) {
					temp += predictionPriceList.get(predictionPriceList.size()
							- j)
							* matrixB.get(AR_period - j, 0);
				} else {
					temp += matrixB.get(AR_period, 0);
				}
			}
			predictionPriceList.add(temp);
		}

		for (int i = AR_period; i > 0; --i) {
			predictionPriceList.remove(0);
		}

		predictionPriceList.add(0,
				priceEntityList.get(priceEntityList.size() - 1).getClose());

		Matrix matrixC = matrixX.transpose().times(matrixX);
		
		//matrixC = pseudo inverse of matrixC
		{
			SingularValueDecomposition svdMatrixC = matrixC.svd();
			Matrix pseudoInverseSigma = svdMatrixC.getS();
			int sizePseudoInverseSigma = Math.min(pseudoInverseSigma.getColumnDimension(), pseudoInverseSigma.getRowDimension());
			for (int i = 0; i < sizePseudoInverseSigma; ++i) {
				pseudoInverseSigma.set(i, i, 1/pseudoInverseSigma.get(i, i));
			}
			matrixC = (svdMatrixC.getV().times(pseudoInverseSigma)).times(svdMatrixC.getU().transpose());			
		}		
		
		Double variance = utility.Utility.variance(movingAverage);
		Double s_b0 = Math.sqrt(variance * matrixC.get(0, 0));
		Double t;
		TDistribution tDistribution = new TDistribution(AR_period - 1);
		t = tDistribution.cumulative(1 - confidenceLevel);

		Double lambda = t * s_b0;

		/*
		 * TreeMap<AssetEntity, ArrayList<Double>> predictionPriceMap = new
		 * TreeMap<AssetEntity, ArrayList<Double>>();
		 * predictionPriceMap.put(asset, predictionPriceList); return new
		 * OutputForAutoRegression(predictionPriceMap, startPredictionDate,
		 * lambda);
		 */

		Date startPredictionDate = priceEntityList.get(
				priceEntityList.size() - 1).getDate();
		// startPredictionDate = new
		// java.sql.Date(utility.Utility.increaseDate(startPredictionDate).getTime());
		ArrayList<PriceEntry> priceEntryList = Utility.constructPriceList(
				asset, predictionPriceList, startPredictionDate);
		TreeMap<AssetEntity, ArrayList<PriceEntry>> predictionPriceMap = new TreeMap<AssetEntity, ArrayList<PriceEntry>>();
		predictionPriceMap.put(asset, priceEntryList);
		return new OutputForAutoRegression(predictionPriceMap, lambda);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Auto Regression (Moving Average)";
	}

	// need to revise here
	// public Date setStartPredictionDate(Date lastDate) {
	// return null;
	// }

	// public static void main(String args[]) {
	// ArrayList<Double> priceList = new ArrayList<Double>();
	// priceList.add(23.0);
	// priceList.add(23.1);
	// priceList.add(24.0);
	// priceList.add(25.2);
	// priceList.add(24.8);
	// priceList.add(24.9);
	// priceList.add(24.7);
	// priceList.add(24.4);
	// priceList.add(24.4);
	// priceList.add(24.4);
	// int futureInterval = 10;
	// Double confidence_level = 0.9;
	// int MA_period = 3;
	// int AR_period = 3;
	// TreeMap<String, Object> map = new TreeMap<String, Object>();
	// map.put("Future interval", futureInterval);
	// map.put("Confidence level", confidence_level);
	// map.put("MA period", MA_period);
	// map.put("AR period", AR_period);
	// // AutoRegression ar = new AutoRegression();
	// /*
	// * ar.setParametersValue(map); ar.setPriceList(priceList);
	// */
	// try {
	//
	// /*
	// * ParamList output = ar.runAlgorithm(); for (int i = 0; i <
	// * futureInterval; ++i) { OutputForAutoRegression temp =
	// * (OutputForAutoRegression) output;
	// * System.out.println(temp.getPredictionPrice().get(i)); }
	// */
	//
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
}
