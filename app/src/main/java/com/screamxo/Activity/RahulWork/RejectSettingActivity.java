package com.screamxo.Activity.RahulWork;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.BlockedList.Blocked;
import com.example.apimodule.ApiBase.ApiBean.BlockedList.Friend;
import com.example.apimodule.ApiBase.ApiBean.GetUserDetailBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Adapter.BlockedAdapter;
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

public class RejectSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RejectSettingActivity";
    @BindView(R.id.rc_view)
    RecyclerView rc_view;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.lny_image)
    LinearLayout lnyImage;
    Context context;
    @BindView(R.id.progreessbar)
    ProgressBar progressBar;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    Call<Blocked> rejectcall;
    Call<GetUserDetailBean> unBlockCall;
    ArrayList<Friend> friends;
    private FetchrServiceBase mService;
    private Preferences preferences;
    private int offsetLimit = 1;
    private int totalCount = 0;
    private LinearLayoutManager linearLayoutManager;
    private BlockedAdapter blockedAdapter;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private boolean streamCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_setting);
        ButterKnife.bind(this);
        initView();
        initFabIcon();
        initRejectApi();

        rc_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (linearLayoutManager.getItemCount() - 1)) {
                    if (streamCall && friends.size() < totalCount) {
                        streamCall = false;
                        initRejectApi();
                    }
                }
            }
        });

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
                    Utils.hideKeyboard(RejectSettingActivity.this);
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
                    Intent gotoNext = new Intent(RejectSettingActivity.this, UploadDataActivity.class);
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

    private void initRejectApi() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("offset", "" + offsetLimit);
        map.put("limit", "10");
        if (Utils.isInternetOn(context)) {
            progressBar.setVisibility(View.VISIBLE);
            rejectcall = mService.getFetcherService(context).rejectList(map);

            rejectcall.enqueue(new Callback<Blocked>() {
                @Override
                public void onResponse(Call<Blocked> call, Response<Blocked> response) {
                    progressBar.setVisibility(View.GONE);
                    streamCall = true;
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            offsetLimit++;
                            friends.clear();
                            friends.addAll(response.body().getResult().getFriends());
                            try {
                                totalCount = response.body().getResult().getTotalcount();
                            }
                            catch (Exception E){}
                            setAdapter();
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }
                @Override
                public void onFailure(Call<Blocked> call, Throwable t) {    t.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    streamCall = true;
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
            streamCall = true;
        }
    }


    @SuppressLint("SetTextI18n")
    private void initView() {
        preferences = new Preferences(this);
        mService = new FetchrServiceBase();
        context = RejectSettingActivity.this;
        txtToolbarTitle.setText(getString(R.string.txt_cap_reject_list));
        txtToolbarTitle.setTextSize(16);
        lnyImage.setVisibility(View.GONE);
        friends = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);
        rc_view.setLayoutManager(linearLayoutManager);
    }

    public void initUnBlockApi(int position) {
        Map<String, String> map = new HashMap<>();
        map.put("fromid", preferences.getUserId());
        map.put("toid", "" + friends.get(position).getUserid());
        if (Utils.isInternetOn(context)) {
            progressBar.setVisibility(View.VISIBLE);
            unBlockCall = mService.getFetcherService(context).UnBlockUser(map);
            unBlockCall.enqueue(new Callback<GetUserDetailBean>() {
                @Override
                public void onResponse(Call<GetUserDetailBean> call, Response<GetUserDetailBean> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            Utils.showToast(context, response.body().getMsg());
                            friends.remove(position);
                            setAdapter();
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetUserDetailBean> call, Throwable t) {
                    t.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }


    private void setAdapter() {
       // if (blockedAdapter == null) {
            blockedAdapter = new BlockedAdapter(context, friends);
            rc_view.setAdapter(blockedAdapter);
            rc_view.scrollToPosition(friends.size() - 1);
       // } else {
          //  blockedAdapter.notifyDataSetChanged();
           // rc_view.scrollToPosition(friends.size() - 1);
       // }

        if (friends.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rejectcall != null) {
            rejectcall.cancel();
        }

        if (unBlockCall != null) {
            unBlockCall.cancel();
        }
    }
}
