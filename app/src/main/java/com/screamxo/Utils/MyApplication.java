package com.screamxo.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.NotificationCompat;

import com.crashlytics.android.Crashlytics;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailResult;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.screamxo.BuildConfig;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

/**
 * For reminder me@uokafor.com
 */
public class MyApplication extends MultiDexApplication {
    static MyApplication instance = null;
    public ArrayList<ItemDetailResult> cartGlobalArray = new ArrayList<>();
    public ArrayList<ItemDetailResult> cartSaveArray = new ArrayList<>();
    private boolean firstTime = true;

    public static MyApplication getInstance() {
        return instance;
    }

    public ArrayList<ItemDetailResult> getCartGlobalArray() {
        return cartGlobalArray;
    }

    public void setCartGlobalArray(ArrayList<ItemDetailResult> cartGlobalArray) {
        this.cartGlobalArray = cartGlobalArray;
    }

    public ArrayList<ItemDetailResult> getCartSaveArray() {
        return cartSaveArray;
    }

    public void setCartSaveArray(ArrayList<ItemDetailResult> cartSaveArray) {
        this.cartSaveArray = cartSaveArray;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);
        //Fabric.with(this, new Twitter(authConfig), new Crashlytics());
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }

    public void addCartItem(ItemDetailResult result) {
        if (cartGlobalArray != null) {
            cartGlobalArray.add(result);
        }
    }

    public void removeCartItem(ItemDetailResult result) {
        if (cartGlobalArray != null) {
            cartGlobalArray.remove(result);
        }
    }

    public void addSaveItemInArray(ItemDetailResult result) {
        if (cartSaveArray != null) {
            cartSaveArray.add(result);
        }
    }

    public void showNoti(String title, String content, int icon, int notiId, NotificationCompat.Builder builder) {


        builder.setAutoCancel(false);
        builder.setSmallIcon(icon)
                .setContentTitle(title)
                .setSound(null)
                .setContentText(content);

        builder.setOnlyAlertOnce(true);
        builder.setOngoing(true);
        NotificationManager mNotificationManager = (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("131", "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);

            assert mNotificationManager != null;
            builder.setChannelId("131");
            mNotificationManager.createNotificationChannel(notificationChannel);
            mNotificationManager.notify(notiId, builder.build());
        } else
            mNotificationManager.notify(notiId, builder.build());
    }

    public void cancleNotification(int id) {
        NotificationManager mNotificationManager = (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(id);
    }

}
