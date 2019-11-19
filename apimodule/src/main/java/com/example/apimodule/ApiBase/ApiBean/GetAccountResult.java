package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Created by Shubham.Agarwal on 24/11/16.
 */

public class GetAccountResult {

    @SerializedName("card_list")
    @Expose
    public List<CardData> cardList = null;

    public List<CardData> getCardList() {
        return cardList;
    }

    public boolean hasCard() {
        for (CardData eachCardData : getCardList()) {
            if (eachCardData.getIsCard().equalsIgnoreCase("yes")) {
                return true;
            }
        }
        return false;
    }

    public String getPaypal() {
        for (CardData eachCardData : getCardList()) {
            if (eachCardData.getIsCard().equalsIgnoreCase("no") && eachCardData.getType().equalsIgnoreCase("paypal")) {
                return eachCardData.getEmail();
            }

        }
        return "";
    }

    public String getBitcoin() {
        for (CardData eachCardData : getCardList()) {
            if (eachCardData.getIsCard().equalsIgnoreCase("no") && eachCardData.getType().equalsIgnoreCase("bitcoin")) {
                return eachCardData.getEmail();
            }

        }
        return "";
    }

    public String getAlipay() {
        for (CardData eachCardData : getCardList()) {
            if (eachCardData.getIsCard().equalsIgnoreCase("no") && eachCardData.getType().equalsIgnoreCase("alipay")) {
                return eachCardData.getEmail();
            }

        }
        return "";
    }

    public String getWechat() {
        for (CardData eachCardData : getCardList()) {
            if (eachCardData.getIsCard().equalsIgnoreCase("no") && eachCardData.getType().equalsIgnoreCase("wechat")) {
                return eachCardData.getEmail();
            }

        }
        return "";
    }


}
