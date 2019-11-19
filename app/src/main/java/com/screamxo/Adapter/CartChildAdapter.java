package com.screamxo.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.apimodule.ApiBase.ApiBean.ItemDetailMedia;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailResult;
import com.screamxo.Interface.CustomItemClickListener;
import com.screamxo.Model.CartChildImageProcuct;
import com.screamxo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by SONY on 18-03-2018.
 */

public class CartChildAdapter extends RecyclerView.Adapter<CartChildAdapter.ItemViewHolder>{
    Context context;
    ArrayList<ItemDetailMedia> arrayListChild;
    CustomItemClickListener listener;

    public CartChildAdapter(Context context,ArrayList<ItemDetailMedia> arrayListChild,CustomItemClickListener listener){
        this.context = context;
        this.arrayListChild = arrayListChild;
        this.listener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_child_product, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,holder.getPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ItemDetailMedia procuct = arrayListChild.get(position);
        Picasso.with(context).load(procuct.getMediaThumb()).into(holder.imgChild);
    }

    @Override
    public int getItemCount() {
        if(arrayListChild != null){
            return arrayListChild.size();
        }else {
            return 0;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imgChild;
        public ItemViewHolder(View itemView) {
            super(itemView);

            imgChild = itemView.findViewById(R.id.imgChildProduct);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
