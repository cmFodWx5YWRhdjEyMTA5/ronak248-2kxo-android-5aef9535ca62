package com.screamxo.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.screamxo.BuildConfig;
import com.screamxo.Interface.ImagePicckerDialog;
import com.screamxo.Interface.Imagepath;
import com.screamxo.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhotoPicker implements ImagePicckerDialog {

    private static final String TAG = PhotoPicker.class.getSimpleName();
    private Context context;
    private Activity activity;
    private Imagepath imagepath;
    private File photoFile = null;
    private CompressImage compressImage;
    private Uri outputFileUri;
    private static final int IMAGE_SAVE_AT_URI_MAX_FAILURE_COUNT = 5;
    private int image_save_at_uri_failure_count = 0;
    private boolean isCropImage = true;

    public PhotoPicker(Context context, Imagepath imagepath) {
        this.context = context;
        activity = (Activity) context;
        this.imagepath = imagepath;
        compressImage = new CompressImage();
    }

    public PhotoPicker(Context context, Imagepath imagepath, boolean isCropImage) {
        this.context = context;
        activity = (Activity) context;
        this.imagepath = imagepath;
        compressImage = new CompressImage();
        this.isCropImage = isCropImage;
    }

    public void ImagePickker() {
        DialogBox.showAlertImagePicker(activity, this);
    }

    public void ImagePickker(Boolean updateImage) {
        Log.d(TAG, "ImagePickker: ");
        if (updateImage) {
            DialogBox.showAlertImagePickerUpdate(activity, this);
        } else {
            DialogBox.showAlertImagePicker(activity, this);
        }
    }

    @Override
    public void OnclickCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException ignored) {
            }
            if (photoFile != null) {
                Uri photoURI;
                image_save_at_uri_failure_count = 0;
                if (Build.VERSION.SDK_INT >= 24) {
                    photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                } else {
                    photoURI = Uri.fromFile(photoFile);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, StaticConstant.REQUEST_CAPTURE);
            }
        }
    }

    @Override
    public void OnclickGallery() {
        List<Intent> targets = new ArrayList<>();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        List<ResolveInfo> candidates = activity.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo candidate : candidates) {
            String packageName = candidate.activityInfo.packageName;
            if (!packageName.equals("com.google.android.apps.photos") && !packageName.equals("com.google.android.apps.plus") && !packageName.equals("com.android.documentsui")) {
                Intent iWantThis = new Intent();
                iWantThis.setType("image/*");
                iWantThis.setAction(Intent.ACTION_PICK);
                iWantThis.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                iWantThis.setPackage(packageName);
                targets.add(iWantThis);
            }
        }
        if (targets.size() > 0) {
            Intent chooser = Intent.createChooser(targets.remove(0), "Select Picture");
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(new Parcelable[targets.size()]));
            activity.startActivityForResult(chooser, StaticConstant.REQUEST_GALLERY);
        } else {
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType("image/*");
            activity.startActivityForResult(Intent.createChooser(intent1, "Select Picture"), StaticConstant.REQUEST_GALLERY);
        }
    }

    @Override
    public void OnclickNoImage() {
        imagepath.imagePathfromGallery(null);
    }

//    @Override
//    public void onRemovePhoto() {
//        Log.d(TAG, "onRemovePhoto: ");
//    }

    private File createImageFile() throws IOException {
        Log.d(TAG, "createImageFile: ");
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        outputFileUri = Uri.fromFile(image);
        return image;
    }

    private void sendImageFroCrop(Uri selectedImageUri) {
        Log.d(TAG, "sendImageFroCrop: ");
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        options.setToolbarWidgetColor(ContextCompat.getColor(context, R.color.colorBlack));
        options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorBlack));
        options.setToolbarTitle(" ");
        options.setHideBottomControls(true);
        options.setActiveWidgetColor(ContextCompat.getColor(context, R.color.colorPrimary));
        options.setFreeStyleCropEnabled(true);
        try {
            outputFileUri = Uri.fromFile(createImageFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        UCrop.of(selectedImageUri, outputFileUri)
                .withAspectRatio(16, 9)
                .withOptions(options)
                .start((Activity) context);
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onResult requestCode: " + requestCode);
        Log.d(TAG, "onResult resultCode: " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case StaticConstant.REQUEST_CAPTURE:
//                    Log.d(TAG, "onResult Utils.toString(photoFile): " + Utils.toString(photoFile));
//                    Log.d(TAG, "onResult photoFile == null: " + (photoFile == null));
//                    Log.d(TAG, "onResult photoFile.length(): " + (photoFile != null ? photoFile.length() : "photoFile is NULL"));
                    if (photoFile != null && image_save_at_uri_failure_count++ < IMAGE_SAVE_AT_URI_MAX_FAILURE_COUNT) {
                        if (photoFile.length() != 0) {
                            if (isCropImage) {
                                sendImageFroCrop(Uri.fromFile(new File(compressImage.compressImage(FileUtils.getFile(context, outputFileUri).getPath(), context))));
                            }else{
                                imagepath.imagePathfromCamara(photoFile);
                            }
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "run Looper.myLooper() == Looper.getMainLooper(): " + (Looper.myLooper() == Looper.getMainLooper()));
                                    Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show();
                                    onResult(requestCode, resultCode, data);
                                }
                            }, 2000);
                        }
                    } else {
                        Toast.makeText(context, "Capture image failed. Please retry...", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case StaticConstant.REQUEST_GALLERY:
                    photoFile = FileUtils.getFile(context, data.getData());
                    if (photoFile != null && image_save_at_uri_failure_count++ < IMAGE_SAVE_AT_URI_MAX_FAILURE_COUNT) {
                        if (photoFile.length() != 0) {
                            if (isCropImage) {
                                sendImageFroCrop(Uri.fromFile(new File(compressImage.compressImage(FileUtils.getFile(context, data.getData()).getPath(), context))));
                            } else {
                                imagepath.imagePathfromCamara(photoFile);
                            }
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "run Looper.myLooper() == Looper.getMainLooper(): " + (Looper.myLooper() == Looper.getMainLooper()));
                                    Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show();
                                    onResult(requestCode, resultCode, data);
                                }
                            }, 2000);
                        }
                    } else {
                        Toast.makeText(context, "Capture image failed. Please retry...", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    try {
                        photoFile = new File(FileUtils.getFile(context, outputFileUri).getPath());
                        imagepath.imagepathfromCropImage(photoFile);
                    } catch (Exception e) {
                        Log.v("exception", " " + e.toString());
                    }
            }
        } else {
            Toast.makeText(context, "Photo not save please try again", Toast.LENGTH_SHORT).show();
        }
    }

}
