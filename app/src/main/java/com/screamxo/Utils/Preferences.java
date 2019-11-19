package com.screamxo.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.apimodule.ApiBase.ApiBean.CategoryList.Category;
import com.example.apimodule.ApiBase.ApiBean.Result;
import com.screamxo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Preferences extends com.example.apimodule.ApiBase.Preferences {
    private Context context;

    public Preferences(Context context) {
        super(context);
        this.context = context;
    }

    public void saveCategory(String data) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("categorydata", data);
        editor.apply();
    }

    public ArrayList<Category> getCategory() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return StaticConstant.categoryList;
    }

    public void saveNewCategory(String data) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("newCategorydata", data);
        editor.apply();
    }

    public String getNewCategories() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return preferences.getString("newCategorydata", "");
    }

    public void saveAmount(String data) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Walletamount", data);
        /*Preferences preferences1 = new Preferences(context);
        preferences1.saveAmount(""+data);*/
        editor.apply();
    }

    public String getAmount() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return preferences.getString("Walletamount", "");
    }


    public void saveLoginUser(Result bean, String token) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.key_avtar), bean.getPhoto());
        editor.putString(context.getString(R.string.key_mobile), bean.getPhone());
        editor.putString(context.getString(R.string.key_latitude), bean.getLat());
        editor.putString(context.getString(R.string.key_longitude), bean.getLon());
        editor.putString(context.getString(R.string.key_user_token), "" + bean.getToken());

        editor.putString("gender", bean.getGender());
        editor.putString("Walletamount", "" + bean.getWalletAmount());
        editor.putString("realtionstatus", bean.getRealtionstatus());
        editor.putString("sexpreference", bean.getSexpreference());
        editor.putString("job", bean.getJob());
        editor.putString("school", bean.getSchool());
        editor.putString("address", "" + bean.getAddress());
        editor.putString("nationality", bean.getNationality());
        editor.putString("hobby", bean.getHobbies());
        editor.putString("email", bean.getEmail());
        editor.putString("country", bean.getCountry());
        editor.putString("state", bean.getState());

        if (!TextUtils.isEmpty(bean.getFname())) {
            editor.putString("name", bean.getFname());
        }
        if (!TextUtils.isEmpty(bean.getUsername())) {
            editor.putString("username", bean.getUsername());
        }
        editor.apply();

        setStripeCustomerId(bean.getStripeCustomerId());
        com.example.apimodule.ApiBase.Preferences preferences1 = new com.example.apimodule.ApiBase.Preferences(context);
        preferences1.setUserid("" + bean.getId());
        preferences1.setUserToken(token);
    }

    public String getUserEmail() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return preferences.getString("email", "");
    }

    public void setUserEmail(String email) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    public int getServiceId() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return preferences.getInt("serviceId", 0);
    }

    public void setServiceId(int serviceId) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("serviceId", serviceId);
        editor.apply();
    }

    public String getStripeCustomerId() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return preferences.getString("stripe_customer_id", "1");
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("stripe_customer_id", stripeCustomerId);
        editor.apply();
    }

    public boolean isFirstTimeLogin() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return preferences.getBoolean("first_time_login", false);
    }

    public void setFirstTimeLogin(boolean isFirstTimeLogin) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first_time_login", isFirstTimeLogin);
        editor.apply();
    }

    public String getUserFullName() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return preferences.getString("name", "");
    }

    public String getUserId() {
        SharedPreferences preferences = context.getSharedPreferences("ScreamXO_preference", Context.MODE_PRIVATE);
        return preferences.getString("id", "");
    }

    public String getUserName() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return preferences.getString("username", "");
    }

    public String getUserToken() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return preferences.getString(context.getString(R.string.key_user_token), null);
    }

    public String getUserProfile() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return preferences.getString("avatar", null);

    }

    public String getCountry() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return preferences.getString("country", null);

    }

    public String getState() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        return preferences.getString("state", null);
    }

    public HashMap<String, String> getUserDetail() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        HashMap<String, String> map = new HashMap<>();
        map.put("avatar", preferences.getString("avatar", ""));
        map.put("name", preferences.getString("name", ""));
        map.put("school", preferences.getString("school", ""));
        map.put("job", preferences.getString("job", ""));
        map.put("hobby", preferences.getString("hobby", ""));
        map.put("nationality", preferences.getString("nationality", ""));
        map.put("address", preferences.getString("address", ""));
        map.put("sexpreference", preferences.getString("sexpreference", "o"));
        map.put("realtionstatus", preferences.getString("realtionstatus", "a"));
        map.put("gender", preferences.getString("gender", "m"));
        map.put("username", preferences.getString("username", ""));
        map.put("lat", preferences.getString("lat", ""));
        map.put("lon", preferences.getString("lon", ""));
        map.put("country", preferences.getString("country", ""));
        map.put("state", preferences.getString("state", ""));
        return map;

    }

    public void setUserDetail(Map<String, String> uDetail) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("gender", uDetail.get("gender"));
        editor.putString("realtionstatus", uDetail.get("realtionstatus"));
        editor.putString("sexpreference", uDetail.get("sexpreference"));
        editor.putString("job", uDetail.get("job"));
        editor.putString("school", uDetail.get("school"));
        editor.putString("address", uDetail.get("city"));
        editor.putString("nationality", uDetail.get("nationality"));
        editor.putString("hobby", uDetail.get("hobbies"));

        if (uDetail.get("avatar") != null && !uDetail.get("avatar").equals("")) {
            editor.putString("avatar", uDetail.get("avatar"));
        }
        editor.putString("name", uDetail.get("fname") + " " + uDetail.get("lname"));
        editor.putString("username", uDetail.get("username"));
        if (uDetail.containsKey("username")) {
            editor.putString("username", uDetail.get("username"));
        }
        editor.putString("email", uDetail.get("email"));
        editor.putString("lat", uDetail.get("lat"));
        editor.putString("lon", uDetail.get("lon"));
        editor.putString("country", uDetail.get("country"));
        editor.putString("state", uDetail.get("state"));

        editor.apply();
    }

    public void clearAllApreferences() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_screamXo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.key_avtar), "");
        editor.putString("name", "");
        editor.putString("email", "");
        editor.putString("username", "");
        editor.putString("uname", "");
        editor.putString("id", null);
        editor.putString("usertoken", "");
        editor.putString(context.getString(R.string.key_mobile), "");
        editor.putString(context.getString(R.string.key_latitude), "");
        editor.putString(context.getString(R.string.key_longitude), "");
        editor.putString("gender", "");
        editor.putString("realtionstatus", "");
        editor.putString("sexpreference", "");
        editor.putString("job", "");
        editor.putString("school", "");
        editor.putString("address", "");
        editor.putString("nationality", "");
        editor.putString("hobby", "");
        editor.putString("Walletamount", "");
        editor.putString("stripe_customer_id", "");
        setUserid("");
        setUserToken("");
        editor.apply();
    }

}
