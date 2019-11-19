package com.screamxo.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.PostDetails.Comment;
import com.screamxo.Emoji.CustomText;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.Validations;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostReviewAdapter extends RecyclerView.Adapter<PostReviewAdapter.ItemReviewHolder> {

    private Context context;
    private ArrayList<Comment> reviewlist;
    private int height, width;
    private Validations validations;

    public PostReviewAdapter(Context context, ArrayList<Comment> reviewlist) {
        this.context = context;
        this.reviewlist = reviewlist;
        validations = new Validations();
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
    }

    @Override
    public ItemReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.postreview_recycler, parent, false);
        return new ItemReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemReviewHolder holder, int position) {
        if (reviewlist.get(position).getUsername() != null && !reviewlist.get(position).getUsername().equals("")) {
            holder.username.setText(reviewlist.get(position).getUsername());
        }

        if (reviewlist.get(position).getCommentdesc() != null && !reviewlist.get(position).getCommentdesc().equals("")) {
            holder.txtDescription.setText(reviewlist.get(position).getCommentdesc());
        }

        if (reviewlist.get(position).getUserphoto() != null && !reviewlist.get(position).getUserphoto().equals("")) {
            Picasso.with(context)
                    .load(reviewlist.get(position).getUserphoto())
                    .placeholder(R.mipmap.user)
                    .error(R.mipmap.user)
                    .transform(new CircleTransform())
                    .into(holder.imgitem);
        }

        if (reviewlist.get(position).getCommenttime() != null && !reviewlist.get(position).getCommenttime().equalsIgnoreCase("")) {
            String inputFormate = "yyyy-MM-dd'T'HH:mm:ss";
            holder.txtTime.setText(validations.dateFormationUTCtoLocal(inputFormate, reviewlist.get(position).getCommenttime()));
        }

    }


    @Override
    public int getItemCount() {
        return reviewlist.size();
    }

    public class ItemReviewHolder extends RecyclerView.ViewHolder {
        private CustomText txtDescription;
        private TextView username, txtTime;
        private ImageView imgitem;

        public ItemReviewHolder(View itemView) {
            super(itemView);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imgitem = itemView.findViewById(R.id.img_item);
            username = itemView.findViewById(R.id.txt_name);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}

