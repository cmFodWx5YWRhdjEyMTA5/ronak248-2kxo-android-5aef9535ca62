package com.example.apimodule.ApiBase.ApiBean.PostDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostDetailResult {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("post_title")
    @Expose
    private String postTitle;
    @SerializedName("post_tagids")
    @Expose
    private String postTagids;
    @SerializedName("post_type")
    @Expose
    private int postType;
    @SerializedName("posted_by")
    @Expose
    private int postedBy;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("isdeleted")
    @Expose
    private int isdeleted;
    @SerializedName("deleted_date")
    @Expose
    private Object deletedDate;
    @SerializedName("post_oldtitle")
    @Expose
    private String postOldtitle;
    @SerializedName("userid")
    @Expose
    private int userid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("usercity")
    @Expose
    private Object usercity;
    @SerializedName("userphoto")
    @Expose
    private String userphoto;
    @SerializedName("userphotothumb")
    @Expose
    private String userphotothumb;
    @SerializedName("mypost")
    @Expose
    private int mypost;
    @SerializedName("commenttime")
    @Expose
    private String commenttime;
    @SerializedName("media")
    @Expose
    private ArrayList<PostMedium> media = null;
    @SerializedName("likecount")
    @Expose
    private int likecount;
    @SerializedName("islike")
    @Expose
    private int islike;
    @SerializedName("comments")
    @Expose
    private ArrayList<Comment> comments = null;
    @SerializedName("commentcount")
    @Expose
    private int commentcount;
    @SerializedName("iscomment")
    @Expose
    private int iscomment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostTagids() {
        return postTagids;
    }

    public void setPostTagids(String postTagids) {
        this.postTagids = postTagids;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public int getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(int postedBy) {
        this.postedBy = postedBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(int isdeleted) {
        this.isdeleted = isdeleted;
    }

    public Object getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Object deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getPostOldtitle() {
        return postOldtitle;
    }

    public void setPostOldtitle(String postOldtitle) {
        this.postOldtitle = postOldtitle;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Object getUsercity() {
        return usercity;
    }

    public void setUsercity(Object usercity) {
        this.usercity = usercity;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getUserphotothumb() {
        return userphotothumb;
    }

    public void setUserphotothumb(String userphotothumb) {
        this.userphotothumb = userphotothumb;
    }

    public int getMypost() {
        return mypost;
    }

    public void setMypost(int mypost) {
        this.mypost = mypost;
    }

    public String getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(String commenttime) {
        this.commenttime = commenttime;
    }

    public ArrayList<PostMedium> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<PostMedium> media) {
        this.media = media;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public int getIscomment() {
        return iscomment;
    }

    public void setIscomment(int iscomment) {
        this.iscomment = iscomment;
    }
}
