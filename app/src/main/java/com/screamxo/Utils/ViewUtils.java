package com.screamxo.Utils;

import android.view.View;

/**
 * Created by parangat on 11/11/17.
 */

public class ViewUtils {

    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }
}
