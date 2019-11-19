package com.screamxo.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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

import com.example.apimodule.ApiBase.ApiBean.CommentBean;
import com.example.apimodule.ApiBase.ApiBean.DashBoardBean;
import com.example.apimodule.ApiBase.ApiBean.DashBoardResult;
import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.ApiBean.Mediapost;
import com.example.apimodule.ApiBase.ApiBean.Streampost;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.example.apimodule.ApiBase.Preferences;
import com.screamxo.Activity.PostDetailsActivity;
import com.screamxo.Activity.ProfileActivity;
import com.screamxo.Interface.AudioVideoInterface;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
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

/**
 * Created by Shubham Agarwal on 10/02/17.
 * No usage of this class..  it's has been closed as per the instruction of Kamlesh sir
 */
@Deprecated
@SuppressLint("SetTextI18n")
public class WorldAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AudioVideoInterface {
    Context context;
    private ArrayList<Mediapost> mediapostArrayList;
    private ArrayList<Streampost> streampostArrayList;
    private LinearLayout viewStubVideoAudio;
    private int height, width, pageCounterForAllmedia = 2;
    private static MediaPlayer mPlayer;
    private GiraffePlayer player;
    private AudioVideoInterface audioVideoInterface;
    private String headerUrl = "";
    @SuppressLint("StaticFieldLeak")
    private static SeekBar seekBarAudioProgress;
    @SuppressLint("StaticFieldLeak")
    private static TextView txtDuration, txtEndDuration;
    private static boolean running;
    private Preferences preferences;
    private int positionSelect;
    private Call<CommentBean> commentBeanCall;
    private FetchrServiceBase mService;
    private Validations validations;
    private boolean allMediaBoolean = true;
    private DashBoardResult result;
    int totalCount = 0;
    private String uId;

    public WorldAdapter(Context context, ArrayList<Mediapost> mediapostArrayList, ArrayList<Streampost> streampostArrayList, int totalCount) {
        this.totalCount = totalCount;
        audioVideoInterface = this;
        this.context = context;
        mService = new FetchrServiceBase();
        this.mediapostArrayList = mediapostArrayList;
        this.streampostArrayList = streampostArrayList;
        preferences = new Preferences(context);
        uId = "" + preferences.getUserId();
        validations = new Validations();
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        }
        catch (Exception ignored)
        {

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == 0)
        {
            View v = LayoutInflater.from(context).inflate(R.layout.shop_layout, parent, false);
            return new Header(v);
        }
        else {
            View v = LayoutInflater.from(context).inflate(R.layout.item_stream_comment, parent, false);
            return new StreamViewBottom(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof StreamViewBottom) {

            Streampost bean = streampostArrayList.get(position - 1);
            ((StreamViewBottom) holder).mtxtUserName.setText(bean.getFname() + " " + bean.getLname());
            ((StreamViewBottom) holder).mtxtComment.setText(bean.getPostTitle());

            ((StreamViewBottom) holder).mtxtLike.setText("" + bean.getLikecount());
            if (bean.getIslike() == 0) {
                ((StreamViewBottom) holder).mtxtLike.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ico_xo_like_copy, 0, 0, 0);
            } else {
                ((StreamViewBottom) holder).mtxtLike.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.like, 0, 0, 0);
            }
            ((StreamViewBottom) holder).mtxtCommnetCount.setText("" + bean.getCommentcount());

//            ((StreamViewBottom) holder).mtxtLike.setCompoundDrawableTintList(context.getResources().getColorStateList(R.color.colorGray));

            String date = validations.dateFormationUTCtoLocal("yyyy-MM-dd'T'HH:mm:ss", bean.getUpdatedDate());
            ((StreamViewBottom) holder).mtxtTime.setText(date);

            if (bean.getUserphoto() != null && !bean.getUserphoto().equals("")) {
                Picasso.with(context)
                        .load(bean.getUserphoto())
                        .placeholder(R.mipmap.user)
                        .error(R.mipmap.user)
                        .resize(width, height)
                        .centerCrop()
                        .transform(new CircleTransform())
                        .into(((StreamViewBottom) holder).mimgUserProfile);
            }

