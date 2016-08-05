package edu.bu.powercostestimator;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfilesActivity extends Activity {

	private DatabaseAdapter _dbAdapter;
	private ListView _lv;
	private ArrayList<String> _lv_arr = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profiles_layout);

		_dbAdapter = DatabaseAdapter.getInstance();

		_lv = (ListView)findViewById(R.id.profile_listview);
		
		updateProfileList();

		_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String test = (String)_lv.getItemAtPosition(position);
				if (!test.equals(getString(R.string.listview_add_new_profile))) {
					startActivity(new Intent(view.getContext(), SummaryActivity.class)
						.putExtra("profileName", _lv.getItemAtPosition(position).toString()));
				}
				else {
					showAddNewProfileAlert();
				}
			}
		});
		_lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenuInfo menuInfo) {
				// Get the info on which item was selected
				AdapterView.AdapterContextMenuInfo cmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
				final String profileName = (String)_lv.getItemAtPosition(cmi.position);
				
				if (!profileName.equals(getString(R.string.listview_add_new_profile))) {
					menu.add(getString(R.string.label_edit_profile))
					.setOnMenuItemClickListener(new OnMenuItemClickListener() {
						@Override public boolean onMenuItemClick(MenuItem item) {
							showEditProfileAlert(profileName);
							return true;
						}
					});
					menu.add(getString(R.string.label_delete_profile))
					.setOnMenuItemClickListener(new OnMenuItemClickListener() {
						@Override public boolean onMenuItemClick(MenuItem item) {
							showDeleteProfileAlert(profileName);
							return true;
						}
					});
				}
			} 
		});
	}

	/*
	 * Show alert for adding new profile
	 */
	private void showAddNewProfileAlert() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		View alertView = View.inflate(getBaseContext(), R.layout.profiles_alert_layout, null);
		
		alert.setTitle(R.string.label_new_profile);
		alert.setView(alertView);

		final EditText profileNameInput = (EditText) alertView.findViewById(R.id.editText_alert_profile_name);
		final EditText profileCostInput = (EditText) alertView.findViewById(R.id.editText_alert_price_per_kwh);
		
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				try {
					// No ID yet since this is a new profile, so we'll use -1
					updateProfile(-1, profileNameInput, profileCostInput, true);
				} catch (SQLException e) {
					toast(getString(R.string.error_duplicate_profile_name));
				}
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
	 * Show alert for editing existing profile
	 */
	private void showEditProfileAlert(String profileName) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		View alertView = View.inflate(getBaseContext(), R.layout.profiles_alert_layout, null);
		
		alert.setTitle(R.string.label_edit_profile);
		alert.setView(alertView);
		
		final EditText profileNameInput = (EditText) alertView.findViewById(R.id.editText_alert_profile_name);
		final EditText profileCostInput = (EditText) alertView.findViewById(R.id.editText_alert_price_per_kwh);
		
		Cursor c = _dbAdapter.getProfile(profileName);
		c.moveToFirst();
		final int profileId = c.getInt(0);
		profileNameInput.setText(c.getString(1));
		profileCostInput.setText(c.getString(2));
		
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				try {
					updateProfile(profileId, profileNameInput, profileCostInput, false);
				} catch (SQLException e) {
					toast(getString(R.string.error_duplicate_profile_name));
				}
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
	 * Show alert for deleting existing profile
	 */
	private void showDeleteProfileAlert(final String profileName) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle(R.string.label_delete_profile);
		TextView tv = new TextView(this);
		tv.setText(R.string.warning_delete_profile);
		alert.setView(tv);
		
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				_dbAdapter.deleteProfile(profileName);
				updateProfileList();
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
	 * Update existing profile
	 */
	private void updateProfile(int profileId, EditText profileNameInput, EditText profileCostInput, boolean isNewProfile) {
		String profileName = profileNameInput.getText().toString();
		String profileCostString = profileCostInput.getText().toString();
		double profileCost;

		if (profileName.trim().length() < 1) {
			toast(getString(R.string.error_empty_profile_name_field));
			return;
		}
		
		if (profileCostString.length() > 0) {
			profileCost = Double.parseDouble(profileCostString);
		}
		else {
			toast(getString(R.string.error_empty_cost_field));
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
	 * Redraw profile list based on current values
	 */
	private void updateProfileList() {
		_lv_arr.clear();
		
		_lv_arr.addAll(_dbAdapter.getProfileNames());
		_lv_arr.add(getString(R.string.listview_add_new_profile));
		
		_lv.invalidate();
		// By using setAdpater method in listview we an add string array in list.
		_lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _lv_arr));
	}
	
	private void toast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
