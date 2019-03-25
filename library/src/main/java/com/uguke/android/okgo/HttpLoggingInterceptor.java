package com.uguke.android.okgo;

import android.util.Log;

import com.lzy.okgo.utils.IOUtils;
import com.lzy.okgo.utils.OkLogger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;

public class HttpLoggingInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private volatile Level printLevel = Level.NONE;
    private java.util.logging.Level colorLevel;
    private Logger logger;

    private StringBuffer logContent = new StringBuffer();

    private String tag = "数据";
    /** 当前日志等级 **/
    private ThreadLocal<Level> mCurrentLevel;
    /** 当前方法数 **/
    private ThreadLocal<Integer> mCurrentCount;
    /** 当前Tag **/
    private ThreadLocal<String> mCurrentTag;

    private int logLineLength;

    public enum Level {
        //不打印log
        NONE,
        //只打印 请求首行 和 响应首行
        BASIC,
        //打印请求和响应的所有 Header
        HEADERS,
        //所有数据全部打印
        BODY
    }

    public HttpLoggingInterceptor(String tag) {
        logger = Logger.getLogger(tag);

    }

    public void setPrintLevel(Level level) {
        if (printLevel == null) throw new NullPointerException("printLevel == null. Use Level.NONE instead.");
        printLevel = level;
    }

    public void setColorLevel(java.util.logging.Level level) {
        colorLevel = level;
    }

    private void log(String message) {
        logger.log(colorLevel, message);
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (printLevel == Level.NONE) {
            return chain.proceed(request);
        }
        logContent.setLength(0);
        //请求日志拦截
        logForRequest(request, chain.connection());

        //执行请求，计算请求时间
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            log("<-- HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        //响应日志拦截
        return logForResponse(response, tookMs);
    }

    private void logForRequest(Request request, Connection connection) throws IOException {
        boolean logBody = (printLevel == Level.BODY);
        boolean logHeaders = (printLevel == Level.BODY || printLevel == Level.HEADERS);
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        try {
            String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
            //log(requestStartMessage);
            Log.e(tag, requestStartMessage);

            if (logHeaders) {
                if (hasRequestBody) {
                    // Request body headers are only present when installed as a network interceptor. Force
                    // them to be included (when available) so there values are known.
                    if (requestBody.contentType() != null) {
                        //log("\tContent-Type: " + requestBody.contentType());
                        Log.e(tag, "Content-Type: " + requestBody.contentType());
                    }
                    if (requestBody.contentLength() != -1) {
                        //log("\tContent-Length: " + requestBody.contentLength());
                        Log.e(tag, "Content-Length: " + requestBody.contentLength());
                    }
                }
                Headers headers = request.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
                    String name = headers.name(i);
                    // Skip headers from the request body as they are explicitly logged above.
                    if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                        //log("\t" + name + ": " + headers.value(i));
                        Log.e(tag, name + ": " + headers.value(i));
                    }
                }

                //log(" ");
                if (logBody && hasRequestBody) {
                    if (isPlaintext(requestBody.contentType())) {
                        bodyToString(request);
                    } else {
                        Log.e(tag, "body: maybe [binary body], omitted!");
                    }
                }
            }
        } catch (Exception e) {
            OkLogger.printStackTrace(e);
        } finally {
            Log.e(tag, "--> END " + request.method());
        }
    }

    private Response logForResponse(Response response, long tookMs) {
        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();
        ResponseBody responseBody = clone.body();
        boolean logBody = (printLevel == Level.BODY);
        boolean logHeaders = (printLevel == Level.BODY || printLevel == Level.HEADERS);

        try {
            Log.e(tag, "<-- " + clone.code() + ' ' + clone.message() + ' ' + clone.request().url() + " (" + tookMs + "ms）");
            if (logHeaders) {
                Headers headers = clone.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
                    Log.e(tag, headers.name(i) + ": " + headers.value(i));
                }
                //log(" ");
                if (logBody && HttpHeaders.hasBody(clone)) {
                    if (responseBody == null) return response;

                    if (isPlaintext(responseBody.contentType())) {
                        byte[] bytes = IOUtils.toByteArray(responseBody.byteStream());
                        MediaType contentType = responseBody.contentType();
                        String body = new String(bytes, getCharset(contentType));
                        Log.e(tag, "body:" + body);

                        responseBody = ResponseBody.create(responseBody.contentType(), bytes);
                        return response.newBuilder().body(responseBody).build();
                    } else {
                        Log.e(tag, "body: maybe [binary body], omitted!");
                    }
                }
            }
        } catch (Exception e) {
            OkLogger.printStackTrace(e);
        } finally {
            Log.e(tag, "<-- END HTTP");
        }
        return response;
    }

    private static Charset getCharset(MediaType contentType) {
        Charset charset = contentType != null ? contentType.charset(UTF8) : UTF8;
        return charset == null ? UTF8 : charset;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private static boolean isPlaintext(MediaType mediaType) {
        if (mediaType == null) return false;
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        String subtype = mediaType.subtype();
        if (subtype != null) {
            subtype = subtype.toLowerCase();
            if (subtype.contains("x-www-form-urlencoded") || subtype.contains("json") || subtype.contains("xml") || subtype.contains("html")) //
                return true;
        }
        return false;
    }

    private void bodyToString(Request request) {
        try {
            Request copy = request.newBuilder().build();
            RequestBody body = copy.body();
            if (body == null) {
                return;
            }
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            Charset charset = getCharset(body.contentType());
            Log.e(tag, "body:" + buffer.readString(charset));
        } catch (Exception e) {
            OkLogger.printStackTrace(e);
        }
    }


    private void logHeader() {

    }

    private void logContent() {

    }

    private synchronized void log(Level level, Object msg) {
        // 保存当前日志等级
        mCurrentLevel.remove();
        mCurrentLevel.set(level);

            // 获取边框
            String tableTop = StringUtils.getInstance().getTableTop(logLineLength);
            String tableBottom = StringUtils.getInstance().getTableBottom(logLineLength);
            //String content = toContent(msg, strategy.getLang());
            //String tag = Utils.tag(strategy.getTag(), mCurrentTag.get());

            Log.e(tag, tableTop);
            Log.e(tag, tableBottom);

            // 绘制上边框
            //adapter.log(level, tag, top);
            // 绘制头部
            //logHeader(adapter, tag);
            // 绘制内容
            //logContent(adapter, tag, content);
            // 绘制异常内容
            //logThrowable(adapter, tag, t);
            // 绘制下边框
            //adapter.log(level, tag, bottom);
    }
}
