package com.sandrios.sandriosCamera.internal.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class CameraControlPanel extends RelativeLayout implements RecordButton.RecordButtonListener {
    public final static int ACTION_PHOTO = 0;
    public final static int ACTION_VIDEO = 1;
    private static final String TAG = "CameraControlPanel";
    private Context context;
    private CameraSwitchView cameraSwitchView;
    private RecordButton recordButton;
    private FlashSwitchView flashSwitchView;
    private RelativeLayout record_panel;
    private RecyclerView recyclerView;
    private RecyclerView gallery_rv;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout app_bar;
    private ImageView recyclerViewHandlerButton;
    private ImageGalleryAdapter imageHorizontalGalleryAdapter;
    private RecordButton.RecordButtonListener recordButtonListener;
    private CameraSwitchView.OnCameraTypeChangeListener onCameraTypeChangeListener;
    private FlashSwitchView.FlashModeSwitchListener flashModeSwitchListener;
    private long maxVideoFileSize = 0;
    private String mediaFilePath;
    private boolean hasFlash = false;
    public ImageVideoBackgroudOperation imageVideoBackgroudOperation;
    ArrayList<ImageGalleryAdapter.PickerTile> pickerTiles;

    @MediaActionState
    private int mediaActionState;
    private boolean showImageCrop = false;
    private FileObserver fileObserver;

    int lastVerticalOffset = 0;

    private enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    public CameraControlPanel(Context context) {
        this(context, null);
        this.context = context;
    }

    public CameraControlPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        Log.d(TAG, "init: ");

        LayoutInflater.from(context).inflate(R.layout.camera_control_panel_layout, this);
        hasFlash = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        setBackgroundColor(Color.TRANSPARENT);
        cameraSwitchView = findViewById(R.id.front_back_camera_switcher);
        recordButton = findViewById(R.id.record_button);
        flashSwitchView = findViewById(R.id.flash_switch_view);
        record_panel = findViewById(R.id.record_panel);
        recyclerView = findViewById(R.id.recycler_view);
        gallery_rv = findViewById(R.id.gallery_rv);
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        app_bar = findViewById(R.id.app_bar);
        recyclerViewHandlerButton = findViewById(R.id.img_recycler_view_handler);

        cameraSwitchView.setOnCameraTypeChangeListener(onCameraTypeChangeListener);

        setOnCameraTypeChangeListener(onCameraTypeChangeListener);
        setFlashModeSwitchListener(flashModeSwitchListener);
        setRecordButtonListener(recordButtonListener);

        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                app_bar.setExpanded(true, true);
            }
        });

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            private State state;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
//                Log.e("Call->", "verticalOffset->" + verticalOffset);
//                Log.e("Call->", "scrollRange->" + scrollRange);


                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("2KXO");
                    collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#aaaaaa"));
                    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#aaaaaa"));
                    toolbar.setNavigationIcon(ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_gray_24dp));
                    toolbar.setTitle("");
                    // toolbar.setTitleTextColor(Color.parseColor("#"));
                    isShow = true;

                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    toolbar.setNavigationIcon(null);
                    isShow = false;
                }

                if (verticalOffset >= -52 && verticalOffset <= 0) {
                    collapsingToolbarLayout.setTitle("");
                } else {
                    collapsingToolbarLayout.setTitle("2KXO");
                }
                if (verticalOffset == 0) {
                    if (state != State.EXPANDED) {
                        recyclerViewHandlerButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_up_white_24dp));
                    }
                    state = State.EXPANDED;
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != State.COLLAPSED) {
                        recyclerViewHandlerButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_down_white_24dp));
                    }
                    state = State.COLLAPSED;
                    recyclerView.setVisibility(GONE);
                } else {
                    if (state != State.IDLE) {
                        recyclerViewHandlerButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_down_white_24dp));
                    }
                    state = State.IDLE;
                }

//                if (lastVerticalOffset > verticalOffset) {
//                    recyclerViewHandlerButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_up_white_24dp));
//                } else {
//                    recyclerViewHandlerButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_down_white_24dp));
//                }
//
//                 if (verticalOffset == 0) {
//                    recyclerViewHandlerButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_up_white_24dp));
//                }
//
                if (Math.abs(verticalOffset) < 100) {
                    record_panel.setVisibility(VISIBLE);
                }

                if (Math.abs(verticalOffset) >= 100) {
                    record_panel.setVisibility(GONE);
                }

                lastVerticalOffset = verticalOffset;
            }
        });

        setGalleryData();

        int currentCameraType = CameraSwitchView.CAMERA_TYPE_REAR;
