package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SoldItemResult {
    @SerializedName("itemcount")
    @Expose
    private int itemcount;
    @SerializedName("itemdetails")
    @Expose
    private ArrayList<SoldItemDetail> itemdetails = null;


    public int getItemcount() {
        return itemcount;
    }

    public ArrayList<SoldItemDetail> getItemdetails() {
        return itemdetails;
    }

    @Override
    public String toString() {
        return "SoldItemResult{" +
                "itemcount=" + itemcount +
                ", itemdetails=" + itemdetails +
                '}';
    }
}
