package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.droidninja.imageeditengine.ImageEditor;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.Country;
import com.umetechnologypvt.ume.ApplicationClass;
import com.umetechnologypvt.ume.Camera.CameraActivity;
import com.umetechnologypvt.ume.Camera.PhotoRedirectActivity;
import com.umetechnologypvt.ume.Camera.TextStatusActivity;
import com.umetechnologypvt.ume.Camera.VideoRedirectActivity;
import com.umetechnologypvt.ume.Camera.WhatsappCameraActivity;
import com.umetechnologypvt.ume.Models.ChatListModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Stories.StoriesCameraActivity;
import com.umetechnologypvt.ume.Stories.StoriesPickedModel;
import com.umetechnologypvt.ume.Stories.StoryModel;
import com.umetechnologypvt.ume.Stories.StoryViewsModel;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.CompressImage;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.GifSizeFilter;
import com.umetechnologypvt.ume.Utils.NotificationObserver;
import com.umetechnologypvt.ume.Utils.SharedPrefs;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NotificationObserver {
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 99;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;
    public static boolean active;
    private Fragment fragment;
    int value;
    private String postIdFromLink;
    DatabaseReference mDatabase;
    public static MainActivity activity;
    private TextView text;
    public static ArrayList<ArrayList<StoryModel>> arrayLists = new ArrayList<>();
    public static ArrayList<StoryModel> myArrayLists = new ArrayList<>();
    public static ArrayList<StoryModel> toDelete = new ArrayList<>();
    HashMap<String, ArrayList<StoryModel>> map = new HashMap<String, ArrayList<StoryModel>>();
    HashMap<String, ArrayList<StoryModel>> toDeletemap = new HashMap<String, ArrayList<StoryModel>>();
    private static final int REQUEST_CODE_CHOOSE = 23;
    public static HashMap<String, ArrayList<StoryViewsModel>> seenMap = new HashMap<>();
    public static HashMap<String, Boolean> checkSeenList = new HashMap<>();


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            if (!SharedPrefs.getChatCount().equalsIgnoreCase("0")) {
                text.setVisibility(View.VISIBLE);

                text.setText(SharedPrefs.getChatCount());
            } else {
                text.setVisibility(View.GONE);
            }

        }
    };

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

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        if (!SharedPrefs.getChatCount().equalsIgnoreCase("0") && !SharedPrefs.getChatCount().equalsIgnoreCase("")) {
            text.setVisibility(View.VISIBLE);

            text.setText(SharedPrefs.getChatCount());
        } else {
            text.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mMessageReceiver,
                new IntentFilter("chatCount"));
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


        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.layout_news_badge, itemView, true);
        text = badge.findViewById(R.id.badge_text_view);
        getStoriesFromDB();
        getViewsFromDB();
    }

    private void getViewsFromDB() {
        mDatabase.child("StoryViews").child(SharedPrefs.getUserModel().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    map.clear();
                    for (DataSnapshot storyIds : dataSnapshot.getChildren()) {

                        ArrayList<StoryViewsModel> storiesList = new ArrayList<>();
                        for (DataSnapshot users : storyIds.getChildren()) {
                            StoryViewsModel viewsModel = users.getValue(StoryViewsModel.class);
                            if (viewsModel != null) {
                                storiesList.add(viewsModel);
                            }

                        }
                        seenMap.put(storyIds.getKey(), storiesList);
//                        viewsList = map.get(mStoriesList.get(counter).getId());
//                        adapter.notifyDataSetChanged();
//                        views.setText((viewsList == null ? 0 : viewsList.size()) + " views");
//                        viewCount.setText((viewsList == null ? 0 : viewsList.size()) + " views");
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getStoriesFromDB() {
        mDatabase.child("Stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        StoryModel model = snapshot.getValue(StoryModel.class);
                        if (model != null) {

                            if (map.containsKey(model.getStoryByUsername())) {
                                if (System.currentTimeMillis() - model.getTime() < 86400000) {
                                    ArrayList<StoryModel> list = map.get(model.getStoryByUsername());
                                    list.add(model);
                                    map.put(model.getStoryByUsername(), list);
                                } else {
                                    ArrayList<StoryModel> list = toDeletemap.get(model.getStoryByUsername());
                                    list.add(model);
                                    toDeletemap.put(model.getStoryByUsername(), list);
                                }
                            } else {
                                if (System.currentTimeMillis() - model.getTime() < 86400000) {
                                    ArrayList<StoryModel> mStoriesList2 = new ArrayList<>();
                                    mStoriesList2.add(model);
                                    map.put(model.getStoryByUsername(), mStoriesList2);
                                } else {
                                    ArrayList<StoryModel> mStoriesList2 = new ArrayList<>();
                                    mStoriesList2.add(model);
                                    toDeletemap.put(model.getStoryByUsername(), mStoriesList2);

                                }

                            }
                        }
                    }
                    if (arrayLists != null) {
                        arrayLists.clear();
                    }
                    if (myArrayLists != null) {
                        myArrayLists.clear();
                    }
                    if (map.size() > 0) {

                        myArrayLists = map.get(SharedPrefs.getUserModel().getUsername());

                    } else {
                        myArrayLists = new ArrayList<>();
                    }
                    if (map.size() > 0) {
                        map.remove(SharedPrefs.getUserModel().getUsername());
                        arrayLists.clear();
                        for (Map.Entry<String, ArrayList<StoryModel>> entry : map.entrySet()) {
                            if (SharedPrefs.getUserModel().getConfirmFriends().contains(entry.getValue().get(0).getStoryByUsername())) {
                                arrayLists.add(entry.getValue());
                                Collections.sort(arrayLists, new Comparator<ArrayList<StoryModel>>() {
                                    @Override
                                    public int compare(ArrayList<StoryModel> o1, ArrayList<StoryModel> o2) {
                                        Long ob1 = o1.get(o1.size() - 1).getTime();
                                        Long ob2 = o2.get(o2.size() - 1).getTime();
                                        return ob2.compareTo(ob1);
                                    }


                                });


                                SharedPrefs.setHomeStories(arrayLists);
                            }
                        }
                    } else {
                        arrayLists = new ArrayList<>();

                        SharedPrefs.setHomeStories(arrayLists);

                    }
                    sendMsgToFragmentToUpdateList();
                    if (toDeletemap.size() > 0) {
                        toDelete = toDeletemap.get(SharedPrefs.getUserModel().getUsername());
                        if (toDelete != null && toDelete.size() > 0) {
                            for (StoryModel storyModel : toDelete) {
                                mDatabase.child("Stories").child(storyModel.getId()).removeValue();

                            }
                        }

                    }

//                    arrayLists.add(mStoriesList);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendMsgToFragmentToUpdateList() {

        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("updateSeenList");
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        active = false;
        Intent intent = new Intent("pause");
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        active = false;
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(mMessageReceiver);

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
                    showBottomSheet();
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

    private void showBottomSheet() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.post_options_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setContentView(customView);
        ImageView cancel = customView.findViewById(R.id.cancel);
        LinearLayout camera = customView.findViewById(R.id.camera);
        LinearLayout upload = customView.findViewById(R.id.upload);
        LinearLayout text = customView.findViewById(R.id.text);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, TextStatusActivity.class));

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                startActivity(new Intent(MainActivity.this, CameraActivity.class));

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

//                initMatisse();
                showUploadDialog();

            }
        });
