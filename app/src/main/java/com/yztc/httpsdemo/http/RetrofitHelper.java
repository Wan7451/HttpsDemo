package com.yztc.httpsdemo.http;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.yztc.httpsdemo.App;
import com.yztc.httpsdemo.utils.FileUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wanggang on 2016/11/2.
 *
 * 初始化 Retrofit  Okhttp
 *
 */
public class RetrofitHelper {

    public static final String BASE_URL="https://10.0.158.192:8443/MyWeb/";

    public static final String TAG ="======";

    private static RetrofitHelper instance;

    private Retrofit retrofit;
    private IApi api;

    public static RetrofitHelper getInstance() {
        if (instance == null) {
            instance = new RetrofitHelper();
        }
        return instance;
    }

    private RetrofitHelper() {
         retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client())
                 //.serializeNulls  解决 gson  null值不进行转换问题
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    private OkHttpClient client() {

        //缓存路径
        File file = FileUtils.getHttpCacheFile();
        Cache cache=new Cache(file,20*1024*1024);


        //打印网络日志
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i(TAG,  message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //公共参数
        BasicParamsInterceptor basicParamsInterceptor=new BasicParamsInterceptor.Builder()
                .addQueryParam("basicparama1","aaa")
                .addQueryParam("basicparama2","bbb")
                .addQueryParam("basicparama3","ccc")
                .addQueryParam("basicparama4","ddd")
                .addHeaderParam("Cache-Control","public,max-age=3600") //指定请求和响应遵循的缓存机制
                .addHeaderParam("Connection","Keep-Alive") //表示是否需要持久连接。
                .addHeaderParam("Accept-Encoding","gzip")//指定可以支持的web服务器返回内容压缩编码类型。
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                //SSLSocket
                .sslSocketFactory(
                        SSLHelper.getSSLSocketFactory(App.getInstance()),
                        SSLHelper.getTrustManager())
                //hostname 验证
                .hostnameVerifier(SSLHelper.getHostnameVerifier())
                //baseparams 拦截器
                .addInterceptor(basicParamsInterceptor)
                //本地缓存拦截器
                .addInterceptor(new RewriteCacheControlInterceptor())
                .addNetworkInterceptor(new RewriteCacheControlInterceptor())
                //打印Log 拦截器
                .addInterceptor(httpLoggingInterceptor)
                .cache(cache)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();

        return client;
    }


    public IApi getApi(){
        if(api==null)
         api = retrofit.create(IApi.class);
        return api;
    }

}