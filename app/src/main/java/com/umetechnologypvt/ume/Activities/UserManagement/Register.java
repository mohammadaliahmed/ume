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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.Activities.Profile;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.PrefManager;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;


public class Register extends AppCompatActivity {

    EditText name, email, username, password, confirmPassword;
    CheckBox agree;
    Button signup;
    TextView login;
    DatabaseReference mDatabase;
    boolean checked;
    HashMap<String, UserModel> map = new HashMap<>();
    ArrayList<String> usernames = new ArrayList<>();
    TextView agreeText;

    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
//            launchHomeScreen();
            finish();
        }
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        agree = findViewById(R.id.agree);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        agreeText = findViewById(R.id.agreeText);
        customTextView(agreeText);
        customLoginTextView(login);


        agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        checked = true;
                    } else {
                        checked = false;
                    }
                }
            }
        });

        if (Login.account != null) {
            name.setText(Login.account.getDisplayName());
            email.setText(Login.account.getEmail());
            username.setText(Login.account.getEmail().replace("@gmail.com", "").replace(".", ""));
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() == 0) {
                    name.setError("Enter name");
                } else if (email.getText().length() == 0) {
                    email.setError("Enter email");
                } else if (username.getText().length() == 0) {
                    username.setError("Enter username");
                } else if (password.getText().length() == 0) {
                    password.setError("Enter password");
                } else if (confirmPassword.getText().length() == 0) {
                    confirmPassword.setError("Enter confirm Password");
                } else if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
                    confirmPassword.setError("Password does not match");
                } else if (!checked) {
                    CommonUtils.showToast("Please agree to terms and conditions");
                } else {
//                    signUp();
                    checkUser();
                }
            }
        });


        getAllUsersFromDB();
    }

    private void checkUser() {

        if (usernames.contains(username.getText().toString())) {
//            loginUser(finalNumber);
            CommonUtils.showToast("Username is already taken");
        } else {
            signUp();
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

    private void signUp() {
        SharedPrefs.setPhoneNumber(username.getText().toString());
        final UserModel userModel = new UserModel(username.getText().toString(),
                name.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                System.currentTimeMillis()
        );
        mDatabase.child("Users").child(username.getText().toString())
                .setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SharedPrefs.setUserModel(userModel);
                launchHomeScreen();
            }
        });
    }


    private void launchHomeScreen() {

        prefManager.setFirstTimeLaunch(false);
        prefManager.setIsFirstTimeLaunchWelcome(false);


        if (SharedPrefs.getSettingDone().equalsIgnoreCase("yes")) {
            startActivity(new Intent(Register.this, MainActivity.class));
        } else {
            startActivity(new Intent(Register.this, Profile.class));
        }
    }

    private void customLoginTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "Already have and account? ");
        spanTxt.append("Login");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                finish();
            }
        }, spanTxt.length() - "Login".length(), spanTxt.length(), 0);


        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "I agree to the ");
        spanTxt.append("Term of services");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                Toast.makeText(getApplicationContext(), "Terms of services Clicked",
//                        Toast.LENGTH_SHORT).show();
            }
        }, spanTxt.length() - "Term of services".length(), spanTxt.length(), 0);
        spanTxt.append(" and");
        spanTxt.setSpan(new ForegroundColorSpan(Color.BLACK), 32, spanTxt.length(), 0);
        spanTxt.append(" Privacy Policy");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                Toast.makeText(getApplicationContext(), "Privacy Policy Clicked",
//                        Toast.LENGTH_SHORT).show();
            }
        }, spanTxt.length() - " Privacy Policy".length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }
}
