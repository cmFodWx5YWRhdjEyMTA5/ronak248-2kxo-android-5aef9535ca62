package com.example.apimodule.ApiBase.WalletOrderPayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class WalletBalanceBean {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private ArrayList<WalletBalanceResult> result = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<WalletBalanceResult> getResult() {
        return result;
    }

    public void setResult(ArrayList<WalletBalanceResult> result) {
        this.result = result;
    }
}
