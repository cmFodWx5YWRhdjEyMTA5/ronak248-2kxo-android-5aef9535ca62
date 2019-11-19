package com.screamxo.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.BlockedList.Friend;
import com.screamxo.Activity.RahulWork.RejectSettingActivity;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Others.MyPopUpWindow;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.RoundedCornersTransform;
import com.screamxo.Utils.Validations;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Shubham Agarwal on 09/01/17.
 */

public class BlockedAdapter extends RecyclerView.Adapter<BlockedAdapter.RecyclerViewHolders> {
    private final String TAG = BlockedAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Friend> messageArrayList;
    private String otherUserName = "";
    private String otherUserPhoto = "";
    private int widthMedia, heightProfile, widthProfile;
    private CommonMethod commonMethod;
    private int selectPosition;
    private String inputFormate = "yyyy-MM-dd'T'HH:mm:ss";
    int height, width;

    public BlockedAdapter(Context context, ArrayList<Friend> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
        Validations validations = new Validations();
        Preferences preferences = new Preferences(context);
        String uid = preferences.getUserId();

        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.pic_holder_dashboard);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {
        }
    }

    @Override
    public BlockedAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.blocked_item, null);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(BlockedAdapter.RecyclerViewHolders holder, int position) {
        holder.txtName.setText(messageArrayList.get(position).getFname());
        String userPhoto = messageArrayList.get(position).getPhoto();
        if (userPhoto != null && !userPhoto.equals("")) {
            Picasso.with(context)
                    .load(userPhoto)
                    .error(R.mipmap.pic_holder_dashboard)
                    .placeholder(R.mipmap.pic_holder_dashboard)
                    .resize(width - 20, height - 20)
                    .transform(new RoundedCornersTransform())
                    .into(holder.imgPic);
        }

    }

    @Override
    public int getItemCount() {
        try {
            return messageArrayList.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName;
        ImageView imgPic;
        RelativeLayout rl_main;
        LinearLayout transperent_view;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgPic = itemView.findViewById(R.id.imgPic);
            rl_main = itemView.findViewById(R.id.rl_main);
            transperent_view = itemView.findViewById(R.id.transperent_view);
            rl_main.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_main:
                    openPopUpmenu();
                    break;
            }

        }

        void openPopUpmenu() {
            MyPopUpWindow popUpWindow = new MyPopUpWindow(context, transperent_view,
                    new String[]{context.getString(R.string.txt_choose_action),
                            context.getString(R.string.txt_unblock)}, rl_main, "RejectList");
            popUpWindow.show(rl_main, MyPopUpWindow.PopUpPosition.RIGHT);

            popUpWindow.setOnPopupItemClickListner(new MyPopUpWindow.OnPopupItemClickListener() {
                @Override
                public boolean onPopupItemClick(int position) {
                    rl_main.setBackground(context.getResources().getDrawable(R.drawable.ractangle));
                    switch (position) {
                        case 1:
                            ((RejectSettingActivity) context).initUnBlockApi(getLayoutPosition());
                            popUpWindow.dismiss();
                            break;

                    }
                    return true;
                }
            });
        }
    }
}

