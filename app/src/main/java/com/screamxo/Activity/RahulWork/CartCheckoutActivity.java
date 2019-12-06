package com.screamxo.Activity.RahulWork;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.AddressBean;
import com.example.apimodule.ApiBase.ApiBean.Boost.StripeProcessBoostResponse;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailResult;
import com.example.apimodule.ApiBase.ApiBean.Payment.StripeToken;
import com.example.apimodule.ApiBase.ApiBean.SellItemBean;
import com.example.apimodule.ApiBase.ApiBean.ShippingAddress;
import com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean;
import com.example.apimodule.ApiBase.ApiBean.cart.model.Item;
import com.example.apimodule.ApiBase.ApiBean.cart.model.Userdetail;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.example.apimodule.ApiBase.StripeAlipay.ProcessStripePaymentResponse;
import com.example.apimodule.ApiBase.WalletOrderPayment.FinalProcessResponse;
import com.google.gson.JsonObject;
import com.screamxo.Activity.CommonLoginSignUpActivity;
import com.screamxo.Activity.PayPalVerificationActivity;
import com.screamxo.Activity.PaymentDetailActivity;
import com.screamxo.Activity.wallet.WalletSendReceiveActivity;
import com.screamxo.Adapter.CartCheckoutAdapter;
import com.screamxo.Adapter.CheckoutRecyclerAdapter;
import com.screamxo.Adapter.DashboardAdapter;
import com.screamxo.Fragment.NewCartFragment;
import com.screamxo.Fragment.SelectPaymentFragment;
import com.screamxo.Fragment.SelectProcessorFragment;
import com.screamxo.Fragment.ShopFragmentView;
import com.screamxo.Interface.CartItemImage;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.DialogFragmentForItemQuantity;
import com.screamxo.Utils.ImageUtils;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.RequestBodyConveter;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.widget.Toast.LENGTH_LONG;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_PAYMENT_DETAIL_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_TOP_UP_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_WALLET_ACTIVITY_RESULTS;

public class CartCheckoutActivity extends AppCompatActivity implements CommonMethod, FragmentManager.OnBackStackChangedListener, SelectPaymentFragment.OnPaymentSelectedListener {
    public static final int REQ_CODE_FRIENDS_ACTIVITY_RESULTS = 101;
    private static final String TAG = "CartCheckoutActivity";
    public TextView txtProduct, txtProductChildName, txtSaveProductDetail, txtItemQuntity, txtPay;
    public boolean isSaved = true;
    ArrayList<String> arrayListCheckout;
    ArrayList<ItemDetailResult> arrayListCheckoutSave;
    RecyclerView rvProduct;
    ImageView imgCrossProductDetail, imgDetailProduct;
    RelativeLayout rlProductDetail;
    CheckoutRecyclerAdapter adapter;
    boolean isSelecte = true;
    int curruntPosition = 0;
    int itemQunt = 0, item_id;
    Context context;
    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.ll_wallet_container)
    LinearLayout ll_wallet_container;
    @BindView(R.id.ll_ship_to_container)
    LinearLayout ll_ship_to_container;
    @BindView(R.id.ll_card_details_container)
    LinearLayout ll_card_details_container;
    @BindView(R.id.ll_card_detail_container)
    LinearLayout llCardDetailContainer;
    @BindView(R.id.ll_payment_gateway_details_container)
    LinearLayout ll_payment_gateway_details_container;
    @BindView(R.id.rlTotalItem)
    RelativeLayout rlTotalItem;
    @BindView(R.id.rlTotalOrder)
    RelativeLayout rlTotalOrder;
    @BindView(R.id.rlShipping)
    RelativeLayout rlShipping;
    @BindView(R.id.txttTotalOrderPrise)
    TextView txttTotalOrderPrise;
    @BindView(R.id.txtTotalItemCheckout)
    TextView txtTotalItemCheckout;
    @BindView(R.id.txtShippingPriseCheckout)
    TextView txtShippingPriseCheckout;
    @BindView(R.id.txtTotalItemPriseCheckout)
    TextView txtTotalItemPriseCheckout;
    @BindView(R.id.tv_first_name)
    TextView tv_first_name;
    @BindView(R.id.tv_card_brand)
    TextView tv_card_brand;
    @BindView(R.id.tv_card_details)
    TextView tv_card_details;
    @BindView(R.id.tv_payement_email)
    TextView tv_payement_email;
    @BindView(R.id.tv_payement_name)
    TextView tv_payement_name;
    @BindView(R.id.tv_selected_payment)
    TextView tv_selected_payment;
    @BindView(R.id.txt_balance)
    TextView txt_balance;
    @BindView(R.id.recyerCheckout)
    RecyclerView recyclerCheckout;
    @BindView(R.id.item_image_preview)
    ImageView itemImagepreview;


    @BindView(R.id.txt_quntity)
    TextView txt_quntity;
    @BindView(R.id.txt_item_name)
    TextView txt_item_name;
    @BindView(R.id.txt_save_item)
    TextView txt_save_item;

    @BindView(R.id.relchangecart)
    RelativeLayout relchangecart;


    double tCost;
    double sum, shippingSum = 0.0;
    String tSum;
    @BindView(R.id.tv_shipping_address)
    TextView tv_shipping_address;
    NumberFormat numFormat = NumberFormat.getCurrencyInstance(Locale.US);
    int pos = 1;
    int itemPos = 0;
    private boolean isUpdate = false;
    private Preferences preferences;
    private FetchrServiceBase mservice;
    private String cardId;
    private Call<SellItemBean> createitembeanCall;
    private Call<AddressBean> getAddressCall;
    private Call<StripeToken> topUsingCard;
    private Call<StripeToken> finalizeTopUpUsingCardCall;
    /*BUY*/
    private Call<ProcessStripePaymentResponse> processBitcoinCall;
    private Call<FinalProcessResponse> finalprocessBitcoinCall;
    private Call<FinalProcessResponse> payUsingWalletCall;
    private Call<ProcessStripePaymentResponse> processAlipayCall;
    private Call<FinalProcessResponse> finalProcessAlipayCall;
    private Call<StripeToken> buyUsingCardCall;
    private Call<FinalProcessResponse> finalizeBuyUsingCardCall;
    //cart items
    private CartCheckoutAdapter cartAdapter;
    private FetchrServiceBase mService;
    private Call<CartBean> cartBeanCall;
    private ArrayList<Userdetail> userdetailsArray;
    /*BOOST*/
    private Call<StripeToken> boostUsingWalletCall;
    private Call<StripeProcessBoostResponse> boostUsingCardcall;
    private Call<ProcessStripePaymentResponse> processBitcoinBoostCall;
    private Call<ProcessStripePaymentResponse> finalProcessBitcoinBoostCall;
    private Call<ProcessStripePaymentResponse> processAlipayBoostCall;
    private Call<ProcessStripePaymentResponse> finalProcessAlipayBoostCall;
    private String name = "";
    private double totalCost = 0;
    private ArrayList<String> arrayListItems = new ArrayList<>();
    private ArrayList<String> arrayListItemsName = new ArrayList<>();
    private ArrayList<Integer> arrayListItemsQuentity = new ArrayList<>();
    private ArrayList<Integer> arrayListItemsTotalQuentity = new ArrayList<>();
    private ArrayList<Integer> arrayListItemsId = new ArrayList<>();
    private ArrayList<String> actualCartItem = new ArrayList<>();
    private ArrayList<String> arrayListPrices = new ArrayList<>();
    private ArrayList<Object> multipleItemarray = new ArrayList<>();
    //    String qty;
    private String itemImagePreviewUrl = "", itemImagePreviewtemp = "";
    private String type[] = {StaticConstant.POST_TYPE_IMAGE, StaticConstant.POST_TYPE_IMAGE, StaticConstant.POST_TYPE_IMAGE, StaticConstant.POST_TYPE_IMAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_checkout);
        inItIds();
        ButterKnife.bind(CartCheckoutActivity.this);
        init();
        initControlValue();
        initFabIcon();
        /*  ToDo 24/4/2019  */
        context = getApplicationContext();
        callGetCartItemApi();
        arrayListCheckout = new ArrayList<>();
