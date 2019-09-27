package com.umetechnologypvt.ume.Camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.droidninja.imageeditengine.ImageEditor;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Stories.StoriesCameraActivity;
import com.umetechnologypvt.ume.Utils.CommonUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AddStoryActivity extends AppCompatActivity {
    private RunTimePermission runTimePermission;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        runTimePermission = new RunTimePermission(this);
        runTimePermission.requestPermission(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, new RunTimePermission.RunTimePermissionListener() {

            @Override
            public void permissionGranted() {
                // First we need to check availability of play services
                startActivity(new Intent(AddStoryActivity.this, StoriesCameraActivity.class));
                finish();

            }

            @Override
            public void permissionDenied() {

                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK && data != null) {
            String imagePath = data.getStringExtra(ImageEditor.EXTRA_EDITED_PATH);
            CommonUtils.showToast(imagePath);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (runTimePermission != null) {
            runTimePermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


}
