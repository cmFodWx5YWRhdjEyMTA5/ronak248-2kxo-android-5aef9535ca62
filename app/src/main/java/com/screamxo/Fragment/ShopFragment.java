package com.screamxo.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.CategoryList.Category;
import com.example.apimodule.ApiBase.ApiBean.ShopData.Shop;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.ProfileActivity;
import com.screamxo.Activity.RahulWork.ItemDetailsAcitvity;
import com.screamxo.Activity.RahulWork.ShopListingActivity;
import com.screamxo.Adapter.ImageVideoShopAdapter;
import com.screamxo.Interface.AudioVideoInterface;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_SHOP_LISTING_ACTIVITY_RESULTS;

/**
 * Created by Shubham Agarwal on 07/11/16.
 */

@SuppressLint("ValidFragment")
public class ShopFragment extends Fragment implements AudioVideoInterface, View.OnClickListener {

    private static final String TAG = "ShopFragment";

    private Context context;
    private FetchrServiceBase mService;
    private Call<Shop> ShopItemApiCall;
    private String uId;
    private ArrayList<Object> mWatched;
    private ArrayList<Object> mRecommend;
    private ArrayList<Object> mRecentitems;
    private ArrayList<Object> mGlobaltrend;

    private ImageVideoShopAdapter imagevideoAdapterAllmedia, imageRecommendAdapter, imgRecentlyAdapter, imgWorldAdapter;
    Preferences preferences;

    @BindView(R.id.rc_Watching)
    RecyclerView rc_Watching;

    @BindView(R.id.rc_Recommended)
    RecyclerView rc_Recommended;

    @BindView(R.id.rc_Recently)
    RecyclerView rc_Recently;

    @BindView(R.id.rc_World)
    RecyclerView rc_World;

    @BindView(R.id.rl_Sell)
    RelativeLayout rl_Sell;

    @BindView(R.id.rl_Watching)
    RelativeLayout rl_Watching;

    @BindView(R.id.rl_Recommended)
    RelativeLayout rl_Recommended;

    @BindView(R.id.rl_Recently)
    RelativeLayout rl_Recently;

    @BindView(R.id.rl_Trending)
    RelativeLayout rl_Trending;

    @BindView(R.id.rl_WaTNoRecord)
    RelativeLayout rl_WaTNoRecord;

    @BindView(R.id.rl_RecRecord)
    RelativeLayout rl_RecRecord;

    @BindView(R.id.rl_RecentlyRecord)
    RelativeLayout rl_RecentlyRecord;

    @BindView(R.id.rl_TrendingRecord)
    RelativeLayout rl_TrendingRecord;


    @BindView(R.id.toolbar_edt_search)
    EditText toolbar_edt_search;


    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;

    @BindView(R.id.rl_search)
    RelativeLayout rl_search;

    @BindView(R.id.rl_search_main)
    RelativeLayout rl_search_main;

    @BindView(R.id.txtCancel)
    TextView txtCancel;

    @BindView(R.id.ic_filter)
    ImageView ic_filter;

    @BindView(R.id.rl_Search)
    RelativeLayout rl_Search;

    @BindView(R.id.txt_item)
    TextView txtItem;
    @BindView(R.id.txt_item_label)
    TextView txt_item_label;

    @BindView(R.id.user_profile)
    ImageView imgUserProfile;
    @BindView(R.id.txt_frd)
    TextView txtFrd;
    @BindView(R.id.txt_frd_frnd)
    TextView txt_frd_frnd;

    String trending = "2"; // default is 2 otherwise 1
    String categoryId = "0"; // default is 2 otherwise 1

