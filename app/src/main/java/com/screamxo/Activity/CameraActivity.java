package com.screamxo.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.screamxo.R;
import com.tbruyelle.rxpermissions.RxPermissions;
import rx.functions.Action1;

public class CameraActivity extends AppCompatActivity
{
    private static final int CAPTURE_MEDIA = 368;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initCamera();
    }

    private void initCamera()
    {

        RxPermissions.getInstance(CameraActivity.this)
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(new Action1<Boolean>()
                {
                    @Override
                    public void call(Boolean aBoolean)
                    {
                        if (aBoolean)
                        {
                            new SandriosCamera(CameraActivity.this, CAPTURE_MEDIA)
                                    .setShowPicker(true)
                                    .setVideoFileSize(20)
                                    .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                                    .enableImageCropping(true)
                                    .launchCamera();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_MEDIA && resultCode == RESULT_OK)
        {
            Toast.makeText(this, "Media captured.", Toast.LENGTH_SHORT).show();
        }
    }
}
