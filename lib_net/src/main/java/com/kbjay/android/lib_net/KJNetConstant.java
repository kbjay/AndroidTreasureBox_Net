package com.kbjay.android.lib_net;

/**
 * 对外暴露的常量
 */
public class KJNetConstant {
    /**
     * 配合host拦截器使用,baseUrl+path
     */
    public static final String REAL_URL = "real_url";

    public static final int DEFUALT_RESPONSE_CODE = 0;

    public static final String TAG = "http";
    public static final int NET_ERROR_IOEXCETION = 11100000;
    public static final int NET_ERROR_HTTPEXCEPTION = 11100001;
    public static final int NET_ERROR_UNKNOW = 11100002;
    public static final String NET_ERROR_UNKNOW_MSG = "unknow";
    public static final int NET_ERROR_JSONPARSEERROR = 11100003;
    public static final String NET_ERROR_JSONPARSEERROR_MSG = "json_parse_error";
    public static final String NET_ERROR_ERROR_BEAN_MSG = "net_error_error_bean";
    public static final int NET_ERROR_ERROR_BEAN = 11100004;
    public static final int NET_ERROR_CLIENT = 11100005;
    public static final String NET_ERROR_CLIENT_MSG = "NET_ERROR_CLIENT";
}
