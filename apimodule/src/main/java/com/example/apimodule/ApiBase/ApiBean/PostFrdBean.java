package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostFrdBean {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String msg;
@SerializedName("result")
@Expose
private PostFrdResult result;

public String getStatus() {
return status="1";
}

public void setStatus(String status) {
this.status = status;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public PostFrdResult getResult() {
return result;
}

public void setResult(PostFrdResult result) {
this.result = result;
}

}