package com.screamxo.Interface;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Optional;

/**
 * Created by Shubham Agarwal on 04/10/16.
 */

public interface FacebookInterface {
    void success(Map<String, String> map);

    @Optional
    void onFbFrdFetch(ArrayList<Object> list);
}
