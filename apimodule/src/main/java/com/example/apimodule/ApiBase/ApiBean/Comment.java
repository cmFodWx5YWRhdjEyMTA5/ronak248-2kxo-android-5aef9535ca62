package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("commentid")
    @Expose
    private String commentid;
    @SerializedName("commentdesc")
    @Expose
    private String commentdesc;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("userphoto")
    @Expose
    private String userphoto;
    @SerializedName("commenttime")
    @Expose
    private String commenttime;

    /**
     * @return The commentid
     */
    public String getCommentid() {
        return commentid;
    }

    /**
     * @param commentid The commentid
     */
    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    /**
     * @return The commentdesc
     */
    public String getCommentdesc() {
        return commentdesc;
    }

    /**
     * @param commentdesc The commentdesc
     */
    public void setCommentdesc(String commentdesc) {
        this.commentdesc = commentdesc;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        if (username == null) {
            return "";
        }
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname The fname
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return The lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname The lname
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @return The userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid The userid
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * @return The userphoto
     */
    public String getUserphoto() {
        return userphoto;
    }

    /**
     * @param userphoto The userphoto
     */
    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    /**
     * @return The commenttime
     */
    public String getCommenttime() {
        return commenttime;
    }

    /**
     * @param commenttime The commenttime
     */
    public void setCommenttime(String commenttime) {
        this.commenttime = commenttime;
    }

}