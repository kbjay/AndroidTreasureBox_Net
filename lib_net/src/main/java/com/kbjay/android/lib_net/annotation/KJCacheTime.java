package com.kbjay.android.lib_net.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存时间
 *
 * @author v-zewan
 * @time 2019/7/3 18:27
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface KJCacheTime {
    /**
     * 缓存秒数
     *
     * @return
     */
    int second();
}
