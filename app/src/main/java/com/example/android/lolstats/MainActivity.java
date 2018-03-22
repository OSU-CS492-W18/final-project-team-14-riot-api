package com.example.android.lolstats;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.text.TextUtils;

import com.example.android.lolstats.utils.LocationContract;
import com.example.android.lolstats.utils.OpenWeatherMapUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements ForecastAdapter.OnForecastItemClickListener, LoaderManager.LoaderCallbacks<String>, SharedPreferences.OnSharedPreferenceChangeListener, NavigationViewAdapter.OnNavigationItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FORECAST_URL_KEY = "forecastURL";
    private static final int FORECAST_LOADER_ID = 0;

    private TextView mForecastLocationTV;
    private RecyclerView mForecastItemsRV;
    private RecyclerView mNavigationItemsRV;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private ForecastAdapter mForecastAdapter;
    private NavigationViewAdapter mNavigationViewAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private SQLiteDatabase mDB;

    private EditText mSearchBoxET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Remove shadow under action bar.
        getSupportActionBar().setElevation(0);

        //mForecastLocationTV = findViewById(R.id.tv_forecast_location);
        mSearchBoxET = (EditText)findViewById(R.id.et_search_box);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);
        mForecastItemsRV = findViewById(R.id.rv_forecast_items);

        mForecastAdapter = new ForecastAdapter(this, this);
        mForecastItemsRV.setAdapter(mForecastAdapter);
        mForecastItemsRV.setLayoutManager(new LinearLayoutManager(this));
        mForecastItemsRV.setHasFixedSize(true);

        mNavigationItemsRV = findViewById(R.id.main_nav_rv);
        mNavigationViewAdapter = new NavigationViewAdapter(this, this);
        mNavigationItemsRV.setAdapter(mNavigationViewAdapter);
        mNavigationItemsRV.setLayoutManager(new LinearLayoutManager(this));
        mNavigationItemsRV.setHasFixedSize(true);

        RecentLocationDBHelper dbHelper = new RecentLocationDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        mNavigationViewAdapter.updateRecentLocations(getRecentLocations());
        loadForecast(sharedPreferences, true);

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mSearchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    //saveSummonner(searchQuery);

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        mDB.close();
        super.onDestroy();
    }

    @Override
    public void onForecastItemClick(OpenWeatherMapUtils.ForecastItem forecastItem) {
        Intent intent = new Intent(this, ForecastItemDetailActivity.class);
        intent.putExtra(OpenWeatherMapUtils.ForecastItem.EXTRA_FORECAST_ITEM, forecastItem);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_location:
                showForecastLocationInMap();
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadForecast(SharedPreferences sharedPreferences, boolean initialLoad) {
        String forecastLocation = sharedPreferences.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default_value)
        );
        String temperatureUnits = sharedPreferences.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_default_value)
        );

        //mForecastLocationTV.setText(forecastLocation);
        mLoadingIndicatorPB.setVisibility(View.VISIBLE);

        String forecastURL = OpenWeatherMapUtils.buildForecastURL(forecastLocation, temperatureUnits);
        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(FORECAST_URL_KEY, forecastURL);
        LoaderManager loaderManager = getSupportLoaderManager();
        if (initialLoad) {
            loaderManager.initLoader(FORECAST_LOADER_ID, loaderArgs, this);
        } else {
            loaderManager.restartLoader(FORECAST_LOADER_ID, loaderArgs, this);
        }
    }

    public void showForecastLocationInMap() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String forecastLocation = sharedPreferences.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default_value)
        );
        Uri geoUri = Uri.parse("geo:0,0").buildUpon()
                .appendQueryParameter("q", forecastLocation)
                .build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        String forecastURL = null;
        if (args != null) {
            forecastURL = args.getString(FORECAST_URL_KEY);
        }
        return new ForecastLoader(this, forecastURL);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "got forecast from loader");
        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if (data != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mForecastItemsRV.setVisibility(View.VISIBLE);
            ArrayList<OpenWeatherMapUtils.ForecastItem> forecastItems = OpenWeatherMapUtils.parseForecastJSON(data);
            mForecastAdapter.updateForecastItems(forecastItems);
        } else {
            mForecastItemsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing ...
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        addLocationToDB(sharedPreferences);
        mNavigationViewAdapter.updateRecentLocations(getRecentLocations());
        loadForecast(sharedPreferences, false);
    }

    private ArrayList<String> getRecentLocations() {
        Cursor cursor = mDB.query(
                LocationContract.RecentLocation.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                LocationContract.RecentLocation.COLUMN_TIMESTAMP + " DESC"
        );

        ArrayList<String> recentLocations = new ArrayList<>();
        while (cursor.moveToNext()) {
            String location;
            location = cursor.getString(cursor.getColumnIndex(LocationContract.RecentLocation.COLUMN_LOCATION_NAME));
            recentLocations.add(location);
        }
        cursor.close();
        return recentLocations;
    }

    private long addLocationToDB(SharedPreferences sharedPreferences) {
        String forecastLocation = sharedPreferences.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default_value)
        );

        if (forecastLocation != null && !checkLocationInDB(forecastLocation)) {
            ContentValues values = new ContentValues();
            values.put(LocationContract.RecentLocation.COLUMN_LOCATION_NAME, forecastLocation);
            return mDB.insert(LocationContract.RecentLocation.TABLE_NAME, null, values);
        } else {
            return -1;
        }
    }

    //returns true if Location is in the DB
    private boolean checkLocationInDB(String forecastLocation) {
        boolean isInDB = true;
        if (forecastLocation != null) {
            String sqlSelection = LocationContract.RecentLocation.COLUMN_LOCATION_NAME + " = ?";
            String[] sqlSelectionArgs = {forecastLocation};
            Cursor cursor = mDB.query(
                    LocationContract.RecentLocation.TABLE_NAME,
                    null,
                    sqlSelection,
                    sqlSelectionArgs,
                    null,
                    null,
                    null
            );
            isInDB = cursor.getCount() > 0;
            cursor.close();
        }
        return isInDB;
    }

    @Override
    public void onNavigationItemClicked(String location) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_location_key), location);
        editor.apply();
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

//    private void deleteLocationFromDB(SharedPreferences sharedPreferences) {
//        String forecastLocation = sharedPreferences.getString(
//                getString(R.string.pref_location_key),
//                getString(R.string.pref_location_default_value)
//        );
//
//        if (forecastLocation != null) {
//            String sqlSelection = LocationContract.RecentLocation.COLUMN_LOCATION_NAME + " = ?";
//            String[] sqlSelectionArgs = {forecastLocation};
//            mDB.delete(LocationContract.RecentLocation.TABLE_NAME, sqlSelection, sqlSelectionArgs);
//        }
//    }
}
