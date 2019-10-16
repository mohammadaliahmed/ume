package com.umetechnologypvt.ume.Camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.droidninja.imageeditengine.ImageEditActivity;
import com.droidninja.imageeditengine.ImageEditor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.umetechnologypvt.ume.Activities.Comments.CommentsModel;
import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.Adapters.MultiImagesPickedAdapter;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Stories.CustomViewPager;
import com.umetechnologypvt.ume.Stories.StoriesEditing.MultiStoriesPickedAdapter;
import com.umetechnologypvt.ume.Stories.StoriesEditing.PickedStoriesSliderAdapter;
import com.umetechnologypvt.ume.Stories.StoriesPickedModel;
import com.umetechnologypvt.ume.Stories.StoryRedirectActivity;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.CompressImage;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sotsys016-2 on 13/8/16 in com.cnc3camera.
 */
public class PhotoRedirectActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    StorageReference mStorageRef;
    ImageView delete, back, edit;
    CustomViewPager viewPager;
    MultiStoriesPickedAdapter adapter;
    RecyclerView recyclerview;
    ArrayList<StoriesPickedModel> itemList = new ArrayList<>();
    ArrayList<String> imagess = new ArrayList<>();
    PickedStoriesSliderAdapter sliderAdapter;
    private int posi;
    List<String> finalLivePicsArrayList = new ArrayList<>();

    int uploadPicCount = 0;

    ImageView addStory;
    private PostsModel finalPostModel;
    private String postType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_redirect);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("New post");


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }
        edit = findViewById(R.id.edit);
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);
        addStory = findViewById(R.id.addStory);
        viewPager = findViewById(R.id.viewPager);
        recyclerview = findViewById(R.id.recyclerview);
        itemList = SharedPrefs.getPickedList();
        if (itemList.size() > 1) {
            postType = "multi";
        } else {
            postType = "image";
        }


        addStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.showToast("Adding Post");
                if (itemList.size() > 1) {
                    for (StoriesPickedModel model : itemList) {
                        CompressImage compressImage = new CompressImage(PhotoRedirectActivity.this);
                        String abc = compressImage.compressImage(model.getUri());
                        imagess.add(abc);
                    }

                    sendMultiImages();
                } else {
                    CompressImage compressImage = new CompressImage(PhotoRedirectActivity.this);
                    String abc = compressImage.compressImage(itemList.get(0).getUri());
                    sendImage(abc);
                }
            }
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapter = new MultiStoriesPickedAdapter(this, itemList, new MultiStoriesPickedAdapter.AdapterCallbacks() {
            @Override
            public void onSelected(int position) {
                viewPager.setCurrentItem(position);
            }
        });
        recyclerview.setAdapter(adapter);
        sliderAdapter = new PickedStoriesSliderAdapter(this, itemList);
        viewPager.setAdapter(sliderAdapter);
        viewPager.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                posi = position;
                adapter.setPosition(position);
                if (itemList.get(position).getType().equalsIgnoreCase("video")) {
                    edit.setVisibility(View.GONE);
                } else {
                    edit.setVisibility(View.VISIBLE);
                }
                if (position == 2) {
                    recyclerview.scrollToPosition(0);

                } else if (position == 5) {
                    recyclerview.scrollToPosition(itemList.size() - 1);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.remove(posi);
                if (posi > 0) {
                    posi = posi - 1;
                }
                adapter.setPosition(posi);
                sliderAdapter.setPicturesList(itemList);
                viewPager.setAdapter(sliderAdapter);
                viewPager.setCurrentItem(posi);
                if (itemList.size() == 0) {
                    finish();
                }

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    new ImageEditor.Builder(PhotoRedirectActivity.this, CommonUtils.getRealPathFromURI(Uri.parse(itemList.get(posi).getUri())))
                            .setStickerAssets("stickers")
                            .open();
                } catch (Exception e) {
                    new ImageEditor.Builder(PhotoRedirectActivity.this, itemList.get(posi).getUri())
                            .setStickerAssets("stickers")
                            .open();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 52) {

            String imagePath = data.getStringExtra(ImageEditor.EXTRA_EDITED_PATH);
            StoriesPickedModel abc = itemList.get(posi);
            abc.setUri(imagePath);
            itemList.set(posi, abc);
            adapter.notifyDataSetChanged();
            sliderAdapter.setPicturesList(itemList);
            viewPager.setAdapter(sliderAdapter);
            viewPager.setCurrentItem(posi);
            sliderAdapter.notifyDataSetChanged();
        }
    }

    private void sendImage(String abc) {
//        wholeLayout.setVisibility(View.VISIBLE);
        String id = mDatabase.push().getKey();
        PostsModel model = new PostsModel(
                id,
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getName(),
                SharedPrefs.getUserModel().getThumbnailUrl(),
                "",
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getName(),
                SharedPrefs.getUserModel().getThumbnailUrl(),
                "",
                System.currentTimeMillis(),
                "image",
                1,
                0,
                SharedPrefs.getUserModel().getCountryNameCode(),
                SharedPrefs.getUserModel().getAge(),
                SharedPrefs.getUserModel().getGender()
        );
        mDatabase.child("Posts").child("Posts").child(id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                changeFlagOfAllPosts();
                putPictures(model, abc);
                if (ImageEditActivity.activity != null) {
                    ImageEditActivity.activity.finish();
                }
                if (WhatsappCameraActivity.activity != null) {
                    WhatsappCameraActivity.activity.finish();
                }

                Intent i=new Intent(PhotoRedirectActivity.this,MainActivity.class);
                startActivity(i);
                finish();
//                if (comment.getText().length() > 0) {
//                    String key = mDatabase.push().getKey();
//                    CommentsModel commentsModel = new CommentsModel(
//                            key, comment.getText().toString(),
//                            SharedPrefs.getUserModel().getUsername(),
//                            SharedPrefs.getUserModel().getName(),
//                            SharedPrefs.getUserModel().getThumbnailUrl(),
//                            System.currentTimeMillis(), SharedPrefs.getUserModel().getCountryNameCode()
//
//                    );
//                    mDatabase.child("Posts").child("Comments").child(model.getId()).child(key).setValue(commentsModel);
//                }
                mDatabase.child("PostsBy").child(SharedPrefs.getUserModel().getUsername()).child(id).setValue(id);
            }
        });
    }

    private void changeFlagOfAllPosts() {
        if (SharedPrefs.getPosts() != null) {
            for (PostsModel post : SharedPrefs.getPosts()) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("countryCode", SharedPrefs.getUserModel().getCountryNameCode());
                map.put("userPicUrl", SharedPrefs.getUserModel().getThumbnailUrl());
                map.put("userAge", SharedPrefs.getUserModel().getAge());
                map.put("gender", SharedPrefs.getUserModel().getGender());
                mDatabase.child("Posts").child("Posts").child(post.getId()).updateChildren(map);
            }
        }

    }

    private void sendMultiImages() {
//        wholeLayout.setVisibility(View.VISIBLE);
        String id = mDatabase.push().getKey();
        PostsModel model = new PostsModel(
                id,
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getName(),
                SharedPrefs.getUserModel().getThumbnailUrl(),
                "",
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getName(),
                SharedPrefs.getUserModel().getThumbnailUrl(),
                "",
                System.currentTimeMillis(),
                "multi",
                1,
                0,
                SharedPrefs.getUserModel().getCountryNameCode(), SharedPrefs.getUserModel().getAge(),
                SharedPrefs.getUserModel().getGender()
        );
        this.finalPostModel = model;
        mDatabase.child("Posts").child("Posts").child(id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Sharing Post");
                finish();
                putPictures(model, imagess.get(0));
//                if (comment.getText().length() > 0) {
//                    String key = mDatabase.push().getKey();
//                    CommentsModel commentsModel = new CommentsModel(
//                            key, comment.getText().toString(),
//                            SharedPrefs.getUserModel().getUsername(),
//                            SharedPrefs.getUserModel().getName(),
//                            SharedPrefs.getUserModel().getThumbnailUrl(),
//                            System.currentTimeMillis(), SharedPrefs.getUserModel().getCountryNameCode()
//                    );
//                    mDatabase.child("Posts").child("Comments").child(model.getId()).child(key).setValue(commentsModel);
//                }
                mDatabase.child("PostsBy").child(SharedPrefs.getUserModel().getUsername()).child(id).setValue(id);
            }
        });
    }

    public void putPictures(PostsModel model, String path) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));
        Uri file = Uri.fromFile(new File(path));
        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Posts").child("Posts").child(model.getId()).child("pictureUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (postType.equalsIgnoreCase("multi")) {
                                    finish();
                                    uploadMultiPictures(0);
                                }
