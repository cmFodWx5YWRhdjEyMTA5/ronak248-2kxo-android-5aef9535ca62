package com.screamxo.Activity.RahulWork;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.PostFrdBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.BuildConfig;
import com.screamxo.Others.ContactList;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuessShareActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GuessShareActivity";
    @BindView(R.id.btnShare)
    Button btnShare;
    Context context;
    String contectEmailJsonStrting = "";
    @BindView(R.id.progreessbar_main)
    ProgressBar progreessbarMain;
    Preferences preferences;
    Call<PostFrdBean> postFrdBeanCall;
    private HashMap<String, String> map;
    private FetchrServiceBase mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_share);
        ButterKnife.bind(this);
        initView();
        initPermission(0);
//        Log.d(TAG, "onCreate Music Service component: "+MusicPlayerService.class.getName());
//        Intent intent = new Intent(this, MusicPlayerService.class);
//        startService(intent);
    }

    private void initView() {
        context = GuessShareActivity.this;
        preferences = new Preferences(context);
        mService = new FetchrServiceBase();
        map = new HashMap<>();
    }

    public void initPermission(int val) {
        RxPermissions.getInstance(context)
                .requestEach(Manifest.permission.READ_CONTACTS)
                .subscribe((Permission permission) -> {
                    if (val == 0) {
                        return;
                    }
                    if (permission.granted) {
                        btnShare.setEnabled(false);
                        progreessbarMain.setVisibility(View.VISIBLE);
                        getContactEmail();
                        callPostContactApi();
                    } else {
                        Toast.makeText(context, getString(R.string.msg_permission_required), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getContactEmail() {
        ContactList contactList = new ContactList(context);
        if (BuildConfig.DEBUG) {
            contectEmailJsonStrting = "[{\"email_id\":\"mwshubham@gmail.com\"}]";
        } else {
            contectEmailJsonStrting = contactList.getContacts();
        }
    }

    @OnClick({R.id.btnShare})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShare:
                initPermission(1);
                break;
        }
    }

    private void callPostContactApi() {
        preferences.setFirstTimeLogin(false);
        if (Utils.isInternetOn(context)) {
//            map.put("uid", "1");
            map.put("contactfriends", contectEmailJsonStrting);
            postFrdBeanCall = mService.getFetcherService(context).inviteFrdFromContact(map);
            postFrdBeanCall.enqueue(new Callback<PostFrdBean>() {
                @Override
                public void onResponse(Call<PostFrdBean> call, Response<PostFrdBean> response) {
                    progreessbarMain.setVisibility(View.GONE);
                    btnShare.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Intent gotoHome = new Intent(context, DrawerMainActivity.class);
                        startActivity(gotoHome);
                        finish();
                        /*if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_SUCESS, new DialogInterfaceAction() {
                                @Override
                                public void dialogAction() {
                                    Intent gotoHome = new Intent(context, DrawerMainActivity.class);
                                    startActivity(gotoHome);
                                    finish();
                                }
                            });
                        } else if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }*/

                    }
                }

                @Override
                public void onFailure(Call<PostFrdBean> call, Throwable t) {
                    t.printStackTrace();
                    btnShare.setEnabled(true);
                    progreessbarMain.setVisibility(View.GONE);
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            btnShare.setEnabled(true);
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }
}
