package com.yztc.httpsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yztc.httpsdemo.http.BaseSubscriber;
import com.yztc.httpsdemo.http.ExceptionHandle;
import com.yztc.httpsdemo.http.HttpMethod;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    private CompositeSubscription mCompositeSubscription
            = new CompositeSubscription();

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


        Subscription subscribe = register.subscribe(new BaseSubscriber<String>(this) {
            @Override
            public void onNext(String s) {
                Log.i("======", s);
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                Log.i("======", e.getMessage());
            }

        });
        mCompositeSubscription.add(subscribe);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //为了防止可能的内存泄露，在你的 Activity 或 Fragment 的
        //onDestroy 里，用 Subscription.isUnsubscribed()
        // 检查你的 Subscription 是否是 unsubscribed。
        // 如果调用了 Subscription.unsubscribe() ，
        // Unsubscribing将会对 items 停止通知给你的 Subscriber，
        // 并允许垃圾回收机制释放对象，防止任何 RxJava 造成内存泄露。
        // 如果你正在处理多个 Observables 和 Subscribers，
        // 所有的 Subscription 对象可以添加到 CompositeSubscription，
        // 然后可以使用 CompositeSubscription.unsubscribe()
        // 方法在同一时间进行退订(unsubscribed)。
        mCompositeSubscription.unsubscribe();
    }
}
