package com.umetechnologypvt.ume.Camera;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.googlecode.mp4parser.authoring.Edit;
import com.umetechnologypvt.ume.Activities.Comments.CommentsModel;
import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Stories.StoryModel;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;


public class TextStatusActivity extends AppCompatActivity {

    ImageView pallete, text;
    RelativeLayout wholeLayout;
    ImageView send;
    EditText status_text;
    int[] colorList = {R.color.darkPurple, R.color.darkGrey, R.color.darkSilver,
            R.color.black, R.color.red, R.color.darkGreen, R.color.darkYellow,
            R.color.darkBlue, R.color.darkPink};

    int random = 1;
    RelativeLayout toBitmap;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_status);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        toBitmap = findViewById(R.id.toBitmap);
        status_text = findViewById(R.id.status_text);
        pallete = findViewById(R.id.pallete);
        text = findViewById(R.id.text);
        wholeLayout = findViewById(R.id.wholeLayout);
        send = findViewById(R.id.send);
        Random r = new Random();

        int randomNumber = r.nextInt(colorList.length);
        wholeLayout.setBackgroundColor(getResources().getColor(colorList[randomNumber]));
        toBitmap.setBackgroundColor(getResources().getColor(colorList[randomNumber]));



        pallete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                int randomNumber = r.nextInt(colorList.length);
                wholeLayout.setBackgroundColor(getResources().getColor(colorList[randomNumber]));
                toBitmap.setBackgroundColor(getResources().getColor(colorList[randomNumber]));

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBitmap();
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                int randomNumber = r.nextInt(10);
                Typeface typeface = null;
                if (randomNumber == 1) {
                    typeface = ResourcesCompat.getFont(TextStatusActivity.this, R.font.aguafina_script);

                } else if (randomNumber == 2) {
                    typeface = ResourcesCompat.getFont(TextStatusActivity.this, R.font.atomic_age);

                } else if (randomNumber == 3) {
                    typeface = ResourcesCompat.getFont(TextStatusActivity.this, R.font.caveat);

                } else if (randomNumber == 4) {
                    typeface = ResourcesCompat.getFont(TextStatusActivity.this, R.font.alegreya);

                } else if (randomNumber == 5) {
                    typeface = ResourcesCompat.getFont(TextStatusActivity.this, R.font.alfa_slab_one);

                } else if (randomNumber == 6) {
                    typeface = ResourcesCompat.getFont(TextStatusActivity.this, R.font.crafty_girls);

                } else if (randomNumber == 7) {
                    typeface = ResourcesCompat.getFont(TextStatusActivity.this, R.font.denk_one);

                } else if (randomNumber == 8) {
                    typeface = ResourcesCompat.getFont(TextStatusActivity.this, R.font.graduate);

                } else if (randomNumber == 9) {
                    typeface = ResourcesCompat.getFont(TextStatusActivity.this, R.font.zeyada);

                } else if (randomNumber == 10) {
                    typeface = ResourcesCompat.getFont(TextStatusActivity.this, R.font.sacramento);

                }
                status_text.setTypeface(typeface);
            }
        });


    }

    private void createBitmap() {
        int height=toBitmap.getWidth();
        viewToBitmap(toBitmap, toBitmap.getWidth(), toBitmap.getHeight());
    }

    public Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
//        saveImage(bitmap, status_text.getText().toString().substring(0, 5));
        addPost(getImageUri(bitmap));
        return bitmap;
    }


    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void addPost(Uri uri) {
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
                "Image",
                1,
                0,
                SharedPrefs.getUserModel().getCountryNameCode(),
                SharedPrefs.getUserModel().getAge(),
                SharedPrefs.getUserModel().getGender()
        );
        mDatabase.child("Posts").child("Posts").child(id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Adding post");
                uploadPicture(model, CommonUtils.getRealPathFromURI(uri));
                mDatabase.child("PostsBy").child(SharedPrefs.getUserModel().getUsername()).child(id).setValue(id);
            }
        });
    }

    private void uploadPicture(PostsModel model, String path) {
        finish();
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
                        mDatabase.child("Posts").child("Posts").child(model.getId()).child("pictureUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Post Added");
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
                        CommonUtils.showToast("" + exception.getMessage());
                    }
                });

    }
}
