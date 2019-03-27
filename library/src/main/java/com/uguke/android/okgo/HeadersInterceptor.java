package com.uguke.android.okgo;

import okhttp3.Headers;

/**
 * Headers拦截处理器（对个别Headers进行拦截）
 * @author LeiJue
 */
public interface HeadersInterceptor {
    /**
     * 响应头处理
     * @param headers 响应头
     * @return true为拦截
     */
    boolean onIntercept(Headers headers);
}
