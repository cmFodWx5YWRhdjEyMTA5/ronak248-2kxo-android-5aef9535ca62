package com.screamxo.Activity.RahulWork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.CommentBean;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailBean;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailMedia;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailResult;
import com.example.apimodule.ApiBase.ApiBean.ItemPurchasedata;
import com.example.apimodule.ApiBase.ApiBean.ItemReviewData;
import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.ApiBean.Tracking.UpdateTrackingDetailReposne;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.ChatActivity;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Activity.wallet.WalletSendReceiveActivity;
import com.screamxo.Adapter.HomeViewPagerAdapter;
import com.screamxo.Adapter.ItemDetailAdapter;
import com.screamxo.Adapter.ItemPurchaseDataAdapter;
import com.screamxo.Emoji.CustomText;
import com.screamxo.Fragment.ImagePagerFragment;
import com.screamxo.Interface.DialogInterfaceAction;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.Preferences;
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
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_BOOST_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_BUY_CONG_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CHAT_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CONFIGURE_PAYEMENT_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_EDIT_SELL_ITEM_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_WALLET_ACTIVITY_RESULTS;

public class ItemDetailsAcitvity extends AppCompatActivity implements View.OnClickListener, DialogBox.OnShopItemOptionClickListener {
    public static final int ID = R.id.ItemDetails_ID;
    private static final String TAG = "ItemDetailsAcitvity";
    @BindView(R.id.rl_purchase_data)
    public
    RelativeLayout rl_purchase_data;
    @BindView(R.id.progreessbar)
    public
    ProgressBar progreessbar;
    public String itemid = "";
    @BindView(R.id.txtPrice)
    TextView txtPrice;
    @BindView(R.id.txtDescription)
    CustomText txtDescription;
    @BindView(R.id.ll_message_container)
    LinearLayout ll_message_container;
    @BindView(R.id.tv_topics)
    TextView tv_topics;
    @BindView(R.id.tv_questions)
    TextView tv_questions;
    @BindView(R.id.ll_topics_container)
    LinearLayout ll_topics_container;
    @BindView(R.id.ll_questions_container)
    LinearLayout ll_questions_container;
    @BindView(R.id.img_profile)
    ImageView img_profile;
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtDesc)
    TextView txtDesc;
    @BindView(R.id.rl_BottomSheet)
    RelativeLayout rlBottomSheet;
    @BindView(R.id.ll_BottomSheet)
    LinearLayout ll_BottomSheet;
    @BindView(R.id.txtConditionData)
    TextView txtConditionData;
    @BindView(R.id.txtShippingData)
    TextView txtShippingData;
    @BindView(R.id.txtLocationData)
    TextView txtLocationData;
    Unbinder unbinder;
    @BindView(R.id.rl_Bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.rc_view)
    RecyclerView rcView;
    @BindView(R.id.rv_purchase_data)
    RecyclerView rv_purchase_data;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.rl_Close)
    RelativeLayout rlClose;
    @BindView(R.id.rl_Reviews)
    RelativeLayout rl_Reviews;
    @BindView(R.id.btnReview)
    Button btnReview;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.user_image_container)
    FrameLayout user_image_container;
    @BindView(R.id.user_iv)
    ImageView user_iv;
    @BindView(R.id.imgBookmark)
    ImageView imgBookmark;
    @BindView(R.id.txtProductName)
    TextView txtProductName;
    @BindView(R.id.txtBuy)
    TextView txtBuy;
    Call<CommentBean> reportPostCall;
    @BindView(R.id.tv_topics_condition)
    TextView tv_topics_condition;
    @BindView(R.id.tv_topics_payment)
    TextView tv_topics_payment;
    @BindView(R.id.tv_topics_policy)
    TextView tv_topics_policy;
    @BindView(R.id.tv_topics_shipping)
    TextView tv_topics_shipping;
    @BindView(R.id.tv_topics_others)
    TextView tv_topics_others;
    @BindView(R.id.tv_questions_original_manufacturer)
    TextView tv_questions_original_manufacturer;
    @BindView(R.id.tv_questions_negotiable)
    TextView tv_questions_negotiable;
    @BindView(R.id.tv_questions_ship_internationally)
    TextView tv_questions_ship_internationally;
    @BindView(R.id.tv_questions_return_policy)
    TextView tv_questions_return_policy;
    @BindView(R.id.tv_questions_accepted_payment_form)
    TextView tv_questions_accepted_payment_form;
    @BindView(R.id.tv_questions_need_asap)
    TextView tv_questions_need_asap;
    @BindView(R.id.tv_questions_other)
    TextView tv_questions_other;
    @BindView(R.id.ico_back)
    ImageView ico_back;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image3)
    ImageView image3;
    Context context;
    Call<ItemDetailBean> streampostcall;
    ArrayList<ItemDetailMedia> mediaArrayList;
    Preferences preferences;
    FragmentActivity myContext;
    int height = 0, width = 0;
    ItemDetailAdapter itemDetailAdapter;
    Boolean isBookmarkCheck = false;
    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    boolean isUpdate = false;
    private FetchrServiceBase mService;
    private FetchrServiceBase mservice;
    private FetchrServiceBase mservice1;
    private ItemDetailResult itemDetailResult;
    private ItemPurchaseDataAdapter itemPurchaseDataAdapter;
    private ArrayList<ItemReviewData> reviewlist;
    private ArrayList<ItemPurchasedata> purchaseDatas;
    private boolean addTrackingDetails;
    private Call<UpdateTrackingDetailReposne> updatetrackingdetailCall;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_item_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        initView();
        initFabIcon();
        callItemDetailapi();

        ico_back.setOnClickListener(view -> finish());
        if (getIntent().getExtras().containsKey("openReviews")) {
            btnReview.callOnClick();
        }
    }

    @Override
    public void onEditItem() {
        if (itemDetailResult != null) {
            Intent intent = new Intent(this, com.screamxo.Activity.SellItemActivity.class);
            intent.putExtra("isedit", true);
            intent.putExtra("beanObject", itemDetailResult);
            startActivityForResult(intent, REQ_CODE_EDIT_SELL_ITEM_ACTIVITY_RESULTS);
        }
    }

    @Override
    public void onClickPurchaseHistory() {
        rl_Reviews.setVisibility(View.GONE);
        ll_BottomSheet.setVisibility(View.GONE);
        rl_purchase_data.setVisibility(View.VISIBLE);
    }

    @Override
    public void shareItem() {
        String shareContent = itemDetailResult.getItemName() +
                "\n" +
                itemDetailResult.getItemDescription() +
                itemDetailResult.getMedia().get(0).getMediaUrl();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void sendMessage() {
        ll_message_container.setVisibility(View.VISIBLE);
        txtDescription.setVisibility(View.GONE);
    }

    @Override
    public void reportPost() {
        vreportPost();
    }

    private void vreportPost() {
        try {
            Map<String, Integer> map = new HashMap<>();
            map.put("uid", Integer.parseInt(preferences.getUserId()));
            map.put("item_id", itemDetailResult.getItemId());

            if (Utils.isInternetOn(context)) {
                reportPostCall = mservice.getFetcherService(context).ReportItem(map);
                reportPostCall.enqueue(new retrofit2.Callback<CommentBean>() {
                    @Override
                    public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {
                        if (response == null) {
                            return;
                        }
                        if (response.code() == StaticConstant.RESULT_OK) {
                            int type;
                            if (response.body().getStatus().equalsIgnoreCase(StaticConstant.STATUS_1)) {
                                type = DialogBox.DIALOG_SUCESS;
                            } else {
                                type = DialogBox.DIALOG_FAILURE;
                            }
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), type, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentBean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                Utils.showToast(context, context.getString(R.string.toast_no_internet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                public void onMenuOpened(FloatingMenuButton floatingMenuButton) {
                    Utils.hideKeyboard(ItemDetailsAcitvity.this);
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
                    Intent gotoNext = new Intent(ItemDetailsAcitvity.this, UploadDataActivity.class);
                    startActivityForResult(gotoNext, REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS);
                }
            });

            sbWorld = findViewById(R.id.sbWorld);
            sbWorld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    startActivityForResult(new Intent(ItemDetailsAcitvity.this, WalletSendReceiveActivity.class), REQ_CODE_WALLET_ACTIVITY_RESULTS);
                }
            });

            sbSearch = findViewById(R.id.sbSearch);
            sbSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_options));
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    if (itemDetailResult != null) {
                        if (itemDetailResult.getMyitem() == 1) {
                            DialogBox.showMyItemOptions(ItemDetailsAcitvity.this, itemDetailResult.hasPurchasedData());
                        } else {
                            DialogBox.showOtherItemOptions(ItemDetailsAcitvity.this);
                        }
                    }

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

        }
    }

    private void initView() {
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
        mservice1 = new FetchrServiceBase();
        mservice = new FetchrServiceBase();
        context = ItemDetailsAcitvity.this;
        preferences = new Preferences(context);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("itemid")) {
            itemid = getIntent().getExtras().getString("itemid");
        }
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("ADD_TRACKING")) {
            addTrackingDetails = getIntent().getExtras().getBoolean("ADD_TRACKING");
        }
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("ORDER_ID")) {
            orderId = getIntent().getExtras().getInt("ORDER_ID");
        }

        //    itemid = getArguments().getString("itemid");
        String uId = preferences.getUserId();
        mediaArrayList = new ArrayList<>();
        reviewlist = new ArrayList<>();
        purchaseDatas = new ArrayList<>();
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
                            if (isFinishing()) {
                                return;
                            }
                            mediaArrayList.clear();
                            mediaArrayList.addAll(response.body().getResult().getMedia());
                            itemDetailResult = response.body().getResult();
                            setValue();
                            setImagePager();
                            if (response.body().getResult().getReviewdata().size() > 0) {
                                reviewlist.addAll(response.body().getResult().getReviewdata());
                                txtNoData.setVisibility(View.GONE);
                            }
                            if (response.body().getResult().getPurchasedata() != null && response.body().getResult().getPurchasedata().size() > 0) {
                                purchaseDatas.addAll(response.body().getResult().getPurchasedata());
                            }
                            setAdapter();

                            try {
                                if (mediaArrayList != null) {
                                    Picasso.with(context)
                                            .load(mediaArrayList.get(1).getMediaThumb())
                                            .into(image1);

                                    Picasso.with(context)
                                            .load(mediaArrayList.get(2).getMediaThumb())
                                            .into(image2);

                                    Picasso.with(context)
                                            .load(mediaArrayList.get(3).getMediaThumb())
                                            .into(image3);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (addTrackingDetails) {
                                courierTrackingDetailsDialog(0, orderId);
                            }
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemDetailBean> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                    Utils.showToast(context, t.toString());
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
            progreessbar.setVisibility(View.GONE);
        }
    }

    public void courierTrackingDetailsDialog(int position, int orderId) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_courier_tracking_details);
        EditText courier_company_name_et = dialog.findViewById(R.id.courier_company_name_et);
        EditText tracking_no_et = dialog.findViewById(R.id.tracking_no_et);
        TextView cancel_tv = dialog.findViewById(R.id.cancel_tv);
        TextView done_tv = dialog.findViewById(R.id.done_tv);

        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        done_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callUpdateTrackingDetails(position, orderId, courier_company_name_et.getText().toString(), tracking_no_et.getText().toString());
            }
        });
        dialog.show();
    }

    private void callUpdateTrackingDetails(int position, int orderId, String companyName, String trackingId) {
        Map<String, String> map = new HashMap<>();
        map.put("orderid", String.valueOf(orderId));
        map.put("itemid", itemid);
        map.put("companyname", companyName);
        map.put("trackingid", trackingId);
        if (Utils.isInternetOn(this)) {
            progreessbar.setVisibility(View.VISIBLE);
            updatetrackingdetailCall = mservice.getFetcherService(context).updatetrackingdetail(map);
            updatetrackingdetailCall.enqueue(new Callback<UpdateTrackingDetailReposne>() {
                @Override
                public void onResponse(Call<UpdateTrackingDetailReposne> call, Response<UpdateTrackingDetailReposne> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_SUCESS, new DialogInterfaceAction() {
                                @Override
                                public void dialogAction() {
                                    isUpdate = true;
                                    ((ItemDetailsAcitvity) context).updateTrackingDetails(position, companyName, trackingId);
                                }
                            });
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateTrackingDetailReposne> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                    if (updatetrackingdetailCall != null && !updatetrackingdetailCall.isCanceled()) {
                        progreessbar.setVisibility(View.GONE);
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
            progreessbar.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setValue() {
        txtLocationData.setText(itemDetailResult.getUsercity());

        Picasso.with(context)
                .load(itemDetailResult.getMedia().size() == 0 ? null : itemDetailResult.getMedia().get(0).getMediaThumb())
                .placeholder(R.mipmap.user)
                .error(R.mipmap.user)
                .transform(new CircleTransform())
                .into(user_iv);

        imgBookmark.setVisibility(itemDetailResult.getMyitem() == 1 ? View.INVISIBLE : View.VISIBLE);

        txtBuy.setText(itemDetailResult.getMyitem() == 1 ? getString(R.string.txt_boost) : "BUY");

        if (itemDetailResult.getIspurchased().equalsIgnoreCase("1")) {
            imgBookmark.setVisibility(View.INVISIBLE);
            txtBuy.setVisibility(View.INVISIBLE);
        }

        if (itemDetailResult.getIswatched() == 1) {
            Picasso.with(context)
                    .load(R.mipmap.bookmark_active)
                    .placeholder(R.mipmap.bookmark_active)
                    .error(R.mipmap.bookmark_active)
                    .into(imgBookmark);
            isBookmarkCheck = true;
        } else {
            Picasso.with(context)
                    .load(R.mipmap.bookmark)
                    .placeholder(R.mipmap.bookmark)
                    .error(R.mipmap.bookmark)
                    .into(imgBookmark);
            isBookmarkCheck = false;
        }
        txtProductName.setText(itemDetailResult.getItemName());

        if (itemDetailResult.getItemPrice() == null || Utils.getFormattedPrice(itemDetailResult.getItemPrice()) == null) {
            txtPrice.setVisibility(View.GONE);
        } else {
            txtPrice.setText(Utils.getFormattedPrice(itemDetailResult.getItemPrice()));
        }

        txtDescription.setText(itemDetailResult.getItemDescription());

        Picasso.with(context)
                .load(itemDetailResult.getUserphoto())
                .placeholder(R.mipmap.profile_pic_placeholder)
                .error(R.mipmap.profile_pic_placeholder)
                .fit()
                .transform(new CircleTransform())
                .centerCrop()
                .into(img_profile);

        Validations validations;
        String date = null;
        if (itemDetailResult.getCreatedDate() != null) {
            validations = new Validations();
            date = validations.dateFormationUTCtoLocal("yyyy-MM-dd'T'HH:mm:ss", itemDetailResult.getCreatedDate());
            txtDate.setText(date);
        }

        txtName.setText(itemDetailResult.getUsername());
        txtDesc.setText(itemDetailResult.getUsercity());
        txtShippingData.setText(itemDetailResult.getItemShippingCost());
        txtConditionData.setText(itemDetailResult.getItem_condition());

        rcView.setLayoutManager(new LinearLayoutManager(context));
        rv_purchase_data.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setAdapter() {
        Log.d(TAG, "setAdapter: ");

        if (itemDetailAdapter == null) {
            itemDetailAdapter = new ItemDetailAdapter(context, reviewlist);
            rcView.setAdapter(itemDetailAdapter);
        } else {
            itemDetailAdapter.notifyDataSetChanged();
        }

        if (itemPurchaseDataAdapter == null) {
            itemPurchaseDataAdapter = new ItemPurchaseDataAdapter(context, purchaseDatas);
            rv_purchase_data.setAdapter(itemPurchaseDataAdapter);
        } else {
            itemPurchaseDataAdapter.notifyDataSetChanged();
        }
    }

    public void updateTrackingDetails(int position, String companyName, String trackingId) {
        if (purchaseDatas != null) {
            try {
                if (addTrackingDetails) {
                    position = getPosition(orderId);
                }
                purchaseDatas.get(position).getTrackingdetail().setComapnyname(companyName);
                purchaseDatas.get(position).getTrackingdetail().setTrackingid(trackingId);
                purchaseDatas.get(position).setHastrackingdetail(true);
            } catch (IndexOutOfBoundsException ignored) {
                Log.e(TAG, "updateTrackingDetails: ", ignored);
            }
        }

        if (itemPurchaseDataAdapter != null) {
            itemPurchaseDataAdapter.notifyDataSetChanged();
        }
    }

    private int getPosition(int orderId) {
        if (purchaseDatas != null) {
            for (int position = 0; position < purchaseDatas.size(); position++) {
                ItemPurchasedata purchaseData = purchaseDatas.get(position);
                if (purchaseData.getOrderId() == orderId) {
                    return position;
                }
            }
        }
        throw new IndexOutOfBoundsException("getPosition -> IGNORE");
    }

    private void setImagePager() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        view_pager.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewPager.LayoutParams.MATCH_PARENT,
                (int) (metrics.heightPixels * .47)));
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(((ItemDetailsAcitvity) context).getSupportFragmentManager());
        ImagePagerFragment fragment1;
        if (mediaArrayList.size() == 0) {
            fragment1 = new ImagePagerFragment(0, mediaArrayList, isSoldVisible());
            adapter.addFragment(fragment1, "" + 0);
        } else {
            for (int i = 0; i < mediaArrayList.size(); i++) {
                ImagePagerFragment fragment = new ImagePagerFragment(i, mediaArrayList, isSoldVisible());
                adapter.addFragment(fragment, "" + i);
            }
        }
        view_pager.setAdapter(adapter);

        image1.setOnClickListener(view -> view_pager.setCurrentItem(1));
        image2.setOnClickListener(view1 -> view_pager.setCurrentItem(2));
        image3.setOnClickListener(view2 -> view_pager.setCurrentItem(3));
    }

    Boolean isSoldVisible() {
        return itemDetailResult.getItemQtyRemained() <= 0;
    }

    @OnClick({R.id.rl_BottomSheet, R.id.rl_Close, R.id.rl_purchase_data_close, R.id.btnReview, R.id.imgBookmark, R.id.txtBuy
            , R.id.tv_topics, R.id.tv_questions
            , R.id.tv_topics_condition, R.id.tv_topics_payment, R.id.tv_topics_policy, R.id.tv_topics_shipping, R.id.tv_topics_others
            , R.id.tv_questions_original_manufacturer, R.id.tv_questions_negotiable, R.id.tv_questions_ship_internationally,
            R.id.tv_questions_return_policy, R.id.tv_questions_accepted_payment_form, R.id.tv_questions_need_asap, R.id.tv_questions_other})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_BottomSheet:
                if (ll_BottomSheet.getVisibility() == View.VISIBLE) {
                    ll_BottomSheet.setVisibility(View.GONE);
                } else {
                    ll_BottomSheet.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.rl_Close:
                rl_Reviews.setVisibility(View.GONE);
                txtBuy.setVisibility(View.VISIBLE);
                break;

            case R.id.ico_back:
                finish();
                break;

            case R.id.rl_purchase_data_close:
                rl_purchase_data.setVisibility(View.GONE);
                break;

            case R.id.btnReview:
                setAdapter();
                rl_Reviews.setVisibility(View.VISIBLE);
                if (txtBuy.getVisibility() == View.VISIBLE) {
                    txtBuy.setVisibility(View.GONE);
                } else {
                    txtBuy.setVisibility(View.VISIBLE);
                }

                if (ll_BottomSheet.getVisibility() == View.VISIBLE) {
                    ll_BottomSheet.setVisibility(View.GONE);
                } else {
                    ll_BottomSheet.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.imgBookmark:
                if (!isBookmarkCheck) {
                    setBookmark(itemDetailResult.getItemId(), StaticConstant.UNLIKE);
                } else {
                    setBookmark(itemDetailResult.getItemId(), StaticConstant.LIKE);
                }
                break;
            case R.id.txtBuy:
                if (itemDetailResult == null) {
                    return;
                }

                if (itemDetailResult.getMyitem() == 0) {
                    Intent i = new Intent(context, NewConfigurePaymentActivity.class);
                    i.putExtra("screen", ItemDetailsAcitvity.class.getSimpleName());
                    i.putExtra("item_shipping_cost", itemDetailResult.getItemShippingCost());
                    i.putExtra("total_quantity", String.valueOf(itemDetailResult.getItemQtyRemained()));
                    i.putExtra("item_cost", itemDetailResult.getItemPrice());
                    i.putExtra("itemid", String.valueOf(itemDetailResult.getItemId()));
                    i.putExtra("item_name", String.valueOf(itemDetailResult.getItemName()));
                    i.putExtra("item_media_thumb", String.valueOf(mediaArrayList.get(0).getMediaUrl()));
                    startActivityForResult(i, REQ_CODE_CONFIGURE_PAYEMENT_RESULTS);
                } else {
                    Intent gotoActivity = new Intent(context, BoostActivity.class);
//                    gotoActivity.putExtra("boost_url", String.valueOf(itemDetailResult.getBoost_url()));
                    gotoActivity.putExtra("itemid", String.valueOf(itemDetailResult.getItemId()));
                    if (itemDetailResult.getMedia().size() > 0) {
                        gotoActivity.putExtra("image", (itemDetailResult.getMedia().get(0).getMediaUrl()));
                    } else {
                        gotoActivity.putExtra("image", (itemDetailResult.getUserphoto()));
                    }
                    gotoActivity.putExtra("boost_url", ("http://apis.2kxo.com/item/itemdetail/" + itemDetailResult.getItemId()));

                    gotoActivity.putExtra("boost_type", String.valueOf(StaticConstant.BOOST_TYPE_SHOP_ITEM));
                    gotoActivity.putExtra("item_name", itemDetailResult.getItemName());
                    ((Activity) context).startActivityForResult(gotoActivity, REQ_CODE_BOOST_ACTIVITY_RESULTS);
                }
                break;

            case R.id.tv_topics:
                if (ll_topics_container.getVisibility() == View.VISIBLE) {
                    ll_topics_container.setVisibility(View.GONE);
                } else {
                    ll_topics_container.setVisibility(View.VISIBLE);
                    ll_questions_container.setVisibility(View.GONE);
                }
                break;

            case R.id.tv_questions:
                if (ll_questions_container.getVisibility() == View.VISIBLE) {
                    ll_questions_container.setVisibility(View.GONE);
                } else {
                    ll_questions_container.setVisibility(View.VISIBLE);
                    ll_topics_container.setVisibility(View.GONE);
                }
                break;

            case R.id.tv_topics_condition:
                askTheSeller(getString(R.string.txt_condition), true);
                break;
            case R.id.tv_topics_payment:
                askTheSeller(getString(R.string.txt_payment), true);
                break;
            case R.id.tv_topics_policy:
                askTheSeller(getString(R.string.txt_policy), true);
                break;
            case R.id.tv_topics_shipping:
                askTheSeller(getString(R.string.txt_shipping), true);
                break;
            case R.id.tv_topics_others:
                askTheSeller(getString(R.string.txt_other), true);
                break;


            case R.id.tv_questions_original_manufacturer:
                askTheSeller(getString(R.string.txt_original_manufacture), false);
                break;
            case R.id.tv_questions_negotiable:
                askTheSeller(getString(R.string.txt_negotiable), false);
                break;
            case R.id.tv_questions_ship_internationally:
                askTheSeller(getString(R.string.msg_ship_internally), false);
                break;
            case R.id.tv_questions_return_policy:
                askTheSeller(getString(R.string.txt_whats_return_policy), false);
                break;
            case R.id.tv_questions_accepted_payment_form:
                askTheSeller(getString(R.string.txt_accepted_forms_payment), false);
                break;
            case R.id.tv_questions_need_asap:
                askTheSeller(getString(R.string.msg_need_asap), false);
                break;
            case R.id.tv_questions_other:
                askTheSeller(getString(R.string.txt_other), false);
                break;
        }
    }

    private void askTheSeller(String message, boolean isTopic) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("otherUid", String.valueOf(itemDetailResult.getUserid()));
        intent.putExtra("username", itemDetailResult.getUsername());
        intent.putExtra("userProfile", itemDetailResult.getUserphoto());
        intent.putExtra("fullname", itemDetailResult.getFname() + " " + itemDetailResult.getLname());
        intent.putExtra("itemId", String.valueOf(itemDetailResult.getItemId()));
        intent.putExtra("message", message);
        intent.putExtra("isTopic", isTopic);
        ((Activity) context).startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
    }

    private void setBookmark(int id, int action) {
        progreessbar.setVisibility(View.VISIBLE);
        Map<String, Integer> map = new HashMap<>();
        map.put("itemid", id);
        map.put("uid", Integer.parseInt(preferences.getUserId()));
        map.put("action", action);
        mservice1.getFetcherService(context).Bookmark(map).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                progreessbar.setVisibility(View.GONE);
                int type = 0;

                try {
                    if (response.body().getStatus().equalsIgnoreCase(StaticConstant.STATUS_1)) {
                        type = DialogBox.DIALOG_SUCESS;
                    } else {
                        type = DialogBox.DIALOG_FAILURE;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), type, null);
                if (!isBookmarkCheck) {
                    //   itemDetailResult.setIswatched(0);
                    Picasso.with(context)
                            .load(R.mipmap.bookmark_active)
                            .placeholder(R.mipmap.bookmark_active)
                            .error(R.mipmap.bookmark_active)
                            .into(imgBookmark);
                    isBookmarkCheck = true;
                } else {
                    Picasso.with(context)
                            .load(R.mipmap.bookmark)
                            .placeholder(R.mipmap.bookmark)
                            .error(R.mipmap.bookmark)
                            .into(imgBookmark);
                    //itemDetailResult.setIswatched(1);
                    isBookmarkCheck = false;
                }
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                progreessbar.setVisibility(View.GONE
                );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.printIntentData(data);

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
            case REQ_CODE_EDIT_SELL_ITEM_ACTIVITY_RESULTS:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("IS_ITEM_DELETED") && data.getExtras().getBoolean("IS_ITEM_DELETED")) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("MESSAGE", data.getExtras().getString("MESSAGE"));
                        setResult(RESULT_OK, returnIntent);
                        finish();
                        return;
                    }
                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("IS_ITEM_UPDATED")) {
                        callItemDetailapi();
                        return;
                    }
                }
                break;

            case REQ_CODE_CONFIGURE_PAYEMENT_RESULTS:
                if (data != null && data.getExtras() != null && data.getExtras().containsKey("itemid")) {
                    Intent intent = new Intent(this, ItemBuyFinal.class);

                    intent.putExtra("otherUid", String.valueOf(itemDetailResult.getUserid()));
                    intent.putExtra("username", itemDetailResult.getUsername());
                    intent.putExtra("userProfile", itemDetailResult.getUserphotothumb());
                    intent.putExtra("fullname", itemDetailResult.getFname() + " " + itemDetailResult.getLname());

                    intent.putExtra("item_image", data.getStringExtra("item_image"));
                    intent.putExtra("item_name", data.getStringExtra("item_name"));
                    intent.putExtra("itemid", data.getStringExtra("itemid"));

                   /*NewConfigurePaymentActivity
                    Intent intent = new Intent(this, PostCongActivity.class);
                    if (data.getExtras().containsKey("itemid")) {
                        intent.putExtra("itemid", data.getExtras().getString("itemid"));
                    }

                    if (data.getExtras().containsKey("item_name")) {
                        intent.putExtra("item_name", data.getExtras().getString("item_name"));
                    }

                    if (data.getExtras().containsKey("item_image")) {
                        intent.putExtra("item_image", data.getExtras().getString("item_image"));
                    }*/
                    startActivityForResult(intent, REQ_CODE_BUY_CONG_ACTIVITY_RESULTS);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updatetrackingdetailCall != null) {
            updatetrackingdetailCall.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        if (BundleUtils.getIntentExtra(getIntent(), "from_notification", false)) {
            Intent gotoHome = new Intent(context, DrawerMainActivity.class);
            startActivity(gotoHome);
            return;
        }
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
