package com.example.cs492final;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ItemDetailActivity extends AppCompatActivity {

    private TextView mTitle;
    private TextView mOpen;
    private TextView mClose;
    private TextView mHigh;
    private TextView mLow;
    private TextView mVolume;

    private AlphaVantageUtils.AlphaVantageRepo mRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        mTitle = findViewById(R.id.tv_title);
        mOpen = findViewById(R.id.tv_open);
        mClose = findViewById(R.id.tv_close);
        mHigh = findViewById(R.id.tv_high);
        mLow = findViewById(R.id.tv_low);
        mVolume = findViewById(R.id.tv_volume);


        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(AlphaVantageUtils.EXTRA_ALPHA_VANTAGE_ITEM)){
            //Update for getting the required information and assigning it to the required content.
            //mTitle.setText((String) intent.getSerializableExtra("TEMP"));
            mRepo = (AlphaVantageUtils.AlphaVantageRepo) intent.getSerializableExtra(
                    AlphaVantageUtils.EXTRA_ALPHA_VANTAGE_ITEM
            );
            fillInLayout(mRepo);
            //mOpen.setText(mRepo.open);
        }
    }

    private void fillInLayout(AlphaVantageUtils.AlphaVantageRepo repo){
        mTitle.setText("Closer View");
        mOpen.setText("Open: " + repo.open);
        mClose.setText("Close: " + repo.close);
        mHigh.setText("High: " + repo.high);
        mLow.setText("Low: " + repo.low);
        mVolume.setText("Volume: " + repo.volume);
    }
}
