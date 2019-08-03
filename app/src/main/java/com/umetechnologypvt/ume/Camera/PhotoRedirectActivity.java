package com.umetechnologypvt.ume.Camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.CompressImage;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sotsys016-2 on 13/8/16 in com.cnc3camera.
 */
public class PhotoRedirectActivity extends AppCompatActivity {

    ProgressBar progress;
    EditText comment;
    DatabaseReference mDatabase;
    StorageReference mStorageRef;
    private String postType;
    private String imagePath;
    RelativeLayout wholeLayout;
    String imgUrl;
    CircleImageView image;
    ImageView thumbnail;
    List<String> multiImagesList = new ArrayList<>();
    List<String> finalLivePicsArrayList = new ArrayList<>();
    RecyclerView recycler;
    MultiImagesPickedAdapter adapter;
    int uploadPicCount = 0;
    private PostsModel finalPostModel;

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
        progress = findViewById(R.id.progress);
        thumbnail = findViewById(R.id.thumbnail);
        image = findViewById(R.id.image);
        recycler = findViewById(R.id.recycler);
        Glide.with(this).load(SharedPrefs.getUserModel().getThumbnailUrl()).into(image);


        if (getIntent().getStringExtra("WHO").equalsIgnoreCase("Image")) {
            postType = "Image";
            imagePath = getIntent().getStringExtra("PATH");
            CompressImage image = new CompressImage(this);
            imgUrl = image.compressImage(imagePath);
            Glide.with(this).load(imagePath).into(thumbnail);
        }
        if (getIntent().getStringExtra("WHO").equalsIgnoreCase("Multi")) {
            postType = "Multi";
            multiImagesList = SharedPrefs.getMultiImgs();
            Glide.with(this).load(multiImagesList.get(0)).into(thumbnail);
            initMultiImages();
        }
        comment = findViewById(R.id.comment);
        wholeLayout = findViewById(R.id.wholeLayout);


    }

    private void initMultiImages() {
        adapter = new MultiImagesPickedAdapter(this, multiImagesList, new MultiImagesPickedAdapter.AdapterCallbacks() {
            @Override
            public void onDelete(String id, int position) {
                multiImagesList.remove(position);
                SharedPrefs.setMultiPickedImg(multiImagesList);

                adapter.notifyDataSetChanged();
            }
        });
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        recycler.setAdapter(adapter);
    }


    private void sendImage() {
        wholeLayout.setVisibility(View.VISIBLE);
        String id = mDatabase.push().getKey();
        PostsModel model = new PostsModel(
                id,
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getName(),
                SharedPrefs.getUserModel().getThumbnailUrl(),
                comment.getText().toString(),
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getName(),
                SharedPrefs.getUserModel().getThumbnailUrl(),
                "",
                System.currentTimeMillis(),
                postType,
                1,
                comment.getText().length() > 0 ? 1 : 0,
                SharedPrefs.getUserModel().getCountryNameCode()
        );
        mDatabase.child("Posts").child("Posts").child(id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                putPictures(model, imgUrl);
                if (comment.getText().length() > 0) {
                    String key = mDatabase.push().getKey();
                    CommentsModel commentsModel = new CommentsModel(
                            key, comment.getText().toString(),
                            SharedPrefs.getUserModel().getUsername(),
                            SharedPrefs.getUserModel().getName(),
                            SharedPrefs.getUserModel().getPicUrl(),
                            System.currentTimeMillis()

                    );
                    mDatabase.child("Posts").child("Comments").child(model.getId()).child(key).setValue(commentsModel);
                }
                mDatabase.child("PostsBy").child(SharedPrefs.getUserModel().getUsername()).child(id).setValue(id);
            }
        });
    }

    private void sendMultiImages() {
        wholeLayout.setVisibility(View.VISIBLE);
        String id = mDatabase.push().getKey();
        PostsModel model = new PostsModel(
                id,
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getName(),
                SharedPrefs.getUserModel().getThumbnailUrl(),
                comment.getText().toString(),
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getName(),
                SharedPrefs.getUserModel().getThumbnailUrl(),
                "",
                System.currentTimeMillis(),
                postType,
                1,
                comment.getText().length() > 0 ? 1 : 0,
                SharedPrefs.getUserModel().getCountryNameCode()
        );
        this.finalPostModel=model;
        mDatabase.child("Posts").child("Posts").child(id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                putPictures(model, multiImagesList.get(0));
                if (comment.getText().length() > 0) {
                    String key = mDatabase.push().getKey();
                    CommentsModel commentsModel = new CommentsModel(
                            key, comment.getText().toString(),
                            SharedPrefs.getUserModel().getUsername(),
                            SharedPrefs.getUserModel().getName(),
                            SharedPrefs.getUserModel().getPicUrl(),
                            System.currentTimeMillis()
                    );
                    mDatabase.child("Posts").child("Comments").child(model.getId()).child(key).setValue(commentsModel);
                }
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
                                    uploadMultiPictures(0);
                                }
                                wholeLayout.setVisibility(View.GONE);
                                WhatsappCameraActivity.activity.finish();
                                Intent i=new Intent(PhotoRedirectActivity.this,MainActivity.class);
                                startActivity(i);
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
        putMultiPictures(finalPostModel, multiImagesList.get(count));
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
                        if (finalLivePicsArrayList.size() > 0 && finalLivePicsArrayList.size() == multiImagesList.size()) {
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
            if (postType.equalsIgnoreCase("image")) {
                sendImage();
            }
            if (postType.equalsIgnoreCase("Multi")) {
                sendMultiImages();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
