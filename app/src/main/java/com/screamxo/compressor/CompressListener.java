package com.screamxo.compressor;

public interface CompressListener {
    public void onExecSuccess(String message);
    public void onExecFail(String reason);
    public void onExecProgress(String message);

}
