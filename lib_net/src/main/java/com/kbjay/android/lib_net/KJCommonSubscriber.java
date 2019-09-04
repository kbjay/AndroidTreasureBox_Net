package com.kbjay.android.lib_net;

import android.util.Log;

import com.kbjay.android.lib_net.tip.KJTipEntity;
import com.kbjay.android.lib_net.tip.KJTipManager;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.HashMap;

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
            HashMap<Integer, String> customTips = KJTipManager.getInstance().getCustomTips();
            if (customTips != null && customTips.size() > 0) {
                //如果设置了commonError以客户端设置的为准
                if (!customTips.keySet().contains(exception.getErrorCode())) {
                    code = exception.getErrorCode();
                    msg = customTips.get(exception.getErrorCode());
                } else {
                    code = exception.getErrorCode();
                    msg = exception.getErrorMsg();
                    //服务端返回了一个异常，但是不在客户端异常列表中
                    Log.w(KJNetConstant.TAG, "custom error not in client error-list" + code + " " + msg);
                }
            } else {
                Log.w(KJNetConstant.TAG, "suggest config KJTipManager");
                //客户端没有设置自定义异常
                code = exception.getErrorCode();
                msg = exception.getErrorMsg();
            }
        } else if (t instanceof IOException) {
            //网络异常
            KJTipEntity ioExceptionTip = KJTipManager.getInstance().getIOExceptionTip();
            if (ioExceptionTip != null) {
                code = ioExceptionTip.code;
                msg = ioExceptionTip.tipContent;
            } else {
                Log.w(KJNetConstant.TAG, "suggest config KJTipManager");
                code = KJNetConstant.NET_ERROR_IOEXCETION;
                msg = t.getMessage();
            }
        } else if (t instanceof HttpException) {
            //非200 异常
            KJTipEntity httpExceptionTip = KJTipManager.getInstance().getHttpExceptionTip();
            if (httpExceptionTip != null) {
                code = httpExceptionTip.code;
                msg = httpExceptionTip.tipContent;
            } else {
                Log.w(KJNetConstant.TAG, "suggest config KJTipManager");
                code = KJNetConstant.NET_ERROR_HTTPEXCEPTION;
                msg = t.getMessage();
            }
        } else {
            //未知异常
            KJTipEntity unknowTip = KJTipManager.getInstance().getUnknowTip();
            if (unknowTip != null) {
                code = unknowTip.code;
                msg = unknowTip.tipContent;
            } else {
                Log.w(KJNetConstant.TAG, "suggest config KJTipManager");
                code = KJNetConstant.NET_ERROR_UNKNOW;
                msg = KJNetConstant.NET_ERROR_UNKNOW_MSG;
            }
        }
        onError(code, msg);
    }

    @Override
    public void onComplete() {
    }

    public abstract void onError(int code, String msg);

    public abstract void onSuccess(T t);
}
