
package com.example.apimodule.ApiBase.ApiBean.ProfileMedia;

import com.example.apimodule.ApiBase.ApiBean.Mediapost;
import com.example.apimodule.ApiBase.ApiBean.Streampost;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("posts")
    @Expose
    private List<Mediapost> posts = null;
    @SerializedName("totalcount")
    @Expose
    private Integer totalcount;
    @SerializedName("count")
    @Expose
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Mediapost> getPosts() {
        return posts;
    }

    public void setPosts(List<Mediapost> posts) {
        this.posts = posts;
    }

    public Result withPosts(List<Mediapost> posts) {
        this.posts = posts;
        return this;
    }

    public Integer getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(Integer totalcount) {
        this.totalcount = totalcount;
    }

    public Result withTotalcount(Integer totalcount) {
        this.totalcount = totalcount;
        return this;
    }

}
