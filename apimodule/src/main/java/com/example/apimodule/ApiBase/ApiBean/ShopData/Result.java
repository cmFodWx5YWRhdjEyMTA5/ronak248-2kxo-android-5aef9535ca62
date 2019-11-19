
package com.example.apimodule.ApiBase.ApiBean.ShopData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("watched")
    @Expose
    private Watched watched;
    @SerializedName("recommend")
    @Expose
    private Recommend recommend;
    @SerializedName("recentitems")
    @Expose
    private Recentitems recentitems;
    @SerializedName("globaltrend")
    @Expose
    private Globaltrend globaltrend;

    public Watched getWatched() {
        return watched;
    }

    public void setWatched(Watched watched) {
        this.watched = watched;
    }

    public Result withWatched(Watched watched) {
        this.watched = watched;
        return this;
    }

    public Recommend getRecommend() {
        return recommend;
    }

    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
    }

    public Result withRecommend(Recommend recommend) {
        this.recommend = recommend;
        return this;
    }

    public Recentitems getRecentitems() {
        return recentitems;
    }

    public void setRecentitems(Recentitems recentitems) {
        this.recentitems = recentitems;
    }

    public Result withRecentitems(Recentitems recentitems) {
        this.recentitems = recentitems;
        return this;
    }

    public Globaltrend getGlobaltrend() {
        return globaltrend;
    }

    public void setGlobaltrend(Globaltrend globaltrend) {
        this.globaltrend = globaltrend;
    }

    public Result withGlobaltrend(Globaltrend globaltrend) {
        this.globaltrend = globaltrend;
        return this;
    }

}
