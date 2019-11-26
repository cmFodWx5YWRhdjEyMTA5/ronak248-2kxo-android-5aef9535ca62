package com.screamxo.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.screamxo.Activity.RahulWork.RecorderTagActivity;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.R;
import com.screamxo.Utils.DialogFragments;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.sandrios.sandriosCamera.internal.ui.preview.PreviewDashboardActivity.FILE_CAPTION_ARG;
import static com.sandrios.sandriosCamera.internal.ui.preview.PreviewDashboardActivity.FILE_PATH_ARG;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_RECORDER_TAG_ACTIVITY_RESULTS;

@SuppressLint("DefaultLocale")
public class UploadMediaAcitvity extends AppCompatActivity implements View.OnClickListener, CommonMethod {

    public static final int RC_PICK_AUDIO_CONTENT = 101;
    public static final int RequestPermissionCode = 1;

    private static final int CAPTURE_MEDIA = 368;
    private static final String TAG = "UploadMediaAcitvity";
    @BindView(R.id.imgCamera)
    ImageView imgCamera;
    @BindView(R.id.imgRecord)
    ImageView imgRecord;
    @BindView(R.id.imgMusic)
    ImageView imgMusic;
    @BindView(R.id.txtTimer)
    TextView txtTimer;
    @BindView(R.id.txtControl)
    TextView txtControl;

