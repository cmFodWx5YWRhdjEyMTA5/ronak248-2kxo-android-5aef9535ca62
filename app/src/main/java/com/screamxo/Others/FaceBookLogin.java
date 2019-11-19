package com.screamxo.Others;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.Emailfriend;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.screamxo.Interface.FacebookInterface;
import com.screamxo.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class FaceBookLogin {
    private static final String TAG = "FaceBookLogin";
    private CallbackManager callbackManager;

    public FaceBookLogin(final Context context, final FacebookInterface facebookInterface) {
        Log.d(TAG, "FaceBookLogin: ");
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d(TAG, "onSuccess: ");
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                Log.d(TAG, "onCompleted: " + object);
                                if (loginResult.getAccessToken() != null) {
                                    Set<String> declinedPermissions = loginResult.getRecentlyDeniedPermissions();
                                    if (declinedPermissions.size() > 0 && (declinedPermissions.contains("email") || declinedPermissions.contains("user_friends"))) {
                                        if (declinedPermissions.contains("email")) {
                                            Toast.makeText(context, "Please allow email permission in facebook", Toast.LENGTH_LONG).show();
                                        } else if (declinedPermissions.contains("user_friends")) {
                                            Toast.makeText(context, "Please allow User Friend permission in facebook", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    try {
                                        HashMap<String, String> param = new HashMap<>();
                                        param.put("fname", String.valueOf(object.optString("first_name")));
                                        param.put("lname", String.valueOf(object.optString("last_name")));
                                        param.put("email", String.valueOf(object.optString("email")));
                                        param.put("fbid", loginResult.getAccessToken().getUserId());
                                        facebookInterface.success(param);
                                        getFriendList(AccessToken.getCurrentAccessToken(), facebookInterface);
//                                        LoginManager.getInstance().logOut();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name,picture{url}");
                request.setParameters(parameters);
                request.executeAsync();
                // App code
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
                Toast.makeText(context, context.getResources().getString(R.string.toast_login_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
                Log.d(TAG, "onError: " + exception.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.toast_login_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getFriendList(AccessToken accessToken, FacebookInterface facebookInterface) {
        Log.d(TAG, "getFriendList: " + accessToken);
        GraphRequest request = GraphRequest.newMyFriendsRequest(accessToken, (graphArray, graphResponse) -> {
            Log.d(TAG, "onCompleted: " + graphArray);
            ArrayList<Object> list = new ArrayList<>();
            try {
                for (int i = 0; i < graphArray.length(); i++) {
                    JSONObject jsonObject = graphArray.getJSONObject(i);
                    Emailfriend bean = new Emailfriend();
                    bean.setFname(String.valueOf(jsonObject.getString("first_name")));
                    bean.setLname(String.valueOf(jsonObject.getString("last_name")));
                    bean.setPhoto("http://graph.facebook.com/ " + String.valueOf(jsonObject.getString("id")) + "/picture?type=large");
                    bean.setFbid(String.valueOf(jsonObject.getString("id")));
                    if ((bean.getFbid() != null) && !bean.getFbid().equals("")) {
                        list.add(bean);
                    }
                }
            } catch (Exception e) {
                list = new ArrayList<>();
            }
            facebookInterface.onFbFrdFetch(list);
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,picture{url}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void faceBookManager(Context context) {
        //LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile", "email", "user_friends"));
//        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile"));
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
