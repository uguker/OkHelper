package com.uguke.android.okgo;

import com.google.gson.annotations.SerializedName;

/**
 * 网络请求响应实体
 * @author  LeiJue
 */
public class ResponseImpl<T> implements Response<T> {

    @SerializedName(
            value = "data",
            alternate = {"result"})
    private T data;

    @SerializedName(
            value = "code",
            alternate = {"resultcode", "returnCode", "status"})
    private int code;

    @SerializedName(
            value = "message",
            alternate = {"reason", "returnMsg", "msg", "error"})
    private String message;

    @Override
    public T body() {
        return data;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }
}
