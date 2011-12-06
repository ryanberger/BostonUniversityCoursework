package edu.bu.powercostestimator;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

		Bundle extras = getIntent().getExtras();
		_profileName = extras.getString("profileName");
		
		setContentView(R.layout.summary_layout);
		setTitle(_profileName + " Summary");
		
		_lv = (ListView)findViewById(R.id.summary_listview);
		_dbAdapter = DatabaseAdapter.getInstance();
		_dbAdapter.open(this);
		_res = getResources();
		
		updateSummaryList();
	}
	
	/*
	 * Remove device from profile by removing relationship in profile_device table
	 */
	private void removeDeviceFromProfile(int position, String profileName, Cursor c) {
		c.moveToPosition(position);
		_dbAdapter.removeDeviceFromProfile(c.getInt(0), profileName);
		updateSummaryList();
	}
	
	private void updateSummaryList() {
		_lv_arr.clear();
		
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
				menu.add(_res.getString(R.string.label_edit_device));
				menu.add(_res.getString(R.string.label_delete_device))
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override public boolean onMenuItemClick(MenuItem item) {
						// Get the info on which item was selected
						AdapterView.AdapterContextMenuInfo cmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
						removeDeviceFromProfile(cmi.position, _profileName, c);
						return true;
					}
				});
			} 
		});
		
		_lv.invalidate();
		// By using setAdpater method in listview we an add string array in list.
		_lv.setAdapter(new ArrayAdapter<String>(this, R.layout.device_summary_list_item, _lv_arr));
	}
	
	public void showChartListener(View view) {
		startActivity(GraphActivityHelper.showGraph(getApplicationContext(), _gcList, _totalCost));
	}
	
	private void toast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
