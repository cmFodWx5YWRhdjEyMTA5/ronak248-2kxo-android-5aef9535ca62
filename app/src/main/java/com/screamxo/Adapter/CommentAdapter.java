package com.screamxo.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.Comment;
import com.example.apimodule.ApiBase.ApiBean.CommentBean;
import com.example.apimodule.ApiBase.ApiBean.CommentResult;
import com.example.apimodule.ApiBase.ApiBean.GetUserDetailBean;
import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.PostDetailsActivity;
import com.screamxo.Activity.ProfileActivity;
import com.screamxo.Activity.RahulWork.WebViewActivity;
import com.screamxo.Emoji.CustomText;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.Validations;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    public CommentAdapter(Context context, CommentResult streampost, ArrayList<Comment> comments, CommonMethod commonMethod) {
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stream_comment, parent, false);
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


            if (streampost.getCommentcount() <= 0)
                ((HeaderView) holder).img_comment.setImageResource(R.drawable.ico_comment_new_grey);


            if (streampost.getMedia().size() != 0 && !streampost.getMedia().get(0).getMediaUrl().equals("") && streampost.getMedia().get(0).getMediaType().contains("image")) {

                ImageView imgPhoto = new ImageView(context);

                ((HeaderView) holder).lnyComtainer.setVisibility(View.VISIBLE);
                ((HeaderView) holder).lnyComtainer.removeAllViews();
                ((HeaderView) holder).lnyComtainer.addView(imgPhoto);

                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int width = displaymetrics.widthPixels;
                int height = (int) context.getResources().getDimension(R.dimen._130sdp);

                Picasso.with(context)
                        .load(streampost.getMedia().get(0).getMediaUrl())
                        .placeholder(R.mipmap.img_placeholder).centerCrop()
                        .resize(width, height)
                        .error(R.mipmap.img_placeholder)
                        .into(imgPhoto);
            }
            ((HeaderView) holder).mtxtMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context, ((HeaderView) holder).mtxtMore);
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
                                        callDeleteApi(streampost.getUserid(), streampost.getId(), Integer.parseInt(streampost.getPostType()));
                                    } else {
                                        ((PostDetailsActivity) context).callReportPostApi(streampost.getUserid(), streampost.getId());
                                    }
                                    break;

                                case R.id.pop_up_share:
                                    Utils.shareText(context, streampost.getPostTitle(), "Send to");
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

//            if (streampost.getUserid().toString().equals(preferences.getUserId()))
//                ((CommentHolder) holder).txtCommentMore.setVisibility(View.VISIBLE);
//            else
//                ((CommentHolder) holder).txtCommentMore.setVisibility(View.GONE);

            if (position == 1 && streampost.getCommentcount() > comments.size()) {
                ((CommentHolder) holder).txtMoreComment.setVisibility(View.VISIBLE);
            } else {
                ((CommentHolder) holder).txtMoreComment.setVisibility(View.GONE);
            }

