package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import JSci.maths.statistics.TDistribution;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;

public class AutoRegressionFE extends AbstractPredictAlgorithm {

	private ArrayList<double[]> cubicTrialFunctions = new ArrayList<double[]>();
	private Matrix coMatrix1; // coefficient matrix of linear system: phi(0)=?;
								// phi'(0)=?;phi(1)=?; phi'(1)=?
	private ArrayList<Double> parameterList = new ArrayList<Double>();

	// where phi() is a cubic functions, this is the linear system used for
	// finding phi()

	private void createTrialFunctions() {
		double[][] array = { { 1., 0., 0., 0. }, { 0., 1., 0., 0. },
				{ 1., 1., 1., 1. }, { 0., 1., 2., 3. } };
		coMatrix1 = new Matrix(array);
		Matrix b = new Matrix(4, 1);
		Matrix coTrialFunction;

		// find 4 trial cubic functions

		// first cubic
		b.set(0, 0, 1);
		b.set(1, 0, 0);
		b.set(2, 0, 0);
		b.set(3, 0, 0);
		coTrialFunction = coMatrix1.solve(b);
		cubicTrialFunctions.add(new double[4]);
		cubicTrialFunctions.get(0)[0] = coTrialFunction.get(0, 0);
		cubicTrialFunctions.get(0)[1] = coTrialFunction.get(1, 0);
		cubicTrialFunctions.get(0)[2] = coTrialFunction.get(2, 0);
		cubicTrialFunctions.get(0)[3] = coTrialFunction.get(3, 0);

		// second cubic
		b.set(0, 0, 0);
		b.set(1, 0, 1);
		b.set(2, 0, 0);
		b.set(3, 0, 0);
		coTrialFunction = coMatrix1.solve(b);
		cubicTrialFunctions.add(new double[4]);
		cubicTrialFunctions.get(1)[0] = coTrialFunction.get(0, 0);
		cubicTrialFunctions.get(1)[1] = coTrialFunction.get(1, 0);
		cubicTrialFunctions.get(1)[2] = coTrialFunction.get(2, 0);
		cubicTrialFunctions.get(1)[3] = coTrialFunction.get(3, 0);

		// third cubic
		b.set(0, 0, 0);
		b.set(1, 0, 0);
		b.set(2, 0, 1);
		b.set(3, 0, 0);
		coTrialFunction = coMatrix1.solve(b);
		cubicTrialFunctions.add(new double[4]);
		cubicTrialFunctions.get(2)[0] = coTrialFunction.get(0, 0);
		cubicTrialFunctions.get(2)[1] = coTrialFunction.get(1, 0);
		cubicTrialFunctions.get(2)[2] = coTrialFunction.get(2, 0);
		cubicTrialFunctions.get(2)[3] = coTrialFunction.get(3, 0);

		// fourth cubic
		b.set(0, 0, 0);
		b.set(1, 0, 0);
		b.set(2, 0, 0);
		b.set(3, 0, 1);
		coTrialFunction = coMatrix1.solve(b);
		cubicTrialFunctions.add(new double[4]);
		cubicTrialFunctions.get(3)[0] = coTrialFunction.get(0, 0);
		cubicTrialFunctions.get(3)[1] = coTrialFunction.get(1, 0);
		cubicTrialFunctions.get(3)[2] = coTrialFunction.get(2, 0);
		cubicTrialFunctions.get(3)[3] = coTrialFunction.get(3, 0);
	}

