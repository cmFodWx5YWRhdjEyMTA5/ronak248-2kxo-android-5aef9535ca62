package com.screamxo.Activity.RahulWork;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.Payment.StripeToken;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Others.MyPopUpWindow;
import com.screamxo.R;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

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

public class SupportActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SupportActivity";

    @BindView(R.id.imgArrow)
    ImageView imgArrow;
    @BindView(R.id.rl_Drop)
    RelativeLayout rlDrop;
    @BindView(R.id.edDescription)
    EditText edDescription;
    @BindView(R.id.btnSend)
    Button btnSend;
    Context context;
    @BindView(R.id.txtIssue)
    TextView txtIssue;
    Preferences preferences;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.img_transperent)
    ImageView img_transperent;
    @BindView(R.id.img_pop_up)
    ImageView img_pop_up;
    @BindView(R.id.lnr_description)
    LinearLayout lnr_description;

    String issue = "";
    Call<StripeToken> queryCall;
    String screen = "";
    private FetchrServiceBase mService;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        getWindow().setStatusBarColor(Color.BLACK);
        ButterKnife.bind(this);
        initData();
        initFabIcon();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        if (getIntent().getExtras() != null) {
            screen = getIntent().getStringExtra("screen");
        }

        if (screen.equalsIgnoreCase("BOOST")) {
            txtIssue.setText(getString(R.string.txt_custom_boost));
            issue = "Custom Boost";
        }

        context = SupportActivity.this;
        mService = new FetchrServiceBase();
        preferences = new Preferences(context);

        img_transperent.setOnClickListener(view -> {
            if (img_transperent.getVisibility() == View.VISIBLE) {
                setPopUpVisibility(true);
            }
        });
    }

    private void setPopUpVisibility(boolean isVisible) {
        if (isVisible) {
            img_transperent.setVisibility(View.GONE);
            rlDrop.setBackground(getDrawable(R.drawable.rect_gray_border_new));
            img_pop_up.setVisibility(View.GONE);
            lnr_description.setVisibility(View.GONE);
            edDescription.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.VISIBLE);
        } else {
            img_transperent.setVisibility(View.VISIBLE);
            rlDrop.setBackgroundColor(Color.WHITE);
            img_pop_up.setVisibility(View.VISIBLE);
            lnr_description.setVisibility(View.VISIBLE);
            edDescription.setVisibility(View.GONE);
            btnSend.setVisibility(View.GONE);
        }
    }

    private void initFabIcon() {
        Log.d(TAG, "initFabIcon: ");
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
                    Log.d(TAG, "onMenuOpened: ");
                    Utils.hideKeyboard(SupportActivity.this);
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
                    Intent gotoNext = new Intent(SupportActivity.this, UploadDataActivity.class);
                    startActivityForResult(gotoNext, REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS);
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
            Log.e(TAG, "initFabIcon: ", e);
        }
    }

    @OnClick({R.id.rl_Drop,R.id.txt_send_money,R.id.txt_request_money,R.id.txt_invoices,R.id.txt_withdraw,
    R.id.txt_technical_bugs,R.id.txt_dispute,R.id.txt_enquiry})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_Drop:
                if (!screen.equalsIgnoreCase("boost")) {
                    openPopUpmenu();
                }
                break;
            case R.id.txt_send_money:
                setPopUpVisibility(true);
                txtIssue.setText(getString(R.string.txt_how_to_send_money));
                issue = "How to send Money";
                break;
            case R.id.txt_request_money:
                setPopUpVisibility(true);
                txtIssue.setText(getString(R.string.txt_how_to_request_money));
                issue = "How to request money";
                break;
            case R.id.txt_invoices:
                setPopUpVisibility(true);
                txtIssue.setText(getString(R.string.txt_invoice_reporting));
                issue = "Invoices & Reporting";
                break;
            case R.id.txt_withdraw:
                setPopUpVisibility(true);
                txtIssue.setText(getString(R.string.txt_how_to_withdraw_cash));
                issue = "How to withdraw Xocash";
                break;
            case R.id.txt_technical_bugs:
                setPopUpVisibility(true);
                txtIssue.setText(getString(R.string.txt_technical_bugs));
                issue = "Technical & Bugs";
                break;
            case R.id.txt_dispute:
                setPopUpVisibility(true);
                txtIssue.setText(getString(R.string.txt_dispute_transaction));
                issue = "Dispute Transaction";
                break;
            case R.id.txt_enquiry:
                setPopUpVisibility(true);
                txtIssue.setText(getString(R.string.txt_general_enquiry));
                issue = "General Enquiry";
                break;
        }
    }

    public void openPopUpmenu() {
        setPopUpVisibility(false);
    }

    public void initApi() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("issue", issue);
        map.put("message", edDescription.getText().toString());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            queryCall = mService.getFetcherService(context).sendQuery(map);
            queryCall.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_SUCESS, null);
                        } else if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                }
            });
        }
    }

    public void funSubmitQuery(View view) {
        Utils.hideKeyboard(this);
        if (!issue.equalsIgnoreCase("")) {
            if (!edDescription.getText().toString().equalsIgnoreCase("")) {
                initApi();
            } else {
                Toast.makeText(SupportActivity.this, getString(R.string.msg_write_query), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SupportActivity.this, getString(R.string.txt_choose_issue), Toast.LENGTH_SHORT).show();
        }
    }
}
