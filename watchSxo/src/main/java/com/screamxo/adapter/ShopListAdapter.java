package com.screamxo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.screamxo.R;

import java.util.Arrays;
import java.util.List;

public final class ShopListAdapter extends WearableRecyclerView.Adapter<ShopListAdapter.WearCardViewHolder> {
    private Context context;
    private List<String> list;
    private LayoutInflater inflater;
    private ItemSelectedListener listener;

    public ShopListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = Arrays.asList(context.getResources().getStringArray(R.array.str_shop));
    }

    public void setListener(ItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public WearCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearCardViewHolder(inflater.inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(WearCardViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class WearCardViewHolder extends RecyclerView.ViewHolder {


        WearCardViewHolder(View itemView) {
            super(itemView);

        }

        void bind(final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemSelected(position);
                    }
                }
            });
        }
    }

    public interface ItemSelectedListener {
        void onItemSelected(int position);
    }
}
