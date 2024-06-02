package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Set homefragment as the first thing to see

        // Make the bottom navigational bar and message float permanent position
        binding.bottomNavigationalView.setBackground(null);

        binding.bottomNavigationalView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    break;
                case R.id.dentist:
                    break;
                case R.id.profile:
                    break;
                case R.id.services:
                    break;
                case R.id.about:
                    break;
            }
            return true;
        });
        binding.message.setBackground(null);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}