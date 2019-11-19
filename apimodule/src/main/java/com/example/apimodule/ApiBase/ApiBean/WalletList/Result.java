
package com.example.apimodule.ApiBase.ApiBean.WalletList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.StringEscapeUtils;

public class Result {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("to_user_id")
    @Expose
    private String toUserId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("detail")
    @Expose
    private String detail;

    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("item_qty")
    @Expose
    private String itemQty;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("added_on")
    @Expose
    private String addedOn;
    @SerializedName("userOtherId")
    @Expose
    private String userOtherId;
    @SerializedName("walletMessage")
    @Expose
    private String walletMessage;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public String getAmount() {
        return amount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }



    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemQty() {
        return itemQty;
    }

    public String getStatus() {
        return status="1";
    }

    public String getAddedOn() {
        return addedOn;
    }

    public String getUserOtherId() {
        return userOtherId;
    }

    public String getWalletMessage() {
        return StringEscapeUtils.unescapeJava(walletMessage.replaceAll(" --- @@:-:@@", "").replace("@@:-:@@", ""));
    }

    @Override
    public String toString() {
        return "Result{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", amount='" + amount + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                ", type='" + type + '\'' +
                ", detail='" + detail + '\'' +

                ", itemId='" + itemId + '\'' +
                ", itemQty='" + itemQty + '\'' +
                ", status='" + status + '\'' +
                ", addedOn='" + addedOn + '\'' +
                ", userOtherId='" + userOtherId + '\'' +
                ", walletMessage='" + walletMessage + '\'' +
                '}';
    }
}
