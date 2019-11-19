package com.sandrios.sandriosCamera.internal.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.ui.model.PhotoQualityOption;
import com.sandrios.sandriosCamera.internal.ui.model.VideoQualityOption;
import com.sandrios.sandriosCamera.internal.ui.preview.PreviewActivity;
import com.sandrios.sandriosCamera.internal.ui.preview.PreviewDashboardActivity;
import com.sandrios.sandriosCamera.internal.ui.view.CameraControlPanel;
import com.sandrios.sandriosCamera.internal.ui.view.CameraSwitchView;
import com.sandrios.sandriosCamera.internal.ui.view.FlashSwitchView;
import com.sandrios.sandriosCamera.internal.ui.view.FrontFlashView;
import com.sandrios.sandriosCamera.internal.ui.view.ImageGalleryAdapter;
import com.sandrios.sandriosCamera.internal.ui.view.RecordButton;
import com.sandrios.sandriosCamera.internal.utils.CameraHelper;
import com.sandrios.sandriosCamera.internal.utils.Size;

import java.io.File;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.sandrios.sandriosCamera.internal.ui.preview.PreviewDashboardActivity.FILE_CAPTION_ARG;
import static com.sandrios.sandriosCamera.internal.ui.preview.PreviewDashboardActivity.FILE_PATH_ARG;

/**
 * Created by Arpit Gandhi on 12/1/16.
 */

