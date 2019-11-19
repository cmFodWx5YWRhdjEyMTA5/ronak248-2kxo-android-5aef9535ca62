package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 12/11/16.
 */

public class SellItemResult {
    @SerializedName("itemid")
    @Expose
    private int itemid;

    @SerializedName("item_name")
    @Expose
    private String item_name;

    @SerializedName("item_image")
    @Expose
    private String item_image;

    public int getItemid() {
        return itemid;
    }

    public String getItem_name() {
        if (item_name == null) {
            return "";
        }
        return item_name;
    }

    public String getItem_image() {
        return item_image;
    }
}
