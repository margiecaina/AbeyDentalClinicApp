package com.example.user_app;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.user_app.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView notificationBtn;

    @SuppressLint({"MissingInflatedId", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // remove app name
        setTitle("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Set profile fragment as the first thing to see
        replaceFragment(new HomeFragment());

        binding.bottomNavigationalView.setBackground(null);
        binding.bottomNavigationalView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.dentist:
                    replaceFragment(new DentistFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.services:
                    replaceFragment(new ServicesFragment());
                    break;
                case R.id.Feedback:
                    replaceFragment(new FeedbackFragment());
                    break;
            }
            return true;
        });

        // notification code when clicked it will go to notification fragment
        notificationBtn = findViewById(R.id.notification);
        notificationBtn.setOnClickListener(v -> replaceFragment(new NotificationFragment()));

        // hooks

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.sideNavigationMenu);
        toolbar = findViewById(R.id.toolbar);

        // toolbar
        setSupportActionBar(toolbar);

        // navigation drawer menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);

        // change toggle color
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // navigational bar side

        binding.sideNavigationMenu.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.aboutUs:
                    replaceFragment(new AboutUsFragment());
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.history:
                    replaceFragment(new HistoryFragment());
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}