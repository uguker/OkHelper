package com.uguke.okgo;

import com.google.gson.annotations.SerializedName;

/**
 * 功能描述：网络请求返回实体
 * @author  雷珏
 * @date    2018/06/14
 */
public class NetBean<T> {

    @SerializedName(
            value = "code",
            alternate = {"resultcode", "returnCode", "status"})
    private int code;

    @SerializedName(
            value = "message",
            alternate = {"reason", "returnMsg", "msg", "error"})
    private String message;

    @SerializedName(
            value = "result",
            alternate = {"data"})
    private T result;

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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
