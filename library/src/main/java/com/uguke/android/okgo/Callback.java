package com.uguke.android.okgo;

import com.lzy.okgo.model.Progress;

/**
 * 功能描述：网络请求回调
 * @author LeiJue
 * @param <T>
 */
public abstract class Callback<T> {
    /**
     * 功能描述：请求成功回调
     * @param response     返回实例
     */
    public abstract void onSucceed(T response);

    /**
     * 功能描述：请求失败回调
     * @param response     返回实例
     */
    public abstract void onFailed(T response);

    public void onProgress(Progress progress) {}
}