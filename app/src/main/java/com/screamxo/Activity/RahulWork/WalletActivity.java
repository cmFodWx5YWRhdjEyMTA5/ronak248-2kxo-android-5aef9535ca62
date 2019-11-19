package com.screamxo.Activity.RahulWork;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.WalletList.WalletList;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Adapter.WalletAdapter;
import com.screamxo.Others.MyPopUpWindow;
import com.screamxo.R;
import com.screamxo.Utils.HidingScrollListener;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_SUPPORT_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_TOP_UP_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "WalletActivity";

    @BindView(R.id.txtWalletAmount)
    TextView txtWalletAmount;
    @BindView(R.id.txtJan)
    TextView txtJan;
    @BindView(R.id.txtFeb)
    TextView txtFeb;
    @BindView(R.id.txtMar)
    TextView txtMar;
    @BindView(R.id.txtApr)
    TextView txtApr;
    @BindView(R.id.txtMay)
    TextView txtMay;
    @BindView(R.id.txtJun)
    TextView txtJun;
    @BindView(R.id.txtJul)
    TextView txtJul;
    @BindView(R.id.txtAug)
    TextView txtAug;
    @BindView(R.id.txtSep)
    TextView txtSep;
    @BindView(R.id.txtOct)
    TextView txtOct;
    @BindView(R.id.txtNov)
    TextView txtNov;
    @BindView(R.id.txtDec)
    TextView txtDec;

    @BindView(R.id.view_jan)
    View view_jan;
    @BindView(R.id.view_feb)
    View view_feb;
    @BindView(R.id.view_mar)
    View view_mar;
    @BindView(R.id.view_apr)
    View view_apr;
    @BindView(R.id.view_May)
    View view_May;
    @BindView(R.id.view_Jun)
    View view_Jun;
    @BindView(R.id.view_Jul)
    View view_Jul;
    @BindView(R.id.view_Sep)
    View view_Sep;
    @BindView(R.id.view_Aug)
    View view_Aug;
    @BindView(R.id.view_Oct)
    View view_Oct;
    @BindView(R.id.view_Nov)
    View view_Nov;
    @BindView(R.id.view_Dec)
    View view_Dec;

    @BindView(R.id.rc_Month)
    RecyclerView rcMonth;
    Context context;
    WalletAdapter walletAdapter;
    ArrayList<Object> walletList;
    Preferences preferences;
    @BindView(R.id.progreessbar)
    ProgressBar progressBar;

    @BindView(R.id.imgMenu)
    ImageView imgMenu;
    @BindView(R.id.btnTopUp)
    Button btnTopUp;
    @BindView(R.id.hs_months)
    HorizontalScrollView hsMonths;
    private FetchrServiceBase mService;
    Call<WalletList> rejectcall;
    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    public static final int REQ_CODE_TRANSACTION_HISTORY = 2002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        initView();
        initFabIcon();
    }

    private void initView() {
        preferences = new Preferences(WalletActivity.this);
        walletList = new ArrayList<>();
        context = WalletActivity.this;
        mService = new FetchrServiceBase();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcMonth.setLayoutManager(linearLayoutManager);

        rcMonth.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                if (!floatingButton.isMenuOpen()) {
                    floatingButton.setBackground(getResources().getDrawable(R.drawable.ic_float_menu_up));
                }
            }

            @Override
            public void onShow() {
                if (!floatingButton.isMenuOpen()) {
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
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
                public void onMenuOpened(FloatingMenuButton floatingMenuButton) {
                    Log.d(TAG, "onMenuOpened: ");
                    Utils.hideKeyboard(WalletActivity.this);
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.cross));
                }

                @Override
                public void onMenuClosed(FloatingMenuButton floatingMenuButton) {
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                }
            });

            floatingButton.setOnClickBtnListener(new FloatingMenuButton.OnClickBtnListener() {
                @Override
                public void onClickBtn() {

                    if (!floatingButton.isMenuOpen()
                            && floatingButton.getBackground().getConstantState() != null
                            && floatingButton.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ic_float_menu_up).getConstantState())) {
                        rcMonth.scrollToPosition(0);
                        rcMonth.setOnClickListener(null);
                        floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
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
                    Intent gotoNext = new Intent(WalletActivity.this, UploadDataActivity.class);
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

    @Override
    protected void onResume() {
        if (Utils.getFormattedPrice(preferences.getAmount()) == null) {
            txtWalletAmount.setVisibility(View.GONE);
        } else {
            txtWalletAmount.setText(Utils.getFormattedPrice(preferences.getAmount()));
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int month = calendar.get(Calendar.MONTH) + 1;
        Log.i("walletactivity", "........month....." + String.valueOf(month));
        colorMonth(month);
        initApi(String.valueOf(month));
        super.onResume();
    }

    public void resetTextViewUi() {
        txtJan.setTextColor(getResources().getColor(R.color.colorGray));
        txtJan.setTypeface(null, Typeface.NORMAL);
//        txtJan.setPaintFlags(txtJan.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        view_jan.setVisibility(View.INVISIBLE);

        txtFeb.setTextColor(getResources().getColor(R.color.colorGray));
        txtFeb.setTypeface(null, Typeface.NORMAL);
//        txtFeb.setPaintFlags(txtFeb.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        view_feb.setVisibility(View.INVISIBLE);

        txtMar.setTextColor(getResources().getColor(R.color.colorGray));
        txtMar.setTypeface(null, Typeface.NORMAL);
//        txtMar.setPaintFlags(txtMar.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        view_mar.setVisibility(View.INVISIBLE);

        txtApr.setTextColor(getResources().getColor(R.color.colorGray));
        txtApr.setTypeface(null, Typeface.NORMAL);
//        txtApr.setPaintFlags(txtApr.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        view_apr.setVisibility(View.INVISIBLE);

        txtMay.setTextColor(getResources().getColor(R.color.colorGray));
        txtMay.setTypeface(null, Typeface.NORMAL);
//        txtMay.setPaintFlags(txtMay.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        view_May.setVisibility(View.INVISIBLE);

        txtJun.setTextColor(getResources().getColor(R.color.colorGray));
        txtJun.setTypeface(null, Typeface.NORMAL);
//        txtJun.setPaintFlags(txtJun.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        view_Jun.setVisibility(View.INVISIBLE);

        txtJul.setTextColor(getResources().getColor(R.color.colorGray));
        txtJul.setTypeface(null, Typeface.NORMAL);
//        txtJul.setPaintFlags(txtJul.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        view_Jul.setVisibility(View.INVISIBLE);

        txtAug.setTextColor(getResources().getColor(R.color.colorGray));
        txtAug.setTypeface(null, Typeface.NORMAL);
//        txtAug.setPaintFlags(txtAug.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        view_Aug.setVisibility(View.INVISIBLE);

        txtSep.setTextColor(getResources().getColor(R.color.colorGray));
        txtSep.setTypeface(null, Typeface.NORMAL);
//        txtSep.setPaintFlags(txtSep.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        view_Sep.setVisibility(View.INVISIBLE);

        txtOct.setTextColor(getResources().getColor(R.color.colorGray));
        txtOct.setTypeface(null, Typeface.NORMAL);
//        txtOct.setPaintFlags(txtOct.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        view_Oct.setVisibility(View.INVISIBLE);

        txtNov.setTextColor(getResources().getColor(R.color.colorGray));
        txtNov.setTypeface(null, Typeface.NORMAL);
//        txtNov.setPaintFlags(txtNov.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        view_Nov.setVisibility(View.INVISIBLE);

        txtDec.setTextColor(getResources().getColor(R.color.colorGray));
        txtDec.setTypeface(null, Typeface.NORMAL);
//        txtDec.setPaintFlags(txtDec.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        view_Dec.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("PrivateResource")
    public void colorMonth(int month) {
        hsMonths.postDelayed(new Runnable() {
            public void run() {
                hsMonths.fullScroll(View.FOCUS_RIGHT);
            }
        }, 100L);
        TextView selectedTv = null;
        View underlineView = null;
        switch (month) {
            case 1:
                selectedTv = txtJan;
                underlineView = view_jan;
                break;
            case 2:
                selectedTv = txtFeb;
                underlineView = view_feb;
                break;
            case 3:
                selectedTv = txtMar;
                underlineView = view_mar;
                break;
            case 4:
                selectedTv = txtApr;
                underlineView = view_apr;
                break;
            case 5:
                selectedTv = txtMay;
                underlineView = view_May;
                break;
            case 6:
                selectedTv = txtJun;
                underlineView = view_Jun;
                break;
            case 7:
                selectedTv = txtJul;
                underlineView = view_Jul;
                break;
            case 8:
                selectedTv = txtAug;
                underlineView = view_Aug;
                break;
            case 9:
                selectedTv = txtSep;
                underlineView = view_Sep;
                break;
            case 10:
                selectedTv = txtOct;
                underlineView = view_Oct;
                break;
            case 11:
                selectedTv = txtNov;
                underlineView = view_Nov;
                break;
            case 12:
                selectedTv = txtDec;
                underlineView = view_Dec;
                break;
        }

        if (selectedTv != null) {
            resetTextViewUi();
            selectedTv.setTextColor(getResources().getColor(R.color.tw__blue_default));
            selectedTv.setTypeface(null, Typeface.BOLD);
            underlineView.setVisibility(View.VISIBLE);
//            hsMonths.postDelayed(new Runnable() {
//                public void run() {
//                    hsMonths.smoothScrollTo(1,month-1);
//                }
//            }, 100L);
            //            selectedTv.setPaintFlags(selectedTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

    }

    private void setAdapter() {
        if (walletAdapter == null) {
            walletAdapter = new WalletAdapter(context, walletList);
            rcMonth.setAdapter(walletAdapter);
        } else {
            walletAdapter.notifyDataSetChanged();
        }

    }

    @SuppressLint("PrivateResource")
    @OnClick({R.id.btnTopUp, R.id.imgMenu, R.id.txtJan, R.id.txtFeb, R.id.txtMar, R.id.txtApr, R.id.txtMay, R.id.txtJun, R.id.txtJul, R.id.txtAug, R.id.txtSep, R.id.txtOct, R.id.txtNov, R.id.txtDec})
    @Override
    public void onClick(View view) {
        TextView selectedTv = null;
        View underlineView = null;
        switch (view.getId()) {
            case R.id.txtJan:
                selectedTv = txtJan;
                underlineView = view_jan;
                initApi("1");
                break;
            case R.id.txtFeb:
                selectedTv = txtFeb;
                underlineView = view_feb;
                initApi("2");
                break;
            case R.id.txtMar:
                selectedTv = txtMar;
                underlineView = view_mar;
                initApi("3");
                break;
            case R.id.txtApr:
                selectedTv = txtApr;
                underlineView = view_apr;
                initApi("4");
                break;
            case R.id.txtMay:
                selectedTv = txtMay;
                underlineView = view_May;
                initApi("5");
                break;
            case R.id.txtJun:
                selectedTv = txtJun;
                underlineView = view_Jun;
                initApi("6");
                break;
            case R.id.txtJul:
                selectedTv = txtJul;
                underlineView = view_Jul;
                initApi("7");
                break;
            case R.id.txtAug:
                selectedTv = txtAug;
                underlineView = view_Aug;
                initApi("8");
                break;
            case R.id.txtSep:
                selectedTv = txtSep;
                underlineView = view_Sep;
                initApi("9");
                break;
            case R.id.txtOct:
                selectedTv = txtOct;
                underlineView = view_Oct;
                initApi("10");
                break;
            case R.id.txtNov:
                selectedTv = txtNov;
                underlineView = view_Nov;
                initApi("11");
                break;
            case R.id.txtDec:
                selectedTv = txtDec;
                underlineView = view_Dec;
                initApi("12");
                break;

            case R.id.imgMenu:
                openPopUpmenu(view, imgMenu);
                break;
            case R.id.btnTopUp:
                startActivityForResult(new Intent(WalletActivity.this, TopUpActivity.class), REQ_CODE_TOP_UP_ACTIVITY_RESULTS);
                break;
        }
        if (selectedTv != null) {
            resetTextViewUi();
            selectedTv.setTextColor(getResources().getColor(R.color.tw__blue_default));
            selectedTv.setTypeface(null, Typeface.BOLD);
//            selectedTv.setPaintFlags(selectedTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            underlineView.setVisibility(View.VISIBLE);
        }
    }

    public void openPopUpmenu(View view, ImageView img_filter) {
        MyPopUpWindow popUpWindow = new MyPopUpWindow(context, view, new String[]{getString(R.string.txt_send_money),
                getString(R.string.txt_contact_support)}, img_filter, "wallet");
        popUpWindow.show(img_filter, MyPopUpWindow.PopUpPosition.RIGHT);

        popUpWindow.setOnPopupItemClickListner(new MyPopUpWindow.OnPopupItemClickListener() {
            @Override
            public boolean onPopupItemClick(int position) {
                switch (position) {
                    case 0:
                        startActivityForResult(new Intent(WalletActivity.this, SendMoneyActivity.class), REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS);
                        break;
                    case 1:
                        startActivityForResult(new Intent(WalletActivity.this, SupportActivity.class), REQ_CODE_SUPPORT_ACTIVITY_RESULTS);
                        break;
                }
                popUpWindow.dismiss();
                return true;
            }
        });
    }

    public void initApi(String data) {
        Map<String, String> map = new HashMap<>();
//        map.put("user_id", preferences.getUserId());
        map.put("month", "" + data);

        if (Utils.isInternetOn(context)) {

            progressBar.setVisibility(View.VISIBLE);
            rejectcall = mService.getFetcherService(context).walletList(map);

            rejectcall.enqueue(new Callback<WalletList>() {
                @Override
                public void onResponse(Call<WalletList> call, Response<WalletList> response) {
                    //   setViewEnableDisable(true);
                    progressBar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        preferences.saveAmount(response.body().getPage_flag());

                        walletList.clear();
                        walletList.addAll(response.body().getResult());
                        setAdapter();
                        if (Utils.getFormattedPrice(response.body().getPage_flag()) == null) {
                            txtWalletAmount.setVisibility(View.GONE);
                        } else {
                            txtWalletAmount.setText(Utils.getFormattedPrice(response.body().getPage_flag()));
                        }
                    } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                        Utils.unAuthentication(context);
                    }
                }

                @Override
                public void onFailure(Call<WalletList> call, Throwable t) {
                    t.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
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
                //noinspection UnnecessaryReturnStatement
                return;
            }
        }
    }
}
