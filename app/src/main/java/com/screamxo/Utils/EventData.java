package com.screamxo.Utils;

public class EventData {

    public static final int EVENT_SCROLL_TO_TOP = 100;
    public static final int EVENT_SCROLL_TO_TOP_NEW = 106;
    public static final int EVENT_SCROLL_TO_TOP_NEW_PROFILE = 107;
    public static final int EVENT_SCROLL_TO_TOP_tranding = 109;
    public static final int EVENT_HIDE_SEARCH = 101;
    public static final int EVENT_SHOW_SEARCH = 102;
    public static final int EVENT_TOGGLE_SEARCH = 103;


    private final int code;
    private final String data;

    public EventData(int code, String data) {
        this.code = code;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public int getCode() {
        return code;
    }
}
