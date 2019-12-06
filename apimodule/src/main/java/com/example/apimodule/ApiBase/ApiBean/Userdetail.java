package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham Agarwal on 07/11/16.
 */
public class Userdetail {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("wallet_amount")
    @Expose
    private Integer walletAmount;

    public Integer getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(Integer walletAmount) {
        this.walletAmount = walletAmount;
    }

    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("facebook_token")
    @Expose
    private String facebookToken;
    @SerializedName("twitter_token")
    @Expose
    private String twitterToken;
    @SerializedName("google_token")
    @Expose
    private String googleToken;
    @SerializedName("isverified")
    @Expose
    private String isverified;
    @SerializedName("isdeleted")
    @Expose
    private String isdeleted;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("deleteddate")
    @Expose
    private String deleteddate;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("nationality")
    @Expose
    private String nationality;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("freeze_status")
    @Expose
    private int freezeStatus;

    @SerializedName("hold_amount")
    @Expose
    private String holdAmount;


    public int getFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(int freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    public String getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(String holdAmount) {
        this.holdAmount = holdAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @SerializedName("realtionstatus")
    @Expose
    private String realtionstatus;
    @SerializedName("sexpreference")
    @Expose
    private String sexpreference;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("hobbies")
    @Expose
    private String hobbies;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("photothumb")
    @Expose
    private String photothumb;
    @SerializedName("users_info")
    @Expose
    private String usersInfo;
    @SerializedName("users_shop")
    @Expose
    private String usersShop;
    @SerializedName("users_buffet")
    @Expose
    private String usersBuffet;
    @SerializedName("users_media")
    @Expose
    private String usersMedia;

    @SerializedName("total_item")
    @Expose
    private int totalItem;

    @SerializedName("block")
    @Expose
    private int block;


    @SerializedName("total_friend")
    @Expose
    private int totalFriend;
    @SerializedName("totalpostcomments")
    @Expose
    private int totalpostcomments;
    @SerializedName("totalpostlikes")
    @Expose
    private int totalpostlikes;
    @SerializedName("totalsolditem")
    @Expose
    private int totalsolditem;
    @SerializedName("totalpurchaseditem")
    @Expose
    private int totalpurchaseditem;

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public int getTotalFriend() {
        return totalFriend;
    }

    public void setTotalFriend(int totalFriend) {
        this.totalFriend = totalFriend;
    }

    public int getTotalpostcomments() {
        return totalpostcomments;
    }

    public void setTotalpostcomments(int totalpostcomments) {
        this.totalpostcomments = totalpostcomments;
    }

    public int getTotalpostlikes() {
        return totalpostlikes;
    }

    public void setTotalpostlikes(int totalpostlikes) {
        this.totalpostlikes = totalpostlikes;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }


    public int getTotalsolditem() {
        return totalsolditem;
    }

    public void setTotalsolditem(int totalsolditem) {
        this.totalsolditem = totalsolditem;
    }

    public int getTotalpurchaseditem() {
        return totalpurchaseditem;
    }

    public void setTotalpurchaseditem(int totalpurchaseditem) {
        this.totalpurchaseditem = totalpurchaseditem;
    }

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
     * @return The firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname The firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return The lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname The lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
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
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return The facebookToken
     */
    public String getFacebookToken() {
        return facebookToken;
    }

    /**
     * @param facebookToken The facebook_token
     */
    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    /**
     * @return The twitterToken
     */
    public String getTwitterToken() {
        return twitterToken;
    }

    /**
     * @param twitterToken The twitter_token
     */
    public void setTwitterToken(String twitterToken) {
        this.twitterToken = twitterToken;
    }

    /**
     * @return The googleToken
     */
    public String getGoogleToken() {
        return googleToken;
    }

    /**
     * @param googleToken The google_token
     */
    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }

    /**
     * @return The isverified
     */
    public String getIsverified() {
        return isverified;
    }

    /**
     * @param isverified The isverified
     */
    public void setIsverified(String isverified) {
        this.isverified = isverified;
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
     * @return The deleteddate
     */
    public String getDeleteddate() {
        return deleteddate;
    }

    /**
     * @param deleteddate The deleteddate
     */
    public void setDeleteddate(String deleteddate) {
        this.deleteddate = deleteddate;
    }

    /**
     * @return The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return The nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * @param nationality The nationality
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * @return The realtionstatus
     */
    public String getRealtionstatus() {
        return realtionstatus;
    }

    /**
     * @param realtionstatus The realtionstatus
     */
    public void setRealtionstatus(String realtionstatus) {
        this.realtionstatus = realtionstatus;
    }

    /**
     * @return The sexpreference
     */
    public String getSexpreference() {
        return sexpreference;
    }

    /**
     * @param sexpreference The sexpreference
     */
    public void setSexpreference(String sexpreference) {
        this.sexpreference = sexpreference;
    }

    /**
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return The city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return The hobbies
     */
    public String getHobbies() {
        return hobbies;
    }

    /**
     * @param hobbies The hobbies
     */
    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    /**
     * @return The school
     */
    public String getSchool() {
        return school;
    }

    /**
     * @param school The school
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * @return The job
     */
    public String getJob() {
        return job;
    }

    /**
     * @param job The job
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * @return The photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * @param photo The photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * @return The photothumb
     */
    public String getPhotothumb() {
        return photothumb;
    }

    /**
     * @param photothumb The photothumb
     */
    public void setPhotothumb(String photothumb) {
        this.photothumb = photothumb;
    }

    /**
     * @return The usersInfo
     */
    public String getUsersInfo() {
        return usersInfo;
    }

    /**
     * @param usersInfo The users_info
     */
    public void setUsersInfo(String usersInfo) {
        this.usersInfo = usersInfo;
    }

    /**
     * @return The usersShop
     */
    public String getUsersShop() {
        return usersShop;
    }

    /**
     * @param usersShop The users_shop
     */
    public void setUsersShop(String usersShop) {
        this.usersShop = usersShop;
    }

    /**
     * @return The usersBuffet
     */
    public String getUsersBuffet() {
        return usersBuffet;
    }

    /**
     * @param usersBuffet The users_buffet
     */
    public void setUsersBuffet(String usersBuffet) {
        this.usersBuffet = usersBuffet;
    }

    /**
     * @return The usersMedia
     */
    public String getUsersMedia() {
        return usersMedia;
    }

    /**
     * @param usersMedia The users_media
     */
    public void setUsersMedia(String usersMedia) {
        this.usersMedia = usersMedia;
    }


    @SerializedName("userid")
    @Expose
    private Integer userid;

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


}
