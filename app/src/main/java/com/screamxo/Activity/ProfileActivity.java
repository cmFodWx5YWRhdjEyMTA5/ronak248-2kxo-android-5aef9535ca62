package com.screamxo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.FriendBean;
import com.example.apimodule.ApiBase.ApiBean.GetUserDetailBean;
import com.example.apimodule.ApiBase.ApiBean.UserBean;
import com.example.apimodule.ApiBase.ApiBean.UserResult;
import com.example.apimodule.ApiBase.ApiBean.Userdetail;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.screamxo.Activity.RahulWork.BuyCongActivity;
import com.screamxo.Activity.RahulWork.ItemBuyFinal;
import com.screamxo.Activity.RahulWork.PostCongActivity;
import com.screamxo.Activity.RahulWork.UploadDataActivity;
import com.screamxo.Emoji.Emojix;
import com.screamxo.Emoji.tagging.OnHashTagClickListener;
import com.screamxo.Fragment.ProfileFragment;
import com.screamxo.Interface.FriendActionInterface;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_BUY_CONG_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CHAT_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CONFIGURE_PAYEMENT_RESULTS;

public class ProfileActivity extends AppCompatActivity implements FriendActionInterface {

    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Boolean isLoginUser = false;
    Fragment fragment;
    private Activity activity;
    private Context context;
    private FriendActionInterface friendactioninterface;
    private Preferences preferences;
    private Call<GetUserDetailBean> GetUserInfoCall;
    private FetchrServiceBase mService;
    private Call<FriendBean> friendBeanCall;
    private String friendshType, frdshipId;
    private int isFriend = 1;
    Userdetail userinfo;


    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: ");
        Utils.printIntentData(getIntent());
        ButterKnife.bind(this);
        getWindow().setStatusBarColor(Color.BLACK);
        init();
        initControl();
        initControlValue();
        initControlListener();
        initFabIcon();
        callGetUserMoreInfoApi();
    }

    public void callGetUserMoreInfoApi() {
        Map<String, String> map = new HashMap<>();
        map.put("userid", getIntent().getExtras().getString("uid"));
        if (Utils.isInternetOn(context)) {
            // dialog_progreessbar.setVisibility(View.VISIBLE);

            GetUserInfoCall = mService.getFetcherService(context).GetUserInfo(map);
            GetUserInfoCall.enqueue(new Callback<GetUserDetailBean>() {
                @Override
                public void onResponse(Call<GetUserDetailBean> call, Response<GetUserDetailBean> response) {
                    //   dialog_progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null && response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                        userinfo = response.body().getResult().getUserdetail();
                    }
                }

                @Override
                public void onFailure(Call<GetUserDetailBean> call, Throwable t) {
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
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
                    Utils.hideKeyboard(ProfileActivity.this);
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.cross));

                    sbSearch.setBackground(ContextCompat.getDrawable(ProfileActivity.this, R.drawable.floating_chat));
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
                    Intent intent = new Intent(ProfileActivity.this, UploadDataActivity.class);
                    startActivity(intent);
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

    public void updateValue(String data) {
        if (fragment instanceof ProfileFragment) {
            ((ProfileFragment) fragment).passData(data);
        }
    }

    private void init() {
        activity = this;
        context = this;
        mService = new FetchrServiceBase();
        friendactioninterface = this;
        preferences = new Preferences(this);
        toolbar.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("isFriend")) {
            isFriend = bundle.getInt("isFriend");
        }
    }

    private void initControl() {
        fragment = new ProfileFragment();
        fragment.setArguments(getIntent().getExtras());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framlayout, fragment, ProfileFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    private void initControlValue() {
        txtToolbarTitle.setText(getString(R.string.title_activity_drawer_screamxo));

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("uid") && preferences.getUserId().equals(getIntent().getExtras().getString("uid"))) {
            imgToolbarRightIcon.setImageDrawable(getResources().getDrawable(R.mipmap.edit_pencil));
            isLoginUser = true;
        } else {
            imgToolbarRightIcon.setEnabled(false);
            imgToolbarRightIcon.setImageDrawable(getResources().getDrawable(R.mipmap.ico_more));
            isLoginUser = false;
        }
        imgToolbarRightIcon.setVisibility(View.VISIBLE);
    }

    private void initControlListener() {

        imgToolbarRightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoginUser) {
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    intent.putExtra("screen", ProfileActivity.class.getSimpleName());
                    startActivity(intent);
                } else {
//                    friendshType=   0=sent,1=accpeted,2=rejected,3=blocked,4=unbloked,5=unfriend,6=cancelled
                    DialogBox.showAlertFriendAction(activity, friendactioninterface, friendshType);
                }
            }
        });

        imgToolbarLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClickBlock() {
        callBlockApi();
    }

    @Override
    public void onClickUnFriend() {
        callApiFromSelection();
    }

    private void callApiFromSelection() {

        switch (friendshType) {
            case "0":
                callAddFrd();
                break;

            case "1":
                callUnfrdApi();
                break;

            case "2":
                callCancleFrdRequest();
                break;

            case "3":
                callAcceptFrdRequestApi();
                break;
        }
    }

    @Override
    public void onClickMessage() {
        if (friendshType.equals("1")) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("otherUid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));
            intent.putExtra("username", getIntent().getExtras().getString("username"));
            intent.putExtra("userProfile", "");
            intent.putExtra("uFullName", BundleUtils.getIntentExtra(getIntent(), "uFullName", ""));
            startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
        } else if (friendshType.equals("3")) {
            callRejectFrdRequest();
        }
    }

    @Override
    public void onClickUnBlock() {
        callUnBlockApi();
    }

    public void getFrdidAndType(String frdid, String frdType) {
        //                    friendshType=   0=sent,1=accpeted,2=rejected,3=blocked,4=unbloked,5=unfriend,6=cancelled
        frdshipId = frdid;
        friendshType = frdType;
        imgToolbarRightIcon.setEnabled(true);
    }

    public void callBlockApi() {
        Map<String, String> map = new HashMap<>();
        map.put("fromid", preferences.getUserId());
        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        if (Utils.isInternetOn(context)) {

            GetUserInfoCall = mService.getFetcherService(context).BlockUser(map);
            GetUserInfoCall.enqueue(new Callback<GetUserDetailBean>() {
                @Override
                public void onResponse(Call<GetUserDetailBean> call, Response<GetUserDetailBean> response) {
//                    if (response.code() == StaticConstant.RESULT_OK) {
                    Utils.showToast(context, "Blocked Successfully");
//                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
//                            finish();
//                        }

                    if (fragment instanceof ProfileFragment)
                        ((ProfileFragment) fragment).isBlock = 1;
//                    }
                }

                @Override
                public void onFailure(Call<GetUserDetailBean> call, Throwable t) {
                    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void callAcceptFrdRequestApi() {
        if (Utils.isInternetOn(context)) {

            HashMap<String, String> map = new HashMap<>();
            map.put("fromid", preferences.getUserId());
            map.put("toid", "" + BundleUtils.getIntentExtra(getIntent(), "uid", ""));
            map.put("friendshipid", "" + frdshipId);

            friendBeanCall = mService.getFetcherService(context).acceptFrdRequest(map);
            friendBeanCall.enqueue(new Callback<FriendBean>() {
                @Override
                public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            friendshType = "1";

                            updateValue("1");
                        }
                    }
                }

                @Override
                public void onFailure(Call<FriendBean> call, Throwable t) {
                    t.printStackTrace();
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    //    Reject request (Other User send request than)
    void callRejectFrdRequest() {
        if (Utils.isInternetOn(context)) {

            HashMap<String, String> map = new HashMap<>();
            map.put("fromid", preferences.getUserId());
            map.put("toid", "" + BundleUtils.getIntentExtra(getIntent(), "uid", ""));
//            map.put("friendshipid", "" + ((ProfileFragment) fragment).friendshipid);

            friendBeanCall = mService.getFetcherService(context).rejectFrdRequest(map);

            friendBeanCall.enqueue(new Callback<FriendBean>() {
                @Override
                public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            friendshType = "0";
                            updateValue("0");
                        }
                    }
                }

                @Override
                public void onFailure(Call<FriendBean> call, Throwable t) {
                    t.printStackTrace();
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    // Cancel Request (Login User send request and want to cancel request)
    public void callCancleFrdRequest() {
        if (Utils.isInternetOn(context)) {
            HashMap<String, String> map = new HashMap<>();
//            map.put("fromid", preferences.getUserId());
            map.put("toid", "" + BundleUtils.getIntentExtra(getIntent(), "uid", ""));
            map.put("friendshipid", "" + frdshipId);
            friendBeanCall = mService.getFetcherService(context).CancelFrdRequest(map);
            friendBeanCall.enqueue(new Callback<FriendBean>() {
                @Override
                public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            friendshType = "0";
                            //  StaticConstant.REQUESTFLAG = true;
                            updateValue("0");
                        }
                    }
                }

                @Override
                public void onFailure(Call<FriendBean> call, Throwable t) {
                    t.printStackTrace();
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void callUnfrdApi() {
        if (Utils.isInternetOn(context)) {

            HashMap<String, String> map = new HashMap<>();
            map.put("fromid", preferences.getUserId());
            map.put("toid", "" + BundleUtils.getIntentExtra(getIntent(), "uid", ""));
            map.put("friendshipid", frdshipId);

            friendBeanCall = mService.getFetcherService(context).UnFrdRequest(map);

            friendBeanCall.enqueue(new Callback<FriendBean>() {
                @Override
                public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            friendshType = "" + 0;
                            updateValue("0");
                        }
                    }
                }

                @Override
                public void onFailure(Call<FriendBean> call, Throwable t) {
                    t.printStackTrace();
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    //    send Frd request
    public void callAddFrd() {
        if (Utils.isInternetOn(context)) {

            HashMap<String, String> map = new HashMap<>();
            map.put("fromid", preferences.getUserId());
            map.put("toid", "" + BundleUtils.getIntentExtra(getIntent(), "uid", ""));
//            map.put("friendshipid", "" + ((ProfileFragment) fragment).friendshipid);

            friendBeanCall = mService.getFetcherService(context).sendFrdRequest(map);

            friendBeanCall.enqueue(new Callback<FriendBean>() {
                @Override
                public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            friendshType = "" + 2;
                            frdshipId = "" + response.body().getResult().getTotalcount();
                            updateValue("2");
                            if (fragment instanceof ProfileFragment) {
                                ((ProfileFragment) fragment).isMyFriend = 0;
                            }
                        }
                    } else if (response.errorBody() != null) {
                        try {
                            FriendBean friendBean = new Gson().fromJson(response.errorBody().string(), FriendBean.class);
                            Utils.showToast(context, friendBean.getMsg());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(Call<FriendBean> call, Throwable t) {
                    t.printStackTrace();
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void callUnBlockApi() {
        Map<String, String> map = new HashMap<>();

        map.put("fromid", preferences.getUserId());
        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        if (Utils.isInternetOn(context)) {

            GetUserInfoCall = mService.getFetcherService(context).UnBlockUser(map);

            GetUserInfoCall.enqueue(new Callback<GetUserDetailBean>() {
                @Override
                public void onResponse(Call<GetUserDetailBean> call, Response<GetUserDetailBean> response) {
                    Utils.showToast(context, "Unblocked Successfully");

                    if (fragment instanceof ProfileFragment)
                        ((ProfileFragment) fragment).isBlock = 0;
                }

                @Override
                public void onFailure(Call<GetUserDetailBean> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Emojix.wrap(newBase, new OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {

            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("FRAGMENT_INDEX")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", data.getExtras().getInt("FRAGMENT_INDEX"));
                setResult(RESULT_OK, returnIntent);
                finish();
                return;
            }
        }

        switch (requestCode) {
            case StaticConstant.REQUEST_SELL_ITEM:
                if (data != null && data.getExtras() != null && data.getExtras().containsKey("POST_ANOTHER")) {
                    Intent intent = new Intent(context, com.screamxo.Activity.RahulWork.SellItemActivity.class);
                    ((Activity) context).startActivityForResult(intent, StaticConstant.REQUEST_SELL_ITEM);
                    return;
                }
                break;

            case REQ_CODE_CONFIGURE_PAYEMENT_RESULTS:
                if (data != null && data.getExtras() != null && data.getExtras().containsKey("itemid")) {
                    Intent intent = new Intent(this, ItemBuyFinal.class);
                    if (data.getExtras().containsKey("itemid")) {
                        if (data != null && data.getExtras() != null && data.getExtras().containsKey("itemid")) {


                            intent.putExtra("otherUid", userinfo.getId());
                            intent.putExtra("username", userinfo.getUsername());
                            intent.putExtra("userProfile", userinfo.getUserphotothumb());
                            intent.putExtra("fullname", userinfo.getFirstname() + " " + userinfo.getLastname());

                            intent.putExtra("item_image", data.getStringExtra("item_image"));
                            intent.putExtra("item_name", data.getStringExtra("item_name"));
                            intent.putExtra("itemid", data.getStringExtra("itemid"));
                            startActivityForResult(intent, REQ_CODE_BUY_CONG_ACTIVITY_RESULTS);
                        }
                    }
/*
                    if (data.getExtras().containsKey("item_name")) {
                        intent.putExtra("item_name", data.getExtras().getString("item_name"));
                    }

                    if (data.getExtras().containsKey("item_image")) {
                        intent.putExtra("item_image", data.getExtras().getString("item_image"));
                    }*/
//                    startActivityForResult(intent, REQ_CODE_BUY_CONG_ACTIVITY_RESULTS);
                    return;
                }
                break;

            case REQ_CODE_BUY_CONG_ACTIVITY_RESULTS:
                if (data != null && data.getExtras() != null && data.getExtras().containsKey("FINISH")) {
                    if (data.getExtras().getBoolean("FINISH")) {
                        finish();
                        return;
                    }
                }
                if (data != null && data.getExtras() != null && data.getExtras().containsKey("KEEP_SHOPPING")) {
                    if (data.getExtras().getBoolean("KEEP_SHOPPING")) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("FRAGMENT_INDEX", 3);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                        return;
                    }
                }
                break;
        }
    }
}


