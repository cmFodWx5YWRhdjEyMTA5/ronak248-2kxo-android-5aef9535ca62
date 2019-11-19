package com.screamxo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.screamxo.Interface.CartItemImage;
import com.screamxo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartCheckoutAdapter extends RecyclerView.Adapter<CartCheckoutAdapter.RecyclerViewHolders> {

    private Context context;
    private ArrayList<String> itemMedia;
    private CartItemImage cartItemImage;
    private ArrayList<String> arrayListPrices;
    private ArrayList<Integer> arrayListItemsId;
    private ArrayList<String> arrayListItemsName;
    private ArrayList<Integer> arrayListItemsQuentity;

    public CartCheckoutAdapter(Context context, ArrayList<String> itemMedia,
                               ArrayList<String> arrayListPrices,ArrayList<Integer> arrayListItemsId,ArrayList<String> arrayListItemsName,ArrayList<Integer> arrayListItemsQuentity, CartItemImage cartItemImage) {
        this.context = context;
        this.itemMedia = itemMedia;
        this.cartItemImage = cartItemImage;
        this.arrayListPrices = arrayListPrices;
        this.arrayListItemsId = arrayListItemsId;
        this.arrayListItemsName = arrayListItemsName;
        this.arrayListItemsQuentity = arrayListItemsQuentity;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout, null);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        if (itemMedia.get(position) != null) {
            Picasso.with(context)
                    .load(itemMedia.get(position))
                    .placeholder(R.mipmap.img_placeholder)
                    .error(R.mipmap.img_placeholder)
                    .resize(180,180)
                    .into(holder.itemImage);
        }

        if (arrayListPrices!=null && arrayListPrices.get(position)!=null){
            holder.txt_price.setText(arrayListPrices.get(position));
        }

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItemImage.ItemImage(itemMedia.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemMedia.size();
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView txt_price;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            txt_price = itemView.findViewById(R.id.txt_price);
        }
    }
}

