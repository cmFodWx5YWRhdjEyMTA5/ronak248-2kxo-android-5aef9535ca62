package com.screamxo.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.MediaController;

import com.screamxo.Activity.RahulWork.GuessShareActivity;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.TextureVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    @BindView(R.id.vdView)
    TextureVideoView vdView;
    @BindView(R.id.rl_main)
    FrameLayout rlMain;
    Preferences preferences;
    private boolean isCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        preferences = new Preferences(SplashActivity.this);
        String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.splashvideo;
        Uri path = Uri.parse(uriPath);
        MediaController media_control = new MediaController(this);

        vdView.setScaleType(TextureVideoView.ScaleType.CENTER_CROP);
        vdView.setDataSource(this, path);
        vdView.play();
        vdView.setListener(new TextureVideoView.MediaPlayerListener() {
            @Override
            public void onVideoPrepared() {
                Log.d(TAG, "onVideoPrepared: ");
            }

            @Override
            public void onVideoEnd() {
                if (!isCall) {
                    isCall = true;
                    Intent in = new Intent(SplashActivity.this, GuessShareActivity.class);
                    startActivity(in);
                    finish();
                }
            }
        });
    }
}
