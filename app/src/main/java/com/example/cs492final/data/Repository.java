package com.example.cs492final.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.cs492final.AlphaVantageUtils;

import java.util.List;
import java.util.Map;


public class Repository implements StockSearchAsyncTask.Callback{
    String TAG="Repository";
    private MutableLiveData<Map<String, AlphaVantageUtils.AlphaVantageRepo>> mSearchResults2;
    private MutableLiveData<List<AlphaVantageUtils.AlphaVantageRepo>> mSearchResults;
//    private MutableLiveData<AsyncTask.Status> mLoadingStatus;
    public Repository(){
        mSearchResults2=new MutableLiveData<>();
        mSearchResults2.setValue(null);
    }
    public LiveData<Map<String, AlphaVantageUtils.AlphaVantageRepo>> getSearchResults(){
        return mSearchResults2;
    }
    public void loadSearchResults(String query){
        mSearchResults2.setValue(null);
        String url = AlphaVantageUtils.buildAlphaVantageURL(query);
        Log.d(TAG, "executing search with this url"+url);
        //execute search with url
        new StockSearchAsyncTask(url, this).execute();

    }

    @Override
    public MutableLiveData<Map<String, AlphaVantageUtils.AlphaVantageRepo>> onSearchFinished(List<AlphaVantageUtils.AlphaVantageRepo> searchResults) {
        return mSearchResults2;
    }
}
