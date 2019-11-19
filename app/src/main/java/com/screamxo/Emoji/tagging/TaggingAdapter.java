package com.screamxo.Emoji.tagging;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.Friend;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shubham Agarwal on 20/12/16.
 */
public class TaggingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private int width, height;
    private List<Friend> friendArrayList;
    private TagginListener listener;

    public TaggingAdapter(Context context, List<Friend> friendArrayList, TagginListener listener) {
        this.context = context;
        this.listener = listener;
        this.friendArrayList = friendArrayList;
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.watched_item_list_recycler, parent, false);
        return new FriendHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FriendHolder) {
            Friend friendbean = friendArrayList.get(position);

            ((FriendHolder) holder).imgRejectRequest.setVisibility(View.GONE);
            ((FriendHolder) holder).imgMsg.setVisibility(View.GONE);

            ((FriendHolder) holder).txtUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("Tag", friendbean.getUserid().toString());
                    if (listener != null) {
                        listener.onclick(friendbean);
                    }
                }
            });

            if (!TextUtils.isEmpty(friendbean.getUsername())) {
                ((FriendHolder) holder).txtUserName.setText(friendbean.getUsername());
            } else if (!TextUtils.isEmpty(friendbean.getFname())) {
                ((FriendHolder) holder).txtUserName.setText(friendbean.getFname());
            }
            if (friendbean.getPhoto() != null && !friendbean.getPhoto().equals("")) {
                Picasso.with(context)
                        .load(friendbean.getPhoto())
                        .placeholder(R.mipmap.user)
                        .error(R.mipmap.user)
                        .resize(width, height)
                        .centerCrop()
                        .transform(new CircleTransform())
                        .into(((FriendHolder) holder).imgUserProfile);
            } else {
                ((FriendHolder) holder).imgUserProfile.setImageDrawable(context.getResources().getDrawable(R.mipmap.user));
            }
        }
    }

    @Override
    public int getItemCount() {
        return friendArrayList.size();
    }

    public interface TagginListener {
        void onclick(Friend bean);
    }

    private class FriendHolder extends RecyclerView.ViewHolder {
        ImageView imgUserProfile, imgMsg, imgRejectRequest;
        TextView txtUserName;

        FriendHolder(View v) {
            super(v);
            imgUserProfile = v.findViewById(R.id.img_item);
            imgMsg = v.findViewById(R.id.img_delete);
            txtUserName = v.findViewById(R.id.txt_item_name);
            imgUserProfile.setImageResource(R.mipmap.user);
            imgRejectRequest = v.findViewById(R.id.img_reject);
        }
    }
}