//        if(getIntent() != null){
//            qty= getIntent().getStringExtra("qunty");
//        }


        txt_quntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setQuantityList();
            }
        });

        tv_selected_payment.setVisibility(View.GONE);
        ll_payment_gateway_details_container.setVisibility(View.VISIBLE);
        ll_card_details_container.setVisibility(View.GONE);

        tv_payement_name.setText("Connect");
        tv_payement_email.setText("");
        this.cardId = null;

    }

    private void setUpToolbar() {
        setTitle();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (fragment != null && (fragment instanceof SelectPaymentFragment || fragment instanceof SelectProcessorFragment)) {
            updatePinkToolbar();
            return;
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(NewCartFragment.class.getSimpleName())) {
            updateWhiteToolbar();
        } else {
            updatePinkToolbar();
        }
    }

    private void updateWhiteToolbar() {
        imgToolbarLeftIcon.setImageResource(R.drawable.ico_back);
        toolbar.setBackgroundColor(Color.WHITE);
        txtToolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }

    void updatePinkToolbar() {
        imgToolbarLeftIcon.setImageResource(R.drawable.ico_back);
        toolbar.setBackgroundColor(Color.WHITE);
        txtToolbarTitle.setTextColor(Color.BLACK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }

    @OnClick(R.id.txt_save_item)
    void saveItem() {
        callMoveToSaveApi();
    }

    private void callUpdateSaveQty(String id, String qty) {
        Map<String, String> map = new HashMap<>();
        Preferences preference = new Preferences(context);
        map.put("user_id", preference.getUserId());
        map.put("item_id", id);
        map.put("cart_qty", qty);
//        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        cartBeanCall = mService.getFetcherService(context).updateSaveQty(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {
                    callGetCartItemApi();

                }

                @Override
                public void onFailure(Call<CartBean> call, Throwable t) {
                    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    int items_pos = 0;

    private void callMoveToSaveApi() {
        Map<String, String> map = new HashMap<>();
        Preferences preference = new Preferences(context);
        map.put("user_id", preference.getUserId());
        map.put("item_id", String.valueOf(item_id));
        map.put("cart_qty", String.valueOf(itemQunt));
//        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        cartBeanCall = mService.getFetcherService(context).moveToSave(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {
//                    clickEvent.onClick("", 0);
                    Log.e("saveItem", response.body().toString());
                    callUpdateSaveQty(String.valueOf(item_id), String.valueOf(itemQunt));
                    callUpdateCartQty(String.valueOf(item_id), String.valueOf(itemQunt));

//                    TODO debug
                    userdetailsArray.get(item_Pos).getItems().remove(item_Pos);


                    if (userdetailsArray.get(item_Pos).getItems().size() == 0) {

                        userdetailsArray.remove(item_Pos);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("fire", true);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                    itemImagepreview.setVisibility(View.GONE);
                    relchangecart.setVisibility(View.GONE);


                }

                @Override
                public void onFailure(Call<CartBean> call, Throwable t) {
                    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }

    }

    public void initControlValue() {

        ll_wallet_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CartCheckoutActivity.this, WalletSendReceiveActivity.class), REQ_CODE_WALLET_ACTIVITY_RESULTS);
            }
        });

        llCardDetailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_container, SelectPaymentFragment.newInstance());
                fragmentTransaction.addToBackStack(SelectPaymentFragment.class.getSimpleName());
                fragmentTransaction.commit();
            }
        });

        ll_ship_to_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartCheckoutActivity.this, PaymentDetailActivity.class);
                startActivityForResult(i, REQ_CODE_PAYMENT_DETAIL_ACTIVITY_RESULTS);
            }
        });
    }

    public void inItIds() {
        rvProduct = findViewById(R.id.recyerCheckout);
        rlProductDetail = findViewById(R.id.rlProductDetail);
        txtProductChildName = findViewById(R.id.txtProductChildName);
        txtSaveProductDetail = findViewById(R.id.txtSaveProductDetail);
        txtItemQuntity = findViewById(R.id.txtItemQuntity);
        imgCrossProductDetail = findViewById(R.id.imgCrossProductDetail);
        imgDetailProduct = findViewById(R.id.imgDetailProduct);
        txtPay = findViewById(R.id.txtPay);

    }

    private void setTitle() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (fragment != null && fragment instanceof SelectPaymentFragment) {
            txtToolbarTitle.setText(getString(R.string.txt_select_payment));
            return;
        }


        if (fragment != null && fragment instanceof SelectProcessorFragment) {
            txtToolbarTitle.setText(getString(R.string.txt_select_processor));
            return;
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(NewCartFragment.class.getSimpleName())
                /*|| BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(DashboardAdapter.class.getSimpleName())
                || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ShopFragmentView.class.getSimpleName())*/) {
            txtToolbarTitle.setText(getString(R.string.txt_checkout));
            return;
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase("BoostActivity")) {
            txtToolbarTitle.setText(getString(R.string.txt_checkout));
            return;
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(SellItemActivity.class.getSimpleName())) {
            txtToolbarTitle.setText(getString(R.string.txt_checkout));
            return;
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(TopUpActivity.class.getSimpleName())) {
            txtToolbarTitle.setText(getString(R.string.title_topup));
            return;
        }

        txtToolbarTitle.setText(getString(R.string.txt_checkout));
    }

    private void initFabIcon() {
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
                Utils.hideKeyboard(CartCheckoutActivity.this);
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
                floatingButton.closeMenu();
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 6);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    gotoLogin();
                }
            }
        });

        sbSocial = findViewById(R.id.sbSocial);
        sbSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingButton.closeMenu();
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 3);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    gotoLogin();
                }
            }
        });

        sbChat = findViewById(R.id.sbChat);
        sbChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingButton.closeMenu();
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    Intent gotoNext = new Intent(CartCheckoutActivity.this, UploadDataActivity.class);
                    startActivityForResult(gotoNext, REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS);
                } else {
                    gotoLogin();
                }

            }
        });

        sbWorld = findViewById(R.id.sbWorld);
        sbWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingButton.closeMenu();
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    startActivityForResult(new Intent(CartCheckoutActivity.this, WalletSendReceiveActivity.class), REQ_CODE_WALLET_ACTIVITY_RESULTS);
                    finish();
                } else {
                    gotoLogin();
                }
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
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 7);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    gotoLogin();
                }

            }
        });

        subFriend = findViewById(R.id.subFriend);
        subFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingButton.closeMenu();
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    Intent gotoNext = new Intent(CartCheckoutActivity.this, FriendsActivity.class);
                    startActivityForResult(gotoNext, REQ_CODE_FRIENDS_ACTIVITY_RESULTS);
                    finish();
                } else {
                    gotoLogin();
                }
            }
        });

        sbflHome = findViewById(R.id.sbflHome);
        sbflHome.setOnClickListener(view -> {
            floatingButton.closeMenu();
            if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", 1);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                gotoLogin();
            }
        });
    }

    public void gotoLogin() {
        Intent gotoLogin = new Intent(this, CommonLoginSignUpActivity.class);
        gotoLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(gotoLogin);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.getFormattedPrice(preferences.getAmount()) == null) {
            txt_balance.setVisibility(View.GONE);
        } else {
            txt_balance.setText(Utils.getFormattedPrice(preferences.getAmount()));
        }
        callGetAddressApi();
    }

    public void init() {
        context = this;
        mService = new FetchrServiceBase();
        mservice = new FetchrServiceBase();
        preferences = new Preferences(context);
        userdetailsArray = new ArrayList<>();
        getSupportFragmentManager().addOnBackStackChangedListener(CartCheckoutActivity.this);

        tv_first_name.setText(preferences.getUserFullName());
        Utils.printIntentData(getIntent());
        setUpToolbar();
        name = BundleUtils.getIntentExtra(getIntent(), "item_name", null);

        itemImagepreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemImagePreviewUrl != null) {
                    ImageUtils.customChoosePhoto(CartCheckoutActivity.this, itemImagePreviewUrl);
                }
            }
        });
    }

    private void callGetCartItemApi() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", preferences.getUserId());

        cartBeanCall = new FetchrServiceBase().getFetcherService(context).getCartItem(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {

                    if (response.code() == StaticConstant.RESULT_OK) {
                        userdetailsArray.clear();
                        arrayListItems.clear();
                        actualCartItem.clear();
                        multipleItemarray.clear();
                        arrayListPrices.clear();

                        double itemPrice = 0, shippingCost = 0;
                        int itemCount = 0;
                        if (response.body().getResult().getUserdetails() != null) {
                            userdetailsArray.addAll(response.body().getResult().getUserdetails());
                            for (int i = 0; i < userdetailsArray.size(); i++) {
                                ArrayList<Item> arrayListitem = userdetailsArray.get(i).getItems();
                                for (int image = 0; image < arrayListitem.size(); image++) {
                                    arrayListItems.add(arrayListitem.get(image).getIimage().get(0).getMediaThumb());
                                    arrayListItemsName.add(arrayListitem.get(image).getItemName());
                                    arrayListItemsQuentity.add(arrayListitem.get(image).getCartQty());
                                    arrayListItemsTotalQuentity.add(arrayListitem.get(image).getItemQtyRemained());
                                    arrayListItemsId.add(arrayListitem.get(image).getId());
                                    actualCartItem.add(arrayListitem.get(image).getIimage().get(0).getMediaUrl());
                                    arrayListPrices.add("$" + arrayListitem.get(image).getItemPrice());
                                    itemPrice = itemPrice + ((arrayListitem.get(image).getItemPrice())
                                            * (arrayListitem.get(image).getCartQty()));
                                    itemCount = itemCount + arrayListitem.get(image).getCartQty();
                                    shippingCost = shippingCost + arrayListitem.get(image).getItemShippingCost();
                                    JsonObject jsonMultipleItem = new JsonObject();
                                    jsonMultipleItem.addProperty("item_id", arrayListitem.get(image).getId());
                                    jsonMultipleItem.addProperty("productqty", arrayListitem.get(image).getCartQty());
                                    multipleItemarray.add(jsonMultipleItem);

                                }


                            }

                            totalCost = itemPrice + 0;
                        }

                        if (arrayListItems != null) {
                            setAdapter();
                        }

                        txtTotalItemPriseCheckout.setText("$" + new DecimalFormat("#.##").format(itemPrice));
                        if (shippingCost != 0)
                            txtShippingPriseCheckout.setText(getString(R.string.txt_free));

                        else
                            txtShippingPriseCheckout.setText(getString(R.string.txt_free));
                        txttTotalOrderPrise.setText("$" + new DecimalFormat("#.##").format(totalCost));
                        txtTotalItemCheckout.setText("Items" + "(" + itemCount + ")");
                    }
                }

                @Override
                public void onFailure(Call<CartBean> call, Throwable t) {
                    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    int item_Pos = 0;

    private void setAdapter() {
        cartAdapter = new CartCheckoutAdapter(context, arrayListItems, arrayListPrices, arrayListItemsId, arrayListItemsName, arrayListItemsQuentity, new CartItemImage() {
            @Override
            public void ItemImage(String path, int position) {
                if (itemImagepreview.getVisibility() == View.VISIBLE && path.equals(itemImagePreviewtemp)) {
                    item_Pos = position;
                    if (arrayListItems.size() == 1) {
                        if (itemImagepreview.getVisibility() == View.VISIBLE) {
                            itemImagepreview.setVisibility(View.GONE);
                            relchangecart.setVisibility(View.GONE);
                        } else {
                            itemImagepreview.setVisibility(View.VISIBLE);
                            relchangecart.setVisibility(View.VISIBLE);
                        }
                    } else {
                        itemImagepreview.setVisibility(View.GONE);
                        relchangecart.setVisibility(View.GONE);
                    }
                } else {
                    itemImagepreview.setVisibility(View.VISIBLE);
                    relchangecart.setVisibility(View.VISIBLE);

                }

                itemImagePreviewtemp = arrayListItems.get(position);
                itemImagePreviewUrl = actualCartItem.get(position);

                Picasso.with(itemImagepreview.getContext())
                        .load(path)
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .into(itemImagepreview);

                txt_quntity.setText(String.valueOf(arrayListItemsQuentity.get(position)));
                txt_item_name.setText(String.valueOf(arrayListItemsName.get(position)));
                itemQunt = arrayListItemsTotalQuentity.get(position);
                item_id = arrayListItemsId.get(position);
                curruntPosition = position;

            }
        });
        recyclerCheckout.setAdapter(cartAdapter);
    }

    private void setQuantityList() {
        ArrayList<String> qtyList = new ArrayList<>();
        if (itemQunt > 500) {
            itemQunt = 500;
        }
        for (int i = 1; i <= itemQunt; i++) {
            qtyList.add(String.valueOf(i));
            //holder.txtItemQuntity.setText(String.valueOf(i));
        }
        DialogFragmentForItemQuantity dialogFragment = new DialogFragmentForItemQuantity(new CommonMethod() {
            @Override
            public void commonMethod(String type, File... files) {
                txt_quntity.setText(type);

                callUpdateCartQty(String.valueOf(item_id), type);

                for (int i = 0; i < multipleItemarray.size(); i++) {
                    JsonObject jb = (JsonObject) multipleItemarray.get(i);
                    if (jb.get("item_id").toString().contains(String.valueOf(item_id))) {
                        jb.addProperty("item_id", String.valueOf(item_id));
                        jb.addProperty("productqty", type);
                        multipleItemarray.remove(jb);
                        multipleItemarray.add(jb);
                    }
                }
                arrayListItemsQuentity.set(curruntPosition, Integer.parseInt(type));
            }
        },
                qtyList, context);
        android.app.FragmentManager manager = (this).getFragmentManager();
        dialogFragment.show(manager, "");

    }

    public void callUpdateCartQty(String id, String qty) {
        Map<String, String> map = new HashMap<>();
        Preferences preference = new Preferences(context);
        map.put("user_id", preference.getUserId());
        map.put("item_id", id);
        map.put("cart_qty", qty);
//        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        cartBeanCall = mService.getFetcherService(context).updateCartQty(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {
                    Log.e("CartAdapter", response.body().toString());
                    callGetCartItemApi();
                }

                @Override
                public void onFailure(Call<CartBean> call, Throwable t) {
                    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void onBackStackChanged() {
        setUpToolbar();
    }

    @Override
    public void commonMethod(String type, File... files) {
        txtItemQuntity.setText(type);
        txttTotalOrderPrise.setText(tSum);
    }

    @Override
    public void onCardSelected(String cardNumber, String expDate, String brand, String cardId) {
        tv_selected_payment.setVisibility(View.GONE);
        ll_payment_gateway_details_container.setVisibility(View.GONE);
        ll_card_details_container.setVisibility(View.VISIBLE);
        tv_card_details.setVisibility(View.VISIBLE);
        tv_card_brand.setVisibility(View.VISIBLE);
        tv_card_details.setText(cardNumber + " " + expDate);
        tv_card_brand.setText(brand);
        this.cardId = cardId;
    }

    @Override
    public void onPaymentGatewaySelected(String paymentProcessorName, String email) {
        tv_selected_payment.setVisibility(View.GONE);
        ll_payment_gateway_details_container.setVisibility(View.VISIBLE);
        ll_card_details_container.setVisibility(View.GONE);

        tv_payement_name.setText(paymentProcessorName);
        tv_payement_email.setText(email);
        this.cardId = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("FRAGMENT_INDEX")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", data.getExtras().getInt("FRAGMENT_INDEX"));
                setResult(RESULT_OK, returnIntent);
                finish();
                return;
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case StaticConstant.REQUEST_SELL_ITEM:
                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("POST_ANOTHER")) {
                        Intent intent = new Intent(this, SellItemActivity.class);
                        startActivityForResult(intent, StaticConstant.REQUEST_SELL_ITEM);
                        return;
                    }
                    break;
                case 1000:
                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("payerId")) {
                        String payerId = data.getExtras().getString("payerId");
                        String paypalsourceId = data.getExtras().getString("paypalsourceid");
                        if (payerId != null && paypalsourceId != null) {
                            finalizePayPalPaymentProcess(paypalsourceId, payerId);
                        }
                    }
                    break;
            }
        }

    }

    @OnClick({R.id.img_toolbar_left_icon, R.id.txtPay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_toolbar_left_icon:
                onBackPressed();
                break;
            case R.id.txtPay:
                if (!TextUtils.isEmpty(tv_shipping_address.getText().toString())) {
                    if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ItemDetailsAcitvity.class.getSimpleName())
                            || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(DashboardAdapter.class.getSimpleName())
                            || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ShopFragmentView.class.getSimpleName())
                            || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(NewCartFragment.class.getSimpleName())) {
                        if (tv_selected_payment.getVisibility() == View.VISIBLE || tv_payement_name.getText().toString().trim().equals("Connect")) {
                            //  payUsingWallet();
                            if (Double.parseDouble(preferences.getAmount()) > 0)
                                payUsingWalletMultipleItems();
                            else {
                                insufficientBalanceDialog();
                            }
                        } else {
                            if (cardId == null) {
                                if (tv_payement_name.getText().toString().equalsIgnoreCase("paypal")) {
                                    payWithPayPal();
                                    return;
                                }

                                if (tv_payement_name.getText().toString().equalsIgnoreCase("alipay")) {
                                    payWithAliPay();
                                    return;
                                }
                                if (tv_payement_name.getText().toString().equalsIgnoreCase("wechat")) {
                                    return;
                                }
                            } else {
                                buyMultipleitemsUsingCard();

                                // buyUsingCard();
                            }
                        }
                        return;
                    }

                    if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase("BoostActivity")) {
                        if (tv_selected_payment.getVisibility() == View.VISIBLE || tv_payement_name.getText().toString().trim().equals("XOCASH")) {
                            boostUsingWallet();
                        } else {

                            if (cardId == null) {
                                if (tv_payement_name.getText().toString().equalsIgnoreCase("paypal")) {
                                    processPayPalBoost();
                                    return;
                                }

                                if (tv_payement_name.getText().toString().equalsIgnoreCase("alipay")) {
                                    processAlipayBoost();
                                    return;
                                }

                                if (tv_payement_name.getText().toString().equalsIgnoreCase("wechat")) {
                                    return;
                                }
                            } else {
                                boostWithCard();
                            }

                        }
                        return;
                    }

                    if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(SellItemActivity.class.getSimpleName())) {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.txt_confirm))
                                .setMessage(getString(R.string.txt_confirm_forms_payment))
                                .setPositiveButton(getString(R.string.txt_agree), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        callCreateItemApi();
                                    }
                                }).setNegativeButton(getString(R.string.txt_cancel), null).show();
                    }

                    if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(TopUpActivity.class.getSimpleName())) {
                        if (tv_selected_payment.getVisibility() == View.VISIBLE) {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.msg_select_card_to_topup), DialogBox.DIALOG_FAILURE, null);
                            return;
                        }

                        topUpUsingCard();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.msg_add_shipping_address), LENGTH_LONG).show();
                }
                break;
        }
    }


    private void insufficientBalanceDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("2KXO")
                .setMessage(getString(R.string.msg_less_amount_in_wallet))
                .setPositiveButton(getString(R.string.txt_add_money), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(CartCheckoutActivity.this, TopUpActivity.class), REQ_CODE_TOP_UP_ACTIVITY_RESULTS);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        return;
    }
    /*TOP UP */

    private void topUpUsingCard() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("card_id", cardId);
        map.put("customer_id", preferences.getStripeCustomerId());
        map.put("type", "IN");
        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "amount", ""));

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            topUsingCard = mservice.getFetcherService(context).stripeProcessPayment(map);
            topUsingCard.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            finalizeTopUpUsingCard(response.body().getResult().getStripePaymentId());
                        } else {
                            progreessbar.setVisibility(View.GONE);
                            txtPay.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void finalizeTopUpUsingCard(String stripePaymentId) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("card_id", cardId);
        map.put("customer_id", preferences.getStripeCustomerId());
        map.put("type", "IN");
        map.put("stripe_id", stripePaymentId);
        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "amount", ""));

        if (Utils.isInternetOn(context)) {
            finalizeTopUpUsingCardCall = mservice.getFetcherService(context).addAmountToWallet(map);
            finalizeTopUpUsingCardCall.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            Utils.showToast(context, response.body().getMsg());
                            preferences.saveAmount(String.valueOf(response.body().getResult().getWalletBalance()));
                            finish();
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void buyMultipleitemsUsingCard() {
        Map<String, String> map = new HashMap<>();
        Map<String, ArrayList<Object>> mapitem = new HashMap<>();

        map.put("card_id", cardId);
        map.put("customer_id", preferences.getStripeCustomerId());

        if (multipleItemarray != null)
            mapitem.put("itemid", multipleItemarray);
        map.put("type", "OUT");
        map.put("shipping", tv_shipping_address.getText().toString());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            buyUsingCardCall = mservice.getFetcherService(context).stripeProcessPaymentMultipleItems(map, mapitem);
            buyUsingCardCall.enqueue(new Callback<StripeToken>() {

                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            finalizeBuyMultipleItemCard(response.body().getResult().getStripePaymentId());
                        } else {
                            progreessbar.setVisibility(View.GONE);
                            txtPay.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    /*BUY ITEM */
    private void buyUsingCard() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("card_id", cardId);
        map.put("customer_id", preferences.getStripeCustomerId());
        map.put("itemid", String.valueOf(userdetailsArray.get(0).getItems().get(0).getId()));
        map.put("type", "OUT");
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("productqty", String.valueOf(userdetailsArray.get(0).getItems().get(0).getItemQtyRemained()));

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            buyUsingCardCall = mservice.getFetcherService(context).stripeProcessPayment(map);
            buyUsingCardCall.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            finalizeBuyUsingCard(response.body().getResult().getStripePaymentId());
                        } else {
                            progreessbar.setVisibility(View.GONE);
                            txtPay.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void finalizeBuyUsingCard(String stripePaymentId) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("card_id", cardId);
        map.put("customer_id", preferences.getStripeCustomerId());
        map.put("itemid", String.valueOf(userdetailsArray.get(0).getItems().get(0).getId()));
        map.put("type", "OUT");
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("productqty", String.valueOf(userdetailsArray.get(0).getItems().get(0).getCartQty()));
        map.put("strip_response_id", stripePaymentId);

        if (Utils.isInternetOn(context)) {
            finalizeBuyUsingCardCall = mservice.getFetcherService(context).finalizeBuyUsingCard(map);
            finalizeBuyUsingCardCall.enqueue(new Callback<FinalProcessResponse>() {
                @Override
                public void onResponse(Call<FinalProcessResponse> call, Response<FinalProcessResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            returnResultAfterSuccessfullPayment(response);
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<FinalProcessResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void finalizeBuyMultipleItemCard(String stripePaymentId) {
        Map<String, String> map = new HashMap<>();
        Map<String, ArrayList<Object>> mapItem = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("card_id", cardId);
        map.put("customer_id", preferences.getStripeCustomerId());
        mapItem.put("itemid", multipleItemarray);
        map.put("type", "OUT");
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("strip_response_id", stripePaymentId);

        if (Utils.isInternetOn(context)) {
            finalizeBuyUsingCardCall = mservice.getFetcherService(context).finalizeMultipleItemBuyUsingCard(map, mapItem);
            finalizeBuyUsingCardCall.enqueue(new Callback<FinalProcessResponse>() {
                @Override
                public void onResponse(Call<FinalProcessResponse> call, Response<FinalProcessResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            Utils.showToast(context, response.body().getMsg());
                            returnResultAfterSuccessfullPayment(response);
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<FinalProcessResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void payUsingWallet() {
        Log.d(TAG, "payUsingWallet: ");
        try {
            NumberFormat format = NumberFormat.getCurrencyInstance();
            Number totalCostValueStr = format.parse(txttTotalOrderPrise.getText().toString());
            if (Double.parseDouble(preferences.getAmount()) < totalCostValueStr.doubleValue()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("2KXO")
                        .setMessage(getString(R.string.msg_less_amount_in_wallet))
                        .setPositiveButton(getString(R.string.txt_add_money), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(CartCheckoutActivity.this, TopUpActivity.class), REQ_CODE_TOP_UP_ACTIVITY_RESULTS);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return;
            }
        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<>();
        map.put("productqty", String.valueOf(userdetailsArray.get(0).getItems().get(0).getItemQtyRemained()));
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("wallet_amount", preferences.getAmount());
        map.put("itemid", String.valueOf(userdetailsArray.get(0).getItems().get(0).getId()));
        map.put("uid", "" + preferences.getUserId());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            payUsingWalletCall = mservice.getFetcherService(context).getWalletPay(map);
            payUsingWalletCall.enqueue(new Callback<FinalProcessResponse>() {
                @Override
                public void onResponse(Call<FinalProcessResponse> call, Response<FinalProcessResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            preferences.saveAmount(String.valueOf(response.body().getResult().getAmount()));
                            returnResultAfterSuccessfullPayment(response);
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<FinalProcessResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void payUsingWalletMultipleItems() {
        Log.d(TAG, "payUsingWallet: ");
        double availableAmount = Double.parseDouble(preferences.getAmount()) - Double.parseDouble(preferences.getHoldAmount());


        try {
            NumberFormat format = NumberFormat.getCurrencyInstance();
            String totalCostValueStr = txttTotalOrderPrise.getText().toString();
            if (Double.parseDouble(preferences.getAmount()) < Double.parseDouble(totalCostValueStr.substring(1, txttTotalOrderPrise.getText().toString().length()))) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("2KXO")
                        .setMessage(getString(R.string.msg_less_amount_in_wallet))
                        .setPositiveButton(getString(R.string.txt_add_money), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(CartCheckoutActivity.this, TopUpActivity.class), REQ_CODE_TOP_UP_ACTIVITY_RESULTS);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return;
            } else if (Double.parseDouble(preferences.getHoldAmount()) > 0 && availableAmount < Double.parseDouble(totalCostValueStr.substring(1, txttTotalOrderPrise.getText().toString().length()))) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("2KXO")
                        .setMessage(getString(R.string.msg_hold))
                        .setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                startActivityForResult(new Intent(CartCheckoutActivity.this, TopUpActivity.class), REQ_CODE_TOP_UP_ACTIVITY_RESULTS);
                                dialog.dismiss();
                            }
                        })

                        .show();
                return;

            } else if (preferences.getFreezeStatus() == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("2KXO")
                        .setMessage(getString(R.string.msg_freeze))
                        .setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                startActivityForResult(new Intent(CartCheckoutActivity.this, TopUpActivity.class), REQ_CODE_TOP_UP_ACTIVITY_RESULTS);
                                dialog.dismiss();
                            }
                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
                        .show();
                return;

            } else {
                Map<String, String> map = new HashMap<>();
                Map<String, ArrayList<Object>> mapItem = new HashMap<>();
                map.put("shipping", tv_shipping_address.getText().toString());
                map.put("wallet_amount", preferences.getAmount());
                mapItem.put("itemid", multipleItemarray);

                if (Utils.isInternetOn(context)) {
                    progreessbar.setVisibility(View.VISIBLE);
                    txtPay.setEnabled(false);
                    payUsingWalletCall = mservice.getFetcherService(context).getWalletPayMultipleitem(map, mapItem);
                    payUsingWalletCall.enqueue(new Callback<FinalProcessResponse>() {
                        @Override
                        public void onResponse(Call<FinalProcessResponse> call, Response<FinalProcessResponse> response) {
                            progreessbar.setVisibility(View.GONE);
                            if (response.body() != null) {
                                txtPay.setEnabled(true);
                                Toast.makeText(context, response.body().getMsg().toString(), Toast.LENGTH_SHORT).show();
                                if (response.code() == StaticConstant.RESULT_OK) {
                                    if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                                        preferences.saveAmount(String.valueOf(Double.parseDouble(preferences.getAmount()) - totalCost));
                                        returnResultAfterSuccessfullPayment(response);
                                    } else {
                                        DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                                    }
                                }
                            } else {
                                Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FinalProcessResponse> call, Throwable t) {
                            t.printStackTrace();
                            progreessbar.setVisibility(View.GONE);
                            txtPay.setEnabled(true);
                        }
                    });

                } else {
                    Utils.showToast(context, context.getString(R.string.toast_no_internet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void payWithPayPal() {
        Map<String, String> mappayPal = new HashMap<>();
        mappayPal.put("wallet_amount", preferences.getAmount());
        mappayPal.put("shipping", tv_shipping_address.getText().toString());
        mappayPal.put("productqty", txtItemQuntity.getText().toString());
        mappayPal.put("itemid", String.valueOf(userdetailsArray.get(0).getItems().get(0).getId()));
        mappayPal.put("uid", preferences.getUserId());
        mappayPal.put("email_id", tv_payement_email.getText().toString());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            processBitcoinCall = mservice.getFetcherService(context).processPayPal(mappayPal);
            processBitcoinCall.enqueue(new Callback<ProcessStripePaymentResponse>() {
                @Override
                public void onResponse(Call<ProcessStripePaymentResponse> call, Response<ProcessStripePaymentResponse> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (response.body().getResult().getApproveLink() != null) {
                                Intent intent = new Intent(CartCheckoutActivity.this, PayPalVerificationActivity.class);
                                intent.putExtra("verificationLink", response.body().getResult().getApproveLink());
                                intent.putExtra("paypalsourceid", response.body().getResult().getPayPalSourceId());
                                startActivityForResult(intent, 1000);
                            } else {
                                Toast.makeText(context, getString(R.string.txt_not_getting_verification_link), Toast.LENGTH_SHORT).show();
                            }
                            // finalizePayPalPaymentProcess(response.body().getResult().getPayPalSourceId());
                        } else {
                            progreessbar.setVisibility(View.GONE);
                            txtPay.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);
                        txtPay.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ProcessStripePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void finalizePayPalPaymentProcess(String stripeSourceId, String payerId) {
        Map<String, String> map = new HashMap<>();
        map.put("paypal_source_id", stripeSourceId);
        map.put("payer_id", payerId);

        if (Utils.isInternetOn(context)) {
            finalprocessBitcoinCall = mservice.getFetcherService(context).finalProcessPayPal(map);
            finalprocessBitcoinCall.enqueue(new Callback<FinalProcessResponse>() {
                @Override
                public void onResponse(Call<FinalProcessResponse> call, Response<FinalProcessResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            returnResultAfterSuccessfullPayment(response);
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<FinalProcessResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });

        } else {
            progreessbar.setVisibility(View.GONE);
            txtPay.setEnabled(true);
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void payWithAliPay() {
        Map<String, String> map = new HashMap<>();
        map.put("wallet_amount", preferences.getAmount());
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("productqty", String.valueOf(userdetailsArray.get(0).getItems().get(0).getItemQtyRemained()));
        map.put("itemid", String.valueOf(userdetailsArray.get(0).getItems().get(0).getId()));
        map.put("uid", "" + preferences.getUserId());
        map.put("email_id", tv_payement_email.getText().toString());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            processAlipayCall = mservice.getFetcherService(context).processAlipay(map);
            processAlipayCall.enqueue(new Callback<ProcessStripePaymentResponse>() {
                @Override
                public void onResponse(Call<ProcessStripePaymentResponse> call, Response<ProcessStripePaymentResponse> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equalsIgnoreCase(StaticConstant.STATUS_1)) {
                            finalizeAlipayPaymentProcess(response.body().getResult().getStripeSourceId());
                        } else {
                            progreessbar.setVisibility(View.GONE);
                            txtPay.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                            ;
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);
                        txtPay.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ProcessStripePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void finalizeAlipayPaymentProcess(String stripeSourceId) {
        Map<String, String> map = new HashMap<>();
        map.put("stripe_source_id", stripeSourceId);

        if (Utils.isInternetOn(context)) {
            finalProcessAlipayCall = mservice.getFetcherService(context).finalProcessApipay(map);
            finalProcessAlipayCall.enqueue(new Callback<FinalProcessResponse>() {
                @Override
                public void onResponse(Call<FinalProcessResponse> call, Response<FinalProcessResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            returnResultAfterSuccessfullPayment(response);
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<FinalProcessResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });

        } else {
            progreessbar.setVisibility(View.GONE);
            txtPay.setEnabled(true);
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void returnResultAfterSuccessfullPayment(Response<FinalProcessResponse> response) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("itemid", response.body().getResult().getItemid());
        returnIntent.putExtra("item_name", response.body().getResult().getItemname());
        returnIntent.putExtra("item_image", response.body().getResult().getItemimage());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    /*BOOST */
    private void boostUsingWallet() {
        Log.d(TAG, "boostUsingWallet: ");
        try {
            NumberFormat format = NumberFormat.getCurrencyInstance();
            Number totalCostValueStr = format.parse(txttTotalOrderPrise.getText().toString());
            if (Double.parseDouble(preferences.getAmount()) < totalCostValueStr.doubleValue()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("2KXO")
                        .setMessage(getString(R.string.msg_less_amount_in_wallet))
                        .setPositiveButton(getString(R.string.txt_add_money), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(CartCheckoutActivity.this, TopUpActivity.class), REQ_CODE_TOP_UP_ACTIVITY_RESULTS);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return;
            }
        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<>();

        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "price", ""));
        map.put("no_days", BundleUtils.getIntentExtra(getIntent(), "day", ""));
        map.put("no_users", BundleUtils.getIntentExtra(getIntent(), "reach", ""));
        map.put("boost_type", BundleUtils.getIntentExtra(getIntent(), "boost_type", ""));

        map.put("wallet_amount", preferences.getAmount());
        map.put("itemid", String.valueOf(userdetailsArray.get(0).getItems().get(0).getId()));
        map.put("uid", "" + preferences.getUserId());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            boostUsingWalletCall = mservice.getFetcherService(context).finalBoostProcess(map);
            boostUsingWalletCall.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            preferences.saveAmount(String.valueOf(response.body().getResult().getAmount()));
                            onBoostSuccess();
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void boostWithCard() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "price", ""));
        map.put("card_id", cardId);
        map.put("customer_id", preferences.getStripeCustomerId());
        map.put("type", "OUT");
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("itemid", String.valueOf(userdetailsArray.get(0).getItems().get(0).getId()));
        map.put("boost_type", BundleUtils.getIntentExtra(getIntent(), "boost_type", ""));

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            boostUsingCardcall = mservice.getFetcherService(context).stripeProcessBoost(map);
            boostUsingCardcall.enqueue(new Callback<StripeProcessBoostResponse>() {
                @Override
                public void onResponse(Call<StripeProcessBoostResponse> call, Response<StripeProcessBoostResponse> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            stripeFinalBoostProcessUsingCard(response.body().getResult().getStripePaymentId());
                        } else {
                            progreessbar.setVisibility(View.GONE);
                            txtPay.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeProcessBoostResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void stripeFinalBoostProcessUsingCard(String stripeResponseId) {
        Map<String, String> map = new HashMap<>();

        map.put("strip_response_id", stripeResponseId);
        map.put("no_days", BundleUtils.getIntentExtra(getIntent(), "day", ""));
        map.put("no_users", BundleUtils.getIntentExtra(getIntent(), "reach", ""));
        map.put("wallet_amount", preferences.getUserId());
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("uid", preferences.getUserId());
        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "price", ""));
        map.put("card_id", cardId);
        map.put("customer_id", preferences.getStripeCustomerId());
        map.put("type", "OUT");
        map.put("itemid", String.valueOf(userdetailsArray.get(0).getItems().get(0).getId()));
        map.put("boost_type", BundleUtils.getIntentExtra(getIntent(), "boost_type", ""));

        if (Utils.isInternetOn(context)) {
            boostUsingCardcall = mservice.getFetcherService(context).stripeFinalBoostProcess(map);
            boostUsingCardcall.enqueue(new Callback<StripeProcessBoostResponse>() {
                @Override
                public void onResponse(Call<StripeProcessBoostResponse> call, Response<StripeProcessBoostResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            onBoostSuccess();
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeProcessBoostResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void processPayPalBoost() {
        Log.d(TAG, "processPayPalBoost: ");
        Map<String, String> map = new HashMap<>();
        map.put("wallet_amount", preferences.getAmount());
        map.put("itemid", String.valueOf(userdetailsArray.get(0).getItems().get(0).getId()));
        map.put("uid", preferences.getUserId());
        map.put("email_id", tv_payement_email.getText().toString());
        map.put("type", "OUT");
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "price", ""));
        map.put("boost_type", BundleUtils.getIntentExtra(getIntent(), "boost_type", ""));
        map.put("no_days", BundleUtils.getIntentExtra(getIntent(), "day", ""));
        map.put("no_users", BundleUtils.getIntentExtra(getIntent(), "reach", ""));

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            processBitcoinBoostCall = mservice.getFetcherService(context).processPayPalBoost(map);
            processBitcoinBoostCall.enqueue(new Callback<ProcessStripePaymentResponse>() {
                @Override
                public void onResponse(Call<ProcessStripePaymentResponse> call, Response<ProcessStripePaymentResponse> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            finalProcessPayPalBoost(response.body().getResult().getPayPalpaymentId());
                        } else {
                            progreessbar.setVisibility(View.GONE);
                            txtPay.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);
                        txtPay.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ProcessStripePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void finalProcessPayPalBoost(String stripeSourceId) {
        Log.d(TAG, "finalProcessPayPalBoost: ");

        Map<String, String> map = new HashMap<>();
        map.put("wallet_amount", preferences.getAmount());
        map.put("itemid", String.valueOf(userdetailsArray.get(0).getItems().get(0).getId()));
        map.put("uid", preferences.getUserId());
        map.put("email_id", tv_payement_email.getText().toString());
        // map.put("stripe_source_id", stripeSourceId);
        map.put("type", "OUT");
        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "price", ""));
        map.put("boost_type", BundleUtils.getIntentExtra(getIntent(), "boost_type", ""));
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("paypal_response_id", stripeSourceId);
        map.put("no_days", BundleUtils.getIntentExtra(getIntent(), "day", ""));
        map.put("no_users", BundleUtils.getIntentExtra(getIntent(), "reach", ""));

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            finalProcessBitcoinBoostCall = mservice.getFetcherService(context).finalProcessPayPalBoost(map);
            finalProcessBitcoinBoostCall.enqueue(new Callback<ProcessStripePaymentResponse>() {
                @Override
                public void onResponse(Call<ProcessStripePaymentResponse> call, Response<ProcessStripePaymentResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            onBoostSuccess();
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);
                        txtPay.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ProcessStripePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void processAlipayBoost() {
        Map<String, String> map = new HashMap<>();
        map.put("wallet_amount", preferences.getAmount());
        map.put("itemid", String.valueOf(userdetailsArray.get(0).getItems().get(0).getId()));
        map.put("uid", preferences.getUserId());
        map.put("email_id", tv_payement_email.getText().toString());
        map.put("type", "OUT");
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "price", ""));
        map.put("boost_type", BundleUtils.getIntentExtra(getIntent(), "boost_type", ""));
        map.put("no_days", BundleUtils.getIntentExtra(getIntent(), "day", ""));
        map.put("no_users", BundleUtils.getIntentExtra(getIntent(), "reach", ""));

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            processAlipayBoostCall = mservice.getFetcherService(context).processAlipayBoost(map);
            processAlipayBoostCall.enqueue(new Callback<ProcessStripePaymentResponse>() {
                @Override
                public void onResponse(Call<ProcessStripePaymentResponse> call, Response<ProcessStripePaymentResponse> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            finalProcessAlipayBoost(response.body().getResult().getStripeSourceId());
                        } else {
                            progreessbar.setVisibility(View.GONE);
                            txtPay.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);
                        txtPay.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ProcessStripePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void finalProcessAlipayBoost(String stripeSourceId) {

        Map<String, String> map = new HashMap<>();
        map.put("wallet_amount", preferences.getAmount());
        map.put("itemid", String.valueOf(userdetailsArray.get(0).getItems().get(0).getId()));
        map.put("uid", preferences.getUserId());
        map.put("email_id", tv_payement_email.getText().toString());
        map.put("stripe_source_id", stripeSourceId);
        map.put("type", "OUT");
        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "price", ""));
        map.put("boost_type", BundleUtils.getIntentExtra(getIntent(), "boost_type", ""));
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("strip_response_id", stripeSourceId);
        map.put("no_days", BundleUtils.getIntentExtra(getIntent(), "day", ""));
        map.put("no_users", BundleUtils.getIntentExtra(getIntent(), "reach", ""));

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            finalProcessAlipayBoostCall = mservice.getFetcherService(context).finalProcessAlipayBoost(map);
            finalProcessAlipayBoostCall.enqueue(new Callback<ProcessStripePaymentResponse>() {
                @Override
                public void onResponse(Call<ProcessStripePaymentResponse> call, Response<ProcessStripePaymentResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            onBoostSuccess();
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);
                        txtPay.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ProcessStripePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void onBoostSuccess() {
        Intent gotoCOngrats = new Intent(CartCheckoutActivity.this, BoostCongActivity.class);
        /*gotoCOngrats.putExtra("image", BundleUtils.getIntentExtra(getIntent(), "item_image", null));
        gotoCOngrats.putExtra("name", name);*/
        startActivity(gotoCOngrats);
        finish();
    }

    /*GET SHIPPING ADDRESS*/
    private void callGetAddressApi() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());

        if (Utils.isInternetOn(context)) {
            txtPay.setEnabled(false);
            progreessbar.setVisibility(View.VISIBLE);
            getAddressCall = mservice.getFetcherService(context).getAddress();
            getAddressCall.enqueue(new Callback<AddressBean>() {
                @Override
                public void onResponse(Call<AddressBean> call, Response<AddressBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                    if (response.body() != null) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            ShippingAddress shippingAddress = response.body().getResult().getShippingAddress();
                            if (shippingAddress != null)
                                tv_shipping_address.setText(shippingAddress.getAddress());
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddressBean> call, Throwable t) {
                    t.printStackTrace();
                    txtPay.setEnabled(true);
                    progreessbar.setVisibility(View.GONE);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    /*Create Item APi.. */
    private void callCreateItemApi() {
        if (Utils.isInternetOn(context)) {
            Utils.printIntentData(getIntent());
            HashMap<String, String> map = (HashMap<String, String>) getIntent().getSerializableExtra("mapString");
            HashMap<String, File> fileArray = new HashMap<String, File>();
            for (int i = 0; i < StaticConstant.imageFiles_new.length; i++) {
                fileArray.put("fileArraymap", StaticConstant.imageFiles_new[i]);
            }
            RequestBodyConveter requestbodyconverter = new RequestBodyConveter();
            progreessbar.setVisibility(View.VISIBLE);
            txtPay.setEnabled(false);
            createitembeanCall = mservice.getFetcherService(context).CreateItem(requestbodyconverter.converRequestBodyFromMap(map), requestbodyconverter.converRequestBodyFromMapImage(fileArray, type));

            createitembeanCall.enqueue(new Callback<SellItemBean>() {
                @Override
                public void onResponse(Call<SellItemBean> call, Response<SellItemBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            Utils.showToast(context, response.body().getMsg());
                            isUpdate = true;
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("update", isUpdate);
                            returnIntent.putExtra("itemid", String.valueOf(response.body().getResult().getItemid()));
                            returnIntent.putExtra("item_name", response.body().getResult().getItem_name());
                            returnIntent.putExtra("item_image", response.body().getResult().getItem_image());
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<SellItemBean> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    txtPay.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (fragment != null && (fragment instanceof SelectPaymentFragment || fragment instanceof SelectProcessorFragment)) {
            super.onBackPressed();
            return;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("update", isUpdate);
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onDestroy() {
        if (payUsingWalletCall != null) {
            payUsingWalletCall.cancel();
        }

        if (processAlipayCall != null) {
            processAlipayCall.cancel();
        }

        if (finalProcessAlipayCall != null) {
            finalProcessAlipayCall.cancel();
        }

        if (processBitcoinCall != null) {
            processBitcoinCall.cancel();
        }


        if (finalprocessBitcoinCall != null) {
            finalprocessBitcoinCall.cancel();
        }

        if (getAddressCall != null) {
            getAddressCall.cancel();
        }

        if (createitembeanCall != null) {
            createitembeanCall.cancel();
        }
        if (buyUsingCardCall != null) {
            buyUsingCardCall.cancel();
        }
        if (finalizeBuyUsingCardCall != null) {
            finalizeBuyUsingCardCall.cancel();
        }
        if (topUsingCard != null) {
            topUsingCard.cancel();
        }
        if (finalizeTopUpUsingCardCall != null) {
            finalizeTopUpUsingCardCall.cancel();
        }

        /*BOOST */

        if (boostUsingWalletCall != null) {
            boostUsingWalletCall.cancel();
        }
        if (boostUsingCardcall != null) {
            boostUsingCardcall.cancel();
        }

        if (processBitcoinBoostCall != null) {
            processBitcoinBoostCall.cancel();
        }

        if (finalProcessBitcoinBoostCall != null) {
            finalProcessBitcoinBoostCall.cancel();
        }

        if (processAlipayBoostCall != null) {
            processAlipayBoostCall.cancel();
        }

        if (finalProcessAlipayBoostCall != null) {
            finalProcessAlipayBoostCall.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
