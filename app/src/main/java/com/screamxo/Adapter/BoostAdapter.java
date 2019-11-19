package com.screamxo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.screamxo.Activity.RahulWork.BoostActivity;
import com.screamxo.R;
import com.screamxo.Utils.StaticConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 8/8/17.
 */
public class BoostAdapter extends RecyclerView.Adapter<BoostAdapter.RecyclerViewHolders> {
    private Context context;
    private List<String> listData;
    private int screen = 0;

    public BoostAdapter(Context context, ArrayList<String> listData, int screen) {
        this.context = context;
        this.listData = listData;
        this.screen = screen;
    }

    @Override
    public BoostAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == StaticConstant.BOOST_TYPE_DAYS) {
            return new RecyclerViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_boost_days, parent, false));
        } else {
            return new RecyclerViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_boost_price, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return screen;
    }

    @Override
    public void onBindViewHolder(BoostAdapter.RecyclerViewHolders holder, int position) {
        holder.txtData.setText(listData.get(position));
    }

    @Override
    public int getItemCount() {
        try {
            return listData.size();
        } catch (Exception e) {
            return 0;
        }
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder {
        TextView txtData;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            txtData = itemView.findViewById(R.id.txtData);
            txtData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listData.get(getLayoutPosition()).isEmpty()) {
                        return;
                    }
                    ((BoostActivity) context).onRecyclerClicked(screen, listData.get(getLayoutPosition()));
                }
            });
        }
    }
}
