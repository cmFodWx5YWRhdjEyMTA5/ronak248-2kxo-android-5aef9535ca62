package com.sandrios.sandriosCamera.internal.ui.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by sagi on 2/8/2017.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private final String LOG_TAG = "CameraPreview";

    public CameraPreview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CameraPreview(Context context) {
        super(context);
    }

    public void connectCamera(Camera camera) {
        this.camera = camera;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        startPreview();
    }

    public void releaseCamera() {
        if (camera != null) {
            stopPreview();
            camera = null;
        }
    }

    void startPreview() {
        if (camera != null && surfaceHolder.getSurface() != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (Exception e) {
                Log.e(LOG_TAG, String.format("Error setting preview display: %s", e.getMessage()));
            }
        }
    }

    void stopPreview() {
        if (camera != null) {
            try {
                camera.stopPreview();
            } catch (Exception e) {
                Log.e(LOG_TAG, String.format("Error stopping preview: %s", e.getMessage()));
            }
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startPreview();
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        stopPreview();
        startPreview();
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        stopPreview();
    }
}