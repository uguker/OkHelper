package com.uguke.demo.okgo;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.init(this);
        ARouter.openLog();
        ARouter.openDebug();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }
}
