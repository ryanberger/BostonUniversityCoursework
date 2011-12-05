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
import android.widget.TextView;
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

		_lv = (ListView)findViewById(R.id.profile_listview);
		
		updateProfileList();

		_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String test = (String)_lv.getItemAtPosition(position);
				if (!test.equals(_res.getString(R.string.listview_add_new_profile))) {
					showViewExistingProfileAlert(_lv.getItemAtPosition(position).toString());
				}
				else {
					showAddNewProfileAlert();
				}
			}
		});
		_lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenuInfo menuInfo) {
				menu.add(_res.getString(R.string.label_edit_profile))
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override public boolean onMenuItemClick(MenuItem item) {
						// Get the info on which item was selected
						AdapterView.AdapterContextMenuInfo cmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
						String test = (String)_lv.getItemAtPosition(cmi.position);
						if (!test.equals(_res.getString(R.string.listview_add_new_profile))) {
							showEditProfileAlert(test);
						}
						return true;
					}
				});
				menu.add(_res.getString(R.string.label_delete_profile))
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override public boolean onMenuItemClick(MenuItem item) {
						// Get the info on which item was selected
						AdapterView.AdapterContextMenuInfo cmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
						String test = (String)_lv.getItemAtPosition(cmi.position);
						if (!test.equals(_res.getString(R.string.listview_add_new_profile))) {
							showDeleteProfileAlert(test);
						}
						return true;
					}
				});
			} 
		});
	}

	private void showAddNewProfileAlert() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		final EditText profileNameInput = new EditText(this);
		final EditText profileCostInput = new EditText(this);
		
		alert.setTitle(R.string.label_new_profile);
		alert.setView(getProfileLayout(profileNameInput, profileCostInput));

		alert.setPositiveButton(_res.getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// No ID yet since this is a new profile, so we'll use -1
				updateProfile(-1, profileNameInput, profileCostInput, true);
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
	
	private void showEditProfileAlert(String profileName) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		final EditText profileNameInput = new EditText(this);
		final EditText profileCostInput = new EditText(this);
		
		alert.setTitle(R.string.label_edit_profile);
		alert.setView(getProfileLayout(profileNameInput, profileCostInput));
		
		Cursor c = _dbAdapter.getProfile(profileName);
		c.moveToFirst();
		final int profileId = c.getInt(0);
		profileNameInput.setText(c.getString(1));
		profileCostInput.setText(c.getString(2));
		
		alert.setPositiveButton(_res.getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				updateProfile(profileId, profileNameInput, profileCostInput, false);
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
	
	private void showDeleteProfileAlert(final String profileName) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle(R.string.label_delete_profile);
		TextView tv = new TextView(this);
		tv.setText(R.string.warning_delete_profile);
		alert.setView(tv);
		
		alert.setPositiveButton(_res.getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				_dbAdapter.deleteProfile(profileName);
				updateProfileList();
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
	
	private LinearLayout getProfileLayout(EditText profileNameInput, EditText profileCostInput) {
		// Set an EditText view to get user input
		LinearLayout layout = new LinearLayout(this);
		// Set to vertical layout
		layout.setOrientation(1);
		TextView profileName = new TextView(this);
		TextView profileCost = new TextView(this);
		profileName.setText(R.string.label_profile_name);
		profileCost.setText(R.string.label_price_per_kwh);
		profileNameInput.setHint(R.string.hint_profile_name);
		profileCostInput.setHint(R.string.hint_price_per_kwh);
		profileCostInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		layout.addView(profileName);
		layout.addView(profileNameInput);
		layout.addView(profileCost);
		layout.addView(profileCostInput);
		return layout;
	}
	
	private void showViewExistingProfileAlert(final String profileName) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle(R.string.label_profile_summary);
		
		final Cursor c = _dbAdapter.getProfileDevices(profileName);
		final ArrayList<GraphContent> gcList = new ArrayList<GraphContent>();
		final ListView lv = new ListView(this);
		ArrayList<String> deviceSummary = new ArrayList<String>();
		
		double profileCost = _dbAdapter.getProfileCost(profileName);
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
			
			deviceSummary.add(String.format(_res.getString(R.string.format_device_summary), 
					deviceName, normalUsage, normalTime, standbyUsage, standbyTime,
					calcHelper.toString(calcHelper.costPerDay()),
					calcHelper.toString(calcHelper.costPerMonth()), 
					calcHelper.toString(calcHelper.costPerYear())));
			gcList.add(gc);
		}
		
		final double totalCost = totalCostPerYear;
		// Now show total usage
		deviceSummary.add(String.format(_res.getString(R.string.format_device_summary), 
				"Total", totalNormalUsage, totalNormalTime, totalStandbyUsage, totalStandbyTime,
				calcHelper.toString(totalCostPerDay), calcHelper.toString(totalCostPerMonth), calcHelper.toString(totalCostPerYear)));
		
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.device_summary_list_item, deviceSummary));
		
		lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenuInfo menuInfo) {
				menu.add(_res.getString(R.string.label_edit_device));
				menu.add(_res.getString(R.string.label_delete_device))
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override public boolean onMenuItemClick(MenuItem item) {
						// Get the info on which item was selected
						AdapterView.AdapterContextMenuInfo cmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
						removeDeviceFromProfile(cmi.position, profileName, c);
						return true;
					}
				});
			} 
		});
		
		alert.setView(lv);
		
		alert.setPositiveButton(_res.getString(R.string.done), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				//Done
			}
		});
		
		alert.setNeutralButton("Show Chart", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				startActivity(GraphActivityHelper.showGraph(getApplicationContext(), gcList, totalCost));
			}
		});
		
		alert.show();
	}

	private void updateProfile(int profileId, EditText profileNameInput, EditText profileCostInput, boolean isNewProfile) {
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
		
		if (isNewProfile) {
			_dbAdapter.addProfile(profileName, profileCost);
		} else {
			_dbAdapter.updateProfile(profileId, profileName, profileCost);
		}
		
		updateProfileList();
	}
	
	/*
	 * Remove device from profile by removing relationship in profile_device table
	 */
	private void removeDeviceFromProfile(int position, String profileName, Cursor c) {
		c.moveToPosition(position);
		_dbAdapter.removeDeviceFromProfile(c.getInt(0), profileName);
	}
	
	private void updateProfileList() {
		_lv_arr.clear();
		
		_lv_arr.addAll(_dbAdapter.getProfileNames());
		_lv_arr.add(_res.getString(R.string.listview_add_new_profile));
		
		_lv.invalidate();
		// By using setAdpater method in listview we an add string array in list.
		_lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _lv_arr));
	}
	
	private void toast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
