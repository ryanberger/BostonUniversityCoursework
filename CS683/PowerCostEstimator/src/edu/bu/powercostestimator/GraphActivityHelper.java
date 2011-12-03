package edu.bu.powercostestimator;

import java.util.ArrayList;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class GraphActivityHelper {
	public static Intent showGraph(Context context) {
		double[] values = new double[] { 12, 14, 11, 10, 19 };
		DefaultRenderer renderer = buildCategoryRenderer(getRandomColors(5));
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomEnabled(true);
		renderer.setChartTitleTextSize(20);
		return new Intent(ChartFactory.getPieChartIntent(context, buildCategoryDataset("Project budget", values),
				renderer, "Breakdown by total cost"));
	}

	/**
	 * Builds a category renderer to use the provided colors.
	 * 
	 * @param colors the colors
	 * @return the category renderer
	 */
	private static DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	/**
	 * Builds a category series using the provided values.
	 * 
	 * @param titles the series titles
	 * @param values the values
	 * @return the category series
	 */
	private static CategorySeries buildCategoryDataset(String title, double[] values) {
		CategorySeries series = new CategorySeries(title);
		int k = 0;
		for (double value : values) {
			series.add("Project " + ++k, value);
		}

		return series;
	}
	
	private static int[] getRandomColors(int numberRequested) {
		ArrayList<Integer> colors = new ArrayList<Integer>();
		for (int i = 0; i < 360; i += 360 / numberRequested) {
			Random rnd = new Random(); 
			colors.add(Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
		}
		
		int[] ret = new int[colors.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = colors.get(i).intValue();
	    }
	    return ret;
	}
}
