package com.kbjay.android.lib_net;


import com.google.gson.Gson;

import io.reactivex.functions.Function;

import static com.kbjay.android.lib_net.KJNetConstant.DEFUALT_RESPONSE_CODE;

/**
 * base request
 *
 * @author v-zewan
 * @time 2019/7/3 8:08
 **/
public class KJCommonRequest {
    public static class HttpResultJson<T> implements Function<String, T> {

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
