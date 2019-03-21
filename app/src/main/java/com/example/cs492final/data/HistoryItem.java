package com.example.cs492final.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "history")
public class HistoryItem implements Serializable {
    @NonNull
    @PrimaryKey
    public String StockName;
}
