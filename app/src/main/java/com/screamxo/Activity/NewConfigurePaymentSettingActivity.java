package com.screamxo.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.AddressBean;
import com.example.apimodule.ApiBase.ApiBean.Boost.StripeProcessBoostResponse;
import com.example.apimodule.ApiBase.ApiBean.Payment.StripeToken;
import com.example.apimodule.ApiBase.ApiBean.SellItemBean;
import com.example.apimodule.ApiBase.ApiBean.ShippingAddress;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.example.apimodule.ApiBase.StripeAlipay.ProcessStripePaymentResponse;
import com.example.apimodule.ApiBase.WalletOrderPayment.FinalProcessResponse;
import com.google.gson.JsonObject;
import com.screamxo.Activity.RahulWork.BoostCongActivity;
import com.screamxo.Activity.RahulWork.FriendsActivity;
import com.screamxo.Activity.RahulWork.ItemDetailsAcitvity;
import com.screamxo.Activity.RahulWork.TopUpActivity;
import com.screamxo.Activity.RahulWork.UploadDataActivity;
import com.screamxo.Activity.wallet.WalletSendReceiveActivity;
import com.screamxo.Adapter.DashboardAdapter;
import com.screamxo.Fragment.SelectPaymentFragment;
import com.screamxo.Fragment.SelectProcessorFragment;
import com.screamxo.Fragment.SettingFragment;
import com.screamxo.Fragment.ShopFragmentView;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Interface.DialogInterfaceAction;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.DialogFragmentForItemQuantity;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.RequestBodyConveter;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_PAYMENT_DETAIL_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_TOP_UP_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_WALLET_ACTIVITY_RESULTS;

@SuppressWarnings("unchecked")
@SuppressLint("SetTextI18n")
public class NewConfigurePaymentSettingActivity extends AppCompatActivity implements CommonMethod, FragmentManager.OnBackStackChangedListener, SelectPaymentFragment.OnPaymentSelectedListener {

