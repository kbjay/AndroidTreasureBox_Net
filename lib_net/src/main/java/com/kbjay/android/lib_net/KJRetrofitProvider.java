package com.kbjay.android.lib_net;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.kbjay.android.lib_net.converter.StringConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 提供retrofit
 *
 * @author v-zewan
 * @time 2019/7/3 8:07
 **/
public class KJRetrofitProvider {

    private KJOkHttpProvider mOkHttpProvider;
    private String mBaseUrl;
    /**
     * 跟服务端约定的error
     */
    private ArrayMap<String, String> mCustomError;

    public static KJRetrofitProvider getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final KJRetrofitProvider INSTANCE = new KJRetrofitProvider();
    }

    private KJRetrofitProvider() {
    }

    public KJRetrofitProvider withBaseUrl(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new NullPointerException("baseUrl == null");
        }

        mBaseUrl = baseUrl;
        return this;
    }

    public KJRetrofitProvider withOkHttpProvider(KJOkHttpProvider provider) {
        if (provider == null) {
            throw new NullPointerException("provider == null");
        }

        mOkHttpProvider = provider;
        return this;
    }

    public KJRetrofitProvider withCustomError(ArrayMap<String, String> customError) {
        this.mCustomError = customError;
        return this;
    }

    public ArrayMap<String, String> getCustomErrors() {
        return mCustomError;
    }

    public Retrofit provide() {
        if (TextUtils.isEmpty(mBaseUrl)) {
            throw new NullPointerException("baseUrl == null");
        }

        if (mOkHttpProvider == null) {
            throw new NullPointerException("okhttp == null");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(mOkHttpProvider.provide())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .build();
        return retrofit;
    }

    public <T> T provide(Class<T> clazz) {
        return provide().create(clazz);
    }
}
