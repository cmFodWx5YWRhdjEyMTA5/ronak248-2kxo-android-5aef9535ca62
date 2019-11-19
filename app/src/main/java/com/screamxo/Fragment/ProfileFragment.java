package com.screamxo.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.GetUserDetailBean;
import com.example.apimodule.ApiBase.ApiBean.ProfileMedia.Posts;
import com.example.apimodule.ApiBase.ApiBean.SendMsgBean;
import com.example.apimodule.ApiBase.ApiBean.UserBean;
import com.example.apimodule.ApiBase.ApiBean.UserResult;
import com.example.apimodule.ApiBase.ApiBean.Userdetail;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.ChatActivity;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Activity.EditProfileActivity;
import com.screamxo.Activity.ProfileActivity;
import com.screamxo.Activity.PurchaseHistoryActivity;
import com.screamxo.Activity.RahulWork.FriendsActivity;
import com.screamxo.Activity.RahulWork.SendMoneyActivity;
import com.screamxo.Activity.SoldHistoryActivity;
import com.screamxo.Activity.WatchedItemListActivity;
import com.screamxo.Activity.wallet.WalletSendReceiveActivity;
import com.screamxo.Others.CustomViewPager;
import com.screamxo.Others.MyPopUpWindow;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.ImageUtils;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.RequestBodyConveter;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CHAT_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_FRIENDS_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_PURCHASE_HISTORY_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_SOLD_HISTORY_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_WALLET_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_WATCH_ITEM_LIST_ACTIVITY_RESULTS;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    public String friendshipid, frdShipType = "-1";
    public Boolean isFriend = false, isBlock = false;
    public int isMyFriend;
    Context context;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.li_frnd)
    LinearLayout li_frnd;

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    CustomViewPager viewpager;
    ViewPagerAdapter viewPagerAdapter;
    Fragment fragment;
    @BindView(R.id.txt_item)
    TextView txtItem;
    @BindView(R.id.txt_item_label)
    TextView txt_item_label;

    @BindView(R.id.user_profile)
    ImageView imgUserProfile;
    @BindView(R.id.txt_frd)
    TextView txtFrd;
    @BindView(R.id.txt_frd_frnd)
    TextView txt_frd_frnd;

    @BindView(R.id.txt_user_name)
    TextView txtUserName;
    @BindView(R.id.progreessbar)
    ProgressBar progressBar;
    @BindView(R.id.linear_profile)
    ConstraintLayout linearLayout;
    @BindView(R.id.txt_user_unique_name)
    TextView txtUserUniqueName;

    @BindView(R.id.more_info_iv)
    ImageView more_info_iv;

    @BindView(R.id.txt_message)
    TextView txtMessage;

    @BindView(R.id.send_money_iv)
    ImageView send_money_iv;

    @BindView(R.id.friends_iv)
    ImageView friends_iv;

    @BindView(R.id.img_edit)
    ImageView img_edit;
    @BindView(R.id.liner_message)
    LinearLayout linerMessage;
    @BindView(R.id.scroll_msg)
    ScrollView scrollMsg;
    @BindView(R.id.rl_Header)
    RelativeLayout rl_Header;
    UserResult userdetail;
    Userdetail userinfo;
    Preferences preferences;
    Call<SendMsgBean> sendMsgBeanCall;
    RequestBodyConveter requestbodyconverter;
    private String userName;
    private Call<UserBean> UserApiCall;
    private Call<GetUserDetailBean> GetUserInfoCall;
    private FetchrServiceBase mService;
    private TextView txtrelationstatus, txtsex, txtsexpreference, txtschool, txtlocation, txtjob, txthobbies, txtUsername, txtFullName;
    private Boolean isLoginUser = false;
    private ProgressBar dialog_progreessbar;
    private Call<Posts> UserApi;
    private String uId;
    private String[] strings = null;
    private String[] strings3 = null;
    private String[] strings4 = null;
    private String[] strings2 = null;
    private String[] strings1 = null;
    private String username, fullname;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment_new, container, false);
        ButterKnife.bind(this, view);
        init();
        initBundleDataAndViews();
        initControlValue();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        initBundleDataAndViews();
    }

    private void initBundleDataAndViews() {
        Utils.printBundleData(this.getArguments());

        if (getArguments().containsKey("isFriend")) {
            isMyFriend = getArguments().getInt("isFriend");
        }

        if (getArguments().containsKey("friendshipId")) {
            friendshipid = "" + getArguments().getInt("friendshipId");
        }

        if (BundleUtils.getBundleExtra(getArguments(), "uid", "").equalsIgnoreCase(preferences.getUserId())) {
            userName = preferences.getUserName();
        } else {
            userName = BundleUtils.getBundleExtra(getArguments(), "username", "");
        }
        callUserInfoApi();

        if (preferences.getUserId().equals(BundleUtils.getBundleExtra(getArguments(), "uid", ""))) {
            isLoginUser = true;
            more_info_iv.setVisibility(View.VISIBLE);
        }
        linearLayout.setVisibility(View.INVISIBLE);
    }

    public void init() {
        preferences = new Preferences(context);
        mService = new FetchrServiceBase();
        requestbodyconverter = new RequestBodyConveter();
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        callGetUserMoreInfoApi();
    }

    public void initControlValue() {
        Log.d(TAG, "initControlValue: ");
        viewPagerAdapter.addFragment(new MediaFragment(), getString(R.string.txt_media));
        viewPagerAdapter.addFragment(new ShopFragmentView(isLoginUser), getString(R.string.txt_shop));
        viewPagerAdapter.addFragment(new StreamFragment(), getString(R.string.txt_stream));
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setOffscreenPageLimit(3);
        tabs.setupWithViewPager(viewpager);
    }

    @SuppressLint("SetTextI18n")
    public void setUserInfoData() {
        if (userinfo != null) {
            if (userinfo.getRealtionstatus() != null && !userinfo.getRealtionstatus().equals("")) {
                String status = userinfo.getRealtionstatus();
                switch (status) {
                    case "a":
                        txtrelationstatus.setText(getString(R.string.txt_available));
                        break;
                    case "u":
                        txtrelationstatus.setText(getString(R.string.txt_unavailable));
                        break;
                }
            }

            if (userinfo.getGender() != null && !userinfo.getGender().equals("")) {
                String gender = userinfo.getGender();
                switch (gender) {
                    case "m":
                        txtsex.setText(getString(R.string.txt_male));
                        break;
                    case "f":
                        txtsex.setText(getString(R.string.txt_female));
                        break;
                    case "t":
                        txtsex.setText(getString(R.string.txt_transgender));
                        break;
                }
            }

            if (userinfo.getSexpreference() != null && !userinfo.getSexpreference().equals("")) {
                String sexpreference = userinfo.getSexpreference();
                switch (sexpreference) {
                    case "o":
                        txtsexpreference.setText(getString(R.string.txt_opposite));
                        break;
                    case "s":
                        txtsexpreference.setText(getString(R.string.txt_same));
                        break;
                }
            }
            if (userinfo.getSchool() != null && !userinfo.getSchool().equals("")) {
                txtschool.setText(userinfo.getSchool());
            }
            if (userinfo.getJob() != null && !userinfo.getJob().equals("")) {
                txtjob.setText(userinfo.getJob());
            }
            if (userinfo.getState() != null && !userinfo.getState().equals("")) {
                txtlocation.setText(userinfo.getState());
            }
            if (userinfo.getHobbies() != null && !userinfo.getHobbies().equals("")) {
                txthobbies.setText(userinfo.getHobbies());
            }

            if (isLoginUser) {
                txtFullName.setText(userinfo.getFname() + " " + userinfo.getLname());
                if (userinfo.getUsername() != null) {
                    txtUsername.setText(userinfo.getUsername());
                } else if (userinfo.getFname() != null) {
                    txtUsername.setText(userinfo.getFname());
                }
            } else {
                txtFullName.setText(BundleUtils.getBundleExtra(getArguments(), "uFullName", ""));
                if (userinfo.getUsername() != null) {
                    txtUsername.setText(userinfo.getUsername());
                } else if (userinfo.getFname() != null) {
                    txtUsername.setText(userinfo.getFname());
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void setUserdata() {
        try {
            if (context.getClass() == ProfileActivity.class) {
                ((ProfileActivity) context).getFrdidAndType(friendshipid, frdShipType);
            }

            if (userinfo != null) {
                txtItem.setText(String.valueOf(userinfo.getTotalItem()));
                txtFrd.setText(String.valueOf(userinfo.getTotalFriend()));

                if (userinfo.getTotalItem() > 1) {
                    txt_item_label.setText(getString(R.string.txt_items));
                }
            }

            li_frnd.setOnClickListener(v -> startActivityForResult(new Intent(context, FriendsActivity.class), REQ_CODE_FRIENDS_ACTIVITY_RESULTS));
            if (BundleUtils.getBundleExtra(getArguments(), "uid", "").equalsIgnoreCase(preferences.getUserId())) {
                txtUserName.setText(preferences.getUserFullName());
                txtFullName.setText(preferences.getUserFullName());
                fullname = preferences.getUserFullName();
            } else {
                txtUserName.setText(BundleUtils.getBundleExtra(getArguments(), "uFullName", ""));
                txtFullName.setText(BundleUtils.getBundleExtra(getArguments(), "uFullName", ""));
                fullname = BundleUtils.getBundleExtra(getArguments(), "uFullName", "");
            }

            if (!userName.isEmpty()) {
                txtUserUniqueName.setText("@" + userName);
                txtUsername.setText(userName);
                username = userName;
            }
            txtUserUniqueName.setTextColor(Color.GRAY);

            // uid now static pls chnage when complete

            if (!BundleUtils.getBundleExtra(getArguments(), "uProfile", "").isEmpty()) {
                Picasso.with(context)
                        .load(BundleUtils.getBundleExtra(getArguments(), "uProfile", null))
                        .error(R.mipmap.pic_holder_dashboard)
                        .placeholder(R.mipmap.pic_holder_dashboard)
                        .transform(new CircleTransform())
                        .into(imgUserProfile);

                imgUserProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageUtils.customChoosePhoto(context, BundleUtils.getBundleExtra(getArguments(), "uProfile", null));
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "setUserdata: ", e);
        }
    }

    public String getUid() {
        return BundleUtils.getBundleExtra(getArguments(), "uid", "");
    }

    public void callGetUserMoreInfoApi() {
        Map<String, String> map = new HashMap<>();
        map.put("userid", BundleUtils.getBundleExtra(getArguments(), "uid", ""));
        if (Utils.isInternetOn(context)) {
            // dialog_progreessbar.setVisibility(View.VISIBLE);

            GetUserInfoCall = mService.getFetcherService(context).GetUserInfo(map);
            GetUserInfoCall.enqueue(new Callback<GetUserDetailBean>() {
                @Override
                public void onResponse(Call<GetUserDetailBean> call, Response<GetUserDetailBean> response) {
                    //   dialog_progreessbar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null && response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                        userinfo = response.body().getResult().getUserdetail();
                        String url = userinfo.getPhotothumb();
                        txtFrd.setText(String.valueOf(userinfo.getTotalFriend()));
                        txtItem.setText(String.valueOf(userinfo.getTotalItem()));
                        try {
                            if (userinfo.getTotalItem() > 1) {
                                txt_item_label.setText(getString(R.string.txt_items));
                            }
                        } catch (Exception e) {
                            Log.e("Exc", e.getMessage());
                        }

                        if (userinfo.getPhoto() != null)
                            Picasso.with(context)
                                    .load(userinfo.getPhoto())
                                    .error(R.mipmap.pic_holder_dashboard)
                                    .placeholder(R.mipmap.pic_holder_dashboard)
                                    .transform(new CircleTransform())
                                    .into(imgUserProfile);
                    }
                }

                @Override
                public void onFailure(Call<GetUserDetailBean> call, Throwable t) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void callUserInfoApi() {
        Map<String, String> map = new HashMap<>();

        // uid now static pls chnage when complete
        map.put("uid", BundleUtils.getBundleExtra(getArguments(), "uid", ""));
        map.put("offset", "1");
        map.put("limit", StaticConstant.LIMIT);
        map.put("posttype", "1");

        if (Utils.isInternetOn(context)) {
            progressBar.setVisibility(View.VISIBLE);

            UserApiCall = mService.getFetcherService(context).UserDetail(map);

            UserApiCall.enqueue(new Callback<UserBean>() {
                @Override
                public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            userdetail = response.body().getResult();
                            if (response.body().getResult().getFriendshipid() != null)
                                friendshipid = "" + response.body().getResult().getFriendshipid();
                            frdShipType = "" + response.body().getResult().getFriendshipstate();
                            isFriend = userdetail.getFriendshipstate() == 1 ? true : false;
                            //  isMyFriend = userdetail.getFriendshipstate();
                            setUserdata();
                        } else {
                            ((Activity) context).finish();
                        }
                    }
                }

                //
                @Override
                public void onFailure(Call<UserBean> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callSendMsgApi(String msg) {
        linerMessage.setVisibility(View.GONE);
        scrollMsg.setVisibility(View.GONE);
        if (Utils.isInternetOn(context)) {
            progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("fromid", "" + preferences.getUserId());
            map.put("toid", BundleUtils.getBundleExtra(getArguments(), "uid", ""));
            map.put("messagetype", "1");
            map.put("messagedetail", msg);
            sendMsgBeanCall = mService.getFetcherService(context).sendMsg(requestbodyconverter.converRequestBodyFromMap(map), null);

            sendMsgBeanCall.enqueue(new Callback<SendMsgBean>() {
                @Override
                public void onResponse(Call<SendMsgBean> call, Response<SendMsgBean> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<SendMsgBean> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @OnClick({R.id.more_info_iv, R.id.txt_message, R.id.txt_direct, R.id.txt_good_morning, R.id.txt_good_evening, R.id.txt_good_night
            , R.id.txt_saying_hello, R.id.txt_what_doing, R.id.txt_sos
            , R.id.img_edit, R.id.txt_thanks, R.id.txt_sending_love, R.id.txt_on_way, R.id.txt_ur_favourite_song
            , R.id.txt_ur_favourite_movie, R.id.txt_ur_favourite_me, R.id.txt_call_me, R.id.friends_iv, R.id.send_money_iv})

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_info_iv:
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View popupView;
                if (layoutInflater == null) {
                    return;
                }
                popupView = layoutInflater.inflate(R.layout.dialog_user_info, null);
                PopupWindow popupWindow;
                popupWindow = new PopupWindow(popupView, Utils.dip2pixel(context, 200), Utils.dip2pixel(context, 250));
                popupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);

                txtrelationstatus = popupView.findViewById(R.id.txt_relative_status_value);
                txtsex = popupView.findViewById(R.id.txt_sex);
                txtsexpreference = popupView.findViewById(R.id.txt_sex_preference);
                txtschool = popupView.findViewById(R.id.txt_school);
                txtjob = popupView.findViewById(R.id.txt_job);
                txtlocation = popupView.findViewById(R.id.txt_location);
                txthobbies = popupView.findViewById(R.id.txt_hobby);
                dialog_progreessbar = popupView.findViewById(R.id.progreessbar);
                txtUsername = popupView.findViewById(R.id.txt_username);
                txtFullName = popupView.findViewById(R.id.txt_full_name);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        dialog_progreessbar.setVisibility(View.GONE);
                    }
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    popupWindow.showAsDropDown(more_info_iv, -130, 0, Gravity.CENTER | Gravity.TOP);
                } else {
                    popupWindow.showAsDropDown(more_info_iv,
                            -130, 0);
                }

                if (preferences != null) {
                    txtFullName.setText(preferences.getUserFullName());
                    txtUsername.setText(preferences.getUserName());
                }

                callGetUserMoreInfoApi();
                setUserInfoData();
                break;

            case R.id.txt_message:
                if (scrollMsg.getVisibility() == View.GONE) {
                    scrollMsg.setVisibility(View.VISIBLE);
                } else {
                    scrollMsg.setVisibility(View.GONE);
                }
                if (linerMessage.getVisibility() == View.GONE) {
                    linerMessage.setVisibility(View.VISIBLE);
                } else {
                    linerMessage.setVisibility(View.GONE);
                }
                break;
            case R.id.txt_direct:
                callIntentScreen();
                break;
            case R.id.txt_good_morning:
                callSendMsgApi(getString(R.string.txt_good_morning));
                break;
            case R.id.txt_good_evening:
                callSendMsgApi(getString(R.string.txt_good_evening));
                break;
            case R.id.txt_good_night:
                callSendMsgApi(getString(R.string.txt_good_night));
                break;
            case R.id.txt_saying_hello:
                callSendMsgApi(getString(R.string.txt_saying_hello));
                break;
            case R.id.txt_what_doing:
                callSendMsgApi(getString(R.string.txt_what_you_doing));
                break;
            case R.id.txt_sos:
                callSendMsgApi(getString(R.string.txt_sos));
                break;
            case R.id.txt_thanks:
                callSendMsgApi(getString(R.string.txt_than_you));
                break;
            case R.id.txt_sending_love:
                callSendMsgApi(getString(R.string.txt_my_love));
                break;
            case R.id.txt_on_way:
                callSendMsgApi(getString(R.string.txt_on_my_way));
                break;
            case R.id.txt_ur_favourite_song:
                callSendMsgApi(getString(R.string.txt_whats_ur_fav_song));
                break;
            case R.id.txt_ur_favourite_movie:
                callSendMsgApi(getString(R.string.txt_whats_ur_fav_movie));
                break;
            case R.id.txt_ur_favourite_me:
                callSendMsgApi(getString(R.string.txt_whtas_ur_fav_meal));
                break;
            case R.id.txt_call_me:
                callSendMsgApi(getString(R.string.txt_call_me));
                break;
            case R.id.img_edit:
                openPopUpmenu();
                break;
            case R.id.friends_iv:
                if (preferences.getUserId().equals(BundleUtils.getBundleExtra(getArguments(), "uid", ""))) {
                    if (context instanceof ProfileActivity) {
                        loadMessenger();
                    } else if (context instanceof DrawerMainActivity) {
                        ((DrawerMainActivity) context).setFragment(1, 0);
                    }
                } else {
                    openChatActivity();
                }

                break;
            case R.id.send_money_iv:
                startActivityForResult(new Intent(context, WalletSendReceiveActivity.class).putExtra("uid", BundleUtils.getBundleExtra(getArguments(), "uid", "")).putExtra("from", "profile"), REQ_CODE_WALLET_ACTIVITY_RESULTS);

                /*Intent intent = new Intent(context, SendMoneyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", "" + preferences.getUserId());
                bundle.putString("uFullName", preferences.getUserFullName());
                intent.putExtras(bundle);
                ((Activity) context).startActivityForResult(intent, REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS);*/
                break;
        }
    }

    private void performActionOnMessageClick() {
        if (scrollMsg.getVisibility() == View.GONE) {
            scrollMsg.setVisibility(View.VISIBLE);
        } else {
            scrollMsg.setVisibility(View.GONE);
        }
        if (linerMessage.getVisibility() == View.GONE) {
            linerMessage.setVisibility(View.VISIBLE);
        } else {
            linerMessage.setVisibility(View.GONE);
        }
    }

    public void openPopUpmenu() {
        if (!isLoginUser) {
            switch (frdShipType) {
                case "0":
                    if (isMyFriend == 0) {
                        strings = new String[]{getString(R.string.txt_message), getString(R.string.txt_add_friend),
                                getString(R.string.txt_share), getString(R.string.txt_send_money), getString(R.string.txt_block)};
                    } else {
                        strings = new String[]{getString(R.string.txt_message), getString(R.string.txt_block),
                                getString(R.string.txt_un_friend), getString(R.string.txt_share), getString(R.string.txt_send_money)};
                    }

                    MyPopUpWindow popUpWindow2 = new MyPopUpWindow(context, img_edit,
                            strings, img_edit, "ProfileRejectList");
                    popUpWindow2.show(img_edit, MyPopUpWindow.PopUpPosition.CENTER);
                    popUpWindow2.setOnPopupItemClickListner(new MyPopUpWindow.OnPopupItemClickListener() {
                        @Override
                        public boolean onPopupItemClick(int position) {
                            img_edit.setBackground(context.getResources().getDrawable(R.drawable.ractangle));
                            switch (strings[position]) {
                                case "Message":
                                    performActionOnMessageClick();
                                    break;
                                case "Block":
                                    ((ProfileActivity) context).callBlockApi();
                                    strings = new String[]{getString(R.string.txt_unblock), getString(R.string.txt_add_friend),
                                            getString(R.string.txt_share), getString(R.string.txt_send_money)};
                                    break;
                                case "UnBlock":
                                    ((ProfileActivity) context).callUnBlockApi();
                                    strings = new String[]{getString(R.string.txt_block), getString(R.string.txt_add_friend),
                                            getString(R.string.txt_share), getString(R.string.txt_send_money)};
                                    break;
                                case "Add Friend":
                                    ((ProfileActivity) context).callAddFrd();

                                    break;
                                case "Un Friend":
                                    ((ProfileActivity) context).callUnfrdApi();
                                    isMyFriend = 0;
                                    break;
                                case "Share":
                                    openChatActivity();
                                    break;
                                case "Send money":
//                                    startActivityForResult(new Intent(context, SendMoneyActivity.class), REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS);
                                    startActivityForResult(new Intent(context, WalletSendReceiveActivity.class).putExtra("uid", BundleUtils.getBundleExtra(getArguments(), "uid", "")).putExtra("from", "profile"), REQ_CODE_WALLET_ACTIVITY_RESULTS);
                                    break;
                            }
                            popUpWindow2.dismiss();
                            return true;
                        }
                    });
                    break;

                case "1":

                    if (isMyFriend == 0) {
                        strings1 = new String[]{getString(R.string.txt_message), getString(R.string.txt_add_friend),
                                getString(R.string.txt_share), getString(R.string.txt_send_money)};
                    } else {
                        strings1 = new String[]{getString(R.string.txt_message), getString(R.string.txt_block), getString(R.string.txt_un_friend),
                                getString(R.string.txt_share), getString(R.string.txt_send_money)};
                    }

                    MyPopUpWindow popUpWindow1 = new MyPopUpWindow(context, img_edit,
                            strings1, img_edit, "ProfileRejectList");
                    popUpWindow1.show(img_edit, MyPopUpWindow.PopUpPosition.RIGHT);
                    popUpWindow1.setOnPopupItemClickListner(new MyPopUpWindow.OnPopupItemClickListener() {
                        @Override
                        public boolean onPopupItemClick(int position) {
                            img_edit.setBackground(context.getResources().getDrawable(R.drawable.ractangle));
                            switch (strings1[position]) {
                                case "Message":
                                    performActionOnMessageClick();
                                    break;
                                case "Block":
                                    ((ProfileActivity) context).callBlockApi();
                                    strings1 = new String[]{getString(R.string.txt_unblock), getString(R.string.txt_un_friend),
                                            getString(R.string.txt_share), getString(R.string.txt_send_money)};
                                    break;
                                case "UnBlock":
                                    ((ProfileActivity) context).callUnBlockApi();
                                    strings1 = new String[]{getString(R.string.txt_block), getString(R.string.txt_un_friend),
                                            getString(R.string.txt_share), getString(R.string.txt_send_money)};
                                    break;
                                case "Add Friend":
                                    ((ProfileActivity) context).callAddFrd();
                                    isMyFriend = 1;
                                    break;
                                case "Un Friend":
                                    ((ProfileActivity) context).callUnfrdApi();
                                    isMyFriend = 0;
                                    break;
                                case "Share":
                                    getActivity().finish();
                                    break;
                                case "Send money":
                                    startActivityForResult(new Intent(context, SendMoneyActivity.class), REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS);
                                    break;
                            }
                            popUpWindow1.dismiss();
                            return true;
                        }
                    });
                    break;

                case "2":

                    if (isMyFriend == 0) {
                        strings2 = new String[]{getString(R.string.txt_message), getString(R.string.txt_cancel_request),
                                getString(R.string.txt_share), getString(R.string.txt_send_money)};
                    } else {
                        strings2 = new String[]{getString(R.string.txt_message), getString(R.string.txt_block),
                                getString(R.string.txt_cancel_request), getString(R.string.txt_share), getString(R.string.txt_send_money)};
                    }

                    MyPopUpWindow popUpWindow = new MyPopUpWindow(context, img_edit,
                            strings2, img_edit, "ProfileRejectList");
                    popUpWindow.show(img_edit, MyPopUpWindow.PopUpPosition.CENTER);
                    popUpWindow.setOnPopupItemClickListner(new MyPopUpWindow.OnPopupItemClickListener() {
                        @Override
                        public boolean onPopupItemClick(int position) {
                            img_edit.setBackground(context.getResources().getDrawable(R.drawable.ractangle));
                            switch (strings2[position]) {
                                case "Message":
                                    performActionOnMessageClick();
                                    break;
                                case "Block":
                                    ((ProfileActivity) context).callBlockApi();
                                    strings2 = new String[]{getString(R.string.txt_unblock), getString(R.string.txt_cancel_request),
                                            getString(R.string.txt_share), getString(R.string.txt_send_money)};
                                    break;
                                case "UnBlock":
                                    ((ProfileActivity) context).callUnBlockApi();
                                    strings2 = new String[]{getString(R.string.txt_block), getString(R.string.txt_cancel_request),
                                            getString(R.string.txt_share), getString(R.string.txt_send_money)};
                                    break;
                                case "Cancel Request":
                                    ((ProfileActivity) context).callCancleFrdRequest();
                                    break;
                                case "Share":
                                    Intent intent = new Intent(context, ChatActivity.class);
                                    intent.putExtra("otherUid", BundleUtils.getBundleExtra(getArguments(), "uid", ""));
                                    intent.putExtra("username", userName);
                                    intent.putExtra("userProfile", BundleUtils.getBundleExtra(getArguments(), "uProfile", ""));
                                    intent.putExtra("fullname", BundleUtils.getBundleExtra(getArguments(), "uFullName", ""));
                                    startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
                                    break;
                                case "Send money":
                                    startActivityForResult(new Intent(context, SendMoneyActivity.class), REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS);
                                    break;
                            }
                            popUpWindow.dismiss();
                            return true;
                        }
                    });
                    break;

                case "3":

                    if (isMyFriend == 0) {
                        strings3 = new String[]{getString(R.string.txt_message), getString(R.string.txt_accept_friend),
                                getString(R.string.txt_share), getString(R.string.txt_send_money)};
                    } else {
                        strings3 = new String[]{getString(R.string.txt_message), getString(R.string.txt_block),
                                getString(R.string.txt_accept_friend), getString(R.string.txt_share), getString(R.string.txt_send_money)};
                    }

                    MyPopUpWindow popUpWindowa = new MyPopUpWindow(context, img_edit,
                            strings3, img_edit, "ProfileRejectList");
                    popUpWindowa.show(img_edit, MyPopUpWindow.PopUpPosition.CENTER);
                    popUpWindowa.setOnPopupItemClickListner(new MyPopUpWindow.OnPopupItemClickListener() {
                        @Override
                        public boolean onPopupItemClick(int position) {
                            img_edit.setBackground(context.getResources().getDrawable(R.drawable.ractangle));
                            switch (strings3[position]) {
                                case "Message":
                                    performActionOnMessageClick();
                                    break;
                                case "Block":
                                    ((ProfileActivity) context).callBlockApi();
                                    strings3 = new String[]{getString(R.string.txt_unblock), getString(R.string.txt_accept_friend),
                                            getString(R.string.txt_share), getString(R.string.txt_send_money)};
                                    break;
                                case "UnBlock":
                                    ((ProfileActivity) context).callUnBlockApi();
                                    strings3 = new String[]{getString(R.string.txt_block), getString(R.string.txt_accept_friend),
                                            getString(R.string.txt_share), getString(R.string.txt_send_money)};
                                    break;
                                case "Accept Friend":
                                    ((ProfileActivity) context).callAcceptFrdRequestApi();
                                    break;
                                case "Share":
                                    Intent intent = new Intent(context, ChatActivity.class);
                                    intent.putExtra("otherUid", BundleUtils.getBundleExtra(getArguments(), "uid", ""));
                                    intent.putExtra("username", userName);
                                    intent.putExtra("userProfile", BundleUtils.getBundleExtra(getArguments(), "uProfile", ""));
                                    intent.putExtra("fullname", BundleUtils.getBundleExtra(getArguments(), "uFullName", ""));
                                    startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
                                    break;
                                case "Send money":
                                    startActivityForResult(new Intent(context, SendMoneyActivity.class), REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS);
                                    break;
                            }
                            popUpWindowa.dismiss();
                            return true;
                        }
                    });
                    break;
                case "4":
                    if (isMyFriend == 0) {
                        strings4 = new String[]{getString(R.string.txt_message), getString(R.string.txt_accept_friend),
                                getString(R.string.txt_share), getString(R.string.txt_send_money)};
                    } else {
                        strings4 = new String[]{getString(R.string.txt_message), getString(R.string.txt_unblock), getString(R.string.txt_accept_friend),
                                getString(R.string.txt_share), getString(R.string.txt_send_money)};
                    }

                    MyPopUpWindow popUpWindowa1 = new MyPopUpWindow(context, img_edit,
                            strings4, img_edit, "ProfileRejectList");
                    popUpWindowa1.show(img_edit, MyPopUpWindow.PopUpPosition.CENTER);
                    popUpWindowa1.setOnPopupItemClickListner(new MyPopUpWindow.OnPopupItemClickListener() {
                        @Override
                        public boolean onPopupItemClick(int position) {
                            img_edit.setBackground(context.getResources().getDrawable(R.drawable.ractangle));
                            switch (strings4[position]) {
                                case "Message":
                                    performActionOnMessageClick();
                                    break;
                                case "Block":
                                    ((ProfileActivity) context).callBlockApi();
                                    strings4 = new String[]{getString(R.string.txt_unblock), getString(R.string.txt_accept_friend),
                                            getString(R.string.txt_share), getString(R.string.txt_send_money)};
                                    break;
                                case "UnBlock":
                                    ((ProfileActivity) context).callUnBlockApi();
                                    strings4 = new String[]{getString(R.string.txt_block), getString(R.string.txt_accept_friend),
                                            getString(R.string.txt_share), getString(R.string.txt_send_money)};
                                    break;
                                case "Accept Friend":
                                    ((ProfileActivity) context).callAcceptFrdRequestApi();
                                    break;
                                case "Share":
                                    Intent intent = new Intent(context, ChatActivity.class);
                                    intent.putExtra("otherUid", BundleUtils.getBundleExtra(getArguments(), "uid", ""));
                                    intent.putExtra("username", userName);
                                    intent.putExtra("userProfile", BundleUtils.getBundleExtra(getArguments(), "uProfile", ""));
                                    intent.putExtra("fullname", BundleUtils.getBundleExtra(getArguments(), "uFullName", ""));
                                    startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
                                    break;
                                case "Send money":
                                    startActivityForResult(new Intent(context, SendMoneyActivity.class), REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS);
                                    break;
                            }
                            popUpWindowa1.dismiss();
                            return true;
                        }
                    });
                    break;
            }

        } else {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View popupView;
            if (layoutInflater == null) {
                return;
            }
            popupView = layoutInflater.inflate(R.layout.dialog_profile_option, null);
            PopupWindow popupWindow;
            popupWindow = new PopupWindow(context);
            popupWindow.setContentView(popupView);
            popupWindow.setWidth(Utils.dip2pixel(context, 200));
            popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            TextView tv_watching = popupView.findViewById(R.id.tv_watching);
            TextView tv_bought = popupView.findViewById(R.id.tv_bought);
            TextView tv_sold = popupView.findViewById(R.id.tv_sold);
            TextView tv_edit_profile = popupView.findViewById(R.id.tv_edit_profile);

            tv_bought.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PurchaseHistoryActivity.class);
                    intent.putExtra("userid", preferences.getUserId());
                    startActivityForResult(intent, REQ_CODE_PURCHASE_HISTORY_ACTIVITY_RESULTS);
                    popupWindow.dismiss();
                }
            });

            tv_sold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SoldHistoryActivity.class);
                    intent.putExtra("userid", preferences.getUserId());
                    startActivityForResult(intent, REQ_CODE_SOLD_HISTORY_ACTIVITY_RESULTS);
                    popupWindow.dismiss();
                }
            });

            tv_edit_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditProfileActivity.class);
                    intent.putExtra("screen", DrawerMainActivity.class.getSimpleName());
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });

            tv_watching.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WatchedItemListActivity.class);
                    getActivity().startActivityForResult(intent, REQ_CODE_WATCH_ITEM_LIST_ACTIVITY_RESULTS);
                    popupWindow.dismiss();
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                popupWindow.showAsDropDown(txtUserName, img_edit.getRight() - popupWindow.getWidth() - img_edit.getWidth() / 2, 0, Gravity.CENTER | Gravity.TOP);
            } else {
                popupWindow.showAsDropDown(txtUserName, img_edit.getRight() - popupWindow.getWidth() - img_edit.getWidth() / 2, 0);
            }
        }
    }

    private void openChatActivity() {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("otherUid", BundleUtils.getBundleExtra(getArguments(), "uid", ""));
        intent.putExtra("username", userName);
        intent.putExtra("userProfile", BundleUtils.getBundleExtra(getArguments(), "uProfile", ""));
        intent.putExtra("fullname", BundleUtils.getBundleExtra(getArguments(), "uFullName", ""));
        startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
    }

    private void loadMessenger() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("FRAGMENT_INDEX", 101);
        ((Activity) context).setResult(RESULT_OK, returnIntent);
        ((Activity) context).finish();
    }

    void callIntentScreen() {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("otherUid", BundleUtils.getBundleExtra(getArguments(), "uid", ""));
        intent.putExtra("username", userName);
        intent.putExtra("userProfile", BundleUtils.getBundleExtra(getArguments(), "uProfile", ""));
        intent.putExtra("fullname", BundleUtils.getBundleExtra(getArguments(), "uFullName", ""));
        startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
    }

    public void passData(String name) {
        frdShipType = name;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (UserApiCall != null) {
            UserApiCall.cancel();
        }
        if (sendMsgBeanCall != null) {
            sendMsgBeanCall.cancel();
        }
        if (GetUserInfoCall != null) {
            GetUserInfoCall.cancel();
        }
    }

    public Fragment getFragmentFromViewpager(int position) {
        return ((Fragment) (viewPagerAdapter.instantiateItem(viewpager, position)));
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        initBundleDataAndViews();
        initControlValue();
    }
}
