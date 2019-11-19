package com.screamxo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.CategoryList.CategoryList;
import com.example.apimodule.ApiBase.ApiBean.FollowersList;
import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.ApiBean.StripeCreateCustomer.StripeCreateCustomerReponse;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.screamxo.Activity.instagram.InstagramHelper;
import com.screamxo.Activity.instagram.InstagramHelperConstants;
import com.screamxo.Activity.instagram.InstagramUser;
import com.screamxo.BuildConfig;
import com.screamxo.Interface.FacebookInterface;
import com.screamxo.Others.FaceBookLogin;
import com.screamxo.Others.MyTwitterApiClient;
import com.screamxo.R;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

@Deprecated
public class LoginActivity extends AppCompatActivity implements FacebookInterface {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int FACEBOOK_REQUEST_CODE = 64206;
    @BindView(R.id.btn_signin)
    Button btnSignin;
    @BindView(R.id.img_facebook)
    ImageView imgFacebook;
    @BindView(R.id.img_twitter)
    ImageView imgTwitter;
    @BindView(R.id.img_instagram)
    ImageView imgInstagram;
    @BindView(R.id.txt_forgot_password)
    TextView txtForgotPassword;
    @BindView(R.id.txt_new_sign_up)
    TextView txtNewSignUp;
    @BindView(R.id.edt_user_name)
    EditText medtUserName;
    @BindView(R.id.edt_password)
    EditText medtPassword;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
//    private static final String TWITTER_KEY = "0nlwkqUg9rVCOCGcolOhJcfiV";
//    private static final String TWITTER_SECRET = "OQqwNleDliiae6v29ekZhcObFmvlUEr2fkkrEV7aGjv9yyiu9f";
    @BindView(R.id.progreessbar)
    ProgressBar progressbar;

    String Token;
    private LoginActivity context;
    private FaceBookLogin faceBookLogin;
    private HashMap<String, String> map;
    private TwitterAuthClient mTwitterAuthClient;
    private InstagramHelper instagramHelper;
    private FetchrServiceBase mService;
    private Preferences preferences;

