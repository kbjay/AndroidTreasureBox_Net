package com.kbjay.android.lib_net;

import retrofit2.Retrofit;

/**
 * 提供retrofit
 *
 * @author v-zewan
 * @time 2019/7/3 8:07
 **/
public class KJRetrofitProvider {
    private Retrofit mRetrofit;

    public KJRetrofitProvider(String baseUrl) {
        this(new KJOkHttpProvider.Builder().build(), baseUrl);
    }

    public KJRetrofitProvider(KJOkHttpProvider okHttpProvider, String baseUrl) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpProvider.provide())
                .build();
    }

    private Retrofit provide() {
        return mRetrofit;
    }
}
