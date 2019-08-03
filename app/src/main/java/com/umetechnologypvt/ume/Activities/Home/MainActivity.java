package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umetechnologypvt.ume.Camera.CameraActivity;
import com.umetechnologypvt.ume.Interface.NotificationInterface;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.NotificationObserver;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

public class MainActivity extends AppCompatActivity implements NotificationObserver {
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;
    public static boolean active;
    private Fragment fragment;
    int value;
    private String postIdFromLink;
    DatabaseReference mDatabase;
    public static MainActivity activity;

    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            postIdFromLink = data.substring(data.lastIndexOf("/") + 1);
            getuserDataFromDB(postIdFromLink);
        }
    }

    private void getuserDataFromDB(String postIdFromLink) {
        if (postIdFromLink.equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
            fragment = new MyProfileFragment();
        } else {
            Constants.USER_ID = postIdFromLink;
            fragment = new UserProfileFragment();


        }
        loadFragment(fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar toolbar = getSupportActionBar();

        if (postIdFromLink != null) {

        } else {
//        fragment = new HomeFragment();
            fragment = new NewHomeFragment();

            loadFragment(fragment);
        }
        value = getIntent().getIntExtra("value", 0);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        int requestCode = 0;
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // You don't have permission
                checkPermission();
            } else {
                // Do as per your logic
            }
        }
        if (value == 1) {
            fragment = new MyProfileFragment();
            loadFragment(fragment);
        } else if (value == 2) {
            fragment = new UserProfileFragment();
            loadFragment(fragment);
        } else if (value == 3) {
            Fragment fragment = new PostLikesFragment();
            loadFragment(fragment);
        }
        onNewIntent(getIntent());

    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        active = false;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        active = false;
    }

    public void checkPermission() {
        System.out.println("CHECK PERMISSIONS:");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    toolbar.setTitle("Shop");
                    fragment = new NewHomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_chats:
                    fragment = new ChatListFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_post:
//                    toolbar.setTitle("Cart");
                    startActivity(new Intent(MainActivity.this, CameraActivity.class));
                    return true;
                case R.id.navigation_search:
                    fragment = new FiltersFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = new MyProfileFragment();
                    loadFragment(fragment);
//                    startActivity(new Intent(MainActivity.this,EditProfile.class));

                    return true;

            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // You don't have permission
                checkPermission();
            } else {
                // Do as per your logic
            }
        }
    }

    @Override
    public void onSuccess(String chatId) {

    }

    @Override
    public void onFailure() {

    }


}
