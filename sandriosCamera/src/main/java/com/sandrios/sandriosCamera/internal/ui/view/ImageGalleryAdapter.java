package com.sandrios.sandriosCamera.internal.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.GalleryViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "ImageGalleryAdapter";
    private ArrayList<PickerTile> pickerTiles;
    private Context context;
    private OnItemClickListener onItemClickListener;

    ImageGalleryAdapter(Context context, ArrayList<PickerTile> pickerTiles) {
        this.context = context;
        this.pickerTiles = pickerTiles;
    }

    void updatePickerTiles(ArrayList<PickerTile> pickerTiles) {
        this.pickerTiles.addAll(pickerTiles);
        notifyDataSetChanged();
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GalleryViewHolder(View.inflate(context, R.layout.image_item, null));
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final PickerTile pickerTile = pickerTiles.get(holder.getAdapterPosition());
        holder.iv_play.setVisibility(pickerTile.getMediaType().equals(String.valueOf(MEDIA_TYPE_VIDEO)) ? View.VISIBLE : View.GONE);
        Picasso
                .with(context).load(R.mipmap.ico_player)
                .placeholder(R.mipmap.ico_player)
                .fit()
                .error(R.mipmap.ico_player)
                .into(holder.iv_play);


        if (pickerTile.getMediaType().equals(String.valueOf(MEDIA_TYPE_VIDEO))) {
            Picasso
                    .with(context).load(pickerTile.getLocation())
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_gallery))
                    .fit()
                    .centerCrop()
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_error))
                    .into(holder.iv_thumbnail);

        } else {
            Picasso.with(context)
                    .load(pickerTile.getImageUri())
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_gallery))
                    .fit()
                    .centerCrop()
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_error))
                    .into(holder.iv_thumbnail);
        }

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition(), pickerTile);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return pickerTiles.size();
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, PickerTile pickerTile);
    }

    public static class PickerTile {

        final Uri imageUri;
        final String mediaType;

        final Uri location;

        PickerTile(@NonNull Uri imageUri, String mediaType, Uri location) {
            this.imageUri = imageUri;
            this.mediaType = mediaType;
            this.location = location;
        }

        @Nullable
        public Uri getImageUri() {
            return imageUri;
        }

        public String getMediaType() {
            return mediaType;
        }

        @Nullable
        Uri getLocation() {
            return location;
        }

        @Override
        public String toString() {
            return "ImageTile: " + imageUri;
        }
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_thumbnail;
        ImageView iv_play;

        GalleryViewHolder(View view) {
            super(view);
            iv_thumbnail = view.findViewById(R.id.image);
            iv_play = view.findViewById(R.id.img_play);
//            iv_thumbnail.setLayoutParams(new RelativeLayout.LayoutParams(Utils.convertDipToPixels(context, 80) + 100, Utils.convertDipToPixels(context, 80) + 100));
        }
    }
}