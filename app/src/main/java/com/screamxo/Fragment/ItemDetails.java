package com.screamxo.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailBean;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailMedia;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailResult;
import com.example.apimodule.ApiBase.ApiBean.ItemReviewData;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Adapter.HomeViewPagerAdapter;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * NO idea where it is called in the application..
 */
public class ItemDetails extends Fragment implements View.OnClickListener {
    public static final int ID = R.id.ItemDetails_ID;
    private static final String TAG = "ItemDetails";
    @BindView(R.id.txtPrice)
    TextView txtPrice;
    @BindView(R.id.txtDescription)
    TextView txtDescription;
    @BindView(R.id.img_profile)
    ImageView img_profile;
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtDesc)
    TextView txtDesc;
    @BindView(R.id.rl_BottomSheet)
    RelativeLayout rlBottomSheet;
    @BindView(R.id.ll_BottomSheet)
    LinearLayout ll_BottomSheet;
    @BindView(R.id.txtConditionData)
    TextView txtConditionData;
    @BindView(R.id.txtShippingData)
    TextView txtShippingData;
    @BindView(R.id.txtLocationData)
    TextView txtLocationData;
    Unbinder unbinder;
    @BindView(R.id.rl_Bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.rc_view)
    RecyclerView rcView;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.rl_Close)
    RelativeLayout rlClose;
    @BindView(R.id.rl_Reviews)
    RelativeLayout rl_Reviews;
    @BindView(R.id.btnReview)
    Button btnReview;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.imgBookmark)
    ImageView imgBookmark;
    @BindView(R.id.txtProductName)
    TextView txtProductName;
    String itemid = "";
    Context context;
    Call<ItemDetailBean> streampostcall;
    ArrayList<ItemDetailMedia> mediaArrayList;
    Preferences preferences;
    FragmentActivity myContext;
    int height = 0, width = 0;
    private FetchrServiceBase mservice;
    private ItemDetailResult itemDetailResult;
    private ArrayList<ItemReviewData> reviewlist;

    public ItemDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        callItemDetailapi();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void initView() {
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
        mservice = new FetchrServiceBase();
        context = getActivity();
        preferences = new Preferences(context);
        itemid = getArguments().getString("itemid");
        String uId = preferences.getUserId();
        mediaArrayList = new ArrayList<>();
    }


    public void callItemDetailapi() {

        Map<String, String> map = new HashMap<>();
        map.put("itemid", itemid);
        map.put("myid", preferences.getUserId());
        if (Utils.isInternetOn(context)) {

            progreessbar.setVisibility(View.VISIBLE);
            streampostcall = mservice.getFetcherService(context).GetItemDetail(map);

            streampostcall.enqueue(new Callback<ItemDetailBean>() {
                @Override
                public void onResponse(Call<ItemDetailBean> call, Response<ItemDetailBean> response) {

                    progreessbar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
//                            Utils.showToast(context, response.body().getStatus());
                            //   linearUserDetail.setVisibility(View.VISIBLE);

                            mediaArrayList.clear();
                            mediaArrayList.addAll(response.body().getResult().getMedia());
                            itemDetailResult = response.body().getResult();
                            reviewlist = itemDetailResult.getReviewdata();

                            //    setVibility();
                            setValue();
                            setImagePager();

                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemDetailBean> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                    Utils.showToast(context, t.toString());
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
            progreessbar.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setValue() {
        if (itemDetailResult.getIswatched() == 1) {
            imgBookmark.setBackgroundResource(R.mipmap.bookmark_active);
        } else {
            imgBookmark.setBackgroundResource(R.mipmap.bookmark);
        }

        txtProductName.setText(itemDetailResult.getItemName());
        txtPrice.setText("$ " + itemDetailResult.getItemPrice());
        txtDescription.setText(itemDetailResult.getItemDescription());
        Glide.with(this).load(itemDetailResult.getUserphoto()).load(img_profile);
        Picasso.with(context)
                .load(itemDetailResult.getUserphoto())
                .placeholder(R.mipmap.profile_pic_placeholder)
                .error(R.mipmap.profile_pic_placeholder)
                .resize(150, 150)
                .transform(new CircleTransform())
                .centerCrop()
                .into(img_profile);
        txtDate.setText(itemDetailResult.getCreatedDate());
        txtName.setText(itemDetailResult.getFname());
        txtDesc.setText(itemDetailResult.getUsercity());
        txtShippingData.setText(itemDetailResult.getItemShippingCost());
        txtConditionData.setText(getString(R.string.txt_new));

    }


    private void setImagePager() {

        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(myContext.getSupportFragmentManager());
        ImagePagerFragment fragment1;
        if (mediaArrayList.size() == 0) {
            fragment1 = new ImagePagerFragment(0, mediaArrayList, isSoldVisible());
            adapter.addFragment(fragment1, "" + 0);
        } else {

            for (int i = 0; i < mediaArrayList.size(); i++) {
                fragment1 = new ImagePagerFragment(i, mediaArrayList, isSoldVisible());
                adapter.addFragment(fragment1, "" + i);
            }
        }
        view_pager.setAdapter(adapter);
    }

    Boolean isSoldVisible() {
        return itemDetailResult.getIspurchased().equals("1");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_BottomSheet, R.id.rl_Close, R.id.btnReview})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_BottomSheet:
                if (ll_BottomSheet.getVisibility() == View.VISIBLE) {
                    ll_BottomSheet.setVisibility(View.GONE);
                } else {
                    ll_BottomSheet.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_Close:
                rl_Reviews.setVisibility(View.GONE);
                break;
            case R.id.btnReview:
                rl_Reviews.setVisibility(View.VISIBLE);
                if (ll_BottomSheet.getVisibility() == View.VISIBLE) {
                    ll_BottomSheet.setVisibility(View.GONE);
                } else {
                    ll_BottomSheet.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
