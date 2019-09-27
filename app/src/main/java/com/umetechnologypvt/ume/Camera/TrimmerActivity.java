package com.umetechnologypvt.ume.Camera;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.Activities.Profile;
import com.umetechnologypvt.ume.Activities.UserManagement.Login;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.PrefManager;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import androidx.appcompat.app.AppCompatActivity;
//import life.knowledge4.videotrimmer.K4LVideoTrimmer;
//import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;


public class TrimmerActivity extends AppCompatActivity {

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimmer);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
//        K4LVideoTrimmer videoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));

        path = getIntent().getStringExtra("path");
//        mVideoTrimmerView?.setVideoPath(videoPath!!)?.handle()

//        if (videoTrimmer != null)
//
//        {
//            videoTrimmer.setVideoURI(Uri.parse(path));
//            videoTrimmer.setMaxDuration(100);
//            videoTrimmer.setOnTrimVideoListener(new OnTrimVideoListener() {
//                @Override
//                public void getResult(Uri uri) {
//                    CommonUtils.showToast(""+uri);
//                }
//
//                @Override
//                public void cancelAction() {
//
//                }
//            });
//
//        }

    }


//    @Override
//    public void getResult(Uri uri) {
//        CommonUtils.showToast("" + uri);
//    }
//
//    @Override
//    public void cancelAction() {
//
//    }
}
