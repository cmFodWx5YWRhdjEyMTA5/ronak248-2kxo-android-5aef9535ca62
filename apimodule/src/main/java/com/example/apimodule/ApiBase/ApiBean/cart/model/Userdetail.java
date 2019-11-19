package com.example.apimodule.ApiBase.ApiBean.cart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Userdetail {

    @SerializedName("seller")
    @Expose
    private String seller;
    @SerializedName("items")
    @Expose
    private ArrayList<Item> items = null;

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

}