    String RandomAudioFileName = "2KXO";
    MediaRecorder mediaRecorder;
    Boolean canRecord = true;
    long timeInMilliseconds = 0L;
    long updatedTime = 0L;
    long timeSwapBuff = 0L;
    Context context;
    String type[];
    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private long startTime = 0L;
    private Handler mainHandler = new Handler();
    private String outputAudioFilePath;
    private Runnable updateTimerThread = new Runnable() {

        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            txtTimer.setVisibility(View.VISIBLE);
            txtTimer.setText(String.format("%s:%s", String.format("%02d", mins), String.format("%02d", secs)));
            mainHandler.postDelayed(this, 0);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_media_acitvity);
        ButterKnife.bind(this);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorNewBlue));

        initData();
        initFabIcon();
    }

    private void initData() {
        context = UploadMediaAcitvity.this;
        type = new String[1];
        StaticConstant.selectedTimePos = -1;
    }

    private void initFabIcon() {
        Log.d(TAG, "initFabIcon: ");
        try {
            floatingButton = findViewById(R.id.my_floating_button);
            floatingButton.setStartAngle(0)
                    .setEndAngle(360)
                    .setAnimationType(AnimationType.EXPAND)
                    .setRadius(170)
                    .setAnchored(false)
                    .getAnimationHandler()
                    .setOpeningAnimationDuration(500)
                    .setClosingAnimationDuration(200)
                    .setLagBetweenItems(0)
                    .setOpeningInterpolator(new FastOutSlowInInterpolator())
                    .setClosingInterpolator(new FastOutLinearInInterpolator())
                    .shouldFade(true)
                    .shouldScale(true)
                    .shouldRotate(true);

            floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));

            floatingButton.setStateChangeListener(new FloatingMenuStateChangeListener() {
                @Override
                public void onMenuOpened(FloatingMenuButton floatingMenuButton) {
                    Log.d(TAG, "onMenuOpened: ");
                    Utils.hideKeyboard(UploadMediaAcitvity.this);
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.cross));
                }

                @Override
                public void onMenuClosed(FloatingMenuButton floatingMenuButton) {
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                }
            });

            sbProfile = findViewById(R.id.sbProfile);
            sbProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 6);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbSocial = findViewById(R.id.sbSocial);
            sbSocial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 3);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbChat = findViewById(R.id.sbChat);
            sbChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                }
            });

            sbWorld = findViewById(R.id.sbWorld);
            sbWorld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 2);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbSearch = findViewById(R.id.sbSearch);
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                }
            });

            sbflSetting = findViewById(R.id.sbflSetting);
            sbflSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 7);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            subFriend = findViewById(R.id.subFriend);
            subFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 5);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbflHome = findViewById(R.id.sbflHome);
            sbflHome.setOnClickListener(view -> {
                floatingButton.closeMenu();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", 1);
                setResult(RESULT_OK, returnIntent);
                finish();
            });

        } catch (Exception e) {
            Log.e(TAG, "initFabIcon: ", e);
        }
    }

    @OnClick({R.id.lnr_camera, R.id.lnr_record, R.id.lnr_music, R.id.lnr_timer})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnr_camera:
                RxPermissions.getInstance(UploadMediaAcitvity.this).request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO).subscribe(aBoolean -> {
                    if (aBoolean) {
                        new SandriosCamera(UploadMediaAcitvity.this, CAPTURE_MEDIA).setShowPicker(true).setMediaAction(CameraConfiguration
                                .MEDIA_ACTION_BOTH).enableImageCropping(true).launchCamera();
                    }
                });
                break;
            case R.id.lnr_record:
                if (canRecord) {
                    if (hasRequiredPermissions()) {
                        canRecord = false;
                        startTime = SystemClock.uptimeMillis();
                        mainHandler.postDelayed(updateTimerThread, 0);
                        imgRecord.setImageDrawable(getResources().getDrawable(R.mipmap.mic_black));
                        setUpMediaRecorder();
                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                            Toast.makeText(UploadMediaAcitvity.this, "Recording started", Toast.LENGTH_LONG).show();
                        } catch (IllegalStateException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        requestPermission();
                    }

                } else {
                    timeSwapBuff += timeInMilliseconds;
                    mainHandler.removeCallbacks(updateTimerThread);
                    mediaRecorder.stop();
                    canRecord = true;
                    imgRecord.setImageDrawable(getResources().getDrawable(R.drawable.ico_microphone));

                    Intent gotoNext = new Intent(UploadMediaAcitvity.this, RecorderTagActivity.class);
                    gotoNext.putExtra("songUrl", outputAudioFilePath);
                    gotoNext.putExtra("from", "record");
                    startActivityForResult(gotoNext, REQ_CODE_RECORDER_TAG_ACTIVITY_RESULTS);
                }
                break;
            case R.id.lnr_music:
                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent_upload, "Play the song with"), RC_PICK_AUDIO_CONTENT);
                break;

            case R.id.lnr_timer:
                FragmentManager fm = getFragmentManager();
                DialogFragments dialogFragment = new DialogFragments(this);
                dialogFragment.show(fm, "Time Fragment");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult requestCode: " + requestCode);
        Log.d(TAG, "onActivityResult resultCode: " + resultCode);
        Utils.printIntentData(data);

        if (resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("FRAGMENT_INDEX")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", data.getExtras().getInt("FRAGMENT_INDEX"));
                setResult(RESULT_OK, returnIntent);
                finish();
                return;
            }
        }

        switch (requestCode) {
            case CAPTURE_MEDIA:
                switch (resultCode) {
                    case RESULT_OK:
                        if (data != null) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra(FILE_PATH_ARG, data.getStringExtra(FILE_PATH_ARG));
                            resultIntent.putExtra(FILE_CAPTION_ARG, data.getStringExtra(FILE_CAPTION_ARG));
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                        break;
                }
                break;

            case RC_PICK_AUDIO_CONTENT:

//                content://com.google.android.apps.docs.storage/document/acc%3D1%3Bdoc%3D1779

                switch (resultCode) {
                    case RESULT_OK:
                        if (data != null) {
                            Uri uri = data.getData();
                            if (uri != null) {
                                Intent gotoNext = new Intent(UploadMediaAcitvity.this, RecorderTagActivity.class);
                                gotoNext.putExtra("songUrl", uri.toString());
                                gotoNext.putExtra("from", "music");
                                gotoNext.putExtra("from", "music");

                                startActivityForResult(gotoNext, REQ_CODE_RECORDER_TAG_ACTIVITY_RESULTS);
                            }
                        }
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        resetMediaRecorder();
    }

    private void resetMediaRecorder() {
        canRecord = true;
        imgRecord.setBackground(getResources().getDrawable(R.drawable.ico_microphone));
        timeInMilliseconds = 0L;
        updatedTime = 0L;
        timeSwapBuff = 0L;
        startTime = 0L;
        mainHandler.removeCallbacks(updateTimerThread);
        txtTimer.setVisibility(View.GONE);
        txtTimer.setText("");
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(UploadMediaAcitvity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(UploadMediaAcitvity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(UploadMediaAcitvity.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    public boolean hasRequiredPermissions()
    {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void setUpMediaRecorder()
    {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        outputAudioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(5) + "2KXO.m4a";
        mediaRecorder.setOutputFile(outputAudioFilePath);
    }

    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        Random random = new Random();
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));
            i++;
        }
        return stringBuilder.toString();
    }

    @Override
    public void commonMethod(String type, File... files) {
        Log.d(TAG, "commonMethod: " + type);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StaticConstant.selectedTimePos = -1;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}





