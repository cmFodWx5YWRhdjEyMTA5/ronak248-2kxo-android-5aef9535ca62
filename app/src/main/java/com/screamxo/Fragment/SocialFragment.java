package com.screamxo.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.screamxo.Activity.DrawerMainActivity;
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SocialFragment extends Fragment implements FacebookInterface {

    Context context;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    ViewPagerAdapter viewPagerAdapter;
    Fragment fragment;
    @BindView(R.id.rl_Pager)
    RelativeLayout rl_Pager;
    @BindView(R.id.rl_search_main)
    RelativeLayout rl_search_main;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;
    @BindView(R.id.txtCancel)
    TextView txtCancel;
    @BindView(R.id.imgRight)
    ImageView imgRight;
    @BindView(R.id.toolbar_edt_search)
    EditText toolbar_edt_search;
    @BindView(R.id.rc_view)
    RecyclerView rc_view;
    @BindView(R.id.imgUser)
    ImageView imgUser;
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

    private HashMap<String, String> map;
    String limit = StaticConstant.LIMIT;
    Preferences preferences;
    Call<FriendBean> friendBeanCall;
    private FetchrServiceBase mService;
    ArrayList<Friend> searchFrdList;
    ArrayList<Object> frdList, friends;
    private FriendAdapter friendAdapter;
    private FaceBookLogin faceBookLogin;
    String contectEmailJsonStrting = "", fbFrdIdJsonString = "", twitterFrdlisJsonString = "", instaFoloowerList = "";
    private TwitterAuthClient mTwitterAuthClient;
    private Call<PostFrdBean> postFrdBeanCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public SocialFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.network_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        initControlValue();
        return view;
    }

    private void init() {
        frdList = new ArrayList<>();
        friends = new ArrayList<>();
        searchFrdList = new ArrayList<>();
        mService = new FetchrServiceBase();
        preferences = new Preferences(getActivity());
        map = new HashMap<>();
        getActivity().getWindow().setStatusBarColor(Color.BLACK);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rc_view.setLayoutManager(linearLayoutManager);
        faceBookLogin = new FaceBookLogin(context, this);
    }

    private void initControlValue() {
        viewPagerAdapter.addFragment(new PleaserFragment("0"), getString(R.string.txt_pleasure));
        viewPagerAdapter.addFragment(new PleaserFragment("1"), getString(R.string.txt_business));
        viewPagerAdapter.addFragment(new PleaserFragment("2"), getString(R.string.txt_network));
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setOffscreenPageLimit(3);
        tabs.setupWithViewPager(viewpager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    if (!((DrawerMainActivity) context).floatingButton.isMenuOpen()) {
                        ((DrawerMainActivity) context).floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rl_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lnyFinderInvite != null && lnyFinderInvite.getVisibility() == View.VISIBLE) {
                    lnyFinderInvite.setVisibility(View.GONE);
                }

                rl_search.setVisibility(View.GONE);
                rl_search_main.setVisibility(View.VISIBLE);
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar_edt_search.setText("");
                rl_search_main.setVisibility(View.GONE);
                rl_search.setVisibility(View.VISIBLE);
            }
        });

        imgRight.setOnClickListener(view -> {
            @SuppressLint("RestrictedApi")
            List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment instanceof DashboardPagerFragment) {
                    ((DashboardPagerFragment) fragment).viewPager.setCurrentItem(1);
                }
            }
        });

        toolbar_edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    rl_Pager.setVisibility(View.GONE);
                    rc_view.setVisibility(View.VISIBLE);
                    callSearchFrdApiFromActivity(editable.toString(), 0);
                } else {
                    rl_Pager.setVisibility(View.VISIBLE);
                    rc_view.setVisibility(View.GONE);
                }
            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rl_Pager.setVisibility(View.VISIBLE);
                rc_view.setVisibility(View.GONE);

                if (lnyFinderInvite.getVisibility() == View.VISIBLE) {
                    lnyFinderInvite.setVisibility(View.GONE);
                } else {
                    lnyFinderInvite.setVisibility(View.VISIBLE);
                }
            }
        });

        txtFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        txtInviter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
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
                    endAuthorizeInProgress();
                }

                private void endAuthorizeInProgress() {
                    try {
                        Field authStateField = mTwitterAuthClient.getClass().getDeclaredField("authState");
                        authStateField.setAccessible(true);
                        Object authState = authStateField.get(mTwitterAuthClient);
                        Method endAuthorize = authState.getClass().getDeclaredMethod("endAuthorize");
                        endAuthorize.invoke(authState);
                    } catch (NoSuchFieldException | SecurityException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {

                    }
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
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
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
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


    private void callPostContactApi() {
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
                        frdList.clear();
                        frdList.addAll(response.body().getResult().getEmailfriends());
                        frdList.addAll(response.body().getResult().getGooglefriends());
                        frdList.addAll(response.body().getResult().getFbfriends());
                        frdList.addAll(response.body().getResult().getTwitterfriends());
                        setAdapter("6");
                    } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                        Utils.unAuthentication(context);
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

    private void callGetInstaUserFollowerApi() {
        Call<InstaFollowerBean> instaFollowerBeanCal = mService.getFetcherService(context, false, new Boolean[]{true}).GetTwitterFollower("follows?access_token=" + SharedPrefUtils.getToken(context));

        instaFollowerBeanCal.enqueue(new Callback<InstaFollowerBean>() {
            @Override
            public void onResponse(Call<InstaFollowerBean> call, Response<InstaFollowerBean> response) {
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

    public void onTwitterClick() {
        Log.d(TAG, "onTwitterClick: ");
        mTwitterAuthClient = new TwitterAuthClient();

        if (Utils.isInternetOn(context)) {
            mTwitterAuthClient.authorize((Activity) context, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

                @Override
                public void success(Result<TwitterSession> twitterSessionResult) {
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

    public void getContactEmail() {
        ContactList contactList = new ContactList(context);
        contectEmailJsonStrting = "";
        contectEmailJsonStrting = contactList.getContacts();
    }

    private static final String TAG = "SocialFragment";

    public void callSearchFrdApiFromActivity(String name, int offset) {
        if (name.length() > 0) {
            if (Utils.isInternetOn(context)) {

                map.put("offset", "" + offset);
                map.put("limit", "50");
                map.put("string", name);
                map.put("uid", preferences.getUserId());

                friendBeanCall = mService.getFetcherService(context).searchFrdList(map);

                friendBeanCall.enqueue(new Callback<FriendBean>() {
                    @Override
                    public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                        //            progreessbarMain.setVisibility(View.GONE);
                        if (response.code() == StaticConstant.RESULT_OK) {
                            if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                              //  if (offset == 1) {
                                    searchFrdList.clear();
                                    //               offsetForSearchFrd = 1;
                                //}
                                searchFrdList.addAll(response.body().getResult().getFriends());
                                //              offsetForSearchFrd++;
                                setAdapter("4");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FriendBean> call, Throwable t) {
                        //           progreessbarMain.setVisibility(View.GONE);
                        if (!call.isCanceled()) {
                            Utils.showToast(context, t.toString());
                        }
                    }
                });

            } else {
                Utils.showToast(context, context.getString(R.string.toast_no_internet));
            }
        } else {
        }
    }

    private void setAdapter(String type) {
//        0 = member, 1 = suggettion ,2 = Request ,3 = filter (invite), 4 = searchFriend
        friends.clear();
        switch (type) {
            case "4":
                friends.addAll(searchFrdList);
                break;

            case "6":
                friends.addAll(frdList);
                rl_Pager.setVisibility(View.GONE);
                rc_view.setVisibility(View.VISIBLE);
                break;
        }

        if (friendAdapter == null) {
            friendAdapter = new FriendAdapter(context, friends, type, "");
            rc_view.setAdapter(friendAdapter);
        } else {
            friendAdapter.type = type;
            friendAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void success(Map<String, String> map) {
        Log.d(TAG, "success: ");
    }

    @Override
    public void onFbFrdFetch(ArrayList<Object> list) {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mTwitterAuthClient != null) {
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case DrawerMainActivity.FACEBOOK_REQUEST_CODE:
                    faceBookLogin.onResult(requestCode, resultCode, data);
                    break;
                case InstagramHelperConstants.INSTA_LOGIN:
                    callGetInstaUserFollowerApi();
                    break;
            }
        }
    }

    public void onBackPressed() {
        if (lnyFinderInvite != null && lnyFinderInvite.getVisibility() == View.VISIBLE) {
            lnyFinderInvite.setVisibility(View.GONE);
            return;
        }
    }
}
