
package com.example.apimodule.ApiBase.ApiBean.PostDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostDetails {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private PostDetailResult result;

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

    public PostDetailResult getResult() {
        return result;
    }

    public void setResult(PostDetailResult result) {
        this.result = result;
    }

}
