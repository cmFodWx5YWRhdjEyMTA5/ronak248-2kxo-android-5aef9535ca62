
package com.screamxo.Activity.RahulWork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.screamxo.Activity.CommonLoginSignUpActivity;
import com.screamxo.Activity.LoginActivity;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaPlayerActivity extends Activity {

    public static final String CONFIG = "mConfig";
    @SuppressWarnings("unused")
    private static final String TAG = "MediaPlayerActivity";
    public MediaPlayer mMediaPlayer;

    @BindView(R.id.app_video_finish)
    ImageView appVideoFinish;

    @BindView(R.id.img_share)
    ImageView img_share;

    @BindView(R.id.app_audio_seekBar)
    SeekBar appAudiosSeekbar;

    @BindView(R.id.app_video_play)
    ImageView appVideoPlay;

    @BindView(R.id.app_audio_endTime)
    TextView appAudioEndTime;

    @BindView(R.id.app_audio_currentTime)
    TextView appAudioCurrentTime;

//    @BindView(R.id.progreessbar)
//    ProgressBar progreessbar;

    @BindView(R.id.img_audio_background)
    ImageView imgAudioBg;

    private Config mConfig;
    private Handler mHandler = new Handler();
    private Preferences preferences;
    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            appAudiosSeekbar.setProgress(mMediaPlayer.getCurrentPosition());
            int cSeconds = (mMediaPlayer.getCurrentPosition() / 1000) % 60;
            int cMinutes = ((mMediaPlayer.getCurrentPosition() / (1000 * 60)) % 60);
            appAudioCurrentTime.setText(String.format(Locale.getDefault(), "%02d:%02d", cMinutes, cSeconds));
            if (mMediaPlayer.isPlaying()) {
                appAudiosSeekbar.postDelayed(onEverySecond, 50);
            }
        }
    };
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying() && appAudiosSeekbar != null) {
                long totalDuration = mMediaPlayer.getDuration();
                long currentDuration = mMediaPlayer.getCurrentPosition();

                appAudioEndTime.setText(Utils.milliSecondsToTimer(totalDuration));
                appAudioCurrentTime.setText(Utils.milliSecondsToTimer(currentDuration));
                appAudiosSeekbar.setProgress(mMediaPlayer.getCurrentPosition());
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    public static MediaPlayerActivity.Config configPlayer(Activity activity) {
        return new MediaPlayerActivity.Config(activity);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        preferences = new Preferences(this);
//        Log.d(TAG, "onCreate sMediaPlayerLastPosition: " + sMediaPlayerLastPosition);
        ButterKnife.bind(this);
        mConfig = getIntent().getParcelableExtra(CONFIG);
        appVideoFinish.setOnClickListener(view -> finish());
        img_share.setOnClickListener(view -> imgShare());
        setFullScreen();
    }

    private void updateSeekBarTimerTask() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private void setFullScreen() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try{
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
//            mConfig.setMediaPlayerCurrentPosition(DashboardAdapter.sMediaPlayerLastPosition());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
//            mMediaPlayer.seekTo(sMediaPlayerLastPosition);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        try{
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void imgShare() {
        Log.d(TAG, "imgShare: ");
        if (mConfig == null || TextUtils.isEmpty(mConfig.getUrl())) {
            Toast.makeText(this, R.string.toast_error_cannot_share_file, Toast.LENGTH_SHORT).show();
            return;
        }

        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
            Intent gotoLogin = new Intent(this, CommonLoginSignUpActivity.class);
            gotoLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(gotoLogin);
            finish();
        } else {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, (mConfig.getUrl()));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Send to"));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        if (mConfig == null || TextUtils.isEmpty(mConfig.getUrl())) {
            Toast.makeText(this, R.string.giraffe_player_url_empty, Toast.LENGTH_SHORT).show();
        } else {
            Picasso.with(this).load(mConfig.getAudioThumbnail()).placeholder(R.mipmap.headphone_placeholder).error(R.mipmap.headphone_placeholder).into(imgAudioBg);


            updateSeekBarTimerTask();
            mMediaPlayer = new MediaPlayer();
//            progreessbar.setVisibility(View.VISIBLE);


            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(mConfig.getUrl());
                mMediaPlayer.prepareAsync();
                appAudioCurrentTime.setText(getString(R.string.txt_loading));
            } catch (IllegalArgumentException | IOException e) {
                Toast.makeText(this,getString(R.string.msg_cant_play_music), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


            mMediaPlayer.setOnPreparedListener(mp -> {
//                progreessbar.setVisibility(View.GONE);
                mMediaPlayer.start();
//                Log.d(TAG, "onStart sMediaPlayerLastPosition: " + sMediaPlayerLastPosition);
//                mMediaPlayer.seekTo(sMediaPlayerLastPosition);

                long totalDuration = mMediaPlayer.getDuration();
                long currentDuration = mMediaPlayer.getCurrentPosition();

                appAudioEndTime.setText(Utils.milliSecondsToTimer(totalDuration));
                appAudioCurrentTime.setText(Utils.milliSecondsToTimer(currentDuration));

                appVideoPlay.setEnabled(true);
                appVideoPlay.setImageResource(R.drawable.ic_stop_white_24dp);

                appAudiosSeekbar.setMax(mp.getDuration());
            });


            appVideoPlay.setEnabled(false);
            appVideoPlay.setOnClickListener(v -> {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
//                    sMediaPlayerLastPosition = mMediaPlayer.getCurrentPosition();
                    appVideoPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                } else {
                    mMediaPlayer.start();
//                    mMediaPlayer.seekTo(sMediaPlayerLastPosition);
                    appVideoPlay.setImageResource(R.drawable.ic_stop_white_24dp);
                }
            });

            appAudiosSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.seekTo(progress);
                    }
                }
            });

            mMediaPlayer.setOnCompletionListener(mp -> {
//             finish();
            });
        }
    }

    @Override
    public void finish() {
        if (mMediaPlayer != null) {
//            sMediaPlayerLastPosition = mMediaPlayer.getCurrentPosition();
        }
        super.finish();
    }

    @SuppressWarnings("WeakerAccess")
    public static class Config implements Parcelable {

        public static final Parcelable.Creator<Config> CREATOR = new Parcelable.Creator<Config>() {
            public Config createFromParcel(Parcel in) {
                return new Config(in);
            }

            public Config[] newArray(int size) {
                return new Config[size];
            }
        };
        private Activity activity;
        private String url;
        //        private int mMediaPlayerCurrentPosition;
        private String mAudioThumbnail;

        Config(Activity activity) {
            this.activity = activity;
        }

        private Config(Parcel in) {
            url = in.readString();
        }

        public String getUrl() {
            return url;
        }

        public Config setUrl(String url) {
            this.url = url;
            return this;
        }

//        @SuppressWarnings("WeakerAccess")
//        public int getMediaPlayerCurrentPosition() {
//            return mMediaPlayerCurrentPosition;
//        }

//        public Config setMediaPlayerCurrentPosition(int mediaPlayerCurrentPosition) {
//            mMediaPlayerCurrentPosition = mediaPlayerCurrentPosition;
//            return this;
//        }

        public String getAudioThumbnail() {
            return mAudioThumbnail;
        }

        public Config setAudioThumbnail(String audioThumbnail) {
            mAudioThumbnail = audioThumbnail;
            return this;
        }

        public void play() {
            Intent intent = new Intent(activity, MediaPlayerActivity.class);
            intent.putExtra(CONFIG, this);
            activity.startActivity(intent);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(url);
        }
    }

}
