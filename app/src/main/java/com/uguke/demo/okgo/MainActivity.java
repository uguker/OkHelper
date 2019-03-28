package com.uguke.demo.okgo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.uguke.android.okgo.OkUtils;

import java.io.IOException;

@Route(path = "/app/main")
public class MainActivity extends AppCompatActivity {

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.ss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refresh(view);
//                ARouter.getInstance().build("/app/login")
//                        .navigation();

            }
        });
        ((ViewGroup)(findViewById(R.id.ss).getParent())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "呵呵", 1000).show();
            }
        });


    }

    private void refresh(final View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkUtils.toInitial(this)
                            .get("http://211.149.191.242:8080/DApp/version/index/selectIndex")
                            //.get("https://api2.bmob.cn/1/classes/AppVersion")
                            .params("1", "1")
                            .multipart(true)
                            .spliceUrl(true)
                            .loading(view)
                            .loadingColors(Color.BLACK)
                            .succeedCodes(0, 200)
                            .execute();
                    OkUtils.dismissLoading(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        OkUtils.toJson(this)
//                .get("http://211.149.191.242:8080/DApp/version/index/selectIndex")
//                //.get("https://api2.bmob.cn/1/classes/AppVersion")
//                .params("1", "1")
//                .multipart(true)
//                .spliceUrl(true)
//                .loading(view)
//                .loadingColors(Color.BLACK)
//                .succeedCodes(0, 200)
//                .execute(new Callback<Response<String>>() {
//                    @Override
//                    public void onSucceed(Response<String> response) {
//                        Log.e("数据", response.body());
//                    }
//
//                    @Override
//                    public void onFailed(Response<String> response) {
//                        Log.e("数据", response.message());
//                    }
//                });



    }
}
