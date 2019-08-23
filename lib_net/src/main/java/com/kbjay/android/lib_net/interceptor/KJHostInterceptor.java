package com.kbjay.android.lib_net.interceptor;

import android.text.TextUtils;

import com.kbjay.android.lib_net.KJNetConstant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 多个host
 * <p>
 * 使用者需要添加header参数指定{@link com.kbjay.android.lib_net.KJNetConstant#REAL_URL}
 * 注意是全路径
 *
 * @author v-zewan
 * @time 2019/7/3 8:17
 **/
public class KJHostInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String realDomain = request.header(KJNetConstant.REAL_URL);
        if (!TextUtils.isEmpty(realDomain)) {
            request = request.newBuilder().url(realDomain).build();
        }

        return chain.proceed(request);
    }
}

