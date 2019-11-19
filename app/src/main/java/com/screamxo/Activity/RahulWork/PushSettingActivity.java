package com.screamxo.Activity.RahulWork;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.Setting.SettingResponse;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;

public class PushSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PushSettingActivity";
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.lny_image)
    LinearLayout lnyImage;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;

    @BindView(R.id.rb_MyFriend)
    RadioButton rb_MyFriend;
    @BindView(R.id.rb_EveryOne)
    RadioButton rb_EveryOne;
    @BindView(R.id.rb_CommentFriend)
    RadioButton rb_CommentFriend;
    @BindView(R.id.rb_CommentOne)
    RadioButton rb_CommentOne;
    @BindView(R.id.cb_Contacts)
    CheckBox cb_Contacts;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private Preferences preferences;
    private FetchrServiceBase mService;
    private Call<SettingResponse> getSettingCall;
    private Call<SettingResponse> saveSettingCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_setting);
        ButterKnife.bind(this);
        init();
        initToolbar();
        initFabIcon();
        getSettingData();
    }

    private void init() {
        preferences = new Preferences(this);
        mService = new FetchrServiceBase();
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
                    Utils.hideKeyboard(PushSettingActivity.this);
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
                    Intent gotoNext = new Intent(PushSettingActivity.this, UploadDataActivity.class);
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
            sbSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_chat));
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 101);
                    setResult(RESULT_OK, returnIntent);
                    finish();
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

    @SuppressLint("SetTextI18n")
    private void initToolbar() {
        txtToolbarTitle.setText("Push Settings");
        lnyImage.setVisibility(View.GONE);
    }

    private void getSettingData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        if (Utils.isInternetOn(this)) {
            progreessbar.setVisibility(View.VISIBLE);
            getSettingCall = mService.getFetcherService(this).getSettings(map);
            getSettingCall.enqueue(new Callback<SettingResponse>() {
                @Override
                public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            rb_MyFriend.setChecked(response.body().getResult().getUserNotificationLike().equalsIgnoreCase("1"));
                            rb_EveryOne.setChecked(response.body().getResult().getUserNotificationLike().equalsIgnoreCase("2"));
                            rb_CommentFriend.setChecked(response.body().getResult().getUserNotificationComment().equalsIgnoreCase("1"));
                            rb_CommentOne.setChecked(response.body().getResult().getUserNotificationComment().equalsIgnoreCase("2"));
                            cb_Contacts.setChecked(response.body().getResult().getUserNotificationNewcontact().equalsIgnoreCase("1"));

                            rb_MyFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    saveSettings();
                                }
                            });

                            rb_EveryOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    saveSettings();
                                }
                            });

                            rb_CommentFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    saveSettings();
                                }
                            });

                            rb_CommentOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    saveSettings();
                                }
                            });

                            cb_Contacts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    saveSettings();
                                }
                            });

                        } else {
                            Utils.showToast(PushSettingActivity.this, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<SettingResponse> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                }
            });

        } else {
            Utils.showToast(PushSettingActivity.this, this.getString(R.string.toast_no_internet));
        }
    }

    @OnClick({R.id.img_toolbar_left_icon})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_toolbar_left_icon:
                finish();
                break;
        }
    }

    private void saveSettings() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("notificationlike", rb_MyFriend.isChecked() ? "1" : "2");
        map.put("notificationcomment", rb_CommentFriend.isChecked() ? "1" : "2");
        map.put("notificationnewcontact", cb_Contacts.isChecked() ? "1" : "0");

        if (saveSettingCall != null) {
            saveSettingCall.cancel();
        }

        if (Utils.isInternetOn(this)) {
            saveSettingCall = mService.getFetcherService(this).saveSettings(map);
            saveSettingCall.enqueue(new Callback<SettingResponse>() {
                @Override
                public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                }

                @Override
                public void onFailure(Call<SettingResponse> call, Throwable t) {
                }
            });

        } else {
            Utils.showToast(PushSettingActivity.this, this.getString(R.string.toast_no_internet));
        }
    }

    @Override
    protected void onDestroy() {
        if (getSettingCall != null) {
            getSettingCall.cancel();
        }
        saveSettings();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
