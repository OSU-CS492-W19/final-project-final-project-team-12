package com.example.cs492final.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.cs492final.data.HistoryItem;

import java.util.List;

@Dao
public interface StockHistoryDao {
    @Insert
    void insert(HistoryItem s_item);

    @Delete
    void delete(HistoryItem s_item);

    @Query("SELECT * FROM history")
    LiveData<List<HistoryItem>> getAllHistory();

    @Query("SELECT * FROM history WHERE StockName = :city_name LIMIT 1")
    LiveData<HistoryItem> getSingleHistory(String city_name);
}
