package com.screamxo.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by rahul on 18/8/17.
 */

public class CustomSeekBar extends android.support.v7.widget.AppCompatSeekBar {

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    Drawable mThumb;

    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
        mThumb = thumb;
    }

    public Drawable getSeekBarThumb() {
        return mThumb;
    }
}