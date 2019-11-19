package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 07/11/16.
 */

public class PostMediaResult {
    @SerializedName("pid")
    @Expose
    private int pid;

    /**
     *
     * @return
     * The pid
     */
    public int getPid() {
        return pid;
    }

    /**
     *
     * @param pid
     * The pid
     */
    public void setPid(int pid) {
        this.pid = pid;
    }

}

