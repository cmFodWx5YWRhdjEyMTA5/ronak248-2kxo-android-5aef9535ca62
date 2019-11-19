package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderdetail {
    boolean isOpened=false;

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    @SerializedName("item_id")
    @Expose
    private int itemId;


    @SerializedName("details")
    @Expose
    private ArrayList<PurchaseDetail> details = null;
    @SerializedName("media")
    @Expose
    private ArrayList<Medium> media = null;
    @SerializedName("isreview")
    @Expose
    private boolean isreview;
    @SerializedName("hastrackingdetail")
    @Expose
    private int hastrackingdetail;
    @SerializedName("seller")
    @Expose
    private Seller seller;

    @SerializedName("item_tracking_data")
    @Expose
    private String itemTrackingData;

    @SerializedName("purchase_date")
    @Expose
    private String purchaseDate;

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("item_tracking_data_new")
    @Expose
    private List<ItemTrackingDataNew> itemTrackingDataNew ;

    private QtyBean qty;

    public List<ItemTrackingDataNew> getItemTrackingDataNew() {
        return itemTrackingDataNew;
    }

    public void setItemTrackingDataNew(List<ItemTrackingDataNew> itemTrackingDataNew) {
        this.itemTrackingDataNew = itemTrackingDataNew;
    }

    public QtyBean getQty() {
        return qty;
    }

    public void setQty(QtyBean qty) {
        this.qty = qty;
    }

    public String getItemTrackingData() {
        return itemTrackingData;
    }

    public void setItemTrackingData(String itemTrackingData) {
        this.itemTrackingData = itemTrackingData;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public boolean isIsreview() {
        return isreview;
    }

    public void setIsreview(boolean isreview) {
        this.isreview = isreview;
    }

    public int getHastrackingdetail() {
        return hastrackingdetail;
    }

    public void setHastrackingdetail(int hastrackingdetail) {
        this.hastrackingdetail = hastrackingdetail;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public ArrayList<PurchaseDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<PurchaseDetail> details) {
        this.details = details;
    }

    public ArrayList<Medium> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Medium> media) {
        this.media = media;
    }

    public class ItemTrackingDataNew {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("item_id")
        @Expose
        private String itemId;
        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("postal_carrier")
        @Expose
        private String postalCarrier;
        @SerializedName("estimated_delivery")
        @Expose
        private String estimatedDelivery;
        @SerializedName("tracking_number")
        @Expose
        private String trackingNumber;
        @SerializedName("created_at")
        @Expose
        private Object createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPostalCarrier() {
            return postalCarrier;
        }

        public void setPostalCarrier(String postalCarrier) {
            this.postalCarrier = postalCarrier;
        }

        public String getEstimatedDelivery() {
            return estimatedDelivery;
        }

        public void setEstimatedDelivery(String estimatedDelivery) {
            this.estimatedDelivery = estimatedDelivery;
        }

        public String getTrackingNumber() {
            return trackingNumber;
        }

        public void setTrackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
        }

        public Object getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Object createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }
}
