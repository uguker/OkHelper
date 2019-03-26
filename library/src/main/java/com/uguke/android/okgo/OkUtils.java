package com.uguke.android.okgo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.ColorInt;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.utils.HttpUtils;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

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

    private int failedCode;
    private int succeedCode;
    private Application app;
    private Class<? extends Response> responseClass;

    /** 全局过滤器 **/
    private InterceptHandler interceptHandler;
    /** 全局请求头处理器 **/
    private HeadersHandler headersHandler;
    /** 全局预处理器 **/
    private ConvertHandler convertHandler;

    static class Holder {
        @SuppressLint("StaticFieldLeak")
        static final OkUtils INSTANCE = new OkUtils();
    }

    private OkUtils() {
        mLoadingSize = 60;
        responseClass = ResponseImpl.class;
    }

    public OkUtils init(Application app) {
        this.app = app;
        OkGo.getInstance().init(app);
        return this;
    }

    public OkUtils openDebug(String tag) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(tag);
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.SEVERE);
        builder.addInterceptor(loggingInterceptor);
        OkGo.getInstance().setOkHttpClient(builder.build());
        return this;
    }

    public OkUtils openDebug() {
       return openDebug("OkUtils");
    }

    public OkUtils setResponseClass(Class<? extends Response> clazz) {
       responseClass = clazz;
        return this;
    }

    public OkUtils setFailedCode(int code) {
        failedCode = code;
        return this;
    }

    public OkUtils setSucceedCode(int code) {
        succeedCode = code;
        return this;
    }

    public OkUtils setHeadersHandler(HeadersHandler handler) {
        headersHandler = handler;
        return this;
    }

    public OkUtils setInterceptHandler(InterceptHandler handler) {
        interceptHandler = handler;
        return this;
    }

    public OkUtils setConvertHandler(ConvertHandler handler) {
        convertHandler = handler;
        return this;
    }

    HeadersHandler getHeadersHandler() {
        return headersHandler;
    }

    InterceptHandler getInterceptHandler() {
        return interceptHandler;
    }

    ConvertHandler getConvertHandler() {
        return convertHandler;
    }

    int getFailedCode() {
        return failedCode;
    }

    int getSucceedCode() {
        return succeedCode;
    }

    public Context getContext() {
        HttpUtils.checkNotNull(app, "please call OkUtils.getInstance().init() first in application!");
        return app.getApplicationContext();
    }

    Class<? extends Response> getResponseClass() {
        return responseClass;
    }

    public static OkUtils getInstance() {
        return Holder.INSTANCE;
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

    //================================================//
    //================OkUtils数据请求==================//
    //================================================//

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

    public static <T> OkRequest<String> toJson(Object obj) {
        return new OkRequest<>(obj, String.class, OkRequest.TYPE_STRING);
    }

    public static <T> OkRequest<List<T>> toList(Object obj, Class<T> clazz) {
        return new OkRequest<>(obj, clazz, OkRequest.TYPE_NET_LIST);
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
