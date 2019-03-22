package com.uguke.android.okgo.handler;

import com.uguke.android.okgo.Response;

/**
 * 功能描述：
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
