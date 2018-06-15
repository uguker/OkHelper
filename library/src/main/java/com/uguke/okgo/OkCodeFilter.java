package com.uguke.okgo;

/**
 * 功能描述：OkGo请求Code过滤器
 * @author  雷珏
 * @date    2018/06/15
 */
public interface OkCodeFilter {
    /**
     * 功能描述：根据Code筛选
     * @param bean  网络请求返回数据
     * @return 返回是否请求失败
     */
    boolean filter(NetBean bean);
}
