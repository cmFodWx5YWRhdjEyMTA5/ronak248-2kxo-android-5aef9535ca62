package com.screamxo.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.AddwatchedItemBean;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailBean;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailMedia;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailResult;
import com.example.apimodule.ApiBase.ApiBean.ItemReviewData;
import com.example.apimodule.ApiBase.ApiBean.SendMsgBean;
import com.example.apimodule.ApiBase.ApiBean.SoldItemBean;
import com.example.apimodule.ApiBase.ApiBean.SoldItemDetail;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Adapter.HomeViewPagerAdapter;
import com.screamxo.Adapter.ItemReviewAdapter;
import com.screamxo.Adapter.SoldItemListAdapter;
import com.screamxo.Fragment.ImagePagerFragment;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.RequestBodyConveter;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.Validations;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CHAT_ACTIVITY_RESULTS;

/**
 * No Intent on this class is used as per my knowledge.
 */
@Deprecated
public class ItemDetailActivity extends AppCompatActivity {
    private static final String TAG = "ItemDetailActivity";

    public SoldItemListAdapter solditemlistadapter;
    public ItemReviewAdapter itemreviewadapter;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.txt_buy_item)
    TextView txtBuyItem;
    @BindView(R.id.img_user)
    ImageView imgUser;
    @BindView(R.id.txt_username)
    TextView txtUsername;
    @BindView(R.id.txt_user_city)
    TextView txtUserCity;
    @BindView(R.id.txt_item_cost)
    TextView txtItemCost;
    @BindView(R.id.txt_item_description)
    TextView txtItemDescription;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_watch)
    TextView txtWatch;
    @BindView(R.id.txt_sold_list)
    TextView txtSoldList;
    @BindView(R.id.img_arrow_watch)
    ImageView imgArrowWatch;
    @BindView(R.id.img_arrow_message)
    ImageView imgArrowMessage;
    @BindView(R.id.img_arrow_review)
    ImageView imgArrowReview;
    @BindView(R.id.img_arrow_buy_item)
    ImageView imgArrowBuyItem;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.txt_message)
    TextView txtMessage;
    @BindView(R.id.linear_no_data)
    LinearLayout linearNoData;
    @BindView(R.id.activity_item_detail)
    RelativeLayout activityItemDetail;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.linear_user_detail)
    LinearLayout linearUserDetail;
    String itemid;
    @BindView(R.id.msg_liner)
    LinearLayout msgLiner;
    @BindView(R.id.liner_direct_msg)
    LinearLayout linerDirectMsg;
    @BindView(R.id.liner_quick_msg)
    LinearLayout linerQuickMsg;
    @BindView(R.id.txt_direct)
    TextView txtDirect;
    @BindView(R.id.txt_quick)
    TextView txtQuick;
    @BindView(R.id.txt_review)
    TextView txtReview;
    @BindView(R.id.txt_condition)
    TextView txtCondition;
    @BindView(R.id.txt_payment)
    TextView txtPayment;
    @BindView(R.id.txt_policy)
    TextView txtPolicy;
    @BindView(R.id.txt_shipping)
    TextView txtShipping;
    @BindView(R.id.txt_other)
    TextView txtOther;
    @BindView(R.id.img_other)
    ImageView imgOther;
    @BindView(R.id.txt_manufacturer)
    TextView txtManufacturer;
    @BindView(R.id.txt_negotiable)
    TextView txtNegotiable;
    @BindView(R.id.txt_ship)
    TextView txtShip;
    @BindView(R.id.txt_return_policy)
    TextView txtReturnPolicy;
    @BindView(R.id.txt_forms_payment)
    TextView txtFormsPayment;
    @BindView(R.id.txt_need_asap)
    TextView txtNeedAsap;
    @BindView(R.id.txt_other_quick)
    TextView txtOtherQuick;
    @BindView(R.id.img_other_quick)
    ImageView imgOtherQuick;
    Map<String, String> map;
    ArrayList<ItemDetailMedia> mediaArrayList;
    Call<ItemDetailBean> streampostcall;
    Call<AddwatchedItemBean> addwatcheditemcall;
    Call<SoldItemBean> solditemcall;
    Call<SendMsgBean> sendMsgBeanCall;
    int height, width, widthMedia, heightMedia;
    RequestBodyConveter requestbodyconverter;
    private Context context;
    private Preferences preferences;
    private FetchrServiceBase mservice;
    private Validations mvalidations;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<SoldItemDetail> solditemlist;
    private ArrayList<ItemReviewData> reviewlist;
    private ItemDetailResult itemDetailResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Log.d(TAG, "onCreate: ");
        ButterKnife.bind(this);
        init();
        initControlValue();
        initControl();
    }

    public void init() {
        context = this;
        preferences = new Preferences(context);
        solditemlist = new ArrayList<>();
        mediaArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);
        mservice = new FetchrServiceBase();
        mvalidations = new Validations();
        requestbodyconverter = new RequestBodyConveter();
        map = new HashMap<>();
        Point size = new Point();
        WindowManager w = getWindowManager();
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            BitmapDrawable bd1 = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.img_placeholder);

            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
            w.getDefaultDisplay().getSize(size);
            widthMedia = size.y;
            heightMedia = bd1.getBitmap().getHeight();
        } catch (Exception ignored) {

        }
    }

    public void initControlValue() {
        txtToolbarTitle.setText(R.string.title_item_detail);
        imgToolbarLeftIcon.setImageResource(R.mipmap.ico_up);
        imgToolbarLeftIcon.setRotation(-90);

        imgToolbarRightIcon.setVisibility(View.GONE);
        imgToolbarRightIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.edit_pencil));

        recyclerView.setLayoutManager(linearLayoutManager);
