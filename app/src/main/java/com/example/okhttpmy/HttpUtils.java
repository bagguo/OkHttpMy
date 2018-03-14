package com.example.okhttpmy;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by guodazhao on 2018/2/27 0027.
 */

public class HttpUtils {

    //测试服务器
    //线上服务器
    private static final String BASE_URL = "http://mapi.damai.cn/";

    private static HttpUtils instance;
    private OkHttpClient client;
    private Handler handler;

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }

        }
        return instance;
    }

    //执行一次，初始化的操作
    private HttpUtils() {

        //log拦截器,拦截器等级
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i(TAG, "log: ===========" + message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//缓存路径
        File cacheFile = FileUtils.getHttpCacheFile();
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10);
//1、构建okhttpclient
        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .cache(cache)//配置缓存路径
//                .addNetworkInterceptor()
//                .addInterceptor(new Interceptor() {//Application 拦截器 拦截请求
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        //拦截请求
//                        Request request = chain.request();
//                        //添加公共参数
//                        //打印日志
//
//                        //执行网络请求
//                        Response response = chain.proceed(request);
//                        String string = response.body().string();
//
//                        //status
//                        //统一处理
//
//                        return null;
//                    }
//                })
                .build();

        //保证回调在主线程执行
        //looper在那个线程 handler handlemessage就在哪个线程执行
        //looper。getmainlooper 保证handlermessage在主线程执行
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * get请求
     * @param path
     * @param params
     * @param callBack
     */
    public void get(String path, HashMap<String, String> params, final HttpResponse callBack) {
//1、将参数进行拼串处理
        //url地址拼接
        //http://mapi.damai.cn/proj/HotProjV1.aspx？
        StringBuilder url = new StringBuilder();
        url.append(BASE_URL);
        url.append(path);
        url.append("?");
        //1、判断不为空 2、集合内数据不为空
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                url.append(entry.getKey());
                url.append("=");
                url.append(entry.getValue());
                url.append("&");
            }
        }
        url.setLength(url.length() - 1);
//2、构建请求体
        Request request = new Request.Builder()
                .url(url.toString())
                .addHeader("Connection", "Keep-Alive")//保持长连接，减少http握手消耗的时间
                .addHeader("Accept-Encoding", "gzip")
                .build();
//3、请求
        //下载 同步
        //普通请求 异步
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callBack != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError(e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (callBack == null) return;
                //响应后响应可能为空
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onResponse(data);
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError(response.message());
                        }
                    });
                }
            }
        });


    }

    public static HashMap<String, String> getBasicParams() {
        HashMap<String, String> basic = new HashMap<>();
        basic.put("osType", NetConfig.osType);
        basic.put("channel_from", NetConfig.channel_from);
        basic.put("source", NetConfig.source);
        basic.put("version", NetConfig.version);
        basic.put("appType", NetConfig.appType);

        return basic;
    }













}
