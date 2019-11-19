package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 13/12/16.
 */

public class GetUserResult {

    @SerializedName("userdetail")
    @Expose
    private Userdetail userdetail;

    /**
     *
     * @return
     * The userdetail
     */
    public Userdetail getUserdetail() {
        return userdetail;
    }

    /**
     *
     * @param userdetail
     * The userdetail
     */
    public void setUserdetail(Userdetail userdetail) {
        this.userdetail = userdetail;
    }
}
