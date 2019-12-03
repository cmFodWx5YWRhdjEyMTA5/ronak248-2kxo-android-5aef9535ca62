package com.screamxo.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.apimodule.ApiBase.ApiBean.CategoryList.Category;
import com.example.apimodule.ApiBase.ApiBean.CommentBean;
import com.example.apimodule.ApiBase.ApiBean.DashBoardBean;
import com.example.apimodule.ApiBase.ApiBean.DashBoardResult;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailBean;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailMedia;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailResult;
import com.example.apimodule.ApiBase.ApiBean.Itemdetail;
import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.ApiBean.Mediapost;
import com.example.apimodule.ApiBase.ApiBean.Shop.ShopItems;
import com.example.apimodule.ApiBase.ApiBean.Streampost;
import com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean;
import com.example.apimodule.ApiBase.ApiBean.shop_dashboard.ShopDashboard;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Activity.PostDetailsActivity;
import com.screamxo.Activity.PostStreamActivity;
import com.screamxo.Activity.ProfileActivity;
import com.screamxo.Activity.RahulWork.BoostActivity;
import com.screamxo.Activity.RahulWork.CartCheckoutActivity;
import com.screamxo.Activity.RahulWork.FriendsActivity;
import com.screamxo.Activity.RahulWork.ItemDetailsAcitvity;
import com.screamxo.Activity.RahulWork.MediaItemDetailActivity;
import com.screamxo.Activity.RahulWork.WebViewActivity;
import com.screamxo.Activity.UploadMediaAcitvity;
import com.screamxo.Fragment.DashboardFragment;
import com.screamxo.Fragment.DashboardPagerFragment;
import com.screamxo.Fragment.NewCartFragment;
import com.screamxo.Interface.AudioVideoInterface;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Others.CustomLinearLayoutManager;
import com.screamxo.Others.LockableScrollView;
import com.screamxo.Others.MyPopUpWindow;
import com.screamxo.R;
import com.screamxo.Service.InlinePlayerService;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.ImageUtils;
import com.screamxo.Utils.MyApplication;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.Validations;
import com.screamxo.Utils.ViewUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tcking.github.com.giraffeplayer.ScreamxoPlayer;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.screamxo.Activity.DrawerMainActivity.CAPTURE_MEDIA;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_BOOST_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_FRIENDS_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_POST_STREAM_ACTIVITY_RESULTS;
import static com.screamxo.Utils.StaticConstant.LIMIT;
import static com.screamxo.Utils.StaticConstant.RESULT_OK;

@SuppressLint("SetTextI18n")
public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AudioVideoInterface, ScreamxoPlayer.ScreamxoPlayerClickListener {
    private static final int CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private final RecyclerView dashboardMainRv;
    private final LinearLayout rellandscape;
    public MyPopUpWindow commentFilterPopupWindow;
    public Context context;
    public String itemid = "";
    public MyApplication application;
    ArrayList<ItemDetailMedia> mediaArrayList = new ArrayList<>();
    Call<ItemDetailBean> streampostcall;
    boolean sharedClick = false;
    private int sMediaPlayerLastPosition;
    private LockableScrollView sv_container_media;
    private boolean isLocked;
    private ScreamxoPlayer screamxoPlayer = null;
    public int screenWidthPixels, screenHeightPixels;
    private DashboardFragment dashboardFragment;
    private String categoryId = "0";
    private String categoryName = "";
    private MediaPlayer mMediaPlayer;
    private TextView txtDuration, txtEndDuration;
    private SeekBar seekBarAudioProgress;
    private Handler mHandler = new Handler();
    private String TAG = DashboardAdapter.class.getSimpleName();
    private ImageVideoAdapter imagevideoAdapterAllmedia, imagevideoAdapterImageShop;
    private CommonMethod commonMethod;
    private AudioVideoInterface audioVideoInterface;
    private RecyclerView recyclerView;
    private DashBoardResult mDashBoardResult;
    private ArrayList<Mediapost> mediaposts;
    private ArrayList<Itemdetail> iteamDetail;
    private int width, height;
    private int returnCount, pageCounterForAllmedia = 2, pageCounterForShop = 2, positionSelect;
    private String uId;
    private Preferences preferences;
    private String LIMIT = StaticConstant.LIMIT;
    private boolean canLazyLoadMoreMedia = true, canLazyLoadMoreShopItems = true;
    private FetchrServiceBase mService;
    private LinearLayout ll_audio_video_image_media_container, ll_audio_video_image_shop_container, ll_media_preview_bottom_bar;
    private Validations validations;
    private Call<ShopItems> ShopApiCall;
    private Call<ShopDashboard> ShopApiCallMore;
    private Call<CommentBean> commentBeanCall;
    private String mediaType = "";
    private Dialog photoDialog;
    private FetchrServiceBase mservice;
    private FetchrServiceBase mservice1;
    private ItemDetailResult itemDetailResult;
    private String currentType;
    private String videoUrl;
    private int currentMediaPosition = -1;
    private int currentShopPosition = -1;
    private int tempIndex = -1;
    private boolean isVideo = false;
    private boolean videoClicked = false;

    private static final int REQUEST_PLAY = 1;

    /**
     * The request code for pause action PendingIntent.
     */

    private static final int REQUEST_PAUSE = 2;

    /**
     * The request code for info action PendingIntent.
     */
    private static final int REQUEST_INFO = 3;

    /**
     * The intent extra value for play action.
     */
    private static final int CONTROL_TYPE_PLAY = 1;

    private static final int CONTROL_TYPE_PAUSE = 2;

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying() && seekBarAudioProgress != null) {
                    long totalDuration = mMediaPlayer.getDuration();
                    long currentDuration = mMediaPlayer.getCurrentPosition();

                    txtEndDuration.setText(Utils.milliSecondsToTimer(totalDuration));
                    txtDuration.setText(Utils.milliSecondsToTimer(currentDuration));
                    seekBarAudioProgress.setProgress(mMediaPlayer.getCurrentPosition());
                }
                mHandler.postDelayed(this, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private boolean isPortrait;
    private RelativeLayout rl_mediaAudioBottomBar, app_video_bottom_box = null;
    private LinearLayout ll_footer = null;
    private ImageView imgPlay = null;
    private Configuration newConfig;

    private AudioManager.OnAudioFocusChangeListener focusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    switch (focusChange) {

                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                            // Lower the volume while ducking.
                            mMediaPlayer.setVolume(0.2f, 0.2f);
                            break;
                        case (AudioManager.AUDIOFOCUS_GAIN):
                            // Return the volume to normal and resume if paused.
                            try {
                                mMediaPlayer.setVolume(1f, 1f);
                            } catch (Exception e) {
                                Log.d("Exception:", e.getMessage());
                            }
                            break;
                        default:
                            break;
                    }
                }
            };

    public DashboardAdapter(Context context, CommonMethod commonMethod, DashBoardResult dashBoardResult, DashboardFragment dashboardFragment) {
        this.context = context;
        this.commonMethod = commonMethod;
        this.mDashBoardResult = dashBoardResult;
        isPortrait = context.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT;
        mService = new FetchrServiceBase();
        mediaposts = new ArrayList<>();
        iteamDetail = new ArrayList<>();
        mservice = new FetchrServiceBase();
        preferences = new Preferences(context);
        audioVideoInterface = this;
        validations = new Validations();
        uId = "" + preferences.getUserId();
        this.dashboardFragment = dashboardFragment;
        this.dashboardMainRv = dashboardFragment.mrecyclerView;
        this.rellandscape = dashboardFragment.rellandscape;
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
        if (mDashBoardResult.getStreampost().size() != 0) {
            returnCount = mDashBoardResult.getStreampost().size();
        } else {
            returnCount = 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder viewType: " + viewType);
        if (viewType == 0) {
            View v = LayoutInflater.from(context).inflate(R.layout.shop_layout, parent, false);
            return new Header(v);
        } else if (viewType == 1) {
            View v = LayoutInflater.from(context).inflate(R.layout.shop_layout, parent, false);
            return new Shop(v);
        } else if (viewType == 2) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_stream_comment_new, parent, false);
            return new StreamViewBottom(v);
        } else if (viewType == 3) {
            View v = LayoutInflater.from(context).inflate(R.layout.no_data_found, parent, false);
            return new NoData(v);
        } else {
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        if (position == 1) {
            return 1;
        }
        if (mDashBoardResult.getStreampostcount() <= 0) {
            return 3;
        }

        return 2;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0 && holder instanceof Header) {
            /*VISIBILITY*/
            if (isPortrait) {
                ((Header) holder).mRelativeLayoutTag.setVisibility(View.VISIBLE);
                ((Header) holder).includeNoData.setVisibility(mediaposts.size() == 0 && !isLocked ? View.VISIBLE : View.GONE);
                ((Header) holder).mrecyclerView.setVisibility(mediaposts.size() == 0 || isLocked ? View.GONE : View.VISIBLE);
                ((Header) holder).greyline.setVisibility(isLocked ? View.GONE : View.VISIBLE);
                ll_audio_video_image_media_container.setVisibility(isLocked || !ViewUtils.isVisible(ll_audio_video_image_media_container) ? View.GONE : View.VISIBLE);
                ll_media_preview_bottom_bar.setVisibility(isLocked && ViewUtils.isVisible(ll_media_preview_bottom_bar) ? View.GONE : View.VISIBLE);
                if (rl_mediaAudioBottomBar != null) {
                    rl_mediaAudioBottomBar.setVisibility(View.VISIBLE);
                }
            } else {
                if ((screamxoPlayer != null && screamxoPlayer.isPlaying()) || (mMediaPlayer != null && mMediaPlayer.isPlaying())) {
                    ((Header) holder).mRelativeLayoutTag.setVisibility(View.GONE);
                    ((Header) holder).includeNoData.setVisibility(View.GONE);
                    ((Header) holder).mrecyclerView.setVisibility(View.GONE);
                    ((Header) holder).greyline.setVisibility(View.GONE);

                    ll_media_preview_bottom_bar.setVisibility(View.GONE);
                    if (rl_mediaAudioBottomBar != null) {
                        rl_mediaAudioBottomBar.setVisibility(View.GONE);
                    }
                }
            }
        } else if (position == 1 && holder instanceof Shop) {
            if (isPortrait) {
                ((Shop) holder).itemView.setVisibility(View.VISIBLE);
                ((Shop) holder).includeNoData.setVisibility(iteamDetail.size() == 0 && !isLocked ? View.VISIBLE : View.GONE);
                ((Shop) holder).mrecyclerView.setVisibility(iteamDetail.size() == 0 || isLocked ? View.GONE : View.VISIBLE);
                ((Shop) holder).greyline.setVisibility(isLocked ? View.GONE : View.VISIBLE);
                ll_audio_video_image_shop_container.setVisibility(isLocked || !ViewUtils.isVisible(ll_audio_video_image_shop_container) ? View.GONE : View.VISIBLE);
            } else {
                if ((screamxoPlayer != null && screamxoPlayer.isPlaying()) || (mMediaPlayer != null && mMediaPlayer.isPlaying())) {
                    ((Shop) holder).itemView.setVisibility(View.GONE);
                }
            }
        } else if (position > 1 && holder instanceof StreamViewBottom) {

            // if (isPortrait) {
            try {
                Streampost streampost = mDashBoardResult.getStreampost().get(position - 2);
                ((StreamViewBottom) holder).mtxtUserName.setText(TextUtils.isEmpty(streampost.getUsername()) ? streampost.getFname() : streampost.getUsername());

                ((StreamViewBottom) holder).mtxtComment.setText(streampost.getPostTitle());
                if (URLUtil.isValidUrl(streampost.getPostTitle())) {
                    ((StreamViewBottom) holder).mtxtComment.setTextColor(Color.parseColor("#FF4F67"));
                }
                ((StreamViewBottom) holder).mtxtComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isVideo) {
                            playInlinePlayer();
                        }
                        if (URLUtil.isValidUrl(streampost.getPostTitle())) {
                            Intent new_intent = new Intent(context, WebViewActivity.class);
                            new_intent.putExtra("url", streampost.getPostTitle());
                            context.startActivity(new_intent);
                        } else {
                            Utils.showToast(context, "invalid link");
                        }
                        //return false;
                    }
                });
