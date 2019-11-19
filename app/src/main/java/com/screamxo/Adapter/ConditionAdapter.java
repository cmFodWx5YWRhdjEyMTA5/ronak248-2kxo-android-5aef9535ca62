package com.screamxo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.screamxo.R;

/**
 * Created by android on 8/8/17.
 *
 * There is no use of this adapter in the project
 */
public class ConditionAdapter extends RecyclerView.Adapter<ConditionAdapter.RecyclerViewHolders> {
    private final String[] categoryNameList;
    private static final String TAG = "ConditionAdapter";

    public ConditionAdapter(Context context, String[] categoryNameList) {
        Context context1 = context;
        this.categoryNameList = categoryNameList;
    }


    @Override
    public ConditionAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_condition, null);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(ConditionAdapter.RecyclerViewHolders holder, int position) {
        try {
            holder.label_tv.setText(categoryNameList[position]);
            holder.desc_tv.setText(categoryNameList[position]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return categoryNameList.length;
        } catch (Exception e) {
            return 0;
        }
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder {
        TextView label_tv;
        TextView desc_tv;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            label_tv = itemView.findViewById(R.id.label_tv);
            desc_tv = itemView.findViewById(R.id.desc_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: ");
                }
            });
        }
    }
}
