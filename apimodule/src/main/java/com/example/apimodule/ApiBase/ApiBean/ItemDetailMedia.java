package com.example.apimodule.ApiBase.ApiBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 23/11/16.
 */
public class ItemDetailMedia implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("media_id")
    @Expose
    private int mediaId;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("media_name")
    @Expose
    private String mediaName;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("media_thumb")
    @Expose
    private String mediaThumb;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    /**
     *
     * @return
     * The mediaType
     */


    public String getMediaType() {
        return mediaType;
    }

    /**
     *
     * @param mediaType
     * The media_type
     */
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    /**
     *
     * @return
     * The mediaName
     */
    public String getMediaName() {
        return mediaName;
    }

    /**
     *
     * @param mediaName
     * The media_name
     */
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    /**
     *
     * @return
     * The mediaUrl
     */
    public String getMediaUrl() {
        return mediaUrl;
    }

    /**
     *
     * @param mediaUrl
     * The media_url
     */
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    /**
     *
     * @return
     * The mediaThumb
     */
    public String getMediaThumb() {
        return mediaThumb;
    }

    /**
     *
     * @param mediaThumb
     * The media_thumb
     */
    public void setMediaThumb(String mediaThumb) {
        this.mediaThumb = mediaThumb;
    }

    /**
     *
     * @return
     * The createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     *
     * @param createdDate
     * The created_date
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    protected ItemDetailMedia(Parcel in) {
        id = in.readInt();
        mediaType = in.readString();
        mediaName = in.readString();
        mediaUrl = in.readString();
        mediaThumb = in.readString();
        createdDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(mediaType);
        dest.writeString(mediaName);
        dest.writeString(mediaUrl);
        dest.writeString(mediaThumb);
        dest.writeString(createdDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ItemDetailMedia> CREATOR = new Parcelable.Creator<ItemDetailMedia>() {
        @Override
        public ItemDetailMedia createFromParcel(Parcel in) {
            return new ItemDetailMedia(in);
        }

        @Override
        public ItemDetailMedia[] newArray(int size) {
            return new ItemDetailMedia[size];
        }
    };
}