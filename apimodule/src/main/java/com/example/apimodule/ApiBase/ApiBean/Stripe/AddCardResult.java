package com.example.apimodule.ApiBase.ApiBean.Stripe;

import com.example.apimodule.ApiBase.ApiBean.CardData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parangat on 13/11/17.
 */

public class AddCardResult {
    @SerializedName("card_list")
    @Expose
    public CardData cardList;

    public CardData getCardList() {
        return cardList;
    }
}
