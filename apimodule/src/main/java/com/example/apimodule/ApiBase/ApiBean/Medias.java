package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham Agarwal on 03/11/16.
 */
public class Medias {

    @SerializedName("id")
    @Expose
    private Integer id;

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

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The mediaType
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * @param mediaType The media_type
     */
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * @return The mediaName
     */
    public String getMediaName() {
        return mediaName;
    }

    /**
     * @param mediaName The media_name
     */
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    /**
     * @return The mediaUrl
     */
    public String getMediaUrl() {
        return mediaUrl;
    }

    /**
     * @param mediaUrl The media_url
     */
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    /**
     * @return The mediaThumb
     */
    public String getMediaThumb() {
        return mediaThumb;
    }

    /**
     * @param mediaThumb The media_thumb
     */
    public void setMediaThumb(String mediaThumb) {
        this.mediaThumb = mediaThumb;
    }

    /**
     * @return The createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate The created_date
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
