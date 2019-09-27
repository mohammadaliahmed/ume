package com.umetechnologypvt.ume.Activities.UserManagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText username, password;
    Button login;
    TextView signup;
    DatabaseReference mDatabase;
    HashMap<String, UserModel> map = new HashMap<>();
    ArrayList<String> usernames = new ArrayList<>();

    private PrefManager prefManager;
    RelativeLayout google;
    public static GoogleSignInAccount account;
    GoogleApiClient apiClient;


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

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        apiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        google = findViewById(R.id.google);
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
                    checkUser(username.getText().toString());
                }
            }
        });


        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });
        getAllUsersFromDB();
    }

    private void signin() {
        Intent i = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(i, 100);
        apiClient.connect();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(googleSignInResult);
        }

    }

    private void handleResult(GoogleSignInResult googleSignInResult) {
        if (googleSignInResult.isSuccess()) {
            account = googleSignInResult.getSignInAccount();

            CommonUtils.showToast(account.getDisplayName());

            String userId = account.getEmail().replace("@", "").replace(".", "");
            String email = account.getEmail();
            Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {

                }
            });
            String id = email.replace("@gmail.com", "").replace(".", "");
            checkUserFromGmail(id);


        }
    }

    private void checkUserFromGmail(String id) {

        if (usernames.contains(id)) {
            CommonUtils.showToast("Logged in successfully");
            SharedPrefs.setUserModel(map.get(id));
            launchHomeScreen();
        } else {
            final UserModel userModel = new UserModel(id,
                    account.getDisplayName(),
                    account.getEmail(),
                    account.getId(),
                    System.currentTimeMillis()
            );
            mDatabase.child("Users").child(id)
                    .setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    SharedPrefs.setUserModel(userModel);
                    launchHomeScreen();
                    CommonUtils.showToast("Successfully Registered");
//                    startActivity(new Intent(Login.this, Profile.class));
                }
            });


        }
    }

    private void checkUser(String id) {

        if (usernames.contains(id)) {
//            loginUser(finalNumber);
            if (map.get(username.getText().toString()).getPassword().equals(password.getText().toString())) {
                SharedPrefs.setUserModel(map.get(id));
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
                        try {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            String number = snapshot.getKey();
                            usernames.add(userModel.getUsername());
                            map.put(number, new UserModel(userModel.getUsername(), userModel.getName(),
                                    userModel.getEmail(), userModel.getPassword(), userModel.getTime()));
                        } catch (Exception e) {
                            Log.d("userIssue", snapshot.getKey());
                        }


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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
