package com.example.apimodule.ApiBase.ApiBean;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shubham.Agarwal on 09/12/16.
 */

public class PurchaseHistoryDetail {
    @SerializedName("item_shipping_details")
    @Expose
    private String item_shipping_details;

    public String getItem_shipping_details() {
        return item_shipping_details;
    }

    public void setItem_shipping_details(String item_shipping_details) {
        this.item_shipping_details = item_shipping_details;
    }

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("item_order_id")
    @Expose
    private String itemOrderId;
    @SerializedName("item_order_date")
    @Expose
    private String itemOrderDate;


    @SerializedName("item_id")
    @Expose
    private String itemId;



    @SerializedName("item_qty")
    @Expose
    private int itemQty;


    @SerializedName("item_purchased_by")
    @Expose
    private int itemPurchasedBy;

    @SerializedName("item_purchased_details")
    @Expose
    private String itemPurchasedDetails;
    @SerializedName("item_purchaseby")
    @Expose
    private String itemPurchaseby;
    @SerializedName("item_tracking_data")
    @Expose
    private Object itemTrackingData;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("orderdetails")
    @Expose
    private ArrayList<PurchaseOrderdetail> orderdetails = null;

    @SerializedName("seller")
    @Expose
    private Seller seller;

    /*@SerializedName("isreview")
    @Expose
    private boolean isreview;
    @SerializedName("hastrackingdetail")
    @Expose
    private int hastrackingdetail;
    @SerializedName("trackingdetail")
    @Expose
    private Object trackingdetail;*/

    @SuppressWarnings("unchecked")
    public String getTrackingId() {
        try {
            if (itemTrackingData != null && itemTrackingData instanceof LinkedTreeMap) {
                return ((LinkedTreeMap<String, String>) itemTrackingData).get("trackingid");
            }
        } catch (Exception ignored) {

        }
        return "";
    }

    @SuppressWarnings("unchecked")
    public String getCourierCompanyName() {
        try {
            if (itemTrackingData != null && itemTrackingData instanceof LinkedTreeMap) {
                return ((LinkedTreeMap<String, String>) itemTrackingData).get("comapnyname");
            }
        } catch (Exception ignored) {

        }
        return "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemOrderId() {
        return itemOrderId;
    }

    public void setItemOrderId(String itemOrderId) {
        this.itemOrderId = itemOrderId;
    }

    public String getItemOrderDate() {
        return itemOrderDate;
    }

    public void setItemOrderDate(String itemOrderDate) {
        this.itemOrderDate = itemOrderDate;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public int getItemPurchasedBy() {
        return itemPurchasedBy;
    }

    public void setItemPurchasedBy(int itemPurchasedBy) {
        this.itemPurchasedBy = itemPurchasedBy;
    }


    public String getItemPurchasedDetails() {
        return itemPurchasedDetails;
    }

    public void setItemPurchasedDetails(String itemPurchasedDetails) {
        this.itemPurchasedDetails = itemPurchasedDetails;
    }

    public String getItemPurchaseby() {
        return itemPurchaseby;
    }

    public void setItemPurchaseby(String itemPurchaseby) {
        this.itemPurchaseby = itemPurchaseby;
    }

    public Object getItemTrackingData() {
        return itemTrackingData;
    }

    public void setItemTrackingData(Object itemTrackingData) {
        this.itemTrackingData = itemTrackingData;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public ArrayList<PurchaseOrderdetail> getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(ArrayList<PurchaseOrderdetail> orderdetails) {
        this.orderdetails = orderdetails;
    }

     class Seller{
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("fname")
        @Expose
        private String fname;
        @SerializedName("username")
        @Expose
        private String username;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

    }
}
