package com.sandrios.sandriosCamera.internal.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.sandrios.sandriosCamera.R;

/**
 * Created by dinal.koyani on 2/2/2018.
 */

public class FrontFlashView extends FrameLayout {

    private Context context;
    private Camera camera;
    private boolean isFlashOn;
    private FrameLayout flashEmulator;
    private float lastScreenBrightness;
    private CameraPreview cameraPreview;
    int fullScreen = WindowManager.LayoutParams.MATCH_PARENT;

    public FrontFlashView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
       // startCamera();
    }

    public FrontFlashView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        //startCamera();
    }

    private void init(){
        //((Activity)getContext()).getWindow().setFlags(fullScreen,fullScreen);
        addView(LayoutInflater.from(context).inflate(R.layout.front_flash_framlayout,null));
        flashEmulator = (FrameLayout) findViewById(R.id.flashEmulator);
      //  cameraPreview = (CameraPreview)findViewById(R.id.cameraPreview);
    }

    private void startCamera() {
        if (camera == null) {
            try {
                camera = Camera.open(1);
                Camera.Parameters params =  camera.getParameters();

                params.setPreviewSize(1280, 720);
                camera.setDisplayOrientation(90);
                camera.setParameters(params);
                cameraPreview.connectCamera(camera);
                cameraPreview.startPreview();
            } catch (Exception e) {
            }
        }
    }

    public void turnOnFlash() {

            int color = Color.WHITE;
            float[] hsv = new float[3];
            final Window window = ((Activity)getContext()).getWindow();
            final WindowManager.LayoutParams params = window.getAttributes();

            lastScreenBrightness = params.screenBrightness;
            params.screenBrightness = 1F;
            window.setAttributes(params);
            flashEmulator.setVisibility(View.VISIBLE);
            Color.colorToHSV(color, hsv);
            hsv[2] *= 1f;
            color = Color.HSVToColor(hsv);
            flashEmulator.setBackgroundColor(color);

    }

    public void turnOffFlash() {

            Window window = ((Activity)getContext()).getWindow();
            WindowManager.LayoutParams params = window.getAttributes();

            params.screenBrightness = lastScreenBrightness;
            window.setAttributes(params);
            flashEmulator.setVisibility(View.GONE);


    }

}
