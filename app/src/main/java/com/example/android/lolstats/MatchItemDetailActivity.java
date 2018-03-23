//package com.example.android.lolstats;
//
//import android.support.v4.app.LoaderManager;
//import android.support.v7.app.AppCompatActivity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.support.v4.app.ShareCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.preference.PreferenceManager;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.Loader;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.example.android.lolstats.utils.RiotUtils;
//
//import java.text.DateFormat;
//
///**
// * Created by shane on 3/22/18.
// */
//
//public class MatchItemDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
//
//    private static final String MATCH_HASHTAG = "#LOLStats";
//    private static final DateFormat DATE_FORMATTER = DateFormat.getDateTimeInstance();
//    private RiotUtils.DetailedMatchData mDetailedMatchItem;
//    private static final String DETAILED_MATACH_VIEW_URL_KEY = "matchviewURL";
//    private ProgressBar mLoadingIndicatorPB;
//    private TextView mLoadingErrorMessageTV;
//
//
//
//    private TextView mDateTV;
//    private TextView mBlueTeamWinTV;
//    private TextView mRedTeamWinTV;
//
//    private TextView mBlueTeamSummonerTV1;
//    private TextView mBlueTeamSummonerTV2;
//    private TextView mBlueTeamSummonerTV3;
//    private TextView mBlueTeamSummonerTV4;
//    private TextView mBlueTeamSummonerTV5;
//    private TextView mBlueTeamKdaTV1;
//    private TextView mBlueTeamKdaTV2;
//    private TextView mBlueTeamKdaTV3;
//    private TextView mBlueTeamKdaTV4;
//    private TextView mBlueTeamKdaTV5;
//    private TextView mBlueTeamCsTV1;
//    private TextView mBlueTeamCsTV2;
//    private TextView mBlueTeamCsTV3;
//    private TextView mBlueTeamCsTV4;
//    private TextView mBlueTeamCsTV5;
//
//    private TextView mRedTeamSummonerTV1;
//    private TextView mRedTeamSummonerTV2;
//    private TextView mRedTeamSummonerTV3;
//    private TextView mRedTeamSummonerTV4;
//    private TextView mRedTeamSummonerTV5;
//    private TextView mRedTeamKdaTV1;
//    private TextView mRedTeamKdaTV2;
//    private TextView mRedTeamKdaTV3;
//    private TextView mRedTeamKdaTV4;
//    private TextView mRedTeamKdaTV5;
//    private TextView mRedTeamCsTV1;
//    private TextView mRedTeamCsTV2;
//    private TextView mRedTeamCsTV3;
//    private TextView mRedTeamCsTV4;
//    private TextView mRedTeamCsTV5;
//
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_forecast_item_detail);
//
//        mDateTV = findViewById(R.id.tv_detailed_match_date);;
//        mBlueTeamWinTV = findViewById(R.id.tv_match_outcome_blue_name);;
//        mRedTeamWinTV = findViewById(R.id.tv_match_outcome_red);;
//
//        mBlueTeamSummonerTV1 = findViewById(R.id.tv_detailed_name_P1);
//        mBlueTeamSummonerTV2 = findViewById(R.id.tv_detailed_name_P2);
//        mBlueTeamSummonerTV3 = findViewById(R.id.tv_detailed_name_P3);
//        mBlueTeamSummonerTV4 = findViewById(R.id.tv_detailed_name_P4);
//        mBlueTeamSummonerTV5 = findViewById(R.id.tv_detailed_name_P5);
//        mBlueTeamKdaTV1 = findViewById(R.id.tv_detailed_kda_P1);
//        mBlueTeamKdaTV2 = findViewById(R.id.tv_detailed_kda_P2);
//        mBlueTeamKdaTV3 = findViewById(R.id.tv_detailed_kda_P3);
//        mBlueTeamKdaTV4 = findViewById(R.id.tv_detailed_kda_P4);
//        mBlueTeamKdaTV5 = findViewById(R.id.tv_detailed_kda_P5);
//        mBlueTeamCsTV1 = findViewById(R.id.tv_detailed_cs_P1);
//        mBlueTeamCsTV2 = findViewById(R.id.tv_detailed_cs_P2);
//        mBlueTeamCsTV3 = findViewById(R.id.tv_detailed_cs_P3);
//        mBlueTeamCsTV4 = findViewById(R.id.tv_detailed_cs_P4);
//        mBlueTeamCsTV5 = findViewById(R.id.tv_detailed_cs_P5);
//
//        mRedTeamSummonerTV1 = findViewById(R.id.tv_detailed_name_P1);
//        mRedTeamSummonerTV2 = findViewById(R.id.tv_detailed_name_P2);
//        mRedTeamSummonerTV3 = findViewById(R.id.tv_detailed_name_P3);
//        mRedTeamSummonerTV4 = findViewById(R.id.tv_detailed_name_P4);
//        mRedTeamSummonerTV5 = findViewById(R.id.tv_detailed_name_P5);
//        mRedTeamKdaTV1 = findViewById(R.id.tv_detailed_kda_P1);
//        mRedTeamKdaTV2 = findViewById(R.id.tv_detailed_kda_P2);
//        mRedTeamKdaTV3 = findViewById(R.id.tv_detailed_kda_P3);
//        mRedTeamKdaTV4 = findViewById(R.id.tv_detailed_kda_P4);
//        mRedTeamKdaTV5 = findViewById(R.id.tv_detailed_kda_P5);
//        mRedTeamCsTV1 = findViewById(R.id.tv_detailed_cs_P1);
//        mRedTeamCsTV2 = findViewById(R.id.tv_detailed_cs_P2);
//        mRedTeamCsTV3 = findViewById(R.id.tv_detailed_cs_P3);
//        mRedTeamCsTV4 = findViewById(R.id.tv_detailed_cs_P4);
//        mRedTeamCsTV5 = findViewById(R.id.tv_detailed_cs_P5);
//
////        Intent intent = getIntent();
////        if (intent != null && intent.hasExtra(RiotUtils.ForecastItem.EXTRA_FORECAST_ITEM)) {
////            mForecastItem = (RiotUtils.ForecastItem) intent.getSerializableExtra(
////                    OpenWeatherMapUtils.ForecastItem.EXTRA_FORECAST_ITEM
////            );
////            fillInLayoutText(mForecastItem);
////        }
//    }
//
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.forecast_item_detail, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_share:
//                shareMatchItem();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    public void shareMatchItem() {
//        if (mDetailedMatchItem != null) {
////            String shareText = "Weather for " + mForecastLocation +
////                    ", " + DATE_FORMATTER.format(mForecastItem.dateTime) +
////                    ": " + mForecastItem.temperature + mTemperatureUnitsAbbr +
////                    " - " + mForecastItem.description +
////                    " " + FORECAST_HASHTAG;
//            String shareText = "ThisIsTheMatchItem";
//            ShareCompat.IntentBuilder.from(this)
//                    .setType("text/plain")
//                    .setText(shareText)
//                    .setChooserTitle(R.string.share_chooser_title)
//                    .startChooser();
//        }
//    }
//
//    @Override
//    private void fillInLayoutText(RiotUtils.DetailedMatchData detailedMatchItem) {
//
//    }
//
//    @Override
//    public Loader<String> onCreateLoader(int id, Bundle args) {
//        String detailedMatchViewURL = null;
//        if (args != null && id == 2) {
//            detailedMatchViewURL = args.getString(DETAILED_MATACH_VIEW_URL_KEY);
//        }
//        return new StatsLoader(this, DETAILED_MATACH_VIEW_URL_KEY);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<String> loader, String data) {
////        Log.d(TAG, "got forecast from loader");
////        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
//        if (data != null) {
////            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
////            mForecastItemsRV.setVisibility(View.VISIBLE);
//            mDetailedMatchItem = RiotUtils.parseForecastJSON(data);
//            // set text views
//            mDateTV.setText(mDetailedMatchItem);
//            mBlueTeamWinTV.setText(mDetailedMatchItem.teams[0].win);
//            mRedTeamWinTV.setText(mDetailedMatchItem.teams[1].win);
//
//            mBlueTeamSummonerTV1.setText(mDetailedMatchItem.participantIdentities[0].player.summonerName);
//            mBlueTeamSummonerTV2.setText(mDetailedMatchItem.participantIdentities[1].player.summonerName);
//            mBlueTeamSummonerTV3.setText(mDetailedMatchItem.participantIdentities[2].player.summonerName);
//            mBlueTeamSummonerTV4.setText(mDetailedMatchItem.participantIdentities[3].player.summonerName);
//            mBlueTeamSummonerTV5.setText(mDetailedMatchItem.participantIdentities[4].player.summonerName);
////            mBlueTeamKdaTV1.setText(mDetailedMatchItem);
////            mBlueTeamKdaTV2.setText(mDetailedMatchItem);
////            mBlueTeamKdaTV3.setText(mDetailedMatchItem);
////            mBlueTeamKdaTV4.setText(mDetailedMatchItem);
////            mBlueTeamKdaTV5.setText(mDetailedMatchItem);
//            mBlueTeamCsTV1.setText(mDetailedMatchItem.participants.stats.totalMinionsKilled);
//            mBlueTeamCsTV2.setText(mDetailedMatchItem.participants.stats.totalMinionsKilled);
//            mBlueTeamCsTV3.setText(mDetailedMatchItem.participants.stats.totalMinionsKilled);
//            mBlueTeamCsTV4.setText(mDetailedMatchItem.participants.stats.totalMinionsKilled);
//            mBlueTeamCsTV5.setText(mDetailedMatchItem.participants.stats.totalMinionsKilled);
//
//            mRedTeamSummonerTV1.setText(mDetailedMatchItem.participantIdentities[5].player.summonerName);
//            mRedTeamSummonerTV2.setText(mDetailedMatchItem.participantIdentities[6].player.summonerName);
//            mRedTeamSummonerTV3.setText(mDetailedMatchItem.participantIdentities[7].player.summonerName);
//            mRedTeamSummonerTV4.setText(mDetailedMatchItem.participantIdentities[8].player.summonerName);
//            mRedTeamSummonerTV5.setText(mDetailedMatchItem.participantIdentities[9].player.summonerName);
////            mRedTeamKdaTV1.setText(mDetailedMatchItem);
////            mRedTeamKdaTV2.setText(mDetailedMatchItem);
////            mRedTeamKdaTV3.setText(mDetailedMatchItem);
////            mRedTeamKdaTV4.setText(mDetailedMatchItem);
////            mRedTeamKdaTV5.setText(mDetailedMatchItem);
//            mRedTeamCsTV1.setText(mDetailedMatchItem.participants.stats.totalMinionsKilled);
//            mRedTeamCsTV2.setText(mDetailedMatchItem.participants.stats.totalMinionsKilled);
//            mRedTeamCsTV3.setText(mDetailedMatchItem.participants.stats.totalMinionsKilled);
//            mRedTeamCsTV4.setText(mDetailedMatchItem.participants.stats.totalMinionsKilled);
//            mRedTeamCsTV5.setText(mDetailedMatchItem.participants.stats.totalMinionsKilled);
//        } else {
////            mForecastItemsRV.setVisibility(View.INVISIBLE);
////            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onLoaderReset(Loader<String> loader) {
//        // Nothing ...
//    }
//}
