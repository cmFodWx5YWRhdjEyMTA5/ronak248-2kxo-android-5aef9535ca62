package com.screamxo.Activity.RahulWork;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.Payment.FinalReceiptBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;

public class TransactionHIstoryActivity extends AppCompatActivity {
    private static final String TAG = "TransactionHIstoryActiv";
    private String itemId = "";
    private StringBuffer stringBuffer;
    private Preferences preferences;
    @BindView(R.id.pdfView)
    WebView pdfView;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    private Call<FinalReceiptBean> rejectcall;
    @BindView(R.id.img_print)
    ImageView img_print;

    public FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        ButterKnife.bind(this);
        getWindow().setStatusBarColor(getColor(R.color.colorNewBlue));
       // initData();
        callGenerateReceiptApi();
        initFabIcon();
    }

    private void callGenerateReceiptApi() {
        if (getIntent() != null)
            itemId = getIntent().getStringExtra("itemId");
        java.util.Map<String, Integer> map = new java.util.HashMap<>();
        map.put("wallet_id", Integer.parseInt(itemId));

        if (Utils.isInternetOn(this)) {

            progreessbar.setVisibility(View.VISIBLE);
            rejectcall = new FetchrServiceBase().getFetcherService(this).getWalletTransactionReceipt(map);

            rejectcall.enqueue(new Callback<FinalReceiptBean>() {
                @Override
                public void onResponse(Call<FinalReceiptBean> call, Response<FinalReceiptBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    pdfView.loadData(response.body().getResult(), "text/html", "UTF-8");
                    initControlValue();
                }

                @Override
                public void onFailure(Call<FinalReceiptBean> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                }
            });

        } else {
            Utils.showToast(this,getString(R.string.toast_no_internet));
        }
    }

    private void initControlValue() {
        img_print.setVisibility(View.VISIBLE);
        img_print.setOnClickListener(view -> createWebPagePrint(pdfView));
    }

    public  void createWebPagePrint(WebView webView) {
		/*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return;*/
        PrintManager printManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

            PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
            String jobName = getString(R.string.app_name) + " Document";
            PrintAttributes.Builder builder = new PrintAttributes.Builder();
            builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5);
            PrintJob printJob = printManager.print(jobName, printAdapter, builder.build());

            if (printJob.isCompleted()) {
                Toast.makeText(getApplicationContext(), getString(R.string.msg_print_completed), Toast.LENGTH_LONG).show();
            } else if (printJob.isFailed()) {
                Toast.makeText(getApplicationContext(), getString(R.string.msg_print_failed), Toast.LENGTH_LONG).show();
            }
        }
        // Save the job object for later status checking
    }

    private void initFabIcon() {
        try {
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
                    Utils.hideKeyboard(TransactionHIstoryActivity.this);
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
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 6);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbSocial = findViewById(R.id.sbSocial);
            sbSocial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 3);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbChat = findViewById(R.id.sbChat);
            sbChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                }
            });

            sbWorld = findViewById(R.id.sbWorld);
            sbWorld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 2);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbSearch = findViewById(R.id.sbSearch);
            sbSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_chat));
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                }
            });

            sbflSetting = findViewById(R.id.sbflSetting);
            sbflSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 7);
                    setResult(RESULT_OK, returnIntent);
                    finish();

                }
            });

            subFriend = findViewById(R.id.subFriend);
            subFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 5);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbflHome = findViewById(R.id.sbflHome);
            sbflHome.setOnClickListener(view -> {
                floatingButton.closeMenu();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", 1);
                setResult(RESULT_OK, returnIntent);
                finish();
            });

        } catch (Exception e) {
        }
    }

    private void initData() {
        preferences = new Preferences(this);
        itemId = getIntent().getStringExtra("itemId");
        stringBuffer = new StringBuffer();
        stringBuffer.append("https://api.screamxo.com/mobileservice/Walletorderpayment/generateReceipt/");
        stringBuffer.append(preferences.getUserId());
        stringBuffer.append("/");
        stringBuffer.append(itemId);
        pdfView.loadUrl(stringBuffer.toString());
    }

}
