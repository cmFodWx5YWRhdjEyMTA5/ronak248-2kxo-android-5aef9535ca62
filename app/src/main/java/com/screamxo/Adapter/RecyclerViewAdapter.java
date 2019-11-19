package com.screamxo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Others.MyPopUpWindow;
import com.screamxo.R;
import com.screamxo.Utils.StaticConstant;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<MyPopUpWindow.CheckboxResponse> str;
    MyPopUpWindow myPopUpWindow;
    private boolean onBind = false;

    public RecyclerViewAdapter(Context context, ArrayList<MyPopUpWindow.CheckboxResponse> str, MyPopUpWindow myPopUpWindow) {
        this.context = context;
        this.str = str;
        this.myPopUpWindow = myPopUpWindow;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.setting_filter, parent, false);
        return new HolderClass(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBind = false;
        ((HolderClass) holder).txt.setText(str.get(position).getData());
        if (str.get(position).getFlag()) {
            ((HolderClass) holder).chCheck.setChecked(true);
        } else {
            ((HolderClass) holder).chCheck.setChecked(false);
        }
        onBind = true;
    }

    @Override
    public int getItemCount() {
        return str.size();
    }

    private void countCheck(boolean isChecked, int layoutPosition) {
        str.get(layoutPosition).setFlag(true);
        (myPopUpWindow).setCount(layoutPosition);
        StaticConstant.from_posi = str.get(layoutPosition).getId();
        ((DrawerMainActivity) context).callDashboardAPi(str.get(layoutPosition).getId());
    }

    private class HolderClass extends RecyclerView.ViewHolder {
        private TextView txt;
        private ImageView img;
        private CheckBox chCheck;

        public HolderClass(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt_no_data);
            img = itemView.findViewById(R.id.img_no_data);
            chCheck = itemView.findViewById(R.id.chCheck);
            img.setVisibility(View.GONE);

            CompoundButton.OnCheckedChangeListener changeListener = (buttonView, isChecked) -> {
                if (onBind) {
                    countCheck(isChecked, getLayoutPosition());
                }
            };

            chCheck.setOnCheckedChangeListener(changeListener);

        }
    }
}