package com.screamxo.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.Comment;
import com.example.apimodule.ApiBase.ApiBean.CommentBean;
import com.example.apimodule.ApiBase.ApiBean.CommentResult;
import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Emoji.CustomText;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.Validations;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tcking.github.com.giraffeplayer.GiraffePlayer;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Shubham Agarwal on 15/02/17.
 */

public class MediaDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @SuppressLint("StaticFieldLeak")
    private static SeekBar seekBarAudioProgress;
    private static boolean running;
    private static MediaPlayer mPlayer;
    private static TextView txtDuration;
    private static Runnable onEverySecond = new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            if (running) {
                if (seekBarAudioProgress != null) {
                    seekBarAudioProgress.setProgress(mPlayer.getCurrentPosition());
                    int cSeconds = (mPlayer.getCurrentPosition() / 1000) % 60;
                    int cMinutes = ((mPlayer.getCurrentPosition() / (1000 * 60)) % 60);
                    txtDuration.setText(String.format("%02d:%02d", cMinutes, cSeconds));
                } else {
                    mPlayer.stop();
                }
                if (mPlayer.isPlaying()) {
                    seekBarAudioProgress.postDelayed(onEverySecond, 50);
                }

//                if(seekBarAudioProgress.)
            }
        }
    };
    private Context context;
    private CommentResult streampost;
    private Validations validations;
    private int height, width;
    private ArrayList<Comment> comments;
    private Preferences preferences;
    private FetchrServiceBase mService;
    private CommonMethod commonMethod;
    private int positionSelect;
    private Call<CommentBean> commentBeanCall;
    private GiraffePlayer player;
    private TextView txtEndDuration;

    public MediaDetailAdapter(Context context, CommentResult streampost, ArrayList<Comment> comments, CommonMethod commonMethod) {
        this.context = context;
        this.streampost = streampost;
        this.comments = comments;
        mService = new FetchrServiceBase();
        this.commonMethod = commonMethod;
        preferences = new Preferences(context);
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_detail_adapter_layout, parent, false);
            return new HeaderView(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_adapter, parent, false);
            return new CommentHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0 && holder instanceof HeaderView) {

            ((HeaderView) holder).mtxtCommnetCount.setText("" + streampost.getCommentcount());

            if (streampost.getMedia().size() != 0 && !streampost.getMedia().get(0).getMediaUrl().equals("") && streampost.getMedia().get(0).getMediaType().contains("image")) {

                ImageView imgPhoto = new ImageView(context);

                ((HeaderView) holder).lnyComtainer.setVisibility(View.VISIBLE);
                ((HeaderView) holder).lnyComtainer.removeAllViews();
                ((HeaderView) holder).lnyComtainer.addView(imgPhoto);

                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int width = displaymetrics.widthPixels;
                int height = (int) context.getResources().getDimension(R.dimen._170sdp);

                Picasso.with(context)
                        .load(streampost.getMedia().get(0).getMediaUrl())
                        .placeholder(R.mipmap.img_placeholder)
                        .resize(width, height)
                        .error(R.mipmap.img_placeholder)
                        .into(imgPhoto);
            } else if (streampost.getMedia().size() != 0 && !streampost.getMedia().get(0).getMediaUrl().equals("") && streampost.getMedia().get(0).getMediaType().contains("audio")) {

                View view = LayoutInflater.from(context).inflate(R.layout.audio_view_layout, null);


                TextView txtTitle = view.findViewById(R.id.txt_title);
                ImageView imgShare = view.findViewById(R.id.img_share);
                RelativeLayout relativeLayout = view.findViewById(R.id.relative_bottom);
                ((HeaderView) holder).lnyComtainer.setVisibility(View.VISIBLE);
                ((HeaderView) holder).lnyComtainer.removeAllViews();
                ((HeaderView) holder).lnyComtainer.addView(view);

                relativeLayout.setVisibility(View.GONE);

                txtTitle.setText(streampost.getPostTitle());

                imgShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, (streampost.getMedia().get(0).getMediaUrl()));
                        sendIntent.setType("text/plain");
                        context.startActivity(Intent.createChooser(sendIntent, "Send to"));
                    }
                });


                setAudioPlayer(streampost.getMedia().get(0).getMediaUrl(), view, streampost.getMedia().get(0).getMediaThumb());

            } else if ((streampost.getMedia().size() != 0 && !streampost.getMedia().get(0).getMediaUrl().equals("") && streampost.getMedia().get(0).getMediaType().contains("video"))) {
                View view = LayoutInflater.from(context).inflate(R.layout.view_screamxo_player, null);
                ((HeaderView) holder).lnyComtainer.removeAllViews();
                ((HeaderView) holder).lnyComtainer.addView(view);
                ((HeaderView) holder).lnyComtainer.setVisibility(View.VISIBLE);
                player = new GiraffePlayer((Activity) context);
                player.play(streampost.getMedia().get(0).getMediaUrl());

                player.onComplete(new Runnable() {
                    @Override
                    public void run() {
                        player.seekTo(0, true);
                        player.start();
                    }
                });
            }

            ((HeaderView) holder).mtxtMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context, ((HeaderView) holder).mtxtMore);

                    popup.getMenuInflater().inflate(R.menu.menu_share_delete, popup.getMenu());

                    if (preferences.getUserId().equals("" + streampost.getUserid())) {
                        popup.getMenu().getItem(0).setTitle("Delete");
                    } else {
                        popup.getMenu().getItem(0).setTitle("Report Post");
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            positionSelect = position;

                            switch (item.getItemId()) {
                                case R.id.pop_up_detele:
                                    if (preferences.getUserId().equals("" + streampost.getUserid())) {
                                        callDeleteApi(streampost.getUserid(), streampost.getId(), Integer.parseInt(streampost.getPostType()));
                                    } else {
                                        callReportPostApi(streampost.getUserid(), streampost.getId(), Integer.parseInt(streampost.getPostType()));
                                    }
                                    break;

                                case R.id.pop_up_share:

                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, streampost.getPostTitle());
                                    sendIntent.setType("text/plain");
                                    context.startActivity(Intent.createChooser(sendIntent, "Send to"));

                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });


        } else if (holder instanceof CommentHolder) {
            Comment comment = comments.get(position - 1);

            if (position == 1 && streampost.getCommentcount() > comments.size()) {
                ((CommentHolder) holder).txtMoreComment.setVisibility(View.VISIBLE);
            } else {
                ((CommentHolder) holder).txtMoreComment.setVisibility(View.GONE);
            }

            ((CommentHolder) holder).txtMoreComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (streampost.getCommentcount() > comments.size()) {
                        commonMethod.commonMethod("4");
                    }
                }
            });


            ((CommentHolder) holder).txtUserName.setText(comment.getFname() + " " + comment.getLname());
            ((CommentHolder) holder).txtCommnet.setText(comment.getCommentdesc());

            String date = validations.dateFormationUTCtoLocal("yyyy-MM-dd'T'HH:mm:ss", comment.getCommenttime());
            ((CommentHolder) holder).txtTime.setText(date);

            if (comment.getUserphoto() != null && !comment.getUserphoto().equals("")) {
                Picasso.with(context)
                        .load(comment.getUserphoto())
                        .placeholder(R.mipmap.user)
                        .error(R.mipmap.user)
                        .resize(width, height)
                        .centerCrop()
                        .transform(new CircleTransform())
                        .into(((CommentHolder) holder).imgUserProfile);
            }

            ((CommentHolder) holder).txtCommnet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionSelect = position;
                    PopupMenu popup = new PopupMenu(context, ((CommentHolder) holder).txtCommnet);

                    popup.getMenuInflater().inflate(R.menu.menu_share_delete, popup.getMenu());

                    if (preferences.getUserId().equals("" + comment.getUserid()) || preferences.getUserId().equals("" + streampost.getUserid())) {
                        popup.getMenu().getItem(0).setTitle("Copy");
                        popup.getMenu().getItem(1).setTitle("Delete");
                    } else {
                        popup.getMenu().getItem(0).setTitle("Copy");
//                        popup.getMenu().removeItem(1);
                        popup.getMenu().getItem(1).setVisible(false);
//                        popup.getMenu().getItem(0).setTitle("Hello");
                    }
