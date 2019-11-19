package com.screamxo.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.GetAccountBean;
import com.example.apimodule.ApiBase.ApiBean.Payment.StripeToken;
import com.example.apimodule.ApiBase.ApiBean.SellItemBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.example.apimodule.ApiBase.WalletOrderPayment.FinalProcessResponse;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;
import com.screamxo.Activity.RahulWork.BoostCongActivity;
import com.screamxo.Activity.RahulWork.CardInformationActivity;
import com.screamxo.Activity.RahulWork.FriendsActivity;
import com.screamxo.Activity.RahulWork.UploadDataActivity;
import com.screamxo.Activity.wallet.WalletSendReceiveActivity;
import com.screamxo.PaypalCall.PaypalRetofitSeviceBase;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.RequestBodyConveter;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_WALLET_ACTIVITY_RESULTS;

@Deprecated
public class ConfigurePaymentActivity extends AppCompatActivity {

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    private static PayPalConfiguration config;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    @BindView(R.id.btn_continue)
    Button btnContinue;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.txt_email)
    TextView txtEmail;
    @BindView(R.id.txt_email_bitcoin)
    TextView txtEmailBitcoin;
    @BindView(R.id.txtCurrent)
    TextView txtCurrent;
    @BindView(R.id.txt_balance)
    TextView txt_balance;
    @BindView(R.id.linear_paypal)
    LinearLayout linearPaypal;
    @BindView(R.id.linear_PaymentMethod)
    LinearLayout linear_PaymentMethod;
    //    GetAccountResult getAccountResult;
    Call<GetAccountBean> getaccountbeancall;
    Call<SellItemBean> createitembeanCall;
    String authorization_code;
    String type[] = {"2", "2", "2", "2"};
    HashMap<String, String> map;
    HashMap<String, File> fileArray;
    String itemPrice = "", itemDay = "", itemId = "", reach = "", image = "", quantity = "", address = "", screen = "";
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private FetchrServiceBase mservice;
    private Preferences preferences;
    private boolean isUpdate = false;

    public static final int REQ_CODE_FRIENDS_ACTIVITY_RESULTS = 101;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    private static final String TAG = "ConfigurePaymentActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_payment);
        ButterKnife.bind(this);
        init();
        initControlValue();
//        CallGetAccountApi();
//        callGetSecretKeyClientId();
        initFabIcon();
    }

    public void init() {
        context = this;
        map = new HashMap<>();
        fileArray = new HashMap<>();
        mservice = new FetchrServiceBase();
        PaypalRetofitSeviceBase paypalRetofitSeviceBase = new PaypalRetofitSeviceBase();
        preferences = new Preferences(context);
        txt_balance.setText(preferences.getAmount());
    }

    public void initControlValue() {
        txtToolbarTitle.setText(R.string.title_configure_payment);
        imgToolbarLeftIcon.setImageResource(R.mipmap.ico_up);
        imgToolbarLeftIcon.setRotation(-90);
        imgToolbarRightIcon.setVisibility(View.GONE);

        if (getIntent().getExtras() != null) {
            if (getIntent().getStringExtra("screen").equalsIgnoreCase("BoostActivity")) {
                itemPrice = getIntent().getStringExtra("price");
                itemDay = getIntent().getStringExtra("day");
                itemId = getIntent().getStringExtra("itemid");
                reach = getIntent().getStringExtra("reach");
                image = getIntent().getStringExtra("image");
            } else if (getIntent().getStringExtra("screen").equalsIgnoreCase("paymentdetails")) {
                itemId = getIntent().getStringExtra("itemid");
                quantity = getIntent().getStringExtra("itemquantity");
                address = getIntent().getStringExtra("shippingAdress");
            } else if (getIntent().getStringExtra("screen").equalsIgnoreCase(com.screamxo.Activity.RahulWork.SellItemActivity.class.getSimpleName())) {
//                btnContinue.setVisibility(View.VISIBLE);
            } else {
                screen = getIntent().getStringExtra("screen");
            }
        }
    }

