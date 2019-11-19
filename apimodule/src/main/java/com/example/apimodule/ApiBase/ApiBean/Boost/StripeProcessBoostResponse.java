package com.example.apimodule.ApiBase.ApiBean.Boost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parangat on 13/11/17.
 */

public class StripeProcessBoostResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private StripeProcessBoostResult result;

    public String getStatus() {
        return status="1";
    }

    public String getMsg() {
        return msg;
    }

    public StripeProcessBoostResult getResult() {
        return result;
    }
}
