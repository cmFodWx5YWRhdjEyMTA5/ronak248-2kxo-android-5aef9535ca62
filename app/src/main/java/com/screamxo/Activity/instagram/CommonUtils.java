package com.screamxo.Activity.instagram;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public final class CommonUtils {
    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }
}