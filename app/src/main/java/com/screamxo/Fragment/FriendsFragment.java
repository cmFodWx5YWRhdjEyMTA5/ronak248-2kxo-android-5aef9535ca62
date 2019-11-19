package com.screamxo.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.Emailfriend;
import com.example.apimodule.ApiBase.ApiBean.FollowersList;
import com.example.apimodule.ApiBase.ApiBean.Friend;
import com.example.apimodule.ApiBase.ApiBean.FriendBean;
import com.example.apimodule.ApiBase.ApiBean.InstaFollowerBean;
import com.example.apimodule.ApiBase.ApiBean.PostFrdBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.screamxo.Activity.RahulWork.FriendsActivity;
import com.screamxo.Activity.instagram.InstagramHelper;
import com.screamxo.Activity.instagram.InstagramHelperConstants;
import com.screamxo.Activity.instagram.SharedPrefUtils;
import com.screamxo.Adapter.FriendAdapter;
import com.screamxo.BuildConfig;
import com.screamxo.Interface.FacebookInterface;
import com.screamxo.Others.ContactList;
import com.screamxo.Others.FaceBookLogin;
import com.screamxo.Others.MyTwitterApiClient;
import com.screamxo.R;
import com.screamxo.Utils.EventData;
import com.screamxo.Utils.HidingScrollListener;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;

import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP;

