package com.sandrios.sandriosCamera.internal.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.configuration.ConfigurationProvider;
import com.sandrios.sandriosCamera.internal.controller.CameraController;
import com.sandrios.sandriosCamera.internal.controller.view.CameraView;
import com.sandrios.sandriosCamera.internal.ui.view.AspectFrameLayout;
import com.sandrios.sandriosCamera.internal.utils.Size;
import com.sandrios.sandriosCamera.internal.utils.Utils;

abstract public class SandriosCameraFragment<CameraId> extends Fragment implements ConfigurationProvider, CameraView, SensorEventListener {

    protected AspectFrameLayout previewContainer;
    protected ViewGroup userContainer;
    @CameraConfiguration.SensorPosition
    protected int sensorPosition = CameraConfiguration.SENSOR_POSITION_UNSPECIFIED;
    @CameraConfiguration.DeviceDefaultOrientation
    protected int deviceDefaultOrientation;
    private SensorManager sensorManager = null;
    private CameraController<CameraId> cameraController;
    private int degrees = -1;

    private static final String TAG = "SandriosCameraFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraController = createCameraController(this, this);
        cameraController.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        int defaultOrientation = Utils.getDeviceDefaultOrientation(getContext());

        if (defaultOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            deviceDefaultOrientation = CameraConfiguration.ORIENTATION_LANDSCAPE;
        } else if (defaultOrientation == Configuration.ORIENTATION_PORTRAIT) {
            deviceDefaultOrientation = CameraConfiguration.ORIENTATION_PORTRAIT;
        }

        View decorView = getActivity().getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 15) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.generic_camera_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        previewContainer = (AspectFrameLayout) view.findViewById(R.id.previewContainer);
        userContainer = (ViewGroup) view.findViewById(R.id.userContainer);

        onProcessBundle(savedInstanceState);
        setUserContent();

        previewContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (event.getPointerCount() > 1 && action == MotionEvent.ACTION_MOVE) {
                    Log.e("Touch event", "Move");
                }
                return false;
            }
        });
    }

    protected void onProcessBundle(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        cameraController.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        cameraController.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraController.onDestroy();
    }

    public final CameraController<CameraId> getCameraController() {
        return cameraController;
    }

    public abstract CameraController<CameraId> createCameraController(CameraView cameraView, ConfigurationProvider configurationProvider);

    private void setUserContent() {
        userContainer.removeAllViews();
        userContainer.addView(getUserContentView(LayoutInflater.from(getContext()), userContainer));
    }

    public final void setCameraPreview(View preview, Size previewSize) {
        Log.i(TAG, "setCameraPreview isResumed(): " + isResumed());
        if (isResumed()) {
            onCameraControllerReady();
            if (previewContainer == null || preview == null) {
                return;
            }
            previewContainer.removeAllViews();

            previewContainer.addView(preview);
        }

        //        uncommented for camera aspect ratio issue By Amit on 16 jan 2019
//        previewContainer.setAspectRatio(previewSize.getHeight() / (double) previewSize.getWidth());
        previewContainer.setAspectRatio(previewSize.getHeight() / (double) previewSize.getWidth());
    }

    public final void clearCameraPreview() {
        if (previewContainer != null) {
            previewContainer.removeAllViews();
        }
    }

    abstract View getUserContentView(LayoutInflater layoutInflater, ViewGroup parent);

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                if (sensorEvent.values[0] < 4 && sensorEvent.values[0] > -4) {
                    if (sensorEvent.values[1] > 0) {
                        // UP
                        sensorPosition = CameraConfiguration.SENSOR_POSITION_UP;
                        degrees = deviceDefaultOrientation == CameraConfiguration.ORIENTATION_PORTRAIT ? 0 : 90;
                    } else if (sensorEvent.values[1] < 0) {
                        // UP SIDE DOWN
                        sensorPosition = CameraConfiguration.SENSOR_POSITION_UP_SIDE_DOWN;
                        degrees = deviceDefaultOrientation == CameraConfiguration.ORIENTATION_PORTRAIT ? 180 : 270;
                    }
                } else if (sensorEvent.values[1] < 4 && sensorEvent.values[1] > -4) {
                    if (sensorEvent.values[0] > 0) {
                        // LEFT
                        sensorPosition = CameraConfiguration.SENSOR_POSITION_LEFT;
                        degrees = deviceDefaultOrientation == CameraConfiguration.ORIENTATION_PORTRAIT ? 90 : 180;
                    } else if (sensorEvent.values[0] < 0) {
                        // RIGHT
                        sensorPosition = CameraConfiguration.SENSOR_POSITION_RIGHT;
                        degrees = deviceDefaultOrientation == CameraConfiguration.ORIENTATION_PORTRAIT ? 270 : 0;
                    }
                }
                onScreenRotation(degrees);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public final int getSensorPosition() {
        return sensorPosition;
    }

    @Override
    public final int getDegrees() {
        return degrees;
    }

    protected abstract void onScreenRotation(int degrees);

    protected void onCameraControllerReady() {
    }
}
