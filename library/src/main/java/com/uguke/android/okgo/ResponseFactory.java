package com.uguke.android.okgo;

import java.io.File;

/**
 * 网络数据工具
 * @author LeiJue
 */
class ResponseFactory {

    static <T> Response<T> createFailedResponse(final okhttp3.Response response) {
        return new Response<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public T body() {
                return null;
            }

            @Override
            public int code() {
                return response.code();
            }

            @Override
            public String message() {
                return response.message();
            }
        };
    }

    static <T> Response<T> createFailedResponse(final com.lzy.okgo.model.Response response) {
        return new Response<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public T body() {
                return null;
            }

            @Override
            public int code() {
                return response.code();
            }

            @Override
            public String message() {
                return response.message();
            }
        };
    }

    static <T> Response<T> createResponseBody(final String body) {
        return new Response<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public T body() {
                return (T) body;
            }

            @Override
            public int code() {
                return OkUtils.getInstance().getSucceedCode();
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
                return OkUtils.getInstance().getFailedCode();
            }

            @Override
            public String message() {
                return "Json数据解析异常";
            }
        };
    }

    static <T> Response<T> createInterceptHeaders() {
        return new Response<T>() {
            @Override
            public T body() {
                return null;
            }

            @Override
            public int code() {
                return OkUtils.getInstance().getFailedCode();
            }

            @Override
            public String message() {
                return null;
            }
        };
    }

    static <T> Response<T> createInterceptResponse() {
        return new Response<T>() {
            @Override
            public T body() {
                return null;
            }

            @Override
            public int code() {
                return OkUtils.getInstance().getFailedCode();
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
                return OkUtils.getInstance().getFailedCode();
            }

            @Override
            public String message() {
                return "无网络";
            }
        };
    }

    static <T> Response<T> createServerException() {
        return new Response<T>() {
            @Override
            public T body() {
                return null;
            }

            @Override
            public int code() {
                return OkUtils.getInstance().getFailedCode();
            }

            @Override
            public String message() {
                return "服务器异常";
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
                return OkUtils.getInstance().getFailedCode();
            }

            @Override
            public String message() {
                return null;
            }
        };
    }
}
