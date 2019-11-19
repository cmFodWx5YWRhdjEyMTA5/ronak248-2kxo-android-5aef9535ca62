package com.screamxo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.AddwatchedItemBean;
import com.example.apimodule.ApiBase.ApiBean.WatchingItem;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.RahulWork.ItemDetailsAcitvity;
import com.screamxo.Activity.WatchedItemListActivity;
import com.screamxo.Interface.DialogInterfaceAction;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS;

/**
 * Created by Shubham.Agarwal on 02/12/16.
 */

public class WatchedItemListAdapter extends RecyclerView.Adapter<WatchedItemListAdapter.WatchItemHolder> implements DialogInterfaceAction {

    private Context context;
    private ArrayList<WatchingItem> watchingitemlist;
    private int height, width;
    private Preferences preference;

    Call<AddwatchedItemBean> addwatcheditemcall;
    private FetchrServiceBase mservice;

    public WatchedItemListAdapter(Context context, ArrayList<WatchingItem> watchingitemlist) {
        this.context = context;
        this.watchingitemlist = watchingitemlist;
        preference = new Preferences(context);
        mservice = new FetchrServiceBase();
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.img_placeholder);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
    }

    @Override
    public WatchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.watched_item_list_recycler, parent, false);
        return new WatchItemHolder(v);
    }

    @Override
    public void onBindViewHolder(WatchItemHolder holder, int position) {
        String name = watchingitemlist.get(position).getItemName();
        String price = watchingitemlist.get(position).getItemPrice();
        String merge = name + "\n\n" + Utils.getFormattedPrice(price);

        SpannableString ss = new SpannableString(merge);
        ss.setSpan(new ForegroundColorSpan(Color.GRAY), name.length(), merge.length(), 0);
        if (watchingitemlist != null) {
            holder.txtitemname.setText(ss);
        }

        if (watchingitemlist.get(position).getMediaThumb() != null && watchingitemlist.get(position).getMediaThumb() != "") {
            Picasso.with(context)
                    .load(watchingitemlist.get(position).getMediaThumb())
                    .placeholder(R.mipmap.img)
                    .error(R.mipmap.img)
                    .resize(width, height)
                    .centerCrop()
                    .into(holder.imgitem);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, ItemDetailsAcitvity.class);
            i.putExtra("itemid", String.valueOf(watchingitemlist.get(position).getItemId()));
            ((Activity) context).startActivityForResult(i, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
        });

        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

                builder1.setMessage(context.getString(R.string.msg_remove_item_from_watch_list));
                builder1.setCancelable(true);
                builder1.setTitle(context.getString(R.string.app_name));
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Map<String, String> map = new HashMap<>();
                                map.put("uid", preference.getUserId());
                                map.put("itemid", String.valueOf(watchingitemlist.get(position).getItemId()));
                                map.put("action", "0");
                                if (Utils.isInternetOn(context)) {
                                    ((WatchedItemListActivity) context).progreessbar.setVisibility(View.VISIBLE);
                                    addwatcheditemcall = mservice.getFetcherService(context).AddWatchedItem(map);
                                    addwatcheditemcall.enqueue(new Callback<AddwatchedItemBean>() {
                                        @Override
                                        public void onResponse(Call<AddwatchedItemBean> call, Response<AddwatchedItemBean> response) {
                                            ((WatchedItemListActivity) context).progreessbar.setVisibility(View.GONE);
                                            if (response.code() == StaticConstant.RESULT_OK) {
                                                if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                                                    Utils.showToast(context, response.body().getMsg());
                                                    removeAt(position);
                                                } else {
                                                    Utils.showToast(context, response.body().getMsg());
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<AddwatchedItemBean> call, Throwable t) {
                                            ((WatchedItemListActivity) context).progreessbar.setVisibility(View.GONE);
                                            Utils.showToast(context, t.toString());
                                        }
                                    });

                                } else {
                                    Utils.showToast(context, context.getString(R.string.toast_no_internet));
                                }
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Cancel",
                        (dialog, id) -> dialog.cancel());
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return watchingitemlist.size();
    }

    @Override
    public void dialogAction() {
    }

    public void removeAt(int position) {
        watchingitemlist.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, watchingitemlist.size());
    }

    public class WatchItemHolder extends RecyclerView.ViewHolder {
        private ImageView imgitem;
        private TextView txtitemname;
        private TextView imgdelete;

        public WatchItemHolder(View v) {
            super(v);
            imgitem = v.findViewById(R.id.img_item);
            txtitemname = v.findViewById(R.id.txt_item_name);
            imgdelete = v.findViewById(R.id.img_delete);
        }
    }
}
