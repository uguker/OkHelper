package com.uguke.okgo;

/**
 * 功能描述：接口数据预处理器
 * @author LeiJue
 */
public interface PretreatHandler {
    /**
     * @param body 接口Body数据
     * @return  处理后的数据
     */
    Object onHandle(Object body);
}
