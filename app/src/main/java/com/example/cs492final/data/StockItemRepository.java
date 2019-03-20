package com.example.cs492final.data;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class StockItemRepository {
    private StockItemDao mStockItemDao;

    public StockItemRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mStockItemDao = db.StockItemDao();
    }

    public LiveData<List<StockItemDB>> getAllStockItems() {
        return mStockItemDao.getAllItems();
    }

    public LiveData<StockItemDB> getSingleItem(String cityName) {
        return mStockItemDao.getSingleItem(cityName);
    }


    public void insertStockItem(StockItemDB item) {
        new InsertItemAsyncTask(mStockItemDao).execute(item);
    }

    public void deleteStockItem(StockItemDB item) {
        new DeleteAsyncTask(mStockItemDao).execute(item);
    }

    private static class InsertItemAsyncTask extends AsyncTask<StockItemDB, Void, Void> {
        private StockItemDao mAsyncTaskDao;

        InsertItemAsyncTask(StockItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(StockItemDB... items) {
            mAsyncTaskDao.insert(items[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<StockItemDB, Void, Void> {
        private StockItemDao mAsyncTaskDao;

        DeleteAsyncTask(StockItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(StockItemDB... items) {
            mAsyncTaskDao.delete(items[0]);
            return null;
        }
    }


}
