package com.uguke.android.okgo;

/**
 * 功能描述：预处理器（对个别Response提前处理）
 * @author LeiJue
 */
public interface PretreatHandler {

    /**
     * 功能描述：筛选处理
     * @param response 请求码
     * @return true为拦截
     */
    boolean onHandle(Response response);
}