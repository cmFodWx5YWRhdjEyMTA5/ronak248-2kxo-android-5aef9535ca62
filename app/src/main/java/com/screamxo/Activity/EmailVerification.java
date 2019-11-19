package com.screamxo.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.ApiBean.StripeCreateCustomer.StripeCreateCustomerReponse;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailVerification extends AppCompatActivity {
    private static final String TAG = "EmailVerification";
    Call<LoginBean> emailverificationcall;
    Call<StripeCreateCustomerReponse> createStripeCustomerCall;

    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    @BindView(R.id.edt_code)
    EditText edtCode;
    @BindView(R.id.btn_resend)
    Button btnResend;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.progreessbar)
    ProgressBar mprogreessbar;
    @BindView(R.id.txt_fixTimer)
    TextView txt_fixTimer;

    int count = 60;
    private Context context;
    private FetchrServiceBase mService;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        ButterKnife.bind(this);
        init();
        initTimer();
        initControlValue();
    }

    private void initTimer() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                count--;
                if (count == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_fixTimer.setVisibility(View.GONE);
                            timer.cancel();
                            timer.purge();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            txt_fixTimer.setText("Verification code will be deliver in Optional(" + count + ") Seconds");
                        }
                    });
                }


            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    public void init() {
        context = this;
        mService = new FetchrServiceBase();
        preferences = new Preferences(context);
    }

    public void initControlValue() {
        txtToolbarTitle.setText(R.string.title_verification);
        imgToolbarRightIcon.setVisibility(View.GONE);
    }

    public void onClickConfirm(View view) {

        Utils.hideKeyboard(this);
        String uniqueid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("vtoken", edtCode.getText().toString());
        map.put("uniquestring", uniqueid);

        if (Utils.isInternetOn(context)) {
            mprogreessbar.setVisibility(View.VISIBLE);
            btnConfirm.setEnabled(false);
            btnResend.setEnabled(false);

            emailverificationcall = mService.getFetcherService(context).EmailVerification(map);
            emailverificationcall.enqueue(new Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    Log.d(TAG, "onResponse: ");

                    if (response.code() == StaticConstant.RESULT_OK) {
                        mprogreessbar.setVisibility(View.GONE);
                        btnConfirm.setEnabled(true);
                        btnResend.setEnabled(true);
                        preferences.saveLoginUser(response.body().getResult(), response.body().getToken());

                        if (response.body().getResult().getPhoto() != null)
                            createStripeCustomer(true);
                        else
                            createStripeCustomer(false);

                    } else {
                        mprogreessbar.setVisibility(View.GONE);
                        btnConfirm.setEnabled(true);
                        btnResend.setEnabled(true);
                        DialogBox.showDialog(context, context.getString(R.string.app_name), "Something wrong!", DialogBox.DIALOG_FAILURE, null);
                    }
                }

                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {
                    t.printStackTrace();
                    mprogreessbar.setVisibility(View.GONE);
                    btnConfirm.setEnabled(true);
                    btnResend.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void onClickResend(final View view) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", preferences.getUserId());
        if (Utils.isInternetOn(context)) {

            mprogreessbar.setVisibility(View.VISIBLE);
            btnConfirm.setEnabled(false);
            btnResend.setEnabled(false);

            emailverificationcall = mService.getFetcherService(context).EmailVerificationAgain(map);
            emailverificationcall.enqueue(new Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                    }
                    mprogreessbar.setVisibility(View.GONE);
                    btnConfirm.setEnabled(true);
                    btnResend.setEnabled(true);
                }

                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {
                    t.printStackTrace();
                    mprogreessbar.setVisibility(View.GONE);
                    btnConfirm.setEnabled(true);
                    btnResend.setEnabled(true);
                    Utils.showToast(context, t.toString());
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void createStripeCustomer(Boolean hasPhoto) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("email", preferences.getUserEmail());
        map.put("description", preferences.getUserName());  // need to put username

        if (Utils.isInternetOn(context)) {
            createStripeCustomerCall = mService.getFetcherService(context).createStripeCustomer(map);
            createStripeCustomerCall.enqueue(new retrofit2.Callback<StripeCreateCustomerReponse>() {
                @Override
                public void onResponse(Call<StripeCreateCustomerReponse> call, Response<StripeCreateCustomerReponse> response) {
                    mprogreessbar.setVisibility(View.GONE);
                    btnConfirm.setEnabled(true);
                    btnResend.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            preferences.setStripeCustomerId(response.body().getResult().getStripeCustomerId());

                            if (!hasPhoto) {
                                Intent intent = new Intent(context, EditProfileActivity.class);
                                intent.putExtra("screen", EmailVerification.class.getSimpleName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                return;
                            }
                            if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(SignupActivity.class.getSimpleName())) {
                                Intent intent = new Intent(context, EditProfileActivity.class);
                                intent.putExtra("screen", EmailVerification.class.getSimpleName());

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                return;
                            }

                            if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(EmailLoginActivity.class.getSimpleName())) {
                                Intent intent = new Intent(context, EditProfileActivity.class);
                                intent.putExtra("screen", EmailLoginActivity.class.getSimpleName());
                                startActivity(intent);
                                finish();
                                return;
                            }

                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeCreateCustomerReponse> call, Throwable t) {
                    t.printStackTrace();
                    mprogreessbar.setVisibility(View.GONE);
                    btnConfirm.setEnabled(true);
                    btnResend.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @OnClick(R.id.img_toolbar_left_icon)
    public void onClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        if (createStripeCustomerCall != null) {
            createStripeCustomerCall.cancel();
        }

        if (emailverificationcall != null) {
            emailverificationcall.cancel();
        }

        super.onDestroy();
    }
}
