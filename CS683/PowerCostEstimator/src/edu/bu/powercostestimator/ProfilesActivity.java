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
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ProfilesActivity extends Activity {

	private DatabaseAdapter _dbAdapter;
	private ListView _lv;
	private ArrayList<String> _lv_arr = new ArrayList<String>();
	private Resources _res;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profiles_layout);

		_dbAdapter = DatabaseAdapter.getInstance();
		_res = getResources();

		_lv = (ListView)findViewById(R.id.ListView01);
		
		// By using setAdpater method in listview we an add string array in list.
		_lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _lv_arr));
		
		updateProfileList();

		_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String test = (String)_lv.getItemAtPosition(position);
				if (test.equals(_res.getString(R.string.listview_add_new_profile))) {
					showAddNewProfileAlert();
				}
				else {
					showViewExistingProfileAlert(_lv.getItemAtPosition(position).toString());
				}
			}
		});
	}

	private void showAddNewProfileAlert() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.label_new_profile);

		// Set an EditText view to get user input
		LinearLayout layout = new LinearLayout(this);
		// Set to vertical layout
		layout.setOrientation(1);
		final EditText profileNameInput = new EditText(this);
		final EditText profileCostInput = new EditText(this);
		profileNameInput.setHint(R.string.label_profile_name);
		profileCostInput.setHint(R.string.label_price_per_kwh);
		profileCostInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		layout.addView(profileNameInput);
		layout.addView(profileCostInput);
		alert.setView(layout);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				addNewProfile(profileNameInput, profileCostInput);
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
	
	private void showViewExistingProfileAlert(String profileName) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle(R.string.label_profile_summary);
		
		Cursor c = _dbAdapter.getProfileDevices(profileName);
		final ListView lv = new ListView(this);
		ArrayList<String> deviceSummary = new ArrayList<String>();
		
		double profileCost = _dbAdapter.getProfileCost(profileName);
		double normalUsage, normalTime, standbyUsage, standbyTime,
			totalNormalUsage = 0.0, totalNormalTime = 0.0, totalStandbyUsage = 0.0, totalStandbyTime = 0.0;
		CalculateHelper calcHelper;
		
		while (c.moveToNext()) {
			normalUsage = c.getDouble(2);
			normalTime = c.getDouble(3);
			standbyUsage = c.getDouble(4);
			standbyTime = c.getDouble(5);
			calcHelper = new CalculateHelper(profileCost, normalUsage, normalTime, standbyUsage, standbyTime);
			totalNormalUsage += normalUsage;
			totalNormalTime += normalTime;
			totalStandbyUsage += standbyUsage;
			totalStandbyTime += standbyTime;
			
			deviceSummary.add(String.format(_res.getString(R.string.format_device_summary), 
					c.getString(1), "test", normalUsage, normalTime, 
					calcHelper.toString(calcHelper.costPerDay()), standbyUsage, standbyTime, 
					calcHelper.toString(calcHelper.costPerMonth()), 
					calcHelper.toString(calcHelper.costPerYear())));
		}
		// Now show total usage
		calcHelper = new CalculateHelper(profileCost, totalNormalUsage, totalNormalTime, totalStandbyUsage, totalStandbyTime);
		deviceSummary.add(String.format(_res.getString(R.string.format_device_summary), 
				"Total", "100", totalNormalUsage, totalNormalTime, 
				calcHelper.toString(calcHelper.costPerDay()), totalStandbyUsage, totalStandbyTime, 
				calcHelper.toString(calcHelper.costPerMonth()), 
				calcHelper.toString(calcHelper.costPerYear())));
		
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.device_summary_list_item, deviceSummary));
		
		lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenuInfo menuInfo) {
				menu.add("Edit Device");
				menu.add("Delete Device")
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override public boolean onMenuItemClick(MenuItem item) {
						lv.getItemAtPosition(0); // TODO: determine which device was clicked
						return true;
					}
				});
			} 
		});

		
		alert.setView(lv);
		
		alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				//Done
			}
		});
		
		alert.show();
	}

	private void addNewProfile(EditText profileNameInput, EditText profileCostInput) {
		String profileName = profileNameInput.getText().toString();
		String profileCostString = profileCostInput.getText().toString();
		double profileCost;

		if (profileName.trim().length() < 1) {
			toast(_res.getString(R.string.error_empty_profile_name_field));
			return;
		}
		
		if (profileCostString.length() > 0) {
			profileCost = Double.parseDouble(profileCostString);
		}
		else {
			toast(_res.getString(R.string.error_empty_cost_field));
			return;
		}
		
		_dbAdapter.addProfile(profileName, profileCost);

		updateProfileList();
	}
	
	private void removeDeviceFromProfile() {
		
	}
	
	private void updateProfileList() {
		_lv_arr.clear();
		
		_lv_arr.addAll(_dbAdapter.getProfileNames());
		_lv_arr.add(_res.getString(R.string.listview_add_new_profile));
		
		_lv.refreshDrawableState();
	}
	
	private void toast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
