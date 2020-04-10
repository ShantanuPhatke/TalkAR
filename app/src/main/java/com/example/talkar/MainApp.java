package com.example.talkar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MainApp extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    public static final String sp_previous_day = "PreviousDay";

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainapp);

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int previousDay = sharedPreferences.getInt(sp_previous_day, 0);

        // This condition will trigger only once a day
        if (previousDay != currentDay){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(sp_previous_day, currentDay);
            editor.apply();

            // Open the dialog
            showWordOTD();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

    }

    private void showWordOTD() {
        WordDialog wordDialog = new WordDialog();
        wordDialog.show(getSupportFragmentManager(), "Word of the day");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_lesson:
                        selectedFragment = new LessonFragment();
                        break;
                    case R.id.nav_progress:
                        selectedFragment = new ProgressFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true;
            };

}
