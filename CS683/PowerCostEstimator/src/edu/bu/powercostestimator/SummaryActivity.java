package edu.bu.powercostestimator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

public class SummaryActivity extends Activity {

	private DatabaseAdapter _dbAdapter;
	private ListView _lv;
	private ArrayList<String> _lv_arr = new ArrayList<String>();
	private ArrayList<DeviceContent> _dcList = new ArrayList<DeviceContent>();
	private ArrayList<GraphContent> _gcList = new ArrayList<GraphContent>();
	private double _totalCost;
	private String _profileName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get profile name from ProfileActivity
		Bundle extras = getIntent().getExtras();
		_profileName = extras.getString("profileName");
		
		setContentView(R.layout.summary_layout);
		setTitle(String.format(getString(R.string.label_summary), _profileName));
		
		_lv = (ListView)findViewById(R.id.summary_listview);
		_dbAdapter = DatabaseAdapter.getInstance();
		_dbAdapter.open(this);
		
		updateSummaryList();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (_gcList.size() > 0) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.summary_menu, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.menu_show_chart:
				startActivity(GraphActivityHelper.showGraph(getApplicationContext(), _gcList, _totalCost));
				return true;
			case R.id.menu_export:
				showExportAlert();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/*
	 * Open a new alert window to edit the currently selected device
	 */
	private void showEditDeviceAlert(int position, Cursor c) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		View alertView = View.inflate(getBaseContext(), R.layout.summary_alert_layout, null);
		
		alert.setTitle(R.string.label_edit_device);
		alert.setView(alertView);
		
		final EditText deviceNameInput = (EditText) alertView.findViewById(R.id.editText_alert_device_name);
		final EditText devicePowerFullInput = (EditText) alertView.findViewById(R.id.editText_alert_power_full);
		final EditText deviceTimeFullInput = (EditText) alertView.findViewById(R.id.editText_alert_time_full);
		final EditText devicePowerStandbyInput = (EditText) alertView.findViewById(R.id.editText_alert_power_standby);
		final EditText deviceTimeStandbyInput = (EditText) alertView.findViewById(R.id.editText_alert_time_standby);
		final int deviceId = getDeviceId(position, c);
		
