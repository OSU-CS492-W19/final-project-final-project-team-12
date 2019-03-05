package com.example.cs492final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mChatRV;
    private EditText mMessageET;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChatRV = findViewById(R.id.rv_chat_list);
        mMessageET = findViewById(R.id.et_message);

        mRecyclerViewAdapter = new RecyclerViewAdapter();

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
                }
            }
        });
        //Get information from the Slack Server

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
}