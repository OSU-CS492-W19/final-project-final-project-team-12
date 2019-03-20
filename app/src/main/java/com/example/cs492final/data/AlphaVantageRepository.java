package com.example.cs492final.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cs492final.AlphaVantageUtils;

import java.util.List;
import java.util.Map;


public class AlphaVantageRepository implements StockSearchAsyncTask.Callback{
    String TAG="AlphaVantageRepository";
    private MutableLiveData<Map<String, AlphaVantageUtils.AlphaVantageRepo>> mSearchResults2;
    private MutableLiveData<List<AlphaVantageUtils.AlphaVantageRepo>> mSearchResults;
    private MutableLiveData<Status> mLoadingStatus;


    public AlphaVantageRepository(){
        mSearchResults2=new MutableLiveData<>();
        mSearchResults2.setValue(null);

        mLoadingStatus=new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);
    }
    public LiveData<Map<String, AlphaVantageUtils.AlphaVantageRepo>> getSearchResults(){
        return mSearchResults2;
    }
    public void loadSearchResults(String query) {
        mLoadingStatus.setValue(Status.LOADING);
        mSearchResults2.setValue(null);
        String url = AlphaVantageUtils.buildAlphaVantageURL(query);
        Log.d(TAG, "executing search with this url" + url);
        //execute search with url
        new StockSearchAsyncTask(url, this).execute();

    }

    @Override
    public void onSearchFinished(Map<String, AlphaVantageUtils.AlphaVantageRepo> searchResults) {
        mSearchResults2.setValue(searchResults);
        if (searchResults != null) {
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }

    }

    public LiveData<Status> getLoadingStatus(){
      return mLoadingStatus;
    }
}
