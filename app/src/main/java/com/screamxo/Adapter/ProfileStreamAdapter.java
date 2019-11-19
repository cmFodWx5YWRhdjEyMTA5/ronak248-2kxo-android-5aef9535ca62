package com.screamxo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.CommentBean;
import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.ApiBean.Mediapost;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Activity.PostDetailsActivity;
import com.screamxo.Activity.RahulWork.WebViewActivity;
import com.screamxo.R;
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

public class ProfileStreamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Object> result;
    private int width, height;
    private Preferences preferences;
    private FetchrServiceBase mService;
    private int positionSelect;

    public ProfileStreamAdapter(Context context, ArrayList<Object> result) {
        this.context = context;
        this.result = result;

        preferences = new Preferences(context);
        mService = new FetchrServiceBase();
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_stream_comment_new, parent, false);
        return new StreamViewBottom(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Mediapost bean = (Mediapost) result.get(position);
        ((StreamViewBottom) holder).mtxtUserName.setText(bean.getFname() + " " + bean.getLname());
        ((StreamViewBottom) holder).mtxtComment.setText(bean.getPostTitle());

        if (URLUtil.isValidUrl(bean.getPostTitle())) {
            ((StreamViewBottom) holder).mtxtComment.setTextColor(Color.parseColor("#FF4F67"));
        }
        ((StreamViewBottom) holder).mtxtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (URLUtil.isValidUrl(bean.getPostTitle())) {
                    Intent new_intent = new Intent(context, WebViewActivity.class);
                    new_intent.putExtra("url", bean.getPostTitle());
                    context.startActivity(new_intent);
                } else {
                    Utils.showToast(context, "invalid link");
                }
                //return false;
            }
        });

//        ((StreamViewBottom) holder).mtxtComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (URLUtil.isValidUrl(bean.getPostTitle())) {
//                    Intent new_intent = new Intent(context, WebViewActivity.class);
//                    new_intent.putExtra("url",bean.getPostTitle());
//                    context.startActivity(new_intent);
//                                   }
//                                   else {
//                    Utils.showToast(context, "invalid link");
//                }
//            }
//        });

        ((StreamViewBottom) holder).mtxtLike.setText("" + bean.getLikecount());
        ((StreamViewBottom) holder).mtxtCommnetCount.setText("" + bean.getCommentcount());
        if (bean.getCommentcount() <= 0)
            ((StreamViewBottom) holder).imgComment.setImageResource(R.drawable.ico_comment_new_grey);
        else
            ((StreamViewBottom) holder).imgComment.setImageResource(R.drawable.ico_comment_new);


        Validations validations = new Validations();
        String date = validations.dateFormationUTCtoLocal("yyyy-MM-dd'T'HH:mm:ss", bean.getUpdatedDate());
        ((StreamViewBottom) holder).mtxtTime.setText(date);

        if (bean.getIslike() == 0) {
            ((StreamViewBottom) holder).emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
        } else {
            ((StreamViewBottom) holder).emoji_iv.setImageResource(R.drawable.ico_reaction_new);
        }

        if (bean.getUserphoto() != null && !bean.getUserphoto().equals("")) {
            Picasso.with(context)
                    .load(bean.getUserphoto())
                    .placeholder(R.mipmap.user)
                    .error(R.mipmap.user)
                    .resize(width, height)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(((StreamViewBottom) holder).user_iv);
        }

        ((StreamViewBottom) holder).mtxtLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                    ((DrawerMainActivity) context).gotoLogin(context);
                } else {
                    if (bean.getIslike() == 0) {
                        ((StreamViewBottom) holder).emoji_iv.setImageResource(R.drawable.ico_reaction_new);
                        ((StreamViewBottom) holder).mtxtLike.setText("" + (bean.getLikecount() + 1));
                        bean.setLikecount(bean.getLikecount() + 1);
                        bean.setIslike(1);
                        setLikeApi(bean.getId(), StaticConstant.LIKE);
                    } else {
                        ((StreamViewBottom) holder).emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
                        ((StreamViewBottom) holder).mtxtLike.setText("" + (bean.getLikecount() - 1));
                        bean.setLikecount(bean.getLikecount() - 1);
                        bean.setIslike(0);
                        setLikeApi(bean.getId(), StaticConstant.UNLIKE);
                    }
                }
            }
        });

        ((StreamViewBottom) holder).emoji_iv.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (preferences.getUserId().isEmpty() || preferences.getStripeCustomerId().isEmpty()) {
                    ((DrawerMainActivity) context).gotoLogin(context);
                } else {
                    if (bean.getIslike() == 0) {
                        ((StreamViewBottom) holder).emoji_iv.setImageResource(R.drawable.ico_reaction_new);
                        ((StreamViewBottom) holder).mtxtLike.setText("" + (bean.getLikecount() + 1));
                        bean.setLikecount(bean.getLikecount() + 1);
                        bean.setIslike(1);
                        setLikeApi(bean.getId(), StaticConstant.LIKE);
                    } else {
                        ((StreamViewBottom) holder).emoji_iv.setImageResource(R.drawable.ic_emoji_smile_white_bg);
                        ((StreamViewBottom) holder).mtxtLike.setText("" + (bean.getLikecount() - 1));
                        bean.setLikecount(bean.getLikecount() - 1);
                        bean.setIslike(0);
                        setLikeApi(bean.getId(), StaticConstant.UNLIKE);
                    }
                }
            }
        });

        ((StreamViewBottom) holder).mtxtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, ((StreamViewBottom) holder).mtxtMore);
                popup.getMenuInflater().inflate(R.menu.menu_share_delete, popup.getMenu());

                if (bean.getMypost() == 1) {
                    popup.getMenu().getItem(0).setTitle(context.getString(R.string.txt_delete_post));
                } else {
                    popup.getMenu().getItem(0).setTitle(context.getString(R.string.txt_report_post));
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        positionSelect = position;
                        switch (item.getItemId()) {
                            case R.id.pop_up_detele:
                                if (bean.getMypost() == 1) {
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

        ((StreamViewBottom) holder).mtxtCommnetCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("postId", "" + bean.getId());
                context.startActivity(intent);
            }
        });

        ((StreamViewBottom) holder).imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("postId", "" + bean.getId());
                context.startActivity(intent);
            }
        });

        /*((StreamViewBottom) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("postId", "" + bean.getId());
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    private class StreamViewBottom extends RecyclerView.ViewHolder {
        ImageView user_iv, imgComment;
        ImageView emoji_iv;
        FrameLayout user_image_container;
        //        ImageView mimgUserProfile;
        TextView mtxtUserName, mtxtComment, mtxtTime, mtxtLike, mtxtCommnetCount, mtxtMore;

        StreamViewBottom(View v) {
            super(v);
//            mimgUserProfile = (ImageView) v.findViewById(R.id.img_user_profile);
            // user_image_container = v.findViewById(R.id.user_image_container);
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

    private void callDeleteApi(int uId, int postId, int postType) {
        Map<String, Integer> map = new HashMap<>();
        map.put("uid", uId);
        map.put("postid", postId);
        map.put("posttype", postType);
        if (Utils.isInternetOn(context)) {
            Call<CommentBean> commentBeanCall = mService.getFetcherService(context).DeleteStream(map);

            commentBeanCall.enqueue(new retrofit2.Callback<CommentBean>() {
                @Override
                public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {

                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            result.remove(positionSelect);
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

            Call<CommentBean> commentBeanCall = mService.getFetcherService(context).ReportComment(map);

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
}
