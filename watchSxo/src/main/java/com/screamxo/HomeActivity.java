package com.screamxo;

import android.content.Intent;
import android.support.wearable.view.WatchViewStub;
import android.text.Html;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.screamxo.based.BasedActivity;
import com.screamxo.common.ShapeWear;

public class HomeActivity extends BasedActivity implements View.OnClickListener {
    private TextView txtScreamxo;
    private LinearLayout viewBuffet;
    private LinearLayout viewShop;
    private LinearLayout viewStream;

    @Override
    public int getRectLayout() {
        return R.layout.home_rect;
    }

    @Override
    public int getRoundLayout() {
        return R.layout.home_round;
    }

    @Override
    public void getLayoutType(ShapeWear.ScreenShape shape) {
    }

    @Override
    public void init(Intent intent) {
    }

    @Override
    public void initControl(WatchViewStub view) {
        txtScreamxo = (TextView) view.findViewById(R.id.txtScreamxo);
        viewBuffet = (LinearLayout) view.findViewById(R.id.viewBuffet);
        viewShop = (LinearLayout) view.findViewById(R.id.viewShop);
        viewStream = (LinearLayout) view.findViewById(R.id.viewStream);
    }

    @Override
    public void initValue() {
        txtScreamxo.setText(Html.fromHtml(getString(R.string.screamxo)));
    }

    @Override
    public void initListener() {
        viewBuffet.setOnClickListener(this);
        viewShop.setOnClickListener(this);
        viewStream.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewBuffet:
                callBuffetScreen();
                break;

            case R.id.viewShop:
                callShopScreen();
                break;

            case R.id.viewStream:
                callStreamScreen();
                break;
        }
    }

    private void callBuffetScreen() {
        Intent intent = new Intent(getApplicationContext(), BuffetActivity.class);
        startActivity(intent);
    }

    private void callShopScreen() {
        Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
        startActivity(intent);
    }

    private void callStreamScreen() {
        Intent intent = new Intent(getApplicationContext(), StreamActivity.class);
        startActivity(intent);
    }
}
