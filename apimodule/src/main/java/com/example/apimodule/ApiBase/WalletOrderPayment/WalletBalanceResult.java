package com.example.apimodule.ApiBase.WalletOrderPayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletBalanceResult {
    @SerializedName("wallet_amount")
    @Expose
    private int walletAmount;

    public int getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(int walletAmount) {
        this.walletAmount = walletAmount;
    }
}
