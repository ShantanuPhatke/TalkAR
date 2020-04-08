 package com.example.talkar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hsalf.smilerating.SmileRating;

 public class Feedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        SmileRating smileRating2= findViewById(R.id.smile_rating2);
        smileRating2.setOnSmileySelectionListener((smiley, reselected) -> {
            switch (smiley) {
                case SmileRating.BAD: Toast.makeText(Feedback.this, "BAD", Toast.LENGTH_SHORT).show();
                    break;
                case SmileRating.GOOD:
                    Toast.makeText(Feedback.this, "GOOD", Toast.LENGTH_SHORT).show();
                    break;
                case SmileRating.GREAT:
                    Toast.makeText(Feedback.this, "GREAT",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmileRating.OKAY:
                    Toast.makeText(Feedback.this, "OKAY", Toast.LENGTH_SHORT).show
                            ();
                    break;
                case SmileRating.TERRIBLE:
                    Toast.makeText(Feedback.this, "TERRIBLE",
                            Toast.LENGTH_SHORT).show();
                    break;
            }

        });
        smileRating2.setOnRatingSelectedListener((level, reselected) ->
                Toast.makeText(Feedback.this, "Selected Rating" +level,Toast.LENGTH_SHORT).show());
        SmileRating smileRating1= (SmileRating) findViewById(R.id.smile_rating1);
        smileRating1.setOnSmileySelectionListener((smiley, reselected) -> {

            switch (smiley) {
                case SmileRating.BAD: Toast.makeText(Feedback.this, "BAD", Toast.LENGTH_SHORT).show();
                    break;
                case SmileRating.GOOD:
                    Toast.makeText(Feedback.this, "GOOD", Toast.LENGTH_SHORT).show();
                    break;
                case SmileRating.GREAT:
                    Toast.makeText(Feedback.this, "GREAT",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmileRating.OKAY:
                    Toast.makeText(Feedback.this, "OKAY", Toast.LENGTH_SHORT).show
                            ();
                    break;
                case SmileRating.TERRIBLE:
                    Toast.makeText(Feedback.this, "TERRIBLE",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        smileRating1.setOnRatingSelectedListener((level, reselected) ->
                Toast.makeText(Feedback.this, "Selected Rating" +level,Toast.LENGTH_SHORT).show());
        SmileRating smileRating= (SmileRating) findViewById(R.id.smile_rating);
        smileRating.setOnSmileySelectionListener((smiley, reselected) -> {

            switch (smiley) {
                case SmileRating.BAD: Toast.makeText(Feedback.this, "BAD", Toast.LENGTH_SHORT).show();
                    break;
                case SmileRating.GOOD:
                    Toast.makeText(Feedback.this, "GOOD", Toast.LENGTH_SHORT).show();
                    break;
                case SmileRating.GREAT:
                    Toast.makeText(Feedback.this, "GREAT",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmileRating.OKAY:
                    Toast.makeText(Feedback.this, "OKAY", Toast.LENGTH_SHORT).show
                            ();
                    break;
                case SmileRating.TERRIBLE:
                    Toast.makeText(Feedback.this, "TERRIBLE",
                            Toast.LENGTH_SHORT).show();
                    break;
        }
        });
        smileRating.setOnRatingSelectedListener((level, reselected) ->
                Toast.makeText(Feedback.this, "Selected Rating" +level,Toast.LENGTH_SHORT).show());
    }

     public void Submit(View view) {
         Toast.makeText(Feedback.this, "Thank you!!", Toast.LENGTH_SHORT).show();

     }
 }
