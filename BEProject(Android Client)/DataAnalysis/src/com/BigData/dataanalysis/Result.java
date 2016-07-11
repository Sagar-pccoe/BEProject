package com.BigData.dataanalysis;

/*
 *This class maintains the result obtained after analysis
 */

public class Result {

	private static Result result = new Result();
	
	private double poor,avg,good;
	
  public double getPoor() {
		return poor;
	}

	public void setPoor(double poor) {
		this.poor = poor;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public double getGood() {
		return good;
	}

	public void setGood(double good) {
		this.good = good;
	}

private Result()
	{
	 poor=avg=good=0.0;	
	}
	
	private Result(double p,double a, double g)
	{
		poor = p;
		avg = a;
		good = g;
	}
	
	
	public static Result getInstance()
	{
		return result;
	}
	
	public String toString()
	{
		return "[ Poor:"+ poor + ", Average:"+ avg + ", Good:" + good + "];";
	}
}