            ((StreamViewBottom) holder).mtxtLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getIslike() == 0) {
                        ((StreamViewBottom) holder).mtxtLike.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.like, 0, 0, 0);
                        ((StreamViewBottom) holder).mtxtLike.setText("" + (bean.getLikecount() + 1));
                        bean.setLikecount(bean.getLikecount() + 1);
                        bean.setIslike(1);
                        setLikeApi(bean.getId(), StaticConstant.LIKE);
                    } else {
                        ((StreamViewBottom) holder).mtxtLike.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ico_xo_like_copy, 0, 0, 0);
                        ((StreamViewBottom) holder).mtxtLike.setText("" + (bean.getLikecount() - 1));
                        bean.setLikecount(bean.getLikecount() - 1);
                        bean.setIslike(0);
                        setLikeApi(bean.getId(), StaticConstant.UNLIKE);
                    }
                }
            });

            ((StreamViewBottom) holder).mtxtComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailsActivity.class);
                    intent.putExtra("postId", "" + bean.getId());

                    // context.startActivity(intent);
                    ((Activity) context).startActivityForResult(intent, StaticConstant.REQUEST_POST_DETAIL);
                }
            });

            ((StreamViewBottom) holder).mtxtCommnetCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailsActivity.class);
                    intent.putExtra("postId", "" + bean.getId());
                    // context.startActivity(intent);
                    ((Activity) context).startActivityForResult(intent, StaticConstant.REQUEST_POST_DETAIL);
                }
            });

            ((StreamViewBottom) holder).relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailsActivity.class);
                    intent.putExtra("postId", "" + bean.getId());
                    // context.startActivity(intent);
                    ((Activity) context).startActivityForResult(intent, StaticConstant.REQUEST_POST_DETAIL);
                }
            });
            ((StreamViewBottom) holder).mtxtMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popup = new PopupMenu(context, ((StreamViewBottom) holder).mtxtMore);

                    popup.getMenuInflater().inflate(R.menu.menu_share_delete, popup.getMenu());

                    if (preferences.getUserId().equals("" + bean.getUserid())) {
                        popup.getMenu().getItem(0).setTitle("Delete");
//                        popup.getMenu().getItem(0).setTitle("Hello");
                    } else {
                        popup.getMenu().getItem(0).setTitle("Report Post");
//                        popup.getMenu().getItem(0).setTitle("Hello");
                    }
