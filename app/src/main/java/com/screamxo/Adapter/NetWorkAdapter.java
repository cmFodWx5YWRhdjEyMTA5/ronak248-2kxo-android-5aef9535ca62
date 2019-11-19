package com.screamxo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.Notification;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Activity.PostDetailsActivity;
import com.screamxo.Activity.RahulWork.ItemDetailsAcitvity;
import com.screamxo.Activity.RahulWork.MediaItemDetailActivity;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.Validations;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS;

/**
 * Created by Shubham Agarwal on 16/01/17.
 */

public class NetWorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "NetWorkAdapter";
    public String type;
    Context context;
    private ArrayList<Notification> notificationArrayList;
    private Validations validations;
    private int heightProfile, widthProfile, heightItem, widthItem;

    public NetWorkAdapter(Context context, ArrayList<Notification> notificationArrayList) {
        this.context = context;
        this.notificationArrayList = notificationArrayList;
        validations = new Validations();

        BitmapDrawable bdItem = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.img_placeholder);
        BitmapDrawable bdUser = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.pic_holder_dashboard);
        heightProfile = bdUser.getBitmap().getHeight();
        widthProfile = bdUser.getBitmap().getWidth();

        heightItem = bdItem.getBitmap().getHeight();
        widthItem = bdItem.getBitmap().getWidth();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.network_adapter, parent, false);
        return new NetworkHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Notification notification = notificationArrayList.get(position);
        if (holder instanceof NetworkHolder) {
            String strNotification = notification.getNotificationsdetail();
            ((NetworkHolder) holder).txt_username.setVisibility(strNotification.contains("__username__") ? View.VISIBLE : View.GONE);


            ((NetworkHolder) holder).txt_username.setText(notification.getUserdata().getUsername());
            ((NetworkHolder) holder).txtNotification.setText(strNotification.replaceAll("__username__", ""));
            String inputFormate = "yyyy-MM-dd'T'HH:mm:ss";
            ((NetworkHolder) holder).txtTime.setText(validations.dateFormationUTCtoLocal(inputFormate, notification.getNotificationstime()));

            if (notification.getNotificationstype().equalsIgnoreCase("2")) {
                Picasso.with(context)
                        .load(R.drawable.chat_user)
                        .error(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .resize(widthItem - 20, heightItem - 20)
                        .into(((NetworkHolder) holder).imgItem);
            } else if (notification.getNotificationstype().equalsIgnoreCase("4")) {
                Picasso.with(context)
                        .load(R.drawable.add_user)
                        .error(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .resize(widthItem - 20, heightItem - 20)
                        .into(((NetworkHolder) holder).imgItem);
            } else {
                Picasso.with(context)
                        .load(R.drawable.chat_user)
                        .error(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .resize(widthItem - 20, heightItem - 20)
                        .into(((NetworkHolder) holder).imgItem);
            }
            ((NetworkHolder) holder).imgItem.setVisibility(View.GONE);

            String userPhto = notification.getUserdata().getUserphoto();
            if (userPhto != null && !userPhto.isEmpty()) {
                Picasso.with(context)
                        .load(userPhto)
                        .error(R.mipmap.pic_holder_dashboard)
                        .placeholder(R.mipmap.pic_holder_dashboard)
                        .resize(widthProfile + 10, heightProfile + 10)
                        .centerCrop()
                        .transform(new CircleTransform())
                        .into(((NetworkHolder) holder).imgUserProfile);
            }

            ((NetworkHolder) holder).itemView.setOnClickListener(view -> onClickItem(notification));
            ((NetworkHolder) holder).txtNotification.setOnClickListener(view -> onClickItem(notification));
            ((NetworkHolder) holder).txt_username.setOnClickListener(view -> onClickItem(notification));
            ((NetworkHolder) holder).txtTime.setOnClickListener(view -> onClickItem(notification));
            ((NetworkHolder) holder).imgItem.setOnClickListener(view -> onClickItem(notification));
            ((NetworkHolder) holder).imgUserProfile.setOnClickListener(view -> onClickItem(notification));
            ((NetworkHolder) holder).txtReply.setOnClickListener(view -> onClickItem(notification));

            switch (notification.getNotificationstype()) {
                case "1":
                    ((NetworkHolder) holder).txtReply.setVisibility(View.VISIBLE);
                    ((NetworkHolder) holder).imgItem.setVisibility(View.GONE);
                    ((NetworkHolder) holder).txtReply.setText(" View ");
                    ((NetworkHolder) holder).txtReply.setBackgroundColor(context.getResources().getColor(R.color.colorPink));
                    break;
                case "2":
                    ((NetworkHolder) holder).txtReply.setVisibility(View.VISIBLE);
                    ((NetworkHolder) holder).imgItem.setVisibility(View.GONE);
                    ((NetworkHolder) holder).txtReply.setText(" Reply ");
                    ((NetworkHolder) holder).txtReply.setBackgroundColor(context.getResources().getColor(R.color.colorgreen));
                    break;
                case "3":
                    ((NetworkHolder) holder).txtReply.setVisibility(View.GONE);
                    ((NetworkHolder) holder).imgItem.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    ((NetworkHolder) holder).txtReply.setVisibility(View.GONE);
                    ((NetworkHolder) holder).imgItem.setVisibility(View.GONE);
                    break;
                case "5":
                    ((NetworkHolder) holder).txtReply.setVisibility(View.GONE);
                    ((NetworkHolder) holder).imgItem.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }


    private void onClickItem(Notification notification) {
        String id = "9944";
        switch (notification.getNotificationstype()) {
            case "0":
                break;
            case "1":// like post
                if (notification.getPost_type() == 1) {
                    Intent intent1 = new Intent(context, PostDetailsActivity.class);

                    if (!String.valueOf(notification.getPostid()).equalsIgnoreCase("null")) {
                        id = String.valueOf(notification.getPostid());
                    } else
                        id = String.valueOf(notification.getNid());
                    intent1.putExtra("postId", id);
                    context.startActivity(intent1);
                } else {

                    Intent intent1 = new Intent(context, MediaItemDetailActivity.class);

                    if (!String.valueOf(notification.getPostid()).equalsIgnoreCase("null")) {
                        id = String.valueOf(notification.getPostid());
                    } else
                        id = String.valueOf(notification.getNid());
                    intent1.putExtra("itemId", id);
                    context.startActivity(intent1);
                }
                break;
            case "2":// commented on your post
                if (notification.getPost_type() == 0) {
                    Intent intent1 = new Intent(context, PostDetailsActivity.class);

                    if (!String.valueOf(notification.getPostid()).equalsIgnoreCase("null")) {
                        id = String.valueOf(notification.getPostid());
                    } else
                        id = String.valueOf(notification.getNid());
                    intent1.putExtra("postId", id);
                    context.startActivity(intent1);
                } else {

                    Intent intent1 = new Intent(context, MediaItemDetailActivity.class);

                    if (!String.valueOf(notification.getPostid()).equalsIgnoreCase("null")) {
                        id = String.valueOf(notification.getPostid());
                    } else
                        id = String.valueOf(notification.getNid());
                    intent1.putExtra("itemId", id);
                    context.startActivity(intent1);
                }
                break;
            case "3": // purchased your item
                Intent intent = new Intent(context, ItemDetailsAcitvity.class);
                intent.putExtra("itemid", "" + notification.getItemdata().getItemId());
                ((Activity) context).startActivityForResult(intent, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
                break;
            case "4": // sent you a friend request
                if (DrawerMainActivity.class == context.getClass()) {
                    ((DrawerMainActivity) context).changeFragmentFromNotificationFragment("4");
                }
                break;
            case "5": // accepted friendship
                if (DrawerMainActivity.class == context.getClass()) {
                    ((DrawerMainActivity) context).changeFragmentFromNotificationFragment("5");
                }
                break;
            case "6":
                // Chat page...
                break;
            case "7":
                // Tracking no. added
                Intent trackingIntent = new Intent(context, ItemDetailsAcitvity.class);
                trackingIntent.putExtra("itemid", "" + notification.getItemdata().getItemId());
                ((Activity) context).startActivityForResult(trackingIntent, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
                break;
            case "8":
                // requested to update tracking info for your product
                break;
            case "9":  //  You've been tagged in a post!
                Intent intent9 = new Intent(context, PostDetailsActivity.class);
                intent9.putExtra("postId", String.valueOf(notification.getItemdata().getPostId()));
                context.startActivity(intent9);
                break;
            case "10":   //  You are tagged in comment
                Intent intent10 = new Intent(context, PostDetailsActivity.class);
                intent10.putExtra("postId", String.valueOf(notification.getItemdata().getPostId()));
                context.startActivity(intent10);
                break;
        }
    }


    class NetworkHolder extends RecyclerView.ViewHolder {
        ImageView imgUserProfile;
        TextView txtNotification;
        TextView txt_username;
        TextView txtTime;
        ImageView imgItem;
        ImageView imgPlay;
        TextView txtReply;

        public NetworkHolder(View itemView) {
            super(itemView);
            imgUserProfile = itemView.findViewById(R.id.img_user_profile);
            txtNotification = itemView.findViewById(R.id.txt_notification);
            txt_username = itemView.findViewById(R.id.txt_username);
            txtTime = itemView.findViewById(R.id.txt_time);
            imgItem = itemView.findViewById(R.id.img_item);
            imgPlay = itemView.findViewById(R.id.img_play);
            txtReply = itemView.findViewById(R.id.txt_reply);
        }

    }
}
