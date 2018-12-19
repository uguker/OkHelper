package com.uguke.okgo;

/**
 * 功能描述：
 *
 * @author LeiJue
 * @date 2018/11/19
 */
public interface FiltersHandler {

    /**
     * 功能描述：筛选处理
     * @param code 请求码
     * @return true为拦截
     */
    boolean onHandle(int code);
}
