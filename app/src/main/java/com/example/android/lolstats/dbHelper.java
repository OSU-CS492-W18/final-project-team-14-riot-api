package com.example.android.lolstats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weatherlocations.db";
    private static int DATABASE_VERSION = 1;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SAVED_REPOS_TABLE =
                "CREATE TABLE " + dbContract.SavedSummoners.TABLE_NAME + "(" +
                        dbContract.SavedSummoners._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        dbContract.SavedSummoners.COLUMN_SUMMONER + " TEXT NOT NULL, " +
                        dbContract.SavedSummoners.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ");";
        db.execSQL(SQL_CREATE_SAVED_REPOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + dbContract.SavedSummoners.TABLE_NAME + ";");
        onCreate(db);
    }
}
