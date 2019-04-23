package com.hongguo.common.retrofit.convert;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by losg on 2017/7/27.
 */

public class StringConverter implements Converter<ResponseBody, String> {

    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }
}
