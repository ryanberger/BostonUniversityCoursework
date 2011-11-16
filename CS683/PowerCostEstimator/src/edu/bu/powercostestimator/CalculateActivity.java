package edu.bu.powercostestimator;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class CalculateActivity extends Activity {
	
	private CalculateActivity mActivity;
	private DatabaseAdapter myDbAdapter;
	private AutoCompleteTextView _deviceField;
	private EditText _powerFullField;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        myDbAdapter = new DatabaseAdapter(mActivity);
        
        setContentView(R.layout.calculate_layout);
        _deviceField = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_device);
        _powerFullField = (EditText) findViewById(R.id.editText_power_full);
        
        String[] devices = getResources().getStringArray(R.array.devices_array);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, devices);
        _deviceField.setAdapter(adapter);
    }
	 
	/*
	 * Override default onKeyUp behavior to account for auto-complete field.
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			if (_deviceField.hasFocus()) {
				// sends focus to another field (user pressed "Next")
		    	_powerFullField.requestFocus();
			    return true;
		    }
		    else {
		    	// use default behavior
	    	 	return super.onKeyUp(keyCode, event);
		    }
		}
		return false;
	}
}
