package com.screamxo.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.Emailfriend;
import com.example.apimodule.ApiBase.ApiBean.Friend;
import com.example.apimodule.ApiBase.ApiBean.FriendBean;
import com.example.apimodule.ApiBase.ApiBean.Payment.StripeToken;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.example.apimodule.ApiBase.Preferences;
import com.screamxo.Activity.ChatActivity;
import com.screamxo.Activity.ProfileActivity;
import com.screamxo.Activity.RahulWork.FriendsActivity;
import com.screamxo.Activity.RahulWork.SendMoneyActivity;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CHAT_ACTIVITY_RESULTS;

/**
 * Created by Shubham Agarwal on 20/12/16.
 */

public class FriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "FriendAdapter";
    public String type;
    Context context;
    private int width, height;
    private ArrayList<Object> friendArrayList;
    private FetchrServiceBase mService;
    private Call<FriendBean> friendBeanCall;
    private String logInUid;
    private String amount;
    private int selectPosition;
    private String screenType;

    //0 = member ,1 = suggettion ,2 = Request ,3 = filter (Invite) , 4 = searchFriend, 5= TAG FRIEND, 6= FINDER
    public FriendAdapter(Context context, ArrayList<Object> friendArrayList, String type, String screenType) {
        Log.d(TAG, "FriendAdapter screenType: " + screenType);
        Log.d(TAG, "FriendAdapter type: " + type);
        this.context = context;
        this.screenType = screenType;
        mService = new FetchrServiceBase();
        this.friendArrayList = friendArrayList;
        logInUid = new Preferences(context).getUserId();
        this.type = type;
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
    }

    public FriendAdapter(Context context, ArrayList<Object> friendArrayList, String type, String screenType, String amount) {
        Log.d(TAG, "FriendAdapter screenType: " + screenType);
        Log.d(TAG, "FriendAdapter type: " + type);
        this.context = context;
        this.screenType = screenType;
        this.amount = amount;
        mService = new FetchrServiceBase();
        this.friendArrayList = friendArrayList;
        logInUid = new Preferences(context).getUserId();
        this.type = type;
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_friend_list, parent, false);
        return new FriendHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + type);
        if (holder instanceof FriendHolder) {
            if (friendArrayList.get(position) instanceof Friend) {
                Friend friendbean = (Friend) friendArrayList.get(position);

                switch (type) {
                    case "0":
                        ((FriendHolder) holder).imgMsg.setImageDrawable(context.getResources().getDrawable(R.drawable.android_message_arrow));
                        ((FriendHolder) holder).imgMsg.setVisibility(View.VISIBLE);
                        ((FriendHolder) holder).imgRejectRequest.setVisibility(View.GONE);
                        break;

                    case "1":
                        ((FriendHolder) holder).imgMsg.setImageDrawable(context.getResources().getDrawable(R.mipmap.ico_add));
                        ((FriendHolder) holder).imgMsg.setVisibility(View.VISIBLE);
                        ((FriendHolder) holder).imgRejectRequest.setVisibility(View.GONE);
                        break;

                    case "2":
                        ((FriendHolder) holder).imgMsg.setImageDrawable(context.getResources().getDrawable(R.mipmap.ico_right_black));
                        ((FriendHolder) holder).imgMsg.setVisibility(View.VISIBLE);
                        ((FriendHolder) holder).imgRejectRequest.setVisibility(View.VISIBLE);
                        break;

                    case "4":
                        ((FriendHolder) holder).imgRejectRequest.setVisibility(View.GONE);

                        if (friendbean.getIssent() == 1) {
                            ((FriendHolder) holder).imgMsg.setVisibility(View.GONE);
                        } else {
                            ((FriendHolder) holder).imgMsg.setVisibility(View.VISIBLE);
                        }

                        if (friendbean.getIsfriend() == 1) {
                            ((FriendHolder) holder).imgMsg.setImageDrawable(context.getResources().getDrawable(R.mipmap.send_message_button));
                        } else {
                            ((FriendHolder) holder).imgMsg.setImageDrawable(context.getResources().getDrawable(R.mipmap.ico_add));
                        }
                        break;
                    case "5":
                        ((FriendHolder) holder).imgRejectRequest.setVisibility(View.GONE);
                        ((FriendHolder) holder).imgMsg.setVisibility(View.GONE);
                        break;
                }

                ((FriendHolder) holder).imgMsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectPosition = position;
                        switch (type) {
                            case "0":
                                if (((Activity) context).getIntent() != null && ((Activity) context).getIntent().getExtras() != null) {
                                    String screenType = ((Activity) context).getIntent().getExtras().getString("screen");
                                    if (screenType != null)
                                        if (screenType.equals("sendmoney")) {
                                            String amount = ((Activity) context).getIntent().getExtras().getString("amount");
                                            sendMoneyApi(friendbean.getUserid().toString(), amount);
                                        }
                                }
                                if (StaticConstant.from_.equalsIgnoreCase("media") && StaticConstant.from_url != null &&
                                        !((Activity) context).getIntent().getExtras().containsKey("screen")) {
                                    Intent intent = new Intent(context, ChatActivity.class);
                                    intent.putExtra("otherUid", friendbean.getUserid().toString());
                                    intent.putExtra("username", friendbean.getUsername());
                                    intent.putExtra("userProfile", friendbean.getPhoto());
                                    intent.putExtra("message", StaticConstant.from_url);
                                    intent.putExtra("fullname", friendbean.getFname() + " " + friendbean.getLname());
                                    ((Activity) context).startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);

                                } else {

                                    Intent intent = new Intent(context, ChatActivity.class);
                                    intent.putExtra("otherUid", friendbean.getUserid().toString());
                                    intent.putExtra("username", friendbean.getUsername());
                                    intent.putExtra("userProfile", friendbean.getPhoto());
                                    if (screenType.equals("sendmoney")) {
                                        intent.putExtra("amount", amount);
                                        intent.putExtra("screen", screenType);
                                    }
                                    // intent.putExtra("message", friendbean.getPhoto());
                                    intent.putExtra("fullname", friendbean.getFname() + " " + friendbean.getLname());
                                    ((Activity) context).startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
                                }
                                break;

                            case "2":
                                callAcceptRequest(friendbean.getUserid(), friendbean.getFriendshipid());
                                friendArrayList.remove(selectPosition);
                                notifyDataSetChanged();
                                break;

                            case "4":
                            case "1":
                                if (friendbean.getIsfriend() != 1) {
                                    callSendRequest(friendbean.getUserid());
                                    if (!type.equals("4")) {
                                        friendArrayList.remove(selectPosition);
                                        notifyDataSetChanged();
                                    } else {
                                        ((Friend) (friendArrayList.get(selectPosition))).setIssent(1);
                                        notifyDataSetChanged();
                                    }
                                } else {
                                    Intent intent1 = new Intent(context, ChatActivity.class);
                                    intent1.putExtra("otherUid", friendbean.getUserid().toString());
                                    intent1.putExtra("username", friendbean.getUsername());
                                    intent1.putExtra("userProfile", friendbean.getPhoto());
                                    intent1.putExtra("fullname", friendbean.getFname() + " " + friendbean.getLname());
                                    ((Activity) context).startActivityForResult(intent1, REQ_CODE_CHAT_ACTIVITY_RESULTS);
                                }
                                break;
                        }
                    }
                });

                ((FriendHolder) holder).imgUserProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (screenType.equalsIgnoreCase("sendmoney")) {
                            Intent intent = new Intent(context, SendMoneyActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("uid", "" + friendbean.getUserid().toString());
                            bundle.putString("uFullName", friendbean.getFname() + " " + friendbean.getLname());
                            intent.putExtras(bundle);
//                            ((Activity) context).startActivityForResult(intent, REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS);
                            if (context instanceof FriendsActivity) {
                                ((Activity) context).setResult(RESULT_OK, intent);
                                ((Activity) context).finish();
                            }
                        } else {
                            Intent intent = new Intent(context, ProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("uid", "" + friendbean.getUserid().toString());
                            bundle.putString("uFullName", friendbean.getFname() + " " + friendbean.getLname());
                            bundle.putString("username", friendbean.getUsername());
                            bundle.putString("uProfile", friendbean.getPhoto());
                            bundle.putInt("isFriend", friendbean.getIsfriend());
                            intent.putExtras(bundle);
                            ((Activity) context).startActivityForResult(intent, FriendsActivity.REQ_CODE_PROFILE_ACTIVITY_FLOATING_BUTTON);
                        }
                    }
                });

                ((FriendHolder) holder).txtUserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (screenType.equalsIgnoreCase("sendmoney")) {
                            Intent intent = new Intent(context, SendMoneyActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("uid", "" + friendbean.getUserid().toString());
                            bundle.putString("uFullName", friendbean.getFname() + " " + friendbean.getLname());
                            intent.putExtras(bundle);
//                            ((Activity) context).startActivityForResult(intent, REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS);
                            if (context instanceof FriendsActivity) {
                                ((Activity) context).setResult(RESULT_OK, intent);
                                ((Activity) context).finish();
                            }
                        } else {
                            Intent intent = new Intent(context, ProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("uid", "" + friendbean.getUserid().toString());
                            bundle.putString("uFullName", friendbean.getFname() + " " + friendbean.getLname());
                            bundle.putString("username", friendbean.getUsername());
                            bundle.putString("uProfile", friendbean.getPhoto());
                            bundle.putInt("isFriend", friendbean.getIsfriend());
                            intent.putExtras(bundle);
                            ((Activity) context).startActivityForResult(intent, FriendsActivity.REQ_CODE_PROFILE_ACTIVITY_FLOATING_BUTTON);
                        }
                    }
                });

                ((FriendHolder) holder).imgRejectRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callRejectFrdRequest(friendbean.getUserid(), friendbean.getFriendshipid());
                    }
                });

                ((FriendHolder) holder).txtUserName.setText(friendbean.getFname() + " " + friendbean.getLname());

                String userpic = friendbean.getPhoto().isEmpty() ? null : friendbean.getPhoto();
                Picasso.with(context)
                        .load(userpic)
                        .placeholder(R.mipmap.user)
                        .error(R.mipmap.user)
                        .resize(width, height)
                        .centerCrop()
                        .transform(new CircleTransform())
                        .into(((FriendHolder) holder).imgUserProfile);

            } else if (friendArrayList.get(position) instanceof Emailfriend) {

                Emailfriend emailfriend = (Emailfriend) friendArrayList.get(position);

                switch (type) {
                    case "3":
                    case "6":
                        ((FriendHolder) holder).imgMsg.setImageDrawable(context.getResources().getDrawable(R.mipmap.send_message_button));
                        ((FriendHolder) holder).imgMsg.setVisibility(View.VISIBLE);
                        ((FriendHolder) holder).imgRejectRequest.setVisibility(View.GONE);
                        break;
                }

                ((FriendHolder) holder).imgMsg.setOnClickListener(view -> {
                    selectPosition = position;
                    switch (type) {
                        case "3":
                        case "6":
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("otherUid", emailfriend.getUserid().toString());
                            intent.putExtra("username", emailfriend.getUsername());
                            intent.putExtra("userProfile", emailfriend.getPhoto());
                            // intent.putExtra("message", emailfriend.getPhoto());
                            intent.putExtra("fullname", emailfriend.getFname() + " " + emailfriend.getLname());
                            ((Activity) context).startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
                            break;
                    }
                });

                ((FriendHolder) holder).imgUserProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (screenType.equalsIgnoreCase("sendmoney")) {
                            Intent intent = new Intent(context, SendMoneyActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("uid", "" + emailfriend.getUserid().toString());
                            bundle.putString("uFullName", emailfriend.getFname() + " " + emailfriend.getLname());
                            intent.putExtras(bundle);
//                            ((Activity) context).startActivityForResult(intent, REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS);
                            if (context instanceof FriendsActivity) {
                                ((Activity) context).setResult(RESULT_OK, intent);
                                ((Activity) context).finish();
                            }
                        } else {
                            Intent intent = new Intent(context, ProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("uid", "" + emailfriend.getUserid().toString());
                            bundle.putString("uFullName", emailfriend.getFname() + " " + emailfriend.getLname());
                            bundle.putString("username", emailfriend.getUsername());
                            bundle.putString("uProfile", emailfriend.getPhoto());
                            bundle.putInt("isFriend", emailfriend.getIsfriend());
                            intent.putExtras(bundle);
                            ((Activity) context).startActivityForResult(intent, FriendsActivity.REQ_CODE_PROFILE_ACTIVITY_FLOATING_BUTTON);
                        }
                    }
                });

                ((FriendHolder) holder).txtUserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (screenType.equalsIgnoreCase("sendmoney")) {
                            Intent intent = new Intent(context, SendMoneyActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("uid", "" + emailfriend.getUserid().toString());
                            bundle.putString("uFullName", emailfriend.getFname() + " " + emailfriend.getLname());
                            intent.putExtras(bundle);
//                            ((Activity) context).startActivityForResult(intent, REQ_CODE_SEND_MONEY_ACTIVITY_RESULTS);
                            if (context instanceof FriendsActivity) {
                                ((Activity) context).setResult(RESULT_OK, intent);
                                ((Activity) context).finish();
                            }
                        } else {
                            Intent intent = new Intent(context, ProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("uid", "" + emailfriend.getUserid().toString());
                            bundle.putString("uFullName", emailfriend.getFname() + " " + emailfriend.getLname());
                            bundle.putString("username", emailfriend.getUsername());
                            bundle.putString("uProfile", emailfriend.getPhoto());
                            bundle.putInt("isFriend", emailfriend.getIsfriend());
                            intent.putExtras(bundle);
                            ((Activity) context).startActivityForResult(intent, FriendsActivity.REQ_CODE_PROFILE_ACTIVITY_FLOATING_BUTTON);
                        }
                    }
                });

                ((FriendHolder) holder).imgRejectRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callRejectFrdRequest(emailfriend.getUserid(), emailfriend.getFriendshipid());
                    }
                });

                ((FriendHolder) holder).txtUserName.setText(emailfriend.getFname() + " " + emailfriend.getLname());


                if (emailfriend.getPhoto() != null && !emailfriend.getPhoto().equals("")) {
                    Picasso.with(context)
                            .load(emailfriend.getPhoto())
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
    }

    private void sendMoneyApi(String userId, String amount) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", new Preferences(context).getUserId());
        map.put("to_user_id", userId);
        map.put("amount", amount);

        if (Utils.isInternetOn(context)) {
            Call<StripeToken> sendMoney = mService.getFetcherService(context).sendMoney(map);

            sendMoney.enqueue(new Callback<StripeToken>() {
                @Override
                public void onResponse(Call<StripeToken> call, Response<StripeToken> response) {
                    //   setViewEnableDisable(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            new com.screamxo.Utils.Preferences(context).saveAmount(String.valueOf(response.body().getResult().
                                    getAmount()));
                            Toast.makeText(context, context.getString(R.string.msg_money_send_sucess), Toast.LENGTH_SHORT).show();
                           /* DialogBox.showDialog(context, context.getString(R.string.app_name),
                                    context.getString(R.string.msg_money_send_sucess), DialogBox.DIALOG_SUCESS, null);*/
                            new com.screamxo.Utils.Preferences(context).saveAmount(String.valueOf(response.body().getResult().getAmount()));
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeToken> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public int getItemCount() {
        return friendArrayList.size();
    }

    private void callRejectFrdRequest(Integer userid, Integer friendshipid) {
        if (Utils.isInternetOn(context)) {

            HashMap<String, String> map = new HashMap<>();

            map.put("fromid", logInUid);
            map.put("toid", "" + userid);
            map.put("friendshipid", "" + friendshipid);

            friendBeanCall = mService.getFetcherService(context).rejectFrdRequest(map);

            friendBeanCall.enqueue(new Callback<FriendBean>() {
                @Override
                public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (!type.equals("4")) {
                                friendArrayList.remove(selectPosition);
                                notifyDataSetChanged();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<FriendBean> call, Throwable t) {
                    t.printStackTrace();
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callSendRequest(Integer userid) {
        if (Utils.isInternetOn(context)) {

            HashMap<String, String> map = new HashMap<>();
            map.put("fromid", logInUid);
            map.put("toid", "" + userid);

            friendBeanCall = mService.getFetcherService(context).sendFrdRequest(map);

            friendBeanCall.enqueue(new Callback<FriendBean>() {
                @Override
                public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {

                        } else if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
//                            Utils.unAuthentication(context);
                        }
                    }
                }

                @Override
                public void onFailure(Call<FriendBean> call, Throwable t) {
                    t.printStackTrace();
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callAcceptRequest(Integer userid, Integer friendshipid) {
        if (Utils.isInternetOn(context)) {

            HashMap<String, String> map = new HashMap<>();
            map.put("fromid", logInUid);
            map.put("toid", "" + userid);
            map.put("friendshipid", "" + friendshipid);

            friendBeanCall = mService.getFetcherService(context).acceptFrdRequest(map);

            friendBeanCall.enqueue(new Callback<FriendBean>() {
                @Override
                public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<FriendBean> call, Throwable t) {
                    t.printStackTrace();
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
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
            if (type.equals("1")) {
                imgMsg.setImageDrawable(context.getResources().getDrawable(R.mipmap.ico_add));
            } else if (type.equals("0")) {
                imgMsg.setImageDrawable(context.getResources().getDrawable(R.mipmap.send_message_button));
            }
        }
    }
}
