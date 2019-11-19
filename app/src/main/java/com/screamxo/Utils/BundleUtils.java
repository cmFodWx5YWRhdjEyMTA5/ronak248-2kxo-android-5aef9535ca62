package com.screamxo.Utils;

import android.content.Intent;
import android.os.Bundle;

import javax.annotation.Nullable;

/**
 * Created by parangat on 26/10/17.
 */

public class BundleUtils {

    @SuppressWarnings("unused")
    private static final String TAG = "BundleUtils";

    public static String getIntentExtra(Intent intent, String Key, @Nullable String defaultValue) {
        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey(Key)) {
            return intent.getExtras().getString(Key);
        }
        return defaultValue;
    }

    public static String getBundleExtra(Bundle bundle, String Key, @Nullable String defaultValue) {
        if (bundle != null && bundle.containsKey(Key)) {
            return bundle.getString(Key);
        }
        return defaultValue;
    }

    public static boolean getIntentExtra(Intent intent, String Key, @Nullable boolean defaultValue) {
        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey(Key)) {
            return intent.getExtras().getBoolean(Key);
        }
        return defaultValue;
    }

    public static boolean getBundleExtra(Bundle bundle, String Key, @Nullable boolean defaultValue) {
        if (bundle != null && bundle.containsKey(Key)) {
            return bundle.getBoolean(Key);
        }
        return defaultValue;
    }




}
