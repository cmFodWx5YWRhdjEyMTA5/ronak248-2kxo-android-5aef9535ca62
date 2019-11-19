package com.screamxo.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.ItemReviewData;
import com.screamxo.Emoji.CustomText;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 8/8/17.
 */
public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.RecyclerViewHolders> {
    private int height, width;
    private Context context;
    private List<ItemReviewData> listData;

    public ItemDetailAdapter(Context context, ArrayList<ItemReviewData> ItemReviewData) {
        this.context = context;
        this.listData = ItemReviewData;
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
    }


    @Override
    public ItemDetailAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_adapter, null);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(ItemDetailAdapter.RecyclerViewHolders holder, int position) {
        try {
            holder.txt_time.setVisibility(View.GONE);
            holder.txt_view_more.setVisibility(View.GONE);
            holder.txt_user_name.setText(listData.get(position).getRevieweusername());
            holder.txt_comment.setText(listData.get(position).getDescription());
            String imgurl = listData.get(position).getReviewuserphoto();
            if (imgurl != null && !imgurl.isEmpty()) {
                Picasso.with(context)
                        .load(imgurl)
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .resize(width, height)
                        .transform(new CircleTransform())
                        .into(holder.img_user_profile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return listData.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder {
        TextView txtData, txt_view_more, txt_time, txt_user_name;
        CustomText txt_comment;
        ImageView img_user_profile;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            txt_view_more = itemView.findViewById(R.id.txt_view_more);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            img_user_profile = itemView.findViewById(R.id.img_user_profile);
        }
    }
}