    public static final int REQ_CODE_FRIENDS_ACTIVITY_RESULTS = 101;
    private static final String TAG = "NewConfigurePaymentSettingActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_continue)
    Button btnContinue;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.txt_balance)
    TextView txt_balance;
    @BindView(R.id.ll_header_item_detail_container)
    LinearLayout ll_header_item_detail_container;
    @BindView(R.id.ll_wallet_container)
    LinearLayout ll_wallet_container;
    @BindView(R.id.ll_ship_to_container)
    LinearLayout ll_ship_to_container;
    @BindView(R.id.ll_payment_details)
    LinearLayout ll_payment_details;
    @BindView(R.id.tv_selected_payment)
    TextView tv_selected_payment;
    @BindView(R.id.ll_payment_gateway_details_container)
    LinearLayout ll_payment_gateway_details_container;
    @BindView(R.id.ll_card_details_container)
    LinearLayout ll_card_details_container;
    @BindView(R.id.tv_payement_name)
    TextView tv_payement_name;
    @BindView(R.id.tv_payement_email)
    TextView tv_payement_email;
    @BindView(R.id.tv_card_details)
    TextView tv_card_details;
    @BindView(R.id.tv_card_brand)
    TextView tv_card_brand;
    @BindView(R.id.fl_container)
    FrameLayout fl_container;
    @BindView(R.id.ll_card_detail_container)
    LinearLayout ll_card_detail_container;
    @BindView(R.id.iv_product_image)
    ImageView iv_product_image;
    @BindView(R.id.tv_product_name)
    TextView tv_product_name;
    @BindView(R.id.tv_product_price)
    TextView tv_product_price;
    @BindView(R.id.tv_shipping_address)
    TextView tv_shipping_address;
    @BindView(R.id.tv_first_name)
    TextView tv_first_name;
    @BindView(R.id.frm_item_quantity)
    FrameLayout frmItemQuantity;
    @BindView(R.id.txt_quntity)
    EditText txt_quntity;
    @BindView(R.id.txt_cost_value)
    TextView txt_cost_value;
    @BindView(R.id.txt_shipping_cost_value)
    TextView txt_shipping_cost_value;
    @BindView(R.id.txt_total_cost_value)
    TextView txt_total_cost_value;
    @BindView(R.id.imgItem)
    ImageView imgItem;
    @BindView(R.id.relative_price)
    RelativeLayout relativePrice;
    @BindView(R.id.frame_item_img)
    FrameLayout frameItemImg;
    @BindView(R.id.txt_campaign_value)
    TextView txtCampaignValue;
    @BindView(R.id.txt_total_price)
    TextView txtTotalPrice;
    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private String cardId;
    private String type[] = {StaticConstant.POST_TYPE_IMAGE, StaticConstant.POST_TYPE_IMAGE, StaticConstant.POST_TYPE_IMAGE, StaticConstant.POST_TYPE_IMAGE};
    private Context context;
    private String paypalsourceId;
    private FetchrServiceBase mservice;
    private Preferences preferences;
    private boolean isUpdate = false;
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

    /*BOOST*/
    private Call<StripeToken> boostUsingWalletCall;

    private Call<StripeProcessBoostResponse> boostUsingCardcall;

    private Call<ProcessStripePaymentResponse> processBitcoinBoostCall;
    private Call<ProcessStripePaymentResponse> finalProcessBitcoinBoostCall;

    private Call<ProcessStripePaymentResponse> processAlipayBoostCall;
    private Call<ProcessStripePaymentResponse> finalProcessAlipayBoostCall;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_new_configure_payment_setting);
        ButterKnife.bind(this);
        init();
        initControlValue();
        initFabIcon();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.getFormattedPrice(preferences.getAmount()) == null) {
            txt_balance.setVisibility(View.GONE);
        } else {
            txt_balance.setText(Utils.getFormattedPrice(preferences.getAmount()));
        }

    }

    public void init() {
        context = this;
        mservice = new FetchrServiceBase();
        preferences = new Preferences(context);
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        tv_first_name.setText(preferences.getUserFullName());
        Utils.printIntentData(getIntent());
        loadBundleData();
        setUpToolbar();
        name = BundleUtils.getIntentExtra(getIntent(), "item_name", null);
    }

    private void setUpToolbar() {
        setTitle();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (fragment != null && (fragment instanceof SelectPaymentFragment || fragment instanceof SelectProcessorFragment)) {
            updatePinkToolbar();
            return;
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ItemDetailsAcitvity.class.getSimpleName())
                || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(DashboardAdapter.class.getSimpleName())
                || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ShopFragmentView.class.getSimpleName())
                || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase("BoostActivity")) {
            updateWhiteToolbar();
        } else {
            updatePinkToolbar();
        }
    }

    private void updateWhiteToolbar() {
        imgToolbarLeftIcon.setImageResource(R.drawable.ico_back);
        toolbar.setBackgroundColor(Color.WHITE);
        txtToolbarTitle.setTextColor(Color.BLACK);
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

    public void initControlValue() {

        frmItemQuantity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArrayList<String> qtyList = new ArrayList<>();
                for (int i = 1; i <= Integer.parseInt(BundleUtils.getIntentExtra(getIntent(), "total_quantity", "0")); i++) {
                    qtyList.add(String.valueOf(i));
                }

                DialogFragmentForItemQuantity dialogFragment = new DialogFragmentForItemQuantity(NewConfigurePaymentSettingActivity.this, qtyList, context);
                dialogFragment.show(getFragmentManager(), "Time Fragment");
            }
        });

        ll_wallet_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(NewConfigurePaymentSettingActivity.this, WalletSendReceiveActivity.class), REQ_CODE_WALLET_ACTIVITY_RESULTS);
            }
        });

        ll_card_detail_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent i = new Intent(context, PaymentDetailActivity.class);
                ((Activity) context).startActivityForResult(i, REQ_CODE_PAYMENT_DETAIL_ACTIVITY_RESULTS);
            }
        });
    }

    private void loadBundleData() {
        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ItemDetailsAcitvity.class.getSimpleName())
                || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(DashboardAdapter.class.getSimpleName())
                || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ShopFragmentView.class.getSimpleName())) {
            ll_header_item_detail_container.setVisibility(View.VISIBLE);
            ll_payment_details.setVisibility(View.VISIBLE);

//          txt_quntity.setText(BundleUtils.getIntentExtra(getIntent(), "selected_quantity", ""));
            tv_product_name.setText(BundleUtils.getIntentExtra(getIntent(), "item_name", ""));
            if (Utils.getFormattedPrice(BundleUtils.getIntentExtra(getIntent(), "item_cost", "")) == null) {
                tv_product_price.setVisibility(View.GONE);
            } else {
                tv_product_price.setText(Utils.getFormattedPrice(BundleUtils.getIntentExtra(getIntent(), "item_cost", "")));
            }
            Picasso.with(this).load(BundleUtils.getIntentExtra(getIntent(), "item_media_thumb", null)).placeholder(R.mipmap.img_placeholder).error(R.mipmap.img_placeholder).fit().into(iv_product_image);

            txt_cost_value.setText(BundleUtils.getIntentExtra(getIntent(), "item_cost", ""));
            txt_shipping_cost_value.setText(BundleUtils.getIntentExtra(getIntent(), "item_shipping_cost", ""));
            txt_total_cost_value.setText(String.valueOf(Double.parseDouble(BundleUtils.getIntentExtra(getIntent(), "item_cost", ""))
                    + Double.parseDouble(BundleUtils.getIntentExtra(getIntent(), "item_shipping_cost", ""))));
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase("BoostActivity")) {
            btnContinue.setText("Pay");
            String price = getIntent().getExtras().getString("price");
            if (price != null) {
                txtCampaignValue.setText(price);
                txtTotalPrice.setText(price);
            }
            relativePrice.setVisibility(View.VISIBLE);
            frameItemImg.setVisibility(View.VISIBLE);
            if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("item_image")) {
                Picasso.with(this)
                        .load(getIntent().getExtras().getString("item_image"))
                        .into(imgItem);
            }
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(SettingFragment.class.getSimpleName())) {
            btnContinue.setVisibility(View.VISIBLE);
            btnContinue.setText("Top Up");
            tv_payement_name.setText("Select Payment");
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(TopUpActivity.class.getSimpleName())) {
            tv_payement_name.setText("Select Payment");
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(SellItemActivity.class.getSimpleName())) {
            btnContinue.setText("Sell Item");
        }
    }

    private void setTitle() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (fragment != null && fragment instanceof SelectPaymentFragment) {
            txtToolbarTitle.setText("SELECT PAYMENT");
            return;
        }

        if (fragment != null && fragment instanceof SelectProcessorFragment) {
            txtToolbarTitle.setText("SELECT PROCESSOR");
            return;
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ItemDetailsAcitvity.class.getSimpleName())
                || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(DashboardAdapter.class.getSimpleName())
                || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ShopFragmentView.class.getSimpleName())) {
            txtToolbarTitle.setText("PAYMENT");
            return;
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase("BoostActivity")) {
            txtToolbarTitle.setText("PAYMENT");
            return;
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(SellItemActivity.class.getSimpleName())) {
            txtToolbarTitle.setText("PAYMENT");
            return;
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(TopUpActivity.class.getSimpleName()) ||
                BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(SettingFragment.class.getSimpleName())) {
            txtToolbarTitle.setText("Payments");
            return;
        }

        txtToolbarTitle.setText("PAYMENT");
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
                Utils.hideKeyboard(NewConfigurePaymentSettingActivity.this);
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
                    Intent gotoNext = new Intent(NewConfigurePaymentSettingActivity.this, UploadDataActivity.class);
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
                    startActivityForResult(new Intent(NewConfigurePaymentSettingActivity.this, WalletSendReceiveActivity.class), REQ_CODE_WALLET_ACTIVITY_RESULTS);
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
                if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                    Intent gotoNext = new Intent(NewConfigurePaymentSettingActivity.this, FriendsActivity.class);
                    startActivityForResult(gotoNext, REQ_CODE_FRIENDS_ACTIVITY_RESULTS);
                    finish();
                    /*Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 0);
                    setResult(RESULT_OK, returnIntent);*/
                } else {
                    gotoLogin();
                }
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
                    Intent gotoNext = new Intent(NewConfigurePaymentSettingActivity.this, FriendsActivity.class);
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
                        Intent intent = new Intent(this, com.screamxo.Activity.RahulWork.SellItemActivity.class);
                        startActivityForResult(intent, StaticConstant.REQUEST_SELL_ITEM);
                        return;
                    }
                    break;
                case REQ_CODE_PAYMENT_DETAIL_ACTIVITY_RESULTS:
                    break;
                case 1000:
                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("payerId")) {
                        String payerId = data.getExtras().getString("payerId");
                        paypalsourceId = data.getExtras().getString("paypalsourceid");
                        if (payerId != null && paypalsourceId != null) {
                            finalizePayPalPaymentProcess(paypalsourceId, payerId);
                        }
                    }
                    break;
                case 1001:
                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("payerId")) {
                        String payerId = data.getExtras().getString("payerId");
                        paypalsourceId = data.getExtras().getString("paypalsourceid");
                        if (payerId != null && paypalsourceId != null) {
                            finalProcessPayPalBoost(paypalsourceId, payerId);
                        }
                    }
                    break;

            }
        }

    }

    @OnClick({R.id.img_toolbar_left_icon, R.id.btn_continue})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_toolbar_left_icon:
                onBackPressed();
                break;
            case R.id.btn_continue:
                if (!TextUtils.isEmpty(tv_shipping_address.getText().toString())) {
                    if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ItemDetailsAcitvity.class.getSimpleName())
                            || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(DashboardAdapter.class.getSimpleName())
                            || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ShopFragmentView.class.getSimpleName())) {
                        if (tv_selected_payment.getVisibility() == View.VISIBLE || tv_payement_name.getText().toString().trim().equals("XOCASH")) {
                            payUsingWalletforMultipleItem();
                            //  payUsingWallet();
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
                                //buyUsingCard();
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
                                .setMessage(getString(R.string.txt_accepted_forms_payment))
                                .setPositiveButton(getString(R.string.txt_agree), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        callCreateItemApi();
                                    }
                                }).setNegativeButton(getString(R.string.txt_cancel), null).show();
                    }

                    if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(TopUpActivity.class.getSimpleName())) {
                        if (tv_selected_payment.getVisibility() == View.VISIBLE) {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.msg_select_card_to_topup
                            ), DialogBox.DIALOG_FAILURE, null);
                            return;
                        }

                        topUpUsingCard();
                    }

                    if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(SettingFragment.class.getSimpleName())) {
                        startActivityForResult(new Intent(NewConfigurePaymentSettingActivity.this, WalletSendReceiveActivity.class), REQ_CODE_TOP_UP_ACTIVITY_RESULTS);
                        finish();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.txt_enter_shipping_address), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /*TOP UP */

    private void topUpUsingCard()
    {

        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("card_id", cardId);
        map.put("customer_id", preferences.getStripeCustomerId());
        map.put("type", "IN");
        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "amount", ""));

        if (Utils.isInternetOn(context))
        {
            progreessbar.setVisibility(View.VISIBLE);
            btnContinue.setEnabled(false);
            topUsingCard = mservice.getFetcherService(context).stripeProcessPayment(map);
            topUsingCard.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        finalizeTopUpUsingCard(response.body().getResult().getStripePaymentId());
                    } else if (response.code() == StaticConstant.BAD_REQUEST) {
                        progreessbar.setVisibility(View.GONE);
                        btnContinue.setEnabled(true);
                        DialogBox.showDialog(context, context.getString(R.string.app_name), "Something go wrong!", DialogBox.DIALOG_FAILURE, null);
                    } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                        Utils.unAuthentication(context);
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
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

        ArrayList<Object> multipleItemarray = new ArrayList<>();
        JsonObject jsonMultipleItem = new JsonObject();
        jsonMultipleItem.addProperty("item_id", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
        jsonMultipleItem.addProperty("productqty", BundleUtils.getIntentExtra(getIntent(), "total_quantity", ""));
        multipleItemarray.add(jsonMultipleItem);

        if (multipleItemarray != null)
            mapitem.put("itemid", multipleItemarray);
        map.put("type", "OUT");
        map.put("shipping", tv_shipping_address.getText().toString());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            btnContinue.setEnabled(false);
            buyUsingCardCall = mservice.getFetcherService(context).stripeProcessPaymentMultipleItems(map, mapitem);
            buyUsingCardCall.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            finalizeBuyMultipleItemCard(response.body().getResult().getStripePaymentId());
                        } else if (response.code() == StaticConstant.BAD_REQUEST) {
                            progreessbar.setVisibility(View.GONE);
                            btnContinue.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), "Something go wrong!",
                                    DialogBox.DIALOG_FAILURE, null);
                        } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                            Utils.unAuthentication(context);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
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
                    btnContinue.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
