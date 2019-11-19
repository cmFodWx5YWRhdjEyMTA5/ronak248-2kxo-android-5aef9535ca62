package com.screamxo.Activity.RahulWork;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.PhotoPicker;
import com.screamxo.Utils.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dellparangat on 13/12/17.
 */

public class WebViewActivity extends AppCompatActivity{
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;


    @BindView(R.id.img_toolbar_right_icon)
    ImageView img_toolbar_right_icon;

    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;

    private Context context;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        url= getIntent().getStringExtra("url");
        initWebView();
        img_toolbar_right_icon.setVisibility(View.GONE);
        txtToolbarTitle.setText(getString(R.string.txt_open_view));
        imgToolbarLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initWebView() {

        webView.setWebChromeClient(new MyWebChromeClient(context));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl(url);
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }

    }
}
