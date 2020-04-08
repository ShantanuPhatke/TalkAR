package com.example.talkar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button callSignUp, login_btn;
    ImageView image;
    TextView heading, subHeading;
    TextInputLayout username, password, email;
    int alphabetsCompleted, numbersCompleted, shapesCompleted, colorsCompleted, wordsCompleted, greetingsCompleted, sentencesCompleted, quizCompleted;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String sp_fullName = "FullName";
    public static final String sp_username = "Username";
    public static final String sp_email = "Email";
    public static final String sp_phoneNo = "PhoneNo";
    public static final String sp_password = "Password";
    public static final String sp_isLoggedIn = "IsLoggedIn";
    public static final String sp_lesson_alphabet = "AlphabetsCompleted";
    public static final String sp_lesson_number = "NumbersCompleted";
    public static final String sp_lesson_shape = "ShapesCompleted";
    public static final String sp_lesson_color = "ColorsCompleted";
    public static final String sp_lesson_word = "WordsCompleted";
    public static final String sp_lesson_greeting = "GreetingsCompleted";
    public static final String sp_lesson_sentence = "SentencesCompleted";
    public static final String sp_lesson_quiz = "QuizCompleted";

//    int[] lessonDetails = new int[2];

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        callSignUp = findViewById(R.id.signup_screen);
        image = findViewById(R.id.logo_image);
        heading = findViewById(R.id.textGreeting);
        subHeading = findViewById(R.id.textTitle);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email_profile);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login);

        callSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, SignUp.class);

            Pair[] pairs = new Pair[7];

            pairs[0] = new Pair<View, String>(image, "logo_image");
            pairs[1] = new Pair<View, String>(heading, "heading");
            pairs[2] = new Pair<View, String>(subHeading, "subheading");
            pairs[3] = new Pair<View, String>(username, "username_trans");
            pairs[4] = new Pair<View, String>(password, "password_trans");
            pairs[5] = new Pair<View, String>(login_btn, "proceedButton_trans");
            pairs[6] = new Pair<View, String>(callSignUp, "optionButton_trans");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
            startActivity(intent, options.toBundle());

        });

//            Intent intent = new Intent(getApplicationContext(), MainApp.class);
//            startActivity(intent);
//            finish();

    }


    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View view) {
        //Validate Login Info
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }


    private void isUser() {

        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)){

                        password.setError(null);
                        password.setErrorEnabled(false);

                        String nameFromDB = dataSnapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);

                        // Calls function for fetching lesson data (It calls function to save them directly in Shared Prefs)
                        getLessonDetails(usernameFromDB);

                        // Saves profile data in shared prefs
                        savedata(nameFromDB, usernameFromDB, phoneNoFromDB, emailFromDB, passwordFromDB);

                        Intent intent = new Intent(getApplicationContext(), MainApp.class);

                        startActivity(intent);
                        finish();
                    }
                    else {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                }
                else {
                    username.setError("No such user exists");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // Fetches lesson specific data from database
    private void getLessonDetails(String username) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("lessons");
        Query checkUser = reference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    alphabetsCompleted = dataSnapshot.child(username).child("alphabets").getValue(int.class);
                    numbersCompleted = dataSnapshot.child(username).child("numbers").getValue(int.class);
                    shapesCompleted = dataSnapshot.child(username).child("shapes").getValue(int.class);
                    colorsCompleted = dataSnapshot.child(username).child("colors").getValue(int.class);
                    wordsCompleted = dataSnapshot.child(username).child("words").getValue(int.class);
                    greetingsCompleted = dataSnapshot.child(username).child("greetings").getValue(int.class);
                    sentencesCompleted = dataSnapshot.child(username).child("sentences").getValue(int.class);
                    quizCompleted = dataSnapshot.child(username).child("quiz").getValue(int.class);

                    savedataLesson(alphabetsCompleted, numbersCompleted, shapesCompleted, colorsCompleted, wordsCompleted, greetingsCompleted, sentencesCompleted, quizCompleted);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // Stores lesson specific data in SharedPrefs
    private void savedataLesson(int alphabetsCompleted, int numbersCompleted, int shapesCompleted, int colorsCompleted, int wordsCompleted, int greetingsCompleted, int sentencesCompleted, int quizCompleted) {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sp_lesson_alphabet, alphabetsCompleted);
        editor.putInt(sp_lesson_number, numbersCompleted);
        editor.putInt(sp_lesson_shape, shapesCompleted);
        editor.putInt(sp_lesson_color, colorsCompleted);
        editor.putInt(sp_lesson_word, wordsCompleted);
        editor.putInt(sp_lesson_greeting, greetingsCompleted);
        editor.putInt(sp_lesson_sentence, sentencesCompleted);
        editor.putInt(sp_lesson_quiz, quizCompleted);

        editor.apply();
    }

    // Stores profile data in SharedPrefs
    public void savedata(String nameFromDB, String usernameFromDB, String phoneNoFromDB, String emailFromDB, String passwordFromDB) {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(sp_fullName, nameFromDB);
        editor.putString(sp_username, usernameFromDB);
        editor.putString(sp_email, emailFromDB);
        editor.putString(sp_phoneNo, phoneNoFromDB);
        editor.putString(sp_password, passwordFromDB);
        editor.putBoolean(sp_isLoggedIn, true);

        editor.apply();

        Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();

    }

}