//                    ((StreamViewBottom) holder).mtxtComment.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (URLUtil.isValidUrl(streampost.getPostTitle())) {
//                                Intent new_intent = new Intent(context, WebViewActivity.class);
//                                new_intent.putExtra("url",streampost.getPostTitle());
//                                context.startActivity(new_intent);
//                            }
//                            else {
//                                Utils.showToast(context, "invalid link");
//                            }
//                        }
//                    });
                ((StreamViewBottom) holder).mtxtCommnetCount.setText("" + streampost.getCommentcount());

                String date = validations.dateFormationUTCtoLocal("yyyy-MM-dd'T'HH:mm:ss", streampost.getUpdatedDate());
                ((StreamViewBottom) holder).mtxtTime.setText(date);

                if (streampost.getUserphoto() != null && !streampost.getUserphoto().equals("")) {
                    Picasso.with(context).load(streampost.getUserphoto()).placeholder(R.mipmap.user).error(R.mipmap.user).resize(width, height).centerCrop().transform(new CircleTransform()).into((
                            (StreamViewBottom) holder).user_iv);
                }

                ((StreamViewBottom) holder).mtxtUserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isVideo) {
                            playInlinePlayer();
                        }
                        Intent intent = new Intent(context, ProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("uid", "" + streampost.getUserid());
                        bundle.putString("uFullName", streampost.getFname() + " " + streampost.getLname());
                        bundle.putString("username", streampost.getUsername());
                        bundle.putString("uProfile", streampost.getUserphotothumb());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });

                ((StreamViewBottom) holder).user_iv.setOnClickListener(view -> {
                    if (isVideo) {
                        playInlinePlayer();
                    }
                    Intent intent = new Intent(context, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("uid", "" + streampost.getUserid());
                    bundle.putString("uFullName", streampost.getFname() + " " + streampost.getLname());
                    bundle.putString("username", streampost.getUsername());
                    bundle.putString("uProfile", streampost.getUserphotothumb());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                });

                ((StreamViewBottom) holder).mtxtLike.setText("" + (streampost.getLikecount()));

                if (streampost.getIslike() == 0) {
                    ((StreamViewBottom) holder).emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
                } else {
                    ((StreamViewBottom) holder).emoji_iv.setImageResource(R.drawable.ico_reaction_new);
                }

                ((StreamViewBottom) holder).emoji_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                        } else {
                            if (streampost.getIslike() == 0) {
                                ((StreamViewBottom) holder).emoji_iv.setImageResource(R.drawable.ico_reaction_new);
                                ((StreamViewBottom) holder).mtxtLike.setText("" + (streampost.getLikecount() + 1));
                                streampost.setLikecount(streampost.getLikecount() + 1);
                                streampost.setIslike(1);
                                setLikeApi(streampost.getId(), StaticConstant.LIKE);
                            } else {
                                ((StreamViewBottom) holder).emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
                                ((StreamViewBottom) holder).mtxtLike.setText("" + (streampost.getLikecount() - 1));
                                streampost.setLikecount(streampost.getLikecount() - 1);
                                streampost.setIslike(0);
                                setLikeApi(streampost.getId(), StaticConstant.UNLIKE);
                            }
                        }
                    }
                });

                ((StreamViewBottom) holder).mtxtLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                        } else {
                            if (streampost.getIslike() == 0) {
                                ((StreamViewBottom) holder).emoji_iv.setImageResource(R.drawable.ico_reaction_new);
                                ((StreamViewBottom) holder).mtxtLike.setText("" + (streampost.getLikecount() + 1));
                                streampost.setLikecount(streampost.getLikecount() + 1);
                                streampost.setIslike(1);
                                setLikeApi(streampost.getId(), StaticConstant.LIKE);
                            } else {
                                ((StreamViewBottom) holder).emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
                                ((StreamViewBottom) holder).mtxtLike.setText("" + (streampost.getLikecount() - 1));
                                streampost.setLikecount(streampost.getLikecount() - 1);
                                streampost.setIslike(0);
                                setLikeApi(streampost.getId(), StaticConstant.UNLIKE);
                            }
                        }
                    }
                });

                ((StreamViewBottom) holder).mtxtComment.setOnClickListener(view -> {
                    if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                        ((DrawerMainActivity) context).gotoLogin(context);
                    } else {
                        if (isVideo) {
                            playInlinePlayer();
                        }
                        Intent intent = new Intent(context, PostDetailsActivity.class);
                        intent.putExtra("postId", "" + streampost.getId());
                        commonMethod.commonMethod("" + (position - 2));
                        ((Activity) context).startActivityForResult(intent, StaticConstant.REQUEST_POST_DETAIL);
                    }
                });

                ((StreamViewBottom) holder).mtxtCommnetCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                        } else {
                            if (isVideo) {
                                playInlinePlayer();
                            }
                            Intent intent = new Intent(context, PostDetailsActivity.class);
                            intent.putExtra("postId", "" + streampost.getId());
                            commonMethod.commonMethod("" + (position - 2));
                            ((Activity) context).startActivityForResult(intent, StaticConstant.REQUEST_POST_DETAIL);
                        }
                    }
                });
                ((StreamViewBottom) holder).imgComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                        } else {
                            if (isVideo) {
                                playInlinePlayer();
                            }
                            Intent intent = new Intent(context, PostDetailsActivity.class);
                            intent.putExtra("postId", "" + streampost.getId());
                            commonMethod.commonMethod("" + (position - 2));
                            ((Activity) context).startActivityForResult(intent, StaticConstant.REQUEST_POST_DETAIL);
                        }
                    }
                });
                if (streampost.getCommentcount() > 0)
                    ((StreamViewBottom) holder).imgComment.setImageResource(R.drawable.ico_comment_new);
                else
                    ((StreamViewBottom) holder).imgComment.setImageResource(R.drawable.ico_comment_new_grey);

                ((StreamViewBottom) holder).mtxtMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                        } else {
                            PopupMenu popup = new PopupMenu(context, ((StreamViewBottom) holder).mtxtMore);
                            popup.getMenuInflater().inflate(R.menu.menu_share_delete, popup.getMenu());
                            if (preferences.getUserId().equals("" + streampost.getUserid())) {
                                popup.getMenu().getItem(0).setTitle(context.getString(R.string.txt_delete));
                            } else {
                                popup.getMenu().getItem(0).setTitle(context.getString(R.string.txt_report_post));
                            }
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {

                                    positionSelect = position;

                                    switch (item.getItemId()) {
                                        case R.id.pop_up_detele:
                                            if (preferences.getUserId().equals("" + streampost.getUserid())) {
                                                callDeleteApi(streampost.getUserid(), streampost.getId(), streampost.getPosttype());
                                            } else {
                                                callReportPostApi(streampost.getUserid(), streampost.getId());
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
                    }
                });

            } catch (Exception e) {
                Log.i(TAG, "onBindViewHolder => possible indexOutOfBound on scrolling when filtering list.");
                e.printStackTrace();
            }

            ((StreamViewBottom) holder).itemView.setVisibility(isLocked ? View.GONE : View.VISIBLE);
            ll_audio_video_image_shop_container.setVisibility(isLocked || !ViewUtils.isVisible(ll_audio_video_image_shop_container) ? View.GONE : View.VISIBLE);
            // } else {
            if (!isPortrait) {

                if ((screamxoPlayer != null && screamxoPlayer.isPlaying()) || (mMediaPlayer != null && mMediaPlayer.isPlaying())) {
                    ((StreamViewBottom) holder).itemView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void callDeleteApi(int uId, int postId, int postType) {
        Log.d(TAG, "callDeleteApi: ");

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
                            mDashBoardResult.getStreampost().remove(positionSelect - 2);
                            returnCount = mDashBoardResult.getStreampost().size();
                            notifyDataSetChanged();
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

    private void callReportPostApi(int uId, int postId) {
        Log.d(TAG, "callReportPostApi: ");
        Map<String, Integer> map = new HashMap<>();

        map.put("uid", uId);
        map.put("postid", postId);

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

    private void setBookmark(int id, int action) {
        Log.d(TAG, "setBookmark: ");
        Map<String, Integer> map = new HashMap<>();
        map.put("itemid", id);
        map.put("uid", Integer.parseInt(preferences.getUserId()));
        map.put("action", action);
        mService.getFetcherService(context).Bookmark(map).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                int type;
                if (response.body().getStatus().equalsIgnoreCase(StaticConstant.STATUS_1)) {
                    type = DialogBox.DIALOG_SUCESS;
                } else {
                    type = DialogBox.DIALOG_FAILURE;
                }
                DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), type, null);
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
            }
        });
    }

    private void setLikeApi(int id, int action) {
        Log.d(TAG, "setLikeApi: ");
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

    @Override
    public int getItemCount() {
        return (returnCount + 2);
    }

    private void openPopUpmenu(View view, ImageView img_filter, int data) {
        Log.d(TAG, "openPopUpmenu: ");
        if (data == 0) {
            MyPopUpWindow popUpWindow = new MyPopUpWindow(context, view, new String[]{"All", "Video", "Audio", "Music", "Image", "Trending"}, img_filter, "Dashboard");
            popUpWindow.show(img_filter, MyPopUpWindow.PopUpPosition.RIGHT);

            if (((Activity) context) instanceof DrawerMainActivity) {
                ((DrawerMainActivity) context).setTranperentVisibility(View.VISIBLE);
            }

            popUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (((Activity) context) instanceof DrawerMainActivity) {
                        ((DrawerMainActivity) context).setTranperentVisibility(View.GONE);
                    }
                }
            });
            popUpWindow.setOnPopupItemClickListner(new MyPopUpWindow.OnPopupItemClickListener() {
                @Override
                public boolean onPopupItemClick(int position) {
                    // 0 = video , 1 = Image , 2 = Voice , 3 = Audio 4 = all 5 = trending
                    switch (position) {
                        case 0:
                            callFilterApiFromActivity("");
                            break;
                        case 1:
                            callFilterApiFromActivity("1");
                            break;
                        case 2:
                            callFilterApiFromActivity("3");
                            break;
                        case 3:
                            callFilterApiFromActivity("4");
                            break;
                        case 4:
                            callFilterApiFromActivity("2");
                            break;
                        case 5:
                            callFilterApiFromActivity("-1");
                            break;
                    }
                    popUpWindow.dismiss();
                    return true;
                }
            });

        } else if (data == 1) {
            try {
//                Result category = new Gson().fromJson(preferences.getCategory(), Result.class);
                ArrayList<Category> category = preferences.getCategory();
                String[] categoryList = new String[category.size()];
                for (int i = 0; i < category.size(); i++) {
                    categoryList[i] = category.get(i).getCategoryName();
                }

                MyPopUpWindow popUpWindow = new MyPopUpWindow(context, view, categoryList, img_filter, "Dashboardshop");
                popUpWindow.show(img_filter, MyPopUpWindow.PopUpPosition.RIGHT);

                if (((Activity) context) instanceof DrawerMainActivity) {
                    ((DrawerMainActivity) context).setTranperentVisibility(View.VISIBLE);
                }

                popUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (((Activity) context) instanceof DrawerMainActivity) {
                            ((DrawerMainActivity) context).setTranperentVisibility(View.GONE);
                        }
                    }
                });
                popUpWindow.setOnPopupItemClickListner(new MyPopUpWindow.OnPopupItemClickListener() {
                    @Override
                    public boolean onPopupItemClick(int position) {
                        ll_audio_video_image_shop_container.removeAllViews();
                        ll_audio_video_image_shop_container.setVisibility(View.GONE);
                        currentShopPosition = -1;
                        if (!String.valueOf(category.get(position).getId()).equals("20"))
                            filterShop(String.valueOf(category.get(position).getId()));
                        else
                            callShopFromActivity(category.get(position).getCategoryName(), String.valueOf(category.get(position).getId()));
                        popUpWindow.dismiss();
                        return true;
                    }
                });

            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        } else {
            String[] datacateg = new String[5];
            commentFilterPopupWindow = new MyPopUpWindow(context, img_filter, datacateg, img_filter, "commentFilter");
            commentFilterPopupWindow.show(img_filter, MyPopUpWindow.PopUpPosition.RIGHT);

            if (((Activity) context) instanceof DrawerMainActivity) {
                ((DrawerMainActivity) context).setTranperentVisibility(View.VISIBLE);
            }
            commentFilterPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (((Activity) context) instanceof DrawerMainActivity) {
                        ((DrawerMainActivity) context).setTranperentVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void callMoreShopApi() {
        Log.d(TAG, "callMoreShopApi categoryId: " + categoryId);
        Log.d(TAG, "callMoreShopApi categoryName: " + categoryName);
        Map<String, String> map = new HashMap<>();
        map.put("uid", uId);
        map.put("limit", LIMIT);
        map.put("offset", "" + 0);
//        if (!categoryId.equals("20"))
        map.put("categoryid", categoryId);

        if (categoryName.equalsIgnoreCase("trending"))
            map.put("trending", "2");


//        if (StaticConstant.SEARCHSTRING.length() > 0) {
        map.put("searchstring", StaticConstant.SEARCHSTRING);
        map.put("userfiltertype", "2");
        map.put("posttype", "2");
        map.put("type", "3");
        map.put("mediatype", "");
        // }
        if (Utils.isInternetOn(context)) {
            if (categoryName.equalsIgnoreCase("trending")) {
                if (pageCounterForShop < 2)
                    dashboardFragment.progreessbar.setVisibility(View.VISIBLE);

                ShopApiCallMore = mService.getFetcherService(context).getShopDashboardData(map);
                ShopApiCallMore.enqueue(new retrofit2.Callback<ShopDashboard>() {
                    @Override
                    public void onResponse(Call<ShopDashboard> call, Response<ShopDashboard> response) {
                        dashboardFragment.progreessbar.setVisibility(View.GONE);
                        if (((DrawerMainActivity) context).fragment != null && ((DrawerMainActivity) context).fragment instanceof DashboardPagerFragment) {
                            ((DashboardPagerFragment) ((DrawerMainActivity) context).fragment).viewPager.setPagingEnabled(true);
                        }
                        canLazyLoadMoreShopItems = true;
                        if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
                            if (response.body().getMessage().equals("Fetching data success")) {

                                if (pageCounterForShop == 1) {
                                    iteamDetail.clear();
                                }
                                for (int i = 0; i < response.body().getResult().getRecentitems().getItemdetails().size(); i++) {
                                    Itemdetail itemdetail = new Itemdetail();

                                    itemdetail.setUserid(response.body().getResult().getRecentitems().getItemdetails().get(i).getUserId());
                                    itemdetail.setItemId(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemId());
                                    itemdetail.setItemName(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemName());
                                    itemdetail.setItemPrice(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemPrice());
                                    itemdetail.setMyitem(response.body().getResult().getRecentitems().getItemdetails().get(i).getMyitem());
                                    itemdetail.setItemQty(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemQty());
                                    itemdetail.setItemQtyRemained(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemQtyRemained());
                                    itemdetail.setIspurchased(response.body().getResult().getRecentitems().getItemdetails().get(i).getIspurchased());
                                    itemdetail.setMediaType(response.body().getResult().getRecentitems().getItemdetails().get(i).getMediaType());
                                    itemdetail.setMediaUrl(response.body().getResult().getRecentitems().getItemdetails().get(i).getMediaUrl());
                                    itemdetail.setMediaThumb(response.body().getResult().getRecentitems().getItemdetails().get(i).getMediaThumb());
                                    itemdetail.setUsername(response.body().getResult().getRecentitems().getItemdetails().get(i).getUsername());
                                    itemdetail.setFname(response.body().getResult().getRecentitems().getItemdetails().get(i).getFname());
                                    itemdetail.setLname(response.body().getResult().getRecentitems().getItemdetails().get(i).getLname());
                                    itemdetail.setItemActualprice(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemActualprice());
                                    itemdetail.setItemShippingCost(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemShippingCost());
                                    itemdetail.setItemCreatedBy(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemCreatedBy());
                                    itemdetail.setItemCreatedBy(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemCreatedBy());
                                    itemdetail.setIspaypal(response.body().getResult().getRecentitems().getItemdetails().get(i).getIspaypal());
                                    itemdetail.setIsbitcoin(response.body().getResult().getRecentitems().getItemdetails().get(i).getBitcoinacc());
                                    itemdetail.setBitcoinmail(response.body().getResult().getRecentitems().getItemdetails().get(i).getBitcoinmail());
                                    iteamDetail.add(itemdetail);

                                }


                                pageCounterForShop++;
                                imagevideoAdapterImageShop.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ShopDashboard> call, Throwable t) {
                        dashboardFragment.progreessbar.setVisibility(View.GONE);

                        if (((DrawerMainActivity) context).fragment != null && ((DrawerMainActivity) context).fragment instanceof DashboardPagerFragment) {
                            ((DashboardPagerFragment) ((DrawerMainActivity) context).fragment).viewPager.setPagingEnabled(true);
                        }
                        canLazyLoadMoreShopItems = true;
                    }
                });
            } else {
                ShopApiCall = mService.getFetcherService(context).ShopMore(map);
                ShopApiCall.enqueue(new retrofit2.Callback<ShopItems>() {
                    @Override
                    public void onResponse(Call<ShopItems> call, Response<ShopItems> response) {
                        if (((DrawerMainActivity) context).fragment != null && ((DrawerMainActivity) context).fragment instanceof DashboardPagerFragment) {
                            ((DashboardPagerFragment) ((DrawerMainActivity) context).fragment).viewPager.setPagingEnabled(true);
                        }
                        canLazyLoadMoreShopItems = true;
                        if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
//                            if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (pageCounterForShop == 1) {
                                iteamDetail.clear();
                            }
                            iteamDetail.addAll(response.body().getResult().getItems());
                            pageCounterForShop++;
                            imagevideoAdapterImageShop.notifyDataSetChanged();
//                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ShopItems> call, Throwable t) {
                        t.printStackTrace();
                        if (((DrawerMainActivity) context).fragment != null && ((DrawerMainActivity) context).fragment instanceof DashboardPagerFragment) {
                            ((DashboardPagerFragment) ((DrawerMainActivity) context).fragment).viewPager.setPagingEnabled(true);
                        }
                        canLazyLoadMoreShopItems = true;
                    }
                });
            }


        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
            if (((DrawerMainActivity) context).fragment != null && ((DrawerMainActivity) context).fragment instanceof DashboardPagerFragment) {
                ((DashboardPagerFragment) ((DrawerMainActivity) context).fragment).viewPager.setPagingEnabled(true);
            }
        }
    }

    private void callMoreMedia(String mediaType) {
        Log.d(TAG, "callMoreMedia: ");
        Map<String, String> map = new HashMap<>();
        map.put("uid", uId);
        map.put("limit", LIMIT);
        map.put("mediatype", mediaType);
        map.put("offset", "" + pageCounterForAllmedia);
        map.put("myid", uId);

        if (StaticConstant.SEARCHSTRING.length() > 0) {
            map.put("searchstring", StaticConstant.SEARCHSTRING);
            map.put("userfiltertype", "2");
            map.put("posttype", "2");
        }

        if (mediaType.equals("-1")) {
            map.put("trending_mode", "1");
        }

        if (Utils.isInternetOn(context)) {
            if (pageCounterForAllmedia < 2)
                dashboardFragment.progreessbar.setVisibility(View.VISIBLE);

            Call<com.example.apimodule.ApiBase.ApiBean.Comments.Posts> shopMediaCall = mService.getFetcherService(context).MediaMore(map);
            shopMediaCall.enqueue(new retrofit2.Callback<com.example.apimodule.ApiBase.ApiBean.Comments.Posts>() {
                @Override
                public void onResponse(Call<com.example.apimodule.ApiBase.ApiBean.Comments.Posts> call, Response<com.example.apimodule.ApiBase.ApiBean.Comments.Posts> response) {
                    dashboardFragment.progreessbar.setVisibility(View.GONE);
                    if (((DrawerMainActivity) context).fragment != null && ((DrawerMainActivity) context).fragment instanceof DashboardPagerFragment) {
                        ((DashboardPagerFragment) ((DrawerMainActivity) context).fragment).viewPager.setPagingEnabled(true);
                    }
                    canLazyLoadMoreMedia = true;
                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
//                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                        if (pageCounterForAllmedia == 1) {
                            mediaposts.clear();
                        }
                        mDashBoardResult.setMediapostcount(response.body().getResult().getTotalcount());
                        mediaposts.addAll(response.body().getResult().getPosts());
                        pageCounterForAllmedia++;
                        imagevideoAdapterAllmedia.notifyDataSetChanged();
                        notifyDataSetChanged();
//                        }
                    }
                }

                @Override
                public void onFailure(Call<com.example.apimodule.ApiBase.ApiBean.Comments.Posts> call, Throwable t) {
                    dashboardFragment.progreessbar.setVisibility(View.GONE);
                    if (((DrawerMainActivity) context).fragment != null && ((DrawerMainActivity) context).fragment instanceof DashboardPagerFragment) {
                        ((DashboardPagerFragment) ((DrawerMainActivity) context).fragment).viewPager.setPagingEnabled(true);
                    }
                    t.printStackTrace();
                    canLazyLoadMoreMedia = true;
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
            if (((DrawerMainActivity) context).fragment != null && ((DrawerMainActivity) context).fragment instanceof DashboardPagerFragment) {
                ((DashboardPagerFragment) ((DrawerMainActivity) context).fragment).viewPager.setPagingEnabled(true);
            }
        }
    }

    public void hideToprecyclerview(int visibility) {
        Log.d(TAG, "hideToprecyclerview: ");
        if (mDashBoardResult.getMediapostcount() == 0) {
            View view = (View) recyclerView.getParent();
            LinearLayout linearLayout = view.findViewById(R.id.include_no_data);
            linearLayout.setVisibility(visibility);
        } else {
            recyclerView.setVisibility(visibility);
            View view = (View) recyclerView.getParent();
            LinearLayout linearLayout = view.findViewById(R.id.view_player);
            if (linearLayout.getChildCount() != 0) {
                linearLayout.setVisibility(visibility);
            }

//            linearLayout.removeAllViews();
//            bottomTagLayout.setVisibility(visibility);
        }
        commonMethod.commonMethod("scroll");
//        View bottomTagLayout = (View) recyclerView.getParent();
//        ((LinearLayout) bottomTagLayout).removeAllViews();
    }

    private void callFilterApiFromActivity(String filterType) {
        Log.d(TAG, "callFilterApiFromActivity: ");
        pageCounterForAllmedia = 1;
        if (ShopApiCall != null) {
            ShopApiCall.cancel();
        }
        callMoreMedia(filterType);
        mediaType = filterType;
        releaseMediaPlayer();
        stopVideoPlayer();
        ll_audio_video_image_media_container.removeAllViews();
        ll_audio_video_image_media_container.setVisibility(View.GONE);
    }

    private void releaseMediaPlayer() {
        try {
            if (mMediaPlayer != null) {
                sMediaPlayerLastPosition = 0;
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void stopVideoPlayer() {
        if (screamxoPlayer != null) {
            screamxoPlayer.stop();
        }
    }

    private void callShopFromActivity(String categoryName, String filterType) {
        pageCounterForShop = 1;
        this.categoryName = categoryName;
        categoryId = filterType;
        callMoreShopApi();
    }

    public void setReturnCount(int size) {
        if (size == 0) {
            returnCount = 1;
        } else {
            returnCount = size;
        }
    }

    private void stopService() {
        try {
            Intent intent = new Intent(context, InlinePlayerService.class);
            (context).stopService(intent);
        } catch (Exception e) {

        }
    }

    @SuppressLint("NewApi")
    private void showOverLayPermission(Context context, boolean isShowOverlayPermission, String videoUrl) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            // stopService();
            final Intent intent = new Intent(context, InlinePlayerService.class);
            intent.putExtra("videoUrl", videoUrl);
            ContextCompat.startForegroundService(context, intent);
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (Settings.canDrawOverlays(context)) {
                // stopService();
                final Intent intent = new Intent(context, InlinePlayerService.class);
                intent.putExtra("videoUrl", videoUrl);
                ContextCompat.startForegroundService(context, intent);
                return;
            }

            if (isShowOverlayPermission) {
                final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
                ((Activity) context).startActivityForResult(intent, CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void clearVideoPreviewView() {
        Log.d(TAG, "clearVideoPreviewView: ");
        stopVideoPlayer();
        isVideo = false;
        videoUrl = "";
        ll_audio_video_image_media_container.removeAllViews();
        ll_media_preview_bottom_bar.removeAllViews();
        ll_media_preview_bottom_bar.setVisibility(View.GONE);
        ll_audio_video_image_media_container.setVisibility(View.GONE);
        currentMediaPosition = -1;
    }

    /* ON CLICK OF AUDIO FROM IMAGE VIDEO ADAPTER*/

    @Override
    public void Audio(String url, String audioThum, String title, String type, int index) {
        Log.d(TAG, "Audio index: " + index);
        this.currentType = type;
        releaseMediaPlayer();
        stopVideoPlayer();

        if (currentMediaPosition != index) {
            currentMediaPosition = index;
            View audioViewContainer = LayoutInflater.from(context).inflate(R.layout.audio_view_layout, ll_audio_video_image_media_container, false);

            rl_mediaAudioBottomBar = audioViewContainer.findViewById(R.id.audio_bottom_view);
            app_video_bottom_box = audioViewContainer.findViewById(R.id.app_video_bottom_box);
            ll_footer = audioViewContainer.findViewById(R.id.ll_footer);
            FrameLayout user_image_container = audioViewContainer.findViewById(R.id.user_image_container);
            ImageView emoji_iv = audioViewContainer.findViewById(R.id.emoji_iv);
            ImageView user_iv = audioViewContainer.findViewById(R.id.user_iv);

            TextView txtUserName = audioViewContainer.findViewById(R.id.txt_user_name);
            TextView txtLikeCount = audioViewContainer.findViewById(R.id.txt_like);
            TextView txtTitle = audioViewContainer.findViewById(R.id.txt_title);
            ImageView imgShare = audioViewContainer.findViewById(R.id.img_share);
            Button btnShare = audioViewContainer.findViewById(R.id.btnShare);
            ImageView imgComment = audioViewContainer.findViewById(R.id.img_comment);


            ll_media_preview_bottom_bar.setVisibility(View.GONE);
            ll_media_preview_bottom_bar.removeAllViews();
            commonMethod.commonMethod("0");

            ll_audio_video_image_media_container.setVisibility(View.VISIBLE);
            ll_audio_video_image_media_container.removeAllViews();
            ll_audio_video_image_media_container.addView(audioViewContainer);

//            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
//                int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPixels, heightPixels);
//                if ((mediaposts.get(currentMediaPosition).getMediaThumb().contains("voice"))
//                        || (mediaposts.get(currentMediaPosition).getMediaThumb().contains("audio"))) {
//                    ll_audio_video_image_media_container.findViewById(R.id.relative_audio).setLayoutParams(layoutParams);
//                    ll_audio_video_image_media_container.findViewById(R.id.img_audio_background).setPadding(300, 0, 400, 0);
//                    ll_audio_video_image_media_container.findViewById(R.id.audio_bottom_view).setVisibility(View.GONE);
//                }
//                ll_audio_video_image_media_container.setLayoutParams(layoutParams);
//                ll_audio_video_image_media_container.setBackgroundColor(Color.BLACK);
//            }

            txtTitle.setText(mediaposts.get(currentMediaPosition).getPostTitle().replaceAll("[-+.^:,@]", ""));

            Picasso.with(context)
                    .load(mediaposts.get(currentMediaPosition).getUserphotothumb())
                    .placeholder(R.mipmap.user)
                    .error(R.mipmap.user)
                    .transform(new CircleTransform())
                    .into(user_iv);

            user_iv.setOnClickListener(viewProfile ->
            {
                if (isVideo) {
                    playInlinePlayer();
                }
                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", "" + mediaposts.get(currentMediaPosition).getUserid());
                bundle.putString("uFullName", mediaposts.get(currentMediaPosition).getFname() + " "
                        + mediaposts.get(currentMediaPosition).getLname());
                bundle.putString("username", mediaposts.get(currentMediaPosition).getUsername());
                bundle.putString("uProfile", mediaposts.get(currentMediaPosition).getUserphotothumb());
                intent.putExtras(bundle);
                context.startActivity(intent);
            });

            try {
                if (mediaposts.get(currentMediaPosition).getUserid() == Integer.parseInt(preferences.getUserId())) {
                    btnShare.setText("BOOST");
                } else {
                    btnShare.setText("Share");
                }
            } catch (NumberFormatException e) {
                btnShare.setText("Share");
            }
            String name = mediaposts.get(currentMediaPosition).getPostTitle().replaceAll("[-+.^:,@]", "");
            txtUserName.setText(name);
            txtLikeCount.setText("" + mediaposts.get(currentMediaPosition).getLikecount());
            if (mediaposts.get(currentMediaPosition).getCommentcount() > 0)
                imgComment.setImageResource(R.drawable.ico_comment_new);
            else
                imgComment.setImageResource(R.drawable.ico_comment_new_grey);

            if (mediaposts.get(currentMediaPosition).getIslike() == 0) {
                emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
            } else {
                emoji_iv.setImageResource(R.drawable.ico_reaction_new);
            }

            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedClick = true;
                    if (mMediaPlayer != null)
                        mMediaPlayer.pause();
                    if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                        ((DrawerMainActivity) context).gotoLogin(context);
                        return;
                    }

                    if (isVideo) {
                        playInlinePlayer();
                    }

                    StaticConstant.from_ = "media";
                    StaticConstant.from_type = mediaposts.get(currentMediaPosition).getMediaType();
                    StaticConstant.from_url = mediaposts.get(currentMediaPosition).getMediaUrl();

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, (mediaposts.get(currentMediaPosition).getMediaUrl()));
                    sendIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(sendIntent, "Send to"));
                }
            });

            txtUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                        ((DrawerMainActivity) context).gotoLogin(context);
                    } else {
                        if (isVideo) {
                            playInlinePlayer();
                        }

                        Intent intent = new Intent(context, MediaItemDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("itemId", "" + mediaposts.get(currentMediaPosition).getId());
                        bundle.putString("username", mediaposts.get(currentMediaPosition).getUsername());
                        intent.putExtras(bundle);
                        ((Activity) context).startActivityForResult(intent, REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS);
                    }
                }
            });

            imgComment.setOnClickListener(viewComment -> {
                if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                    ((DrawerMainActivity) context).gotoLogin(context);
                } else {
                    if (isVideo) {
                        playInlinePlayer();
                    }
                    Intent intent = new Intent(context, MediaItemDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("itemId", "" + mediaposts.get(currentMediaPosition).getId());
                    bundle.putString("username", mediaposts.get(currentMediaPosition).getUsername());
                    intent.putExtras(bundle);
                    ((Activity) context).startActivityForResult(intent, REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS);
                }
            });

            emoji_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                        ((DrawerMainActivity) context).gotoLogin(context);
                    } else {
                        try {
                            if (mediaposts.get(currentMediaPosition).getIslike() == 0) {
                                emoji_iv.setImageResource(R.drawable.ico_reaction_new);
                                txtLikeCount.setText("" + (mediaposts.get(currentMediaPosition).getLikecount() + 1));
                                mediaposts.get(currentMediaPosition).setLikecount(mediaposts.get(currentMediaPosition).getLikecount() + 1);
                                mediaposts.get(currentMediaPosition).setIslike(1);
                                setLikeApi(mediaposts.get(currentMediaPosition).getId(), StaticConstant.LIKE);
                            } else {
                                emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
                                txtLikeCount.setText("" + (mediaposts.get(currentMediaPosition).getLikecount() - 1));
                                mediaposts.get(currentMediaPosition).setLikecount(mediaposts.get(currentMediaPosition).getLikecount() - 1);
                                mediaposts.get(currentMediaPosition).setIslike(0);
                                setLikeApi(mediaposts.get(currentMediaPosition).getId(), StaticConstant.UNLIKE);
                            }
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
//                                Utils.showToast(context, "Index Out of Box ");
                        }
                    }
                }
            });

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMediaPlayer != null)
//                        mMediaPlayer.pause();
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                            return;
                        }

                    if (mediaposts.get(currentMediaPosition).getUserid() == Integer.parseInt(preferences.getUserId())) {
                        if (isVideo) {
                            playInlinePlayer();
                        }
                        Intent gotoActivity = new Intent(context, BoostActivity.class);
                        gotoActivity.putExtra("itemid", String.valueOf(mediaposts.get(currentMediaPosition).getId()));
                        if (mediaposts.get(currentMediaPosition).getMediaType().startsWith("image")) {
                            gotoActivity.putExtra("image", mediaposts.get(currentMediaPosition).getMediaUrl());
                        } else {
                            gotoActivity.putExtra("image", mediaposts.get(currentMediaPosition).getMediaThumb());
                        }
                        gotoActivity.putExtra("item_name", mediaposts.get(currentMediaPosition).getPostTitle());
                        gotoActivity.putExtra("boost_type", String.valueOf(StaticConstant.BOOST_TYPE_MEDIA_ITEM));
                        gotoActivity.putExtra("boost_url", String.valueOf(mediaposts.get(currentMediaPosition).getBoost_url()));
                        ((Activity) context).startActivityForResult(gotoActivity, REQ_CODE_BOOST_ACTIVITY_RESULTS);
                    } else {
                        StaticConstant.from_ = "media";
                        StaticConstant.from_type = mediaposts.get(currentMediaPosition).getMediaType();
                        StaticConstant.from_url = mediaposts.get(currentMediaPosition).getMediaUrl();
                        ((DrawerMainActivity) context).setFragment(5);
                    }
                }
            });

            setAudioPlayer(audioViewContainer);

            if (newConfig != null) {
                onConfigurationChanged(newConfig);
            } else
                setPortrait();


        } else {
            ll_audio_video_image_media_container.removeAllViews();
            ll_audio_video_image_media_container.setVisibility(View.GONE);
            currentMediaPosition = -1;
            sMediaPlayerLastPosition = 0;
        }
    }

    @Override
    public void Image(String imgurl, String postId, int index, String type) {
        this.currentType = type;
//        Issue_091 fix issue by commenting following two lines:FIXED
        // 0 = all media
// 1 = Shop
        switch (type) {
            case "0":
                ll_media_preview_bottom_bar.removeAllViews();
                ll_media_preview_bottom_bar.setVisibility(View.GONE);

                if (currentMediaPosition != index) {
                    releaseMediaPlayer();
                    stopVideoPlayer();
                    currentMediaPosition = index;
                    addViewHeader();
                } else {
                    ll_audio_video_image_media_container.removeAllViews();
                    ll_audio_video_image_media_container.setVisibility(View.GONE);
                    currentMediaPosition = -1;
                }
                break;
            case "1":
                if (currentShopPosition != index) {
                    currentShopPosition = index;
                    addViewShop();
                } else {
                    ll_audio_video_image_shop_container.removeAllViews();
                    ll_audio_video_image_shop_container.setVisibility(View.GONE);
                    currentShopPosition = -1;
                }
                break;
        }

        if (newConfig != null)
            onConfigurationChanged(newConfig);
        else
            setPortrait();
    }

    @Override
    public void Video(String url, String type, int index) {
        Log.d(TAG, "Video index: " + index);
        videoClicked = true;
        this.currentType = type;
        videoUrl = url;
        releaseMediaPlayer();
        stopVideoPlayer();
        if (currentMediaPosition != index) {
            currentMediaPosition = index;
            View girrafePlayerView = LayoutInflater.from(context).inflate(R.layout.view_screamxo_player, ll_audio_video_image_media_container, false);
            @SuppressLint("InflateParams")
            View viewBottomView = LayoutInflater.from(context).inflate(R.layout.video_bottom_view, null);
            isVideo = true;
            ll_audio_video_image_media_container.removeAllViews();
            ll_media_preview_bottom_bar.removeAllViews();

            ll_audio_video_image_media_container.addView(girrafePlayerView);
            ll_media_preview_bottom_bar.setPadding(Utils.dip2pixel(context, 10), 0, Utils.dip2pixel(context, 23), 25);

            ll_media_preview_bottom_bar.addView(viewBottomView);

            FrameLayout user_image_container = viewBottomView.findViewById(R.id.user_image_container);
            TextView txtUserName = viewBottomView.findViewById(R.id.txt_user_name);
            TextView txtLikeCount = viewBottomView.findViewById(R.id.txt_like);
            Button btnShare = viewBottomView.findViewById(R.id.btnShare);
            ImageView emoji_iv = viewBottomView.findViewById(R.id.emoji_iv);
            ImageView user_iv = viewBottomView.findViewById(R.id.user_iv);
            ImageView imgComment = viewBottomView.findViewById(R.id.img_comment);

            ll_audio_video_image_media_container.setVisibility(View.VISIBLE);
            ll_media_preview_bottom_bar.setVisibility(View.VISIBLE);

            try {
                if (mediaposts.get(currentMediaPosition).getUserid() == Integer.parseInt(preferences.getUserId())) {
                    btnShare.setText("BOOST");
                } else {
                    btnShare.setText("Share");
                }
            } catch (NumberFormatException e) {
                btnShare.setText("Share");
            }

            Picasso.with(context)
                    .load(mediaposts.get(currentMediaPosition).getUserphotothumb())
                    .placeholder(R.mipmap.user)
                    .error(R.mipmap.user)
                    .transform(new CircleTransform())
                    .into(user_iv);

            user_iv.setOnClickListener(view -> {
                if (isVideo) {
                    playInlinePlayer();
                }
                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", "" + mediaposts.get(currentMediaPosition).getUserid());
                bundle.putString("uFullName", mediaposts.get(currentMediaPosition).getFname() + " "
                        + mediaposts.get(currentMediaPosition).getLname());
                bundle.putString("username", mediaposts.get(currentMediaPosition).getUsername());
                bundle.putString("uProfile", mediaposts.get(currentMediaPosition).getUserphotothumb());
                intent.putExtras(bundle);
                context.startActivity(intent);
            });

            txtUserName.setText((mediaposts.get(currentMediaPosition).getPostTitle().replaceAll("[-+.^:,@]", "")
                    .replace("\"", ""))
                    .replace("\"", ""));

            txtLikeCount.setText("" + mediaposts.get(currentMediaPosition).getLikecount());

            if (mediaposts.get(currentMediaPosition).getCommentcount() > 0)
                imgComment.setImageResource(R.drawable.ico_comment_new);
            else
                imgComment.setImageResource(R.drawable.ico_comment_new_grey);

            if (mediaposts.get(currentMediaPosition).getIslike() == 0) {
                emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
            } else {
                emoji_iv.setImageResource(R.drawable.ico_reaction_new);
            }

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                        ((DrawerMainActivity) context).gotoLogin(context);
                        return;
                    }
                    if (mediaposts.get(currentMediaPosition).getUserid() == Integer.parseInt(preferences.getUserId())) {
                        if (isVideo) {
                            playInlinePlayer();
                        }
                        Intent gotoActivity = new Intent(context, BoostActivity.class);
                        if ((mediaposts.get(currentMediaPosition).getMediaType().startsWith("video"))) {
                            gotoActivity.putExtra("image", mediaposts.get(currentMediaPosition).getMediaThumb());
                            gotoActivity.putExtra("videourl", mediaposts.get(currentMediaPosition).getMediaUrl());
                        } else if (mediaposts.get(currentMediaPosition).getMediaType().startsWith("image")) {
                            gotoActivity.putExtra("image", mediaposts.get(currentMediaPosition).getMediaUrl());
                        } else {
                            gotoActivity.putExtra("image", mediaposts.get(currentMediaPosition).getMediaThumb());
                        }
                        gotoActivity.putExtra("boost_type", String.valueOf(StaticConstant.BOOST_TYPE_MEDIA_ITEM));
                        gotoActivity.putExtra("itemid", String.valueOf(mediaposts.get(currentMediaPosition).getId()));
                        gotoActivity.putExtra("boost_url", String.valueOf(mediaposts.get(currentMediaPosition).getBoost_url()));
                        ((Activity) context).startActivityForResult(gotoActivity, REQ_CODE_BOOST_ACTIVITY_RESULTS);
                    } else {
                        StaticConstant.from_ = "media";
                        StaticConstant.from_type = mediaposts.get(currentMediaPosition).getMediaType();
                        StaticConstant.from_url = mediaposts.get(currentMediaPosition).getMediaUrl();
                        ((DrawerMainActivity) context).setFragment(5);
                    }
                }
            });

            txtUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                        ((DrawerMainActivity) context).gotoLogin(context);
                    } else {
                        if (isVideo) {
                            playInlinePlayer();
                        }
                        Log.d(TAG, "onClick mediaposts.get(currentMediaPosition).getId(): " + mediaposts.get(currentMediaPosition).getId());
                        Intent intent = new Intent(context, MediaItemDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("itemId", "" + mediaposts.get(currentMediaPosition).getId());
                        bundle.putString("username", mediaposts.get(currentMediaPosition).getUsername());
                        intent.putExtras(bundle);
                        ((Activity) context).startActivityForResult(intent, REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS);
                    }
                }
            });

            imgComment.setOnClickListener(view ->
            {
                if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                    ((DrawerMainActivity) context).gotoLogin(context);
                } else {
                    if (isVideo) {
                        playInlinePlayer();
                    }
                    Log.d(TAG, "onClick mediaposts.get(currentMediaPosition).getId(): " + mediaposts.get(currentMediaPosition).getId());
                    Intent intent = new Intent(context, MediaItemDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("itemId", "" + mediaposts.get(currentMediaPosition).getId());
                    bundle.putString("username", mediaposts.get(currentMediaPosition).getUsername());
                    intent.putExtras(bundle);
                    ((Activity) context).startActivityForResult(intent, REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS);
                }
            });

            emoji_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                        ((DrawerMainActivity) context).gotoLogin(context);
                    } else {
                        try {
                            if (mediaposts.get(currentMediaPosition).getIslike() == 0) {
                                emoji_iv.setImageResource(R.drawable.ico_reaction_new);
                                txtLikeCount.setText("" + (mediaposts.get(currentMediaPosition).getLikecount() + 1));
                                mediaposts.get(currentMediaPosition).setLikecount(mediaposts.get(currentMediaPosition).getLikecount() + 1);
                                mediaposts.get(currentMediaPosition).setIslike(1);
                                setLikeApi(mediaposts.get(currentMediaPosition).getId(), StaticConstant.LIKE);
                            } else {
                                emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
                                txtLikeCount.setText("" + (mediaposts.get(currentMediaPosition).getLikecount() - 1));
                                mediaposts.get(currentMediaPosition).setLikecount(mediaposts.get(currentMediaPosition).getLikecount() - 1);
                                mediaposts.get(currentMediaPosition).setIslike(0);
                                setLikeApi(mediaposts.get(currentMediaPosition).getId(), StaticConstant.UNLIKE);
                            }
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            screamxoPlayer = new ScreamxoPlayer((AppCompatActivity) context);
            screamxoPlayer.setScaleType(ScreamxoPlayer.SCALETYPE_FITXY);
            screamxoPlayer.play(mediaposts.get(currentMediaPosition).getMediaUrl());
            screamxoPlayer.setScreamxoPlayerClickListener(this);
            screenWidthPixels = screamxoPlayer.screenWidthPixels;
            screenHeightPixels = screamxoPlayer.screenHeightPixels;
            ((DrawerMainActivity) context).setHeigthWidth(screenWidthPixels, screenHeightPixels);

            screamxoPlayer.onComplete(() ->
            {
                for (int i = currentMediaPosition + 1; i < mediaposts.size(); i++) {
                    if (mediaposts.get(currentMediaPosition).getPosttype() == mediaposts.get(i).getPosttype()) {
                        Video(null, null, i);
                        break;
                    }
                }
            });


        } else {
            clearVideoPreviewView();
        }

        if (newConfig != null) {
            onConfigurationChanged(newConfig);
        } else
            setPortrait();
    }


    public void playInlinePlayer() {
        if (!TextUtils.isEmpty(videoUrl)) {
            showOverLayPermission(context, true, videoUrl);
        } else if (currentMediaPosition != -1) {
            if (mediaposts != null || mediaposts.size() != 0)
                showOverLayPermission(context, true, mediaposts.get(currentMediaPosition).getMediaUrl());
        }
    }

    /**
     * Add shop item when the image is clicked...
     */
    public void addViewShop() {
        Log.d(TAG, "addViewShop: ");
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.shop_inflate_view, null);

        ll_audio_video_image_shop_container.setVisibility(View.VISIBLE);
        ll_audio_video_image_shop_container.removeAllViews();
        ll_audio_video_image_shop_container.addView(view);

        TextView txtName, txtItemCost;
        Button txtBuy;
        ImageView imgItem, imgSold, img_more, user_iv;
        FrameLayout user_image_container, frame_item_img;

        user_image_container = view.findViewById(R.id.user_image_container);
        frame_item_img = view.findViewById(R.id.frame_item_img);
        user_iv = view.findViewById(R.id.user_iv);
        img_more = view.findViewById(R.id.img_more);
        txtName = view.findViewById(R.id.txt_Text);
        txtBuy = view.findViewById(R.id.txt_buy);

        imgItem = view.findViewById(R.id.img_item);
        imgSold = view.findViewById(R.id.img_sold);
        txtItemCost = view.findViewById(R.id.txt_item_cost);
        if (iteamDetail.get(currentShopPosition) != null) {
            Itemdetail itemdetailbean = iteamDetail.get(currentShopPosition);
            Picasso.with(context)
                    .load(itemdetailbean.getUserphotothumb())
                    .placeholder(R.mipmap.user)
                    .error(R.mipmap.user)
                    .transform(new CircleTransform())
                    .into(user_iv);

            user_iv.setOnClickListener(viewProfile -> {
                if (isVideo) {
                    playInlinePlayer();
                }
                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", "" + itemdetailbean.getUserid());
                bundle.putString("uFullName", itemdetailbean.getFname() + " " + itemdetailbean.getLname());
                bundle.putString("username", itemdetailbean.getUsername());
                bundle.putString("uProfile", itemdetailbean.getUserphotothumb());
                intent.putExtras(bundle);
                context.startActivity(intent);
            });

            //  Log.i("preference","............"+itemdetailbean.getUserid());
            if (itemdetailbean.getMyitem() == 1) {
                txtBuy.setText(context.getString(R.string.txt_boost));
            } else {

                if (itemdetailbean.getIsincart() == 0) {
                    txtBuy.setText(context.getString(R.string.txt_cap_add_to_cart));
                } else {
                    txtBuy.setText(context.getString(R.string.txt_cap_pay));
                }
            }

            if (itemdetailbean.getMyitem() == 1) {
                img_more.setVisibility(View.INVISIBLE);
            }
            Log.e("Positon shop->" + currentShopPosition, "->" + itemdetailbean.getItemQtyRemained());
            if (itemdetailbean.getItemQtyRemained() > 0) {
                imgSold.setVisibility(View.GONE);
            } else {
                imgSold.setVisibility(View.VISIBLE);
                txtBuy.setVisibility(View.INVISIBLE);
                img_more.setVisibility(View.INVISIBLE);
            }

            if (itemdetailbean.getIswatched().equalsIgnoreCase("0")) {
                Picasso.with(context).load(R.mipmap.bookmark).placeholder(R.mipmap.bookmark).error(R.mipmap.bookmark).into(img_more);
            } else {
                Picasso.with(context).load(R.mipmap.bookmark_active).placeholder(R.mipmap.bookmark_active).error(R.mipmap.bookmark_active).into(img_more);
            }

            if (itemdetailbean.getMediaUrl() != null && !itemdetailbean.getMediaUrl().isEmpty()) {
                Glide.with(context)
                        .load(itemdetailbean.getMediaUrl())
                        .apply(new RequestOptions().centerCrop())
                        .placeholder(R.mipmap.img_placeholder).error(R.mipmap.img_placeholder)
                        .into(imgItem);


             /*   Picasso.with(context).load(itemdetailbean.getMediaUrl())
                        .placeholder(R.mipmap.img_placeholder).resize(600, 400)
                        .error(R.mipmap.img_placeholder)
                        .into(imgItem, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(context).load(itemdetailbean.getMediaUrl())
                                        .placeholder(R.mipmap.img_placeholder)
                                        .error(R.mipmap.img_placeholder)
                                        .into(imgItem);
                            }
                        });*/
                frame_item_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        photoDialog = ImageUtils.customChoosePhoto(context, itemdetailbean.getMediaUrl());
                    }
                });
            }
            //  txtName.setVisibility(View.VISIBLE);
            txtName.setText(itemdetailbean.getItemName());

            if (itemdetailbean.getItemPrice() == null || Utils.getFormattedPrice(itemdetailbean.getItemPrice()) == null) {
                txtItemCost.setVisibility(View.GONE);
            } else {
                txtItemCost.setText(Utils.getFormattedPrice(itemdetailbean.getItemPrice()));
            }

            img_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                        ((DrawerMainActivity) context).gotoLogin(context);
                    } else {
                        if (itemdetailbean.getIswatched().equalsIgnoreCase("0")) {
                            Picasso.with(context).load(R.mipmap.bookmark_active).placeholder(R.mipmap.bookmark_active).error(R.mipmap.bookmark_active).into(img_more);
                            itemdetailbean.setIswatched("1");
                            setBookmark(itemdetailbean.getItemId(), StaticConstant.UNLIKE);
                        } else {
                            Picasso.with(context).load(R.mipmap.bookmark).placeholder(R.mipmap.bookmark).error(R.mipmap.bookmark).into(img_more);
                            itemdetailbean.setIswatched("0");
                            setBookmark(itemdetailbean.getItemId(), StaticConstant.LIKE);
                        }
                    }
                }
            });

            txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                        ((DrawerMainActivity) context).gotoLogin(context);
                    } else {
                        if (isVideo) {
                            playInlinePlayer();
                        }
                        Intent i = new Intent(context, ItemDetailsAcitvity.class);
                        i.putExtra("itemid", "" + itemdetailbean.getItemId());
                        ((Activity) context).startActivityForResult(i, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
                        ll_audio_video_image_shop_container.removeAllViews();
                        ll_audio_video_image_shop_container.setVisibility(View.GONE);
                        currentShopPosition = -1;
                    }
                }
            });

            txtBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                        ((DrawerMainActivity) context).gotoLogin(context);
                    } else {
                        if (itemdetailbean.getMyitem() == 1) {
                            if (isVideo) {
                                playInlinePlayer();
                            }
                            Intent gotoActivity = new Intent(context, BoostActivity.class);
                            if (itemdetailbean.getMediaType().startsWith("image")) {
                                gotoActivity.putExtra("image", itemdetailbean.getMediaUrl());
                            } else {
                                gotoActivity.putExtra("image", (itemdetailbean.getMediaThumb()));
                            }
                            gotoActivity.putExtra("boost_type", String.valueOf(StaticConstant.BOOST_TYPE_SHOP_ITEM));
                            gotoActivity.putExtra("item_name", String.valueOf(itemdetailbean.getItemName()));
                            gotoActivity.putExtra("itemid", String.valueOf(itemdetailbean.getItemId()));
                            gotoActivity.putExtra("boost_url", String.valueOf(itemdetailbean.getBoost_url()));
                            ((Activity) context).startActivityForResult(gotoActivity, REQ_CODE_BOOST_ACTIVITY_RESULTS);
                        } else {
                            Log.d(TAG, "onClick: " + itemdetailbean);

                            int id = itemdetailbean.getItemId();


                            if (itemdetailbean.getIsincart() == 0) {
                                callMoveToCartApi(String.valueOf(itemdetailbean.getItemId()), "1");
                                txtBuy.setText("PAY");
                                iteamDetail.get(currentShopPosition).setIsincart(1);
                                // itemDetailResult.setItemQuntity("1");
                            } else {
                                if (isVideo) {
                                    playInlinePlayer();
                                }
                                Intent i = new Intent(context, CartCheckoutActivity.class);
                                i.putExtra("screen", NewCartFragment.class.getSimpleName());
                                ((DrawerMainActivity) context).startActivityForResult(i, 1234);
                               /* if (itemdetailbean.getIsincart() ==0) {
                                    callMoveToCartApi(String.valueOf(itemdetailbean.getItemId()), "1");
                                    txtBuy.setText("PAY");
                                    iteamDetail.get(currentShopPosition).setIsincart(1);
                                    //  itemDetailResult.setItemQuntity("1");
                                } else {
                                    txtBuy.setText("ADD TO CART");
                                }*/
                            }
                            Log.i("ITEM_ID", String.valueOf(id));
                        }
                    }
                }
            });
        }
    }

    private void addViewHeader() {
        Log.d(TAG, "addViewHeader: ");
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.image_view_inflate, null);
        View userImageContainer = view.findViewById(R.id.user_image_container);
        ImageView userIv = view.findViewById(R.id.user_iv);
        ImageView emojiIv = view.findViewById(R.id.emoji_iv);

        ImageView imgPhoto = view.findViewById(R.id.img_preview);
        TextView txtUserName = view.findViewById(R.id.txt_user_name);
        TextView txtLikeCount = view.findViewById(R.id.txt_like);
        Button btnShare = view.findViewById(R.id.btnShare);
        ImageView imgComment = view.findViewById(R.id.img_comment);

        ll_audio_video_image_media_container.setVisibility(View.VISIBLE);
        ll_audio_video_image_media_container.removeAllViews();
        ll_audio_video_image_media_container.addView(view);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int width = displaymetrics.widthPixels;
