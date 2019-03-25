package com.uguke.android.okgo;

import okhttp3.Headers;

/**
 *
 *
 * @author LeiJue
 */
public interface HeadersHandler {
    /**
     * 响应头处理
     * @param headers 响应头
     * @return true为拦截
     */
    boolean onHandle(Headers headers);
}
