package com.screamxo.Activity.cart;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean;
import com.example.apimodule.ApiBase.ApiBean.cart.model.Userdetail;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.example.apimodule.ApiBase.Preferences;
import com.screamxo.Fragment.NewCartFragment;
import com.screamxo.R;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartTabFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.lny_no_data)
    LinearLayout lnyNoData;
    GetItemCount getItemCount;
    private Activity context;
    private CartAdapter cartAdapter;
    private FetchrServiceBase mService;
    private Call<CartBean> cartBeanCall;
    public ArrayList<Userdetail> userdetailsArray;
    public SaveItemFragment saveItemFragment;
    int item_quentity;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_only, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        callGetCartItemApi();
    }

    private void init() {
        mService = new FetchrServiceBase();
        userdetailsArray = new ArrayList<>();
    }

    private void setAdapter() {
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        cartAdapter = new CartAdapter(context, userdetailsArray, true, new ClickEvent() {
            @Override
            public void onClick(String url, int index) {
                callGetCartItemApi();
                if (getParentFragment() instanceof NewCartFragment) {
                    ((NewCartFragment) getParentFragment()).saveItemFragment.callGetSaveCartItemApi();
                }
            }
        });
        recyclerView.setAdapter(cartAdapter);

    }

    public void callGetCartItemApi() {
        Map<String, String> map = new HashMap<>();
        Preferences preference = new Preferences(context);
        map.put("user_id", preference.getUserId());
//        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        cartBeanCall = mService.getFetcherService(context).getCartItem(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {

                    userdetailsArray.clear();
                    if (response.code() == StaticConstant.RESULT_OK) {
                        getItemCount.setCartItem(String.valueOf(response.body().getResult().getCount()));
                        userdetailsArray.addAll(response.body().getResult().getUserdetails());
                        setAdapter();
                    } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                        Utils.unAuthentication(context);

                    } else {
                        getItemCount.setCartItem(String.valueOf(0));
                    }
                }

                @Override
                public void onFailure(Call<CartBean> call, Throwable t) {
                    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.context = context;
        getItemCount = (GetItemCount) getParentFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        callGetCartItemApi();
        recyclerView.setVisibility(View.GONE);
    }
}