package com.example.android.lolstats;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.lolstats.utils.RiotUtils;

import java.text.DateFormat;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchItemViewHolder> {
    private RiotUtils.MatchData[] mMatchData;
    private OnMatchItemClickListener mMatchItemClickListener;
    private Context mContext;

    public interface OnMatchItemClickListener {
        void onMatchItemClick(RiotUtils.MatchData matchItem);
    }

    public MatchAdapter(Context context, OnMatchItemClickListener clickListener) {
        mContext = context;
        mMatchItemClickListener = clickListener;
    }

    public void updateMatchDataItem(RiotUtils.MatchData[] matchData) {
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

    @Override
    public MatchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.forecast_item, parent, false);
        return new MatchItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MatchItemViewHolder holder, int position) {
        holder.bind(mMatchData[position]);
    }

    class MatchItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mMatchRole;
        private TextView mDate;
        private final DateFormat mDateFormatter = DateFormat.getDateTimeInstance();

        public MatchItemViewHolder(View itemView) {
            super(itemView);
            mMatchRole = itemView.findViewById(R.id.tv_role);
            mDate = itemView.findViewById(R.id.tv_match_date);
            itemView.setOnClickListener(this);
        }

        public void bind(RiotUtils.MatchData matchData) {
            mMatchRole.setText(matchData.lane);
            String dateString = mDateFormatter.format(matchData.timestamp);
            mDate.setText(dateString);
        }

        @Override
        public void onClick(View v) {
            RiotUtils.MatchData matchData = mMatchData[getAdapterPosition()];
            mMatchItemClickListener.onMatchItemClick(matchData);
        }
    }

}

