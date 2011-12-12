package edu.bu.powercostestimator;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class PowerCostEstimator extends TabActivity {

	DatabaseAdapter _dbAdapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		_dbAdapter = DatabaseAdapter.getInstance();
		_dbAdapter.open(this);
		_dbAdapter.createDatabase();

		TabHost tabHost = getTabHost();  // The activity TabHost
		TabHost.TabSpec spec;  // Reusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, CalculateActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("calculate").setIndicator(getString(R.string.calculate))
		.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, ProfilesActivity.class);
		spec = tabHost.newTabSpec("profiles").setIndicator(getString(R.string.profiles))
		.setContent(intent);
		tabHost.addTab(spec);

		// Set current table to "Calculate"
		tabHost.setCurrentTab(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.settings:
				// Start menu Intent
				startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
				return true;
			case R.id.help:
				// Start help Intent
				startActivity(new Intent(getApplicationContext(), HelpActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		_dbAdapter.close();
	}
}