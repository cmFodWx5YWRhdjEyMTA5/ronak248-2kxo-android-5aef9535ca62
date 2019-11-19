package com.example.apimodule.ApiBase.ApiBean.Tracking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parangat on 17/10/17.
 */

public class UpdateTrackingDetailReposne {
    @SuppressWarnings("unused")
    private static final String TAG = "UpdateTrackingDetailRep";

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String msg;
    @SerializedName("result")
    @Expose
    public Result result;

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
