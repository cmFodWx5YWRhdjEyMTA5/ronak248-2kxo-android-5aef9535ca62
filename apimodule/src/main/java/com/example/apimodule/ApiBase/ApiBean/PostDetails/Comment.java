
package com.example.apimodule.ApiBase.ApiBean.PostDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("commentid")
    @Expose
    private String commentid;
    @SerializedName("commentdesc")
    @Expose
    private String commentdesc;
    @SerializedName("post_comment_tagids")
    @Expose
    private Object postCommentTagids;
    @SerializedName("commentolddesc")
    @Expose
    private String commentolddesc;
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
    private Integer userid;
    @SerializedName("userphoto")
    @Expose
    private String userphoto;
    @SerializedName("commenttime")
    @Expose
    private String commenttime;

    public String getCommentid() {
        return commentid;
    }

    public String getCommentdesc() {
        return commentdesc;
    }

    public Object getPostCommentTagids() {
        return postCommentTagids;
    }

    public String getCommentolddesc() {
        return commentolddesc;
    }

    public String getUsername() {
        return username;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public Integer getUserid() {
        return userid;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public String getCommenttime() {
        return commenttime;
    }

}
