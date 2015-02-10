/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package business.dataVisualization.chart;

/**
 * 
 * @author Dinh
 */
public class ChartStyle {

	@SuppressWarnings("rawtypes")
	private Class chartClass;

	@SuppressWarnings("rawtypes")
	public ChartStyle(Class chartClass) {
		this.chartClass = chartClass;
	}

	@SuppressWarnings("rawtypes")
	public Class getChartClass() {
		return chartClass;
	}

	@Override
	public String toString() {
		return chartClass.getSimpleName();
	}

}
