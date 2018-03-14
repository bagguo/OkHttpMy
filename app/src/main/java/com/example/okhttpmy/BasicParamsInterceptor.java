package com.example.okhttpmy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 网络操作，添加 公用参数 的拦截器
 * Created by guodazhao on 2018/2/28 0028.
 */

public class BasicParamsInterceptor implements Interceptor {

    Map<String, String> queryParamsMap = new HashMap<>();
    Map<String, String> paramsMap = new HashMap<>();
    Map<String, String> headerParamsMap = new HashMap<>();
    List<String> headerLinesList = new ArrayList<>();

    public BasicParamsInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }
}
