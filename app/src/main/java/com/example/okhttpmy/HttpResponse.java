package com.example.okhttpmy;

/**
 * Created by guodazhao on 2018/2/28 0028.
 */

public interface HttpResponse {

    void onResponse(String data);

    void onError(String msg);
}
