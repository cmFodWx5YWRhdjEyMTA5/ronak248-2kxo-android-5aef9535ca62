package com.screamxo.based;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.WindowInsets;

import com.screamxo.R;
import com.screamxo.common.ShapeWear;

/**
 * Created by Krunal.Kevadiya on 22/02/17.
 */
public abstract class BasedActivity extends Activity {
    private WatchViewStub stub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_based);

        init(getIntent());
        ShapeWear.initShapeWear(this);

        stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setRectLayout(getRectLayout());
        stub.setRoundLayout(getRoundLayout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLayoutType(ShapeWear.getShape());
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                initControl(stub);
                initValue();
                initListener();
            }
        });
    }

    public abstract int getRectLayout();
    public abstract int getRoundLayout();
    public abstract void getLayoutType(ShapeWear.ScreenShape shape);

    public abstract void init(Intent intent);
    public abstract void initControl(WatchViewStub view);
    public abstract void initValue();
    public abstract void initListener();
}
