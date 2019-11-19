package com.example.apimodule.ApiBase.ApiBean.shop_dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ShopDashboard  implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private Result result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result  implements Serializable{

        @SerializedName("watched")
        @Expose
        private Watched watched;
        @SerializedName("recommend")
        @Expose
        private Recommend recommend;
        @SerializedName("recentitems")
        @Expose
        private Recentitems recentitems;
        @SerializedName("globaltrend")
        @Expose
        private Globaltrend globaltrend;

        public Watched getWatched() {
            return watched;
        }

        public void setWatched(Watched watched) {
            this.watched = watched;
        }

        public Recommend getRecommend() {
            return recommend;
        }

        public void setRecommend(Recommend recommend) {
            this.recommend = recommend;
        }

        public Recentitems getRecentitems() {
            return recentitems;
        }

        public void setRecentitems(Recentitems recentitems) {
            this.recentitems = recentitems;
        }

        public Globaltrend getGlobaltrend() {
            return globaltrend;
        }

        public void setGlobaltrend(Globaltrend globaltrend) {
            this.globaltrend = globaltrend;
        }

    }

    public class Watched implements Serializable{

        @SerializedName("itemdetails")
        @Expose
        private List<Itemdetail> itemdetails = null;
        @SerializedName("itemcount")
        @Expose
        private Integer itemcount;

        public List<Itemdetail> getItemdetails() {
            return itemdetails;
        }

        public void setItemdetails(List<Itemdetail> itemdetails) {
            this.itemdetails = itemdetails;
        }

        public Integer getItemcount() {
            return itemcount;
        }

        public void setItemcount(Integer itemcount) {
            this.itemcount = itemcount;
        }
    }

    public class Recommend implements Serializable{

        @SerializedName("itemdetails")
        @Expose
        private List<Object> itemdetails = null;
        @SerializedName("itemcount")
        @Expose
        private Integer itemcount;

        public List<Object> getItemdetails() {
            return itemdetails;
        }

        public void setItemdetails(List<Object> itemdetails) {
            this.itemdetails = itemdetails;
        }

        public Integer getItemcount() {
            return itemcount;
        }

        public void setItemcount(Integer itemcount) {
            this.itemcount = itemcount;
        }

    }

    public class Recentitems implements Serializable{

        @SerializedName("itemdetails")
        @Expose
        private List<Itemdetail_> itemdetails = null;
        @SerializedName("itemcount")
        @Expose
        private Integer itemcount;

        public List<Itemdetail_> getItemdetails() {
            return itemdetails;
        }

        public void setItemdetails(List<Itemdetail_> itemdetails) {
            this.itemdetails = itemdetails;
        }

        public Integer getItemcount() {
            return itemcount;
        }

        public void setItemcount(Integer itemcount) {
            this.itemcount = itemcount;
        }

    }

    public class Itemdetail_ implements Serializable{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("item_id")
        @Expose
        private Integer itemId;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("item_name")
        @Expose
        private String itemName;
        @SerializedName("item_description")
        @Expose
        private String itemDescription;
        @SerializedName("item_tags")
        @Expose
        private String itemTags;
        @SerializedName("item_price")
        @Expose
        private float itemPrice;
        @SerializedName("item_actualprice")
        @Expose
        private String itemActualprice;
        @SerializedName("item_shipping_cost")
        @Expose
        private String itemShippingCost;
        @SerializedName("item_condition")
        @Expose
        private String itemCondition;
        @SerializedName("item_created_by")
        @Expose
        private String itemCreatedBy;
        @SerializedName("item_category_id")
        @Expose
        private Integer itemCategoryId;
        @SerializedName("ispurchased")
        @Expose
        private Integer ispurchased;
        @SerializedName("item_qty")
        @Expose
        private Integer itemQty;
        @SerializedName("item_qty_remained")
        @Expose
        private Integer itemQtyRemained;
        @SerializedName("updated_date")
        @Expose
        private String updatedDate;
        @SerializedName("isdeleted")
        @Expose
        private Integer isdeleted;
        @SerializedName("deleted_date")
        @Expose
        private Object deletedDate;
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
        private Integer isverified;
        @SerializedName("lastlogin")
        @Expose
        private String lastlogin;
        @SerializedName("badge")
        @Expose
        private Integer badge;
        @SerializedName("paypalacc")
        @Expose
        private Object paypalacc;
        @SerializedName("bitcoinacc")
        @Expose
        private Integer bitcoinacc;
        @SerializedName("alipayacc")
        @Expose
        private Object alipayacc;
        @SerializedName("wechatacc")
        @Expose
        private Object wechatacc;
        @SerializedName("stripe_customer_id")
        @Expose
        private String stripeCustomerId;
        @SerializedName("deleteddate")
        @Expose
        private String deleteddate;
        @SerializedName("suspend_status")
        @Expose
        private String suspendStatus;
        @SerializedName("media_id")
        @Expose
        private Integer mediaId;
        @SerializedName("media_type")
        @Expose
        private String mediaType;
        @SerializedName("media_thumb")
        @Expose
        private String mediaThumb;
        @SerializedName("media_name")
        @Expose
        private String mediaName;
        @SerializedName("media_url")
        @Expose
        private String mediaUrl;
        @SerializedName("totalView")
        @Expose
        private Integer totalView;
        @SerializedName("itemid")
        @Expose
        private Integer itemid;
        @SerializedName("myitem")
        @Expose
        private Integer myitem;
        @SerializedName("ispaypal")
        @Expose
        private Integer ispaypal;
        @SerializedName("iswallet")
        @Expose
        private Integer iswallet;
        @SerializedName("bitcoinmail")
        @Expose
        private String bitcoinmail;
        @SerializedName("isbitcoin")
        @Expose
        private Integer isbitcoin;
        @SerializedName("userconnectionid")
        @Expose
        private Object userconnectionid;
        @SerializedName("userblockid")
        @Expose
        private Object userblockid;
        @SerializedName("shopsetting")
        @Expose
        private Integer shopsetting;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        public String getItemTags() {
            return itemTags;
        }

        public void setItemTags(String itemTags) {
            this.itemTags = itemTags;
        }

        public float getItemPrice() {
            return itemPrice;
        }

        public void setItemPrice(float itemPrice) {
            this.itemPrice = itemPrice;
        }

        public String getItemActualprice() {
            return itemActualprice;
        }

        public void setItemActualprice(String itemActualprice) {
            this.itemActualprice = itemActualprice;
        }

        public String getItemShippingCost() {
            return itemShippingCost;
        }

        public void setItemShippingCost(String itemShippingCost) {
            this.itemShippingCost = itemShippingCost;
        }

        public String getItemCondition() {
            return itemCondition;
        }

        public void setItemCondition(String itemCondition) {
            this.itemCondition = itemCondition;
        }

        public String getItemCreatedBy() {
            return itemCreatedBy;
        }

        public void setItemCreatedBy(String itemCreatedBy) {
            this.itemCreatedBy = itemCreatedBy;
        }

        public Integer getItemCategoryId() {
            return itemCategoryId;
        }

        public void setItemCategoryId(Integer itemCategoryId) {
            this.itemCategoryId = itemCategoryId;
        }

        public Integer getIspurchased() {
            return ispurchased;
        }

        public void setIspurchased(Integer ispurchased) {
            this.ispurchased = ispurchased;
        }

        public Integer getItemQty() {
            return itemQty;
        }

        public void setItemQty(Integer itemQty) {
            this.itemQty = itemQty;
        }

        public Integer getItemQtyRemained() {
            return itemQtyRemained;
        }

        public void setItemQtyRemained(Integer itemQtyRemained) {
            this.itemQtyRemained = itemQtyRemained;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public Integer getIsdeleted() {
            return isdeleted;
        }

        public void setIsdeleted(Integer isdeleted) {
            this.isdeleted = isdeleted;
        }

        public Object getDeletedDate() {
            return deletedDate;
        }

        public void setDeletedDate(Object deletedDate) {
            this.deletedDate = deletedDate;
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

        public Object getPaypalacc() {
            return paypalacc;
        }

        public void setPaypalacc(Object paypalacc) {
            this.paypalacc = paypalacc;
        }

        public Integer getBitcoinacc() {
            return bitcoinacc;
        }

        public void setBitcoinacc(Integer bitcoinacc) {
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

        public Integer getMediaId() {
            return mediaId;
        }

        public void setMediaId(Integer mediaId) {
            this.mediaId = mediaId;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        public String getMediaThumb() {
            return mediaThumb;
        }

        public void setMediaThumb(String mediaThumb) {
            this.mediaThumb = mediaThumb;
        }

        public String getMediaName() {
            return mediaName;
        }

        public void setMediaName(String mediaName) {
            this.mediaName = mediaName;
        }

        public String getMediaUrl() {
            return mediaUrl;
        }

        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }

        public Integer getTotalView() {
            return totalView;
        }

        public void setTotalView(Integer totalView) {
            this.totalView = totalView;
        }

        public Integer getItemid() {
            return itemid;
        }

        public void setItemid(Integer itemid) {
            this.itemid = itemid;
        }

        public Integer getMyitem() {
            return myitem;
        }

        public void setMyitem(Integer myitem) {
            this.myitem = myitem;
        }

        public Integer getIspaypal() {
            return ispaypal;
        }

        public void setIspaypal(Integer ispaypal) {
            this.ispaypal = ispaypal;
        }

        public Integer getIswallet() {
            return iswallet;
        }

        public void setIswallet(Integer iswallet) {
            this.iswallet = iswallet;
        }

        public String getBitcoinmail() {
            return bitcoinmail;
        }

        public void setBitcoinmail(String bitcoinmail) {
            this.bitcoinmail = bitcoinmail;
        }

        public Integer getIsbitcoin() {
            return isbitcoin;
        }

        public void setIsbitcoin(Integer isbitcoin) {
            this.isbitcoin = isbitcoin;
        }

        public Object getUserconnectionid() {
            return userconnectionid;
        }

        public void setUserconnectionid(Object userconnectionid) {
            this.userconnectionid = userconnectionid;
        }

        public Object getUserblockid() {
            return userblockid;
        }

        public void setUserblockid(Object userblockid) {
            this.userblockid = userblockid;
        }

        public Integer getShopsetting() {
            return shopsetting;
        }

        public void setShopsetting(Integer shopsetting) {
            this.shopsetting = shopsetting;
        }

    }


    public class Itemdetail implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("item_id")
        @Expose
        private Integer itemId;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("isdeleted")
        @Expose
        private Integer isdeleted;
        @SerializedName("deleted_date")
        @Expose
        private Object deletedDate;
        @SerializedName("item_name")
        @Expose
        private String itemName;
        @SerializedName("item_description")
        @Expose
        private String itemDescription;
        @SerializedName("item_tags")
        @Expose
        private String itemTags;
        @SerializedName("item_price")
        @Expose
        private Integer itemPrice;
        @SerializedName("item_actualprice")
        @Expose
        private Integer itemActualprice;
        @SerializedName("item_shipping_cost")
        @Expose
        private Integer itemShippingCost;
        @SerializedName("item_condition")
        @Expose
        private String itemCondition;
        @SerializedName("item_created_by")
        @Expose
        private Integer itemCreatedBy;
        @SerializedName("item_category_id")
        @Expose
        private Integer itemCategoryId;
        @SerializedName("ispurchased")
        @Expose
        private Integer ispurchased;
        @SerializedName("item_qty")
        @Expose
        private Integer itemQty;
        @SerializedName("item_qty_remained")
        @Expose
        private Integer itemQtyRemained;
        @SerializedName("updated_date")
        @Expose
        private String updatedDate;
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
        private Integer isverified;
        @SerializedName("lastlogin")
        @Expose
        private String lastlogin;
        @SerializedName("badge")
        @Expose
        private Integer badge;
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
        @SerializedName("deleteddate")
        @Expose
        private String deleteddate;
        @SerializedName("suspend_status")
        @Expose
        private String suspendStatus;
        @SerializedName("media_id")
        @Expose
        private Integer mediaId;
        @SerializedName("totalView")
        @Expose
        private Integer totalView;
        @SerializedName("media_type")
        @Expose
        private String mediaType;
        @SerializedName("media_thumb")
        @Expose
        private String mediaThumb;
        @SerializedName("media_name")
        @Expose
        private String mediaName;
        @SerializedName("media_url")
        @Expose
        private String mediaUrl;
        @SerializedName("userid")
        @Expose
        private Integer userid;
        @SerializedName("myitem")
        @Expose
        private Integer myitem;
        @SerializedName("ispaypal")
        @Expose
        private Integer ispaypal;
        @SerializedName("iswallet")
        @Expose
        private Integer iswallet;
        @SerializedName("bitcoinmail")
        @Expose
        private Object bitcoinmail;
        @SerializedName("isbitcoin")
        @Expose
        private Integer isbitcoin;
        @SerializedName("userconnectionid")
        @Expose
        private Object userconnectionid;
        @SerializedName("userblockid")
        @Expose
        private Object userblockid;
        @SerializedName("shopsetting")
        @Expose
        private Integer shopsetting;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
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

        public Object getDeletedDate() {
            return deletedDate;
        }

        public void setDeletedDate(Object deletedDate) {
            this.deletedDate = deletedDate;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        public String getItemTags() {
            return itemTags;
        }

        public void setItemTags(String itemTags) {
            this.itemTags = itemTags;
        }

        public Integer getItemPrice() {
            return itemPrice;
        }

        public void setItemPrice(Integer itemPrice) {
            this.itemPrice = itemPrice;
        }

        public Integer getItemActualprice() {
            return itemActualprice;
        }

        public void setItemActualprice(Integer itemActualprice) {
            this.itemActualprice = itemActualprice;
        }

        public Integer getItemShippingCost() {
            return itemShippingCost;
        }

        public void setItemShippingCost(Integer itemShippingCost) {
            this.itemShippingCost = itemShippingCost;
        }

        public String getItemCondition() {
            return itemCondition;
        }

        public void setItemCondition(String itemCondition) {
            this.itemCondition = itemCondition;
        }

        public Integer getItemCreatedBy() {
            return itemCreatedBy;
        }

        public void setItemCreatedBy(Integer itemCreatedBy) {
            this.itemCreatedBy = itemCreatedBy;
        }

        public Integer getItemCategoryId() {
            return itemCategoryId;
        }

        public void setItemCategoryId(Integer itemCategoryId) {
            this.itemCategoryId = itemCategoryId;
        }

        public Integer getIspurchased() {
            return ispurchased;
        }

        public void setIspurchased(Integer ispurchased) {
            this.ispurchased = ispurchased;
        }

        public Integer getItemQty() {
            return itemQty;
        }

        public void setItemQty(Integer itemQty) {
            this.itemQty = itemQty;
        }

        public Integer getItemQtyRemained() {
            return itemQtyRemained;
        }

        public void setItemQtyRemained(Integer itemQtyRemained) {
            this.itemQtyRemained = itemQtyRemained;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
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

        public Integer getMediaId() {
            return mediaId;
        }

        public void setMediaId(Integer mediaId) {
            this.mediaId = mediaId;
        }

        public Integer getTotalView() {
            return totalView;
        }

        public void setTotalView(Integer totalView) {
            this.totalView = totalView;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        public String getMediaThumb() {
            return mediaThumb;
        }

        public void setMediaThumb(String mediaThumb) {
            this.mediaThumb = mediaThumb;
        }

        public String getMediaName() {
            return mediaName;
        }

        public void setMediaName(String mediaName) {
            this.mediaName = mediaName;
        }

        public String getMediaUrl() {
            return mediaUrl;
        }

        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }

        public Integer getUserid() {
            return userid;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public Integer getMyitem() {
            return myitem;
        }

        public void setMyitem(Integer myitem) {
            this.myitem = myitem;
        }

        public Integer getIspaypal() {
            return ispaypal;
        }

        public void setIspaypal(Integer ispaypal) {
            this.ispaypal = ispaypal;
        }

        public Integer getIswallet() {
            return iswallet;
        }

        public void setIswallet(Integer iswallet) {
            this.iswallet = iswallet;
        }

        public Object getBitcoinmail() {
            return bitcoinmail;
        }

        public void setBitcoinmail(Object bitcoinmail) {
            this.bitcoinmail = bitcoinmail;
        }

        public Integer getIsbitcoin() {
            return isbitcoin;
        }

        public void setIsbitcoin(Integer isbitcoin) {
            this.isbitcoin = isbitcoin;
        }

        public Object getUserconnectionid() {
            return userconnectionid;
        }

        public void setUserconnectionid(Object userconnectionid) {
            this.userconnectionid = userconnectionid;
        }

        public Object getUserblockid() {
            return userblockid;
        }

        public void setUserblockid(Object userblockid) {
            this.userblockid = userblockid;
        }

        public Integer getShopsetting() {
            return shopsetting;
        }

        public void setShopsetting(Integer shopsetting) {
            this.shopsetting = shopsetting;
        }

    }

    public class Globaltrend implements Serializable{

        @SerializedName("itemdetails")
        @Expose
        private List<Object> itemdetails = null;
        @SerializedName("itemcount")
        @Expose
        private Integer itemcount;

        public List<Object> getItemdetails() {
            return itemdetails;
        }

        public void setItemdetails(List<Object> itemdetails) {
            this.itemdetails = itemdetails;
        }

        public Integer getItemcount() {
            return itemcount;
        }

        public void setItemcount(Integer itemcount) {
            this.itemcount = itemcount;
        }

    }
}