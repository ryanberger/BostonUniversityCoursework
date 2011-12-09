package edu.bu.powercostestimator;

public class DeviceContent {
	private String deviceName;
	private double normalUsage;
	private double normalTime;
	private double standbyUsage;
	private double standbyTime;
	
	public DeviceContent(String deviceName, double normalUsage, double normalTime, double standbyUsage, double standbyTime) {
		this.deviceName = deviceName;
		this.normalUsage = normalUsage;
		this.normalTime = normalTime;
		this.standbyUsage = standbyUsage;
		this.standbyTime = standbyTime;
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
}