//        imgSold.setVisibility(View.GONE);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("itemid")) {
            itemid = getIntent().getExtras().getString("itemid");
        }
        imgToolbarRightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemDetailActivity.this, SellItemActivity.class);
                intent.putExtra("isedit", true);
                intent.putExtra("beanObject", itemDetailResult);
                startActivity(intent);
            }
        });

//        callItemDetailapi();
    }

    public void initControl() {
        imgToolbarLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WatchedItemListActivity.class);
                startActivityForResult(intent, StaticConstant.REQUEST_SELL_ITEM);
            }
        });
    }

    public void setSoldItemAdapter() {
        if (solditemlistadapter == null) {
            solditemlistadapter = new SoldItemListAdapter(context, solditemlist);
            recyclerView.setAdapter(solditemlistadapter);
        } else {
            solditemlistadapter.notifyDataSetChanged();
        }
    }

    public void setItemReviewAdapter() {
        if (itemreviewadapter == null) {
            itemreviewadapter = new ItemReviewAdapter(context, reviewlist);
            recyclerView.setAdapter(itemreviewadapter);
        } else {
            itemreviewadapter.notifyDataSetChanged();
        }
    }

    public void callItemDetailapi() {

        Map<String, String> map = new HashMap<>();
        map.put("itemid", itemid);
        map.put("myid", preferences.getUserId());
        if (Utils.isInternetOn(context)) {

            progreessbar.setVisibility(View.VISIBLE);
            streampostcall = mservice.getFetcherService(context).GetItemDetail(map);

            streampostcall.enqueue(new Callback<ItemDetailBean>() {
                @Override
                public void onResponse(Call<ItemDetailBean> call, Response<ItemDetailBean> response) {

                    progreessbar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {

                            linearUserDetail.setVisibility(View.VISIBLE);

                            mediaArrayList.clear();
                            mediaArrayList.addAll(response.body().getResult().getMedia());
                            itemDetailResult = response.body().getResult();
                            reviewlist = itemDetailResult.getReviewdata();

                            setVibility();
                            setValue();
                            setImagePager();

                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemDetailBean> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: ", t);

                    /*view network adapter-> username purchased your item view item detail and boom error. */
                    Utils.showToast(context, t.toString());
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @SuppressLint("SetTextI18n")
    private void setValue() {

        txtToolbarTitle.setText(itemDetailResult.getItemName());
        txtItemCost.setText("$ " + itemDetailResult.getItemPrice());

        if (itemDetailResult.getUsername() != null) {
            txtUsername.setText(itemDetailResult.getUsername());
        }
        if (itemDetailResult.getUsercity() != null) {
            txtUserCity.setText(itemDetailResult.getUsercity());
        }

        if (itemDetailResult.getItemDescription() != null) {
            txtItemDescription.setText(itemDetailResult.getItemDescription());
        }

        if (itemDetailResult.getCreatedDate() != null) {
            txtDate.setText(mvalidations.dateFormationUTCtoLocal("yyyy-MM-dd'T'HH:mm:ss", itemDetailResult.getCreatedDate()));
        }

        if (itemDetailResult.getUserphotothumb() != null && !itemDetailResult.getUserphotothumb().equals("")) {
            Picasso.with(context)
                    .load(itemDetailResult.getUserphotothumb())
                    .placeholder(R.mipmap.user)
                    .error(R.mipmap.user)
                    .transform(new CircleTransform())
                    .resize(width, height)
                    .into(imgUser);
        }

        if (itemDetailResult.getIswatched() == 1) {
            txtWatch.setText(R.string.txt_unwatch);
        } else {
            txtWatch.setText(R.string.txt_watch);
        }

    }

    private void setVibility() {
//      review text always visible
        if (itemDetailResult.getMyitem() == 1 && itemDetailResult.getIspurchased().equals("0")) {

            imgToolbarRightIcon.setVisibility(View.VISIBLE);
            txtWatch.setVisibility(View.GONE);
            txtBuyItem.setVisibility(View.GONE);
            txtMessage.setVisibility(View.GONE);
            if (itemDetailResult.getItemQtyRemained() == itemDetailResult.getItemQtyRemained()) {
                txtSoldList.setVisibility(View.GONE);
            } else {
                txtSoldList.setVisibility(View.VISIBLE);
            }

        } else {

            if (itemDetailResult.getIspurchased().equals("0")) {
                txtWatch.setVisibility(View.VISIBLE);
                txtBuyItem.setVisibility(View.VISIBLE);
                txtMessage.setVisibility(View.VISIBLE);
            } else {
                txtWatch.setVisibility(View.GONE);
                txtBuyItem.setVisibility(View.GONE);
                txtMessage.setVisibility(View.GONE);
            }
        }

    }

    private void setImagePager() {

        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(((ItemDetailActivity) context).getSupportFragmentManager());
        ImagePagerFragment fragment1;
        if (mediaArrayList.size() == 0) {
            fragment1 = new ImagePagerFragment(0, mediaArrayList, isSoldVisible());
            adapter.addFragment(fragment1, "" + 0);
        } else {

            for (int i = 0; i < mediaArrayList.size(); i++) {
                fragment1 = new ImagePagerFragment(i, mediaArrayList, isSoldVisible());
                adapter.addFragment(fragment1, "" + i);
            }
        }
        viewPager.setAdapter(adapter);
    }

    Boolean isSoldVisible() {
        return itemDetailResult.getIspurchased().equals("1");
    }

    public void onBuyItemClick(View view) {
//        msgLiner.setVisibility(View.GONE);
//        linearNoData.setVisibility(View.GONE);
//        imgArrowMessage.setVisibility(View.GONE);
//        imgArrowReview.setVisibility(View.GONE);
//        recyclerView.setVisibility(View.GONE);
//        linerQuickMsg.setVisibility(View.GONE);
//        linerDirectMsg.setVisibility(View.GONE);
//
////        if (itemid != null && itemquantity != null && txtItemCost.getText() != null && shippingcost != null) {
//        Intent i = new Intent(context, PaymentDetailActivity.class);
//        i.putExtra("itemcost", itemDetailResult.getItemPrice());
//        i.putExtra("shippingcost", itemDetailResult.getItemShippingCost());
//        i.putExtra("itemquantiy", "" + itemDetailResult.getItemQty());
//        i.putExtra("itemid", "" + itemDetailResult.getItemId());
//        startActivity(i);
//        }
    }

    public void onReviewClick(View view) {
        linerDirectMsg.setVisibility(View.GONE);
        linerQuickMsg.setVisibility(View.GONE);
        msgLiner.setVisibility(View.GONE);
        if (reviewlist.size() == 0) {
            imgArrowReview.setVisibility(View.VISIBLE);
            imgArrowMessage.setVisibility(View.INVISIBLE);
            linearNoData.setVisibility(View.VISIBLE);
            txtNoData.setText(R.string.msg_no_review);
            recyclerView.setVisibility(View.GONE);
        } else {
            imgArrowReview.setVisibility(View.VISIBLE);
            imgArrowMessage.setVisibility(View.VISIBLE);
            linearNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            setItemReviewAdapter();
        }
    }

    public void onWatchclick(View view) {

        linerQuickMsg.setVisibility(View.GONE);
        linerDirectMsg.setVisibility(View.GONE);
        msgLiner.setVisibility(View.GONE);
        linearNoData.setVisibility(View.GONE);
        imgArrowMessage.setVisibility(View.GONE);
        imgArrowReview.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        callAddWatchedItemapi();
    }

    public void onMessageClick(View view) {

        if (imgArrowMessage.getVisibility() == View.VISIBLE) {
            imgArrowMessage.setVisibility(View.GONE);
        } else {
            imgArrowMessage.setVisibility(View.VISIBLE);
        }
        if (msgLiner.getVisibility() == View.VISIBLE) {
            msgLiner.setVisibility(View.GONE);
            if (linerQuickMsg.getVisibility() == View.VISIBLE) {
                linerQuickMsg.setVisibility(View.GONE);
            } else if (linerDirectMsg.getVisibility() == View.VISIBLE) {
                linerDirectMsg.setVisibility(View.GONE);
            }
        } else {
            msgLiner.setVisibility(View.VISIBLE);
        }
        txtDirect.setTextColor(getResources().getColor(android.R.color.white));
        txtQuick.setTextColor(getResources().getColor(android.R.color.white));
        imgArrowReview.setVisibility(View.INVISIBLE);
        imgArrowWatch.setVisibility(View.INVISIBLE);
        imgArrowBuyItem.setVisibility(View.INVISIBLE);
        linearNoData.setVisibility(View.GONE);
    }

    public void onDirectClick(View view) {
        txtDirect.setTextColor(getResources().getColor(R.color.colorBlack));
        txtQuick.setTextColor(getResources().getColor(android.R.color.white));
        linearNoData.setVisibility(View.GONE);
        imgArrowReview.setVisibility(View.INVISIBLE);
        imgArrowWatch.setVisibility(View.INVISIBLE);
        imgArrowBuyItem.setVisibility(View.INVISIBLE);
        linerDirectMsg.setVisibility(View.VISIBLE);
        linerQuickMsg.setVisibility(View.GONE);
    }

    public void OnQuickClick(View view) {
        txtDirect.setTextColor(getResources().getColor(android.R.color.white));
        txtQuick.setTextColor(getResources().getColor(R.color.colorBlack));
        linearNoData.setVisibility(View.GONE);
        imgArrowReview.setVisibility(View.INVISIBLE);
        imgArrowWatch.setVisibility(View.INVISIBLE);
        imgArrowBuyItem.setVisibility(View.INVISIBLE);
        linerDirectMsg.setVisibility(View.GONE);
        linerQuickMsg.setVisibility(View.VISIBLE);
    }

    public void callAddWatchedItemapi() {

        // if isWatched=0 unwatched then action = 0 and isWatched=1 watched then action = 1
        // action=0 means remove from list ad action=1 means add into the list
        String action;
        if (itemDetailResult.getIswatched() == 0) {
            action = "0";
        } else {
            action = "1";
        }

        Map<String, String> map = new HashMap<>();
        map.put("itemid", itemid);
        map.put("uid", preferences.getUserId());
        map.put("action", action);

        if (Utils.isInternetOn(context)) {

            progreessbar.setVisibility(View.VISIBLE);
            addwatcheditemcall = mservice.getFetcherService(context).AddWatchedItem(map);
            addwatcheditemcall.enqueue(new Callback<AddwatchedItemBean>() {
                @Override
                public void onResponse(Call<AddwatchedItemBean> call, Response<AddwatchedItemBean> response) {

                    progreessbar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (itemDetailResult.getIswatched() == 0) {
                                itemDetailResult.setIswatched(1);
                                txtWatch.setText(R.string.txt_unwatch);
                            } else {
                                txtWatch.setText(R.string.txt_watch);
                                itemDetailResult.setIswatched(0);
                            }


                        }
                    }
                }

                @Override
                public void onFailure(Call<AddwatchedItemBean> call, Throwable t) {    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    Utils.showToast(context, t.toString());
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void onSoldItemListClick(View view) {
        callSoldItemListapi();
    }

    public void callSoldItemListapi() {
        imgArrowWatch.setVisibility(View.GONE);
        imgArrowMessage.setVisibility(View.VISIBLE);
        imgArrowReview.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("offset", "1");
        map.put("limit", "10");

        if (Utils.isInternetOn(context)) {

            progreessbar.setVisibility(View.VISIBLE);
            solditemcall = mservice.getFetcherService(context).GetSoldItemList(map);
            solditemcall.enqueue(new Callback<SoldItemBean>() {
                @Override
                public void onResponse(Call<SoldItemBean> call, Response<SoldItemBean> response) {

                    progreessbar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            solditemlist = response.body().getResult().getItemdetails();
                            if (solditemlist.size() == 0) {
                                linearNoData.setVisibility(View.VISIBLE);
                                txtNoData.setText(R.string.msg_no_data);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                linearNoData.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                setSoldItemAdapter();
                            }

                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<SoldItemBean> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                    Utils.showToast(context, t.toString());
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callSendMsgApi(String msg) {

        linerDirectMsg.setVisibility(View.GONE);
        linerQuickMsg.setVisibility(View.GONE);

        if (Utils.isInternetOn(context)) {

            progreessbar.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();

            map.put("fromid", "" + preferences.getUserId());
            map.put("toid", "" + itemDetailResult.getUserid());
            map.put("messagetype", "1");
            map.put("messagedetail", "" + msg);

//            if (msgDeleteTimer != null)
//                map.put("messagetiming", msgDeleteTimer);

//            if (iscameFromSellerChat)
            map.put("itemid", "" + itemDetailResult.getItemId());

            sendMsgBeanCall = mservice.getFetcherService(context).sendMsg(requestbodyconverter.converRequestBodyFromMap(map), null);

            sendMsgBeanCall.enqueue(new Callback<SendMsgBean>() {
                @Override
                public void onResponse(Call<SendMsgBean> call, Response<SendMsgBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<SendMsgBean> call, Throwable t) {    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void onBackPressed() {
        finish();
    }

    @OnClick({R.id.txt_condition, R.id.txt_payment, R.id.txt_policy, R.id.txt_shipping, R.id.txt_other, R.id.img_other, R.id.txt_manufacturer, R.id.txt_negotiable, R.id.txt_ship, R.id.txt_return_policy, R.id.txt_forms_payment, R.id.txt_need_asap, R.id.txt_other_quick, R.id.img_other_quick})
    public void onClick(View view) {
        switch (view.getId()) {
//             Direct Tab from Message all text
            case R.id.txt_condition:
                callIntentScreen("Condition");
                break;
            case R.id.txt_payment:
                callIntentScreen("Payment");
                break;
            case R.id.txt_policy:
                callIntentScreen("Policy");
                break;
            case R.id.txt_shipping:
                callIntentScreen("Shipping");
                break;

            case R.id.txt_other:
            case R.id.img_other:
                callIntentScreen("Other");
                break;

//            Quick Tab from Message all text
            case R.id.txt_manufacturer:
                callSendMsgApi("Original Manufacturer?");
                break;
            case R.id.txt_negotiable:
                callSendMsgApi("Negotiable");
                break;
            case R.id.txt_ship:
                callSendMsgApi("Do you ship internationally?");
                break;
            case R.id.txt_return_policy:
                callSendMsgApi("What is your return policy?");
                break;
            case R.id.txt_forms_payment:
                callSendMsgApi("Accepted forms of payment?");
                break;
            case R.id.txt_need_asap:
                callSendMsgApi("Need This ASAP!");
                break;

            case R.id.txt_other_quick:
            case R.id.img_other_quick:
                callSendMsgApi("Other");
                break;
        }
    }

    void callIntentScreen(String msg) {
        Intent intent = new Intent(ItemDetailActivity.this, ChatActivity.class);
        intent.putExtra("otherUid", "" + itemDetailResult.getUserid());
        intent.putExtra("username", itemDetailResult.getUsername());
        intent.putExtra("fullname", itemDetailResult.getFname() + " " + itemDetailResult.getLname());
        intent.putExtra("itemId", "" + itemDetailResult.getItemId());
        intent.putExtra("userProfile",itemDetailResult.getUserphoto());
        intent.putExtra("message", msg + ":");
        startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.removeAllViews();
        callItemDetailapi();
    }
}
