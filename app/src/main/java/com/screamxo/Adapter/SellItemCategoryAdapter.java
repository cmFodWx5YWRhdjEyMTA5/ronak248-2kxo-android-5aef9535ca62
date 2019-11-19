package com.screamxo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.screamxo.Others.OnBottomSheetItemClickListener;
import com.screamxo.R;
import java.util.ArrayList;
import java.util.List;

public class SellItemCategoryAdapter extends RecyclerView.Adapter<SellItemCategoryAdapter.ViewHolder> {
    private static final String TAG = "BottomSheetAdapter";
    private final Context context;
    private final ArrayList<String> inputList;
    private final ArrayList<String> inputIds;
    private OnBottomSheetItemClickListener onBottomSheetItemClickListener;

    public SellItemCategoryAdapter(Context context, List<String> inputList, List<String> inputIds,OnBottomSheetItemClickListener onBottomSheetItemClickListener) {
        this.context = context;
        this.inputList = (ArrayList<String>) inputList;
        this.inputIds = (ArrayList<String>) inputIds;
        this.onBottomSheetItemClickListener = onBottomSheetItemClickListener;
    }

    @Override
    public SellItemCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_sell_item_category, parent, false);
        return new SellItemCategoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SellItemCategoryAdapter.ViewHolder holder, int position) {
        holder.tv_content.setText(inputList.get(holder.getAdapterPosition()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputId = null;
                try {
                    inputId = inputIds.get(holder.getAdapterPosition());
                } catch (Exception ignored) {
                }
                onBottomSheetItemClickListener.onItemClick(inputList.get(holder.getAdapterPosition()), inputId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inputList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_content;

        ViewHolder(View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }
}
