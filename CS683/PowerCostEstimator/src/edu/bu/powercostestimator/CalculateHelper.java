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
	
	private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
	
	public CalculateHelper(double costPerKwh, double powerFull, double timeFull) {
		this.costPerKwh = costPerKwh;
		this.powerFull = powerFull;
		this.timeFull = timeFull;
	}
	
	public double costPerDay() {
		// Convert powerFull from Watts to kiloWatts by dividing by 1000.
		return (powerFull / 1000) * timeFull * costPerKwh;
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
}
