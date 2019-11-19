package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SoldDetail {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("wallet_amount")
    @Expose
    private int walletAmount;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("nationality")
    @Expose
    private Object nationality;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("hobbies")
    @Expose
    private String hobbies;
    @SerializedName("realtionstatus")
    @Expose
    private String realtionstatus;
    @SerializedName("sexpreference")
    @Expose
    private String sexpreference;
    @SerializedName("shipping_address")
    @Expose
    private String shippingAddress;
    @SerializedName("facebook_token")
    @Expose
    private Object facebookToken;
    @SerializedName("twitter_token")
    @Expose
    private Object twitterToken;
    @SerializedName("google_token")
    @Expose
    private Object googleToken;
    @SerializedName("devicetoken")
    @Expose
    private Object devicetoken;
    @SerializedName("devicetype")
    @Expose
    private String devicetype;
    @SerializedName("verification_token")
    @Expose
    private String verificationToken;
    @SerializedName("token")
    @Expose
    private Object token;
    @SerializedName("isverified")
    @Expose
    private int isverified;
    @SerializedName("lastlogin")
    @Expose
    private String lastlogin;
    @SerializedName("badge")
    @Expose
    private int badge;
    @SerializedName("paypalacc")
    @Expose
    private Object paypalacc;
    @SerializedName("bitcoinacc")
    @Expose
    private Object bitcoinacc;
    @SerializedName("alipayacc")
    @Expose
    private Object alipayacc;
    @SerializedName("wechatacc")
    @Expose
    private Object wechatacc;
    @SerializedName("stripe_customer_id")
    @Expose
    private String stripeCustomerId;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("isdeleted")
    @Expose
    private int isdeleted;
    @SerializedName("deleteddate")
    @Expose
    private String deleteddate;
    @SerializedName("suspend_status")
    @Expose
    private String suspendStatus;
    @SerializedName("users_buffet")
    @Expose
    private int usersBuffet;
    @SerializedName("users_info")
    @Expose
    private int usersInfo;
    @SerializedName("users_media")
    @Expose
    private int usersMedia;
    @SerializedName("users_shop")
    @Expose
    private int usersShop;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(int walletAmount) {
        this.walletAmount = walletAmount;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Object getNationality() {
        return nationality;
    }

    public void setNationality(Object nationality) {
        this.nationality = nationality;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getRealtionstatus() {
        return realtionstatus;
    }

    public void setRealtionstatus(String realtionstatus) {
        this.realtionstatus = realtionstatus;
    }

    public String getSexpreference() {
        return sexpreference;
    }

    public void setSexpreference(String sexpreference) {
        this.sexpreference = sexpreference;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Object getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(Object facebookToken) {
        this.facebookToken = facebookToken;
    }

    public Object getTwitterToken() {
        return twitterToken;
    }

    public void setTwitterToken(Object twitterToken) {
        this.twitterToken = twitterToken;
    }

    public Object getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(Object googleToken) {
        this.googleToken = googleToken;
    }

    public Object getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(Object devicetoken) {
        this.devicetoken = devicetoken;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }

    public int getIsverified() {
        return isverified;
    }

    public void setIsverified(int isverified) {
        this.isverified = isverified;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public Object getPaypalacc() {
        return paypalacc;
    }

    public void setPaypalacc(Object paypalacc) {
        this.paypalacc = paypalacc;
    }

    public Object getBitcoinacc() {
        return bitcoinacc;
    }

    public void setBitcoinacc(Object bitcoinacc) {
        this.bitcoinacc = bitcoinacc;
    }

    public Object getAlipayacc() {
        return alipayacc;
    }

    public void setAlipayacc(Object alipayacc) {
        this.alipayacc = alipayacc;
    }

    public Object getWechatacc() {
        return wechatacc;
    }

    public void setWechatacc(Object wechatacc) {
        this.wechatacc = wechatacc;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(int isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getDeleteddate() {
        return deleteddate;
    }

    public void setDeleteddate(String deleteddate) {
        this.deleteddate = deleteddate;
    }

    public String getSuspendStatus() {
        return suspendStatus;
    }

    public void setSuspendStatus(String suspendStatus) {
        this.suspendStatus = suspendStatus;
    }

    public int getUsersBuffet() {
        return usersBuffet;
    }

    public void setUsersBuffet(int usersBuffet) {
        this.usersBuffet = usersBuffet;
    }

    public int getUsersInfo() {
        return usersInfo;
    }

    public void setUsersInfo(int usersInfo) {
        this.usersInfo = usersInfo;
    }

    public int getUsersMedia() {
        return usersMedia;
    }

    public void setUsersMedia(int usersMedia) {
        this.usersMedia = usersMedia;
    }

    public int getUsersShop() {
        return usersShop;
    }

    public void setUsersShop(int usersShop) {
        this.usersShop = usersShop;
    }
}
