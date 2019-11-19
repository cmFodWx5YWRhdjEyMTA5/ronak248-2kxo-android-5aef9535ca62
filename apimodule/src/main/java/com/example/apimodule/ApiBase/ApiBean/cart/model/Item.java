package com.example.apimodule.ApiBase.ApiBean.cart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_price")
    @Expose
    private double itemPrice;
    @SerializedName("item_shipping_cost")
    @Expose
    private double itemShippingCost;

    @SerializedName("item_qty")
    @Expose
    private int itemQty;

    @SerializedName("item_qty_remained")
    @Expose
    private Integer itemQtyRemained;

    public Integer getItemQtyRemained() {
        return itemQtyRemained;
    }

    public void setItemQtyRemained(Integer itemQtyRemained) {
        this.itemQtyRemained = itemQtyRemained;
    }

    @SerializedName("cart_qty")
    @Expose
    private int cartQty;
    @SerializedName("seller")
    @Expose
    private String seller;
    @SerializedName("iimage")
    @Expose
    private List<Iimage> iimage = null;

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

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getItemShippingCost() {
        return itemShippingCost;
    }

    public void setItemShippingCost(double itemShippingCost) {
        this.itemShippingCost = itemShippingCost;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public int getCartQty() {
        return cartQty;
    }

    public void setCartQty(int cartQty) {
        this.cartQty = cartQty;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public List<Iimage> getIimage() {
        return iimage;
    }

    public void setIimage(List<Iimage> iimage) {
        this.iimage = iimage;
    }

}