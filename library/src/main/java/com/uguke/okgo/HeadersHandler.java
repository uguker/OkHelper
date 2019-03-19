package com.uguke.okgo;

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
     * @param extra 额外的
     * @return true为拦截
     */
    boolean onHandle(Headers headers, Object extra);
}
