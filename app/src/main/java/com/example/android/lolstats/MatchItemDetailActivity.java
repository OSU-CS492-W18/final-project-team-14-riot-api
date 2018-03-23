package com.example.android.lolstats;

import android.os.AsyncTask;
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

import com.example.android.lolstats.utils.NetworkUtils;
import com.example.android.lolstats.utils.RiotUtils;

import java.io.IOException;
import java.text.DateFormat;

/**
 * Created by shane on 3/22/18.
 */

public class MatchItemDetailActivity extends AppCompatActivity {
    private static final String TAG = MatchItemDetailActivity.class.getSimpleName();

    private final DateFormat mDateFormatter = DateFormat.getDateTimeInstance();
    private static final String MATCH_HASHTAG = "#LOLStats";
    private static final DateFormat DATE_FORMATTER = DateFormat.getDateTimeInstance();
    private RiotUtils.MatchData mMatchItem;
    private RiotUtils.DetailedMatchData mDetailedMatchItem;
    private ProgressBar mLoadingPB;
    private TextView mLoadingErrorMessageTV;
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
    private TextView mBlueTeamTowerTV;
    private TextView mBlueTeamInhibitorTV;
    private TextView mBlueTeamDragonTV;
    private TextView mBlueTeamBaronTV;

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
    private TextView mRedTeamTowerTV;
    private TextView mRedTeamInhibitorTV;
    private TextView mRedTeamDragonTV;
    private TextView mRedTeamBaronTV;


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
        mBlueTeamTowerTV = findViewById(R.id.tv_blue_towers);
        mBlueTeamInhibitorTV = findViewById(R.id.tv_blue_inhibitors);
        mBlueTeamDragonTV = findViewById(R.id.tv_blue_dragons);
        mBlueTeamBaronTV = findViewById(R.id.tv_blue_inhibitors);

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
        mRedTeamTowerTV = findViewById(R.id.tv_red_towers);
        mRedTeamInhibitorTV = findViewById(R.id.tv_red_inhibitors);
        mRedTeamDragonTV = findViewById(R.id.tv_red_dragons);
        mRedTeamBaronTV = findViewById(R.id.tv_red_inhibitors);

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

    public String buildKDA (RiotUtils.Stats item){
        String formattedDateTime = item.kills + " / " + item.deaths + " / " + item.assists;
        return formattedDateTime;
    }

    public void loadDetailedMatch(long gameId, String region) {
        mLoadingPB.setVisibility(View.VISIBLE);

        String detailedMatchURL = RiotUtils.buildDetailedMatchURL(gameId, region);
        Log.d(TAG, "detailedMatchURL: " + detailedMatchURL);

        new DetailedMatchTask().execute(detailedMatchURL);
    }

