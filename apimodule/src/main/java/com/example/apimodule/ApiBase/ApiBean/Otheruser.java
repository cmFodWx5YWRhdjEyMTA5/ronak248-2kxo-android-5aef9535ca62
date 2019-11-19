package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Otheruser {

@SerializedName("userid")
@Expose
private Integer userid;
@SerializedName("fname")
@Expose
private String fname;
@SerializedName("lname")
@Expose
private String lname;
@SerializedName("username")
@Expose
private String username;
@SerializedName("userphoto")
@Expose
private String userphoto;
@SerializedName("userphotothumb")
@Expose
private String userphotothumb;

public Integer getUserid() {
return userid;
}

public void setUserid(Integer userid) {
this.userid = userid;
}

public String getFname() {
return fname;
}

public void setFname(String fname) {
this.fname = fname;
}

public String getLname() {
return lname;
}

public void setLname(String lname) {
this.lname = lname;
}

public String getUsername() {
return username;
}

public void setUsername(String username) {
this.username = username;
}

public String getUserphoto() {
return userphoto;
}

public void setUserphoto(String userphoto) {
this.userphoto = userphoto;
}

public String getUserphotothumb() {
return userphotothumb;
}

public void setUserphotothumb(String userphotothumb) {
this.userphotothumb = userphotothumb;
}

}