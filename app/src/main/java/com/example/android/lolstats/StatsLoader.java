package com.example.android.lolstats;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.lolstats.utils.NetworkUtils;

import java.io.IOException;

public class StatsLoader extends AsyncTaskLoader<String> {
    private final static String TAG = StatsLoader.class.getSimpleName();

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
        String dataJSON = null;
        if (mDataURL != null) {
            Log.d(TAG, "loading data from Riot using this URL: " + mDataURL);
            try {
                dataJSON = NetworkUtils.doHTTPGet(mDataURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dataJSON;
    }

    @Override
    public void deliverResult(@Nullable String data) {
        mCachedDataJSON = data;
        super.deliverResult(data);
    }
}


