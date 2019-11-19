package com.screamxo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.apimodule.ApiBase.ApiBean.ItemDetailMedia;
import com.screamxo.Interface.CustomItemClickListener;
import com.screamxo.R;
import com.screamxo.Utils.MyApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SONY on 18-03-2018.
 */

public class CheckoutRecyclerAdapter extends RecyclerView.Adapter<CheckoutRecyclerAdapter.ItemViewHolder>{
    Context context;
    ArrayList<String> arrayListChild;
    CustomItemClickListener listener;

    public CheckoutRecyclerAdapter(Context context, ArrayList<String> arrayListChild, CustomItemClickListener listener){
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

//        for (int i=0;i<(((MyApplication)context.getApplicationContext()).cartGlobalArray.size());i++){
//            String mediaThumb = ((MyApplication)context.getApplicationContext()).cartGlobalArray.get(i).getMedia().get(0).getMediaThumb();
            Picasso.with(context).load(arrayListChild.get(position).toString()).into(holder.imgChild);
      //  }
        //ItemDetailMedia procuct = arrayListChild.get(position);

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