//                            Utils.showToast(context, response.body().getMsg());
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_SUCESS, new DialogInterfaceAction() {
                                @Override
                                public void dialogAction() {
                                    finish();
                                }
                            });
                            preferences.saveAmount(String.valueOf(response.body().getResult().getWalletBalance()));

                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
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
        map.put("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
        map.put("type", "OUT");
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("productqty", txt_quntity.getText().toString());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            btnContinue.setEnabled(false);
            buyUsingCardCall = mservice.getFetcherService(context).stripeProcessPayment(map);
            buyUsingCardCall.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        finalizeBuyUsingCard(response.body().getResult().getStripePaymentId());
                    } else if (response.code() == StaticConstant.BAD_REQUEST) {
                        progreessbar.setVisibility(View.GONE);
                        btnContinue.setEnabled(true);
                        DialogBox.showDialog(context, context.getString(R.string.app_name), "Something go wrong!",
                                DialogBox.DIALOG_FAILURE, null);
                    } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                        Utils.unAuthentication(context);
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
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

        ArrayList<Object> multipleItemarray = new ArrayList<>();
        JsonObject jsonMultipleItem = new JsonObject();
        jsonMultipleItem.addProperty("item_id", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
        jsonMultipleItem.addProperty("productqty", BundleUtils.getIntentExtra(getIntent(), "total_quantity", ""));
        multipleItemarray.add(jsonMultipleItem);

        if (multipleItemarray != null)
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
                    btnContinue.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            returnResultAfterSuccessfullPayment(response);
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }
                @Override
                public void onFailure(Call<FinalProcessResponse> call, Throwable t)
                {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
                }
            });
        }
        else
            {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void finalizeBuyUsingCard(String stripePaymentId)
    {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("card_id", cardId);
        map.put("customer_id", preferences.getStripeCustomerId());
        map.put("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
        map.put("type", "OUT");
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("productqty", txt_quntity.getText().toString());
        map.put("strip_response_id", stripePaymentId);

        if (Utils.isInternetOn(context)) {
            finalizeBuyUsingCardCall = mservice.getFetcherService(context).finalizeBuyUsingCard(map);
            finalizeBuyUsingCardCall.enqueue(new Callback<FinalProcessResponse>() {
                @Override
                public void onResponse(Call<FinalProcessResponse> call, Response<FinalProcessResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
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
                    btnContinue.setEnabled(true);
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
            Number totalCostValueStr = format.parse(txt_total_cost_value.getText().toString());
            if (Double.parseDouble(preferences.getAmount()) < totalCostValueStr.doubleValue()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("2KXO")
                        .setMessage(getString(R.string.msg_less_amount_in_wallet))
                        .setPositiveButton(getString(R.string.txt_add_money), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(NewConfigurePaymentSettingActivity.this, TopUpActivity.class), REQ_CODE_TOP_UP_ACTIVITY_RESULTS);
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
        map.put("productqty", txt_quntity.getText().toString());
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("wallet_amount", preferences.getAmount());
        map.put("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
        map.put("uid", "" + preferences.getUserId());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            btnContinue.setEnabled(false);
            payUsingWalletCall = mservice.getFetcherService(context).getWalletPay(map);
            payUsingWalletCall.enqueue(new Callback<FinalProcessResponse>() {
                @Override
                public void onResponse(Call<FinalProcessResponse> call, Response<FinalProcessResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
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
                public void onFailure(Call<FinalProcessResponse> call, Throwable t)
                {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void payUsingWalletforMultipleItem() {
        Log.d(TAG, "payUsingWallet: ");
        try {
            NumberFormat format = NumberFormat.getCurrencyInstance();
            String totalCostValueStr = txt_total_cost_value.getText().toString();
            if (Double.parseDouble(preferences.getAmount()) < Double.parseDouble(totalCostValueStr)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("2KXO")
                        .setMessage(getString(R.string.msg_less_amount_in_wallet))
                        .setPositiveButton(getString(R.string.txt_add_money), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(NewConfigurePaymentSettingActivity.this, TopUpActivity.class), REQ_CODE_TOP_UP_ACTIVITY_RESULTS);
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
            } else {

                Map<String, String> map = new HashMap<>();
                HashMap<String, ArrayList<Object>> mapitem = new HashMap<>();
                map.put("shipping", tv_shipping_address.getText().toString());
                map.put("wallet_amount", preferences.getAmount());

                ArrayList<Object> multipleItemarray = new ArrayList<>();
                JsonObject jsonMultipleItem = new JsonObject();
                jsonMultipleItem.addProperty("item_id", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
                jsonMultipleItem.addProperty("productqty", BundleUtils.getIntentExtra(getIntent(), "total_quantity", ""));
                multipleItemarray.add(jsonMultipleItem);

                if (multipleItemarray != null)
                    mapitem.put("itemid", multipleItemarray);

                if (Utils.isInternetOn(context)) {
                    progreessbar.setVisibility(View.VISIBLE);
                    btnContinue.setEnabled(false);
                    payUsingWalletCall = mservice.getFetcherService(context).getWalletPayMultipleitem(map, mapitem);
                    payUsingWalletCall.enqueue(new Callback<FinalProcessResponse>() {
                        @Override
                        public void onResponse(Call<FinalProcessResponse> call, Response<FinalProcessResponse> response) {
                            progreessbar.setVisibility(View.GONE);
                            btnContinue.setEnabled(true);
                            if (response.code() == StaticConstant.RESULT_OK) {
                                if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                                    preferences.saveAmount(String.valueOf(Double.parseDouble(preferences.getAmount())
                                            - Double.parseDouble(txt_total_cost_value.getText().toString())));
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
                            btnContinue.setEnabled(true);
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
        Log.d(TAG, "payWithPayPal: ");
        Map<String, String> map = new HashMap<>();
        map.put("wallet_amount", preferences.getAmount());
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("productqty", txt_quntity.getText().toString());
        map.put("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
        map.put("uid", preferences.getUserId());
        map.put("email_id", tv_payement_email.getText().toString());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            btnContinue.setEnabled(false);
            processBitcoinCall = mservice.getFetcherService(context).processPayPal(map);
            processBitcoinCall.enqueue(new Callback<ProcessStripePaymentResponse>() {
                @Override
                public void onResponse(Call<ProcessStripePaymentResponse> call, Response<ProcessStripePaymentResponse> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            paypalsourceId = response.body().getResult().getPayPalSourceId();
                            if (response.body().getResult().getApproveLink() != null) {
                                Intent intent = new Intent(NewConfigurePaymentSettingActivity.this, PayPalVerificationActivity.class);
                                intent.putExtra("verificationLink", response.body().getResult().getApproveLink());
                                intent.putExtra("paypalsourceid", paypalsourceId);
                                startActivityForResult(intent, 1000);
                            } else {
                                Toast.makeText(context, "not getting paypal verification link", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            progreessbar.setVisibility(View.GONE);
                            btnContinue.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);
                        btnContinue.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ProcessStripePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void finalizePayPalPaymentProcess(String payPalSourceId, String payerId) {
        Log.d(TAG, "finalizePayPalPaymentProcess: ");
        Map<String, String> map = new HashMap<>();
        map.put("paypal_source_id", payPalSourceId);
        map.put("payer_id", payerId);

        if (Utils.isInternetOn(context)) {
            finalprocessBitcoinCall = mservice.getFetcherService(context).finalProcessPayPal(map);
            finalprocessBitcoinCall.enqueue(new Callback<FinalProcessResponse>() {
                @Override
                public void onResponse(Call<FinalProcessResponse> call, Response<FinalProcessResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
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
                    btnContinue.setEnabled(true);
                }
            });

        } else {
            progreessbar.setVisibility(View.GONE);
            btnContinue.setEnabled(true);
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void payWithAliPay() {
        Log.d(TAG, "payWithAliPay: ");
        Map<String, String> map = new HashMap<>();
        map.put("wallet_amount", preferences.getAmount());
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("productqty", txt_quntity.getText().toString());
        map.put("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
        map.put("uid", "" + preferences.getUserId());
        map.put("email_id", tv_payement_email.getText().toString());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            btnContinue.setEnabled(false);
            processAlipayCall = mservice.getFetcherService(context).processAlipay(map);
            processAlipayCall.enqueue(new Callback<ProcessStripePaymentResponse>() {
                @Override
                public void onResponse(Call<ProcessStripePaymentResponse> call, Response<ProcessStripePaymentResponse> response) {
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equalsIgnoreCase(StaticConstant.STATUS_1)) {
                            finalizeAlipayPaymentProcess(response.body().getResult().getStripeSourceId());
                        } else {
                            progreessbar.setVisibility(View.GONE);
                            btnContinue.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                            ;
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);
                        btnContinue.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ProcessStripePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void finalizeAlipayPaymentProcess(String stripeSourceId) {
        Log.d(TAG, "finalizeAlipayPaymentProcess: ");
        Map<String, String> map = new HashMap<>();
        map.put("stripe_source_id", stripeSourceId);

        if (Utils.isInternetOn(context)) {
            finalProcessAlipayCall = mservice.getFetcherService(context).finalProcessApipay(map);
            finalProcessAlipayCall.enqueue(new Callback<FinalProcessResponse>() {
                @Override
                public void onResponse(Call<FinalProcessResponse> call, Response<FinalProcessResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
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
                    btnContinue.setEnabled(true);
                }
            });

        } else {
            progreessbar.setVisibility(View.GONE);
            btnContinue.setEnabled(true);
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
            Number totalCostValueStr = format.parse(txt_total_cost_value.getText().toString());
            if (Double.parseDouble(preferences.getAmount()) < totalCostValueStr.doubleValue()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("2KXO")
                        .setMessage(getString(R.string.msg_less_amount_in_wallet))
                        .setPositiveButton(getString(R.string.txt_add_money), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(NewConfigurePaymentSettingActivity.this, TopUpActivity.class), REQ_CODE_TOP_UP_ACTIVITY_RESULTS);
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
        map.put("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
        map.put("uid", "" + preferences.getUserId());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            btnContinue.setEnabled(false);
            boostUsingWalletCall = mservice.getFetcherService(context).finalBoostProcess(map);
            boostUsingWalletCall.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
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
                    btnContinue.setEnabled(true);
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
        map.put("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
        map.put("boost_type", BundleUtils.getIntentExtra(getIntent(), "boost_type", ""));

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            btnContinue.setEnabled(false);
            boostUsingCardcall = mservice.getFetcherService(context).stripeProcessBoost(map);
            boostUsingCardcall.enqueue(new Callback<StripeProcessBoostResponse>() {
                @Override
                public void onResponse(Call<StripeProcessBoostResponse> call, Response<StripeProcessBoostResponse> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            stripeFinalBoostProcessUsingCard(response.body().getResult().getStripePaymentId());
                        } else {
                            progreessbar.setVisibility(View.GONE);
                            btnContinue.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeProcessBoostResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
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
        map.put("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
        map.put("boost_type", BundleUtils.getIntentExtra(getIntent(), "boost_type", ""));

        if (Utils.isInternetOn(context)) {
            boostUsingCardcall = mservice.getFetcherService(context).stripeFinalBoostProcess(map);
            boostUsingCardcall.enqueue(new Callback<StripeProcessBoostResponse>() {
                @Override
                public void onResponse(Call<StripeProcessBoostResponse> call, Response<StripeProcessBoostResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
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
                    btnContinue.setEnabled(true);
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
        map.put("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
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
            btnContinue.setEnabled(false);
            processBitcoinBoostCall = mservice.getFetcherService(context).processPayPalBoost(map);
            processBitcoinBoostCall.enqueue(new Callback<ProcessStripePaymentResponse>() {
                @Override
                public void onResponse(Call<ProcessStripePaymentResponse> call, Response<ProcessStripePaymentResponse> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (response.body().getResult().getApproveLink() != null) {
                                Intent intent = new Intent(NewConfigurePaymentSettingActivity.this, PayPalVerificationActivity.class);
                                intent.putExtra("verificationLink", response.body().getResult().getApproveLink());
                                intent.putExtra("paypalsourceid", paypalsourceId);
                                startActivityForResult(intent, 1001);
                            } else {
                                Toast.makeText(context, "not getting paypal verification link", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            progreessbar.setVisibility(View.GONE);
                            btnContinue.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);
                        btnContinue.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ProcessStripePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void finalProcessPayPalBoost(String paypalSourceId, String payerId) {
        Log.d(TAG, "finalProcessPayPalBoost: ");

        Map<String, String> map = new HashMap<>();
        map.put("wallet_amount", preferences.getAmount());
        map.put("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
        map.put("uid", preferences.getUserId());
        map.put("email_id", tv_payement_email.getText().toString());
        map.put("type", "OUT");
        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "price", ""));
        map.put("boost_type", BundleUtils.getIntentExtra(getIntent(), "boost_type", ""));
        map.put("shipping", tv_shipping_address.getText().toString());
        map.put("paypal_response_id", paypalSourceId);
        map.put("payer_id", payerId);
        map.put("no_days", BundleUtils.getIntentExtra(getIntent(), "day", ""));
        map.put("no_users", BundleUtils.getIntentExtra(getIntent(), "reach", ""));

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            btnContinue.setEnabled(false);
            finalProcessBitcoinBoostCall = mservice.getFetcherService(context).finalProcessPayPalBoost(map);
            finalProcessBitcoinBoostCall.enqueue(new Callback<ProcessStripePaymentResponse>() {
                @Override
                public void onResponse(Call<ProcessStripePaymentResponse> call, Response<ProcessStripePaymentResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            onBoostSuccess();
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);
                        btnContinue.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ProcessStripePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void processAlipayBoost() {
        Log.d(TAG, "processAlipayBoost: ");
        Map<String, String> map = new HashMap<>();
        map.put("wallet_amount", preferences.getAmount());
        map.put("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
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
            btnContinue.setEnabled(false);
            processAlipayBoostCall = mservice.getFetcherService(context).processAlipayBoost(map);
            processAlipayBoostCall.enqueue(new Callback<ProcessStripePaymentResponse>() {
                @Override
                public void onResponse(Call<ProcessStripePaymentResponse> call, Response<ProcessStripePaymentResponse> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            finalProcessAlipayBoost(response.body().getResult().getStripeSourceId());
                        } else {
                            progreessbar.setVisibility(View.GONE);
                            btnContinue.setEnabled(true);
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);
                        btnContinue.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ProcessStripePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void finalProcessAlipayBoost(String stripeSourceId) {
        Log.d(TAG, "finalProcessBitcoinBoost: ");

        Map<String, String> map = new HashMap<>();
        map.put("wallet_amount", preferences.getAmount());
        map.put("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
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
            btnContinue.setEnabled(false);
            finalProcessAlipayBoostCall = mservice.getFetcherService(context).finalProcessAlipayBoost(map);
            finalProcessAlipayBoostCall.enqueue(new Callback<ProcessStripePaymentResponse>() {
                @Override
                public void onResponse(Call<ProcessStripePaymentResponse> call, Response<ProcessStripePaymentResponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            onBoostSuccess();
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    } else {
                        progreessbar.setVisibility(View.GONE);
                        btnContinue.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ProcessStripePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void onBoostSuccess() {
        Intent gotoCOngrats = new Intent(NewConfigurePaymentSettingActivity.this, BoostCongActivity.class);
        gotoCOngrats.putExtra("image", BundleUtils.getIntentExtra(getIntent(), "item_image", null));
        gotoCOngrats.putExtra("name", name);
        startActivity(gotoCOngrats);
        finish();
    }

    /*GET SHIPPING ADDRESS*/
    private void callGetAddressApi() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());

        if (Utils.isInternetOn(context)) {
            btnContinue.setEnabled(false);
            progreessbar.setVisibility(View.VISIBLE);
            getAddressCall = mservice.getFetcherService(context).getAddress();
            getAddressCall.enqueue(new Callback<AddressBean>() {
                @Override
                public void onResponse(Call<AddressBean> call, Response<AddressBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        ShippingAddress shippingAddress = response.body().getResult().getShippingAddress();
                        if (shippingAddress != null)
                            tv_shipping_address.setText(shippingAddress.getAddress());
                    } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                        Utils.unAuthentication(context);
                    }

                }

                @Override
                public void onFailure(Call<AddressBean> call, Throwable t) {
                    t.printStackTrace();
                    btnContinue.setEnabled(true);
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
//            HashMap<String, ArrayList<File>> fileArray = new HashMap<String, ArrayList<File>>();

            File file = StaticConstant.imageFiles_new[0];
//            fileArray.put("media", files);

            RequestBody propertyImage = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part propertyImagePart = MultipartBody.Part.createFormData("PropertyImage",
                    file.getName(), propertyImage);

            MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[4];

            for (int index = 0; index < 4; index++) {
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
                surveyImagesParts[index] = MultipartBody.Part.createFormData("media", file.getName(), surveyBody);
            }

            RequestBodyConveter requestbodyconverter = new RequestBodyConveter();
            progreessbar.setVisibility(View.VISIBLE);
            btnContinue.setEnabled(false);
            createitembeanCall = mservice.getFetcherService(context).cretaeItemMulti(requestbodyconverter.converRequestBodyFromMap(map),
                    surveyImagesParts);

            createitembeanCall.enqueue(new Callback<SellItemBean>() {
                @Override
                public void onResponse(Call<SellItemBean> call, Response<SellItemBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    btnContinue.setEnabled(true);

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
                    btnContinue.setEnabled(true);
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
    public void commonMethod(String quantity, File... files) {
        txt_quntity.setText(quantity);
        double totalCost = (Double.parseDouble(BundleUtils.getIntentExtra(getIntent(), "item_cost", "0").replace("$", "")))
                + Double.parseDouble(BundleUtils.getIntentExtra(getIntent(), "item_shipping_cost", "0").replace("$", ""));

        txt_total_cost_value.setText(String.valueOf(totalCost * Integer.parseInt(txt_quntity.getText().toString())));
    }

    @Override
    public void onBackStackChanged() {
        Log.d(TAG, "onBackStackChanged: ");
        setUpToolbar();
    }

    @Override
    public void onPaymentGatewaySelected(String paymentProcessorName, String email) {
        Log.d(TAG, "onPaymentGatewaySelected paymentProcessorName: " + paymentProcessorName);
        tv_selected_payment.setVisibility(View.GONE);
        ll_payment_gateway_details_container.setVisibility(View.VISIBLE);
        ll_card_details_container.setVisibility(View.GONE);

        tv_payement_name.setText(paymentProcessorName);
        tv_payement_email.setText(email);
        this.cardId = null;
    }

    @Override
    public void onCardSelected(String cardNumber, String expDate, String brand, String cardId) {
        Log.d(TAG, "onCardSelected: ");
        tv_selected_payment.setVisibility(View.GONE);
        ll_payment_gateway_details_container.setVisibility(View.GONE);
        ll_card_details_container.setVisibility(View.VISIBLE);

        tv_card_details.setText(cardNumber + " " + expDate);
        tv_card_brand.setText(brand);
        this.cardId = cardId;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
