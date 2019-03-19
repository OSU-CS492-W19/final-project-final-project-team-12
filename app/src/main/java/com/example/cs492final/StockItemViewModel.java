package com.example.cs492final;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.cs492final.data.StockItemDB;
import com.example.cs492final.data.StockItemRepository;

import java.util.List;

public class StockItemViewModel extends AndroidViewModel {
    private StockItemRepository mStockItemRepository;

    public StockItemViewModel(Application application) {
        super(application);
        mStockItemRepository = new StockItemRepository(application);
    }

    public void insertStockItem(StockItemDB item) {
        mStockItemRepository.insertStockItem(item);
    }

    public void deleteStockItem(StockItemDB item) {
        mStockItemRepository.deleteStockItem(item);
    }
    public LiveData<List<StockItemDB>> getAllStockItems() {
        return mStockItemRepository.getAllStockItems();
    }

    public LiveData<StockItemDB> getSingleStockItem(String cityName) {
        return mStockItemRepository.getSingleItem(cityName);
    }


}