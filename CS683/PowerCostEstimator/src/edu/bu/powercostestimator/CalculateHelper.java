package edu.bu.powercostestimator;

public class CalculateHelper {
	
	private double costPerKwh;
	private double powerFull;
	private double timeFull;
	
	public CalculateHelper(){
		
	}
	
	public CalculateHelper(double costPerKwh, double powerFull, double timeFull) {
		this.costPerKwh = costPerKwh;
		this.powerFull = powerFull;
		this.timeFull = timeFull;
	}
	
	public double costPerHour() {
		// Convert powerFull from Watts to kiloWatts by dividing by 1000.
		return (powerFull / 1000) * timeFull * costPerKwh;
	}
	
	public double costPerDay() {
		return costPerHour() * 24;
	}
	
	public double costPerMonth() {
		// Calculation based on average number of days per month in a given year.
		return costPerDay() * 29.53;
	}
	
	public double costPerYear() {
		return costPerDay() * 365;
	}
}
