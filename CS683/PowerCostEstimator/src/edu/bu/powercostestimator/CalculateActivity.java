package edu.bu.powercostestimator;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class CalculateActivity extends Activity {

	private CalculateActivity mActivity;
	private DatabaseAdapter myDbAdapter;
	private AutoCompleteTextView _deviceField;
	private EditText _powerFullField;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myDbAdapter = new DatabaseAdapter(mActivity);

		setContentView(R.layout.calculate_layout);
		_deviceField = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_device);
		_powerFullField = (EditText) findViewById(R.id.editText_power_full);

		String[] devices = getResources().getStringArray(R.array.devices_array);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, devices);
		_deviceField.setAdapter(adapter);
	}

	public void onSubmitClick(View submitButton) {
		double powerFull, timeFull;
		
		// This will be moved to post-submit since this only applies to calculations saved to a profile
		final EditText deviceField = (EditText) findViewById(R.id.autoCompleteTextView_device);
		String device = deviceField.getText().toString();
		
		final EditText powerFullField = (EditText) findViewById(R.id.editText_power_full);
		String powerFullString = powerFullField.getText().toString().trim();
		if (powerFullString.length() > 0) {
			powerFull = Double.parseDouble(powerFullString);
		}
		else {
			Toast.makeText(getApplicationContext(), "Error: required field Power Usage not filled out!", Toast.LENGTH_LONG).show();
			return;
		}
		
		final EditText timeFullField = (EditText) findViewById(R.id.editText_time_full);
		String timeFullString = timeFullField.getText().toString().trim();
		if (timeFullString.length() > 0) {
			timeFull = Double.parseDouble(timeFullString);
		}
		else {
			Toast.makeText(getApplicationContext(), "Error: required field Time not filled out!", Toast.LENGTH_LONG).show();
			return;
		}
		
		// Optional values - TODO: handle
//		final EditText powerStandbyField = (EditText) findViewById(R.id.editText_power_standby);
//		double powerStandby = powerStandbyField.getText().toString().length() > 0 ? Double.parseDouble(powerStandbyField.getText().toString()) : null;
//		final EditText timeStandbyField = (EditText) findViewById(R.id.editText_time_standby);
//		double timeStandby = timeStandbyField.getText().toString().length() > 0 ? Double.parseDouble(timeStandbyField.getText().toString()) : null;
		
		// $0.05 per kWh is hard-coded until we can pull the value from profile database
		CalculateHelper calcHelper = new CalculateHelper(0.05, powerFull, timeFull);
		
		//Temporarily send Toast for debugging
		Toast.makeText(getApplicationContext(), "Total cost (per hour): $"+calcHelper.costPerHour(), Toast.LENGTH_LONG).show();
	}
	
	/*
	 * Override default onKeyUp behavior to account for auto-complete field.
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			if (_deviceField.hasFocus()) {
				// sends focus to another field (user pressed "Next")
				_powerFullField.requestFocus();
				return true;
			}
			else {
				// use default behavior
				return super.onKeyUp(keyCode, event);
			}
		}
		return false;
	}
}
