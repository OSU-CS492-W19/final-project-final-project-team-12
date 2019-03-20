package com.example.cs492final.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StockItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StockItemDB s_item);

    @Delete
    void delete(StockItemDB s_item);

    @Query("SELECT * FROM stockitems")
    LiveData<List<StockItemDB>> getAllItems();

    @Query("SELECT * FROM stockitems WHERE company_symbol = :city_name LIMIT 1")
    LiveData<StockItemDB> getSingleItem(String city_name);
}