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

        findViewById(R.id.ss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refresh();
//                ARouter.getInstance().build("/app/login")
//                        .navigation();
            }
        });



    }

    private void refresh() {


        OkUtils.toJson(this)
                .get("http://211.149.191.242:8080/DApp/version/index/selectIndex")
                //.get("https://api2.bmob.cn/1/classes/AppVersion")
                .params("1", "1")
                .showLoading(this)
                .succeedCodes(0, 200)

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
}
