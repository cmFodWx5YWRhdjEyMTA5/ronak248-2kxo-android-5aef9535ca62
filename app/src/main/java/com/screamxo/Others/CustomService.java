package com.screamxo.Others;

import com.example.apimodule.ApiBase.ApiBean.FollowersList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CustomService {
    @GET("/1.1/followers/list.json")
    Call<FollowersList> show();
}