package com.screamxo.FireBasePush;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.screamxo.Activity.ChatActivity;
import com.screamxo.Activity.PostDetailsActivity;
import com.screamxo.Activity.PurchaseHistoryActivity;
import com.screamxo.Activity.RahulWork.ItemDetailsAcitvity;
import com.screamxo.Activity.RahulWork.MediaItemDetailActivity;
import com.screamxo.Activity.SoldHistoryActivity;
import com.screamxo.Activity.wallet.WalletSendReceiveActivity;
import com.screamxo.Fragment.PleaserFragment;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;

import org.json.JSONObject;

import java.util.Date;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_SOLD_HISTORY_ACTIVITY_RESULTS;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String ACTION_NETWORK_BROADCAST = "com.screamxo.networkbroadcast";
    public static final String ACTION_COMMENT_MEDIA = "com.screamxo.commentmedia";
    public static final String ACTION_COMMENT_POST = "com.screamxo.commentpost";
    public static final String ACTION_CHAT_MESSAGE = "com.screamxo.chatmessage";
    private static final String TAG = "MyFirebaseMsgService";
    private Preferences preferences;

    private String type;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            preferences = new Preferences(getApplicationContext());

            String senderId = "";

            /*for (String key : remoteMessage.getData().keySet()) {
                Log.d(TAG, "onMessageReceived: remoteMessage.getData().get(key) " + remoteMessage.getData().get(key));
                Log.d(TAG, "onMessageReceived: key " + key);
            }

            if (remoteMessage.getData() != null && remoteMessage.getData().containsKey("message")) {
                JSONObject messageObj = new JSONObject(remoteMessage.getData().get("message"));

                if (messageObj.has("ntype") && (messageObj.getString("ntype").equalsIgnoreCase("6")
                        || messageObj.getString("ntype").equalsIgnoreCase("8"))) {
                    messageObj.put("notifications_type", messageObj.getString("ntype"));
                }

                switch (messageObj.getString("notifications_type")) {
                    case "6":
                        sendChatMessageBroadcast(remoteMessage.getData().get("message"));
                        break;

                    case "10":

                        if (messageObj.getString("posttype").equalsIgnoreCase("0")) {
                            sendPostCommentBroadcast(remoteMessage.getData().get("notifications_details"));
                        } else {
                            sendMediaCommentBroadcast(remoteMessage.getData().get("notifications_details"));
                        }

                        break;
                }
                if (!messageObj.getString("notifications_type").equalsIgnoreCase("6")) {
                    setNetworkBroadcast();
                }*/

            NotificationCompat.Builder notificationBuilder = null;
            PendingIntent pendingIntent = null;
            if (remoteMessage.getData() != null) {

                String message = "";
                if (!TextUtils.isEmpty(remoteMessage.getData().get("body"))) {
                    message = remoteMessage.getData().get("body");
                    type = remoteMessage.getData().get("type");
                } else if (remoteMessage.getNotification() != null) {
                    message = remoteMessage.getNotification().getBody();
                }

                if (type.equals("chat")) {
                    senderId = remoteMessage.getData().get("senderid");

                    if (!TextUtils.isEmpty(senderId)) {
                        Intent intent = new Intent(this, ChatActivity.class);
                        intent.putExtra("otherUid", senderId);
                        // intent.putExtra("userProfile",BundleUtils.getBundleExtra(getArguments(), "uProfile", ""));
                        // intent.putExtra("username", userName);
                        // intent.putExtra("fullname", BundleUtils.getBundleExtra(getArguments(), "uFullName", ""));
                        pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);
                    }

                    if (PleaserFragment.isPleasureOpened) {
                        sendChatMessageBroadcast();
                    }


                    if (isOnTop(ChatActivity.class.getName())) {
                        sendChatMessageBroadcast();
                    } else {
                        showNoti(remoteMessage.getData().get("title"), remoteMessage, R.drawable.notilogo, type);

/*
                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.new_2kxo_logo)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.new_2kxo_logo))
                                .setContentTitle("2KXO")
                                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                                .setContentText(message)
                                .setAutoCancel(true)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent);*/

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                    }
                } else {
                    showNoti(remoteMessage.getData().get("title"), remoteMessage, R.drawable.notilogo, type);
                }
            }
