package com.screamxo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.AboutUsActivity;
import com.screamxo.Activity.ChangePasswordActivity;
import com.screamxo.Activity.CommonLoginSignUpActivity;
import com.screamxo.Activity.NewConfigurePaymentSettingActivity;
import com.screamxo.Activity.RahulWork.NewConfigurePaymentActivity;
import com.screamxo.Activity.RahulWork.PrivacySettingActivity;
import com.screamxo.Activity.RahulWork.PushSettingActivity;
import com.screamxo.Activity.RahulWork.RejectSettingActivity;
import com.screamxo.Activity.SignupActivity;
import com.screamxo.Activity.TermsAndUseActivity;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_ABOUT_US_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CHANGE_PASSWORD_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CONFIGURE_PAYMENT_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_PRIVACY_SETTING_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_PUSH_SETTING_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_REJECT_SETTING_ACTIVITY_RESULTS;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SettingFragment";

    Call<LoginBean> logoutcall;
    @BindView(R.id.txt_logout)
    TextView txtLogout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    Map<String, String> map;
    @BindView(R.id.rl_push)
    RelativeLayout rl_push;
    @BindView(R.id.rl_reject)
    RelativeLayout rl_reject;
    @BindView(R.id.rl_setting)
    RelativeLayout rl_setting;
    @BindView(R.id.rl_payment)
    RelativeLayout rl_payment;
    @BindView(R.id.rl_help)
    RelativeLayout rl_help;
    @BindView(R.id.rl_password)
    RelativeLayout rl_password;
    @BindView(R.id.about_us_container)
    RelativeLayout about_us_container;
    @BindView(R.id.rl_Logout)
    RelativeLayout rl_Logout;
    @BindView(R.id.live_support_container)
    RelativeLayout live_support_container;

    private FetchrServiceBase mService;
    private Context context;
    private Preferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_activity, container, false);
        ButterKnife.bind(this, view);
        init();
        initcontrollistener();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        preferences = new Preferences(context);
    }

    public void init() {
        mService = new FetchrServiceBase();
        map = new HashMap<>();
    }

    public void initcontrollistener() {
//        txtLogout.setOnClickListener(this);
    }

    void setViewEnableDisable(Boolean enable) {
        txtLogout.setEnabled(enable);
    }

    @OnClick({
            R.id.rl_push, R.id.rl_reject, R.id.rl_setting, R.id.rl_payment, R.id.rl_help, R.id.rl_password, R.id.about_us_container, R.id.rl_Logout
            , R.id.txt_notification_setting, R.id.txt_reject, R.id.txt_privacy_setting, R.id.txt_payment, R.id.txt_password, R.id.txt_about_us, R.id.txt_help, R.id.txt_logout
            , R.id.imgArrow, R.id.imgArrow1, R.id.imgArrow2, R.id.imgArrow3, R.id.imgArrow5, R.id.imgArrow6, R.id.imgArrow4,
            R.id.imageView9, R.id.txt_live_support, R.id.live_support_container
    })
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        switch (view.getId()) {
            case R.id.rl_push:
            case R.id.txt_notification_setting:
            case R.id.imgArrow:
                startActivityForResult(new Intent(getActivity(), PushSettingActivity.class), REQ_CODE_PUSH_SETTING_ACTIVITY_RESULTS);
                break;
            case R.id.rl_reject:
            case R.id.txt_reject:
            case R.id.imgArrow1:
                startActivityForResult(new Intent(getActivity(), RejectSettingActivity.class), REQ_CODE_REJECT_SETTING_ACTIVITY_RESULTS);
                break;
            case R.id.rl_setting:
            case R.id.txt_privacy_setting:
            case R.id.imgArrow2:
                startActivityForResult(new Intent(getActivity(), PrivacySettingActivity.class), REQ_CODE_PRIVACY_SETTING_ACTIVITY_RESULTS);
                break;
            case R.id.rl_payment:
            case R.id.txt_payment:
            case R.id.imgArrow3:
                Intent i = new Intent(context, NewConfigurePaymentSettingActivity.class);
                i.putExtra("screen", SettingFragment.class.getSimpleName());
                ((Activity) context).startActivityForResult(i, REQ_CODE_CONFIGURE_PAYMENT_ACTIVITY_RESULTS);
                break;
            case R.id.rl_password:
            case R.id.txt_password:
            case R.id.imgArrow5:
                startActivityForResult(new Intent(context, ChangePasswordActivity.class), REQ_CODE_CHANGE_PASSWORD_ACTIVITY_RESULTS);
                break;
            case R.id.about_us_container:
            case R.id.txt_about_us:
            case R.id.imgArrow6:
                startActivityForResult(new Intent(context, AboutUsActivity.class), REQ_CODE_ABOUT_US_ACTIVITY_RESULTS);
                break;
            case R.id.rl_help:
            case R.id.txt_help:
            case R.id.imgArrow4:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://api.screamxo.com/pages/load/privacypolicy"));
                startActivity(browserIntent);
                break;
            case R.id.rl_Logout:
            case R.id.txt_logout:
            case R.id.imageView9:
                showLogOutDialog();
                break;
            case R.id.txt_live_support:
            case R.id.live_support_container:
                Intent intent = new Intent(getActivity(), TermsAndUseActivity.class);
                intent.putExtra("screenType", "LiveSupport");
                startActivity(intent);
                break;
        }
    }

    private void showLogOutDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.txt_want_to_logout)
                .setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Utils.isInternetOn(context)) {
                            setViewEnableDisable(false);
                            progressBar.setVisibility(View.VISIBLE);
                            logoutcall = mService.getFetcherService(context).Logout();

                            logoutcall.enqueue(new Callback<LoginBean>() {
                                @Override
                                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                                    setViewEnableDisable(true);
                                    progressBar.setVisibility(View.GONE);
                                    if (response.code() == StaticConstant.RESULT_OK) {
                                        Utils.showToast(context, response.body().getMsg());

//                                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                                        preferences.clearAllApreferences();

                                        Intent intent = new Intent(context, CommonLoginSignUpActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
//                                        ((Activity) context).finish();
//                                        } else {
//                                            Utils.showToast(context, response.body().getMsg());
//                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoginBean> call, Throwable t) {
                                    setViewEnableDisable(true);
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                        } else {
                            Utils.showToast(context, context.getString(R.string.toast_no_internet));
                        }
                    }
                })
                .setNegativeButton(getString(R.string.txt_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface ar) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.show();
    }
}


