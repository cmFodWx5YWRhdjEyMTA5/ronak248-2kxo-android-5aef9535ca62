package com.example.apimodule.ApiBase;

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

    public void setUserid(String uid) {
        SharedPreferences preferences = context.getSharedPreferences("ScreamXO_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", uid);
        editor.apply();
    }


    public void setUsername(String uid) {
        SharedPreferences preferences = context.getSharedPreferences("ScreamXO_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", uid);
        editor.apply();
    }

   /* public void saveAmount(String data) {
        SharedPreferences preferences = context.getSharedPreferences("ScreamXO_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Walletamount", data);
        editor.apply();
    }*/


   /* public String getAmount() {
        SharedPreferences preferences = context.getSharedPreferences("ScreamXO_preference", Context.MODE_PRIVATE);
        return preferences.getString("Walletamount", "");
    }*/


    public String getUserToken() {
        SharedPreferences preferences = context.getSharedPreferences("ScreamXO_preference", Context.MODE_PRIVATE);
        return preferences.getString("usertoken", null);
    }

    public void setUserToken(String uid) {
        SharedPreferences preferences = context.getSharedPreferences("ScreamXO_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usertoken", uid);
        editor.apply();
    }
}
