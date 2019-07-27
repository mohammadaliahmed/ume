package com.umetechnologypvt.ume.Camera;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.umetechnologypvt.ume.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class CameraActivity extends AppCompatActivity {

   private RunTimePermission runTimePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_activity);

        runTimePermission = new RunTimePermission(this);
        runTimePermission.requestPermission(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, new RunTimePermission.RunTimePermissionListener() {

            @Override
            public void permissionGranted() {
                // First we need to check availability of play services
                startActivity(new Intent(CameraActivity.this,WhatsappCameraActivity.class));
                finish();

            }

            @Override
            public void permissionDenied() {

                finish();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(runTimePermission!=null){
            runTimePermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
