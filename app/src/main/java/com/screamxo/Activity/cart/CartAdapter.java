package com.screamxo.Activity.cart;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean;
import com.example.apimodule.ApiBase.ApiBean.cart.model.Item;
import com.example.apimodule.ApiBase.ApiBean.cart.model.Userdetail;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.R;
import com.screamxo.Utils.DialogFragmentForItemQuantity;
import com.screamxo.Utils.ImageUtils;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.RecyclerViewHolders> {
    private final String TAG = CartAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Userdetail> userdetailsArray;
    private boolean isCart;
    public static String qty = "1";
    private FetchrServiceBase mService;
    private Call<CartBean> cartBeanCall;
    private ClickEvent clickEvent;

    public CartAdapter(Context context, ArrayList<Userdetail> userdetailsArray, boolean isCart, ClickEvent clickEvent) {
        this.context = context;
        this.userdetailsArray = userdetailsArray;
        this.isCart = isCart;

        mService = new FetchrServiceBase();
        this.clickEvent = clickEvent;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cart_adapter, parent, false);
//        View v = LayoutInflater.from(context).inflate(R.layout.cart_adapter_recyclerview, parent, false);
        return new RecyclerViewHolders(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return userdetailsArray.size();
    }

    private void callDeleteSaveItemApi(String id) {
        Map<String, String> map = new HashMap<>();
        Preferences preference = new Preferences(context);
        map.put("user_id", preference.getUserId());
        map.put("item_id", id);
//        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        cartBeanCall = mService.getFetcherService(context).deleteSaveItem(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {
                    clickEvent.onClick("", 0);
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

    private void callDeleteCartItemApi(String id) {
        Map<String, String> map = new HashMap<>();
        Preferences preference = new Preferences(context);
        map.put("user_id", preference.getUserId());
        map.put("item_id", id);
//        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        cartBeanCall = mService.getFetcherService(context).deleteCartItem(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {
                    clickEvent.onClick("", 0);
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

    private void callMoveToCartApi(String id, String qty) {
        Map<String, String> map = new HashMap<>();
        Preferences preference = new Preferences(context);
        map.put("user_id", preference.getUserId());
        map.put("item_id", id);
        map.put("cart_qty", qty);
//        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        cartBeanCall = mService.getFetcherService(context).moveToCart(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {
                    clickEvent.onClick("", 0);
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

    private void callUpdateCartQty(String id, String qty) {
        Map<String, String> map = new HashMap<>();
        Preferences preference = new Preferences(context);
        map.put("user_id", preference.getUserId());
        map.put("item_id", id);
        map.put("cart_qty", qty);
//        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        cartBeanCall = mService.getFetcherService(context).updateCartQty(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {
                    Log.e("CartAdapter", response.body().toString());

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

    private void callUpdateSaveQty(String id, String qty) {
        Map<String, String> map = new HashMap<>();
        Preferences preference = new Preferences(context);
        map.put("user_id", preference.getUserId());
        map.put("item_id", id);
        map.put("cart_qty", qty);
//        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        cartBeanCall = mService.getFetcherService(context).updateSaveQty(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {

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

    private void callMoveToSaveApi(String id, String qty) {
        Map<String, String> map = new HashMap<>();
        Preferences preference = new Preferences(context);
        map.put("user_id", preference.getUserId());
        map.put("item_id", id);
        map.put("cart_qty", qty);
//        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        cartBeanCall = mService.getFetcherService(context).moveToSave(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {
                    clickEvent.onClick("", 0);
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

    public class RecyclerViewHolders extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView txtSellerName, txtItemName, txtItemQty, txtSave;
        ImageView imgPreview, imgRemoveCart;
        LinearLayoutManager linearLayoutManager;
        CartItemAdapter cartItemAdapter;
        ArrayList<Item> itemsArray;
        String path;
        int sectIndex = 0;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
            imgPreview = (ImageView) itemView.findViewById(R.id.img_preview);
            txtSellerName = (TextView) itemView.findViewById(R.id.txt_seller_name);
            txtItemName = itemView.findViewById(R.id.txt_item_name);
            txtItemQty = itemView.findViewById(R.id.txt_quntity);
            imgRemoveCart = itemView.findViewById(R.id.img_close);
            txtSave = itemView.findViewById(R.id.txt_save_item);
            itemsArray = new ArrayList<>();
            imgPreview.setVisibility(View.GONE);
            imgRemoveCart.setVisibility(imgPreview.getVisibility());
            txtItemQty.setVisibility(imgPreview.getVisibility());
            txtItemName.setVisibility(imgPreview.getVisibility());
            txtSave.setVisibility(imgPreview.getVisibility());
            if (isCart)
                txtSave.setText(context.getString(R.string.txt_save));
            else
                txtSave.setText(context.getString(R.string.txt_add_to_cart));
        }

        public void bind(int position) {

            txtItemName.setText(userdetailsArray.get(position).getItems().get(0).getItemName());
            qty = String.valueOf(userdetailsArray.get(position).getItems().get(0).getCartQty());
            txtItemQty.setText(" " + qty + " ");
            txtSellerName.setText(userdetailsArray.get(position).getItems().get(0).getUserName());
            itemsArray.clear();
            itemsArray.addAll(userdetailsArray.get(position).getItems());

            imgPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageUtils.customChoosePhoto(context, path);
                }
            });
            if (cartItemAdapter == null) {
                cartItemAdapter = new CartItemAdapter(context, itemsArray, (url, index) -> {
                    sectIndex = index;
                    txtItemName.setText(userdetailsArray.get(position).getItems().get(index).getItemName());
                    txtItemQty.setText(String.valueOf(userdetailsArray.get(position).getItems().get(index).getCartQty()));

                    Picasso picasso = new Picasso.Builder(context)
                            .listener(new Picasso.Listener() {
                                @Override
                                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                    //Here your log
                                    exception.printStackTrace();
                                }
                            })
                            .build();
                    picasso.load(url)
                            .error(R.mipmap.img_placeholder)
                            .placeholder(R.mipmap.img_placeholder)
                            .into(imgPreview);

                    if (imgPreview.getVisibility() == View.VISIBLE && path.equals(url))
                        imgPreview.setVisibility(View.GONE);
                    else
                        imgPreview.setVisibility(View.VISIBLE);

                    path = url;

                    imgRemoveCart.setVisibility(imgPreview.getVisibility());
                    txtItemQty.setVisibility(imgPreview.getVisibility());
                    txtItemName.setVisibility(imgPreview.getVisibility());
                    txtSave.setVisibility(imgPreview.getVisibility());

                });
                linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(cartItemAdapter);
            } else {
                cartItemAdapter.notifyDataSetChanged();
            }

            imgRemoveCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(userdetailsArray.size() > 0 && userdetailsArray.get(position).getItems().size() > 0)) {
                        return;
                    }
                    if (isCart) {
                        callDeleteCartItemApi(String.valueOf(userdetailsArray.get(position).getItems().get(sectIndex).getId()));
                    } else {
                        callDeleteSaveItemApi(String.valueOf(userdetailsArray.get(position).getItems().get(sectIndex).getId()));
                    }
                    userdetailsArray.get(position).getItems().remove(sectIndex);
                    if (userdetailsArray.get(position).getItems().size() == 0)
                        userdetailsArray.remove(position);

                    imgPreview.setVisibility(View.GONE);

                    imgRemoveCart.setVisibility(imgPreview.getVisibility());
                    txtItemQty.setVisibility(imgPreview.getVisibility());
                    txtItemName.setVisibility(imgPreview.getVisibility());
                    txtSave.setVisibility(imgPreview.getVisibility());
                    notifyDataSetChanged();

                }
            });

            txtSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCart) {
                        callMoveToSaveApi(String.valueOf(userdetailsArray.get(position).getItems().get(sectIndex).getId()), txtItemQty.getText().toString());
                    } else {
                        callMoveToCartApi(String.valueOf(userdetailsArray.get(position).getItems().get(sectIndex).getId()), txtItemQty.getText().toString());
                    }
                    userdetailsArray.get(position).getItems().remove(sectIndex);
                    if (userdetailsArray.get(position).getItems().size() == 0)
                        userdetailsArray.remove(position);
                    notifyDataSetChanged();

