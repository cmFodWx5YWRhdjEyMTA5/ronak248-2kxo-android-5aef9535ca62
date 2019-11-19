package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham Agarwal on 26/12/16.
 */
public class Emailfriend {
    @SerializedName("userid")
    @Expose
    private Integer userid;
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
    @SerializedName("fbid")
    @Expose
    private String fbid;
    @SerializedName("googleid")
    @Expose
    private String googleid;
    @SerializedName("twitterid")
    @Expose
    private String twitterid;
    @SerializedName("isfriend")
    @Expose
    private Integer isfriend;
    @SerializedName("friendshipid")
    @Expose
    private Integer friendshipid;
    @SerializedName("issent")
    @Expose
    private Integer issent;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
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

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getGoogleid() {
        return googleid;
    }

    public String getTwitterid() {
        return twitterid;
    }

    public Integer getIsfriend() {
        return isfriend;
    }

    public Integer getFriendshipid() {
        return friendshipid;
    }

    public Integer getIssent() {
        return issent;
    }

}
