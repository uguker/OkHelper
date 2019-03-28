package com.uguke.android.okgo;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpMethod;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;

import com.lzy.okgo.request.base.BodyRequest;
import com.lzy.okgo.request.base.Request;
import com.uguke.reflect.TypeBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * OkUtils网络请求
 * @author LeiJue
 */
public class OkRequest<T> {

    /** Response内嵌Object **/
    static final int TYPE_RESPONSE_OBJECT = 0;
    /** Response内嵌List **/
    static final int TYPE_RESPONSE_LIST = 1;
    /** String数据 **/
    static final int TYPE_STRING = 2;
    /** File数据 **/
    static final int TYPE_FILE = 3;

    private String requestUrl;
    private Object upData;
    private MediaType mediaType;
    private Object tag;
    private boolean multipart;
    private boolean spliceUrl;
    private int retryCount;
    private long cacheTime;
    private String cacheKey;
    private CacheMode cacheMode;
    private HttpMethod httpMethod;
    private HttpParams httpParams;
    private HttpHeaders httpHeaders;
    
    private Type responseType;
    private int requestType;
    private SparseBooleanArray responseCodes;
    private ConvertHandler convertHandler;
    private EncryptHandler encryptHandler;
    private HeadersInterceptor headersInterceptor;
    /** 用来防止空指针 **/
    private Reference<Object> reference;
    private Activity loadingActivity;

    OkRequest(Object context, Class<?> clazz, int type) {
        requestType = type;
        reference = new WeakReference<>(context);
        OkUtils okUtils = OkUtils.getInstance();
        switch (type) {
            case TYPE_RESPONSE_OBJECT:
                responseType = TypeBuilder.newInstance(okUtils.getResponseClass())
                        .addTypeParam(clazz)
                        .build();
                break;
            case TYPE_RESPONSE_LIST:
                responseType =  TypeBuilder.newInstance(okUtils.getResponseClass())
                        .beginSubType(List.class)
                        .addTypeParam(clazz)
                        .endSubType()
                        .build();
                break;
            case TYPE_STRING:
                responseType = TypeBuilder.newInstance(String.class).build();
                break;
            case TYPE_FILE:
                responseType = TypeBuilder.newInstance(File.class).build();
                break;
            default:
        }
        retryCount = 3;
        httpParams = new HttpParams();
        httpHeaders = new HttpHeaders();
        responseCodes = new SparseBooleanArray();
        responseCodes.put(okUtils.getSucceedCode(), true);
        responseCodes.put(okUtils.getFailedCode(), false);
    }

    //================================================//
    //=================接口请求方式===================//
    //================================================//

    public OkRequest<T> get(String url) {
        requestUrl = url;
        httpMethod = HttpMethod.GET;
        return this;
    }

    public OkRequest<T> post(String url) {
        requestUrl = url;
        httpMethod = HttpMethod.POST;
        return this;
    }

    public OkRequest<T> put(String url) {
        requestUrl = url;
        httpMethod = HttpMethod.PUT;
        return this;
    }

    public OkRequest<T> delete(String url) {
        requestUrl = url;
        httpMethod = HttpMethod.DELETE;
        return this;
    }

    public OkRequest<T> head(String url) {
        requestUrl = url;
        httpMethod = HttpMethod.HEAD;
        return this;
    }

    public OkRequest<T> patch(String url) {
        requestUrl = url;
        httpMethod = HttpMethod.PATCH;
        return this;
    }

    public OkRequest<T> options(String url) {
        requestUrl = url;
        httpMethod = HttpMethod.OPTIONS;
        return this;
    }

    public OkRequest<T> trace(String url) {
        requestUrl = url;
        httpMethod = HttpMethod.TRACE;
        return this;
    }

    //================================================//
    //=================请求其他设置===================//
    //================================================//

    public OkRequest<T>  upString(String string) {
        this.upData = string;
        this.mediaType = null;
        return this;
    }

    public OkRequest<T>  upString(String string, MediaType mediaType) {
        this.upData = string;
        this.mediaType = mediaType;
        return this;
    }

    public OkRequest<T>  upJson(String json) {
        this.upData = json;
        this.mediaType = HttpParams.MEDIA_TYPE_JSON;
        return this;
    }

    public OkRequest<T>  upJson(JSONObject jsonObject) {
        this.upData = jsonObject;
        this.mediaType = null;
        return this;
    }

