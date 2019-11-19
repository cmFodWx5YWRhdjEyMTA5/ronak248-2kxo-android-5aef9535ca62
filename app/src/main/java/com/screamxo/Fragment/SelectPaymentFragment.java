package com.screamxo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.CardData;
import com.example.apimodule.ApiBase.ApiBean.GetAccountBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.RahulWork.CardInformationActivity;
import com.screamxo.Activity.RahulWork.TopUpActivity;
import com.screamxo.Activity.SelectPaymentMethodActivity;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by parangat on 26/10/17.
 */

public class SelectPaymentFragment extends Fragment {

    public interface OnPaymentSelectedListener {
        void onCardSelected(String cardNumber, String expDate, String brand, String cardId);
        void onPaymentGatewaySelected(String paymentProcessorName, String email);
    }

    @SuppressWarnings("unused")
    private static final String TAG = "SelectPaymentFragment";
    public static final int RC_ADD_CARD = 101;

    @BindView(R.id.sv_container)
    ScrollView sv_container;

    @BindView(R.id.tv_add_new_card)
    TextView tv_add_new_card;
    @BindView(R.id.tv_payment_processor)
    TextView tv_payment_processor;

    @BindView(R.id.ll_saved_card)
    LinearLayout ll_saved_card;

    @BindView(R.id.ll_saved_payment_details)
    LinearLayout ll_saved_payment_details;

    @BindView(R.id.ll_paypal_container)
    RelativeLayout ll_paypal_container;

    @BindView(R.id.ll_bitcoin_container)
    LinearLayout ll_bitcoin_container;

    @BindView(R.id.ll_alipay_container)
    LinearLayout ll_alipay_container;

    @BindView(R.id.ll_wechat_container)
    LinearLayout ll_wechat_container;

