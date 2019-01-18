package com.uguke.okgo;

import android.app.Application;
import android.support.annotation.ColorInt;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.io.File;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * 功能描述：OkGo辅助工具类
 * @author  雷珏
 * @date    2018/06/14
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

    public static <T> OkRequest<T> toObj(Class<T> clazz) {
        return new OkRequest<>(clazz, false);
    }

    public static <T> OkRequest<T> toObj() {
        return new OkRequest<>(Object.class, false);
    }

    public static <T> OkRequest<T> toStr() {
        return new OkRequest<>(String.class, false);
    }

    public static OkRequest<File> toFile() {
        return new OkRequest<>(File.class, false);
    }

    public static <T> OkRequest<List<T>> toList(Class<T> clazz) {
        return new OkRequest<>(clazz, true);
    }

    // ---------- OkGo方法 ------------//

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
