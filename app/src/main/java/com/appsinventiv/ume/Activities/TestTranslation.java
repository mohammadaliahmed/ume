package com.appsinventiv.ume.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.PrefManager;
import com.appsinventiv.ume.Utils.SharedPrefs;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class TestTranslation extends AppCompatActivity {

    private static final String API_KEY = "AIzaSyC80CrRlGZBMvWM0C-hWkmvj0Kkv9iExb4";
    EditText text;
    Button translate;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tran);

        textView = (TextView) findViewById(R.id.textView);
        translate = (Button) findViewById(R.id.translate);
        text = (EditText) findViewById(R.id.text);


        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateNow();
            }
        });


    }

    private void translateNow() {
        final Handler textViewHandler = new Handler();


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(API_KEY)
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(text.getText().toString(),
                                Translate.TranslateOption.targetLanguage("de"));
                textViewHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (textView != null) {
                            textView.setText(translation.getTranslatedText());
                        }
                    }
                });
                return null;
            }
        }.execute();

    }

}
