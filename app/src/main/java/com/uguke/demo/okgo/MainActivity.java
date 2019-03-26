package com.uguke.demo.okgo;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.uguke.android.okgo.Callback;
import com.uguke.android.okgo.Response;
import com.uguke.android.okgo.ResponseImpl;
import com.uguke.android.okgo.OkUtils;

import java.util.Locale;

@Route(path = "/app/main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ARouter.getInstance().inject(this);
        OkUtils.openDebug();
        findViewById(R.id.ss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submit();
                refresh();
//                ARouter.getInstance().build("/app/login")
//                        .navigation();
            }
        });
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = Resources.getSystem().getConfiguration().locale;
        }

        Drawable d = getResources().getDrawable(R.mipmap.ic_launcher);

        Log.e("数据", (locale.getLanguage()) + "");

        OkUtils.setNetDataImplClass(ResponseImpl.class);
        //OkGo.getInstance().setCacheTime(1000);
        OkUtils.setSucceedCode(10);
//        OkUtils.setFiltersHandler(new FiltersHandler() {
//            @Override
//            public boolean onHandle(int code) {
//                Log.e("数据", "呵呵");
//                return true;
//            }
//        });
//
//        OkUtils.setHeadersHandler(new HeadersHandler() {
//            @Override
//            public boolean onHandle(Headers headers) {
//                return false;
//            }
//        });
        //refresh();
    }

    private void refresh() {



        OkUtils.toJson(this)
                //.get("http://211.149.191.242:8080/DApp/version/index/selectIndex")
                .get("https://api2.bmob.cn/1/classes/AppVersion")
                .loading(this)
                .succeedCodes(0, 200)
//                .filters(200)
//                .filtersHandler(new FiltersHandler() {
//                    @Override
//                    public boolean onHandle(int code) {
//                        Log.e("数据", "呵呵2");
//                        return true;
//                    }
//                })
//                .headersHandler(new HeadersHandler() {
//                    @Override
//                    public boolean onHandle(Headers headers) {
//                        return false;
//                    }
//                })

                .execute(new Callback<Response<String>>() {
                    @Override
                    public void onSucceed(Response<String> response) {
                        Log.e("数据", response.body());
                    }

                    @Override
                    public void onFailed(Response<String> response) {
                        Log.e("数据", response.message());
                    }
                });

    }

    private void submit() {


//
//        OkUtils
//                // 返回NetBean<List<Object>>实体
//                .toFile()
//                .filters(true, 11111,1,1)
//                // Loading颜色
//                .loadingColor(Color.RED)
//                // 文字
//                .loadingText("正在加载...")
//                // 背景是否昏暗
//                .loadingDimEnable(true)
//                // 方法
//                .get("https://imtt.dd.qq.com/16891/10CFF2EC7BF5B2C260A8E3F9A5011576.apk?fsname=com.qq.reader_6.7.0.667_119.apk&csr=1bbd")
//                // 传参
//                //.params()
//                //.params()
//
//                .execute(new Callback<NetData<File>>() {
//                    @Override
//                    public void onSucceed(NetData<File> data) {
//                        Log.e("数据", "下载成功");
//                    }
//
//                    @Override
//                    public void onFailed(String msg) {
//                        Log.e("数据", msg);
//                    }
//
//                    @Override
//                    public void onProgress(Progress progress) {
//                        super.onProgress(progress);
//
//                        Log.e("数据", progress.fraction + "");
//                    }
//                });
    }
}
