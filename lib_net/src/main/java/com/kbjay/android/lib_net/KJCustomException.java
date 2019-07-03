package com.kbjay.android.lib_net;

/**
 * 自定义异常（跟服务端约定的）
 *
 * @author v-zewan
 * @time 2019/7/3 8:12
 **/
public class KJCustomException extends Exception {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
