package com.kbjay.android.lib_net;

import android.text.TextUtils;
import android.util.Log;

import com.kbjay.android.lib_net.annotation.KJNeedRefreshToken;
import com.kbjay.android.lib_net.converter.StringConverterFactory;

import org.reactivestreams.Publisher;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 提供retrofit
 *
 * @author v-zewan
 * @time 2019/7/3 8:07
 **/
public class KJRetrofitProvider {

    private KJOkHttpProvider mOkHttpProvider;
    private String mBaseUrl;

    private KJRetrofitProvider() {
    }

    /**
     * 为了链式美观一点，避免 new KJRetrofitProvider().withBaseUrl(xxx).xxx
     *
     * @return
     */
    public static KJRetrofitProvider newInstance() {
        return new KJRetrofitProvider();
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

    public Retrofit provide() {
        if (TextUtils.isEmpty(mBaseUrl)) {
            throw new NullPointerException("baseUrl == null");
        }

        if (mOkHttpProvider == null) {
            mOkHttpProvider = new KJOkHttpProvider();
            Log.w(KJNetConstant.TAG, "you sure not config okHttpClient?");
        }

        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(mOkHttpProvider.provide())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .build();
    }

    public <T> T provide(Class<T> clazz) {
        return provide().create(clazz);
    }

    /**
     * 返回可以自动刷新token的代理对象
     * <p>
     * 前提条件是token参数在header中并且使用了该框架的header拦截器。
     * 这样可以保证retry的时候用的是重新获取的新的token
     * <p>
     * 目前只支持flowable 不支持observable
     *
     * @param clazz
     * @param caller 同步获取token接口
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T provide(Class<T> clazz, RefreshTokenCaller caller) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new RefreshTokenProxy<T>(provide(clazz), caller));
    }


    public interface RefreshTokenCaller {
        Throwable syncRefreshToken();
    }

    /**
     * //如果没有自动刷新的注解，或者没有设置code，那么调用方法直接跳过就行
     * //如果有，那么访问服务器
     * ////如果没有返回token失效，那么跳过
     * ////如果访问返回token失效，那么请求新的token
     * //////如果成功，那么重新发起请求
     * //////如果识别，那么提示失败
     *
     * @param <T>
     */
    private static class RefreshTokenProxy<T> implements InvocationHandler {
        private RefreshTokenCaller caller;
        T obj;
        private static final int RETRY_TIME = 1;
        private int retryTime;

        public RefreshTokenProxy(T obj, RefreshTokenCaller caller) {
            this.obj = obj;
            this.caller = caller;
        }

        @Override
        public Object invoke(Object proxy, final Method method, Object[] args) throws Throwable {

            if (getRefreshTokenErrorCode(method) == -1) {
                return method.invoke(obj, args);
            }

            Object invoke = method.invoke(obj, args);
            if (!(invoke instanceof Flowable)) {
                throw new RuntimeException("only support flowable");
            }

            Flowable api = (Flowable) invoke;
            return api.retryWhen((Function<Flowable<Throwable>, Publisher<Object>>) throwableFlowable -> throwableFlowable.flatMap((Function<Throwable, Publisher<Object>>) throwable -> {
                if (throwable instanceof KJCustomException && retryTime++ <= RETRY_TIME) {
                    KJCustomException customException = (KJCustomException) throwable;
                    if (customException.getErrorCode() == getRefreshTokenErrorCode(method)) {
                        //确认了token失效了 todo 同步刷新token
                        if (caller != null) {
                            Throwable refreshTokenThrowable = caller.syncRefreshToken();
                            if (refreshTokenThrowable == null) {
                                return Flowable.just(1);
                            } else {
                                return Flowable.error(refreshTokenThrowable);
                            }
                        }
                    }
                }
                return Flowable.error(throwable);
            }));


        }

        private int getRefreshTokenErrorCode(Method method) {
            KJNeedRefreshToken annotation = method.getAnnotation(KJNeedRefreshToken.class);
            if (annotation == null) {
                return -1;
            }

            return annotation.code();
        }
    }
}
