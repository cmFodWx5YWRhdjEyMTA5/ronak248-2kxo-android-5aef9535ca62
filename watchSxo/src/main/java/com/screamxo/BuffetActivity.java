package com.screamxo;

import android.content.Intent;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.WindowInsets;

import com.screamxo.adapter.BuffetAdapter;
import com.screamxo.based.BasedActivity;
import com.screamxo.common.ShapeWear;

public class BuffetActivity extends BasedActivity {
    private GridViewPager pager;
    private DotsPageIndicator dotsPageIndicator;

    @Override
    public int getRectLayout() {
        return R.layout.viewpager_rect;
    }

    @Override
    public int getRoundLayout() {
        return R.layout.viewpager_round;
    }

    @Override
    public void getLayoutType(ShapeWear.ScreenShape shape) {

    }

    @Override
    public void init(Intent intent) {

    }

    @Override
    public void initControl(WatchViewStub view) {
        pager = (GridViewPager) view.findViewById(R.id.pager);
        dotsPageIndicator = (DotsPageIndicator) view.findViewById(R.id.page_indicator);
    }

    @Override
    public void initValue() {
        pager.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                final boolean round = insets.isRound();
                int rowMargin = getResources().getDimensionPixelOffset(R.dimen.page_row_margin);
                int colMargin = getResources().getDimensionPixelOffset(round ?
                        R.dimen.page_column_margin_round : R.dimen.page_column_margin);
                pager.setPageMargins(rowMargin, colMargin);
                pager.onApplyWindowInsets(insets);
                return insets;
            }
        });
        pager.setAdapter(new BuffetAdapter(this, getFragmentManager()));
        dotsPageIndicator.setPager(pager);
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
