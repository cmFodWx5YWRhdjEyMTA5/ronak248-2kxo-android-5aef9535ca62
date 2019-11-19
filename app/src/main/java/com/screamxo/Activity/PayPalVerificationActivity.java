package com.screamxo.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.screamxo.R;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PayPalVerificationActivity extends AppCompatActivity {

    WebView webViewpaypal;
    private String url;
    private String payerId;
    private boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_verification);
        init();
    }

    private void init() {

        webViewpaypal = (WebView) findViewById(R.id.webview_paypal);
        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString("verificationLink");
        }

        webViewpaypal.setWebViewClient(new WebViewClient());
        webViewpaypal.getSettings().setJavaScriptEnabled(true);
        webViewpaypal.getSettings().setSupportZoom(true);       //Zoom Control on web (You don't need this

        webViewpaypal.getSettings().setBuiltInZoomControls(false); //Enable Multitouch if supported by ROM
        webViewpaypal.getSettings().setUseWideViewPort(true);
        webViewpaypal.getSettings().setLoadWithOverviewMode(false);

        webViewpaypal.loadUrl(url);
        webViewpaypal.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
                payerId = Uri.parse(request).getQueryParameter("PayerID");

                if (isFinish) {
                    Intent intent = new Intent();
                    intent.putExtra("payerId", payerId);
                    intent.putExtra("paypalsourceid", getIntent().getExtras().getString("paypalsourceid"));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (payerId != null){
                    isFinish = true;
                    Intent intent = new Intent();
                    intent.putExtra("payerId", payerId);
                    intent.putExtra("paypalsourceid", Uri.parse(url).getQueryParameter("paymentId"));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                super.onPageFinished(view, url);
            }
        });
    }
}
