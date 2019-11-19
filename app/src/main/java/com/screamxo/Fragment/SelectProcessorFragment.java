package com.screamxo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.GetAccountBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.R;
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
public class SelectProcessorFragment extends Fragment implements DialogBox.OnPaymentProcessorEmailAddedListener {

    @SuppressWarnings("unused")
    private static final String TAG = "SelectProcessorFragment";
    @BindView(R.id.tv_paypal)
    TextView tv_paypal;
   /* @BindView(R.id.tv_alipay)
    TextView tv_alipay;
    @BindView(R.id.tv_wechat)
    TextView tv_wechat;
*/
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;

    private Preferences preferences;
    private Context context;
    private Call<GetAccountBean> updateAccountApiCall;
    private FetchrServiceBase mservice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mservice = new FetchrServiceBase();
    }

    public static SelectProcessorFragment newInstance() {
        Bundle args = new Bundle();
        SelectProcessorFragment fragment = new SelectProcessorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_processor, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initControlValue();
    }

    private void initControlValue() {
        tv_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBox.pickPaymentProcessorEmailDialog(getContext(), getString(R.string.msg_enter_paypal_id), "paypalacc", SelectProcessorFragment.this);
            }
        });

       /* tv_alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBox.pickPaymentProcessorEmailDialog(getContext(), "Please enter your Alipay Email id", "alipayacc", SelectProcessorFragment.this);
            }
        });*/

        /*tv_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBox.pickPaymentProcessorEmailDialog(getContext(), "Please enter your WeChat Email id", "wechatacc", SelectProcessorFragment.this);
            }
        });*/
    }

    private void init() {
        preferences = new Preferences(getContext());
    }

    @Override
    public void onSubmit(String paymentProcessorType, String email) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), getString(R.string.msg_enter_valid_email), Toast.LENGTH_SHORT).show();
            return;
        }

        callUpdateAccountApi(paymentProcessorType, email);

    }

    private void callUpdateAccountApi(String paymentProcessorType, String email) {
        Map<String, String> map = new HashMap<>();
       // map.put("uid", preferences.getUserId());
        map.put(paymentProcessorType, email);

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);

            updateAccountApiCall = mservice.getFetcherService(context).updateAccount(map);
            updateAccountApiCall.enqueue(new Callback<GetAccountBean>() {
                @Override
                public void onResponse(Call<GetAccountBean> call, Response<GetAccountBean> response) {
                    progreessbar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            fragmentManager.popBackStack(SelectProcessorFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetAccountBean> call, Throwable t) {    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateAccountApiCall != null) {
            updateAccountApiCall.cancel();
        }
    }
}
