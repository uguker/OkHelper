package com.uguke.demo.okgo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.uguke.android.okgo.Callback;
import com.uguke.android.okgo.OkUtils;
import com.uguke.android.okgo.Response;


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

        OkUtils.toObj(this)
                .get("http://211.149.191.242:8080/DApp/version/index/selectIndex")
                .params("1", "1")
                .multipart(true)
                .spliceUrl(true)
                .loading(view)
                .loadingColors(Color.BLACK)
                .succeedCodes(0, 200)
                .execute(new Callback<Response<Object>>() {
                    @Override
                    public void onSucceed(Response<Object> response) {

                    }

                    @Override
                    public void onFailed(Response<Object> response) {

                    }
                });

    }
}
