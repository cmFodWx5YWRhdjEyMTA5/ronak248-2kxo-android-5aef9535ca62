package com.screamxo.Activity.wallet;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.Payment.StripeToken;
import com.example.apimodule.ApiBase.ApiBean.WalletList.WalletList;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.example.apimodule.ApiBase.WalletOrderPayment.WalletBalanceBean;
import com.screamxo.Activity.RahulWork.FriendsActivity;
import com.screamxo.Activity.RahulWork.SupportActivity;
import com.screamxo.Activity.RahulWork.TopUpActivity;
import com.screamxo.Activity.RahulWork.UploadDataActivity;
import com.screamxo.Activity.SelectPaymentMethodActivity;
import com.screamxo.Adapter.HomeViewPagerAdapter;
import com.screamxo.Fragment.wallet.WalletSendFragment;
import com.screamxo.Interface.DialogInterfaceAction;
import com.screamxo.Interface.SendWallet;
import com.screamxo.Others.MyPopUpWindow;
import com.screamxo.R;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_FRIENDS_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_SUPPORT_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;

public class WalletSendReceiveActivity extends AppCompatActivity implements View.OnClickListener, SendWallet {

    public Context context;
    @BindView(R.id.imgMenu)
    ImageView imgMenu;
    @BindView(R.id.fl_header)
    FrameLayout flHeader;
    @BindView(R.id.txtWalletAmount)
    TextView txtWalletAmount;
    @BindView(R.id.txtWalletCoin)
    TextView txtWalletCoin;
    @BindView(R.id.btnTopUp)
    Button btnTopUp;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    Preferences preferences;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.txt_send_amount)
    TextView txtSendAmount;
    @BindView(R.id.txt_other_amount)
    TextView txtOtherAmount;
    @BindView(R.id.indicator)
    CircleIndicator indicator;

    Call<WalletList> rejectcall;
    CommonFragment commonFragment;
    WalletSendFragment walletSendFragment;
    WalletSendFragment walletSendFragment1;
    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private FetchrServiceBase mService;
    private String uid;
    private HomeViewPagerAdapter adapter;
    private Call<WalletBalanceBean> walletBalanceCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_wallet_send_recieve);
        ButterKnife.bind(this);
        preferences = new Preferences(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorNewBlue));
        }
        mService = new FetchrServiceBase();
        initFabIcon();
        setImagePager();
        setListener();
        callGetWalletBalanceApi();

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("notification")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setHistory();
                    }
                }, 2000);
            } else if (getIntent().getExtras().containsKey("from")) {
                if (getIntent().getExtras().get("from").equals("profile")) {
                    viewPager.setCurrentItem(1);
                }
            }
        }


    }

    private void setHistory() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Date d = new Date();
        tabs.getTabAt(0).setText(getString(R.string.txt_history));
        commonFragment.callHistry(String.valueOf(sdf.format(d)));
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
                    Utils.hideKeyboard(WalletSendReceiveActivity.this);
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
                    if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                        Intent gotoNext = new Intent(WalletSendReceiveActivity.this, UploadDataActivity.class);
                        startActivityForResult(gotoNext, REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS);
                    }
                    finish();
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
            e.printStackTrace();
        }
    }

    private void callGetWalletBalanceApi() {
        if (Utils.isInternetOn(context)) {
            walletBalanceCall = new FetchrServiceBase().getFetcherService(WalletSendReceiveActivity.this).getWalletBalance();
            walletBalanceCall.enqueue(new Callback<WalletBalanceBean>() {
                @Override
                public void onResponse(Call<WalletBalanceBean> call, Response<WalletBalanceBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        preferences.saveAmount(String.valueOf(response.body().getResult().get(0).getWalletAmount()));
                        try {
                            String s = (Utils.getFormattedPrice(response.body().getResult().get(0).getWalletAmount()));
                            if (TextUtils.isEmpty(s))
                                s = "$ 00";
                            txtWalletAmount.setText(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<WalletBalanceBean> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void setListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        btnTopUp.setText(getString(R.string.txt_cap_send));
                        break;
                    case 2:
                        btnTopUp.setText(getString(R.string.txt_receive));
                        break;
                    case 0:
                        btnTopUp.setText(getString(R.string.txt_cap_topup));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        txtWalletAmount.setOnClickListener(view -> {
            Intent intent = new Intent(WalletSendReceiveActivity.this, GiftStepOneActivity.class);
            intent.putExtra("money", txtSendAmount.getText().toString() + "." + txtOtherAmount.getText().toString());
            startActivity(intent);
        });
    }

    private void setImagePager() {

        adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        commonFragment = new CommonFragment();
        walletSendFragment = new WalletSendFragment();
        walletSendFragment1 = new WalletSendFragment();
        commonFragment.setInterface(this::getAmount);
        walletSendFragment.setInterface(this::getAmount);
        walletSendFragment1.setInterface(this::getAmount);
        adapter.addFragment(commonFragment, getString(R.string.txt_cap_topup));
        adapter.addFragment(walletSendFragment, getString(R.string.txt_cap_send));
        adapter.addFragment(walletSendFragment1, getString(R.string.txt_receive));
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        indicator.setViewPager(viewPager);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int month = calendar.get(Calendar.MONTH) + 1;
        try {
            String s = (Utils.getFormattedPrice(preferences.getAmount()));
            if (TextUtils.isEmpty(s))
                s = "$ 00";
            txtWalletAmount.setText(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewPager.setOffscreenPageLimit(3);

    }

    public void initApi(String data) {
        Map<String, String> map = new HashMap<>();
        map.put("month", "" + data);

        if (Utils.isInternetOn(context)) {
            rejectcall = mService.getFetcherService(context).walletList(map);

            rejectcall.enqueue(new Callback<WalletList>() {
                @Override
                public void onResponse(Call<WalletList> call, Response<WalletList> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {

                        if (Utils.getFormattedPrice(response.body().getPage_flag()) == null) {
                            txtWalletAmount.setVisibility(View.GONE);
                        }
                    } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                        Utils.unAuthentication(context);
                    }
                }

                @Override
                public void onFailure(Call<WalletList> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @SuppressLint("PrivateResource")
    @OnClick({R.id.btnTopUp, R.id.imgMenu, R.id.fl_header, R.id.txtWalletAmount,
            R.id.txtWalletCoin, R.id.tabs, R.id.view_pager, R.id.sbSocial,
            R.id.subFriend, R.id.sbChat, R.id.sbSearch, R.id.sbflHome,
            R.id.sbProfile, R.id.sbflSetting, R.id.sbWorld, R.id.my_floating_button})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imgMenu:
                openPopUpmenu(view, imgMenu);
                break;
            case R.id.btnTopUp:
                topUpClick();
                break;
            case R.id.fl_header:
                break;
            case R.id.txtWalletAmount:
                break;
            case R.id.txtWalletCoin:
                break;
            case R.id.tabs:
                break;
            case R.id.view_pager:
                break;
            case R.id.sbSocial:
                break;
            case R.id.subFriend:
                break;
            case R.id.sbChat:
                break;
            case R.id.sbSearch:
                break;
            case R.id.sbflHome:
                break;
            case R.id.sbProfile:
                break;
            case R.id.sbflSetting:
                break;
            case R.id.sbWorld:
                break;
            case R.id.my_floating_button:
                break;
        }
    }

    private void topUpClick() {

        switch (viewPager.getCurrentItem()) {
            case 1:
                if (txtSendAmount.getText().toString().equals("00")) {
                    Toast.makeText(context, getString(R.string.msg_enter_money_to_send), Toast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(txtSendAmount.getText().toString() + "." + txtOtherAmount.getText().toString()) < 1.0) {
                    Toast.makeText(context, getString(R.string.txt_min_amount_should_1), Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(txtSendAmount.getText().toString()) > Double.parseDouble(preferences.getAmount())) {
                    Toast.makeText(context, getString(R.string.txt_dont_have_enough_amoun), Toast.LENGTH_LONG).show();
                } else {

                    if (getIntent().getExtras() == null) {
                        Intent gotoFriends = new Intent(this, FriendsActivity.class);
                        gotoFriends.putExtra("screen", "sendmoney");
                        gotoFriends.putExtra("amount", txtSendAmount.getText().toString() + "." + txtOtherAmount.getText().toString());
                        startActivityForResult(gotoFriends, REQ_CODE_FRIENDS_ACTIVITY_RESULTS);
                    } else {
                        uid = getIntent().getStringExtra("uid");
                        sendMoneyApi();
                    }
                }
                break;
            case 2:
                Toast.makeText(context, "Coming soon!", Toast.LENGTH_LONG).show();
                break;
            case 0:
                if (!tabs.getTabAt(0).getText().toString().equalsIgnoreCase("TOP UP")) {
                    tabs.getTabAt(0).setText(getString(R.string.txt_cap_topup));
                    commonFragment.setSendfrag();
                } else {

                    if (txtSendAmount.getText().toString().equals("00")) {
                        Toast.makeText(context, getString(R.string.msg_enter_money_to_topup), Toast.LENGTH_SHORT).show();
                    } else if (Double.parseDouble(txtSendAmount.getText().toString() + "." + txtOtherAmount.getText().toString()) < 1.0) {
                        Toast.makeText(context, getString(R.string.txt_min_amount_should_1), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(this, SelectPaymentMethodActivity.class);
                        i.putExtra("screen", TopUpActivity.class.getSimpleName());
                        i.putExtra("amount", txtSendAmount.getText().toString() + "." + txtOtherAmount.getText().toString());
                        startActivityForResult(i, 5000);
                    }
                }
                break;
        }
    }

    private void finalizeTopUpUsingCard(String stripePaymentId, String cardId) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("card_id", cardId);
        map.put("customer_id", preferences.getStripeCustomerId());
        map.put("type", "IN");
        map.put("stripe_id", stripePaymentId);
        map.put("amount", txtSendAmount.getText().toString() + "." + txtOtherAmount.getText().toString());

        if (Utils.isInternetOn(context)) {
            FetchrServiceBase mservice = new FetchrServiceBase();

            Call<StripeToken> finalizeTopUpUsingCardCall = mservice.getFetcherService(context).addAmountToWallet(map);
            finalizeTopUpUsingCardCall.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_SUCESS,
                                    new DialogInterfaceAction() {
                                        @Override
                                        public void dialogAction() {

                                        }
                                    });
                            preferences.saveAmount(String.valueOf(response.body().getResult().getWalletBalance()));
                            try {
                                String s = (Utils.getFormattedPrice(response.body().getResult().getWalletBalance()));
                                if (TextUtils.isEmpty(s))
                                    s = "$ 00";
                                txtWalletAmount.setText(s);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void topUpUsingCard(String cardId) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("card_id", cardId);
        map.put("customer_id", preferences.getStripeCustomerId());
        map.put("type", "IN");
        map.put("amount", txtSendAmount.getText().toString() + "." + txtOtherAmount.getText().toString());

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            FetchrServiceBase mservice = new FetchrServiceBase();
            Call<StripeToken> topUsingCard = mservice.getFetcherService(context).stripeProcessPayment(map);
            topUsingCard.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        finalizeTopUpUsingCard(response.body().getResult().getStripePaymentId(), cardId);
                    } else if (response.code() == StaticConstant.BAD_REQUEST) {
                        DialogBox.showDialog(WalletSendReceiveActivity.this, getString(R.string.app_name), "Something go wrong!",
                                DialogBox.DIALOG_FAILURE, null);
                    } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                        Utils.unAuthentication(WalletSendReceiveActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void openPopUpmenu(View view, ImageView img_filter) {

        String[] strings = {getString(R.string.txt_contact_support)};
        String[] string1 = {getString(R.string.txt_history), getString(R.string.txt_contact_support)};
        MyPopUpWindow popUpWindow;
        if (viewPager.getCurrentItem() == 0) {
            popUpWindow = new MyPopUpWindow(context, view, string1, img_filter, "wallet");
        } else
            popUpWindow = new MyPopUpWindow(context, view, strings, img_filter, "wallet");
        popUpWindow.show(img_filter, MyPopUpWindow.PopUpPosition.RIGHT);

        popUpWindow.setOnPopupItemClickListner(new MyPopUpWindow.OnPopupItemClickListener() {
            @Override
            public boolean onPopupItemClick(int position) {
                switch (position) {
                    case 1:
                        startActivityForResult(new Intent(WalletSendReceiveActivity.this, SupportActivity.class), REQ_CODE_SUPPORT_ACTIVITY_RESULTS);
                        break;
                    case 0:
                        if (viewPager.getCurrentItem() == 0) {
                            FragmentManager fm = getFragmentManager();
                            MonthFragment dialogFragment = new MonthFragment(new MonthSelection() {
                                @Override
                                public void setMonth(int month) {
                                    if (commonFragment != null) {
                                        tabs.getTabAt(0).setText(getString(R.string.txt_history));
                                        commonFragment.callHistry(String.valueOf(month));
                                    }
                                }
                            });
                            dialogFragment.show(fm, "Month");
                        } else {
                            startActivityForResult(new Intent(WalletSendReceiveActivity.this, SupportActivity.class), REQ_CODE_SUPPORT_ACTIVITY_RESULTS);
                        }
                        break;
                }
                popUpWindow.dismiss();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_FRIENDS_ACTIVITY_RESULTS:
                    if (data.getExtras() != null && data.getStringExtra("uid") != null) {
                        uid = data.getStringExtra("uid");
                        sendMoneyApi();
                    }
                    break;
                case 5000:
                    if (data != null && data.getExtras().containsKey("cardId")) {
                        String cardId = data.getExtras().getString("cardId");
                        topUpUsingCard(cardId);
                    }
                    break;
            }
        }
    }

    private void sendMoneyApi() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", preferences.getUserId());
        map.put("to_user_id", "" + uid);
        map.put("amount", txtSendAmount.getText().toString() + "." + txtOtherAmount.getText().toString());

        if (Utils.isInternetOn(context)) {

            Call<StripeToken> sendMoney = mService.getFetcherService(context).sendMoney(map);

            sendMoney.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    //   setViewEnableDisable(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            preferences.saveAmount(String.valueOf(response.body().getResult().getAmount()));
                            try {
                                String s = (Utils.getFormattedPrice(preferences.getAmount()));
                                if (TextUtils.isEmpty(s))
                                    s = "$ 00";
                                txtWalletAmount.setText(s);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            txtSendAmount.setText("00");
                            txtOtherAmount.setText("00");
                            walletSendFragment.reSetValue();
                            walletSendFragment1.reSetValue();
                            DialogBox.showDialog(context, context.getString(R.string.app_name),
                                    getString(R.string.msg_money_send_sucess), DialogBox.DIALOG_SUCESS, null);
                            preferences.saveAmount(String.valueOf(response.body().getResult().getAmount()));
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void getAmount(String amount) {

        String[] strings = amount.split(" ");
        if (strings.length > 1) {
            txtSendAmount.setText(strings[0]);
            String s = strings[1];
            if (strings[1].length() > 2) {
                s = String.valueOf(strings[1].charAt(0)) + String.valueOf(strings[1].charAt(1));
            }
            txtOtherAmount.setText(s);
        } else {
            txtSendAmount.setText(amount.trim());
            txtOtherAmount.setText("00");
        }
    }

    @Override
    protected void onResume() {
        callGetWalletBalanceApi();
        super.onResume();
    }
}
