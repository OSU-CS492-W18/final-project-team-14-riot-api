package com.example.android.lolstats;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.lolstats.utils.NetworkUtils;

import java.io.IOException;

public class StatsLoader extends AsyncTaskLoader {
    private final static String TAG = ForecastLoader.class.getSimpleName();

    private String mCachedDataJSON;
    private String mDataURL;

    public StatsLoader(Context context, String dataURL) {
        super(context);
        mDataURL = dataURL;
    }

    @Override
    protected void onStartLoading() {
        if (mDataURL != null) {
            if (mCachedDataJSON != null) {
                Log.d(TAG, "using cached forecast");
                deliverResult(mCachedDataJSON);
            } else {
                forceLoad();
            }
        }
    }

    @Nullable
    @Override
    public String loadInBackground() {
        String forecastJSON = null;
        if (mDataURL != null) {
            Log.d(TAG, "loading forecast from OpenWeatherMap using this URL: " + mDataURL);
            try {
                forecastJSON = NetworkUtils.doHTTPGet(mDataURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return forecastJSON;
    }

    @Override
    public void deliverResult(@Nullable String data) {
        mCachedDataJSON = data;
        super.deliverResult(data);
    }
}


