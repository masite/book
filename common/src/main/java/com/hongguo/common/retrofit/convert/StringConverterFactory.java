package com.hongguo.common.retrofit.convert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by losg on 2017/7/27.
 */

public class StringConverterFactory extends Converter.Factory {

    @Override
    public Converter<ResponseBody, String> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new StringConverter();
    }
}
