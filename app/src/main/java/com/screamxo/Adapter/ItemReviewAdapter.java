package com.screamxo.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.ItemReviewData;
import com.screamxo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Shubham.Agarwal on 12/12/16.
 */

public class ItemReviewAdapter extends RecyclerView.Adapter<ItemReviewAdapter.ItemReviewHolder> {

    private Context context;
    private ArrayList<ItemReviewData> reviewlist;
    private int height, width;

    public ItemReviewAdapter(Context context, ArrayList<ItemReviewData> reviewlist) {
        this.context = context;
        this.reviewlist = reviewlist;

        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
    }

    @Override
    public ItemReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sold_list_recycler, parent, false);
        return new ItemReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemReviewHolder holder, int position) {
        if (reviewlist.get(position).getRevieweusername() != null && !reviewlist.get(position).getRevieweusername().equals("")) {
            holder.username.setText(reviewlist.get(position).getRevieweusername());
        }

        if (reviewlist.get(position).getDescription() != null && !reviewlist.get(position).getDescription().equals("")) {
            holder.txtshippindaddress.setText(reviewlist.get(position).getDescription());
        }

        if (reviewlist.get(position).getReviewuserphotothumb() != null && !reviewlist.get(position).getReviewuserphotothumb().equals("")) {
            Picasso.with(context)
                    .load(reviewlist.get(position).getReviewuserphotothumb())
                    .placeholder(R.mipmap.profile_pic_placeholder)
                    .error(R.mipmap.profile_pic_placeholder)
                    .resize(width, height)
                    .centerCrop()
                    .into(holder.imgitem);
        }
    }


    @Override
    public int getItemCount() {
        return reviewlist.size();
    }

    public class ItemReviewHolder extends RecyclerView.ViewHolder {
        private TextView txtshippindaddress, username, txttrackingdetail;
        private ImageView imgitem;

        public ItemReviewHolder(View itemView) {
            super(itemView);
            txtshippindaddress = itemView.findViewById(R.id.txt_shipping_address);
            imgitem = itemView.findViewById(R.id.img_item);
            username = itemView.findViewById(R.id.txt_purchage_user);
            txttrackingdetail = itemView.findViewById(R.id.txt_tracking_detail);
            txttrackingdetail.setVisibility(View.GONE);
        }
    }
}

