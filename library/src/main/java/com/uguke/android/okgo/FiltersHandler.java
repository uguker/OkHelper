package com.uguke.android.okgo;

/**
 * 功能描述：
 * @author LeiJue
 */
public interface FiltersHandler {

    /**
     * 功能描述：筛选处理
     * @param code 请求码
     * @param refresher 刷新控件
     * @return true为拦截
     */
    boolean onHandle(int code, Object refresher);
}
