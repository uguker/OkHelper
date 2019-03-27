package com.uguke.android.okgo;

/**
 * 接口数据转换处理器
 * @author LeiJue
 */
public interface ConvertHandler {

    /**
     * 对接口body数据进行处理
     * @param body OkGo的Response的body数据
     * @return  处理后的数据
     */
    String onHandle(String body);
}
