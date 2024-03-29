package com.kbjay.android.lib_net.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 该类是为了配合处理客户端跟服务端约定的异常信息
 * 如果直接用GsonConverter的话，那么就需要在每个实体类中加入errorcode跟errorMsg字段，
 * 使用该converter之后客户端只需要关注真正的实体类
 * 另外关于code跟msg该框架有提供方法方便客户端处理出错时的提示
 *
 * @anthor kb_jay
 * create at 2019-08-23 10:58
 */
public class StringConverterFactory extends Converter.Factory {

    public static StringConverterFactory create() {
        return new StringConverterFactory();
    }

    private StringConverterFactory() {
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new StringRequestBodyConverter();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new StringResponseBodyConverter();
    }
}
