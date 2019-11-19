package com.screamxo.Activity;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.Comment;
import com.example.apimodule.ApiBase.ApiBean.CommentBean;
import com.example.apimodule.ApiBase.ApiBean.CommentResult;
import com.example.apimodule.ApiBase.ApiBean.Friend;
import com.example.apimodule.ApiBase.ApiBean.FriendBean;
import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.RahulWork.UploadDataActivity;
import com.screamxo.Activity.wallet.WalletSendReceiveActivity;
import com.screamxo.Adapter.CommentAdapter;
import com.screamxo.Adapter.FriendAdapter;
import com.screamxo.Emoji.EmojiLayoutFragment;
import com.screamxo.Emoji.Emojix;
import com.screamxo.Emoji.tagging.OnHashTagClickListener;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Interface.EmojiTextInterface;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.DialogFragments;
import com.screamxo.Utils.HidingScrollListener;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
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

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_WALLET_ACTIVITY_RESULTS;
import static com.screamxo.FireBasePush.MyFirebaseMessagingService.ACTION_COMMENT_POST;
import static com.screamxo.Utils.StaticConstant.LIMIT;
import static com.screamxo.Utils.StaticConstant.STATUS_1;

@SuppressWarnings("unchecked")
public class PostDetailsActivity extends AppCompatActivity implements CommonMethod, TextWatcher, EmojiTextInterface, DialogBox.OnStreamPostOptionClickListener {

