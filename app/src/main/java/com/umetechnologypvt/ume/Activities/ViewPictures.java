package com.umetechnologypvt.ume.Activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.umetechnologypvt.ume.R;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;


public class ViewPictures extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pictures);
        Intent i=getIntent();
        String  url=i.getStringExtra("url");
        ImageView img=findViewById(R.id.img);

        Glide.with(this).load(url).placeholder(R.drawable.placeholder).into(img);
        img.setOnTouchListener(new ImageMatrixTouchHandler(this));
    }
}