//                        popup.getMenuInflater().inflate(R.menu.repost_share, popup.getMenu());


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            positionSelect = position;

                            switch (item.getItemId()) {
                                case R.id.pop_up_detele:
                                    if (preferences.getUserId().equals("" + bean.getUserid())) {
                                        callDeleteApi(bean.getUserid(), bean.getId(), bean.getPosttype());
                                    } else {
                                        callReportPostApi(bean.getUserid(), bean.getId(), bean.getPosttype());
                                    }
                                    break;

                                case R.id.pop_up_share:

                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, bean.getPostTitle());
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

    @Override
    public int getItemCount() {
        /*return streampostArrayList.size() + 1;*/
        return streampostArrayList.size() + 1;
    }

    @Override
    public void Video(String url, String type, int index) {
        if (mPlayer != null) {
            mPlayer.stop();
        }
        if (player != null) {
            player.stop();
        }

        if (!this.headerUrl.equals(url)) {
            Log.v("Medi Url", " " + url);
            View view = LayoutInflater.from(context).inflate(R.layout.view_screamxo_player, null);
            viewStubVideoAudio.removeAllViews();
            viewStubVideoAudio.addView(view);
            viewStubVideoAudio.setVisibility(View.VISIBLE);
            player = new GiraffePlayer((Activity) context);
            player.play(url);

            player.onComplete(new Runnable() {
                @Override
                public void run() {
                    player.seekTo(0, true);
                    player.start();
//                    player.play(url);
                }
            });
            this.headerUrl = url;
        } else {
            viewStubVideoAudio.removeAllViews();
            viewStubVideoAudio.setVisibility(View.GONE);
            this.headerUrl = "";
        }
    }

    @Override
    public void Audio(String url, String audioThum, String title, String type, int index) {
        if (mPlayer != null) {
            mPlayer.stop();
        }
        if (player != null) {
            player.stop();
        }

        if (!this.headerUrl.equals(url)) {
            View view = LayoutInflater.from(context).inflate(R.layout.audio_view_layout, null);
            Log.v("Medi Url", " " + url);
            TextView txtTitle = view.findViewById(R.id.txt_title);
            viewStubVideoAudio.setVisibility(View.VISIBLE);
            viewStubVideoAudio.removeAllViews();
            viewStubVideoAudio.addView(view);
            txtTitle.setText(title);
            setAudioPlayer(url, view, audioThum);
            this.headerUrl = url;
        } else {
            viewStubVideoAudio.removeAllViews();
            viewStubVideoAudio.setVisibility(View.GONE);
            this.headerUrl = "";
        }
    }

    @Override
    public void Image(String url, String postId, int index, String type) {

        if (mPlayer != null) {
            mPlayer.stop();
        }
        if (player != null) {
            player.stop();
        }
// 0 = all media 1= Shop
        switch (type) {
            case "0":
                if (!headerUrl.equals(url)) {
                    addViewHeader(url, postId, index);
                } else {
                    viewStubVideoAudio.removeAllViews();
                    viewStubVideoAudio.setVisibility(View.GONE);
                    headerUrl = "";
                }
                break;
        }


    }

    private class Header extends RecyclerView.ViewHolder {

        RecyclerView mrecyclerView;
        RelativeLayout mRelativeLayoutTag, mRelativeBottom;
        //        LinearLayout viewStubVideoAudio;
        LinearLayout includeNoData;

        Header(View v) {

            super(v);

            mrecyclerView = v.findViewById(R.id.recycler_view);
            mRelativeLayoutTag = v.findViewById(R.id.top_tag_layout);
            viewStubVideoAudio = v.findViewById(R.id.view_player);
            includeNoData = v.findViewById(R.id.include_no_data);
            mRelativeBottom = v.findViewById(R.id.bottom_tag_layout);

            mRelativeLayoutTag.setVisibility(View.GONE);
            includeNoData.setVisibility(View.GONE);
            mRelativeBottom.setVisibility(View.GONE);

            LinearLayoutManager linearLayoutManagerShop = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            ImageVideoAdapter imagevideoAdapterImageShop = new ImageVideoAdapter(context, mediapostArrayList, null, audioVideoInterface);

            mrecyclerView.setLayoutManager(linearLayoutManagerShop);
            mrecyclerView.setAdapter(imagevideoAdapterImageShop);

            mrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (linearLayoutManagerShop.findLastCompletelyVisibleItemPosition() == (imagevideoAdapterImageShop.getItemCount() - 1)) {
                        if (allMediaBoolean && mediapostArrayList.size() < totalCount) {
                            allMediaBoolean = false;
                            callMoreMedia("", imagevideoAdapterImageShop);
                        }
                    }

                }
            });

        }
    }


    private void callMoreMedia(String mediaType, ImageVideoAdapter imagevideoAdapterImageShop) {
        Map<String, String> map = new HashMap<>();

        map.put("uid", uId);
        String LIMIT = StaticConstant.LIMIT;
        map.put("limit", LIMIT);
        map.put("mediatype", mediaType);
        map.put("offset", "" + pageCounterForAllmedia);
        map.put("myid", uId);
        map.put("posttype", "1");
        if (StaticConstant.WORLDSEARCH.length() > 0) {
            map.put("searchstring", StaticConstant.SEARCHSTRING);
            // map.put("userfiltertype", "2");
        }

        if (Utils.isInternetOn(context)) {

            Call<DashBoardBean> shopMediaCall = mService.getFetcherService(context).GetWorldMedia(map);

            shopMediaCall.enqueue(new retrofit2.Callback<DashBoardBean>() {
                @Override
                public void onResponse(Call<DashBoardBean> call, Response<DashBoardBean> response) {
                    allMediaBoolean = true;
                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                         /*   if (pageCounterForAllmedia == 1)
                                mediaposts.clear();*/

                            //   result.setMediapostcount(response.body().getResult().getTotalcount());
                            totalCount = response.body().getResult().getMediapostcount();
                            mediapostArrayList.addAll(response.body().getResult().getMediapost());
                            pageCounterForAllmedia++;
                            imagevideoAdapterImageShop.notifyDataSetChanged();

                            notifyDataSetChanged();
                        } else if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
//                            Utils.unAuthentication(context);
                        }

                    }
                }

                @Override
                public void onFailure(Call<DashBoardBean> call, Throwable t) {    t.printStackTrace();
                    allMediaBoolean = true;
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private class StreamViewBottom extends RecyclerView.ViewHolder {

        ImageView mimgUserProfile;
        TextView mtxtUserName, mtxtComment, mtxtTime, mtxtLike, mtxtCommnetCount, mtxtMore;
        RelativeLayout relativeLayout;

        StreamViewBottom(View v) {
            super(v);

            mimgUserProfile = v.findViewById(R.id.img_user_profile);
            mtxtUserName = v.findViewById(R.id.txt_user_name);
            mtxtComment = v.findViewById(R.id.txt_comment);
            mtxtTime = v.findViewById(R.id.txt_time);
            mtxtLike = v.findViewById(R.id.txt_like);
            mtxtCommnetCount = v.findViewById(R.id.txt_comment_count);
            mtxtMore = v.findViewById(R.id.txt_more);
            relativeLayout = v.findViewById(R.id.relative_first_child);

        }
    }

    @SuppressLint("SetTextI18n")
    private void setAudioPlayer(String url, final View view, String audioThum) {

        if (mPlayer != null) {
            mPlayer.stop();
        }
        int[] secound = new int[1];

        final boolean[] playFlag = {true};
        ImageView imgPlay = view.findViewById(R.id.app_video_play);
        ImageView imgBackPlayer = view.findViewById(R.id.app_video_finish);
        ImageView audioViewBackground = view.findViewById(R.id.img_audio_background);

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

        imgBackPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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
                playFlag[0] = true;
            }
        });
    }

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

                if (seekBarAudioProgress.getProgress() >= (mPlayer.getDuration() - 50)) {
                    seekBarAudioProgress.setProgress(0);
                    seekBarAudioProgress.postDelayed(onEverySecond, 50);
                    mPlayer.start();
                }
