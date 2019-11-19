
package com.example.apimodule.ApiBase.ApiBean.WalletList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletList {

    @SerializedName("page_flag")
    @Expose
    private String page_flag;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public String getPage_flag() {
        return page_flag;
    }

    public void setPage_flag(String page_flag) {
        this.page_flag = page_flag;
    }

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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

}
