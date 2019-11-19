package com.screamxo.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.screamxo.Fragment.SellItemFirstFragment;
import com.screamxo.R;
import com.screamxo.Utils.RoundCornerTransform;
import com.screamxo.Utils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Shubham.Agarwal on 11/11/16.
 */

public class SellItemAdapter extends RecyclerView.Adapter<SellItemAdapter.ImageHolder> {
    private static final String TAG = "SellItemAdapter";
    File[] imageFiles;
    private Context context;
    OnImagePickedListener onImagePickedListener;

    public interface OnImagePickedListener {
        void onImagePicked(int position);
    }

    public SellItemAdapter(Context context, File[] imageFiles) {
        this.context = context;
        this.onImagePickedListener = (OnImagePickedListener) context;
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.img);
            int height = bd.getBitmap().getHeight();
            int width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
        this.imageFiles = imageFiles;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.imageview_recycler, parent, false);
        return new ImageHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        holder.mimgitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                if (onImagePickedListener != null) {
                    onImagePickedListener.onImagePicked(holder.getAdapterPosition());
                }
            }
        });
        try {
            if (imageFiles[holder.getAdapterPosition()] != null) {
                holder.imgadd.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(imageFiles[holder.getAdapterPosition()])
                        .placeholder(R.drawable.round_corner_rect_gray)
                        .error(R.drawable.round_corner_rect_gray)
                        .into(holder.mimgitem);
            } else {
                holder.imgadd.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(R.drawable.round_corner_rect_gray)
                        .placeholder(R.drawable.round_corner_rect_gray)
                        .error(R.drawable.round_corner_rect_gray)
                        .into(holder.mimgitem);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return SellItemFirstFragment.MAX_IMAGE_COUNT;
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        ImageView mimgitem,imgadd;

        ImageHolder(View itemView) {
            super(itemView);
            mimgitem = itemView.findViewById(R.id.img_item);
            imgadd = itemView.findViewById(R.id.img_add);
        }
    }
}
