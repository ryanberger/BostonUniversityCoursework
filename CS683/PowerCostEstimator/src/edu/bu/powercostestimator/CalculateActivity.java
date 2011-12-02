package edu.bu.powercostestimator;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CalculateActivity extends Activity {

	private DatabaseAdapter _dbAdapter;
	private TextView _labelDaily, _labelMonthly, _labelYearly;
	private double _powerFull, _timeFull, _powerStandby, _timeStandby;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_dbAdapter = DatabaseAdapter.getInstance();

		setContentView(R.layout.calculate_layout);
	}

	public void onSubmitClick(View submitButton) {
		EditText powerFullField = (EditText) findViewById(R.id.editText_power_full);
		String powerFullString = powerFullField.getText().toString().trim();
		if (powerFullString.length() > 0) {
			_powerFull = Double.parseDouble(powerFullString);
		}
		else {
			Toast.makeText(getApplicationContext(), "ERROR: required field Power Usage not filled out!", Toast.LENGTH_LONG).show();
			return;
		}
		
		EditText timeFullField = (EditText) findViewById(R.id.editText_time_full);
		String timeFullString = timeFullField.getText().toString().trim();
		if (timeFullString.length() > 0) {
			_timeFull = Double.parseDouble(timeFullString);
			if (_timeFull > 24) {
				Toast.makeText(getApplicationContext(), "ERROR: Time cannot exceed 24 hours!", Toast.LENGTH_LONG).show();
				return;
			}
		}
		else {
			Toast.makeText(getApplicationContext(), "ERROR: required field Time not filled out!", Toast.LENGTH_LONG).show();
			return;
		}
		
		// Optional values
		EditText powerStandbyField = (EditText) findViewById(R.id.editText_power_standby);
		String powerStandbyText = powerStandbyField.getText().toString().trim();
		if (powerStandbyText.length() > 0) {
			_powerStandby = Double.parseDouble(powerStandbyText);
		}
		EditText timeStandbyField = (EditText) findViewById(R.id.editText_time_standby);
		String timeStandbyText = timeStandbyField.getText().toString().trim();
		if (timeStandbyText.length() > 0) {
			_timeStandby = Double.parseDouble(timeStandbyText);
			if (_timeStandby > 24) {
				Toast.makeText(getApplicationContext(), "ERROR: Time cannot exceed 24 hours!", Toast.LENGTH_LONG).show();
				return;
			}
		}
		
		showChooseProfile();
	}
	
	private void setResults(String profileName, LinearLayout layout) {
		CalculateHelper calcHelper = new CalculateHelper(_dbAdapter.getProfileCost(profileName), _powerFull, _timeFull, _powerStandby, _timeStandby);
		_labelDaily.setText("Daily: " + calcHelper.toString(calcHelper.costPerDay()));
		_labelMonthly.setText("Monthly: " + calcHelper.toString(calcHelper.costPerMonth()));
		_labelYearly.setText("Yearly: " + calcHelper.toString(calcHelper.costPerYear()));
		layout.refreshDrawableState();
	}
	
	private void showChooseProfile() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.label_choose_profile);

		ArrayList<String> profiles = _dbAdapter.getProfileNames();
		
		if (profiles.size() < 1) {
			Toast.makeText(getApplicationContext(), "ERROR: cannot calculate until at least one profile has been added!", Toast.LENGTH_LONG).show();
			return;
		}
		
		final LinearLayout layout = new LinearLayout(this);
		// Set to vertical layout
		layout.setOrientation(1);
		
		final Spinner chooseProfile = new Spinner(this);
		final CheckBox addToProfile = new CheckBox(this);
		
		ArrayAdapter<String> profileAdapter = new ArrayAdapter<String>(this, R.layout.list_item, profiles);
		chooseProfile.setAdapter(profileAdapter);
		
		_labelDaily = new TextView(this);
		_labelMonthly = new TextView(this);
		_labelYearly = new TextView(this);
		layout.addView(_labelDaily);
		layout.addView(_labelMonthly);
		layout.addView(_labelYearly);
		setResults(chooseProfile.getSelectedItem().toString(), layout);
		
		addToProfile.setText(R.string.label_add_to_profile);
		
		layout.addView(chooseProfile);
		layout.addView(addToProfile);
		alert.setView(layout);
		
		chooseProfile.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
					int position, long id) {
				setResults(chooseProfile.getItemAtPosition(position).toString(), layout);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {}
		});
		
		alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				if (addToProfile.isChecked()) {
					showChooseDevice(chooseProfile.getSelectedItem().toString());
				}
			}
		});

		alert.show();
	}
	
	private void showChooseDevice(final String profileName){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.label_name_device);
		
		String[] devices = getResources().getStringArray(R.array.devices_array);
		
		final AutoCompleteTextView deviceName = new AutoCompleteTextView(this);
		ArrayAdapter<String> deviceAdapter = new ArrayAdapter<String>(this, R.layout.list_item, devices);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(1);
		deviceName.setAdapter(deviceAdapter);
		deviceName.setHint(R.string.label_device);
		layout.addView(deviceName);
		alert.setView(layout);
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				String deviceNameValue = deviceName.getText().toString();
				if (deviceNameValue.trim().length() > 0) {
					_dbAdapter.addDeviceToProfile(deviceNameValue, _powerFull, _timeFull, _powerStandby, _timeStandby, profileName);
					Toast.makeText(getApplicationContext(), "Successfully added item to profile", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "ERROR: device name cannot be blank!", Toast.LENGTH_LONG).show();
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});
		
		alert.show();
	}
}
