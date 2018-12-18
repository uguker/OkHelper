package com.uguke.okgo;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpMethod;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.DeleteRequest;
import com.lzy.okgo.request.OptionsRequest;
import com.lzy.okgo.request.PatchRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.PutRequest;
import com.lzy.okgo.request.base.Request;
import com.uguke.reflect.TypeBuilder;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：请求包
 * @author  雷珏
 * @date    2018/06/14
 */
public class OkRequest<T> {

    // OkGo请求相关

    private String mUrl;
    private String mUpJson;
    private Type mType;
    private Object mTag;
    private boolean mDownload;

    private List<Integer> mFilters;
    private HttpMethod mMethod;
    private HttpParams mParams;
    private HttpHeaders mHeaders;

    // Loading对话框相关

    private LoadingStrategy mStrategy;
    private LoadingDialog mLoading;

    OkRequest(Class<?> clazz, boolean list) {
        if (list) {
            mType = TypeBuilder.newInstance(NetData.class)
                    .beginSubType(List.class)
                    .addTypeParam(clazz)
                    .endSubType()
                    .build();
        } else {
            if (clazz == File.class) {
                mDownload = true;
                mType = TypeBuilder.newInstance(File.class).build();
            } else {
                mType = TypeBuilder.newInstance(NetData.class)
                        .addTypeParam(clazz)
                        .build();
            }
        }
        mParams = new HttpParams();
        mHeaders = new HttpHeaders();
        mStrategy = OkHelper.sStrategy.clone();
        mFilters = new LinkedList<>();
        mFilters.add(OkHelper.sFailedCode);
    }

    public OkRequest<T> tag(Object tag) {
        mTag = tag;
        return this;
    }

    public OkRequest<T> filters(int... filters) {
        for (int code : filters) {
            mFilters.add(code);
        }
        return this;
    }

    public OkRequest<T> loading(Context context) {
        mLoading = new LoadingDialog(context);
        return this;
    }

    public OkRequest<T> loadingText(String text) {
        mStrategy.setText(text);
        return this;
    }

    public OkRequest<T> loadingText(@StringRes int resId) {
        mStrategy.setTextId(resId);
        return this;
    }

    public OkRequest<T> loadingColor(@ColorInt int color) {
        mStrategy.setColor(color);
        return this;
    }

    public OkRequest<T> loadingColor(String color) {
        mStrategy.setColor(color);
        return this;
    }

    public OkRequest<T> loadingSize(float size) {
        mStrategy.setSize(size);
        return this;
    }

    public OkRequest<T> loadingDimEnable(boolean enable) {
        mStrategy.setDimEnable(enable);
        return this;
    }

    public OkRequest<T> get(String url) {
        mUrl = url;
        mMethod = HttpMethod.GET;
        return this;
    }

    public OkRequest<T> post(String url) {
        mUrl = url;
        mMethod = HttpMethod.POST;
        return this;
    }

    public OkRequest<T> put(String url) {
        mUrl = url;
        mMethod = HttpMethod.PUT;
        return this;
    }

    public OkRequest<T> delete(String url) {
        mUrl = url;
        mMethod = HttpMethod.DELETE;
        return this;
    }

    public OkRequest<T> head(String url) {
        mUrl = url;
        mMethod = HttpMethod.HEAD;
        return this;
    }

    public OkRequest<T> patch(String url) {
        mUrl = url;
        mMethod = HttpMethod.PATCH;
        return this;
    }

    public OkRequest<T> options(String url) {
        mUrl = url;
        mMethod = HttpMethod.OPTIONS;
        return this;
    }

    public OkRequest<T> trace(String url) {
        mUrl = url;
        mMethod = HttpMethod.TRACE;
        return this;
    }

    public OkRequest<T> upJson(String upJson) {
        mUpJson = upJson;
        return this;
    }

    public OkRequest<T> headers(HttpHeaders headers) {
        mHeaders.put(headers);
        return this;
    }

    public OkRequest<T> headers(String key, String value) {
        mHeaders.put(key, value);
        return this;
    }

    public OkRequest<T> removeHeader(String key) {
        mHeaders.remove(key);
        return this;
    }

    public OkRequest<T> removeAllHeaders() {
        mHeaders.clear();
        return this;
    }

    public OkRequest<T> params(Map<String, String> params, boolean... replace) {
        mParams.put(params, replace);
        return this;
    }

