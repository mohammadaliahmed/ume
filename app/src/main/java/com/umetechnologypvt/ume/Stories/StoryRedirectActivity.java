package com.umetechnologypvt.ume.Stories;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.droidninja.imageeditengine.ImageEditor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;
import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Stories.StoriesEditing.MultiStoriesPickedAdapter;
import com.umetechnologypvt.ume.Stories.StoriesEditing.PickedStoriesSliderAdapter;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.CompressImage;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoryRedirectActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    CustomViewPager viewPager;
    ImageView addStory;
    RecyclerView recyclerview;

    MultiStoriesPickedAdapter adapter;
    //    List<String> itemList = new ArrayList<>();
    ArrayList<StoriesPickedModel> itemList = new ArrayList<>();
    ArrayList<StoriesPickedModel> finalList = new ArrayList<>();
    ArrayList<StoriesPickedModel> listToUpload = new ArrayList<>();


    PickedStoriesSliderAdapter sliderAdapter;
    int posi = 0;
    ImageView delete, back, edit;
    int count = 0;
    RelativeLayout wholeLayout;
    CircleImageView userPic;
    int countt = 0;
    int fnf = 0;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_redirect_activity);

        mDatabase = FirebaseDatabase.getInstance().getReference();
//        itemList = SharedPrefs.getMultiImgs();
        itemList = SharedPrefs.getPickedList();
        finalList = itemList;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }

        wholeLayout = findViewById(R.id.wholeLayout);
        edit = findViewById(R.id.edit);
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);
        viewPager = findViewById(R.id.viewPager);
        addStory = findViewById(R.id.addStory);
        userPic = findViewById(R.id.userPic);
        recyclerview = findViewById(R.id.recyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapter = new MultiStoriesPickedAdapter(this, itemList, new MultiStoriesPickedAdapter.AdapterCallbacks() {
            @Override
            public void onSelected(int position) {
                viewPager.setCurrentItem(position);
            }
        });
        recyclerview.setAdapter(adapter);
        Glide.with(this).load(SharedPrefs.getUserModel().getThumbnailUrl()).into(userPic);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                finalList = itemList;
                if (itemList.size() == 0) {
                    finish();
                }

            }
        });
//        mViewPagerAdapter.notifyDataSetChanged();


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    new ImageEditor.Builder(StoryRedirectActivity.this, CommonUtils.getRealPathFromURI(Uri.parse(itemList.get(posi).getUri())))
                            .setStickerAssets("stickers")
                            .open();
                } catch (Exception e) {

                } finally {
                    new ImageEditor.Builder(StoryRedirectActivity.this, itemList.get(posi).getUri())
                            .setStickerAssets("stickers")
                            .open();
                }

            }
        });

        addStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (String item : itemList) {
//                    wholeLayout.setVisibility(View.VISIBLE);
//                uploadStory(count);
                CommonUtils.showToast("Adding story");
                compressThings(Uri.parse(finalList.get(0).getUri()));
                StoriesCameraActivity.activity.finish();
                finish();
//                }
            }
        });

