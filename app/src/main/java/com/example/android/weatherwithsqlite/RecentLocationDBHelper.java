package com.example.android.weatherwithsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.weatherwithsqlite.utils.LocationContract;

/**
 * Created by Nick Giles on 3/11/2018.
 */

public class RecentLocationDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "openWeatherMapRecentLocations.db";
    private static final int DATABASE_VERSION = 1;

    public RecentLocationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_RECENT_LOCATIONS_TABLE =
                "CREATE TABLE " + LocationContract.RecentLocation.TABLE_NAME + " (" +
                        LocationContract.RecentLocation._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        LocationContract.RecentLocation.COLUMN_LOCATION_NAME + " TEXT NOT NULL, " +
                        LocationContract.RecentLocation.COLUMN_TIMESTAMP +
                        " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ");";

        db.execSQL(SQL_RECENT_LOCATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LocationContract.RecentLocation.TABLE_NAME);
        onCreate(db);
    }
}