		deviceNameInput.setText(c.getString(1));
		devicePowerFullInput.setText(c.getString(2));
		deviceTimeFullInput.setText(c.getString(3));
		devicePowerStandbyInput.setText(c.getString(4));
		deviceTimeStandbyInput.setText(c.getString(5));
		
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				editDevice(deviceId, deviceNameInput, devicePowerFullInput, deviceTimeFullInput,
						devicePowerStandbyInput, deviceTimeStandbyInput);
			}
		});

		alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
			toast(getString(R.string.error_empty_profile_name_field));
			return;
		}
		
		String powerFullString = powerFullField.getText().toString().trim();
		if (powerFullString.length() > 0) {
			powerFull = Double.parseDouble(powerFullString);
		}
		else {
			toast(getString(R.string.error_empty_power_field));
			return;
		}
		
		String timeFullString = timeFullField.getText().toString().trim();
		if (timeFullString.length() > 0) {
			timeFull = Double.parseDouble(timeFullString);
			if (timeFull > 24) {
				toast(getString(R.string.error_over_24_hours));
				return;
			}
		}
		else {
			toast(getString(R.string.error_empty_time_field));
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
				toast(getString(R.string.error_over_24_hours));
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
		
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				deleteDevice(deviceId);
			}
		});

		alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
		_dcList.clear();
		_gcList.clear();
		
		final Cursor c = _dbAdapter.getProfileDevices(_profileName);
		double profileCost = _dbAdapter.getProfileCost(_profileName);
		double totalNormalUsage = 0.0, totalNormalTime = 0.0, totalStandbyUsage = 0.0,
			totalStandbyTime = 0.0, totalCostPerDay = 0.0, totalCostPerMonth = 0.0, totalCostPerYear = 0.0;
		DeviceContent dc;
		GraphContent gc;
		
		while (c.moveToNext()) {
			// Initialize objects with device values
			CalculateHelper calcHelper = new CalculateHelper(profileCost, c.getDouble(2), c.getDouble(3),
					c.getDouble(4), c.getDouble(5));
			dc = new DeviceContent(c.getString(1), c.getDouble(2), c.getDouble(3), c.getDouble(4), c.getDouble(5),
					calcHelper.costPerDay(), calcHelper.costPerMonth(), calcHelper.costPerYear());
			gc = new GraphContent(dc.getDeviceName(), calcHelper.costPerYear());
			
			totalNormalUsage += dc.getNormalUsage();
			totalNormalTime += dc.getNormalTime();
			totalStandbyUsage += dc.getStandbyUsage();
			totalStandbyTime += dc.getStandbyTime();
			totalCostPerDay += calcHelper.costPerDay();
			totalCostPerMonth += calcHelper.costPerMonth();
			totalCostPerYear += calcHelper.costPerYear();
			
			_lv_arr.add(String.format(getString(R.string.format_device_summary), 
					dc.getDeviceName(), dc.getNormalUsage(), dc.getNormalTime(), dc.getStandbyUsage(), dc.getStandbyTime(),
					CalculateHelper.toString(calcHelper.costPerDay()),
					CalculateHelper.toString(calcHelper.costPerMonth()), 
					CalculateHelper.toString(calcHelper.costPerYear())));
			_dcList.add(dc);
			_gcList.add(gc);
		}
		
		_totalCost = totalCostPerYear;
		// Only show results if devices have been added to profile
		if (c.getCount() > 0) {
			// Now show total usage
			dc = new DeviceContent(getString(R.string.total), totalNormalUsage, totalNormalTime, totalStandbyUsage, totalStandbyTime,
					totalCostPerDay, totalCostPerMonth, totalCostPerYear);
			_dcList.add(dc);
			_lv_arr.add(String.format(getString(R.string.format_device_summary), 
					getString(R.string.total), totalNormalUsage, totalNormalTime, totalStandbyUsage, totalStandbyTime,
					CalculateHelper.toString(totalCostPerDay), CalculateHelper.toString(totalCostPerMonth),
						CalculateHelper.toString(totalCostPerYear)));
			
			_lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
				@Override public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenuInfo menuInfo) {
					// Get the info on which item was selected
					final AdapterView.AdapterContextMenuInfo cmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
					
					// Ensure that item selected is not last item in listview (which shows total values)
					if (cmi.position < _lv_arr.size() - 1) {
						menu.add(getString(R.string.label_edit_device)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
							@Override public boolean onMenuItemClick(MenuItem item) {
								
								showEditDeviceAlert(cmi.position, c);
								return true;
							}
						});
						menu.add(getString(R.string.label_delete_device))
						.setOnMenuItemClickListener(new OnMenuItemClickListener() {
							@Override public boolean onMenuItemClick(MenuItem item) {
								showDeleteDeviceAlert(cmi.position, c);
								return true;
							}
						});
					}
				} 
			});
		} else {
			_lv_arr.add(getString(R.string.warning_no_devices));
		}
		
		_lv.invalidate();
		// By using setAdpater method in listview we an add string array in list.
		_lv.setAdapter(new ArrayAdapter<String>(this, R.layout.device_summary_list_item, _lv_arr));
	}
	
	private void showExportAlert() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle(R.string.label_export_profile);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		TextView tv = new TextView(this);
		final EditText et = new EditText(this);
		tv.setText("Specify file name:");
		et.setHint("filename.csv");
		layout.addView(tv);
		layout.addView(et);
		
		alert.setView(layout);
		
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				export(et.getText().toString().trim());
			}
		});

		alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});
		
		alert.show();
	}
	
	private void export(String filename) {
		CSVWriter writer = null;
		try 
		{
			File sdCard = Environment.getExternalStorageDirectory();
			String outputPath = sdCard.getAbsolutePath() + "/" + filename;
			writer = new CSVWriter(new FileWriter(outputPath), ',');
			
			// array of headings
			String[] values = getString(R.string.csv_headings).split("#");
			writer.writeNext(values);
			
			// Fill up CSV file with entries from profile
			for (DeviceContent dc : _dcList) {
				values = new String[] { dc.getDeviceName(), Double.toString(dc.getNormalUsage()), Double.toString(dc.getNormalTime()),
						Double.toString(dc.getStandbyUsage()), Double.toString(dc.getStandbyTime()), Double.toString(dc.getDailyCost()),
						Double.toString(dc.getMonthlyCost()), Double.toString(dc.getYearlyCost()) };
				writer.writeNext(values);
			}
			
			writer.close();
			toast(String.format(getString(R.string.success_export), outputPath));
		} 
		catch (IOException e)
		{
			toast(String.format(getString(R.string.error_generic), e));
		}
	}
	
	private void toast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

}
