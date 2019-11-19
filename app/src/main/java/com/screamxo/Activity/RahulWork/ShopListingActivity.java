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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.ShopData.Shop;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Adapter.ShopListAdapter;
import com.screamxo.R;
import com.screamxo.Utils.EventData;
import com.screamxo.Utils.HidingScrollListener;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import org.greenrobot.eventbus.EventBus;

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
import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;

public class ShopListingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ShopListingActivity";
    @BindView(R.id.lny_image)
    LinearLayout lny_image;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.rc_view)
    RecyclerView rcView;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    Preferences preferences;
    private String uId;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    int position = 0, pageCounter = 1;
    Context context;
    private FetchrServiceBase mService;
    private boolean streamCall = false;
    ArrayList<Object> apiList;
    ShopListAdapter shopListAdapter;
    private int totalCount = 0;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_listing);
        ButterKnife.bind(this);
        initData();
        initFabIcon();
        callShopIteamApi();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        context = ShopListingActivity.this;
        preferences = new Preferences(context);
        position = getIntent().getIntExtra("position", 0);
        uId = preferences.getUserId();
        mService = new FetchrServiceBase();
        apiList = new ArrayList<>();
        lny_image.setVisibility(View.GONE);
        txtToolbarTitle.setTextSize(15);
        switch (position) {
            case 1:
                txtToolbarTitle.setText(getString(R.string.txt_watching));
                break;
            case 2:
                txtToolbarTitle.setText(getString(R.string.txt_recommended));
                break;
            case 3:
                txtToolbarTitle.setText(getString(R.string.txt_recently_viewed));
                break;
            case 4:
                txtToolbarTitle.setText(getString(R.string.txt_world_trending));
                break;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rcView.setLayoutManager(linearLayoutManager);
        rcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                rcView.addOnScrollListener(new HidingScrollListener() {
                    @Override
                    public void onHide()
                    {
                        hideViews();
                    }

                    @Override
                    public void onShow() {

                        showViews();

                    }
                });

                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (shopListAdapter.getItemCount() - 1)) {
                    if (streamCall && apiList.size() < totalCount) {
                        streamCall = false;
                        callShopIteamApi();
                    }
                }
            }
        });


    }

    private void initFabIcon() {
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
                public void onMenuOpened(FloatingMenuButton menu) {
                    Utils.hideKeyboard(ShopListingActivity.this);
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.cross));
                }

                @Override
                public void onMenuClosed(FloatingMenuButton menu) {
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                }
            });
            floatingButton.setOnClickBtnListener(new FloatingMenuButton.OnClickBtnListener() {
                @Override
                public void onClickBtn() {
                    try {
                        if (!floatingButton.isMenuOpen()) {
                            if (floatingButton.getBackground().getConstantState() != null
                                    && floatingButton.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ic_float_menu_up).getConstantState())) {
                                rcView.scrollToPosition(0);

                                if (!floatingButton.isMenuOpen()) {
                                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                                    floatingButton.closeMenu();
                                }
                                floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));

                                return;
                            }
                        }

                    } catch (Exception ignored) {
                    }
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
                    Intent gotoNext = new Intent(ShopListingActivity.this, UploadDataActivity.class);
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

    private void callShopIteamApi() {
        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("uid", uId);
            map.put("offset", "" + pageCounter);
            map.put("limit", StaticConstant.LIMIT);
            map.put("type", "" + position);
            map.put("searchstring", "");
            map.put("categoryid", "0");

            map.put("trending", "2");
            Call<Shop> shopItemApiCall = mService.getFetcherService(context).ProfileShop(map);

            shopItemApiCall.enqueue(new Callback<Shop>() {
                @Override
                public void onResponse(Call<Shop> call, Response<Shop> response) {
                    if (pageCounter == 1) {
                        apiList.clear();
                    }
                    progreessbar.setVisibility(View.GONE);
                    streamCall = true;
                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null && response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                        pageCounter++;
                        switch (position) {
                            case 1:
                                apiList.addAll(response.body().getResult().getWatched().getItemdetails());
                                totalCount = response.body().getResult().getWatched().getItemcount();
                                break;
                            case 2:
                                apiList.addAll(response.body().getResult().getRecommend().getItemdetails());
                                totalCount = response.body().getResult().getRecommend().getItemcount();
                                break;
                            case 3:
                                apiList.addAll(response.body().getResult().getRecentitems().getItemdetails());
                                totalCount = response.body().getResult().getRecentitems().getItemcount();
                                break;
                            case 4:
                                apiList.addAll(response.body().getResult().getGlobaltrend().getItemdetails());
                                totalCount = response.body().getResult().getGlobaltrend().getItemcount();
                                break;
                        }
                        setAdapter();
                    }
                }

                @Override
                public void onFailure(Call<Shop> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                    streamCall = true;
                    t.printStackTrace();
                }
            });
        } else {
            streamCall = true;
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void setAdapter() {
        Log.d(TAG, "setAdapter: ");
        if (shopListAdapter == null) {
            shopListAdapter = new ShopListAdapter(context, apiList);
            rcView.setAdapter(shopListAdapter);
        } else {
            shopListAdapter.notifyDataSetChanged();
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

}
