package com.yztc.httpsdemo.base;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by wanggang on 2016/11/6.
 */

public class BaseFragment extends Fragment {

    protected Activity mActivity;

    public BaseFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //getActivity()空指针
        this.mActivity = (Activity)context;
    }



}
