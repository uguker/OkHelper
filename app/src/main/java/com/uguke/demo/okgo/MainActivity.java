package com.uguke.demo.okgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lzy.okgo.OkGo;

import com.uguke.android.okgo.Callback;
import com.uguke.android.okgo.NetData;
import com.uguke.android.okgo.NetDataImpl;
import com.uguke.android.okgo.OkUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkUtils.init(getApplication());
        String json = "{" +
                "resultcode:0," +
                "message:\"你是杀手\","+
                "data:\"我是谁\"}";

        findViewById(R.id.ss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submit();
                refresh();
            }
        });

        OkUtils.setNetDataImplClass(NetDataImpl.class);
        OkGo.getInstance().setCacheTime(1000);
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

    }

    private void refresh() {
        OkUtils.toObj(this)
                .get("http://211.149.191.242:8080/DApp/version/index/selectIndex")
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
                .execute(new Callback<NetData<Object>>() {
                    @Override
                    public void onSucceed(NetData<Object> data) {
                        Log.e("数据", "呵呵3");
                    }

                    @Override
                    public void onFailed(String msg) {

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