//            ((CommentHolder) holder).txtCommentMore.setOnClickListener(v -> {
//                PopupMenu popup = new PopupMenu(context, ((CommentHolder) holder).txtCommentMore);
//                popup.getMenuInflater().inflate(R.menu.menu_block_delete, popup.getMenu());
//
////                if (preferences.getUserId().equals("" + streampost.getUserid())) {
////                    popup.getMenu().getItem(0).setTitle(context.getString(R.string.txt_delete));
////                } else {
////                    popup.getMenu().getItem(0).setTitle(context.getString(R.string.txt_report_post));
////                }
//
//                popup.setOnMenuItemClickListener(item -> {
//                    positionSelect = position;
//                    switch (item.getItemId()) {
//                        case R.id.pop_up_delete:
//                            callDeleteCommentApi(streampost.getUserid(), Integer.parseInt(comment.getCommentid()), streampost.getId());
//                            break;
//
//                        case R.id.pop_up_block:
//                            callBlockApi(comment.getUserid());
//                            break;
//                    }
//                    return true;
//                });
//                popup.show();
//            });
            ((CommentHolder) holder).txtMoreComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (streampost.getCommentcount() > comments.size()) {
                        commonMethod.commonMethod("4");
                    }
                }
            });
            ((CommentHolder) holder).txtUserName.setText(TextUtils.isEmpty(comment.getUsername()) ? comment.getFname() : comment.getUsername());
            ((CommentHolder) holder).txtCommnet.setText(comment.getCommentdesc());

            String date = validations.dateFormationUTCtoLocal("yyyy-MM-dd'T'HH:mm:ss", comment.getCommenttime().replace(" ", "T"));
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

                    popup.getMenuInflater().inflate(R.menu.menu_select_privacy, popup.getMenu());

                    if (preferences.getUserId().equals("" + comment.getUserid()) || preferences.getUserId().equals("" + streampost.getUserid())) {
                        popup.getMenu().getItem(0).setTitle("Copy");
                        popup.getMenu().getItem(1).setTitle("Delete");
                        popup.getMenu().getItem(2).setTitle("Block");

                    } else {
                        popup.getMenu().getItem(0).setTitle("Copy");
                        popup.getMenu().getItem(1).setVisible(false);
                        popup.getMenu().getItem(2).setVisible(false);
                    }
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.pop_up_public:
                                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("Text Copied", comment.getCommentdesc());
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(context, "Text copied", Toast.LENGTH_SHORT).show();
                                    break;

                                case R.id.pop_up_friends:
                                    callDeleteCommentApi(streampost.getUserid(), Integer.parseInt(comment.getCommentid()), streampost.getId());
                                    break;

                                case R.id.pop_up_private:
                                    callBlockApi(comment.getUserid());
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
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            commonMethod.commonMethod("6");
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

    public void callBlockApi(String toId) {
        Map<String, String> map = new HashMap<>();
        map.put("fromid", preferences.getUserId());
        map.put("toid", toId);

        if (Utils.isInternetOn(context)) {

            mService.getFetcherService(context).BlockUser(map).enqueue(new Callback<GetUserDetailBean>() {
                @Override
                public void onResponse(Call<GetUserDetailBean> call, Response<GetUserDetailBean> response) {
//                    if (response.code() == StaticConstant.RESULT_OK) {
                    Utils.showToast(context, "Blocked Successfully");
//                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
//                            finish();
//                        }

//                    if (fragment instanceof ProfileFragment)
//                        ((ProfileFragment) fragment).isBlock = 1;
//                    }
                }

                @Override
                public void onFailure(Call<GetUserDetailBean> call, Throwable t) {
                    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callDeleteCommentApi(int uId, int commentid, int postId) {
        Map<String, Integer> map = new HashMap<>();

//        map.put("uid", uId);
        map.put("commentId", commentid);
        map.put("postid", postId);

        if (Utils.isInternetOn(context)) {
            commentBeanCall = mService.getFetcherService(context).DeleteComment(map);
            commentBeanCall.enqueue(new retrofit2.Callback<CommentBean>() {
                @Override
                public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {

                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {

                            comments.remove(positionSelect - 1);
                            commonMethod.commonMethod("5");
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
//            txtCommentMore = v.findViewById(R.id.txt__comment_more);
        }
    }

    private class HeaderView extends RecyclerView.ViewHolder {
        ImageView mimgUserProfile;
        ImageView img_like, img_comment;
        TextView mtxtUserName, mtxtTime, mtxtCommnetCount, mtxtMore;
        TextView tv_like_count;
        CustomText mtxtComment;
        LinearLayout lnyComtainer;
        RelativeLayout relativeLayout;
        FrameLayout user_image_container;

        @SuppressLint("SetTextI18n")
        HeaderView(View v) {
            super(v);

            validations = new Validations();
            //  user_image_container = v.findViewById(R.id.user_image_container);
            mimgUserProfile = v.findViewById(R.id.ico_user_profile);
            mtxtUserName = v.findViewById(R.id.txt_user_name);
            mtxtComment = v.findViewById(R.id.txt_comment);
            mtxtTime = v.findViewById(R.id.txt_time);
            tv_like_count = v.findViewById(R.id.txt_like);
//            mtxtLike = (TextView) v.findViewById(R.id.txt_like);
            img_like = v.findViewById(R.id.emoji_iv);
            img_comment = v.findViewById(R.id.img_comment);
            mtxtCommnetCount = v.findViewById(R.id.txt_comment_count);
            mtxtMore = v.findViewById(R.id.txt_more);
            lnyComtainer = v.findViewById(R.id.lny_container);
            relativeLayout = v.findViewById(R.id.relative_first_child);

            if (streampost != null) {
//                mtxtUserName.setText(streampost.getFname() + " " + streampost.getLname());
                mtxtUserName.setText(TextUtils.isEmpty(streampost.getUsername()) ? streampost.getFname() : streampost.getUsername());

                mtxtComment.setText(streampost.getPostTitle());
                if (URLUtil.isValidUrl(streampost.getPostTitle()))
                    mtxtComment.setTextColor(Color.parseColor("#FF4F67"));
            }
            mtxtComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (URLUtil.isValidUrl(streampost.getPostTitle())) {
                        Intent new_intent = new Intent(context, WebViewActivity.class);
                        new_intent.putExtra("url", streampost.getPostTitle());
                        context.startActivity(new_intent);
                    } else {
                        Utils.showToast(context, "invalid link");
                    }
                    // return false;
                }
            });


            tv_like_count.setText("" + streampost.getLikecount());

            String date = validations.dateFormationUTCtoLocal("yyyy-MM-dd'T'HH:mm:ss", streampost.getUpdatedDate());
            mtxtTime.setText(date);

            if (streampost.getUserphotothumb() != null && !streampost.getUserphotothumb().equals("")) {
                Picasso.with(context)
                        .load(streampost.getUserphotothumb())
                        .placeholder(R.mipmap.user).centerCrop()
                        .error(R.mipmap.user)
                        .fit()
                        .transform(new CircleTransform())
                        .into(mimgUserProfile);
            }
            if (streampost.getIslike() == 0) {
                img_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_emoji_smile_white_bg));
            } else {
                img_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ico_reaction_new));
            }

            img_like.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    if (streampost.getIslike() == 0) {
                        tv_like_count.setText("" + (streampost.getLikecount() + 1));
//                            streampost.setLikecount(streampost.getLikecount() +
                        img_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ico_reaction_new));
                        streampost.setIslike(1);
                        setLikeApi(streampost.getId(), StaticConstant.LIKE);

                        commonMethod.commonMethod("0");
                    } else {
                        tv_like_count.setText("" + (streampost.getLikecount() - 1));
                        img_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_emoji_smile_white_bg));
//                            streampost.setLikecount(streampost.getLikecount() - 1);
                        streampost.setIslike(0);
                        setLikeApi(streampost.getId(), StaticConstant.UNLIKE);
                        commonMethod.commonMethod("1");
                    }
                }
            });

            tv_like_count.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    if (streampost.getIslike() == 0) {
                        tv_like_count.setText("" + (streampost.getLikecount() + 1));
//                            streampost.setLikecount(streampost.getLikecount() +
                        img_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ico_reaction_new));
                        streampost.setIslike(1);
                        setLikeApi(streampost.getId(), StaticConstant.LIKE);

                        commonMethod.commonMethod("0");
                    } else {
                        tv_like_count.setText("" + (streampost.getLikecount() - 1));
                        img_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_emoji_smile_white_bg));
//                            streampost.setLikecount(streampost.getLikecount() - 1);
                        streampost.setIslike(0);
                        setLikeApi(streampost.getId(), StaticConstant.UNLIKE);
                        commonMethod.commonMethod("1");
                    }
                }
            });


            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("uid", "" + streampost.getUserid());
                    bundle.putString("uFullName", streampost.getFname() + " " + streampost.getLname());
                    bundle.putString("username", streampost.getUsername());
                    bundle.putString("uProfile", streampost.getUserphoto());
                    intent.putExtras(bundle);

                    context.startActivity(intent);
                }
            });

        }

    }

}

