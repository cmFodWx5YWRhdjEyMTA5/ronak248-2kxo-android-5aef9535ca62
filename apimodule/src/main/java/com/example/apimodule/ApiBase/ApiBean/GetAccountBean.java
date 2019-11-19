package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 24/11/16.
 */

public class GetAccountBean {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private GetAccountResult result;

    public String getStatus() {
        return status="1";
    }

    public String getMsg() {
        return msg;
    }

    public GetAccountResult getResult() {
        return result;
    }

}