//    public void CallGetAccountApi() {
//        Map<String, String> map = new HashMap<>();
//        map.put("uid", preferences.getUserId());
//
//        if (Utils.isInternetOn(context)) {
//            progreessbar.setVisibility(View.VISIBLE);
//            btnContinue.setEnabled(false);
//
//            getaccountbeancall = mservice.getFetcherService(context).GetAccountList(map);
//            getaccountbeancall.enqueue(new Callback<GetAccountBean>() {
//                @Override
//                public void onResponse(Call<GetAccountBean> call, Response<GetAccountBean> response) {
//                    progreessbar.setVisibility(View.GONE);
//                    btnContinue.setEnabled(true);
//
//                    if (response.code() == StaticConstant.RESULT_OK) {
//                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
//                            getAccountResult = response.body().getResult();
//
//                            if (response.body().getResult().getPaypal() != null && !response.body().getResult().getPaypal().isEmpty()) {
//                                txtEmail.setText(response.body().getResult().getPaypal());
//                            } else {
//                                txtEmail.setText("Please Configure Paypal Account");
//                            }
//
//                            if (response.body().getResult().getBitcoin() != null && !response.body().getResult().getBitcoin().isEmpty()) {
//                                txtEmailBitcoin.setText(response.body().getResult().getBitcoin());
//                            } else {
//                                txtEmailBitcoin.setText("Please Configure Bitcoin Account");
//                            }
//
//                            if (getIntent().getExtras() != null) {
//                                if (getIntent().getStringExtra("screen").equalsIgnoreCase(com.screamxo.Activity.RahulWork.SellItemActivity.class.getSimpleName())) {
//                                    btnContinue.setVisibility(View.VISIBLE);
//                                }
//                            }
//
//                        } else {
//                            Utils.showToast(context, response.body().getMsg());
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GetAccountBean> call, Throwable t) {
//                    progreessbar.setVisibility(View.GONE);
//                    btnContinue.setEnabled(true);
//                    Utils.showToast(context, t.toString());
//                }
//            });
//        } else {
//            Utils.showToast(context, context.getString(R.string.toast_no_internet));
//        }
//    }

    private void initFabIcon() {
        floatingButton = findViewById(R.id.my_floating_button);
        floatingButton.setStartAngle(0)
                .setEndAngle(360)
                .setAnimationType(AnimationType.EXPAND)
                .setRadius(170)
                .setAnchored(false)
                .getAnimationHandler()
                .setOpeningAnimationDuration(500)
                .setClosingAnimationDuration(200)
                .setLagBetweenItems(0)
                .setOpeningInterpolator(new FastOutSlowInInterpolator())
                .setClosingInterpolator(new FastOutLinearInInterpolator())
                .shouldFade(true)
                .shouldScale(true)
                .shouldRotate(true);

        floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));

        floatingButton.setStateChangeListener(new FloatingMenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingMenuButton floatingMenuButton) {
                Log.d(TAG, "onMenuOpened: ");
                Utils.hideKeyboard(ConfigurePaymentActivity.this);
                floatingButton.setBackground(getResources().getDrawable(R.mipmap.cross));
            }

            @Override
            public void onMenuClosed(FloatingMenuButton floatingMenuButton) {
                floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
            }
        });

        sbProfile = findViewById(R.id.sbProfile);
        sbProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingButton.closeMenu();
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 6);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    gotoLogin();
                }
            }
        });

        sbSocial = findViewById(R.id.sbSocial);
        sbSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingButton.closeMenu();
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 3);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    gotoLogin();
                }
            }
        });

        sbChat = findViewById(R.id.sbChat);
        sbChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingButton.closeMenu();
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    Intent gotoNext = new Intent(ConfigurePaymentActivity.this, UploadDataActivity.class);
                    startActivityForResult(gotoNext, REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS);
                } else {
                    gotoLogin();
                }

            }
        });

        sbWorld = findViewById(R.id.sbWorld);
        sbWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingButton.closeMenu();
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    startActivityForResult(new Intent(ConfigurePaymentActivity.this, WalletSendReceiveActivity.class), REQ_CODE_WALLET_ACTIVITY_RESULTS);
                    finish();
                } else {
                    gotoLogin();
                }
            }
        });

        sbSearch = findViewById(R.id.sbSearch);
        sbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingButton.closeMenu();
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {

                } else {
                    gotoLogin();
                }
            }
        });

        sbflSetting = findViewById(R.id.sbflSetting);
        sbflSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingButton.closeMenu();
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 7);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    gotoLogin();
                }

            }
        });

        subFriend = findViewById(R.id.subFriend);
        subFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingButton.closeMenu();
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    Intent gotoNext = new Intent(ConfigurePaymentActivity.this, FriendsActivity.class);
                    startActivityForResult(gotoNext, REQ_CODE_FRIENDS_ACTIVITY_RESULTS);
                    finish();
                } else {
                    gotoLogin();
                }
            }
        });

        sbflHome = findViewById(R.id.sbflHome);
        sbflHome.setOnClickListener(view -> {
            floatingButton.closeMenu();
            if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", 1);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                gotoLogin();
            }
        });
    }

    public void gotoLogin() {
        Intent gotoLogin = new Intent(this, CommonLoginSignUpActivity.class);
        gotoLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(gotoLogin);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("FRAGMENT_INDEX")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", data.getExtras().getInt("FRAGMENT_INDEX"));
                setResult(RESULT_OK, returnIntent);
                finish();
                return;
            }
        }

