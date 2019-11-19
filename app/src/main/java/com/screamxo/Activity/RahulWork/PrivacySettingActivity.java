package com.screamxo.Activity.RahulWork;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.apimodule.ApiBase.ApiBean.Setting.SettingResponse;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Adapter.BottomSheetAdapter;
import com.screamxo.Others.OnBottomSheetItemClickListener;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.util.ArrayList;
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

public class PrivacySettingActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private static final String TAG = "PrivacySettingActivity";
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    Preferences preferences;
    Context context;
    @BindView(R.id.lny_image)
    LinearLayout lnyImage;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.tb_Private)
    ToggleButton tb_Private;

    @BindView(R.id.txtMediaData)
    TextView txtMediaData;
    @BindView(R.id.txtStreamData)
    TextView txtStreamData;
    @BindView(R.id.txtShopData)
    TextView txtShopData;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private FetchrServiceBase mService;
    private Call<SettingResponse> getSettingCall;
    private Call<SettingResponse> saveSettingCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_setting);
        ButterKnife.bind(this);
        initView();
        initFabIcon();
        getSettingData();
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
                            tb_Private.setChecked(response.body().getResult().getUsersInfo().equalsIgnoreCase("1"));
                            txtMediaData.setText(getLabel(response.body().getResult().getUsersMedia()));
                            txtStreamData.setText(getLabel(response.body().getResult().getUsersBuffet()));
                            txtShopData.setText(getLabel(response.body().getResult().getUsersShop()));

                            txtMediaData.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!tb_Private.isChecked()) {
                                        selectPrivacy(0);
                                    }
                                }
                            });
                            txtMediaData.addTextChangedListener(PrivacySettingActivity.this);

                            txtStreamData.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!tb_Private.isChecked()) {
                                        selectPrivacy(1);
                                    }
                                }
                            });
                            txtStreamData.addTextChangedListener(PrivacySettingActivity.this);

                            txtShopData.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!tb_Private.isChecked()) {
                                        selectPrivacy(2);
                                    }
                                }
                            });
                            txtShopData.addTextChangedListener(PrivacySettingActivity.this);

                            tb_Private.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    saveUserSettings();
                                }
                            });

                        } else {
                            Utils.showToast(PrivacySettingActivity.this, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<SettingResponse> call, Throwable t) {    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                }
            });

        } else {
            Utils.showToast(PrivacySettingActivity.this, this.getString(R.string.toast_no_internet));
        }
    }

    private String getLabel(String usersMedia) {
        switch (usersMedia) {
            case "0":
                return "Private";
            case "1":
                return "Friends";
            case "2":
                return "Public";
        }
        return "";
    }

    private String getId(String privacySetting) {
        switch (privacySetting) {
            case "Private":
                return "0";
            case "Friends":
                return "1";
            case "Public":
                return "2";
        }
        return "";
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
                    Utils.hideKeyboard(PrivacySettingActivity.this);
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
                    Intent gotoNext = new Intent(PrivacySettingActivity.this, UploadDataActivity.class);
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
        }
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        preferences = new Preferences(this);
        mService = new FetchrServiceBase();
        context = PrivacySettingActivity.this;
        txtToolbarTitle.setText(getString(R.string.txt_privacy_settings));
        lnyImage.setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        if (getSettingCall != null) {
            getSettingCall.cancel();
        }
        saveUserSettings();
        super.onDestroy();
    }

    private void saveUserSettings() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("users_buffet", getId(txtStreamData.getText().toString()));
        map.put("users_shop", getId(txtShopData.getText().toString()));
        map.put("users_media", getId(txtMediaData.getText().toString()));
        map.put("users_info", tb_Private.isChecked() ? "1" : "0");

        if (saveSettingCall != null) {
            saveSettingCall.cancel();
        }

        if (Utils.isInternetOn(this)) {
            saveSettingCall = mService.getFetcherService(this).Saveusersettings(map);
            saveSettingCall.enqueue(new Callback<SettingResponse>() {
                @Override
                public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                }

                @Override
                public void onFailure(Call<SettingResponse> call, Throwable t) {    t.printStackTrace();
                }
            });

        } else {
            Utils.showToast(PrivacySettingActivity.this, this.getString(R.string.toast_no_internet));
        }
    }

    public void selectPrivacy(int type) {

        @SuppressLint("InflateParams") View modalbottomsheet = getLayoutInflater().inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);

        tv_title.setText(getString(R.string.txt_select_privacy));

        ArrayList<String> inputList = new ArrayList<>();
        inputList.add(getString(R.string.txt_public));
        inputList.add(getString(R.string.txt_friends));
        inputList.add(getString(R.string.txt_private));

        rv_container.setAdapter(new BottomSheetAdapter(context, inputList, null, null, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                dialog.dismiss();
                if (input.equalsIgnoreCase("Public")) {
                    switch (type) {
                        case 0:
                            txtMediaData.setText("Public");
                            break;
                        case 1:
                            txtStreamData.setText("Public");
                            break;

                        case 2:
                            txtShopData.setText("Public");
                            break;
                    }
                    return;
                }

                if (input.equalsIgnoreCase(getString(R.string.txt_friends))) {
                    switch (type) {
                        case 0:
                            txtMediaData.setText(getString(R.string.txt_friends));
                            break;
                        case 1:
                            txtStreamData.setText(getString(R.string.txt_friends));
                            break;

                        case 2:
                            txtShopData.setText(getString(R.string.txt_friends));
                            break;
                    }
                    return;
                }

                if (input.equalsIgnoreCase("Private")) {
                    switch (type) {
                        case 0:
                            txtMediaData.setText("Private");
                            break;
                        case 1:
                            txtStreamData.setText("Private");
                            break;

                        case 2:
                            txtShopData.setText("Private");
                            break;
                    }
                    return;
                }

            }
        }));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        saveUserSettings();
    }
}
