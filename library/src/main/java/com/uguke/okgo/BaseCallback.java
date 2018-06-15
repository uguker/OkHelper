package com.uguke.okgo;

import android.content.Context;

/**
 * 功能描述：网络请求回调
 * @author 雷珏
 * @date 2018/6/15
 * @param <T>
 */
public abstract class BaseCallback<T> {

    /** 进度条 **/
    private LoadingDialog dialog;

    protected BaseCallback() {}

    protected BaseCallback(Context context) {
        dialog = new LoadingDialog(context);
    }

    /**
     * 功能描述：请求成功回调
     * @param t     返回实例
     */
    public abstract void onSuccess(T t);

    /**
     * 功能描述：请求失败回调
     * @param msg   提示信息
     */
    public abstract void onError(String msg);

    public void onStart() {}

    public void onFinish() {}

    void showLoading() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
