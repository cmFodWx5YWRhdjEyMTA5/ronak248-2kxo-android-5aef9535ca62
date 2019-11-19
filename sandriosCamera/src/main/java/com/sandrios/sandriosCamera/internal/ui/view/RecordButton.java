package com.sandrios.sandriosCamera.internal.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaActionSound;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RecordButton extends ImageButton {

    public static final int TAKE_PHOTO_STATE = 0;
    public static final int READY_FOR_RECORD_STATE = 1;
    public static final int RECORD_IN_PROGRESS_STATE = 2;
    private Context context;
    private int mediaAction = CameraConfiguration.MEDIA_ACTION_BOTH;
    private
    @RecordState
    int currentState = TAKE_PHOTO_STATE;
    private Drawable takePhotoDrawable;
    private Drawable startRecordDrawable;
    private int iconPadding = 12;
    private RecordButtonListener listener;
    final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            listener.onMediaActionChanged(CameraConfiguration.MEDIA_ACTION_PHOTO);
            setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO);

            if (Build.VERSION.SDK_INT > 15) {
                MediaActionSound sound = new MediaActionSound();
                if (TAKE_PHOTO_STATE == currentState) {
                    takePhoto(sound);
                }
            } else if (TAKE_PHOTO_STATE == currentState) {
                takePhoto();
            }
            setIcon();
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            listener.onMediaActionChanged(CameraConfiguration.MEDIA_ACTION_VIDEO);
            //setMediaAction(CameraConfiguration.MEDIA_ACTION_VIDEO);

            if (Build.VERSION.SDK_INT > 15) {
                MediaActionSound sound = new MediaActionSound();
                if (READY_FOR_RECORD_STATE == currentState) {
                    startRecording(sound);
                }
            } else if (READY_FOR_RECORD_STATE == currentState) {
                startRecording();
            }
            setIcon();
        }
    });
    private int zoom = 0;
    private float mX, mY;
    private float newZoom, oldZoom = 1;

    public RecordButton(@NonNull Context context) {
        this(context, null, 0);
    }

    public RecordButton(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordButton(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        takePhotoDrawable = ContextCompat.getDrawable(context, R.drawable.take_photo_button);
        startRecordDrawable = ContextCompat.getDrawable(context, R.drawable.press_red);
    }

    public void setup(@CameraConfiguration.MediaAction int mediaAction, @NonNull RecordButtonListener listener) {
        setMediaAction(mediaAction);
        this.listener = listener;

        if (Build.VERSION.SDK_INT > 15) {
            setBackground(ContextCompat.getDrawable(context, R.drawable.circle_frame_background));
        } else {
            setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.circle_frame_background));
        }

        setIcon();
        setSoundEffectsEnabled(false);
        setIconPadding(iconPadding);
        setOnTouchListener(new RecordTouchListener());
    }

    private void setIconPadding(int paddingDP) {
        int padding = Utils.convertDipToPixels(context, paddingDP);
        setPadding(padding, padding, padding, padding);
    }

    public void setMediaAction(@CameraConfiguration.MediaAction int mediaAction) {
        this.mediaAction = mediaAction;
        if (CameraConfiguration.MEDIA_ACTION_PHOTO == mediaAction) {
            currentState = TAKE_PHOTO_STATE;
        } else {
            currentState = READY_FOR_RECORD_STATE;
        }
        setRecordState(currentState);
        setIcon();
    }

    @RecordState
    public int getRecordState() {
        return currentState;
    }

    public void setRecordState(@RecordState int state) {
        currentState = state;
        setIcon();
    }

    public void setRecordButtonListener(@NonNull RecordButtonListener listener) {
        this.listener = listener;
    }

    private void setIcon() {
        if (CameraConfiguration.MEDIA_ACTION_VIDEO == mediaAction) {
            if (READY_FOR_RECORD_STATE == currentState) {
//                CameraControlPanel.iv_record_duration_text.setVisibility(VISIBLE);
                setImageDrawable(startRecordDrawable);
                iconPadding = 0;
                setIconPadding(1);
            } else if (RECORD_IN_PROGRESS_STATE == currentState) {
                setImageDrawable(startRecordDrawable);
                setIconPadding(iconPadding);
            }
        } else {
//            CameraControlPanel.iv_record_duration_text.setVisibility(INVISIBLE);
            setImageDrawable(takePhotoDrawable);
            setIconPadding(iconPadding);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void takePhoto(MediaActionSound sound) {
        sound.play(MediaActionSound.SHUTTER_CLICK);
        takePhoto();
    }

    private void takePhoto() {
        if (listener != null) {
            listener.onTakePhotoButtonPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private int cameraZoom(MediaActionSound sound, int zoom) {
        sound.play(MediaActionSound.SHUTTER_CLICK);
        return cameraZoom(zoom);
    }

    private int cameraZoom(int zoom) {
        if (listener != null) {
            return listener.onCameraZoom(zoom);
        } else {
            return zoom;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startRecording(MediaActionSound sound) {
        sound.play(MediaActionSound.START_VIDEO_RECORDING);
        startRecording();
    }

    private void startRecording() {
        currentState = RECORD_IN_PROGRESS_STATE;
        if (listener != null) {
            listener.onStartRecordingButtonPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void stopRecording(MediaActionSound sound) {
        sound.play(MediaActionSound.STOP_VIDEO_RECORDING);
        stopRecording();
    }

    private void stopRecording() {
        currentState = READY_FOR_RECORD_STATE;
        if (listener != null) {
            listener.onStopRecordingButtonPressed();
        }
    }

    @IntDef({TAKE_PHOTO_STATE, READY_FOR_RECORD_STATE, RECORD_IN_PROGRESS_STATE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RecordState {
    }

    public interface RecordButtonListener {

        void onTakePhotoButtonPressed();

        void onStartRecordingButtonPressed();

        void onStopRecordingButtonPressed();

        int onCameraZoom(int zoom);

        void onMediaActionChanged(int currentMediaActionState);
    }

    private class RecordTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            final int action = event.getAction();
            final float x = event.getX();
            final float y = event.getY();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (Build.VERSION.SDK_INT > 15) {
                        MediaActionSound sound = new MediaActionSound();
                        if (RECORD_IN_PROGRESS_STATE == currentState) {
                            stopRecording(sound);
                        }
                    } else if (RECORD_IN_PROGRESS_STATE == currentState) {
                        stopRecording();
                    }
                    setIcon();
                    break;

                case MotionEvent.ACTION_DOWN:
                    mX = x;
                    mY = y;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (currentState == RECORD_IN_PROGRESS_STATE) {
                        final float dx = (x - mX) / v.getWidth();
                        final float dy = (y - mY) / v.getHeight();
                        newZoom = oldZoom * (float) Math.pow(20, -dy);
                        if (oldZoom < newZoom) {
                            zoom++;
                        } else {
                            zoom--;
                        }
                        cameraZoom(zoom);
                        oldZoom = newZoom;
                    }
                    mX = x;
                    mY = y;
                    break;
            }
            return true;
        }
    }
}
