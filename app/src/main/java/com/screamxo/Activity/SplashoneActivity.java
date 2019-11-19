package com.screamxo.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class SplashoneActivity extends AppCompatActivity {

    private ImageView imgLogo;
    private ConstraintLayout constraintLayout;
    private Disposable subscribe;
    private Preferences preferences;
    public static final String PACKAGE = "com.screamxo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashone);
        getWindow().setStatusBarColor(Color.BLACK);
        preferences = new Preferences(SplashoneActivity.this);
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_main);
        FirebaseInstanceId.getInstance().getToken();
        generateHashkey();
        startCountDownTimer();
    }

    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(PACKAGE, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(signature.toByteArray());
                Log.i("hashkey", ".........." + Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

    private void startCountDownTimer() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        constraintLayout.clearAnimation();
        constraintLayout.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.translet);
        anim.reset();
        imgLogo.clearAnimation();
        imgLogo.startAnimation(anim);

        subscribe = Observable.just(100)
                .delay(1500, TimeUnit.MILLISECONDS)
                .subscribe(integer -> {
                    startNextActivity();
                    SplashoneActivity.this.finish();
                });
    }

    private void startNextActivity() {
        Intent gotoNext;
        preferences.setFirstTimeLogin(false);

        if (preferences.isFirstTimeLogin()) {
            gotoNext = new Intent(SplashoneActivity.this, CommonLoginSignUpActivity.class);
            startActivity(gotoNext);
            finish();
            return;
        } else if (!preferences.getUserId().isEmpty() && !preferences.getUserToken().isEmpty()) {
            gotoNext = new Intent(SplashoneActivity.this, DrawerMainActivity.class);
            startActivity(gotoNext);
            finish();
            return;
        } else {
            gotoNext = new Intent(SplashoneActivity.this, CommonLoginSignUpActivity.class);
            startActivity(gotoNext);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isDisposed())
            subscribe.dispose();
        super.onDestroy();
    }
}

/*
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class SplashoneActivity extends AppCompatActivity {
    public static final String PACKAGE = "com.screamxo";
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashone);
        preferences = new Preferences(SplashoneActivity.this);
        FirebaseInstanceId.getInstance().getToken();
        initTimer();
        generateHashkey();
    }

    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(PACKAGE, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("hashkey", ".........." + Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

    private void initTimer() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                Intent gotoNext;
                preferences.setFirstTimeLogin(false);
                if (preferences.isFirstTimeLogin()) {
                    gotoNext = new Intent(SplashoneActivity.this, CommonLoginSignUpActivity.class);
                    startActivity(gotoNext);
                    finish();
                    return;
                } else if (!preferences.getUserId().isEmpty() && !preferences.getUserToken().isEmpty()) {
                    gotoNext = new Intent(SplashoneActivity.this, DrawerMainActivity.class);
                    startActivity(gotoNext);
                    finish();
                    return;
                } else {
                    gotoNext = new Intent(SplashoneActivity.this, CommonLoginSignUpActivity.class);
                    startActivity(gotoNext);
                    finish();
                }
            }
        };
        timer.schedule(timerTask, 3000);
    }
}
*/
