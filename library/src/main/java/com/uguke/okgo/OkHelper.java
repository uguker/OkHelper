package com.uguke.okgo;

import android.app.Application;

import com.lzy.okgo.OkGo;

import java.util.List;

/**
 * 功能描述：OkGo辅助工具类
 * @author  雷珏
 * @date    2018/06/14
 */
public class OkHelper {

    private static OkHelper instance;

    static OkCodeFilter codeFilter;

    public static OkGo init(Application app) {
        if (instance == null) {
            instance = new OkHelper(app);
        }
        return OkGo.getInstance();
    }

    private OkHelper(Application app) {
        OkGo.getInstance().init(app);
    }

    public static <T> OkRequest<T> toObj(Class<T> clazz) {
        return new OkRequest<>(clazz, false);
    }

    public static <T> OkRequest<List<T>> toList(Class<T> clazz) {
        return new OkRequest<>(clazz, true);
    }

    public static void setOkCodeFilter(OkCodeFilter filter) {
        codeFilter = filter;
    }

}
