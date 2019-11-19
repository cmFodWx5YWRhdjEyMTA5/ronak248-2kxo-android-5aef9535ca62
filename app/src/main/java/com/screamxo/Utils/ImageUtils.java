package com.screamxo.Utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.screamxo.Fragment.ImageDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parangat on 1/11/17.
 */
public class ImageUtils {

    @SuppressWarnings("unused")
    private static final String TAG = "ImageUtils";

    public static Dialog customChoosePhoto(Context context, String imageUrl) {
        List<String> images = new ArrayList<>();
        images.add(imageUrl);
        return customChoosePhoto(context, images);
    }

    public static Dialog customChoosePhoto(Context context, List<String> images) {
        Log.d(TAG, "customChoosePhoto: " + images.size());
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            ImageDialogFragment imageDialogFragment = ImageDialogFragment.newInstance(images);
            imageDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), ImageDialogFragment.class.getSimpleName());
        }
        return null;
    }

    public static Dialog customChoosePhoto(Context context, List<String> images,int position) {
        Log.d(TAG, "customChoosePhoto: " + images.size());
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            ImageDialogFragment imageDialogFragment = ImageDialogFragment.newInstance(images,position);
            imageDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), ImageDialogFragment.class.getSimpleName());
        }
        return null;
    }


    public static final String[] IMAGE_FILE_EXTENSION = new String[]{"jpg", "png", "gif", "jpeg"};

}
