package com.example.android.lolstats;

import android.provider.BaseColumns;


public class dbContract {
    private dbContract() {}
    public static class SavedSummoners implements BaseColumns {
        public static final String TABLE_NAME = "savedSummoners";
        public static final String COLUMN_SUMMONER = "summonerName";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
