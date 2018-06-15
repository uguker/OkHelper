package com.uguke.demo.okgo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.uguke.okgo.BaseCallback;
import com.uguke.okgo.NetBean;
import com.uguke.okgo.OkHelper;
import com.uguke.okgo.reflect.TypeBuilder;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHelper.init(getApplication())
            .setCacheTime(1000);
        String json = "{" +
                "resultcode:0," +
                "message:\"你是杀手\","+
                "data:\"我是谁\"}";

        Type type = TypeBuilder.newInstance(NetBean.class)
                .addTypeParam(String.class)
                .build();
        final NetBean<String> net = new Gson().fromJson(json, type);
        Log.e("数据", net.getCode() + net.getMessage() + net.getResult());

        //NetBean b = new NetBean();


        OkHelper
                // 返回NetBean<List<Object>>实体
                .toList(Object.class)
                // 返回NetBean<Object>实体
                //.toObj(Object.class)
                // 加上这句会显示Loading
                .with(this)
                // Loading颜色
                .loadingColor(Color.RED)
                // 文字
                .loadingText("正在加载...")
                // 背景是否昏暗
                .dimEnabled(true)
                // 方法
                //.post(url)
                // 传参
                //.params()
                //.params()
                .execute(new BaseCallback<NetBean<List<Object>>>(this) {
                    @Override
                    public void onSuccess(NetBean<List<Object>> listNetBean) {

                    }

                    @Override
                    public void onError(String msg) {

                    }
                });



    }
}
