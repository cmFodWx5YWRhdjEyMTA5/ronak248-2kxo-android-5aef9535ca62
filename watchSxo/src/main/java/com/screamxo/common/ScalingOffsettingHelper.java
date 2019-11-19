package com.screamxo.common;

import android.support.wearable.view.DefaultOffsettingHelper;
import android.support.wearable.view.WearableRecyclerView;
import android.view.View;

public class ScalingOffsettingHelper extends DefaultOffsettingHelper {
    private static final float MAX_CHILD_SCALE = 0.65f;

    public ScalingOffsettingHelper() {}

    @Override
    public void updateChild(View child, WearableRecyclerView parent) {
        super.updateChild(child, parent);

        // Figure out % progress from top to bottom
        float centerOffset = ((float) child.getHeight() / 2.0f) /  (float) parent.getHeight();
        float yRelativeToCenterOffset = (child.getY() / parent.getHeight()) + centerOffset;

        // Normalize for center
        float mProgressToCenter = Math.abs(0.5f - yRelativeToCenterOffset);
        // Adjust to the maximum scale
        mProgressToCenter = Math.min(mProgressToCenter, MAX_CHILD_SCALE);

        child.setScaleX(1 - mProgressToCenter);
        child.setScaleY(1 - mProgressToCenter);
    }

    /*@Override
    public void adjustAnchorOffsetXY(View child, float[] anchorOffsetXY) {
        anchorOffsetXY[0] = child.getHeight() / 2.0f;
    }*/
}