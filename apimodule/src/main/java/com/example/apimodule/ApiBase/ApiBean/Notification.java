package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("userdata")
    @Expose
    private Userdetail userdata;
    @SerializedName("itemdata")
    @Expose
    private Itemdetail itemdata;
    @SerializedName("notificationsdetail")
    @Expose
    private String notificationsdetail;
    @SerializedName("notificationstype")
    @Expose
    private String notificationstype;
    @SerializedName("notificationstime")
    @Expose
    private String notificationstime;
    @SerializedName("nid")
    @Expose
    private Integer nid;

    @SerializedName("post_type")
    @Expose
    private Integer post_type;


    public Integer getPost_type() {
        return post_type;
    }

    public void setPost_type(Integer post_type) {
        this.post_type = post_type;
    }

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    @SerializedName("postid")
    @Expose
    private Integer postid;

    public Userdetail getUserdata() {
        return userdata;
    }

    public void setUserdata(Userdetail userdata) {
        this.userdata = userdata;
    }

    public Itemdetail getItemdata() {
        return itemdata;
    }

    public void setItemdata(Itemdetail itemdata) {
        this.itemdata = itemdata;
    }

    public String getNotificationsdetail() {
        return notificationsdetail;
    }

    public void setNotificationsdetail(String notificationsdetail) {
        this.notificationsdetail = notificationsdetail;
    }

    public String getNotificationstype() {
        return notificationstype;
    }

    public void setNotificationstype(String notificationstype) {
        this.notificationstype = notificationstype;
    }

    public String getNotificationstime() {
        return notificationstime;
    }

    public void setNotificationstime(String notificationstime) {
        this.notificationstime = notificationstime;
    }

    public Integer getNid() {
        return nid;
    }

    public void setNid(Integer nid) {
        this.nid = nid;
    }

}