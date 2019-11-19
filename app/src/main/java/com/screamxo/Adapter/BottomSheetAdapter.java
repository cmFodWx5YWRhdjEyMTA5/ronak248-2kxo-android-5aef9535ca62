package com.screamxo.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.screamxo.Others.OnBottomSheetItemClickListener;
import com.screamxo.R;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> {
    private static final String TAG = "BottomSheetAdapter";
    private final Context context;
    private final ArrayList<String> inputList;
    private final ArrayList<String> inputIds;
    private final ArrayList<Integer> imageIds;
    private OnBottomSheetItemClickListener onBottomSheetItemClickListener;

    public BottomSheetAdapter(Context context, List<String> inputList, List<String> inputIds, ArrayList<Integer> imageIds, OnBottomSheetItemClickListener onBottomSheetItemClickListener) {
        this.context = context;
        this.inputList = (ArrayList<String>) inputList;
        this.inputIds = (ArrayList<String>) inputIds;
        this.imageIds = imageIds;
        this.onBottomSheetItemClickListener = onBottomSheetItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_bottom_sheet, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_content.setText(inputList.get(holder.getAdapterPosition()));
        //holder.tv_content.setTextColor(Color.parseColor("#ffe81c4f"));

        try {
            holder.iv_image.setVisibility(View.VISIBLE);
            holder.iv_image.setImageDrawable(ContextCompat.getDrawable(context, imageIds.get(holder.getAdapterPosition())));
        } catch (Exception e) {
            holder.iv_image.setVisibility(View.GONE);
        }

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

        final ImageView iv_image;
        final TextView tv_content;

        ViewHolder(View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }
}
