package com.example.android.lolstats;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.lolstats.utils.NetworkUtils;

import java.io.IOException;

/**
 * Created by hessro on 2/24/18.
 */

public class StatsLoader extends AsyncTaskLoader<String> {

    private final static String TAG = StatsLoader.class.getSimpleName();

    private String mCachedSummonerSearchJSON;
    private String mSummonerSearchURL;
//    private String mRecentMatchesURL;
//    private String mDetailedMatchURL;
//    private String mChampionDataURL;

    public StatsLoader(Context context, String RiotURL) {
        super(context);
        mSummonerSearchURL = RiotURL;
    }

    @Override
    protected void onStartLoading() {
        if (mSummonerSearchURL != null) {
            if (mCachedSummonerSearchJSON != null) {
//                Log.d(TAG, "using cached stats");
                deliverResult(mCachedSummonerSearchJSON);
            } else {
                forceLoad();
            }
        }
    }

    @Nullable
    @Override
    public String loadInBackground() {
        String summonerSearchJSON = null;
        if (mSummonerSearchURL != null) {
//            Log.d(TAG, "loading forecast from OpenWeatherMap using this URL: " + mStatsURL);
            try {
                summonerSearchJSON = NetworkUtils.doHTTPGet(mSummonerSearchURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return summonerSearchJSON;
    }

    @Override
    public void deliverResult(@Nullable String data) {
        mCachedSummonerSearchJSON = data;
        super.deliverResult(data);
    }
}
