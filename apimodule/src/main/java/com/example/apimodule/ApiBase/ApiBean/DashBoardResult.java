package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shubham Agarwal on 17/10/16.
 */
public class DashBoardResult {

    @SerializedName("streampost")
    @Expose
    private ArrayList<Streampost> streampost = new ArrayList<>();
    @SerializedName("mediapost")
    @Expose
    private ArrayList<Mediapost> mediapost = new ArrayList<>();
    @SerializedName("itemdetails")
    @Expose
    private ArrayList<Itemdetail> itemdetails = new ArrayList<>();
    @SerializedName("mediapostcount")
    @Expose
    private int mediapostcount;
    @SerializedName("streampostcount")
    @Expose
    private int streampostcount;
    @SerializedName("itemcount")
    @Expose
    private int itemcount;
    @SerializedName("totalcount")
    @Expose
    private Integer totalcount;

    public ArrayList<Streampost> getStreampost() {
        return streampost;
    }

    public ArrayList<Mediapost> getMediapost() {
        return mediapost;
    }

    public ArrayList<Itemdetail> getItemdetails() {
        return itemdetails;
    }

    public int getMediapostcount() {
        return mediapostcount;
    }

    public void setMediapostcount(int mediapostcount) {
        this.mediapostcount = mediapostcount;
    }

    public int getStreampostcount() {
        return streampostcount;
    }

    public int getItemcount() {
        return itemcount;
    }

    public Integer getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(Integer totalcount) {
        this.totalcount = totalcount;
    }

}
