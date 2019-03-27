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
    private Class<? extends Loading> loadingClass;
    private Class<? extends Response> responseClass;

    /** 全局过滤器 **/
    private ResponseInterceptor responseInterceptor;
    /** 全局请求头处理器 **/
    private HeadersInterceptor headersInterceptor;
    /** 全局预处理器 **/
    private ConvertHandler convertHandler;
    /** 全局加密处理器 **/
    private EncryptHandler encryptHandler;

    static class Holder {
        @SuppressLint("StaticFieldLeak")
        static final OkUtils INSTANCE = new OkUtils();
    }

    private OkUtils() {
        mLoadingSize = 60;
        loadingClass = LoadingDialog.class;
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

    //================================================//
    //============OkUtils拦截器处理器部分==============//
    //================================================//

    public OkUtils setConvertHandler(ConvertHandler handler) {
        convertHandler = handler;
        return this;
    }

    public OkUtils setEncryptHandler(EncryptHandler handler) {
        encryptHandler = handler;
        return this;
    }

    public OkUtils setHeadersInterceptor(HeadersInterceptor interceptor) {
        headersInterceptor = interceptor;
        return this;
    }

    public OkUtils setResponseInterceptor(ResponseInterceptor interceptor) {
        responseInterceptor = interceptor;
        return this;
    }

    ConvertHandler getConvertHandler() {
        return convertHandler;
    }

    EncryptHandler getEncryptHandler() {
        return encryptHandler;
    }

    HeadersInterceptor getHeadersInterceptor() {
        return headersInterceptor;
    }

    ResponseInterceptor getResponseInterceptor() {
        return responseInterceptor;
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



    public OkUtils setLoadingColor(@ColorInt int color) {
        Holder.INSTANCE.mLoadingColor = color;
        return this;
    }

    public OkUtils setLoadingSize(float size) {
        Holder.INSTANCE.mLoadingSize = size;
        return this;
    }

    public OkUtils setLoadingText(String text) {
        Holder.INSTANCE.mLoadingText = text;
        return this;
    }

    public OkUtils setLoadingDimEnable(boolean enable) {
        Holder.INSTANCE.mLoadingDimEnable = enable;
        return this;
    }


    public static OkUtils getInstance() {
        return Holder.INSTANCE;
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

    public OkUtils addCommonParams(HttpParams commonParams) {
        OkGo.getInstance().addCommonParams(commonParams);
        return this;
    }

    public OkUtils addCommonHeaders(HttpHeaders commonHeaders) {
        OkGo.getInstance().addCommonHeaders(commonHeaders);
        return this;
    }

    public OkUtils setOkHttpClient(OkHttpClient client) {
        OkGo.getInstance().setOkHttpClient(client);
        return this;
    }

    public OkUtils setCacheMode(CacheMode mode) {
        OkGo.getInstance().setCacheMode(mode);
        return this;
    }

    public OkUtils setCacheTime(long time) {
        OkGo.getInstance().setCacheTime(time);
        return this;
    }

    public OkUtils setRetryCount(int count) {
        OkGo.getInstance().setRetryCount(count);
        return this;
    }

    public OkUtils cancelTag(Object tag) {
        OkGo.getInstance().cancelTag(tag);
        return this;
    }

    public OkUtils cancelAll() {
        OkGo.getInstance().cancelAll();
        return this;
    }

    public HttpParams getCommonParams() {
        return OkGo.getInstance().getCommonParams();
    }

    public HttpHeaders getCommonHeaders() {
        return OkGo.getInstance().getCommonHeaders();
    }

    public CookieJarImpl getCookieJar() {
        return OkGo.getInstance().getCookieJar();
    }

    public OkHttpClient getOkHttpClient() {
        return OkGo.getInstance().getOkHttpClient();
    }

    public CacheMode getCacheMode() {
        return OkGo.getInstance().getCacheMode();
    }

    public int getRetryCount() {
        return OkGo.getInstance().getRetryCount();
    }

    public long getCacheTime() {
        return OkGo.getInstance().getCacheTime();
    }

}
