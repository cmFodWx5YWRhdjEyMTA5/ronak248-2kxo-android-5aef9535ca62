package com.screamxo.Activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.screamxo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TermsAndUseActivity extends AppCompatActivity {

    @BindView(R.id.web_terms_use)
    WebView web_terms_use;

    @BindView(R.id.txt_title)
    TextView txt_title;

    @BindView(R.id.ico_back)
    ImageView ico_back;

    private String loadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_use);
        ButterKnife.bind(this);
        getWindow().setStatusBarColor(Color.BLACK);
        init();
    }

    private void init(){

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null && bundle.containsKey("screenType")){
            if (bundle.getString("screenType").equalsIgnoreCase("TermsUse")){
                txt_title.setText(getString(R.string.txt_terms_of_use));
                loadUrl = "https://2kxo.com/terms-of-use/";
            }else if(bundle.getString("screenType").equalsIgnoreCase("PrivacyPolicy")){
                txt_title.setText(getString(R.string.txt_privacy_policy));
                loadUrl = "https://2kxo.com/privacy-policy/";
            }else if (bundle.getString("screenType").equalsIgnoreCase("LiveSupport")){
                txt_title.setText(getString(R.string.txt_live_support));
                loadUrl = "https://fxo.io/m/8pjqmq98";
            }else {
                txt_title.setText(getString(R.string.txt_refer_friend));
                loadUrl = "https://goo.gl/5uRtf3";
            }
        }

        web_terms_use.setWebViewClient(new WebViewClient());
        web_terms_use.getSettings().setJavaScriptEnabled(true);
        web_terms_use.getSettings().setSupportZoom(true);       //Zoom Control on web (You don't need this

        web_terms_use.getSettings().setBuiltInZoomControls(false); //Enable Multitouch if supported by ROM
        web_terms_use.getSettings().setUseWideViewPort(true);
        web_terms_use.getSettings().setLoadWithOverviewMode(false);

        web_terms_use.loadUrl(loadUrl);

        ico_back.setOnClickListener(view -> finish());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
