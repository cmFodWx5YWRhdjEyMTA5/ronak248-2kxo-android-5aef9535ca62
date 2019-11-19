package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SoldOutOrderDetail {
    @SerializedName("purchased_by")
    @Expose
    private int purchasedBy;
    @SerializedName("item_tracking_data")
    @Expose
    private String itemTrackingData;
    @SerializedName("details")
    @Expose
    private ArrayList<SoldDetail> details = null;
    @SerializedName("hastrackingdetail")
    @Expose
    private int hastrackingdetail;

    @SerializedName("order_id")
    @Expose
    private int orderId;

    @SerializedName("item_tracking_data_new")
    @Expose
    private List<ItemTrackingDataNew> itemTrackingDataNew = null;

    @SerializedName("item_qty")
    @Expose
    private List<ItemQty> itemQty = null;

    public List<ItemTrackingDataNew> getItemTrackingDataNew() {
        return itemTrackingDataNew;
    }

    public void setItemTrackingDataNew(List<ItemTrackingDataNew> itemTrackingDataNew) {
        this.itemTrackingDataNew = itemTrackingDataNew;
    }

    public List<ItemQty> getItemQty() {
        return itemQty;
    }

    public void setItemQty(List<ItemQty> itemQty) {
        this.itemQty = itemQty;
    }

    public class ItemQty {

        @SerializedName("item_id")
        @Expose
        private Integer itemId;
        @SerializedName("productqty")
        @Expose
        private Integer productqty;

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public Integer getProductqty() {
            return productqty;
        }

        public void setProductqty(Integer productqty) {
            this.productqty = productqty;
        }

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPurchasedBy() {
        return purchasedBy;
    }

    public void setPurchasedBy(int purchasedBy) {
        this.purchasedBy = purchasedBy;
    }

    public String getItemTrackingData() {
        return itemTrackingData;
    }

    public void setItemTrackingData(String itemTrackingData) {
        this.itemTrackingData = itemTrackingData;
    }

    public ArrayList<SoldDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<SoldDetail> details) {
        this.details = details;
    }

    public int getHastrackingdetail() {
        return hastrackingdetail;
    }

    public void setHastrackingdetail(int hastrackingdetail) {
        this.hastrackingdetail = hastrackingdetail;
    }

    public class ItemTrackingDataNew implements Serializable {

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
