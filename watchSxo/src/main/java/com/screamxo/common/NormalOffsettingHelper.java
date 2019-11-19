package com.screamxo.common;

import android.support.wearable.view.DefaultOffsettingHelper;
import android.support.wearable.view.WearableRecyclerView;
import android.view.View;

public class NormalOffsettingHelper extends DefaultOffsettingHelper {
    public NormalOffsettingHelper() {}

    @Override
    public void updateChild(View child, WearableRecyclerView parent) {
        super.updateChild(child, parent);
    }
}