//              sendNotification(messageObj);
            //  }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNoti(String title, RemoteMessage remoteMessage, int icon, String type) {
        int notiId = ((int) new Date().getTime());
        String notiChannel = "1";
        Intent intent;

        if (type.equals("money")) {
            notiChannel = "121";
            intent = new Intent(this, WalletSendReceiveActivity.class);
            intent.putExtra("notification", "notification");

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else if (type.equals("post_like") || type.equals("post_comment")) {
            if (remoteMessage.getData().get("posttype").equals("'0'")) {
                notiChannel = "123";
                intent = new Intent(this, PostDetailsActivity.class);
                intent.putExtra("postId", "" + remoteMessage.getData().get("itemId"));
            } else {
                notiChannel = "124";
                intent = new Intent(this, MediaItemDetailActivity.class);
                intent.putExtra("itemId", remoteMessage.getData().get("itemId"));
                intent.putExtra("username", remoteMessage.getData().get("username"));
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        } else if (type.equals("chat")) {
            notiChannel = "129";

            intent = new Intent(this, ChatActivity.class);
            intent.putExtra("otherUid", remoteMessage.getData().get("senderid"));
        } else if (type.equals("sold")) {
            notiChannel = "126";
            intent = new Intent(this, SoldHistoryActivity.class);
            intent.putExtra("userid", preferences.getUserId());

           /* Intent intent = new Intent(context, SoldHistoryActivity.class);
            intent.putExtra("userid", preferences.getUserId());
            startActivityForResult(intent, REQ_CODE_SOLD_HISTORY_ACTIVITY_RESULTS);*/

           /*
            Intent intent = new Intent(context, PurchaseHistoryActivity.class);
                    intent.putExtra("userid", preferences.getUserId());
                    startActivityForResult(intent, REQ_CODE_PURCHASE_HISTORY_ACTIVITY_RESULTS);
            */
        } else {
            notiChannel = "127";
            intent = new Intent(this, PurchaseHistoryActivity.class);
            intent.putExtra("userid", preferences.getUserId());
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(getApplicationContext(), notiChannel);
        builder.setSmallIcon(icon)
                .setContentTitle(title)
                .setSound(null)
                .setContentText(remoteMessage.getData().get("body")).setContentIntent(pendingIntent);

        builder.setStyle(new NotificationCompat.BigTextStyle());

//        builder.setOnlyAlertOnce(false);
//        builder.setOngoing(false);
//        builder.setContentIntent()
        NotificationManager mNotificationManager = (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("131", "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);

            notificationChannel.setShowBadge(true);

            assert mNotificationManager != null;
            builder.setChannelId("131");
            mNotificationManager.createNotificationChannel(notificationChannel);
            mNotificationManager.notify(notiId, builder.build());
        } else
            mNotificationManager.notify(notiId, builder.build());
    }


    private void sendChatBroadcast() {
    }

    private void setNetworkBroadcast() {
        Intent intent1 = new Intent();
        intent1.setAction(ACTION_NETWORK_BROADCAST);
        sendBroadcast(intent1);
    }

    private void sendNotification(JSONObject messageObj) {
        Log.d(TAG, "sendNotification: ");
        try {
            Intent intent = null;
            String contentText = null;

            switch (messageObj.getString("notifications_type")) {

                //   __username__ purchased your item \"gghh\"
                case "3":
                    intent = new Intent(this, ItemDetailsAcitvity.class);
                    intent.putExtra("itemid", messageObj.getString("detailid"));
                    intent.putExtra("from_notification", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    contentText = messageObj.getString("notifications_details");
                    break;

                //                You received new text message
                case "6":
                    if (isOnTop(ChatActivity.class.getName())) {
                        return;
                    }

                    intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("otherUid", messageObj.getString("senderid"));
                    intent.putExtra("username", messageObj.getString("username"));
                    intent.putExtra("from_notification", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    contentText = messageObj.getString("alert");
                    break;

                // Tracking no. bhgft by hghb is added for your ordered item vvh.
                case "7":
                    intent = new Intent(this, ItemDetailsAcitvity.class);
                    intent.putExtra("itemid", messageObj.getString("detailid"));
                    intent.putExtra("from_notification", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    contentText = messageObj.getString("notifications_details");
                    break;

//              James Lee. has requested to update tracking info for your product
//                or
//                James Lee. has added review for your product
                case "8":
                    intent = new Intent(this, ItemDetailsAcitvity.class);
                    intent.putExtra("itemid", messageObj.getString("detailid"));
                    intent.putExtra("from_notification", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    contentText = messageObj.getString("message");
                    break;

                //New comment on post
                case "10":
                    if (messageObj.getString("posttype").equalsIgnoreCase("0")) {
                        if (isOnTop(PostDetailsActivity.class.getName())) {
                            return;
                        }

                        intent = new Intent(this, PostDetailsActivity.class);
                        intent.putExtra("postId", "" + messageObj.getString("detailid"));
                    } else {
                        if (isOnTop(MediaItemDetailActivity.class.getName())) {
                            return;
                        }

                        intent = new Intent(this, MediaItemDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("itemId", "" + messageObj.getString("detailid"));
                        bundle.putString("username", messageObj.getString("name"));
                        intent.putExtras(bundle);
                    }

                    intent.putExtra("from_notification", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    contentText = messageObj.getString("notifications_details");
                    break;
            }

            if (StaticConstant.CHAT_SCREEN && messageObj.getString("notifications_type").equalsIgnoreCase("6")) {
                return;
            }

            Log.d(TAG, "sendNotification contentText: " + contentText);
            NotificationCompat.Builder notificationBuilder = null;
            if (contentText != null) {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_emoji_happy)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentTitle("2KXO")
                        .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .setContentText(contentText)
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setSound(defaultSoundUri);
            }

            if (intent != null && notificationBuilder != null) {
                PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);
                notificationBuilder.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(0, notificationBuilder.build());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendMediaCommentBroadcast(String message) {
        Log.d(TAG, "sendMediaCommentBroadcast: ");
        Intent intent1 = new Intent();
        intent1.setAction(ACTION_COMMENT_MEDIA);
        sendBroadcast(intent1);
    }

    public void sendPostCommentBroadcast(String message) {
        Log.d(TAG, "sendPostCommentBroadcast: ");
        Intent intent1 = new Intent();
        intent1.setAction(ACTION_COMMENT_POST);
        sendBroadcast(intent1);
    }


    public void sendChatMessageBroadcast() {
        Log.d(TAG, "sendChatMessageBroadcast: ");
        Intent intent1 = new Intent();
        intent1.setAction(ACTION_CHAT_MESSAGE);
        //intent1.putExtra("message", message);
        sendBroadcast(intent1);
    }

    public boolean isOnTop(String className) {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            Log.d(TAG, "isOnTop: " + cn.getClassName());
            Log.d(TAG, "isOnTop: " + className);
            if (cn.getClassName().equalsIgnoreCase(className)) {
                return true;
            }
        }
        return false;
    }


/*
    Money notifications open the wallet history page

    Item sold: open the seller history page

    Item bought: open the receipts page*/
}