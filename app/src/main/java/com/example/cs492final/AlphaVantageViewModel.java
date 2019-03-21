package com.example.cs492final;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cs492final.data.AlphaVantageRepository;
import com.example.cs492final.data.Status;

import java.util.Map;

public class AlphaVantageViewModel extends ViewModel {
    //private LiveData<List<AlphaVantageUtils.AlphaVantageRepo>> mSearchResults;
    private LiveData<Map<String, AlphaVantageUtils.AlphaVantageRepo>> mSearchResultsMap;
    private AlphaVantageRepository mAlphaVantageRepository;
    private LiveData<Status> mLoadingStatus;


    public AlphaVantageViewModel(){
        mAlphaVantageRepository =new AlphaVantageRepository();
        mSearchResultsMap= mAlphaVantageRepository.getSearchResults();
        mLoadingStatus= mAlphaVantageRepository.getLoadingStatus();
    }
    public void loadSearchResults(String query){
        mAlphaVantageRepository.loadSearchResults(query);
    }
//    public LiveData<List<AlphaVantageUtils.AlphaVantageRepo>> getSearchResults() {
//        return mSearchResults;
//    }

    public LiveData<Map<String, AlphaVantageUtils.AlphaVantageRepo>> getmSearchResultsMap() {
        return mSearchResultsMap;
    }
    public LiveData<Status> getLoadingStatus(){
        return mLoadingStatus;
    }
}
