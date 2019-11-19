package com.example.apimodule.ApiBase;

import com.example.apimodule.ApiBase.ApiBean.AddItemreviewBean;
import com.example.apimodule.ApiBase.ApiBean.AddressBean;
import com.example.apimodule.ApiBase.ApiBean.AddwatchedItemBean;
import com.example.apimodule.ApiBase.ApiBean.BlockedList.Blocked;
import com.example.apimodule.ApiBase.ApiBean.Boost.StripeProcessBoostResponse;
import com.example.apimodule.ApiBase.ApiBean.CategoryList.CategoryList;
import com.example.apimodule.ApiBase.ApiBean.ChatBean;
import com.example.apimodule.ApiBase.ApiBean.CommentBean;
import com.example.apimodule.ApiBase.ApiBean.DashBoardBean;
import com.example.apimodule.ApiBase.ApiBean.FriendBean;
import com.example.apimodule.ApiBase.ApiBean.GetAccountBean;
import com.example.apimodule.ApiBase.ApiBean.GetUserDetailBean;
import com.example.apimodule.ApiBase.ApiBean.InstaFollowerBean;
import com.example.apimodule.ApiBase.ApiBean.ItemCategoriesBean;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailBean;
import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.ApiBean.NotificationBean;
import com.example.apimodule.ApiBase.ApiBean.PayPalBean;
import com.example.apimodule.ApiBase.ApiBean.Payment.FinalReceiptBean;
import com.example.apimodule.ApiBase.ApiBean.Payment.StripeToken;
import com.example.apimodule.ApiBase.ApiBean.PostDetails.PostDetails;
import com.example.apimodule.ApiBase.ApiBean.PostFrdBean;
import com.example.apimodule.ApiBase.ApiBean.PostMediaBean;
import com.example.apimodule.ApiBase.ApiBean.Posts.Posts;
import com.example.apimodule.ApiBase.ApiBean.PurchaseHistoryBean;
import com.example.apimodule.ApiBase.ApiBean.SalesUser;
import com.example.apimodule.ApiBase.ApiBean.SellItemBean;
import com.example.apimodule.ApiBase.ApiBean.SendMsgBean;
import com.example.apimodule.ApiBase.ApiBean.Setting.SettingResponse;
import com.example.apimodule.ApiBase.ApiBean.Shop.ShopItems;
import com.example.apimodule.ApiBase.ApiBean.ShopData.Shop;
import com.example.apimodule.ApiBase.ApiBean.SoldItemBean;
import com.example.apimodule.ApiBase.ApiBean.Stripe.AddCardReponse;
import com.example.apimodule.ApiBase.ApiBean.StripeCreateCustomer.StripeCreateCustomerReponse;
import com.example.apimodule.ApiBase.ApiBean.Tracking.UpdateTrackingDetailReposne;
import com.example.apimodule.ApiBase.ApiBean.UserBean;
import com.example.apimodule.ApiBase.ApiBean.WalletList.WalletList;
import com.example.apimodule.ApiBase.ApiBean.WatchingItemBean;
import com.example.apimodule.ApiBase.ApiBean.WithdrawBean;
import com.example.apimodule.ApiBase.ApiBean.deliveryUpdate.DeliveryUpdatePojo;
import com.example.apimodule.ApiBase.ApiBean.shop_dashboard.ShopDashboard;
import com.example.apimodule.ApiBase.StripeAlipay.ProcessStripePaymentResponse;
import com.example.apimodule.ApiBase.WalletOrderPayment.FinalProcessResponse;
import com.example.apimodule.ApiBase.WalletOrderPayment.WalletBalanceBean;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Map;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface FetchrServiceInterface {

    @FormUrlEncoded
    @POST("users/signup")
        // 26.
    Call<LoginBean> SocialLoginUser(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("users/login")
    Call<LoginBean> LoginUser(@FieldMap Map<String, String> params);

    /*@FormUrlEncoded
    @POST("users/login")
    Call<LoginBean> LoginUser(@FieldMap Map<String, String> params);*/

    @FormUrlEncoded
    @POST("users/forgotpassword")
    Call<LoginBean> ForgetPassword(@FieldMap Map<String, String> params);

    @Multipart
    @POST("users/signup")
        // 26.
    Call<LoginBean> CustomSignUP(@PartMap Map<String, RequestBody> params, @Part ArrayList<MultipartBody.Part> file);

    @FormUrlEncoded
    @POST("users/verifyuser")
        //36.
    Call<LoginBean> EmailVerification(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("stripepaymentnew/process_boost")
    Call<StripeProcessBoostResponse> stripeProcessBoost(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("stripepaymentnew/finalboostprocess")
        //84
    Call<StripeProcessBoostResponse> stripeFinalBoostProcess(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("stripepaymentnew/process")
        // 73
    Call<StripeToken> stripeProcessPayment(@FieldMap Map<String, String> params);

    // buy multiple item card
    @FormUrlEncoded
    @POST("multipleitemorder/process")
    // 73
    Call<StripeToken> stripeProcessPaymentMultipleItems(@FieldMap Map<String, String> params, @FieldMap Map<String, ArrayList<Object>> multipltItem);

    @FormUrlEncoded
    @POST(" multipleitemorder/finalprocess")
        // 87
    Call<FinalProcessResponse> finalizeMultipleItemBuyUsingCard(@FieldMap Map<String, String> params,
                                                                @FieldMap Map<String, ArrayList<Object>> multipltItem);

    @FormUrlEncoded
    @POST("stripepaymentnew/finalprocess")
        // 87
    Call<FinalProcessResponse> finalizeBuyUsingCard(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("stripepaymentnew/addAmountToWallet")
    Call<StripeToken> addAmountToWallet(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user_wallet/sendMoney")
        //38
    Call<StripeToken> sendMoney(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("setting/send_enquiry")
        // 59
    Call<StripeToken> sendQuery(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("stripepaymentnew/addcard")
        // 71
    Call<AddCardReponse> stripeAddCard(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("walletorderpayment/finalboostprocess")
        // 33.
    Call<StripeToken> finalBoostProcess(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("walletorderpayment/finalprocess")
        // 89.
    Call<FinalProcessResponse> getWalletPay(@FieldMap Map<String, String> params);

    //wallet pay for multiple items
    @FormUrlEncoded
    @POST("walletorderpayment1/finalprocess")
    // 89.
    Call<FinalProcessResponse> getWalletPayMultipleitem(@FieldMap Map<String, String> params,
                                                        @FieldMap Map<String, ArrayList<Object>> paramItem);

    /*Paypal*/

    @FormUrlEncoded
    @POST("paypalpayment/process_paypal")
    Call<ProcessStripePaymentResponse> processPayPal(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("paypalpayment/finalprocess")
    Call<FinalProcessResponse> finalProcessPayPal(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("paypalpayment/process_boost")
        //83
    Call<ProcessStripePaymentResponse> processPayPalBoost(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("paypalpayment/finalboostprocess")
    Call<ProcessStripePaymentResponse> finalProcessPayPalBoost(@FieldMap Map<String, String> params);


    /*APIPAY*/

    @FormUrlEncoded
    @POST("stripealipaypayment/process_alipay")
        // 86
    Call<ProcessStripePaymentResponse> processAlipay(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("stripealipaypayment/finalprocess")
    Call<FinalProcessResponse> finalProcessApipay(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("stripealipaypayment/process_boost")
        // 88
    Call<ProcessStripePaymentResponse> processAlipayBoost(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("stripealipaypayment/finalboostprocess")
    Call<ProcessStripePaymentResponse> finalProcessAlipayBoost(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("users/resendcode")
    Call<LoginBean> EmailVerificationAgain(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/dashboard")
        // 3.
    Call<DashBoardBean> getDashboardEventsData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/getshopdashboard")
        // 3.
    Call<ShopDashboard> getShopDashboardData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/getdashboardstream")
        // 6.
    Call<Posts> StreamMore(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("users/getworldstream")
    Call<DashBoardBean> GetMoreStream(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/getdashboardmedia")
        // 5.
    Call<com.example.apimodule.ApiBase.ApiBean.Comments.Posts> MediaMore(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("friend/friendlist")
        //19.
    Call<FriendBean> GetFrdList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("friend/suggestedFriends")
    Call<FriendBean> GetSuggestFrdList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("friend/friendrequestlist")
        //20.
    Call<FriendBean> GetRequestFrdList(@FieldMap Map<String, String> params);

    //    @FormUrlEncoded
    @POST("category/listallcategories")
    // 22
    Call<CategoryList> GetCategory();

    @FormUrlEncoded
    @POST("category/listallcategoriesnew")
        // 47 (Need to pass param)
    Call<CategoryList> getNewCategories(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("users/getworldata")
    Call<DashBoardBean> GetWorldData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("users/getworldmedia")
    Call<DashBoardBean> GetWorldMedia(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("friend/fetchfriends")
    Call<PostFrdBean> postFetchFrdList(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("friend/invitefriends")
        // 15.
    Call<PostFrdBean> inviteFrdFromContact(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("friend/searchfriends")
        //48
    Call<FriendBean> searchFrdList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("friend/acceptfriend")
        // 44
    Call<FriendBean> acceptFrdRequest(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("friend/unfriend")
//41
    Call<FriendBean> UnFrdRequest(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("friend/addfriend")
        //40.
    Call<FriendBean> sendFrdRequest(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("friend/rejectfriend")
        // 45
    Call<FriendBean> rejectFrdRequest(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user_chat/getchat")
        //61
    Call<ChatBean> getChatData(@FieldMap Map<String, String> params);

    @Multipart
    @POST("user_chat/sendmessage")
        // 62
    Call<SendMsgBean> sendMsg(@PartMap Map<String, RequestBody> params, @Part ArrayList<MultipartBody.Part> file);

    @FormUrlEncoded
    @POST("user_chat/deletemessage")
    Call<ChatBean> deleteMsg(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user_chat/friendmessagelist")
        // 35.
    Call<ChatBean> getFrdChatHeader(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user_chat/buyersellermessagelist")
        // 60
    Call<ChatBean> getByerSellerChatHeader(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("notification/getallnotification")
        // 46
    Call<NotificationBean> getNetworkNotification(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/getdashboarditembyuid")
        // 7.
    Call<ShopItems> ShopMore(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("item/getitembyuid")
        // 16.
    Call<ShopItems> getitembyuid(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/deletepost")
        //24
    Call<CommentBean> DeleteStream(@FieldMap Map<String, Integer> params);


    // delete media post

    @FormUrlEncoded
    @POST("post/deletepost")
        //24
    Call<ItemCategoriesBean> deleteMediaPost(@FieldMap Map<String, Integer> params);

    @FormUrlEncoded
    @POST("Posts/deletecomment")
    Call<CommentBean> DeleteComment(@FieldMap Map<String, Integer> params);

    @FormUrlEncoded
    @POST("post/reportpost")
        // 85
    Call<CommentBean> ReportComment(@FieldMap Map<String, Integer> params);

    @FormUrlEncoded
    @POST("item/Reportitem")
    Call<CommentBean> ReportItem(@FieldMap Map<String, Integer> params);

    @FormUrlEncoded
    @POST("post/getpostbyuid")
        //17.
    Call<com.example.apimodule.ApiBase.ApiBean.ProfileMedia.Posts> UserDetailData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/getpostbyuid")
        //17.
    Call<UserBean> UserDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/getshopdashboard")
        // 50.
    Call<Shop> ProfileShop(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/getshopdashboard")
        // 50.
    Call<DashBoardBean> ProfileShopView(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/getpost")
        //23.
    Call<CommentBean> PostDetails(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Posts/getpostscomment")
    Call<CommentBean> MoreComment(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/createcomment")
        //12.
    Call<LoginBean> PostCommet(@FieldMap Map<String, String> params);

    @Multipart
    @POST("users/updateprofile")
        //30
    Call<LoginBean> EditProfile(@PartMap Map<String, RequestBody> params, @Part ArrayList<MultipartBody.Part> file);

    @Multipart
    @POST("post/createpost")
        //13.
    Call<PostMediaBean> PostMedia(@PartMap Map<String, RequestBody> params, @Part ArrayList<MultipartBody.Part> file, @Part MultipartBody.Part audioCover);

    @FormUrlEncoded
    @POST("post/createpost")
        //13.
    Call<LoginBean> PostStream(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/likepost")
        //9.
    Call<LoginBean> Like(@FieldMap Map<String, Integer> params);

    @FormUrlEncoded
    @POST("item/addwatcheditem")
        //51
    Call<LoginBean> Bookmark(@FieldMap Map<String, Integer> params);


    @GET
    Call<InstaFollowerBean> GetTwitterFollower(@Url String token);

/*    @FormUrlEncoded
    @POST("Posts/unlikepost")
    Call<LoginBean> UnLike(@FieldMap Map<String, Integer> params);*/

    @GET("users/logout")
        // 4.
    Call<LoginBean> Logout();

    @FormUrlEncoded
    @POST("friend/blockedlist")
        //53.
    Call<Blocked> rejectList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user_wallet/userwalletlistnew")
        // 34.
    Call<WalletList> walletList(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("item/deleteitem")
        // 68
    Call<ItemCategoriesBean> DeleteItem(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("item/deleteitemmedia")
    Call<ItemCategoriesBean> deleteMediaItem(@FieldMap Map<String, String> params);

    @Multipart
    @POST("item/edititem")
        //69
    Call<SellItemBean> UpdateItem(@PartMap Map<String, RequestBody> params, @Part ArrayList<MultipartBody.Part> file);

    @Multipart
    @POST("item/createitem")
        //14.
    Call<SellItemBean> CreateItem(@PartMap Map<String, RequestBody> params, @Part ArrayList<MultipartBody.Part> file);

    @Multipart
    @POST("item/createitem")
        //14.
    Call<SellItemBean> cretaeItemMulti(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part[] surveyImagesParts);

    @FormUrlEncoded
    @POST("Paypalpayment/chainpay")
    Call<PayPalBean> PayPalPayment(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Paypalpayment/finalpaydetails")
    Call<PayPalBean> FinalPaymentDetail(@FieldMap Map<String, String> params);

    //    @FormUrlEncoded
    @GET("users/getshippingaddress")
    // 11.
    Call<AddressBean> getAddress();

    @FormUrlEncoded
    @POST("users/updateshippingaddress")
    Call<AddressBean> postAddress(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("item/getitem")
        //31
    Call<ItemDetailBean> GetItemDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/getpost")
        //23.
    Call<PostDetails> getPostDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("stripepaymentnew/getcardlist")
        // 72
    Call<GetAccountBean> getCardList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("users/updateaccount")
//32.
    Call<GetAccountBean> updateAccount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @GET("setting/getadmindetail")
        //21.
    Call<GetAccountBean> GetPaypalDetail();

    @FormUrlEncoded
    @POST("item/addwatcheditem")
        //51.
    Call<AddwatchedItemBean> AddWatchedItem(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("item/listwatcheditem")
        // 63
    Call<WatchingItemBean> GetWatchedItemList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("users/getsolditemlist")
        // 66
    Call<SoldItemBean> GetSoldItemList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("users/getpurchasedlist")
        // 64
    Call<PurchaseHistoryBean> GetPurchasedItemList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("item/addreview")
        //65
    Call<AddItemreviewBean> AddItemReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("users/getuserbyid")
        // 2.
    Call<GetUserDetailBean> GetUserInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("friend/blockfriend")
        // 42
    Call<GetUserDetailBean> BlockUser(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("friend/unblockfriend")
//43
    Call<GetUserDetailBean> UnBlockUser(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("friend/cancelfriend")
    Call<FriendBean> CancelFrdRequest(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("users/updatepassword")
        //57
    Call<LoginBean> ChangePassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("item/notifyItemOwnerForTrackingUpdate")
    Call<BaseApiBean> notifyItemOwnerForTrackingUpdate(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("item/updatetrackingdetail")
        // 67
    Call<UpdateTrackingDetailReposne> updatetrackingdetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("setting/getsettings")
        //54
    Call<SettingResponse> getSettings(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("setting/savesettings")
        // 56.
    Call<SettingResponse> saveSettings(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("setting/saveusersettings")
        // 55
    Call<SettingResponse> Saveusersettings(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("stripepaymentnew/createcustomer")
        // 70
    Call<StripeCreateCustomerReponse> createStripeCustomer(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/users/getallcartitems")
        //51
    Call<com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean> getCartItem(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/users/deletecart")
        //51
    Call<com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean> deleteCartItem(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/users/deletesaveditem")
        //51
    Call<com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean> deleteSaveItem(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/users/movetosaveditem")
        //51
    Call<com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean> moveToSave(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/users/movetocartitem")
        //51
    Call<com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean> moveToCart(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/users/cart")
        //51
    Call<com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean> addToCart(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/users/updatecartqty")
        //51
    Call<com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean> updateCartQty(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("/users/updatesaveditemqty")
        //51
    Call<com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean> updateSaveQty(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/users/getallsavedcartitems")
        //51
    Call<com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean> getSaveCartItem(@FieldMap Map<String, String> params);

    @GET("booster")
    Call<JsonElement> uploadBoost(@Query("user") String userId,
                                  @Query("marketer") int marketers,
                                  @Query("booster") int boosters,
                                  @Query("link") String link,
                                  @Query("description") String description,
                                  @Query("image") String image,
                                  @Query("type") int type,
                                  @Query("app_user") String app_user,
                                  @Query("days") String days);

    // final payment transaction receipt
    @FormUrlEncoded
    @POST("/multipleitemorder/generatereceipt")
    //51
    Call<FinalReceiptBean> getTransactionReceipt(@FieldMap Map<String, Integer> params);

    // wallet order payment transaction receipt
    @FormUrlEncoded
    @POST("/walletorderpayment1/generatereceipt")
    //51
    Call<FinalReceiptBean> getWalletTransactionReceipt(@FieldMap Map<String, Integer> params);

    // get wallet balance
    @GET("/users/walletamount")
    Call<WalletBalanceBean> getWalletBalance();

    // get sales user list
    @GET("users")
    Call<ArrayList<SalesUser>> getSalesUser();

    //transfer money
    @FormUrlEncoded
    @POST("users/transfermoney")
    Call<WithdrawBean> withdrawAmount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("users/deliveryUpdate")
    Call<DeliveryUpdatePojo> deliveryUpdate(@FieldMap Map<String, String> params);


}