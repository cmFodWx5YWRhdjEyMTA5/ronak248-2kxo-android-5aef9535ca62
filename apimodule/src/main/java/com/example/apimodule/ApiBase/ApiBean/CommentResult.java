package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shubham Agarwal on 03/11/16.
 */
public class CommentResult {
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
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    @SerializedName("media")
    @Expose
    private ArrayList<Medias> media = new ArrayList<Medias>();
    @SerializedName("commentcount")
    @Expose
    private Integer commentcount;
    @SerializedName("likecount")
    @Expose
    private Integer likecount;
    @SerializedName("islike")
    @Expose
    private int islike;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The postTitle
     */
    public String getPostTitle() {
        return postTitle;
    }

    /**
     * @param postTitle The post_title
     */
    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    /**
     * @return The postedBy
     */
    public String getPostedBy() {
        return postedBy;
    }

    /**
     * @param postedBy The posted_by
     */
    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    /**
     * @return The postType
     */
    public String getPostType() {
        return postType;
    }

    /**
     * @param postType The post_type
     */
    public void setPostType(String postType) {
        this.postType = postType;
    }

    /**
     * @return The createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate The created_date
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return The updatedDate
     */
    public String getUpdatedDate() {
        return updatedDate;
    }

    /**
     * @param updatedDate The updated_date
     */
    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * @return The isdeleted
     */
    public String getIsdeleted() {
        return isdeleted;
    }

    /**
     * @param isdeleted The isdeleted
     */
    public void setIsdeleted(String isdeleted) {
        this.isdeleted = isdeleted;
    }

    /**
     * @return The deletedDate
     */
    public String getDeletedDate() {
        return deletedDate;
    }

    /**
     * @param deletedDate The deleted_date
     */
    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

    /**
     * @return The mypost
     */
    public Integer getMypost() {
        return mypost;
    }

    /**
     * @param mypost The mypost
     */
    public void setMypost(Integer mypost) {
        this.mypost = mypost;
    }

    /**
     * @return The userid
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * @param userid The userid
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * @return The username
     */
    public String getUsername() {
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
     * @return The userphotothumb
     */
    public String getUserphotothumb() {
        return userphotothumb;
    }

    /**
     * @param userphotothumb The userphotothumb
     */
    public void setUserphotothumb(String userphotothumb) {
        this.userphotothumb = userphotothumb;
    }

    /**
     * @return The usercity
     */
    public String getUsercity() {
        return usercity;
    }

    /**
     * @param usercity The usercity
     */
    public void setUsercity(String usercity) {
        this.usercity = usercity;
    }

    /**
     * @return The comments
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    /**
     * @param comments The comments
     */
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    /**
     * @return The media
     */
    public ArrayList<Medias> getMedia() {
        return media;
    }

    /**
     * @param media The media
     */
    public void setMedia(ArrayList<Medias> media) {
        this.media = media;
    }

    /**
     * @return The commentcount
     */
    public Integer getCommentcount() {
        return commentcount;
    }

    /**
     * @param commentcount The commentcount
     */
    public void setCommentcount(Integer commentcount) {
        this.commentcount = commentcount;
    }

    /**
     * @return The likecount
     */
    public Integer getLikecount() {
        return likecount;
    }

    /**
     * @param likecount The likecount
     */
    public void setLikecount(Integer likecount) {
        this.likecount = likecount;
    }

    public int getIslike() {
        return islike;
    }

    /**
     * @param islike The islike
     */
    public void setIslike(int islike) {
        this.islike = islike;
    }

}
