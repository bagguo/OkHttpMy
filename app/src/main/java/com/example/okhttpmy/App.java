package com.example.okhttpmy;

import android.app.Application;
import android.content.Context;

/**
 * Created by guodazhao on 2018/2/27 0027.
 */

public class App extends Application {
    private static Context instance;

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
