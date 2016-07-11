package com.BigData.dataanalysis;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.support.v7.app.ActionBarActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

public class BarGraphActivity extends ActionBarActivity {
	
	private XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
	private XYSeriesRenderer renderer=new XYSeriesRenderer();
	private XYMultipleSeriesRenderer mRenderer=new XYMultipleSeriesRenderer();
	private XYSeries series=new XYSeries("Sales Data Analysis");
	private GraphicalView chartView; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bar_graph);
		
		Intent i=getIntent();
		
		setDataset();
		renderer=setRenderer();
		setmRenderer(renderer);
		
		
		if(chartView==null){
			LinearLayout layout=(LinearLayout)findViewById(R.id.chart);
			chartView=ChartFactory.getBarChartView(this,dataset,mRenderer,Type.DEFAULT);
			layout.addView(chartView);
		}
		else{
			chartView.repaint();
		}
	}
	
	private XYMultipleSeriesDataset setDataset(){
		
		
		Result result = Result.getInstance();
		
		double a1,a2,a3;
		
		a1=(result.getGood()/(result.getGood()+result.getAvg()+result.getPoor()))*100;
		a2=(result.getAvg()/(result.getGood()+result.getAvg()+result.getPoor()))*100;
		a3=(result.getPoor()/(result.getGood()+result.getAvg()+result.getPoor()))*100;
		
		double[] array1={a1,a2,a3};
		
		for(int i=0;i<array1.length;i++){
			series.add(i,array1[i]);
		}
		dataset.addSeries(series);
		return dataset;
	}
	
	private XYSeriesRenderer setRenderer(){
		renderer.setColor(Color.RED);
	    renderer.setDisplayChartValues(true);
	    renderer.setChartValuesSpacing((float) 5.5d);
	    renderer.setLineWidth((float) 10.5d);    
		return renderer;
	}
	
	private XYMultipleSeriesRenderer setmRenderer(XYSeriesRenderer renderer){
		String[] Array={"Good","Average","Bad"};
		mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		mRenderer.setXLabels(0);
		mRenderer.setChartTitle("Sales Data Analysis");
		mRenderer.setXTitle("Sales Performance");
		mRenderer.setYTitle("Sales Performance in %");
		mRenderer.setChartTitleTextSize(45);
		  //setting text size of the axis title
		mRenderer.setAxisTitleTextSize(40);
		mRenderer.setZoomButtonsVisible(true);   
		mRenderer.setShowLegend(true);
	    mRenderer.setShowGridX(true);      // this will show the grid in  graph
	    mRenderer.setShowGridY(true);              
//	        mRenderer.setAntialiasing(true);
	    mRenderer.setBarSpacing(1.5);   // adding spacing between the line or stacks
	    mRenderer.setApplyBackgroundColor(true);
	    mRenderer.setBackgroundColor(Color.BLACK);
	    mRenderer.setXAxisMin(0);
//	        mRenderer.setYAxisMin(.5);
	    mRenderer.setXAxisMax(5);
	    mRenderer.setYAxisMax(75);
		for(int i=0;i<Array.length;i++){
			mRenderer.addXTextLabel(i,Array[i]);
		}
		
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.setPanEnabled(true, true);   
		return mRenderer;
	}
	

}