    @BindView(R.id.tv_paypal_email)
    TextView tv_paypal_email;
    @BindView(R.id.tv_bitcoin_email)
    TextView tv_bitcoin_email;
    @BindView(R.id.tv_alipay_email)
    TextView tv_alipay_email;
    @BindView(R.id.tv_wechat_email)
    TextView tv_wechat_email;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.tv_xocash)
    TextView tv_xocash;
    @BindView(R.id.ll_xo_cash_container)
    RelativeLayout ll_xo_cash_container;

    private Preferences preferences;
    private Context context;
    private FetchrServiceBase mservice;
    private OnPaymentSelectedListener onPaymentSelectedListener;
    private Call<GetAccountBean> getaccountbeancall;

    public static SelectPaymentFragment newInstance() {
        Bundle args = new Bundle();
        SelectPaymentFragment fragment = new SelectPaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mservice = new FetchrServiceBase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_select, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ll_xo_cash_container.setVisibility(View.VISIBLE);
        if (Utils.getFormattedPrice(preferences.getAmount()) == null)
        {
            tv_xocash.setVisibility(View.GONE);
        }
        else
            {
            tv_xocash.setText(Utils.getFormattedPrice(preferences.getAmount()));
        }
        ll_paypal_container.setVisibility(View.GONE);
        ll_bitcoin_container.setVisibility(View.GONE);
        ll_alipay_container.setVisibility(View.GONE);
        ll_wechat_container.setVisibility(View.GONE);
        CallGetAccountApi();
    }

    public void CallGetAccountApi() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("customer_id", preferences.getStripeCustomerId());

        if (Utils.isInternetOn(context))
        {
            progreessbar.setVisibility(View.VISIBLE);

            getaccountbeancall = mservice.getFetcherService(context).getCardList(map);
            getaccountbeancall.enqueue(new Callback<GetAccountBean>() {
                @Override
                public void onResponse(Call<GetAccountBean> call, Response<GetAccountBean> response) {
                    progreessbar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK)
                    {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1))
                        {
                            ll_saved_card.setVisibility(response.body().getResult().hasCard() ? View.VISIBLE : View.GONE);
                            ll_saved_card.removeAllViews();
                            for (CardData cardData : response.body().getResult().getCardList())
                            {
                                if (cardData.getIsCard().equalsIgnoreCase("yes"))
                                {
                                    View itemCardView = LayoutInflater.from(context).inflate(R.layout.item_card, ll_saved_card, false);
                                    ImageView iv_card_image = itemCardView.findViewById(R.id.iv_card_image);
                                    TextView tv_card_number = itemCardView.findViewById(R.id.tv_card_number);
                                    TextView tv_exp_mon_year = itemCardView.findViewById(R.id.tv_exp_mon_year);
                                    TextView tv_address = itemCardView.findViewById(R.id.tv_address);
                                    TextView tv_card_name = itemCardView.findViewById(R.id.txt_card_name);

                                    switch (cardData.getBrand().toLowerCase())
                                    {
                                        case "visa":
                                            iv_card_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_visa));
                                            tv_card_name.setText(R.string.txt_us_visa);
                                            break;

                                        case "jcb":
                                            iv_card_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_jcb));
                                            tv_card_name.setText(R.string.txt_jcb);
                                            break;

                                        case "diners club":
                                            iv_card_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_diners));
                                            tv_card_name.setText(R.string.txt_diners_club);
                                            break;

                                        case "mastercard":
                                            iv_card_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_mastercard));
                                            tv_card_name.setText(R.string.txt_master_card);
                                            break;

                                    }

                                    tv_card_number.setText(String.format("****%s", cardData.getLast4()));
                                    tv_exp_mon_year.setText(String.format("%s/%s", cardData.getExpMonth(), cardData.getExpYear()));
                                    tv_address.setText(cardData.getAddress());
                                    itemCardView.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            onPaymentSelectedListener.onCardSelected(tv_card_number.getText().toString(), tv_exp_mon_year.getText().toString(), cardData.getBrand(), cardData.getId());
                                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                            fragmentManager.popBackStack(SelectPaymentFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                        }
                                    });
                                    ll_saved_card.addView(itemCardView);
                                }
                            }

                            ll_paypal_container.setVisibility(TextUtils.isEmpty(response.body().getResult().getPaypal()) ? View.GONE : View.VISIBLE);
                            tv_paypal_email.setText(response.body().getResult().getPaypal());

                            ll_paypal_container.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    onPaymentSelectedListener.onPaymentGatewaySelected("PayPal", response.body().getResult().getPaypal());
                                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                    fragmentManager.popBackStack(SelectPaymentFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                }
                            });

                            ll_xo_cash_container.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    onPaymentSelectedListener.onPaymentGatewaySelected("XOCASH", "");
                                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                    fragmentManager.popBackStack(SelectPaymentFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                }
                            });

                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetAccountBean> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        try {
            this.onPaymentSelectedListener = (OnPaymentSelectedListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initControlValue();
    }

    private void initControlValue() {
        tv_payment_processor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_container, SelectProcessorFragment.newInstance());
                fragmentTransaction.addToBackStack(SelectProcessorFragment.class.getSimpleName());
                fragmentTransaction.commit();
            }
        });

        tv_add_new_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoNext = new Intent(context, CardInformationActivity.class);
                gotoNext.putExtra("screen", SelectPaymentFragment.class.getSimpleName());
                startActivityForResult(gotoNext, RC_ADD_CARD);
            }
        });
    }

    private void init() {
        preferences = new Preferences(context);
        if (BundleUtils.getIntentExtra(((Activity) context).getIntent(), "screen", "")
                .equalsIgnoreCase(TopUpActivity.class.getSimpleName()) || BundleUtils.getIntentExtra(((Activity) context).getIntent(), "screen", "")
                .equalsIgnoreCase(SettingFragment.class.getSimpleName())) {
            tv_payment_processor.setVisibility(View.GONE);
            ll_saved_payment_details.setVisibility(View.GONE);
            ll_xo_cash_container.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getaccountbeancall != null) {
            getaccountbeancall.cancel();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.printIntentData(data);
    }
}
