package com.kbjay.android.lib_net;

/**
 * 自定义异常（跟服务端约定的）
 *
 * @author v-zewan
 * @time 2019/7/3 8:12
 **/
public class KJCustomException extends Exception {
    private int errorCode;
    private String errorMsg;

    public KJCustomException(int code, String message) {
        this.errorCode = code;
        this.errorMsg = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
