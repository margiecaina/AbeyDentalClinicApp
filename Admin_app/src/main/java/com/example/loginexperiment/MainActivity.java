package com.example.loginexperiment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.loginexperiment.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView notificationBtn;

    // notification background
    final String TAG = "HOME_FRAGMENT";
    NotificationCompat.Builder builder;
    String[] permissions = { Manifest.permission.POST_NOTIFICATIONS };
    final String CHANNEL_ID = "APPOINTMENT_REMINDER";
    private static final int REQUEST_CODE_PERMISSION = 1001;
    boolean globalValue;
    int counter = 0;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("");

        // Set homefragment as the first thing to see

        replaceFragment(new HomeFragment());
        // Make the bottom navigational bar and message float permanent position
        binding.bottomNavigationalView.setBackground(null);

        binding.bottomNavigationalView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.schedule:
                    replaceFragment(new ScheduleFragment());
                    break;
                case R.id.approval:
                    replaceFragment(new ApprovalFragment());
                    break;
                case R.id.queue:
                    replaceFragment(new QueueFragment());
                    break;
                case R.id.feedback:
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

        // navigational bar side

        binding.sideNavigationMenu.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.dentistprofile:
                    replaceFragment(new ProfileFragment());
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.history:
                    replaceFragment(new HistoryFragment());
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }
            return true;
        });

        checkAppointments();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()        {
            @Override
            public void run()
            {
                globalValueChecker();
                if (counter != 2)
                {
                    counter += 1;
                    handler.postDelayed(this, 5000);
                }
                else
                {
                    handler.removeCallbacks(this);
                }
            }
        }, 5000);

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // to notify in background

    private void globalValueChecker()
    {
        globalValue = GlobalVariables.getInstance().getGlobalVariable();
        if (globalValue)
        {
            Log.d(TAG, "globalValueChecker: "+ globalValue);
            onReminder();
        }
        else
        {
            Log.d(TAG, "globalValueChecker: "+ globalValue);
        }

    }
    private void onReminder ()
    {
        createNotificationChannel();
        String textTitle = "Appointment Reminder";
        String textContent = "You have an appointment today! ";

        builder = new NotificationCompat.Builder(getApplicationContext().getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.dental_icon)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        showNotification();
    }

    private void showNotification()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions();
        }
        else
        {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(666, builder.build());
            GlobalVariables.getInstance().setGlobalVariable(false);
        }
    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(new MainActivity(), permissions, REQUEST_CODE_PERMISSION);
    }

    private void createNotificationChannel()
    {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "APPOINTMENT_REMINDER";
            String description = "This channel is for appointment reminder notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void checkAppointments() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Appointment");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    GetterSetter appointment = ds.getValue(GetterSetter.class);
                    if (appointment != null) {
                        GetterSetter getterSetter = snapshot.getValue(GetterSetter.class);
                        String getDate = getterSetter.getAppointmentdate();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Error retrieving appointments.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}