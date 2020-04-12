package com.example.talkar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView logo, circles;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String sp_isLoggedIn = "IsLoggedIn";
    private boolean userLoggedIn;

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        logo = findViewById(R.id.image_talkar);
        circles = findViewById(R.id.imageFadeCircles);

        logo.setAnimation(topAnim);
        circles.setAnimation(bottomAnim);

        new Handler().postDelayed(() -> {
            if (!isUserLoggedIn()){
                Intent intent = new Intent(MainActivity.this, Login.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(logo, "logo_image");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                startActivity(intent, options.toBundle());
                finish();
            }
            else {
                Intent intent = new Intent(MainActivity.this, MainApp.class);
                startActivity(intent);
                finish();
            }

        }, 3000);

    }

    private Boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        userLoggedIn = sharedPreferences.getBoolean(sp_isLoggedIn, false);
        return userLoggedIn;
    }
}
