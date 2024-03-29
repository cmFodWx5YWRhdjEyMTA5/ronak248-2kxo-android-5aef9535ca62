package com.sandrios.sandriosCamera.internal.ui.preview;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.Toast;

import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.configuration.Preferences;
import com.sandrios.sandriosCamera.internal.ui.BaseSandriosActivity;
import com.sandrios.sandriosCamera.internal.ui.view.AspectFrameLayout;
import com.sandrios.sandriosCamera.internal.utils.Utils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.view.UCropView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by Arpit Gandhi on 7/6/16.
 */
public class PreviewDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String FILE_PATH_ARG = "file_path_arg";
    public final static String FILE_CAPTION_ARG = "file_caption_arg";
    private static final String TAG = "PreviewActivity";
    private final static String SHOW_CROP = "show_crop";
    private final static String MEDIA_ACTION_ARG = "media_action_arg";
    private final static String RESPONSE_CODE_ARG = "response_code_arg";
    private final static String VIDEO_POSITION_ARG = "current_video_position";
    private final static String VIDEO_IS_PLAYED_ARG = "is_played";
    private final static String MIME_TYPE_VIDEO = "video";
    private final static String MIME_TYPE_IMAGE = "image";
    private final static String IS_GALLARY_IMAGE = "IS_GALLARY_IMAGE";
    int duration = 0;
    private int mediaAction;
    private String previewFilePath;
    private PreviewDashboardActivity mContext;
    private SurfaceView surfaceView;
    private FrameLayout photoPreviewContainer;
    private UCropView imagePreview;
    private ViewGroup buttonPanel;
    private AspectFrameLayout videoPreviewContainer;
    private EditText captionView;
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
    private int currentPlaybackPosition = 0;
    private boolean isVideoPlaying = true;
    private boolean showCrop = false;
    private boolean isGallaryImage;
    private Preferences preferences;
    private MediaController.MediaPlayerControl MediaPlayerControlImpl = new MediaController.MediaPlayerControl() {
        @Override
        public void start() {
            mediaPlayer.start();
        }

        @Override
        public void pause() {
            mediaPlayer.pause();
        }

        @Override
        public int getDuration() {
            return mediaPlayer.getDuration();
        }

        @Override
        public int getCurrentPosition() {
            return mediaPlayer.getCurrentPosition();
        }

        @Override
        public void seekTo(int pos) {
            mediaPlayer.seekTo(pos);
        }

        @Override
        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        @Override
        public int getBufferPercentage() {
            return 0;
        }

        @Override
        public boolean canPause() {
            return true;
        }

        @Override
        public boolean canSeekBackward() {
            return true;
        }

        @Override
        public boolean canSeekForward() {
            return true;
        }

        @Override
        public int getAudioSessionId() {
            return mediaPlayer.getAudioSessionId();
        }
    };
    private SurfaceHolder.Callback surfaceCallbacks = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            showVideoPreview(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };


    public static Intent newIntent(Context context,
                                   @CameraConfiguration.MediaAction int mediaAction,
                                   String filePath, boolean showImageCrop, boolean isGallaryImage) {

        return new Intent(context, PreviewDashboardActivity.class)
                .putExtra(MEDIA_ACTION_ARG, mediaAction)
                .putExtra(SHOW_CROP, showImageCrop)
                .putExtra(FILE_PATH_ARG, filePath)
                .putExtra(IS_GALLARY_IMAGE, isGallaryImage);
    }

    public static boolean isResultConfirm(@NonNull Intent resultIntent) {
        return BaseSandriosActivity.ACTION_CONFIRM == resultIntent.getIntExtra(RESPONSE_CODE_ARG, -1);
    }

    public static String getMediaFilePatch(@NonNull Intent resultIntent) {
        return resultIntent.getStringExtra(FILE_PATH_ARG);
    }

    public static String getMediaFileCaption(@NonNull Intent resultIntent) {
        return resultIntent.getStringExtra(FILE_CAPTION_ARG);
    }

    public static boolean isResultRetake(@NonNull Intent resultIntent) {
        return BaseSandriosActivity.ACTION_RETAKE == resultIntent.getIntExtra(RESPONSE_CODE_ARG, -1);
    }

    public static boolean isResultCancel(@NonNull Intent resultIntent) {
        return BaseSandriosActivity.ACTION_CANCEL == resultIntent.getIntExtra(RESPONSE_CODE_ARG, -1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_preview_dashboard);
        mContext = this;
        preferences = new Preferences(this);
        surfaceView = (SurfaceView) findViewById(R.id.video_preview);
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mediaController == null) {
                    return false;
                }
                if (mediaController.isShowing()) {
                    mediaController.hide();
                    showButtonPanel(true);
                } else {
                    showButtonPanel(false);
                    mediaController.show();
                }
                return false;
            }
        });

        videoPreviewContainer = (AspectFrameLayout) findViewById(R.id.previewAspectFrameLayout);
        photoPreviewContainer = (FrameLayout) findViewById(R.id.photo_preview_container);
        imagePreview = (UCropView) findViewById(R.id.image_view);
        buttonPanel = (ViewGroup) findViewById(R.id.preview_control_panel);
        captionView = (EditText) findViewById(R.id.et_Captionview);
        View confirmMediaResult = findViewById(R.id.confirm_media_result);
        View reTakeMedia = findViewById(R.id.re_take_media);
        View cancelMediaAction = findViewById(R.id.cancel_media_action);

        findViewById(R.id.crop_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UCrop.Options options = new UCrop.Options();
                options.setToolbarColor(ContextCompat.getColor(mContext, android.R.color.black));
                options.setStatusBarColor(ContextCompat.getColor(mContext, android.R.color.black));
                Uri uri = Uri.fromFile(new File(previewFilePath));
                UCrop.of(uri, uri)
                        .withOptions(options)
                        .start(mContext);
            }
        });

        if (confirmMediaResult != null) {
            confirmMediaResult.setOnClickListener(this);
        }

        if (reTakeMedia != null) {
            reTakeMedia.setOnClickListener(this);
        }

        if (cancelMediaAction != null) {
            cancelMediaAction.setOnClickListener(this);
        }

        Bundle args = getIntent().getExtras();

        mediaAction = args.getInt(MEDIA_ACTION_ARG);
        previewFilePath = args.getString(FILE_PATH_ARG);
        showCrop = args.getBoolean(SHOW_CROP);
        isGallaryImage = args.getBoolean(IS_GALLARY_IMAGE);

        Log.d(TAG, "onCreate: " + mediaAction);
        Log.d(TAG, "onCreate: " + previewFilePath);
        Log.d(TAG, "onCreate: " + showCrop);
        Log.d(TAG, "onCreate: " + isGallaryImage);

        if (mediaAction == CameraConfiguration.MEDIA_ACTION_VIDEO) {
            displayVideo(savedInstanceState);
        } else if (mediaAction == CameraConfiguration.MEDIA_ACTION_PHOTO) {
            displayImage();
        } else {
            String mimeType = Utils.getMimeType(previewFilePath);
            if (mimeType.contains(MIME_TYPE_VIDEO)) {
                displayVideo(savedInstanceState);
            } else if (mimeType.contains(MIME_TYPE_IMAGE)) {
                displayImage();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveVideoParams(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            showImagePreview();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaController != null) {
            mediaController.hide();
            mediaController = null;
        }
    }

    private void displayImage() {
        if (showCrop) {
            findViewById(R.id.crop_image).setVisibility(View.GONE);
        } else {
            findViewById(R.id.crop_image).setVisibility(View.GONE);
        }

        videoPreviewContainer.setVisibility(View.GONE);
        surfaceView.setVisibility(View.GONE);
        showImagePreview();
    }

    private void showImagePreview() {
        try {
            Uri uri = Uri.fromFile(new File(previewFilePath));
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);

            imagePreview.getCropImageView().setImageUri(uri, null);
            imagePreview.getOverlayView().setShowCropFrame(false);
            imagePreview.getOverlayView().setShowCropGrid(false);
            imagePreview.getCropImageView().setRotateEnabled(false);
            imagePreview.getOverlayView().setDimmedColor(Color.TRANSPARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayVideo(Bundle savedInstanceState) {
        findViewById(R.id.crop_image).setVisibility(View.GONE);
        if (savedInstanceState != null) {
            loadVideoParams(savedInstanceState);
        }
        photoPreviewContainer.setVisibility(View.GONE);
        surfaceView.getHolder().addCallback(surfaceCallbacks);
    }

    private void showVideoPreview(SurfaceHolder holder) {
        try {
            Log.e("previewFilePath", new File(previewFilePath).exists() + " Exits");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(previewFilePath);
            mediaPlayer.setDisplay(holder);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaController = new MediaController(mContext);
                    mediaController.setAnchorView(surfaceView);
                    mediaController.setMediaPlayer(MediaPlayerControlImpl);

                    int videoWidth = mp.getVideoWidth();
                    int videoHeight = mp.getVideoHeight();

                    videoPreviewContainer.setAspectRatio((double) videoWidth / videoHeight);
                    duration = mediaPlayer.getDuration() / 1000;
                    mediaPlayer.start();
                    mediaPlayer.seekTo(currentPlaybackPosition);

                    if (!isVideoPlaying) {
                        mediaPlayer.pause();
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    finish();
                    return true;
                }
            });
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.e(TAG, "Error media player playing video.");
            e.printStackTrace();
            finish();
        }
    }

    private void saveCroppedImage(Uri croppedFileUri) {
        try {
            File saveFile = new File(previewFilePath);
            FileInputStream inStream = new FileInputStream(new File(croppedFileUri.getPath()));
            FileOutputStream outStream = new FileOutputStream(saveFile);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveVideoParams(Bundle outState) {
        if (mediaPlayer != null) {
            outState.putInt(VIDEO_POSITION_ARG, mediaPlayer.getCurrentPosition());
            outState.putBoolean(VIDEO_IS_PLAYED_ARG, mediaPlayer.isPlaying());
        }
    }

    private void loadVideoParams(Bundle savedInstanceState) {
        currentPlaybackPosition = savedInstanceState.getInt(VIDEO_POSITION_ARG, 0);
        isVideoPlaying = savedInstanceState.getBoolean(VIDEO_IS_PLAYED_ARG, true);
    }

    private void showButtonPanel(boolean show) {
        if (show) {
            buttonPanel.setVisibility(View.VISIBLE);
        } else {
            buttonPanel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        Intent resultIntent = new Intent();
        if (view.getId() == R.id.confirm_media_result) {
            if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {

                if (duration < 3 && mediaPlayer!=null) {
                    Toast.makeText(this, "Video must be of above 3 second duration", Toast.LENGTH_SHORT).show();

                } else if (!TextUtils.isEmpty(captionView.getText().toString())) {
                    resultIntent.putExtra(RESPONSE_CODE_ARG, BaseSandriosActivity.ACTION_CONFIRM);
                    resultIntent.putExtra(FILE_PATH_ARG, previewFilePath);
                    resultIntent.putExtra(FILE_CAPTION_ARG, captionView.getText().toString());
                } else {
                    Toast.makeText(mContext, "Please enter post title.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please login to use this functionality", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.re_take_media) {
            if (!isGallaryImage) {
                deleteMediaFile();
            }
            resultIntent.putExtra(RESPONSE_CODE_ARG, BaseSandriosActivity.ACTION_RETAKE);
        } else if (view.getId() == R.id.cancel_media_action) {
            if (!isGallaryImage) {
                deleteMediaFile();
            }
            resultIntent.putExtra(RESPONSE_CODE_ARG, BaseSandriosActivity.ACTION_CANCEL);
        }
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!isGallaryImage) {
            deleteMediaFile();
        }
    }

    private boolean deleteMediaFile() {
        File mediaFile = new File(previewFilePath);
        return mediaFile.delete();
    }
}