    Call<LoginBean> loginBeanCall;
    Call<CategoryList> categoryListCall;
    Call<StripeCreateCustomerReponse> createStripeCustomerCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseInstanceId.getInstance().getToken();
        setContentView(R.layout.activity_login);
        init();
        Log.e(TAG, "onCreate: " + FirebaseInstanceId.getInstance().getToken());
        Token = FirebaseInstanceId.getInstance().getToken();
        ButterKnife.bind(this);
    }

    private void init() {
        context = this;
        preferences = new Preferences(context);
        mService = new FetchrServiceBase();
        faceBookLogin = new FaceBookLogin(context, this);
        map = new HashMap<>();
    }

    @Override
    public void success(Map<String, String> map) {
        this.map.putAll(map);
        callSocialLogin();
    }

    @Override
    public void onFbFrdFetch(ArrayList<Object> list) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mTwitterAuthClient != null) {
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FACEBOOK_REQUEST_CODE:
                    faceBookLogin.onResult(requestCode, resultCode, data);
                    break;

                case InstagramHelperConstants.INSTA_LOGIN:

                    InstagramUser user = instagramHelper.getInstagramUser(this);
                    String[] userName = user.getData().getFullName().split(" ");

                    map.put("fname", userName[0]);

                    if (userName.length > 1 && userName[1] != null) {
                        map.put("lname", userName[1]);
                    } else {
                        map.put("lname", "");
                    }

                    map.put("googleid", user.getData().getId());
                    callSocialLogin();
                    break;
            }

        } else {
            Utils.showToast(context, context.getResources().getString(R.string.toast_login_fail));
        }
    }

    public void onClickFb(View view) {
        if (Utils.isInternetOn(context)) {
            faceBookLogin.faceBookManager(context);
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void onTwitterClick(View view) {
        Log.d(TAG, "onTwitterClick: ");
        mTwitterAuthClient = new TwitterAuthClient();
        if (Utils.isInternetOn(context)) {
            mTwitterAuthClient.authorize(context, new Callback<TwitterSession>() {

                @Override
                public void success(Result<TwitterSession> twitterSessionResult) {
                    Log.d(TAG, " mTwitterAuthClient success: ");
                    Call<User> userCall = Twitter.getApiClient().getAccountService().verifyCredentials(true, false);
                    userCall.enqueue(new Callback<User>() {

                        @Override
                        public void success(Result<User> result) {
                            User user = result.data;
                            String[] userName = user.name.split(" ");

                            map.put("fname", userName[0]);
                            if (userName.length > 1 && userName[1] != null) {
                                map.put("lname", userName[1]);
                            } else {
                                map.put("lname", result.data.screenName);
                                preferences.setUsername(result.data.screenName);
                            }
                            map.put("twitterid", "" + user.getId());
                            callSocialLogin();
                        }

                        @Override
                        public void failure(TwitterException exception) {
                            exception.printStackTrace();
                            Utils.showToast(context, context.getResources().getString(R.string.toast_login_fail));
                        }
                    });

                    Call<FollowersList> iq = new MyTwitterApiClient(twitterSessionResult.data).getCustomService().show();
                    iq.enqueue(new retrofit2.Callback<FollowersList>() {
                        @Override
                        public void onResponse(Call<FollowersList> call, Response<FollowersList> response) {
                            StringBuffer buffer = new StringBuffer();
                            try {
                                for (int i = 0; i < response.body().getUsers().size(); i++) {
                                    buffer.append(String.valueOf(response.body().getUsers().get(i).getId()));
                                    if ((i + 1) != response.body().getUsers().size()) {
                                        buffer.append(",");
                                    }
                                }
                            } catch (Exception e) {
                                buffer = new StringBuffer();
                            }
                        }

                        @Override
                        public void onFailure(Call<FollowersList> call, Throwable t) {

                        }
                    });
                    // Success
                }

                @Override
                public void failure(TwitterException e) {
                    Log.d(TAG, "mTwitterAuthClient failure: ");
                    e.printStackTrace();
                    Utils.showToast(context, context.getResources().getString(R.string.toast_login_fail));
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void onInstaClick(View view) {

        if (Utils.isInternetOn(context)) {
            instagramHelper = new InstagramHelper.Builder()
                    .withClientId(BuildConfig.INSTAGRAMCLIENID)
                    .withRedirectUrl(BuildConfig.INSTAGRAMREDIRECTURL)
                    .withScope("follower_list")
                    .build();
            instagramHelper.loginFromActivity(this);

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void onClickSignin(View view) {
        Utils.hideKeyboard(context);
        String userName = medtUserName.getText().toString().trim();
        String password = medtPassword.getText().toString();

        if (isValidation()) {
            map.put("uname", userName);
            map.put("username", userName);
            map.put("password", password);
            callLogin();
        }
    }

    void callSocialLogin() {
        Log.d(TAG, "callSocialLogin: ");
        String uniqueid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        map.put("uniquestring", uniqueid);
        map.put("devicetype", "android");
        map.put("devicetoken", FirebaseInstanceId.getInstance().getToken());

        if (Utils.isInternetOn(context)) {
            setViewEnableDisable(false);
            progressbar.setVisibility(View.VISIBLE);
            loginBeanCall = mService.getFetcherService(context).SocialLoginUser(map);
            loginBeanCall.enqueue(new retrofit2.Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    setViewEnableDisable(true);
                    progressbar.setVisibility(View.GONE);
                    switch (response.code()) {
                        case StaticConstant.RESULT_OK:
                            preferences.saveLoginUser(response.body().getResult(), response.body().getToken());
//                            if (response.body().getResult().getStripeCustomerId() == null || response.body().getResult().getStripeCustomerId().isEmpty()) {
//                                createStripeCustomer();
//                            }

                            if (response.body().getResult().getUserStatus() != null) {
                                if (response.body().getResult().getUserStatus().equals("0")) {
                                    Intent intent = new Intent(context, EmailVerification.class);
                                    intent.putExtra("screen", "");
                                    startActivity(intent);
                                }
                            } else {
                                Intent intent = new Intent(context, DrawerMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                            finish();

                            break;
                        case StaticConstant.UNAUTHORIZE:
                            Utils.unAuthentication(context);
                            break;
                        case StaticConstant.BAD_REQUEST:
                            Utils.showToast(context, getString(R.string.toast_error_something_went_wrong));
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    t.printStackTrace();
                    setViewEnableDisable(true);
                    progressbar.setVisibility(View.GONE);
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    void callLogin() {
        String uniqueid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        map.put("uniquestring", uniqueid);
        map.put("devicetype", "android");
        map.put("devicetoken", FirebaseInstanceId.getInstance().getToken());
//        RequestBodyConveter requestBodyConveter = new RequestBodyConveter();
        if (Utils.isInternetOn(context)) {
            setViewEnableDisable(false);
            progressbar.setVisibility(View.VISIBLE);
            loginBeanCall = mService.getFetcherService(context).LoginUser(map);
            loginBeanCall.enqueue(new retrofit2.Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    progressbar.setVisibility(View.GONE);
                    setViewEnableDisable(true);

                    switch (response.code()) {
                        case StaticConstant.RESULT_OK:
                            if (response.body().getResult().getUserStatus() != null) {
                                if (response.body().getResult().getUserStatus().equals("0")) {
                                    preferences.setUserid("" + response.body().getResult().getId());
                                    Intent intent = new Intent(context, EmailVerification.class);
                                    startActivity(intent);
                                }
                            } else {
                                preferences.saveLoginUser(response.body().getResult(), response.body().getToken());
                                if (response.body().getResult().getStripeCustomerId() == null || response.body().getResult().getStripeCustomerId().isEmpty()) {
                                    createStripeCustomer();
                                } else {
                                    Intent intent = new Intent(context, DrawerMainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            break;
                        case StaticConstant.UNAUTHORIZE:
                            Utils.unAuthentication(context);
                            break;
                        case StaticConstant.BAD_REQUEST:
                            Utils.showToast(context, getString(R.string.toast_error_something_went_wrong));
                            break;
                        default:
                            break;
                    }

                }

                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {
                    t.printStackTrace();
                    setViewEnableDisable(true);
                    progressbar.setVisibility(View.GONE);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void createStripeCustomer() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("email", preferences.getUserEmail());
        map.put("description", preferences.getUserName());  // need to put username

        if (Utils.isInternetOn(context)) {
            createStripeCustomerCall = mService.getFetcherService(context).createStripeCustomer(map);
            createStripeCustomerCall.enqueue(new retrofit2.Callback<StripeCreateCustomerReponse>() {
                @Override
                public void onResponse(Call<StripeCreateCustomerReponse> call, Response<StripeCreateCustomerReponse> response) {
                    progressbar.setVisibility(View.GONE);
                    setViewEnableDisable(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
//                            preferences.setStripeCustomerId(response.body().getResult().getStripeCustomerId());
//                            Intent intent = new Intent(context, EditProfileActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            intent.putExtra("loginScrean", true);
//                            startActivity(intent);
                        } else {
//                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeCreateCustomerReponse> call, Throwable t) {
                    t.printStackTrace();
                    progressbar.setVisibility(View.GONE);
                    setViewEnableDisable(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @OnClick({R.id.txt_forgot_password, R.id.txt_new_sign_up})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txt_forgot_password:
                Intent intent = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.txt_new_sign_up:
                Intent i = new Intent(this, SignupActivity.class);
                startActivity(i);
                break;
        }
    }

    void setViewEnableDisable(Boolean enable) {
        imgFacebook.setEnabled(enable);
        imgInstagram.setEnabled(enable);
        imgTwitter.setEnabled(enable);
        btnSignin.setEnabled(enable);
    }

    public boolean isValidation() {
        if (medtUserName.getText().toString().trim().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_username_null), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (medtPassword.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_password_null), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (createStripeCustomerCall != null) {
            createStripeCustomerCall.cancel();
        }
        if (loginBeanCall != null) {
            loginBeanCall.cancel();
        }
    }
}
