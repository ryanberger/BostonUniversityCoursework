package edu.bu.powercostestimator.test;

import android.test.ActivityInstrumentationTestCase2;
import edu.bu.powercostestimator.CalculateHelper;
import edu.bu.powercostestimator.PowerCostEstimator;

public class PowerCostEstimatorTest extends
		ActivityInstrumentationTestCase2<PowerCostEstimator> {

	private CalculateHelper _calcHelper;

	public PowerCostEstimatorTest() {
		super("edu.bu.powercostestimator", PowerCostEstimator.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		_calcHelper = new CalculateHelper(0.05, 142.0, 8.0, 67.0, 12.0);
	}

	public void testCalculateCostPerDay() {
		assertEquals(_calcHelper.toString(_calcHelper.costPerDay()), "$0.10");
	}
	
	public void testCalculateCostPerMonth() {
		assertEquals(_calcHelper.toString(_calcHelper.costPerMonth()), "$2.86");
	}
	
	public void testCalculateCostPerYear() {
		assertEquals(_calcHelper.toString(_calcHelper.costPerYear()), "$35.43");
	}

	@Override
	public void tearDown() {
		try {
			super.tearDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
