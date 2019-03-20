package com.uguke.android.okgo;

import okhttp3.Headers;

/**
 * 功能描述：
 *
 * @author LeiJue
 */
public interface HeadersHandler {
    /**
     * 功能描述：响应头处理
     * @param headers 响应头
     * @param refresher 刷新控件
     * @return true为拦截
     */
    boolean onHandle(Headers headers, Object refresher);
}
