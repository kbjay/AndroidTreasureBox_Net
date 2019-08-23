package com.kbjay.android.lib_net;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import retrofit2.HttpException;

/**
 * 异常统一处理
 *
 * @author v-zewan
 * @time 2019/7/3 8:14
 **/
public abstract class KJCommonSubscriber<T> implements Subscriber<T> {

    private String mCancelTag;

    public void setCancelTag(String mCancelTag) {
        this.mCancelTag = mCancelTag;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(1);
        KJDisposableManager.getInstance().put(mCancelTag, s);
    }

    @Override
    public void onNext(T t) {
        try {
            onSuccess(t);
        } catch (Exception e) {
            if (e instanceof ClassCastException) {
                onError(KJNetConstant.NET_ERROR_ERROR_BEAN, KJNetConstant.NET_ERROR_ERROR_BEAN_MSG);
            } else {
                onError(KJNetConstant.NET_ERROR_CLIENT, KJNetConstant.NET_ERROR_CLIENT_MSG);
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        int code;
        String msg;
        if (t instanceof KJCustomException) {
            KJCustomException exception = (KJCustomException) t;
            ArrayMap<String, String> commonError = KJNetManager.getCommonError();
            if (commonError != null && commonError.size() > 0) {
                //如果设置了commonError以客户端设置的为准
                if (!commonError.keySet().contains(String.valueOf(exception.getErrorCode()))) {
                    code = exception.getErrorCode();
                    msg = commonError.get(String.valueOf(exception.getErrorCode()));
                } else {
                    Log.w(KJNetConstant.TAG, "custom error not in client error-list");
                    code = exception.getErrorCode();
                    msg = exception.getErrorMsg();
                }
            } else {
                code = exception.getErrorCode();
                msg = exception.getErrorMsg();
            }
        } else if (t instanceof IOException) {
            //网络异常
            code = KJNetConstant.NET_ERROR_IOEXCETION;
            msg = t.getMessage();
        } else if (t instanceof HttpException) {
            //非200 异常
            code = KJNetConstant.NET_ERROR_HTTPEXCEPTION;
            msg = t.getMessage();
        } else {
            //未知异常
            code = KJNetConstant.NET_ERROR_UNKNOW;
            msg = KJNetConstant.NET_ERROR_UNKNOW_MSG;
        }
        onError(code, msg);
    }

    @Override
    public void onComplete() {
    }

    public abstract void onError(int code, String msg);

    public abstract void onSuccess(T t);
}
