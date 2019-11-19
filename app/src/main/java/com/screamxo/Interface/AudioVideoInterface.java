package com.screamxo.Interface;

/**
 * Created by Shubham Agarwal on 21/10/16.
 */

public interface AudioVideoInterface {
    void Video(String url, String type, int index);

    void Audio(String url, String audioThum, String title, String type, int index);

    void Image(String url, String postId, int index, String type);
}
