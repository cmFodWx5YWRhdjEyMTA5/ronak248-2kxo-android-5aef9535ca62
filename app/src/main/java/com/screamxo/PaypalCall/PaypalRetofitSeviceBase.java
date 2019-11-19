package com.screamxo.PaypalCall;

import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.screamxo.Utils.StaticConstant;

import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.services.network.HttpRequest;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shubham Agarwal on 28/01/17.
 */

public class PaypalRetofitSeviceBase {

    private Retrofit GetRestAdapter(final Context context, String authHeader, String content_type) {

        String BASE_URL;
        BASE_URL = "https://api.sandbox.paypal.com/v1/";

        Interceptor interceptor = chain -> {

            Request original = chain.request();

            String text = "AYuIo-X4PK27s5RernB7kkIKAhsqjI3IsnX3B-Zp9utG3LH3IaIY4Swqz5si23ErYjRXGe2OC_zukrrd" + ":" + "ECXLfKPL8WfAsrSiZtN-mrUJ9FiImJHzGKQsZwWRsBBvpeuIulH2klkV09QfabGunX10q98VUlDjg0eY";
            Log.e(StaticConstant.SECRET_KEY, StaticConstant.CONFIG_CLIENT_ID);

            String base64 = "Basic " + HttpRequest.Base64.encode(text);
            base64 = base64.trim();
            Log.e("PayPal Base 64String-->", authHeader);

            Request request = original.newBuilder()
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        };

        //setup cache
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(loggingInterceptor)
                .addInterceptor(interceptor)
                .readTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES);

        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public PaypalService getFetcherService(Context context, String authHeader, String content_type) {
        return GetRestAdapter(context, authHeader, content_type).create(PaypalService.class);
    }
}
