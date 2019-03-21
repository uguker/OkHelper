package com.uguke.android.okgo;

/**
 * 网络请求响应（数据实体）
 * @author Leijue
 */
public interface Response<T> {

    T body();

    int code();

    String message();

}
