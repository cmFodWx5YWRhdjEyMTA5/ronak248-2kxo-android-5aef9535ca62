package com.screamxo.Utils;

import android.content.Context;
import android.util.Log;

import com.example.apimodule.ApiBase.ApiBean.CategoryList.CategoryList;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by parangat on 24/11/17.
 */

public class ApiConnectionUtils {
    private static final String TAG = "ApiConnectionUtils";

    public static void initCategory(Context context) {
        Log.d(TAG, "initCategory: ");
        Preferences preferences = new Preferences(context);
        FetchrServiceBase fetchrServiceBase = new FetchrServiceBase();
        if (Utils.isInternetOn(context)) {
            Call<CategoryList> categoryListCall = fetchrServiceBase.getFetcherService(context).GetCategory();
            categoryListCall.enqueue(new retrofit2.Callback<CategoryList>() {
                @Override
                public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            StaticConstant.categoryList.addAll(response.body().getResult().getCategories());
                            Gson gson = new Gson();
                            preferences.saveCategory(gson.toJson(response.body().getResult().getCategories()));
                            Log.e(TAG, "onResponse preferences.getCategory: " + preferences.getCategory());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CategoryList> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public static void initNewCategories(Context context) {
        Log.d(TAG, "initNewCategories: ");
        Preferences preferences = new Preferences(context);
        FetchrServiceBase fetchrServiceBase = new FetchrServiceBase();
        if (Utils.isInternetOn(context)) {
            Map<String,String> map= new HashMap<>();
            map.put("twitterfriends","[{\"twitter_id\":\"932842479220539393\"},{\"twitter_id\":\"931477499158044672\"}]");
            Call<CategoryList> categoryListCall = fetchrServiceBase.getFetcherService(context).getNewCategories(map);
            categoryListCall.enqueue(new retrofit2.Callback<CategoryList>() {
                @Override
                public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                         {
                            Gson gson = new Gson();
                            preferences.saveNewCategory(gson.toJson(response.body().getResult()));
                            Log.d(TAG, "onResponse preferences.getNewCategories: " + preferences.getNewCategories());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CategoryList> call, Throwable t) {    t.printStackTrace();
                }
            });
        }
    }

}
