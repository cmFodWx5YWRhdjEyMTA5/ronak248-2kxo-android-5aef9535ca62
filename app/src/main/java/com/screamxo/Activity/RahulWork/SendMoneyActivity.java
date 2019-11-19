package com.screamxo.Activity.RahulWork;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.Payment.StripeToken;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.R;
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

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_FRIENDS_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;


public class SendMoneyActivity extends AppCompatActivity {
    private static final String TAG = "SendMoneyActivity";

    @BindView(R.id.edUsername)
    EditText edUsername;
    String name = "", uid = "";
    Context context;
    Preferences preferences;
    @BindView(R.id.edAmount)
    EditText edAmount;
    @BindView(R.id.progreessbar)
    ProgressBar progressBar;
    Call<StripeToken> sendMoney;
    @BindView(R.id.imgUser)
    ImageView imgUser;
    @BindView(R.id.rl_Drop)
    RelativeLayout rlDrop;
    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.sbSocial)
    FloatingSubButton sbSocial;
    @BindView(R.id.subFriend)
    FloatingSubButton subFriend;
    @BindView(R.id.sbChat)
    FloatingSubButton sbChat;
    @BindView(R.id.sbSearch)
    FloatingSubButton sbSearch;
    @BindView(R.id.sbflHome)
    FloatingSubButton sbflHome;
    @BindView(R.id.sbProfile)
    FloatingSubButton sbProfile;
    @BindView(R.id.sbflSetting)
    FloatingSubButton sbflSetting;
    @BindView(R.id.sbWorld)
    FloatingSubButton sbWorld;
    @BindView(R.id.my_floating_button)
    FloatingMenuButton myFloatingButton;
    FloatingMenuButton floatingButton;
    String amount = "";
//    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private FetchrServiceBase mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        ButterKnife.bind(this);
        initData();
        initFabIcon();
    }

    private void initData() {
        mService = new FetchrServiceBase();
        context = SendMoneyActivity.this;
        preferences = new Preferences(context);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("amount")) {
            amount = bundle.getString("amount");
            edAmount.setText(amount);
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
                    Utils.hideKeyboard(SendMoneyActivity.this);
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
                    Intent gotoNext = new Intent(SendMoneyActivity.this, UploadDataActivity.class);
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

    public void funImage() {
        Intent gotoFriends = new Intent(SendMoneyActivity.this, FriendsActivity.class);
        gotoFriends.putExtra("screen", "sendmoney");
        startActivityForResult(gotoFriends, REQ_CODE_FRIENDS_ACTIVITY_RESULTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_FRIENDS_ACTIVITY_RESULTS:
                    if (data.getExtras() != null) {
                        name = data.getStringExtra("uFullName");
                        uid = data.getStringExtra("uid");
                        edUsername.setText(name);
                    }
                    break;
            }
        }
    }

    public void funSend() {
        if (!uid.equalsIgnoreCase("")) {
            if (!edAmount.getText().toString().equalsIgnoreCase("")) {
                initApi();
            } else {
                Toast.makeText(SendMoneyActivity.this, "Please enter the Amount", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(SendMoneyActivity.this, "Please select the user", Toast.LENGTH_SHORT).show();
        }
    }

    private void initApi() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", preferences.getUserId());
        map.put("to_user_id", "" + uid);
        map.put("amount", edAmount.getText().toString());

        if (Utils.isInternetOn(context)) {
            progressBar.setVisibility(View.VISIBLE);
            sendMoney = mService.getFetcherService(context).sendMoney(map);

            sendMoney.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    //   setViewEnableDisable(true);
                    progressBar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            finish();
                            //        preferences.saveAmount(String.valueOf(response.body().getResult().getAmountint()));
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @OnClick({R.id.imgUser, R.id.btnSend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgUser:
                funImage();
                break;
            case R.id.btnSend:
                funSend();
                break;
        }
    }
}
