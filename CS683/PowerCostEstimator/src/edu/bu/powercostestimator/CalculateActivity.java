package edu.bu.powercostestimator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TimePicker;

public class CalculateActivity extends Activity {
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.calculate_layout);
	        
	        String[] devices = getResources().getStringArray(R.array.devices_array);
	        
	        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewDevice);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, devices);
	        textView.setAdapter(adapter);
	        
	        
	    }
}
