package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("media_id")
    @Expose
    private Integer mediaId;
    @SerializedName("itemdescription")
    @Expose
    private Object itemdescription;
    @SerializedName("itemprice")
    @Expose
    private String itemprice;
    @SerializedName("itemfinalprice")
    @Expose
    private String itemfinalprice;
    @SerializedName("ispurchased")
    @Expose
    private String ispurchased = "0";

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public String getMedia_thumb() {
        return media_thumb;
    }

    public void setMedia_thumb(String media_thumb) {
        this.media_thumb = media_thumb;
    }

    @SerializedName("media_thumb")
    @Expose

    private String media_thumb;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getItemdescription() {
        return itemdescription;
    }

    public void setItemdescription(Object itemdescription) {
        this.itemdescription = itemdescription;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public String getItemfinalprice() {
        return itemfinalprice;
    }

    public void setItemfinalprice(String itemfinalprice) {
        this.itemfinalprice = itemfinalprice;
    }

    public String getIspurchased() {
        return ispurchased;
    }

    public void setIspurchased(String ispurchased) {
        this.ispurchased = ispurchased;
    }

}