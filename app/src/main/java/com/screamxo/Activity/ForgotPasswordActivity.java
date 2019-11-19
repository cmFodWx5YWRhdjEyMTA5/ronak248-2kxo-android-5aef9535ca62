package com.screamxo.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Interface.DialogInterfaceAction;
import com.screamxo.R;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.img_toolbar_left_icon)
    TextView imgToolbarLeftIcon;
    @BindView(R.id.edt_email)
    EditText medtEmail;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.btn_send_email)
    TextView mbtbSendEmail;

    String email;
    Context context;
    FetchrServiceBase mService;
    Call<LoginBean> forgetBeanCall;
    DialogInterfaceAction dialogInterfaceAction;
    private Validations mValidations;
    private  int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        init();
        initControl();
    }

    public void init() {
        context = this;
        mService = new FetchrServiceBase();
        mValidations = new Validations();
    }

    public void initControl() {
    }

    @OnClick({R.id.img_toolbar_left_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_toolbar_left_icon:
                onBackPressed();
                break;
        }
    }

    public void onBackPressed() {
        finish();
    }

    public void BtnsendEmailClick(View view) {

        if (isValidation()) {
            callForgetPasswordApi();
        }
    }

    private void callForgetPasswordApi() {
        Map<String, String> map = new HashMap<>();
        map.put("email", email);

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            mbtbSendEmail.setEnabled(false);
            forgetBeanCall = mService.getFetcherService(context).ForgetPassword(map);
            forgetBeanCall.enqueue(new Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    mbtbSendEmail.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {

                        if (response.body().getStatus().equalsIgnoreCase(StaticConstant.STATUS_1)) {
                            type = DialogBox.DIALOG_SUCESS;
                        } else {
                            type = DialogBox.DIALOG_FAILURE;
                        }

                        DialogBox.showDialog(context, context.getString(R.string.app_name),response.body().getMsg(), type, new DialogInterfaceAction() {
                            @Override
                            public void dialogAction() {
                                switch (type) {
                                    case DialogBox.DIALOG_SUCESS:
                                        onBackPressed();
                                        break;
                                }
                            }
                        });
                    }else {
                        try {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), new JSONObject(response.errorBody().string()).getString("message"), type, new DialogInterfaceAction() {
                                @Override
                                public void dialogAction() {
                                    switch (type) {
                                        case DialogBox.DIALOG_SUCESS:
                                            onBackPressed();
                                            break;
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    mbtbSendEmail.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public boolean isValidation() {
        if (mValidations.checkEmail(medtEmail.getText().toString())) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_email_invalid), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (medtEmail.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_email_null), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else {
            email = medtEmail.getText().toString();
            return true;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

