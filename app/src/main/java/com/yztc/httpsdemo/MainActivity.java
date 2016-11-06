package com.yztc.httpsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yztc.httpsdemo.http.BaseSubscriber;
import com.yztc.httpsdemo.http.ExceptionHandle;
import com.yztc.httpsdemo.http.HttpMethod;

import rx.Observable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Observable<ResponseBody> url = RetrofitHelper.getInstance().getApi().getUrl("http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0106/2275.html");
//        url.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ResponseBody>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody body) {
//
//                    }
//                });


        Observable register = HttpMethod.getInstance().register("a", "b");

        register.subscribe(new BaseSubscriber<String>(this) {
            @Override
            public void onNext(String s) {
                Log.i("======", s);
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                Log.i("======", e.getMessage());
            }

        });

        //取消操作
        //register.unsubscribeOn(Schedulers.io());
    }

}
