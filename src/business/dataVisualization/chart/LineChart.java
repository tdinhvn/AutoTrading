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
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYDataset;

/**
 * 
 * @author Dinh
 */
public class LineChart extends VisulizationChart {

	private XYDataset priceDataset, volumeDataset;

	@Override
	public void setPrices(ArrayList<PriceEntity> prices) {
		TimeSeries priceSeries = new TimeSeries("Price Series");
		TimeSeries volumeSeries = new TimeSeries("Volume Series");

		for (PriceEntity price : prices) {
			priceSeries.add(new Day(price.getDate()), price.getClose());
			volumeSeries.add(new Day(price.getDate()), price.getVolume());
		}

		TimeSeriesCollection dataset = new TimeSeriesCollection(priceSeries);
		priceDataset = dataset;

		dataset = new TimeSeriesCollection(volumeSeries);
		volumeDataset = new XYBarDataset(dataset, 100);

	}

	@Override
	public void updateChart() {
		XYPlot plot = chart.getXYPlot();
		plot.setDataset(0, priceDataset);
		plot.setDataset(1, volumeDataset);
		plot.setDataset(2, predictionDataset);
		plot.setDataset(3, markPointsDataset);
	}

	@Override
	public void initalChart() {
		chart = ChartFactory.createTimeSeriesChart("", "Date", "Price", null,
				true, true, false);
		// chart.getXYPlot().getRangeAxis().setLowerBound(0);
		chart.setBackgroundPaint(Color.white);
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.blue);
		plot.setDomainGridlinePaint(Color.blue);

		DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
		dateAxis.setPositiveArrowVisible(true);

		NumberAxis priceRangeAxis = (NumberAxis) plot.getRangeAxis();
		priceRangeAxis.setLowerMargin(1.00); // to leave room for volume bars
		DecimalFormat format = new DecimalFormat("00.00");
		priceRangeAxis.setNumberFormatOverride(format);

		XYItemRenderer priceRenderer = plot.getRenderer();

		priceRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));

		// add volume series
		NumberAxis volumeRangeAxis = new NumberAxis("Volume");
		volumeRangeAxis.setUpperMargin(1.00); // to leave room for price line
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
		// deviationRenderer.setSeriesStroke(0, new BasicStroke(3F, 1, 1));
		// deviationRenderer.setSeriesPaint(0, Color.BLUE);
		// deviationRenderer.setSeriesFillPaint(0, new Color(255, 200, 200));
		plot.setRenderer(2, deviationRenderer);

		// add mark points
		plot.mapDatasetToRangeAxis(3, 0);
		XYLineAndShapeRenderer xyLineAndShapeRenderer = new XYLineAndShapeRenderer();
		plot.setRenderer(3, xyLineAndShapeRenderer);

	}
}
