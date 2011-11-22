package edu.bu.powercostestimator;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ProfilesActivity extends Activity {

	private DatabaseAdapter mDbAdapter;
	private ListView _lv1;
	private ArrayList<String> _lv_arr = new ArrayList<String>();
	private Resources _res;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profiles_layout);

		mDbAdapter = new DatabaseAdapter(this);
		mDbAdapter.open();
		_res = getResources();

		_lv1 = (ListView)findViewById(R.id.ListView01);
		
		updateProfileList();
		
		// By using setAdpater method in listview we an add string array in list.
		_lv1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _lv_arr));

		_lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String test = (String)_lv1.getItemAtPosition(position);
				if (test.equals(_res.getString(R.string.listview_add_new_profile)))
					showAlert();
			}
		});
	}

	private void showAlert() {
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

	private void addNewProfile(EditText profileNameInput, EditText profileCostInput) {
		String profileName = profileNameInput.getText().toString();
		String profileCostString = profileCostInput.getText().toString();
		double profileCost;

		if (profileName.trim().length() < 1) {
			Toast.makeText(getApplicationContext(), "Error: required field Name not filled out!", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (profileCostString.length() > 0) {
			profileCost = Double.parseDouble(profileCostString);
		}
		else {
			Toast.makeText(getApplicationContext(), "Error: required field Cost not filled out!", Toast.LENGTH_LONG).show();
			return;
		}
		
		mDbAdapter.addProfile(profileName, profileCost);

		updateProfileList();
	}
	
	private void updateProfileList() {
		_lv_arr.clear();
		
		_lv_arr.addAll(mDbAdapter.getProfileNames());
		_lv_arr.add(_res.getString(R.string.listview_add_new_profile));
		
		_lv1.refreshDrawableState();
	}

}
