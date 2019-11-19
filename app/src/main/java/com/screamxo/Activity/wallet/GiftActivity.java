package com.screamxo.Activity.wallet;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.apimodule.ApiBase.ApiBean.WithdrawBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.hbb20.CountryCodePicker;
import com.screamxo.Interface.DialogInterfaceAction;
import com.screamxo.R;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.baseClass.BaseActivity;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.btn_exchange)
    Button btn_exchange;

    @BindView(R.id.txt_country)
    CountryCodePicker txt_country;

    @BindView(R.id.txt_email)
    EditText txt_email;

    @BindView(R.id.txt_account_number)
    EditText txt_account_number;

    @BindView(R.id.txt_account_name)
    EditText txt_account_name;

    @BindView(R.id.txt_routing_number)
    EditText txt_routing_number;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    private String moneyToTranser;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_gift;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        img_back.setOnClickListener(view -> finish());
        btn_exchange.setOnClickListener(view -> callExchangeMoneyApi());

        if (getIntent() != null && Objects.requireNonNull(getIntent().getExtras()).containsKey("money")) {
            moneyToTranser = getIntent().getExtras().getString("money");
        }
    }

    private void callExchangeMoneyApi()
    {

        if (isValidation()) {
            Map<String, String> map = new HashMap<>();
            map.put("country", txt_country.getSelectedCountryNameCode());
            map.put("account_holder_name", txt_account_name.getText().toString());
            map.put("routing_number", txt_routing_number.getText().toString());
            map.put("account_number", txt_account_number.getText().toString());
            map.put("email", txt_email.getText().toString());
            map.put("amount", moneyToTranser);

            Call<WithdrawBean> withdrawBeanCall = new FetchrServiceBase().getFetcherService(context).withdrawAmount(map);
            if (Utils.isInternetOn(context))
            {
                progress_bar.setVisibility(View.VISIBLE);
                withdrawBeanCall.enqueue(new Callback<WithdrawBean>()
                {
                    @Override
                    public void onResponse(Call<WithdrawBean> call, Response<WithdrawBean> response)
                    {
                        progress_bar.setVisibility(View.GONE);
                        if (response.code() == StaticConstant.RESULT_OK)
                        {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.msg_amount_transfered_sucess), DialogBox.DIALOG_SUCESS, new DialogInterfaceAction() {
                                        @Override
                                        public void dialogAction()
                                        {
                                            finish();
                                        }
                                    });
                        }
                        else if (response.code() == StaticConstant.BAD_REQUEST)
                        {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.message(), DialogBox.DIALOG_SUCESS, new DialogInterfaceAction() {
                                @Override
                                public void dialogAction() {
                                }
                            });
                        }
                        else if (response.code() == StaticConstant.UNAUTHORIZE)
                        {
                            Utils.unAuthentication(context);
                        }
                    }
                    @Override
                    public void onFailure(Call<WithdrawBean> call, Throwable t)
                    {
                        progress_bar.setVisibility(View.GONE);
                        t.printStackTrace();
                        Utils.showToast(context, t.toString());
                    }
                });
            } else {
                Utils.showToast(context, context.getString(R.string.toast_no_internet));
            }
        }
    }

    private boolean isValidation()
    {
        if (TextUtils.isEmpty(txt_account_name.getText().toString())) {
            Toast.makeText(context, getString(R.string.msg_enter_account_name), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(txt_account_number.getText().toString())) {
            Toast.makeText(context, getString(R.string.msg_enter_account_number), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(txt_email.getText().toString())) {
            Toast.makeText(context, getString(R.string.msg_enter_email), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(txt_routing_number.getText().toString())) {
            Toast.makeText(context, getString(R.string.msg_enter_routing_no), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
