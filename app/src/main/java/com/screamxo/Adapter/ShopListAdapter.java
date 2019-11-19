package com.screamxo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail_;
import com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail__;
import com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail___;
import com.screamxo.Activity.RahulWork.ItemDetailsAcitvity;
import com.screamxo.R;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS;

/**
 * Created by Shubham Agarwal on 13/10/16.
 */

public class ShopListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ShopListAdapter";
    private Context context;
    private ArrayList<Object> result;
    private int width, height;

    public ShopListAdapter(Context context, ArrayList<Object> result) {
        this.context = context;
        this.result = result;
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
            height = bd.getBitmap().getHeight() + 20;
            width = bd.getBitmap().getWidth() + 20;
        } catch (Exception ignored) {

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.shop_adapter_list_recycler, parent, false);
        return new ImageVideo(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + result.get(position).getClass().getSimpleName());

        if (result.get(position) instanceof com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail) {
            com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail itemdetail = (com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail) result.get(position);

            ((ImageVideo) holder).imgsoldout.setVisibility(itemdetail.getIspurchased() == 1 ? View.VISIBLE : View.GONE);

            if (itemdetail.getMediaUrl() != null && !itemdetail.getMediaUrl().isEmpty()) {
                Picasso.with(context)
                        .load(itemdetail.getMediaThumb())
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .resize(width, height)
                        .into(((ImageVideo) holder).imgThum);
            } else {
                Picasso.with(context)
                        .load(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .into(((ImageVideo) holder).imgThum);
            }

            ((ImageVideo) holder).itemView.setOnClickListener(view -> {
                Intent i = new Intent(context, ItemDetailsAcitvity.class);
                i.putExtra("itemid", "" + itemdetail.getItemId());
                ((Activity) context).startActivityForResult(i, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
            });

            if (itemdetail.getItemPrice() == null || Utils.getFormattedPrice(itemdetail.getItemPrice()) == null) {
                ((ImageVideo) holder).txtPrice.setVisibility(View.GONE);
            } else {
                ((ImageVideo) holder).txtPrice.setText(Utils.getFormattedPrice(itemdetail.getItemPrice()));
            }

            ((ImageVideo) holder).txtHeader.setText(itemdetail.getItemName());

        } else if (result.get(position) instanceof Itemdetail_) {
            Itemdetail_ itemdetail = (Itemdetail_) result.get(position);

            ((ImageVideo) holder).imgsoldout.setVisibility(itemdetail.getIspurchased().equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);

            if (itemdetail.getMediaUrl() != null && !itemdetail.getMediaUrl().isEmpty()) {
                Picasso.with(context)
                        .load(itemdetail.getMediaThumb())
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .resize(width, height)
                        .into(((ImageVideo) holder).imgThum);
            } else {
                Picasso.with(context)
                        .load(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .into(((ImageVideo) holder).imgThum);
            }

            ((ImageVideo) holder).itemView.setOnClickListener(view -> {
                Intent i = new Intent(context, ItemDetailsAcitvity.class);
                i.putExtra("itemid", "" + itemdetail.getItemId());
                ((Activity) context).startActivityForResult(i, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
            });

            if (itemdetail.getItemPrice() == null || Utils.getFormattedPrice(itemdetail.getItemPrice()) == null) {
                ((ImageVideo) holder).txtPrice.setVisibility(View.GONE);
            } else {
                ((ImageVideo) holder).txtPrice.setText(Utils.getFormattedPrice(itemdetail.getItemPrice()));
            }

            ((ImageVideo) holder).txtHeader.setText(itemdetail.getItemName());
        } else if (result.get(position) instanceof Itemdetail___) {
            Itemdetail___ itemdetail = (Itemdetail___) result.get(position);

            ((ImageVideo) holder).imgsoldout.setVisibility(itemdetail.getIspurchased() == 1 ? View.VISIBLE : View.GONE);

            if (itemdetail.getMediaUrl() != null && !itemdetail.getMediaUrl().isEmpty()) {
                Picasso.with(context)
                        .load(itemdetail.getMediaThumb())
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .resize(width, height)
                        .into(((ImageVideo) holder).imgThum);
            } else {
                Picasso.with(context)
                        .load(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .into(((ImageVideo) holder).imgThum);
            }

            ((ImageVideo) holder).itemView.setOnClickListener(view -> {
                Intent i = new Intent(context, ItemDetailsAcitvity.class);
                i.putExtra("itemid", "" + itemdetail.getItemId());
                ((Activity) context).startActivityForResult(i, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
            });

            if (itemdetail.getItemPrice() == null || Utils.getFormattedPrice(itemdetail.getItemPrice()) == null) {
                ((ImageVideo) holder).txtPrice.setVisibility(View.GONE);
            } else {
                ((ImageVideo) holder).txtPrice.setText(Utils.getFormattedPrice(itemdetail.getItemPrice()));
            }

            ((ImageVideo) holder).txtHeader.setText(itemdetail.getItemName());

        } else if (result.get(position) instanceof Itemdetail__) {
            Itemdetail__ itemdetail = (Itemdetail__) result.get(position);

            ((ImageVideo) holder).imgsoldout.setVisibility(itemdetail.getIspurchased() == 1 ? View.VISIBLE : View.GONE);

            if (itemdetail.getMediaUrl() != null && !itemdetail.getMediaUrl().isEmpty()) {
                Picasso.with(context)
                        .load(itemdetail.getMediaThumb())
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .resize(width, height)
                        .into(((ImageVideo) holder).imgThum);
            } else {
                Picasso.with(context)
                        .load(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .into(((ImageVideo) holder).imgThum);
            }

            ((ImageVideo) holder).itemView.setOnClickListener(view -> {
                Intent i = new Intent(context, ItemDetailsAcitvity.class);
                i.putExtra("itemid", "" + itemdetail.getItemId());
                ((Activity) context).startActivityForResult(i, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
            });

            if (itemdetail.getItemPrice() == null || Utils.getFormattedPrice(itemdetail.getItemPrice()) == null) {
                ((ImageVideo) holder).txtPrice.setVisibility(View.GONE);
            } else {
                ((ImageVideo) holder).txtPrice.setText(Utils.getFormattedPrice(itemdetail.getItemPrice()));
            }

            ((ImageVideo) holder).txtHeader.setText(itemdetail.getItemName());
        }
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    private class ImageVideo extends RecyclerView.ViewHolder {

        ImageView imgThum, imgsoldout;
        TextView txtPrice, txtHeader, txtData;

        ImageVideo(View v) {
            super(v);
            imgThum = v.findViewById(R.id.img_thum);
            imgsoldout = v.findViewById(R.id.img_sold_out);
            txtPrice = v.findViewById(R.id.txtPrice);
            txtHeader = v.findViewById(R.id.txtHeader);
        }
    }
}
