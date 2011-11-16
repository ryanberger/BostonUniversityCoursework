package edu.bu.powercostestimator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ProfilesActivity extends Activity {
	
	private ListView _lv1;
	private String _lv_arr[] = {"Home", "Office", "+ Add New"};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.profiles_layout);
	    
		final Resources res = getResources();
		
		_lv1 = (ListView)findViewById(R.id.ListView01);
		// By using setAdpater method in listview we an add string array in list.
		_lv1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _lv_arr));
		
		_lv1.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		            int position, long id) {
		    			String test = (String)_lv1.getItemAtPosition(position);
		    			if (test.equals(res.getString(R.string.listview_add_new_profile)))
		    				showAlert();
		        }
		      });
	}
	
	private void showAlert() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.label_new_profile);
		//alert.setMessage("Message");

		// Set an EditText view to get user input
		LinearLayout layout = new LinearLayout(this);
		// Set to vertical layout
		layout.setOrientation(1);
		final EditText input1 = new EditText(this);
		final EditText input2 = new EditText(this);
		input1.setHint(R.string.label_profile_name);
		input2.setHint(R.string.label_price_per_kwh);
		layout.addView(input1);
		layout.addView(input2);
		alert.setView(layout);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input1.getText().toString();
				// Do something with value!
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
	}
	

			            
}
