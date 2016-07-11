package com.BigData.dataanalysis;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class PieChartActivity extends Activity {
 private View mChart;

 @Override
 public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.activity_pie_chart);
	 Intent intent=getIntent();
	 openChart();
 }
 
 private void openChart() {

 // Pie Chart Section Names
 String[] code = new String[] { "Good","Average","Poor" };

 // Pie Chart Section Value
 Result result = Result.getInstance();
	
/*double a1,a2,a3;
	
a1=(result.getGood()/(result.getGood()+result.getAvg()+result.getPoor()))*100;
a2=(result.getAvg()/(result.getGood()+result.getAvg()+result.getPoor()))*100;
a3=(result.getPoor()/(result.getGood()+result.getAvg()+result.getPoor()))*100;*/
	
 double[] distribution = {result.getGood(),result.getAvg(),result.getPoor()};

 // Color of each Pie Chart Sections
 int[] colors = { Color.BLUE, Color.MAGENTA, Color.GREEN};

 // Instantiating CategorySeries to plot Pie Chart
 CategorySeries distributionSeries = new CategorySeries(
 " Sales Data Analysis");
 for (int i = 0; i < distribution.length; i++) {
 // Adding a slice with its values and name to the Pie Chart
 distributionSeries.add(code[i], distribution[i]);
 }

 // Instantiating a renderer for the Pie Chart
 DefaultRenderer defaultRenderer = new DefaultRenderer();
 for (int i = 0; i < distribution.length; i++) {
 SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
 seriesRenderer.setColor(colors[i]);
 seriesRenderer.setDisplayChartValues(true);
//Adding colors to the chart
 defaultRenderer.setBackgroundColor(Color.BLACK);
 defaultRenderer.setApplyBackgroundColor(true);
 // Adding a renderer for a slice
 defaultRenderer.addSeriesRenderer(seriesRenderer);
 }

 defaultRenderer
 .setChartTitle("Sales Data Analysis");
 defaultRenderer.setChartTitleTextSize(20);
 defaultRenderer.setZoomButtonsVisible(false);

 // this part is used to display graph on the xml
 // Creating an intent to plot bar chart using dataset and
 // multipleRenderer
 // Intent intent = ChartFactory.getPieChartIntent(getBaseContext(),
 // distributionSeries , defaultRenderer, "AChartEnginePieChartDemo");

 // Start Activity
 // startActivity(intent);

 LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
 // remove any views before u paint the chart
 chartContainer.removeAllViews();
 // drawing pie chart
 mChart = ChartFactory.getPieChartView(getBaseContext(),
 distributionSeries, defaultRenderer);
 // adding the view to the linearlayout
 chartContainer.addView(mChart);

 }
}


