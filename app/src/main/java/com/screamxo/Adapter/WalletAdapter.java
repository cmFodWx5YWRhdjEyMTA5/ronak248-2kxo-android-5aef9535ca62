package com.screamxo.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.WalletList.Result;
import com.screamxo.Activity.RahulWork.TransactionHIstoryActivity;
import com.screamxo.Activity.RahulWork.WalletActivity;
import com.screamxo.R;
import com.screamxo.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 8/8/17.
 */
public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.RecyclerViewHolders> {
    private static final String TAG = "WalletAdapter";

    private Context context;
    private List<Object> listData;


    public WalletAdapter(Context context, ArrayList<Object> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public WalletAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_item_recycler, null);
        return new RecyclerViewHolders(layoutView);
    }

    @SuppressLint("PrivateResource")
    @Override
    public void onBindViewHolder(WalletAdapter.RecyclerViewHolders holder, int position) {
        try {
            Result bean = (Result) listData.get(position);
            Log.d(TAG, "onBindViewHolder: " + bean);
            holder.txtMonth.setText(bean.getAddedOn());
            holder.txtDescription.setText(bean.getWalletMessage());

            if (bean.getAmount() == null || Utils.getFormattedPrice(bean.getAmount()) == null) {
                holder.txtAmount.setVisibility(View.GONE);
            } else {
                holder.txtAmount.setText(Utils.getFormattedPrice(bean.getAmount()));
            }

            if (bean.getPaymentMode().equalsIgnoreCase("in")) {
                holder.imgCircle.setBackgroundResource(R.mipmap.circle);
                holder.txtAmount.setTextColor(context.getResources().getColor(R.color.tw__blue_pressed));
            } else {
                holder.imgCircle.setBackgroundResource(R.mipmap.circle1);
                holder.txtAmount.setTextColor(context.getResources().getColor(R.color.colorPink));
            }
            //holder.txt_user_name.setText(listData.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return listData.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder {
        TextView txtMonth, txtDescription, txtAmount;
        ImageView imgCircle;
        RelativeLayout rl_Item;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            txtMonth = itemView.findViewById(R.id.txtMonth);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            imgCircle = itemView.findViewById(R.id.imgCircle);
            rl_Item = itemView.findViewById(R.id.rl_Item);
            rl_Item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Result bean = (Result) listData.get(getLayoutPosition());
                    Intent gotoTransactionView = new Intent(context, TransactionHIstoryActivity.class);
                    gotoTransactionView.putExtra("itemId", bean.getId());
                    ((Activity) context).startActivityForResult(gotoTransactionView, WalletActivity.REQ_CODE_TRANSACTION_HISTORY);
                }
            });
        }
    }
}
