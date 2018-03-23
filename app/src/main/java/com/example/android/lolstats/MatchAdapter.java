package com.example.android.lolstats;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.android.lolstats.utils.RiotUtils;

/**
 * Created by Nick Giles on 3/22/2018.
 */

public class MatchAdapter extends RecyclerView.Adapter {
    private RiotUtils.MatchData[] mMatchData;
    private OnMatchItemClickListener mMatchItemClickListener;
    private Context mContext;

    public interface OnMatchItemClickListener {
        void onMatchItemClick(RiotUtils.MatchData matchData);
    }

    public MatchAdapter(Context context, OnMatchItemClickListener clickListener) {
        mContext = context;
        mMatchItemClickListener = clickListener;
    }

    public void updateForecastItem(RiotUtils.MatchData[] matchData) {
        mMatchData = matchData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mMatchData != null) {
            return mMatchData.length;
        } else {
            return 0;
        }
    }


}
