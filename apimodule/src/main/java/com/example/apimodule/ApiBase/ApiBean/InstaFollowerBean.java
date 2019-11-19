package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstaFollowerBean {


@SerializedName("meta")
@Expose
private Meta meta;
@SerializedName("data")
@Expose
private List<Datum> data = null;


public Meta getMeta() {
return meta;
}

public void setMeta(Meta meta) {
this.meta = meta;
}

public List<Datum> getData() {
return data;
}

public void setData(List<Datum> data) {
this.data = data;
}

}