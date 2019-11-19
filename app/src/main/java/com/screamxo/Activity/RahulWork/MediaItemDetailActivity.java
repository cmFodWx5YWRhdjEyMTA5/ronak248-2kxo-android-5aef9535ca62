package com.screamxo.Activity.RahulWork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.apimodule.ApiBase.ApiBean.CommentBean;
import com.example.apimodule.ApiBase.ApiBean.ItemCategoriesBean;
import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.ApiBean.PostDetails.Comment;
import com.example.apimodule.ApiBase.ApiBean.PostDetails.PostDetailResult;
import com.example.apimodule.ApiBase.ApiBean.PostDetails.PostDetails;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Adapter.BottomSheetAdapter;
import com.screamxo.Adapter.ImageVideoAdapter;
import com.screamxo.Adapter.PostReviewAdapter;
import com.screamxo.Emoji.EmojiLayoutFragment;
import com.screamxo.Emoji.Emojix;
import com.screamxo.Emoji.tagging.OnHashTagClickListener;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Interface.EmojiTextInterface;
import com.screamxo.Others.OnBottomSheetItemClickListener;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.DialogFragments;
import com.screamxo.Utils.HidingScrollListener;
import com.screamxo.Utils.ImageUtils;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;
import tcking.github.com.giraffeplayer.ScreamxoPlayer;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_BOOST_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;
import static com.screamxo.FireBasePush.MyFirebaseMessagingService.ACTION_COMMENT_MEDIA;

// TODO: 27/11/17 media item delete...

/* Activity class showing detail of each media item e.g. AUDIO VEDIO IMAGE.....  NOT SHOP ITEM.. */
public class MediaItemDetailActivity extends AppCompatActivity implements View.OnClickListener, EmojiTextInterface, TextWatcher, CommonMethod, ScreamxoPlayer.ScreamxoPlayerClickListener {
    private static final String TAG = "MediaItemDetailActivity";
    public int sMediaPlayerLastPosition;
    public PostReviewAdapter itemreviewadapter;
    @BindView(R.id.ll_container)
    LinearLayout ll_container;
    @BindView(R.id.rl_footer)
    RelativeLayout rl_footer;
    @BindView(R.id.rl_Bottom)
    RelativeLayout rl_Bottom;
    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    EmojiTextInterface emojiTextInterface;
    @BindView(R.id.img_emoji)
    ImageView imgEmoji;
    @BindView(R.id.frame_emoji)
    FrameLayout frameEmoji;
    CommonMethod commonMethod;
    int textLenght = 0;
    ScreamxoPlayer screamxoPlayer;
    @BindView(R.id.user_iv)
    ImageView user_iv;
    @BindView(R.id.emoji_iv)
    ImageView emoji_iv;
    @BindView(R.id.txtProductName)
    TextView txtProductName;
    @BindView(R.id.txtBuy)
    Button txtBuy;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.frame_detail)
    FrameLayout frame_detail;
    String itemId = "";
    @BindView(R.id.txtBookmarkCount)
    TextView txtBookmarkCount;
    PostDetailResult postDetails;
    Boolean isBookmarkCheck = false;
    @BindView(R.id.rc_Comments)
    RecyclerView rc_Comments;
    @BindView(R.id.img_User)
    ImageView img_User;
    @BindView(R.id.edt_msg)
    EditText edtMsg;
    int height = 0, width = 0, profileheight = 0, profilewidth = 0;
    Call<PostDetails> streampostcall;
    Call<LoginBean> sendMsgBeanCall;
    Call<ItemCategoriesBean> deleteMediaItemCall;
    Call<CommentBean> reportPostCall;
    private BroadcastReceiver mReceiver;
    private MediaPlayer mMediaPlayer;
    private SeekBar seekBarAudioProgress;
    private TextView txtDuration, txtEndDuration;
    private Context context;
    private Preferences preferences;
    private FetchrServiceBase mservice;
    private ArrayList<Comment> listComments;
    private FetchrServiceBase mService;
    private Dialog photoDialog;
    private boolean isPortrait;
    private Handler mHandler = new Handler();
    private ImageView imgPlay;
    private int totalMediaPlayerDuration;
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying() && seekBarAudioProgress != null) {
                long currentDuration = mMediaPlayer.getCurrentPosition();

                txtEndDuration.setText(Utils.milliSecondsToTimer(totalMediaPlayerDuration));
                txtDuration.setText(Utils.milliSecondsToTimer(currentDuration));
                seekBarAudioProgress.setProgress(mMediaPlayer.getCurrentPosition());
            }
            mHandler.postDelayed(this, 1000);
        }
    };
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
                                Log.d(TAG, e.getMessage());
                            }
                            break;
                        default:
                            break;
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_item_detail);
        ButterKnife.bind(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        frame_detail.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewPager.LayoutParams.MATCH_PARENT,
                (int) (metrics.heightPixels * .60)));
        initData();
        initFabIcon();
    }

    private void initData() {
        commonMethod = this;
        emojiTextInterface = this;
        context = MediaItemDetailActivity.this;
        isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        mService = new FetchrServiceBase();
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.img_placeholder);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {
        }

        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            profileheight = bd.getBitmap().getHeight();
            profilewidth = bd.getBitmap().getWidth();
        } catch (Exception ignored) {
        }

        listComments = new ArrayList<>();
        preferences = new Preferences(context);
        mservice = new FetchrServiceBase();
        if (getIntent().getExtras() != null) {
            itemId = getIntent().getStringExtra("itemId");

            if (getIntent().getStringExtra("username") == null)
                txtProductName.setText(preferences.getUserName());
            else
                txtProductName.setText(getIntent().getStringExtra("username"));
            if (!TextUtils.isEmpty(preferences.getUserProfile()))
                Picasso.with(context)
                        .load(preferences.getUserProfile())
                        .placeholder(R.mipmap.user)
                        .error(R.mipmap.user)
                        .transform(new CircleTransform())
                        .into(user_iv);
            else {
                Picasso.with(context)
                        .load(R.mipmap.user)
                        .placeholder(R.mipmap.user)
                        .error(R.mipmap.user)
                        .transform(new CircleTransform())
                        .into(user_iv);
            }

        }

        rc_Comments.setLayoutManager(new LinearLayoutManager(context));

        rc_Comments.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                if (!floatingButton.isMenuOpen()) {
                    floatingButton.setBackground(getResources().getDrawable(R.drawable.ic_float_menu_up));
                }
            }

            @Override
            public void onShow() {
                if (!floatingButton.isMenuOpen()) {
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                }
            }
        });

        callItemDetailapi();

