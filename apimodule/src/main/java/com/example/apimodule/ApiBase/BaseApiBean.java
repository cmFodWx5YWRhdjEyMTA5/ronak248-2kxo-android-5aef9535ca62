package com.example.apimodule.ApiBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parangat on 10/10/17.
 */

public class BaseApiBean {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String msg;


    public String getStatus() {
        if (status == null) {
            return "";
        }
        return status="1";
    }

    public String getMsg() {
        if (msg == null) {
            return "";
        }

        return msg;
    }
}
