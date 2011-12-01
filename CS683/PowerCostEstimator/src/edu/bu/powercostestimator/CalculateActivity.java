package edu.bu.powercostestimator;

import java.text.NumberFormat;
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

	private DatabaseAdapter myDbAdapter;
	private TextView _labelDaily;
	private TextView _labelWeekly;
	private TextView _labelMonthly;
	private TextView _labelYearly;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myDbAdapter = DatabaseAdapter.getInstance();

		setContentView(R.layout.calculate_layout);
	}

	public void onSubmitClick(View submitButton) {
		double powerFull, timeFull;
		
		EditText powerFullField = (EditText) findViewById(R.id.editText_power_full);
		String powerFullString = powerFullField.getText().toString().trim();
		if (powerFullString.length() > 0) {
			powerFull = Double.parseDouble(powerFullString);
		}
		else {
			Toast.makeText(getApplicationContext(), "ERROR: required field Power Usage not filled out!", Toast.LENGTH_LONG).show();
			return;
		}
		
		EditText timeFullField = (EditText) findViewById(R.id.editText_time_full);
		String timeFullString = timeFullField.getText().toString().trim();
		if (timeFullString.length() > 0) {
			timeFull = Double.parseDouble(timeFullString);
		}
		else {
			Toast.makeText(getApplicationContext(), "ERROR: required field Time not filled out!", Toast.LENGTH_LONG).show();
			return;
		}
		
		// Optional values - TODO: handle
//		final EditText powerStandbyField = (EditText) findViewById(R.id.editText_power_standby);
//		double powerStandby = powerStandbyField.getText().toString().length() > 0 ? Double.parseDouble(powerStandbyField.getText().toString()) : null;
//		final EditText timeStandbyField = (EditText) findViewById(R.id.editText_time_standby);
//		double timeStandby = timeStandbyField.getText().toString().length() > 0 ? Double.parseDouble(timeStandbyField.getText().toString()) : null;
		
		showChooseProfile(powerFull, timeFull);
	}
	
	private void setResults(double devicePowerFull, double deviceTimeFull, String profileName, LinearLayout layout) {
		CalculateHelper calcHelper = new CalculateHelper(myDbAdapter.getProfileCost(profileName), devicePowerFull, deviceTimeFull);
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
		_labelDaily.setText("Daily: " + currencyFormatter.format(calcHelper.costPerDay()));
		_labelWeekly.setText("Weekly: " + currencyFormatter.format(calcHelper.costPerWeek()));
		_labelMonthly.setText("Monthly: " + currencyFormatter.format(calcHelper.costPerMonth()));
		_labelYearly.setText("Yearly: " + currencyFormatter.format(calcHelper.costPerYear()));
		layout.refreshDrawableState();
	}
	
	private void showChooseProfile(final double devicePowerFull, final double deviceTimeFull) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.label_choose_profile);

		ArrayList<String> profiles = myDbAdapter.getProfileNames();
		
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
		_labelWeekly = new TextView(this);
		_labelMonthly = new TextView(this);
		_labelYearly = new TextView(this);
		layout.addView(_labelDaily);
		layout.addView(_labelWeekly);
		layout.addView(_labelMonthly);
		layout.addView(_labelYearly);
		setResults(devicePowerFull, deviceTimeFull, chooseProfile.getSelectedItem().toString(), layout);
		
		addToProfile.setText(R.string.label_add_to_profile);
		
		layout.addView(chooseProfile);
		layout.addView(addToProfile);
		alert.setView(layout);
		
		chooseProfile.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
					int position, long id) {
				setResults(devicePowerFull, deviceTimeFull, chooseProfile.getItemAtPosition(position).toString(), layout);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {}
		});
		
		alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				if (addToProfile.isChecked()) {
					showChooseDevice(devicePowerFull, deviceTimeFull, chooseProfile.getSelectedItem().toString());
				}
			}
		});

		alert.show();
	}
	
	private void showChooseDevice(final double devicePowerFull, final double deviceTimeFull, final String profileName){
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
					myDbAdapter.addDeviceToProfile(deviceNameValue, devicePowerFull, deviceTimeFull, profileName);
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