//        if (context instanceof BaseSandriosActivity) {
//            currentCameraType = ((BaseSandriosActivity) context).getCurrentCameraType();
//        } else if (context instanceof BaseSandriosFragment) {
//            currentCameraType = ((BaseSandriosActivity) context).getCurrentCameraType();
//        }

        if (hasFlash) {
            flashSwitchView.setVisibility(VISIBLE);
        } else {
            flashSwitchView.setVisibility(GONE);
        }
//        if( CameraSwitchView.CAMERA_TYPE_REAR==1)
//        {
//            flashSwitchView.setVisibility(VISIBLE);
//        }

    }

    public void setItemClickListener(ImageGalleryAdapter.OnItemClickListener listener) {
        if (imageHorizontalGalleryAdapter != null) {
            imageHorizontalGalleryAdapter.setOnItemClickListener(listener);
        }
    }

    public void lockControls() {
        cameraSwitchView.setEnabled(false);
        recordButton.setEnabled(false);
        flashSwitchView.setEnabled(false);
    }

    public void unLockControls() {
        cameraSwitchView.setEnabled(true);
        recordButton.setEnabled(true);
        flashSwitchView.setEnabled(true);
    }

    public void setup(int mediaAction) {
        if (CameraConfiguration.MEDIA_ACTION_VIDEO == mediaAction) {
            recordButton.setup(mediaAction, this);
            flashSwitchView.setVisibility(VISIBLE);
        } else {
            recordButton.setup(CameraConfiguration.MEDIA_ACTION_PHOTO, this);
            flashSwitchView.setVisibility(VISIBLE);
        }
    }

    public void setMediaFilePath(final File mediaFile) {
        this.mediaFilePath = mediaFile.toString();
    }

    public void setMaxVideoFileSize(long maxVideoFileSize) {
        this.maxVideoFileSize = maxVideoFileSize;
    }

    public void setFlasMode(@FlashSwitchView.FlashMode int flashMode) {
        flashSwitchView.setFlashMode(flashMode);
    }

    public void setHasFlash(boolean hasFlash) {
        this.hasFlash = hasFlash;
        if (hasFlash) {
            flashSwitchView.setVisibility(VISIBLE);
        } else {
            flashSwitchView.setVisibility(GONE);
        }
    }

    public void setMediaActionState(@MediaActionState int actionState) {
        if (mediaActionState == actionState) {
            return;
        }
        if (CameraControlPanel.ACTION_PHOTO == actionState) {
            recordButton.setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO);
            if (hasFlash) {
                flashSwitchView.setVisibility(VISIBLE);
            }
        } else {
            recordButton.setMediaAction(CameraConfiguration.MEDIA_ACTION_VIDEO);
            flashSwitchView.setVisibility(VISIBLE);
        }
        mediaActionState = actionState;
    }

    public void setRecordButtonListener(RecordButton.RecordButtonListener recordButtonListener) {
        this.recordButtonListener = recordButtonListener;
    }

    @SuppressLint("ObsoleteSdkInt")
    public void rotateControls(int rotation) {
        if (Build.VERSION.SDK_INT > 10) {
            cameraSwitchView.setRotation(rotation);
            flashSwitchView.setRotation(rotation);
        }
    }

    public void setOnCameraTypeChangeListener(CameraSwitchView.OnCameraTypeChangeListener onCameraTypeChangeListener) {
        this.onCameraTypeChangeListener = onCameraTypeChangeListener;
        if (cameraSwitchView != null) {
            cameraSwitchView.setOnCameraTypeChangeListener(this.onCameraTypeChangeListener);
        }
    }

    public void setFlashModeSwitchListener(FlashSwitchView.FlashModeSwitchListener flashModeSwitchListener) {
        this.flashModeSwitchListener = flashModeSwitchListener;
        if (flashSwitchView != null) {
            flashSwitchView.setFlashSwitchListener(this.flashModeSwitchListener);
        }
    }

    @Override
    public void onTakePhotoButtonPressed() {
        if (recordButtonListener != null) {
            recordButtonListener.onTakePhotoButtonPressed();
        }
    }

    public void onStartVideoRecord(final File mediaFile) {
        setMediaFilePath(mediaFile);
        if (maxVideoFileSize > 0) {
            try {
                fileObserver = new FileObserver(this.mediaFilePath) {
                    private long lastUpdateSize = 0;

                    @Override
                    public void onEvent(int event, String path) {
                        final long fileSize = mediaFile.length() / (1024 * 1024);
                        if ((fileSize - lastUpdateSize) >= 1) {
                            lastUpdateSize = fileSize;
                        }
                    }
                };
                fileObserver.startWatching();
            } catch (Exception e) {
                Log.e("FileObserver", "setMediaFilePath: ", e);
            }
        }
    }

    public void allowRecord(boolean isAllowed) {
        recordButton.setEnabled(isAllowed);
    }

    public void showPicker(boolean isShown) {
        recyclerView.setVisibility(isShown ? VISIBLE : GONE);
    }

    public boolean showCrop() {
        return showImageCrop;
    }

    public void shouldShowCrop(boolean showImageCrop) {
        this.showImageCrop = showImageCrop;
    }

    public void allowCameraSwitching(boolean isAllowed) {
        cameraSwitchView.setVisibility(isAllowed ? VISIBLE : GONE);
    }

    public void onStopVideoRecord() {
        if (fileObserver != null) {
            fileObserver.stopWatching();
        }
        recyclerView.setVisibility(VISIBLE);
        cameraSwitchView.setVisibility(View.VISIBLE);

        recordButton.setRecordState(RecordButton.READY_FOR_RECORD_STATE);
    }

    @Override
    public void onStartRecordingButtonPressed() {
        cameraSwitchView.setVisibility(View.GONE);
        recyclerView.setVisibility(GONE);

        if (recordButtonListener != null) {
            recordButtonListener.onStartRecordingButtonPressed();
        }
    }

    @Override
    public void onStopRecordingButtonPressed() {
        onStopVideoRecord();
        if (recordButtonListener != null) {
            recordButtonListener.onStopRecordingButtonPressed();
        }
    }

    @Override
    public int onCameraZoom(int zoom) {
        if (recordButtonListener != null) {
            return recordButtonListener.onCameraZoom(zoom);
        } else {
            return zoom;
        }
    }

    @Override
    public void onMediaActionChanged(int mediaActionState) {
        setMediaActionState(mediaActionState);
    }

    @IntDef({ACTION_PHOTO, ACTION_VIDEO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaActionState {
    }

    private void setGalleryData() {
        pickerTiles = new ArrayList<>();
        imageHorizontalGalleryAdapter = new ImageGalleryAdapter(context, pickerTiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(imageHorizontalGalleryAdapter);

        gallery_rv.setLayoutManager(new GridLayoutManager(context, 3));
        gallery_rv.setAdapter(imageHorizontalGalleryAdapter);

        imageVideoBackgroudOperation = new ImageVideoBackgroudOperation();
        imageVideoBackgroudOperation.execute();
        Log.d(TAG, "PROCESS_STARTED");
    }

    @SuppressLint("StaticFieldLeak")
    public class ImageVideoBackgroudOperation extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: ");
            Cursor imageVideoCursor = null;
            try {
                final String[] columns = {MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.MEDIA_TYPE};
                final String orderBy = MediaStore.Files.FileColumns.DATE_ADDED + " DESC";

                String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?";
                String[] selectionArgs = new String[]{String.valueOf(MEDIA_TYPE_IMAGE), String.valueOf(MEDIA_TYPE_VIDEO)};
                imageVideoCursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, selectionArgs, orderBy);

                if (imageVideoCursor != null) {
                    while (imageVideoCursor.moveToNext()) {
                        String location = imageVideoCursor.getString(imageVideoCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                        String mediaType = imageVideoCursor.getString(imageVideoCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE));
                        File file = new File(location);
                        pickerTiles.add(new ImageGalleryAdapter.PickerTile(Uri.fromFile(file), mediaType, getVideoContentUri(file)));
                        Log.d(TAG, "PROCESS_ADDING");

                        if (isCancelled()) {
                            return false;
                        }
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (imageVideoCursor != null && !imageVideoCursor.isClosed()) {
                    imageVideoCursor.close();
                }
            }
            return false;
        }

        private Uri getVideoContentUri(File file) {
            Uri videoContentUri = null;
            Cursor cursor = null;
            try {
                String filePath = file.getAbsolutePath();
                cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Video.Media._ID},
                        MediaStore.Video.Media.DATA + "=? ",
                        new String[]{filePath}, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                    cursor.close();
                    videoContentUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "" + id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return videoContentUri;
        }

        protected void onPostExecute(Boolean isSuccess) {
            try {
                if (isSuccess) {
                    imageHorizontalGalleryAdapter.updatePickerTiles(pickerTiles);
                    Log.d(TAG, "PROCESS_FINISHED");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
