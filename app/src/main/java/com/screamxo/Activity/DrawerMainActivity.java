package com.screamxo.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Rational;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.CategoryList.CategoryList;
import com.example.apimodule.ApiBase.ApiBean.DashBoardResult;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.screamxo.Activity.RahulWork.CustomWebViewFragment;
import com.screamxo.Activity.RahulWork.FriendsActivity;
import com.screamxo.Activity.RahulWork.PostCongActivity;
import com.screamxo.Activity.RahulWork.UploadDataActivity;
import com.screamxo.Activity.wallet.WalletSendReceiveActivity;
import com.screamxo.Adapter.DashboardAdapter;
import com.screamxo.Emoji.Emojix;
import com.screamxo.Emoji.tagging.OnHashTagClickListener;
import com.screamxo.Fragment.DashboardFragment;
import com.screamxo.Fragment.DashboardPagerFragment;
import com.screamxo.Fragment.FriendsFragment;
import com.screamxo.Fragment.ItemDetails;
import com.screamxo.Fragment.MediaFragment;
import com.screamxo.Fragment.NewCartFragment;
import com.screamxo.Fragment.ProfileFragment;
import com.screamxo.Fragment.SettingFragment;
import com.screamxo.Fragment.ShopFragment;
import com.screamxo.Fragment.ShopFragmentView;
import com.screamxo.Fragment.SocialFragment;
import com.screamxo.Fragment.StreamFragment;
import com.screamxo.Interface.IHttpresponse;
import com.screamxo.Interface.INavigator;
import com.screamxo.R;
import com.screamxo.Utils.ApiConnectionUtils;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.EventData;
import com.screamxo.Utils.HomeWatcher;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;
import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP_NEW_PROFILE;
import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP_tranding;

public class DrawerMainActivity extends AppCompatActivity implements /*NavigationView.OnNavigationItemSelectedListener,*/ TextWatcher, INavigator, IHttpresponse {

    Boolean isLocked = false;
    public static boolean refresh = false;
    public static final int REQ_CODE_FRIENDS_ACTIVITY_RESULTS = 101;
    public static final int REQ_CODE_CONFIGURE_PAYMENT_ACTIVITY_RESULTS = 102;
    public static final int REQ_CODE_WATCH_ITEM_LIST_ACTIVITY_RESULTS = 103;
    public static final int REQ_CODE_PURCHASE_HISTORY_ACTIVITY_RESULTS = 104;
    public static final int REQ_CODE_PAYMENT_DETAIL_ACTIVITY_RESULTS = 105;
    public static final int REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS = 106;
    public static final int REQ_CODE_SHOP_LISTING_ACTIVITY_RESULTS = 107;
    public static final int REQ_CODE_SOLD_HISTORY_ACTIVITY_RESULTS = 108;
    public static final int REQ_CODE_BOOST_ACTIVITY_RESULTS = 108;
    public static final int REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS = 109;
    public static final int REQ_CODE_UPLOAD_MEDIA_ACTIVITY_RESULTS = 110;
    public static final int REQ_CODE_POST_STREAM_ACTIVITY_RESULTS = 111;
    public static final int REQ_CODE_RECORDER_TAG_ACTIVITY_RESULTS = 111;
    public static final int REQ_CODE_POST_CONG_ACTIVITY_RESULTS = 112;
    public static final int REQ_CODE_SUPPORT_ACTIVITY_RESULTS = 113;
    public static final int REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS = 114;
    public static final int REQ_CODE_WALLET_ACTIVITY_RESULTS = 115;
    public static final int REQ_CODE_TOP_UP_ACTIVITY_RESULTS = 116;
    public static final int REQ_CODE_PUSH_SETTING_ACTIVITY_RESULTS = 117;
    public static final int REQ_CODE_REJECT_SETTING_ACTIVITY_RESULTS = 118;
    public static final int REQ_CODE_PRIVACY_SETTING_ACTIVITY_RESULTS = 118;
    public static final int REQ_CODE_CHANGE_PASSWORD_ACTIVITY_RESULTS = 119;
    public static final int REQ_CODE_ABOUT_US_ACTIVITY_RESULTS = 120;
    public static final int REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS = 121;
    public static final int REQ_CODE_EDIT_SELL_ITEM_ACTIVITY_RESULTS = 122;
    public static final int REQ_CODE_CHAT_ACTIVITY_RESULTS = 123;
    public static final int REQ_CODE_CONFIGURE_PAYEMENT_RESULTS = 124;
    public static final int REQ_CODE_BUY_CONG_ACTIVITY_RESULTS = 125;
    public static final int CAPTURE_MEDIA = 368;
    public static final int FACEBOOK_REQUEST_CODE = 64206;