@SuppressLint("ValidFragment")
public class FriendsFragment extends Fragment implements FacebookInterface {
    private static final int FACEBOOK_REQUEST_CODE = 64206;
    private static final String TAG = "FriendsFragment";
    Context context;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.img_pop_up)
    ImageView imgPopUp;
    @BindView(R.id.img_arrow)
    ImageView imgArrow;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    @BindView(R.id.txt_member)
    TextView txtMember;
    @BindView(R.id.txt_request)
    TextView txtRequest;
    @BindView(R.id.txt_suggetion)
    TextView txtSuggetion;
    @BindView(R.id.no_data)
    LinearLayout linearLayoutNodata;
    @BindView(R.id.txt_finder)
    TextView txtFinder;
    @BindView(R.id.finder_pop_up)
    LinearLayout finder_pop_up;
    @BindView(R.id.inviter_pop_up)
    LinearLayout inviter_pop_up;
    @BindView(R.id.txt_inviter)
    TextView txtInviter;
    @BindView(R.id.lny_finder_invite)
    RelativeLayout lnyFinderInvite;
    @BindView(R.id.progreessbar_main)
    ProgressBar progreessbarMain;
    Call<FriendBean> friendBeanCall;
    Call<PostFrdBean> postFrdBeanCall;
    Preferences preferences;
    ArrayList<Friend> memberFriendArrayList, suggetionFrdList, requestFriendList, searchFrdList;
    Boolean isMemberSelect = true, isSuggetionSelect = false, isRequestSelect = false, isFinderSelected;
    int offset = 0, offsetForSearchFrd = 0, totalMember = 0;
    String limit = StaticConstant.LIMIT, socialPeopleType = "";
    String contectEmailJsonStrting = "", fbFrdIdJsonString = "", twitterFrdlisJsonString = "", instaFoloowerList = "";
    ArrayList<Object> frdList, friends;
    String screenType = "";
    String amount = "";
    View view;
    private FetchrServiceBase mService;
    private HashMap<String, String> map;
    private LinearLayoutManager linearLayoutManager;
    private FriendAdapter friendAdapter;
    private FaceBookLogin faceBookLogin;
    private TwitterAuthClient mTwitterAuthClient;

    public FriendsFragment() {
    }

    @SuppressLint("ValidFragment")
    public FriendsFragment(String socialPeopleType, String screenType, String amount) {
        this.socialPeopleType = socialPeopleType;
        this.screenType = screenType;
        this.amount = amount;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, view);
        init();
        initControlValue();
        initListener();
        return view;
    }

    private void initListener() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                recyclerView.addOnScrollListener(new HidingScrollListener() {
                    @Override
                    public void onHide() {
                        if (context instanceof FriendsActivity) {
                            ((FriendsActivity) context).hideViews();
                        }
                    }

                    @Override
                    public void onShow() {
                        if (context instanceof FriendsActivity) {
                            ((FriendsActivity) context).showViews();
                        }
                    }
                });

                if (isFinderSelected) {
                    return;
                }
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (friendAdapter.getItemCount() - 1)) {
                    if (isMemberSelect && memberFriendArrayList.size() < totalMember) {
                        callGetFrdList();
                    } else if (isRequestSelect && requestFriendList.size() < totalMember) {
                        callGetRequestFrdList();
                    } else if (isSuggetionSelect && suggetionFrdList.size() < totalMember) {
                        callGetSuggestFrdList();
                    }
                }
            }
        });
    }

    private void init() {
        map = new HashMap<>();
        frdList = new ArrayList<>();
        mService = new FetchrServiceBase();
        preferences = new Preferences(context);
        memberFriendArrayList = new ArrayList<>();
        suggetionFrdList = new ArrayList<>();
        requestFriendList = new ArrayList<>();
        searchFrdList = new ArrayList<>();
        friends = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        faceBookLogin = new FaceBookLogin(context, this);
    }

    private void callGetFrdList() {
        if (Utils.isInternetOn(context)) {
            if (friendBeanCall != null) {
                friendBeanCall.cancel();
            }
            map.put("uid", preferences.getUserId());
            map.put("offset", "" + offset);
            map.put("limit", "" + 50);
            friendBeanCall = mService.getFetcherService(context).GetFrdList(map);
            if (memberFriendArrayList.size() == 0) {
                progreessbarMain.setVisibility(View.VISIBLE);
            }
            friendBeanCall.enqueue(new Callback<FriendBean>() {
                @Override
                public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                    progreessbarMain.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (offset == 0) {
                                memberFriendArrayList.clear();
                            }
                            offset++;
                            memberFriendArrayList.addAll(response.body().getResult().getFriends());
                            totalMember = response.body().getResult().getTotalcount();
                            setAdapter("0");
                        } else if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
                            Utils.unAuthentication(context);
                        }

                    }
                }

                @Override
                public void onFailure(Call<FriendBean> call, Throwable t) {
                    progreessbarMain.setVisibility(View.GONE);
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callGetSuggestFrdList() {
        if (Utils.isInternetOn(context)) {
            HashMap<String, String> bean = preferences.getUserDetail();

            map.put("offset", "" + offset);
            map.put("limit", limit);
            map.put("uid", preferences.getUserId());
            map.put("lat", "22.3");
            map.put("lon", "72.0");
            if (friendBeanCall != null) {
                friendBeanCall.cancel();
            }
            if (suggetionFrdList.size() == 0) {
                progreessbarMain.setVisibility(View.VISIBLE);
            }
            friendBeanCall = mService.getFetcherService(context).GetSuggestFrdList(map);
            friendBeanCall.enqueue(new Callback<FriendBean>() {
                @Override
                public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                    progreessbarMain.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (offset == 0) {
                                suggetionFrdList.clear();
                            }

                            offset++;
                            suggetionFrdList.addAll(response.body().getResult().getFriends());
                            totalMember = response.body().getResult().getTotalcount();
                            setAdapter("1");
                        }
                    }
                }

                @Override
                public void onFailure(Call<FriendBean> call, Throwable t) {
                    progreessbarMain.setVisibility(View.GONE);
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callGetRequestFrdList() {
        if (Utils.isInternetOn(context)) {
            map.put("offset", "" + offset);
            map.put("limit", limit);
            map.put("uid", preferences.getUserId());
            if (friendBeanCall != null) {
                friendBeanCall.cancel();
            }
            if (requestFriendList.size() == 0) {
                progreessbarMain.setVisibility(View.VISIBLE);
            }
            friendBeanCall = mService.getFetcherService(context).GetRequestFrdList(map);
            friendBeanCall.enqueue(new Callback<FriendBean>() {
                @Override
                public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                    progreessbarMain.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (offset == 0) {
                                requestFriendList.clear();
                            }

                            offset++;
                            requestFriendList.addAll(response.body().getResult().getFriends());
                            totalMember = response.body().getResult().getTotalcount();
                            setAdapter("2");
                        } else if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
                            Utils.unAuthentication(context);
                        }
                    }
                }

                @Override
                public void onFailure(Call<FriendBean> call, Throwable t) {
                    progreessbarMain.setVisibility(View.GONE);
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void setAdapter(String type) {
//        0 = member, 1 = suggettion ,2 = Request ,3 = filter (invite), 4 = searchFriend
        Log.d(TAG, "setAdapter: ");
        friends.clear();
        switch (type) {
            case "0":
                friends.addAll(memberFriendArrayList);
                break;
            case "1":
                friends.addAll(suggetionFrdList);
                break;
            case "2":
                friends.addAll(requestFriendList);
                break;
            case "3":
                friends.addAll(frdList);
                frdList.clear();
                break;
            case "4":
                friends.addAll(searchFrdList);
                break;
        }

        if (friends.size() > 0) {
            if (recyclerView.getVisibility() == View.GONE) {
                recyclerView.setVisibility(View.VISIBLE);
            }
            linearLayoutNodata.setVisibility(View.GONE);
        } else if (progreessbarMain.getVisibility() != View.VISIBLE) {
            if (recyclerView.getVisibility() == View.VISIBLE) {
                recyclerView.setVisibility(View.GONE);
            }
            linearLayoutNodata.setVisibility(View.VISIBLE);
        } else {
            linearLayoutNodata.setVisibility(View.VISIBLE);
        }

        if (friendAdapter == null) {
            friendAdapter = new FriendAdapter(context, friends, type, screenType, amount);
            recyclerView.setAdapter(friendAdapter);
        } else {
            friendAdapter.type = type;
            friendAdapter.notifyDataSetChanged();
        }
    }

    private void initControlValue() {
        imgArrow.setVisibility(View.VISIBLE);
        setTypeface(socialPeopleType);
        setUnderLine(socialPeopleType);
        setTextColor("0");
        setSelect(socialPeopleType);

// 0 = Member, 1 = suggest, 2 = request
        switch (socialPeopleType) {
            case "0":
                callGetFrdList();
                break;
            case "1":
                callGetSuggestFrdList();
                break;
            case "2":
                callGetRequestFrdList();
                break;
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEventMainThread(EventData event) {

        switch (event.getCode()) {
            case EVENT_SCROLL_TO_TOP:

                try {
                    if (getUserVisibleHint()) {
                        FloatingMenuButton floatingMenuButton = ((FriendsActivity) context).floatingButton;
                        if (!floatingMenuButton.isMenuOpen()) {
                            if (floatingMenuButton.getBackground().getConstantState() != null
                                    && floatingMenuButton.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ic_float_menu_up).getConstantState())) {
                                recyclerView.scrollToPosition(0);
                                if (!floatingMenuButton.isMenuOpen()) {
                                    floatingMenuButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                                    floatingMenuButton.closeMenu();
                                }
                                floatingMenuButton.setBackground(getResources().getDrawable(R.mipmap.menu));

                                return;
                            }
                        }
                    }
                } catch (Exception ignored) {

                }
                break;

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @OnClick({R.id.txt_member, R.id.txt_request, R.id.txt_suggetion, R.id.txt_finder, R.id.txt_inviter})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txt_member:
                setTypeface("0");
                setUnderLine("0");
                setTextColor("0");
                setSelect("0");
                offset = 0;
                setAdapter("0");
                callGetFrdList();
                break;
            case R.id.txt_request:
                setTypeface("2");
                setUnderLine("2");
                setTextColor("2");
                setSelect("2");
                offset = 00;
                setAdapter("2");
                callGetRequestFrdList();
                break;

            case R.id.txt_suggetion:
                setTypeface("1");
                setSelect("1");
                setUnderLine("1");
                setTextColor("1");
                offset = 0;
                setAdapter("1");
                callGetSuggestFrdList();
                break;

            case R.id.txt_finder:

                if (finder_pop_up.getVisibility() == View.GONE) {
                    finder_pop_up.setVisibility(View.VISIBLE);
                    txtFinder.setTextColor(ContextCompat.getColor(context, R.color.colordarkGray));
                } else {
                    finder_pop_up.setVisibility(View.GONE);
                    txtFinder.setTextColor(Color.WHITE);
                }

                txtInviter.setTextColor(Color.WHITE);
                inviter_pop_up.setVisibility(View.GONE);

                finder_pop_up.removeAllViews();
                LayoutInflater.from(getContext()).inflate(R.layout.finder_pop_up, finder_pop_up);

                CheckBox checkBoxFb, checkBoxTwitter, checkBoxInsta, checkBoxContact;
                Button btnStart;

                btnStart = finder_pop_up.findViewById(R.id.btn_start);
                checkBoxFb = finder_pop_up.findViewById(R.id.check_box_fb);
                checkBoxInsta = finder_pop_up.findViewById(R.id.check_box_insta);
                checkBoxTwitter = finder_pop_up.findViewById(R.id.check_box_twitter);
                checkBoxContact = finder_pop_up.findViewById(R.id.check_box_cont);

                checkBoxFb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked && faceBookLogin != null) {
                            if (Utils.isInternetOn(context)) {
                                faceBookLogin.faceBookManager(context);
                            } else {
                                Utils.showToast(context, context.getString(R.string.toast_no_internet));
                            }
                        }
                    }
                });

                checkBoxContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            RxPermissions.getInstance(context)
                                    .requestEach(Manifest.permission.READ_CONTACTS)
                                    .subscribe((Permission permission) -> {
                                        if (permission.granted) {
                                            getContactEmail();
                                        }
                                    });
                        }
                    }
                });

                checkBoxTwitter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked && mTwitterAuthClient == null) {
                            onTwitterClick();
                        }
                    }
                });

                checkBoxInsta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            if (SharedPrefUtils.getToken(context) == null) {
                                callGetInstaUser();
                            } else {
                                callGetInstaUserFollowerApi();
                            }
                        }
                    }
                });

                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callPostContactApi();
                        finder_pop_up.setVisibility(View.GONE);
                        txtFinder.setTextColor(Color.WHITE);
                        lnyFinderInvite.setVisibility(View.GONE);
                    }
                });

                break;
            case R.id.txt_inviter:
                if (inviter_pop_up.getVisibility() == View.GONE) {
                    inviter_pop_up.setVisibility(View.VISIBLE);
                    txtInviter.setTextColor(ContextCompat.getColor(context, R.color.colordarkGray));
                } else {
                    inviter_pop_up.setVisibility(View.GONE);
                    txtInviter.setTextColor(Color.WHITE);
                }
                txtFinder.setTextColor(Color.WHITE);
                finder_pop_up.setVisibility(View.GONE);

                inviter_pop_up.removeAllViews();
                LayoutInflater.from(getContext()).inflate(R.layout.inviter_pop_up, inviter_pop_up);

                RadioButton rbContact;
                RadioGroup radioGroup;
                Button btnInviteStart;
                btnInviteStart = inviter_pop_up.findViewById(R.id.btn_start);
                rbContact = inviter_pop_up.findViewById(R.id.rb_cont);
                radioGroup = inviter_pop_up.findViewById(R.id.radio_grp);
                rbContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            RxPermissions.getInstance(context)
                                    .requestEach(Manifest.permission.READ_CONTACTS)
                                    .subscribe((Permission permission) -> {
                                        if (permission.granted) {
                                            getContactEmail();
                                        }
                                    });
                        }
                    }
                });

                btnInviteStart.setOnClickListener(view1 -> {
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.rb_fb:
                            openFbInviteDialog();
                            break;
                        case R.id.rb_cont:
                            callInviteFromContactAPi();
                            break;
                        case R.id.rb_twitter:
                            callInviteFromTwitter();
                            break;
                    }

                    inviter_pop_up.setVisibility(View.GONE);
                    txtInviter.setTextColor(Color.WHITE);
                    lnyFinderInvite.setVisibility(View.GONE);
                });
                break;
        }
    }

    private void setTextColor(String selectType) {
        switch (selectType) {
            case "0":
                isMemberSelect = true;
                txtMember.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                txtSuggetion.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
                txtRequest.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
                break;
            case "1":
                txtMember.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
                txtSuggetion.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                txtRequest.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
                break;
            case "2":
                txtMember.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
                txtSuggetion.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
                txtRequest.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                break;
        }
        isFinderSelected = false;
    }

    private void setSelect(String selectType) {
        switch (selectType) {
            case "0":
                isMemberSelect = true;
                isRequestSelect = false;
                isSuggetionSelect = false;
                break;
            case "1":
                isMemberSelect = false;
                isRequestSelect = false;
                isSuggetionSelect = true;
                break;
            case "2":
                isMemberSelect = false;
                isRequestSelect = true;
                isSuggetionSelect = false;
                break;
        }
        isFinderSelected = false;
    }

    private void callInviteFromTwitter() {
        mTwitterAuthClient = new TwitterAuthClient();
        if (Utils.isInternetOn(context)) {
            mTwitterAuthClient.authorize((Activity) context, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> twitterSessionResult) {

                    final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                    final Intent intent = new ComposerActivity.Builder(context)
                            .session(session)
                            .createIntent();
                    startActivity(intent);
                }

                @Override
                public void failure(TwitterException e) {

                    e.printStackTrace();
                    Utils.showToast(context, context.getResources().getString(R.string.toast_login_fail));
                    Twitter.getSessionManager().clearActiveSession();
                    Twitter.logOut();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void openFbInviteDialog() {
        String appLinkUrl, previewImageUrl;
        appLinkUrl = "https://fb.me/124654174915494";
        previewImageUrl = "https://i0.wp.com/screamxo.com/wp-content/uploads/2017/04/logo-screamxo.png";
        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(this, content);
        }
    }

    private void callInviteFromContactAPi() {

        if (Utils.isInternetOn(context)) {
//            map.put("email_id", contectEmailJsonStrting);
            map.put("contactfriends", contectEmailJsonStrting);
            if (friendBeanCall != null) {
                friendBeanCall.cancel();
            }

            postFrdBeanCall = mService.getFetcherService(context).inviteFrdFromContact(map);
            postFrdBeanCall.enqueue(new Callback<PostFrdBean>() {
                @Override
                public void onResponse(Call<PostFrdBean> call, Response<PostFrdBean> response) {
                    progreessbarMain.setVisibility(View.GONE);
                    switch (response.code()) {
                        case StaticConstant.RESULT_OK:
                            Utils.showToast(context, response.body().getMsg());
                            break;
                        case StaticConstant.UNAUTHORIZE:
                            Utils.unAuthentication(context);
                            break;
                        case StaticConstant.BAD_REQUEST:
                            Utils.showToast(context, getString(R.string.toast_error_something_went_wrong));
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onFailure(Call<PostFrdBean> call, Throwable t) {

                    progreessbarMain.setVisibility(View.GONE);
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callGetInstaUser() {
        if (Utils.isInternetOn(context)) {
            InstagramHelper instagramHelper = new InstagramHelper.Builder()
                    .withClientId(BuildConfig.INSTAGRAMCLIENID)
                    .withRedirectUrl(BuildConfig.INSTAGRAMREDIRECTURL)
                    .withScope("follower_list")
                    .build();
            instagramHelper.loginFromActivity((Activity) context);
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (mTwitterAuthClient != null) {
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case FACEBOOK_REQUEST_CODE:
                    faceBookLogin.onResult(requestCode, resultCode, data);
                    break;
                case InstagramHelperConstants.INSTA_LOGIN:
                    callGetInstaUserFollowerApi();
                    break;
            }
        }
    }

    private void callGetInstaUserFollowerApi() {
        Call<InstaFollowerBean> instaFollowerBeanCal = mService.getFetcherService(context, false, new Boolean[]{true}).GetTwitterFollower("follows?access_token=" + SharedPrefUtils.getToken(context));

        instaFollowerBeanCal.enqueue(new Callback<InstaFollowerBean>() {
            @Override
            public void onResponse(Call<InstaFollowerBean> call, Response<InstaFollowerBean> response) {
                Log.d(TAG, "onResponse: ");
                if (response.code() == 200) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("[");
                    try {
                        int j = response.body().getData().size();
                        for (int i = 0; i < j; i++) {
                            buffer.append("{\"Insta_id\":\"").append(response.body().getData().get(i).getId()).append("\"},");
                        }
                    } catch (Exception e) {
                        //Exception Track
                        buffer = new StringBuffer();
                    }
                    if (buffer.length() > 2) {
                        buffer.deleteCharAt(buffer.length() - 1);
                    }
                    buffer.append("]");
                    instaFoloowerList = buffer.toString();
                }
            }

            @Override
            public void onFailure(Call<InstaFollowerBean> call, Throwable t) {
            }
        });

    }

    public void getContactEmail() {
        ContactList contactList = new ContactList(context);
        contectEmailJsonStrting = "";
        contectEmailJsonStrting = contactList.getContacts();
    }

    @Override
    public void success(Map<String, String> map) {
        Log.d(TAG, "success: ");
    }

    @Override
    public void onFbFrdFetch(ArrayList<Object> list) {
        Log.d(TAG, "onFbFrdFetch: ");
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        for (Object aList : list) {
            if (aList instanceof Emailfriend) {
                stringBuilder.append("{\"fb_id\":\"").append(((Emailfriend) aList).getFbid()).append("\"},");
            }
        }
        if (stringBuilder.length() > 2) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("]");

        fbFrdIdJsonString = stringBuilder.toString();
        Log.d(TAG, "onFbFrdFetch fbFrdIdJsonString: " + fbFrdIdJsonString);
    }

    private void callPostContactApi() {
        Log.d(TAG, "callPostContactApi: ");
        if (Utils.isInternetOn(context)) {
            JsonParser jsonParser = new JsonParser();
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            if (!TextUtils.isEmpty(contectEmailJsonStrting))
                stringObjectHashMap.put("contactfriends", (JsonArray) jsonParser.parse(contectEmailJsonStrting));
            if (!TextUtils.isEmpty(fbFrdIdJsonString))
                stringObjectHashMap.put("fbfriends", (JsonArray) jsonParser.parse(fbFrdIdJsonString));
            if (!TextUtils.isEmpty(twitterFrdlisJsonString))
                stringObjectHashMap.put("twitterfriends", (JsonArray) jsonParser.parse(twitterFrdlisJsonString));
            stringObjectHashMap.put("uid", preferences.getUserId());

            if (friendBeanCall != null) {
                friendBeanCall.cancel();
            }

            postFrdBeanCall = mService.getFetcherService(context).postFetchFrdList(stringObjectHashMap);
            postFrdBeanCall.enqueue(new Callback<PostFrdBean>() {
                @Override
                public void onResponse(Call<PostFrdBean> call, Response<PostFrdBean> response) {
                    progreessbarMain.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            frdList.clear();
                            frdList.addAll(response.body().getResult().getEmailfriends());
                            frdList.addAll(response.body().getResult().getFbfriends());
                            frdList.addAll(response.body().getResult().getGooglefriends());
                            frdList.addAll(response.body().getResult().getTwitterfriends());

                            // updating view back to memeber,
                            txtMember.setTypeface(null, Typeface.BOLD);
                            txtRequest.setTypeface(null, Typeface.NORMAL);
                            txtSuggetion.setTypeface(null, Typeface.NORMAL);
                            setUnderLine("0");
                            setTextColor("0");
                            setSelect("0");
                            isFinderSelected = true;
                            setAdapter("3");
                        }
                    }
                }

                @Override
                public void onFailure(Call<PostFrdBean> call, Throwable t) {
                    progreessbarMain.setVisibility(View.GONE);
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }

    }

    public void onTwitterClick() {
        Log.d(TAG, "onTwitterClick: ");
        mTwitterAuthClient = new TwitterAuthClient();

        if (Utils.isInternetOn(context)) {
            mTwitterAuthClient.authorize((Activity) context, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

                @Override
                public void success(Result<TwitterSession> twitterSessionResult) {
                    Log.d(TAG, "success: ");
                    Call<FollowersList> followersListCall = new MyTwitterApiClient(twitterSessionResult.data).getCustomService().show();
                    followersListCall.enqueue(new Callback<FollowersList>() {
                        @Override
                        public void onResponse(Call<FollowersList> call, Response<FollowersList> response) {
                            StringBuffer buffer = new StringBuffer();
                            buffer.append("[");
                            try {
                                for (int i = 0; i < response.body().getUsers().size(); i++) {
                                    buffer.append("{\"twitter_id\":\"").append(response.body().getUsers().get(i).getId()).append("\"},");
                                }
                            } catch (Exception e) {
                                //Exception Track
                                buffer = new StringBuffer();
                            }
                            if (buffer.length() > 2) {
                                buffer.deleteCharAt(buffer.length() - 1);
                            }
                            buffer.append("]");
                            twitterFrdlisJsonString = buffer.toString();
                            Log.d(TAG, "onResponse: " + twitterFrdlisJsonString);
                        }

                        @Override
                        public void onFailure(Call<FollowersList> call, Throwable t) {
                            mTwitterAuthClient = null;

                        }
                    });
                    // Success
                }

                @Override
                public void failure(TwitterException e) {
                    Log.d(TAG, "failure: ");
                    e.printStackTrace();
                    Utils.showToast(context, context.getResources().getString(R.string.toast_login_fail));
                    Twitter.getSessionManager().clearActiveSession();
                    Twitter.logOut();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void clickToolbarSarchIcon() {
        if (lnyFinderInvite.getVisibility() == View.VISIBLE) {
            lnyFinderInvite.setVisibility(View.GONE);
        } else {
            lnyFinderInvite.setVisibility(View.VISIBLE);
        }
    }

    public void callSearchFrdApiFromActivity(String name) {
        Log.d(TAG, "callSearchFrdApiFromActivity: ");
        if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
            if (postFrdBeanCall != null) {
                postFrdBeanCall.cancel();
            }
            if (friendBeanCall != null) {
                friendBeanCall.cancel();
            }
            if (name.length() > 0) {
                if (Utils.isInternetOn(context)) {

                    map.put("offset", "" + 0);
                    map.put("limit", limit);
                    map.put("string", name);
                    map.put("uid", preferences.getUserId());

                    friendBeanCall = mService.getFetcherService(context).searchFrdList(map);
                    progreessbarMain.setVisibility(View.VISIBLE);
                    friendBeanCall.enqueue(new Callback<FriendBean>() {
                        @Override
                        public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                            progreessbarMain.setVisibility(View.GONE);
                            if (response.code() == StaticConstant.RESULT_OK) {
                                if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
//                                    if (offsetForSearchFrd == 0)
                                    searchFrdList.clear();
                                    searchFrdList.addAll(response.body().getResult().getFriends());
                                    offsetForSearchFrd++;
                                    setAdapter("4");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FriendBean> call, Throwable t) {
                            progreessbarMain.setVisibility(View.GONE);
                            if (!call.isCanceled()) {
                                Utils.showToast(context, t.toString());
                            }
                        }
                    });

                } else {
                    Utils.showToast(context, context.getString(R.string.toast_no_internet));
                }
            } else {
                if (txtMember.getBackground() != null) {
                    setUnderLine("0");
                    setTextColor("0");
                    setAdapter("0");
                } else if (txtSuggetion.getBackground() != null) {
                    setUnderLine("1");
                    setTextColor("1");
                    setAdapter("1");
                } else {
                    setUnderLine("2");
                    setTextColor("2");
                    setAdapter("2");
                }

            }
        }
    }

    void setUnderLine(String type) {
// 0 = member , 1 = suggetion , 3 = Request
        switch (type) {
            case "0":
                txtMember.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_background_green));
                txtSuggetion.setBackground(null);
                txtRequest.setBackground(null);

//                txtMember.setPaintFlags(txtMember.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                txtSuggetion.setPaintFlags(0);
//                txtRequest.setPaintFlags(0);
                break;
            case "1":
                txtMember.setBackground(null);
                txtSuggetion.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_background_green));
                txtRequest.setBackground(null);

//                txtSuggetion.setPaintFlags(txtSuggetion.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                txtMember.setPaintFlags(0);
//                txtRequest.setPaintFlags(0);
                break;
            case "2":
                txtMember.setBackground(null);
                txtSuggetion.setBackground(null);
                txtRequest.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_background_green));

//                txtRequest.setPaintFlags(txtRequest.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                txtMember.setPaintFlags(0);
//                txtSuggetion.setPaintFlags(0);
                break;
        }

    }


    void setTypeface(String type) {
// 0 = member , 1 = suggetion , 3 = Request
        switch (type) {
            case "0":
                txtMember.setTypeface(null, Typeface.BOLD);
                txtRequest.setTypeface(null, Typeface.NORMAL);
                txtSuggetion.setTypeface(null, Typeface.NORMAL);
                break;
            case "1":
                txtMember.setTypeface(null, Typeface.NORMAL);
                txtRequest.setTypeface(null, Typeface.NORMAL);
                txtSuggetion.setTypeface(null, Typeface.BOLD);
                break;
            case "2":
                txtMember.setTypeface(null, Typeface.NORMAL);
                txtRequest.setTypeface(null, Typeface.BOLD);
                txtSuggetion.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (friendBeanCall != null) {
            friendBeanCall.cancel();
        }

        if (postFrdBeanCall != null) {
            postFrdBeanCall.cancel();
        }
    }
}
