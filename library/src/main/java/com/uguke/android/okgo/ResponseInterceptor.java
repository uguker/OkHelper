package com.uguke.android.okgo;

/**
 * 请求结果拦截处理器（对个别Response进行拦截）
 * @author LeiJue
 */
public interface ResponseInterceptor {

    /**
     * 对个别符合要求的响应头进行拦截
     * @param response 请求码
     * @return true为拦截
     */
    boolean onIntercept(Response response);
}
