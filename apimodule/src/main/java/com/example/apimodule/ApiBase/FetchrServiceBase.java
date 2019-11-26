package com.example.apimodule.ApiBase;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.twitter.sdk.android.core.internal.scribe.ScribeConfig.BASE_URL;

public class FetchrServiceBase {

//   http://development.screamxo.com/mobileservice/
//    https://api.screamxo.com/mobileservice/

    Boolean isInstaCall = false;
    private Preferences preferences;
    private String TAG = FetchrServiceBase.class.getSimpleName();

    private Retrofit GetRestAdapter(final Context context, boolean isUploadService) {
        String BASE_URL;
        if (isInstaCall) {
            BASE_URL = "https://api.instagram.com/v1/users/self/";
            isInstaCall = false;
        } else {
//            BASE_URL = "http://52.89.108.182:5000/mobileservice/";
//            BASE_URL = "http://52.89.108.182:5000/";
            BASE_URL = "http://apis.2kxo.com/";
            //  BASE_URL = "http://52.89.108.182/";
        }
        // BASE_URL = "http://development.screamxo.com/api/";
        preferences = new Preferences(context);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient(context, isUploadService))
                .build();
    }

    private Retrofit GetRestAdapterBoostItem(final Context context) {
//        String BASE_URL = "http://demo.swot.co.in/screamxo/public/api/";
        String BASE_URL = "https://www.2kxo.com/portal/public/api/";
        preferences = new Preferences(context);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient(context, false))
                .build();
    }

    private OkHttpClient getClient(final Context context, @SuppressWarnings("unused") final boolean isUploadService) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                String userdevice = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                String userToken = preferences.getUserToken();

                if (userToken == null) {
                    userToken = "null";
                }
                Request request = null;
                if (preferences.getUserId().isEmpty()) {
                    request = original.newBuilder()
//                            .header("uid", "1")
//                            .header("Authorization", "m6dYTparmi")
//                            .header("userdevice", "6CB9F594-07B7-4F4C-9078-2D78CA615B71")
                            .method(original.method(), original.body())
                            .build();
                } else {
                    request = original.newBuilder()
//                            .header("uid", "" + preferences.getUserId())
                            .header("Authorization", userToken)
//                            .header("userdevice", userdevice)
                            .method(original.method(), original.body())
                            .build();
                }
                Log.e(TAG, "intercept: uid--> " + preferences.getUserId() + "\n Authorization--> " + userToken + "\n userdevice--> " + userdevice);
                return chain.proceed(request);
            }
        };

        //setup cache
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(loggingInterceptor)
                .addInterceptor(interceptor)
                .readTimeout(15, TimeUnit.MINUTES)
                .connectTimeout(15, TimeUnit.MINUTES);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        builder.addInterceptor(interceptor);

        return builder.build();
    }

    public FetchrServiceInterface getFetcherService(Context context, boolean isUploadService, Boolean... isInstacall) {
        if (isInstacall != null && isInstacall.length > 0) {
            isInstaCall = isInstacall[0];
        }

        return GetRestAdapter(context, isUploadService).create(FetchrServiceInterface.class);
    }

    public FetchrServiceInterface getBoostFetcherService(Context context, boolean isUploadService) {

        return GetRestAdapterBoostItem(context).create(FetchrServiceInterface.class);
    }

    public FetchrServiceInterface getFetcherService(Context context, Boolean... isInstacall) {
        if (isInstacall != null && isInstacall.length > 0) {
            isInstaCall = isInstacall[0];
        }

        return GetRestAdapter(context, false).create(FetchrServiceInterface.class);
    }
}
