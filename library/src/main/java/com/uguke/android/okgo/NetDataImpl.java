package com.uguke.android.okgo;

import com.google.gson.annotations.SerializedName;

/**
 * 功能描述：网络请求返回实体
 * @author  LeiJue
 */
public class NetDataImpl<T> implements NetData<T> {

    @SerializedName(
            value = "code",
            alternate = {"resultcode", "returnCode", "status"})
    private int code;

    @SerializedName(
            value = "message",
            alternate = {"reason", "returnMsg", "msg", "error"})
    private String message;

    @SerializedName(
            value = "data",
            alternate = {"result"})
    private T data;

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