//        compressThings(Uri.parse(finalList.get(0).getUri()));


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 52) {

            String imagePath = data.getStringExtra(ImageEditor.EXTRA_EDITED_PATH);
            StoriesPickedModel abc = itemList.get(posi);
            abc.setUri(imagePath);
            itemList.set(posi, abc);
            CompressImage compressImage = new CompressImage(this);
            String aaa = compressImage.compressImage(imagePath);
            StoriesPickedModel ali = finalList.get(posi);
            ali.setUri(aaa);
            finalList.set(posi, ali);
            adapter.notifyDataSetChanged();
            sliderAdapter.setPicturesList(itemList);
            viewPager.setAdapter(sliderAdapter);
            viewPager.setCurrentItem(posi);
            sliderAdapter.notifyDataSetChanged();
            finalList = itemList;
        }
    }

    private void uploadStory(int poson) {

        if (finalList.get(poson).getType().equalsIgnoreCase("image")) {

            String key = mDatabase.push().getKey();
            StoryModel model = new StoryModel(
                    key,
                    SharedPrefs.getUserModel().getName(),
                    SharedPrefs.getUserModel().getUsername(),
                    SharedPrefs.getUserModel().getThumbnailUrl(),
                    "",
                    "",
                    "image/png",
                    System.currentTimeMillis(), SharedPrefs.getUserModel().getCountryNameCode()
            );
            mDatabase.child("Stories").child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    uploadPicture(model, finalList.get(poson).getUri());
                }
            });
        } else {
            String key = mDatabase.push().getKey();
            StoryModel model = new StoryModel(
                    key,
                    SharedPrefs.getUserModel().getName(),
                    SharedPrefs.getUserModel().getUsername(),
                    SharedPrefs.getUserModel().getThumbnailUrl(),
                    "",
                    "",
                    "video/mp4",
                    System.currentTimeMillis(), SharedPrefs.getUserModel().getCountryNameCode()
            );
            mDatabase.child("Stories").child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    uploadVideo(model, finalList.get(poson).getUri());
                }
            });
        }
    }

    private void uploadPicture(StoryModel model, String path) {
        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
                        mDatabase.child("Stories").child(model.getId()).child("imageUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                count++;
//                                CommonUtils.showToast("Uploaded" + count);

                                if (count < finalList.size()) {
                                    uploadStory(count);

                                } else {
                                    wholeLayout.setVisibility(View.GONE);
                                    CommonUtils.showToast("Story Added");
//                                    StoriesCameraActivity.activity.finish();
                                    finish();
                                }
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast("" + exception.getMessage());
                    }
                });

    }

    public void uploadVideo(StoryModel model, String path) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        ;
        Uri file = Uri.fromFile(new File(path));
        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();


        StorageReference riversRef = mStorageRef.child("Videos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Stories").child(model.getId()).child("videoUrl").setValue("" + downloadUrl)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        count++;
//                                        CommonUtils.showToast("Uploaded" + count);
                                        if (count < finalList.size()) {
                                            uploadStory(count);

                                        } else {
                                            wholeLayout.setVisibility(View.GONE);
                                            CommonUtils.showToast("Story Added");
//                                            StoriesCameraActivity.activity.finish();
                                            finish();

                                        }
                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(StoryRedirectActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void compressThings(Uri uri) {
        if (finalList.size() > 1) {
            fnf++;
            if (fnf == finalList.size()) {
                uploadStory(count);
            }
            if (countt < finalList.size()) {
                if (finalList.get(countt).getType().equalsIgnoreCase("video")) {
                    try {

                        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
                        if (f.mkdirs() || f.isDirectory())

                            new StoryRedirectActivity.VideoCompressAsyncTask(this, countt).execute("" + CommonUtils.getRealPathFromURI(uri), f.getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    if (countt < finalList.size()) {
                        CompressImage compressImage = new CompressImage(this);
                        String aaa = compressImage.compressImage(finalList.get(countt).getUri());
                        StoriesPickedModel ali = finalList.get(countt);
                        ali.setUri(aaa);
                        finalList.set(countt, ali);
                    }
                    countt++;
                    try {
                        compressThings(Uri.parse(finalList.get(countt).getUri()));

                    } catch (Exception e) {

                    }
                }
            } else {
                CommonUtils.showToast("Compressed");
            }
        } else {
            if (finalList.get(countt).getType().equalsIgnoreCase("video")) {
                try {

                    File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
                    if (f.mkdirs() || f.isDirectory())

                        new StoryRedirectActivity.VideoCompressAsyncTask(this, countt).execute("" + CommonUtils.getRealPathFromURI(uri), f.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                    File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
                    if (f.mkdirs() || f.isDirectory())

                        new StoryRedirectActivity.VideoCompressAsyncTask(this, countt).execute("" + uri, f.getPath());

                }
            } else {

                if (countt < finalList.size()) {
                    CompressImage compressImage = new CompressImage(this);
                    String aaa = compressImage.compressImage(finalList.get(countt).getUri());
                    StoriesPickedModel ali = finalList.get(countt);
                    ali.setUri(aaa);
                    finalList.set(countt, ali);
                    uploadStory(count);
                }

            }
        }

    }

    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        Context mContext;
        int posit;

        public VideoCompressAsyncTask(Context context, int posit) {
            mContext = context;
            this.posit = posit;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

            String finalVideoPath = compressedFilePath;
            StoriesPickedModel abc = finalList.get(posit);
            abc.setUri(finalVideoPath);
            finalList.set(posit, abc);
            countt++;
            if (finalList.size() == 1) {
                uploadStory(count);
            } else {
                if (countt < finalList.size()) {
                    compressThings(Uri.parse(finalList.get(countt).getUri()));
                } else {
//                CommonUtils.showToast("compressed videos"+countt);
                }
            }
            Log.i("Silicompressor", "Path: " + compressedFilePath);
        }
    }


}
