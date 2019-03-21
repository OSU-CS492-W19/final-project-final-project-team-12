package com.example.cs492final;

import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
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
import android.support.v7.widget.Toolbar;

import com.example.cs492final.data.HistoryItem;
import com.example.cs492final.data.StockItemDB;

import com.example.cs492final.data.Status;

import java.io.IOException;
import java.util.List;
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
        implements RecyclerViewAdapter.OnTempItemClickListener, NavigationView.OnNavigationItemSelectedListener, HistoryAdapter.OnHistoryClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mChatRV;
    private EditText mMessageET;
    private String mLatestSearch;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private DrawerLayout mDrawerLayout;
    private HistoryAdapter mHistoryAdapter;

    private RecyclerView mHistoryRV;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private TextView mAPIErrorMessageTV;

    private AlphaVantageViewModel mViewModel;
    private HistoryViewModel mHistoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChatRV = findViewById(R.id.rv_chat_list);
        mMessageET = findViewById(R.id.et_message);
        mHistoryRV = findViewById(R.id.rv_history);

        mDrawerLayout =findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nv_nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mHistoryAdapter = new HistoryAdapter(this);
        mHistoryRV.setAdapter(mHistoryAdapter);
        mHistoryRV.setLayoutManager(new LinearLayoutManager(this));
        mHistoryRV.setHasFixedSize(true);

        mHistoryViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        LiveData<List<HistoryItem>> allHistory = mHistoryViewModel.getAllHistory();

        allHistory.observe(this, new Observer<List<HistoryItem>>() {
            @Override
            public void onChanged(@Nullable List<HistoryItem> historyItems) {
                //Update our recycler view
                mHistoryAdapter.updateSearchResults(historyItems);
            }
        });

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
        //Add to our database
        mHistoryViewModel.getSingleHistory(mLatestSearch).observe(this, new Observer<HistoryItem>() {
            @Override
            public void onChanged(@Nullable HistoryItem historyItem) {
                if(historyItem != null){
                    //Do nothing, already in list.
                }
                else{
                    //Add to list.
                    HistoryItem temp = new HistoryItem();
                    temp.StockName = mLatestSearch;
                    mHistoryViewModel.insertHistory(temp);
                }
            }
        });
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
            case R.id.action_name:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_search_test:
                Log.d(TAG, "query is " + mLatestSearch);
                Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                searchIntent.putExtra(SearchManager.QUERY, mLatestSearch + " NASDAQ company list symbols");
                startActivity(searchIntent);
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mDrawerLayout.closeDrawers();
        switch (menuItem.getItemId()){
            default:
                return false;
        }
    }

    @Override
    public void onHistoryClick(HistoryItem info) {
        //Activity item clicked, update.
        doAlphaVantageSearch(info.StockName);
        mDrawerLayout.closeDrawers();
    }
}