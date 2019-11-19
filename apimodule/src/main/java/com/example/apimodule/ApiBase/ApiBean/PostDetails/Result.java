
package com.example.apimodule.ApiBase.ApiBean.PostDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("post_title")
    @Expose
    private String postTitle;
    @SerializedName("posted_by")
    @Expose
    private String postedBy;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("post_tagids")
    @Expose
    private String postTagids;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("isdeleted")
    @Expose
    private String isdeleted;
    @SerializedName("deleted_date")
    @Expose
    private String deletedDate;
    @SerializedName("post_oldtitle")
    @Expose
    private String postOldtitle;
    @SerializedName("mypost")
    @Expose
    private Integer mypost;
    @SerializedName("userid")
    @Expose
    private Integer userid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("userphoto")
    @Expose
    private String userphoto;
    @SerializedName("userphotothumb")
    @Expose
    private String userphotothumb;
    @SerializedName("usercity")
    @Expose
    private String usercity;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;
    @SerializedName("media")
    @Expose
    private List<Medium> media = null;
    @SerializedName("commentcount")
    @Expose
    private Integer commentcount;
    @SerializedName("likecount")
    @Expose
    private Integer likecount;
    @SerializedName("islike")
    @Expose
    private Integer islike;
    @SerializedName("iscomment")
    @Expose
    private Integer iscomment;

    public Integer getId() {
        return id;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostTagids() {
        return postTagids;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public String getIsdeleted() {
        return isdeleted;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public String getPostOldtitle() {
        return postOldtitle;
    }

    public Integer getMypost() {
        return mypost;
    }

    public Integer getUserid() {
        return userid;
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

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public String getUserphotothumb() {
        return userphotothumb;
    }

    public String getUsercity() {
        return usercity;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Medium> getMedia() {
        return media;
    }

    public Integer getCommentcount() {
        return commentcount;
    }

    public Integer getLikecount() {
        return likecount;
    }

    public void setLikecount(Integer likecount) {
        this.likecount = likecount;
    }

    public Integer getIslike() {
        return islike;
    }

    public void setIslike(Integer islike) {
        this.islike = islike;
    }

    public Integer getIscomment() {
        return iscomment;
    }

}
