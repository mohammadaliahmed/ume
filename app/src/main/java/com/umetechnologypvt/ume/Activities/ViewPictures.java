package com.umetechnologypvt.ume.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.R;

import androidx.appcompat.app.AppCompatActivity;


public class ViewPictures extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pictures);
        Intent i=getIntent();
        String  url=i.getStringExtra("url");
        ImageView img=findViewById(R.id.img);
        Glide.with(this).load(url).into(img);
        img.setOnTouchListener(new ImageMatrixTouchHandler(this));
    }
}
