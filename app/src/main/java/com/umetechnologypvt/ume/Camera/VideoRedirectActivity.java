package com.umetechnologypvt.ume.Camera;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;
import com.umetechnologypvt.ume.Activities.Comments.CommentsModel;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.CompressImageToThumbnail;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sotsys016-2 on 13/8/16 in com.cnc3camera.
 */
public class VideoRedirectActivity extends AppCompatActivity {

    ProgressBar progress;
    EditText comment;
//    ImageView send;

    DatabaseReference mDatabase;
    StorageReference mStorageRef;
    private String postType;
    private String imagePath;
    RelativeLayout wholeLayout;
    String imgUrl;
    private Uri videoPath;
    //    LinearLayout compressionMsg;
    private String finalVideoPath;
    RelativeLayout videoLayout;
    private boolean videoCompressed = false;
    private Uri videoThumbPath;
    CircleImageView image;
    ImageView thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_redirect);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("New post");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        progress = findViewById(R.id.progress);
        image = findViewById(R.id.image);
        videoLayout = findViewById(R.id.videoLayout);
        thumbnail = findViewById(R.id.thumbnail);
        Glide.with(this).load(SharedPrefs.getUserModel().getThumbnailUrl()).into(image);
        if (getIntent().getStringExtra("WHO").equalsIgnoreCase("Video")) {
            videoLayout.setVisibility(View.VISIBLE);
            videoPath = Uri.parse(getIntent().getStringExtra("PATH"));
            videoThumbPath = Uri.parse(getIntent().getStringExtra("THUMB"));
            postType = "Video";
            initVideo();
            Glide.with(this).load(videoThumbPath.getPath()).into(thumbnail);

        } else if (getIntent().getStringExtra("WHO").equalsIgnoreCase("GalleryVideo")) {
            videoLayout.setVisibility(View.VISIBLE);
            videoPath = Uri.parse(getIntent().getStringExtra("PATH"));
            videoThumbPath = Uri.parse(getIntent().getStringExtra("THUMB"));
            postType = "Video";
            initVideo();
            Glide.with(this).load(videoThumbPath.getPath()).into(thumbnail);

        }

        comment = findViewById(R.id.comment);
        wholeLayout = findViewById(R.id.wholeLayout);


    }

    private void sendVideo() {
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
                "",
                System.currentTimeMillis(),
                postType,
                1,
                comment.getText().length() > 0 ? 1 : 0, 0,
                SharedPrefs.getUserModel().getCountryNameCode(), SharedPrefs.getUserModel().getAge(),
                SharedPrefs.getUserModel().getGender()
        );
        mDatabase.child("Posts").child("Posts").child(id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                putVideo(model, finalVideoPath);
                if (comment.getText().length() > 0) {
                    String key = mDatabase.push().getKey();
                    CommentsModel commentsModel = new CommentsModel(
                            key, comment.getText().toString(),
                            SharedPrefs.getUserModel().getUsername(),
                            SharedPrefs.getUserModel().getName(),
                            SharedPrefs.getUserModel().getThumbnailUrl(),
                            System.currentTimeMillis(), SharedPrefs.getUserModel().getCountryNameCode()
                    );
                    mDatabase.child("Posts").child("Comments").child(model.getId()).child(key).setValue(commentsModel);
                }
                mDatabase.child("PostsBy").child(SharedPrefs.getUserModel().getUsername()).child(id).setValue(id);
            }
        });
    }


    private void initVideo() {
        try {

            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
            if (f.mkdirs() || f.isDirectory())

                new VideoCompressAsyncTask(this).execute("" + videoPath, f.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void putVideo(PostsModel model, String path) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        ;
        Uri file = Uri.fromFile(new File(path));


        StorageReference riversRef = mStorageRef.child("Videos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Posts").child("Posts").child(model.getId()).child("videoUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                wholeLayout.setVisibility(View.GONE);
                                WhatsappCameraActivity.activity.finish();
                                CompressImageToThumbnail compressImage = new CompressImageToThumbnail(VideoRedirectActivity.this);
                                putVideoPicture(compressImage.compressImage("" + CommonUtils.getVideoPic("" + downloadUrl)), model);

                                finish();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(VideoRedirectActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void putVideoPicture(String path, PostsModel model) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

//        final Uri file = path;
        Uri file = Uri.fromFile(new File(path));

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        mDatabase.child("Posts").child("Posts").child(model.getId())
                                .child("videoThumbnailUrl").setValue("" + downloadUrl)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast(exception.getMessage() + "");

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
                                wholeLayout.setVisibility(View.GONE);
                                WhatsappCameraActivity.activity.finish();
                                finish();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(VideoRedirectActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void onBackPressed() {
//        if (videoView.isPlaying()) {
//            videoView.pause();
//        }
        super.onBackPressed();
    }

    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        Context mContext;

        public VideoCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
            try {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(paths[0]);
                String width = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                String height = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1],
                        Integer.parseInt(width), Integer.parseInt(height), 450000);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return filePath;

        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            File imageFile = new File(compressedFilePath);
            float length = imageFile.length() / 1024f; // Size in KB
            String value;
            if (length >= 1024)
                value = length / 1024f + " MB";
            else
                value = length + " KB";
            String text = String.format(Locale.US, "%s\nName: %s\nSize: %s", getString(R.string.video_compression_complete), imageFile.getName(), value);

            finalVideoPath = compressedFilePath;
            progress.setVisibility(View.GONE);
            videoCompressed = true;
            Log.i("Silicompressor", "Path: " + compressedFilePath);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.videredirect_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }
        if (item.getItemId() ==R.id.action_trim) {

//        Intent i=new Intent(VideoRedirectActivity.this,TrimmerActivity.class);
//        i.putExtra("path",""+videoPath);
//        startActivity(i);


        }
        if (item.getItemId() == R.id.action_share) {
            if (postType.equalsIgnoreCase("video")) {
                if (videoCompressed) {
                    sendVideo();
                } else {
                    CommonUtils.showToast("Preparing video..");
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
