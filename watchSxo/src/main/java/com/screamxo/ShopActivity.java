package com.screamxo;

import android.content.Intent;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableRecyclerView;
import android.widget.TextView;

import com.screamxo.adapter.ShopAdapter;
import com.screamxo.based.BasedActivity;
import com.screamxo.common.NormalOffsettingHelper;
import com.screamxo.common.ShapeWear;

public class ShopActivity extends BasedActivity implements ShopAdapter.ItemSelectedListener {
    private WearableRecyclerView recyclerView;
    private TextView txtTitle;
    private ShopAdapter adapter;

    @Override
    public int getRectLayout() {
        return R.layout.recycleview_rect;
    }

    @Override
    public int getRoundLayout() {
        return R.layout.recycleview_round;
    }

    @Override
    public void getLayoutType(ShapeWear.ScreenShape shape) {

    }

    @Override
    public void init(Intent intent) {

    }

    @Override
    public void initControl(WatchViewStub view) {
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        recyclerView = (WearableRecyclerView) view.findViewById(R.id.recyclerView);
    }

    @Override
    public void initValue() {
        txtTitle.setText(getString(R.string.shop));
        NormalOffsettingHelper offsettingHelper = new NormalOffsettingHelper();
        recyclerView.setOffsettingHelper(offsettingHelper);
        adapter = new ShopAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void initListener() {
        adapter.setListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemSelected(int position) {
        callShopListScreen();
    }

    private void callShopListScreen() {
        Intent intent = new Intent(getApplicationContext(), ShopListActivity.class);
        startActivity(intent);
    }
}
