package edu.bu.powercostestimator;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Adapter class for accessing PowerCostEstimator database.
 */
public class DatabaseAdapter {

	// Database tables
	private static final String PROFILE_CREATE = "CREATE TABLE IF NOT EXISTS profile (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ "profile_name TEXT NOT NULL, profile_price_per_kwh NUMERIC NOT NULL);";
	
    private static final String DEVICE_CREATE = "CREATE TABLE IF NOT EXISTS device (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ "device_name TEXT NOT NULL, device_power_full NUMERIC NOT NULL, device_cost_full NUMERIC NOT NULL, " 
		+ "device_power_standby NUMERIC, device_cost_standby NUMERIC, device_price_per_kwh NUMERIC);";
    
    private static final String PROFILE_DEVICE_CREATE = "CREATE TABLE IF NOT EXISTS profile_device (profile_id INTEGER NOT NULL, "
		+ "device_id INTEGER NOT NULL, FOREIGN KEY(profile_id) REFERENCES profile(_id), FOREIGN KEY(device_id) REFERENCES device(_id));";
    
	private Context context;
	private SQLiteDatabase myDatabase;
	private DatabaseHelper myDbHelper;

	public DatabaseAdapter(Context context) {
		this.context = context;
	}

	public DatabaseAdapter open() throws SQLException {
		myDbHelper = new DatabaseHelper(context);
		myDatabase = myDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		myDbHelper.close();
	}
	
	public void createDatabase() {
		myDatabase.execSQL(PROFILE_CREATE + DEVICE_CREATE + PROFILE_DEVICE_CREATE);
	}
	
	public void clearDatabase() {
		myDatabase.execSQL("DROP TABLE profile; DROP TABLE device; DROP TABLE profile_device;");
	}
	
	public void setProfile(String profileName, double costPerKwh) {
		myDatabase.execSQL("INSERT INTO profile (profile_name, profile_price_per_kwh) VALUES('" + profileName + "'," + costPerKwh + ");");
	}
	
	public void deleteProfile(String name) {
		myDatabase.rawQuery("DELETE FROM profile WHERE profile_name = '" + name + "';", null);
	}
 
	public Cursor getProfiles() {
		return myDatabase.rawQuery("SELECT * FROM profile", null);
	}
	
	public Cursor getProfile(String name) {
		return myDatabase.rawQuery("SELECT * FROM profile WHERE profile_name = '" + name + "';", null);
	}
}
