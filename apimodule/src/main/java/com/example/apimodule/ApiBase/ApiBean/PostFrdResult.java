package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shubham Agarwal on 26/12/16.
 */
public class PostFrdResult {
    @SerializedName("googlefriends")
    @Expose
    private ArrayList<Emailfriend> googlefriends = null;
    @SerializedName("fbfriends")
    @Expose
    private ArrayList<Emailfriend> fbfriends = null;
    @SerializedName("emailfriends")
    @Expose
    private ArrayList<Emailfriend> emailfriends = null;
    @SerializedName("twitterfriends")
    @Expose
    private ArrayList<Emailfriend> twitterfriends = null;

    public ArrayList<Emailfriend> getGooglefriends() {
        return googlefriends;
    }

    public void setGooglefriends(ArrayList<Emailfriend> googlefriends) {
        this.googlefriends = googlefriends;
    }

    public ArrayList<Emailfriend> getFbfriends() {
        return fbfriends;
    }

    public void setFbfriends(ArrayList<Emailfriend> fbfriends) {
        this.fbfriends = fbfriends;
    }

    public ArrayList<Emailfriend> getEmailfriends() {
        return emailfriends;
    }

    public void setEmailfriends(ArrayList<Emailfriend> emailfriends) {
        this.emailfriends = emailfriends;
    }

    public ArrayList<Emailfriend> getTwitterfriends() {
        return twitterfriends;
    }

    public void setTwitterfriends(ArrayList<Emailfriend> twitterfriends) {
        this.twitterfriends = twitterfriends;
    }

}