public abstract class BaseSandriosActivity<CameraId> extends SandriosCameraActivity<CameraId>
        implements
        RecordButton.RecordButtonListener,
        FlashSwitchView.FlashModeSwitchListener,
        CameraSwitchView.OnCameraTypeChangeListener, ImageGalleryAdapter.OnItemClickListener {

    public static final int ACTION_CONFIRM = 900;
    public static final int ACTION_RETAKE = 901;
    public static final int ACTION_CANCEL = 902;
    protected static final int REQUEST_PREVIEW_CODE = 1001;
    private static final String TAG = "BaseSandriosActivity";
    protected int requestCode = -1;
    @CameraConfiguration.MediaAction
    protected int mediaAction = CameraConfiguration.MEDIA_ACTION_BOTH;

//    @CameraConfiguration.MediaQuality
//    protected int mediaQuality = CameraConfiguration.MEDIA_QUALITY_HIGHEST;

    @CameraConfiguration.MediaQuality
    protected int mediaQuality = CameraConfiguration.MEDIA_QUALITY_LOW;

//    @CameraConfiguration.MediaQuality
//    protected int passedMediaQuality = CameraConfiguration.MEDIA_QUALITY_HIGHEST;

    @CameraConfiguration.MediaQuality
    protected int passedMediaQuality = CameraConfiguration.MEDIA_QUALITY_LOW;


    protected CharSequence[] videoQualities;
    protected CharSequence[] photoQualities;
    protected boolean enableImageCrop = false;
    protected int videoDuration = -1;
    protected long videoFileSize = -1;
    protected int minimumVideoDuration = -1;
    protected boolean showPicker = true;
    @CameraControlPanel.MediaActionState
    protected int currentMediaActionState;
    @CameraSwitchView.CameraType
    protected int currentCameraType = CameraSwitchView.CAMERA_TYPE_REAR;
    @CameraConfiguration.MediaQuality
    protected int newQuality = -1;
    @CameraConfiguration.FlashMode
    protected int flashMode = CameraConfiguration.FLASH_MODE_AUTO;
    FrontFlashView flashFrame;
    private CameraControlPanel cameraControlPanel;
    private AlertDialog settingsDialog;

    @Override
    protected void onProcessBundle(Bundle savedInstanceState) {
        super.onProcessBundle(savedInstanceState);

        extractConfiguration(getIntent().getExtras());
        currentMediaActionState = mediaAction == CameraConfiguration.MEDIA_ACTION_VIDEO ?
                CameraControlPanel.ACTION_VIDEO : CameraControlPanel.ACTION_PHOTO;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraControlPanel.imageVideoBackgroudOperation.cancel(true);
        Log.d(TAG, "PROCESS_CANCLED");
    }

    @Override
    protected void onCameraControllerReady() {
        super.onCameraControllerReady();

        videoQualities = getVideoQualityOptions();
        photoQualities = getPhotoQualityOptions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraControlPanel.lockControls();
        cameraControlPanel.allowRecord(false);
        cameraControlPanel.showPicker(showPicker);
    }

    @Override
    protected void onPause() {
        super.onPause();

        cameraControlPanel.lockControls();
        cameraControlPanel.allowRecord(false);
    }

    private void extractConfiguration(Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(CameraConfiguration.Arguments.REQUEST_CODE)) {
                requestCode = bundle.getInt(CameraConfiguration.Arguments.REQUEST_CODE);
            }

            if (bundle.containsKey(CameraConfiguration.Arguments.MEDIA_ACTION)) {
                switch (bundle.getInt(CameraConfiguration.Arguments.MEDIA_ACTION)) {
                    case CameraConfiguration.MEDIA_ACTION_PHOTO:
                        mediaAction = CameraConfiguration.MEDIA_ACTION_PHOTO;
                        break;
                    case CameraConfiguration.MEDIA_ACTION_VIDEO:
                        mediaAction = CameraConfiguration.MEDIA_ACTION_VIDEO;
                        break;
                    default:
                        mediaAction = CameraConfiguration.MEDIA_ACTION_BOTH;
                        break;
                }
            }

            if (bundle.containsKey(CameraConfiguration.Arguments.MEDIA_QUALITY)) {
                switch (bundle.getInt(CameraConfiguration.Arguments.MEDIA_QUALITY)) {
                    case CameraConfiguration.MEDIA_QUALITY_AUTO:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_AUTO;
                        break;
                    case CameraConfiguration.MEDIA_QUALITY_HIGHEST:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_HIGHEST;
                        break;
                    case CameraConfiguration.MEDIA_QUALITY_HIGH:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_HIGH;
                        break;
                    case CameraConfiguration.MEDIA_QUALITY_MEDIUM:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_MEDIUM;
                        break;
                    case CameraConfiguration.MEDIA_QUALITY_LOW:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_LOW;
                        break;
                    case CameraConfiguration.MEDIA_QUALITY_LOWEST:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_LOWEST;
                        break;
                    default:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_MEDIUM;
                        break;
                }
                passedMediaQuality = mediaQuality;
            }

            if (bundle.containsKey(CameraConfiguration.Arguments.VIDEO_DURATION)) {
                videoDuration = bundle.getInt(CameraConfiguration.Arguments.VIDEO_DURATION);
            }

            if (bundle.containsKey(CameraConfiguration.Arguments.VIDEO_FILE_SIZE)) {
                videoFileSize = bundle.getLong(CameraConfiguration.Arguments.VIDEO_FILE_SIZE);
            }

            if (bundle.containsKey(CameraConfiguration.Arguments.MINIMUM_VIDEO_DURATION)) {
                minimumVideoDuration = bundle.getInt(CameraConfiguration.Arguments.MINIMUM_VIDEO_DURATION);
            }

            if (bundle.containsKey(CameraConfiguration.Arguments.SHOW_PICKER)) {
                showPicker = bundle.getBoolean(CameraConfiguration.Arguments.SHOW_PICKER);
            }

            if (bundle.containsKey(CameraConfiguration.Arguments.ENABLE_CROP)) {
                enableImageCrop = bundle.getBoolean(CameraConfiguration.Arguments.ENABLE_CROP);
            }

            if (bundle.containsKey(CameraConfiguration.Arguments.FLASH_MODE)) {
                switch (bundle.getInt(CameraConfiguration.Arguments.FLASH_MODE)) {
                    case CameraConfiguration.FLASH_MODE_AUTO:
                        flashMode = CameraConfiguration.FLASH_MODE_AUTO;
                        break;
                    case CameraConfiguration.FLASH_MODE_ON:
                        flashMode = CameraConfiguration.FLASH_MODE_ON;
                        break;
                    case CameraConfiguration.FLASH_MODE_OFF:
                        flashMode = CameraConfiguration.FLASH_MODE_OFF;
                        break;
                    default:
                        flashMode = CameraConfiguration.FLASH_MODE_AUTO;
                        break;
                }
            }
        }
    }