    private static final String TAG = "PostDetailsActivity";
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.img_pop_up)
    ImageView imgPopUp;
    @BindView(R.id.img_arrow)
    ImageView imgArrow;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.img_send)
    ImageView imgSend;
    @BindView(R.id.edt_comment)
    EditText edtCommet;
    @BindView(R.id.linear_comment)
    LinearLayout linearComment;
    @BindView(R.id.activity_post_details)
    RelativeLayout activityPostDetails;
    @BindView(R.id.progreessbar)
    ProgressBar progressBar;
    @BindView(R.id.frame_emoji)
    FrameLayout frameEmoji;
    @BindView(R.id.img_emoji)
    ImageView imgEmoji;
    @BindView(R.id.user_iv)
    ImageView user_iv;

    @BindView(R.id.lny_emoji_msg)
    LinearLayout lnyEmojiMsg;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rc_Friends)
    RecyclerView rc_Friends;
    CommentResult commentBean;
    ArrayList<Comment> comments;
    Preferences preferences;
    int likeCount, islike;
    String limit = StaticConstant.LIMIT;

    ArrayList<Friend> searchFrdList;
    ArrayList<Object> frdList, friends;
    int[] mSelection = new int[2];
    private Context context;
    private CommentAdapter commentAdapter;
    private FetchrServiceBase mService;
    private String postId;
    private int pageCounterForShop = 0;
    private int textLenght = 0;
    private HashMap<String, String> map;
    private FriendAdapter friendAdapter;
    private ArrayList<int[]> mUserList = new ArrayList<>();
    private ArrayList<int[]> mHashTagList = new ArrayList<>();

    Call<FriendBean> friendBeanCall;
    private Call<CommentBean> deletePostCall;
    private Call<CommentBean> reportPostCall;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        ButterKnife.bind(this);
        init();
        initControlValue();
        initFabIcon();
    }

    private void init() {
        mUserList = new ArrayList<>();
        mHashTagList = new ArrayList<>();
        friends = new ArrayList<>();
        context = this;
        searchFrdList = new ArrayList<>();
        comments = new ArrayList<>();
        mService = new FetchrServiceBase();
        preferences = new Preferences(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.addOnScrollListener(new HidingScrollListener() {
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

            }
        });

        map = new HashMap<>();
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("postId")) {
            postId = getIntent().getExtras().getString("postId");
        }
        toolbar.setVisibility(View.GONE);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, " onRecieve");
                Utils.printIntentData(intent);
                pageCounterForShop = 0;
                callGetPostDetailApi();
            }
        };
        edtCommet.addTextChangedListener(this);

        edtCommet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    if (edtCommet.getText().toString().equals("")) {
                        Utils.showToast(context, "Enter Comment");
                    } else {
                        imgSend.setEnabled(false);
                        callGiveCommnetApi();
                    }
                    return true;
                }
                return false;
            }
        });

        linearLayoutManager = new LinearLayoutManager(context);
        rc_Friends.setLayoutManager(linearLayoutManager);
    }

    private void initFabIcon() {
        Log.d(TAG, "initFabIcon: ");
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
                    Log.d(TAG, "onMenuOpened: ");
                    Utils.hideKeyboard(PostDetailsActivity.this);
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
                    Log.d(TAG, "floatingButton.setOnClickBtnListener: ");
                    if (!floatingButton.isMenuOpen()
                            && floatingButton.getBackground().getConstantState() != null
                            && floatingButton.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ic_float_menu_up).getConstantState())) {
                        recyclerView.scrollToPosition(0);
                        floatingButton.closeMenu();
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
                    Intent gotoNext = new Intent(PostDetailsActivity.this, UploadDataActivity.class);
                    startActivityForResult(gotoNext, REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS);
                }
            });

            sbWorld = findViewById(R.id.sbWorld);
            sbWorld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    startActivityForResult(new Intent(PostDetailsActivity.this, WalletSendReceiveActivity.class), REQ_CODE_WALLET_ACTIVITY_RESULTS);
                }
            });

            sbSearch = findViewById(R.id.sbSearch);
            sbSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_options));
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    if (commentBean != null) {
                        DialogBox.showStreamPostOption(PostDetailsActivity.this, commentBean.getUserid());
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
                    returnIntent.putExtra("FRAGMENT_INDEX", 2);
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
            Log.e(TAG, "initFabIcon: ", e);
        }
    }

    public ArrayList getSpans(String body, char prefix) {
        Log.d(TAG, "getSpans: ");
        ArrayList spans = new ArrayList();
        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);
        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }
        return spans;
    }

    private void initControlValue() {
        toolbar.setVisibility(View.VISIBLE);
        txtToolbarTitle.setText("");
        imgToolbarLeftIcon.setOnClickListener(view -> finish());
        imgToolbarRightIcon.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(preferences.getUserProfile()))
            Picasso.with(context)
                    .load(preferences.getUserProfile())
                    .placeholder(R.mipmap.user)
                    .fit()
                    .error(R.mipmap.user)
                    .transform(new CircleTransform())
                    .into(user_iv);
        else {
            Picasso.with(context)
                    .load(R.mipmap.user)
                    .placeholder(R.mipmap.user)
                    .fit()
                    .error(R.mipmap.user)
                    .transform(new CircleTransform())
                    .into(user_iv);
        }
        callGetPostDetailApi();
    }

    private void setAdapter() {
        Collections.reverse(comments);
        if (commentAdapter == null) {
            commentAdapter = new CommentAdapter(context, commentBean, comments, this);
            recyclerView.setAdapter(commentAdapter);
        } else {
            commentAdapter.notifyDataSetChanged();
        }
    }

    void callGetPostDetailApi() {
        Log.d(TAG, "callGetPostDetailApi: ");
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("postid", "" + postId);
        map.put("limit", "50");
        map.put("offset", "" + pageCounterForShop);

        if (Utils.isInternetOn(context)) {
            progressBar.setVisibility(View.VISIBLE);
            Call<CommentBean> commentBeanCall = mService.getFetcherService(context).PostDetails(map);

            commentBeanCall.enqueue(new Callback<CommentBean>() {
                @Override
                public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            commentBean = response.body().getResult();
                            //if (pageCounterForShop == 0) {
                            comments.clear();
                            // }
                            comments.addAll(commentBean.getComments());
                            // Collections.reverse(comments);
                            pageCounterForShop++;
                            setAdapter();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentBean> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    t.printStackTrace();
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    void callGetMoreCommentApi() {
        Log.d(TAG, "callGetMoreCommentApi: ");
        Map<String, String> map = new HashMap<>();
//        map.put("uid", preferences.getUserId());
        map.put("postid", "" + postId);
        map.put("limit", LIMIT);
        map.put("offset", "" + pageCounterForShop);

        if (Utils.isInternetOn(context)) {
            progressBar.setVisibility(View.VISIBLE);
            Call<CommentBean> commentBeanCall = mService.getFetcherService(context).MoreComment(map);

            commentBeanCall.enqueue(new Callback<CommentBean>() {
                @Override
                public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            //  if (pageCounterForShop == 0) {
                            comments.clear();
                            // }
                            // Collections.reverse(response.body().getResult().getComments());
                            comments.addAll(response.body().getResult().getComments());
                            pageCounterForShop++;
                            setAdapter();
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentBean> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }

    }

    @OnClick({R.id.img_toolbar_left_icon, R.id.img_send, R.id.img_emoji})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_toolbar_left_icon:
                onBackPressed();
                break;
            case R.id.img_send:
                FragmentManager fm = getFragmentManager();
                DialogFragments dialogFragment = new DialogFragments(this);
                dialogFragment.show(fm, "Time Fragment");
                break;
            case R.id.img_emoji:
                addEmojiFragment();
                break;
        }
    }

    /*todo update create comment api whereever exist and update keys..*/
    private void callGiveCommnetApi() {
        String commentDesc = edtCommet.getText().toString();
        edtCommet.setText("");
        Utils.hideKeyboard(this);
        Map<String, String> map = new HashMap<>();
        map.put("postid", "" + postId);
        map.put("commentby", "" + preferences.getUserId());
        map.put("commentdesc", commentDesc);

        if (Utils.isInternetOn(context)) {
            progressBar.setVisibility(View.VISIBLE);
            Call<LoginBean> commnetApiCall = mService.getFetcherService(context).PostCommet(map);
            commnetApiCall.enqueue(new Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                    imgSend.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(STATUS_1)) {
                            commentBean.setCommentcount(commentBean.getCommentcount() + 1);
                            Comment co = new Comment();
                            Date date = new Date();

                            @SuppressLint("SimpleDateFormat") SimpleDateFormat format_before = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            format_before.setTimeZone(TimeZone.getTimeZone("UTC"));
                            format_before.format(date);

                            String[] userName = preferences.getUserFullName().split(" ");
                            co.setUsername(preferences.getUserName());
                            co.setFname(userName[0]);
                            co.setLname("");
                            co.setCommentid(response.body().getResult().getCommnetId());
                            co.setCommentdesc(commentDesc);
                            co.setCommenttime(format_before.format(date));
                            co.setUserphoto(preferences.getUserProfile());
                            co.setUserid(preferences.getUserId());
                            Collections.reverse(comments);
                            comments.add(co);
                            setAdapter();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {
                    imgSend.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void onBackPressed() {
        if (BundleUtils.getIntentExtra(getIntent(), "from_notification", false)) {
            Intent gotoHome = new Intent(context, DrawerMainActivity.class);
            startActivity(gotoHome);
            return;
        }
        finish();
    }

    @Override
    public void commonMethod(String type, File... files) {
        // 0 = like , 1 = Unlike 2 = Like APi call 4 = click view more Comment text  5 = delete Comment from adapter 6 = Delete Stream Post successfully
        switch (type) {
            case "0":
                islike = 1;
                likeCount = commentBean.getLikecount() + 1;
                commentBean.setLikecount(likeCount);
                setAdapter();
                break;

            case "1":
                islike = 0;
                likeCount = commentBean.getLikecount() - 1;
                commentBean.setLikecount(likeCount);
                setAdapter();
                break;

            case "2":
                break;

            case "4":
                callGetMoreCommentApi();
                break;
            case "5":
                commentBean.setCommentcount(commentBean.getCommentcount() - 1);
                commentAdapter.notifyDataSetChanged();
                break;
            case "6":
                onBackPressed();
                break;

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Emojix.wrap(newBase, new OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                Log.d(TAG, "onHashTagClicked: ");
            }
        }));
    }

    private void addEmojiFragment() {
        View view = this.getCurrentFocus();
        if (frameEmoji.getChildCount() > 0) {
            frameEmoji.removeAllViews();
            imgEmoji.setImageResource(R.mipmap.ico_xo_like_copy);

        } else {
            Fragment fragment = new EmojiLayoutFragment(this);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_emoji, fragment, "");
            fragmentTransaction.commit();
            imgEmoji.setImageResource(R.drawable.vect_key_board);
            Utils.hideKeyboard(this);
        }
    }

    private void deleteEmoji() {
        Log.d(TAG, "deleteEmoji: ");
        String text = edtCommet.getText().toString();

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
        edtCommet.setText(s);
        edtCommet.setSelection(s.length());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult requestCode: " + requestCode);
        Log.d(TAG, "onActivityResult resultCode: " + resultCode);

        if (resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("FRAGMENT_INDEX")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", data.getExtras().getInt("FRAGMENT_INDEX"));
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    @Override
    public void onDeletePost() {
        Log.d(TAG, "onDeletePost: ");
        callDeleteApi(commentBean.getUserid(), commentBean.getId(), Integer.parseInt(commentBean.getPostType()));
    }

    @Override
    public void onSharePost() {
        Log.d(TAG, "onSharePost: ");
        Utils.shareText(context, commentBean.getPostTitle(), "Send to");
    }

    @Override
    public void onReportPost() {
        Log.d(TAG, "onReportPost: ");
        callReportPostApi(commentBean.getUserid(), commentBean.getId());
    }

    private void callDeleteApi(int uId, int postId, int postType) {
        Log.d(TAG, "callDeleteApi: ");
        Map<String, Integer> map = new HashMap<>();
        map.put("uid", uId);
        map.put("postid", postId);
        map.put("posttype", postType);
        if (Utils.isInternetOn(context)) {
            deletePostCall = mService.getFetcherService(context).DeleteStream(map);
            deletePostCall.enqueue(new retrofit2.Callback<CommentBean>() {
                @Override
                public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            commonMethod("6");
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

    public void callReportPostApi(int uId, int postId) {
        Map<String, Integer> map = new HashMap<>();
        map.put("uid", uId);
        map.put("postid", postId);

        if (Utils.isInternetOn(context)) {
            reportPostCall = mService.getFetcherService(context).ReportComment(map);
            reportPostCall.enqueue(new retrofit2.Callback<CommentBean>() {
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

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(ACTION_COMMENT_POST));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (deletePostCall != null) {
            deletePostCall.cancel();
        }

        if (reportPostCall != null) {
            reportPostCall.cancel();
        }
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
        String s = edtCommet.getText().toString().concat(emojiText);
        edtCommet.setText(s);
        edtCommet.setSelection(s.length());
    }
}
