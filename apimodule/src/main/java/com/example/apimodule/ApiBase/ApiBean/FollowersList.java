package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

public class FollowersList {

@SerializedName("users")
@Expose
private List<User> users = null;
@SerializedName("next_cursor")
@Expose
private Integer nextCursor;
@SerializedName("next_cursor_str")
@Expose
private String nextCursorStr;
@SerializedName("previous_cursor")
@Expose
private Integer previousCursor;
@SerializedName("previous_cursor_str")
@Expose
private String previousCursorStr;

public List<User> getUsers() {
return users;
}

public void setUsers(List<User> users) {
this.users = users;
}

public Integer getNextCursor() {
return nextCursor;
}

public void setNextCursor(Integer nextCursor) {
this.nextCursor = nextCursor;
}

public String getNextCursorStr() {
return nextCursorStr;
}

public void setNextCursorStr(String nextCursorStr) {
this.nextCursorStr = nextCursorStr;
}

public Integer getPreviousCursor() {
return previousCursor;
}

public void setPreviousCursor(Integer previousCursor) {
this.previousCursor = previousCursor;
}

public String getPreviousCursorStr() {
return previousCursorStr;
}

public void setPreviousCursorStr(String previousCursorStr) {
this.previousCursorStr = previousCursorStr;
}

}