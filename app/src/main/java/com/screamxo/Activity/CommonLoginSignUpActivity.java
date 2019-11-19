package com.screamxo.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

@SuppressWarnings("unchecked")
public class CommonLoginSignUpActivity extends AppCompatActivity implements FacebookInterface {
    private static final String TAG = CommonLoginSignUpActivity.class.getSimpleName();

    private static final int FACEBOOK_REQUEST_CODE = 64206;
    private static final int TWITTER_REQUEST_CODE = 140;

    @BindView(R.id.iv_2kxo)
    ImageView iv_2kxo;
    @BindView(R.id.btn_login)
    TextView btn_login;
    @BindView(R.id.tv_signup)
    TextView tv_signup;
    @BindView(R.id.progreessbar)
    ProgressBar progressbar;
    Call<LoginBean> loginBeanCall;
    Call<StripeCreateCustomerReponse> createStripeCustomerCall;
    private CommonLoginSignUpActivity context;
    private Preferences preferences;
    private FetchrServiceBase mService;
    private FaceBookLogin faceBookLogin;
    private HashMap map;
    private TwitterAuthClient mTwitterAuthClient;
    private InstagramHelper instagramHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseInstanceId.getInstance().getToken();
        setContentView(R.layout.activity_common_login_sign_up);
        ButterKnife.bind(this);
        init();
        initControl();
    }

    private void init() {
        context = this;
        preferences = new Preferences(context);
        preferences.clearAllApreferences();
        mService = new FetchrServiceBase();
        faceBookLogin = new FaceBookLogin(context, this);
        map = new HashMap<>();
    }

    private void initControl() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EmailLoginActivity.class);
                startActivity(intent);
                // showLoginSignupOptions(true);
            }
        });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(context, NewCommonSignUpActivity.class);
                startActivity(intentSignUp);
                // showLoginSignupOptions(false);
            }
        });
    }

    private void showLoginSignupOptions(boolean isLogin) {
        @SuppressLint("InflateParams") View modalbottomsheet = getLayoutInflater().inflate(R.layout.bottom_sheet_login, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_facebook = modalbottomsheet.findViewById(R.id.tv_facebook);
        TextView tv_twitter = modalbottomsheet.findViewById(R.id.tv_twitter);
        TextView tv_instagram = modalbottomsheet.findViewById(R.id.tv_instagram);
        TextView tv_email = modalbottomsheet.findViewById(R.id.tv_email);
        TextView tv_cancel = modalbottomsheet.findViewById(R.id.tv_cancel);

        tv_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Utils.isInternetOn(context)) {
                    faceBookLogin.faceBookManager(context);
                } else {
                    Utils.showToast(context, context.getString(R.string.toast_no_internet));
                }

            }
        });

        tv_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                loginViaTwitter();
            }
        });
        tv_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

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
            }
        });

        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isLogin) {
                    Intent intent = new Intent(context, EmailLoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, SignupActivity.class);
                    startActivity(intent);
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
                            if (response.body().getResult().getIsverified() == 0 || !TextUtils.isEmpty(response.body().getResult().getEmail())) {
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

    private void createStripeCustomer() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("email", preferences.getUserEmail());
        map.put("description", preferences.getUserName());  // need to put username

        if (Utils.isInternetOn(context)) {
            progressbar.setVisibility(View.VISIBLE);
            controlUi(false);
            createStripeCustomerCall = mService.getFetcherService(context).createStripeCustomer(map);
            createStripeCustomerCall.enqueue(new retrofit2.Callback<StripeCreateCustomerReponse>() {
                @Override
                public void onResponse(Call<StripeCreateCustomerReponse> call, Response<StripeCreateCustomerReponse> response) {
                    progressbar.setVisibility(View.GONE);
                    controlUi(true);
                    switch (response.code()) {
                        case StaticConstant.RESULT_OK:
                            preferences.setStripeCustomerId(response.body().getResult().getStripeCustomerId());
                            Intent intent = new Intent(context, DrawerMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
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
                public void onFailure(Call<StripeCreateCustomerReponse> call, Throwable t) {
                    t.printStackTrace();
                    progressbar.setVisibility(View.GONE);
                    controlUi(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult requestCode: " + requestCode);
        Log.d(TAG, "onActivityResult resultCode: " + resultCode);

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

    @Override
    public void success(Map<String, String> map) {
        Log.d(TAG, "success: ");
        this.map.putAll(map);
        callSocialLogin();
    }

    @Override
    public void onFbFrdFetch(ArrayList<Object> list) {
        Log.d(TAG, "onFbFrdFetch: ");
    }

    @Override
    protected void onDestroy() {
        if (loginBeanCall != null) {
            loginBeanCall.cancel();
        }
        if (createStripeCustomerCall != null) {
            createStripeCustomerCall.cancel();
        }

        super.onDestroy();

    }

    public void controlUi(boolean isEnable) {
        btn_login.setEnabled(isEnable);
        tv_signup.setEnabled(isEnable);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
