package com.appsinventiv.ume.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.ume.Models.UserModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;
import com.appsinventiv.ume.Utils.CompressImage;
import com.appsinventiv.ume.Utils.GifSizeFilter;
import com.appsinventiv.ume.Utils.SharedPrefs;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    DatabaseReference mDatabase;
    CircleImageView image;
    EditText name;
    Button continuebtn;
    ArrayList<String> imageUrl = new ArrayList<>();
    List<Uri> mSelected = new ArrayList<>();
    private static final int REQUEST_CODE_CHOOSE = 23;
    RadioGroup radioGender;
    RadioButton radioGenderButton;
    RadioGroup radioInterest;
    RadioButton radioInterestButton;
    TextView chooseCountry, chooseLanguage, chooseBirthday;
    public static String language = "English", country = "Pakistan";
    String gender, interest;
    StorageReference mStorageRef;
    ProgressBar progress;
    private String dob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.setTitle("Setup profile");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        getPermissions();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        continuebtn = findViewById(R.id.continuebtn);
        chooseBirthday = findViewById(R.id.chooseBirthday);
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        radioGender = findViewById(R.id.radioGender);
        radioInterest = findViewById(R.id.radioInterest);
        chooseCountry = findViewById(R.id.chooseCountry);
        chooseLanguage = findViewById(R.id.chooseLanguage);
        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        getUserDataFromDB();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMatisse();
            }
        });

        chooseLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageAlert();
            }
        });
        chooseCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountryAlert();
            }
        });

        chooseBirthday.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog dlg = new DatePickerDialog(Profile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dob = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        chooseBirthday.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));

                    }
                }, 1990, 1, 1);
                dlg.show();

            }
        });


        continuebtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                if (name.getText().length() == 0) {
                    name.setError("Enter name");
                } else if (language == null) {
                    CommonUtils.showToast("Choose language");
                } else if (country == null) {
                    CommonUtils.showToast("Choose country");
                }else if (dob == null) {
                    CommonUtils.showToast("Choose date of birth");
                } else {

                    int selectedId = radioGender.getCheckedRadioButtonId();
                    radioGenderButton = (RadioButton) findViewById(selectedId);
                    if (radioGenderButton == null) {
                        CommonUtils.showToast("Choose gender");
                        return;
                    } else {
                        gender = radioGenderButton.getText().toString();
                    }
                    int interestId = radioInterest.getCheckedRadioButtonId();
                    radioInterestButton = (RadioButton) findViewById(interestId);
                    if (radioInterestButton == null) {
                        CommonUtils.showToast("Choose Interest");
                        return;
                    } else {
                        interest = radioInterestButton.getText().toString();

                    }
                    takeUserToNextScreen();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            progress.setVisibility(View.VISIBLE);
            for (Uri img :
                    mSelected) {
                CompressImage compressImage = new CompressImage(Profile.this);
                imageUrl.add(compressImage.compressImage("" + img));
            }
            Glide.with(Profile.this).load(mSelected.get(0)).into(image);
            putPictures(imageUrl.get(0));

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void putPictures(String path) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        ;
        Uri file = Uri.fromFile(new File(path));


        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("picUrl")
                                .setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Pic uploaded");
                                progress.setVisibility(View.GONE);
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(Profile.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void getUserDataFromDB() {
        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    UserModel model = dataSnapshot.getValue(UserModel.class);
                    if (model != null) {
                        SharedPrefs.setUserModel(model);
                        country = model.getCountry();
                        language = model.getLanguage();
                        dob = model.getDob();
                        name.setText(model.getName());

                        chooseBirthday.setText("DOB: " + (dob==null?"":dob));
                        chooseCountry.setText("Country: " + (country==null?"":country));
                        chooseLanguage.setText("Language: " + (language==null?"":language));
                        if (model.getPicUrl() != null) {
                            Glide.with(Profile.this).load(model.getPicUrl()).into(image);
                            progress.setVisibility(View.GONE);

                        }
                    }
                    progress.setVisibility(View.GONE);
                } else {
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void initMatisse() {
        Matisse.from(Profile.this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(8)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    private void takeUserToNextScreen() {
        UserModel userModel = SharedPrefs.getUserModel();
        userModel.setName(name.getText().toString());
        userModel.setCountry(country);
        userModel.setGender(gender);
        userModel.setDob(dob);
//        userModel.setInterest(interest);
        userModel.setLanguage(language);

        HashMap<String, Object> map = new HashMap<>();
        map.put(SharedPrefs.getUserModel().getUsername(), userModel);

        mDatabase.child("Users").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Updated");
                SharedPrefs.setSettingDone("yes");
                startActivity(new Intent(Profile.this, MainActivity.class));
                finish();
            }
        });
    }

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,

        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showCountryAlert() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Profile.this);
        builderSingle.setTitle("Select Country");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Profile.this, android.R.layout.simple_list_item_1);
        arrayAdapter.add("Pakistan");
        arrayAdapter.add("India");
        arrayAdapter.add("Bangladesh");
        arrayAdapter.add("UAE");
        arrayAdapter.add("USA");


        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chooseCountry.setText("Country: " + arrayAdapter.getItem(which));
                country = arrayAdapter.getItem(which);

            }
        });
        builderSingle.show();
    }

    private void showLanguageAlert() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Profile.this);
        builderSingle.setTitle("Select Langauge");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Profile.this, android.R.layout.simple_list_item_1);
        arrayAdapter.add("English");
        arrayAdapter.add("Urdu");
        arrayAdapter.add("Bangla");


        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chooseLanguage.setText("Language: " + arrayAdapter.getItem(which));
                language = arrayAdapter.getItem(which);

            }
        });
        builderSingle.show();
    }


}
