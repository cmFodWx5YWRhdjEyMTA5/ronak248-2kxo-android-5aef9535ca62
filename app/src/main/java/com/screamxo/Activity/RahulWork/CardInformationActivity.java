package com.screamxo.Activity.RahulWork;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.Payment.StripeToken;
import com.example.apimodule.ApiBase.ApiBean.Stripe.AddCardReponse;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Fragment.SelectPaymentFragment;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CardInformationActivity extends AppCompatActivity {
    @BindView(R.id.txtCancel)
    TextView txtCancel;
    @BindView(R.id.txtDone)
    TextView txtDone;
    @BindView(R.id.rl_Email)
    RelativeLayout rlEmail;
    @BindView(R.id.card_input_widget)
    CardInputWidget cardInputWidget;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    Preferences preferences;
    Context context;
    Call<StripeToken> tokenCall;
    Call<StripeToken> addAmountToWallet;
    Call<AddCardReponse> addCardCall;
    private FetchrServiceBase mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_information);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        context = CardInformationActivity.this;
        mService = new FetchrServiceBase();
        preferences = new Preferences(context);
    }

    private void initPayment() {
        Card cardToSave = cardInputWidget.getCard();
        if (cardToSave == null) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.ttx_invalid_card_details), DialogBox.DIALOG_FAILURE, null);
            return;
        }

        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(SelectPaymentFragment.class.getSimpleName())) {
            addCardCall(cardToSave);
        }
    }

    private void addCardCall(Card cardToSave) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("customer_id", preferences.getStripeCustomerId());
        map.put("number", cardToSave.getNumber());
        map.put("exp_month", String.valueOf(cardToSave.getExpMonth()));
        map.put("exp_year", String.valueOf(cardToSave.getExpYear()));
        map.put("cvc", String.valueOf(cardToSave.getCVC()));

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            addCardCall = mService.getFetcherService(context).stripeAddCard(map);
            addCardCall.enqueue(new Callback<AddCardReponse>() {
                @Override
                public void onResponse(Call<AddCardReponse> call, Response<AddCardReponse> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            Utils.showToast(context, response.body().getMsg());
                            finish();
                        } else if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddCardReponse> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                }
            });
        }
    }

    public void funDone(View view) {
        initPayment();
    }

    public void funCancel(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {

        if (tokenCall != null) {
            tokenCall.cancel();
        }
        if (addAmountToWallet != null) {
            addAmountToWallet.cancel();
        }
        if (addCardCall != null) {
            addCardCall.cancel();
        }

        super.onDestroy();

    }
}
