package com.screamxo.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.screamxo.Fragment.SellItemFirstFragment;
import com.screamxo.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Shubham.Agarwal on 11/11/16.
 */

public class SellIEdittemAdapter extends RecyclerView.Adapter<SellIEdittemAdapter.ImageHolder> {
    private static final String TAG = "SellIEdittemAdapter";
    ArrayList<String> imageFiles;
    private Context context;
    OnImagePickedListener onImagePickedListener;

    public interface OnImagePickedListener {
        void onImagePicked(int position);
    }

    public SellIEdittemAdapter(Context context, ArrayList<String> imageFiles) {
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
            if (imageFiles.get(holder.getAdapterPosition()) != null) {
                if(imageFiles.get(holder.getAdapterPosition()).contains("storage"))
                {
                    Uri uri = Uri.fromFile(new File(imageFiles.get(holder.getAdapterPosition())));

                    Picasso.with(context).load(uri)
                            .placeholder(R.mipmap.img)
                            .error(R.mipmap.img)
                            .fit()
                            .centerCrop()
                            .into(holder.mimgitem);;
                }
                else {
                    Picasso.with(context)
                            .load(imageFiles.get(holder.getAdapterPosition()))
                            .placeholder(R.mipmap.img)
                            .error(R.mipmap.img)
                            .fit()
                            .centerCrop()
                            .into(holder.mimgitem);
                }
            } else {
                Picasso.with(context)
                        .load(R.mipmap.img)
                        .placeholder(R.mipmap.img)
                        .error(R.mipmap.img)
                        .fit()
                        .centerCrop()
                        .into(holder.mimgitem);
            }

        }
        catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return SellItemFirstFragment.MAX_IMAGE_COUNT;
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        ImageView mimgitem;

        ImageHolder(View itemView) {
            super(itemView);
            mimgitem = itemView.findViewById(R.id.img_item);
        }
    }
}
