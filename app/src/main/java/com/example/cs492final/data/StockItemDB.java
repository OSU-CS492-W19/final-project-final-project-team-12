package com.example.cs492final.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "stockitems")
public class StockItemDB {
    @NonNull
    @PrimaryKey
    public String company_symbol;
}
