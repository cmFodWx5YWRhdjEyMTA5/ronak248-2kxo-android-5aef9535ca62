package com.screamxo.Activity.RahulWork;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.screamxo.Adapter.BoostAdapter;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_SUPPORT_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;

public class BoostActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BoostActivity";

    @BindView(R.id.rc_Days)
    RecyclerView rc_Days;
    @BindView(R.id.rc_Price)
    RecyclerView rc_Price;
    @BindView(R.id.btnTopUp)
    Button btnTopUp;

    @BindView(R.id.txtDays)
    TextView txtDays;
    @BindView(R.id.txtPrice)
    TextView txtPrice;
    @BindView(R.id.txtReachData)
    TextView txtReachData;
    @BindView(R.id.imgBoast)
    ImageView imgBoast;
    Context context;
    ArrayList<String> daysList, priceList;
    int priceVal = 0, dayVal = 0;
    String price = "20", day = "5";
    String itemid = "", image = "", reach = "1200", item_name = "";

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost);
        ButterKnife.bind(this);
        initData();
        initAdapter();
        initFabIcon();
    }

    private void initData() {
        if (getIntent().getExtras() != null) {
            itemid = getIntent().getExtras().getString("itemid");
            item_name = getIntent().getExtras().getString("item_name");
            image = getIntent().getExtras().getString("image");
        }

        if (!TextUtils.isEmpty(image)) {
            Picasso.with(this)
                    .load(image)
                    .error(R.color.colorbackgroundgray)
                    .placeholder(R.color.colorbackgroundgray)
                    .into(imgBoast);
        }

        context = BoostActivity.this;
        daysList = new ArrayList<>();

        daysList.add("");
        for (int i = 1; i <= 30; i = i + 1) {
            daysList.add(String.valueOf(i));
        }
        daysList.add("");

        priceList = new ArrayList<>();
        priceList.add("");
        for (int i = 5; i <= 95; i = i + 5) {
            priceList.add(String.valueOf(i));
        }

        for (int i = 100; i <= 950; i = i + 50) {
            priceList.add(String.valueOf(i));
        }

        for (int i = 1000; i <= 9000; i = i + 1000) {
            priceList.add(String.valueOf(i));
        }
        priceList.add("More");
        priceList.add("");
    }

    public void onRecyclerClicked(int screenType, String data) {
        if (screenType == StaticConstant.BOOST_TYPE_DAYS) {
            txtDays.setFocusable(true);
            txtDays.setClickable(true);
        } else {
            txtPrice.setFocusable(true);
            txtPrice.setClickable(true);
        }

        recyclerCLicked(screenType, data);
    }

    public void recyclerCLicked(int screenType, String data) {

        if (screenType == StaticConstant.BOOST_TYPE_DAYS) {
            // rl_Days.setBackground(getResources().getDrawable(R.drawable.transparentblacklayout));
            //txtDays.setTextColor(Color.BLACK);

            txtDays.setText(String.format("%s DAYS", data));
            day = data;
            rc_Days.setVisibility(View.GONE);
            dayVal = 0;
        } else {
            // rl_Price.setBackground(getResources().getDrawable(R.drawable.transparentblacklayout));
            //txtPrice.setTextColor(Color.BLACK);

            if (data.equalsIgnoreCase("More")) {
                Intent gotoNext = new Intent(BoostActivity.this, SupportActivity.class);
                gotoNext.putExtra("screen", "boost");
                startActivityForResult(gotoNext, REQ_CODE_SUPPORT_ACTIVITY_RESULTS);
            } else {
                try {
                    txtPrice.setText(String.format("$%s", data));
                    rc_Price.setVisibility(View.GONE);
                    int reach = Integer.parseInt(data) * 60;
                    try {
                        txtReachData.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(reach));
                    } catch (Exception e) {
                        txtReachData.setText(String.valueOf(reach));
                    }
                    this.reach = String.valueOf(reach);
                    price = data;
                    priceVal = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initAdapter() {
        rc_Days.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));
        rc_Days.setAdapter(new BoostAdapter(context, daysList, StaticConstant.BOOST_TYPE_DAYS));
        rc_Days.smoothScrollToPosition(5);

        rc_Price.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));
        rc_Price.setAdapter(new BoostAdapter(context, priceList, StaticConstant.BOOST_TYPE_PRICE));


        rc_Price.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                priceVal = lastVisible - 1;
            }
        });

        rc_Days.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                dayVal = lastVisible - 1;
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
                    Utils.hideKeyboard(BoostActivity.this);
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
                    Intent gotoNext = new Intent(BoostActivity.this, UploadDataActivity.class);
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
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
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

    @OnClick({R.id.txtPrice, R.id.txtDays, R.id.btnTopUp})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtPrice:
                Log.d(TAG, "onClick: " + priceVal);
                txtPrice.setFocusable(false);
                txtPrice.setClickable(false);

                rc_Price.setVisibility(View.VISIBLE);
                txtPrice.setText("");
                if (priceVal != 0) {
                    recyclerCLicked(StaticConstant.BOOST_TYPE_PRICE, priceList.get(priceVal));
                }
                //  rl_Price.setBackground(getResources().getDrawable(R.drawable.transparentpinklayout));
                break;

            case R.id.txtDays:
                Log.d(TAG, "onClick: " + dayVal);
                txtDays.setFocusable(false);
                txtDays.setClickable(false);

                rc_Days.setVisibility(View.VISIBLE);
                txtDays.setText("");
                if (dayVal != 0) {
                    recyclerCLicked(StaticConstant.BOOST_TYPE_DAYS, daysList.get(dayVal));
                }
                //  rl_Days.setBackground(getResources().getDrawable(R.drawable.transparentpinklayout));
                break;

            case R.id.btnTopUp:
                Intent gotoNext = new Intent(context, NewConfigurePaymentActivity.class);
                gotoNext.putExtra("price", price);
                gotoNext.putExtra("day", day);
                gotoNext.putExtra("itemid", itemid);
                gotoNext.putExtra("reach", reach);
                gotoNext.putExtra("item_image", image);
                gotoNext.putExtra("boost_url", getIntent().getExtras().getString("boost_url"));
                gotoNext.putExtra("screen", "BoostActivity");

                if (getIntent().getExtras().getString("videourl") != null)
                    gotoNext.putExtra("videourl", getIntent().getExtras().getString("videourl"));

                if (getIntent().getExtras().getString("item_name") != null)
                    gotoNext.putExtra("item_name", getIntent().getExtras().getString("item_name"));

                gotoNext.putExtra("boost_type", BundleUtils.getIntentExtra(getIntent(), "boost_type", ""));
                startActivity(gotoNext);
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
}
