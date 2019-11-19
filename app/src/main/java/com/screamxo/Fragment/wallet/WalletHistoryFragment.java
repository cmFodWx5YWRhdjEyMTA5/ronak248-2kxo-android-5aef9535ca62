package com.screamxo.Fragment.wallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.apimodule.ApiBase.ApiBean.WalletList.WalletList;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Adapter.WalletAdapter;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.baseClass.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class WalletHistoryFragment extends BaseFragment {
    Context context;
    WalletAdapter walletAdapter;
    ArrayList<Object> walletList;
    Preferences preferences;
    @BindView(R.id.rc_Month)
    RecyclerView rcMonth;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    Unbinder unbinder;
    private FetchrServiceBase mService;
    Call<WalletList> rejectcall;
    String month;

    public WalletHistoryFragment(String s) {
        month=s;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_wallet_history;
    }

    @Override
    public void onAttach(Activity activity) {
        context = activity;
        super.onAttach(activity);

    }

    @Override
    public void init()
    {
        preferences = new Preferences(context);
        walletList = new ArrayList<>();
        mService = new FetchrServiceBase();
//        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
//        int month = calendar.get(Calendar.MONTH) + 1;
//        initApi(String.valueOf(month));
    }

    public void initApi(String data)
    {
        Map<String, String> map = new HashMap<>();
//        map.put("user_id", preferences.getUserId());
        map.put(getString(R.string.txt_month), "" + data);

        if (Utils.isInternetOn(context))
        {
            progreessbar.setVisibility(View.VISIBLE);
            if (rejectcall != null)
                rejectcall.cancel();
            rejectcall = mService.getFetcherService(context).walletList(map);

            rejectcall.enqueue(new Callback<WalletList>() {
                @Override
                public void onResponse(Call<WalletList> call, Response<WalletList> response) {
                    //   setViewEnableDisable(true);
                        progreessbar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK)
                    {
                        preferences.saveAmount(response.body().getPage_flag());

                        walletList.clear();
                        walletList.addAll(response.body().getResult());
                        setAdapter();

                    } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                        Utils.unAuthentication(context);
                    } else if (response.code() == StaticConstant.BAD_REQUEST) {
                        walletList.clear();
                        setAdapter();
                    }
                }

                @Override
                public void onFailure(Call<WalletList> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void setAdapter()
    {
        if (walletAdapter == null)
        {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            rcMonth.setLayoutManager(linearLayoutManager);
            walletAdapter = new WalletAdapter(context, walletList);
            rcMonth.setAdapter(walletAdapter);
        }
        else
            {
            walletAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(TextUtils.isEmpty(month))
        {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
             month =String.valueOf(calendar.get(Calendar.MONTH) + 1);
        }
        initApi(String.valueOf(month));
    }

}