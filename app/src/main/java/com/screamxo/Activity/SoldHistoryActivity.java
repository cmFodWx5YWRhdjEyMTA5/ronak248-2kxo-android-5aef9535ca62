package com.screamxo.Activity;

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
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.SoldItemBean;
import com.example.apimodule.ApiBase.ApiBean.SoldItemDetail;
import com.example.apimodule.ApiBase.ApiBean.deliveryUpdate.DeliveryUpdatePojo;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.RahulWork.UploadDataActivity;
import com.screamxo.Adapter.SoldHistoryAdapter;
import com.screamxo.R;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.util.ArrayList;
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

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;

public class SoldHistoryActivity extends AppCompatActivity {
    private static final String TAG = "SoldHistoryActivity";

    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.no_data)
    LinearLayout noData;

    private SoldHistoryActivity context;
    private ArrayList<SoldItemDetail> soldItemDetailArrayList;
    private FetchrServiceBase mservice;
    private SoldHistoryAdapter soldHistryAdapter;
    private Map<String, String> map;
    private Call<SoldItemBean> getpurchaseitemcall;
    private Call<DeliveryUpdatePojo> postUpdateDelivery;
    private String userid, LIMIT = StaticConstant.LIMIT;
    private int pageCounter = 0;
    private boolean streamCall = false;
    private int totalCount = 0;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("userid")) {
            userid = getIntent().getExtras().getString("userid");
        }

        init();
        initControlValue();
        initControlListener();
        initFabIcon();
        GetSoldItemList();
    }

    public void init() {
        context = this;
        pageCounter = 0;
        totalCount = 0;
        streamCall = false;
        map = new HashMap<>();
        soldItemDetailArrayList = new ArrayList<>();
        mservice = new FetchrServiceBase();
    }

    public void initControlValue() {
        txtToolbarTitle.setText(R.string.title_sold_history);
        noData.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (soldHistryAdapter.getItemCount() - 1)) {
                    if (streamCall && soldItemDetailArrayList.size() < totalCount) {
                        streamCall = false;
                        GetSoldItemList();
                    }
                }
            }
        });
    }

    public void initControlListener() {
        imgToolbarLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                    Utils.hideKeyboard(SoldHistoryActivity.this);
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
                    Intent gotoNext = new Intent(SoldHistoryActivity.this, UploadDataActivity.class);
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

    public void GetSoldItemList() {
        Log.d(TAG, "GetSoldItemList: ");
        Map<String, String> map = new HashMap<>();
        map.put("uid", userid);
        map.put("offset", "" + pageCounter);
        map.put("limit", "" + 10);

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            getpurchaseitemcall = mservice.getFetcherService(context).GetSoldItemList(map);
            getpurchaseitemcall.enqueue(new Callback<SoldItemBean>() {
                @Override
                public void onResponse(Call<SoldItemBean> call, Response<SoldItemBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (pageCounter == 1) {
                                soldItemDetailArrayList.clear();
                            }
                            streamCall = true;
                            pageCounter = pageCounter + response.body().getResult().getItemdetails().size();
                            totalCount = response.body().getResult().getItemcount();
                            soldItemDetailArrayList.addAll(response.body().getResult().getItemdetails());
                            setAdapter();
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<SoldItemBean> call, Throwable t) {
                    t.printStackTrace();
                    Log.e(TAG, "onFailure: ", t);
                    streamCall = true;
                    progreessbar.setVisibility(View.GONE);
                }
            });

        } else {
            streamCall = true;
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void setAdapter() {
        if (soldItemDetailArrayList.size() == 0) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }

        if (soldHistryAdapter == null) {
            soldHistryAdapter = new SoldHistoryAdapter(context, soldItemDetailArrayList);
            recyclerView.setAdapter(soldHistryAdapter);
        } else {
            soldHistryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getpurchaseitemcall != null) {
            getpurchaseitemcall.cancel();
        }
    }

    public void onBackPressed() {
        finish();
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
            case REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS:
                if (data != null && data.getExtras() != null && data.getExtras().containsKey("IS_UPDATE")) {
                    init();
                    GetSoldItemList();
                    return;
                }
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void setChanged(boolean selected, int index) {

        soldItemDetailArrayList.get(index).setOpened(selected);
        soldHistryAdapter.notifyItemChanged(index);
    }

    public void deliveryUpdate(int item_id, int order_id, String postal_carrier, String estimated_delivery, String tracking_number) {
        Log.d(TAG, "deliveryUpdate: ");
        Map<String, String> map = new HashMap<>();
        map.put("item_id", item_id + "");
        map.put("order_id", "" + order_id);
        map.put("postal_carrier", postal_carrier);
        map.put("estimated_delivery", estimated_delivery);
        map.put("tracking_number", tracking_number);

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            postUpdateDelivery = mservice.getFetcherService(context).deliveryUpdate(map);
            postUpdateDelivery.enqueue(new Callback<DeliveryUpdatePojo>() {
                @Override
                public void onResponse(Call<DeliveryUpdatePojo> call, Response<DeliveryUpdatePojo> response) {
                    progreessbar.setVisibility(View.VISIBLE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.isSuccessful()) {
                            progreessbar.setVisibility(View.GONE);

                            if (response.body().getMessage().equalsIgnoreCase("Updated successsfully")) {
                                Toast.makeText(context, "Updated successsfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);

                        Toast.makeText(context, "Somthing went wrong...!", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<DeliveryUpdatePojo> call, Throwable t) {
                    t.printStackTrace();
                    Log.e(TAG, "onFailure: ", t);
                    streamCall = true;
                    progreessbar.setVisibility(View.GONE);
                }
            });
        }
    }
}


