package com.example.android.lolstats.utils;

import android.provider.BaseColumns;

/**
 * Created by Nick Giles on 3/11/2018.
 */

public class LocationContract {
    private LocationContract(){}

    public static class RecentLocation implements BaseColumns {
        public static final String TABLE_NAME = "recentLocations";
        public static final String COLUMN_LOCATION_NAME = "locationName";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
