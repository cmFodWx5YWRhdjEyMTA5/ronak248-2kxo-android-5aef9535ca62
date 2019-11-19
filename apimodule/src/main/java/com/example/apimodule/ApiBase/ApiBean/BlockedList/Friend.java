
package com.example.apimodule.ApiBase.ApiBean.BlockedList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Friend withUserid(Integer userid) {
        this.userid = userid;
        return this;
    }

    public Integer getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(Integer isfriend) {
        this.isfriend = isfriend;
    }

    public Friend withIsfriend(Integer isfriend) {
        this.isfriend = isfriend;
        return this;
    }

    public Integer getFriendshipid() {
        return friendshipid;
    }

    public void setFriendshipid(Integer friendshipid) {
        this.friendshipid = friendshipid;
    }

    public Friend withFriendshipid(Integer friendshipid) {
        this.friendshipid = friendshipid;
        return this;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Friend withFname(String fname) {
        this.fname = fname;
        return this;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Friend withLname(String lname) {
        this.lname = lname;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Friend withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Friend withPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Friend withCity(String city) {
        this.city = city;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Friend withAddress(String address) {
        this.address = address;
        return this;
    }

}
