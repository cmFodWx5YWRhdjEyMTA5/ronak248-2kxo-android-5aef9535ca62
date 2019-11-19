package com.screamxo.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailMedia;
import com.screamxo.R;
import com.screamxo.Utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shubham Agarwal on 08/02/17.
 */

@SuppressLint("ValidFragment")
public class ImagePagerFragment extends Fragment {
    private static final String TAG = "ImagePagerFragment";
    Context context;
    String path;
    @BindView(R.id.img_rest_item)
    ImageView imgRestItem;

    int height1 = 0;
    int width1 = 0;
    int position;
    Boolean isSoldDisplay;

    ArrayList<ItemDetailMedia> mediaArrayList;
    @BindView(R.id.img_sold)
    ImageView imgSold;
    private Dialog photoDialog;

    public ImagePagerFragment() {
    }

    public ImagePagerFragment(int i, ArrayList<ItemDetailMedia> mediaArrayList, Boolean isSoldDisplay) {
        path = mediaArrayList.get(i).getMediaUrl();
        position = i;
        this.mediaArrayList = mediaArrayList;
        this.isSoldDisplay = isSoldDisplay;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_view_pager, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            try {
                BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.mipmap.sold_out);
                height1 = bd.getBitmap().getHeight();
                width1 = bd.getBitmap().getWidth();
            } catch (Exception e) {
            }

            if (isSoldDisplay) {
                imgSold.setVisibility(View.VISIBLE);
            } else {
                imgSold.setVisibility(View.GONE);
            }

            if (path != null && !path.isEmpty()) {
                imgRestItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        photoDialog = ImageUtils.customChoosePhoto(context, getImages(),position);
                    }
                });

//                Glide.with(imgRestItem).load(path).error(R.mipmap.img_placeholder).placeholder(R.mipmap.img_placeholder);

                Picasso.Builder builder = new Picasso.Builder(context);
                builder.listener(new Picasso.Listener()
                {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                    {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(path).into(imgRestItem);


             /*   Picasso.with(context)
                        .load(path)
                        .error(R.mipmap.img_placeholder)
                        .placeholder(R.mipmap.img_placeholder)
                        .fit()
                        .centerCrop()
                        .into(imgRestItem);*/
            } else {
                Picasso.with(context)
                        .load(R.mipmap.img_placeholder)
                        .into(imgRestItem);
            }
        } catch (Exception ignored) {

        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (photoDialog != null) {
            photoDialog.dismiss();
        }
    }

    private List<String> getImages() {
        List<String> images = new ArrayList<>();
        for (ItemDetailMedia itemDetailMedia : mediaArrayList) {
            images.add(itemDetailMedia.getMediaUrl());
        }
        return images;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }
}