//                        popup.getMenuInflater().inflate(R.menu.repost_share, popup.getMenu());


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.pop_up_detele:
                                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("Text Copied", comment.getCommentdesc());
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(context, "Text copied", Toast.LENGTH_SHORT).show();
                                    break;

                                case R.id.pop_up_share:
                                    callDeleteCommentApi(streampost.getUserid(), Integer.parseInt(comment.getCommentid()), streampost.getId());
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (comments.size() + 1);
    }

    private void setLikeApi(int id, int action) {
        Map<String, Integer> map = new HashMap<>();
        map.put("postid", id);
        map.put("uid", Integer.parseInt(preferences.getUserId()));
        map.put("action", action);
        mService.getFetcherService(context).Like(map).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                commonMethod.commonMethod("2");
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {

            }
        });
    }

    private void callDeleteApi(int uId, int postId, int postType) {
        Map<String, Integer> map = new HashMap<>();

        map.put("uid", uId);
        map.put("postid", postId);
        map.put("posttype", postType);

        if (Utils.isInternetOn(context)) {

            commentBeanCall = mService.getFetcherService(context).DeleteStream(map);

            commentBeanCall.enqueue(new retrofit2.Callback<CommentBean>() {
                @Override
                public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {

                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            commonMethod.commonMethod("6");
//                            ((Activity) context).finish();
//                            streamposts.remove(positionSelect - 3);
//                            notifyDataSetChanged();
                        } else if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
//                            Utils.unAuthentication(context);
                        }
                        Utils.showToast(context, response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<CommentBean> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callDeleteCommentApi(int uId, int commentid, int postId) {
        Map<String, Integer> map = new HashMap<>();

        map.put("uid", uId);
        map.put("commentid", commentid);
        map.put("postid", postId);
//        map.put("posttype", postType);

        if (Utils.isInternetOn(context)) {

            commentBeanCall = mService.getFetcherService(context).DeleteComment(map);

            commentBeanCall.enqueue(new retrofit2.Callback<CommentBean>() {
                @Override
                public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {

                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {

                            comments.remove(positionSelect - 1);
//                            streampost.setCommentcount(streampost.getCommentcount() - 1);
                            commonMethod.commonMethod("5");
                            notifyDataSetChanged();
//                            streamposts.remove(positionSelect - 3);
//                            notifyDataSetChanged();
                        } else if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
//                            Utils.unAuthentication(context);
                        }
                        Utils.showToast(context, response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<CommentBean> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callReportPostApi(int uId, int postId, int postType) {
        Map<String, Integer> map = new HashMap<>();

        map.put("uid", uId);
        map.put("postid", postId);
//        map.put("posttype", postType);

        if (Utils.isInternetOn(context)) {

            commentBeanCall = mService.getFetcherService(context).ReportComment(map);

            commentBeanCall.enqueue(new retrofit2.Callback<CommentBean>() {
                @Override
                public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {

                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
                        Utils.showToast(context, response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<CommentBean> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @SuppressLint("SetTextI18n")
    private void setAudioPlayer(String url, final View view, String audioThum) {

        int[] secound = new int[1];

        final boolean[] playFlag = {true};
        ImageView imgPlay = view.findViewById(R.id.app_video_play);
        ImageView imgBackPlayer = view.findViewById(R.id.app_video_finish);
        ImageView audioViewBackground = view.findViewById(R.id.img_audio_background);
        LinearLayout lnyAudioTitle = view.findViewById(R.id.lny_audio_title);
        LinearLayout lnyPlayerSeekbar = view.findViewById(R.id.lny_player_seek_bar);

        seekBarAudioProgress = view.findViewById(R.id.app_audio_seekBar);
        txtDuration = view.findViewById(R.id.app_audio_currentTime);
        txtEndDuration = view.findViewById(R.id.app_audio_endTime);

        Picasso.with(context)
                .load(audioThum)
                .placeholder(R.mipmap.headphone_placeholder)
                .error(R.mipmap.headphone_placeholder)
                .into(audioViewBackground);

        imgPlay.setEnabled(false);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            //            http://api.theataapp.com/storage/uploads/question/1474445817-Re2x2LW1mDZvNbQ.m4a
            mPlayer.setDataSource(url);
            mPlayer.prepareAsync();
            txtDuration.setText("Loading...");
        } catch (IOException e) {
            e.printStackTrace();
        }

        audioViewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lnyAudioTitle.getVisibility() == View.VISIBLE) {
                    lnyAudioTitle.setVisibility(View.GONE);
                    lnyPlayerSeekbar.setVisibility(View.GONE);
                } else {
                    lnyAudioTitle.setVisibility(View.VISIBLE);
                    lnyPlayerSeekbar.setVisibility(View.VISIBLE);
                }

            }
        });

        imgBackPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout view1 = (LinearLayout) v.getParent().getParent().getParent();
                view1.removeAllViews();
                view1.setVisibility(View.GONE);
                if (mPlayer != null) {
                    mPlayer.stop();
                }
            }
        });

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBarAudioProgress.setMax(mPlayer.getDuration());

                mPlayer.start();
                running = true;
                seekBarAudioProgress.postDelayed(onEverySecond, 50);
                imgPlay.setEnabled(true);
                secound[0] = mPlayer.getDuration() / 1000;
                txtDuration.setText(String.format("%02d:%02d", secound[0] / 60, secound[0] % 60));
                playFlag[0] = false;
                imgPlay.setImageResource(R.drawable.ic_stop_white_24dp);

                int cSeconds = (mPlayer.getDuration() / 1000) % 60;
                int cMinutes = ((mPlayer.getDuration() / (1000 * 60)) % 60);

                txtEndDuration.setText(String.format("%02d:%02d", cMinutes, cSeconds));
            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playFlag[0]) {
                    mPlayer.start();
                    seekBarAudioProgress.postDelayed(onEverySecond, 100);
                    imgPlay.setImageResource(R.drawable.ic_stop_white_24dp);
                    playFlag[0] = false;
                } else {
                    mPlayer.pause();
                    playFlag[0] = true;
                    imgPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                }
            }
        });

        seekBarAudioProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.v("Seek bar3", "" + seekBarAudioProgress.getProgress() + " two " + mPlayer.getDuration());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPlayer.seekTo(progress);
                }
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                imgPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                seekBarAudioProgress.setProgress(0);
                seekBarAudioProgress.postDelayed(onEverySecond, 50);
                mPlayer.start();
//                playFlag[0] = true;
            }
        });
    }

    private class CommentHolder extends RecyclerView.ViewHolder {
        ImageView imgUserProfile;
        TextView txtUserName, txtTime, txtMoreComment;
        CustomText txtCommnet;

        CommentHolder(View v) {
            super(v);

            imgUserProfile = v.findViewById(R.id.img_user_profile);
            txtUserName = v.findViewById(R.id.txt_user_name);
            txtCommnet = v.findViewById(R.id.txt_comment);
            txtTime = v.findViewById(R.id.txt_time);
            txtMoreComment = v.findViewById(R.id.txt_view_more);
        }
    }

    private class HeaderView extends RecyclerView.ViewHolder {
        TextView mtxtLike, mtxtCommnetCount, mtxtMore;
        CustomText mtxtComment;
        LinearLayout lnyComtainer;


        @SuppressLint("SetTextI18n")
        HeaderView(View v) {
            super(v);

            validations = new Validations();
            mtxtComment = v.findViewById(R.id.txt_comment);
            mtxtLike = v.findViewById(R.id.txt_like);
            mtxtCommnetCount = v.findViewById(R.id.txt_comment_count);
            mtxtMore = v.findViewById(R.id.txt_more);
            lnyComtainer = v.findViewById(R.id.lny_container);

            if (streampost != null) {

                mtxtLike.setText("" + streampost.getLikecount());

                if (streampost.getIslike() == 0) {
                    mtxtLike.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ico_xo_like_copy, 0, 0, 0);
                } else {
                    mtxtLike.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ico_face_dark, 0, 0, 0);
                }

                mtxtLike.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        if (streampost.getIslike() == 0) {
                            mtxtLike.setText("" + (streampost.getLikecount() + 1));
                            mtxtLike.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ico_face_dark, 0, 0, 0);
//                            streampost.setLikecount(streampost.getLikecount() + 1);
                            streampost.setIslike(1);
                            setLikeApi(streampost.getId(), StaticConstant.LIKE);

                            commonMethod.commonMethod("0");
                        } else {
                            mtxtLike.setText("" + (streampost.getLikecount() - 1));
                            mtxtLike.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ico_xo_like_copy, 0, 0, 0);
//                            streampost.setLikecount(streampost.getLikecount() - 1);
                            streampost.setIslike(0);
                            setLikeApi(streampost.getId(), StaticConstant.UNLIKE);
                            commonMethod.commonMethod("1");
                        }
                    }
                });

            }

        }

    }
}
