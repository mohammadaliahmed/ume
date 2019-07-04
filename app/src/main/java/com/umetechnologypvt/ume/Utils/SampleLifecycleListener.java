package com.umetechnologypvt.ume.Utils;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

public class SampleLifecycleListener implements LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        CommonUtils.sendCustomerStatus("Online");

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onDestroy() {

        CommonUtils.sendCustomerStatus("" + System.currentTimeMillis());

    }
}