package com.example.apimodule.ApiBase.ApiBean.cart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Iimage {

    @SerializedName("media_id")
    @Expose
    private int mediaId;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("media_thumb")
    @Expose
    private String mediaThumb;
    @SerializedName("media_name")
    @Expose
    private String mediaName;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaThumb() {
        return mediaThumb;
    }

    public void setMediaThumb(String mediaThumb) {
        this.mediaThumb = mediaThumb;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}