//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case 3:
//                    PayPalAuthorization auth =
//                            data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
//                    if (auth != null) {
//                        try {
//                            Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));
//                            authorization_code = auth.getAuthorizationCode();
//                            callGetRefreshToken();
//                            Log.i("ProfileSharingExample", authorization_code);
//
//                        } catch (JSONException e) {
//                            Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
//                        }
//                    }
//                    break;
//
//                case StaticConstant.REQUEST_SELL_ITEM:
//                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("POST_ANOTHER")) {
//                        Intent intent = new Intent(this, com.screamxo.Activity.RahulWork.SellItemActivity.class);
//                        startActivityForResult(intent, StaticConstant.REQUEST_SELL_ITEM);
//                        return;
//                    }
//                    break;
//            }
//        } else if (resultCode == Activity.RESULT_CANCELED) {
//            Log.i("paypal", "The user canceled.");
//        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//            Log.i("paypal", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
//        }

    }

//    private void callGetSecretKeyClientId() {
//
//        linearPaypal.setEnabled(false);
//        Map<String, String> map = new HashMap<>();
//        map.put("uid", preferences.getUserId());
//
//        if (Utils.isInternetOn(context)) {
//
//            progreessbar.setVisibility(View.VISIBLE);
//            btnContinue.setEnabled(false);
//
//            getaccountbeancall = mservice.getFetcherService(context).GetPaypalDetail(map);
//
//            getaccountbeancall.enqueue(new Callback<GetAccountBean>() {
//                @Override
//                public void onResponse(Call<GetAccountBean> call, Response<GetAccountBean> response) {
//                    linearPaypal.setEnabled(true);
//                    progreessbar.setVisibility(View.GONE);
//                    btnContinue.setEnabled(true);
//
//                    if (response.code() == StaticConstant.RESULT_OK) {
//                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
//                            CONFIG_CLIENT_ID = response.body().getResult().getPaypalApi();
//                            StaticConstant.SECRET_KEY = response.body().getResult().getPaypalSecret();
//
//                            Log.e("SecretKey: " + response.body().getResult().getPaypalSecret(), " CLientId: " + response.body().getResult().getPaypalApi());
//
//                            config = new PayPalConfiguration()
//                                    .environment(CONFIG_ENVIRONMENT)
//                                    .clientId("AYuIo-X4PK27s5RernB7kkIKAhsqjI3IsnX3B-Zp9utG3LH3IaIY4Swqz5si23ErYjRXGe2OC_zukrrd")
//                                    // The following are only used in PayPalFuturePaymentActivity.
//                                    .merchantName("hiren panchal")
//                                    .merchantPrivacyPolicyUri(Uri.parse("https://developer.paypal.com/developer"))
//                                    .merchantUserAgreementUri(Uri.parse("https://developer.paypal.com/developer"));
//
//                            Intent intent = new Intent(ConfigurePaymentActivity.this, PayPalService.class);
//                            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                            startService(intent);
//
//                        } else {
//                            Utils.showToast(context, response.body().getMsg());
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GetAccountBean> call, Throwable t) {
//                    linearPaypal.setEnabled(true);
//                    progreessbar.setVisibility(View.GONE);
//                    btnContinue.setEnabled(true);
//                    Utils.showToast(context, t.toString());
//                }
//            });
//        } else {
//            Utils.showToast(context, context.getString(R.string.toast_no_internet));
//        }
//    }

//    private void callGetRefreshToken() {
//
//        String text = "AYuIo-X4PK27s5RernB7kkIKAhsqjI3IsnX3B-Zp9utG3LH3IaIY4Swqz5si23ErYjRXGe2OC_zukrrd" + ":" + "ECXLfKPL8WfAsrSiZtN-mrUJ9FiImJHzGKQsZwWRsBBvpeuIulH2klkV09QfabGunX10q98VUlDjg0eY";
//        Log.e(StaticConstant.SECRET_KEY, StaticConstant.CONFIG_CLIENT_ID);
//
//        String base64 = "Basic " + HttpRequest.Base64.encode(text);
//        base64 = base64.trim();
//
//        Map<String, String> map = new HashMap<>();
//        map.put("grant_type", "authorization_code");
//        map.put("code", authorization_code);
//
//        paypalRetofitSeviceBase.getFetcherService(context, base64, "application/x-www-form-urlencoded").getRefreskToken(map).enqueue(new Callback<PaypalAccessTokenBean>() {
//            @Override
//            public void onResponse(Call<PaypalAccessTokenBean> call, Response<PaypalAccessTokenBean> response) {
//                callGetRefreshAccessToken(response.body().getRefreshToken());
//            }
//
//            @Override
//            public void onFailure(Call<PaypalAccessTokenBean> call, Throwable t) {
//
//            }
//        });
//    }

//    private void callGetRefreshAccessToken(String refreshToken) {
//        String text = "AYuIo-X4PK27s5RernB7kkIKAhsqjI3IsnX3B-Zp9utG3LH3IaIY4Swqz5si23ErYjRXGe2OC_zukrrd" + ":" + "ECXLfKPL8WfAsrSiZtN-mrUJ9FiImJHzGKQsZwWRsBBvpeuIulH2klkV09QfabGunX10q98VUlDjg0eY";
//        Log.e(StaticConstant.SECRET_KEY, StaticConstant.CONFIG_CLIENT_ID);
//
//        String base64 = "Basic " + HttpRequest.Base64.encode(text);
//        base64 = base64.trim();
//
//        Map<String, String> map = new HashMap<>();
//        map.put("grant_type", "refresh_token");
//        map.put("refresh_token", refreshToken);
//        Log.e("Header:Paypal", base64);
//        paypalRetofitSeviceBase.getFetcherService(context, base64, "application/x-www-form-urlencoded").getRefreskToken(map).enqueue(new Callback<PaypalAccessTokenBean>() {
//            @Override
//            public void onResponse(Call<PaypalAccessTokenBean> call, Response<PaypalAccessTokenBean> response) {
//
//
//                callGetUserInfo(response.body().getAccessToken());
//            }
//
//            @Override
//            public void onFailure(Call<PaypalAccessTokenBean> call, Throwable t) {
//
//            }
//        });
//
//    }

//    private void callGetUserInfo(String accessToken) {
//        Map<String, String> map = new HashMap<>();
//        map.put("grant_type", "refresh_token");
//        map.put("refresh_token", accessToken);
//
//        String base64 = "Bearer " + accessToken;
//        base64 = base64.trim();
//
//        paypalRetofitSeviceBase.getFetcherService(context, base64, "application/json").getUserEmail().enqueue(new Callback<PaypalUserInfo>() {
//            @Override
//            public void onResponse(Call<PaypalUserInfo> call, Response<PaypalUserInfo> response) {
//                callAddPaypalEmail(response.body().getEmail());
//            }
//
//            @Override
//            public void onFailure(Call<PaypalUserInfo> call, Throwable t) {
//
//            }
//        });
//
//    }

//    private void callAddPaypalEmail(String emailId) {
//        Map<String, String> map = new HashMap<>();
//        map.put("uid", preferences.getUserId());
//        map.put("paypal", emailId);
//
//        if (Utils.isInternetOn(context)) {
//
//            progreessbar.setVisibility(View.VISIBLE);
//            btnContinue.setEnabled(false);
//
//            getaccountbeancall = mservice.getFetcherService(context).PostPayPalUserEmail(map);
//
//            getaccountbeancall.enqueue(new Callback<GetAccountBean>() {
//                @Override
//                public void onResponse(Call<GetAccountBean> call, Response<GetAccountBean> response) {
//                    progreessbar.setVisibility(View.GONE);
//                    btnContinue.setEnabled(true);
//
//                    if (response.code() == StaticConstant.RESULT_OK) {
//                        Utils.showToast(context, response.body().getMsg());
//                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
//                            txtEmail.setText(emailId);
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GetAccountBean> call, Throwable t) {
//                    progreessbar.setVisibility(View.GONE);
//                    btnContinue.setEnabled(true);
//                    Utils.showToast(context, t.toString());
//                }
//            });
//        } else {
//            Utils.showToast(context, context.getString(R.string.toast_no_internet));
//        }
//
//
//    }

    @OnClick({R.id.linear_paypal, R.id.linear_bitcoin, R.id.img_toolbar_left_icon, R.id.btn_continue, R.id.linear_PaymentMethod})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_paypal:
                //     configurePayPal();
                break;
            case R.id.linear_bitcoin:
                break;
            case R.id.img_toolbar_left_icon:
                onBackPressed();
                break;
            case R.id.btn_continue:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.txt_confirm))
                        .setMessage(getString(R.string.msg_continue_with_above_account))
                        .setPositiveButton(getString(R.string.txt_agree), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callCreateItemApi();
                            }
                        }).setNegativeButton(getString(R.string.txt_cancel), null).show();
