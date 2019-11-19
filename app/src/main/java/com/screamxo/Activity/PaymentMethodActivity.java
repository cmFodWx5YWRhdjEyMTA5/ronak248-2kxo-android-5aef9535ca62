package com.screamxo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.PayPalBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodActivity extends AppCompatActivity {

    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    @BindView(R.id.img_paypal)
    ImageView imgPaypal;

    String itemid, itemquantity, itemTotalCost;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.webview_paypal)
    WebView webviewPaypal;
    private FetchrServiceBase mService;
    Call<PayPalBean> paypalbeanCall;
    private Preferences preference;
    private Context context;
    private String paymentId, shippingAdress = "";
    Boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        ButterKnife.bind(this);
        init();
        initControlValue();
        initControl();
    }

    public void init() {

        context = this;
        Map<String, String> map = new HashMap<>();
        preference = new Preferences(context);
        mService = new FetchrServiceBase();

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("itemid") && getIntent().getExtras().containsKey("itemquantity")) {
            itemid = getIntent().getExtras().getString("itemid");
            itemquantity = getIntent().getExtras().getString("itemquantity");
            shippingAdress = getIntent().getExtras().getString("shippingAdress");
        }
    }

    public void initControlValue() {
        txtToolbarTitle.setText(R.string.title_payment_method);
        imgToolbarLeftIcon.setImageResource(R.mipmap.ico_up);
        imgToolbarLeftIcon.setRotation(-90);
        imgToolbarRightIcon.setVisibility(View.GONE);
        webviewPaypal.setVisibility(View.GONE);

    }

    public void initControl() {
        imgToolbarLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void onClickPaypal(View view) {


        Map<String, String> map = new HashMap<>();

        map.put("uid", preference.getUserId());
        map.put("itemid", itemid);
        map.put("productqty", itemquantity);

        if (Utils.isInternetOn(context)) {

            progreessbar.setVisibility(View.VISIBLE);
            imgPaypal.setEnabled(false);

            paypalbeanCall = mService.getFetcherService(context).PayPalPayment(map);
            paypalbeanCall.enqueue(new Callback<PayPalBean>() {
                @Override
                public void onResponse(Call<PayPalBean> call, Response<PayPalBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    imgPaypal.setEnabled(true);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            paymentId = response.body().getResult().getPayKey();
                            webviewPaypal.setVisibility(View.VISIBLE);
                            txtToolbarTitle.setText(R.string.title_payment_process);
                            webviewPaypal.loadUrl(response.body().getResult().getRedirectURL());
                            webviewPaypal.getSettings().setJavaScriptEnabled(true);
                            webviewPaypal.setWebViewClient(new WebViewClient() {
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    Log.e("Url", url);
                                    if (url.contains("execution=e2s2")) {
                                        webviewPaypal.setVisibility(View.GONE);
                                        callSuccessPaymentApi();
                                        return true;
                                    } else {
                                        return super.shouldOverrideUrlLoading(view, url);
                                    }
                                }
                            });
                        } else {
                            webviewPaypal.setVisibility(View.GONE);
                        }
                        Utils.showToast(context, response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<PayPalBean> call, Throwable t) {    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    imgPaypal.setEnabled(true);
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callSuccessPaymentApi() {

        Map<String, String> map = new HashMap<>();

        map.put("uid", preference.getUserId());
        map.put("paymentkey", paymentId);
        map.put("productqty", itemquantity);
        map.put("shipping", shippingAdress);

        if (Utils.isInternetOn(context)) {

            progreessbar.setVisibility(View.VISIBLE);
            imgPaypal.setEnabled(false);

            paypalbeanCall = mService.getFetcherService(context).FinalPaymentDetail(map);
            paypalbeanCall.enqueue(new Callback<PayPalBean>() {
                @Override
                public void onResponse(Call<PayPalBean> call, Response<PayPalBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    imgPaypal.setEnabled(true);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            isUpdate = true;
                            onBackPressed();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PayPalBean> call, Throwable t) {    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    imgPaypal.setEnabled(true);
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void onBackPressed() {
        Intent returnintent = new Intent();
        returnintent.putExtra("update", isUpdate);
        setResult(RESULT_OK, returnintent);
        finish();
    }

}
