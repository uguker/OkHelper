package com.uguke.okgo;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
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
    /** 是否加在过滤规则中的数据以onSucceed()返回 **/
    private boolean mToSucceed;

    private List<Integer> mFilters;
    private HttpMethod mMethod;
    private HttpParams mParams;
    private HttpHeaders mHeaders;

    // Loading对话框相关

    private int mLoadingColor;
    private float mLoadingSize;
    private String mLoadingText;
    private boolean mLoadingDimEnable;
    private LoadingDialog mLoading;

    private FiltersHandler mFiltersHandler;
    private HeadersHandler mHeadersHandler;

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
        mFilters = new LinkedList<>();
        mToSucceed = true;
    }

    public OkRequest<T> tag(Object tag) {
        mTag = tag;
        return this;
    }

    public OkRequest<T> filters(int... filters) {
        mToSucceed = true;
        mFilters.clear();
        for (int code : filters) {
            mFilters.add(code);
        }
        return this;
    }

    public OkRequest<T> filters(boolean toSucceed, int... filters) {
        mToSucceed = toSucceed;
        mFilters.clear();
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
        mLoadingText = text;
        return this;
    }

    public OkRequest<T> loadingColor(@ColorInt int color) {
        mLoadingColor = color;
        return this;
    }

    public OkRequest<T> loadingSize(float size) {
        mLoadingSize = size;
        return this;
    }

    public OkRequest<T> loadingDimEnable(boolean enable) {
        mLoadingDimEnable = enable;
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

    public OkRequest<T> headersHandler(HeadersHandler handler) {
        mHeadersHandler = handler;
        return this;
    }

    public OkRequest<T> filtersHandler(FiltersHandler handler) {
        mFiltersHandler = handler;
        return this;
    }

    public void execute(Callback<NetData<T>> callback) {
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

    private void execute(final Request<String, ?> request, final Callback<NetData<T>> callback) {
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                // 如果需要，取消对话框
                dismissLoading();
                OkUtils utils = OkUtils.Holder.INSTANCE;
                // 如果设置了Headers处理器
                if (mHeadersHandler != null || utils.mHeadersHandler != null) {
                    if (mHeadersHandler != null) {
                        // 不继续向下
                        if (mHeadersHandler.onHandle(response.headers())) {
                            return;
                        }
                    } else {
                        // 不继续向下
                        if (utils.mHeadersHandler.onHandle(response.headers())) {
                            return;
                        }
                    }
                }
                NetData<T> data = new Gson().fromJson(response.body(), mType);
                // 如果有异常
                if (data == null) {
                    return;
                }
                // 根据code回调
                if (utils.mSucceedCode == data.getCode()) {
                    callback.onSucceed(data);
                } else if (utils.mFailedCode == data.getCode()) {
                    callback.onFailed(data.getMessage());
                } else {
                    for (int filter : mFilters) {
                        if (filter == data.getCode()) {
                            handleFilters(data, mToSucceed, callback);
                            return;
                        }
                    }
                    handleFilters(data, !mToSucceed, callback);
                }
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                showLoading();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                dismissLoading();
                callback.onFailed(OkUtils.Holder.INSTANCE.mFailedText);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void execute2(final Request<File, ?> request, final Callback<NetData<T>> callback) {
        request.execute(new FileCallback() {
            @Override
            public void onSuccess(Response<File> response) {
                OkUtils utils = OkUtils.Holder.INSTANCE;
                // 如果设置了Headers处理器
                if (mHeadersHandler != null || utils.mHeadersHandler != null) {
                    if (mHeadersHandler != null) {
                        // 不继续向下
                        if (mHeadersHandler.onHandle(response.headers())) {
                            return;
                        }
                    } else {
                        // 不继续向下
                        if (utils.mHeadersHandler.onHandle(response.headers())) {
                            return;
                        }
                    }
                }
                NetData<File> data = new NetData<>();
                data.setCode(OkUtils.Holder.INSTANCE.mSucceedCode);
                data.setData(response.body());
                callback.onSucceed((NetData<T>) data);
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                callback.onFailed(OkUtils.Holder.INSTANCE.mFailedText);
            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);
                callback.onProgress(progress);
            }
        });
    }

    private void handleFilters(NetData<T> data, boolean succeed, final Callback<NetData<T>> callback) {
        OkUtils utils = OkUtils.Holder.INSTANCE;
        if (mFiltersHandler != null || utils.mFiltersHandler !=null) {
            if (mFiltersHandler != null) {
                // 继续向下
                if (!mFiltersHandler.onHandle(data.getCode())) {
                    if (succeed) {
                        callback.onSucceed(data);
                    } else {
                        callback.onFailed(data.getMessage());
                    }
                }
            } else {
                // 继续向下
                if (!utils.mFiltersHandler.onHandle(data.getCode())) {
                    if (succeed) {
                        callback.onSucceed(data);
                    } else {
                        callback.onFailed(data.getMessage());
                    }
                }
            }
        }
    }

    private void showLoading() {
        if (mLoading != null) {
            OkUtils utils = OkUtils.Holder.INSTANCE;
            if (utils.mLoadingColor == 0) {
                utils.mLoadingColor = ContextCompat.getColor(
                        OkGo.getInstance().getContext(), R.color.colorAccent);
            }
            mLoadingText = TextUtils.isEmpty(mLoadingText) ? utils.mLoadingText : mLoadingText;
            mLoadingSize = mLoadingSize == 0 ? utils.mLoadingSize : mLoadingSize;
            mLoadingColor = mLoadingColor == 0 ? utils.mLoadingColor : mLoadingColor;
            mLoading.text(mLoadingText)
                    .size(mLoadingSize)
                    .color(mLoadingColor)
                    .dimEnable(mLoadingDimEnable)
                    .show();
        }
    }

    private void dismissLoading() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }
}
