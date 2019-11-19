package com.screamxo.Activity.RahulWork;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.screamxo.R;
import com.screamxo.Utils.StaticConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConditionActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ll_New)
    LinearLayout ll_New;
    @BindView(R.id.ll__Like_New)
    LinearLayout ll__Like_New;
    @BindView(R.id.ll__Refurbished)
    LinearLayout ll__Refurbished;
    @BindView(R.id.ll__Fair)
    LinearLayout ll__Fair;
    @BindView(R.id.ll__Poor)
    LinearLayout ll__Poor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);
        ButterKnife.bind(this);
    }

    public void funBack(View view) {
        finish();
    }

    @OnClick({R.id.ll_New, R.id.ll__Like_New, R.id.ll__Refurbished, R.id.ll__Fair, R.id.ll__Poor})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_New:
                StaticConstant.SELL_CONDITION = getString(R.string.txt_new);
                returnResult("New");
                break;
            case R.id.ll__Like_New:
                StaticConstant.SELL_CONDITION = getString(R.string.txt_like_new);
                returnResult("Like New");
                break;
            case R.id.ll__Refurbished:
                StaticConstant.SELL_CONDITION = getString(R.string.txt_refurnished);
                returnResult("Refurbished");
                break;
            case R.id.ll__Fair:
                StaticConstant.SELL_CONDITION = getString(R.string.txt_fair);
                returnResult("Fair");
                break;
            case R.id.ll__Poor:
                StaticConstant.SELL_CONDITION = getString(R.string.txt_poor);
                returnResult("Poor");
                break;
        }
    }

    private void returnResult(String condition) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("SELL_CONDITION", condition);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
