package com.example.apimodule.ApiBase.WalletOrderPayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parangat on 11/10/17.
 */

public class Result {

    @SerializedName("orderid")
    @Expose
    public int orderid;
    @SerializedName("itemid")
    @Expose
    public String itemid;
    @SerializedName("itemname")
    @Expose
    public String itemname;
    @SerializedName("itemimage")
    @Expose
    public String itemimage;
    @SerializedName("amount")
    @Expose
    public float amount;

    @SerializedName("wallet_balance")
    @Expose
    public double walletBalance;

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public int getOrderid() {
        return orderid;
    }

    public String getItemid() {
        return itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public String getItemimage() {
        return itemimage;
    }

    public float getAmount() {
        return amount;
    }
}
