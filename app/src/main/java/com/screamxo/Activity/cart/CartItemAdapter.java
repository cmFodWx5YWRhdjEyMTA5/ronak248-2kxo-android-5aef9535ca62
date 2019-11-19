package com.screamxo.Activity.cart;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.apimodule.ApiBase.ApiBean.cart.model.Item;
import com.screamxo.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int width = 150, height = 150;
    private ArrayList<Item> itemsArray;
    private ClickEvent clickEvent;

    public CartItemAdapter(Context context, ArrayList<Item> itemsArray, ClickEvent clickEvent) {
        this.context = context;
        this.itemsArray = itemsArray;
        this.clickEvent = clickEvent;
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
            height = bd.getBitmap().getHeight() + 40;
            width = bd.getBitmap().getWidth() + 95;
        } catch (Exception ignored) {
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.audio_video_recycler, parent, false);
        return new ImageVideo(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ImageVideo) {
            ((ImageVideo) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return itemsArray.size();
    }

    private class ImageVideo extends RecyclerView.ViewHolder {

        ImageView imgPlay, imgThum, imgsoldout;
        TextView txtPrice;

        ImageVideo(View v) {
            super(v);
            imgPlay = v.findViewById(R.id.img_play);
            imgThum = v.findViewById(R.id.img_thum);
            imgsoldout = v.findViewById(R.id.img_sold_out);
            txtPrice = v.findViewById(R.id.txtPrice);

            txtPrice.setGravity(Gravity.CENTER);
            imgPlay.setVisibility(View.GONE);
            imgsoldout.setVisibility(View.GONE);
        }

        public void bind(int position) {
            try {
                Picasso.with(context)
                        .load(itemsArray.get(position).getIimage().get(0).getMediaThumb())
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .resize(width, height)
                        .into(imgThum, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Glide.with(context)
                                        .load(itemsArray.get(position).getIimage().get(0).getMediaThumb())
                                            .apply(new RequestOptions().fitCenter().centerCrop().override(width,height).error(R.mipmap.img_placeholder))
                                        .into(imgThum);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                txtPrice.setText("$"+String.valueOf(itemsArray.get(position).getItemPrice()));
                txtPrice.setWidth(width);
            } catch (Exception e) {
                e.printStackTrace();
            }
            imgThum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickEvent.onClick(itemsArray.get(position).getIimage().get(0).getMediaUrl(), position);
                }
            });
        }
    }
}
