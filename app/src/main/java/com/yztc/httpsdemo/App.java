package com.yztc.httpsdemo;

import android.app.Application;

/**
 * Created by wanggang on 2016/11/2.
 */

public class App extends Application {


    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
    }

    public static App getInstance(){
        return app;
    }
}