//        int height = (int) context.getResources().getDimension(R.dimen._180sdp);
        if (mediaposts.get(currentMediaPosition).getMediaUrl() != null
                && !mediaposts.get(currentMediaPosition).getMediaUrl().isEmpty()) {

            Glide.with(context)
                    .load(mediaposts.get(currentMediaPosition).getMediaUrl())
                    .apply(new RequestOptions().centerCrop())
                    .placeholder(R.mipmap.img_placeholder).error(R.mipmap.img_placeholder)
                    .into(imgPhoto);


        /*    Picasso.with(context).load(mediaposts.get(currentMediaPosition).getMediaUrl())
                    .placeholder(R.mipmap.img_placeholder)
                    .error(R.mipmap.img_placeholder)
                    .resize(600, 400)
                    .into(imgPhoto);*/
            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoDialog = ImageUtils.customChoosePhoto(context, mediaposts.get(currentMediaPosition).getMediaUrl());
                }
            });
        }

        commonMethod.commonMethod("0");

        try {
            if (mediaposts.get(currentMediaPosition).getUserid() == Integer.parseInt(preferences.getUserId())) {
                btnShare.setText(context.getString(R.string.txt_boost));
            } else {
                btnShare.setText(context.getString(R.string.txt_share));
            }
        } catch (NumberFormatException e) {
            btnShare.setText(context.getString(R.string.txt_share));
        }

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                    ((DrawerMainActivity) context).gotoLogin(context);
                    return;
                }

                if (mediaposts.get(currentMediaPosition).getUserid() == Integer.parseInt(preferences.getUserId())) {
                    if (isVideo) {
                        playInlinePlayer();
                    }
                    Intent gotoActivity = new Intent(context, BoostActivity.class);
                    if (mediaposts.get(currentMediaPosition).getMediaType().startsWith("image")) {
                        gotoActivity.putExtra("image", mediaposts.get(currentMediaPosition).getMediaUrl());
                    } else {
                        gotoActivity.putExtra("image", mediaposts.get(currentMediaPosition).getMediaThumb());
                    }
                    gotoActivity.putExtra("boost_type", String.valueOf(StaticConstant.BOOST_TYPE_MEDIA_ITEM));
                    gotoActivity.putExtra("itemid", String.valueOf(mediaposts.get(currentMediaPosition).getId()));
                    gotoActivity.putExtra("boost_url", String.valueOf(mediaposts.get(currentMediaPosition).getBoost_url()));
                    ((Activity) context).startActivityForResult(gotoActivity, REQ_CODE_BOOST_ACTIVITY_RESULTS);
                } else {
                    if (isVideo) {
                        playInlinePlayer();
                    }
                    StaticConstant.from_ = "media";
                    StaticConstant.from_url = mediaposts.get(currentMediaPosition).getMediaUrl();
                    Intent intent = new Intent(context, FriendsActivity.class);
                    intent.putExtra("imageUrl", mediaposts.get(currentMediaPosition).getMediaThumb());
                    ((Activity) context).startActivityForResult(intent, REQ_CODE_FRIENDS_ACTIVITY_RESULTS);
                }
            }
        });

        if (mediaposts.get(currentMediaPosition).getIslike() == 0) {
            emojiIv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
        } else {
            emojiIv.setImageResource(R.drawable.ico_reaction_new);
        }

        Picasso.with(context)
                .load(mediaposts.get(currentMediaPosition).getUserphotothumb())
                .error(R.mipmap.pic_holder_dashboard)
                .placeholder(R.mipmap.pic_holder_dashboard)
                .fit()
                .centerCrop()
                .transform(new CircleTransform())
                .into(userIv);


        userIv.setOnClickListener(viewProfile -> {
            if (isVideo) {
                playInlinePlayer();
            }
            Intent intent = new Intent(context, ProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("uid", "" + mediaposts.get(currentMediaPosition).getUserid());
            bundle.putString("uFullName", mediaposts.get(currentMediaPosition).getFname() + " "
                    + mediaposts.get(currentMediaPosition).getLname());
            bundle.putString("uProfile", mediaposts.get(currentMediaPosition).getUserphotothumb());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        txtUserName.setText((mediaposts.get(currentMediaPosition).getPostTitle().replaceAll("[-+.^:,@]", "")).replace("\"", ""));
        txtLikeCount.setText("" + mediaposts.get(currentMediaPosition).getLikecount());

        if (mediaposts.get(currentMediaPosition).getCommentcount() > 0)
            imgComment.setImageResource(R.drawable.ico_comment_new);
        else
            imgComment.setImageResource(R.drawable.ico_comment_new_grey);


        txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                    ((DrawerMainActivity) context).gotoLogin(context);
                } else {
                    if (isVideo) {
                        playInlinePlayer();
                    }
                    Intent intent = new Intent(context, MediaItemDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("itemId", "" + mediaposts.get(currentMediaPosition).getId());
                    bundle.putString("username", mediaposts.get(currentMediaPosition).getUsername());
                    intent.putExtras(bundle);
                    ((Activity) context).startActivityForResult(intent, REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS);
                }
            }
        });

        imgComment.setOnClickListener(viewComment -> {
            if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                ((DrawerMainActivity) context).gotoLogin(context);
            } else {
                if (isVideo) {
                    playInlinePlayer();
                }
                Intent intent = new Intent(context, MediaItemDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("itemId", "" + mediaposts.get(currentMediaPosition).getId());
                bundle.putString("username", mediaposts.get(currentMediaPosition).getUsername());
                intent.putExtras(bundle);
                ((Activity) context).startActivityForResult(intent, REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS);
            }
        });

        userImageContainer.setOnClickListener(v -> {
            if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                ((DrawerMainActivity) context).gotoLogin(context);
            } else {
                try {
                    if (mediaposts.get(currentMediaPosition).getIslike() == 0) {
                        emojiIv.setImageResource(R.drawable.ico_reaction_new);
                        txtLikeCount.setText("" + (mediaposts.get(currentMediaPosition).getLikecount() + 1));
                        mediaposts.get(currentMediaPosition).setLikecount(mediaposts.get(currentMediaPosition).getLikecount() + 1);
                        mediaposts.get(currentMediaPosition).setIslike(1);
                        setLikeApi(mediaposts.get(currentMediaPosition).getId(), StaticConstant.LIKE);
                    } else {
//                                txtLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_emoji_smile_white_bg, 0, 0, 0);
                        emojiIv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
                        txtLikeCount.setText("" + (mediaposts.get(currentMediaPosition).getLikecount() - 1));
                        mediaposts.get(currentMediaPosition).setLikecount(mediaposts.get(currentMediaPosition).getLikecount() - 1);
                        mediaposts.get(currentMediaPosition).setIslike(0);
                        setLikeApi(mediaposts.get(currentMediaPosition).getId(), StaticConstant.UNLIKE);
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setAudioPlayer(final View view) {
        Log.d(TAG, "setAudioPlayer: ");

        imgPlay = view.findViewById(R.id.app_video_play);
        ImageView imgBackPlayer = view.findViewById(R.id.app_video_finish);
        ImageView audioViewBackground = view.findViewById(R.id.img_audio_background);
        LinearLayout lnyAudioTitle = view.findViewById(R.id.lny_audio_title);
        LinearLayout lnyPlayerSeekbar = view.findViewById(R.id.lny_player_seek_bar);

        seekBarAudioProgress = view.findViewById(R.id.app_audio_seekBar);
        txtDuration = view.findViewById(R.id.app_audio_currentTime);
        txtEndDuration = view.findViewById(R.id.app_audio_endTime);

        if ((mediaposts.get(currentMediaPosition).getMediaThumb().contains("voice"))
                || (mediaposts.get(currentMediaPosition).getMediaThumb().contains("audio"))) {

            if (mediaposts.get(currentMediaPosition).getMediaThumb() != null) {
                Picasso.with(context)
                        .load(mediaposts.get(currentMediaPosition).getMediaThumb())
                        .placeholder(R.drawable.ico_app)
                        .error(R.drawable.ico_app)
                        .into(audioViewBackground);
            } else {
                Picasso.with(context)
                        .load(R.drawable.ico_app)
                        .placeholder(R.drawable.ico_app)
                        .error(R.drawable.ico_app)
                        .into(audioViewBackground);
            }
        } else {
            Picasso.with(context)
                    .load(mediaposts.get(currentMediaPosition).getMediaThumb())
                    .placeholder(R.drawable.ico_app)
                    .error(R.drawable.ico_app)
                    .into(audioViewBackground);
        }
        imgPlay.setEnabled(false);
        mMediaPlayer = new MediaPlayer();

        try {
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int result = am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

            if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == result) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    updateSeekBarTimerTask();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    mMediaPlayer.setDataSource(mediaposts.get(currentMediaPosition).getMediaUrl());
                    mMediaPlayer.prepareAsync();
                    txtDuration.setText("Loading...");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mMediaPlayer.setOnPreparedListener(mp -> {
                    Log.d(TAG, "onPrepared: ");
                    mMediaPlayer.start();

                    long totalDuration = mMediaPlayer.getDuration();
                    long currentDuration = mMediaPlayer.getCurrentPosition();

                    txtEndDuration.setText(Utils.milliSecondsToTimer(totalDuration));
                    txtDuration.setText(Utils.milliSecondsToTimer(currentDuration));

                    imgPlay.setEnabled(true);
                    imgPlay.setImageResource(R.drawable.ic_stop_white_24dp);

                    seekBarAudioProgress.setMax(mp.getDuration());
                });

                audioViewBackground.setOnClickListener(view1 -> {
                    Log.d(TAG, "audioViewBackground onClick: ");
                    if (lnyAudioTitle.getVisibility() == View.VISIBLE) {
                        lnyAudioTitle.setVisibility(View.INVISIBLE);
                        lnyPlayerSeekbar.setVisibility(View.INVISIBLE);
                    } else {
                        lnyAudioTitle.setVisibility(View.VISIBLE);
                        lnyPlayerSeekbar.setVisibility(View.VISIBLE);
                    }
                });

                imgBackPlayer.setOnClickListener(v -> {
                    if (!isPortrait) {
                        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    LinearLayout view1 = (LinearLayout) v.getParent().getParent().getParent();
                    view1.removeAllViews();
                    view1.setVisibility(View.GONE);
                    releaseMediaPlayer();
                });

                imgPlay.setOnClickListener(v ->
                {
                    Log.d(TAG, "setOnClickListener: ");
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        sMediaPlayerLastPosition = mMediaPlayer.getCurrentPosition();
                        imgPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);

                    } else {
                        mMediaPlayer.start();
                        mMediaPlayer.seekTo(sMediaPlayerLastPosition);
                        imgPlay.setImageResource(R.drawable.ic_stop_white_24dp);
                    }
                });

                seekBarAudioProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser && mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                            mMediaPlayer.seekTo(progress);
                        }
                    }
                });

                mMediaPlayer.setOnCompletionListener(mp ->
                {
                    if (sharedClick) {
                        sharedClick = false;
                    } else {
                        Log.d(TAG, "onCompletion: ");
                        sMediaPlayerLastPosition = 0;
                        seekBarAudioProgress.setProgress(0);
                        imgPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);

                        long currentDuration = 0; // in case of the completion...
                        long totalDuration = mMediaPlayer.getDuration();

                        txtDuration.setText(Utils.milliSecondsToTimer(currentDuration));
                        txtEndDuration.setText(Utils.milliSecondsToTimer(totalDuration));

                        for (int i = currentMediaPosition + 1; i < mediaposts.size(); i++) {
                            if (mediaposts.get(currentMediaPosition).getPosttype() == mediaposts.get(i).getPosttype()) {
                                Audio(null, null, null, null, i);
                                break;
                            }
                        }
                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateSeekBarTimerTask() {
        boolean result = mHandler.postDelayed(mUpdateTimeTask, 100);
        Log.d(TAG, "updateSeekBarTimerTask: " + result);
    }

    public void notifydata(DashBoardResult dashBoardResult) {
        Log.d(TAG, "notifydata: ");
        mDashBoardResult = dashBoardResult;

        iteamDetail.clear();
        iteamDetail.addAll(mDashBoardResult.getItemdetails());

        mediaposts.clear();
        mediaposts.addAll(mDashBoardResult.getMediapost());

        try {
            imagevideoAdapterAllmedia.notifyDataSetChanged();
            imagevideoAdapterImageShop.notifyDataSetChanged();
        } catch (NullPointerException e) {
            Utils.showToast(context, "Null Pointer Exception in " + DashboardAdapter.class.getSimpleName());
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        this.newConfig = newConfig;
        isPortrait = newConfig.orientation == ORIENTATION_PORTRAIT;


        Log.d(TAG, "onConfigurationChanged: " + newConfig.orientation);

        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        if (screamxoPlayer != null) {
            screamxoPlayer.onConfigurationChanged(newConfig);
        }

        if (isPortrait) {
            rellandscape.setVisibility(View.GONE);
            ((CustomLinearLayoutManager) dashboardMainRv.getLayoutManager()).setScrollEnabled(true);
            sv_container_media.setScrollingEnabled(true);

            if (!((DrawerMainActivity) context).IsPipMode) {
                ((DrawerMainActivity) context).floatingButton.setVisibility(View.VISIBLE);
            }


            LinearLayout.LayoutParams layoutParams;
            if (currentMediaPosition >= 0) {
                if ((mediaposts.get(currentMediaPosition).getMediaType().contains("video/mp4"))) {
                    layoutParams = new LinearLayout.LayoutParams(widthPixels, Utils.dip2pixel(context, 240));
                    layoutParams.setMargins(Utils.dip2pixel(context, 18), 0, 0, 0);
                    ll_audio_video_image_media_container.setLayoutParams(layoutParams);
                } else if ((mediaposts.get(currentMediaPosition).getMediaType().contains("image"))) {
                    layoutParams = new LinearLayout.LayoutParams(widthPixels, Utils.dip2pixel(context, 310));
                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(widthPixels, Utils.dip2pixel(context, 240));
                    ll_audio_video_image_media_container.findViewById(R.id.img_preview).setLayoutParams(layoutParams1);
                    layoutParams.setMargins(Utils.dip2pixel(context, 18), 0, 0, 0);
                    ll_audio_video_image_media_container.setLayoutParams(layoutParams);

                }
                ll_audio_video_image_media_container.setPadding(Utils.dip2pixel(context, 10), 0, Utils.dip2pixel(context, 40), 0);
                ll_audio_video_image_media_container.setBackgroundColor(Color.WHITE);
                ll_audio_video_image_media_container.setGravity(Gravity.NO_GRAVITY);

                try {
                    if (mediaposts != null || mediaposts.size() != 0) {
                        if ((mediaposts.get(currentMediaPosition).getMediaThumb().contains("voice"))
                                || (mediaposts.get(currentMediaPosition).getMediaThumb().contains("audio"))) {
                            ll_audio_video_image_media_container.findViewById(R.id.ll_footer).setPadding(50, 0, 50, 0);
                            ((ImageView) ll_audio_video_image_media_container.findViewById(R.id.img_audio_background)).setScaleType(ImageView.ScaleType.FIT_CENTER);

                            ll_audio_video_image_media_container.findViewById(R.id.audio_bottom_view).setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(widthPixels, Utils.dip2pixel(context, 310));
                            layoutParams1.setMargins(Utils.dip2pixel(context, 18), 0, 0, 0);
                            ll_audio_video_image_media_container.setLayoutParams(layoutParams1);
                            ll_audio_video_image_media_container.setPadding(Utils.dip2pixel(context, 10), 0, Utils.dip2pixel(context, 40), 0);
                            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    Utils.dip2pixel(context, 240));
                            ll_audio_video_image_media_container.findViewById(R.id.relative_audio).setLayoutParams(layoutParams2);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            rellandscape.setVisibility(View.GONE);

            if ((screamxoPlayer != null && screamxoPlayer.isPlaying()) || (mMediaPlayer != null && mMediaPlayer.isPlaying())) {
                dashboardMainRv.getLayoutManager().scrollToPosition(0);

                sv_container_media.setScrollingEnabled(false);
                ((CustomLinearLayoutManager) dashboardMainRv.getLayoutManager()).setScrollEnabled(false);
                ((DrawerMainActivity) context).floatingButton.closeMenu();
                ((DrawerMainActivity) context).floatingButton.setVisibility(View.GONE);
            }

            if (dashboardFragment != null) {
                dashboardFragment.hideSearchToolbar();
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPixels, heightPixels);
            layoutParams.setMargins(0, 0, 0, 0);

            try {
                if (mediaposts != null || mediaposts.size() != 0) {
                    if ((mediaposts.get(currentMediaPosition).getMediaThumb().contains("voice"))
                            || (mediaposts.get(currentMediaPosition).getMediaThumb().contains("audio"))) {
                        ll_audio_video_image_media_container.findViewById(R.id.relative_audio).setLayoutParams(layoutParams);
                        if (!videoClicked)
                            ll_audio_video_image_media_container.findViewById(R.id.ll_footer).setPadding(50, 0, 50, 50);

                        ll_audio_video_image_media_container.findViewById(R.id.img_audio_background).setPadding(50, 0, 50, 0);
                        ll_audio_video_image_media_container.findViewById(R.id.audio_bottom_view).setVisibility(View.GONE);
                        ((ImageView) ll_audio_video_image_media_container.findViewById(R.id.img_audio_background)).setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (currentMediaPosition != -1) {
                if (mediaposts.get(currentMediaPosition).getMediaType().contains("image")) {
                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(widthPixels, Utils.dip2pixel(context, 280));
                    layoutParams1.setMargins(0, 0, 0, 0);
                    ll_audio_video_image_media_container.findViewById(R.id.img_preview).setLayoutParams(layoutParams1);
                    layoutParams.setMargins(Utils.dip2pixel(context, 18), 0, 0, 0);
                    ll_audio_video_image_media_container.setLayoutParams(layoutParams);
                } else {
                    ll_audio_video_image_media_container.setLayoutParams(layoutParams);
                    ll_audio_video_image_media_container.setPadding(0, 0, 0, 0);
                }
            }
//            ll_audio_video_image_media_container.setBackgroundColor(Color.BLACK);
        }
        notifyDataSetChanged();

        if (photoDialog != null) {
            photoDialog.dismiss();
        }
    }

    private void setPortrait() {
        rellandscape.setVisibility(View.GONE);
        ((CustomLinearLayoutManager) dashboardMainRv.getLayoutManager()).setScrollEnabled(true);
        sv_container_media.setScrollingEnabled(true);
        //  ((DrawerMainActivity) context).floatingButton.setVisibility(View.VISIBLE);


        if (!((DrawerMainActivity) context).IsPipMode) {
            ((DrawerMainActivity) context).floatingButton.setVisibility(View.VISIBLE);
        }

        LinearLayout.LayoutParams layoutParams;
        if (currentMediaPosition > 0) {
            if ((mediaposts.get(currentMediaPosition).getMediaType().contains("video/mp4"))) {
                layoutParams = new LinearLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels, Utils.dip2pixel(context, 240));
                layoutParams.setMargins(Utils.dip2pixel(context, 18), 0, 0, 0);
                ll_audio_video_image_media_container.setLayoutParams(layoutParams);

            } else if ((mediaposts.get(currentMediaPosition).getMediaType().contains("image"))) {
                layoutParams = new LinearLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels, Utils.dip2pixel(context, 310));
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels, Utils.dip2pixel(context, 240));
                ll_audio_video_image_media_container.findViewById(R.id.img_preview).setLayoutParams(layoutParams1);
                layoutParams.setMargins(Utils.dip2pixel(context, 18), 0, 0, 0);
                ll_audio_video_image_media_container.setLayoutParams(layoutParams);
            }

            ll_audio_video_image_media_container.setPadding(Utils.dip2pixel(context, 10), 0, Utils.dip2pixel(context, 40), 0);
            ll_audio_video_image_media_container.setBackgroundColor(Color.WHITE);
            ll_audio_video_image_media_container.setGravity(Gravity.NO_GRAVITY);

            try {
                if (mediaposts != null || mediaposts.size() != 0) {
                    if ((mediaposts.get(currentMediaPosition).getMediaThumb().contains("voice"))
                            || (mediaposts.get(currentMediaPosition).getMediaThumb().contains("audio"))) {
                        ll_audio_video_image_media_container.findViewById(R.id.ll_footer).setPadding(50, 0, 50, 0);
                        ((ImageView) ll_audio_video_image_media_container.findViewById(R.id.img_audio_background)).setScaleType(ImageView.ScaleType.FIT_CENTER);

                        ll_audio_video_image_media_container.findViewById(R.id.audio_bottom_view).setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels, Utils.dip2pixel(context, 310));
                        layoutParams1.setMargins(Utils.dip2pixel(context, 18), 0, 0, 0);
                        ll_audio_video_image_media_container.setLayoutParams(layoutParams1);
                        ll_audio_video_image_media_container.setPadding(Utils.dip2pixel(context, 10), 0, Utils.dip2pixel(context, 40), 0);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                Utils.dip2pixel(context, 240));
                        ll_audio_video_image_media_container.findViewById(R.id.relative_audio).setLayoutParams(layoutParams2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackArrowClicked() {
        Log.d(TAG, "onBackArrowClicked: ");
        clearVideoPreviewView();
    }

    @Override
    public void onShareButtonClicked() {
        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
            ((DrawerMainActivity) context).gotoLogin(context);
        } else {
            if (isVideo) {
                playInlinePlayer();
            }
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, (mediaposts.get(currentMediaPosition).getMediaUrl()));
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent, "Send to"));
        }
    }

    @Override
    public void onFullScreenBtnClicked() {

    }

    public void lockUnlockScreen() {
        isLocked = !isLocked;
        releaseMediaPlayer();
        stopVideoPlayer();
        notifyDataSetChanged();
    }

    public void onResume() {
        if (isVideo) {
            playInlinePlayer();
        }

        try {
            if (screamxoPlayer != null) {
                screamxoPlayer.onResume();
            }

            if (mMediaPlayer != null && mMediaPlayer.getCurrentPosition() != 0) {
                mMediaPlayer.start();
                imgPlay.setImageResource(R.drawable.ic_stop_white_24dp);
                mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        if (isVideo) {
            playInlinePlayer();
        }

        try {
            if (mMediaPlayer != null) {
                //  mMediaPlayer.pause();
                imgPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                sMediaPlayerLastPosition = mMediaPlayer.getCurrentPosition();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        try {

            if (screamxoPlayer != null) {
                screamxoPlayer.onDestroy();
                mMediaPlayer.pause();
            }
            releaseMediaPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callMoveToCartApi(String id, String qty) {
        Map<String, String> map = new HashMap<>();
        Preferences preference = new Preferences(context);
        map.put("user_id", preference.getUserId());
        map.put("item_id", id);
        map.put("cart_qty", qty);
//        map.put("toid", BundleUtils.getIntentExtra(getIntent(), "uid", ""));

        Call<CartBean> cartBeanCall = mService.getFetcherService(context).addToCart(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {

                }

                @Override
                public void onFailure(Call<CartBean> call, Throwable t) {
                    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void ItemDetailApi(int itemid) {
        Map<String, String> map = new HashMap<>();
        map.put("itemid", String.valueOf(itemid));
        map.put("myid", preferences.getUserId());
        if (Utils.isInternetOn(context)) {
            // progreessbar.setVisibility(View.VISIBLE);
            streampostcall = mservice.getFetcherService(context).GetItemDetail(map);
            streampostcall.enqueue(new Callback<ItemDetailBean>() {
                @Override
                public void onResponse(Call<ItemDetailBean> call, Response<ItemDetailBean> response) {
                    //  progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
//                            if (isFinishing()) {
//                                return;
//                            }
                            mediaArrayList.clear();
                            mediaArrayList.addAll(response.body().getResult().getMedia());
                            itemDetailResult = response.body().getResult();
                            ((MyApplication) context.getApplicationContext()).addCartItem(itemDetailResult);
                            Log.i("CART", String.valueOf(((MyApplication) context.getApplicationContext()).cartGlobalArray.size()));

                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemDetailBean> call, Throwable t) {
                    // progreessbar.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: ", t);
                    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
            // progreessbar.setVisibility(View.GONE);
        }
    }

    /*Media items...*/
    private class Header extends RecyclerView.ViewHolder {
        RecyclerView mrecyclerView;
        RelativeLayout mRelativeLayoutTag, relative_header;
        LinearLayout includeNoData;
        ImageView imgMore;
        TextView txt_tag;
        View greyline;

        Header(View v) {
            super(v);
            Log.d(TAG, "Header: ");
            imgMore = v.findViewById(R.id.img_more);
            sv_container_media = v.findViewById(R.id.sv_container);
            relative_header = v.findViewById(R.id.relative_header);
            mrecyclerView = v.findViewById(R.id.recycler_view);
            txt_tag = v.findViewById(R.id.txt_tag);
            mediaposts.addAll(mDashBoardResult.getMediapost());
            mRelativeLayoutTag = v.findViewById(R.id.top_tag_layout);
            ll_audio_video_image_media_container = v.findViewById(R.id.ll_audio_video_image_container);
            ll_media_preview_bottom_bar = v.findViewById(R.id.lny_vedio_like_bottom);
            includeNoData = v.findViewById(R.id.include_no_data);
            greyline = v.findViewById(R.id.greyline);

            mrecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            imagevideoAdapterAllmedia = new ImageVideoAdapter(context, mediaposts, null, audioVideoInterface);
            mRelativeLayoutTag.setVisibility(View.VISIBLE);
            mrecyclerView.setAdapter(imagevideoAdapterAllmedia);
            recyclerView = mrecyclerView;
            txt_tag.setText("MEDIA");

            if (mDashBoardResult.getMediapostcount() == 0) {
                mrecyclerView.setVisibility(View.GONE);
                includeNoData.setVisibility(View.VISIBLE);
            } else {
                mrecyclerView.setVisibility(View.VISIBLE);
                includeNoData.setVisibility(View.GONE);
            }

            mrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() == (imagevideoAdapterAllmedia.getItemCount() - 1)) {
                        if (canLazyLoadMoreMedia && mediaposts.size() < mDashBoardResult.getMediapostcount()) {
                            if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                                ((DrawerMainActivity) context).gotoLogin(context);
                            } else {
                                canLazyLoadMoreMedia = false;
                                if (((DrawerMainActivity) context).fragment != null && ((DrawerMainActivity) context).fragment instanceof DashboardPagerFragment) {
                                    ((DashboardPagerFragment) ((DrawerMainActivity) context).fragment).viewPager.setPagingEnabled(false);
                                }
                                callMoreMedia(mediaType);
                            }
                        }
                    }
                }
            });

            imgMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.layout_dashboard_popupview, null);
                    PopupWindow window = new PopupWindow(layout, 250, 450, true);
                    window.setOutsideTouchable(true);
                    window.showAsDropDown(imgMore);

                    if (((Activity) context) instanceof DrawerMainActivity) {
                        ((DrawerMainActivity) context).setTranperentVisibility(View.VISIBLE);
                    }

                    window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            if (((Activity) context) instanceof DrawerMainActivity) {
                                ((DrawerMainActivity) context).setTranperentVisibility(View.GONE);
                            }
                        }
                    });

                    TextView txtClose, txtFilter, txtUpload;
                    txtClose = (TextView) layout.findViewById(R.id.txt_close);
                    txtFilter = (TextView) layout.findViewById(R.id.txt_filter);
                    txtUpload = (TextView) layout.findViewById(R.id.txt_upload);

                    if (mrecyclerView.getVisibility() == View.GONE || isLocked) {
                        txtClose.setText("Open");
                    } else {
                        txtClose.setText("Close");
                    }


                    txtClose.setOnClickListener(viewClose -> {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                        } else {
                            if (mrecyclerView.getVisibility() == View.VISIBLE) {
                                mrecyclerView.setVisibility(View.GONE);
                                ll_audio_video_image_shop_container.setVisibility(View.GONE);
                                if (mDashBoardResult.getItemcount() == 0) {
                                    View view1 = (View) mrecyclerView.getParent();
                                    LinearLayout linearLayout = view.findViewById(R.id.include_no_data);
                                    linearLayout.setVisibility(View.GONE);
                                }
                                txtClose.setText("Open");
                            } else {
                                mrecyclerView.setVisibility(View.VISIBLE);
                                ll_audio_video_image_shop_container.setVisibility(View.VISIBLE);
                                mrecyclerView.requestFocus();
                                Log.d(TAG, "onClick mrecyclerView.requestFocus(): " + mrecyclerView.requestFocus());
                                if (mDashBoardResult.getItemcount() == 0) {
                                    View view2 = (View) mrecyclerView.getParent();
                                    LinearLayout linearLayout = view.findViewById(R.id.include_no_data);
                                    linearLayout.setVisibility(View.VISIBLE);
                                }
                                txtClose.setText("Close");
                            }
                        }
                        window.dismiss();
                    });

                    txtFilter.setOnClickListener(viewFilter -> {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                        } else {
                            openPopUpmenu(view, imgMore, 0);
                        }
                        window.dismiss();
                    });

                    txtUpload.setOnClickListener(viewUpload -> {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                        } else {
                            if (isVideo) {
                                playInlinePlayer();
                            }
                            Intent gotoUpload = new Intent(context, UploadMediaAcitvity.class);
                            ((Activity) context).startActivityForResult(gotoUpload, CAPTURE_MEDIA);
                        }
                        window.dismiss();
                    });
                }
            });
        }
    }

    private class Shop extends RecyclerView.ViewHolder {

        RecyclerView mrecyclerView;
        RelativeLayout mRelativeLayoutTag, relativeBottom;
        TextView mtxtTagTop, mtxtTagBottom;
        Boolean canCollapseBottom = true;
        View bottomTagLayout;
        LinearLayout includeNoData, ll_icons, ll_iconsbottom;
        View greyline;
        ImageView imgMore, imgMoreBottom;

        Shop(View v) {
            super(v);
            iteamDetail.addAll(mDashBoardResult.getItemdetails());
            ll_audio_video_image_shop_container = v.findViewById(R.id.ll_audio_video_image_container);
            mrecyclerView = v.findViewById(R.id.recycler_view);
            mRelativeLayoutTag = v.findViewById(R.id.relative_header);
            mtxtTagTop = v.findViewById(R.id.txt_tag);
            imgMore = v.findViewById(R.id.img_more);

            bottomTagLayout = v.findViewById(R.id.bottom_tag_layout);
            mtxtTagBottom = bottomTagLayout.findViewById(R.id.txt_tag);
            imgMoreBottom = bottomTagLayout.findViewById(R.id.img_more);
            imgMoreBottom.setVisibility(View.VISIBLE);

            includeNoData = v.findViewById(R.id.include_no_data);
            greyline = v.findViewById(R.id.greyline);

            imagevideoAdapterImageShop = new ImageVideoAdapter(context, null, iteamDetail, audioVideoInterface);
            mrecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            mrecyclerView.setAdapter(imagevideoAdapterImageShop);

            mrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() == (imagevideoAdapterImageShop.getItemCount() - 1)) {
                        if (canLazyLoadMoreShopItems && iteamDetail.size() < mDashBoardResult.getItemcount()) {
                            if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                                ((DrawerMainActivity) context).gotoLogin(context);
                            } else {
                                canLazyLoadMoreShopItems = true;
                                if (((DrawerMainActivity) context).fragment != null && ((DrawerMainActivity) context).fragment instanceof DashboardPagerFragment) {
                                    ((DashboardPagerFragment) ((DrawerMainActivity) context).fragment).viewPager.setPagingEnabled(false);
                                }
//                                callMoreShopApi();
                            }
                        }
                    }
                }
            });

            mtxtTagTop.setText(context.getResources().getString(R.string.txt_tag_top_shop));
            mtxtTagBottom.setText(context.getResources().getString(R.string.txt_tag_bottom_stream));

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(38, 0, 0, 0);
            ll_audio_video_image_shop_container.setLayoutParams(lparams);
            bottomTagLayout.setVisibility(View.VISIBLE);
            if (mDashBoardResult.getItemcount() == 0) {
                mrecyclerView.setVisibility(View.GONE);
                includeNoData.setVisibility(View.VISIBLE);
            } else {
                mrecyclerView.setVisibility(View.VISIBLE);
                includeNoData.setVisibility(View.GONE);
            }

            imgMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.layout_dashboard_popupview, null);
                    PopupWindow window = new PopupWindow(layout, 250, 450, true);
                    window.setOutsideTouchable(true);
                    window.showAsDropDown(imgMore);

                    if (((Activity) context) instanceof DrawerMainActivity) {
                        ((DrawerMainActivity) context).setTranperentVisibility(view.VISIBLE);
                    }

                    window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            if (((Activity) context) instanceof DrawerMainActivity) {
                                ((DrawerMainActivity) context).setTranperentVisibility(view.GONE);
                            }
                        }
                    });

                    TextView txtClose, txtFilter, txtUpload;
                    txtClose = (TextView) layout.findViewById(R.id.txt_close);
                    txtFilter = (TextView) layout.findViewById(R.id.txt_filter);
                    txtUpload = (TextView) layout.findViewById(R.id.txt_upload);
                    if (mrecyclerView.getVisibility() == View.GONE || isLocked) {
                        txtClose.setText("Open");
                    } else {
                        txtClose.setText("Close");
                    }

                    txtClose.setOnClickListener(viewClose -> {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                        } else {
                            if (mrecyclerView.getVisibility() == View.VISIBLE) {
                                mrecyclerView.setVisibility(View.GONE);
                                ll_audio_video_image_shop_container.setVisibility(View.GONE);
                                if (mDashBoardResult.getItemcount() == 0) {
                                    View view1 = (View) mrecyclerView.getParent();
                                    LinearLayout linearLayout = view.findViewById(R.id.include_no_data);
                                    linearLayout.setVisibility(View.GONE);
                                }
                                txtClose.setText("Open");
                            } else {
                                mrecyclerView.setVisibility(View.VISIBLE);
                                ll_audio_video_image_shop_container.setVisibility(View.VISIBLE);
                                if (mDashBoardResult.getItemcount() == 0) {
                                    View view2 = (View) mrecyclerView.getParent();
                                    LinearLayout linearLayout = view.findViewById(R.id.include_no_data);
                                    linearLayout.setVisibility(View.VISIBLE);
                                }
                                txtClose.setText("Close");
                            }
                            window.dismiss();
                        }
                    });

                    txtFilter.setOnClickListener(viewFilter -> {
                        openPopUpmenu(view, imgMore, 1);
                        window.dismiss();
                    });

                    txtUpload.setOnClickListener(viewUpload -> {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                        } else {
                            if (isVideo) {
                                playInlinePlayer();
                            }
                            Intent intent = new Intent(context, com.screamxo.Activity.RahulWork.SellItemActivity.class);
                            ((Activity) context).startActivityForResult(intent, StaticConstant.REQUEST_SELL_ITEM);
                        }
                        window.dismiss();
                    });
                }
            });

            imgMoreBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.layout_dashboard_popupview, null);
                    PopupWindow window = new PopupWindow(layout, 250, 450, true);
                    window.setOutsideTouchable(true);
                    window.showAsDropDown(bottomTagLayout, 200, 0, Gravity.RIGHT);

                    if (((Activity) context) instanceof DrawerMainActivity) {
                        ((DrawerMainActivity) context).setTranperentVisibility(view.VISIBLE);
                    }

                    TextView txtClose, txtFilter, txtUpload;
                    txtClose = (TextView) layout.findViewById(R.id.txt_close);
                    txtFilter = (TextView) layout.findViewById(R.id.txt_filter);
                    txtUpload = (TextView) layout.findViewById(R.id.txt_upload);
                    if (mrecyclerView.getVisibility() == View.GONE || isLocked) {
                        txtClose.setText("Open");
                    } else {
                        txtClose.setText("Close");
                    }

                    window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            if (((Activity) context) instanceof DrawerMainActivity) {
                                ((DrawerMainActivity) context).setTranperentVisibility(view.GONE);
                            }
                        }
                    });

                    txtClose.setOnClickListener(viewClose -> {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                        } else {
                            Log.d(TAG, "onClick canCollapseBottom: " + canCollapseBottom);
                            if (canCollapseBottom) {
                                canCollapseBottom = false;
                                dashboardFragment.streamCall = false;
                                txtClose.setText("Open");
                                returnCount = 0;
                            } else if (mDashBoardResult.getStreampost().size() != 0) {
                                canCollapseBottom = true;
                                dashboardFragment.streamCall = true;
                                txtClose.setText("Close");
                                returnCount = mDashBoardResult.getStreampost().size();
                            } else {
                                canCollapseBottom = true;
                                dashboardFragment.streamCall = true;
                                txtClose.setText("Close");
                                returnCount = 1;
                            }
                            notifyDataSetChanged();
                            window.dismiss();
                        }
                    });

                    txtFilter.setOnClickListener(viewFilter -> {
                        openPopUpmenu(view, imgMoreBottom, 2);
                        window.dismiss();
                    });

                    txtUpload.setOnClickListener(viewUpload -> {
                        if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                            ((DrawerMainActivity) context).gotoLogin(context);
                        } else {
                            if (isVideo) {
                                playInlinePlayer();
                            }
                            Intent intent = new Intent(context, PostStreamActivity.class);
                            ((Activity) context).startActivityForResult(intent, REQ_CODE_POST_STREAM_ACTIVITY_RESULTS);
                        }
                        window.dismiss();
                    });
                }
            });
        }
    }

    private class NoData extends RecyclerView.ViewHolder {
        TextView mtxtNodata;

        NoData(View itemView) {
            super(itemView);
            Log.d(TAG, "NoData: ");
            itemView.setVisibility(View.VISIBLE);
            mtxtNodata = itemView.findViewById(R.id.txt_no_data);

            mtxtNodata.setVisibility(View.VISIBLE);
        }
    }

    private class StreamViewBottom extends RecyclerView.ViewHolder {
        ImageView user_iv;
        ImageView emoji_iv, imgComment;
        FrameLayout user_image_container;
        TextView mtxtUserName, mtxtComment, mtxtTime, mtxtLike, mtxtCommnetCount, mtxtMore;

        StreamViewBottom(View v) {
            super(v);
            Log.d(TAG, "StreamViewBottom: ");
//            mimgUserProfile = (ImageView) v.findViewById(R.id.img_user_profile);
            user_image_container = v.findViewById(R.id.user_image_container);
            user_iv = v.findViewById(R.id.ico_user_profile);
            emoji_iv = v.findViewById(R.id.emoji_iv);
            imgComment = v.findViewById(R.id.img_comment);
            mtxtUserName = v.findViewById(R.id.txt_user_name);
            mtxtComment = v.findViewById(R.id.txt_comment);
            mtxtTime = v.findViewById(R.id.txt_time);
            mtxtLike = v.findViewById(R.id.txt_like);
            mtxtCommnetCount = v.findViewById(R.id.txt_comment_count);
            mtxtMore = v.findViewById(R.id.txt_more);
        }
    }

    public void callShopDashboard() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uId);
        map.put("limit", LIMIT);
        map.put("offset", "" + 0);
