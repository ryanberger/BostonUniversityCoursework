package edu.bu.powercostestimator;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Adapter class for accessing PowerCostEstimator database.
 */
public class DatabaseAdapter {

	private static final DatabaseAdapter instance = new DatabaseAdapter();
	
	// Database tables
	private static final String PROFILE_CREATE = "CREATE TABLE IF NOT EXISTS profile (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ "profile_name TEXT NOT NULL, profile_price_per_kwh NUMERIC NOT NULL);";
	
    private static final String DEVICE_CREATE = "CREATE TABLE IF NOT EXISTS device (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ "device_name TEXT NOT NULL, device_power_full NUMERIC NOT NULL, device_time_full NUMERIC NOT NULL, " 
		+ "device_power_standby NUMERIC, device_time_standby NUMERIC);";
    
    private static final String PROFILE_DEVICE_CREATE = "CREATE TABLE IF NOT EXISTS profile_device (profile_id INTEGER NOT NULL, "
		+ "device_id INTEGER NOT NULL, FOREIGN KEY(profile_id) REFERENCES profile(_id), FOREIGN KEY(device_id) REFERENCES device(_id), "
    	+ "PRIMARY KEY(profile_id, device_id));";
    
	private Context context;
	private SQLiteDatabase myDatabase;
	private DatabaseHelper myDbHelper;

	private DatabaseAdapter() {}
	
	public static DatabaseAdapter getInstance() {
		return instance;
	}

	public DatabaseAdapter open(Context context) throws SQLException {
		myDbHelper = new DatabaseHelper(context);
		myDatabase = myDbHelper.getWritableDatabase();
		return this;
	}
	
	 /**
     * Open
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * Accepts name and version for database. Useful for unit testing.
     * @param databaseName
     * @param databaseVersion
     */
	public DatabaseAdapter open(String databaseName, int databaseVersion) throws SQLException {
		myDbHelper = new DatabaseHelper(context, databaseName, databaseVersion);
		myDatabase = myDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		myDbHelper.close();
	}
	
	public void createDatabase() {
		myDatabase.execSQL(PROFILE_CREATE);
		myDatabase.execSQL(DEVICE_CREATE);
		myDatabase.execSQL(PROFILE_DEVICE_CREATE);
	}
	
	public void clearDatabase() {
		myDatabase.execSQL("DROP TABLE profile;");
		myDatabase.execSQL("DROP TABLE device;");
		myDatabase.execSQL("DROP TABLE profile_device;");
	}
	
	public void addProfile(String profileName, double costPerKwh) {
		myDatabase.execSQL("INSERT INTO profile (profile_name, profile_price_per_kwh) VALUES('" + profileName + "'," + costPerKwh + ");");
	}
	
	public void deleteProfile(String profileName) {
		myDatabase.rawQuery("DELETE FROM profile WHERE profile_name = '" + profileName + "';", null);
	}
 
	public Cursor getProfiles() {
		return myDatabase.rawQuery("SELECT * FROM profile;", null);
	}
	
	public ArrayList<String> getProfileNames() {
		return asStringArrayList(myDatabase.rawQuery("SELECT profile_name FROM profile;", null));
	}
	
	public Cursor getProfile(String profileName) {
		return myDatabase.rawQuery("SELECT * FROM profile WHERE profile_name = '"+profileName+"';", null);
	}
	
	public double getProfileCost(String profileName) {
		Cursor c = myDatabase.rawQuery("SELECT profile_price_per_kwh FROM profile WHERE profile_name = '"+profileName+"';", null);
		c.moveToFirst();
		return c.getDouble(0);
	}
	
	public void addDeviceToProfile(String deviceName, double powerFull, double timeFull,
			double powerStandby, double timeStandby, String profileName) {
		// Add device
		myDatabase.execSQL("INSERT INTO device (device_name, device_power_full, device_time_full, device_power_standby, device_time_standby) VALUES ('"
				+ deviceName + "', " + powerFull + ", " + timeFull + ", " + powerStandby + ", " + timeStandby + ");");
		// Now, add device to profile
		myDatabase.execSQL("INSERT INTO profile_device (profile_id, device_id) VALUES ("+getProfileId(profileName)
				+","+getDeviceId(deviceName, powerFull, timeFull)+");");
	}
	
	public int getDeviceId(String deviceName, double powerFull, double timeFull) {
		return getId(myDatabase.rawQuery("SELECT _id FROM device WHERE device_name='"+deviceName+"';", null));
	}
	
	public int getProfileId(String profileName) {
		return getId(myDatabase.rawQuery("SELECT _id FROM profile WHERE profile_name='"+profileName+"';", null));
	}
	
	public Cursor getProfileDevices(String profileName) {
		return myDatabase.rawQuery("SELECT * FROM device WHERE _id IN "
				+ "(SELECT device_id FROM profile_device WHERE profile_id IN "
				+ "(SELECT _id from profile WHERE profile_name='"+profileName+"'));", null);
	}
	
	private int getId(Cursor cursor) {
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	private ArrayList<String> asStringArrayList(Cursor dbCursor) {
		ArrayList<String> outArrayList = new ArrayList<String>();
		
		while (dbCursor.moveToNext()) {
			outArrayList.add(dbCursor.getString(dbCursor.getColumnIndex(dbCursor.getColumnName(0))));
		}
		
		return outArrayList;
	}
}
