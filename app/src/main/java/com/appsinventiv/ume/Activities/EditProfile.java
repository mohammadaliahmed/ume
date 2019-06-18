package com.appsinventiv.ume.Activities;

import android.app.DatePickerDialog;
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
import android.view.Menu;
import android.view.MenuItem;
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

import com.appsinventiv.ume.Activities.ImageCrop.PickerBuilder;
import com.appsinventiv.ume.BottomDialogs.BottomDialog;
import com.appsinventiv.ume.BottomDialogs.DialogCallbacks;
import com.appsinventiv.ume.Models.Example;
import com.appsinventiv.ume.Models.LangaugeModel;
import com.appsinventiv.ume.Models.UserModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;
import com.appsinventiv.ume.Utils.CompressImage;
import com.appsinventiv.ume.Utils.Constants;
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
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    DatabaseReference mDatabase;
    CircleImageView image;
    EditText name;
    Button continuebtn;
    ArrayList<String> imageUrl = new ArrayList<>();
    public static List<String> learningLanguages = new ArrayList<>();
    public static List<String> interestList = new ArrayList<>();
    List<Uri> mSelected = new ArrayList<>();
    private static final int REQUEST_CODE_CHOOSE = 23;
    RadioGroup radioGender;
    RadioButton radioGenderButton;

    TextView chooseCountry, chooseLanguage, chooseBirthday, chooseLeaningLanguage, chooseInterest, chooseCurrentLocation;
    public static String language, country, currentLocation;
    String gender, interest;
    StorageReference mStorageRef;
    ProgressBar progress;
    private String dob;
    int yearr = 1990;
    private int age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        this.setTitle("Edit profile");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }

        getPermissions();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        chooseCurrentLocation = findViewById(R.id.chooseCurrentLocation);
        chooseLeaningLanguage = findViewById(R.id.chooseLeaningLanguage);
        chooseInterest = findViewById(R.id.chooseInterest);
        continuebtn = findViewById(R.id.continuebtn);
        chooseBirthday = findViewById(R.id.chooseBirthday);
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        radioGender = findViewById(R.id.radioGender);
        chooseCountry = findViewById(R.id.chooseCountry);
        chooseLanguage = findViewById(R.id.chooseLanguage);
        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        getUserDataFromDB();
        setupLearningLangaue();
        setupInterest();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                initMatisse();
                startGallery();
            }
        });

        chooseLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showLanguageAlert();
                BottomDialog.showSpokenDialog(EditProfile.this, CommonUtils.languageList(), new DialogCallbacks() {
                    @Override
                    public void onOkPressed() {
                        chooseLanguage.setText("Language : " + language);
                    }

                    @Override
                    public void onCancelled() {

                    }
                });
            }
        });
        chooseCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myJson = inputStreamToString(getResources().openRawResource(R.raw.countries));
                Example myModel = new Gson().fromJson(myJson, Example.class);
                BottomDialog.showCountiesDialog(EditProfile.this, myModel.getCountries(), new DialogCallbacks() {
                    @Override
                    public void onOkPressed() {
                        chooseCountry.setText("Country: " + country);
                    }

                    @Override
                    public void onCancelled() {

                    }
                });


            }
        });
        chooseCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                BottomDialog.showCountiesDialog(EditProfile.this, CommonUtils.countryList(), new DialogCallbacks() {
                    @Override
                    public void onOkPressed() {
                        chooseCurrentLocation.setText("Current location: " + currentLocation);
                    }

                    @Override
                    public void onCancelled() {

                    }
                });


            }
        });

        chooseBirthday.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog dlg = new DatePickerDialog(EditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        yearr = year;
                        age = 2019 - yearr;
                        dob = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        chooseBirthday.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));

                    }
                }, yearr, 1, 1);
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
                } else if (dob == null) {
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

                    takeUserToNextScreen();
                }
            }
        });
    }

    private void startGallery() {

        imageUrl.clear();
        new PickerBuilder(EditProfile.this, PickerBuilder.SELECT_FROM_GALLERY)
                .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                    @Override
                    public void onImageReceived(Uri imageUri) {
                        mSelected = new ArrayList<>();
                        mSelected.add(imageUri);
                        progress.setVisibility(View.VISIBLE);
                        for (Uri img :
                                mSelected) {
                            CompressImage compressImage = new CompressImage(EditProfile.this);
                            imageUrl.add(compressImage.compressImage("" + img));
                        }
                        Glide.with(EditProfile.this).load(mSelected.get(0)).into(image);
                        putPictures(imageUrl.get(0));

                    }
                })
                .start();
    }

    public String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }

    private void setupLearningLangaue() {

        chooseLeaningLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.showLanguagesDialog(EditProfile.this, CommonUtils.languageList(), new DialogCallbacks() {
                    @Override
                    public void onOkPressed() {
                        if (learningLanguages.size() > 0) {
                            chooseLeaningLanguage.setText("Learning languages: " + learningLanguages);
                        }
                    }

                    @Override
                    public void onCancelled() {

                    }
                });
            }
        });
    }

    private void setupInterest() {
        ArrayList<String> inerests = new ArrayList<>();
        inerests.add("Games");
        inerests.add("Dating");
        inerests.add("Food");
        inerests.add("Movies");
        inerests.add("Music");
        inerests.add("Outdoor");
        inerests.add("Driving");


        chooseInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.showInterestDialog(EditProfile.this, inerests, new DialogCallbacks() {
                    @Override
                    public void onOkPressed() {
                        if (interestList.size() > 0) {
                            chooseInterest.setText("Interests: " + interestList);
                        }
                    }

                    @Override
                    public void onCancelled() {

                    }
                });
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
                CompressImage compressImage = new CompressImage(EditProfile.this);
                imageUrl.add(compressImage.compressImage("" + img));
            }
            Glide.with(EditProfile.this).load(mSelected.get(0)).into(image);
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
                        Toast.makeText(EditProfile.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void takeUserToNextScreen() {
        UserModel userModel = SharedPrefs.getUserModel();
        userModel.setName(name.getText().toString());
        userModel.setCountry(country);
        userModel.setGender(gender);
        userModel.setDob(dob);
        userModel.setAge(age);
        userModel.setCurrentLocation(currentLocation);
        userModel.setInterests(interestList);
        userModel.setLearningLanguage(learningLanguages);
        userModel.setLanguage(language);

        HashMap<String, Object> map = new HashMap<>();
        map.put(SharedPrefs.getUserModel().getUsername(), userModel);

        mDatabase.child("Users").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Updated");
                startActivity(new Intent(EditProfile.this, MainActivity.class));
                finish();
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
                        currentLocation = model.getCurrentLocation();
                        language = model.getLanguage();
                        dob = model.getDob();
                        name.setText(model.getName());
                        age = model.getAge();

                        chooseBirthday.setText("DOB: " + (dob == null ? "" : dob));
                        chooseCountry.setText("Country: " + (country == null ? "" : country));
                        chooseCurrentLocation.setText("Current: " + (currentLocation == null ? "" : currentLocation));
                        chooseLanguage.setText("Language: " + (language == null ? "" : language));
                        if (model.getPicUrl() != null) {
                            try {
                                Glide.with(EditProfile.this).load(model.getPicUrl()).into(image);
                                progress.setVisibility(View.GONE);
                            } catch (IllegalArgumentException e) {
                                progress.setVisibility(View.GONE);
                            }

                        }
                        if (model.getGender().equalsIgnoreCase("male")) {
                            ((RadioButton) radioGender.getChildAt(0)).setChecked(true);
                        } else {
                            ((RadioButton) radioGender.getChildAt(1)).setChecked(true);

                        }

                        learningLanguages = model.getLearningLanguage();
                        interestList = model.getInterests();
                        if (interestList.size() > 0) {
                            chooseInterest.setText("Interests: " + interestList);
                        }
                        if (learningLanguages.size() > 0) {
                            chooseLeaningLanguage.setText("Learning languages: " + learningLanguages);
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
        Matisse.from(EditProfile.this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(1)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

//    private void showCountryAlert() {
//        AlertDialog.Builder builderSingle = new AlertDialog.Builder(EditProfile.this);
//        builderSingle.setTitle("Select Country");
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditProfile.this, android.R.layout.simple_list_item_1);
//        arrayAdapter.add("Pakistan");
//        arrayAdapter.add("India");
//        arrayAdapter.add("Bangladesh");
//        arrayAdapter.add("UAE");
//        arrayAdapter.add("USA");
//
//
//        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                chooseCountry.setText("Country: " + arrayAdapter.getItem(which));
//                country = arrayAdapter.getItem(which);
//
//            }
//        });
//        builderSingle.show();
//    }

    private void showLanguageAlert() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(EditProfile.this);
        builderSingle.setTitle("Select Langauge");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditProfile.this, android.R.layout.simple_list_item_1);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