    public OkRequest<T> params(String key, String value, boolean... replace) {
        mParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, int value, boolean... replace) {
        mParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, float value, boolean... replace) {
        mParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, double value, boolean... replace) {
        mParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, long value, boolean... replace) {
        mParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, char value, boolean... replace) {
        mParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, boolean value, boolean... replace) {
        mParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, File file) {
        mParams.put(key, file);
        return this;
    }

    public OkRequest<T> params(String key, List<File> files) {
        mParams.putFileParams(key, files);
        return this;
    }

    public OkRequest<T> paramsWrapper(String key, HttpParams.FileWrapper fileWrapper) {
        mParams.put(key, fileWrapper);
        return this;
    }

    public OkRequest<T> paramsWrapper(String key, List<HttpParams.FileWrapper> fileWrappers) {
        mParams.putFileWrapperParams(key, fileWrappers);
        return this;
    }

    public OkRequest<T> addUrlParams(String key, List<String> values) {
        mParams.putUrlParams(key, values);
        return this;
    }

    public OkRequest<T> removeParam(String key) {
        mParams.remove(key);
        return this;
    }

    public OkRequest<T> removeAllParams() {
        mParams.clear();
        return this;
    }

    public void execute(Callback<NetData<T>> callback) {
        if (mLoading != null) {
            mLoading.strategy(mStrategy);
        }

        if (mDownload) {
            Request<File, ?> request = OkGo.get(mUrl);
            request.tag(mTag);
            request.params(mParams);
            request.headers(mHeaders);
            execute2(request, callback);
            return;
        }

        Request<String, ?> request;
        switch (mMethod) {
            case POST:
                PostRequest<String> postRequest = OkGo.post(mUrl);
                if (!TextUtils.isEmpty(mUpJson)) {
                    postRequest.upJson(mUpJson);
                }
                request = postRequest;
                break;
            case PUT:
                PutRequest<String> putRequest = OkGo.put(mUrl);
                if (!TextUtils.isEmpty(mUpJson)) {
                    putRequest.upJson(mUpJson);
                }
                request = putRequest;
                break;
            case DELETE:
                DeleteRequest<String> deleteRequest = OkGo.delete(mUrl);
                if (!TextUtils.isEmpty(mUpJson)) {
                    deleteRequest.upJson(mUpJson);
                }
                request = deleteRequest;
                break;
            case HEAD:
                request = OkGo.head(mUrl);
                break;
            case PATCH:
                PatchRequest<String> patchRequest = OkGo.patch(mUrl);
                if (!TextUtils.isEmpty(mUpJson)) {
                    patchRequest.upJson(mUpJson);
                }
                request = patchRequest;
                break;
            case OPTIONS:
                OptionsRequest<String> optionsRequest = OkGo.options(mUrl);
                if (!TextUtils.isEmpty(mUpJson)) {
                    optionsRequest.upJson(mUpJson);
                }
                request = optionsRequest;
                break;
            case TRACE:
                request = OkGo.trace(mUrl);
                break;
            default:
                request = OkGo.get(mUrl);
        }
        request.tag(mTag);
        request.params(mParams);
        request.headers(mHeaders);
        execute(request, callback);
    }

    private void execute(Request<String, ?> request, final Callback<NetData<T>> callback) {
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dismissLoading();
                NetData<T> data = new Gson().fromJson(response.body(), mType);
                if (mFilters != null) {
                    for (int filter : mFilters) {
                        if (filter == data.getCode()) {
                            callback.onFailed(data.getMessage());
                            return;
                        }
                    }
                }
                callback.onSucceed(data);
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                if (mLoading != null) {
                    mLoading.show();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                dismissLoading();
                callback.onFailed(OkHelper.sFailedText);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void execute2(final Request<File, ?> request, final Callback<NetData<T>> callback) {
        request.execute(new FileCallback() {
            @Override
            public void onSuccess(Response<File> response) {
                NetData<File> data = new NetData<>();
                data.setCode(OkHelper.sSucceedCode);
                data.setData(response.body());
                callback.onSucceed((NetData<T>) data);
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                callback.onFailed(OkHelper.sFailedText);
            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);
                callback.onProgress(progress);
            }
        });
    }

    private void dismissLoading() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }

}
