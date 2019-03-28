package com.uguke.android.okgo;

import android.app.Activity;

/**
 * 加载对话框接口
 * @author LeiJue
 * @param <T>
 */
public interface Loading<T extends Loading> {

    <T> T size(float size);

    <T> T colors(int... colors);

    <T> T dimEnabled(boolean enable);

    void show(Activity activity, String tag);

}
