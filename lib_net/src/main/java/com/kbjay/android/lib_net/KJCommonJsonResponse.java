package com.kbjay.android.lib_net;

/**
 * json response
 * <p>
 * 服务端返回的json必须是正常的格式
 *
 * @author v-zewan
 * @time 2019/7/3 17:28
 **/
public class KJCommonJsonResponse<T> {
    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
