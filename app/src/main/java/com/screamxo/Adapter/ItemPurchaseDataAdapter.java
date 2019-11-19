package com.screamxo.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.ItemPurchasedata;
import com.example.apimodule.ApiBase.ApiBean.Tracking.UpdateTrackingDetailReposne;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.RahulWork.ItemDetailsAcitvity;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by parangat on 13/10/17.
 */

public class ItemPurchaseDataAdapter extends RecyclerView.Adapter<ItemPurchaseDataAdapter.ViewHolder> {
    private static final String TAG = "ItemPurchaseDataAdapter";
    private final Context context;
    private final ArrayList<ItemPurchasedata> itemPurchasedatas;
    private Call<UpdateTrackingDetailReposne> updatetrackingdetailCall;


    public ItemPurchaseDataAdapter(Context context, @NonNull ArrayList<ItemPurchasedata> itemPurchasedatas) {
        this.context = context;
        this.itemPurchasedatas = itemPurchasedatas;
        FetchrServiceBase mservice = new FetchrServiceBase();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_data, null);
        return new ItemPurchaseDataAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (TextUtils.isEmpty(itemPurchasedatas.get(holder.getAdapterPosition()).getPurchaseuserphotothumb())) {
            Picasso.with(context)
                    .load(R.mipmap.user)
                    .placeholder(R.mipmap.user)
                    .error(R.mipmap.user)
                    .fit()
                    .transform(new CircleTransform())
                    .into(holder.user_iv);
        } else Picasso.with(context)
                .load(itemPurchasedatas.get(holder.getAdapterPosition()).getPurchaseuserphotothumb())
                .placeholder(R.mipmap.user)
                .error(R.mipmap.user)
                .fit()
                .transform(new CircleTransform())
                .into(holder.user_iv);


        holder.add_tracking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemPurchasedatas.get(holder.getAdapterPosition()).isHastrackingdetail()) {
                    ((ItemDetailsAcitvity) context).rl_purchase_data.setVisibility(View.GONE);
                    ((ItemDetailsAcitvity) context).courierTrackingDetailsDialog(holder.getAdapterPosition(), itemPurchasedatas.get(holder.getAdapterPosition()).getOrderId());
                }
            }
        });

        holder.username_tv.setText(itemPurchasedatas.get(holder.getAdapterPosition()).getPurchaseusername());
        holder.info_tv.setText(String.format("Shipping Address: %s", itemPurchasedatas.get(holder.getAdapterPosition()).getShippingaddress()));
        if (itemPurchasedatas.get(holder.getAdapterPosition()).isHastrackingdetail()) {
            holder.add_tracking_btn.setText(String.format("Tracking Id: %s", itemPurchasedatas.get(holder.getAdapterPosition()).getTrackingdetail().getTrackingid()));
        }
    }

    @Override
    public int getItemCount() {
        return itemPurchasedatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username_tv, info_tv;
        ImageView user_iv;
        Button add_tracking_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            username_tv = itemView.findViewById(R.id.username_tv);
            info_tv = itemView.findViewById(R.id.info_tv);
            user_iv = itemView.findViewById(R.id.user_iv);
            add_tracking_btn = itemView.findViewById(R.id.add_tracking_btn);
        }
    }
}
