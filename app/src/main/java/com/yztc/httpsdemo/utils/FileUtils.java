package com.yztc.httpsdemo.utils;

import android.os.Environment;

import com.yztc.httpsdemo.App;

import java.io.File;

/**
 * Created by wanggang on 2016/11/2.
 */

public class FileUtils {

    public static File getRootCacheFile(){
        File root=null;
        if(Environment.isExternalStorageEmulated()){
            root= App.getInstance().getExternalCacheDir();
        }else {
            root=App.getInstance().getCacheDir();
        }
        return root;
    }

    public static File getHttpCacheFile(){
        return new File(getRootCacheFile(),"http");
    }

}
