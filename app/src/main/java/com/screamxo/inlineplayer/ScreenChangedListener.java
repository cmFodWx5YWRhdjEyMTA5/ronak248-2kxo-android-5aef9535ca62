package com.screamxo.inlineplayer;

import android.graphics.Rect;

interface ScreenChangedListener {
    void onScreenChanged(Rect windowRect, int visibility);
}
