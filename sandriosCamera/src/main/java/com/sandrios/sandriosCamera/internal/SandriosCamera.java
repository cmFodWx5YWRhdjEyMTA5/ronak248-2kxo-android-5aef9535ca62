package com.sandrios.sandriosCamera.internal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.ui.camera.Camera1Activity;
import com.sandrios.sandriosCamera.internal.ui.camera.Camera1Fragment;
import com.sandrios.sandriosCamera.internal.ui.camera2.Camera2Activity;
import com.sandrios.sandriosCamera.internal.ui.camera2.Camera2Fragment;
import com.sandrios.sandriosCamera.internal.utils.CameraHelper;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * Sandrios Camera Builder Class
 * Created by Arpit Gandhi on 7/6/16.
 */
public class SandriosCamera {
    private static final String TAG = "SandriosCamera";
    public static SandriosCameraListener listener;
    public int requestCode;
    private SandriosCamera mInstance = null;
    private Activity mActivity;
    private int mediaAction = CameraConfiguration.MEDIA_ACTION_BOTH;
    private boolean showPicker = true;
    private boolean showPreviewlayout = false;
    private boolean enableImageCrop = false;
    private long videoSize = -1;
    public boolean sliding = true;

    /***
     * Creates SandriosCamera instance with default configuration set to both.
     *
     * @param activity - fromList which request was invoked
     * @param code     - request code which will return in onActivityForResult
     */
    public SandriosCamera(Activity activity, @IntRange(from = 0) int code) {
        mInstance = this;
        mActivity = activity;
        requestCode = code;
    }

    public SandriosCamera setShowPicker(boolean showPicker) {
        this.showPicker = showPicker;
        return mInstance;
    }

    public SandriosCamera setShowPreviewlayout(boolean showPreviewlayout) {
        this.showPreviewlayout = showPreviewlayout;
        return mInstance;
    }

    public SandriosCamera setMediaAction(int mediaAction) {
        this.mediaAction = mediaAction;
        return mInstance;
    }

    public SandriosCamera enableImageCropping(boolean enableImageCrop) {
        this.enableImageCrop = enableImageCrop;
        return mInstance;
    }

    public SandriosCamera setVideoFileSize(int fileSize) {
        this.videoSize = fileSize;
        return mInstance;
    }

    public void launchCamera() {
        RxPermissions.getInstance(mActivity)
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            launchIntent();
                        }
                    }
                });
    }

    public void launchGallery() {
        RxPermissions.getInstance(mActivity)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            launchIntent();
                        }
                    }
                });
    }

    public void dispatchPhotoSelectionIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mActivity.startActivityForResult(galleryIntent, CameraConfiguration.REQUEST_IMAGE_SELECTOR);
    }

    public Fragment launchCamera(SandriosCameraListener listener) {
        SandriosCamera.listener = listener;
        return launchFragment();
    }

    private void launchIntent() {
        Log.d(TAG, "launchIntent: ");
        if (CameraHelper.hasCamera(mActivity)) {
            Intent cameraIntent;
            if (CameraHelper.hasCamera2(mActivity)) {
                cameraIntent = new Intent(mActivity, Camera2Activity.class);
            } else {
                cameraIntent = new Intent(mActivity, Camera1Activity.class);
            }
            cameraIntent.putExtra(CameraConfiguration.Arguments.REQUEST_CODE, requestCode);
            cameraIntent.putExtra(CameraConfiguration.Arguments.SHOW_PICKER, showPicker);
            cameraIntent.putExtra(CameraConfiguration.Arguments.MEDIA_ACTION, mediaAction);
            cameraIntent.putExtra(CameraConfiguration.Arguments.ENABLE_CROP, enableImageCrop);
            if (videoSize > 0) {
                cameraIntent.putExtra(CameraConfiguration.Arguments.VIDEO_FILE_SIZE, videoSize * 1024 * 1024);
            }
            mActivity.startActivityForResult(cameraIntent, requestCode);
        }
    }

    private Fragment launchFragment() {
        if (CameraHelper.hasCamera(mActivity)) {
            Fragment cameraFragment;
            Log.i(TAG, "launchFragment CameraHelper.hasCamera2(mActivity): " + CameraHelper.hasCamera2(mActivity));
            if (CameraHelper.hasCamera2(mActivity)) {
                cameraFragment = new Camera2Fragment();
            } else {
                cameraFragment = new Camera1Fragment();
            }
            Bundle bundle = new Bundle();
            bundle.putInt(CameraConfiguration.Arguments.REQUEST_CODE, requestCode);
            bundle.putBoolean(CameraConfiguration.Arguments.SHOW_PICKER, showPicker);
            bundle.putInt(CameraConfiguration.Arguments.MEDIA_ACTION, mediaAction);
            bundle.putBoolean(CameraConfiguration.Arguments.ENABLE_CROP, enableImageCrop);
            if (videoSize > 0) {
                bundle.putLong(CameraConfiguration.Arguments.VIDEO_FILE_SIZE, videoSize * 1024 * 1024);
            }
            cameraFragment.setArguments(bundle);
            return cameraFragment;
        } else {
            return null;
        }
    }

    public SandriosCamera setSliding(boolean sliding) {
        this.sliding = sliding;
        if (sliding == true) {
            setShowPicker(true);
        }else {
            setShowPicker(false);
        }
        return mInstance;
    }
}