    private static final String TAG = "DrawerMainActivity";
    public Fragment fragment;
    public FloatingMenuButton floatingButton;
    @BindView(R.id.framlayout)
    FrameLayout mframlayout;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content_drawer_main)
    RelativeLayout contentDrawerMain;
    @BindView(R.id.img_arrow)
    ImageView imgArrow;
    @BindView(R.id.img_pop_up)
    ImageView imgPopUp;
    Boolean collepsImgArrow = true;
    Preferences preferences;
    DashBoardResult dashBoardResult;
    Context context;
    String socialPeopleType = "0";
    @BindView(R.id.toolbar_edt_search)
    EditText toolbarEdtSearch;
    @BindView(R.id.img_transperent)
    ImageView img_transperent;
    Call<CategoryList> categoryListCall;
    Boolean flagFriends = false;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private FetchrServiceBase mService;
    private DashBoardResult mDashBoardResult;
    DashboardPagerFragment dashboardPagerFragment;
    PictureInPictureParams.Builder pictureInPictureParamsBuilder;
    public boolean has_pip;
    public int player_height;
    public int player_width;
    public boolean IsPipMode, activity_Created;
    HomeWatcher mHomeWatcher;

    private static final String ACTION_MEDIA_CONTROL = "media_control";
    private static final String EXTRA_CONTROL_TYPE = "control_type";

    /**
     * The request code for play action PendingIntent.
     */
    private static final int REQUEST_PLAY = 1;

    /**
     * The request code for pause action PendingIntent.
     */
    private static final int REQUEST_PAUSE = 2;

    /**
     * The request code for info action PendingIntent.
     */
    private static final int REQUEST_INFO = 3;

    /**
     * The intent extra value for play action.
     */
    private static final int CONTROL_TYPE_PLAY = 1;

    private static final int CONTROL_TYPE_PAUSE = 2;
    private static final int CONTROL_TYPE_NEXT = 3;
    private static final int CONTROL_TYPE_PREVIOUS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);
        dashboardPagerFragment = DashboardPagerFragment.newInstance("1");
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        init();
        initFabIcon();
        setUpFloatingMenuItems();
        Log.e(TAG, "onCreate: " + FirebaseInstanceId.getInstance().getToken());

        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (activity_Created)
                    enterInPipMode();
            }

            @Override
            public void onHomeLongPressed() {
                if (activity_Created)
                    enterInPipMode();
            }
        });
        mHomeWatcher.startWatch();

    }

    private void init() {
        context = this;
        preferences = new Preferences(this);
        mService = new FetchrServiceBase();
        mHomeWatcher = new HomeWatcher(this);
        ApiConnectionUtils.initCategory(context);
        ApiConnectionUtils.initNewCategories(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPictureParamsBuilder = new PictureInPictureParams.Builder();
        }
    }

    public void setTranperentVisibility(int visibility) {
        img_transperent.setVisibility(visibility);
    }

    private void initFabIcon() {

        Log.d(TAG, "initFabIcon: ");
        try {

            floatingButton = findViewById(R.id.my_floating_button);
            sbProfile = findViewById(R.id.sbProfile);

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

            floatingButton.setOnClickBtnListener(new FloatingMenuButton.OnClickBtnListener() {
                @Override
                public void onClickBtn() {

                    Log.d(TAG, "onClickBtn: ");

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                        DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
                        dashboardPagerFragment.check_Page();

                        /*Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(StaticConstant.count);
                        if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment) {
                            EventBus.getDefault().post(new EventData(EVENT_SCROLL_TO_TOP, ""));
                        } else if (currentVpFrag != null && currentVpFrag instanceof SocialFragment) {
                            EventBus.getDefault().post(new EventData(EVENT_SCROLL_TO_TOP_NEW, ""));
                        }*/

                    } else {
                        EventBus.getDefault().post(new EventData(EVENT_SCROLL_TO_TOP_NEW_PROFILE, ""));
                    }
                }
            });

            floatingButton.setStateChangeListener(new FloatingMenuStateChangeListener() {
                @Override
                public void onMenuOpened(FloatingMenuButton floatingMenuButton) {
                    Log.d(TAG, "onMenuOpened: ");
                    Utils.hideKeyboard(DrawerMainActivity.this);
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.cross));
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    Log.d(TAG, "onMenuOpened: " + ((currentFragment == null) ? "(currentFragment == null)" : currentFragment.getClass().getSimpleName()));
                    if (currentFragment != null && currentFragment instanceof ShopFragment) {
                        sbSearch.setBackground(ContextCompat.getDrawable(DrawerMainActivity.this, R.mipmap.filterred));
                    } else if (currentFragment != null && (currentFragment instanceof ProfileFragment
                            || currentFragment instanceof SettingFragment || currentFragment instanceof NewCartFragment)) {
                        sbSearch.setBackground(ContextCompat.getDrawable(DrawerMainActivity.this, R.drawable.floating_chat));
                    } else {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            sbSearch.setBackground(ContextCompat.getDrawable(DrawerMainActivity.this, R.drawable.filter));
                        } else {
                            sbSearch.setBackground(ContextCompat.getDrawable(DrawerMainActivity.this, R.drawable.floating_filters));
                        }
                    }

                    if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                        sbflHome.setBackground(ContextCompat.getDrawable(DrawerMainActivity.this, R.drawable.dashboard));
                    } else {
//                        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
//                            sbflHome.setBackground(ContextCompat.getDrawable(DrawerMainActivity.this, R.mipmap.lock));
//                            DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
//                            Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager();
//                            if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment) {
//                                ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()).lockUnlockScreen();
//                            }
//                        } else {
//                            setFragment(1);
//                        }
                        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                            if (isLocked)
                                sbflHome.setBackground(ContextCompat.getDrawable(DrawerMainActivity.this, R.drawable.floating_locked));
                            else
                                sbflHome.setBackground(ContextCompat.getDrawable(DrawerMainActivity.this, R.drawable.floating_unlocked));
//                            sbflHome.setBackground(ContextCompat.getDrawable(DrawerMainActivity.this, R.drawable.folating_locks));
                        } else {
                            sbflHome.setBackground(ContextCompat.getDrawable(DrawerMainActivity.this, R.drawable.floating_dashboard));
                        }
                    }
                }

                @Override
                public void onMenuClosed(FloatingMenuButton floatingMenuButton)
                {
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                }
            });

            sbProfile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    floatingButton.closeMenu();

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                        DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
                        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                            Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(1);
                            if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment && ((DashboardAdapter) ((DashboardFragment) currentVpFrag)
                                    .mrecyclerView.getAdapter()) != null) {
                                ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()).playInlinePlayer();
                            }
                        }
                    }

                    if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                        setFragment(6);
                    } else {
                        gotoLogin(context);
                    }
                }
            });

            sbSocial = findViewById(R.id.sbSocial);
            sbSocial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                        DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
                        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                            Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(1);
                            if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment &&
                                    ((DashboardAdapter) ((DashboardFragment) currentVpFrag)
                                            .mrecyclerView.getAdapter()) != null) {
                                ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()).playInlinePlayer();
                            }
                        }
                    }

                    if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                        setFragment(3);
                    } else {
                        gotoLogin(context);
                    }
                }
            });

            sbChat = findViewById(R.id.sbChat);

            sbChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                        DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
                        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                            Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(1);
                            if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment &&
                                    ((DashboardAdapter) ((DashboardFragment) currentVpFrag)
                                            .mrecyclerView.getAdapter()) != null) {
                                ((DashboardAdapter) ((DashboardFragment) currentVpFrag)
                                        .mrecyclerView.getAdapter()).playInlinePlayer();
                            }
                        }
                    }
                    if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                        Intent gotoNext = new Intent(DrawerMainActivity.this, UploadDataActivity.class);
                        startActivityForResult(gotoNext, REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS);
                    }
                    //   setFragment(4, 0);
                    else {
                        gotoLogin(context);
                    }

                }
            });

            sbWorld = findViewById(R.id.sbWorld);
            sbWorld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                        DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
                        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                            Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(1);
                            if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment &&
                                    ((DashboardAdapter) ((DashboardFragment) currentVpFrag)
                                            .mrecyclerView.getAdapter()) != null) {
                                ((DashboardAdapter) ((DashboardFragment) currentVpFrag)
                                        .mrecyclerView.getAdapter()).playInlinePlayer();
                            }
                        }
                    }
                    if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                        setFragment(2);
                    } else {
                        gotoLogin(context);
                    }
                }
            });

            sbSearch = findViewById(R.id.sbSearch);
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                        DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
                        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                            Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(1);
                            if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment &&
                                    ((DashboardAdapter) ((DashboardFragment) currentVpFrag)
                                            .mrecyclerView.getAdapter()) != null) {
                                ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()).playInlinePlayer();
                            }
                        }
                    }

                    if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                        // Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);

                        if (currentFragment != null && currentFragment instanceof ShopFragment) {
                            ((ShopFragment) currentFragment).onFilterBtnClick();
                            return;
                        }

                        if (currentFragment != null && (currentFragment instanceof ProfileFragment
                                || currentFragment instanceof MediaFragment
                                || currentFragment instanceof ShopFragmentView
                                || currentFragment instanceof NewCartFragment
                                || currentFragment instanceof StreamFragment
                                || currentFragment instanceof SettingFragment)) {
                            setFragment(1, 0);
                            return;
                        }

                        if (fragment.getClass() == DashboardPagerFragment.class) {
                            Utils.hideKeyboard(context);
                            ((DashboardPagerFragment) getSupportFragmentManager().findFragmentById(R.id.framlayout)).initSearch("");
                        }

                    } else {
                        gotoLogin(context);
                    }
                }
            });

            sbflSetting = findViewById(R.id.sbflSetting);
            sbflSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                        DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
                        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                            Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(1);
                            if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment &&
                                    ((DashboardAdapter) ((DashboardFragment) currentVpFrag)
                                            .mrecyclerView.getAdapter()) != null) {
                                ((DashboardAdapter) ((DashboardFragment) currentVpFrag)
                                        .mrecyclerView.getAdapter()).playInlinePlayer();
                            }
                        }
                    }
                    if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                        setFragment(7);
                    } else {
                        gotoLogin(context);
                    }
                }
            });

            subFriend = findViewById(R.id.subFriend);
            subFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                        DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
                        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                            Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(1);
                            if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment &&
                                    ((DashboardAdapter) ((DashboardFragment) currentVpFrag)
                                            .mrecyclerView.getAdapter()) != null) {
                                ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()).playInlinePlayer();
                            }
                        }
                    }
                    if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                        setFragment(5);
                    } else {
                        gotoLogin(context);
                    }
                }
            });

            sbflHome = findViewById(R.id.sbflHome);
            sbflHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    floatingButton.closeMenu();
                    try {
                        if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                            if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                                DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
                                Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(1);
                                if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment &&
                                        ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()) != null) {
                                    isLocked = !isLocked;

                                    ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()).lockUnlockScreen();
                                }
                            } else {
                                setFragment(1);
                            }
                        } else {
                            gotoLogin(context);
                        }
                    } catch (Exception e) {
                        Toast.makeText(DrawerMainActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            initControlValue();
        } catch (Exception e) {
            Log.e(TAG, "initFabIcon: ", e);
        }
    }

    public void setUpFloatingMenuItems() {
        Log.d(TAG, "setUpFloatingMenuItems: ");
        if (preferences.getUserId().isEmpty()) {
            sbflSetting.setBackground(ContextCompat.getDrawable(this, R.drawable.setting_with_name));
            sbflHome.setBackground(ContextCompat.getDrawable(this, R.drawable.dashboard_with_name));
            subFriend.setBackground(ContextCompat.getDrawable(this, R.drawable.post_with_name));
            sbSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.search));
            sbWorld.setBackground(ContextCompat.getDrawable(this, R.drawable.world));
            sbProfile.setBackground(ContextCompat.getDrawable(this, R.drawable.profile_with_name));
            sbChat.setBackground(ContextCompat.getDrawable(this, R.drawable.post_with_name));
            sbSocial.setBackground(ContextCompat.getDrawable(this, R.drawable.shop));
        } else {

            sbflSetting.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_posts));
            sbflHome.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_dashboard));
            subFriend.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_peoples));
            if (fragment instanceof NewCartFragment)
                sbSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_chat));
            else
                sbSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_filters));
            sbSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_filters));
            sbWorld.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_wallates));
            sbProfile.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_profiles));
            sbChat.setBackground(ContextCompat.getDrawable(this, R.drawable.post_gsm_new));
            sbSocial.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_trolly));
        }
    }

    private void initControlValue()
    {
        imgToolbarLeftIcon.setImageDrawable(getResources().getDrawable(R.mipmap.ico_back_chat));
        toolbarEdtSearch.addTextChangedListener(this);
        setFragment(1);
    }

    public void gotoLogin(Context context) {
        Intent gotoLogin = new Intent(context, CommonLoginSignUpActivity.class);
        gotoLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(gotoLogin);
        finish();
    }

    public void setHeigthWidth(int player_height, int player_width)
    {
        this.player_height = player_height;
        this.player_width = player_width;
    }

    public void setFragment(int i, int pos)
    {
        try
        {
            Log.d(TAG, "setFragment: " + i);
            setUpFloatingMenuItems();
            Fragment currentFragment;
            flagFriends = true;
            toolbarEdtSearch.setText("");
            String tag = "";
            boolean flagWallet = false;
            switch (i)
            {
                case 1:
                    currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof DashboardPagerFragment)
                    {
                        ((DashboardPagerFragment) currentFragment).viewPager.setCurrentItem(pos);
                        return;
                    }
                    tag = DashboardPagerFragment.class.getSimpleName();

//                    fragment = DashboardPagerFragment.newInstance(String.valueOf(pos));
                    fragment = dashboardPagerFragment;
                    setVisibility(1, View.VISIBLE);
                    break;

                case 2:
                    flagWallet = true;
                    startActivityForResult(new Intent(DrawerMainActivity.this, WalletSendReceiveActivity.class), REQ_CODE_WALLET_ACTIVITY_RESULTS);
                    break;

                case 3:
                    currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof NewCartFragment) { // replace ShopFragment
                        return;
                    }
                    fragment = new NewCartFragment(false);
                    setVisibility(2, View.GONE);
                    break;

                case 4:
                    currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof SocialFragment) {
                        return;
                    }
                    fragment = new SocialFragment();
                    setVisibility(4, View.GONE);
                    break;

                case 5:
                    flagFriends = false;
                    Intent gotoNext = new Intent(DrawerMainActivity.this, FriendsActivity.class);
                    gotoNext.putExtra("SOCIAL_PEOPLE_TYPE", socialPeopleType);
                    startActivityForResult(gotoNext, REQ_CODE_FRIENDS_ACTIVITY_RESULTS);
                    break;

                case 6:
                    currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof ProfileFragment)
                    {
                        return;
                    }
                    fragment = new ProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("uid", preferences.getUserId());
                    bundle.putString("uFullName", preferences.getUserFullName());
                    bundle.putString("username", preferences.getUserName());
                    bundle.putString("uProfile", preferences.getUserProfile());
                    bundle.putString("screen", "drawer");
                    fragment.setArguments(bundle);
                    tag = ProfileFragment.class.getSimpleName();
                    setVisibility(6, View.GONE);
                    break;

                case 7:
                    currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
                    if (currentFragment != null && currentFragment instanceof SettingFragment) {
                        return;
                    }
                    fragment = new SettingFragment();
                    setVisibility(7, View.GONE);
                    break;

                case 8:
                    String urlStr = "http://api.screamxo.com/pages/load/privacypolicy";
                    fragment = CustomWebViewFragment.newInstance(urlStr);
                    tag = CustomWebViewFragment.class.getSimpleName();
                    break;

                default:
                    fragment = new DashboardPagerFragment();
                    setVisibility(0, View.GONE);
                    break;
            }

            if (flagFriends && !flagWallet) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                if (fragment instanceof DashboardPagerFragment) {
                    if (getSupportFragmentManager().findFragmentByTag(DashboardPagerFragment.class.getSimpleName()) != null) {
                        fragmentTransaction.replace(R.id.framlayout, getSupportFragmentManager().findFragmentByTag(DashboardPagerFragment.class.getSimpleName()), tag);
                        fragmentTransaction.commit();
//                        getSupportFragmentManager().beginTransaction().attach(getSupportFragmentManager().findFragmentByTag(DashboardPagerFragment.class.getSimpleName())).commit();
                    } else {
                        fragmentTransaction.add(R.id.framlayout, fragment, tag);
                        fragmentTransaction.commit();
                    }
                } else {
                    fragmentTransaction.add(R.id.framlayout, fragment, tag);
                    fragmentTransaction.commit();
                }
            }
        } catch (Exception ignored) {

        }
    }

    public void setFragment(int i) {
        setFragment(i, 1);
    }

    @OnClick({R.id.img_toolbar_left_icon, R.id.img_arrow, R.id.img_pop_up, R.id.img_toolbar_right_icon})
    public void onClick(View view) {
        Log.d(TAG, "onClick: " + view.getId());
        int visibility;
        switch (view.getId()) {

            case R.id.img_toolbar_left_icon:
                //drawerLayout.openDrawer(Gravity.LEFT);
                break;

            case R.id.img_arrow:
                if (collepsImgArrow) {
                    imgArrow.setRotation(0);
                    collepsImgArrow = false;
                    visibility = View.GONE;
                } else {
                    imgArrow.setRotation(180);
                    collepsImgArrow = true;
                    visibility = View.VISIBLE;
                }
                if (fragment.getClass() == DashboardFragment.class) {
                    ((DashboardFragment) getSupportFragmentManager().findFragmentById(R.id.framlayout)).callFromActivity(visibility);
                }
                break;

            case R.id.img_pop_up:
                //   openPopUpmenu();
                break;

            case R.id.img_toolbar_right_icon:
                Log.d(TAG, "onClick img_toolbar_right_icon: ");
                if (fragment.getClass() == DashboardFragment.class) {
                    Intent intent = new Intent(context, PostMediaActivity.class);
                    ((Activity) context).startActivityForResult(intent, StaticConstant.REQUEST_POST_MEDIA);
                    //context.startActivity(intent);
                } else if (fragment.getClass() == ProfileFragment.class) {
                    Intent intent = new Intent(context, EditProfileActivity.class);
                    intent.putExtra("screen", DrawerMainActivity.class.getSimpleName());
                    startActivity(intent);
                }/* else if (fragment.getClass() == FriendsFragment.class) {
                    ((FriendsFragment) fragment).clickToolbarSarchIcon();
                }*/
                break;
        }
    }

    public void setVisibility(int caseNo, int visibility) {
        Log.d(TAG, "setVisibility: ");
        imgArrow.setVisibility(visibility);
        imgPopUp.setVisibility(visibility);

        switch (caseNo)
        {
            case 1:
                imgToolbarLeftIcon.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                txtToolbarTitle.setText(Html.fromHtml(getString(R.string.txt_tag_top_MEDIA)));
                txtToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                imgArrow.setRotation(180);
                imgPopUp.setBackground(null);
                imgToolbarRightIcon.setVisibility(View.VISIBLE);
                imgToolbarRightIcon.setImageDrawable(getResources().getDrawable(R.mipmap.add));

                txtToolbarTitle.setVisibility(View.VISIBLE);
                toolbarEdtSearch.setVisibility(View.GONE);
                // toolbarEdtSearch.setVisibility(View.VISIBLE);
                break;

            case 2:
                toolbar.setVisibility(View.GONE);
                txtToolbarTitle.setVisibility(View.GONE);
                toolbarEdtSearch.setVisibility(View.VISIBLE);
                imgToolbarRightIcon.setImageDrawable(getResources().getDrawable(R.drawable.ico_user_new));
                imgToolbarRightIcon.setVisibility(View.VISIBLE);
                toolbarEdtSearch.setHint("World");
                txtToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;

            case 3:
                toolbar.setVisibility(View.VISIBLE);
                break;

            case 4:
                imgToolbarLeftIcon.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                txtToolbarTitle.setText(getResources().getString(R.string.title_activity_drawer_social));
                txtToolbarTitle.setVisibility(View.GONE);
                toolbarEdtSearch.setVisibility(View.GONE);
                imgToolbarRightIcon.setVisibility(View.GONE);
                txtToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;

            case 5:
                toolbar.setVisibility(View.VISIBLE);
                txtToolbarTitle.setVisibility(View.GONE);
                toolbarEdtSearch.setVisibility(View.VISIBLE);
                imgToolbarRightIcon.setImageDrawable(getResources().getDrawable(R.drawable.ico_user_new));
                imgToolbarRightIcon.setVisibility(View.VISIBLE);
                txtToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                toolbarEdtSearch.setHint("People");
                break;

            case 6:
                toolbar.setVisibility(View.GONE);
                imgToolbarRightIcon.setImageDrawable(getResources().getDrawable(R.mipmap.edit_pencil));
                imgToolbarRightIcon.setVisibility(View.VISIBLE);
                txtToolbarTitle.setText(getResources().getString(R.string.title_activity_drawer_profile));
                txtToolbarTitle.setVisibility(View.VISIBLE);
                toolbarEdtSearch.setVisibility(View.GONE);
                txtToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;

            case 7:
                toolbar.setVisibility(View.VISIBLE);
                txtToolbarTitle.setText(getResources().getString(R.string.title_activity_drawer_setting));
                txtToolbarTitle.setVisibility(View.VISIBLE);
                txtToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                imgToolbarRightIcon.setVisibility(View.GONE);
                toolbarEdtSearch.setVisibility(View.GONE);
                break;

            case 0:
                toolbar.setVisibility(View.VISIBLE);
                txtToolbarTitle.setText(getResources().getString(R.string.title_activity_drawer_dash_board));
                txtToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;

            case -1:
                toolbar.setVisibility(View.GONE);
                break;
        }
    }

    public void callDashboardAPi(String id) {
//        if (fragment.getClass() == DashboardPagerFragment.class) {
//            ((DashboardFragment) getSupportFragmentManager().findFragmentById(R.id.framlayout)).callDashBoardApiore("",1","");
//        }

        EventBus.getDefault().post(new EventData(EVENT_SCROLL_TO_TOP_tranding, ""));

    }

    public void hideViews() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        if (!floatingButton.isMenuOpen()) {
            floatingButton.setBackground(getResources().getDrawable(R.drawable.ic_float_menu_up));
        }
    }

    public void showViews() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        if (!floatingButton.isMenuOpen()) {
            floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
        }
    }

    public void setApiData(DashBoardResult dashBoardResult) throws NullPointerException {
        this.dashBoardResult = dashBoardResult;
    }

    public DashBoardResult getApidata() {
        return dashBoardResult;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult requestCode: " + requestCode);
        Log.d(TAG, "onActivityResult resultCode: " + resultCode);

        Utils.printIntentData(data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1234) {
                if (data.getBooleanExtra("fire", false)) {
                    fragment = new NewCartFragment(true);
                    setVisibility(2, View.GONE);

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                    if (fragment instanceof DashboardPagerFragment) {
                        if (getSupportFragmentManager().findFragmentByTag(DashboardPagerFragment.class.getSimpleName()) != null) {
                            fragmentTransaction.replace(R.id.framlayout, getSupportFragmentManager().findFragmentByTag(DashboardPagerFragment.class.getSimpleName()), NewCartFragment.class.getSimpleName());
                            fragmentTransaction.commit();
//                        getSupportFragmentManager().beginTransaction().attach(getSupportFragmentManager().findFragmentByTag(DashboardPagerFragment.class.getSimpleName())).commit();
                        } else {
                            fragmentTransaction.add(R.id.framlayout, fragment, NewCartFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        }
                    } else {
                        fragmentTransaction.add(R.id.framlayout, fragment, NewCartFragment.class.getSimpleName());
                        fragmentTransaction.commit();
                    }
                }
            }

            if (data != null && data.getExtras() != null && data.getExtras().containsKey("FRAGMENT_INDEX")) {
                switch (data.getExtras().getInt("FRAGMENT_INDEX")) {

                    case 101:
                        showChatPage();
                        break;

                    default: {
                        // commented to stop refresh from wallet screen
//                        refresh = true;
                        setFragment(data.getExtras().getInt("FRAGMENT_INDEX"));
                    }
                    break;
                }
                return;
            }
        }

        switch (requestCode) {
            case StaticConstant.REQUEST_SELL_ITEM:
                if (data != null && data.getExtras() != null && data.getExtras().containsKey("POST_ANOTHER")) {
                    if (data.getExtras().containsKey("POST_ANOTHER")) {
                        Intent intent = new Intent(context, com.screamxo.Activity.RahulWork.SellItemActivity.class);
                        ((Activity) context).startActivityForResult(intent, StaticConstant.REQUEST_SELL_ITEM);
                        return;
                    }
                }
                break;

            case REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("MESSAGE")) {
                        DialogBox.showDialog(context, context.getString(R.string.app_name), data.getExtras().getString("MESSAGE"), DialogBox.DIALOG_SUCESS, null);
                        return;
                    }
                }
                break;

            case REQ_CODE_CONFIGURE_PAYEMENT_RESULTS:
                if (data != null && data.getExtras() != null && data.getExtras().containsKey("itemid")) {
                    Intent intent = new Intent(this, PostCongActivity.class);
                    if (data.getExtras().containsKey("itemid")) {
                        intent.putExtra("itemid", data.getExtras().getString("itemid"));
                    }

                    if (data.getExtras().containsKey("item_name")) {
                        intent.putExtra("item_name", data.getExtras().getString("item_name"));
                    }

                    if (data.getExtras().containsKey("item_image")) {
                        intent.putExtra("item_image", data.getExtras().getString("item_image"));
                    }
                    startActivityForResult(intent, REQ_CODE_BUY_CONG_ACTIVITY_RESULTS);
                    return;
                }
                break;

            case REQ_CODE_BUY_CONG_ACTIVITY_RESULTS:
