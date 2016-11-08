package com.yztc.httpsdemo.http;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 网络请求封装
 * Created by wanggang on 2016/11/4.
 */

public class HttpMethod {

    private  IApi api;

    private static HttpMethod instance;

    public static HttpMethod getInstance() {
        if (instance == null) {
            instance = new HttpMethod();
        }
        return instance;
    }

    private HttpMethod() {
        if(api==null)
         api = RetrofitHelper.getInstance().getApi();
    }

    public Observable register(String username, String passwprd){
       return  api.register(username, passwprd)
                .delay(1, TimeUnit.SECONDS)//延迟1S执行网络操作
                .compose(schedulersTransformer())//线程调度
                .compose(transformer());//处理 baseResponse
    }

    public Observable registerGet(String username, String passwprd){
        return  api.registerGet(username, passwprd)
                .delay(1, TimeUnit.SECONDS)
                .compose(schedulersTransformer())
                .compose(transformer());
    }



    private <T> Observable.Transformer<BaseResponse<T>, T> transformer() {

        return new Observable.Transformer() {

            @Override
            public Object call(Object observable) {
                return ((Observable) observable)
                        .map(new HandleFuc<T>())//数据类型转换，取出真正的业务数据
                        .onErrorResumeNext(new HttpResponseFunc<T>());//处理异常
            }
        };
    }

    private static class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override public Observable<T> call(Throwable t) {
            //发错错误的 Observable
            return Observable.error(ExceptionHandle.handleException(t));
        }
    }

    private static class HandleFuc<T> implements Func1<BaseResponse<T>, T> {
        @Override
        public T call(BaseResponse<T> response) {
            //当前业务操作是否成功
            if (!response.isOk())
                throw new RuntimeException(response.getStatus() + "" + response.getMsg() != null ? response.getMsg(): "");
            return response.getData();
        }
    }


   private Observable.Transformer schedulersTransformer() {
        return new Observable.Transformer() {

            @Override
            public Object call(Object observable) {
                return ((Observable)  observable)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
