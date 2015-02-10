/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package business.dataVisualization.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;

import business.algorithm.decisionAlgorithm.AbstractDecisionAlgorithm;
import business.algorithm.decisionAlgorithm.Order;
import business.algorithm.predictAlgorithm.AbstractPredictAlgorithm;
import business.algorithm.predictAlgorithm.PriceEntry;
import dataAccess.databaseManagement.entity.PriceEntity;

/**
 * 
 * @author Dinh
 */
public abstract class VisulizationChart {
	public static final ChartStyle[] CHART_STYLES = {
			new ChartStyle(LineChart.class),
			new ChartStyle(CandleStickChart.class) };

	protected static final ArrayList<Color> predictingLineColors = new ArrayList<Color>();
	protected JFreeChart chart;
	protected XYDataset predictionDataset = new YIntervalSeriesCollection(),
			markPointsDataset = new TimeSeriesCollection();
	private HashMap<Object, Integer> mappingOrderSeries = new HashMap<Object, Integer>();
	private HashMap<Object, Integer> mappingPredictionPriceSeries = new HashMap<Object, Integer>();

	public void addOrders(AbstractDecisionAlgorithm decAlgo,
			ArrayList<Order> orders) {

		// Utility.debug(orders.size());

		TimeSeries buySeries = new TimeSeries("Buy Signals - "
				+ decAlgo.toString());
		TimeSeries sellSeries = new TimeSeries("Sell Signals - "
				+ decAlgo.toString());

		for (Order order : orders) {
			if (order.isOrderType() == Order.ORDER_BUY) {
				buySeries.add(new Day(order.getDate()), order.getPrice());
			} else {
				sellSeries.add(new Day(order.getDate()), order.getPrice());
			}
		}

		mappingOrderSeries.put(decAlgo, markPointsDataset.getSeriesCount());
		((TimeSeriesCollection) markPointsDataset).addSeries(buySeries);
		((TimeSeriesCollection) markPointsDataset).addSeries(sellSeries);

		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer xYLineAndShapeRenderer = (XYLineAndShapeRenderer) plot
				.getRenderer(3);
		xYLineAndShapeRenderer.setSeriesLinesVisible(
				markPointsDataset.getSeriesCount() - 1, false);
		xYLineAndShapeRenderer.setSeriesLinesVisible(
				markPointsDataset.getSeriesCount() - 2, false);
	}

	public void addPredictionPrices(AbstractPredictAlgorithm algo,
			ArrayList<PriceEntry> output) {

		YIntervalSeries yintervalseries = new YIntervalSeries("Predict - "
				+ algo.toString());

		for (PriceEntry entry : output) {
			yintervalseries.add(entry.getDate().getTime(), entry.getPrice(),
					entry.getPrice(), entry.getPrice());
		}

		mappingPredictionPriceSeries.put(algo,
				predictionDataset.getSeriesCount());
		((YIntervalSeriesCollection) predictionDataset)
				.addSeries(yintervalseries);

		XYPlot plot = chart.getXYPlot();
		DeviationRenderer deviationRenderer = (DeviationRenderer) plot
				.getRenderer(2);
		deviationRenderer.setSeriesStroke(
				predictionDataset.getSeriesCount() - 1, new BasicStroke(3F, 1,
						1));

		Random random = new Random(Calendar.getInstance().getTimeInMillis());
		while (predictingLineColors.size() < predictionDataset.getSeriesCount()) {
			Color color = new Color(random.nextInt(256), random.nextInt(256),
					random.nextInt(256));
			while (predictingLineColors.contains(color)) {
				color = new Color(random.nextInt(256), random.nextInt(256),
						random.nextInt(256));
			}
			predictingLineColors.add(color);
		}

		deviationRenderer.setSeriesFillPaint(
				predictionDataset.getSeriesCount() - 1, predictingLineColors
						.get(predictionDataset.getSeriesCount() - 1));
		deviationRenderer.setSeriesPaint(
				predictionDataset.getSeriesCount() - 1, predictingLineColors
						.get(predictionDataset.getSeriesCount() - 1));
	}

	public void removeOrder(AbstractDecisionAlgorithm decAlgo) {
		if (mappingOrderSeries.get(decAlgo) == null) {
			return;
		}

		((TimeSeriesCollection) markPointsDataset)
				.removeSeries(mappingOrderSeries.get(decAlgo));
		((TimeSeriesCollection) markPointsDataset)
				.removeSeries(mappingOrderSeries.get(decAlgo));

		mappingOrderSeries.remove(decAlgo);
	}

	public void removeAllOrders() {
		((TimeSeriesCollection) markPointsDataset).removeAllSeries();
		mappingOrderSeries.clear();
	}

	public void removePredictionPrice(
			AbstractPredictAlgorithm abstractPredictAlgorithm) {
		if (mappingPredictionPriceSeries.get(abstractPredictAlgorithm) == null) {
			return;
		}

		((YIntervalSeriesCollection) predictionDataset)
				.removeSeries(mappingPredictionPriceSeries
						.get(abstractPredictAlgorithm));

		mappingPredictionPriceSeries.remove(abstractPredictAlgorithm);
	}

	public void removeAllPredictionPrice() {
		((YIntervalSeriesCollection) predictionDataset).removeAllSeries();
		mappingPredictionPriceSeries.clear();
	}

	public JFreeChart getChart() {
		return chart;
	};

	public abstract void setPrices(ArrayList<PriceEntity> prices);

	public abstract void initalChart();

	public abstract void updateChart();
}
