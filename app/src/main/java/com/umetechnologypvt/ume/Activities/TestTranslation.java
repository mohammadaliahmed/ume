package com.umetechnologypvt.ume.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umetechnologypvt.ume.R;
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
