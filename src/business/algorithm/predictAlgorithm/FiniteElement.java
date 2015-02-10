package business.algorithm.predictAlgorithm;

import java.util.ArrayList;

import Jama.Matrix;

public class FiniteElement {
	private static ArrayList<double[]> cubicTrialFunctions = new ArrayList<double[]>();
	private static Matrix coMatrix1; // coefficient matrix of linear system: phi(0)=?;
								// phi'(0)=?;phi(1)=?; phi'(1)=?
	
	private static void createTrialFunctions() {
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
	
	public static ArrayList<Double> finiteElement(ArrayList<Double> series)
	{
		createTrialFunctions();
		
		// approximate r'(x) (center)
		ArrayList<Double> firstDerivativeOfR = new ArrayList<Double>();
		firstDerivativeOfR.add(series.get(1) - series.get(0));
		for (int i = 1; i < series.size() - 1; ++i) {
			firstDerivativeOfR.add((series.get(i + 1) - series.get(i - 1)) / 2);
		}
		firstDerivativeOfR.add(series.get(series.size() - 1) - series.get(series.size() - 2));

		// approximate -r''(x) (center)
		ArrayList<Double> secondDerivativeOfR = new ArrayList<Double>();
		secondDerivativeOfR.add(-firstDerivativeOfR.get(1) + firstDerivativeOfR.get(0));
		for (int i = 1; i < firstDerivativeOfR.size() - 1; ++i) {
			secondDerivativeOfR.add((-firstDerivativeOfR.get(i + 1) + firstDerivativeOfR.get(i - 1)) / 2);
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
		F.set(0, 0, series.get(0));
		double value = ((secondDerivativeOfR.get(0) + secondDerivativeOfR.get(1))/2)
				* (cubicTrialFunctions.get(3)[0]
						+ cubicTrialFunctions.get(3)[1] / 2
						+ cubicTrialFunctions.get(3)[2] / 3 
						+ cubicTrialFunctions.get(3)[3] / 4);
		F.set(1, 0, value);
		F.set(secondDerivativeOfR.size() * 2 - 2, 0, series.get(series.size() - 1));
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
		for (int i = 0; i < series.size(); ++i) {
			smootingCurve.add(U.get(i * 2, 0));
		}

		return smootingCurve;
	}
}
