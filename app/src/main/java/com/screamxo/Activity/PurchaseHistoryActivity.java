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

import com.example.apimodule.ApiBase.ApiBean.PurchaseHistoryBean;
import com.example.apimodule.ApiBase.ApiBean.PurchaseHistoryDetail;
import com.example.apimodule.ApiBase.ApiBean.PurchaseOrderdetail;
import com.example.apimodule.ApiBase.ApiBean.QtyBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.RahulWork.UploadDataActivity;
import com.screamxo.Activity.wallet.WalletSendReceiveActivity;
import com.screamxo.Adapter.PurchaseItemListAdapter;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

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

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_WALLET_ACTIVITY_RESULTS;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private static final String TAG = "PurchaseHistoryActivity";
    @BindView(R.id.progreessbar)
    public ProgressBar progreessbar;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.no_data)
    LinearLayout noData;
    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private boolean streamCall = false;
    private int totalCount = 0;
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<PurchaseHistoryDetail> puchaseitemlist;
    private ArrayList<PurchaseOrderdetail> purchaseOrderList;
    private FetchrServiceBase mservice;
    private PurchaseItemListAdapter mpurchaseadapter;
    private Map<String, String> map;
    private Call<PurchaseHistoryBean> getpurchaseitemcall;
    private String userid, shippingaddress = "";
    ;
    private int pageCounter = 0;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        ButterKnife.bind(this);
        init();
        initFabIcon();
        initControlValue();
        initControlListener();
    }

    public void init() {
        context = this;
        preferences = new Preferences(context);
        puchaseitemlist = new ArrayList<>();
        purchaseOrderList = new ArrayList<>();
        map = new HashMap<>();
        mservice = new FetchrServiceBase();
        linearLayoutManager = new LinearLayoutManager(context);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("userid")) {
            userid = getIntent().getExtras().getString("userid");
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
                    Utils.hideKeyboard(PurchaseHistoryActivity.this);
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
                    Intent gotoNext = new Intent(PurchaseHistoryActivity.this, UploadDataActivity.class);
                    startActivityForResult(gotoNext, REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS);
                }
            });

            sbWorld = findViewById(R.id.sbWorld);
            sbWorld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    startActivityForResult(new Intent(PurchaseHistoryActivity.this, WalletSendReceiveActivity.class), REQ_CODE_WALLET_ACTIVITY_RESULTS);
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
                    returnIntent.putExtra("FRAGMENT_INDEX", 2);
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

    public void initControlValue() {
        noData.setVisibility(View.GONE);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (recyclerView.getAdapter().getItemCount() - 1)
                        && totalCount > puchaseitemlist.size()) {
                    if (streamCall && puchaseitemlist.size() < totalCount) {
                        streamCall = false;
                        callGetPurchaseListApi();
                    }
                }
            }
        });
        callGetPurchaseListApi();
    }

    public void gotoLogin() {
        Intent gotoLogin = new Intent(this, CommonLoginSignUpActivity.class);
        gotoLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(gotoLogin);
        finish();
    }

    public void initControlListener() {

        imgToolbarLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void callGetPurchaseListApi() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", userid);
        map.put("offset", "" + pageCounter);
        map.put("limit", "10");
        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            getpurchaseitemcall = mservice.getFetcherService(context).GetPurchasedItemList(map);
            getpurchaseitemcall.enqueue(new Callback<PurchaseHistoryBean>() {
                @Override
                public void onResponse(Call<PurchaseHistoryBean> call, Response<PurchaseHistoryBean> response) {
                    progreessbar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            streamCall = true;
                            puchaseitemlist.clear();
                            puchaseitemlist.addAll(response.body().getResult().getItemdetails());
                            shippingaddress=response.body().getResult().getItemdetails().get(0).getItem_shipping_details();

                            for (int i = 0; i < puchaseitemlist.size(); i++) {
                                pageCounter++;

                                if (puchaseitemlist.get(i).getOrderdetails().size() != 0) {
                                    ArrayList<QtyBean> qtyList = new ArrayList<>();
                                    try {
                                        JSONArray data = new JSONArray(puchaseitemlist.get(i).getItemId());

                                        for (int p = 0; p < data.length(); p++) {
                                            JSONObject obj = (JSONObject) data.get(p);
                                            int id = obj.getInt("item_id");
                                            int qty = obj.getInt("productqty");

                                            qtyList.add(new QtyBean(id, qty));
                                        }

                                    } catch (Exception e) {
                                        Log.e("Parsing Exception: ", e.getMessage());
                                    }

                                    for (int j = 0; j < puchaseitemlist.get(i).getOrderdetails().size(); j++) {
                                        PurchaseOrderdetail pd = puchaseitemlist.get(i).getOrderdetails().get(j);
                                        pd.setQty(qtyList.get(j));
                                        purchaseOrderList.add(pd);
                                    }
                                }
                            }
                            totalCount = response.body().getResult().getItemcount();
                            if (purchaseOrderList.size() == 0) {
                                noData.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                noData.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                setAdapter();
                            }
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PurchaseHistoryBean> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    streamCall = true;
                    Utils.showToast(context, t.toString());
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
            streamCall = true;
        }
    }

    public void setAdapter() {
        if (mpurchaseadapter == null) {
            mpurchaseadapter = new PurchaseItemListAdapter(this, purchaseOrderList, userid,shippingaddress);
            recyclerView.setAdapter(mpurchaseadapter);
        } else {
            mpurchaseadapter.notifyDataSetChanged();
        }
    }

    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult requestCode: " + requestCode);
        Log.d(TAG, "onActivityResult resultCode: " + resultCode);

        if (resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("FRAGMENT_INDEX")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", data.getExtras().getInt("FRAGMENT_INDEX"));
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void setChanged(boolean selected, int index) {

        purchaseOrderList.get(index).setOpened(selected);
        mpurchaseadapter.notifyItemChanged(index);
    }

}