//                if (getAccountResult.getPaypal() != null && !getAccountResult.getPaypal().isEmpty())
//                    callCreateItemApi();
//                else {
//                    Toast.makeText(context, "Please configure alteast one account before proceed!", Toast.LENGTH_LONG).show();
//                }
                break;
            case R.id.linear_PaymentMethod:
                Intent gotoNext = new Intent(ConfigurePaymentActivity.this, CardInformationActivity.class);

                if (getIntent().getStringExtra("screen").equalsIgnoreCase("BoostActivity")) {
                    gotoNext.putExtra("price", itemPrice);
                    gotoNext.putExtra("day", itemDay);
                    gotoNext.putExtra("itemid", itemId);
                    gotoNext.putExtra("reach", reach);
                    gotoNext.putExtra("image", image);
                    gotoNext.putExtra("screen", "boost");


                } else if (getIntent().getStringExtra("screen").equalsIgnoreCase("paymentdetails")) {
                    gotoNext.putExtra("itemquantity", quantity);
                    gotoNext.putExtra("shippingAdress", address);
                    gotoNext.putExtra("itemid", itemId);
                    gotoNext.putExtra("screen", "paymentdetails");
                }

                gotoNext.putExtra("amount", "noamount");
                gotoNext.putExtra("screen", screen);
                startActivity(gotoNext);
                finish();
                break;
        }
    }

    private void callCreateItemApi() {
        if (Utils.isInternetOn(context)) {

            map = (HashMap<String, String>) getIntent().getSerializableExtra("mapString");
            fileArray = (HashMap<String, File>) getIntent().getSerializableExtra("fileArraymap");

            RequestBodyConveter requestbodyconverter = new RequestBodyConveter();
            progreessbar.setVisibility(View.VISIBLE);
            btnContinue.setEnabled(false);
            createitembeanCall = mservice.getFetcherService(context).CreateItem(requestbodyconverter.converRequestBodyFromMap(map), requestbodyconverter.converRequestBodyFromMapImage(fileArray, type));

            createitembeanCall.enqueue(new Callback<SellItemBean>() {
                @Override
                public void onResponse(Call<SellItemBean> call, Response<SellItemBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            Utils.showToast(context, response.body().getMsg());
                            isUpdate = true;
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("update", isUpdate);
                            returnIntent.putExtra("itemid", response.body().getResult().getItemid());
                            returnIntent.putExtra("item_name", response.body().getResult().getItem_name());
                            returnIntent.putExtra("item_image", response.body().getResult().getItem_image());
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<SellItemBean> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
                    Utils.showToast(context, t.toString());
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("update", isUpdate);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(context, PayPalService.class));
        super.onDestroy();
    }

    public void funWallet(View view) {
        if (getIntent().getStringExtra("screen").equalsIgnoreCase("paymentdetails")) {
            Map<String, String> map = new HashMap<>();
            map.put("amount", itemPrice);
            map.put("wallet_amount", "" + preferences.getAmount());
            map.put("shipping", "" + address);
            map.put("productqty", "" + quantity);
            map.put("itemid", "" + itemId);
            map.put("uid", "" + preferences.getUserId());

            if (Utils.isInternetOn(context)) {
                progreessbar.setVisibility(View.VISIBLE);
                mservice.getFetcherService(context).getWalletPay(map).enqueue(new Callback<FinalProcessResponse>() {
                    @Override
                    public void onResponse(Call<FinalProcessResponse> call, Response<FinalProcessResponse> response) {
                        //   setViewEnableDisable(true);
                        progreessbar.setVisibility(View.GONE);
                        if (response.code() == StaticConstant.RESULT_OK) {
                            if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                                preferences.saveAmount(String.valueOf(response.body().getResult().getAmount()));
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("itemid", response.body().getResult().getItemid());
                                returnIntent.putExtra("item_name", response.body().getResult().getItemname());
                                returnIntent.putExtra("item_image", response.body().getResult().getItemimage());
                                setResult(RESULT_OK, returnIntent);
                                finish();
                            } else {
                                Utils.showToast(context, response.body().getMsg());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FinalProcessResponse> call, Throwable t) {

                        progreessbar.setVisibility(View.GONE);
                    }
                });

            } else {
                Utils.showToast(context, context.getString(R.string.toast_no_internet));
            }

        } else if (getIntent().getStringExtra("screen").equalsIgnoreCase("BoostActivity")) {
            Map<String, String> map = new HashMap<>();
            map.put("amount", itemPrice);
            map.put("no_days", "" + itemDay);
            map.put("no_users", "" + reach);
            map.put("wallet_amount", "" + preferences.getAmount());
            map.put("uid", "" + preferences.getUserId());
            map.put("itemid", "" + itemId);
            map.put("boost_type", "" + "2");

            if (Utils.isInternetOn(context)) {
                progreessbar.setVisibility(View.VISIBLE);
                Call<StripeToken> calRedeem = mservice.getFetcherService(context).finalBoostProcess(map);
                calRedeem.enqueue(new Callback<StripeToken>() {
                    @Override
                    public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                        //   setViewEnableDisable(true);
                        progreessbar.setVisibility(View.GONE);
                        if (response.code() == StaticConstant.RESULT_OK) {
                            if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                                Intent gotoCOngrats = new Intent(ConfigurePaymentActivity.this, BoostCongActivity.class);
                                gotoCOngrats.putExtra("image", image);
                                startActivity(gotoCOngrats);
                                finish();
                            } else {
                                Utils.showToast(context, response.body().getMsg());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StripeToken> call, Throwable t) {

                        progreessbar.setVisibility(View.GONE);
                    }
                });

            } else {
                Utils.showToast(context, context.getString(R.string.toast_no_internet));
            }
        }
    }
}
