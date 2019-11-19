package com.screamxo.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Adapter.MediaDetailAdapter;
import com.screamxo.Emoji.EmojiLayoutFragment;
import com.screamxo.Emoji.Emojix;
import com.screamxo.Emoji.tagging.OnHashTagClickListener;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Interface.EmojiTextInterface;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

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

import static com.screamxo.Utils.StaticConstant.LIMIT;
import static com.screamxo.Utils.StaticConstant.STATUS_1;


/**
 * Don't know where the class has been used under what circumstances... @killer
 */
@Deprecated
public class MediaDetailActivity extends AppCompatActivity implements CommonMethod, EmojiTextInterface, TextWatcher {

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
    @BindView(R.id.lny_emoji_msg)
    LinearLayout lnyEmojiMsg;

    private Context context;
    private Boolean isupdate = false, isDelete = false;
    private MediaDetailAdapter commentAdapter;
    private FetchrServiceBase mService;
    private String postId;
    private int pageCounterForShop = 1;
    CommentResult commentBean;
    ArrayList<Comment> comments;
    Preferences preferences;
    int likeCount, islike;
    private int textLenght = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);
        ButterKnife.bind(this);
        init();
        initControlValue();
    }

    private void init() {
        context = this;
        comments = new ArrayList<>();
        mService = new FetchrServiceBase();
        preferences = new Preferences(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("postId")) {
            postId = getIntent().getExtras().getString("postId");
        }
    }

    private void initControlValue() {
        txtToolbarTitle.setText("");
        imgToolbarRightIcon.setVisibility(View.GONE);
        callGetPostDetailApi();
    }

    private void setAdapter() {
        if (commentAdapter == null) {
            commentAdapter = new MediaDetailAdapter(context, commentBean, comments, this);
            recyclerView.setAdapter(commentAdapter);
        } else {
            commentAdapter.notifyDataSetChanged();
        }
    }

    void callGetPostDetailApi() {
        Map<String, String> map = new HashMap<>();

        map.put("uid", preferences.getUserId());
        map.put("postid", "" + postId);
        map.put("limit", LIMIT);
        map.put("offset", "" + pageCounterForShop);

        if (Utils.isInternetOn(context)) {
            progressBar.setVisibility(View.VISIBLE);
            Call<CommentBean> commentBeanCall = mService.getFetcherService(context).PostDetails(map);

            commentBeanCall.enqueue(new Callback<CommentBean>() {
                @Override
                public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            commentBean = response.body().getResult();
                            if (pageCounterForShop == 1) {
                                comments.clear();
                            }
                            txtToolbarTitle.setText(response.body().getResult().getUsername());
                            comments.addAll(commentBean.getComments());
                            Collections.reverse(comments);
                            pageCounterForShop++;
                            setAdapter();


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

    void callGetMoreCommentApi() {

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
//                            commentBean = response.body().getResult();
                            if (pageCounterForShop == 1) {
                                comments.clear();
                            }


                            Collections.reverse(response.body().getResult().getComments());
                            comments.addAll(0, response.body().getResult().getComments());
//                            comments.addAll(commentBean.getComments());
//                            Collections.reverse(comments);
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
                if (edtCommet.getText().toString().equals("")) {
                    Utils.showToast(context, getString(R.string.txt_enter_comment));
                } else {
                    imgSend.setEnabled(false);
                    callGiveCommnetApi();
                }
                break;
            case R.id.img_emoji:
                addEmojiFragment();
                break;
        }
    }
    private void callGiveCommnetApi() {
        Map<String, String> map = new HashMap<>();

        map.put("postid", "" + postId);
        map.put("commentby", "" + preferences.getUserId());
        map.put("commentdesc", edtCommet.getText().toString());

        if (Utils.isInternetOn(context))
        {
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

                            isupdate = true;
                            commentBean.setCommentcount(commentBean.getCommentcount() + 1);
                            Comment co = new Comment();
                            Date date = new Date();

                            @SuppressLint("SimpleDateFormat") SimpleDateFormat format_before = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            format_before.setTimeZone(TimeZone.getTimeZone("UTC"));
                            format_before.format(date);

                            co.setFname(preferences.getUserFullName());
                            co.setLname("");
                            co.setCommentid(response.body().getResult().getCommnetId());
                            co.setCommentdesc(edtCommet.getText().toString());
                            co.setCommenttime(format_before.format(date));
                            co.setUserphoto(preferences.getUserProfile());
                            co.setUserid(preferences.getUserId());

                            comments.add(co);
//                            comments.add(0,co);
                            setAdapter();
                            edtCommet.setText("");

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
        //  super.onBackPressed();
        HashMap<String, Integer> map = new HashMap<>();
        if (commentBean != null) {
            map.put("comment_count", commentBean.getCommentcount());
        }

        map.put("like_count", likeCount);
        map.put("is_like", islike);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("update", isupdate);
        returnIntent.putExtra("isDelete", isDelete);
        returnIntent.putExtra("map_update", map);
        setResult(RESULT_OK, returnIntent);
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
                isupdate = true;
                break;

            case "4":
                callGetMoreCommentApi();
                break;
            case "5":
                commentBean.setCommentcount(commentBean.getCommentcount() - 1);
                commentAdapter.notifyDataSetChanged();
                break;
            case "6":
                isupdate = true;
                isDelete = true;
                onBackPressed();
                break;

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Emojix.wrap(newBase, new OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {

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

    @Override
    public void getEmojiText(String emojiText) {
        String s = edtCommet.getText().toString().concat(emojiText);
        edtCommet.setText(s);
        edtCommet.setSelection(s.length());
    }

    private void deleteEmoji() {

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
        Log.e("setText " + s, "removeString: " + edtCommet.getText().toString());
        edtCommet.setSelection(s.length());
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
}
