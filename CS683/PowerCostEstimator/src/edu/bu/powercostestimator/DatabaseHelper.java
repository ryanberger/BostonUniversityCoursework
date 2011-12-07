package edu.bu.powercostestimator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database Helper class for initializing SQLite database
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private static String DB_NAME = "PowerCostEstimatorDb";

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources. Allows for custom
	 * database name and version. Useful for unit testing.
	 * 
	 * @param context
	 * @param databaseName
	 * @param databaseVersion
	 */
	public DatabaseHelper(Context context, String databaseName,
			int databaseVersion) {
		super(context, databaseName, null, databaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
