/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.dataVisualization.chart;

import dataAccess.databaseManagement.entity.PriceEntity;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYDataset;

/**
 * 
 * @author Dinh
 */
public class CandleStickChart extends VisulizationChart {

	private OHLCDataset candleDataset;
	private XYDataset volumeDataset;

	@Override
	public void setPrices(ArrayList<PriceEntity> prices) {
		OHLCDataItem[] oHLCDataItems = new OHLCDataItem[prices.size()];
		TimeSeries volumeSeries = new TimeSeries("Volume Series");

		for (int i = 0; i < prices.size(); ++i) {
			double open = prices.get(i).getOpen();
			double high = prices.get(i).getHigh();
			double low = prices.get(i).getLow();
			if (open == 0) {
				open = prices.get(i).getClose();
			}
			if (high == 0) {
				high = open;
			}
			if (low == 0) {
				low = open;
			}
			oHLCDataItems[i] = new OHLCDataItem(prices.get(i).getDate(), open,
					high, low, prices.get(i).getClose(), prices.get(i)
							.getVolume());
			volumeSeries.add(new Day(prices.get(i).getDate()), prices.get(i)
					.getVolume());
		}

		candleDataset = new DefaultOHLCDataset("Price Series", oHLCDataItems);

		TimeSeriesCollection dataset = new TimeSeriesCollection(volumeSeries);
		volumeDataset = new XYBarDataset(dataset, 100);

	}

	@Override
	public void updateChart() {
		XYPlot plot = chart.getXYPlot();
		plot.setDataset(0, candleDataset);
		plot.setDataset(1, volumeDataset);
		plot.setDataset(2, predictionDataset);
		plot.setDataset(3, markPointsDataset);
	}

	@Override
	public void initalChart() {
		chart = ChartFactory.createCandlestickChart("", "Date", "Price", null,
				true);
		chart.setBackgroundPaint(Color.white);
		CandlestickRenderer candlestickRenderer = (CandlestickRenderer) chart
				.getXYPlot().getRenderer();
		candlestickRenderer.setDrawVolume(false);
		candlestickRenderer
				.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_SMALLEST);

		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.blue);
		plot.setDomainGridlinePaint(Color.blue);

		DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
		dateAxis.setPositiveArrowVisible(true);

		NumberAxis priceRangeAxis = (NumberAxis) plot.getRangeAxis();
		priceRangeAxis.setLowerMargin(0.5); // to leave room for volume bars
		priceRangeAxis.setAutoRangeIncludesZero(false);

		NumberAxis volumeRangeAxis = new NumberAxis("Volume");
		volumeRangeAxis.setUpperMargin(0.5); // to leave room for price line
                volumeRangeAxis.setLabelFont(priceRangeAxis.getLabelFont());
                volumeRangeAxis.setTickLabelFont(priceRangeAxis.getTickLabelFont());
		plot.setRangeAxis(1, volumeRangeAxis);
		plot.mapDatasetToRangeAxis(1, 1);
		XYBarRenderer volumeRenderer = new XYBarRenderer(0.20);
		volumeRenderer.setBarPainter(new StandardXYBarPainter());
		volumeRenderer.setShadowVisible(false);
		volumeRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat(
						"0,000.00")));
		volumeRenderer.setSeriesPaint(0, Color.GRAY);
		plot.setRenderer(1, volumeRenderer);

		// add prediction price series
		plot.mapDatasetToRangeAxis(2, 0);
		DeviationRenderer deviationRenderer = new DeviationRenderer(true, false);
		plot.setRenderer(2, deviationRenderer);

		// add mark points
		plot.mapDatasetToRangeAxis(3, 0);
		XYLineAndShapeRenderer xyLineAndShapeRenderer = new XYLineAndShapeRenderer();
		plot.setRenderer(3, xyLineAndShapeRenderer);
	}
}
