package com.example.apimodule.ApiBase.ApiBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 12/12/16.
 */

public class ItemPurchasedata implements Parcelable {
    @SerializedName("shippingaddress")
    @Expose
    private String shippingaddress;
    @SerializedName("order_id")
    @Expose
    private int orderId;
    @SerializedName("purchaseusername")
    @Expose
    private String purchaseusername;
    @SerializedName("purchaseuserid")
    @Expose
    private int purchaseuserid;
    @SerializedName("purchasefname")
    @Expose
    private String purchasefname;
    @SerializedName("purchaselname")
    @Expose
    private String purchaselname;
    @SerializedName("hastrackingdetail")
    @Expose
    private boolean hastrackingdetail;
    @SerializedName("trackingdetail")
    @Expose
    private Trackingdetail trackingdetail;
    @SerializedName("purchaseuserphoto")
    @Expose
    private String purchaseuserphoto;
    @SerializedName("purchaseuserphotothumb")
    @Expose
    private String purchaseuserphotothumb;

    public String getShippingaddress() {
        return shippingaddress;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getPurchaseusername() {
        return purchaseusername;
    }

    public int getPurchaseuserid() {
        return purchaseuserid;
    }

    public String getPurchasefname() {
        return purchasefname;
    }

    public String getPurchaselname() {
        return purchaselname;
    }

    public boolean isHastrackingdetail() {
        return hastrackingdetail;
    }

    public void setHastrackingdetail(boolean hastrackingdetail) {
        this.hastrackingdetail = hastrackingdetail;
    }

    public Trackingdetail getTrackingdetail() {
        return trackingdetail;
    }

    public String getPurchaseuserphoto() {
        return purchaseuserphoto;
    }

    public String getPurchaseuserphotothumb() {
        return purchaseuserphotothumb;
    }

    protected ItemPurchasedata(Parcel in) {
        shippingaddress = in.readString();
        orderId = in.readInt();
        purchaseusername = in.readString();
        purchaseuserid = in.readInt();
        purchasefname = in.readString();
        purchaselname = in.readString();
        hastrackingdetail = in.readByte() != 0x00;
        trackingdetail = (Trackingdetail) in.readValue(Trackingdetail.class.getClassLoader());
        purchaseuserphoto = in.readString();
        purchaseuserphotothumb = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shippingaddress);
        dest.writeInt(orderId);
        dest.writeString(purchaseusername);
        dest.writeInt(purchaseuserid);
        dest.writeString(purchasefname);
        dest.writeString(purchaselname);
        dest.writeByte((byte) (hastrackingdetail ? 0x01 : 0x00));
        dest.writeValue(trackingdetail);
        dest.writeString(purchaseuserphoto);
        dest.writeString(purchaseuserphotothumb);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ItemPurchasedata> CREATOR = new Parcelable.Creator<ItemPurchasedata>() {
        @Override
        public ItemPurchasedata createFromParcel(Parcel in) {
            return new ItemPurchasedata(in);
        }

        @Override
        public ItemPurchasedata[] newArray(int size) {
            return new ItemPurchasedata[size];
        }
    };
}