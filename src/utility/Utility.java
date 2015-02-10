package utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Utility {
	public static double variance(ArrayList<Double> list) {
		double var = 0;
		double mean = mean(list);
		for (Double d : list)
			var += Math.pow(d - mean, 2) / (list.size() - 1);
		return var;
	}

	public static double mean(ArrayList<Double> list) {
		double mean = 0;
		for (int i = 0; i < list.size(); ++i)
			mean += list.get(i);
		mean = mean / list.size();
		return mean;
	}

	public static Date increaseDate(Date currentDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	public static void debug(Object... os) {
		JOptionPane.showMessageDialog(new JFrame(), Arrays.deepToString(os));
	}

}
