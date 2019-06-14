package com.appsinventiv.ume.Activities.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.ume.Activities.EditProfile;
import com.appsinventiv.ume.Adapters.SearchedUserListAdapter;
import com.appsinventiv.ume.BottomDialogs.BottomDialog;
import com.appsinventiv.ume.BottomDialogs.DialogCallbacks;
import com.appsinventiv.ume.Models.Example;
import com.appsinventiv.ume.Models.UserModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;
import com.appsinventiv.ume.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Filters extends AppCompatActivity {


    TextView chooseCountry, chooseLearningLanguage, chooseSpokenLanguage, chooseInterest, chooseAge, chooseCurrent;
    Button ok, reset;
    RadioGroup radioGender;
    private RadioButton radioButton;

    EditText search;
    String word;
    private String ageGroup;
    public static String learnLanguage = "any",
            country = "any", currentLocation = "any",
            language = "any", interest = "any", startAge = "18", endAge = "70", gender = "any";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Custom filters");
        reset = findViewById(R.id.reset);
        ok = findViewById(R.id.ok);
        radioGender = findViewById(R.id.radioGender);
        chooseCurrent = findViewById(R.id.chooseCurrent);
        chooseAge = findViewById(R.id.chooseAge);
        chooseLearningLanguage = findViewById(R.id.chooseLearningLanguage);
        chooseSpokenLanguage = findViewById(R.id.chooseSpokenLanguage);
        chooseInterest = findViewById(R.id.chooseInterest);
        chooseCountry = findViewById(R.id.chooseCountry);
        ((RadioButton) radioGender.getChildAt(0)).setChecked(true);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGender.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                gender = radioButton.getText().toString();
//                CommonUtils.showToast(radioButton.getText().toString()+" "+country);
                Intent i = new Intent(Filters.this, SearchActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("learnLanguage", learnLanguage);
                i.putExtra("country", country);
                i.putExtra("interest", interest);
                i.putExtra("currentLocation", currentLocation);
                i.putExtra("language", language);
                i.putExtra("startAge", Integer.parseInt(startAge));
                i.putExtra("endAge", Integer.parseInt(endAge));
                i.putExtra("gender", gender);

                startActivity(i);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                learnLanguage = "any";
                country = "any";
                currentLocation = "any";
                language = "any";
                interest = "any";
                startAge = "18";
                endAge = "70";
                gender = "any";
                chooseCountry.setText("Any");
                chooseLearningLanguage.setText("Any");
                chooseSpokenLanguage.setText("Any");
                chooseInterest.setText("Any");
                chooseAge.setText("Any");
                chooseCurrent.setText("Any");
            }
        });

        chooseCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.showCountiesDialog(Filters.this, CommonUtils.countryList(), new DialogCallbacks() {
                    @Override
                    public void onOkPressed() {
                        chooseCurrent.setText(country);
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
                BottomDialog.showCountiesDialog(Filters.this, CommonUtils.countryList(), new DialogCallbacks() {
                    @Override
                    public void onOkPressed() {
                        chooseCountry.setText(country);
                    }

                    @Override
                    public void onCancelled() {

                    }
                });
            }
        });
        chooseLearningLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.showSpokenDialog(Filters.this, CommonUtils.languageList(), new DialogCallbacks() {
                    @Override
                    public void onOkPressed() {
                        chooseLearningLanguage.setText(language);
                    }

                    @Override
                    public void onCancelled() {

                    }
                });
            }
        });
        chooseInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> inerests = new ArrayList<>();
                inerests.add("Games");
                inerests.add("Dating");
                inerests.add("Food");
                inerests.add("Movies");
                inerests.add("Music");
                inerests.add("Outdoor");
                inerests.add("Driving");

                BottomDialog.showSingleInterestDialog(Filters.this, inerests, new DialogCallbacks() {
                    @Override
                    public void onOkPressed() {
                        chooseInterest.setText(interest);
                    }

                    @Override
                    public void onCancelled() {

                    }
                });
            }
        });
        chooseSpokenLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.showSpokenDialog(Filters.this, CommonUtils.languageList(), new DialogCallbacks() {
                    @Override
                    public void onOkPressed() {
                        chooseSpokenLanguage.setText(language);
                    }

                    @Override
                    public void onCancelled() {

                    }
                });
            }
        });


        chooseAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.showAgeRangePicker(Filters.this, new DialogCallbacks() {
                    @Override
                    public void onOkPressed() {
                        chooseAge.setText(startAge + " - " + endAge);
                    }

                    @Override
                    public void onCancelled() {

                    }
                });
            }
        });


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
