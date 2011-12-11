package edu.bu.powercostestimator;

public class DeviceContent {
	private String deviceName;
	private double normalUsage;
	private double normalTime;
	private double standbyUsage;
	private double standbyTime;
	private double dailyCost;
	private double monthlyCost;
	private double yearlyCost;
	
	public DeviceContent(String deviceName, double normalUsage, double normalTime, double standbyUsage, double standbyTime,
			double dailyCost, double monthlyCost, double yearlyCost) {
		this.deviceName = deviceName;
		this.normalUsage = normalUsage;
		this.normalTime = normalTime;
		this.standbyUsage = standbyUsage;
		this.standbyTime = standbyTime;
		this.dailyCost = dailyCost;
		this.monthlyCost = monthlyCost;
		this.yearlyCost = yearlyCost;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	
	public double getNormalUsage() {
		return normalUsage;
	}
	
	public double getNormalTime() {
		return normalTime;
	}
	
	public double getStandbyUsage() {
		return standbyUsage;
	}
	
	public double getStandbyTime() {
		return standbyTime;
	}
	
	public double getDailyCost() {
		return dailyCost;
	}
	
	public double getMonthlyCost() {
		return monthlyCost;
	}
	
	public double getYearlyCost() {
		return yearlyCost;
	}
}
