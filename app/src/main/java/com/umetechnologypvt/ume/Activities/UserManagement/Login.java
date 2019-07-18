package com.umetechnologypvt.ume.Activities.UserManagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umetechnologypvt.ume.Activities.MainActivity;
import com.umetechnologypvt.ume.Activities.Profile;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.PrefManager;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {

    EditText username, password;
    Button login;
    TextView signup;
    DatabaseReference mDatabase;
    HashMap<String, UserModel> map = new HashMap<>();
    ArrayList<String> usernames = new ArrayList<>();

    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
//            launchHomeScreen();
            finish();
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        customLoginTextView(signup);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().length() == 0) {
                    username.setError("Enter username");
                } else if (password.getText().length() == 0) {
                    password.setError("Enter password");
                } else {
                    checkUser();
                }
            }
        });


        getAllUsersFromDB();
    }

    private void checkUser() {

        if (usernames.contains(username.getText().toString())) {
//            loginUser(finalNumber);
            if (map.get(username.getText().toString()).getPassword().equals(password.getText().toString())) {
                SharedPrefs.setUserModel(map.get(username.getText().toString()));
                launchHomeScreen();
            } else {
                CommonUtils.showToast("Wrong Password");
            }
        } else {
            CommonUtils.showToast("Username does not exits\nPlease signup");
        }
    }


    private void getAllUsersFromDB() {
        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        String number = snapshot.getKey();
                        usernames.add(userModel.getUsername());
                        map.put(number, new UserModel(userModel.getUsername(), userModel.getName(),
                                userModel.getEmail(), userModel.getPassword(), userModel.getTime()));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void launchHomeScreen() {

        prefManager.setFirstTimeLaunch(false);
        prefManager.setIsFirstTimeLaunchWelcome(false);


        if (SharedPrefs.getSettingDone().equalsIgnoreCase("yes")) {
            startActivity(new Intent(Login.this, MainActivity.class));
        } else {
            startActivity(new Intent(Login.this, Profile.class));
        }
    }

    private void customLoginTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "Dont have an account? ");
        spanTxt.append("Signup here");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(Login.this, Register.class));
            }
        }, spanTxt.length() - "Signup here".length(), spanTxt.length(), 0);


        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }


}
