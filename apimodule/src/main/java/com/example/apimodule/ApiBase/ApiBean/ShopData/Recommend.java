
package com.example.apimodule.ApiBase.ApiBean.ShopData;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recommend {

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

    public Recommend withItemdetails(List<Itemdetail_> itemdetails) {
        this.itemdetails = itemdetails;
        return this;
    }

    public Integer getItemcount() {
        return itemcount;
    }

    public void setItemcount(Integer itemcount) {
        this.itemcount = itemcount;
    }

    public Recommend withItemcount(Integer itemcount) {
        this.itemcount = itemcount;
        return this;
    }

}
