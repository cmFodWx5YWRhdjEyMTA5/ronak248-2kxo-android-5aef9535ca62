package com.screamxo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail_;
import com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail__;
import com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail___;
import com.screamxo.Interface.AudioVideoInterface;
import com.screamxo.R;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Shubham Agarwal on 13/10/16.
 */

public class ImageVideoShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Object> result;
    private int width, height;
    private AudioVideoInterface commonMethod;

    public ImageVideoShopAdapter(Context context, ArrayList<Object> result, AudioVideoInterface commonMethod) {
        this.context = context;
        this.result = result;
        this.commonMethod = commonMethod;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.shop_adapter_recycler, parent, false);
        return new ImageVideo(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (result.get(holder.getAdapterPosition()) instanceof com.example.apimodule.ApiBase.ApiBean.Shop.Item) {
            ((ImageVideo) holder).imgPlay.setVisibility(View.GONE);
            if (((com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail) result.get(holder.getAdapterPosition())).getIspurchased() == 1) {
                ((ImageVideo) holder).imgsoldout.setVisibility(View.VISIBLE);
            } else {
                ((ImageVideo) holder).imgsoldout.setVisibility(View.GONE);
            }
            com.example.apimodule.ApiBase.ApiBean.Shop.Item itemdetail = (com.example.apimodule.ApiBase.ApiBean.Shop.Item) result.get(holder.getAdapterPosition());
            if (itemdetail.getMediaUrl() != null && !itemdetail.getMediaUrl().equals("")) {
                Picasso.with(context)
                        .load(itemdetail.getMediaUrl())
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .fit()
                        .into(((ImageVideo) holder).imgThum);
            } else {
                Picasso.with(context)
                        .load(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .fit()
                        .into(((ImageVideo) holder).imgThum);
            }
            ((ImageVideo) holder).imgThum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //      if (itemdetail.getMediaType().contains("image") && !itemdetail.getMediaUrl().equals(""))
                    commonMethod.Image(itemdetail.getMediaUrl(), "" + itemdetail.getItemId(), holder.getAdapterPosition(), "1");
                }
            });

            if (itemdetail.getItemPrice() == null || Utils.getFormattedPrice(itemdetail.getItemPrice()) == null) {
                ((ImageVideo) holder).txtPrice.setVisibility(View.GONE);
            } else {
                ((ImageVideo) holder).txtPrice.setText(Utils.getFormattedPrice(itemdetail.getItemPrice()));
            }

        } else if (result.get(holder.getAdapterPosition()) instanceof com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail) {
            ((ImageVideo) holder).imgPlay.setVisibility(View.GONE);
            if (((com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail) result.get(holder.getAdapterPosition())).getIspurchased() == 1) {
                ((ImageVideo) holder).imgsoldout.setVisibility(View.VISIBLE);
            } else {
                ((ImageVideo) holder).imgsoldout.setVisibility(View.GONE);
            }
            com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail itemdetail = (com.example.apimodule.ApiBase.ApiBean.ShopData.Itemdetail) result.get(holder.getAdapterPosition());
            if (itemdetail.getMediaUrl() != null && !itemdetail.getMediaUrl().equals("")) {
                Picasso.with(context)
                        .load(itemdetail.getMediaUrl())
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .fit()
                        .into(((ImageVideo) holder).imgThum);
            } else {
                Picasso.with(context)
                        .load(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .fit()
                        .into(((ImageVideo) holder).imgThum);
            }
            ((ImageVideo) holder).imgThum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //      if (itemdetail.getMediaType().contains("image") && !itemdetail.getMediaUrl().equals(""))
                    commonMethod.Image(itemdetail.getMediaUrl(), "" + itemdetail.getItemId(), holder.getAdapterPosition(), "1");
                }
            });

            if (itemdetail.getItemPrice() == null || Utils.getFormattedPrice(itemdetail.getItemPrice()) == null) {
                ((ImageVideo) holder).txtPrice.setVisibility(View.GONE);
            } else {
                ((ImageVideo) holder).txtPrice.setText(Utils.getFormattedPrice(itemdetail.getItemPrice()));
            }
        } else if (result.get(holder.getAdapterPosition()) instanceof Itemdetail_) {
            ((ImageVideo) holder).imgPlay.setVisibility(View.GONE);
            if (((Itemdetail_) result.get(holder.getAdapterPosition())).getIspurchased().equalsIgnoreCase("1")) {
                ((ImageVideo) holder).imgsoldout.setVisibility(View.VISIBLE);
            } else {
                ((ImageVideo) holder).imgsoldout.setVisibility(View.GONE);
            }
            Itemdetail_ itemdetail = (Itemdetail_) result.get(holder.getAdapterPosition());
            if (itemdetail.getMediaUrl() != null && !itemdetail.getMediaUrl().equals("")) {
                Picasso.with(context)
                        .load(itemdetail.getMediaUrl())
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .fit()
                        .into(((ImageVideo) holder).imgThum);
            } else {
                Picasso.with(context)
                        .load(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .fit()
                        .into(((ImageVideo) holder).imgThum);
            }
            ((ImageVideo) holder).imgThum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //      if (itemdetail.getMediaType().contains("image") && !itemdetail.getMediaUrl().equals(""))
                    commonMethod.Image(itemdetail.getMediaUrl(), "" + itemdetail.getItemId(), holder.getAdapterPosition(), "1");
                }
            });

            if (itemdetail.getItemPrice() == null || Utils.getFormattedPrice(itemdetail.getItemPrice()) == null) {
                ((ImageVideo) holder).txtPrice.setVisibility(View.GONE);
            } else {
                ((ImageVideo) holder).txtPrice.setText(Utils.getFormattedPrice(itemdetail.getItemPrice()));
            }

        } else if (result.get(holder.getAdapterPosition()) instanceof Itemdetail___) {
            ((ImageVideo) holder).imgPlay.setVisibility(View.GONE);
            if (((Itemdetail___) result.get(holder.getAdapterPosition())).getIspurchased() == 1) {
                ((ImageVideo) holder).imgsoldout.setVisibility(View.VISIBLE);
            } else {
                ((ImageVideo) holder).imgsoldout.setVisibility(View.GONE);
            }
            Itemdetail___ itemdetail = (Itemdetail___) result.get(holder.getAdapterPosition());
            if (itemdetail.getMediaUrl() != null && !itemdetail.getMediaUrl().equals("")) {
                Picasso.with(context)
                        .load(itemdetail.getMediaUrl())
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .fit()
                        .into(((ImageVideo) holder).imgThum);
            } else {
                Picasso.with(context)
                        .load(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .fit()
                        .into(((ImageVideo) holder).imgThum);
            }
            ((ImageVideo) holder).imgThum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //      if (itemdetail.getMediaType().contains("image") && !itemdetail.getMediaUrl().equals(""))
                    commonMethod.Image(itemdetail.getMediaUrl(), "" + itemdetail.getItemId(), holder.getAdapterPosition(), "1");
                }
            });
            if (itemdetail.getItemPrice() == null || Utils.getFormattedPrice(itemdetail.getItemPrice()) == null) {
                ((ImageVideo) holder).txtPrice.setVisibility(View.GONE);
            } else {
                ((ImageVideo) holder).txtPrice.setText(Utils.getFormattedPrice(itemdetail.getItemPrice()));
            }
        } else if (result.get(holder.getAdapterPosition()) instanceof Itemdetail__) {
            ((ImageVideo) holder).imgPlay.setVisibility(View.GONE);
            if (((Itemdetail__) result.get(holder.getAdapterPosition())).getIspurchased() == 1) {
                ((ImageVideo) holder).imgsoldout.setVisibility(View.VISIBLE);
            } else {
                ((ImageVideo) holder).imgsoldout.setVisibility(View.GONE);
            }
            Itemdetail__ itemdetail = (Itemdetail__) result.get(holder.getAdapterPosition());
            if (itemdetail.getMediaUrl() != null && !itemdetail.getMediaUrl().equals("")) {
                Picasso.with(context)
                        .load(itemdetail.getMediaUrl())
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .fit()
                        .into(((ImageVideo) holder).imgThum);
            } else {
                Picasso.with(context)
                        .load(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .fit()
                        .into(((ImageVideo) holder).imgThum);
            }
            ((ImageVideo) holder).imgThum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //      if (itemdetail.getMediaType().contains("image") && !itemdetail.getMediaUrl().equals(""))
                    commonMethod.Image(itemdetail.getMediaUrl(), "" + itemdetail.getItemId(), holder.getAdapterPosition(), "1");
                }
            });
            if (itemdetail.getItemPrice() == null || Utils.getFormattedPrice(itemdetail.getItemPrice()) == null) {
                ((ImageVideo) holder).txtPrice.setVisibility(View.GONE);
            } else {
                ((ImageVideo) holder).txtPrice.setText(Utils.getFormattedPrice(itemdetail.getItemPrice()));
            }

        }

    }


    @Override
    public int getItemCount() {
        return result.size();
    }

    private class ImageVideo extends RecyclerView.ViewHolder {

        ImageView imgPlay, imgThum, imgsoldout;
        TextView txtPrice;

        ImageVideo(View v)
        {
            super(v);
            imgPlay = v.findViewById(R.id.img_play);
            imgThum = v.findViewById(R.id.img_thum);
            imgsoldout = v.findViewById(R.id.img_sold_out);
            txtPrice = v.findViewById(R.id.txtPrice);
        }
    }
}
