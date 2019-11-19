package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shubham.Agarwal on 09/12/16.
 */

public class PuchaseHistoryResult {
    @SerializedName("itemcount")
    @Expose
    private int itemcount;
    @SerializedName("itemdetails")
    @Expose
    private ArrayList<PurchaseHistoryDetail> itemdetails = null;

    /**
     *
     * @return
     * The itemcount
     */
    public int getItemcount() {
        return itemcount;
    }

    /**
     *
     * @param itemcount
     * The itemcount
     */
    public void setItemcount(int itemcount) {
        this.itemcount = itemcount;
    }

    /**
     *
     * @return
     * The itemdetails
     */
    public ArrayList<PurchaseHistoryDetail> getItemdetails() {
        return itemdetails;
    }

    /**
     *
     * @param itemdetails
     * The itemdetails
     */
    public void setItemdetails(ArrayList<PurchaseHistoryDetail> itemdetails) {
        this.itemdetails = itemdetails;
    }

    @Override
    public String toString() {
        return "PuchaseHistoryResult{" +
                "itemcount=" + itemcount +
                ", itemdetails=" + itemdetails +
                '}';
    }
}
