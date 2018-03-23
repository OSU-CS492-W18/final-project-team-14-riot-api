package com.example.android.lolstats;

import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.lolstats.utils.RiotUtils;

import java.text.DateFormat;

/**
 * Created by shane on 3/22/18.
 */

public class MatchItemDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final String TAG = MatchItemDetailActivity.class.getSimpleName();

    private static final String MATCH_HASHTAG = "#LOLStats";
    private static final DateFormat DATE_FORMATTER = DateFormat.getDateTimeInstance();
    private RiotUtils.MatchData mMatchItem;
    private RiotUtils.DetailedMatchData mDetailedMatchItem;
    private static final String DETAILED_MATCH_VIEW_URL_KEY = "matchviewURL";
    private static final int DETAILED_MATCH_VIEW_ID = 2;
    private ProgressBar mLoadingPB;
    private TextView mLoadingErrorMessageTV;
    private static boolean mMatchDetailInitialLoad = true;
    private LinearLayout mContentView;

    private TextView mDateTV;
    private TextView mBlueTeamWinTV;
    private TextView mRedTeamWinTV;

    private TextView mBlueTeamSummonerTV1;
    private TextView mBlueTeamSummonerTV2;
    private TextView mBlueTeamSummonerTV3;
    private TextView mBlueTeamSummonerTV4;
    private TextView mBlueTeamSummonerTV5;
    private TextView mBlueTeamKdaTV1;
    private TextView mBlueTeamKdaTV2;
    private TextView mBlueTeamKdaTV3;
    private TextView mBlueTeamKdaTV4;
    private TextView mBlueTeamKdaTV5;
    private TextView mBlueTeamCsTV1;
    private TextView mBlueTeamCsTV2;
    private TextView mBlueTeamCsTV3;
    private TextView mBlueTeamCsTV4;
    private TextView mBlueTeamCsTV5;

    private TextView mRedTeamSummonerTV1;
    private TextView mRedTeamSummonerTV2;
    private TextView mRedTeamSummonerTV3;
    private TextView mRedTeamSummonerTV4;
    private TextView mRedTeamSummonerTV5;
    private TextView mRedTeamKdaTV1;
    private TextView mRedTeamKdaTV2;
    private TextView mRedTeamKdaTV3;
    private TextView mRedTeamKdaTV4;
    private TextView mRedTeamKdaTV5;
    private TextView mRedTeamCsTV1;
    private TextView mRedTeamCsTV2;
    private TextView mRedTeamCsTV3;
    private TextView mRedTeamCsTV4;
    private TextView mRedTeamCsTV5;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_match_view);

        mDateTV = findViewById(R.id.tv_detailed_match_date);
        mBlueTeamWinTV = findViewById(R.id.tv_match_outcome_blue);
        mRedTeamWinTV = findViewById(R.id.tv_match_outcome_red);

        mLoadingPB = findViewById(R.id.pb_loading_indicator_match_detail);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message_match_detail);
        mContentView = findViewById(R.id.detailed_match_content);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mBlueTeamSummonerTV1 = findViewById(R.id.tv_detailed_name_P1);
        mBlueTeamSummonerTV2 = findViewById(R.id.tv_detailed_name_P2);
        mBlueTeamSummonerTV3 = findViewById(R.id.tv_detailed_name_P3);
        mBlueTeamSummonerTV4 = findViewById(R.id.tv_detailed_name_P4);
        mBlueTeamSummonerTV5 = findViewById(R.id.tv_detailed_name_P5);
        mBlueTeamKdaTV1 = findViewById(R.id.tv_detailed_kda_P1);
        mBlueTeamKdaTV2 = findViewById(R.id.tv_detailed_kda_P2);
        mBlueTeamKdaTV3 = findViewById(R.id.tv_detailed_kda_P3);
        mBlueTeamKdaTV4 = findViewById(R.id.tv_detailed_kda_P4);
        mBlueTeamKdaTV5 = findViewById(R.id.tv_detailed_kda_P5);
        mBlueTeamCsTV1 = findViewById(R.id.tv_detailed_cs_P1);
        mBlueTeamCsTV2 = findViewById(R.id.tv_detailed_cs_P2);
        mBlueTeamCsTV3 = findViewById(R.id.tv_detailed_cs_P3);
        mBlueTeamCsTV4 = findViewById(R.id.tv_detailed_cs_P4);
        mBlueTeamCsTV5 = findViewById(R.id.tv_detailed_cs_P5);

        mRedTeamSummonerTV1 = findViewById(R.id.tv_detailed_name_P6);
        mRedTeamSummonerTV2 = findViewById(R.id.tv_detailed_name_P7);
        mRedTeamSummonerTV3 = findViewById(R.id.tv_detailed_name_P8);
        mRedTeamSummonerTV4 = findViewById(R.id.tv_detailed_name_P9);
        mRedTeamSummonerTV5 = findViewById(R.id.tv_detailed_name_P10);
        mRedTeamKdaTV1 = findViewById(R.id.tv_detailed_kda_P6);
        mRedTeamKdaTV2 = findViewById(R.id.tv_detailed_kda_P7);
        mRedTeamKdaTV3 = findViewById(R.id.tv_detailed_kda_P8);
        mRedTeamKdaTV4 = findViewById(R.id.tv_detailed_kda_P9);
        mRedTeamKdaTV5 = findViewById(R.id.tv_detailed_kda_P10);
        mRedTeamCsTV1 = findViewById(R.id.tv_detailed_cs_P6);
        mRedTeamCsTV2 = findViewById(R.id.tv_detailed_cs_P7);
        mRedTeamCsTV3 = findViewById(R.id.tv_detailed_cs_P8);
        mRedTeamCsTV4 = findViewById(R.id.tv_detailed_cs_P9);
        mRedTeamCsTV5 = findViewById(R.id.tv_detailed_cs_P10);

        String region = sharedPreferences.getString(
                getString(R.string.pref_region_key),
                getString(R.string.pref_region_default_value)
        );

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RiotUtils.MatchData.EXTRA_MATCH_ITEM)) {
            mMatchItem = (RiotUtils.MatchData) intent.getSerializableExtra(
                    RiotUtils.MatchData.EXTRA_MATCH_ITEM
            );
            loadDetailedMatch(mMatchItem.gameId, region);
        }
    }

    public void loadDetailedMatch(long gameId, String region) {
        mLoadingPB.setVisibility(View.VISIBLE);

        String detailedMatchURL = RiotUtils.buildDetailedMatchURL(gameId, region);
        Log.d(TAG, "detailedMatchURL: " + detailedMatchURL);

        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(DETAILED_MATCH_VIEW_URL_KEY, detailedMatchURL);
        LoaderManager loaderManager = getSupportLoaderManager();
        if (mMatchDetailInitialLoad) {
            mMatchDetailInitialLoad = false;
            loaderManager.initLoader(DETAILED_MATCH_VIEW_ID, loaderArgs, this);
        } else {
            loaderManager.restartLoader(DETAILED_MATCH_VIEW_ID, loaderArgs, this);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareMatchItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareMatchItem() {
        if (mDetailedMatchItem != null) {
//            String shareText = "Weather for " + mForecastLocation +
//                    ", " + DATE_FORMATTER.format(mForecastItem.dateTime) +
//                    ": " + mForecastItem.temperature + mTemperatureUnitsAbbr +
//                    " - " + mForecastItem.description +
//                    " " + FORECAST_HASHTAG;
            String shareText = "ThisIsTheMatchItem";
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareText)
                    .setChooserTitle(R.string.share_chooser_title)
                    .startChooser();
        }
    }

//    @Override
//    private void fillInLayoutText(RiotUtils.DetailedMatchData detailedMatchItem) {
//
//    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        String detailedMatchViewURL = null;
        if (args != null && id == 2) {
            detailedMatchViewURL = args.getString(DETAILED_MATCH_VIEW_URL_KEY);
        }
        return new StatsLoader(this, DETAILED_MATCH_VIEW_URL_KEY);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "got forecast from loader");
        mLoadingPB.setVisibility(View.INVISIBLE);
        if (data != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mContentView.setVisibility(View.VISIBLE);
            mDetailedMatchItem = RiotUtils.parseDetailedMatchData(data);
            // set text views
            mDateTV.setText("LULDATEHOLDER");
            mBlueTeamWinTV.setText(mDetailedMatchItem.teams[0].win);
            mRedTeamWinTV.setText(mDetailedMatchItem.teams[1].win);

            mBlueTeamSummonerTV1.setText(mDetailedMatchItem.participantIdentities[0].player.summonerName);
            mBlueTeamSummonerTV2.setText(mDetailedMatchItem.participantIdentities[1].player.summonerName);
            mBlueTeamSummonerTV3.setText(mDetailedMatchItem.participantIdentities[2].player.summonerName);
            mBlueTeamSummonerTV4.setText(mDetailedMatchItem.participantIdentities[3].player.summonerName);
            mBlueTeamSummonerTV5.setText(mDetailedMatchItem.participantIdentities[4].player.summonerName);
//            mBlueTeamKdaTV1.setText(mDetailedMatchItem);
//            mBlueTeamKdaTV2.setText(mDetailedMatchItem);
//            mBlueTeamKdaTV3.setText(mDetailedMatchItem);
//            mBlueTeamKdaTV4.setText(mDetailedMatchItem);
//            mBlueTeamKdaTV5.setText(mDetailedMatchItem);
            mBlueTeamCsTV1.setText(mDetailedMatchItem.participants[0].stats.totalMinionsKilled);
            mBlueTeamCsTV2.setText(mDetailedMatchItem.participants[1].stats.totalMinionsKilled);
            mBlueTeamCsTV3.setText(mDetailedMatchItem.participants[2].stats.totalMinionsKilled);
            mBlueTeamCsTV4.setText(mDetailedMatchItem.participants[3].stats.totalMinionsKilled);
            mBlueTeamCsTV5.setText(mDetailedMatchItem.participants[4].stats.totalMinionsKilled);

            mRedTeamSummonerTV1.setText(mDetailedMatchItem.participantIdentities[5].player.summonerName);
            mRedTeamSummonerTV2.setText(mDetailedMatchItem.participantIdentities[6].player.summonerName);
            mRedTeamSummonerTV3.setText(mDetailedMatchItem.participantIdentities[7].player.summonerName);
            mRedTeamSummonerTV4.setText(mDetailedMatchItem.participantIdentities[8].player.summonerName);
            mRedTeamSummonerTV5.setText(mDetailedMatchItem.participantIdentities[9].player.summonerName);
//            mRedTeamKdaTV1.setText(mDetailedMatchItem);
//            mRedTeamKdaTV2.setText(mDetailedMatchItem);
//            mRedTeamKdaTV3.setText(mDetailedMatchItem);
//            mRedTeamKdaTV4.setText(mDetailedMatchItem);
//            mRedTeamKdaTV5.setText(mDetailedMatchItem);
            mRedTeamCsTV1.setText(mDetailedMatchItem.participants[5].stats.totalMinionsKilled);
            mRedTeamCsTV2.setText(mDetailedMatchItem.participants[6].stats.totalMinionsKilled);
            mRedTeamCsTV3.setText(mDetailedMatchItem.participants[7].stats.totalMinionsKilled);
            mRedTeamCsTV4.setText(mDetailedMatchItem.participants[8].stats.totalMinionsKilled);
            mRedTeamCsTV5.setText(mDetailedMatchItem.participants[9].stats.totalMinionsKilled);
        } else {
            mContentView.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing ...
    }
}

