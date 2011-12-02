package edu.bu.powercostestimator;

import java.text.NumberFormat;

public class CalculateHelper {
	
	private final int DAYS_PER_WEEK = 7;
	// Calculation based on average number of days per month in a given year.
	private final double DAYS_PER_MONTH = 29.53;
	private final double DAYS_PER_YEAR = 365.25;
	
	private double costPerKwh;
	private double powerFull;
	private double timeFull;
	private double powerStandby = 0.0;
	private double timeStandby = 0.0;
	
	private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
	
	public CalculateHelper(double costPerKwh, double powerFull, double timeFull, double powerStandby, double timeStandby) {
		this.costPerKwh = costPerKwh;
		this.powerFull = powerFull;
		this.timeFull = timeFull;
		this.powerStandby = powerStandby;
		this.timeStandby = timeStandby;
	}
	
	public double costPerDay() {
		return costPerDayFull() + costPerDayStandby();
	}
	
	public double costPerWeek() {
		return costPerDay() * DAYS_PER_WEEK;
	}
	
	public double costPerMonth() {
		return costPerDay() * DAYS_PER_MONTH;
	}
	
	public double costPerYear() {
		return costPerDay() * DAYS_PER_YEAR;
	}
	
	public String toString(double value) {
		return currencyFormatter.format(value);
	}
	
	private double costPerDayFull() {
		return wattsToKilowatts(powerFull) * timeFull * costPerKwh;
	}
	
	// Standby cost is optional and will not affect final calculation if omitted
	private double costPerDayStandby() {
		return wattsToKilowatts(powerStandby) * timeStandby * costPerKwh;
	}
	
	private double wattsToKilowatts(double input) {
		return input / 1000;
	}
}