    public ShopFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_fragmentlayout, container, false);
        ButterKnife.bind(this, view);
        initControValue();
        if (context.getClass().getSimpleName().equals(ProfileActivity.class.getSimpleName())) {
            uId = ((ProfileFragment) ((ProfileActivity) context).getSupportFragmentManager().getFragments().get(0)).getUid();
        } else {
            uId = preferences.getUserId();
        }
        callShopIteamApi("");
        return view;
    }

    private void initControValue() {
        mService = new FetchrServiceBase();
        mWatched = new ArrayList<>();
        mRecommend = new ArrayList<>();
        mRecentitems = new ArrayList<>();
        mGlobaltrend = new ArrayList<>();

        preferences = new Preferences(context);

        rl_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_search_main.setVisibility(View.VISIBLE);
                rl_search.setVisibility(View.GONE);
                ic_filter.setVisibility(View.GONE);
//                InputMethodManager inputMethodManager =
//                        (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.toggleSoftInputFromWindow(
//                        toolbar_edt_search.getApplicationWindowToken(),
//                        InputMethodManager.SHOW_FORCED, 0);
//                Utils.showKeyboard(context,toolbar_edt_search);
//                InputMethodManager imm = (InputMethodManager)
//                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(toolbar_edt_search ,
//                        InputMethodManager.SHOW_IMPLICIT);

                toolbar_edt_search.requestFocus();
                InputMethodManager imm = (InputMethodManager)  getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                Utils.showKeyboard(context,toolbar_edt_search);
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_search.setVisibility(View.VISIBLE);
                rl_search_main.setVisibility(View.GONE);
            }
        });

        ic_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterPopUp();
            }
        });

        rc_Watching.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rc_Recommended.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rc_Recently.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rc_World.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        toolbar_edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callShopIteamApi(toolbar_edt_search.getText().toString());
                        }
                    }, 2000);



            }
        });
    }

    private void showFilterPopUp() {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupFilterContainer;
        if (layoutInflater != null) {
            popupFilterContainer = layoutInflater.inflate(R.layout.popup_shop_filter, null);
            LinearLayout ll_filter = popupFilterContainer.findViewById(R.id.ll_filter);
            PopupWindow popupWindow = new PopupWindow(context);
//            Result category = new Gson().fromJson(preferences.getCategory(), Result.class);
            ArrayList<Category> category = preferences.getCategory();
//            Log.d(TAG, "showFilterPopUp preferences.getCategory(): " + category);
            for (int i = 0; i < category.size(); i++) {
                TextView labelTv = new TextView(context);
                labelTv.setTag(i);
                labelTv.setText(category.get(i).getCategoryName());
                labelTv.setPadding(com.sandrios.sandriosCamera.internal.utils.Utils.convertDipToPixels(context, 16)
                        , com.sandrios.sandriosCamera.internal.utils.Utils.convertDipToPixels(context, 10)
                        , com.sandrios.sandriosCamera.internal.utils.Utils.convertDipToPixels(context, 16)
                        , com.sandrios.sandriosCamera.internal.utils.Utils.convertDipToPixels(context, 10));

                if (categoryId.equalsIgnoreCase(String.valueOf(category.get(i).getId()))) {
                    labelTv.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_my_popup_selected));
                } else {
                    labelTv.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_my_popup));
                }

                labelTv.setOnClickListener(view -> {
                    for (int i1 = 0; i1 < ll_filter.getChildCount(); i1++) {
                        ll_filter.getChildAt(i1).setBackground(ContextCompat.getDrawable(context, R.drawable.bg_my_popup));
                    }
                    if (((TextView) view).getText().toString().equalsIgnoreCase("trending")) {
                        trending = "1";
                    } else {
                        trending = "2";
                    }
                    categoryId = String.valueOf(category.get((Integer) view.getTag()).getId());
                    callShopIteamApi("");
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_my_popup_selected));
                    popupWindow.dismiss();
                });
                ll_filter.addView(labelTv);
            }

            popupWindow.setContentView(popupFilterContainer);
