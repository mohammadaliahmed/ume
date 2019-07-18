package com.umetechnologypvt.ume.Activities.Search;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umetechnologypvt.ume.BottomDialogs.BottomDialog;
import com.umetechnologypvt.ume.BottomDialogs.DialogCallbacks;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;

public class Filters extends AppCompatActivity {


    TextView chooseCountry, chooseLearningLanguage, chooseSpokenLanguage, chooseInterest, chooseAge, chooseCurrent;
    Button ok, reset;
    RadioGroup radioGender;
    private RadioButton radioButton;
    RelativeLayout chooseSpoken, chooseLearning, chooseCountryr, chooseCurrentLocation, chooseInterestRel, chooseAgeRel;

    EditText search;
    String word = "";
    private String ageGroup;
    public static String learnLanguage = "any",
            country = "any", currentLocation = "any",
            language = "any", interest = "any", startAge = "18", endAge = "70", gender = "any";


    @Override
    protected void onResume() {
        super.onResume();
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
        search.setText("");
    }

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
        search = findViewById(R.id.search);
        chooseCurrent = findViewById(R.id.chooseCurrent);
        chooseAge = findViewById(R.id.chooseAge);
        chooseLearningLanguage = findViewById(R.id.chooseLearningLanguage);
        chooseSpokenLanguage = findViewById(R.id.chooseSpokenLanguage);
        chooseInterest = findViewById(R.id.chooseInterest);
        chooseCountry = findViewById(R.id.chooseCountry);


        chooseSpoken = findViewById(R.id.chooseSpoken);
        chooseLearning = findViewById(R.id.chooseLearning);
        chooseCountryr = findViewById(R.id.chooseCountryr);
        chooseCurrentLocation = findViewById(R.id.chooseCurrentLocation);
        chooseInterestRel = findViewById(R.id.chooseInterestRel);
        chooseAgeRel= findViewById(R.id.chooseAgeRel);







        ((RadioButton) radioGender.getChildAt(0)).setChecked(true);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = search.getText().toString();
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
                i.putExtra("word", word);

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
                search.setText("");
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


                BottomDialog.showSingleInterestDialog(Filters.this,  CommonUtils.interestList(), new DialogCallbacks() {
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







        chooseCurrentLocation.setOnClickListener(new View.OnClickListener() {
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
        chooseCountryr.setOnClickListener(new View.OnClickListener() {
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
        chooseLearning.setOnClickListener(new View.OnClickListener() {
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
        chooseInterestRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomDialog.showSingleInterestDialog(Filters.this,  CommonUtils.interestList(), new DialogCallbacks() {
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
        chooseSpoken.setOnClickListener(new View.OnClickListener() {
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


        chooseAgeRel.setOnClickListener(new View.OnClickListener() {
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
