package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umetechnologypvt.ume.Activities.Search.Filters;
import com.umetechnologypvt.ume.Activities.Search.SearchActivity;
import com.umetechnologypvt.ume.BottomDialogs.BottomDialog;
import com.umetechnologypvt.ume.BottomDialogs.DialogCallbacks;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;

import androidx.fragment.app.Fragment;


public class FiltersFragment extends Fragment {
    Context context;

    DatabaseReference mDatabase;

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
    public void onResume() {
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

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase=FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_filters_fragment, container, false);
        reset = rootView.findViewById(R.id.reset);
        ok = rootView.findViewById(R.id.ok);
        radioGender = rootView.findViewById(R.id.radioGender);
        search = rootView.findViewById(R.id.search);
        chooseCurrent = rootView.findViewById(R.id.chooseCurrent);
        chooseAge = rootView.findViewById(R.id.chooseAge);
        chooseLearningLanguage = rootView.findViewById(R.id.chooseLearningLanguage);
        chooseSpokenLanguage = rootView.findViewById(R.id.chooseSpokenLanguage);
        chooseInterest = rootView.findViewById(R.id.chooseInterest);
        chooseCountry = rootView.findViewById(R.id.chooseCountry);


        chooseSpoken = rootView.findViewById(R.id.chooseSpoken);
        chooseLearning = rootView.findViewById(R.id.chooseLearning);
        chooseCountryr = rootView.findViewById(R.id.chooseCountryr);
        chooseCurrentLocation = rootView.findViewById(R.id.chooseCurrentLocation);
        chooseInterestRel = rootView.findViewById(R.id.chooseInterestRel);
        chooseAgeRel= rootView.findViewById(R.id.chooseAgeRel);







        ((RadioButton) radioGender.getChildAt(0)).setChecked(true);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = search.getText().toString();
                int selectedId = radioGender.getCheckedRadioButtonId();
                radioButton = (RadioButton) rootView.findViewById(selectedId);
                gender = radioButton.getText().toString();
//                CommonUtils.showToast(radioButton.getText().toString()+" "+country);
                Intent i = new Intent(context, SearchActivity.class);
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
                BottomDialog.showCountiesDialog(context, CommonUtils.countryList(), new DialogCallbacks() {
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
                BottomDialog.showCountiesDialog(context, CommonUtils.countryList(), new DialogCallbacks() {
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
                BottomDialog.showSpokenDialog(context, CommonUtils.languageList(), new DialogCallbacks() {
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


                BottomDialog.showSingleInterestDialog(context,  CommonUtils.interestList(), new DialogCallbacks() {
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
                BottomDialog.showSpokenDialog(context, CommonUtils.languageList(), new DialogCallbacks() {
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
                BottomDialog.showAgeRangePicker(context, new DialogCallbacks() {
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
                BottomDialog.showCountiesDialog(context, CommonUtils.countryList(), new DialogCallbacks() {
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
                BottomDialog.showCountiesDialog(context, CommonUtils.countryList(), new DialogCallbacks() {
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
                BottomDialog.showSpokenDialog(context, CommonUtils.languageList(), new DialogCallbacks() {
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

                BottomDialog.showSingleInterestDialog(context,  CommonUtils.interestList(), new DialogCallbacks() {
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
                BottomDialog.showSpokenDialog(context, CommonUtils.languageList(), new DialogCallbacks() {
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
                BottomDialog.showAgeRangePicker(context, new DialogCallbacks() {
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


    


        return rootView;
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
