package com.example.apimodule.ApiBase.ApiBean.Tracking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("orderid")
    @Expose
    public String orderid;

    public String getOrderid() {
        return orderid;
    }
}