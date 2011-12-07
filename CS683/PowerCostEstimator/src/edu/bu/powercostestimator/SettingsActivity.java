package edu.bu.powercostestimator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends PreferenceActivity {
	
	DatabaseAdapter _dbAdapter;
	private Resources _res;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.settings_layout);
		
		_dbAdapter = DatabaseAdapter.getInstance();
		_dbAdapter.open(this);
		_res = getResources();
		
		addPreferencesFromResource(R.xml.preferences);

		Preference clearData = (Preference) findPreference("clearData");
		clearData.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				showClearDataAlert();
				return true;
			}
		});
		
		Preference about = (Preference) findPreference("about");
		about.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				showAboutAlert();
				return true;
			}
		});
	}
	
	private void showClearDataAlert() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle(R.string.settings_clear_data);
		
		LinearLayout layout = new LinearLayout(this);
		// Set to vertical layout
		layout.setOrientation(1);
		TextView warning = new TextView(this);
		warning.setText(R.string.settings_clear_data_warn);
		layout.addView(warning);
		alert.setView(layout);
		
		alert.setPositiveButton(_res.getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// Remove all database tables and then move to first activity
				_dbAdapter.clearDatabase();

				Intent i = getBaseContext().getPackageManager()
						.getLaunchIntentForPackage(getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
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
	
	private void showAboutAlert() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle(R.string.settings_about);
		
		LinearLayout layout = new LinearLayout(this);
		// Set to vertical layout
		layout.setOrientation(1);
		layout.setPadding(8, 8, 8, 8);
		TextView about = new TextView(this);
		about.setText(R.string.settings_about_desc);
		layout.addView(about);
		alert.setView(layout);
		
		alert.show();
	}
}
