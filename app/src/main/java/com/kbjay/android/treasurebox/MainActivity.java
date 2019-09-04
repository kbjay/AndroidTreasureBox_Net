package com.kbjay.android.treasurebox;

import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.kbjay.android.lib_net.KJCommonSubscriber;
import com.kbjay.android.lib_net.KJNetManager;
import com.kbjay.android.lib_net.KJOkHttpProvider;
import com.kbjay.android.lib_net.KJRetrofitProvider;
import com.kbjay.android.treasurebox.test_net.Api;
import com.kbjay.android.treasurebox.test_net.Data;

import java.security.Provider;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayMap<String, String> commonHeader = new ArrayMap<>();
        commonHeader.put("token", "123");

        KJOkHttpProvider okHttpProvider = new KJOkHttpProvider.Builder()
                .setCommonHeader(() -> commonHeader)
                .setIsLogOpen(true)
                .setIsMultHost(true)
                .build();

        api = KJRetrofitProvider.newInstance()
                .withBaseUrl("https://wanandroid.com")
                .withOkHttpProvider(okHttpProvider)
                .provide(Api.class);

        this.findViewById(R.id.button).setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "hhh", Toast.LENGTH_SHORT).show();
            KJNetManager.doRequest(MainActivity.class.getSimpleName(),
                    api.test(),
                    new KJCommonSubscriber<ArrayList<Data>>() {

                        @Override
                        public void onError(int code, String msg) {
                            Toast.makeText(MainActivity.this, code + " " + msg, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(ArrayList<Data> data) {
                            Toast.makeText(MainActivity.this, data.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消网络请求
        KJNetManager.cancel(MainActivity.class.getSimpleName());
    }
}

