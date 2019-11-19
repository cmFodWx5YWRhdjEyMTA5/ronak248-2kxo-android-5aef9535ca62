package com.screamxo.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.ApiBean.Mediapost;
import com.example.apimodule.ApiBase.ApiBean.ProfileMedia.Posts;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Activity.ProfileActivity;
import com.screamxo.Activity.RahulWork.BoostActivity;
import com.screamxo.Activity.RahulWork.MediaItemDetailActivity;
import com.screamxo.Adapter.ImageVideoAdapter;
import com.screamxo.Interface.AudioVideoInterface;
import com.screamxo.R;
import com.screamxo.Service.InlinePlayerService;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.EventData;
import com.screamxo.Utils.HidingScrollListener;
import com.screamxo.Utils.ImageUtils;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import tcking.github.com.giraffeplayer.GiraffePlayer;

import static android.app.Activity.RESULT_OK;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_BOOST_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS;
import static com.screamxo.R.mipmap.like;
import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP_NEW_PROFILE;
import static com.screamxo.Utils.StaticConstant.LIMIT;

/*todo update media fragment and other page if neccessary on delete of the media itemmmm.. */
public class MediaFragment extends Fragment implements AudioVideoInterface, GiraffePlayer.GiraffePlayerClickListener {
    private static final String TAG = ProfileFragment.class.getSimpleName() + " 1 ";
    private MediaPlayer mMediaPlayer;
    private TextView txtDuration, txtEndDuration;
    private SeekBar seekBarAudioProgress;
    private Handler mHandler = new Handler();
    private int currentMediaPosition = -1;
    private int totalMediaPlayerDuration;

