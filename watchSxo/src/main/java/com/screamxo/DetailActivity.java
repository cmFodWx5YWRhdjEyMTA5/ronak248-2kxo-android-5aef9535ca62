package com.screamxo;

import android.content.Intent;
import android.support.wearable.view.WatchViewStub;

import com.screamxo.based.BasedActivity;
import com.screamxo.common.ShapeWear;

public class DetailActivity extends BasedActivity {

    @Override
    public int getRectLayout() {
        return R.layout.item_detail_rect;
    }

    @Override
    public int getRoundLayout() {
        return R.layout.item_detail_round;
    }

    @Override
    public void getLayoutType(ShapeWear.ScreenShape shape) {
    }

    @Override
    public void init(Intent intent) {

    }

    @Override
    public void initControl(WatchViewStub view) {
    }

    @Override
    public void initValue() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