//    @Override
//    public void onItemClick(Uri filePath) {
//        Intent resultIntent = new Intent();
//        resultIntent.putExtra(CameraConfiguration.Arguments.FILE_PATH,
//                filePath.toString());
//        setResult(RESULT_OK, resultIntent);
//        finish();
//    }

    @Override
    View getUserContentView(LayoutInflater layoutInflater, ViewGroup parent) {
        cameraControlPanel = (CameraControlPanel) layoutInflater.inflate(R.layout.user_control_layout, parent, false);
        if (cameraControlPanel != null) {
            cameraControlPanel.setup(getMediaAction());
            switch (flashMode) {
                case CameraConfiguration.FLASH_MODE_AUTO:
                    cameraControlPanel.setFlasMode(FlashSwitchView.FLASH_AUTO);
                    break;
                case CameraConfiguration.FLASH_MODE_ON:
                    cameraControlPanel.setFlasMode(FlashSwitchView.FLASH_ON);
                    break;
                case CameraConfiguration.FLASH_MODE_OFF:
                    cameraControlPanel.setFlasMode(FlashSwitchView.FLASH_OFF);
                    break;
            }
            cameraControlPanel.setRecordButtonListener(this);
            cameraControlPanel.setFlashModeSwitchListener(this);
            cameraControlPanel.setOnCameraTypeChangeListener(this);
            cameraControlPanel.setMaxVideoFileSize(getVideoFileSize());
            cameraControlPanel.shouldShowCrop(enableImageCrop);
            cameraControlPanel.setItemClickListener(this); // for image gallary adapter
        }
        return cameraControlPanel;
    }

    public int getCurrentCameraType() {
        return currentCameraType;
    }

    @Override
    public void onCameraTypeChanged(@CameraSwitchView.CameraType int cameraType) {
        if (currentCameraType == cameraType) {
            return;
        }
        currentCameraType = cameraType;
        cameraControlPanel.lockControls();
        cameraControlPanel.allowRecord(false);

        int cameraFace = cameraType == CameraSwitchView.CAMERA_TYPE_FRONT ? CameraConfiguration.CAMERA_FACE_FRONT : CameraConfiguration.CAMERA_FACE_REAR;

        getCameraController().switchCamera(cameraFace);

        if (cameraType == CameraSwitchView.CAMERA_TYPE_FRONT) {
            cameraControlPanel.setHasFlash(false);
        } else {
            cameraControlPanel.setHasFlash(true);
        }
    }

    @Override
    public void onFlashModeChanged(@FlashSwitchView.FlashMode int mode) {
        switch (mode) {
            case FlashSwitchView.FLASH_AUTO:
                flashMode = CameraConfiguration.FLASH_MODE_AUTO;
                getCameraController().setFlashMode(CameraConfiguration.FLASH_MODE_AUTO);
                break;
            case FlashSwitchView.FLASH_ON:
                flashMode = CameraConfiguration.FLASH_MODE_ON;
                getCameraController().setFlashMode(CameraConfiguration.FLASH_MODE_ON);
                break;
            case FlashSwitchView.FLASH_OFF:
                flashMode = CameraConfiguration.FLASH_MODE_OFF;
                getCameraController().setFlashMode(CameraConfiguration.FLASH_MODE_OFF);
                break;
        }
    }

    @Override
    public void onMediaActionChanged(int mediaActionState) {
        if (currentMediaActionState == mediaActionState) {
            return;
        }
        currentMediaActionState = mediaActionState;
    }

    @Override
    public void onTakePhotoButtonPressed() {
        getCameraController().takePhoto();
        if (currentCameraType == CameraSwitchView.CAMERA_TYPE_FRONT && !CameraHelper.hasFrontCameraFlash(this) && flashMode == CameraConfiguration.FLASH_MODE_ON) {
            if (flashFrame == null) {
                flashFrame = new FrontFlashView(this);
                ((ViewGroup) this.cameraControlPanel.getParent()).addView(flashFrame);
            }
            if (flashFrame.getVisibility() == View.GONE) {
                flashFrame.setVisibility(View.VISIBLE);
            }
            flashFrame.turnOnFlash();
            flashFrame.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (flashFrame != null) {
                        flashFrame.turnOffFlash();
                        flashFrame.setVisibility(View.GONE);
                    }

                }
            }, 500);
            // startPreviewActivity();
            //  Toast.makeText(this,"Hi dinal it gulep thayo...",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStartRecordingButtonPressed() {
        getCameraController().startVideoRecord();
    }

    @Override
    public void onStopRecordingButtonPressed() {
        getCameraController().stopVideoRecord();
    }

    @Override
    public int onCameraZoom(int zoom) {
        if (getCameraController().isVideoRecording()) {
            getCameraController().setCameraZoom(zoom);
            return zoom;
        } else {
            return zoom;
        }
    }

    @Override
    protected void onScreenRotation(int degrees) {
        cameraControlPanel.rotateControls(degrees);
        rotateSettingsDialog(degrees);
    }

    @Override
    public int getRequestCode() {
        return requestCode;
    }

    @Override
    public int getMediaAction() {
        return mediaAction;
    }

    @Override
    public void setMediaAction(int mediaAction) {
        this.mediaAction = mediaAction;
    }

    @Override
    public int getMediaQuality() {
        return mediaQuality;
    }

    @Override
    public int getVideoDuration() {
        return videoDuration;
    }

    @Override
    public long getVideoFileSize() {
        return videoFileSize;
    }

    @Override
    public int getFlashMode() {
        return flashMode;
    }

    @Override
    public int getMinimumVideoDuration() {
        return minimumVideoDuration / 1000;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void updateCameraPreview(Size size, View cameraPreview) {
        cameraControlPanel.unLockControls();
        cameraControlPanel.allowRecord(true);

        setCameraPreview(cameraPreview, size);
    }

    @Override
    public void updateUiForMediaAction(@CameraConfiguration.MediaAction int mediaAction) {

    }

    @Override
    public void updateCameraSwitcher(int numberOfCameras) {
        cameraControlPanel.allowCameraSwitching(numberOfCameras > 1);
    }

    @Override
    public void onPhotoTaken() {
        startPreviewActivity(false);
    }

    @Override
    public void onVideoRecordStart(int width, int height) {
        cameraControlPanel.onStartVideoRecord(getCameraController().getOutputFile());
    }

    @Override
    public void onVideoRecordStop() {
        cameraControlPanel.allowRecord(false);
        cameraControlPanel.onStopVideoRecord();
        startPreviewActivity(false);
    }

    @Override
    public void releaseCameraPreview() {
        clearCameraPreview();
    }

    private void startPreviewActivity(boolean isGallaryImage) {
        Intent intent = PreviewDashboardActivity.newIntent(this, getMediaAction(), getCameraController().getOutputFile().toString(), cameraControlPanel.showCrop(), isGallaryImage);
        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PREVIEW_CODE) {
                if (PreviewActivity.isResultConfirm(data)) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(FILE_PATH_ARG, data.getStringExtra(FILE_PATH_ARG));
                    resultIntent.putExtra(FILE_CAPTION_ARG, data.getStringExtra(FILE_CAPTION_ARG));
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else if (PreviewActivity.isResultCancel(data)) {
                    setResult(RESULT_CANCELED);
                    finish();
                } else if (PreviewActivity.isResultRetake(data)) {
                }
            }
        }
    }

    private void rotateSettingsDialog(int degrees) {
        if (settingsDialog != null && settingsDialog.isShowing() && Build.VERSION.SDK_INT > 10) {
            ViewGroup dialogView = (ViewGroup) settingsDialog.getWindow().getDecorView();
            for (int i = 0; i < dialogView.getChildCount(); i++) {
                dialogView.getChildAt(i).setRotation(degrees);
            }
        }
    }

    protected abstract CharSequence[] getVideoQualityOptions();

    protected abstract CharSequence[] getPhotoQualityOptions();

    protected int getVideoOptionCheckedIndex() {
        int checkedIndex = -1;
        if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_AUTO) {
            checkedIndex = 0;
        } else if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_HIGH) {
            checkedIndex = 1;
        } else if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_MEDIUM) {
            checkedIndex = 2;
        } else if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_LOW) {
            checkedIndex = 3;
        }

        if (passedMediaQuality != CameraConfiguration.MEDIA_QUALITY_AUTO) {
            checkedIndex--;
        }

        return checkedIndex;
    }

    protected int getPhotoOptionCheckedIndex() {
        int checkedIndex = -1;
        if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_HIGHEST) {
            checkedIndex = 0;
        } else if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_HIGH) {
            checkedIndex = 1;
        } else if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_MEDIUM) {
            checkedIndex = 2;
        } else if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_LOWEST) {
            checkedIndex = 3;
        }
        return checkedIndex;
    }

    protected DialogInterface.OnClickListener getVideoOptionSelectedListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                newQuality = ((VideoQualityOption) videoQualities[index]).getMediaQuality();
            }
        };
    }

    protected DialogInterface.OnClickListener getPhotoOptionSelectedListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                newQuality = ((PhotoQualityOption) photoQualities[index]).getMediaQuality();
            }
        };
    }

    public void onItemClick(View view, int position, ImageGalleryAdapter.PickerTile pickerTile) {
        Log.d(TAG, "onItemClick: ");
        if (pickerTile.getMediaType().equals(String.valueOf(MEDIA_TYPE_VIDEO))) {
            setMediaAction(CameraConfiguration.MEDIA_ACTION_VIDEO);
        } else {
            setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO);
        }

        if (pickerTile.getImageUri() == null) {
            Toast.makeText(this, R.string.error_cannot_preview, Toast.LENGTH_SHORT).show();
            return;
        }

        getCameraController().setOutputFile(new File(pickerTile.getImageUri().getPath()));
        startPreviewActivity(true);
    }


}
