package com.umetechnologypvt.ume;

import android.app.Application;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.os.StrictMode;

import com.google.firebase.database.FirebaseDatabase;
import com.umetechnologypvt.ume.Utils.SampleLifecycleListener;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by AliAh on 11/04/2018.
 */

public class ApplicationClass extends Application {
    private static ApplicationClass instance;
    DatabaseReference mDatabase;

    public static ApplicationClass getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new SampleLifecycleListener());
//        sendUserSatus(true);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }


}
