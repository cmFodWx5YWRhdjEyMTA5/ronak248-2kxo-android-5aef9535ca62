package com.screamxo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.SoldItemDetail;
import com.screamxo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class SoldItemListAdapter extends RecyclerView.Adapter<SoldItemListAdapter.SoldItemHolder> {

    private Context context;
    private ArrayList<SoldItemDetail> solditemlist;
    private int height, width;

    public SoldItemListAdapter(Context context, ArrayList<SoldItemDetail> solditemlist) {
        this.context = context;
        this.solditemlist = solditemlist;

        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
    }

    @Override
    public SoldItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sold_list_recycler, parent, false);
        return new SoldItemHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(SoldItemHolder holder, int position) {
        if (solditemlist.get(position).getOrderdetails().get(0).getDetails().get(0).getShippingAddress() != null
                && !solditemlist.get(position).getOrderdetails().get(0).getDetails().get(0).getShippingAddress().equals("")) {
            holder.txtshippindaddress.setText("Shipping Address:" + " " + solditemlist.get(position).getOrderdetails().get(0)
                    .getDetails().get(0).getShippingAddress());
        }

        if (solditemlist.get(position).getOrderdetails().get(0).getDetails().get(0).getUsername() != null
                && !solditemlist.get(position).getOrderdetails().get(0).getDetails().get(0).getUsername().equals("")) {
            holder.username.setText("Purchased by" + " " +
                    solditemlist.get(position).getOrderdetails().get(0).getDetails().get(0).getUsername());
        }

        if (solditemlist.get(position).getMedia().getMediaThumb() != null
                && !solditemlist.get(position).getMedia().getMediaThumb().equals("")) {
            Picasso.with(context)
                    .load(solditemlist.get(position).getMedia().getMediaThumb())
                    .placeholder(R.mipmap.img)
                    .error(R.mipmap.img)
                    .resize(width, height)
                    .centerCrop()
                    .into(holder.imgitem);
        }
    }

    @Override
    public int getItemCount() {
        return solditemlist.size();
    }

    public class SoldItemHolder extends RecyclerView.ViewHolder {
        private TextView txtshippindaddress, username;
        private ImageView imgitem;

        public SoldItemHolder(View itemView) {
            super(itemView);
            txtshippindaddress = itemView.findViewById(R.id.txt_shipping_address);
            imgitem = itemView.findViewById(R.id.img_item);
            username = itemView.findViewById(R.id.txt_purchage_user);
        }
    }
}
