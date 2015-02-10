package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import Jama.Matrix;
import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;

public class LagrangeFE extends AbstractPredictAlgorithm{

	private ArrayList<double[]> cubicTrialFunctions = new ArrayList<double[]>();
	private Matrix coMatrix1; // coefficient matrix of linear system: phi(0)=?;
								// phi'(0)=?;phi(1)=?; phi'(1)=?
	
	ArrayList<Double> parameterList = new ArrayList<Double>();
	
	public ArrayList<Double> getParameterList() {
		return parameterList;
	}

	public void setParameterList(ArrayList<Double> parameterList) {
		this.parameterList = parameterList;
	}

	
	public LagrangeFE() {
		super(null, null);
	}
	
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
	
	@Override
	public OutputForPredictionAlgorithm runAlgorithm() throws Exception {
		AssetEntity asset = priceList.firstKey();
		ArrayList<PriceEntity> priceEntityList = priceList.get(priceList
				.firstKey());
		
		/*
		 * Finite Elements Step
		 */
		ArrayList<Double> smootingCurve = finiteElementsStep();
		
		int n = smootingCurve.size();
		Matrix A = null;
		
		//Lagrange extrapolation function
		{
			double[] y = new double[n];
			double[][] x = new double[n][];
			for (int i = 0; i < n; ++i) {
				y[i] = smootingCurve.get(i);
				x[i] = new double[n];
				for (int j = 0; j < n; ++j) {
					x[i][j] = Math.pow(i, j);
				}
			}

			Matrix Y = new Matrix(y, 1);
			Y = Y.transpose();

			Matrix X = new Matrix(x);

			A = X.solve(Y);
		}
				
		parameterList.clear();
		for (int i = 0; i < A.getRowDimension(); ++i) {
			parameterList.add(A.get(i, 0));
		}

		
		ArrayList<Double> predictionPriceList = new ArrayList<Double>();
		
		//calculate predicted price
		for (int i = n; i < n + futureInterval; ++i) {
			double[] x = new double[n];
			for (int j = 0; j < n; ++j) {
				x[j] = Math.pow(i, j);
			}
			Matrix X = new Matrix(x, 1);
			Matrix dotProduct = X.times(A);
			predictionPriceList.add(dotProduct.get(0, 0));
		}
		
		predictionPriceList.add(0,
				priceEntityList.get(priceEntityList.size() - 1).getClose());
		
		Date startPredictionDate = priceEntityList.get(priceEntityList.size() - 1).getDate();
		
		ArrayList<PriceEntry> priceEntryList = Utility.constructPriceList(
				asset, predictionPriceList, startPredictionDate);
		TreeMap<AssetEntity, ArrayList<PriceEntry>> predictionPriceMap = new TreeMap<AssetEntity, ArrayList<PriceEntry>>();
		predictionPriceMap.put(asset, priceEntryList);
		return new CommonOutputForPredictionAlgorithm(predictionPriceMap);	
	}

        	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "LagrangeFE";
	}

}
