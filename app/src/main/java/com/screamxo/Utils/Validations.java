package com.screamxo.Utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shubham.Agarwal on 10/10/16.
 */

public class Validations {
    String TAG = Validations.class.getSimpleName();

    public boolean checkEmail(String inputMail) {
        Pattern p = Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_])+(\\.([a-zA-Z]{2,4}+))?(\\.([a-zA-Z]{2,4}))$");
        Matcher m = p.matcher(inputMail);
        return !m.matches();
    }

    public boolean checkPhoneNo(String inputPhone) {
        Pattern pattern = Pattern.compile("^[+0-9]{10,13}$");
        Matcher m = pattern.matcher(inputPhone);
        return !m.matches();

    }

    public boolean ischeckPassword(String password) {
        return password.length() < 6;
    }

    public String isNullThenEmpty(String str) {
        if (str == null || str.equalsIgnoreCase("null") || str.trim().length() == 0) {
            return "";
        } else {
            return str;
        }
    }

    //  2017-08-09 05:23:56
    public String dateFormationUTCtoLocal(String inPutFormate, String utcTime) {
//        Log.d(TAG, "dateFormationUTCtoLocal inPutFormate: " + inPutFormate);
//        Log.d(TAG, "dateFormationUTCtoLocal utcTime: " + utcTime);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format_before = new SimpleDateFormat(inPutFormate);

        format_before.setTimeZone(TimeZone.getTimeZone("UTC"));

        //   SimpleDateFormat format_to_Convert = new SimpleDateFormat("yyyy-MMMM-dd HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format_to_Convert = new SimpleDateFormat("yyyy-MMMM-dd_h:mm a");

        format_to_Convert.setTimeZone(TimeZone.getDefault());

        Date time = null;
        try {
            time = format_before.parse(utcTime);
            Date now = new Date();

            if (TimeUnit.MILLISECONDS.toDays(now.getTime() - time.getTime()) == 0) {
                if (TimeUnit.MILLISECONDS.toMinutes(now.getTime() - time.getTime()) <= 0) {
                    return "just now";
                } else if (TimeUnit.MILLISECONDS.toHours(now.getTime() - time.getTime()) == 0) {
                    return "" + TimeUnit.MILLISECONDS.toMinutes(Math.max(0, now.getTime() - time.getTime())) + " minutes ago";
                } else {
                    return "" + TimeUnit.MILLISECONDS.toHours(now.getTime() - time.getTime()) + " hours ago";
                }
            } else {
                String date = format_to_Convert.format(time);
                String[] parts = date.split("_");
                String[] strings = parts[0].split("-");
                String[] strings1 = parts[1].split(":");
                //     String[] strings2 = parts[2].split(":");
                return strings[1] + " " + strings[2] + " at " + strings1[0] + ":" + strings1[1];
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";

    }
}
