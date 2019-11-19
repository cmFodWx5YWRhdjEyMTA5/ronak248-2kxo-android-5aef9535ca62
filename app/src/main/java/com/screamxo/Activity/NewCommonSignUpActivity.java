package com.screamxo.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.LoginBean;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewCommonSignUpActivity extends AppCompatActivity implements FacebookInterface {

    private static final int FACEBOOK_REQUEST_CODE = 64206;
    private static final int TWITTER_REQUEST_CODE = 140;
    @BindView(R.id.progreessbar)
    ProgressBar progressbar;
    private Call<LoginBean> loginBeanCall;
    private Preferences preferences;
    private TwitterAuthClient mTwitterAuthClient;
    private InstagramHelper instagramHelper;
    private FaceBookLogin faceBookLogin;
    private HashMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_common_sign_up);
        ButterKnife.bind(this);
        init();
    }

    @OnClick({R.id.title_signup, R.id.frame_email, R.id.frame_facebook, R.id.frame_twitter, R.id.frame_insta})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_signup:
                onBackPressed();
                break;
            case R.id.frame_email:
                Intent intent = new Intent(NewCommonSignUpActivity.this, SignupActivity.class);
                startActivity(intent);
                break;
            case R.id.frame_facebook:
                if (Utils.isInternetOn(NewCommonSignUpActivity.this)) {
                    faceBookLogin.faceBookManager(NewCommonSignUpActivity.this);
                } else {
                    Utils.showToast(NewCommonSignUpActivity.this, getString(R.string.toast_no_internet));
                }
                break;
            case R.id.frame_insta:
                if (Utils.isInternetOn(NewCommonSignUpActivity.this)) {
                    instagramHelper = new InstagramHelper.Builder()
                            .withClientId(BuildConfig.INSTAGRAMCLIENID)
                            .withRedirectUrl(BuildConfig.INSTAGRAMREDIRECTURL)
                            .withScope("follower_list")
                            .build();
                    instagramHelper.loginFromActivity(NewCommonSignUpActivity.this);

                } else {
                    Utils.showToast(NewCommonSignUpActivity.this, getString(R.string.toast_no_internet));
                }
                break;
            case R.id.frame_twitter:
                loginViaTwitter();
                break;
        }
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
            Utils.showToast(NewCommonSignUpActivity.this, getString(R.string.toast_login_fail));
        }
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
    protected void onDestroy() {
        if (loginBeanCall != null) {
            loginBeanCall.cancel();
        }
        super.onDestroy();
    }

    public void onBackPressed() {
        finish();
    }

    private void init() {
        faceBookLogin = new FaceBookLogin(NewCommonSignUpActivity.this, this);
        map = new HashMap<>();
        preferences = new Preferences(NewCommonSignUpActivity.this);
        preferences.clearAllApreferences();
    }

    public void loginViaTwitter() {
        mTwitterAuthClient = new TwitterAuthClient();
        if (Utils.isInternetOn(NewCommonSignUpActivity.this)) {
            mTwitterAuthClient.authorize(NewCommonSignUpActivity.this, new Callback<TwitterSession>() {

                @Override
                public void success(Result<TwitterSession> twitterSessionResult) {
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
                            Utils.showToast(NewCommonSignUpActivity.this, getString(R.string.toast_login_fail));
                        }
                    });
                }

                @Override
                public void failure(TwitterException e) {
                    e.printStackTrace();
                    Utils.showToast(NewCommonSignUpActivity.this, getResources().getString(R.string.toast_login_fail));
                }
            });
        } else {
            Utils.showToast(NewCommonSignUpActivity.this, getString(R.string.toast_no_internet));
        }
    }

    private void callSocialLogin() {
        @SuppressLint("HardwareIds")
        String uniqueid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        map.put("uniquestring", uniqueid);
        map.put("devicetype", "android");
        map.put("devicetoken", FirebaseInstanceId.getInstance().getToken());

        if (Utils.isInternetOn(NewCommonSignUpActivity.this)) {
            progressbar.setVisibility(View.VISIBLE);
            loginBeanCall = new FetchrServiceBase().getFetcherService(NewCommonSignUpActivity.this).SocialLoginUser(map);
            loginBeanCall.enqueue(new retrofit2.Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    progressbar.setVisibility(View.GONE);

                    switch (response.code()) {
                        case StaticConstant.RESULT_OK:

                            preferences.saveLoginUser(response.body().getResult(), response.body().getToken());
                            if (response.body().getResult().getIsverified() == 0 || !TextUtils.isEmpty(response.body().getResult().getEmail())
                                    || response.body().getResult().getPhoto()==null) {
                                Intent intent = new Intent(NewCommonSignUpActivity.this, EditProfileActivity.class);
                                intent.putExtra("screen", CommonLoginSignUpActivity.class.getSimpleName());
                                startActivity(intent);
                                finish();
                            } /*else if (response.body().getResult().getStripeCustomerId() == null || response.body().getResult().getStripeCustomerId().isEmpty()) {
                                createStripeCustomer();
                            }*/ else {
                                Intent intent = new Intent(NewCommonSignUpActivity.this, DrawerMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            break;
                        case StaticConstant.UNAUTHORIZE:
                            Utils.unAuthentication(NewCommonSignUpActivity.this);
                            break;
                        case StaticConstant.BAD_REQUEST:
                            Utils.showToast(NewCommonSignUpActivity.this, getString(R.string.toast_error_something_went_wrong));
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(NewCommonSignUpActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                    Utils.showToast(NewCommonSignUpActivity.this, t.toString());
                }
            });
        } else {
            Utils.showToast(NewCommonSignUpActivity.this, getString(R.string.toast_no_internet));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