    private Runnable mUpdateTimeTask = new Runnable()
    {
        public void run()
        {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying() && seekBarAudioProgress != null)
            {
                long currentDuration = mMediaPlayer.getCurrentPosition();
                txtEndDuration.setText(Utils.milliSecondsToTimer(totalMediaPlayerDuration));
                txtDuration.setText(Utils.milliSecondsToTimer(currentDuration));
                seekBarAudioProgress.setProgress(mMediaPlayer.getCurrentPosition());
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    @BindView(R.id.recycler_view)
    RecyclerView mrecyclerView;
    @BindView(R.id.ll_audio_video_image_container)
    LinearLayout ll_audio_video_image_media_container;
    @BindView(R.id.lny_vedio_like_bottom)
    LinearLayout lnyVideoBottomLike;
    @BindView(R.id.include_no_data)
    LinearLayout linearLayoutNodata;
    Preferences preferences;
    int pageCounterForAllmedia = 1;
    int mediaPostCount = 0;
    GiraffePlayer giraffePlayer;
    private LinearLayoutManager linearLayoutManagerImageAudio;
    private ImageVideoAdapter imagevideoAdapterAllmedia;
    private Context context;
    private String uId;
    private FetchrServiceBase mService;
    private ArrayList<Mediapost> mediaposts;
    private boolean allMediaBoolean = true;
    String url = "";
    private Call<Posts> UserApiCall;
    private Dialog photoDialog;

    public MediaFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_media_layout, container, false);
        ButterKnife.bind(this, view);
        initControlValue();
        callUserInfoApi();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEventMainThread(EventData event) {
        switch (event.getCode()) {
            case EVENT_SCROLL_TO_TOP_NEW_PROFILE:
                if (getUserVisibleHint()) {
                    FloatingMenuButton floatingMenuButton = ((DrawerMainActivity) context).floatingButton;
                    if (!floatingMenuButton.isMenuOpen()) {
                        if (floatingMenuButton.getBackground().getConstantState() != null
                   && floatingMenuButton.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ic_float_menu_up).getConstantState())) {
                            mrecyclerView.scrollToPosition(0);
                            floatingMenuButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                            floatingMenuButton.closeMenu();
                            return;
                        }
                    }
                }

                break;
        }
    }

    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    private void initControlValue()
    {
        mService = new FetchrServiceBase();
        mediaposts = new ArrayList<>();
        preferences = new Preferences(context);

        pageCounterForAllmedia = 1;
        linearLayoutNodata.setVisibility(View.GONE);
        ((TextView) linearLayoutNodata.findViewById(R.id.txt_no_data)).setText(getString(R.string.msg_no_media_uploaded));
        linearLayoutManagerImageAudio = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mrecyclerView.setLayoutManager(linearLayoutManagerImageAudio);

        mrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mrecyclerView.addOnScrollListener(new HidingScrollListener() {
                    @Override
                    public void onHide() {
                        if (context.getClass() == DrawerMainActivity.class) {
                            ((DrawerMainActivity) context).hideViews();
                        }

                        if (context.getClass() == ProfileActivity.class) {
                            ((ProfileActivity) context).hideViews();
                        }
                    }

                    @Override
                    public void onShow() {
                        if (context.getClass() == DrawerMainActivity.class) {
                            ((DrawerMainActivity) context).showViews();
                        }

                        if (context.getClass() == ProfileActivity.class)
                        {
                            ((ProfileActivity) context).showViews();
                        }
                    }
                });

                if (linearLayoutManagerImageAudio.findLastCompletelyVisibleItemPosition() == (imagevideoAdapterAllmedia.getItemCount() - 1)) {
                    if (allMediaBoolean && mediaposts.size() < mediaPostCount) {
                        updatePaging(false);
                        allMediaBoolean = false;
                        callUserInfoApi();
                    }
                }
            }
        });

        if (context instanceof ProfileActivity)
        {
            uId = ((ProfileFragment) ((ProfileActivity) context).getSupportFragmentManager().getFragments().get(0)).getUid();
        } else {
            uId = preferences.getUserId();
        }
    }

    private void updatePaging(boolean isEnable) {
        if (context instanceof ProfileActivity) {
            Fragment fragment = ((ProfileActivity) context).getSupportFragmentManager().findFragmentByTag(ProfileFragment.class.getSimpleName());
            if (fragment != null && fragment instanceof ProfileFragment) {
                ((ProfileFragment) fragment).viewpager.setPagingEnabled(isEnable);
            }
        }

        if (context instanceof DrawerMainActivity) {
            Fragment fragment = ((DrawerMainActivity) context).getSupportFragmentManager().findFragmentByTag(ProfileFragment.class.getSimpleName());
            if (fragment != null && fragment instanceof ProfileFragment) {
                ((ProfileFragment) fragment).viewpager.setPagingEnabled(isEnable);
            }
        }
    }

    private void callUserInfoApi() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uId);
        map.put("offset", "" + 0);
        map.put("limit", LIMIT);
        map.put("posttype", "1");
        map.put("myid", uId);
        if (Utils.isInternetOn(context)) {
            UserApiCall = mService.getFetcherService(context).UserDetailData(map);
            UserApiCall.enqueue(new Callback<Posts>() {
                @Override
                public void onResponse(Call<Posts> call, Response<Posts> response) {
                    updatePaging(true);
                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {

                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (pageCounterForAllmedia == 1) {
                                mediaposts.clear();
                            }

                            pageCounterForAllmedia++;
                            mediaPostCount = response.body().getResult().getCount();
                            mediaposts.addAll(response.body().getResult().getPosts());
                            if (mediaposts.size() == 0) {
                                linearLayoutNodata.setVisibility(View.VISIBLE);
                                mrecyclerView.setVisibility(View.GONE);
                            } else {
                                mrecyclerView.setVisibility(View.VISIBLE);
                                linearLayoutNodata.setVisibility(View.GONE);
                                setAdapter();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Posts> call, Throwable t) {
                    updatePaging(true);
                }
            });
        } else {
            updatePaging(true);
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void setAdapter() {
        if (imagevideoAdapterAllmedia == null) {
            imagevideoAdapterAllmedia = new ImageVideoAdapter(context, mediaposts, null, this);
            mrecyclerView.setAdapter(imagevideoAdapterAllmedia);
        } else {
            imagevideoAdapterAllmedia.notifyDataSetChanged();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void Video(String url, String type, int index)
    {

        releaseMediaPlayer();
        releaseVideoPlayer();

        if (currentMediaPosition != index)
        {
            currentMediaPosition = index;

            View girrafePlayerView = LayoutInflater.from(context).inflate(R.layout.view_screamxo_player, ll_audio_video_image_media_container, false);
            @SuppressLint("InflateParams")
            View viewBottomView = LayoutInflater.from(context).inflate(R.layout.video_bottom_view, null);

            ll_audio_video_image_media_container.removeAllViews();
            lnyVideoBottomLike.removeAllViews();

            ll_audio_video_image_media_container.addView(girrafePlayerView);
            lnyVideoBottomLike.addView(viewBottomView);

            ll_audio_video_image_media_container.setVisibility(View.VISIBLE);
            lnyVideoBottomLike.setVisibility(View.VISIBLE);

            FrameLayout user_image_container = viewBottomView.findViewById(R.id.user_image_container);
            TextView txtUserName = viewBottomView.findViewById(R.id.txt_user_name);
            ImageView imageComment = viewBottomView.findViewById(R.id.img_comment);
            TextView txtLikeCount = viewBottomView.findViewById(R.id.txt_like);
            Button btnShare = viewBottomView.findViewById(R.id.btnShare);
            ImageView emoji_iv = viewBottomView.findViewById(R.id.emoji_iv);
            ImageView user_iv = viewBottomView.findViewById(R.id.user_iv);

            if (mediaposts.get(index).getMypost() == 1) {
                btnShare.setText(getString(R.string.txt_boost));
            } else {
                btnShare.setText(getString(R.string.txt_share));
            }
            Picasso.with(context)
                    .load(mediaposts.get(index).getUserphotothumb())
                    .placeholder(R.mipmap.user)
                    .error(R.mipmap.user)
                    .transform(new CircleTransform())
                    .into(user_iv);

            txtUserName.setText(mediaposts.get(index).getPostTitle().replaceAll("[-+.^:,@]", ""));

            txtLikeCount.setText("" + mediaposts.get(index).getLikecount());

            if (mediaposts.get(index).getIslike() == 1) {
                emoji_iv.setImageResource(R.drawable.ico_reaction_new);

            } else {
                emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
            }
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaposts.get(index).getMypost() == 1) {
                        Intent gotoActivity = new Intent(context, BoostActivity.class);
                        if (mediaposts.get(index).getMediaUrl().startsWith("image")) {
                            gotoActivity.putExtra("image", mediaposts.get(index).getMediaUrl());
                        } else {
                            gotoActivity.putExtra("image", mediaposts.get(index).getMediaThumb());
                            gotoActivity.putExtra("videourl", mediaposts.get(index).getMediaUrl());
                        }
                        gotoActivity.putExtra("boost_url", String.valueOf(mediaposts.get(index).getBoost_url()));

                        gotoActivity.putExtra("boost_type", String.valueOf(StaticConstant.BOOST_TYPE_MEDIA_ITEM));
                        gotoActivity.putExtra("itemid", String.valueOf(mediaposts.get(index).getId()));
                        ((Activity) context).startActivityForResult(gotoActivity, REQ_CODE_BOOST_ACTIVITY_RESULTS);
                    } else {
                        StaticConstant.from_ = "media";
                        StaticConstant.from_url = mediaposts.get(currentMediaPosition).getMediaUrl();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("FRAGMENT_INDEX", 5);
                        getActivity().setResult(RESULT_OK, returnIntent);
                        getActivity().finish();
                    }
                }
            });

            emoji_iv.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    try {
                        if (mediaposts.get(index).getIslike() == 0) {
                            emoji_iv.setImageResource(R.drawable.ico_reaction_new);
                            txtLikeCount.setText("" + (mediaposts.get(index).getLikecount() + 1));
                            mediaposts.get(index).setLikecount(mediaposts.get(index).getLikecount() + 1);
                            mediaposts.get(index).setIslike(1);
                            setLikeApi(mediaposts.get(index).getId(), StaticConstant.LIKE);
                        } else {
                            emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
                            txtLikeCount.setText("" + (mediaposts.get(index).getLikecount() - 1));
                            mediaposts.get(index).setLikecount(mediaposts.get(index).getLikecount() - 1);
                            mediaposts.get(index).setIslike(0);
                            setLikeApi(mediaposts.get(index).getId(), StaticConstant.UNLIKE);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            });
            txtLikeCount.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                        ((DrawerMainActivity) context).gotoLogin(context);
                    } else {
                        try {
                            if (mediaposts.get(index).getIslike() == 0) {
                                txtLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_reaction_new, 0, 0, 0);
                                txtLikeCount.setText("" + (mediaposts.get(index).getLikecount() + 1));
                                mediaposts.get(index).setLikecount(mediaposts.get(index).getLikecount() + 1);
                                mediaposts.get(index).setIslike(1);
                                setLikeApi(mediaposts.get(index).getId(), StaticConstant.LIKE);
                            } else {
                                txtLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_emoji_smile_white_bg, 0, 0, 0);
                                txtLikeCount.setText("" + (mediaposts.get(index).getLikecount() - 1));
                                mediaposts.get(index).setLikecount(mediaposts.get(index).getLikecount() - 1);
                                mediaposts.get(index).setIslike(0);
                                setLikeApi(mediaposts.get(index).getId(), StaticConstant.UNLIKE);
                            }
                        } catch (IndexOutOfBoundsException e) {
                            Utils.showToast(context, "Index Out of Box ");
                        }
                    }
                }
            });

            girrafePlayerView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    showOverLayPermission(context, true, mediaposts.get(currentMediaPosition).getMediaUrl());
                    return true;
                }
            });

            giraffePlayer = new GiraffePlayer((AppCompatActivity) context);
            giraffePlayer.play(mediaposts.get(currentMediaPosition).getMediaUrl());
            giraffePlayer.setGiraffePlayerClickListener(this);
            giraffePlayer.onComplete(() -> {
//                giraffePlayer.seekTo(0, true);
//                giraffePlayer.start();
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
    }

    @SuppressLint("NewApi")
    private void showOverLayPermission(Context context, boolean isShowOverlayPermission, String videoUrl) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            final Intent intent = new Intent(context, InlinePlayerService.class);
            intent.putExtra("videoUrl", videoUrl);
            ContextCompat.startForegroundService(context, intent);
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (Settings.canDrawOverlays(context)) {
                final Intent intent = new Intent(context, InlinePlayerService.class);
                intent.putExtra("videoUrl", videoUrl);
                ContextCompat.startForegroundService(context, intent);
                return;
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (isShowOverlayPermission) {
                final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
                ((Activity) context).startActivityForResult(intent, 1000);
            }
        }
    }

    private void clearVideoPreviewView() {
        Log.d(TAG, "clearVideoPreviewView: ");
        ll_audio_video_image_media_container.removeAllViews();
        lnyVideoBottomLike.removeAllViews();
        lnyVideoBottomLike.setVisibility(View.GONE);
        ll_audio_video_image_media_container.setVisibility(View.GONE);
        currentMediaPosition = -1;
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void releaseVideoPlayer() {
        if (giraffePlayer != null) {
            giraffePlayer.stop();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void Audio(String url, String audioThum, String title, String type, int index) {
        Log.d(TAG, "Audio index: " + index);
        releaseMediaPlayer();
        releaseVideoPlayer();
        if (currentMediaPosition != index) {
            currentMediaPosition = index;
            View audioViewContainer = LayoutInflater.from(context).inflate(R.layout.audio_view_layout, ll_audio_video_image_media_container, false);

            FrameLayout user_image_container = audioViewContainer.findViewById(R.id.user_image_container);
            ImageView emoji_iv = audioViewContainer.findViewById(R.id.emoji_iv);
            ImageView user_iv = audioViewContainer.findViewById(R.id.user_iv);

            TextView txtUserName = audioViewContainer.findViewById(R.id.txt_user_name);
            TextView txtLikeCount = audioViewContainer.findViewById(R.id.txt_like);
            TextView txtTitle = audioViewContainer.findViewById(R.id.txt_title);
            ImageView imgShare = audioViewContainer.findViewById(R.id.img_share);
            Button btnShare = audioViewContainer.findViewById(R.id.btnShare);
            ImageView imgComment = audioViewContainer.findViewById(R.id.img_comment);

            lnyVideoBottomLike.setVisibility(View.GONE);
            lnyVideoBottomLike.removeAllViews();

            ll_audio_video_image_media_container.setVisibility(View.VISIBLE);
            ll_audio_video_image_media_container.removeAllViews();
            ll_audio_video_image_media_container.addView(audioViewContainer);

            txtTitle.setText(mediaposts.get(currentMediaPosition).getPostTitle().replaceAll("[-+.^:,@]", ""));

            Picasso.with(context)
                    .load(mediaposts.get(currentMediaPosition).getUserphotothumb())
                    .placeholder(R.mipmap.user)
                    .error(R.mipmap.user)
                    .transform(new CircleTransform())
                    .into(user_iv);

            if (mediaposts.get(index).getMypost() == 1) {
                btnShare.setText(getString(R.string.txt_boost));
            } else {
                btnShare.setText(getString(R.string.txt_share));
            }

            txtUserName.setText(mediaposts.get(currentMediaPosition).getPostTitle().replaceAll("[-+.^:,@]", ""));
            txtLikeCount.setText("" + mediaposts.get(index).getLikecount());

            if (mediaposts.get(index).getCommentcount() > 0)
                imgComment.setImageResource(R.drawable.ico_comment_new);
            else
                imgComment.setImageResource(R.drawable.ico_comment_new_grey);


            if (mediaposts.get(index).getIslike() == 1) {
                emoji_iv.setImageResource(R.drawable.ico_reaction_new);

            } else {
                emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
            }
            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
                        Intent intent = new Intent(context, MediaItemDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("itemId", "" + mediaposts.get(currentMediaPosition).getId());
                        bundle.putString("username", mediaposts.get(currentMediaPosition).getUsername());
                        intent.putExtras(bundle);
                        ((Activity) context).startActivityForResult(intent, REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS);
                    }

                }
            });


            imgComment.setOnClickListener(view -> {
                if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                    ((DrawerMainActivity) context).gotoLogin(context);
                } else {
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
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaposts.get(index).getMypost() == 1) {
                        Intent gotoActivity = new Intent(context, BoostActivity.class);
                        gotoActivity.putExtra("itemid", String.valueOf(mediaposts.get(currentMediaPosition).getId()));
                        if (mediaposts.get(currentMediaPosition).getMediaUrl().startsWith("image")) {
                            gotoActivity.putExtra("image", mediaposts.get(currentMediaPosition).getMediaUrl());
                        } else {
                            gotoActivity.putExtra("image", mediaposts.get(currentMediaPosition).getMediaThumb());
                        }
                        gotoActivity.putExtra("item_name", mediaposts.get(currentMediaPosition).getPostTitle());
                        gotoActivity.putExtra("boost_type", String.valueOf(StaticConstant.BOOST_TYPE_MEDIA_ITEM));
                        gotoActivity.putExtra("boost_url", String.valueOf(mediaposts.get(index).getBoost_url()));

                        ((Activity) context).startActivityForResult(gotoActivity, REQ_CODE_BOOST_ACTIVITY_RESULTS);
                    } else {
                        StaticConstant.from_ = "media";
                        StaticConstant.from_url = mediaposts.get(currentMediaPosition).getMediaUrl();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("FRAGMENT_INDEX", 5);
                        getActivity().setResult(RESULT_OK, returnIntent);
                        getActivity().finish();
                    }
                }
            });

            setAudioPlayer(audioViewContainer);
        } else {
            ll_audio_video_image_media_container.removeAllViews();
            ll_audio_video_image_media_container.setVisibility(View.GONE);
            currentMediaPosition = -1;
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void Image(String imgurl, String postId, int index, String type) {
        Log.d(TAG, "Image: " + mediaposts.get(index));

        if (!this.url.equals(imgurl)) {
            View imageInflatedView = LayoutInflater.from(context).inflate(R.layout.item_image_media_fragment, null);

            View userImageContainer = imageInflatedView.findViewById(R.id.user_image_container);
            ImageView userIv = imageInflatedView.findViewById(R.id.user_iv);
            ImageView emojiIv = imageInflatedView.findViewById(R.id.emoji_iv);

            TextView txt_user_name = imageInflatedView.findViewById(R.id.txt_user_name);
            ImageView imgPhoto = imageInflatedView.findViewById(R.id.img_preview);
            TextView txtLikeCount = imageInflatedView.findViewById(R.id.txt_like);
            ImageView imgComment = imageInflatedView.findViewById(R.id.img_comment);
            Button btnShare = imageInflatedView.findViewById(R.id.btnShare);
            lnyVideoBottomLike.setVisibility(View.GONE);
            lnyVideoBottomLike.removeAllViews();
            ll_audio_video_image_media_container.setVisibility(View.VISIBLE);
            ll_audio_video_image_media_container.removeAllViews();
            ll_audio_video_image_media_container.addView(imageInflatedView);

            Picasso.with(context)
                    .load(imgurl)
                    .placeholder(R.mipmap.img_placeholder)
                    .error(R.mipmap.img_placeholder)
                    .fit()
                    .centerCrop()
                    .into(imgPhoto);

            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoDialog = ImageUtils.customChoosePhoto(context, imgurl);
                }
            });

            Picasso.with(context)
                    .load(mediaposts.get(index).getUserphotothumb())
                    .error(R.mipmap.pic_holder_dashboard)
                    .placeholder(R.mipmap.pic_holder_dashboard)
                    .fit()
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(userIv);

            if (mediaposts.get(index).getMypost() == 1) {
                btnShare.setText(getString(R.string.txt_boost));
            } else {
                btnShare.setText(getString(R.string.txt_share));
            }

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaposts.get(index).getMypost() == 1) {
                        Intent gotoActivity = new Intent(context, BoostActivity.class);
                        if (mediaposts.get(index).getMediaUrl().startsWith("image")) {
                            gotoActivity.putExtra("image", mediaposts.get(index).getMediaUrl());
                        } else {
                            gotoActivity.putExtra("image", mediaposts.get(index).getMediaThumb());
                        }
                        gotoActivity.putExtra("boost_type", String.valueOf(StaticConstant.BOOST_TYPE_MEDIA_ITEM));
                        gotoActivity.putExtra("itemid", String.valueOf(mediaposts.get(index).getId()));
                        gotoActivity.putExtra("item_name", String.valueOf(mediaposts.get(index).getPostTitle()));
                        gotoActivity.putExtra("boost_url", String.valueOf(mediaposts.get(index).getBoost_url()));
                        ((Activity) context).startActivityForResult(gotoActivity, REQ_CODE_BOOST_ACTIVITY_RESULTS);
                    } else {
                        StaticConstant.from_ = "media";
                        StaticConstant.from_url = imgurl;
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("FRAGMENT_INDEX", 5);
                        getActivity().setResult(RESULT_OK, returnIntent);
                        getActivity().finish();
                    }
                }
            });
            if (mediaposts.get(index).getIslike() == 1) {
                emojiIv.setImageResource(R.drawable.ico_reaction_new);


            } else {
                emojiIv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
            }

            txtLikeCount.setText("" + mediaposts.get(index).getLikecount());
            txt_user_name.setText(mediaposts.get(index).getPostTitle().replaceAll("[-+.^:,@]", ""));

            emojiIv.setOnClickListener(v ->
            {
                try
                {
                    if (mediaposts.get(index).getIslike() == 0) {
                        emojiIv.setImageResource(R.drawable.ico_reaction_new);
                        txtLikeCount.setText("" + (mediaposts.get(index).getLikecount() + 1));
                        mediaposts.get(index).setLikecount(mediaposts.get(index).getLikecount() + 1);
                        mediaposts.get(index).setIslike(1);
                        setLikeApi(mediaposts.get(index).getId(), StaticConstant.LIKE);
                    } else {
                        emojiIv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
                        txtLikeCount.setText("" + (mediaposts.get(index).getLikecount() - 1));
                        mediaposts.get(index).setLikecount(mediaposts.get(index).getLikecount() - 1);
                        mediaposts.get(index).setIslike(0);
                        setLikeApi(mediaposts.get(index).getId(), StaticConstant.UNLIKE);
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            });

            txt_user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MediaItemDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("itemId", "" + mediaposts.get(index).getId());
                    bundle.putString("username", mediaposts.get(index).getUsername());
                    intent.putExtras(bundle);
                    ((Activity) context).startActivityForResult(intent, REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS);
                }
            });
            if (mediaposts.get(index).getCommentcount() > 0)
                imgComment.setImageResource(R.drawable.ico_comment_new);
            else
                imgComment.setImageResource(R.drawable.ico_comment_new_grey);


            imgComment.setOnClickListener(view -> {
                Intent intent = new Intent(context, MediaItemDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("itemId", "" + mediaposts.get(index).getId());
                bundle.putString("username", mediaposts.get(index).getUsername());
                intent.putExtras(bundle);
                ((Activity) context).startActivityForResult(intent, REQ_CODE_MEDIA_ITEM_DETAIL_ACTIVITY_RESULTS);
            });

            url = imgurl;
        } else {
            ll_audio_video_image_media_container.removeAllViews();
            ll_audio_video_image_media_container.setVisibility(View.GONE);
            url = "";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (UserApiCall != null) {
            UserApiCall.cancel();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setAudioPlayer(final View view) {

        ImageView imgPlay = view.findViewById(R.id.app_video_play);
        ImageView imgBackPlayer = view.findViewById(R.id.app_video_finish);
        ImageView audioViewBackground = view.findViewById(R.id.img_audio_background);
        LinearLayout lnyAudioTitle = view.findViewById(R.id.lny_audio_title);
        LinearLayout lnyPlayerSeekbar = view.findViewById(R.id.lny_player_seek_bar);

        seekBarAudioProgress = view.findViewById(R.id.app_audio_seekBar);
        txtDuration = view.findViewById(R.id.app_audio_currentTime);
        txtEndDuration = view.findViewById(R.id.app_audio_endTime);
        imgPlay.setEnabled(false);

        if ((mediaposts.get(currentMediaPosition).getMediaThumb().contains("voice")) || (mediaposts.get(currentMediaPosition).getMediaThumb().contains("audio"))) {
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

        imgBackPlayer.setOnClickListener(v -> {
            try {
                LinearLayout view1 = (LinearLayout) v.getParent().getParent().getParent();
                view1.removeAllViews();
                view1.setVisibility(View.GONE);
                releaseMediaPlayer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        mMediaPlayer = new MediaPlayer();

        try {
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int result = am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

            if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == result) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                updateSeekBarTimerTask();

                try {
                    mMediaPlayer.setDataSource(mediaposts.get(currentMediaPosition).getMediaUrl());
                    mMediaPlayer.prepareAsync();
                    txtDuration.setText(R.string.txt_loading);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mMediaPlayer.setOnPreparedListener(mp -> {
                    try {
                        mMediaPlayer.start();

                        totalMediaPlayerDuration = mp.getDuration();
                        long currentDuration = mMediaPlayer.getCurrentPosition();

                        txtEndDuration.setText(Utils.milliSecondsToTimer(totalMediaPlayerDuration));
                        txtDuration.setText(Utils.milliSecondsToTimer(currentDuration));

                        imgPlay.setEnabled(true);
                        imgPlay.setImageResource(R.drawable.ic_stop_white_24dp);

                        seekBarAudioProgress.setMax(totalMediaPlayerDuration);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                imgPlay.setOnClickListener(v -> {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        imgPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                    } else {
                        mMediaPlayer.start();
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
                    try {
                        seekBarAudioProgress.setProgress(0);
                        imgPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);

                        long currentDuration = 0; // in case of the completion...

                        txtDuration.setText(Utils.milliSecondsToTimer(currentDuration));
                        txtEndDuration.setText(Utils.milliSecondsToTimer(totalMediaPlayerDuration));

                        for (int i = currentMediaPosition + 1; i < mediaposts.size(); i++) {
                            if (mediaposts.get(currentMediaPosition).getPosttype() == mediaposts.get(i).getPosttype()) {
                                Audio(null, null, null, null, i);
                                break;
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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
                            mMediaPlayer.setVolume(1f, 1f);
                            break;
                        default:
                            break;
                    }
                }
            };

    private void updateSeekBarTimerTask() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private void setLikeApi(int id, int action) {
        Map<String, Integer> map = new HashMap<>();
        map.put("postid", id);
        map.put("uid", Integer.parseInt(preferences.getUserId())); // only the logged in user can like !
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
    public void onBackArrowClicked()
    {
        ll_audio_video_image_media_container.removeAllViews();
        lnyVideoBottomLike.removeAllViews();
        lnyVideoBottomLike.setVisibility(View.GONE);
        ll_audio_video_image_media_container.setVisibility(View.GONE);
        url = "";
    }

    @Override
    public void onShareButtonClicked()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, (url));
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Send to"));
    }

    @Override
    public void onFullScreenBtnClicked()
    {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (photoDialog != null) {
            photoDialog.dismiss();
        }
    }

    public void onPause() {
        super.onPause();
        if (giraffePlayer != null) {
            giraffePlayer.onPause();
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void onResume() {
        super.onResume();
        if (giraffePlayer != null) {
            giraffePlayer.onResume();
            giraffePlayer.start();
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }
}