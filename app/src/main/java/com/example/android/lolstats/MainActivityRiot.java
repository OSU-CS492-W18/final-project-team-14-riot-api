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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.lolstats.utils.LocationContract;
import com.example.android.lolstats.utils.OpenWeatherMapUtils;
import com.example.android.lolstats.utils.RiotUtils;

import java.util.ArrayList;

/**
 * Created by Nick Giles on 3/22/2018.
 */

public class MainActivityRiot extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = MainActivityRiot.class.getSimpleName();
    private static final String SUMMONER_URL_KEY = "summonerURL";
    private static final int SUMMONER_LOADER_ID = 0;
    private static final String RECENT_MATCH_URL_KEY = "recentMatchURL";
    private static final int RECENT_MATCH_LOADER_ID = 1;

    private TextView mSummonerTV;
    private RecyclerView mRecentMatchesRV;
    //private RecyclerView mRecentSummonersRV;
    private ProgressBar mLoadingPB;
    private TextView mLoadingErrorMessageTV;
    //private RecentMatchAdapter mRecentMatchAdapter;
//    private DrawerLayout mDrawerLayout;
//    private ActionBarDrawerToggle mDrawerToggle;
    private boolean SummonerInitialLoad = true;
    private boolean RecentMatchInitialLoad = true;

    //private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_main);

        //Remove shadow under action bar
        getSupportActionBar().setElevation(0);

        mSummonerTV = findViewById(R.id.tv_summoner_name);

//        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
//        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mLoadingPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);
        mRecentMatchesRV = findViewById(R.id.rv_match_items);
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
}
