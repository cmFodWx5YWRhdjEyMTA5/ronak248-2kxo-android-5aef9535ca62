package com.screamxo.Activity.RahulWork;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.screamxo.Fragment.FriendsFragment;
import com.screamxo.R;
import com.screamxo.Utils.EventData;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.Utils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;
import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP;

public class FriendsActivity extends AppCompatActivity implements TextWatcher {
    public static final int REQ_CODE_PROFILE_ACTIVITY_FLOATING_BUTTON = 42;
    private static final String TAG = "FriendsActivity";
    public String imageUrl = "";
    public FloatingMenuButton floatingButton;
    FriendsFragment fragment;
    Preferences preferences;
    Context context;
    @BindView(R.id.framlayout)
    FrameLayout framlayout;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_edt_search)
    EditText toolbarEdtSearch;
    String screenType = "";
    String amount = "";
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private String socialPeopleType = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);
        initView();
        initControl();
        initToolbar();
        findViewById(R.id.img_toolbar_left_icon).setOnClickListener(v -> finish());
        initFabIcon();
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
                    Utils.hideKeyboard(FriendsActivity.this);
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.cross));
                }

                @Override
                public void onMenuClosed(FloatingMenuButton floatingMenuButton) {
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                }
            });

            floatingButton.setOnClickBtnListener(new FloatingMenuButton.OnClickBtnListener() {
                @Override
                public void onClickBtn() {
                    EventBus.getDefault().post(new EventData(EVENT_SCROLL_TO_TOP, ""));
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
                    Intent gotoNext = new Intent(FriendsActivity.this, UploadDataActivity.class);
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

    private void initToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
        txtToolbarTitle.setVisibility(View.GONE);

        imgToolbarRightIcon.setImageDrawable(getResources().getDrawable(R.drawable.ico_user_new));
        imgToolbarRightIcon.setVisibility(View.VISIBLE);
        txtToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        toolbarEdtSearch.setVisibility(View.VISIBLE);
        toolbarEdtSearch.setHint("People");
        toolbarEdtSearch.addTextChangedListener(this);
    }

    private void initView() {
        context = FriendsActivity.this;
        preferences = new Preferences(context);
    }

    private void initControl() {
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("screen")) {
                screenType = getIntent().getStringExtra("screen");
                amount = getIntent().getStringExtra("amount");
            }
            if (getIntent().getExtras().containsKey("imageUrl")) {
                imageUrl = getIntent().getStringExtra("imageUrl");
            }
            if (getIntent().getExtras().containsKey("SOCIAL_PEOPLE_TYPE")) {
                socialPeopleType = getIntent().getStringExtra("SOCIAL_PEOPLE_TYPE");
            }
        }

        fragment = new FriendsFragment(socialPeopleType, screenType, amount);
        Bundle bundle = getIntent().getExtras();
        if (!TextUtils.isEmpty(imageUrl))
            bundle.putString("imageUrl", imageUrl);

        if(getIntent().getExtras()!=null)
        if (getIntent().getExtras().containsKey("screen"))
            if (getIntent().getStringExtra("screen").equals("sendmoney"))
                bundle.putString("amount", getIntent().getStringExtra("amount"));

        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framlayout, fragment, "");
        fragmentTransaction.commit();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (fragment != null && fragment.getClass() == FriendsFragment.class) {
            fragment.callSearchFrdApiFromActivity(charSequence.toString());
        }
    }

    @OnClick({R.id.img_toolbar_left_icon, R.id.img_arrow, R.id.img_pop_up, R.id.img_toolbar_right_icon})
    public void onClick(View view) {
        Log.d(TAG, "onClick: " + view.getId());
        switch (view.getId()) {

            case R.id.img_toolbar_left_icon:
                break;

            case R.id.img_arrow:
                break;

            case R.id.img_pop_up:
                break;

            case R.id.img_toolbar_right_icon:
                if (fragment != null) {
                    fragment.clickToolbarSarchIcon();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_PROFILE_ACTIVITY_FLOATING_BUTTON:
                if (resultCode == RESULT_OK) {
                    if (data.getExtras() != null && data.getExtras().containsKey("FRAGMENT_INDEX")) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("FRAGMENT_INDEX", data.getExtras().getInt("FRAGMENT_INDEX"));
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                }
                break;
            default:
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void onBackPressed() {
        if (isFinishing()) {
            return;
        }

        if (screenType.equalsIgnoreCase("sendmoney")) {
//            startActivityForResult(new Intent(FriendsActivity.this, SendMoneyActivity.class), REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS);
            finish();
        } else {
            finish();
        }
        super.onBackPressed();
    }

    public void hideViews() {
        if (!floatingButton.isMenuOpen()) {
            floatingButton.setBackground(getResources().getDrawable(R.drawable.ic_float_menu_up));
        }
    }

    public void showViews() {
        if (!floatingButton.isMenuOpen()) {
            floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
        }
    }


}
