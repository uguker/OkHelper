package com.uguke.android.okgo;

import android.app.Application;
import android.content.Context;
import android.support.annotation.ColorInt;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.utils.HttpUtils;

import java.io.File;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * OkGo辅助工具类
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
    protected InterceptHandler mFiltersHandler;
    protected HeadersHandler mHeadersHandler;

    private Class<?> mResponseDataClass;
    private Application app;
    /** 数据预处理器 **/
    protected ConvertHandler mPretreatHandler;

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

    public OkUtils init(Application app) {
        this.app = app;
        OkGo.getInstance().init(app);
        return this;
    }

    private static class OkUtilsHolder {
        //@SuppressLint("StaticFieldLeak")

        private final static OkUtils HOLDER = new OkUtils();
    }

    public static OkUtils getInstance() {
        return OkUtils.OkUtilsHolder.HOLDER;
    }



    public static void openDebug() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(java.util.logging.Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        OkGo.getInstance().setOkHttpClient(builder.build());
    }

    public Context getContext() {
        HttpUtils.checkNotNull(app, "please call OkUtils.getInstance().init() first in application!");
        return app.getApplicationContext();
    }

    public static void setNetDataImplClass(Class<? extends Response> clazz) {
        Holder.INSTANCE.mResponseDataClass = clazz;
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
        OkRequest.defaultFailedCode = code;
    }

    public static void setSucceedCode(int code) {
        OkRequest.defaultSucceedCode = code;
    }

    public static void setJsonErrorCode(int code) {
        Holder.INSTANCE.mJsonErrorCode = code;
    }

    public static void setHeadersHandler(HeadersHandler handler) {
        OkRequest.defaultHeadersHandler = handler;
    }

    public static void setInterceptHandler(InterceptHandler handler) {
        OkRequest.defaultInterceptHandler = handler;
    }

    public static void setConvertHandler(ConvertHandler handler) {
        OkRequest.defaultConvertHandler = handler;
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
