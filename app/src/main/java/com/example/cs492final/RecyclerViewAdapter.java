package com.example.cs492final;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    private Map<String, AlphaVantageUtils.AlphaVantageRepo> mRepos;
    private int mGoBackThisManyDays;
    private OnTempItemClickListener mOnTempItemClickListener;

    public void updateSearchResults(Map<String, AlphaVantageUtils.AlphaVantageRepo> repos) {
        mRepos = repos;
        notifyDataSetChanged();
    }

    public interface OnTempItemClickListener{
        void onTempItemClick(AlphaVantageUtils.AlphaVantageRepo repo); //Change string s to our data type.
    }

    public RecyclerViewAdapter(OnTempItemClickListener clickListener){
        mOnTempItemClickListener = clickListener;
        mRepos = null;
        mGoBackThisManyDays = -1;
    }

    @Override
    public int getItemCount(){
        if(mRepos != null) {
            return mRepos.size();
        }
        else{
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.chat_list_item, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    public String getPositionKey(int i){
        String key = null;
        int hour = 9;

        Calendar cal = Calendar.getInstance();
        //cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); //for testing
        //Log.d(TAG, "Original day is " + cal.getTime() + " go back this many days: " + mGoBackThisManyDays);
        if (mGoBackThisManyDays > 0) {
            cal.add(Calendar.DAY_OF_WEEK, (-mGoBackThisManyDays));   // will go back however many days we need to make the weeks go by
        }

        // Once we have the day, we need to set the hour. The data only records from 9:30:00 to 15:30:00 each day.
        hour = 9 + (i%7);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);

        //Log.d(TAG, "day is " + cal.getTime() + " the number value is " + cal.get(Calendar.DAY_OF_WEEK));

        //Now format the day into a String
        Date date1 = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format1.format(date1);

        //Log.d(TAG, "Date is " + date);
        key = date;

        // CATCH: There's no data for weekends so we have to give a bogus key - it will trigger an empty repo to take the place
        // Sunday value is 1, 2 is Monday... Saturday is 7
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            key = "No Data for Weekends - Sunday";
        }
        if (cal.get(Calendar.DAY_OF_WEEK) == 7) {
            key = "No Data for Weekends - Saturday";
        }

        return key;
    }

    public static AlphaVantageUtils.AlphaVantageRepo returnEmptyRepo(){
        AlphaVantageUtils.AlphaVantageRepo emptyRepo = new AlphaVantageUtils.AlphaVantageRepo();
        emptyRepo.open = "NA";
        emptyRepo.close = "NA";
        emptyRepo.high = "NA";
        emptyRepo.low = "NA";
        emptyRepo.volume = "NA";

        return emptyRepo;
    }

    public void assignDaysToGoBack (int i) {
        if (i >= 0 && i < 7 ) {
            mGoBackThisManyDays = 0;
        }
        else if (i >= 7 && i < 14) {
            mGoBackThisManyDays = 1;
        }
        else if (i >= 14 && i < 21) {
            mGoBackThisManyDays = 2;
        }
        else if (i >= 21 && i < 28) {
            mGoBackThisManyDays = 3;
        }
        else if (i >= 28 && i < 35) {
            mGoBackThisManyDays = 4;
        }
        else if (i >= 35 && i < 42) {
            mGoBackThisManyDays = 5;
        }
        else if (i >= 42 && i < 49) {
            mGoBackThisManyDays = 6;
        }
        else if (i >= 49 && i < 56) {
            mGoBackThisManyDays = 7;
        }
        else if (i >= 56 && i < 63) {
            mGoBackThisManyDays = 8;
        }
        else if (i >= 63 && i < 70) {
            mGoBackThisManyDays = 9;
        }
        else if (i >= 70 && i < 77) {
            mGoBackThisManyDays = 10;
        }
        else if (i >= 77 && i < 84) {
            mGoBackThisManyDays = 11;
        }
        else if (i >= 84 && i < 91) {
            mGoBackThisManyDays = 12;
        }
        else if (i >= 91 && i < 98) {
            mGoBackThisManyDays = 13;
        }
        else {
            mGoBackThisManyDays = 14;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i){
        // There are 7 data points per day so every 7 data points, we have to go back a day.
        // This function is called for every data points so we have to keep track of days as a member of the Adapter class
        assignDaysToGoBack(i);


        Log.d(TAG, "i is " + i + "and go back this many days " + mGoBackThisManyDays);

        String key = getPositionKey(i);
        //Log.d(TAG, "after calling getPositionKey, key is " + key);

        try {
            recyclerViewHolder.bind(mRepos.get(key), key);
        } catch (NullPointerException e) {
            Log.d(TAG, e.getMessage());
            recyclerViewHolder.bind(returnEmptyRepo(), key);
        }
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mChatTV;
        private TextView mOpenTV;
        private TextView mCloseTV;
        private TextView mHighTV;
        private TextView mLowTV;
        private TextView mVolumeTV;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mChatTV = itemView.findViewById(R.id.tv_rvitem);
            mOpenTV = itemView.findViewById(R.id.rv_tv_open);
            mCloseTV = itemView.findViewById(R.id.rv_tv_close);
            mHighTV = itemView.findViewById(R.id.rv_tv_high);
            mLowTV = itemView.findViewById(R.id.rv_tv_low);
            mVolumeTV = itemView.findViewById(R.id.rv_tv_volume);

            itemView.setOnClickListener(this);
        }

        public void bind(AlphaVantageUtils.AlphaVantageRepo repo, String chat){
            mChatTV.setText(chat);
            mOpenTV.setText("Open: " + repo.open);
            mCloseTV.setText("Close: " + repo.close);
            mHighTV.setText("High: " + repo.high);
            mLowTV.setText("Low: " + repo.low);
            mVolumeTV.setText("Volume: " + repo.volume);
        }

        @Override
        public void onClick(View v) {
            AlphaVantageUtils.AlphaVantageRepo repo = mRepos.get(getPositionKey(getAdapterPosition()));
            if (repo != null) {
                mOnTempItemClickListener.onTempItemClick(repo);
            }
            else {
                mOnTempItemClickListener.onTempItemClick(returnEmptyRepo());
            }
        }
    }
}
