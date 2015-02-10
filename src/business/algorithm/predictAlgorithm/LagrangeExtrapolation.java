package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import Jama.Matrix;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;

public class LagrangeExtrapolation extends AbstractPredictAlgorithm{

	ArrayList<Double> parameterList = new ArrayList<Double>();
	
	public ArrayList<Double> getParameterList() {
		return parameterList;
	}

	public void setParameterList(ArrayList<Double> parameterList) {
		this.parameterList = parameterList;
	}

	public LagrangeExtrapolation() {
		super(null, null);
	}
	
	@Override
	public OutputForPredictionAlgorithm runAlgorithm() throws Exception {
		AssetEntity asset = priceList.firstKey();
		ArrayList<PriceEntity> priceEntityList = priceList.get(priceList
				.firstKey());
		
		
		int n = priceEntityList.size();
		Matrix A = null;
		
		//Lagrange extrapolation function
		{
			double[] y = new double[n];
			double[][] x = new double[n][];
			for (int i = 0; i < n; ++i) {
				y[i] = priceEntityList.get(i).getClose();
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
		return "Lagrange";
	}

}
