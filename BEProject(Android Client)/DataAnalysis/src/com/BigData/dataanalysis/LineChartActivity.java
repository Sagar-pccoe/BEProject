package com.BigData.dataanalysis;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class LineChartActivity extends Activity {

private View mChart;
private String[] mMonth = new String[] {
"Good","Average","Poor"
};

@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_line_chart);
	Intent i = getIntent();
	openChart();
}

private void openChart(){
int[] x = {0,1,2};

Result result = Result.getInstance();
double a1,a2,a3;

a1=(result.getGood()/(result.getGood()+result.getAvg()+result.getPoor()))*100;
a2=(result.getAvg()/(result.getGood()+result.getAvg()+result.getPoor()))*100;
a3=(result.getPoor()/(result.getGood()+result.getAvg()+result.getPoor()))*100;

double[] income = {a1,a2,a3};

// Creating an XYSeries for Income
XYSeries incomeSeries = new XYSeries("Sales Performance");
// Adding data to Income Series
for(int i=0;i<x.length;i++){
incomeSeries.add(i,income[i]);
}

// Creating a dataset to hold each series
XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
// Adding Income Series to the dataset
dataset.addSeries(incomeSeries);

// Creating XYSeriesRenderer to customize incomeSeries
XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
incomeRenderer.setColor(Color.CYAN); //color of the graph set to cyan
incomeRenderer.setFillPoints(true);
incomeRenderer.setLineWidth(2f);
incomeRenderer.setDisplayChartValues(true);
//setting chart value distance
incomeRenderer.setDisplayChartValuesDistance(10);
//setting line graph point style to circle
incomeRenderer.setPointStyle(PointStyle.CIRCLE);
//setting stroke of the line chart to solid
incomeRenderer.setStroke(BasicStroke.SOLID);  

// Creating a XYMultipleSeriesRenderer to customize the whole chart
XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
multiRenderer.setXLabels(0);
multiRenderer.setChartTitle("Sales Data Analysis");
multiRenderer.setXTitle("Sales Performance");
multiRenderer.setYTitle("Percentage of Sales");

/***
* Customizing graphs
*/
//setting text size of the title
multiRenderer.setChartTitleTextSize(28);
//setting text size of the axis title
multiRenderer.setAxisTitleTextSize(24);
//setting text size of the graph lable
multiRenderer.setLabelsTextSize(24);
//setting zoom buttons visiblity
multiRenderer.setZoomButtonsVisible(false);
//setting pan enablity which uses graph to move on both axis
multiRenderer.setPanEnabled(false, false);
//setting click false on graph
multiRenderer.setClickEnabled(false);
//setting zoom to false on both axis
multiRenderer.setZoomEnabled(false, false);
//setting lines to display on y axis
multiRenderer.setShowGridY(true);
//setting lines to display on x axis
multiRenderer.setShowGridX(true);
//setting legend to fit the screen size
multiRenderer.setFitLegend(true);
//setting displaying line on grid
multiRenderer.setShowGrid(true);
//setting zoom to false
multiRenderer.setZoomEnabled(false);
//setting external zoom functions to false
multiRenderer.setExternalZoomEnabled(false);
//setting displaying lines on graph to be formatted(like using graphics)
multiRenderer.setAntialiasing(true);
//setting to in scroll to false
multiRenderer.setInScroll(false);
//setting to set legend height of the graph
multiRenderer.setLegendHeight(30);
//setting x axis label align
multiRenderer.setXLabelsAlign(Align.CENTER);
//setting y axis label to align
multiRenderer.setYLabelsAlign(Align.LEFT);
//setting text style
multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
//setting no of values to display in y axis
multiRenderer.setYLabels(10);
// setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.
// if you use dynamic values then get the max y value and set here
multiRenderer.setYAxisMax(100);
//setting used to move the graph on xaxiz to .5 to the right
multiRenderer.setXAxisMin(-0.5);
//setting used to move the graph on xaxiz to .5 to the right
multiRenderer.setXAxisMax(11);
//setting bar size or space between two bars
//multiRenderer.setBarSpacing(0.5);
//Setting background color of the graph to transparent
multiRenderer.setBackgroundColor(Color.TRANSPARENT);
//Setting margin color of the graph to transparent
multiRenderer.setMarginsColor(getResources().getColor(R.color.abc_search_url_text_holo));
multiRenderer.setApplyBackgroundColor(true);
multiRenderer.setScale(2f);
//setting x axis point size
multiRenderer.setPointSize(4f);
//setting the margin size for the graph in the order top, left, bottom, right
multiRenderer.setMargins(new int[]{30, 30, 30, 30});

for(int i=0; i< x.length;i++){
multiRenderer.addXTextLabel(i, mMonth[i]);
}

// Adding incomeRenderer and expenseRenderer to multipleRenderer
// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
// should be same
multiRenderer.addSeriesRenderer(incomeRenderer);

//this part is used to display graph on the xml
LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
//remove any views before u paint the chart
chartContainer.removeAllViews();
//drawing bar chart
mChart = ChartFactory.getLineChartView(this, dataset, multiRenderer);
//adding the view to the linearlayout
chartContainer.addView(mChart);

}

}
