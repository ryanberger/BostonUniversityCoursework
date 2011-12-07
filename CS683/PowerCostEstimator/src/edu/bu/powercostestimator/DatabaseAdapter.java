package edu.bu.powercostestimator;

import java.util.ArrayList;

import android.content.ContentValues;
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
		+ "profile_name TEXT UNIQUE NOT NULL, profile_price_per_kwh NUMERIC NOT NULL);";

	private static final String DEVICE_CREATE = "CREATE TABLE IF NOT EXISTS device (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ "device_name TEXT NOT NULL, device_power_full NUMERIC NOT NULL, device_time_full NUMERIC NOT NULL, " 
		+ "device_power_standby NUMERIC, device_time_standby NUMERIC);";

	private static final String PROFILE_DEVICE_CREATE = "CREATE TABLE IF NOT EXISTS profile_device (profile_id INTEGER NOT NULL, "
		+ "device_id INTEGER NOT NULL, FOREIGN KEY(profile_id) REFERENCES profile(_id) ON DELETE CASCADE, "
		+ "FOREIGN KEY(device_id) REFERENCES device(_id) ON DELETE CASCADE, "
		+ "PRIMARY KEY(profile_id, device_id));";

	private Context context;
	private SQLiteDatabase _database;
	private DatabaseHelper _dbHelper;

	private DatabaseAdapter() {}

	public static DatabaseAdapter getInstance() {
		return instance;
	}

	public DatabaseAdapter open(Context context) throws SQLException {
		_dbHelper = new DatabaseHelper(context);
		_database = _dbHelper.getWritableDatabase();
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
		_dbHelper = new DatabaseHelper(context, databaseName, databaseVersion);
		_database = _dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		_dbHelper.close();
	}

	public void createDatabase() {
		_database.execSQL(PROFILE_CREATE);
		_database.execSQL(DEVICE_CREATE);
		_database.execSQL(PROFILE_DEVICE_CREATE);
	}

	public void clearDatabase() {
		_database.execSQL("DROP TABLE profile;");
		_database.execSQL("DROP TABLE device;");
		_database.execSQL("DROP TABLE profile_device;");
	}

	public void addProfile(String profileName, double costPerKwh) throws SQLException {
		_database.execSQL(String.format("INSERT INTO profile (profile_name, profile_price_per_kwh) VALUES('%1$s', %2$s);", profileName, costPerKwh));
	}

	public void updateProfile(int profileId, String profileName, double costPerKwh) throws SQLException {
		_database.execSQL(String.format("UPDATE profile SET profile_name = '%1$s', profile_price_per_kwh = %2$s WHERE _id = '%3$s';", profileName, costPerKwh, profileId));
	}

	public void deleteProfile(String profileName) {
		_database.execSQL(String.format("DELETE FROM profile WHERE profile_name = '%1$s';", profileName));
	}

	public Cursor getProfiles() {
		return _database.rawQuery("SELECT * FROM profile;", null);
	}

	public ArrayList<String> getProfileNames() {
		return asStringArrayList(_database.rawQuery("SELECT profile_name FROM profile;", null));
	}

	public Cursor getProfile(String profileName) {
		return _database.rawQuery(String.format("SELECT * FROM profile WHERE profile_name = '%1$s';", profileName), null);
	}

	public double getProfileCost(String profileName) {
		Cursor c = _database.rawQuery(String.format("SELECT profile_price_per_kwh FROM profile WHERE profile_name = '%1$s';", profileName), null);
		c.moveToFirst();
		return c.getDouble(0);
	}

	public void addDeviceToProfile(String deviceName, double powerFull, double timeFull,
			double powerStandby, double timeStandby, String profileName) {
		// Add device
		ContentValues cv = new ContentValues();
		cv.put("device_name", deviceName);
		cv.put("device_power_full", powerFull);
		cv.put("device_time_full", timeFull);
		cv.put("device_power_standby", powerStandby);
		cv.put("device_time_standby", timeStandby);
		long deviceId = _database.insert("device", null, cv);
		// Now, add device to profile
		_database.execSQL(String.format("INSERT INTO profile_device (profile_id, device_id) VALUES (%1$s, %2$s);",
				getProfileId(profileName), deviceId));
	}

	public int getProfileId(String profileName) {
		return getId(_database.rawQuery(String.format("SELECT _id FROM profile WHERE profile_name='%1$s';", profileName), null));
	}

	public Cursor getProfileDevices(String profileName) {
		return _database.rawQuery(String.format("SELECT * FROM device WHERE _id IN "
				+ "(SELECT device_id FROM profile_device WHERE profile_id IN "
				+ "(SELECT _id from profile WHERE profile_name = '%1$s'));", profileName), null);
	}

	public void deleteDevice(int deviceId) {
		_database.execSQL(String.format("DELETE FROM device WHERE device_id = %1$s", deviceId));
	}
	
	public void updateDevice(int deviceId, String deviceName, double powerFull, double timeFull, double powerStandby, double timeStandby) throws SQLException {
		_database.execSQL(String.format("UPDATE device SET device_name = '%1$s', device_power_full = %2$s, device_time_full = %3$s, device_power_standby = %4$s, "
				+ "device_time_standby = %5$s WHERE _id = '%6$s';", deviceName, powerFull, timeFull, powerStandby, timeStandby, deviceId));
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
