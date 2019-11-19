package com.example.apimodule.ApiBase.ApiBean.Setting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user_notification_comment")
    @Expose
    public String userNotificationComment;
    @SerializedName("user_notification_like")
    @Expose
    public String userNotificationLike;
    @SerializedName("user_notification_newcontact")
    @Expose
    public String userNotificationNewcontact;
    @SerializedName("users_buffet")
    @Expose
    public String usersBuffet;
    @SerializedName("users_shop")
    @Expose
    public String usersShop;
    @SerializedName("users_media")
    @Expose
    public String usersMedia;
    @SerializedName("users_info")
    @Expose
    public String usersInfo;
    @SerializedName("updated_date")
    @Expose
    public String updatedDate;

    public int getId() {
        return id;
    }

    public String getUserNotificationComment() {
        return userNotificationComment;
    }

    public String getUserNotificationLike() {
        return userNotificationLike;
    }

    public String getUserNotificationNewcontact() {
        return userNotificationNewcontact;
    }

    public String getUsersBuffet() {
        return usersBuffet;
    }

    public String getUsersShop() {
        return usersShop;
    }

    public String getUsersMedia() {
        return usersMedia;
    }

    public String getUsersInfo() {
        return usersInfo;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }
}