//                    imgRemoveCart.performClick();
                }
            });

            txtItemQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayList<String> qtyList = new ArrayList<>();
                    int quan = 0;

                    if (userdetailsArray.get(position).getItems().get(sectIndex).getItemQtyRemained() > 500)
                        quan = 500;
                    else
                        quan = userdetailsArray.get(position).getItems().get(sectIndex).getItemQtyRemained();

                    for (int i = 1; i <= quan; i++) {
                        qtyList.add(String.valueOf(i));
                        //holder.txtItemQuntity.setText(String.valueOf(i));
                    }
                    DialogFragmentForItemQuantity dialogFragment = new DialogFragmentForItemQuantity(new CommonMethod() {
                        @Override
                        public void commonMethod(String type, File... files) {
                            txtItemQty.setText(type);
                            if (isCart) {
                                userdetailsArray.get(position).getItems().get(sectIndex).setCartQty(Integer.parseInt(type));
                                callUpdateCartQty(String.valueOf(userdetailsArray.get(position).getItems().get(sectIndex).getId()), type);
                            } else
                                callUpdateSaveQty(String.valueOf(userdetailsArray.get(position).getItems().get(sectIndex).getId()), type);
                        }
                    }, qtyList, context);
                    FragmentManager manager = ((Activity) context).getFragmentManager();
                    dialogFragment.show(manager, "");

                }
            });
        }


    }

}
