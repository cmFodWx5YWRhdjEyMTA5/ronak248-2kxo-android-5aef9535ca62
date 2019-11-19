package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham Agarwal on 20/12/16.
 */
public class Friend {
    @SerializedName("userid")
    @Expose
    private Integer userid;
    @SerializedName("isfriend")
    @Expose
    private Integer isfriend;
    @SerializedName("friendshipid")
    @Expose
    private Integer friendshipid;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("issent")
    @Expose
    private Integer issent=0;

    public Integer getIssent() {
        return issent;
    }

    public void setIssent(Integer issent) {
        this.issent = issent;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(Integer isfriend) {
        this.isfriend = isfriend;
    }

    public Integer getFriendshipid() {
        return friendshipid;
    }

    public void setFriendshipid(Integer friendshipid) {
        this.friendshipid = friendshipid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