    public class DetailedMatchTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            String mDataURL = urls[0];
            String dataResult = null;
            if (mDataURL != null) {
                Log.d(TAG, "loading data from Riot using this URL: " + mDataURL);
                try {
                    dataResult = NetworkUtils.doHTTPGet(mDataURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return dataResult;
        }

        @Override
        protected void onPostExecute(String data) {
            Log.d(TAG, "got forecast from loader");
            mLoadingPB.setVisibility(View.INVISIBLE);
            if (data != null) {
                mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                mContentView.setVisibility(View.VISIBLE);
                mDetailedMatchItem = RiotUtils.parseDetailedMatchData(data);
                // set text views
                mDateTV.setText(mDateFormatter.format(mDetailedMatchItem.gameCreation));
                mBlueTeamWinTV.setText(mDetailedMatchItem.teams[0].win);
                mRedTeamWinTV.setText(mDetailedMatchItem.teams[1].win);


                mBlueTeamSummonerTV1.setText(mDetailedMatchItem.participantIdentities[0].player.summonerName);
                mBlueTeamSummonerTV2.setText(mDetailedMatchItem.participantIdentities[1].player.summonerName);
                mBlueTeamSummonerTV3.setText(mDetailedMatchItem.participantIdentities[2].player.summonerName);
                mBlueTeamSummonerTV4.setText(mDetailedMatchItem.participantIdentities[3].player.summonerName);
                mBlueTeamSummonerTV5.setText(mDetailedMatchItem.participantIdentities[4].player.summonerName);
                mBlueTeamKdaTV1.setText(buildKDA(mDetailedMatchItem.participants[0].stats));
                mBlueTeamKdaTV2.setText(buildKDA(mDetailedMatchItem.participants[1].stats));
                mBlueTeamKdaTV3.setText(buildKDA(mDetailedMatchItem.participants[2].stats));
                mBlueTeamKdaTV4.setText(buildKDA(mDetailedMatchItem.participants[3].stats));
                mBlueTeamKdaTV5.setText(buildKDA(mDetailedMatchItem.participants[4].stats));
                mBlueTeamCsTV1.setText(mDetailedMatchItem.participants[0].stats.totalMinionsKilled);
                mBlueTeamCsTV2.setText(mDetailedMatchItem.participants[1].stats.totalMinionsKilled);
                mBlueTeamCsTV3.setText(mDetailedMatchItem.participants[2].stats.totalMinionsKilled);
                mBlueTeamCsTV4.setText(mDetailedMatchItem.participants[3].stats.totalMinionsKilled);
                mBlueTeamCsTV5.setText(mDetailedMatchItem.participants[4].stats.totalMinionsKilled);
                mBlueTeamTowerTV.setText(mDetailedMatchItem.teams[0].towerKills);
                mBlueTeamInhibitorTV.setText(mDetailedMatchItem.teams[0].inhibitorKills);
                mBlueTeamDragonTV.setText(mDetailedMatchItem.teams[0].dragonKills);
                mBlueTeamBaronTV.setText(mDetailedMatchItem.teams[0].baronKills);

                mRedTeamSummonerTV1.setText(mDetailedMatchItem.participantIdentities[5].player.summonerName);
                mRedTeamSummonerTV2.setText(mDetailedMatchItem.participantIdentities[6].player.summonerName);
                mRedTeamSummonerTV3.setText(mDetailedMatchItem.participantIdentities[7].player.summonerName);
                mRedTeamSummonerTV4.setText(mDetailedMatchItem.participantIdentities[8].player.summonerName);
                mRedTeamSummonerTV5.setText(mDetailedMatchItem.participantIdentities[9].player.summonerName);
                mRedTeamKdaTV1.setText(buildKDA(mDetailedMatchItem.participants[5].stats));
                mRedTeamKdaTV2.setText(buildKDA(mDetailedMatchItem.participants[6].stats));
                mRedTeamKdaTV3.setText(buildKDA(mDetailedMatchItem.participants[7].stats));
                mRedTeamKdaTV4.setText(buildKDA(mDetailedMatchItem.participants[8].stats));
                mRedTeamKdaTV5.setText(buildKDA(mDetailedMatchItem.participants[9].stats));
                mRedTeamCsTV1.setText(mDetailedMatchItem.participants[5].stats.totalMinionsKilled);
                mRedTeamCsTV2.setText(mDetailedMatchItem.participants[6].stats.totalMinionsKilled);
                mRedTeamCsTV3.setText(mDetailedMatchItem.participants[7].stats.totalMinionsKilled);
                mRedTeamCsTV4.setText(mDetailedMatchItem.participants[8].stats.totalMinionsKilled);
                mRedTeamCsTV5.setText(mDetailedMatchItem.participants[9].stats.totalMinionsKilled);
                mRedTeamTowerTV.setText(mDetailedMatchItem.teams[1].towerKills);
                mRedTeamInhibitorTV.setText(mDetailedMatchItem.teams[1].inhibitorKills);
                mRedTeamDragonTV.setText(mDetailedMatchItem.teams[1].dragonKills);
                mRedTeamBaronTV.setText(mDetailedMatchItem.teams[1].baronKills);
            } else {
                mContentView.setVisibility(View.INVISIBLE);
                mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
            }
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
}

