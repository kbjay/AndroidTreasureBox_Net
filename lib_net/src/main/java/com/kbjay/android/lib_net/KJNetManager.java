package com.kbjay.android.lib_net;

import com.google.gson.Gson;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.kbjay.android.lib_net.KJNetConstant.DEFUALT_RESPONSE_CODE;

/**
 * 负责开启以及取消网络请求
 *
 * @anthor kb_jay
 * create at 2019-08-22 17:31
 */
public class KJNetManager {

    @SuppressWarnings("unchecked")
    public static <T> void doRequest(String tag, Flowable flowable, KJCommonSubscriber<T> s) {
        //设置中断请求，防止内存泄漏
        s.setCancelTag(tag);
        flowable.map(new HttpResultJson<T>())
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

    private static class HttpResultJson<T> implements Function<String, T> {

        @Override
        public T apply(String s) throws Exception {
            KJCommonJsonResponse jsonResponse = null;

            try {
                Gson gson = new Gson();
                jsonResponse = gson.fromJson(s, KJCommonJsonResponse.class);
            } catch (Exception e) {
                throw new KJCustomException(KJNetConstant.NET_ERROR_JSONPARSEERROR, KJNetConstant.NET_ERROR_JSONPARSEERROR_MSG);
            }

            if (jsonResponse != null && jsonResponse.getCode() != DEFUALT_RESPONSE_CODE) {
                throw new KJCustomException(jsonResponse.getCode(), jsonResponse.getMessage());
            }

            return (T) jsonResponse.getData();
        }
    }

}
