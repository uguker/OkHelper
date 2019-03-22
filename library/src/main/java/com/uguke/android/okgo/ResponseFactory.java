package com.uguke.android.okgo;

import java.io.File;

/**
 * 功能描述：网络数据工具
 * @author LeiJue
 */
class ResponseFactory {

    static <T> Response<T> createResponseBody(final String body) {
        return new Response<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public T body() {
                return (T) body;
            }

            @Override
            public int code() {
                return 0;
            }

            @Override
            public String message() {
                return null;
            }
        };
    }

    static <T> Response<T> createParseJsonException() {
        return new Response<T>() {
            @Override
            public T body() {
                return null;
            }

            @Override
            public int code() {
                return OkRequest.defaultFailedCode;
            }

            @Override
            public String message() {
                return "Json数据解析异常";
            }
        };
    }

    static <T> Response<T> createHandledHeaders() {
        return new Response<T>() {
            @Override
            public T body() {
                return null;
            }

            @Override
            public int code() {
                return OkRequest.defaultFailedCode;
            }

            @Override
            public String message() {
                return null;
            }
        };
    }

    static <T> Response<T> createPretreatResponse() {
        return new Response<T>() {
            @Override
            public T body() {
                return null;
            }

            @Override
            public int code() {
                return OkRequest.defaultFailedCode;
            }

            @Override
            public String message() {
                return null;
            }
        };
    }

    static <T> Response<T> createNoNetwork() {
        return new Response<T>() {
            @Override
            public T body() {
                return null;
            }

            @Override
            public int code() {
                return OkRequest.defaultFailedCode;
            }

            @Override
            public String message() {
                return "无网络";
            }
        };
    }

    static  <T> Response<T> createDownloadComplete(final File file) {
        return new Response<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public T body() {
                return (T) file;
            }

            @Override
            public int code() {
                return OkRequest.defaultSucceedCode;
            }

            @Override
            public String message() {
                return null;
            }
        };
    }
}
