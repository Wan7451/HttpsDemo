package com.yztc.httpsdemo.http;

import android.content.Context;

import com.yztc.httpsdemo.utils.DialogHelper;
import com.yztc.httpsdemo.utils.NetUtils;

import rx.Subscriber;

/**
 * 处理异常的Subscriber
 * Created by wanggang on 2016/11/4.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    private Context context;

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ExceptionHandle.ResponeThrowable) {
            onError((ExceptionHandle.ResponeThrowable) e);
        } else {
            onError(new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        if (!NetUtils.isConnected()) {
            onError(
                    new ExceptionHandle.ResponeThrowable(
                            new Throwable("网络没有连接,请打开网路"),
                            ExceptionHandle.ERROR.NETWORD_ERROR));
        } else {
            DialogHelper.showProgressDlg(context, "网络加载中....");
        }
    }

    @Override
    public void onCompleted() {
        DialogHelper.stopProgressDlg();
    }

    //出现错误直接调用 自定义的 onError
    public abstract void onError(ExceptionHandle.ResponeThrowable e);

}