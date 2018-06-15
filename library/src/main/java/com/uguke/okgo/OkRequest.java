package com.uguke.okgo;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpMethod;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.DeleteRequest;
import com.lzy.okgo.request.OptionsRequest;
import com.lzy.okgo.request.PatchRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.PutRequest;
import com.lzy.okgo.request.base.Request;
import com.uguke.okgo.reflect.TypeBuilder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：请求包
 * @author  雷珏
 * @date    2018/06/14
 */
public class OkRequest<T> {

    // OkGo请求相关

    private String url;
    private String upJson;
    private Type type;

    private HttpMethod method;
    private HttpParams params;
    private HttpHeaders headers;

    // Loading对话框相关

    private boolean dimEnabled;
    private int loadingColor;
    private CharSequence loadingText;
    private LoadingDialog loading;

    OkRequest(Class<?> clazz, boolean list) {
        if (list) {
            type = TypeBuilder.newInstance(NetBean.class)
                    .beginSubType(List.class)
                    .addTypeParam(clazz)
                    .endSubType()
                    .build();
        } else {
            type = TypeBuilder.newInstance(NetBean.class)
                    .addTypeParam(clazz)
                    .build();
        }
        params = new HttpParams();
        headers = new HttpHeaders();
        dimEnabled = true;
    }

    public OkRequest<T> with(@NonNull Context context) {
        loading = new LoadingDialog(context);
        return this;
    }

    public OkRequest<T> with(@NonNull android.app.Fragment fragment) {
        loading = new LoadingDialog(fragment.getActivity());
        return this;
    }

    public OkRequest<T> with(@NonNull Fragment fragment) {
        loading = new LoadingDialog(fragment.getActivity());
        return this;
    }

    public OkRequest<T> with(@NonNull View view) {
        loading = new LoadingDialog(view.getContext());
        return this;
    }

    public OkRequest<T> loadingText(CharSequence text) {
        this.loadingText = text;
        return this;
    }

    public OkRequest<T> loadingText(@StringRes int resId) {
        this.loadingText = OkGo.getInstance().getContext().getString(resId);
        return this;
    }

    public OkRequest<T> loadingColor(@ColorInt int color) {
        this.loadingColor = color;
        return this;
    }

    public OkRequest<T> loadingColor(String color) {
        this.loadingColor = Color.parseColor(color);
        return this;
    }

    public OkRequest<T> dimEnabled(boolean dimEnabled) {
        this.dimEnabled = dimEnabled;
        return this;
    }

    public OkRequest<T> get(String url) {
        this.url = url;
        this.method = HttpMethod.GET;
        return this;
    }

    public OkRequest<T> post(String url) {
        this.url = url;
        this.method = HttpMethod.POST;
        return this;
    }

    public OkRequest<T> put(String url) {
        this.url = url;
        this.method = HttpMethod.PUT;
        return this;
    }

    public OkRequest<T> delete(String url) {
        this.url = url;
        this.method = HttpMethod.DELETE;
        return this;
    }

    public OkRequest<T> head(String url) {
        this.url = url;
        this.method = HttpMethod.HEAD;
        return this;
    }

    public OkRequest<T> patch(String url) {
        this.url = url;
        this.method = HttpMethod.PATCH;
        return this;
    }

    public OkRequest<T> options(String url) {
        this.url = url;
        this.method = HttpMethod.OPTIONS;
        return this;
    }

    public OkRequest<T> trace(String url) {
        this.url = url;
        this.method = HttpMethod.TRACE;
        return this;
    }


    public OkRequest<T> upJson(String upJson) {
        this.upJson = upJson;
        return this;
    }

    public OkRequest<T> headers(HttpHeaders headers) {
        this.headers.put(headers);
        return this;
    }

    public OkRequest<T> headers(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public OkRequest<T> removeHeader(String key) {
        headers.remove(key);
        return this;
    }

    public OkRequest<T> removeAllHeaders() {
        headers.clear();
        return this;
    }

    public OkRequest<T> params(Map<String, String> params, boolean... isReplace) {
        this.params.put(params, isReplace);
        return this;
    }

    public OkRequest<T> params(String key, String value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return this;
    }

    public OkRequest<T> params(String key, int value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return this;
    }

    public OkRequest<T> params(String key, float value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return this;
    }

    public OkRequest<T> params(String key, double value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return this;
    }

    public OkRequest<T> params(String key, long value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return this;
    }

    public OkRequest<T> params(String key, char value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return this;
    }

    public OkRequest<T> params(String key, boolean value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return this;
    }

    public OkRequest<T> addUrlParams(String key, List<String> values) {
        params.putUrlParams(key, values);
        return this;
    }

    public OkRequest<T> removeParam(String key) {
        params.remove(key);
        return this;
    }

    public OkRequest<T> removeAllParams() {
        params.clear();
        return this;
    }

    public void execute(@NonNull final BaseCallback<NetBean<T>> callback) {
        initLoading();
        Request<String, ?> request;
        switch (method) {
            case POST:
                PostRequest<String> postRequest = OkGo.<String>post(url)
                        .params(params)
                        .headers(headers);
                if (!TextUtils.isEmpty(upJson)) {
                    postRequest.upJson(upJson);
                }
                request = postRequest;

                break;
            case PUT:
                PutRequest<String> putRequest = OkGo.<String>put(url)
                        .params(params)
                        .headers(headers);
                if (!TextUtils.isEmpty(upJson)) {
                    putRequest.upJson(upJson);
                }
                request = putRequest;
                break;
            case DELETE:
                DeleteRequest<String> deleteRequest = OkGo.<String>delete(url)
                        .params(params)
                        .headers(headers);
                if (!TextUtils.isEmpty(upJson)) {
                    deleteRequest.upJson(upJson);
                }
                request = deleteRequest;
                break;
            case HEAD:
                request = OkGo.<String>head(url)
                    .params(params)
                    .headers(headers);
                break;
            case PATCH:
                PatchRequest<String> patchRequest = OkGo.<String>patch(url)
                        .params(params)
                        .headers(headers);
                if (!TextUtils.isEmpty(upJson)) {
                    patchRequest.upJson(upJson);
                }
                request = patchRequest;
                break;
            case OPTIONS:
                OptionsRequest<String> optionsRequest = OkGo.<String>options(url)
                        .params(params)
                        .headers(headers);
                if (!TextUtils.isEmpty(upJson)) {
                    optionsRequest.upJson(upJson);
                }
                request = optionsRequest;
                break;
            case TRACE:
                request = OkGo.<String>trace(url)
                        .params(params)
                        .headers(headers);
                break;
            default:
                request = OkGo.<String>get(url)
                        .params(params)
                        .headers(headers);
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                NetBean<T> netBean = new Gson().fromJson(response.body(), type);
                if (OkHelper.codeFilter != null) {
                    if (OkHelper.codeFilter.filter(netBean)) {
                        callback.onError(netBean.getMessage());
                        return;
                    }
                }
                callback.onSuccess(netBean);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                callback.onError(response.body());
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                if (loading != null && !loading.isShowing()) {
                    loading.show();
                }
                callback.onStart();
            }

            @Override
            public void onFinish() {
                if (loading != null && loading.isShowing()) {
                    loading.dismiss();
                }
                callback.onFinish();
            }
        });
    }

    private void initLoading() {
        if (loading != null) {
            loading.color(loadingColor)
                    .text(loadingText)
                    .dimEnabled(dimEnabled);
        }
    }

}
