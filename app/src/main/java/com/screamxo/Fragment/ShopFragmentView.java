package com.screamxo.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.Itemdetail;
import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.ApiBean.Shop.ShopItems;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Activity.ProfileActivity;
import com.screamxo.Activity.RahulWork.BoostActivity;
import com.screamxo.Activity.RahulWork.ItemDetailsAcitvity;
import com.screamxo.Activity.RahulWork.NewConfigurePaymentActivity;
import com.screamxo.Adapter.ImageVideoAdapter;
import com.screamxo.Interface.AudioVideoInterface;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.ImageUtils;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_BOOST_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CONFIGURE_PAYEMENT_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS;

@SuppressLint("ValidFragment")
public class ShopFragmentView extends Fragment implements AudioVideoInterface {

    private static final String TAG = "ShopFragmentView";
    Preferences preferences;
    Boolean isLoginUser = false;
    @BindView(R.id.rc_Watching)
    RecyclerView rc_Watching;

    @BindView(R.id.ll_shop_preview_footer)
    LinearLayout ll_shop_preview_footer;

    @BindView(R.id.rl_WaTNoRecord)
    RelativeLayout rl_WaTNoRecord;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.main_sv)
    ScrollView main_sv;

    private Context context;
    private FetchrServiceBase mService;
    private String uId;

    private ArrayList<Itemdetail> iteamDetail;

    private ImageVideoAdapter imagevideoAdapterAllmedia;
    private int pageCounter = 1;
    private boolean streamCall = false;
    private int totalCount = 0;
    private int currentItemPos = -1;
    private Dialog photoDialog;

    @SuppressLint("ValidFragment")
    public ShopFragmentView(Boolean isLoginUser) {
        this.isLoginUser = isLoginUser;
    }

    public ShopFragmentView() {
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
        View view = inflater.inflate(R.layout.shop_layout_new_screen, container, false);
        ButterKnife.bind(this, view);
        initControValue();
        if (context.getClass().getSimpleName().equals(ProfileActivity.class.getSimpleName())) {
            uId = ((ProfileFragment) ((ProfileActivity) context).getSupportFragmentManager().getFragments().get(0)).getUid();
        } else {
            uId = preferences.getUserId();
        }
        callShopIteamApi();
        return view;
    }

    private void initControValue() {
        mService = new FetchrServiceBase();
        iteamDetail = new ArrayList<>();
        preferences = new Preferences(context);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rc_Watching.setLayoutManager(linearLayoutManager);
        rc_Watching.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (imagevideoAdapterAllmedia.getItemCount() - 1)) {
                    if (streamCall && iteamDetail.size() < totalCount) {
                        updatePaging(false);
                        streamCall = false;
                        callShopIteamApi();
                    }
                }
            }
        });
    }

    private void callShopIteamApi() {
        rl_WaTNoRecord.setVisibility(View.GONE);
        progreessbar.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("uid", uId);
        map.put("myid", uId);
        map.put("offset", "" + 0);
        map.put("limit", StaticConstant.LIMIT);
        if (Utils.isInternetOn(context)) {
            Call<ShopItems> shopItemApiCall = mService.getFetcherService(context).getitembyuid(map);
            shopItemApiCall.enqueue(new Callback<ShopItems>() {
                @Override
                public void onResponse(Call<ShopItems> call, Response<ShopItems> response) {
                    progreessbar.setVisibility(View.GONE);
                    updatePaging(true);
                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null && response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                        pageCounter++;
                        streamCall = true;
                        iteamDetail.addAll(response.body().getResult().getItems());
                        totalCount = response.body().getResult().getTotalcount();

                        if (iteamDetail.size() < 1) {
                            rl_WaTNoRecord.setVisibility(View.VISIBLE);
                        }

                        setAdapter();
                    }
                }

                @Override
                public void onFailure(Call<ShopItems> call, Throwable t) {
                    updatePaging(true);
                    progreessbar.setVisibility(View.GONE);
                    streamCall = true;
                    t.printStackTrace();
                }
            });
        } else {
            updatePaging(true);
            progreessbar.setVisibility(View.GONE);
            streamCall = true;
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void setAdapter() {
        if (imagevideoAdapterAllmedia == null) {
            imagevideoAdapterAllmedia = new ImageVideoAdapter(context, null, iteamDetail, this);
            rc_Watching.setAdapter(imagevideoAdapterAllmedia);
        } else {
            imagevideoAdapterAllmedia.notifyDataSetChanged();
        }
    }

    @Override
    public void Video(String url, String type, int index) {
    }

    @Override
    public void Audio(String url, String audioThum, String title, String type, int index) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void Image(String url, String postId, int index, String type) {
        try {
            if (currentItemPos == index) {
                currentItemPos = -1;
                ll_shop_preview_footer.setVisibility(View.GONE);
            } else {
                currentItemPos = index;
                ll_shop_preview_footer.setVisibility(View.VISIBLE);

                TextView txtName, txtBuy, txtItemCost;
                ImageView imgItem, imgSold, img_more, user_iv;
//          FrameLayout user_image_container;
                FrameLayout frame_item_img;
//        user_image_container = (FrameLayout) ll_shop_preview_footer.findViewById(R.id.user_image_container);
                frame_item_img = ll_shop_preview_footer.findViewById(R.id.frame_item_img);
                user_iv = ll_shop_preview_footer.findViewById(R.id.user_iv);
                img_more = ll_shop_preview_footer.findViewById(R.id.img_more);
                txtName = ll_shop_preview_footer.findViewById(R.id.txt_Text);
                txtBuy = ll_shop_preview_footer.findViewById(R.id.txt_buy);

                imgItem = ll_shop_preview_footer.findViewById(R.id.img_item);
                imgSold = ll_shop_preview_footer.findViewById(R.id.img_sold);
                txtItemCost = ll_shop_preview_footer.findViewById(R.id.txt_item_cost);
                if (iteamDetail.get(currentItemPos) != null) {

                    Itemdetail itemdetailbean = iteamDetail.get(currentItemPos);

                    // Api issue my item is always 1...
                    if (preferences.getUserId().equalsIgnoreCase(uId)) {
                        txtBuy.setText(getString(R.string.txt_boost));
                    } else {
                        txtBuy.setText(getString(R.string.txt_buy));
                    }

                    if (preferences.getUserId().equalsIgnoreCase(uId)) {
                        Picasso.with(context)
                                .load(itemdetailbean.getMediaThumb())
                                .placeholder(R.mipmap.img_placeholder)
                                .error(R.mipmap.img_placeholder)
                                .transform(new CircleTransform())
                                .into(user_iv);
                    } else {
                        Picasso.with(context)
                                .load(itemdetailbean.getUserphotothumb())
                                .placeholder(R.mipmap.img_placeholder)
                                .error(R.mipmap.img_placeholder)
                                .transform(new CircleTransform())
                                .into(user_iv);
                    }

                    if (itemdetailbean.getItemQtyRemained() <= 0) {
                        imgSold.setVisibility(View.VISIBLE);
                        txtBuy.setVisibility(View.INVISIBLE);
                    } else {
                        imgSold.setVisibility(View.GONE);
                        txtBuy.setVisibility(View.VISIBLE);
                    }
                    img_more.setVisibility(itemdetailbean.getIspurchased() == 0 && !preferences.getUserId().equalsIgnoreCase(uId) ? View.VISIBLE : View.GONE);

                    if (itemdetailbean.getIswatched() != null && itemdetailbean.getIswatched().equalsIgnoreCase("1")) {
                        Picasso.with(context).load(R.mipmap.bookmark_active).placeholder(R.mipmap.bookmark_active).error(R.mipmap.bookmark_active).into(img_more);
                    } else {
                        Picasso.with(context).load(R.mipmap.bookmark).placeholder(R.mipmap.bookmark).error(R.mipmap.bookmark).into(img_more);
                    }

                    img_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemdetailbean.getIswatched() == null) {
                                return;
                            }
                            if (itemdetailbean.getIswatched().equalsIgnoreCase("0")) {
                                Picasso.with(context).load(R.mipmap.bookmark_active).placeholder(R.mipmap.bookmark_active).error(R.mipmap.bookmark_active).into(img_more);
                                iteamDetail.get(currentItemPos).setIswatched("1");
                                setBookmark(itemdetailbean.getItemId(), StaticConstant.UNLIKE);
                            } else {
                                Picasso.with(context).load(R.mipmap.bookmark).placeholder(R.mipmap.bookmark).error(R.mipmap.bookmark).into(img_more);
                                iteamDetail.get(currentItemPos).setIswatched("0");
                                setBookmark(itemdetailbean.getItemId(), StaticConstant.LIKE);
                            }
                        }
                    });

                    if (itemdetailbean.getMediaUrl() != null && !itemdetailbean.getMediaUrl().isEmpty()) {
                        Picasso.with(context)
                                .load(itemdetailbean.getMediaUrl())
                                .placeholder(R.mipmap.img_placeholder)
                                .error(R.mipmap.img_placeholder)
                                .fit()
                                .into(imgItem);
                        frame_item_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                photoDialog = ImageUtils.customChoosePhoto(context, itemdetailbean.getMediaUrl());
                            }
                        });
                    }

                    txtName.setText(itemdetailbean.getItemName());

                    if (itemdetailbean.getItemPrice() == null || Utils.getFormattedPrice(itemdetailbean.getItemPrice()) == null) {
                        txtItemCost.setVisibility(View.GONE);
                    } else {
                        txtItemCost.setText(Utils.getFormattedPrice(itemdetailbean.getItemPrice()));
                    }

                    txtName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(context, ItemDetailsAcitvity.class);
                            i.putExtra("itemid", "" + iteamDetail.get(index).getItemId());
                            ((Activity) context).startActivityForResult(i, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
                            ll_shop_preview_footer.setVisibility(View.GONE);
                            currentItemPos = -1;
                        }
                    });
                    txtBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (preferences.getUserId().equalsIgnoreCase(uId)) {
                                Intent gotoActivity = new Intent(context, BoostActivity.class);
                                gotoActivity.putExtra("image", itemdetailbean.getMediaUrl());
                                gotoActivity.putExtra("boost_type", String.valueOf(StaticConstant.BOOST_TYPE_SHOP_ITEM));
                                gotoActivity.putExtra("itemid", String.valueOf(itemdetailbean.getItemId()));
                                gotoActivity.putExtra("item_name", String.valueOf(itemdetailbean.getItemName()));
                                gotoActivity.putExtra("boost_url", String.valueOf(itemdetailbean.getBoost_url()));
                                ((Activity) context).startActivityForResult(gotoActivity, REQ_CODE_BOOST_ACTIVITY_RESULTS);
                            } else {
                                Intent i = new Intent(context, NewConfigurePaymentActivity.class);
                                i.putExtra("screen", ShopFragmentView.class.getSimpleName());
                                i.putExtra("item_shipping_cost", itemdetailbean.getItemShippingCost());
                                i.putExtra("total_quantity", String.valueOf(itemdetailbean.getItemQtyRemained()));
                                i.putExtra("item_cost", String.valueOf(itemdetailbean.getItemPrice()));
                                i.putExtra("itemid", String.valueOf(itemdetailbean.getItemId()));
                                i.putExtra("item_name", String.valueOf(itemdetailbean.getItemName()));
                                i.putExtra("item_media_thumb", String.valueOf(itemdetailbean.getMediaUrl()));
                                ((Activity) context).startActivityForResult(i, REQ_CODE_CONFIGURE_PAYEMENT_RESULTS);
                            }
                            ll_shop_preview_footer.setVisibility(View.GONE);
                            currentItemPos = -1;
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBookmark(int id, int action) {
        Map<String, Integer> map = new HashMap<>();
        map.put("itemid", id);
        map.put("uid", Integer.parseInt(preferences.getUserId()));
        map.put("action", action);
        mService.getFetcherService(context).Bookmark(map).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                int type;
                if (response.body().getStatus().equalsIgnoreCase(StaticConstant.STATUS_1)) {
                    type = DialogBox.DIALOG_SUCESS;
                } else {
                    type = DialogBox.DIALOG_FAILURE;
                }
                DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), type, null);
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (photoDialog != null) {
            photoDialog.dismiss();
        }
    }

    private void updatePaging(boolean isEnable) {
        if (context instanceof ProfileActivity) {
            Fragment fragment = ((ProfileActivity) context).getSupportFragmentManager().findFragmentByTag(ProfileFragment.class.getSimpleName());
            if (fragment != null && fragment instanceof ProfileFragment) {
                ((ProfileFragment) fragment).viewpager.setPagingEnabled(isEnable);
            }
        }

        if (context instanceof DrawerMainActivity) {
            Fragment fragment = ((DrawerMainActivity) context).getSupportFragmentManager().findFragmentByTag(ProfileFragment.class.getSimpleName());
            if (fragment != null && fragment instanceof ProfileFragment) {
                ((ProfileFragment) fragment).viewpager.setPagingEnabled(isEnable);
            }
        }
    }
}