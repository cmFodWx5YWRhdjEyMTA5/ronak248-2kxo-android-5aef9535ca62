package com.example.apimodule.ApiBase.ApiBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Parcelable {
    @SerializedName("commentid")
    @Expose
    private String commnetId;

    public String getCommnetId() {
        return commnetId;
    }

    public void setCommnetId(String commnetId) {
        this.commnetId = commnetId;
    }

    @SerializedName("uid")
    @Expose
    private Integer uid;

    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @SerializedName("wallet_amount")

    @Expose
    private Integer walletAmount;
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
    private String nationality;
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
    private String devicetoken;
    @SerializedName("devicetype")
    @Expose
    private String devicetype;
    @SerializedName("verification_token")
    @Expose
    private String verificationToken;
    @SerializedName("token")
    @Expose
    private Integer token;
    @SerializedName("isverified")
    @Expose
    private Integer isverified;
    @SerializedName("lastlogin")
    @Expose
    private String lastlogin;
    @SerializedName("badge")
    @Expose
    private Integer badge;
    @SerializedName("paypalacc")
    @Expose
    private String paypalacc;
    @SerializedName("bitcoinacc")
    @Expose
    private String bitcoinacc;
    @SerializedName("alipayacc")
    @Expose
    private String alipayacc;
    @SerializedName("wechatacc")
    @Expose
    private String wechatacc;
    @SerializedName("stripe_customer_id")
    @Expose
    private String stripeCustomerId;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("isdeleted")
    @Expose
    private Integer isdeleted;
    @SerializedName("deleteddate")
    @Expose
    private String deleteddate;

    @SerializedName("suspend_status")
    @Expose
    private String suspendStatus;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("state")
    @Expose
    private String state;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @SerializedName("userstatus")
    @Expose
    private String userStatus;

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getId() {
        if (id == null && uid != null)
            id = uid;
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(Integer walletAmount) {
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
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

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
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

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public Integer getIsverified() {
        return isverified;
    }

    public void setIsverified(Integer isverified) {
        this.isverified = isverified;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }

    public Integer getBadge() {
        return badge;
    }

    public void setBadge(Integer badge) {
        this.badge = badge;
    }

    public String getPaypalacc() {
        return paypalacc;
    }

    public void setPaypalacc(String paypalacc) {
        this.paypalacc = paypalacc;
    }

    public String getBitcoinacc() {
        return bitcoinacc;
    }

    public void setBitcoinacc(String bitcoinacc) {
        this.bitcoinacc = bitcoinacc;
    }

    public String getAlipayacc() {
        return alipayacc;
    }

    public void setAlipayacc(String alipayacc) {
        this.alipayacc = alipayacc;
    }

    public String getWechatacc() {
        return wechatacc;
    }

    public void setWechatacc(String wechatacc) {
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

    public Integer getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Integer isdeleted) {
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

    protected Result(Parcel in) {
        this.commnetId = in.readString();
        uid = in.readByte() == 0x00 ? null : in.readInt();
        id = in.readByte() == 0x00 ? null : in.readInt();
        walletAmount = in.readByte() == 0x00 ? null : in.readInt();
        fname = in.readString();
        lname = in.readString();
        username = in.readString();
        email = in.readString();
        photo = in.readString();
        gender = in.readString();
        phone = in.readString();
        address = (Object) in.readValue(Object.class.getClassLoader());
        city = in.readString();
        nationality = in.readString();
        school = in.readString();
        job = in.readString();
        lat = in.readString();
        lon = in.readString();
        hobbies = in.readString();
        realtionstatus = in.readString();
        sexpreference = in.readString();
        shippingAddress = in.readString();
        facebookToken = (Object) in.readValue(Object.class.getClassLoader());
        twitterToken = (Object) in.readValue(Object.class.getClassLoader());
        googleToken = (Object) in.readValue(Object.class.getClassLoader());
        devicetoken = in.readString();
        devicetype = in.readString();
        verificationToken = in.readString();
        token = in.readByte() == 0x00 ? null : in.readInt();
        isverified = in.readByte() == 0x00 ? null : in.readInt();
        lastlogin = in.readString();
        badge = in.readByte() == 0x00 ? null : in.readInt();
        paypalacc = in.readString();
        bitcoinacc = in.readString();
        alipayacc = in.readString();
        wechatacc = in.readString();
        stripeCustomerId = in.readString();
        createdDate = in.readString();
        isdeleted = in.readByte() == 0x00 ? null : in.readInt();
        deleteddate = in.readString();
        suspendStatus = in.readString();
        userStatus= in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.commnetId);
        if (uid == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(uid);
        }
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (walletAmount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(walletAmount);
        }
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(photo);
        dest.writeString(gender);
        dest.writeString(phone);
        dest.writeValue(address);
        dest.writeString(city);
        dest.writeString(nationality);
        dest.writeString(school);
        dest.writeString(job);
        dest.writeString(lat);
        dest.writeString(lon);
        dest.writeString(hobbies);
        dest.writeString(realtionstatus);
        dest.writeString(sexpreference);
        dest.writeString(shippingAddress);
        dest.writeValue(facebookToken);
        dest.writeValue(twitterToken);
        dest.writeValue(googleToken);
        dest.writeString(devicetoken);
        dest.writeString(devicetype);
        dest.writeString(verificationToken);
        if (token == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(token);
        }
        if (isverified == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(isverified);
        }
        dest.writeString(lastlogin);
        if (badge == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(badge);
        }
        dest.writeString(paypalacc);
        dest.writeString(bitcoinacc);
        dest.writeString(alipayacc);
        dest.writeString(wechatacc);
        dest.writeString(stripeCustomerId);
        dest.writeString(createdDate);
        if (isdeleted == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(isdeleted);
        }
        dest.writeString(deleteddate);
        dest.writeString(suspendStatus);
        dest.writeString(userStatus);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}