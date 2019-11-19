
package com.example.apimodule.ApiBase.ApiBean.Posts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Posts {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private Result result;

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