//        if (!preferences.getUserProfile().equalsIgnoreCase("")) {
        if (preferences.getUserProfile() != null) {
            Picasso.with(context)
                    .load(preferences.getUserProfile())
                    .placeholder(R.mipmap.user)
                    .error(R.mipmap.user)
                    .transform(new CircleTransform())
                    .into(img_User);
        }

        edtMsg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    callSendMsgApi();
                }
                return handled;
            }
        });

        edtMsg.addTextChangedListener(this);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, " onRecieve");
                Utils.printIntentData(intent);
                callItemDetailapi();
            }
        };
    }

    private void initFabIcon() {
        try {
            floatingButton = findViewById(R.id.my_floating_button);
            floatingButton.setStartAngle(0)
                    .setEndAngle(360)
                    .setAnimationType(AnimationType.EXPAND)
                    .setRadius(170)
                    .setAnchored(false)
                    .getAnimationHandler()
                    .setOpeningAnimationDuration(500)
                    .setClosingAnimationDuration(200)
                    .setLagBetweenItems(0)
                    .setOpeningInterpolator(new FastOutSlowInInterpolator())
                    .setClosingInterpolator(new FastOutLinearInInterpolator())
                    .shouldFade(true)
                    .shouldScale(true)
                    .shouldRotate(true);

            floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));

            floatingButton.setStateChangeListener(new FloatingMenuStateChangeListener() {
                @Override
                public void onMenuOpened(FloatingMenuButton floatingMenuButton) {
                    Utils.hideKeyboard(MediaItemDetailActivity.this);
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.cross));
                }

                @Override
                public void onMenuClosed(FloatingMenuButton floatingMenuButton) {
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                }
            });

            floatingButton.setOnClickBtnListener(new FloatingMenuButton.OnClickBtnListener() {
                @Override
                public void onClickBtn() {
                    if (!floatingButton.isMenuOpen()
                            && floatingButton.getBackground().getConstantState() != null
                            && floatingButton.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ic_float_menu_up).getConstantState())) {
                        rc_Comments.scrollToPosition(0);
                        floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                    }
                }
            });

            sbProfile = findViewById(R.id.sbProfile);
            sbProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 6);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbSocial = findViewById(R.id.sbSocial);
            sbSocial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 3);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbChat = findViewById(R.id.sbChat);
            sbChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent gotoNext = new Intent(MediaItemDetailActivity.this, UploadDataActivity.class);
                    startActivityForResult(gotoNext, REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS);
                }
            });

            sbWorld = findViewById(R.id.sbWorld);
            sbWorld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 2);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbSearch = findViewById(R.id.sbSearch);
            sbSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_options));
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postDetails != null) {
                        floatingButton.closeMenu();
                        showMediaItemOption();
                    }
                }
            });

            sbflSetting = findViewById(R.id.sbflSetting);
            sbflSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 7);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            subFriend = findViewById(R.id.subFriend);
            subFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 5);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbflHome = findViewById(R.id.sbflHome);
            sbflHome.setOnClickListener(view -> {
                floatingButton.closeMenu();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", 1);
                setResult(RESULT_OK, returnIntent);
                finish();
            });

        } catch (Exception e) {
        }
    }

    private void showMediaItemOption() {
        @SuppressLint("InflateParams") View modalbottomsheet = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);

        tv_title.setText(getString(R.string.txt_choose_action));

        ArrayList<String> inputList = new ArrayList<>();
        if (postDetails.getMypost() == 1) {
            inputList.add(getString(R.string.txt_delete_post));
            inputList.add(getString(R.string.txt_share));
        } else {
            inputList.add(getString(R.string.txt_report_post));
            inputList.add(getString(R.string.txt_share));
        }

        rv_container.setAdapter(new BottomSheetAdapter(context, inputList, null, null, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                dialog.dismiss();
                if (input.equalsIgnoreCase(getString(R.string.txt_report_post))) {
                    reportPost();
                    return;
                }

                if (input.equalsIgnoreCase("Share")) {
                    shareItem();
                    return;
                }

                if (input.equalsIgnoreCase("Delete Post")) {
                    deleteItem();
                    return;
                }
            }
        }));
    }

    private void reportPost() {
        try {
            Map<String, Integer> map = new HashMap<>();
            map.put("uid", Integer.parseInt(preferences.getUserId()));
            map.put("postid", postDetails.getId());

            if (Utils.isInternetOn(context)) {
                reportPostCall = mService.getFetcherService(context).ReportComment(map);
                reportPostCall.enqueue(new retrofit2.Callback<CommentBean>() {
                    @Override
                    public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {
                        if (response == null) {
                            return;
                        }
                        if (response.code() == StaticConstant.RESULT_OK) {
                            int type;
                            if (response.body().getStatus().equalsIgnoreCase(StaticConstant.STATUS_1)) {
                                type = DialogBox.DIALOG_SUCESS;
                            } else {
                                type = DialogBox.DIALOG_FAILURE;
                            }
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), type, null);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteItem() {
        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            Map<String, Integer> params = new HashMap<>();
            params.put("postid", postDetails.getId());
            // params.put("itemid", "" + postDetails.getId());
            // params.put("itemcreatedby", preferences.getUserId());
            // params.put("mediaid", String.valueOf(postDetails.getMedia().get(0).getMediaId()));

            deleteMediaItemCall = mService.getFetcherService(context).deleteMediaPost(params);
            deleteMediaItemCall.enqueue(new Callback<ItemCategoriesBean>() {
                @Override
                public void onResponse(Call<ItemCategoriesBean> call, Response<ItemCategoriesBean> response) {
                    progreessbar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (response.body().getMsg() != null) {
                                Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("IS_ITEM_DELETED", true);
                                returnIntent.putExtra("MESSAGE", response.body().getMsg());
                                finish();
                            }

                        } else {
                            if (response.body().getMsg() != null)
                                DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemCategoriesBean> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void deleteEmoji() {

        String text = edtMsg.getText().toString();

        if (text.endsWith("[:angryface:")) {
            removeString(text, "\\[:angryface:");

        } else if (text.endsWith("[:bigsmile:")) {
            removeString(text, "\\[:bigsmile:");

        } else if (text.endsWith("[:cry:")) {
            removeString(text, "\\[:cry:");

        } else if (text.endsWith("[:dizzy:")) {
            removeString(text, "\\[:dizzy:");

        } else if (text.endsWith("[:rosy-chicks:")) {
            removeString(text, "\\[:rosy-chicks:");

        } else if (text.endsWith("[:tongue:")) {
            removeString(text, "\\[:tongue:");

        } else if (text.endsWith("[:wink:")) {
            removeString(text, "\\[:wink:");
        }
    }

    private void removeString(String text, String emoji) {
        Pattern p = Pattern.compile(emoji);
        Matcher m = p.matcher(text);
        int satrtIndex = 0, endIndex = 0;
        while (m.find()) {
            satrtIndex = m.start();
            endIndex = m.end();
        }
        String s = new StringBuilder().append(text).delete(satrtIndex, endIndex).toString();
        edtMsg.setText(s);
        edtMsg.setSelection(s.length());
    }

    private void callSendMsgApi() {
        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();

            map.put("commentby", preferences.getUserId());
            map.put("commentdesc", "" + edtMsg.getText().toString());
            map.put("postid", itemId);

            sendMsgBeanCall = mservice.getFetcherService(context).PostCommet(map);

            sendMsgBeanCall.enqueue(new Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            edtMsg.setText("");
                            callItemDetailapi();
                        } else if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
//                            Utils.unAuthentication(context);
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void callItemDetailapi() {

        progreessbar.setVisibility(View.VISIBLE);
        listComments.clear();
        Map<String, String> map = new HashMap<>();
        map.put("postid", itemId);
        map.put("myid", preferences.getUserId());
        map.put("uid", preferences.getUserId());
        if (Utils.isInternetOn(context)) {
            streampostcall = mservice.getFetcherService(context).getPostDetail(map);
            streampostcall.enqueue(new Callback<PostDetails>() {
                @Override
                public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            postDetails = response.body().getResult();
                            initView();
                            if (response.body().getResult().getComments().size() > 0) {
                                listComments.addAll(response.body().getResult().getComments());
//                              Collections.reverse(listComments);
                                setAdapter();
                            }
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PostDetails> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
            progreessbar.setVisibility(View.GONE);
        }
    }

    private void setAdapter() {
        if (itemreviewadapter == null) {
            itemreviewadapter = new PostReviewAdapter(context, listComments);
            rc_Comments.setAdapter(itemreviewadapter);
        } else {
            itemreviewadapter.notifyDataSetChanged();
        }
        ((LinearLayoutManager) rc_Comments.getLayoutManager()).scrollToPositionWithOffset(0, 0);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        if (postDetails.getMypost() == 1) {
            txtBuy.setText(getString(R.string.txt_boost));
        } else {
            txtBuy.setText("SHARE");
        }

        txtBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postDetails.getMypost() == 1) {
                    Intent gotoActivity = new Intent(context, BoostActivity.class);
                    if (postDetails.getMedia().get(0).getMediaType().startsWith("video")) {
                        gotoActivity.putExtra("image", postDetails.getMedia().get(0).getMediaThumb());
                        gotoActivity.putExtra("videourl", postDetails.getMedia().get(0).getMediaUrl());
                    } else if (postDetails.getMedia().get(0).getMediaType().startsWith("image")) {
                        gotoActivity.putExtra("image", postDetails.getMedia().get(0).getMediaUrl());
                    } else
                        gotoActivity.putExtra("image", postDetails.getMedia().get(0).getMediaThumb());


                    gotoActivity.putExtra("boost_url", "http://apis.2kxo.com/item/itemdetail/" + postDetails.getId());

                    gotoActivity.putExtra("boost_type", String.valueOf(StaticConstant.BOOST_TYPE_MEDIA_ITEM));
                    gotoActivity.putExtra("itemid", String.valueOf(postDetails.getId()));
                    ((Activity) context).startActivityForResult(gotoActivity, REQ_CODE_BOOST_ACTIVITY_RESULTS);
                } else {
                    shareItem();
                }
            }
        });

        txtBookmarkCount.setText(String.valueOf(postDetails.getLikecount()));

        if (postDetails.getIslike() == 1) {
            Picasso.with(context)
                    .load(R.drawable.ico_reaction_new)
                    .placeholder(R.drawable.ico_reaction_new)
                    .error(R.drawable.ico_reaction_new)
                    .into(emoji_iv);
            isBookmarkCheck = true;
        } else {
            Picasso.with(context)
                    .load(R.drawable.goodmoodemoticon)
                    .placeholder(R.drawable.goodmoodemoticon)
                    .error(R.drawable.goodmoodemoticon)
                    .into(emoji_iv);
            isBookmarkCheck = false;
        }

        Picasso.with(context)
                .load(postDetails.getUserphotothumb())
                .error(R.mipmap.pic_holder_dashboard)
                .placeholder(R.mipmap.pic_holder_dashboard)
                .fit()
                .centerCrop()
                .transform(new CircleTransform())
                .into(user_iv);
        if (!postDetails.getMedia().isEmpty())
            if (!postDetails.getMedia().get(0).getMediaType().isEmpty()) {
                if (postDetails.getMedia().get(0).getMediaType().contains("image")) {
                    View view = LayoutInflater.from(context).inflate(R.layout.item_media_details_image, ll_container, false);
//                LinearLayout ll_image_preview_bottom_bar = view.findViewById(R.id.ll_image_preview_bottom_bar);
//                ll_image_preview_bottom_bar.setVisibility(View.GONE);

                    ll_container.removeAllViews();
                    ll_container.addView(view);
                    ll_container.setVisibility(View.VISIBLE);
                    ImageView img_sold = view.findViewById(R.id.img_sold);
                    img_sold.setVisibility(View.GONE);
                    TextView txt_item_cost = view.findViewById(R.id.txt_item_cost);
                    txt_item_cost.setVisibility(View.GONE);

                    ImageView imageItem = view.findViewById(R.id.img_item);


                    Glide.with(context)
                            .load(postDetails.getMedia().get(0).getMediaUrl())
                            .placeholder(R.mipmap.img_placeholder).error(R.mipmap.img_placeholder)
                            .apply(new RequestOptions().fitCenter().centerCrop())
                            .into(imageItem);


              /*  Picasso.with(context)
                        .load(postDetails.getMedia().get(0).getMediaUrl())
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .fit()
                        .centerCrop()
                        .into(imageItem);*/
                    imageItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            photoDialog = ImageUtils.customChoosePhoto(context, postDetails.getMedia().get(0).getMediaUrl());
                        }
                    });
                } else if (postDetails.getMedia().get(0).getMediaType().contains("video")) {
                    if (screamxoPlayer != null && screamxoPlayer.isPlaying()) {
                        return;
                    }
                    View screamxoPlayerView = LayoutInflater.from(context).inflate(R.layout.view_screamxo_player_in_detail, null);
                    ll_container.removeAllViews();
                    ll_container.addView(screamxoPlayerView);
                    ll_container.setVisibility(View.VISIBLE);

                    initScreamxoPlayer(postDetails.getMedia().get(0).getMediaUrl());
                } else {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        return;
                    }

                    releaseMediaPlayer();
                    stopVideoPlayer();

                    View view = LayoutInflater.from(context).inflate(R.layout.audio_view_layout_in_detail, null);
                    // activity layout already contain a bottom views
                    ((ViewGroup) view.findViewById(R.id.audio_bottom_view)).removeAllViews();
                    view.findViewById(R.id.audio_bottom_view).setVisibility(View.GONE);
                    ImageView imageItem = view.findViewById(R.id.img_audio_background);
                    ll_container.setVisibility(View.VISIBLE);
                    ll_container.removeAllViews();
                    ll_container.addView(view);

                    if ((postDetails.getMedia().get(0).getMediaThumb().contains("voice")) || (postDetails.getMedia().get(0).getMediaThumb().contains("audio"))) {

                        if (postDetails.getMedia().get(0).getMediaThumb() != null) {
                            Picasso.with(context)
                                    .load(postDetails.getMedia().get(0).getMediaThumb())
                                    .placeholder(R.drawable.ico_app)
                                    .error(R.drawable.ico_app)
                                    .into(imageItem);
                        } else {
                            Picasso.with(context)
                                    .load(R.drawable.ico_app)
                                    .placeholder(R.drawable.ico_app)
                                    .error(R.drawable.ico_app)
                                    .into(imageItem);
                        }

                    } else {
                        Picasso.with(context)
                                .load(postDetails.getMedia().get(0).getMediaThumb())
                                .placeholder(R.drawable.ico_app)
                                .error(R.drawable.ico_app)
                                .into(imageItem);
                    }

                    setAudioPlayer(postDetails.getMedia().get(0).getMediaUrl(), view, "");
                }
            }
    }

    private void shareItem() {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, (postDetails.getMedia().get(0).getMediaUrl()));
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent, "Send to"));
        } catch (Exception ignored) {

        }
    }

    private void initScreamxoPlayer(String url) {
        screamxoPlayer = new ScreamxoPlayer((AppCompatActivity) context);
        screamxoPlayer.setScaleType(ScreamxoPlayer.SCALETYPE_FITXY);
        screamxoPlayer.play(url);
        screamxoPlayer.setScreamxoPlayerClickListener(this);
        screamxoPlayer.onComplete(() -> {
            screamxoPlayer.seekTo(0, true);
            screamxoPlayer.start();
        });
    }

    @SuppressLint("SetTextI18n")
    @OnClick({R.id.img_emoji, R.id.img_timer, R.id.emoji_iv})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_emoji:
                addEmojiFragment();
                break;
            case R.id.img_timer:
                FragmentManager fm = getFragmentManager();
                DialogFragments dialogFragment = new DialogFragments(commonMethod);
                dialogFragment.show(fm, "Time Fragment");
                break;
            case R.id.emoji_iv:
                if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                    ((DrawerMainActivity) context).gotoLogin(context);
                } else {
                    try {
                        if (postDetails.getIslike() == 0) {
                            Picasso.with(context)
                                    .load(R.drawable.ico_reaction_new)
                                    .placeholder(R.drawable.ico_reaction_new)
                                    .error(R.drawable.ico_reaction_new)
                                    .into(emoji_iv);
                            txtBookmarkCount.setText("" + (postDetails.getLikecount() + 1));
                            postDetails.setLikecount(postDetails.getLikecount() + 1);
                            postDetails.setIslike(1);
                            isBookmarkCheck = true;
                            setLikeApi(postDetails.getId(), StaticConstant.LIKE);
                        } else {
                            Picasso.with(context)
                                    .load(R.drawable.goodmoodemoticon)
                                    .placeholder(R.drawable.goodmoodemoticon)
                                    .error(R.drawable.goodmoodemoticon)
                                    .into(emoji_iv);
                            txtBookmarkCount.setText("" + (postDetails.getLikecount() - 1));
                            postDetails.setLikecount(postDetails.getLikecount() - 1);
                            postDetails.setIslike(0);
                            isBookmarkCheck = false;
                            setLikeApi(postDetails.getId(), StaticConstant.UNLIKE);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }

                break;
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

    private void addEmojiFragment() {
        View view = this.getCurrentFocus();
        if (frameEmoji.getChildCount() > 0) {
            frameEmoji.removeAllViews();
            imgEmoji.setImageResource(R.drawable.ico_reaction_new);

        } else {
            Fragment fragment = new EmojiLayoutFragment(emojiTextInterface);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_emoji, fragment, "");
            fragmentTransaction.commit();
            imgEmoji.setImageResource(R.drawable.vect_key_board);
            Utils.hideKeyboard(this);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setAudioPlayer(String url, final View view, String audioThum) {
        imgPlay = view.findViewById(R.id.app_video_play);
        ImageView imgBackPlayer = view.findViewById(R.id.app_video_finish);
        ImageView audioViewBackground = view.findViewById(R.id.img_audio_background);
        LinearLayout lnyAudioTitle = view.findViewById(R.id.lny_audio_title);
        LinearLayout lnyPlayerSeekbar = view.findViewById(R.id.lny_player_seek_bar);

        seekBarAudioProgress = view.findViewById(R.id.app_audio_seekBar);
        txtDuration = view.findViewById(R.id.app_audio_currentTime);
        txtEndDuration = view.findViewById(R.id.app_audio_endTime);

        imgPlay.setEnabled(false);

        audioViewBackground.setOnClickListener(view1 -> {
            if (lnyAudioTitle.getVisibility() == View.VISIBLE) {
                lnyAudioTitle.setVisibility(View.INVISIBLE);
                lnyPlayerSeekbar.setVisibility(View.INVISIBLE);
            } else {
                lnyAudioTitle.setVisibility(View.VISIBLE);
                lnyPlayerSeekbar.setVisibility(View.VISIBLE);
            }
        });

        view.findViewById(R.id.img_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                    ((DrawerMainActivity) context).gotoLogin(context);
                } else {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, (postDetails.getMedia().get(0).getMediaUrl()));
                    sendIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(sendIntent, "Send to"));
                }
            }
        });

        imgBackPlayer.setOnClickListener(v -> {
            if (!isPortrait) {
                ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return;
            }
            releaseMediaPlayer();
            finish();
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

        mMediaPlayer = new MediaPlayer();

        try {
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int result = am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

            if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == result) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                updateSeekBarTimerTask();

                try {
                    mMediaPlayer.setDataSource(url);
                    mMediaPlayer.prepareAsync();
                    txtDuration.setText(getString(R.string.txt_loading));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mMediaPlayer.setOnPreparedListener(mp -> {

                    try {
                        mMediaPlayer.start();

                        totalMediaPlayerDuration = mp.getDuration();
                        long currentDuration = mp.getCurrentPosition();

                        txtEndDuration.setText(Utils.milliSecondsToTimer(totalMediaPlayerDuration));
                        txtDuration.setText(Utils.milliSecondsToTimer(currentDuration));

                        imgPlay.setEnabled(true);
                        imgPlay.setImageResource(R.drawable.ic_stop_white_24dp);

                        seekBarAudioProgress.setMax(totalMediaPlayerDuration);
                    } catch (Exception e) {
                        e.printStackTrace();
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

                mMediaPlayer.setOnCompletionListener(mp -> {
                    try {
                        sMediaPlayerLastPosition = 0;
                        seekBarAudioProgress.setProgress(0);
                        imgPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);

                        long currentDuration = 0; // in case of the completion...

                        txtDuration.setText(Utils.milliSecondsToTimer(currentDuration));
                        txtEndDuration.setText(Utils.milliSecondsToTimer(totalMediaPlayerDuration));

                        setAudioPlayer(url, view, audioThum);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSeekBarTimerTask() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (textLenght > charSequence.length()) {
            deleteEmoji();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        textLenght = editable.length();
    }

    @Override
    public void getEmojiText(String emojiText) {
        String s = edtMsg.getText().toString().concat(emojiText);
        edtMsg.setText(s);
        edtMsg.setSelection(s.length());
    }

    @Override
    public void commonMethod(String type, File... files) {

    }

    private void stopVideoPlayer() {
        if (screamxoPlayer != null) {
            screamxoPlayer.stop();
        }
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            sMediaPlayerLastPosition = 0;
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void onDestroy() {

        if (sendMsgBeanCall != null) {
            sendMsgBeanCall.cancel();
        }
        if (streampostcall != null) {
            streampostcall.cancel();
        }
        if (deleteMediaItemCall != null) {
            deleteMediaItemCall.cancel();
        }
        if (reportPostCall != null) {
            reportPostCall.cancel();
        }

        releaseMediaPlayer();
        if (screamxoPlayer != null) {
            screamxoPlayer.onDestroy();
        }

        super.onDestroy();
    }

    public void onPause() {
        super.onPause();

        unregisterReceiver(mReceiver);

        if (screamxoPlayer != null) {
            screamxoPlayer.onPause();
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            imgPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            sMediaPlayerLastPosition = mMediaPlayer.getCurrentPosition();
        }
    }

    public void onResume() {
        super.onResume();

        registerReceiver(mReceiver, new IntentFilter(ACTION_COMMENT_MEDIA));

        if (screamxoPlayer != null) {
            screamxoPlayer.onResume();
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            imgPlay.setImageResource(R.drawable.ic_stop_white_24dp);
            mMediaPlayer.seekTo(sMediaPlayerLastPosition);
        }
    }

    @Override
    public void onBackArrowClicked() {
        releaseMediaPlayer();
        stopVideoPlayer();
        finish();
    }

    @Override
    public void onShareButtonClicked() {
        shareItem();
    }

    @Override
    public void onFullScreenBtnClicked() {
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        try {
            isPortrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;

            int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
            int widthPixels = context.getResources().getDisplayMetrics().widthPixels;

            if (screamxoPlayer != null) {
                screamxoPlayer.onConfigurationChanged(newConfig);
            }

            if (isPortrait) {
                ll_container.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, Utils.dip2pixel(context, 220)));
                rl_Bottom.setVisibility(View.VISIBLE);
                rl_footer.setVisibility(View.VISIBLE);
            } else {
                ll_container.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, heightPixels));
                rl_Bottom.setVisibility(View.GONE);
                rl_footer.setVisibility(View.GONE);
            }

            if (photoDialog != null) {
                photoDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        releaseMediaPlayer();
        stopVideoPlayer();
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Emojix.wrap(newBase, new OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
            }
        }));
    }
}
