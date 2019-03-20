package com.uguke.android.okgo;

/**
 * @author 31081
 */
public interface NetData<T> {

    T getData();

    int getCode();

    String getMessage();
}
