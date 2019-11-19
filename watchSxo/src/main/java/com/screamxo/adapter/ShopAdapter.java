package com.screamxo.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.screamxo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ShopAdapter extends WearableRecyclerView.Adapter<ShopAdapter.WearCardViewHolder> {
    private Context context;
    private List<String> list;
    private LayoutInflater inflater;
    private List<Boolean> listStatus;
    private TypedArray unSelectedIcon;
    private TypedArray selectedIcon;
    private ItemSelectedListener listener;

    public ShopAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = Arrays.asList(context.getResources().getStringArray(R.array.str_shop));
        unSelectedIcon = context.getResources().obtainTypedArray(R.array.un_shop);
        selectedIcon = context.getResources().obtainTypedArray(R.array.sl_shop);
        listStatus = new ArrayList<>();
        Boolean[] integers = new Boolean[list.size()];
        Arrays.fill(integers, false);
        listStatus = Arrays.asList(integers);
    }

    public void setListener(ItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public WearCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearCardViewHolder(inflater.inflate(R.layout.item_shop, parent, false));
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
        private TextView txtName;
        private ImageView imgIcon;

        WearCardViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
        }

        void bind(final int position) {
            if(listStatus.get(position)) {
                txtName.setText(list.get(position));
                imgIcon.setImageResource(selectedIcon.getResourceId(position, -1));
                txtName.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            } else {
                txtName.setText(list.get(position));
                imgIcon.setImageResource(unSelectedIcon.getResourceId(position, -1));
                txtName.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listStatus.get(position)) {
                        Boolean[] integers = new Boolean[list.size()];
                        Arrays.fill(integers, false);
                        listStatus = Arrays.asList(integers);
                    } else {
                        Boolean[] integers = new Boolean[list.size()];
                        Arrays.fill(integers, false);
                        listStatus = Arrays.asList(integers);
                        listStatus.set(position, true);
                    }

                    if(listener != null) {
                        notifyDataSetChanged();
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
