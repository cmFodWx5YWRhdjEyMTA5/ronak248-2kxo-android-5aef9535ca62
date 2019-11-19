
package com.example.apimodule.ApiBase.ApiBean.BlockedList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Blocked {

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

    public void setStatus(String status) {
        this.status = status;
    }

    public Blocked withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Blocked withMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Blocked withResult(Result result) {
        this.result = result;
        return this;
    }

}
