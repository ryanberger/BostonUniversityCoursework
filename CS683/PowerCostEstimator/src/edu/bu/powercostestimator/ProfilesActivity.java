package edu.bu.powercostestimator;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ProfilesActivity extends Activity {

	private DatabaseAdapter mDbAdapter;
	private ListView _lv;
	private ArrayList<String> _lv_arr = new ArrayList<String>();
	private Resources _res;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profiles_layout);

		mDbAdapter = DatabaseAdapter.getInstance();
		//mDbAdapter.open(this);
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
		
		Cursor c = mDbAdapter.getProfileDevices(profileName);
		TableLayout tl = new TableLayout(this);
		tl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		while (c.moveToNext()) {
			TableRow tr = new TableRow(this);
			tr.setClickable(true);
			tr.setFocusable(true);
			tr.setFocusableInTouchMode(false);
			TextView tv = new TextView(this);
			tv.setText(c.getString(1));
			tr.addView(tv);
			tl.addView(tr);
			TextView tv2 = new TextView(this);
			tv2.setText(c.getString(2) + "kWh/" + c.getString(3) + "hours");
			tr.addView(tv2);
//			TextView tv3 = new TextView(this);
//			tv3.setText(c.getString(3) + "hours");
//			tr.addView(tv3);
			tr.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						v.setBackgroundColor(Color.YELLOW);
					} else {
						v.setBackgroundColor(Color.DKGRAY);
					}
					// TODO Auto-generated method stub
					return false;
				}
			});
		}
		alert.setView(tl);
		
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
			Toast.makeText(getApplicationContext(), "ERROR: required field Name not filled out!", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (profileCostString.length() > 0) {
			profileCost = Double.parseDouble(profileCostString);
		}
		else {
			Toast.makeText(getApplicationContext(), "ERROR: required field Cost not filled out!", Toast.LENGTH_LONG).show();
			return;
		}
		
		mDbAdapter.addProfile(profileName, profileCost);

		updateProfileList();
	}
	
	private void updateProfileList() {
		_lv_arr.clear();
		
		_lv_arr.addAll(mDbAdapter.getProfileNames());
		_lv_arr.add(_res.getString(R.string.listview_add_new_profile));
		
		_lv.refreshDrawableState();
	}

}
