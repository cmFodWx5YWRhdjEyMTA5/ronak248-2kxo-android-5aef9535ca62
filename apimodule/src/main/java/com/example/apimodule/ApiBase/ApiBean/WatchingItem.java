package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 02/12/16.
 */

public class WatchingItem {
    @SerializedName("item_id")
    @Expose
    private int itemId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_price")
    @Expose
    private String itemPrice;
    @SerializedName("item_qty")
    @Expose
    private String itemQty;
    @SerializedName("item_qty_remained")
    @Expose
    private String itemQtyRemained;
    @SerializedName("ispurchased")
    @Expose
    private String ispurchased;
    @SerializedName("media_thumb")
    @Expose
    private String mediaThumb;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("media_type")
    @Expose
    private String mediaType;

    /**
     *
     * @return
     * The itemId
     */
    public int getItemId() {
        return itemId;
    }

    /**
     *
     * @param itemId
     * The item_id
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     *
     * @return
     * The itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     *
     * @param itemName
     * The item_name
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     *
     * @return
     * The itemPrice
     */
    public String getItemPrice() {
        return itemPrice;
    }

    /**
     *
     * @param itemPrice
     * The item_price
     */
    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    /**
     *
     * @return
     * The itemQty
     */
    public String getItemQty() {
        return itemQty;
    }

    /**
     *
     * @param itemQty
     * The item_qty
     */
    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    /**
     *
     * @return
     * The itemQtyRemained
     */
    public String getItemQtyRemained() {
        return itemQtyRemained;
    }

    /**
     *
     * @param itemQtyRemained
     * The item_qty_remained
     */
    public void setItemQtyRemained(String itemQtyRemained) {
        this.itemQtyRemained = itemQtyRemained;
    }

    /**
     *
     * @return
     * The ispurchased
     */
    public String getIspurchased() {
        return ispurchased;
    }

    /**
     *
     * @param ispurchased
     * The ispurchased
     */
    public void setIspurchased(String ispurchased) {
        this.ispurchased = ispurchased;
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

    @Override
    public String toString() {
        return "WatchingItem{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                ", itemQty='" + itemQty + '\'' +
                ", itemQtyRemained='" + itemQtyRemained + '\'' +
                ", ispurchased='" + ispurchased + '\'' +
                ", mediaThumb='" + mediaThumb + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }
}
