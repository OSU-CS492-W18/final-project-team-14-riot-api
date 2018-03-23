package com.example.android.lolstats;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.lolstats.utils.LocationContract;
import com.example.android.lolstats.utils.OpenWeatherMapUtils;
import com.example.android.lolstats.utils.RiotUtils;

import java.util.ArrayList;

/**
 * Created by Nick Giles on 3/22/2018.
 */

public class MainActivityRiot extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, MatchAdapter.OnMatchItemClickListener {

    private static final String TAG = MainActivityRiot.class.getSimpleName();
    private static final String SUMMONER_URL_KEY = "summonerURL";
    private static final int SUMMONER_LOADER_ID = 0;
    private static final String RECENT_MATCH_URL_KEY = "recentMatchURL";
    private static final int RECENT_MATCH_LOADER_ID = 1;

    private TextView mSummonerTV;
    private RecyclerView mRecentMatchesRV;
    private RecyclerView mNavigationItemsRV;
    private ProgressBar mLoadingPB;
    private TextView mLoadingErrorMessageTV;
    private MatchAdapter mRecentMatchAdapter;
    private NavigationViewAdapter mNavigationViewAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean SummonerInitialLoad = true;
    private boolean RecentMatchInitialLoad = true;

    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_main);

        //Remove shadow under action bar
        getSupportActionBar().setElevation(0);

        mSummonerTV = findViewById(R.id.tv_summoner_name);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mLoadingPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);
        mRecentMatchesRV = findViewById(R.id.rv_match_items);

        mRecentMatchAdapter = new MatchAdapter(this, this);
        mRecentMatchesRV.setAdapter(mRecentMatchAdapter);
        mRecentMatchesRV.setLayoutManager(new LinearLayoutManager(this));
        mRecentMatchesRV.setHasFixedSize(true);

        mNavigationItemsRV = findViewById(R.id.main_nav_rv);
        mNavigationViewAdapter = new NavigationViewAdapter(this, this);
        mNavigationItemsRV.setAdapter(mNavigationViewAdapter);
        mNavigationItemsRV.setLayoutManager(new LinearLayoutManager(this));
        mNavigationItemsRV.setHasFixedSize(true);

        RecentLocationDBHelper dbHelper = new RecentLocationDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        mNavigationViewAdapter.updateRecentLocations(getRecentSummonerSearches());

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mSearchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    //saveSummoner(searchQuery);
                    updateSummonerInDatabase(searchQuery);
                    mNavigationViewAdapter.updateRecentLocations(getRecentSummonerSearches());

                }
            }
        });

        mNavigationViewAdapter.updateRecentLocations(getRecentSummonerSearches());
    }

    public void loadSummoner(String summonerName, String region) {
        mLoadingPB.setVisibility(View.VISIBLE);

        String summonerURL = RiotUtils.buildSummonerSearchURL(summonerName, region);
        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(SUMMONER_URL_KEY, summonerURL);
        LoaderManager loaderManager = getSupportLoaderManager();
        if (SummonerInitialLoad) {
            SummonerInitialLoad = false;
            loaderManager.initLoader(SUMMONER_LOADER_ID, loaderArgs, this);
        } else {
            loaderManager.restartLoader(SUMMONER_LOADER_ID, loaderArgs, this);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String loaderURL = null;
        if (args != null && id == 0) {
            loaderURL = args.getString(SUMMONER_URL_KEY);
        } else if (args != null && id == 1) {
            loaderURL = args.getString(RECENT_MATCH_URL_KEY);
        }
        return new StatsLoader(this, loaderURL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        Log.d(TAG, "got forecast from loader");
        //Handles the return for the Summoner Data call
        if (data != null && loader.getId() == 0) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);

            RiotUtils.SummonerDataResults dataResults = RiotUtils.parseSummonerDataJSON(data);

            mSummonerTV.setText(dataResults.name);
            String recentMatchURL = RiotUtils.buildRecentMatchesURL(dataResults.accountId, "NA1");

            Bundle loaderArgs = new Bundle();
            loaderArgs.putString(RECENT_MATCH_URL_KEY, recentMatchURL);
            LoaderManager loaderManager = getSupportLoaderManager();
            if (RecentMatchInitialLoad) {
                RecentMatchInitialLoad = false;
                loaderManager.initLoader(RECENT_MATCH_LOADER_ID, loaderArgs, this);
            } else {
                loaderManager.restartLoader(RECENT_MATCH_LOADER_ID, loaderArgs, this);
            }
        }
        //Handles the return for the recent Match Data call
        else if (data != null && loader.getId() == 1) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
//            mForecastItemsRV.setVisibility(View.VISIBLE);

            RiotUtils.MatchData[] matchData = RiotUtils.parseRecentMatchDataJSON(data);

            //set The adapter for the matchData
        }
        else {
//            mForecastItemsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        //We don't have to do anything here.
    }

    private ArrayList<String> getRecentSummonerSearches() {
        ArrayList<String> recentSummoners = new ArrayList<>();


        try {
            Log.d(TAG, "table name: " + dbContract.SavedSummoners.TABLE_NAME);

            Cursor cursor = mDB.query(
                    dbContract.SavedSummoners.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    dbContract.SavedSummoners.COLUMN_TIMESTAMP + " DESC"
            );


            while (cursor.moveToNext()) {
                String summoner;
                summoner = cursor.getString(cursor.getColumnIndex(dbContract.SavedSummoners.COLUMN_SUMMONER));
                recentSummoners.add(summoner);
            }
            cursor.close();

        }
        catch (Exception e) {
            Log.d(TAG, "exception in DB code: " + e);
        }

        return recentSummoners;
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
        editor.putString(getString(R.string.pref_summoner_key), location);
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

    @Override
    public void onMatchItemClick(RiotUtils.MatchData matchData) {

    }

    public void updateSummonerInDatabase(String summoner) {
        String summonerToSave = summoner;


        dbHelper dbHelper = new dbHelper(this);
        mDB = dbHelper.getWritableDatabase();

        // check to see if it's already saved in the database
        boolean isSaved = false;

        if(summonerToSave != null) {


            String sqlSelection = dbContract.SavedSummoners.COLUMN_SUMMONER + " = ?";
            String[] sqlSelectionArgs = {summonerToSave};
            Cursor cursor = mDB.query(
                    dbContract.SavedSummoners.TABLE_NAME,
                    null,
                    sqlSelection,
                    sqlSelectionArgs,
                    null,
                    null,
                    null
            );
            isSaved = cursor.getCount() > 0;
            cursor.close();

            Log.d(TAG, "isSaved:  " + isSaved);


            if (!(isSaved)) {
                ContentValues row = new ContentValues();
                row.put(dbContract.SavedSummoners.COLUMN_SUMMONER, summonerToSave);
                mDB.insert(dbContract.SavedSummoners.TABLE_NAME, null, row);
            } else {
                Log.d(TAG, "summoner already saved in DB");
            }

        }
    }
}
