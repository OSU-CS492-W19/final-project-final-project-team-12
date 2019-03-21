package com.example.cs492final.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.cs492final.data.HistoryItem;

@Database(entities = {HistoryItem.class}, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {
    private static volatile HistoryDatabase INSTANCE;
    static HistoryDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HistoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            HistoryDatabase.class, "stock_history_db")
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    public abstract StockHistoryDao StockHistoryDao();

}
