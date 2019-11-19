package com.screamxo.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import com.screamxo.Interface.ImagePicckerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class VideoPicker implements ImagePicckerDialog {
    private static final String TAG = VideoPicker.class.getSimpleName();
    private Context context;
    private Activity activity;
    private Uri capturedVideoUri;
    private VideoInterface listener;

    public VideoPicker(Context context, VideoInterface listener) {
        this.context = context;
        activity = (Activity) context;
        this.listener = listener;
    }

    public void Videopicker() {
        DialogBox.showAlertImagePicker(activity, this);
    }

    @Override
    public void OnclickCamara() {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        try {
            if (Build.VERSION.SDK_INT > 19) {
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.6);
            } else {
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.2);
            }
            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 90);
            activity.startActivityForResult(intent, StaticConstant.REQUEST_VIDEO_CAPTURE);
        } catch (Exception e) {
            Log.v("Video Picker => ", e.getMessage());
            Utils.showToast(context, "Do not create file, Please try again.");
        }
    }

    @Override
    public void OnclickGallery() {

        List<Intent> targets = new ArrayList<>();
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_PICK);
        List<ResolveInfo> candidates = activity.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo candidate : candidates) {
            String packageName = candidate.activityInfo.packageName;
            if (!packageName.equals("com.google.android.apps.photos") && !packageName.equals("com.google.android.apps.plus") && !packageName.equals("com.android.documentsui")) {
                Intent iWantThis = new Intent();
                iWantThis.setType("video/*");
                iWantThis.setAction(Intent.ACTION_PICK);
                iWantThis.setPackage(packageName);
                targets.add(iWantThis);
            }
        }
        if (targets.size() > 0) {
            Intent chooser = Intent.createChooser(targets.remove(0), "Select Video");
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(new Parcelable[targets.size()]));
            activity.startActivityForResult(chooser, StaticConstant.REQUEST_VIDEO);
        } else {
            activity.startActivityForResult(Intent.createChooser(intent, "Select Video"), StaticConstant.REQUEST_VIDEO);
        }
    }

    @Override
    public void OnclickNoImage() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case StaticConstant.REQUEST_VIDEO:
                    try {
                        Log.v(" => data result ", "" + data.getData());
                        String pathFromURI = getRealPathFromURI(data.getData());
                        Log.v("Video path => ", +new File(pathFromURI).length() + " Byte");
                        Log.v("Video path => ", +(new File(pathFromURI).length() / 1024) + " KB");
                        long fileSize = (new File(pathFromURI).length() / (1024 * 1024));
                        Log.v("Video path => ", +(new File(pathFromURI).length() / (1024 * 1024)) + " MB");
                        if (fileSize < 90) {
                            listener.setUriForView(new File(pathFromURI), Uri.parse(pathFromURI));
                        } else {
                            Utils.showToast(context, "Please select less then 90 MB.");
                        }
                    } catch (Exception e) {
                        Log.v(" ==> gallery result ", e.getMessage());
                        Utils.showToast(context, "Do not create file, Please try again.");
                    }
                    break;

                case StaticConstant.REQUEST_VIDEO_CAPTURE:
                    try {
                        String pathFromURI = getRealPathFromURI(data.getData());
                        long fileSize = (new File(pathFromURI).length() / (1024 * 1024));
                        if (fileSize < 90) {
                            listener.setUriForView(new File(pathFromURI), Uri.parse(pathFromURI));
                        } else {
                            Utils.showToast(context, "Please select less then 90 MB.");
                        }
                    } catch (Exception e) {
                        Log.v(" => gallery result ", "" + e.getMessage());
                        Utils.showToast(context, "Do not create file, Please try again.");
                    }
                    break;
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public interface VideoInterface {
        void setUriForView(File file, Uri uri);
    }
}
