package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithdrawBean {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private WithdrawResultBean result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WithdrawResultBean getResult() {
        return result;
    }

    public void setResult(WithdrawResultBean result) {
        this.result = result;
    }
}
