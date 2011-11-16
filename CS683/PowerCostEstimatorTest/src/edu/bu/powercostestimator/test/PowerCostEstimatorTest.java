package edu.bu.powercostestimator.test;

import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import edu.bu.powercostestimator.CalculateHelper;
import edu.bu.powercostestimator.DatabaseAdapter;
import edu.bu.powercostestimator.PowerCostEstimator;

public class PowerCostEstimatorTest extends
		ActivityInstrumentationTestCase2<PowerCostEstimator> {
	
	private PowerCostEstimator mActivity;
    private DatabaseAdapter mDbAdapter;
    private CalculateHelper mCalcHelper;

	public PowerCostEstimatorTest() {
	      super("edu.bu.powercostestimator", PowerCostEstimator.class);
	}

	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        mDbAdapter = new DatabaseAdapter(mActivity);
        mDbAdapter.open("PowerCostEstimatorDbTest", 1);
        mDbAdapter.createDatabase();
        mCalcHelper = new CalculateHelper(0.05, 150, 24);
    }
	
	public void testSetProfile(){
		mDbAdapter.setProfile("Home_TEST", 0.10);
		Cursor c = mDbAdapter.getProfile("Home_TEST");
		int profileNameColumn = c.getColumnIndex("profile_name");
		c.moveToFirst();
		assertEquals(c.getString(profileNameColumn), "Home_TEST");
	}
	
	public void testCalculateCostPerHour() {
		assertEquals(mCalcHelper.costPerHour(), 0.18);
	}
	
	public void testCalculateCostPerDay() {
		assertEquals(mCalcHelper.costPerDay(), 4.32);
	}
	
	@Override
	public void tearDown() {
		mDbAdapter.clearDatabase();
		mDbAdapter.close();
	}
}
