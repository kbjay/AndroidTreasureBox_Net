package com.kbjay.android.lib_net.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标示需要刷新token
 *
 * @author v-zewan
 * @time 2019/7/3 18:24
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface KJNeedRefreshToken {
    /**
     * 跟服务端约定的token失效时返回的code
     *
     * @return
     */
    int code() default -1;
}
