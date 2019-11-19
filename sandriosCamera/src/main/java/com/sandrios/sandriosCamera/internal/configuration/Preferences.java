package com.sandrios.sandriosCamera.internal.configuration;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shubham Agarwal on 28/12/16.
 */

public class Preferences {
    private Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    public String getUserId() {
        SharedPreferences preferences = context.getSharedPreferences("ScreamXO_preference", Context.MODE_PRIVATE);
        return preferences.getString("id", "");
    }

    public String getStripeCustomerId() {
        SharedPreferences preferences = context.getSharedPreferences("ScreamXO_preference", Context.MODE_PRIVATE);
        return preferences.getString("stripe_customer_id", "1");
    }
}
