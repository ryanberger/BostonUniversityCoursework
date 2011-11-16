package edu.bu.powercostestimator;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class PowerCostEstimator extends TabActivity {
    
	private PowerCostEstimator mActivity;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Initialize Database TODO: only do this on first run
        DatabaseAdapter myDbAdapter = new DatabaseAdapter(mActivity);
        //myDbAdapter.createDatabase();
        
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, CalculateActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("calculate").setIndicator("Calculate")
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, ProfilesActivity.class);
        spec = tabHost.newTabSpec("profiles").setIndicator("Profiles")
                      .setContent(intent);
        tabHost.addTab(spec);

//        intent = new Intent().setClass(this, SongsActivity.class);
//        spec = tabHost.newTabSpec("songs").setIndicator("Songs",
//                          res.getDrawable(R.drawable.ic_tab_songs))
//                      .setContent(intent);
//        tabHost.addTab(spec);

        tabHost.setCurrentTab(2);
    }
}