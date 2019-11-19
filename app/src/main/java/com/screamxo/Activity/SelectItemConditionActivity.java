package com.screamxo.Activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.screamxo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectItemConditionActivity extends AppCompatActivity {

    @BindView(R.id.lnr_like_new)
    LinearLayout lnrLikeNew;
    @BindView(R.id.lnr_new)
    LinearLayout lnrNew;
    @BindView(R.id.lnr_refurnished)
    LinearLayout lnrRefurnished;
    @BindView(R.id.lnr_fair)
    LinearLayout lnrFair;
    @BindView(R.id.lnr_poor)
    LinearLayout lnrPoor;
    @BindView(R.id.ico_back)
    ImageView icoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_item_condition);
        ButterKnife.bind(this);

        icoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lnrNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectCondition("New");
            }
        });

        lnrLikeNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectCondition("Like New");
            }
        });

        lnrRefurnished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectCondition("Refurbished");
            }
        });

        lnrFair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectCondition("Fair");
            }
        });

        lnrPoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectCondition("Poor");
            }
        });
    }

    private void onSelectCondition(String selectedCondition) {
        Intent intent = new Intent();
        intent.putExtra("Selected Condition",selectedCondition);
        setResult(RESULT_OK,intent);
        finish();
    }
}