//        startActivity(new Intent(MainActivity.this, CameraActivity.class));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showUploadDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.alert_dialog_upload, null);

        dialog.setContentView(layout);

        RelativeLayout video = layout.findViewById(R.id.video);
        RelativeLayout picture = layout.findViewById(R.id.picture);


        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO);
            }
        });


        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                initMatisse();
            }
        });


        dialog.show();

    }

    private void initMatisse() {
        Matisse.from(MainActivity.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(10)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

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


        if (requestCode == 23) {

            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        if (requestCode == ImageEditor.RC_IMAGE_EDITOR) {

            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // You don't have permission
                checkPermission();
            } else {
                // Do as per your logic
            }
        }
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);

            mSelected = Matisse.obtainResult(data);
            ArrayList<StoriesPickedModel> list = new ArrayList<>();

            for (Uri uri : mSelected) {

                StoriesPickedModel model = new StoriesPickedModel(
                        "" + uri, "" + uri, "", "image", System.currentTimeMillis()
                );
                list.add(model);


            }
            if (list.size() > 0) {
                SharedPrefs.setPickedList(list);
                startActivity(new Intent(this, PhotoRedirectActivity.class));

            }

        } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
            if (data != null) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                Intent mIntent = new Intent(MainActivity.this, VideoRedirectActivity.class);
                mIntent.putExtra("PATH", CommonUtils.getRealPathFromURI(selectedImageUri));
                mIntent.putExtra("THUMB", CommonUtils.getRealPathFromURI(selectedImageUri));
                mIntent.putExtra("WHO", "GalleryVideo");
                startActivity(mIntent);

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
