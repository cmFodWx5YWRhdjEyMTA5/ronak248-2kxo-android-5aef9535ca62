package com.screamxo.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apimodule.ApiBase.ApiBean.Message;
import com.example.apimodule.ApiBase.ApiBean.Otheruser;
import com.screamxo.Activity.ChatActivity;
import com.screamxo.Activity.ProfileActivity;
import com.screamxo.Activity.RahulWork.ChatMediaPlayer;
import com.screamxo.Activity.RahulWork.CustomWebViewFragment;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Others.ImageDownloaderTask;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.ImageUtils;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.Validations;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.screamxo.Utils.ImageUtils.IMAGE_FILE_EXTENSION;

/**
 * Created by Shubham Agarwal on 09/01/17.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements CommonMethod {
    private final String TAG = ChatAdapter.class.getSimpleName();
    private final Preferences preferences;

    private Context context;
    private ArrayList<Message> messageArrayList;
    private Otheruser otherUserData;
    private String otherUserName = "", otherUserPhoto = "", userPhoto;
    private int heightProfile;
    private int widthProfile;
    private CommonMethod commonMethod;
    private int selectPosition;
    private Validations validations;
    private String inputFormate = "yyyy-MM-dd'T'HH:mm:ss";
    private Dialog photoDialog;
    private LoadInAppUrlTask loadInAppUrlTask;

    public ChatAdapter(Context context, ArrayList<Message> messageArrayList, Otheruser otherUserData) {
        this.context = context;
        this.messageArrayList = messageArrayList;
        this.otherUserData = otherUserData;
        commonMethod = this;
        validations = new Validations();
        preferences = new Preferences(context);

        BitmapDrawable bdUser = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.pic_holder_dashboard);
        heightProfile = bdUser.getBitmap().getHeight();
        widthProfile = bdUser.getBitmap().getWidth();

        if (preferences.getUserProfile() != null && !preferences.getUserProfile().equals("")) {
            userPhoto = preferences.getUserProfile();
        }
        if (context.getClass() == ChatActivity.class) {
            this.otherUserName = ((ChatActivity) context).otherUserName;
            this.otherUserPhoto = ((ChatActivity) context).otherUserPhoto;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == StaticConstant.CHAT_SENDER_TEXT) {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_sender_text, parent, false);
            return new SenderText(v);
        } else if (viewType == StaticConstant.CHAT_SENDER_IMAGE) {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_sender_image, parent, false);
            return new SenderImage(v);
        } else if (viewType == StaticConstant.CHAT_RECIEVER_TEXT) {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_receiver_text, parent, false);
            return new ReceiverText(v);
        } else if (viewType == StaticConstant.CHAT_RECIEVER_IMAGE) {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_receiver_image, parent, false);
            return new ReceiverImage(v);
        } else if (viewType == StaticConstant.CHAT_RECEIVER_AUDIO) {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_receiver_audio, parent, false);
            return new ReceiverAudio(v);
        } else if (viewType == StaticConstant.CHAT_SENDER_AUDIO) {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_sender_audio, parent, false);
            return new SenderAudio(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Message messagebean = messageArrayList.get(position);
        if (holder instanceof SenderText) {
            ((SenderText) holder).bindTo(messagebean, position);
        } else if (holder instanceof SenderImage) {
            ((SenderImage) holder).bindTo(messagebean, position);
        } else if (holder instanceof ReceiverText) {
            ((ReceiverText) holder).bindTo(messagebean, position);
        } else if (holder instanceof ReceiverImage) {
            ((ReceiverImage) holder).bindTo(messagebean, position);
        } else if (holder instanceof ReceiverAudio) {
            try {
                ((ReceiverAudio) holder).bindTo(messagebean, position);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (holder instanceof SenderAudio) {
            try {
                ((SenderAudio) holder).bindTo(messagebean, position);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if (preferences.getUserId().equals("" + message.getSenderid()) && message.getMediatype().contains("text")) {
            return StaticConstant.CHAT_SENDER_TEXT;
            /*if (!(message.getMessagetext().substring(message.getMessagetext().length() - 4,
                    message.getMessagetext().length()).equals(".mp3") || message.getMessagetext().substring(message.getMessagetext().length() - 4,
                    message.getMessagetext().length()).equals(".mp4") || message.getMessagetext().substring(message.getMessagetext().length() - 4,
                    message.getMessagetext().length()).equals(".m4a"))) {
                return StaticConstant.CHAT_SENDER_TEXT;
            } else {
                return StaticConstant.CHAT_SENDER_AUDIO;
            }*/
        } else if (preferences.getUserId().equals("" + message.getSenderid()) && (message.getMediatype().contains("image") || message.getMediatype().contains("video"))) {
            return StaticConstant.CHAT_SENDER_IMAGE;
        } else if (!preferences.getUserId().equals("" + message.getSenderid()) && message.getMediatype().contains("text")) {
            return StaticConstant.CHAT_RECIEVER_TEXT;
            /*if (!(message.getMessagetext().substring(message.getMessagetext().length() - 4,
                    message.getMessagetext().length()).equals(".mp3") || message.getMessagetext().substring(message.getMessagetext().length() - 4,
                    message.getMessagetext().length()).equals(".mp4") || message.getMessagetext().substring(message.getMessagetext().length() - 4,
                    message.getMessagetext().length()).equals(".m4a"))) {
                return StaticConstant.CHAT_RECIEVER_TEXT;
            } else {
                return StaticConstant.CHAT_RECEIVER_AUDIO;
            }*/

        } /*else if (preferences.getUserId().equals("" + message.getSenderid()) && (message.getMediatype().contains("text")
                && (message.getMessagetext().substring(message.getMessagetext().length() - 4,
                message.getMessagetext().length()).equals(".mp3") || message.getMessagetext().substring(message.getMessagetext().length() - 4,
                message.getMessagetext().length()).equals(".mp4") || message.getMessagetext().substring(message.getMessagetext().length() - 4,
                message.getMessagetext().length()).equals(".m4a")))) {
            return StaticConstant.CHAT_SENDER_AUDIO;
        } *//*else if (!preferences.getUserId().equals("" + message.getSenderid()) && (message.getMediatype().contains("text")
                && (message.getMessagetext().substring(message.getMessagetext().length() - 4,
                message.getMessagetext().length()).equals(".mp3") || message.getMessagetext().substring(message.getMessagetext().length() - 4,
                message.getMessagetext().length()).equals(".mp4") || message.getMessagetext().substring(message.getMessagetext().length() - 4,
                message.getMessagetext().length()).equals(".m4a")))) {
            return StaticConstant.CHAT_RECEIVER_AUDIO;
        }*/ else {
            return StaticConstant.CHAT_RECIEVER_IMAGE;
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    @Override
    public void commonMethod(String type, File... files) {
//        type =1 Delete Click , type = 0 Cancel click , type = 2 copy
        switch (type) {
            case "1":
                if (context.getClass() == ChatActivity.class) {
                    ((ChatActivity) context).callDeleteChatMsg(selectPosition);
                }
                break;

            case "2":
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Message copied", messageArrayList.get(selectPosition).getMessagetext());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(context, "Message copied", Toast.LENGTH_SHORT).show();
                break;

            case "3":
                ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask(context);
                imageDownloaderTask.execute(messageArrayList.get(selectPosition).getMedia());
                break;

        }
    }

    public void onConfigurationChanged() {
        if (photoDialog != null) {
            photoDialog.dismiss();
        }
    }

    private void viewUserProfile(Message message) {
        Intent intent = new Intent(context, ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("uid", message.getSenderid());
        if (message.getSenderid().equalsIgnoreCase(preferences.getUserId())) {
            bundle.putString("uFullName", preferences.getUserFullName());
            bundle.putString("username", preferences.getUserName());
            bundle.putString("uProfile", preferences.getUserProfile());
        } else {
            bundle.putString("uFullName", otherUserData.getFname() + " " + otherUserData.getLname());
            bundle.putString("username", otherUserData.getUsername());
            bundle.putString("uProfile", otherUserData.getUserphoto());
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void onDestroy() {
        if (loadInAppUrlTask != null) {
            loadInAppUrlTask.cancel(true);
        }
    }

    class SenderAudio extends RecyclerView.ViewHolder {

        @BindView(R.id.img_user_profile)
        ImageView imgUserProfile;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.lny_message)
        LinearLayout lnyMessage;
        @BindView(R.id.img_play)
        ImageView img_play;
        @BindView(R.id.seekbar_play)
        SeekBar seekbar_play;
        private final Handler handler = new Handler();
        private MediaPlayer mediaPlayer;
        private boolean isPlay = false;

        SenderAudio(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        void bindTo(final Message messagebean, int position) throws IOException {
            try {

                txtTime.setText(validations.dateFormationUTCtoLocal(inputFormate,
                        messagebean.getMessagedate().replace(" ", "T")));

                if (messagebean.isIsplaying()) {
                    img_play.setImageResource(R.drawable.vect_pause_sender);
                } else {
                    img_play.setImageResource(R.drawable.vect_play_pause);
                }

                if (userPhoto != null && !userPhoto.equals("")) {
                    Picasso.with(context)
                            .load(userPhoto)
                            .error(R.mipmap.pic_holder_dashboard)
                            .placeholder(R.mipmap.pic_holder_dashboard)
                            .resize(widthProfile, heightProfile)
                            .centerCrop()
                            .transform(new CircleTransform())
                            .into(imgUserProfile);

                    imgUserProfile.setOnClickListener(view -> viewUserProfile(messagebean));
                }

                lnyMessage.setOnLongClickListener(view -> {
                    selectPosition = position;
                    DialogBox.showAlertDeletemsg((Activity) context, commonMethod, R.menu.delete_msg);
                    return true;
                });

            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClick(messagebean, position);
                }
            });
        }

        private void seekChange(View v) {
            if (mediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar) v;
                mediaPlayer.seekTo(sb.getProgress());
            }
        }

        private void buttonClick(Message messagebean, int position) {
            if (!messagebean.isIsplaying()) {
                img_play.setImageResource(R.drawable.vect_pause_sender);
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                for (int i = 0; i < messageArrayList.size(); i++) {
                    if (i == position) {
                        messageArrayList.get(i).setIsplaying(true);
                    } else {
                        messageArrayList.get(i).setIsplaying(false);
                    }
                }
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(messagebean.getMessagetext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.prepareAsync();
                seekbar_play.setMax(mediaPlayer.getDuration());
                seekbar_play.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        seekChange(v);
                        return false;
                    }
                });
                try {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            startPlayProgressUpdater();
                        }
                    });
                } catch (IllegalStateException e) {
                    mediaPlayer.pause();
                    for (int i = 0; i < messageArrayList.size(); i++) {
                        messageArrayList.get(i).setIsplaying(false);
                    }
                }
                notifyDataSetChanged();
            } else {
                img_play.setImageResource(R.drawable.vect_play_pause);
                if (mediaPlayer != null)
                    mediaPlayer.pause();
                for (int i = 0; i < messageArrayList.size(); i++) {
                    messageArrayList.get(i).setIsplaying(false);
                }

                notifyDataSetChanged();
            }
        }

        private void startPlayProgressUpdater() {
            seekbar_play.setProgress(mediaPlayer.getCurrentPosition());

            if (mediaPlayer.isPlaying()) {
                Runnable notification = new Runnable() {
                    public void run() {
                        startPlayProgressUpdater();
                    }
                };
                handler.postDelayed(notification, 1000);
            } else {
                mediaPlayer.pause();
                for (int i = 0; i < messageArrayList.size(); i++) {
                    messageArrayList.get(i).setIsplaying(false);
                }
                img_play.setImageResource(R.drawable.vect_play_pause);
                seekbar_play.setProgress(0);
                notifyDataSetChanged();
            }
        }
    }

    class ReceiverAudio extends RecyclerView.ViewHolder {

        @BindView(R.id.img_user_profile)
        ImageView imgUserProfile;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.lny_message)
        LinearLayout lnyMessage;
        @BindView(R.id.img_play)
        ImageView img_play;
        @BindView(R.id.seekbar_play)
        SeekBar seekbar_play;
        private final Handler handler = new Handler();
        private MediaPlayer mediaPlayer;
        private boolean isPlay = false;

        ReceiverAudio(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        void bindTo(final Message messagebean, int position) throws IOException {
            try {

                txtTime.setText(validations.dateFormationUTCtoLocal(inputFormate, messagebean.getMessagedate().replace(" ", "T")));

                if (userPhoto != null && !userPhoto.equals("")) {
                    Picasso.with(context)
                            .load(userPhoto)
                            .error(R.mipmap.pic_holder_dashboard)
                            .placeholder(R.mipmap.pic_holder_dashboard)
                            .resize(widthProfile, heightProfile)
                            .centerCrop()
                            .transform(new CircleTransform())
                            .into(imgUserProfile);

                    imgUserProfile.setOnClickListener(view -> viewUserProfile(messagebean));
                }

                lnyMessage.setOnLongClickListener(view -> {
                    selectPosition = position;
                    DialogBox.showAlertDeletemsg((Activity) context, commonMethod, R.menu.delete_msg);
                    return true;
                });

            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isPlay = !isPlay;
                    messagebean.isIsplaying();
                    try {
                        buttonClick(messagebean);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void seekChange(View v) {
            if (mediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar) v;
                mediaPlayer.seekTo(sb.getProgress());
            }
        }

        private void buttonClick(Message messagebean) throws IOException {
            if (!messagebean.isIsplaying()) {
                img_play.setImageResource(R.drawable.vect_pause);
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(messagebean.getMessagetext());
                mediaPlayer.prepareAsync();
                seekbar_play.setMax(mediaPlayer.getDuration());
                seekbar_play.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        seekChange(v);
                        return false;
                    }
                });
                try {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            startPlayProgressUpdater();
                        }
                    });
                } catch (IllegalStateException e) {
                    mediaPlayer.pause();
                }
                notifyDataSetChanged();
            } else {
                img_play.setImageResource(R.drawable.vect_play_pause);
                mediaPlayer.pause();
                notifyDataSetChanged();
            }
        }

        private void startPlayProgressUpdater() {
            seekbar_play.setProgress(mediaPlayer.getCurrentPosition());

            if (mediaPlayer.isPlaying()) {
                Runnable notification = new Runnable() {
                    public void run() {
                        startPlayProgressUpdater();
                    }
                };
                handler.postDelayed(notification, 1000);
            } else {
                mediaPlayer.pause();
                img_play.setImageResource(R.drawable.vect_play_pause);
                seekbar_play.setProgress(0);
            }
        }
    }

    class SenderText extends RecyclerView.ViewHolder {

        private static final String TAG = "SenderText";

        @BindView(R.id.img_user_profile)
        ImageView imgUserProfile;
        @BindView(R.id.txt_chat)
        TextView txtChat;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.lny_message)
        LinearLayout lnyMessage;

        SenderText(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        void bindTo(final Message messagebean, int position) {
            try {
                txtChat.setText(messagebean.getMessagetext());
                if (URLUtil.isValidUrl(messagebean.getMessagetext())) {
                    txtChat.setTextColor(Color.parseColor("#01AEF0"));
                } else {
                    txtChat.setTextColor(Color.WHITE);
                }
//                txtChat.setMovementMethod(LinkMovementMethod.getInstance());
                // boolean hasLink = Linkify.addLinks(txtChat, Linkify.WEB_URLS);
                //  txtChat.setLinksClickable(false);
                // Log.d(TAG, "bindTo hasLink: " + hasLink);
                txtChat.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.d(TAG, "onLongClick: ");
                        if (URLUtil.isValidUrl(messagebean.getMessagetext())) {
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fl_container, CustomWebViewFragment.newInstance(messagebean.getMessagetext()));
                            fragmentTransaction.addToBackStack(CustomWebViewFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        } else {
                            Utils.showToast(context, "invalid link");
                        }
//                        loadInAppUrlTask = new LoadInAppUrlTask(messagebean.getMessagetext());
//                        loadInAppUrlTask.execute();
                        return true;
                    }
                });
                txtChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (URLUtil.isValidUrl(messagebean.getMessagetext())) {
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fl_container, CustomWebViewFragment.newInstance(messagebean.getMessagetext()));
                            fragmentTransaction.addToBackStack(CustomWebViewFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        } else {
                            Utils.showToast(context, "invalid link");
                        }
                    }
                });

                txtTime.setText(validations.dateFormationUTCtoLocal(inputFormate, messagebean.getMessagedate().replace(" ", "T")));

                if (userPhoto != null && !userPhoto.equals("")) {
                    Picasso.with(context)
                            .load(userPhoto)
                            .error(R.mipmap.pic_holder_dashboard)
                            .placeholder(R.mipmap.pic_holder_dashboard)
                            .resize(widthProfile, heightProfile)
                            .centerCrop()
                            .transform(new CircleTransform())
                            .into(imgUserProfile);

                    imgUserProfile.setOnClickListener(view -> viewUserProfile(messagebean));
                }

                lnyMessage.setOnLongClickListener(view -> {
                    selectPosition = position;
                    DialogBox.showAlertDeletemsg((Activity) context, commonMethod, R.menu.delete_msg);
                    return true;
                });

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    class SenderImage extends RecyclerView.ViewHolder {

        private static final String TAG = "SenderImage";
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.img_user_profile)
        ImageView imgUserProfile;
        @BindView(R.id.img_chat)
        ImageView imgChat;
        @BindView(R.id.img_play)
        ImageView imgPlay;
        @BindView(R.id.frm_media)
        FrameLayout frmlayPlay;
        @BindView(R.id.lny_message)
        LinearLayout lnyMessage;

        SenderImage(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        void bindTo(Message messagebean, int position) {
            try {
                txtTime.setText(validations.dateFormationUTCtoLocal(inputFormate, messagebean.getMessagedate().replace(" ", "T")));

                if (messagebean.getMediatype().contains("video")) {
                    imgPlay.setVisibility(View.VISIBLE);
                } else {
                    imgPlay.setVisibility(View.GONE);
                }

                if (userPhoto != null && !userPhoto.equals("")) {
                    Picasso.with(context)
                            .load(userPhoto)
                            .error(R.mipmap.pic_holder_dashboard)
                            .placeholder(R.mipmap.pic_holder_dashboard)
                            .resize(widthProfile, heightProfile)
                            .centerCrop()
                            .transform(new CircleTransform())
                            .into(imgUserProfile);
                }

                imgUserProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewUserProfile(messagebean);
                    }
                });
                if (messagebean.getMediatype().contains("video")) {
                    if (messagebean.getMediathumb() != null && !messagebean.getMediathumb().equals("")) {
                        /*Picasso.with(context)
                                .load(messagebean.getMediathumb())
                                .error(R.mipmap.media_placeholder)
                                .placeholder(R.mipmap.media_placeholder)
                                .into(imgChat);*/

                        Glide.with(context)
                                .asBitmap()
                                .load(messagebean.getMedia())
                                .into(imgChat);
                    }
                } else {
                    if (messagebean.getMediathumb() != null && !messagebean.getMediathumb().equals("")) {
                        /*Picasso.with(context)
                                .load(messagebean.getMedia())
                                .error(R.mipmap.media_placeholder)
                                .placeholder(R.mipmap.media_placeholder)
                                .into(imgChat)*/
                        ;

                        Glide.with(context)
                                .asBitmap()
                                .load(messagebean.getMedia())
                                .into(imgChat);
                    }
                }

                lnyMessage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        selectPosition = position;
                        DialogBox.showDownloadImageDialog((Activity) context, commonMethod, R.menu.download_image);
                        return true;
                    }
                });

                frmlayPlay.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        selectPosition = position;
                        DialogBox.showDownloadImageDialog((Activity) context, commonMethod, R.menu.download_image);
                        return true;
                    }
                });

                frmlayPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (messageArrayList.get(getLayoutPosition()).getMediatype().contains("image")) {
                                photoDialog = ImageUtils.customChoosePhoto(context, messagebean.getMedia());
                            } else {
                                Intent gotoNext = new Intent(context, ChatMediaPlayer.class);
                                gotoNext.putExtra("videoUrl", messageArrayList.get(getLayoutPosition()).getMedia());
                                context.startActivity(gotoNext);
                            }

                        } catch (Exception e) {
                            Log.i("error", "chatadapter" + e.getMessage());
                            Toast.makeText(context, "Browser not found", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            } catch (NullPointerException e) {
                Log.e(TAG, "bindTo:SIm " + e.toString());
                e.printStackTrace();
            }
        }
    }

    class ReceiverImage extends RecyclerView.ViewHolder {

        private static final String TAG = "ReceiverImage";

        @BindView(R.id.img_user_profile)
        ImageView imgUserProfile;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_user_name)
        TextView txtUserName;
        @BindView(R.id.img_chat)
        ImageView imgChat;
        @BindView(R.id.img_play)
        ImageView imgPlay;
        @BindView(R.id.frm_media)
        FrameLayout frmlayPlay;
        @BindView(R.id.lny_message)
        LinearLayout lnyMessage;

        ReceiverImage(View v) {

            super(v);
            ButterKnife.bind(this, v);
        }

        void bindTo(Message messagebean, int position) {
            try {
                txtUserName.setText(otherUserName);
                txtTime.setText(validations.dateFormationUTCtoLocal(inputFormate, messagebean.getMessagedate()));

                if (messagebean.getMediatype().contains("video")) {
                    imgPlay.setVisibility(View.VISIBLE);

                } else {
                    imgPlay.setVisibility(View.GONE);
                }

                if (otherUserPhoto != null && !otherUserPhoto.equals("")) {
                    Picasso.with(context)
                            .load(otherUserPhoto)
                            .error(R.mipmap.pic_holder_dashboard)
                            .placeholder(R.mipmap.pic_holder_dashboard)
                            .resize(widthProfile, heightProfile)
                            .centerCrop()
                            .transform(new CircleTransform())
                            .into(imgUserProfile);
                }

                imgUserProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewUserProfile(messagebean);
                    }
                });

                if (messagebean.getMediathumb() != null && !messagebean.getMediathumb().equals("")) {
                    /*Picasso.with(context)
                            .load(messagebean.getMedia())
                            .error(R.mipmap.media_placeholder)
                            .placeholder(R.mipmap.media_placeholder)
                            .into(imgChat);*/

                    Glide.with(context)
                            .asBitmap()
                            .load(messagebean.getMedia())
                            .into(imgChat);
                }

                frmlayPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (messageArrayList.get(getLayoutPosition()).getMediatype().contains("image")) {
                                photoDialog = ImageUtils.customChoosePhoto(context, messagebean.getMedia());
                            } else {
                                Intent gotoNext = new Intent(context, ChatMediaPlayer.class);
                                gotoNext.putExtra("videoUrl", messageArrayList.get(getLayoutPosition()).getMedia());
                                context.startActivity(gotoNext);
                            }
                        } catch (Exception e) {
                            Log.i("error", "chatadapter" + e.getMessage());
                            Toast.makeText(context, "Browser not found", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                lnyMessage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        selectPosition = position;
                        DialogBox.showDownloadImageDialog((Activity) context, commonMethod, R.menu.download_image);
                        return true;
                    }
                });

                frmlayPlay.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        selectPosition = position;
                        DialogBox.showDownloadImageDialog((Activity) context, commonMethod, R.menu.download_image);
                        return true;
                    }
                });

            } catch (NullPointerException e) {
                Log.e(TAG, "bindTo:RIm " + e.toString());
                e.printStackTrace();
            }
        }
    }

    class ReceiverText extends RecyclerView.ViewHolder {

        private static final String TAG = "ReceiverText";

        @BindView(R.id.img_user_profile)
        ImageView imgUserProfile;
        @BindView(R.id.txt_chat)
        TextView txtChat;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_user_name)
        TextView txtUserName;
        @BindView(R.id.lny_message)
        LinearLayout lnyMessage;

        ReceiverText(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        void bindTo(Message messagebean, int position) {
            try {
                txtUserName.setText(otherUserName);
                txtTime.setText(validations.dateFormationUTCtoLocal(inputFormate, messagebean.getMessagedate()));

                txtChat.setText(messagebean.getMessagetext());
                if (URLUtil.isValidUrl(messagebean.getMessagetext())) {
                    txtChat.setTextColor(Color.parseColor("#01AEF0"));
                } else {
                    txtChat.setTextColor(Color.parseColor("#000000"));
                }

                txtChat.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.d(TAG, "onLongClick: ");
                        if (URLUtil.isValidUrl(messagebean.getMessagetext())) {
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fl_container, CustomWebViewFragment.newInstance(messagebean.getMessagetext()));
                            fragmentTransaction.addToBackStack(CustomWebViewFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        } else {
                            Utils.showToast(context, "invalid link");
                        }
                        return true;
                    }
                });
                txtChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (URLUtil.isValidUrl(messagebean.getMessagetext())) {
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fl_container, CustomWebViewFragment.newInstance(messagebean.getMessagetext()));
                            fragmentTransaction.addToBackStack(CustomWebViewFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        } else {
                            Utils.showToast(context, "invalid link");
                        }
                    }
                });

                if (otherUserPhoto != null && !otherUserPhoto.equals("")) {
                    Picasso.with(context)
                            .load(otherUserPhoto)
                            .error(R.mipmap.pic_holder_dashboard)
                            .placeholder(R.mipmap.pic_holder_dashboard)
                            .resize(widthProfile, heightProfile)
                            .centerCrop()
                            .transform(new CircleTransform())
                            .into(imgUserProfile);
                }

                imgUserProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewUserProfile(messagebean);
                    }
                });

                lnyMessage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        selectPosition = position;
                        DialogBox.showAlertDeletemsg((Activity) context, commonMethod, R.menu.delete_msg);
                        return true;
                    }
                });
            } catch (NullPointerException e) {
                Log.e(TAG, "bindTo:RT " + e.toString());
                e.printStackTrace();
            }
        }
    }

    class LoadInAppUrlTask extends AsyncTask<String, String, Boolean> {

        private static final String TAG = "LoadInAppUrlTask";
        String urlStr;

        LoadInAppUrlTask(String urlStr) {
            Log.d(TAG, "LoadInAppUrlTask urlStr: " + urlStr);
            this.urlStr = urlStr;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                File file = new File(urlStr);
                for (String extension : IMAGE_FILE_EXTENSION) {
                    if (file.getName().toLowerCase().endsWith(extension)) {
                        return true;
                    }
                }

                URL url = new URL(urlStr);
                URLConnection connection;
                connection = url.openConnection();
                String contentType = connection.getHeaderField("Content-Type");
                return contentType.startsWith("image/");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        private String getConvertedUrl(String urlStr) {
            String tempUrl = urlStr;
            if (!tempUrl.contains("www")) {
                tempUrl = "www." + urlStr;
            }
            if (!tempUrl.contains("http") || tempUrl.contains("https")) {
                tempUrl = "http://" + tempUrl;
            }
            return tempUrl;
        }

        @Override
        protected void onPostExecute(Boolean isImage) {
            if (isImage) {
                ImageUtils.customChoosePhoto(context, urlStr);
                return;
            }
            Log.d(TAG, "onPostExecute getConvertedUrl(urlStr): " + getConvertedUrl(urlStr));
            if (URLUtil.isValidUrl(getConvertedUrl(urlStr))) {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_container, CustomWebViewFragment.newInstance(getConvertedUrl(urlStr)));
                fragmentTransaction.addToBackStack(CustomWebViewFragment.class.getSimpleName());
                fragmentTransaction.commit();
            }
        }
    }
}

