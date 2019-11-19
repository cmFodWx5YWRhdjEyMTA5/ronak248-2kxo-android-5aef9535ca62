package com.example.apimodule.ApiBase.ApiBean.Stripe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parangat on 13/11/17.
 */

public class AddCardReponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String msg;
    @SerializedName("result")
    @Expose
    public AddCardResult result;

    public String getStatus() {
        return status="1";
    }

    public String getMsg() {
        return msg;
    }

    public AddCardResult getResult() {
        return result;
    }
}
