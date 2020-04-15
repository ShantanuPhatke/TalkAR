package com.example.talkar;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InventoryShapes extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    String[] imagesArray;
    int lessonsCompleted;
    TextView hours;

    private static final String SHARED_PREFS = "sharedPrefs";
    public static final String sp_lesson_shape = "ShapesCompleted";

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_shapes);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Shapes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        lessonsCompleted = sharedPreferences.getInt(sp_lesson_shape, 0);

        imagesArray = getResources().getStringArray(R.array.modelShape_array);

        int []arr = new int[imagesArray.length];

        for (int i=0; i<imagesArray.length; i ++){
            int resID = getResources().getIdentifier(imagesArray[i] , "drawable", getPackageName());
            arr[i] = resID;
        }

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(arr, lessonsCompleted);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);

        // Hours
        hours = findViewById(R.id.hours);
        String hourCount = lessonsCompleted*3+"/"+imagesArray.length*3+" Minutes";
        hours.setText(hourCount);

    }
}

