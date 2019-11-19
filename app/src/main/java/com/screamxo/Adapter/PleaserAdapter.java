package com.screamxo.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.Message;
import com.screamxo.Activity.ChatActivity;
import com.screamxo.Emoji.CustomText;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.Validations;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CHAT_ACTIVITY_RESULTS;

/**
 * Created by Shubham Agarwal on 13/01/17.
 */

public class PleaserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public String type;
    Context context;
    private ArrayList<Message> messageArrayList;
    private Validations validations;
    private int heightProfile, widthProfile, heightItem, widthItem;

    // type: 0 = Pleaser 1 = Business
    public PleaserAdapter(Context context, ArrayList<Message> messageArrayList) {

        this.context = context;
        this.messageArrayList = messageArrayList;
        validations = new Validations();

        BitmapDrawable bdUser = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.pic_holder_dashboard);
        BitmapDrawable bdItem = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.img_placeholder);

        heightItem = bdItem.getBitmap().getHeight();
        widthItem = bdItem.getBitmap().getWidth();
        heightProfile = bdUser.getBitmap().getHeight();
        widthProfile = bdUser.getBitmap().getWidth();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pleaser_chat_header, parent, false);
        return new PleaserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PleaserViewHolder) holder).bindTo(messageArrayList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public class PleaserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_user_profile)
        ImageView imgUserProfile;
        @BindView(R.id.txt_user_name)
        TextView txtUserName;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_description)
        TextView txtDescription;
        @BindView(R.id.txt_msg)
        CustomText txtMsg;
        @BindView(R.id.lny_main)
        LinearLayout linearLayout;

        PleaserViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @SuppressLint("SetTextI18n")
        void bindTo(Message message, int position) {
//        type: 0 = Pleaser,  1 = Business
            switch (type) {
                case "0":
                    if (!TextUtils.isEmpty(message.getUsername())) {
                        txtUserName.setText(message.getUsername());
                    } else {
                        txtUserName.setText(message.getFname() + " " + message.getLname());
                    }

                    txtDescription.setVisibility(View.GONE);

                    try {
                        String userUrl = TextUtils.isEmpty(message.getUserphoto()) ? null : message.getUserphoto();
                        Picasso.with(context)
                                .load(userUrl)
                                .error(R.mipmap.pic_holder_dashboard)
                                .placeholder(R.mipmap.pic_holder_dashboard)
                                .resize(widthProfile + 20, heightProfile + 20)
                                .centerCrop()
                                .transform(new CircleTransform())
                                .into(imgUserProfile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "1":
                    txtDescription.setVisibility(View.VISIBLE);
                    txtDescription.setText(message.getUsername());
                    txtUserName.setText(message.getItemname());
                    if (message.getItemmedia() != null && !message.getItemmedia().equals("")) {
                        Picasso.with(context)
                                .load(message.getItemmedia())
                                .error(R.mipmap.img_placeholder)
                                .placeholder(R.mipmap.img_placeholder)
                                .resize(widthItem, heightItem)
                                .centerCrop()
                                .transform(new CircleTransform())
                                .into(imgUserProfile, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                                                R.mipmap.img_placeholder);
                                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), icon);
                                        drawable.setCircular(true);
                                        imgUserProfile.setImageDrawable(drawable);
                                    }
                                });
                    } else {
                        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_placeholder);
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), icon);
                        drawable.setCircular(true);
                        imgUserProfile.setImageDrawable(drawable);
                    }

                    break;
            }

            String inputFormate = "yyyy-MM-dd'T'HH:mm:ss";
//            2018-04-05T18:20:10.000Z
            txtTime.setText(validations.dateFormationUTCtoLocal(inputFormate, message.getMessagedate()));

            if (message.getMediatype() != null) {


                if (message.getMediatype().contains("text")) {
                    txtMsg.setText(message.getMessagetext());
                } else if (message.getMediatype().contains("video")) {
                    txtMsg.setText("video");
                } else {
                    txtMsg.setText("Image");
                }
            } else {
                txtMsg.setText("Image");
            }
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("otherUid", message.getUserid());
                    intent.putExtra("username", message.getUsername());
                    intent.putExtra("userProfile", message.getUserphoto());
                    intent.putExtra("fullname", message.getFname() + " " + message.getLname());
                    switch (type) {
                        case "1":
                            intent.putExtra("itemId", message.getItemid());
                            break;
                    }
                    ((Activity) context).startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
                }
            });
        }
    }

}
