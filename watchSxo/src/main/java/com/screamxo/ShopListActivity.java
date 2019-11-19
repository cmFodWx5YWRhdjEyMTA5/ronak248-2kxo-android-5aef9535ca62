package com.screamxo;

import android.content.Intent;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableRecyclerView;
import android.widget.TextView;

import com.screamxo.adapter.ShopListAdapter;
import com.screamxo.based.BasedActivity;
import com.screamxo.common.NormalOffsettingHelper;
import com.screamxo.common.ScalingOffsettingHelper;
import com.screamxo.common.ShapeWear;

public class ShopListActivity extends BasedActivity implements ShopListAdapter.ItemSelectedListener {
    private WearableRecyclerView recyclerView;
    private TextView txtTitle;
    private ShopListAdapter adapter;
    private boolean isRounded;

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
        isRounded = shape == ShapeWear.ScreenShape.ROUND;
    }

    @Override
    public void init(Intent intent) {
        isRounded = false;
    }

    @Override
    public void initControl(WatchViewStub view) {
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        recyclerView = (WearableRecyclerView) view.findViewById(R.id.recyclerView);
    }

    @Override
    public void initValue() {
        txtTitle.setText(getString(R.string.shop));
        if(isRounded) {
            ScalingOffsettingHelper offsettingHelper = new ScalingOffsettingHelper();
            recyclerView.setOffsettingHelper(offsettingHelper);
        } else {
            NormalOffsettingHelper offsettingHelper = new NormalOffsettingHelper();
            recyclerView.setOffsettingHelper(offsettingHelper);
        }
        adapter = new ShopListAdapter(getApplicationContext());
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
        callDetailScreen();
    }

    private void callDetailScreen() {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        startActivity(intent);
    }
}