//                                wholeLayout.setVisibility(View.GONE);
                                if (WhatsappCameraActivity.activity != null) {
                                    WhatsappCameraActivity.activity.finish();
                                }
//                                Intent i = new Intent(PhotoRedirectActivity.this, MainActivity.class);
//                                startActivity(i);
//                                finish();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(PhotoRedirectActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void uploadMultiPictures(int count) {
        putMultiPictures(finalPostModel, imagess.get(count));
    }

    private void putMultiPictures(PostsModel model, String path) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));


        Uri file = Uri.fromFile(new File(path));


        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        finalLivePicsArrayList.add("" + downloadUrl);
                        uploadPicCount++;
                        if (finalLivePicsArrayList.size() > 0 && finalLivePicsArrayList.size() == imagess.size()) {
                            mDatabase.child("Posts").child("Posts").child(model.getId()).child("multiImages").setValue(finalLivePicsArrayList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    CommonUtils.showToast("Done");
                                    finish();
                                }
                            });
                        } else {
//                            CommonUtils.showToast("uploading " + uploadPicCount);
                            uploadMultiPictures(uploadPicCount);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(PhotoRedirectActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }


    @Override
    public void onBackPressed() {
        if(ImageEditActivity.activity!=null){
            ImageEditActivity.activity.finish();
        }
        Intent i=new Intent(PhotoRedirectActivity.this,MainActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photoredirect_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }
        if (item.getItemId() == R.id.action_share) {
//            if (postType.equalsIgnoreCase("image")) {
//                sendImage();
//            }
//            if (postType.equalsIgnoreCase("Multi")) {
//                sendMultiImages();
//            }
        }

        return super.onOptionsItemSelected(item);
    }
}
