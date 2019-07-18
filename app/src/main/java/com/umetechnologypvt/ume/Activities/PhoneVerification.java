package com.umetechnologypvt.ume.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.PrefManager;
import com.umetechnologypvt.ume.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PhoneVerification extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    LinearLayout progress;
    //    EditText number;
    Button sendCode;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = null;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    DatabaseReference mDatabase;
    ArrayList<String> phoneNumbers = new ArrayList<>();
    HashMap<String, UserModel> map = new HashMap<>();
    private PrefManager prefManager;
    PhoneAuthProvider phoneAuth;
    CountryCodePicker ccp;
    AppCompatEditText number;
    String foneCode;
    String finalNumber;
    GoogleApiClient apiClient;

    SignInButton google;
    GoogleSignInAccount account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        prefManager = new PrefManager(this);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        if (!prefManager.isFirstTimeLaunch()) {
//            launchHomeScreen();
            finish();
        } else {
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            apiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
            apiClient.connect();
            mDatabase = FirebaseDatabase.getInstance().getReference();

            this.setTitle("Verify your phone");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setElevation(0);
            }
            phoneAuth = PhoneAuthProvider.getInstance();
            google = findViewById(R.id.google);
            sendCode = findViewById(R.id.sendCode);
//            number = findViewById(R.id.number);
            ccp = (CountryCodePicker) findViewById(R.id.ccp);

            number = (AppCompatEditText) findViewById(R.id.number);
            ccp.registerPhoneNumberTextView(number);


            google.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                    startActivityForResult(i, 100);
                }
            });

            progress = findViewById(R.id.progress);
            getListOfUsers();
            foneCode = ccp.getDefaultCountryCode();
            SharedPrefs.setCountryCode(ccp.getDefaultCountryNameCode().toLowerCase());
            ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
                @Override
                public void onCountrySelected(Country selectedCountry) {
                    foneCode = selectedCountry.getPhoneCode();
                    SharedPrefs.setCountryCode(ccp.getDefaultCountryNameCode().toLowerCase());
                }
            });

            sendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (number.getText().length() == 0) {
                        number.setError("Enter phone number");
                    } else if (foneCode == null) {
                        CommonUtils.showToast("Select your country");
                    } else {
//                        loginUser("03030448686");
                        String num = number.getText().toString();
//                        if (foneCode.contains("92") && num.startsWith("03")) {
//                            num=num.substring(1);
//                        }

                        finalNumber = "+" + foneCode + num;
////                        CommonUtils.showToast(finalNumber);
//
                        progress.setVisibility(View.VISIBLE);
                        checkUser();
//                        sendVerifyCode(finalNumber);
                    }
                }
            });

        }
    }


    private void getListOfUsers() {
        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        String number = snapshot.getKey();
                        phoneNumbers.add(number);
                        map.put(number, userModel);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
//            e_fullname.setText(account.getDisplayName());

            String userId = account.getEmail().replace("@", "").replace(".", "");
            String email = account.getEmail();
            Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {

                }
            });
            finalNumber = userId;
            CommonUtils.showToast(userId);
            checkUser();

//            loginUser(account.getEmail().replace("@", "").replace(".", ""));

        }
    }
    private void sendVerifyCode(String phoneNumber) {

        phoneAuth.verifyPhoneNumber(
                finalNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        CommonUtils.showToast("Verified");
                        progress.setVisibility(View.GONE);

                        checkUser();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        CommonUtils.showToast(e.getMessage());
                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        CommonUtils.showToast("Code sent");
                        mVerificationId = verificationId;

                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        CommonUtils.showToast("Time out");
                        sendCode.setText("Resend");
                        progress.setVisibility(View.GONE);

                    }
                }
        );
    }

    private void checkUser() {

        if (phoneNumbers.contains(finalNumber)) {
            loginUser(finalNumber);
        } else {
            signupUser();
        }
    }

    private void loginUser(String num) {

        SharedPrefs.setUserModel(map.get(num));
        launchHomeScreen();
    }

    private void signupUser() {
        SharedPrefs.setPhoneNumber(finalNumber);
        final UserModel userModel = new UserModel(finalNumber,
                finalNumber,
                SharedPrefs.getFcmKey(),
                System.currentTimeMillis(),
                mVerificationId
                ,
                true,
                "Online");
        mDatabase.child("Users").child(finalNumber).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
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

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            startActivity(new Intent(PhoneVerification.this, Profile.class));
        }


        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
