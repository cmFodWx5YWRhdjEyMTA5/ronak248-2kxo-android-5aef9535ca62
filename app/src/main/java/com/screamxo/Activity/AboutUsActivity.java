package com.screamxo.Activity;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.screamxo.Activity.RahulWork.UploadDataActivity;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AboutUsActivity";

    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView img_toolbar_right_icon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    Preferences preferences;
    Context context;
    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        initView();
        initFabIcon();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        preferences = new Preferences(this);
        // mService = new FetchrServiceBase();
        context = AboutUsActivity.this;
        txtToolbarTitle.setText(getString(R.string.txt_about_us));
        img_toolbar_right_icon.setVisibility(View.GONE);
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
                    Utils.hideKeyboard(AboutUsActivity.this);
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
                    Intent gotoNext = new Intent(AboutUsActivity.this, UploadDataActivity.class);
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

    @OnClick({R.id.img_toolbar_left_icon})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_toolbar_left_icon:
                finish();
                break;
        }
    }
}
