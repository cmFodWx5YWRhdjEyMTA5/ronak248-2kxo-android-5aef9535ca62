package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shubham Agarwal on 17/10/16.
 */
public class Itemdetail {

    private static final String TAG = "Itemdetail";

    @SerializedName("myitem")
    @Expose
    private Integer myitem;
    @SerializedName("iswatched") //not
    @Expose
    private String iswatched="0";
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

    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("media")
    @Expose
    private ArrayList<Medium> media = null;
    @SerializedName("userid")
    @Expose
    private Integer userid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("userphoto")
    @Expose
    private String userphoto;
    @SerializedName("userphotothumb")
    @Expose
    private String userphotothumb; //not
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
    private Float itemPrice;
    @SerializedName("ispaypal")
    @Expose
    private Integer ispaypal;
    @SerializedName("isbitcoin")
    @Expose
    private Integer isbitcoin;


    @SerializedName("boost_url")
    @Expose
    private String boost_url;

    public String getBoost_url() {
        return boost_url;
    }

    public void setBoost_url(String boost_url) {
        this.boost_url = boost_url;
    }

    public Integer getIsincart() {
        return isincart;
    }

    public void setIsincart(Integer isincart) {
        this.isincart = isincart;
    }

    @SerializedName("isincart")
    @Expose
    private Integer isincart=0; //not
    @SerializedName("bitcoinmail")
    @Expose
    private String bitcoinmail;


    public String getPostType() {
        return postType;
    }

    public Integer getPostId() {
        return postId;
    }

    public ArrayList<Medium> getMedia() {
        return media;
    }

    public Integer getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public String getUserphotothumb() {
        return userphotothumb;
    }

    public String getIswatched() {
        return iswatched;
    }

    public void setIswatched(String iswatched) {
        this.iswatched = iswatched;
    }

    public Integer getMyitem() {
        return myitem;
    }

    public Integer getItemId() {
        return itemId;
    }

    public Integer getItemQty() {
        return itemQty;
    }

    public Integer getItemQtyRemained() {
        return itemQtyRemained;
    }

    public Integer getIspurchased() {
        return ispurchased;
    }

    public String getItemName() {
        return itemName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getMediaThumb() {
        return mediaThumb;
    }

    public String getItemActualprice() {
        return itemActualprice;
    }

    public String getItemShippingCost() {
        return itemShippingCost;
    }

    public String getItemCreatedBy() {
        return itemCreatedBy;
    }

    public Float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getIspaypal() {
        return ispaypal;
    }

    public Integer getIsbitcoin() {
        return isbitcoin;
    }

    public String getBitcoinmail() {
        return bitcoinmail;
    }

    public void setMyitem(Integer myitem) {
        this.myitem = myitem;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public void setItemQty(Integer itemQty) {
        this.itemQty = itemQty;
    }

    public void setItemQtyRemained(Integer itemQtyRemained) {
        this.itemQtyRemained = itemQtyRemained;
    }

    public void setIspurchased(Integer ispurchased) {
        this.ispurchased = ispurchased;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void setMediaThumb(String mediaThumb) {
        this.mediaThumb = mediaThumb;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setMedia(ArrayList<Medium> media) {
        this.media = media;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public void setUserphotothumb(String userphotothumb) {
        this.userphotothumb = userphotothumb;
    }

    public void setItemActualprice(String itemActualprice) {
        this.itemActualprice = itemActualprice;
    }

    public void setItemShippingCost(String itemShippingCost) {
        this.itemShippingCost = itemShippingCost;
    }

    public void setItemCreatedBy(String itemCreatedBy) {
        this.itemCreatedBy = itemCreatedBy;
    }

    public void setIspaypal(Integer ispaypal) {
        this.ispaypal = ispaypal;
    }

    public void setIsbitcoin(Integer isbitcoin) {
        this.isbitcoin = isbitcoin;
    }

    public void setBitcoinmail(String bitcoinmail) {
        this.bitcoinmail = bitcoinmail;
    }

    @Override
    public String toString() {
        return "Itemdetail{" +
                "myitem=" + myitem +
                ", iswatched='" + iswatched + '\'' +
                ", itemId=" + itemId +
                ", itemQty=" + itemQty +
                ", itemQtyRemained=" + itemQtyRemained +
                ", ispurchased=" + ispurchased +
                ", itemName='" + itemName + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", mediaThumb='" + mediaThumb + '\'' +
                ", postType='" + postType + '\'' +
                ", postId=" + postId +
                ", media=" + media +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", userphoto='" + userphoto + '\'' +
                ", userphotothumb='" + userphotothumb + '\'' +
                ", itemActualprice='" + itemActualprice + '\'' +
                ", itemShippingCost='" + itemShippingCost + '\'' +
                ", itemCreatedBy='" + itemCreatedBy + '\'' +
                ", itemPrice=" + itemPrice +
                ", ispaypal=" + ispaypal +
                ", isbitcoin=" + isbitcoin +
                ", bitcoinmail='" + bitcoinmail + '\'' +
                '}';
    }
}


