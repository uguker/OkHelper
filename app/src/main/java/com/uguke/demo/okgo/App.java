package com.uguke.demo.okgo;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lzy.okgo.model.HttpHeaders;
import com.uguke.android.okgo.OkUtils;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.init(this);
        ARouter.openLog();
        ARouter.openDebug();
        OkUtils.getInstance().init(this);
        HttpHeaders headers = new HttpHeaders();
        headers.put("X-Bmob-Application-Id", "c64afbe25c179804bedb128bb2a7b73b");
        headers.put("X-Bmob-REST-API-Key", "bddfd9994878419feca3987943ad05ef");
        OkUtils.addCommonHeaders(headers);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }
}
