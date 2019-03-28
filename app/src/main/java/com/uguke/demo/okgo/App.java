package com.uguke.demo.okgo;

import android.app.Application;

import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.uguke.android.okgo.ConvertHandler;
import com.uguke.android.okgo.EncryptHandler;
import com.uguke.android.okgo.HeadersInterceptor;
import com.uguke.android.okgo.LoadingDialog;
import com.uguke.android.okgo.OkUtils;
import com.uguke.android.okgo.Response;
import com.uguke.android.okgo.ResponseImpl;
import com.uguke.android.okgo.ResponseInterceptor;

import okhttp3.Headers;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HttpHeaders headers = new HttpHeaders();
        headers.put("X-Bmob-Application-Id", "c64afbe25c179804bedb128bb2a7b73b");
        headers.put("X-Bmob-REST-API-Key", "bddfd9994878419feca3987943ad05ef");
        headers.put("Content-Type", "application/json");
        OkUtils.getInstance()
                .init(this)
                .openDebug()
                .setFailedCode(101)
                .setSucceedCode(200)
                .setResponseClass(ResponseImpl.class)
                .addCommonHeaders(headers)
                .setConvertHandler(new ConvertHandler() {
                    @Override
                    public String onHandle(String body) {
                        return body;
                    }
                })
                .setEncryptHandler(new EncryptHandler() {
                    @Override
                    public HttpParams onHandle(HttpParams params) {
                        return params;
                    }
                })
                .setHeadersInterceptor(new HeadersInterceptor() {
                    @Override
                    public boolean onIntercept(Headers headers) {
                        return false;
                    }
                })
                .setResponseInterceptor(new ResponseInterceptor() {
                    @Override
                    public boolean onIntercept(Response response) {
                        return false;
                    }
                })
                .setLoading(new LoadingDialog().duration(500).dimEnabled(true));
    }
}
