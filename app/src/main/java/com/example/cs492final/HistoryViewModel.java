package com.example.cs492final;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.cs492final.data.HistoryItem;
import com.example.cs492final.data.HistoryRepository;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    private HistoryRepository mHistoryRepository;

    public HistoryViewModel(Application application){
        super(application);
        mHistoryRepository = new HistoryRepository(application);
    }

    public void insertHistory(HistoryItem info){
        mHistoryRepository.insertHistory(info);
    }

    public void deleteHistory(HistoryItem info){
        mHistoryRepository.deleteHistory(info);
    }

    public LiveData<List<HistoryItem>> getAllHistory(){
        return mHistoryRepository.getAllHistory();
    }

    public LiveData<HistoryItem> getSingleHistory(String CurrentLocation){
        return mHistoryRepository.getSingleHistory(CurrentLocation);
    }
}