	public ArrayList<Double> finiteElementsStep() {
		createTrialFunctions();

		ArrayList<PriceEntity> priceEntityList = priceList.get(priceList.firstKey());

		ArrayList<Double> priceArrayList = Utility.convertPriceEntityListToPriceList(priceEntityList); // r(x)

		// approximate r'(x) (center)
		ArrayList<Double> firstDerivativeOfR = new ArrayList<Double>();
		firstDerivativeOfR.add(priceArrayList.get(1) - priceArrayList.get(0));
		for (int i = 1; i < priceArrayList.size() - 1; ++i) {
			firstDerivativeOfR.add((priceArrayList.get(i + 1) - priceArrayList
					.get(i - 1)) / 2);
		}
		firstDerivativeOfR.add(priceArrayList.get(priceArrayList.size() - 1)
				- priceArrayList.get(priceArrayList.size() - 2));

		// approximate -r''(x) (center)
		ArrayList<Double> secondDerivativeOfR = new ArrayList<Double>();
		secondDerivativeOfR.add(-firstDerivativeOfR.get(1)
				+ firstDerivativeOfR.get(0));
		for (int i = 1; i < firstDerivativeOfR.size() - 1; ++i) {
			secondDerivativeOfR
					.add((-firstDerivativeOfR.get(i + 1) + firstDerivativeOfR
							.get(i - 1)) / 2);
		}
		secondDerivativeOfR.add(-firstDerivativeOfR.get(firstDerivativeOfR
				.size() - 1)
				+ firstDerivativeOfR.get(firstDerivativeOfR.size() - 2));

		Matrix K = new Matrix(2 * secondDerivativeOfR.size(),
				2 * secondDerivativeOfR.size(), 0);
		Matrix F = new Matrix(2 * secondDerivativeOfR.size(), 1, 0);
		Matrix elementMatrix = new Matrix(4, 4);

		// calculate the element matrix
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				double[] a1 = cubicTrialFunctions.get(i);
				double[] a2 = cubicTrialFunctions.get(j);
				double value = a1[1]
						* a2[1]
						+ (a1[1] * a2[2] + a1[2] * a2[1])
						+ (a1[1] * a2[3] + (4. / 3.) * a1[2] * a2[2] + a1[3]
								* a2[1]) + (6. / 4.)
						* (a1[2] * a2[3] + a1[3] * a2[2]) + (9. / 5.) * a1[3]
						* a2[3];

				elementMatrix.set(i, j, value);
			}
		}

		// calculate K matrices
		for (int i = 0; i < secondDerivativeOfR.size() - 1; ++i) {
			for (int j = 0; j < 4; ++j) {
				for (int k = 0; k < 4; ++k) {
					K.set(i * 2 + j, i * 2 + k, K.get(i * 2 + j, i * 2 + k)
							+ elementMatrix.get(j, k));
				}
			}
		}

		//calculate F matrices
		for (int i = 1; i < secondDerivativeOfR.size() - 1; ++i) {
			double area1 = cubicTrialFunctions.get(2)[0]
					+ cubicTrialFunctions.get(2)[1] / 2
					+ cubicTrialFunctions.get(2)[2] / 3
					+ cubicTrialFunctions.get(2)[3] / 4;
			double area2 = cubicTrialFunctions.get(0)[0]
					+ cubicTrialFunctions.get(0)[1] / 2
					+ cubicTrialFunctions.get(0)[2] / 3
					+ cubicTrialFunctions.get(0)[3] / 4;
			double value = ((secondDerivativeOfR.get(i - 1) + secondDerivativeOfR.get(i))/2) * area1
					+ ((secondDerivativeOfR.get(i)+secondDerivativeOfR.get(i + 1))/2) * area2;
			F.set(2 * i, 0, value);

			area1 = cubicTrialFunctions.get(3)[0]
					+ cubicTrialFunctions.get(3)[1] / 2
					+ cubicTrialFunctions.get(3)[2] / 3
					+ cubicTrialFunctions.get(3)[3] / 4;
			area2 = cubicTrialFunctions.get(1)[0]
					+ cubicTrialFunctions.get(1)[1] / 2
					+ cubicTrialFunctions.get(1)[2] / 3
					+ cubicTrialFunctions.get(1)[3] / 4;
			value = ((secondDerivativeOfR.get(i - 1) + secondDerivativeOfR.get(i))/2) * area1
					+ ((secondDerivativeOfR.get(i)+secondDerivativeOfR.get(i + 1))/2) * area2;
			F.set(2 * i + 1, 0, value);
		}

		// boundary conditions
		for (int i = 0; i < secondDerivativeOfR.size() * 2; i++) {
			K.set(0, i, 0);
			K.set(secondDerivativeOfR.size() * 2 - 2, i, 0);
		}
		K.set(0, 0, 1);
		K.set(secondDerivativeOfR.size() * 2 - 2,
				secondDerivativeOfR.size() * 2 - 2, 1);

		//
		F.set(0, 0, priceArrayList.get(0));
		double value = ((secondDerivativeOfR.get(0) + secondDerivativeOfR.get(1))/2)
				* (cubicTrialFunctions.get(3)[0]
						+ cubicTrialFunctions.get(3)[1] / 2
						+ cubicTrialFunctions.get(3)[2] / 3 
						+ cubicTrialFunctions.get(3)[3] / 4);
		F.set(1, 0, value);
		F.set(secondDerivativeOfR.size() * 2 - 2, 0, priceArrayList.get(priceArrayList.size() - 1));
		value = ((secondDerivativeOfR.get(secondDerivativeOfR.size() - 2) + secondDerivativeOfR.get(secondDerivativeOfR.size() - 1))/2)
				* (cubicTrialFunctions.get(1)[0]
						+ cubicTrialFunctions.get(1)[1] / 2
						+ cubicTrialFunctions.get(1)[2] / 3 
						+ cubicTrialFunctions.get(1)[3] / 4);
		F.set(secondDerivativeOfR.size() * 2 - 1, 0, value);

		// solve KU=F
		Matrix U = K.solve(F);
		
		ArrayList<Double> smootingCurve = new ArrayList<Double>();

		// training price
		for (int i = 0; i < priceEntityList.size(); ++i) {
			smootingCurve.add(U.get(i * 2, 0));
		}

		return smootingCurve;
	}

	private Double confidenceLevel;
	private Integer AR_period;

	public Double getConfidenceLevel() {
		return confidenceLevel;
	}

	public void setConfidenceLevel(Double confidenceLevel) {
		this.confidenceLevel = confidenceLevel;
	}

	public int getAR_period() {
		return AR_period;
	}

	public void setAR_period(Integer aR_period) {
		AR_period = aR_period;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public TreeMap<String, Class> getParametersList() {
		// TODO Auto-generated method stub
		TreeMap<String, Class> map = super.getParametersList();
		map.put("Confidence level", Double.class);
		map.put("AR period", Integer.class);
		return map;
	}

	@Override
	public void setParametersValue(TreeMap<String, Object> map) {
		// TODO Auto-generated method stub
		super.setParametersValue(map);
		this.confidenceLevel = (Double) map.get("Confidence level");
		this.AR_period = (Integer) map.get("AR period");
	}

	public AutoRegressionFE(
			TreeMap<AssetEntity, ArrayList<PriceEntity>> priceList,
			Integer futureInterval, Double confidenceLevel, Integer MA_period,
			Integer AR_period) {
		super(priceList, futureInterval);
		this.confidenceLevel = confidenceLevel;
		this.AR_period = AR_period;
	}

	public AutoRegressionFE() {
		super(null, null);
		this.confidenceLevel = null;
		this.AR_period = null;
	}

	@Override
	public OutputForPredictionAlgorithm runAlgorithm() throws Exception {
		// TODO Auto-generated method stub
		AssetEntity asset = priceList.firstKey();
		ArrayList<PriceEntity> priceEntityList = priceList.get(priceList
				.firstKey());

		/*
		 * Finite Elements Step
		 */
		ArrayList<Double> smootingCurve = finiteElementsStep();

		/*
		 * Auto Regression step
		 */
		int nSmootingCurve = smootingCurve.size();

		if (nSmootingCurve - AR_period < 0)
			throw new Exception("There are too few samples");

		double[][] x = new double[nSmootingCurve - AR_period][AR_period + 1];
		double[] y = new double[nSmootingCurve - AR_period];
		for (int i = 0; i < (nSmootingCurve - AR_period); ++i) {
			for (int j = 0; j <= AR_period; ++j) {
				if (j != AR_period) {
					x[i][j] = smootingCurve.get(i + j);
				} else {
					x[i][j] = 1;
				}
			}
			y[i] = smootingCurve.get(i + AR_period);
		}
		Matrix matrixX = new Matrix(x);
		Matrix matrixY = new Matrix(y, nSmootingCurve - AR_period);
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
					.add(smootingCurve.get(smootingCurve.size() - i));
			parameterList.add(smootingCurve.get(smootingCurve.size() - i));
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
		
		Double variance = utility.Utility.variance(smootingCurve);
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
		return "Auto Regression (Finite Elements)";
	}

	public ArrayList<Double> getParameterList() {
		return parameterList;
	}

	public void setParameterList(ArrayList<Double> parameterList) {
		this.parameterList = parameterList;
	}

}
