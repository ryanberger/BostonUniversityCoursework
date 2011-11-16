package edu.bu.powercostestimator.test;

import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import edu.bu.powercostestimator.DatabaseAdapter;
import edu.bu.powercostestimator.PowerCostEstimator;

public class PowerCostEstimatorTest extends
		ActivityInstrumentationTestCase2<PowerCostEstimator> {
	
	private PowerCostEstimator mActivity;
    private TextView mView;
    private String resourceString;
    private DatabaseAdapter myDbAdapter;

	public PowerCostEstimatorTest() {
	      super("edu.bu.powercostestimator", PowerCostEstimator.class);
	}

	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        myDbAdapter = new DatabaseAdapter(mActivity);
        myDbAdapter.open();
        mView = (TextView) mActivity.findViewById(edu.bu.powercostestimator.R.id.autoCompleteTextView_device);
        resourceString = mActivity.getString(edu.bu.powercostestimator.R.string.label_device);
    }
	
	public void testPreconditions() {
	      //assertNotNull(mView);
	}
	
	public void testText() {
	      //assertEquals(resourceString,(String)mView.getText());
	}
	
	public void testCreateDatabase() {
		myDbAdapter.clearDatabase();
        myDbAdapter.createDatabase();
	}
	
	public void testSetProfile(){
		myDbAdapter.setProfile("Home_TEST", 0.10);
		Cursor c = myDbAdapter.getProfile("Home_TEST");
		int profileNameColumn = c.getColumnIndex("profile_name");
		c.moveToFirst();
        assertEquals(c.getString(profileNameColumn), "Home_TEST");
        myDbAdapter.deleteProfile("Home_TEST");
	}
}
