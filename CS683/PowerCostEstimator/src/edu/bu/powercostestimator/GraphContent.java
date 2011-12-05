package edu.bu.powercostestimator;

public class GraphContent {
	private String deviceName;
	private double deviceCost;
	
	public GraphContent(String deviceName, double deviceCost) {
		this.deviceName = deviceName;
		this.deviceCost = deviceCost;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	
	public double getDeviceCost() {
		return deviceCost;
	}
}
