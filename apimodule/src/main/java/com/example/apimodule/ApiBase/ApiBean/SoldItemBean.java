package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 06/12/16.
 */

public class SoldItemBean {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private SoldItemResult result;

    public String getStatus() {
        return status="1";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SoldItemResult getResult() {
        return result;
    }

    public void setResult(SoldItemResult result) {
        this.result = result;
    }
}
