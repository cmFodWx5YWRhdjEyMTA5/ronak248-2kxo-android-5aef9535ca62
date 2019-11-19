
package com.example.apimodule.ApiBase.ApiBean.ShopData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Itemdetail_ {

    @SerializedName("myitem")
    @Expose
    private Integer myitem;
    @SerializedName("ispurchased")
    @Expose
    private String ispurchased;
    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("item_price")
    @Expose
    private String itemPrice;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("media_thumb")
    @Expose
    private String mediaThumb;

    public Integer getMyitem() {
        return myitem;
    }

    public void setMyitem(Integer myitem) {
        this.myitem = myitem;
    }

    public Itemdetail_ withMyitem(Integer myitem) {
        this.myitem = myitem;
        return this;
    }

    public String getIspurchased() {
        return ispurchased;
    }

    public void setIspurchased(String ispurchased) {
        this.ispurchased = ispurchased;
    }

    public Itemdetail_ withIspurchased(String ispurchased) {
        this.ispurchased = ispurchased;
        return this;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Itemdetail_ withItemId(Integer itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Itemdetail_ withItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
        return this;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Itemdetail_ withItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Itemdetail_ withMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Itemdetail_ withMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
        return this;
    }

    public String getMediaThumb() {
        return mediaThumb;
    }

    public void setMediaThumb(String mediaThumb) {
        this.mediaThumb = mediaThumb;
    }

    public Itemdetail_ withMediaThumb(String mediaThumb) {
        this.mediaThumb = mediaThumb;
        return this;
    }

}
