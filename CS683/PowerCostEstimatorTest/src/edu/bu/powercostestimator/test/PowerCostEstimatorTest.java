package edu.bu.powercostestimator.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import edu.bu.powercostestimator.*;

public class PowerCostEstimatorTest extends
		ActivityInstrumentationTestCase2<PowerCostEstimator> {
	
	private PowerCostEstimator mActivity;
    private TextView mView;
    private String resourceString;

	public PowerCostEstimatorTest() {
	      super("edu.bu.powercostestimator", PowerCostEstimator.class);
	}

	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        mView = (TextView) mActivity.findViewById(edu.bu.powercostestimator.R.id.textview);
        resourceString = mActivity.getString(edu.bu.powercostestimator.R.string.hello);
    }
	
	public void testPreconditions() {
	      assertNotNull(mView);
	}
	
	public void testText() {
	      assertEquals(resourceString,(String)mView.getText());
	}
	
}
