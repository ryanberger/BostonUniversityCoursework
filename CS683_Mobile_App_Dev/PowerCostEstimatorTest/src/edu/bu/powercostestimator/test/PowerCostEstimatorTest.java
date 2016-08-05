package edu.bu.powercostestimator.test;

import android.test.ActivityInstrumentationTestCase2;
import edu.bu.powercostestimator.CalculateHelper;
import edu.bu.powercostestimator.PowerCostEstimator;

public class PowerCostEstimatorTest extends
		ActivityInstrumentationTestCase2<PowerCostEstimator> {

	private CalculateHelper _calcHelperWithoutStandby;
	private CalculateHelper _calcHelperWithStandby;

	public PowerCostEstimatorTest() {
		super("edu.bu.powercostestimator", PowerCostEstimator.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		_calcHelperWithoutStandby = new CalculateHelper(0.05, 134.0, 5.0);
		_calcHelperWithStandby = new CalculateHelper(0.05, 142.0, 8.0, 67.0, 12.0);
	}

	public void testCalculateCostPerDay() {
		assertEquals(CalculateHelper.toString(_calcHelperWithoutStandby.costPerDay()), "$0.03");
		assertEquals(CalculateHelper.toString(_calcHelperWithStandby.costPerDay()), "$0.10");
	}
	
	public void testCalculateCostPerMonth() {
		assertEquals(CalculateHelper.toString(_calcHelperWithoutStandby.costPerMonth()), "$0.99");
		assertEquals(CalculateHelper.toString(_calcHelperWithStandby.costPerMonth()), "$2.86");
	}
	
	public void testCalculateCostPerYear() {
		assertEquals(CalculateHelper.toString(_calcHelperWithoutStandby.costPerYear()), "$12.24");
		assertEquals(CalculateHelper.toString(_calcHelperWithStandby.costPerYear()), "$35.43");
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
