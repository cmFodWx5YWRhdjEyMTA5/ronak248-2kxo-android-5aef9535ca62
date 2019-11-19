package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 07/11/16.
 */

public class PostMediaBean {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private PostMediaResult result;

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status="1";
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     *
     * @param msg
     * The msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     *
     * @return
     * The result
     */
    public PostMediaResult getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The result
     */
    public void setResult(PostMediaResult result) {
        this.result = result;
    }

}
