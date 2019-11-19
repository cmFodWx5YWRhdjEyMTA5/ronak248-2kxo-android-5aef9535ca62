package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shubham Agarwal on 20/12/16.
 */
public class FriendResult {

    @SerializedName(value = "totalcount" ,alternate = {"totalrequests","friendid"})
    @Expose
    private Integer totalcount=0;
    @SerializedName("friends")
    @Expose
    private ArrayList<Friend> friends = null;

    public Integer getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(Integer totalcount) {
        this.totalcount = totalcount;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

}
