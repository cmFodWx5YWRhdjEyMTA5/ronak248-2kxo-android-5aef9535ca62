
package com.example.apimodule.ApiBase.ApiBean.BlockedList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("totalcount")
    @Expose
    private Integer totalcount;
    @SerializedName("friends")
    @Expose
    private List<Friend> friends = null;

    public Integer getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(Integer totalcount) {
        this.totalcount = totalcount;
    }

    public Result withTotalcount(Integer totalcount) {
        this.totalcount = totalcount;
        return this;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public Result withFriends(List<Friend> friends) {
        this.friends = friends;
        return this;
    }

}
