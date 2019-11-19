package com.screamxo.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import retrofit2.Call;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EmailLoginActivity extends AppCompatActivity implements FacebookInterface {
    private static final String TAG = EmailLoginActivity.class.getSimpleName();
    private static final int FACEBOOK_REQUEST_CODE = 64206;
    private static final int TWITTER_REQUEST_CODE = 140;
    @BindView(R.id.et_user_name)
    EditText et_user_name;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.tv_signin)
    TextView tv_signin;
    @BindView(R.id.progreessbar)
    ProgressBar progressbar;
    @BindView(R.id.txt_back)
    TextView txtBack;
    @BindView(R.id.txt_forgot_password)
    TextView txtForgotPassword;
    @BindView(R.id.frame_facebook)
    ImageView frame_facebook;
    @BindView(R.id.frame_twitter)
    ImageView frame_twitter;
    @BindView(R.id.frame_insta)
    ImageView frame_insta;
    Call<LoginBean> loginBeanCall;
    Call<StripeCreateCustomerReponse> createStripeCustomerCall;
    private EmailLoginActivity context;
    private HashMap<String, String> map;
    private FetchrServiceBase mService;
    private Preferences preferences;
    private FaceBookLogin faceBookLogin;
    private InstagramHelper instagramHelper;
    private TwitterAuthClient mTwitterAuthClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        ButterKnife.bind(this);
        init();
        initControl();
    }

    private void init() {
        context = this;
        preferences = new Preferences(context);
        mService = new FetchrServiceBase();
        map = new HashMap<>();
        faceBookLogin = new FaceBookLogin(context, this);
    }

    private void initControl() {
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(context);
                String userName = et_user_name.getText().toString().trim();
                String password = et_password.getText().toString();

                if (isValidation()) {
                    map.put("uname", userName);
                    map.put("username", userName);
                    map.put("password", password);
                    callLogin();
                }
            }
        });

        txtBack.setOnClickListener(view -> finish());

        txtForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(context, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        frame_facebook.setOnClickListener(view -> {
            if (Utils.isInternetOn(context)) {
                faceBookLogin.faceBookManager(context);
            } else {
                Utils.showToast(context, context.getString(R.string.toast_no_internet));
            }
        });

        frame_insta.setOnClickListener(view -> {
            if (Utils.isInternetOn(context)) {
                instagramHelper = new InstagramHelper.Builder()
                        .withClientId(BuildConfig.INSTAGRAMCLIENID)
                        .withRedirectUrl(BuildConfig.INSTAGRAMREDIRECTURL)
                        .withScope("follower_list")
                        .build();
                instagramHelper.loginFromActivity(context);

            } else {
                Utils.showToast(context, context.getString(R.string.toast_no_internet));
            }
        });

        frame_twitter.setOnClickListener(view -> {
            loginViaTwitter();
        });
    }

    public boolean isValidation() {
        if (et_user_name.getText().toString().trim().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_username_null), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (et_password.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_password_null), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else {
            return true;
        }
    }

    void callLogin() {
        @SuppressLint("HardwareIds") String uniqueid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        map.put("uniquestring", uniqueid);
        map.put("devicetype", "android");
        if (TextUtils.isEmpty(FirebaseInstanceId.getInstance().getToken())) {
            map.put("devicetoken", "123456");
        } else
            map.put("devicetoken", FirebaseInstanceId.getInstance().getToken());
        if (Utils.isInternetOn(context)) {
            controlUI(false);
            progressbar.setVisibility(View.VISIBLE);
            loginBeanCall = mService.getFetcherService(context).LoginUser(map);
            loginBeanCall.enqueue(new retrofit2.Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    controlUI(true);
                    progressbar.setVisibility(View.GONE);
                    switch (response.code()) {
                        case StaticConstant.RESULT_OK:
                            if (response.body().getResult().getUserStatus() != null) /* status 0 if not verified*/ {
                                if (response.body().getResult().getUserStatus().equals("0")) {
                                    preferences.setUserid("" + response.body().getResult().getId());
                                    Intent intent = new Intent(context, EmailVerification.class);
                                    intent.putExtra("screen", EmailLoginActivity.class.getSimpleName());
                                    startActivity(intent);
                                    finish();
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
                            Utils.showToast(context, "Invalid credentials");
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {
                    controlUI(true);
                    progressbar.setVisibility(View.GONE);
                    t.printStackTrace();
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
                    controlUI(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            preferences.setStripeCustomerId(response.body().getResult().getStripeCustomerId());
                            Intent intent = new Intent(context, DrawerMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeCreateCustomerReponse> call, Throwable t) {
                    t.printStackTrace();
                    progressbar.setVisibility(View.GONE);
                    controlUI(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
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

    private void controlUI(boolean enability) {
        tv_signin.setEnabled(enability);
        txtForgotPassword.setEnabled(enability);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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

        Utils.printIntentData(data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TWITTER_REQUEST_CODE:
                    if (mTwitterAuthClient != null) {
                        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
                    }
                    break;
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
                        map.put("lname", "nu");
                    }
                    map.put("googleid", user.getData().getId());
                    callSocialLogin();
                    break;
            }

        } else {
            Utils.showToast(context, context.getResources().getString(R.string.toast_login_fail));
        }
    }

    public void loginViaTwitter() {
        Log.d(TAG, "loginViaTwitter: ");
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
                            map.put("email", user.email != null ? user.email : "");
                            callSocialLogin();
                        }

                        @Override
                        public void failure(TwitterException exception) {
                            exception.printStackTrace();
                            Utils.showToast(context, context.getResources().getString(R.string.toast_login_fail));
                        }
                    });
                }

                @Override
                public void failure(TwitterException e) {
                    e.printStackTrace();
                    Utils.showToast(context, context.getResources().getString(R.string.toast_login_fail));
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    void callSocialLogin() {
        Log.d(TAG, "callSocialLogin: ");
        @SuppressLint("HardwareIds")
        String uniqueid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        map.put("uniquestring", uniqueid);
        map.put("devicetype", "android");
        map.put("devicetoken", FirebaseInstanceId.getInstance().getToken());
//        map.put("uniquestring", uniqueid);
        if (Utils.isInternetOn(context)) {
            progressbar.setVisibility(View.VISIBLE);
            controlUi(false);
            loginBeanCall = mService.getFetcherService(context).SocialLoginUser(map);
            loginBeanCall.enqueue(new retrofit2.Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    progressbar.setVisibility(View.GONE);
                    controlUi(true);

                    switch (response.code()) {
                        case StaticConstant.RESULT_OK:
                            preferences.saveLoginUser(response.body().getResult(), response.body().getToken());
                            if (response.body().getResult().getIsverified() == 0 || TextUtils.isEmpty(response.body().getResult().getEmail())) {
                                Intent intent = new Intent(context, EditProfileActivity.class);
                                intent.putExtra("screen", CommonLoginSignUpActivity.class.getSimpleName());
                                startActivity(intent);
                                finish();
                            } /*else if (response.body().getResult().getStripeCustomerId() == null || response.body().getResult().getStripeCustomerId().isEmpty()) {
                                createStripeCustomer();
                            }*/ else {
                                Intent intent = new Intent(context, DrawerMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
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
                    Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                    controlUi(true);
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void controlUi(boolean isEnable) {
        tv_signin.setEnabled(isEnable);
    }
}
