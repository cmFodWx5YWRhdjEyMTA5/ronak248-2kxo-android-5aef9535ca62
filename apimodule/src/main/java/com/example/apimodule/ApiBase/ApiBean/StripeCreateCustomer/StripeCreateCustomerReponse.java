package com.example.apimodule.ApiBase.ApiBean.StripeCreateCustomer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StripeCreateCustomerReponse {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String msg;
    @SerializedName("result")
    @Expose
    public Result result;

    public String getStatus() {
        return status="1";
    }

    public String getMsg() {
        return msg;
    }

    public Result getResult() {
        return result;
    }
}