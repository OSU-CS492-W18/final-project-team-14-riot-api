package com.example.android.lolstats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nick Giles on 3/10/2018.
 */

public class NavigationViewAdapter extends RecyclerView.Adapter<NavigationViewAdapter.NavigationViewHolder> {
    private ArrayList<String> mRecentLocations;
    private OnNavigationItemClickListener mOnNavigationItemClickListener;
    private SQLiteDatabase mDB;

    public interface OnNavigationItemClickListener {
        void onNavigationItemClicked(String location);
    }

    NavigationViewAdapter(Context context, OnNavigationItemClickListener clickListener) {
        mOnNavigationItemClickListener = clickListener;
        RecentLocationDBHelper dbHelper = new RecentLocationDBHelper(context);
        mDB = dbHelper.getReadableDatabase();
    }

    public void updateRecentLocations(ArrayList<String> recentLocations) {
        mRecentLocations = recentLocations;
        notifyDataSetChanged();
    }

    @Override
    public NavigationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.navigation_item, parent, false);
        return new NavigationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NavigationViewHolder holder, int position) {
        holder.bind(mRecentLocations.get(position));
    }

    @Override
    public int getItemCount() {
        if (mRecentLocations != null)
            return mRecentLocations.size();
        else
            return 0;
    }

    class NavigationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextView;

        public NavigationViewHolder (View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_nav_location);
            itemView.setOnClickListener(this);
        }

        public void bind(String Location) {
            mTextView.setText(Location);
        }

        @Override
        public void onClick(View v) {
            String locationItem = mRecentLocations.get(getAdapterPosition());
            mOnNavigationItemClickListener.onNavigationItemClicked(locationItem);
        }
    }
}
