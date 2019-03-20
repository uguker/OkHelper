package com.uguke.android.okgo;

/**
 * 功能描述：网络数据工具
 * @author LeiJue
 */
class NetDataUtils {

    static <T> NetData<T> createJsonFailedData() {
        return new NetData<T>() {
            @Override
            public T getData() {
                return null;
            }

            @Override
            public int getCode() {
                return 0;
            }

            @Override
            public String getMessage() {
                return "Json格式解析错误，请检查";
            }
        };
    }
}
