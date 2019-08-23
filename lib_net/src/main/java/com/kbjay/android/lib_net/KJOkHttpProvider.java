package com.kbjay.android.lib_net;

import android.util.Log;

import com.kbjay.android.lib_net.interceptor.KJCommonHeaderInterceptor;
import com.kbjay.android.lib_net.interceptor.KJHostInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 提供okhttp
 *
 * @author v-zewan
 * @time 2019/7/2 22:38
 **/

public class KJOkHttpProvider {

    private static final String TAG = "http";
    private OkHttpClient mOkHttpClient;

    /**
     * 根据builder的参数构建okHttpClient
     *
     * @param builder
     */
    private KJOkHttpProvider(Builder builder) {
        OkHttpClient.Builder okHttpBuilder = builder.mOkHttpClient.newBuilder();

        if (builder.mIsLogOpen) {
            //解决部分机型不打印log
            okHttpBuilder.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    // TODO: 2019/7/15 修改log->logging
                    Log.d(TAG, message);
                }
            }));
        }

        if (builder.mIsMultHost) {
            okHttpBuilder.addInterceptor(new KJHostInterceptor());
        }

        if (builder.mCommomHeaderProvider != null) {
            okHttpBuilder.addInterceptor(new KJCommonHeaderInterceptor(builder.mCommomHeaderProvider));
        }

        mOkHttpClient = okHttpBuilder.build();
    }

    public OkHttpClient provide() {
        return mOkHttpClient;
    }

    /**
     * 扩展okhttp的功能（设置参数）
     * 1：log开关
     * 2：todo mock开关
     * 3：multHost开关
     * 4：commonHeader
     * 5：todo cache开关
     *
     * @author v-zewan
     * @time 2019/7/15 11:31
     **/
    public static final class Builder {
        OkHttpClient mOkHttpClient;
        boolean mIsLogOpen;
        boolean mIsMultHost;
        KJCommonHeaderInterceptor.CommonHeaderProvider mCommomHeaderProvider;

        public Builder(OkHttpClient okHttpClient) {
            this.mOkHttpClient = okHttpClient;
        }

        public Builder() {
            this.mOkHttpClient = new OkHttpClient.Builder().build();
        }

        public Builder setIsLogOpen(boolean isLogOpen) {
            this.mIsLogOpen = isLogOpen;
            return this;
        }

        public Builder setIsMultHost(boolean isMultHost) {
            this.mIsMultHost = isMultHost;
            return this;
        }

        public Builder setCommonHeader(KJCommonHeaderInterceptor.CommonHeaderProvider provider) {
            this.mCommomHeaderProvider = provider;
            return this;
        }

        public KJOkHttpProvider build() {
            return new KJOkHttpProvider(this);
        }
    }
}
