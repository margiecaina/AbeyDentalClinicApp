package com.example.user_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;

public class TermsConditions extends AppCompatActivity {

    AppCompatButton IAgreeBtn;
    CheckBox acceptTermsCheckbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        //content of terms and condition * data privacy of abey dental clinic
        String htmlAsString = getString(R.string.html);      // used by WebView

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);

        // code for the button once click i agree
        acceptTermsCheckbox = findViewById(R.id.acceptTerms);
        IAgreeBtn = findViewById(R.id.IAgreeButton);

        IAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acceptTermsCheckbox.isChecked()){
                    startActivity(new Intent(TermsConditions.this, RegisterActivity.class));
                }else{
                    acceptTermsCheckbox.setError("");
                }
            }
        });
    }
}