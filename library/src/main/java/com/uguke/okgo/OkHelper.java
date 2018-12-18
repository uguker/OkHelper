package com.uguke.okgo;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import java.io.File;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * 功能描述：OkGo辅助工具类
 * @author  雷珏
 * @date    2018/06/14
 */
public class OkHelper {

    static OkHelper sInstance;

    static int sFailedCode = 101;
    static int sSucceedCode = 200;
    static String sFailedText = "网络请求异常";
    static LoadingStrategy sStrategy = new LoadingStrategy();

    public static OkGo init(Application app) {
        if (sInstance == null) {
            sInstance = new OkHelper(app);
        }
        return OkGo.getInstance();
    }

    private OkHelper(Application app) {
        OkGo.getInstance().init(app);
    }

    public static void setLoadingStrategy(LoadingStrategy strategy) {
        sStrategy = strategy;
    }

    public static void setOkHttpClient(OkHttpClient client) {
        OkGo.getInstance().setOkHttpClient(client);
    }

    public static void setCacheMode(CacheMode mode) {
        OkGo.getInstance().setCacheMode(mode);
    }

    public static void setCacheTime(long time) {
        OkGo.getInstance().setCacheTime(time);
    }

    public static void setRetryCount(int count) {
        OkGo.getInstance().setRetryCount(count);
    }

    public static void setSucceedCode(int code) {
        sSucceedCode = code;
    }

    public static void setFailedCode(int code) {
        sFailedCode = code;
    }

    public static void setFailedText(String text) {
        sFailedText = text;
    }

    public static <T> OkRequest<T> toObj(Class<T> clazz) {
        return new OkRequest<>(clazz, false);
    }

    public static <T> OkRequest<T> toObj() {
        return new OkRequest<>(Object.class, false);
    }

    public static <T> OkRequest<T> toStr() {
        return new OkRequest<>(String.class, false);
    }

    public static <T> OkRequest<T> toBoolean() {
        return new OkRequest<>(boolean.class, false);
    }

    public static <T> OkRequest<List<T>> toList(Class<T> clazz) {
        return new OkRequest<>(clazz, true);
    }

    public static OkRequest<File> toFile() {
        return new OkRequest<>(File.class, false);
    }

    public static void cancel(Object tag) {
        OkGo.getInstance().cancelTag(tag);
    }

    public static void cancelAll() {
        OkGo.getInstance().cancelAll();
    }
}
