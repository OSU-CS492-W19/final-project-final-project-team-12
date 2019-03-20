package com.example.cs492final;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cs492final.data.Repository;
import com.example.cs492final.data.StockSearchAsyncTask;

import java.util.List;
import java.util.Map;

public class AlphaVantageViewModel extends ViewModel {
    //private LiveData<List<AlphaVantageUtils.AlphaVantageRepo>> mSearchResults;
    private LiveData<Map<String, AlphaVantageUtils.AlphaVantageRepo>> mSearchResultsMap;
    private Repository mRepository;

    public AlphaVantageViewModel(){
        mRepository=new Repository();
        mSearchResultsMap=mRepository.getSearchResults();
    }
    public void loadSearchResults(String query){
        mRepository.loadSearchResults(query);
    }
//    public LiveData<List<AlphaVantageUtils.AlphaVantageRepo>> getSearchResults() {
//        return mSearchResults;
//    }

    public LiveData<Map<String, AlphaVantageUtils.AlphaVantageRepo>> getmSearchResultsMap() {
        return mSearchResultsMap;
    }
}
