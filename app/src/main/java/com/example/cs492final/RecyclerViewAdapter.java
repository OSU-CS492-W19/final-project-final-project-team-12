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

    public AlphaVantageUtils.AlphaVantageRepo returnEmptyRepo(){
        AlphaVantageUtils.AlphaVantageRepo emptyRepo = new AlphaVantageUtils.AlphaVantageRepo();
        emptyRepo.open = "NA";
        emptyRepo.close = "NA";
        emptyRepo.high = "NA";
        emptyRepo.low = "NA";
        emptyRepo.volume = "NA";

        return emptyRepo;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i){
        Log.d(TAG, "i is " + i);

        // There are 7 data points per day so every 7 data points, we have to go back a day.
        // This function is called for every data points so we have to keep track of days as a member of the Adapter class
        if (i%7 == 0) {
            mGoBackThisManyDays++;
        }

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
