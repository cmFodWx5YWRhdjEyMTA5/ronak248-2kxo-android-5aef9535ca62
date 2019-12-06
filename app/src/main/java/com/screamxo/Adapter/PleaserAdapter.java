package com.screamxo.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.GetUserDetailBean;
import com.example.apimodule.ApiBase.ApiBean.Message;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.google.gson.JsonElement;
import com.screamxo.Activity.ChatActivity;
import com.screamxo.Emoji.CustomText;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.Validations;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

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
    private CommonMethod commonMethod;
    FetchrServiceBase mService = new FetchrServiceBase();
    Call<JsonElement> deleteCall;
    Call<GetUserDetailBean> blockApi;

    // type: 0 = Pleaser 1 = Business
    public PleaserAdapter(Context context, ArrayList<Message> messageArrayList, CommonMethod commonMethod) {

        this.context = context;
        this.messageArrayList = messageArrayList;
        this.commonMethod = commonMethod;
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

                    if (!TextUtils.isEmpty(message.getUsername())) {
                        txtUserName.setText(message.getUsername());
                    } else {
                        txtUserName.setText(message.getFname() + " " + message.getLname());
                    }


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
                            intent.putExtra("createdBy", message.getItemCreatedBy());
                            break;
                    }
                    ((Activity) context).startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
                }
            });

            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    PopupMenu popup = new PopupMenu(context, txtTime);
                    popup.getMenuInflater().inflate(R.menu.menu_chat_options, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.pop_up_detele:
                                    if (type.equals("0"))
                                        callDeleteChatApi("", message.getUserid());
                                    else
                                        callDeleteChatApi(message.getItemid(), message.getUserid());

                                    break;

                                case R.id.pop_up_block:
                                    callBlockUserApi(message.getUserid());
                                    break;

                                case R.id.pop_up_spam:
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                    return true;
                }
            });
        }
    }

    public void callDeleteChatApi(String itemId, String otherUid) {
        if (Utils.isInternetOn(context)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("other_user_id", otherUid);

            if (deleteCall != null)
                deleteCall.cancel();

            switch (type) {
                case "0":
                    deleteCall = mService.getFetcherService(context).deleteNormalChat(map);
                    break;
                case "1":
                    map.put("itemid", itemId);
                    deleteCall = mService.getFetcherService(context).deleteBusinessChat(map);
                    break;
            }

            commonMethod.commonMethod("3");

            deleteCall.enqueue(new retrofit2.Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    commonMethod.commonMethod("0");
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    commonMethod.commonMethod("15");
                }
            });
        } else
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
    }

    public void callBlockUserApi(String otherUid) {
        if (Utils.isInternetOn(context)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("toid", otherUid);

            if (blockApi != null)
                blockApi.cancel();

            blockApi = mService.getFetcherService(context).blockChatUser(map);

            commonMethod.commonMethod("3");

            blockApi.enqueue(new retrofit2.Callback<GetUserDetailBean>() {
                @Override
                public void onResponse(Call<GetUserDetailBean> call, Response<GetUserDetailBean> response) {
                    commonMethod.commonMethod("1");
                    try {
                        Utils.showToast(context, response.body().getMsg());
                    } catch (Exception ex) {
                        Utils.showToast(context, "Already Blocked");
                    }
                }

                @Override
                public void onFailure(Call<GetUserDetailBean> call, Throwable t) {
                    commonMethod.commonMethod("15");
                }
            });
        } else
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
    }

}