//        if (!categoryId.equals("20"))
        map.put("categoryid", categoryId);

//        if (categoryName.equalsIgnoreCase("trending"))
        map.put("trending", "2");
//        if (StaticConstant.SEARCHSTRING.length() > 0) {
        map.put("searchstring", StaticConstant.SEARCHSTRING);
        map.put("userfiltertype", "2");
        map.put("posttype", "2");
        map.put("type", "3");
        map.put("mediatype", "");


        dashboardFragment.progreessbar.setVisibility(View.VISIBLE);
        ShopApiCallMore = mService.getFetcherService(context).getShopDashboardData(map);
        ShopApiCallMore.enqueue(new retrofit2.Callback<ShopDashboard>() {
            @Override
            public void onResponse(Call<ShopDashboard> call, Response<ShopDashboard> response) {
                dashboardFragment.progreessbar.setVisibility(View.GONE);
                if (((DrawerMainActivity) context).fragment != null && ((DrawerMainActivity) context).fragment instanceof DashboardPagerFragment) {
                    ((DashboardPagerFragment) ((DrawerMainActivity) context).fragment).viewPager.setPagingEnabled(true);
                }
                canLazyLoadMoreShopItems = true;
                if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
                    if (response.body().getMessage().equals("Fetching data success")) {

//                        if (pageCounterForShop == 1) {
                        iteamDetail.clear();
//                        }
                        for (int i = 0; i < response.body().getResult().getRecentitems().getItemdetails().size(); i++) {
                            Itemdetail itemdetail = new Itemdetail();

                            itemdetail.setUserid(response.body().getResult().getRecentitems().getItemdetails().get(i).getUserId());
                            itemdetail.setItemId(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemId());
                            itemdetail.setItemName(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemName());
                            itemdetail.setItemPrice(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemPrice());
                            itemdetail.setMyitem(response.body().getResult().getRecentitems().getItemdetails().get(i).getMyitem());
                            itemdetail.setItemQty(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemQty());
                            itemdetail.setItemQtyRemained(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemQtyRemained());
                            itemdetail.setIspurchased(response.body().getResult().getRecentitems().getItemdetails().get(i).getIspurchased());
                            itemdetail.setMediaType(response.body().getResult().getRecentitems().getItemdetails().get(i).getMediaType());
                            itemdetail.setMediaUrl(response.body().getResult().getRecentitems().getItemdetails().get(i).getMediaUrl());
                            itemdetail.setMediaThumb(response.body().getResult().getRecentitems().getItemdetails().get(i).getMediaThumb());
                            itemdetail.setUsername(response.body().getResult().getRecentitems().getItemdetails().get(i).getUsername());
                            itemdetail.setFname(response.body().getResult().getRecentitems().getItemdetails().get(i).getFname());
                            itemdetail.setLname(response.body().getResult().getRecentitems().getItemdetails().get(i).getLname());
                            itemdetail.setItemActualprice(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemActualprice());
                            itemdetail.setItemShippingCost(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemShippingCost());
                            itemdetail.setItemCreatedBy(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemCreatedBy());
                            itemdetail.setItemCreatedBy(response.body().getResult().getRecentitems().getItemdetails().get(i).getItemCreatedBy());
                            itemdetail.setIspaypal(response.body().getResult().getRecentitems().getItemdetails().get(i).getIspaypal());
                            itemdetail.setIsbitcoin(response.body().getResult().getRecentitems().getItemdetails().get(i).getBitcoinacc());
                            itemdetail.setBitcoinmail(response.body().getResult().getRecentitems().getItemdetails().get(i).getBitcoinmail());
                            iteamDetail.add(itemdetail);
                        }


                        pageCounterForShop++;
                        imagevideoAdapterImageShop.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShopDashboard> call, Throwable t) {
                dashboardFragment.progreessbar.setVisibility(View.GONE);

                if (((DrawerMainActivity) context).fragment != null && ((DrawerMainActivity) context).fragment instanceof DashboardPagerFragment) {
                    ((DashboardPagerFragment) ((DrawerMainActivity) context).fragment).viewPager.setPagingEnabled(true);
                }
                canLazyLoadMoreShopItems = true;
            }
        });
    }

    public void f() {

        ll_audio_video_image_shop_container.removeAllViews();
        ll_audio_video_image_shop_container.setVisibility(View.GONE);
        currentShopPosition = -1;


        Map<String, String> map = new HashMap<>();
        map.put("userfiltertype", "2");

        map.put("firing_mode", "1");
        map.put("myid", uId);
        map.put("uid", uId);

        map.put("limit", LIMIT);
        map.put("offset", "" + 0);


        if (Utils.isInternetOn(context)) {
            mService.getFetcherService(context).getDashboardEventsData(map).
                    enqueue(new Callback<DashBoardBean>() {
                        @Override
                        public void onResponse(Call<DashBoardBean> call, Response<DashBoardBean> response) {
                            Log.d("Responce", "Success");
                            if (response.code() == RESULT_OK) {
                                if (response.body() != null) {
                                    imagevideoAdapterImageShop = null;
                                    iteamDetail.clear();
                                    iteamDetail.addAll(response.body().getResult().getItemdetails());
                                    imagevideoAdapterImageShop = new ImageVideoAdapter(context, null, iteamDetail, audioVideoInterface);
                                }
                            } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                                Utils.unAuthentication(context);
                            }
                        }

                        @Override
                        public void onFailure(Call<DashBoardBean> call, Throwable t) {
                            Log.d("Responce", "false");

                            t.printStackTrace();
                        }
                    });
        }
    }

    public void filterShop(String categoryId) {

        ll_audio_video_image_shop_container.removeAllViews();
        ll_audio_video_image_shop_container.setVisibility(View.GONE);
        currentShopPosition = -1;

        Map<String, String> map = new HashMap<>();
        map.put("userfiltertype", "2");
        map.put("firing_mode", "1");
        map.put("devicetype", "android");
        map.put("categoryid", categoryId);
        map.put("myid", uId);
        map.put("uid", uId);
        map.put("trending", "2");
        map.put("limit", LIMIT);
        map.put("offset", "" + 0);

        if (Utils.isInternetOn(context)) {
            mService.getFetcherService(context).getDashboardEventsData(map).
                    enqueue(new Callback<DashBoardBean>() {
                        @Override
                        public void onResponse(Call<DashBoardBean> call, Response<DashBoardBean> response) {
                            Log.d("Responce", "Success");
                            if (response.code() == RESULT_OK) {

                                canLazyLoadMoreShopItems = true;
                                if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
//                            if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                                    iteamDetail.clear();
                                    iteamDetail.addAll(response.body().getResult().getItemdetails());
                                    pageCounterForShop++;
                                    imagevideoAdapterImageShop.notifyDataSetChanged();
//                            }
                                }
//                                if (response.body() != null) {
//
//                                    if(response.body().getResult().getItemdetails().size()>0) {
//                                        imagevideoAdapterImageShop = null;
//                                        iteamDetail.clear();
//                                        iteamDetail.addAll(response.body().getResult().getItemdetails());
//                                        imagevideoAdapterImageShop = new ImageVideoAdapter(context, null, iteamDetail, audioVideoInterface);
//                                    }
//                                    else
//                                    {
//                                        iteamDetail.clear();
//                                        imagevideoAdapterImageShop.notifyDataSetChanged();
//                                    }
//                                }
                            } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                                Utils.unAuthentication(context);
                            }
                        }

                        @Override
                        public void onFailure(Call<DashBoardBean> call, Throwable t) {
                            Log.d("Responce", "false");
                            t.printStackTrace();
                        }
                    });
        }
    }

    public void setPipControls() {
        Log.d(TAG, "setOnClickListener:+++3116++");
//        if (imgPlay != null) {
//            imgPlay.setVisibility(View.INVISIBLE);
//        }
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                sMediaPlayerLastPosition = mMediaPlayer.getCurrentPosition();
//                ((DrawerMainActivity) context).updatePictureInPictureActions(R.drawable.ic_play_arrow_white_24dp, "Pause", CONTROL_TYPE_PAUSE, REQUEST_PAUSE);
                ((DrawerMainActivity) context).setPictureInPictureActions(R.drawable.ic_play_arrow_white_24dp, R.drawable.next, "Pause", CONTROL_TYPE_PAUSE, REQUEST_PAUSE);
            } else {
//                ((DrawerMainActivity) context).updatePictureInPictureActions(R.drawable.ic_stop_white_24dp, "Play", CONTROL_TYPE_PLAY, REQUEST_PLAY);
                ((DrawerMainActivity) context).setPictureInPictureActions(R.drawable.ic_stop_white_24dp, R.drawable.next, "Play", CONTROL_TYPE_PLAY, REQUEST_PLAY);

                mMediaPlayer.start();
                mMediaPlayer.seekTo(sMediaPlayerLastPosition);
//            playInlinePlayer();
//            screamxoPlayer.play(mediaposts.get(currentMediaPosition).getMediaUrl());
//            screamxoPlayer.pause();
            }
        } else if (isVideo) {
            if ((screamxoPlayer != null)) {
                if (screamxoPlayer.isPlaying()) {
                    screamxoPlayer.pause();
//                    ((DrawerMainActivity) context).updatePictureInPictureActions(R.drawable.ic_play_arrow_white_24dp, "Pause", CONTROL_TYPE_PAUSE, REQUEST_PAUSE);
                    ((DrawerMainActivity) context).setPictureInPictureActions(R.drawable.ic_play_arrow_white_24dp, R.drawable.next, "Pause", CONTROL_TYPE_PAUSE, REQUEST_PAUSE);
                } else {
                    screamxoPlayer.onResume();
//                    ((DrawerMainActivity) context).updatePictureInPictureActions(R.drawable.ic_stop_white_24dp, "Play", CONTROL_TYPE_PLAY, REQUEST_PLAY);
                    ((DrawerMainActivity) context).setPictureInPictureActions(R.drawable.ic_stop_white_24dp, R.drawable.next, "Play", CONTROL_TYPE_PLAY, REQUEST_PLAY);
                }
            }
        }
    }

    public void setControlsVisibility(String status) {
        if (isVideo) {
            if (app_video_bottom_box != null) {
                if (status.equalsIgnoreCase("hide")) {
                    app_video_bottom_box.setVisibility(View.INVISIBLE);
                    ll_footer.setVisibility(View.INVISIBLE);
                } else if (status.equalsIgnoreCase("show")) {
                    app_video_bottom_box.setVisibility(View.VISIBLE);
                    ll_footer.setVisibility(View.VISIBLE);
                }
            }
        }

        if (mMediaPlayer != null) {
            if (ll_footer != null) {
                if (status.equalsIgnoreCase("hide")) {
                    ll_footer.setVisibility(View.VISIBLE);
                } else if (status.equalsIgnoreCase("show")) {
                    ll_footer.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void playNextItem() {
        if (mMediaPlayer != null) {
            sMediaPlayerLastPosition = 0;
            seekBarAudioProgress.setProgress(0);
            long currentDuration = 0; // in case of the completion...
            long totalDuration = mMediaPlayer.getDuration();

            txtDuration.setText(Utils.milliSecondsToTimer(currentDuration));
            txtEndDuration.setText(Utils.milliSecondsToTimer(totalDuration));

            for (int i = currentMediaPosition + 1; i < mediaposts.size(); i++) {
                if (mediaposts.get(currentMediaPosition).getPosttype() == mediaposts.get(i).getPosttype()) {
                    Audio(null, null, null, null, i);
                    break;
                }
            }
        }

        if (screamxoPlayer != null) {
            for (int i = currentMediaPosition + 1; i < mediaposts.size(); i++) {
                if (mediaposts.get(currentMediaPosition).getPosttype() == mediaposts.get(i).getPosttype()) {
                    Video(null, null, i);
                    break;
                }
            }
        }
    }

    public void playPreviousItem() {
        if (mMediaPlayer != null) {
            sMediaPlayerLastPosition = 0;
            seekBarAudioProgress.setProgress(0);
            long currentDuration = 0; // in case of the completion...
            long totalDuration = mMediaPlayer.getDuration();

            txtDuration.setText(Utils.milliSecondsToTimer(currentDuration));
            txtEndDuration.setText(Utils.milliSecondsToTimer(totalDuration));

            if (currentMediaPosition > 0)
                for (int i = currentMediaPosition - 1; i < mediaposts.size(); i++) {
                    if (mediaposts.get(currentMediaPosition).getPosttype() == mediaposts.get(i).getPosttype()) {
                        Audio(null, null, null, null, i);
                        break;
                    }
                }
        }

        if (screamxoPlayer != null) {
            if (currentMediaPosition > 0)
                for (int i = currentMediaPosition - 1; i < mediaposts.size(); i++) {
                    if (mediaposts.get(currentMediaPosition).getPosttype() == mediaposts.get(i).getPosttype()) {
                        Video(null, null, i);
                        break;
                    }
                }
        }
    }
}