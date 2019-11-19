package com.screamxo.Utils;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Shubham Agarwal on 06/10/16.
 */

public class RequestBodyConveter {
    private static final String TAG = "RequestBodyConveter";

    public Map<String, RequestBody> converRequestBodyFromMap(Map<String, String> map) {
        Log.d(TAG, "converRequestBodyFromMap: ");
        Map<String, RequestBody> map1 = new HashMap<>();
        int size = map.size();
        for (int i = 0; i < size; i++) {
            try {
                String key = (String) map.keySet().toArray()[i];
                RequestBody value = RequestBody.create(MediaType.parse("text/plain"), map.get(key));
                map1.put(key, value);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        return map1;
    }

    public ArrayList<MultipartBody.Part> converRequestBodyFromMapImage(Map<String, File> map, String[] type) {
        Log.d(TAG, "converRequestBodyFromMapImage: ");
        ArrayList<MultipartBody.Part> map1 = new ArrayList<>();
        int size = map.size();
        RequestBody requestBody = null;
        for (int i = 0; i < size; i++) {
            try {
                String key = (String) map.keySet().toArray()[i];
                switch (type[i]) {
                    case "1":
                        requestBody = RequestBody.create(MediaType.parse("video/mp4"), map.get(key));
                        break;

                    case "2":
                        requestBody = RequestBody.create(MediaType.parse("image/*"), map.get(key));
                        break;

                    case "3":
                        requestBody = RequestBody.create(MediaType.parse("audio/m4a"), map.get(key));
                        break;

                    case "4":
                        requestBody = RequestBody.create(MediaType.parse("audio/m4a"), map.get(key));
                        break;
                }

                MultipartBody.Part body = MultipartBody.Part.createFormData(key, map.get(key).getName(), requestBody);
                map1.add(body);

            } catch (IndexOutOfBoundsException ignored) {
                Log.e(TAG, "converRequestBodyFromMapImage: " + ignored.getMessage());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return map1;
    }
}
