package com.example.cs492final;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ItemDetailActivity extends AppCompatActivity {

    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        mTitle = findViewById(R.id.tv_title);

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra("TEMP")){
            //Update for getting the required information and assigning it to the required content.
            mTitle.setText((String) intent.getSerializableExtra("TEMP"));
        }


    }
}
