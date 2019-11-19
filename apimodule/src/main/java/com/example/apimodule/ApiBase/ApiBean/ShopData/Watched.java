
package com.example.apimodule.ApiBase.ApiBean.ShopData;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Watched {

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

    public Watched withItemdetails(List<Itemdetail> itemdetails) {
        this.itemdetails = itemdetails;
        return this;
    }

    public Integer getItemcount() {
        return itemcount;
    }

    public void setItemcount(Integer itemcount) {
        this.itemcount = itemcount;
    }

    public Watched withItemcount(Integer itemcount) {
        this.itemcount = itemcount;
        return this;
    }

}
