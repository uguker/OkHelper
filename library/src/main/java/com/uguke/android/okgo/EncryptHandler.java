package com.uguke.android.okgo;

import com.lzy.okgo.model.HttpParams;

/**
 * 加密处理器
 * @author LeiJue
 */
public interface EncryptHandler {

    /**
     * 对接口请求参数进行加密处理
     * @param params 请求参数
     * @return 加密后的请求
     */
    HttpParams onHandle(HttpParams params);
}