//                if (data != null && data.getExtras() != null && data.getExtras().containsKey("FINISH")) {
//                    if (data.getExtras().getBoolean("FINISH")) {
//                        finish();
//                        return;
//                    }
//                }
                if (data != null && data.getExtras() != null && data.getExtras().containsKey("KEEP_SHOPPING")) {
                    if (data.getExtras().getBoolean("KEEP_SHOPPING")) {
                        setFragment(3);
//                        finish();
                        return;
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

    public void showChatPage() {
        Log.d(TAG, "showChatPage: ");
        @SuppressLint("RestrictedApi") List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment != null && fragment instanceof DashboardPagerFragment) {
                    ((DashboardPagerFragment) fragment).viewPager.setCurrentItem(0);
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (fragment != null && fragment.getClass() == FriendsFragment.class) {
            ((FriendsFragment) fragment).callSearchFrdApiFromActivity(charSequence.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Emojix.wrap(newBase, new OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                Log.d(TAG, "onHashTagClicked: ");
            }
        }));
    }

    public void changeFragmentFromNotificationFragment(String type) {
        switch (type) {
            case "4":
                socialPeopleType = "2";
                setFragment(5);
                break;
            case "5":
                socialPeopleType = "0";
                setFragment(5);
                break;
        }

//        MenuItem viewById = (MenuItem) findViewById(R.id.nav_people);
//        onOptionsItemSelected(viewById);

    }

    public void replaceFragment(int id, Bundle bundle) {
        Fragment frag = null;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (id) {
            case ItemDetails.ID:
//                tag = ItemDetails.class.getSimpleName();
                frag = new ItemDetails();
                break;
            case DashboardFragment.ID:
//                tag = Dashboard.class.getSimpleName();
                frag = new DashboardFragment();
                break;
        }

        if (frag != null) {
            if (bundle != null) {
                frag.setArguments(bundle);
            }
        }
        ft.replace(R.id.framlayout, frag).addToBackStack(null).commit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    @SuppressLint("RestrictedApi")
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");

        if (floatingButton != null) {
            if (floatingButton.isMenuOpen()) {
                floatingButton.closeMenu();
                return;
            }
        }

        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment != null) {
                    List<Fragment> childFragmentList = fragment.getChildFragmentManager().getFragments();
                    if (childFragmentList != null) {
                        for (Fragment childFrag : childFragmentList) {
                            if (childFrag != null && childFrag instanceof SocialFragment && childFrag.isVisible()) {
                                ((SocialFragment) childFrag).onBackPressed();
                            }
                        }
                    }
                }
            }
        }
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
//        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
//            if (((DashboardPagerFragment) currentFragment).viewPager.getCurrentItem() == 0) {
//            }
////            PagerAdapter dashboardViewPagerAdapter = ((DashboardPagerFragment) currentFragment).viewPager.getAdapter();
////            if (dashboardViewPagerAdapter != null) {
////                Fragment currentDashboardVpItem = ((DashboardPagerFragment.MyPageAdapter) dashboardViewPagerAdapter).getItem(((DashboardPagerFragment) currentFragment).viewPager.getCurrentItem());
////                if (currentDashboardVpItem != null && currentDashboardVpItem instanceof SocialFragment) {
////                    ((SocialFragment) currentDashboardVpItem).onBackPressed();
////                    return;
////                }
////            }
//        }
        @SuppressLint("RestrictedApi")
        Fragment frg = null;
        for (int i = fragmentList.size() - 1; i >= 0; i--) {
            frg = fragmentList.get(i);
            if (frg != null) {
                break;
            }
        }

        if (frg instanceof ItemDetails) {
            replaceFragment(DashboardFragment.ID, null);
        } else if (frg instanceof DashboardFragment) {
            finish();
        }

    }

    @Override
    public void httpResponse(String type, Response response) {

    }

    @Override
    public void navigate(int id, int layoutID, Bundle bundle) {
        replaceFragment(id, bundle);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void islogout(boolean status) {

    }

    @Override
    public void isMenuClick(int id) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void clickCloseCart()
    {
        setFragment(1);
    }

//
//    @Override
//    public void onUserLeaveHint()
//    {
//        super.onUserLeaveHint();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            if (hasPip(DrawerMainActivity.this))
//            {
//                if (!isInPictureInPictureMode())
//                {
//                    Rational aspectRatio = new Rational(250, 150);
//                    pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
//                    enterPictureInPictureMode(pictureInPictureParamsBuilder.build());
//                }
//            }
//        }
//    }

    public void enterInPipMode()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if (hasPip(DrawerMainActivity.this))
            {
                if (!isInPictureInPictureMode()) {
                    Rational aspectRatio = new Rational(250, 150);
                    pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
                    // updatePictureInPictureActions(R.drawable.ic_stop_white_24dp, "Play", CONTROL_TYPE_PLAY, REQUEST_PLAY);
                    setPictureInPictureActions(R.drawable.ic_stop_white_24dp, R.drawable.next, "Play", CONTROL_TYPE_PLAY, REQUEST_PLAY);
                    enterPictureInPictureMode(pictureInPictureParamsBuilder.build()); // INTERNAL METHOD
                }
            }
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (isInPictureInPictureMode) {
                floatingButton.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                IsPipMode = true;

                registerReceiver(mReceiver, new IntentFilter(ACTION_MEDIA_CONTROL));
                chamgeFragmentControlsVisibility("show");
            }
            else
                {
                unregisterReceiver(mReceiver);
                // Show the video controls if the video is not playing
                floatingButton.setVisibility(View.VISIBLE);
                IsPipMode = false;
                chamgeFragmentControlsVisibility("hide");

            }
        }
    }

    public boolean hasPip(Context context)
    {
        PackageManager pckMgr = context.getPackageManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            boolean flag = pckMgr.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
            if (flag)
                has_pip = true;
            else
                has_pip = false;
        }
        return has_pip;
    }

    BroadcastReceiver  mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (intent == null || !ACTION_MEDIA_CONTROL.equals(intent.getAction()))
            {
                return;
            }

            // This is where we are called back from Picture-in-Picture action items.

            final int controlType = intent.getIntExtra(EXTRA_CONTROL_TYPE, 0);
            switch (controlType)
            {
                case CONTROL_TYPE_PLAY:  //PLAY==1
                    chamgeSelection();
                    break;
                case CONTROL_TYPE_PAUSE:  //PAUSE==2
                    chamgeSelection();
                    break;
                case CONTROL_TYPE_NEXT:
                    playNextSong();
                    break;
                case CONTROL_TYPE_PREVIOUS:
                    playPreviousSong();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        activity_Created = true;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        activity_Created = false;
    }

    public void updatePictureInPictureActions(@DrawableRes int iconId, String title, int controlType, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            final ArrayList<RemoteAction> actions = new ArrayList<>();
            final PendingIntent intent = PendingIntent.getBroadcast(DrawerMainActivity.this, requestCode, new Intent(ACTION_MEDIA_CONTROL).putExtra(EXTRA_CONTROL_TYPE, controlType), 0);
            final Icon icon = Icon.createWithResource(DrawerMainActivity.this, iconId);

            if (actions.size() > 0)
            {
                actions.clear();
            }

            actions.add(new RemoteAction(icon, title, title, intent));
            pictureInPictureParamsBuilder.setActions(actions);
            // This is how you can update action items (or aspect ratio) for Picture-in-Picture mode.
            // Note this call can happen even when the app is not in PiP mode. In that case, the
            // arguments will be used for at the next call of #enterPictureInPictureMode.
            setPictureInPictureParams(pictureInPictureParamsBuilder.build());
        }
    }

    public void setPictureInPictureActions(@DrawableRes int iconId, @DrawableRes int iconIdnew, String title, int controlType, int requestCode)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            final ArrayList<RemoteAction> actions = new ArrayList<>();

            final PendingIntent intent = PendingIntent.getBroadcast(DrawerMainActivity.this, requestCode,
                    new Intent(ACTION_MEDIA_CONTROL).putExtra(EXTRA_CONTROL_TYPE, controlType), 0);

            final PendingIntent intentnew = PendingIntent.getBroadcast(DrawerMainActivity.this, CONTROL_TYPE_NEXT,
                    new Intent(ACTION_MEDIA_CONTROL).putExtra(EXTRA_CONTROL_TYPE, CONTROL_TYPE_NEXT), 0);

            final PendingIntent intent_previous = PendingIntent.getBroadcast(DrawerMainActivity.this, CONTROL_TYPE_PREVIOUS,
                    new Intent(ACTION_MEDIA_CONTROL).putExtra(EXTRA_CONTROL_TYPE, CONTROL_TYPE_PREVIOUS), 0);

            final Icon icon = Icon.createWithResource(DrawerMainActivity.this, iconId);
            final Icon iconnew = Icon.createWithResource(DrawerMainActivity.this, iconIdnew);
            final Icon icon_previous = Icon.createWithResource(DrawerMainActivity.this, R.drawable.previous);

            if (actions.size() > 0) {
                actions.clear();
            }

            actions.add(new RemoteAction(icon_previous, "Previous", "Previous", intent_previous));
            actions.add(new RemoteAction(icon, title, title, intent));
            actions.add(new RemoteAction(iconnew, "Next", "Next", intentnew));

            pictureInPictureParamsBuilder.setActions(actions);
            // This is how you can update action items (or aspect ratio) for Picture-in-Picture mode.
            // Note this call can happen even when the app is not in PiP mode. In that case, the
            // arguments will be used for at the next call of #enterPictureInPictureMode.
            setPictureInPictureParams(pictureInPictureParamsBuilder.build());
        }
    }

    void chamgeSelection() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
            DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
            if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(1);
                if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment && ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()) != null) {
                    ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()).setPipControls();
                }
            }
        }
    }

    void chamgeFragmentControlsVisibility(String status) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
            DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
            if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(1);
                if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment && ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()) != null) {
                    ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()).setControlsVisibility(status);
                }
            }
        }
    }

    void playNextSong()
    {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
            DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
            if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(1);
                if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment && ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()) != null) {
                    ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()).playNextItem();
                }
            }
        }
    }

    private void playPreviousSong()
    {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framlayout);
        if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
            DashboardPagerFragment dashboardPagerFragment = (DashboardPagerFragment) currentFragment;
            if (currentFragment != null && currentFragment instanceof DashboardPagerFragment) {
                Fragment currentVpFrag = dashboardPagerFragment.getFragmentFromViewpager(1);
                if (currentVpFrag != null && currentVpFrag instanceof DashboardFragment && ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()) != null) {
                    ((DashboardAdapter) ((DashboardFragment) currentVpFrag).mrecyclerView.getAdapter()).playPreviousItem();
                }
            }
        }
    }
}