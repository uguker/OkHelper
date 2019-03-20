package com.uguke.android.okgo;

import android.app.Application;
import android.support.annotation.ColorInt;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.uguke.okgo.PretreatHandler;

import java.io.File;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * 功能描述：OkGo辅助工具类
 * @author  LeiJue
 */
public class OkUtils {

    protected int mLoadingColor;
    protected float mLoadingSize;
    protected String mLoadingText;
    protected boolean mLoadingDimEnable;

    protected int mFailedCode;
    protected int mSucceedCode;
    protected int mJsonErrorCode;
    protected String mFailedText;
    protected String mJsonErrorText;

    protected HttpParams mParams;
    protected FiltersHandler mFiltersHandler;
    protected HeadersHandler mHeadersHandler;

    private Class<?> mNetDataImplClass;
    /** 数据预处理器 **/
    protected PretreatHandler mPretreatHandler;

    static class Holder {
        static final OkUtils INSTANCE = new OkUtils();
    }

    private OkUtils() {
        mSucceedCode = 200;
        mFailedCode = 101;
        mJsonErrorCode = 0;
        mFailedText = "网络请求异常";
        mLoadingSize = 60;
        mParams = new HttpParams();
    }

    public static void init(Application app) {
        OkGo.getInstance().init(app);
    }

    public static void setNetDataImplClass(Class<? extends NetData> clazz) {
        Holder.INSTANCE.mNetDataImplClass = clazz;
    }

    public static void setLoadingColor(@ColorInt int color) {
        Holder.INSTANCE.mLoadingColor = color;
    }

    public static void setLoadingSize(float size) {
        Holder.INSTANCE.mLoadingSize = size;
    }

    public static void setLoadingText(String text) {
        Holder.INSTANCE.mLoadingText = text;
    }

    public static void setLoadingDimEnable(boolean enable) {
        Holder.INSTANCE.mLoadingDimEnable = enable;
    }

    public static void setFailedText(String text) {
        Holder.INSTANCE.mFailedText = text;
    }

    public static void setJsonErrorText(String text) {
        Holder.INSTANCE.mJsonErrorText = text;
    }

    public static void setFailedCode(int code) {
        Holder.INSTANCE.mFailedCode = code;
    }

    public static void setSucceedCode(int code) {
        Holder.INSTANCE.mSucceedCode = code;
    }

    public static void setJsonErrorCode(int code) {
        Holder.INSTANCE.mJsonErrorCode = code;
    }

    public static void setHeadersHandler(HeadersHandler handler) {
        Holder.INSTANCE.mHeadersHandler = handler;
    }

    public static void setFiltersHandler(FiltersHandler handler) {
        Holder.INSTANCE.mFiltersHandler = handler;
    }

    public static <T> OkRequest<T> toObj(Object obj, Class<T> clazz) {
        return new OkRequest<>(obj, clazz, OkRequest.TYPE_NET_OBJECT);
    }

    public static <T> OkRequest<T> toObj(Object obj) {
        return new OkRequest<>(obj, Object.class, OkRequest.TYPE_NET_OBJECT);
    }

    public static <T> OkRequest<T> toStr(Object obj) {
        return new OkRequest<>(obj, String.class, OkRequest.TYPE_NET_OBJECT);
    }

    public static <T> OkRequest<File> toFile(Object obj) {
        return new OkRequest<>(obj, File.class, OkRequest.TYPE_FILE);
    }

    public static <T> OkRequest<List<T>> toList(Object obj, Class<T> clazz) {
        return new OkRequest<>(obj, clazz, OkRequest.TYPE_FILE);
    }

    //================================================//
    //=================OkGo自带方法===================//
    //================================================//

    public static void addCommonParams(HttpParams commonParams) {
        OkGo.getInstance().addCommonParams(commonParams);
    }

    public static void addCommonHeaders(HttpHeaders commonHeaders) {
        OkGo.getInstance().addCommonHeaders(commonHeaders);
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

    public static void cancel(Object tag) {
        OkGo.getInstance().cancelTag(tag);
    }

    public static void cancelAll() {
        OkGo.getInstance().cancelAll();
    }

    public static HttpParams getCommonParams() {
        return OkGo.getInstance().getCommonParams();
    }

    public static HttpHeaders getCommonHeaders() {
        return OkGo.getInstance().getCommonHeaders();
    }

    public static CookieJarImpl getCookieJar() {
        return OkGo.getInstance().getCookieJar();
    }

    public static OkHttpClient getOkHttpClient() {
        return OkGo.getInstance().getOkHttpClient();
    }

    public static CacheMode getCacheMode() {
        return OkGo.getInstance().getCacheMode();
    }

    public static int getRetryCount() {
        return OkGo.getInstance().getRetryCount();
    }

    public static long getCacheTime() {
        return OkGo.getInstance().getCacheTime();
    }

}
