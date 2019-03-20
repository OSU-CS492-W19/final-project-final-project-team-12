package com.example.cs492final;

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

import java.io.IOException;
import java.util.List;
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

        Button mSendB = findViewById(R.id.b_send);

        mViewModel= ViewModelProviders.of(this).get(AlphaVantageViewModel.class);
        //Here's where things go wrong, I think -E
        //this version of the fn is expecting the mapping and crashes on the List
        mViewModel.getmSearchResultsMap().observe(this, new Observer<Map<String, AlphaVantageUtils.AlphaVantageRepo>>() {
            @Override
            public void onChanged(@Nullable Map<String, AlphaVantageUtils.AlphaVantageRepo> stringAlphaVantageRepoMap) {
                mRecyclerViewAdapter.updateSearchResults(stringAlphaVantageRepoMap);
            }

//             This version was an experiment that kinda went nowhere
//            @Override
//            public void onChanged(@Nullable Map<String, AlphaVantageUtils.AlphaVantageRepo> alphaVantageRepos) {
//
//                //updateUI with new search result data
//                mRecyclerViewAdapter.updateSearchResults(alphaVantageRepos);
//            }
        });
        //Create an onclicklistener to listen for when the button is pressed.
        mSendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the string from the EditText box.
                String messageText = mMessageET.getText().toString();
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
//        String url = AlphaVantageUtils.buildAlphaVantageURL(query);
//        Log.d(TAG, "querying search URL: " + url);
//        new GitHubSearchTask().execute(url);   //uses Async - will add ViewModel at some point
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


    class GitHubSearchTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, "onPreExecute");
            mLoadingIndicatorPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            Log.d(TAG,"in doInBackground");
            String url = urls[0];
            String results = null;
            try {
                results = NetworkUtils.doHTTPGet(url);
                //Log.d(TAG, "after GET " + results);
            } catch (IOException e) {
                Log.d(TAG, "error");
                e.printStackTrace();
            }
            return results;
        }


        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, "onPostExecute s is " + s.contains("Error Message"));
            if (s != null && s.contains("Error Message") == false) {
                mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                Map<String, AlphaVantageUtils.AlphaVantageRepo> repos = AlphaVantageUtils.parseGitHubSearchResults(s);

                //Log.d(TAG,"key is 2019-03-14 15:30:00  -  open value is : " + repos.get("2019-03-14 15:30:00").open);
                //Log.d(TAG, "after parsing");
                mRecyclerViewAdapter.updateSearchResults(repos);
            }
            else if (s.contains("Error Message") == true) {
                mChatRV.setVisibility(View.INVISIBLE);
                mAPIErrorMessageTV.setVisibility(View.VISIBLE);
            }
            else {
                mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
                mChatRV.setVisibility(View.INVISIBLE);
            }
            mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        }
    }
}