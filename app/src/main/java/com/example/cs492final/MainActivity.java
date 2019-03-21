package com.example.cs492final;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import com.example.cs492final.data.Status;

import java.io.IOException;
//<<<<<<< HEAD
import java.util.List;
//=======
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
//>>>>>>> master
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements RecyclerViewAdapter.OnTempItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mChatRV;
    private EditText mMessageET;
    private String mLatestSearch;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private TextView mAPIErrorMessageTV;

    private AlphaVantageViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChatRV = findViewById(R.id.rv_chat_list);
        mMessageET = findViewById(R.id.et_message);

        mLatestSearch = "";

        mRecyclerViewAdapter = new RecyclerViewAdapter(this);

        mChatRV.setLayoutManager(new LinearLayoutManager(this));
        mChatRV.setHasFixedSize(true);

        mChatRV.setAdapter(mRecyclerViewAdapter);

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);
        mAPIErrorMessageTV = findViewById(R.id.tv_API_error_message);

        final StockItemViewModel mStockItemViewModel = ViewModelProviders.of(this).get(StockItemViewModel.class);
        LiveData<List<StockItemDB>> allItems = mStockItemViewModel.getAllStockItems();

        Button mSendB = findViewById(R.id.b_send);

        mViewModel= ViewModelProviders.of(this).get(AlphaVantageViewModel.class);
        mViewModel.getmSearchResultsMap().observe(this, new Observer<Map<String, AlphaVantageUtils.AlphaVantageRepo>>() {
            @Override
            public void onChanged(@Nullable Map<String, AlphaVantageUtils.AlphaVantageRepo> stringAlphaVantageRepoMap) {
                mRecyclerViewAdapter.updateSearchResults(stringAlphaVantageRepoMap);
            }
        });

        mViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(@Nullable Status status) {
                if (status==Status.LOADING){
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                    mChatRV.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                    mAPIErrorMessageTV.setVisibility(View.INVISIBLE);
                }
                else if (status==Status.SUCCESS) {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mChatRV.setVisibility(View.VISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                    mAPIErrorMessageTV.setVisibility(View.INVISIBLE);
                }
                else if (status==Status.ERRORAPI){
                    Log.d(TAG, "onChanged: ERRORAPI status");
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mChatRV.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                    mAPIErrorMessageTV.setVisibility(View.VISIBLE);
                }
                else{
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mChatRV.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
                    mAPIErrorMessageTV.setVisibility(View.INVISIBLE);
                }
            }
        });

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
                    mLatestSearch = mMessageET.getText().toString();
                    mMessageET.setText("");
                    doAlphaVantageSearch(messageText); //Get information from the API
                }
            }
        });
    }

    private void doAlphaVantageSearch(String query) {
        mViewModel.loadSearchResults(query);
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
            case R.id.action_search_test:
                Log.d(TAG, "query is " + mLatestSearch);
                Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                searchIntent.putExtra(SearchManager.QUERY, mLatestSearch + " NASDAQ company list symbols");
                startActivity(searchIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}