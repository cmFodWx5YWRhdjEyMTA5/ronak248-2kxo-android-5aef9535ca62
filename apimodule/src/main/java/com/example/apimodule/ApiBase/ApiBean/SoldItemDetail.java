package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SoldItemDetail {
    boolean isOpened=false;

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_description")
    @Expose
    private String itemDescription;
    @SerializedName("item_tags")
    @Expose
    private String itemTags;
    @SerializedName("item_price")
    @Expose
    private double itemPrice;
    @SerializedName("item_actualprice")
    @Expose
    private double itemActualprice;
    @SerializedName("item_shipping_cost")
    @Expose
    private double itemShippingCost;
    @SerializedName("item_condition")
    @Expose
    private String itemCondition;
    @SerializedName("item_created_by")
    @Expose
    private int itemCreatedBy;
    @SerializedName("item_category_id")
    @Expose
    private int itemCategoryId;
    @SerializedName("ispurchased")
    @Expose
    private int ispurchased;
    @SerializedName("item_qty")
    @Expose
    private int itemQty;
    @SerializedName("item_qty_remained")
    @Expose
    private int itemQtyRemained;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("isdeleted")
    @Expose
    private int isdeleted;
    @SerializedName("deleted_date")
    @Expose
    private Object deletedDate;
    @SerializedName("itemid")
    @Expose
    private int itemid;
    @SerializedName("orderdetails")
    @Expose
    private ArrayList<SoldOutOrderDetail> orderdetails = null;
    @SerializedName("media")
    @Expose
    private SoldOutMedia media;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemTags() {
        return itemTags;
    }

    public void setItemTags(String itemTags) {
        this.itemTags = itemTags;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getItemActualprice() {
        return itemActualprice;
    }

    public void setItemActualprice(double itemActualprice) {
        this.itemActualprice = itemActualprice;
    }

    public double getItemShippingCost() {
        return itemShippingCost;
    }

    public void setItemShippingCost(double itemShippingCost) {
        this.itemShippingCost = itemShippingCost;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public int getItemCreatedBy() {
        return itemCreatedBy;
    }

    public void setItemCreatedBy(int itemCreatedBy) {
        this.itemCreatedBy = itemCreatedBy;
    }

    public int getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(int itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public int getIspurchased() {
        return ispurchased;
    }

    public void setIspurchased(int ispurchased) {
        this.ispurchased = ispurchased;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public int getItemQtyRemained() {
        return itemQtyRemained;
    }

    public void setItemQtyRemained(int itemQtyRemained) {
        this.itemQtyRemained = itemQtyRemained;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(int isdeleted) {
        this.isdeleted = isdeleted;
    }

    public Object getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Object deletedDate) {
        this.deletedDate = deletedDate;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public ArrayList<SoldOutOrderDetail> getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(ArrayList<SoldOutOrderDetail> orderdetails) {
        this.orderdetails = orderdetails;
    }

    public SoldOutMedia getMedia() {
        return media;
    }

    public void setMedia(SoldOutMedia media) {
        this.media = media;
    }
}