//                if(seekBarAudioProgress.)
            }
        }
    };

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
                            streampostArrayList.remove(positionSelect);
                            notifyDataSetChanged();
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
                        if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
//                            Utils.unAuthentication(context);
                        }
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

    private void setLikeApi(int id, int action) {
        Map<String, Integer> map = new HashMap<>();
        map.put("postid", id);
        map.put("uid", Integer.parseInt(preferences.getUserId()));
        map.put("action", action);
        mService.getFetcherService(context).Like(map).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {

            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {

            }
        });
    }

    private void addViewHeader(String imgurl, String postId, int index) {

        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.image_view_inflate, null);

        ImageView imgPhoto = view.findViewById(R.id.img_preview);
        TextView txtUserName = view.findViewById(R.id.txt_user_name);
//        ImageView imgUp = (ImageView) bottomTagLayout.findViewById(R.id.img_up);
        TextView txtLikeCount = view.findViewById(R.id.txt_like);
        TextView txtMore = view.findViewById(R.id.txt_more);

        viewStubVideoAudio.setVisibility(View.VISIBLE);
        viewStubVideoAudio.removeAllViews();
        viewStubVideoAudio.addView(view);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = (int) context.getResources().getDimension(R.dimen._130sdp);

        if (imgurl != null && !imgurl.isEmpty()) {
            Picasso.with(context)
                    .load(imgurl)
                    .placeholder(R.mipmap.img_placeholder)
                    .error(R.mipmap.img_placeholder)
                    .resize(width, height)
                    .into(imgPhoto);
        }

        if (mediapostArrayList.get(index).getIslike() == 0) {
            txtLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ico_xo_like_copy, 0, 0, 0);
        } else {
            txtLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.like, 0, 0, 0);
        }

        txtUserName.setText(mediapostArrayList.get(index).getPostTitle().replaceAll("[-+.^:,@]", ""));
        txtLikeCount.setText("" + mediapostArrayList.get(index).getLikecount());

        txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", "" + mediapostArrayList.get(index).getUserid());
                bundle.putString("uFullName", mediapostArrayList.get(index).getFname() + " " + mediapostArrayList.get(index).getLname());
                bundle.putString("username", mediapostArrayList.get(index).getUsername());
                bundle.putString("uProfile", mediapostArrayList.get(index).getUserphoto());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


      /*  imgUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View bottomTagLayout) {
                viewStubVideoAudio.removeAllViews();
                viewStubVideoAudio.setVisibility(View.GONE);
                headerUrl = "";
            }
        });*/

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("postId", postId);
                ((Activity) context).startActivityForResult(intent, StaticConstant.REQUEST_POST_DETAIL_MORE);
            }
        });

        txtLikeCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {

                    if (mediapostArrayList.get(index).getIslike() == 0) {
                        txtLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.like, 0, 0, 0);
                        txtLikeCount.setText("" + (mediapostArrayList.get(index).getLikecount() + 1));
                        mediapostArrayList.get(index).setLikecount(mediapostArrayList.get(index).getLikecount() + 1);
                        mediapostArrayList.get(index).setIslike(1);
                        setLikeApi(mediapostArrayList.get(index).getId(), StaticConstant.LIKE);
                    } else {
                        txtLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ico_xo_like_copy, 0, 0, 0);
                        txtLikeCount.setText("" + (mediapostArrayList.get(index).getLikecount() - 1));
                        mediapostArrayList.get(index).setLikecount(mediapostArrayList.get(index).getLikecount() - 1);
                        mediapostArrayList.get(index).setIslike(0);
                        setLikeApi(mediapostArrayList.get(index).getId(), StaticConstant.UNLIKE);
                    }
                } catch (IndexOutOfBoundsException e) {
                    Utils.showToast(context, "Index Out of Box ");
                }
            }
        });
        headerUrl = imgurl;

    }
}
