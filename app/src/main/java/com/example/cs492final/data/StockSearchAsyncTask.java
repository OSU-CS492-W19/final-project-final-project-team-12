package com.example.cs492final.data;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cs492final.AlphaVantageUtils;
import com.example.cs492final.NetworkUtils;
import com.example.cs492final.RecyclerViewAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StockSearchAsyncTask extends AsyncTask<Void, Void, String> {
    private String mUrl;
    private Callback mCallback;

    StockSearchAsyncTask(String url, Callback callback){
        mUrl=url;
        mCallback=callback;
    }
    @Override
    protected String doInBackground(Void... voids){
        String results=null;
        if (mUrl != null) {
            try {
                results = NetworkUtils.doHTTPGet(mUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
    public interface Callback {
        void onSearchFinished(Map<String, AlphaVantageUtils.AlphaVantageRepo> searchResults, String s);
    }
    @Override
    protected void onPostExecute(String s) {
        Log.d("StockSearchAsyncTask", "onPostExecute: sssssssssssssssssssssssssssss"+s);
        Map<String, AlphaVantageUtils.AlphaVantageRepo> searchResults = null;
        if (s!=null && s.contains("Error Message")==false){
            searchResults = AlphaVantageUtils.parseGitHubSearchResults(s);
        }

        Log.d("StockSearchAsyncTask", "onPostExecute: ahh"+searchResults);
        mCallback.onSearchFinished(searchResults, s);
    }

}