    public OkRequest<T>  upJson(JSONArray jsonArray) {
        this.upData = jsonArray;
        this.mediaType = null;
        return this;
    }

    public OkRequest<T>  upBytes(byte[] bs) {
        this.upData = bs;
        this.mediaType = null;
        return this;
    }

    public OkRequest<T>  upBytes(byte[] bs, MediaType mediaType) {
        this.upData = bs;
        this.mediaType = mediaType;
        return this;
    }

    public OkRequest<T>  upFile(File file) {
        this.upData = file;
        this.mediaType = null;
        return this;
    }

    public OkRequest<T>  upFile(File file, MediaType mediaType) {
        this.upData = file;
        this.mediaType = mediaType;
        return this;
    }

    public OkRequest<T> tag(String tag) {
        this.tag = tag;
        return this;
    }

    public OkRequest<T> multipart(boolean multipart) {
        this.multipart = multipart;
        return this;
    }

    public OkRequest<T> spliceUrl(boolean splice) {
        this.spliceUrl = splice;
        return this;
    }

    public OkRequest<T> retryCount(int count) {
        this.retryCount = count;
        return this;
    }

    public OkRequest<T> cacheTime(long time) {
        this.cacheTime = time;
        return this;
    }

    public OkRequest<T> cacheKey(String key) {
        this.cacheKey = key;
        return this;
    }

    public OkRequest<T> cacheMode(CacheMode mode) {
        this.cacheMode = mode;
        return this;
    }

    //================================================//
    //=================接口参数设置===================//
    //================================================//

