package com.kbjay.android.lib_net;

/**
 * json response
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

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
