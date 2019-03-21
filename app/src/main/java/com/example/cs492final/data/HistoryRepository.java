package com.example.cs492final.data;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class HistoryRepository {
    private StockHistoryDao mStockHistoryDao;

    public HistoryRepository(Application application){
        HistoryDatabase db = HistoryDatabase.getDatabase(application);
        mStockHistoryDao = db.StockHistoryDao();
    }

    public void insertHistory(HistoryItem info){
        new InsertHistoryAsyncTask(mStockHistoryDao).execute(info);
    }

    public void deleteHistory(HistoryItem info){
        new DeleteAsyncTask(mStockHistoryDao).execute(info);
    }

    public LiveData<List<HistoryItem>> getAllHistory(){
        return mStockHistoryDao.getAllHistory();
    }

    public LiveData<HistoryItem> getSingleHistory(String CurrentLocation){
        return mStockHistoryDao.getSingleHistory(CurrentLocation);
    }

    private static class InsertHistoryAsyncTask extends AsyncTask<HistoryItem, Void, Void> {
        private StockHistoryDao mAsyncTaskDao;

        InsertHistoryAsyncTask(StockHistoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(HistoryItem... historyItems) {
            mAsyncTaskDao.insert(historyItems[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<HistoryItem, Void, Void> {
        private StockHistoryDao mAsyncTaskDao;

        DeleteAsyncTask(StockHistoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(HistoryItem... historyItems) {
            mAsyncTaskDao.delete(historyItems[0]);
            return null;
        }
    }
}
