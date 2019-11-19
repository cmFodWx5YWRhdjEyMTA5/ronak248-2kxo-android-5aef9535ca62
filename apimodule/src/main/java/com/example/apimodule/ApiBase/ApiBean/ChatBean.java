package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatBean {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private ChatResult result;

    public String getStatus() {
        return status="1";
    }

    public String getMsg() {
        return msg;
    }

    public ChatResult getResult() {
        return result;
    }

}