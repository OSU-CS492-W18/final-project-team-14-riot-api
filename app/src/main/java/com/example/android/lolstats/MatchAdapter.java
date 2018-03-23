package com.example.android.lolstats;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.lolstats.utils.OpenWeatherMapUtils;
import com.example.android.lolstats.utils.RiotUtils;

import java.text.DateFormat;
import java.util.ArrayList;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchItemViewHolder> {
//    private RiotUtils.MatchData[] mMatchData;
//    private OnMatchItemClickListener mMatchItemClickListener;
//    private Context mContext;
//
//    public interface OnMatchItemClickListener {
//        void onMatchItemClick(RiotUtils.MatchData matchData);
//    }
//
//    public MatchAdapter(Context context, OnMatchItemClickListener clickListener) {
//        mContext = context;
//        mMatchItemClickListener = clickListener;
//    }
//
//    public void updateForecastItem(RiotUtils.MatchData[] matchData) {
//        mMatchData = matchData;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mMatchData != null) {
//            return mMatchData.length;
//        } else {
//            return 0;
//        }
//    }

    private ArrayList<RiotUtils.MatchData> mMatchItems;
    private OnMatchItemClickListener mMatchItemClickListener;
    private Context mContext;

    public interface OnMatchItemClickListener {
        void onMatchItemClick(RiotUtils.MatchData matchData);
    }

    public MatchAdapter(Context context, OnMatchItemClickListener clickListener) {
        mContext = context;
        mMatchItemClickListener = clickListener;
    }

    public void updateMatchItems(ArrayList<RiotUtils.MatchData> matchItems) {
        mMatchItems = matchItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mMatchItems != null) {
            return mMatchItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public MatchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.forecast_item, parent, false);
        return new MatchItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MatchItemViewHolder holder, int position) {
        holder.bind(mMatchItems.get(position));
    }

    class MatchItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public MatchItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(RiotUtils.MatchData matchData) {

        }

        @Override
        public void onClick(View v) {
            RiotUtils.MatchData matchItem = mMatchItems.get(getAdapterPosition());
            mMatchItemClickListener.onMatchItemClick(matchItem);
        }
    }

}


//package com.example.android.lolstats;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//
//import com.example.android.lolstats.utils.RiotUtils;
//
///**
// * Created by Nick Giles on 3/22/2018.
// */
//
//public class MatchAdapter extends RecyclerView.Adapter {
//    private RiotUtils.MatchData[] mMatchData;
//    private OnMatchItemClickListener mMatchItemClickListener;
//    private Context mContext;
//
//    public interface OnMatchItemClickListener {
//        void onMatchItemClick(RiotUtils.MatchData matchData);
//    }
//
//    public MatchAdapter(Context context, OnMatchItemClickListener clickListener) {
//        mContext = context;
//        mMatchItemClickListener = clickListener;
//    }
//
//    public void updateForecastItem(RiotUtils.MatchData[] matchData) {
//        mMatchData = matchData;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mMatchData != null) {
//            return mMatchData.length;
//        } else {
//            return 0;
//        }
//    }
//
//
//}
