package com.example.cs492final;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.cs492final.data.StockItemDB;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements RecyclerViewAdapter.OnTempItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mChatRV;
    private EditText mMessageET;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChatRV = findViewById(R.id.rv_chat_list);
        mMessageET = findViewById(R.id.et_message);

        mRecyclerViewAdapter = new RecyclerViewAdapter(this);

        mChatRV.setLayoutManager(new LinearLayoutManager(this));
        mChatRV.setHasFixedSize(true);

        mChatRV.setAdapter(mRecyclerViewAdapter);

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);

        final StockItemViewModel mStockItemViewModel = ViewModelProviders.of(this).get(StockItemViewModel.class);
        LiveData<List<StockItemDB>> allItems = mStockItemViewModel.getAllStockItems();

        Button mSendB = findViewById(R.id.b_send);
        //Create an onclicklistener to listen for when the button is pressed.
        mSendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StockItemDB mItem = new StockItemDB();
                //Get the string from the EditText box.
                String messageText = mMessageET.getText().toString();
                mItem.company_symbol = mMessageET.getText().toString().toUpperCase();
                mStockItemViewModel.insertStockItem(mItem);
                //Check to see if there is anything in the message box.
                if (!TextUtils.isEmpty(messageText)) {
                    //Update to send to the slack server.
                    //mRecyclerViewAdapter.addChat(messageText);
                    mMessageET.setText("");
                    doAlphaVantageSearch(messageText); //Get information from the API
                }
            }
        });
    }

    private void doAlphaVantageSearch(String query) {
        String url = AlphaVantageUtils.buildAlphaVantageURL(query);
        Log.d(TAG, "querying search URL: " + url);
        new GitHubSearchTask().execute(url);   //uses Async - will add ViewModel at some point
    }

    @Override
    public void onTempItemClick(AlphaVantageUtils.AlphaVantageRepo repo){ //Update parameter based off data type we clicked on
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(AlphaVantageUtils.EXTRA_ALPHA_VANTAGE_ITEM, repo);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    class GitHubSearchTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, "onPreExecute");
            mLoadingIndicatorPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            //Log.d(TAG,"in doInBackground");
            String url = urls[0];
            String results = null;
            try {
                results = NetworkUtils.doHTTPGet(url);
                //Log.d(TAG, "after GET " + results);
            } catch (IOException e) {
                //Log.d(TAG, "error");
                e.printStackTrace();
            }
            return results;
        }


        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, "onPostExecute");
            if (s != null) {
                mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                Map<String, AlphaVantageUtils.AlphaVantageRepo> repos = AlphaVantageUtils.parseGitHubSearchResults(s);

                //Log.d(TAG,"key is 2019-03-14 15:30:00  -  open value is : " + repos.get("2019-03-14 15:30:00").open);
                //Log.d(TAG, "after parsing");
                mRecyclerViewAdapter.updateSearchResults(repos);
            } else {
                mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
                mChatRV.setVisibility(View.INVISIBLE);
            }
            mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        }
    }
}