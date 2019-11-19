
package com.example.apimodule.ApiBase.ApiBean.Shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Item {

    @SerializedName("myitem")
    @Expose
    private Integer myitem;
    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("item_qty")
    @Expose
    private Integer itemQty;
    @SerializedName("item_qty_remained")
    @Expose
    private Integer itemQtyRemained;
    @SerializedName("ispurchased")
    @Expose
    private Integer ispurchased;
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
    @SerializedName("item_actualprice")
    @Expose
    private String itemActualprice;
    @SerializedName("item_shipping_cost")
    @Expose
    private String itemShippingCost;
    @SerializedName("item_created_by")
    @Expose
    private String itemCreatedBy;
    @SerializedName("item_price")
    @Expose
    private Integer itemPrice;
    @SerializedName("ispaypal")
    @Expose
    private Integer ispaypal;
    @SerializedName("isbitcoin")
    @Expose
    private Integer isbitcoin;
    @SerializedName("bitcoinmail")
    @Expose
    private String bitcoinmail;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("iswatched")
    @Expose
    private String iswatched;

    public Integer getMyitem() {
        return myitem;
    }

    public void setMyitem(Integer myitem) {
        this.myitem = myitem;
    }

    public Item withMyitem(Integer myitem) {
        this.myitem = myitem;
        return this;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Item withItemId(Integer itemId) {
        this.itemId = itemId;
        return this;
    }

    public Integer getItemQty() {
        return itemQty;
    }

    public void setItemQty(Integer itemQty) {
        this.itemQty = itemQty;
    }

    public Item withItemQty(Integer itemQty) {
        this.itemQty = itemQty;
        return this;
    }

    public Integer getItemQtyRemained() {
        return itemQtyRemained;
    }

    public void setItemQtyRemained(Integer itemQtyRemained) {
        this.itemQtyRemained = itemQtyRemained;
    }

    public Item withItemQtyRemained(Integer itemQtyRemained) {
        this.itemQtyRemained = itemQtyRemained;
        return this;
    }

    public Integer getIspurchased() {
        return ispurchased;
    }

    public void setIspurchased(Integer ispurchased) {
        this.ispurchased = ispurchased;
    }

    public Item withIspurchased(Integer ispurchased) {
        this.ispurchased = ispurchased;
        return this;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Item withItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Item withMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Item withMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
        return this;
    }

    public String getMediaThumb() {
        return mediaThumb;
    }

    public void setMediaThumb(String mediaThumb) {
        this.mediaThumb = mediaThumb;
    }

    public Item withMediaThumb(String mediaThumb) {
        this.mediaThumb = mediaThumb;
        return this;
    }

    public String getItemActualprice() {
        return itemActualprice;
    }

    public void setItemActualprice(String itemActualprice) {
        this.itemActualprice = itemActualprice;
    }

    public Item withItemActualprice(String itemActualprice) {
        this.itemActualprice = itemActualprice;
        return this;
    }

    public String getItemShippingCost() {
        return itemShippingCost;
    }

    public void setItemShippingCost(String itemShippingCost) {
        this.itemShippingCost = itemShippingCost;
    }

    public Item withItemShippingCost(String itemShippingCost) {
        this.itemShippingCost = itemShippingCost;
        return this;
    }

    public String getItemCreatedBy() {
        return itemCreatedBy;
    }

    public void setItemCreatedBy(String itemCreatedBy) {
        this.itemCreatedBy = itemCreatedBy;
    }

    public Item withItemCreatedBy(String itemCreatedBy) {
        this.itemCreatedBy = itemCreatedBy;
        return this;
    }

    public Integer getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Integer itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Item withItemPrice(Integer itemPrice) {
        this.itemPrice = itemPrice;
        return this;
    }

    public Integer getIspaypal() {
        return ispaypal;
    }

    public void setIspaypal(Integer ispaypal) {
        this.ispaypal = ispaypal;
    }

    public Item withIspaypal(Integer ispaypal) {
        this.ispaypal = ispaypal;
        return this;
    }

    public Integer getIsbitcoin() {
        return isbitcoin;
    }

    public void setIsbitcoin(Integer isbitcoin) {
        this.isbitcoin = isbitcoin;
    }

    public Item withIsbitcoin(Integer isbitcoin) {
        this.isbitcoin = isbitcoin;
        return this;
    }

    public String getBitcoinmail() {
        return bitcoinmail;
    }

    public void setBitcoinmail(String bitcoinmail) {
        this.bitcoinmail = bitcoinmail;
    }

    public Item withBitcoinmail(String bitcoinmail) {
        this.bitcoinmail = bitcoinmail;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Item withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getIswatched() {
        return iswatched;
    }

    public void setIswatched(String iswatched) {
        this.iswatched = iswatched;
    }

    public Item withIswatched(String iswatched) {
        this.iswatched = iswatched;
        return this;
    }

}
