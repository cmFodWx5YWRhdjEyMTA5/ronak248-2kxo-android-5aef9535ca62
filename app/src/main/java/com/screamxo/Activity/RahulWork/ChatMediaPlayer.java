package com.screamxo.Activity.RahulWork;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.screamxo.R;

import tcking.github.com.giraffeplayer.GiraffePlayer;

public class ChatMediaPlayer extends AppCompatActivity {
    public GiraffePlayer player;
    LinearLayout rl_Video;
    Context context;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_screamxo_player);
        initView();
        initMedia();
    }

    private void initMedia() {
//        View view = LayoutInflater.from(context).inflate(R.layout.view_screamxo_player, null);
//        rl_Video.removeAllViews();
//        rl_Video.addView(view);
//        rl_Video.setVisibility(View.VISIBLE);
        player = new GiraffePlayer((AppCompatActivity) context);
        player.play(url);
        // DrawerMainActivity.checkfullscreen = true;
        player.onComplete(new Runnable() {
            @Override
            public void run() {
                player.seekTo(0, true);
                player.start();
//                    screamxoPlayer.play(url);
            }
        });
    }

    private void initView() {
       context = ChatMediaPlayer.this;
//        rl_Video = findViewById(R.id.rl_Video);
        if (getIntent().getExtras() != null) {
            url = getIntent().getStringExtra("videoUrl");
        }
    }

}
