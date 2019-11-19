package com.screamxo.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.apimodule.ApiBase.ApiBean.Payment.StripeToken;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Fragment.SelectPaymentFragment;
import com.screamxo.Interface.DialogInterfaceAction;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectPaymentMethodActivity extends AppCompatActivity implements SelectPaymentFragment.OnPaymentSelectedListener{

    @BindView(R.id.img_toolbar_left_icon)
    ImageView img_toolbar_left_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment_method);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        init();
    }

    private void init() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, SelectPaymentFragment.newInstance());
        fragmentTransaction.addToBackStack(SelectPaymentFragment.class.getSimpleName());
        fragmentTransaction.commit();
        img_toolbar_left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onCardSelected(String cardNumber, String expDate, String brand, String cardId) {
        Intent intent = new Intent();
        intent.putExtra("cardId",cardId);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onPaymentGatewaySelected(String paymentProcessorName, String email) {

    }

    /*@Override
    public void onCardSelected(String cardNumber, String expDate, String brand, String cardId) {

        Map<String, String> map = new HashMap<>();
        map.put("uid", new Preferences(this).getUserId());
        map.put("card_id", cardId);
        map.put("customer_id", new Preferences(this).getStripeCustomerId());
        map.put("type", "IN");
        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "amount", ""));

        if (Utils.isInternetOn(this)) {
            Call<StripeToken> topUsingCard = new FetchrServiceBase().getFetcherService(this).stripeProcessPayment(map);
            topUsingCard.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        finalizeTopUpUsingCard(response.body().getResult().getStripePaymentId(),cardId);
                    } else if (response.code() == StaticConstant.BAD_REQUEST) {
                        DialogBox.showDialog(SelectPaymentMethodActivity.this,getString(R.string.app_name), "Something go wrong!", DialogBox.DIALOG_FAILURE, null);
                    } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                        Utils.unAuthentication(SelectPaymentMethodActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(this,getString(R.string.toast_no_internet));
        }
    }

    private void finalizeTopUpUsingCard(String stripePaymentId,String cardId) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", new Preferences(this).getUserId());
        map.put("card_id", cardId);
        map.put("customer_id", new Preferences(this).getStripeCustomerId());
        map.put("type", "IN");
        map.put("stripe_id", stripePaymentId);
        map.put("amount", BundleUtils.getIntentExtra(getIntent(), "amount", ""));

        if (Utils.isInternetOn(this)) {
            Call<StripeToken> finalizeTopUpUsingCardCall = new FetchrServiceBase().getFetcherService(this).addAmountToWallet(map);
            finalizeTopUpUsingCardCall.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
//                            Utils.showToast(context, response.body().getMsg());
                            DialogBox.showDialog(SelectPaymentMethodActivity.this,getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_SUCESS, new DialogInterfaceAction() {
                                @Override
                                public void dialogAction() {
                                    finish();
                                }
                            });
                            new Preferences(SelectPaymentMethodActivity.this).saveAmount(String.valueOf(response.body().getResult().getWalletBalance()));

                        } else {
                            DialogBox.showDialog(SelectPaymentMethodActivity.this,getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(this,getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void onPaymentGatewaySelected(String paymentProcessorName, String email) {

    }*/

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
