package com.screamxo.Service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

import com.screamxo.Utils.Preferences;
import com.screamxo.inlineplayer.FloatingViewListener;
import com.screamxo.inlineplayer.FloatingViewManager;

public class InlinePlayerService extends Service implements FloatingViewListener {

    String videoPath;
    private FloatingViewManager mFloatingViewManager;

    /*public InlinePlayerService(String name) {
        super(name);
    }*/

    /*public InlinePlayerService() {
        super(InlinePlayerService.class.getSimpleName());
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mFloatingViewManager != null) {
            return START_STICKY;
        }
        Preferences preferences = null;

        try {
            preferences = new Preferences(this);
            stopSelf(preferences.getServiceId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            preferences.setServiceId(startId);
            if (intent != null && intent.getExtras().containsKey("videoUrl")) {
                videoPath = intent.getExtras().getString("videoUrl");
            }

//            final DisplayMetrics metrics = new DisplayMetrics();
//            final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//            windowManager.getDefaultDisplay().getMetrics(metrics);
//            final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
//            final VideoView iconView = (VideoView) inflater.inflate(R.layout.widget_inline_player, null, false);
//
//            mFloatingViewManager = new FloatingViewManager(getApplicationContext(), this);
//            final FloatingViewManager.Options options = new FloatingViewManager.Options();
//            options.floatingViewX = 20;
//            options.floatingViewY = 20;
//            options.overMargin = (int) (16 * metrics.density);
//            mFloatingViewManager.addViewToWindow(iconView, options);
//
//            if (videoPath != null) {
//                iconView.setVideoPath(videoPath);
//                iconView.requestFocus();
//                iconView.start();
//
//                iconView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        destroy();
//                        stopSelf();
//                    }
//                });
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        destroy();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*@Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null && intent.getExtras().containsKey("videoUrl")) {
            videoPath = intent.getExtras().getString("videoUrl");
        }

        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final VideoView iconView = (VideoView) inflater.inflate(R.layout.widget_inline_player, null, false);

        mFloatingViewManager = new FloatingViewManager(this, this);
        final FloatingViewManager.Options options = new FloatingViewManager.Options();
        options.floatingViewX = 20;
        options.floatingViewY = 20;
        options.overMargin = (int) (16 * metrics.density);
        mFloatingViewManager.addViewToWindow(iconView, options);

        if (videoPath != null) {
            iconView.setVideoPath(videoPath);
            iconView.requestFocus();
            iconView.start();
            iconView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
        }
    }*/


    @Override
    public boolean stopService(Intent name) {
        destroy();
        stopSelf();
        return super.stopService(name);
    }

    private void destroy() {
        try {
            if (mFloatingViewManager != null) {
                mFloatingViewManager.removeAllViewToWindow();
                mFloatingViewManager = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinishFloatingView() {
        destroy();
        stopSelf();
    }

    @Override
    public void onTouchFinished(boolean isFinishing, int x, int y) {
        destroy();
        stopSelf();
    }
}

