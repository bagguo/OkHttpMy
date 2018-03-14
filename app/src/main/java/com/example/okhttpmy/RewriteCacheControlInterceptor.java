package com.example.okhttpmy;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by guodazhao on 2018/2/28 0028.
 *
 * 缓存网络数据的拦截器
 * POST方法没有缓存
 * 头部cache-control设为max-age=0时则不会使用缓存
 * 而请求服务器
 * 为if-only-cache时只查询缓存而不会请求服务器
 * max-stale可以配合设置缓存失效时间
 */

public class RewriteCacheControlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        return null;
    }
}
