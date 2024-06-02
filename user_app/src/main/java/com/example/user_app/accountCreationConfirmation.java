package com.example.user_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class accountCreationConfirmation extends AppCompatActivity {
    AppCompatButton ClickHereToLoginBtn;

    ImageView exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation_confirmation);

        ClickHereToLoginBtn = findViewById(R.id.ClickHereToLoginBtn);

        ClickHereToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(accountCreationConfirmation.this, StartActivity.class));
            }
        });

        exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(accountCreationConfirmation.this, StartActivity.class));
            }
        });
    }
}