    public OkRequest<T> params(String key, String value, boolean... replace) {
        httpParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, int value, boolean... replace) {
        httpParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, float value, boolean... replace) {
        httpParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, double value, boolean... replace) {
        httpParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, long value, boolean... replace) {
        httpParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, char value, boolean... replace) {
        httpParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(String key, boolean value, boolean... replace) {
        httpParams.put(key, value, replace);
        return this;
    }

    public OkRequest<T> params(Map<String, String> params, boolean... replace) {
        httpParams.put(params, replace);
        return this;
    }

    public OkRequest<T> params(String key, File file) {
        httpParams.put(key, file);
        return this;
    }

    public OkRequest<T> params(String key, File file, String fileName) {
        httpParams.put(key, file, fileName);
        return this;
    }

    public OkRequest<T> params(String key, File file, String fileName, MediaType contentType) {
        httpParams.put(key, file, fileName, contentType);
        return this;
    }

    public OkRequest<T> params(String key, List<File> files) {
        httpParams.putFileParams(key, files);
        return this;
    }

    public OkRequest<T> paramsWrapper(String key, HttpParams.FileWrapper fileWrapper) {
        httpParams.put(key, fileWrapper);
        return this;
    }

    public OkRequest<T> paramsWrapper(String key, List<HttpParams.FileWrapper> fileWrappers) {
        httpParams.putFileWrapperParams(key, fileWrappers);
        return this;
    }

    public OkRequest<T> addUrlParams(String key, List<String> values) {
        httpParams.putUrlParams(key, values);
        return this;
    }

    public OkRequest<T> removeParams(String key) {
        httpParams.remove(key);
        return this;
    }

    public OkRequest<T> removeAllParams() {
        httpParams.clear();
        return this;
    }

    //================================================//
    //===============二次封装部分参数==================//
    //================================================//

    public OkRequest<T> failedCodes(int ... codes) {
        for (int code : codes) {
            responseCodes.put(code, false);
        }
        return this;
    }

    public OkRequest<T> succeedCodes(int ... codes) {
        for (int code : codes) {
            responseCodes.put(code, true);
        }
        return this;
    }

    public OkRequest<T> convertHandler(ConvertHandler handler) {
        convertHandler = handler;
        return this;
    }

    public OkRequest<T> encryptHandler(EncryptHandler handler) {
        encryptHandler = handler;
        return this;
    }

    public OkRequest<T> headersInterceptor(HeadersInterceptor interceptor) {
        headersInterceptor = interceptor;
        return this;
    }

    public OkRequest<T> loading(Context context) {
        if (context instanceof Activity) {
            loadingActivity = (Activity) context;
        }
        return this;
    }

    public OkRequest<T> loading(Activity activity) {
        loadingActivity = activity;
        return this;
    }

    public OkRequest<T> loading(Fragment fragment) {
        loadingActivity = fragment.getActivity();
        return this;
    }

    public OkRequest<T> loading(android.app.Fragment fragment) {
        loadingActivity = fragment.getActivity();
        return this;
    }

    public OkRequest<T> loading(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                loadingActivity = (Activity) context;
                return this;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return this;
    }

    public OkRequest<T> loadingColors(@ColorInt int... colors) {
        OkUtils.getInstance().getLoading().colors(colors);
        return this;
    }

    public OkRequest<T> loadingSize(float size) {
        OkUtils.getInstance().getLoading().size(size);
        return this;
    }

    public OkRequest<T> loadingDimEnabled(boolean enable) {
        OkUtils.getInstance().getLoading().dimEnabled(enable);
        return this;
    }

    public Response<T> execute() throws IOException {
        Request<String, ?> request = getCommonRequest();
        OkUtils.showLoading(loadingActivity);
        okhttp3.Response response = request.execute();
        ResponseBody responseBody = response.body();
        if (response.isSuccessful() && responseBody != null) {
            String body = responseBody.string();
            if (requestType == TYPE_STRING) {
                return ResponseFactory.createResponseBody(body);
            }
            return new Gson().fromJson(body, responseType);
        }
        return ResponseFactory.createFailedResponse(response);
    }

    @SuppressWarnings("unchecked")
    public void execute(@NonNull Callback<Response<T>> callback) {
        // 如果是请求文件
        if (requestType == TYPE_FILE) {
            Request<File, ?> request = OkGo.get(requestUrl);
            request.tag(tag);
            request.params(httpParams);
            request.headers(httpHeaders);
            executeForFile(request, callback);
            return;
        }
        Request<String, ?> request = getCommonRequest();
        executeForCommon(request, callback);
    }

    private Request<String, ?> getCommonRequest() {
        // 其他正常网络请求
        Request<String, ?> request;
        switch (httpMethod) {
            case POST:
                request = OkGo.post(requestUrl);
                break;
            case PUT:
                request = OkGo.put(requestUrl);
                break;
            case DELETE:
                request = OkGo.delete(requestUrl);
                break;
            case HEAD:
                request = OkGo.head(requestUrl);
                break;
            case PATCH:
                request = OkGo.patch(requestUrl);
                break;
            case OPTIONS:
                request = OkGo.options(requestUrl);
                break;
            case TRACE:
                request = OkGo.trace(requestUrl);
                break;
            default:
                request = OkGo.get(requestUrl);
        }
        request.tag(tag)
                .retryCount(retryCount)
                .headers(httpHeaders)
                .params(handleEncrypt(httpParams));
        setCache(request);
        setUpData(request);
        if (request instanceof BodyRequest) {
            ((BodyRequest) request)
                    .isMultipart(multipart)
                    .isSpliceUrl(spliceUrl);
        }
        return request;
    }

    private void setCache(Request<String, ?> request) {
        if (cacheMode != null) {
            request.cacheMode(cacheMode);
            if (!TextUtils.isEmpty(cacheKey)) {
                request.cacheKey(cacheKey);
            }
            if (cacheTime != 0) {
                request.cacheTime(cacheTime);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setUpData(Request<String, ?> request) {
        if (upData != null && request instanceof BodyRequest) {
            BodyRequest<String, BodyRequest> bodyRequest = (BodyRequest<String, BodyRequest>) request;
            if (upData instanceof File) {
                if (mediaType == null) {
                    bodyRequest.upFile((File) upData);
                } else {
                    bodyRequest.upFile((File) upData, mediaType);
                }
            } else if (upData instanceof JSONObject) {
                bodyRequest.upJson((JSONObject) upData);
            } else if (upData instanceof JSONArray) {
                bodyRequest.upJson((JSONArray) upData);
            } else if (upData instanceof String) {
                if (mediaType == HttpParams.MEDIA_TYPE_JSON) {
                    bodyRequest.upJson((String) upData);
                } else if (mediaType == null) {
                    bodyRequest.upString((String) upData);
                } else {
                    bodyRequest.upString((String) upData, mediaType);
                }
            } else {
                if (mediaType == null) {
                    bodyRequest.upBytes((byte []) upData);
                } else {
                    bodyRequest.upBytes((byte []) upData, mediaType);
                }
            }
        }
    }

    private void executeForFile(final Request<File, ?> request, final Callback<Response<T>> callback) {
        request.execute(new FileCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                if (isReleased()) {
                    return;
                }
                OkUtils.dismissLoading(loadingActivity);
                callback.onSucceed(ResponseFactory.<T>createDownloadComplete(response.body()));
            }

            @Override
            public void onStart(Request<File, ?> request) {
                super.onStart(request);
                if (isReleased()) {
                    return;
                }
                OkUtils.showLoading(loadingActivity);
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<File> response) {
                super.onError(response);
                if (isReleased()) {
                    return;
                }
                OkUtils.dismissLoading(loadingActivity);
                callback.onFailed(null);
            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);
                if (isReleased()) {
                    return;
                }
                callback.onProgress(progress);
            }
        });
    }

    private void executeForCommon(final Request<String, ?> request, final Callback<Response<T>> callback) {
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                if (isReleased()) {
                    return;
                }
                OkUtils.dismissLoading(loadingActivity);
                String body = response.body();
                if (requestType == TYPE_STRING) {
                    callback.onSucceed(ResponseFactory.<T>createResponseBody(body));
                    return;
                }
                body = handleConvert(body);
                Headers headers = response.headers();
                if (interceptHeaders(headers)) {
                    callback.onFailed(ResponseFactory.<T>createInterceptHeaders());
                    return;
                }
                Response<T> resultResponse;
                try {
                    resultResponse = new Gson().fromJson(body, responseType);
                } catch (JsonParseException e) {
                    callback.onFailed(ResponseFactory.<T>createParseJsonException());
                    return;
                }
                if (resultResponse == null) {
                    callback.onFailed(ResponseFactory.<T>createServerException());
                    return;
                }
                if (interceptResponse(resultResponse)) {
                    callback.onFailed(ResponseFactory.<T>createInterceptResponse());
                    return;
                }
                if (isSucceedResponse(resultResponse)) {
                    callback.onSucceed(resultResponse);
                } else {
                    callback.onFailed(resultResponse);
                }
            }

            @Override
            public void onCacheSuccess(com.lzy.okgo.model.Response<String> response) {
                super.onCacheSuccess(response);
                callback.onCached(response);
            }

            @Override
            public void onStart(Request<String, ?> request) {
                super.onStart(request);
                if (isReleased()) {
                    return;
                }
                OkUtils.showLoading(loadingActivity);
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                super.onError(response);
                if (isReleased()) {
                    return;
                }
                OkUtils.dismissLoading(loadingActivity);
                switch (response.code()) {
                    case -1:
                        callback.onFailed(ResponseFactory.<T>createNoNetwork());
                        break;
                    default:
                        callback.onFailed(ResponseFactory.<T>createFailedResponse(response));
                }
            }
        });
    }

    private String handleConvert(String body) {
        ConvertHandler defaultConvertHandler = OkUtils.getInstance().getConvertHandler();
        ConvertHandler validHandler = convertHandler == null ? defaultConvertHandler : convertHandler;
        if (validHandler != null) {
            return validHandler.onHandle(body);
        }
        return body;
    }

    private HttpParams handleEncrypt(HttpParams params) {
        EncryptHandler defaultHandler = OkUtils.getInstance().getEncryptHandler();
        EncryptHandler validHandler = encryptHandler == null ? defaultHandler : encryptHandler;
        return validHandler == null ? params : validHandler.onHandle(params);
    }

    private boolean interceptHeaders(Headers headers) {
        HeadersInterceptor defaultInterceptor = OkUtils.getInstance().getHeadersInterceptor();
        HeadersInterceptor validInterceptor = headersInterceptor == null ? defaultInterceptor : headersInterceptor;
        return validInterceptor != null && validInterceptor.onIntercept(headers);
    }

    private boolean interceptResponse(Response<T> response) {
        ResponseInterceptor defaultInterceptor = OkUtils.getInstance().getResponseInterceptor();
        return defaultInterceptor != null && defaultInterceptor.onIntercept(response);
    }

    private boolean isSucceedResponse(Response<T> response) {
        int len = responseCodes.size();
        for (int i = 0; i < len; i++) {
            int code = responseCodes.keyAt(i);
            if (code == response.code()) {
                return responseCodes.valueAt(i);
            }
        }
        return false;
    }

    private boolean isReleased() {
        return reference == null || reference.get() == null;
    }

}
