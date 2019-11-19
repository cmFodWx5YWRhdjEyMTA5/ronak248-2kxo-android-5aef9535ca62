package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 09/12/16.
 */

public class PurchaseHistoryBean {
boolean isOpened;

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private PuchaseHistoryResult result;

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
    public PuchaseHistoryResult getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The result
     */
    public void setResult(PuchaseHistoryResult result) {
        this.result = result;
    }

}
