package com.screamxo.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.apimodule.ApiBase.ApiBean.Itemdetail;
import com.example.apimodule.ApiBase.ApiBean.Mediapost;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.screamxo.Interface.AudioVideoInterface;
import com.screamxo.R;
import com.screamxo.Utils.MyApplication;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ImageVideoAdapter";
    private Context context;
    private final ArrayList<Mediapost> mMediaposts;
    private final ArrayList<Itemdetail> mItemdetails;
    private int width, height;
    private AudioVideoInterface commonMethod;

//    MediaPlayer mediaPlayer = new MediaPlayer();

    public ImageVideoAdapter(Context context, ArrayList<Mediapost> mediaposts, ArrayList<Itemdetail> itemdetails, AudioVideoInterface commonMethod) {
        this.context = context;
        mMediaposts = mediaposts;
        mItemdetails = itemdetails;
        this.commonMethod = commonMethod;
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
            height = bd.getBitmap().getHeight() + 30;
            width = bd.getBitmap().getWidth() + 45;
        } catch (Exception ignored) {
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.audio_video_recycler, parent, false);
        return new ImageVideo(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mItemdetails != null)
        {
            ((ImageVideo) holder).imgPlay.setVisibility(View.GONE);
            Log.e("Position" + position + " id->" + mItemdetails.get(position).getItemId(), "->" + (mItemdetails.get(position).getItemQtyRemained()));
            if (mItemdetails.get(position).getItemQtyRemained() <= 0) {
                ((ImageVideo) holder).imgsoldout.setVisibility(View.VISIBLE);
                Glide.with(context).load(R.mipmap.sold_out).into(((ImageVideo) holder).imgsoldout);
            } else {
                ((ImageVideo) holder).imgsoldout.setVisibility(View.GONE);
            }

            if (mItemdetails.get(position).getMediaUrl() != null && !mItemdetails.get(position).getMediaUrl().isEmpty())
            {

                Glide.with(context)
                        .load(mItemdetails.get(position).getMediaUrl()).placeholder(R.mipmap.img_placeholder).error(R.mipmap.img_placeholder)
                        .apply(new RequestOptions().fitCenter().centerCrop().override(width+35, height-11))
                        .into(((ImageVideo) holder).imgThum);

               /* Picasso.with(context)
                        .load(mItemdetails.get(position).getMediaUrl())
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .resize(width + 35, height - 11)
                        .into(((ImageVideo) holder).imgThum, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Glide.with(context)
                                        .load(mItemdetails.get(position).getMediaUrl())
                                        .apply(new RequestOptions().fitCenter().centerCrop().override(width, height))
                                        .into(((ImageVideo) holder).imgThum);
                            }
                        });*/
            }
            else
                {
                Picasso.with(context)
                        .load(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .into(((ImageVideo) holder).imgThum);
            }

            ((ImageVideo) holder).imgThum.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (mItemdetails.get(position).getMediaType().contains("image") && !mItemdetails.get(position).getMediaUrl().isEmpty()) {
                        commonMethod.Image(mItemdetails.get(position).getMediaUrl(), "" + mItemdetails.get(position).getItemId(), position, "1");
                    }
                }
            });

            if (mItemdetails.get(position).getItemPrice() == null || Utils.getFormattedPrice(mItemdetails.get(position).getItemPrice()) == null) {
                ((ImageVideo) holder).txtPrice.setVisibility(View.GONE);
            } else {
                ((ImageVideo) holder).txtPrice.setWidth(width);
                ((ImageVideo) holder).txtPrice.setText(Utils.getFormattedPrice(mItemdetails.get(position).getItemPrice()));
            }

        } else if (mMediaposts != null) {
            ((ImageVideo) holder).txtPrice.setWidth(width);
            if (mMediaposts.get(position).getPostTitle().isEmpty()) {
                ((ImageVideo) holder).txtPrice.setVisibility(View.GONE);
            } else {
                ((ImageVideo) holder).txtPrice.setVisibility(View.VISIBLE);
            }

            ((ImageVideo) holder).txtPrice.setText((mMediaposts.get(position).getPostTitle().replace("@@:-:@@", ""))
                    .replace("\"", ""));

            if (mMediaposts.get(position).getMediaType().contains("image")) {
                if (mMediaposts.get(position).getMediaUrl() != null && !mMediaposts.get(position).getMediaUrl().isEmpty()) {

                    Glide.with(context)
                            .load(mMediaposts.get(position).getMediaUrl()).placeholder(R.mipmap.img_placeholder).error(R.mipmap.img_placeholder)
                            .apply(new RequestOptions().fitCenter().centerCrop().override(width+35, height-11))
                            .into(((ImageVideo) holder).imgThum);

                }
                else
                    {
                    Picasso.with(context)
                            .load(R.mipmap.img_placeholder)
                            .placeholder(R.mipmap.img_placeholder)
                            .error(R.mipmap.img_placeholder)
                            .resize(width + 35, height - 11)
                            .into(((ImageVideo) holder).imgThum);
                }
            } else {
                if (mMediaposts.get(position).getMediaThumb() != null && !mMediaposts.get(position).getMediaThumb().isEmpty()) {
                    Picasso.with(context)
                            .load(mMediaposts.get(position).getMediaThumb())
                            .placeholder(R.mipmap.img_placeholder)
                            .error(R.mipmap.img_placeholder)
                            .resize(width + 35, height - 11)
                            .into(((ImageVideo) holder).imgThum);
                } else {
                    Picasso.with(context)
                            .load(R.mipmap.img_placeholder)
                            .placeholder(R.mipmap.img_placeholder)
                            .error(R.mipmap.img_placeholder)
                            .resize(width + 35, height - 11)
                            .into(((ImageVideo) holder).imgThum);
                }
            }

            if (mMediaposts.get(position).getPosttype() == 2)
            {
                ((ImageVideo) holder).imgPlay.setVisibility(View.GONE);
            }
            else
                {
                ((ImageVideo) holder).imgPlay.setVisibility(View.VISIBLE);
            }

            if (mMediaposts.get(position).getPosttype() == 1) {
                ((ImageVideo) holder).imgPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ico_player));
            } else if (mMediaposts.get(position).getPosttype() == 3) {
                ((ImageVideo) holder).imgPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ico_voice_black));
            } else if (mMediaposts.get(position).getPosttype() == 4) {
                ((ImageVideo) holder).imgPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ico_play));
            }

            ((ImageVideo) holder).imgThum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.e("Position->" + position + " PostType->" + mMediaposts.get(position).getPosttype(),
                                "Url->" + mMediaposts.get(position).getMediaUrl());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mMediaposts.get(position).getPosttype() == 1 && !mMediaposts.get(position).getMediaUrl().isEmpty()) {
                        commonMethod.Video(mMediaposts.get(position).getMediaUrl(), "0", position);
                    } else if ((mMediaposts.get(position).getPosttype() == 3 || mMediaposts.get(position).getPosttype() == 4) && !mMediaposts.get(position).getMediaUrl().isEmpty()) {
                        commonMethod.Audio(mMediaposts.get(position).getMediaUrl(), mMediaposts.get(position).getMediaThumb(), mMediaposts.get(position).getPostTitle(), "0", position);
                    } else if (mMediaposts.get(position).getPosttype() != 2) {
                        Utils.showToast(context, "Media Url is Empty");
                    } else if (mMediaposts.get(position).getPosttype() == 2 && !mMediaposts.get(position).getMediaUrl().isEmpty()) {
                        commonMethod.Image(mMediaposts.get(position).getMediaUrl(), "" + mMediaposts.get(position).getId(), position, "0");
                    } else {
                        Utils.showToast(context, "Media Url is Empty");
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        if (mMediaposts != null) {
            return mMediaposts.size();
        }

        if (mItemdetails != null) {
            return mItemdetails.size();
        }

        return 0;
    }

    private class ImageVideo extends RecyclerView.ViewHolder
    {

        ImageView imgPlay, imgThum, imgsoldout;
        TextView txtPrice;

        ImageVideo(View v) {
            super(v);
            imgPlay = v.findViewById(R.id.img_play);
            imgThum = v.findViewById(R.id.img_thum);
            imgsoldout = v.findViewById(R.id.img_sold_out);
            txtPrice = v.findViewById(R.id.txtPrice);
        }
    }
}