//            popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//            popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
            popupWindow.showAsDropDown(ic_filter, 0, 0);
        }
    }


    private void callShopIteamApi(String searchedData) {
        rl_WaTNoRecord.setVisibility(View.GONE);
        rl_RecRecord.setVisibility(View.GONE);
        rl_RecentlyRecord.setVisibility(View.GONE);
        rl_TrendingRecord.setVisibility(View.GONE);

        progreessbar.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();

        map.put("uid", uId);
        map.put("offset", "1");
        map.put("limit", StaticConstant.LIMIT);
        map.put("type", "0");
        map.put("searchstring", searchedData);
        map.put("categoryid", categoryId);
        map.put("trending", trending);
        if (Utils.isInternetOn(context)) {
            ShopItemApiCall = mService.getFetcherService(context).ProfileShop(map);
            ShopItemApiCall.enqueue(new Callback<Shop>() {
                @Override
                public void onResponse(Call<Shop> call, Response<Shop> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null && response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                        mWatched = new ArrayList<>();
                        mRecommend = new ArrayList<>();
                        mRecentitems = new ArrayList<>();
                        mGlobaltrend = new ArrayList<>();

                        mWatched.addAll(response.body().getResult().getWatched().getItemdetails());
                        mRecommend.addAll(response.body().getResult().getRecommend().getItemdetails());
                        mRecentitems.addAll(response.body().getResult().getRecentitems().getItemdetails());
                        mGlobaltrend.addAll(response.body().getResult().getGlobaltrend().getItemdetails());

                        setAdapter();
                        setRecommendAdapter();
                        setRecentAdapter();
                        setGlobalAdapter();

                        if (mWatched.isEmpty()) {
                            rl_WaTNoRecord.setVisibility(View.VISIBLE);
                        }
                        if (mRecommend.isEmpty()) {
                            rl_RecRecord.setVisibility(View.VISIBLE);
                        }
                        if (mRecentitems.isEmpty()) {
                            rl_RecentlyRecord.setVisibility(View.VISIBLE);
                        }
                        if (mGlobaltrend.isEmpty()) {
                            rl_TrendingRecord.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Shop> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                }
            });
        } else {
            progreessbar.setVisibility(View.GONE);
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void setGlobalAdapter() {
//        if (imgWorldAdapter == null) {
            imgWorldAdapter = new ImageVideoShopAdapter(context, mGlobaltrend, this);
            rc_World.setAdapter(imgWorldAdapter);
//        } else {
//            imgWorldAdapter.notifyDataSetChanged();
//        }
    }

    private void setRecommendAdapter() {
//        if (imageRecommendAdapter == null) {
            imageRecommendAdapter = new ImageVideoShopAdapter(context, mRecommend, this);
            rc_Recommended.setAdapter(imageRecommendAdapter);
//        } else {
//            imageRecommendAdapter.notifyDataSetChanged();
//        }
    }

    private void setRecentAdapter() {
//        if (imgRecentlyAdapter == null) {
            imgRecentlyAdapter = new ImageVideoShopAdapter(context, mRecentitems, this);
            rc_Recently.setAdapter(imgRecentlyAdapter);
//        } else {
//            imgRecentlyAdapter.notifyDataSetChanged();
//        }
    }

    private void setAdapter() {
//        if (imagevideoAdapterAllmedia == null) {
            imagevideoAdapterAllmedia = new ImageVideoShopAdapter(context, mWatched, this);
            rc_Watching.setAdapter(imagevideoAdapterAllmedia);
//        } else {
//            imagevideoAdapterAllmedia.notifyDataSetChanged();
//        }
    }

    @Override
    public void Video(String url, String type, int index) {
    }

    @Override
    public void Audio(String url, String audioThum, String title, String type, int index) {
    }

    @Override
    public void Image(String url, String postId, int index, String type) {
        Intent i = new Intent(context, ItemDetailsAcitvity.class);
        i.putExtra("itemid", "" + postId);
        ((Activity) context).startActivityForResult(i, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
    }

    @OnClick({R.id.rl_Sell, R.id.tv_sell, R.id.rl_Watching, R.id.rl_Recommended, R.id.rl_Recently, R.id.rl_Trending,
            R.id.tv_watching,
            R.id.tv_recommended,
            R.id.tv_recent_viewed,
            R.id.tv_trending
    })
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_Sell:
            case R.id.tv_sell:
                Intent gotoSell = new Intent(getActivity(), com.screamxo.Activity.RahulWork.SellItemActivity.class);
                ((Activity) context).startActivityForResult(gotoSell, StaticConstant.REQUEST_SELL_ITEM);
                break;

            case R.id.rl_Watching:
            case R.id.tv_watching:
                Intent gotoWatch = new Intent(getActivity(), ShopListingActivity.class);
                gotoWatch.putExtra("position", 1);
                ((Activity) context).startActivityForResult(gotoWatch, REQ_CODE_SHOP_LISTING_ACTIVITY_RESULTS);
                break;

            case R.id.rl_Recommended:
            case R.id.tv_recommended:
                Intent gotoRecommended = new Intent(getActivity(), ShopListingActivity.class);
                gotoRecommended.putExtra("position", 2);
                ((Activity) context).startActivityForResult(gotoRecommended, REQ_CODE_SHOP_LISTING_ACTIVITY_RESULTS);
                break;

            case R.id.rl_Recently:
            case R.id.tv_recent_viewed:
                Intent gotoRecently = new Intent(getActivity(), ShopListingActivity.class);
                gotoRecently.putExtra("position", 3);
                ((Activity) context).startActivityForResult(gotoRecently, REQ_CODE_SHOP_LISTING_ACTIVITY_RESULTS);
                break;

            case R.id.rl_Trending:
            case R.id.tv_trending:
                Intent gotoTrending = new Intent(getActivity(), ShopListingActivity.class);
                gotoTrending.putExtra("position", 4);
                ((Activity) context).startActivityForResult(gotoTrending, REQ_CODE_SHOP_LISTING_ACTIVITY_RESULTS);
                break;
        }
    }

    public void onFilterBtnClick() {
        Log.d(TAG, "onFilterBtnClick: ");
        if (ic_filter.getVisibility() == View.VISIBLE) {
            ic_filter.setVisibility(View.GONE);
        } else {
            rl_search_main.setVisibility(View.GONE);
            rl_search.setVisibility(View.VISIBLE);
            ic_filter.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ShopItemApiCall != null) {
            ShopItemApiCall.cancel();
        }
    }
}
