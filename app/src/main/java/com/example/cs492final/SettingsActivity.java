package com.example.cs492final;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private TextView titleTV;
    private TextView TV2;
    private TextView TV3;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        titleTV = findViewById(R.id.tv1);
        TV2 = findViewById(R.id.tv2);
        TV3 = findViewById(R.id.tv3);
    }
}
