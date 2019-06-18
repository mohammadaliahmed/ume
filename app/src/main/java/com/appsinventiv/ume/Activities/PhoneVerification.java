package com.appsinventiv.ume.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appsinventiv.ume.Models.UserModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;
import com.appsinventiv.ume.Utils.PrefManager;
import com.appsinventiv.ume.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
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

public class PhoneVerification extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
//            launchHomeScreen();
            finish();
        } else {
            mDatabase = FirebaseDatabase.getInstance().getReference();

            this.setTitle("Verify your phone");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setElevation(0);
            }
            phoneAuth = PhoneAuthProvider.getInstance();
            sendCode = findViewById(R.id.sendCode);
//            number = findViewById(R.id.number);
            ccp = (CountryCodePicker) findViewById(R.id.ccp);

            number = (AppCompatEditText) findViewById(R.id.number);
            ccp.registerPhoneNumberTextView(number);

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
                        finalNumber="+"+foneCode+number.getText().toString();
////                        CommonUtils.showToast(finalNumber);
//
                        progress.setVisibility(View.VISIBLE);
                        checkUser();
//                    sendVerifyCode(finalNumber);
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
                        checkUser();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        CommonUtils.showToast(e.getMessage());
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

}
