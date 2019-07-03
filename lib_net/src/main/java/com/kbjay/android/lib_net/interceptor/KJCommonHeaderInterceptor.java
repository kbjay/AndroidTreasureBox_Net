package com.kbjay.android.lib_net.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 给请求添加相同的header
 * eg：时间戳，token，signature，设备信息等。
 *
 * @author v-zewan
 * @time 2019/7/3 16:54
 **/
public class KJCommonHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }
}
