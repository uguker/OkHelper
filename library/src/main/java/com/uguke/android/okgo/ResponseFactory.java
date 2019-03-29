package com.uguke.android.okgo;

import java.io.File;

/**
 * 网络数据工具
 * @author LeiJue
 */
class ResponseFactory {

    static <T> Response<T> createFailedResponse(final okhttp3.Response response) {
        int code = response.code();
        String message = response.message();
        return ResponseFactory.createNewResponse(null, code, message);
    }

    static <T> Response<T> createFailedResponse(final com.lzy.okgo.model.Response response) {
        int code = response.code();
        String message = response.message();
        return ResponseFactory.createNewResponse(null, code, message);
    }

    @SuppressWarnings("unchecked")
    static <T> Response<T> createResponseBody(final String body) {
        int code = OkUtils.getInstance().getSucceedCode();
        return ResponseFactory.createNewResponse((T) body, code, null);
    }

    static <T> Response<T> createParseJsonException() {
        int code = OkUtils.getInstance().getFailedCode();
        return ResponseFactory.createNewResponse(null, code, "Json数据解析异常");
    }

    static <T> Response<T> createInterceptHeaders() {
        int code = OkUtils.getInstance().getFailedCode();
        return ResponseFactory.createNewResponse(null, code, null);
    }

    static <T> Response<T> createInterceptResponse() {
        int code = OkUtils.getInstance().getFailedCode();
        return ResponseFactory.createNewResponse(null, code, null);
    }

    static <T> Response<T> createNoNetwork() {
        int code = OkUtils.getInstance().getFailedCode();
        return ResponseFactory.createNewResponse(null, code, "无网络");
    }

    static <T> Response<T> createServerException() {
        int code = OkUtils.getInstance().getFailedCode();
        return ResponseFactory.createNewResponse(null, code, "服务器异常");
    }

    @SuppressWarnings("unchecked")
    static  <T> Response<T> createDownloadComplete(final File file) {
        int code = OkUtils.getInstance().getSucceedCode();
        return ResponseFactory.createNewResponse((T) file, code, null);
    }

    private static <T> Response<T> createNewResponse(final T body, final int code, final String message) {
        return new Response<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public T body() {
                return body;
            }

            @Override
            public int code() {
                return code;
            }

            @Override
            public String message() {
                return message;
            }
        };
    }
}
