package com.example.apimodule.ApiBase.ApiBean.PostDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostMedium {
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

    public String getMediaThumb() {
        return mediaThumb;
    }

    public void setMediaThumb(String mediaThumb) {
        this.mediaThumb = mediaThumb;
    }
}
