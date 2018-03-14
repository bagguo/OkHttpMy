package com.example.okhttpmy;

import android.os.Environment;

import java.io.File;

/**
 * Created by guodazhao on 2018/2/27 0027.
 */

public class FileUtils {
    public static File getRootCacheFile() {
        File root=null;
        if (Environment.isExternalStorageEmulated()) {
            //sd/android/data/packagename/cache
            root = App.getInstance().getExternalCacheDir();
        }else {
            //data/data/packagename/cache
            root = App.getInstance().getCacheDir();
        }
        return root;
    }
    public static File getHttpCacheFile(){
        return new File(getRootCacheFile(), "http");
    }
}
