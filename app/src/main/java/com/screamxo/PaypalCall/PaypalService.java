package com.screamxo.PaypalCall;

import com.screamxo.PaypalCall.PaypalBean.PaypalAccessTokenBean;
import com.screamxo.PaypalCall.PaypalBean.PaypalUserInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by Shubham Agarwal on 28/01/17.
 */

public interface PaypalService {

    @POST("identity/openidconnect/tokenservicen")
    Call<PaypalAccessTokenBean> getRefreskToken(@QueryMap Map<String, String> params);

    @GET("identity/openidconnect/userinfo/?schema=openid")
    Call<PaypalUserInfo> getUserEmail();
}
