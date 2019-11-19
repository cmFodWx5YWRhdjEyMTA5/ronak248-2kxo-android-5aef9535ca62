package com.example.apimodule.ApiBase.ApiBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shubham.Agarwal on 23/11/16.
 */

public class ItemDetailResult implements Parcelable {
    @SerializedName("item_id")
    @Expose
    private int itemId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_category_id")
    @Expose
    private String itemCategoryId;
    @SerializedName("item_category_name")
    @Expose
    private String itemCategoryName;
    @SerializedName("item_category_status")
    @Expose
    private int itemCategoryStatus;
    @SerializedName("item_description")
    @Expose
    private String itemDescription;
    @SerializedName("item_tags")
    @Expose
    private String itemTags;
    @SerializedName("item_price")
    @Expose
    private String itemPrice;
    @SerializedName("item_condition")
    @Expose
    private String item_condition;

    @SerializedName("item_actualprice")
    @Expose
    private String itemActualprice;
    @SerializedName("item_shipping_cost")
    @Expose
    private String itemShippingCost;
    @SerializedName("item_qty")
    @Expose
    private int itemQty;
    @SerializedName("item_qty_remained")
    @Expose
    private int itemQtyRemained;
    @SerializedName("item_created_by")
    @Expose
    private String itemCreatedBy;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("ispurchased")
    @Expose
    private String ispurchased;
    @SerializedName("isdeleted")
    @Expose
    private String isdeleted;
    @SerializedName("deleted_date")
    @Expose
    private String deletedDate;
    @SerializedName("myitem")
    @Expose
    private int myitem;
    @SerializedName("iswatched")
    @Expose
    private int iswatched;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("userid")
    @Expose
    private int userid;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("ispaypal")
    @Expose
    private int ispaypal;
    @SerializedName("isbitcoin")
    @Expose
    private int isbitcoin;
    @SerializedName("bitcoinmail")
    @Expose
    private String bitcoinmail;
    @SerializedName("usercity")
    @Expose
    private String usercity;
    @SerializedName("userphoto")
    @Expose
    private String userphoto;
    @SerializedName("userphotothumb")
    @Expose
    private String userphotothumb;
    @SerializedName("reviewdata")
    @Expose
    private ArrayList<ItemReviewData> reviewdata = null;
    @SerializedName("purchasedata")
    @Expose
    private ArrayList<ItemPurchasedata> purchasedata = null;
    @SerializedName("media")
    @Expose
    private ArrayList<ItemDetailMedia> media = null;

    public String getItemQuntity() {
        return itemQuntity;
    }

    public String setItemQuntity(String itemQuntity) {
        this.itemQuntity = itemQuntity;
        return itemQuntity;
    }

    public String itemQuntity;

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCategoryId() {
        return itemCategoryId;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public int getItemCategoryStatus() {
        return itemCategoryStatus;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemTags() {
        return itemTags;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItem_condition() {
        return item_condition;
    }

    public String getItemActualprice() {
        return itemActualprice;
    }

    public String getItemShippingCost() {
        return itemShippingCost;
    }

    public int getItemQty() {
        return itemQty;
    }

    public int getItemQtyRemained() {
        return itemQtyRemained;
    }

    public String getItemCreatedBy() {
        return itemCreatedBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public String getIspurchased() {
        return ispurchased;
    }

    public String getIsdeleted() {
        return isdeleted;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public int getMyitem() {
        return myitem;
    }

    public int getIswatched() {
        return iswatched;
    }

    public void setIswatched(int iswatched) {
        this.iswatched = iswatched;
    }

    public String getUsername() {
        return username;
    }

    public int getUserid() {
        return userid;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public int getIspaypal() {
        return ispaypal;
    }

    public int getIsbitcoin() {
        return isbitcoin;
    }

    public String getBitcoinmail() {
        return bitcoinmail;
    }

    public String getUsercity() {
        return usercity;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public String getUserphotothumb() {
        return userphotothumb;
    }

    public ArrayList<ItemReviewData> getReviewdata() {
        return reviewdata;
    }


    public ArrayList<ItemPurchasedata> getPurchasedata() {
        return purchasedata;
    }


    public boolean hasPurchasedData() {
        return !(purchasedata == null || purchasedata.size() == 0);
    }

    public ArrayList<ItemDetailMedia> getMedia() {
        return media;
    }


    public ItemDetailResult(Parcel in) {
        itemId = in.readInt();
        itemName = in.readString();
        itemCategoryId = in.readString();
        itemCategoryName = in.readString();
        itemCategoryStatus = in.readInt();
        itemDescription = in.readString();
        itemTags = in.readString();
        itemPrice = in.readString();
        item_condition = in.readString();
        itemActualprice = in.readString();
        itemShippingCost = in.readString();
        itemQty = in.readInt();
        itemQtyRemained = in.readInt();
        itemCreatedBy = in.readString();
        createdDate = in.readString();
        updatedDate = in.readString();
        ispurchased = in.readString();
        isdeleted = in.readString();
        deletedDate = in.readString();
        myitem = in.readInt();
        iswatched = in.readInt();
        username = in.readString();
        userid = in.readInt();
        fname = in.readString();
        lname = in.readString();
        ispaypal = in.readInt();
        isbitcoin = in.readInt();
        bitcoinmail = in.readString();
        usercity = in.readString();
        userphoto = in.readString();
        userphotothumb = in.readString();
        if (in.readByte() == 0x01) {
            reviewdata = new ArrayList<ItemReviewData>();
            in.readList(reviewdata, ItemReviewData.class.getClassLoader());
        } else {
            reviewdata = null;
        }
        if (in.readByte() == 0x01) {
            purchasedata = new ArrayList<ItemPurchasedata>();
            in.readList(purchasedata, ItemPurchasedata.class.getClassLoader());
        } else {
            purchasedata = null;
        }
        if (in.readByte() == 0x01) {
            media = new ArrayList<ItemDetailMedia>();
            in.readList(media, ItemDetailMedia.class.getClassLoader());
        } else {
            media = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemId);
        dest.writeString(itemName);
        dest.writeString(itemCategoryId);
        dest.writeString(itemCategoryName);
        dest.writeInt(itemCategoryStatus);
        dest.writeString(itemDescription);
        dest.writeString(itemTags);
        dest.writeString(itemPrice);
        dest.writeString(item_condition);
        dest.writeString(itemActualprice);
        dest.writeString(itemShippingCost);
        dest.writeInt(itemQty);
        dest.writeInt(itemQtyRemained);
        dest.writeString(itemCreatedBy);
        dest.writeString(createdDate);
        dest.writeString(updatedDate);
        dest.writeString(ispurchased);
        dest.writeString(isdeleted);
        dest.writeString(deletedDate);
        dest.writeInt(myitem);
        dest.writeInt(iswatched);
        dest.writeString(username);
        dest.writeInt(userid);
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeInt(ispaypal);
        dest.writeInt(isbitcoin);
        dest.writeString(bitcoinmail);
        dest.writeString(usercity);
        dest.writeString(userphoto);
        dest.writeString(userphotothumb);
        if (reviewdata == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(reviewdata);
        }
        if (purchasedata == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(purchasedata);
        }
        if (media == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(media);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ItemDetailResult> CREATOR = new Parcelable.Creator<ItemDetailResult>() {
        @Override
        public ItemDetailResult createFromParcel(Parcel in) {
            return new ItemDetailResult(in);
        }

        @Override
        public ItemDetailResult[] newArray(int size) {
            return new ItemDetailResult[size];
        }
    };
}