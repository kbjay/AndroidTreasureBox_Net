package com.kbjay.android.lib_net;

import android.support.v4.util.ArrayMap;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @anthor kb_jay
 * create at 2019-08-22 17:31
 */
public class KJNetManager {

    public static KJRetrofitProvider getRetrofitProvider() {
        return KJRetrofitProvider.getInstance();
    }

    public static ArrayMap<String, String> getCommonError() {
        return getRetrofitProvider().getCustomErrors();
    }

    @SuppressWarnings("unchecked")
    public static <T> void doRequest(String tag, Flowable flowable, KJCommonSubscriber<T> s) {
        //设置中断请求，防止内存泄漏
        s.setCancelTag(tag);
        flowable.map(new KJCommonRequest.HttpResultJson<T>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 取消请求，避免内存泄漏
     * 需要在activity或者fragment destroy的时候调用
     *
     * @param requestTag
     */
    public static void cancel(String requestTag) {
        KJDisposableManager.getInstance().clear(requestTag);
    }
}
