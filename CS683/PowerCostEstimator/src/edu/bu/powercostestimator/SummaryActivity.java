package edu.bu.powercostestimator;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SummaryActivity extends Activity {

	private DatabaseAdapter _dbAdapter;
	private ListView _lv;
	private ArrayList<String> _lv_arr = new ArrayList<String>();
	private ArrayList<GraphContent> _gcList = new ArrayList<GraphContent>();
	private double _totalCost;
	private Resources _res;
	private String _profileName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get profile name from ProfileActivity
		Bundle extras = getIntent().getExtras();
		_profileName = extras.getString("profileName");
		_res = getResources();
		
		setContentView(R.layout.summary_layout);
		setTitle(String.format(_res.getString(R.string.label_summary), _profileName));
		
		_lv = (ListView)findViewById(R.id.summary_listview);
		_dbAdapter = DatabaseAdapter.getInstance();
		_dbAdapter.open(this);
		
		updateSummaryList();
	}
	
	/*
	 * Open a new alert window to edit the currently selected device
	 */
	private void showEditDeviceAlert(int position, Cursor c) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		// Set an EditText view to get user input
		LinearLayout layout = new LinearLayout(this);
		// Set to vertical layout
		layout.setOrientation(1);
		
		TextView deviceName = new TextView(this);
		TextView devicePowerFull = new TextView(this);
		TextView deviceTimeFull = new TextView(this);
		TextView devicePowerStandby = new TextView(this);
		TextView deviceTimeStandby = new TextView(this);
		deviceName.setText(R.string.label_device);
		devicePowerFull.setText(R.string.label_power_full);
		deviceTimeFull.setText(R.string.label_time_full);
		devicePowerStandby.setText(R.string.label_power_standby);
		deviceTimeStandby.setText(R.string.label_time_standby);
		
		final EditText deviceNameInput = new EditText(this);
		final EditText devicePowerFullInput = new EditText(this);
		final EditText deviceTimeFullInput = new EditText(this);
		final EditText devicePowerStandbyInput = new EditText(this);
		final EditText deviceTimeStandbyInput = new EditText(this);
		
		alert.setTitle(R.string.label_edit_device);
		//alert.setView(getProfileLayout(profileNameInput, profileCostInput));
		
		final int deviceId = getDeviceId(position, c);
		deviceNameInput.setText(c.getString(1));
		deviceNameInput.setSingleLine();
		deviceNameInput.setHint(R.string.label_device);
		devicePowerFullInput.setText(c.getString(2));
		devicePowerFullInput.setHint(R.string.hint_power_full);
		devicePowerFullInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		deviceTimeFullInput.setText(c.getString(3));
		deviceTimeFullInput.setHint(R.string.hint_time_full);
		deviceTimeFullInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		devicePowerStandbyInput.setText(c.getString(4));
		devicePowerStandbyInput.setHint(R.string.hint_power_standby);
		devicePowerStandbyInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		deviceTimeStandbyInput.setText(c.getString(5));
		deviceTimeStandbyInput.setHint(R.string.hint_time_standby);
		deviceTimeStandbyInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		
		layout.addView(deviceName);
		layout.addView(deviceNameInput);
		layout.addView(devicePowerFull);
		layout.addView(devicePowerFullInput);
		layout.addView(deviceTimeFull);
		layout.addView(deviceTimeFullInput);
		layout.addView(devicePowerStandby);
		layout.addView(devicePowerStandbyInput);
		layout.addView(deviceTimeStandby);
		layout.addView(deviceTimeStandbyInput);
		alert.setView(layout);
		
		alert.setPositiveButton(_res.getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				editDevice(deviceId, deviceNameInput, devicePowerFullInput, deviceTimeFullInput, devicePowerStandbyInput, deviceTimeStandbyInput);
			}
		});

		alert.setNegativeButton(_res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});
		
		alert.show();
	}
	
	/*
	 * Edit existing device
	 */
	private void editDevice(int deviceId, EditText deviceNameField, EditText powerFullField,
			EditText timeFullField, EditText powerStandbyField, EditText timeStandbyField) {
		double powerFull, timeFull, powerStandby = 0.0, timeStandby = 0.0;
		String deviceName = deviceNameField.getText().toString().trim();
		if (deviceName.length() < 1) {
			toast(_res.getString(R.string.error_empty_profile_name_field));
			return;
		}
		
		String powerFullString = powerFullField.getText().toString().trim();
		if (powerFullString.length() > 0) {
			powerFull = Double.parseDouble(powerFullString);
		}
		else {
			toast(_res.getString(R.string.error_empty_power_field));
			return;
		}
		
		String timeFullString = timeFullField.getText().toString().trim();
		if (timeFullString.length() > 0) {
			timeFull = Double.parseDouble(timeFullString);
			if (timeFull > 24) {
				toast(_res.getString(R.string.error_over_24_hours));
				return;
			}
		}
		else {
			toast(_res.getString(R.string.error_empty_time_field));
			return;
		}
		
		// Optional values
		String powerStandbyText = powerStandbyField.getText().toString().trim();
		if (powerStandbyText.length() > 0) {
			powerStandby = Double.parseDouble(powerStandbyText);
		}
		String timeStandbyText = timeStandbyField.getText().toString().trim();
		if (timeStandbyText.length() > 0) {
			timeStandby = Double.parseDouble(timeStandbyText);
			if (timeStandby > 24) {
				toast(_res.getString(R.string.error_over_24_hours));
				return;
			}
		}
		
		_dbAdapter.updateDevice(deviceId, deviceName, powerFull, timeFull, powerStandby, timeStandby);
		updateSummaryList();
	}
	
	/*
	 * Show delete device alert
	 */
	private void showDeleteDeviceAlert(int position, Cursor c) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		final int deviceId = getDeviceId(position, c);
		alert.setTitle(R.string.label_delete_device);
		TextView tv = new TextView(this);
		tv.setText(R.string.warning_delete_device);
		alert.setView(tv);
		
		alert.setPositiveButton(_res.getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				deleteDevice(deviceId);
			}
		});

		alert.setNegativeButton(_res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});
		
		alert.show();
	}
	
	/*
	 * Remove device from database
	 */
	private void deleteDevice(int deviceId) {
		_dbAdapter.deleteDevice(deviceId);
		updateSummaryList();
	}
	
	/*
	 * Retrieve device ID based on current list position (will match database results)
	 */
	private int getDeviceId(int position, Cursor c) {
		c.moveToPosition(position);
		return c.getInt(0);
	}
	
	/*
	 * Refresh summary list with current values
	 */
	private void updateSummaryList() {
		_lv_arr.clear();
		_gcList.clear();
		
		Button showChart = (Button) findViewById(R.id.button_show_chart);
		final Cursor c = _dbAdapter.getProfileDevices(_profileName);
		
		double profileCost = _dbAdapter.getProfileCost(_profileName);
		double normalUsage, normalTime, standbyUsage, standbyTime,
			totalNormalUsage = 0.0, totalNormalTime = 0.0, totalStandbyUsage = 0.0, totalStandbyTime = 0.0,
			totalCostPerDay = 0.0, totalCostPerMonth = 0.0, totalCostPerYear = 0.0;
		String deviceName;
		CalculateHelper calcHelper = new CalculateHelper();
		GraphContent gc;
		
		while (c.moveToNext()) {
			deviceName = c.getString(1);
			normalUsage = c.getDouble(2);
			normalTime = c.getDouble(3);
			standbyUsage = c.getDouble(4);
			standbyTime = c.getDouble(5);
			calcHelper = new CalculateHelper(profileCost, normalUsage, normalTime, standbyUsage, standbyTime);
			gc = new GraphContent(deviceName, calcHelper.costPerYear());
			totalNormalUsage += normalUsage;
			totalNormalTime += normalTime;
			totalStandbyUsage += standbyUsage;
			totalStandbyTime += standbyTime;
			totalCostPerDay +=calcHelper.costPerDay();
			totalCostPerMonth += calcHelper.costPerMonth();
			totalCostPerYear += calcHelper.costPerYear();
			
			_lv_arr.add(String.format(_res.getString(R.string.format_device_summary), 
					deviceName, normalUsage, normalTime, standbyUsage, standbyTime,
					calcHelper.toString(calcHelper.costPerDay()),
					calcHelper.toString(calcHelper.costPerMonth()), 
					calcHelper.toString(calcHelper.costPerYear())));
			_gcList.add(gc);
		}
		
		_totalCost = totalCostPerYear;
		// Now show total usage
		_lv_arr.add(String.format(_res.getString(R.string.format_device_summary), 
				"Total", totalNormalUsage, totalNormalTime, totalStandbyUsage, totalStandbyTime,
				calcHelper.toString(totalCostPerDay), calcHelper.toString(totalCostPerMonth), calcHelper.toString(totalCostPerYear)));
		
		_lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenuInfo menuInfo) {
				menu.add(_res.getString(R.string.label_edit_device)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override public boolean onMenuItemClick(MenuItem item) {
						// Get the info on which item was selected
						AdapterView.AdapterContextMenuInfo cmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
						showEditDeviceAlert(cmi.position, c);
						return true;
					}
				});
				menu.add(_res.getString(R.string.label_delete_device))
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override public boolean onMenuItemClick(MenuItem item) {
						// Get the info on which item was selected
						AdapterView.AdapterContextMenuInfo cmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
						showDeleteDeviceAlert(cmi.position, c);
						return true;
					}
				});
			} 
		});
		
		showChart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(GraphActivityHelper.showGraph(getApplicationContext(), _gcList, _totalCost));
				
			}
		});
		
		_lv.invalidate();
		// By using setAdpater method in listview we an add string array in list.
		_lv.setAdapter(new ArrayAdapter<String>(this, R.layout.device_summary_list_item, _lv_arr));
	}
	
	private void toast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		_dbAdapter.close();
	}
}
