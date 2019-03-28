package com.uguke.android.okgo;

import com.lzy.okgo.model.Progress;

/**
 * 网络请求回调
 * @author LeiJue
 * @param <T>
 */
public abstract class Callback<T> {
    /**
     * 请求成功回调
     * @param response     返回实例
     */
    public abstract void onSucceed(T response);
    /**
     * 请求失败回调
     * @param response     返回实例
     */
    public abstract void onFailed(T response);

    public void onCached(com.lzy.okgo.model.Response<String> response) {}

    public void onProgress(Progress progress) {}
}
