package com.uguke.android.okgo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

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

    private static final String DIALOG_TAG = "ok_utils_dialog_tag";

    private int failedCode;
    private int succeedCode;
    private Application app;
    private Class<? extends Response> responseClass;
    private Loading<? extends DialogFragment> loading;

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

    public static OkUtils getInstance() {
        return Holder.INSTANCE;
    }

    private OkUtils() {
        responseClass = ResponseImpl.class;
        loading = new LoadingDialog();
        failedCode = 101;
        succeedCode = 200;
    }

    public OkUtils init(Application app) {
        this.app = app;
        OkGo.getInstance().init(app);
        return this;
    }

    public OkUtils openDebug() {
        return openDebug("OkUtils", HttpLoggingInterceptor.Level.BODY, Level.INFO);
    }

    public OkUtils openDebug(String tag, Level colorLevel) {
        return openDebug("OkUtils", HttpLoggingInterceptor.Level.BODY, colorLevel);
    }

    public OkUtils openDebug(String tag, HttpLoggingInterceptor.Level printLevel, Level colorLevel) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(tag);
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(printLevel);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(colorLevel);
        builder.addInterceptor(loggingInterceptor);
        OkGo.getInstance().setOkHttpClient(builder.build());
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

    public OkUtils setResponseClass(Class<? extends Response> clazz) {
        responseClass = clazz;
        return this;
    }

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

    public OkUtils setLoading(Loading<? extends DialogFragment> loading) {
        this.loading = loading;
        return this;
    }

    public int getFailedCode() {
        return failedCode;
    }

    public int getSucceedCode() {
        return succeedCode;
    }

    public Class<? extends Response> getResponseClass() {
        return responseClass;
    }

    public ConvertHandler getConvertHandler() {
        return convertHandler;
    }

    public EncryptHandler getEncryptHandler() {
        return encryptHandler;
    }

    public HeadersInterceptor getHeadersInterceptor() {
        return headersInterceptor;
    }

    public ResponseInterceptor getResponseInterceptor() {
        return responseInterceptor;
    }

    public Loading<? extends DialogFragment> getLoading() {
        return loading;
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

    public void cancelTag(Object tag) {
        OkGo.getInstance().cancelTag(tag);
    }

    public void cancelAll() {
        OkGo.getInstance().cancelAll();
    }

    public Context getContext() {
        HttpUtils.checkNotNull(app, "please call OkUtils.getInstance().init() first in application!");
        return app.getApplicationContext();
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

    //================================================//
    //================OkUtils数据请求==================//
    //================================================//

    public static <T> OkRequest<T> toObj(Object context, Class<T> clazz) {
        return new OkRequest<>(context, clazz, OkRequest.TYPE_RESPONSE_OBJECT);
    }

    public static <T> OkRequest<T> toObj(Object context) {
        return new OkRequest<>(context, Object.class, OkRequest.TYPE_RESPONSE_OBJECT);
    }

    public static <T> OkRequest<T> toStr(Object context) {
        return new OkRequest<>(context, String.class, OkRequest.TYPE_RESPONSE_OBJECT);
    }

    public static <T> OkRequest<File> toFile(Object context) {
        return new OkRequest<>(context, File.class, OkRequest.TYPE_FILE);
    }

    public static <T> OkRequest<String> toInitial(Object context) {
        return new OkRequest<>(context, String.class, OkRequest.TYPE_STRING);
    }

    public static <T> OkRequest<List<T>> toList(Object context, Class<T> clazz) {
        return new OkRequest<>(context, clazz, OkRequest.TYPE_RESPONSE_LIST);
    }

    //================================================//
    //============OkUtils对话框显示与隐藏==============//
    //================================================//

    public static void showLoading(@NonNull Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                showLoading((Activity) context);
                break;
            }
        }
    }

    public static void showLoading(final Activity activity) {
        if (activity == null) {
            return;
        }
        final Loading<? extends DialogFragment> loading = OkUtils.getInstance().getLoading();
        if (loading != null) {
            if (!((DialogFragment) loading).isAdded()) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.show(activity, DIALOG_TAG);
                    }
                });
            }
        }
    }

    public static void showLoading(@NonNull Fragment fragment) {
        showLoading(fragment.getActivity());
    }

    public static void showLoading(@NonNull android.app.Fragment fragment) {
        showLoading(fragment.getActivity());
    }

    public static void showLoading(@NonNull View view) {
        showLoading(view.getContext());
    }

    public static void dismissLoading(@NonNull Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                dismissLoading((Activity) context);
                break;
            }
        }
    }

    public static void dismissLoading(final Activity activity) {
        if (activity == null) {
            return;
        }
        final FragmentManager manager = activity.getFragmentManager();
        final DialogFragment dialog = (DialogFragment) manager.findFragmentByTag(DIALOG_TAG);
        if (dialog != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            });
        }
    }

    public static void dismissLoading(@NonNull Fragment fragment) {
        dismissLoading(fragment.getActivity());
    }

    public static void dismissLoading(@NonNull android.app.Fragment fragment) {
        dismissLoading(fragment.getActivity());
    }

    public static void dismissLoading(@NonNull View view) {
        dismissLoading(view.getContext());
    }
}
