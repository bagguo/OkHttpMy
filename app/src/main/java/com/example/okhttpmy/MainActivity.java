package com.example.okhttpmy;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private String baseUrl="http://10.0.164.217:8080/MyWeb/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//1、基本参数
        HashMap<String, String> params = HttpUtils.getBasicParams();
        params.put("cityId", "852");
//2、get请求
        HttpUtils.getInstance()
                .get("proj/HotProjV1.aspx", params, new HttpResponse() {
                    @Override
                    public void onResponse(String data) {
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        HashMap<String, String> params1 = HttpUtils.getBasicParams();
        params1.put("cityId", "852");

        HttpUtils.getInstance()
                .get("Proj/Panev3.aspx", params1, new HttpResponse() {
                    @Override
                    public void onResponse(String data) {
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }
//get方式
    private void getMath(){
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "aaaa");
        params.put("password", "bbbb");
        HttpUtils.getInstance().get("RegServlet", params, new HttpResponse() {
            @Override
            public void onResponse(String data) {
                Toast.makeText(MainActivity.this,data, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(String msg) {
                Toast.makeText(MainActivity.this,msg, Toast.LENGTH_LONG).show();

            }
        });
    }
//post提交
    private void postJson(){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject object = new JSONObject();
        try {
            object.put("username", "aaa");
            object.put("password", "bbb");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //通过json方式提交数据
        RequestBody body = RequestBody.create(JSON, object.toString());

        final Request request = new Request.Builder()
                .url(baseUrl + "JsonHandler")
                .post(body)
                .build();

        //公共参数  header
        //.header("Connection","Keep-Alive") 头部数据可进行传参
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("===========", "onResponse: ========"+response.body().string());
            }
        });
        //call.cancel();//取消网络请求
    }
    private void post(){
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("username", "aaa")
                .add("password","bbb")
                .build();

        Request request = new Request.Builder()
                .url(baseUrl + "RegServlet")
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call,  IOException e) {
                //工作线程
                //打印错误日志
                Log.e("======", "onFailure: ======"+ e.getMessage());
                StackTraceElement[] stackTraceElements = e.getStackTrace();
                for (int i = 0; i < stackTraceElements.length; i++) {
                    Log.e("onFailure: ==========",stackTraceElements[i].toString() );
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                //工作线程
                Log.i("=====", "onResponse: ==========="+string);
//                TextView text = new TextView(MainActivity.this);
//                text.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //主线程
//                    }
//                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void get(){
        new Thread(){
            @Override
            public void run() {
                super.run();


                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(baseUrl + "RegServlet?username=aaa&password=bbb")
                        .build();

                //只能执行一次
                Call call = client.newCall(request);
                //同步
                try {
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        //返回的数据
                        ResponseBody body = response.body();
                        //byte数组  获取服务器返回数据
//                        body.bytes();
                        //inputstream 流 下载文件
//                        body.byteStream();
                        //字符串
                        String string = body.string();
                        Log.i( "run: ===========",string);

                    }else {
                        Log.i("run: ==========",response.message());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
