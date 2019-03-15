package com.example.cs492final;

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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements RecyclerViewAdapter.OnTempItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mChatRV;
    private EditText mMessageET;
    private RecyclerViewAdapter mRecyclerViewAdapter;

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

        Button mSendB = findViewById(R.id.b_send);
        //Create an onclicklistener to listen for when the button is pressed.
        mSendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the string from the EditText box.
                String messageText = mMessageET.getText().toString();
                //Check to see if there is anything in the message box.
                if (!TextUtils.isEmpty(messageText)) {
                    //Update to send to the slack server.
                    mRecyclerViewAdapter.addChat(messageText);
                    mMessageET.setText("");
                    doAlphaVantageSearch(messageText);
                }
            }
        });
        //Get information from the Slack Server

    }

    private void doAlphaVantageSearch(String query) {
        String url = AlphaVantageUtils.buildAlphaVantageURL(query);
        Log.d(TAG, "querying search URL: " + url);
        new GitHubSearchTask().execute(url);
    }

    @Override
    public void onTempItemClick(String s){ //Update parameter based off data type we clicked on
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra("TEMP", s);
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
            Log.d(TAG, "onPreExecute");
            //mLoadingPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            Log.d(TAG,"in doInBackground");
            String url = urls[0];
            String results = null;
            try {
                results = NetworkUtils.doHTTPGet(url);
                Log.d(TAG, "after GET " + results);
            } catch (IOException e) {
                Log.d(TAG, "error");
                e.printStackTrace();
            }
            return results;
        }


        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, "onPostExecute");
            if (s != null) {

                /*DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(sdf.format(new Date()));

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 9);
                cal.set(Calendar.MINUTE, 30);
                cal.set(Calendar.SECOND, 00);

                date = cal.getTime();*/

                //mLoadingErrorTV.setVisibility(View.INVISIBLE);
                //mSearchResultsRV.setVisibility(View.VISIBLE);
                Map<String, AlphaVantageUtils.AlphaVantageRepo> repos = AlphaVantageUtils.parseGitHubSearchResults(s);
                mRecyclerViewAdapter.updateSearchResults(repos);
                Log.d(TAG,"repos dt_text: " + repos.get("2019-03-14 15:30:00").open);
                Log.d(TAG, "after parsing");
            } else {
                //mLoadingErrorTV.setVisibility(View.VISIBLE);
                //mSearchResultsRV.setVisibility(View.INVISIBLE);
            }
            //mLoadingPB.setVisibility(View.INVISIBLE);
        }
    }
}