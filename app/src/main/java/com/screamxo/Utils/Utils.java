package com.screamxo.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.screamxo.Activity.CommonLoginSignUpActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {
    private static final String TAG = "Utils";

    public static void showToast(Context context, String message) {
        if (!(message == null || message.equalsIgnoreCase("null") || message.trim().length() == 0)) {
            //    Log.i("Utils","......."+message);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isInternetOn(final Context context) {
        final boolean[] haveConnectedWifi = {false};
        final boolean[] haveConnectedMobile = {false};

        RxPermissions.getInstance(context).request(Manifest.permission.ACCESS_NETWORK_STATE).subscribe(aBoolean -> {
            if (aBoolean) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                        if (ni.isConnected()) {
                            haveConnectedWifi[0] = true;
                        }
                    }
                    if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                        if (ni.isConnected()) {
                            haveConnectedMobile[0] = true;
                        }
                    }
                }
            }
        });
        return haveConnectedWifi[0] || haveConnectedMobile[0];
    }

    public static void unAuthentication(Context context) {
        Preferences preferences = new Preferences(context);
        preferences.clearAllApreferences();
        Intent intent = new Intent(context, CommonLoginSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     */
    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */
    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = totalDuration / 1000;
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null) {
            return;
        }

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Context ctx, View view) {
        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null) {
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    public static String getFormattedPrice(@Nullable Object unformattedPrice) {
        if (unformattedPrice == null) {
            Log.i(TAG, "getFormattedPrice: UNFORMATTED PRICE IS NULL");
            return "$ 00";
        }

        NumberFormat numberFormat = DecimalFormat.getCurrencyInstance(Locale.getDefault());
//        numberFormat.setCurrency(Currency.getInstance(Locale.US));
        try {
            if (unformattedPrice instanceof String) {
                try {
                    unformattedPrice = Double.parseDouble((String) unformattedPrice);
                } catch (NumberFormatException e) {
                    Log.e(TAG, "getFormattedPrice: ", e);
                }
            }
            return numberFormat.format(unformattedPrice).replace("$", "$ ").replace(".00", "");
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "getFormattedPrice: ", e);
            return null;
        }
    }


    public static void printIntentData(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "printIntentData intent.getData(): " + intent.getData());
            printBundleData(intent.getExtras());
        }

    }

    public static void printBundleData(Bundle bundle) {
        Log.d(TAG, "printBundleData bundle == null: " + (bundle == null));
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d(TAG, String.format("%s %s (%s)", key, value != null ? value.toString() : null, value != null ? value.getClass().getName() : null));
            }
        }
    }


    public static String toString(Object object) {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(object.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                result.append(field.get(object));
            } catch (IllegalAccessException ex) {
                Log.d(TAG, "toString: " + ex.getMessage());
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }

    public static void logFirebaseToken() {
        try {
            Log.e(TAG, "logFirebaseToken: " + FirebaseInstanceId.getInstance().getToken());
        } catch (Exception e) {
            Log.e(TAG, "logFirebaseToken: ", e);
        }

    }

    public static void shareText(Context context, String extraText, String chooserTitle) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, extraText);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, chooserTitle));
    }

    public static int dip2pixel(Context context, float n) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, n, context.getResources().getDisplayMetrics());
    }

    public static int getRandomNumber(int min,int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }

    public static Drawable getMaskDrawable(Context context, int maskId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(maskId);
        } else {
            drawable = context.getResources().getDrawable(maskId);
        }

        if (drawable == null) {
            throw new IllegalArgumentException("maskId is invalid");
        }
        return drawable;
    }
}