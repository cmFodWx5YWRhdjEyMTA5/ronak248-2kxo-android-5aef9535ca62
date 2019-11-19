package com.screamxo.Others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.screamxo.Activity.ChatActivity;
import com.screamxo.Interface.CommonMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private Context context;
    CommonMethod commonMethod;

    public ImageDownloaderTask(Context context, CommonMethod... commonMethods) {
        this.context = context;

        if (commonMethods.length > 0) {
            commonMethod = commonMethods[0];
        }
    }

    @Override
    protected Bitmap doInBackground(String... URL) {

        String imageURL = URL[0];

        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/SXO");

        if (!myDir.exists()) {
            if (!myDir.mkdirs()) {
                return;
            }
        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        String fname = "2KXO" + timeStamp + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            result.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            if (commonMethod != null) {
                commonMethod.commonMethod(file.getName(), file);
            }

            if (context instanceof ChatActivity) {
                Toast.makeText(context, "Image Download successfully", Toast.LENGTH_SHORT).show();
            }
//

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        //something that you want to do
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + file.getAbsolutePath())));
                    }
                });
            } else {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" + file.getAbsolutePath())));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}