package com.kbjay.android.lib_net.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 给请求添加相同的header
 * eg：时间戳，token，signature，设备信息等。
 *
 * @author v-zewan
 * @time 2019/7/3 16:54
 **/
public class KJCommonHeaderInterceptor implements Interceptor {
    private final HashMap<String, String> mCommonHeader;

    public KJCommonHeaderInterceptor(CommonHeaderProvider commonHeaderProvider) {
        if (commonHeaderProvider == null) {
            throw new RuntimeException("provider can not null!");
        }

        mCommonHeader = commonHeaderProvider.provide();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (mCommonHeader == null) {
            return chain.proceed(request);
        }

        Request.Builder requestBuilder = request.newBuilder();
        Set<String> headerKeyArr = mCommonHeader.keySet();
        for (String key : headerKeyArr) {
            requestBuilder.removeHeader(key);
            String value = mCommonHeader.get(key);
            if (value == null) {
                value = "";
            }
            requestBuilder.addHeader(key, value);
        }

        return chain.proceed(requestBuilder.build());
    }

    /**
     * 提供公共的header，由接入者自己实现
     *
     * @author v-zewan
     * @time 2019/7/15 14:22
     **/
    public interface CommonHeaderProvider {
        HashMap<String, String> provide();
    }
}
