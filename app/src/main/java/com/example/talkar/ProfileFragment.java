package com.example.talkar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private TextInputLayout fullName, email, phoneNo, password;
    private TextView fullNameLabel, usernameLabel, lessonCount;

    private String _username, _fullname, _email, _phoneNo, _password;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String sp_fullName = "FullName";
    private static final String sp_username = "Username";
    private static final String sp_email = "Email";
    private static final String sp_phoneNo = "PhoneNo";
    private static final String sp_password = "Password";
    private static final String sp_isLoggedIn = "IsLoggedIn";

    private  static final String sp_lesson_alphabet = "AlphabetsCompleted";
    private  static final String sp_lesson_number = "NumbersCompleted";
    private  static final String sp_lesson_shape = "ShapesCompleted";
    private  static final String sp_lesson_color = "ColorsCompleted";
    private  static final String sp_lesson_word = "WordsCompleted";
    private  static final String sp_lesson_greeting = "GreetingsCompleted";
    private  static final String sp_lesson_sentence = "SentencesCompleted";
    private  static final String sp_lesson_quiz = "QuizCompleted";

    private DatabaseReference reference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        super.onCreate(savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference("users");


        lessonCount = view.findViewById(R.id.lessonCount);
        lessonCount.setText(String.valueOf(getLessonCount()));


        fullNameLabel = view.findViewById(R.id.full_name_field);
        usernameLabel = view.findViewById(R.id.username_field);
        email = view.findViewById(R.id.email_profile);
        phoneNo = view.findViewById(R.id.phone_no_profile);
        password = view.findViewById(R.id.password_profile);
        fullName = view.findViewById(R.id.full_name_profile);
        Button btnLogout = view.findViewById(R.id.btnLogout);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);

        btnLogout.setOnClickListener(v -> {

            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(sp_isLoggedIn, false);
            editor.apply();

            Toast.makeText(this.getActivity(), "Logged out", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity().getApplicationContext(), Login.class);
            startActivity(intent);
            getActivity().finish();

        });

        btnUpdate.setOnClickListener(v -> {

            if (isNameChanged() || isPasswordChanged() || isEmailChanged() || isPhoneNoChanged()){

                Toast.makeText(this.getActivity(), "Data has been updated", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this.getActivity(), "No change found", Toast.LENGTH_SHORT).show();
            }

        });

        getUserData();

        return view;
    }

    private int getLessonCount() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int alphabetCount = sharedPreferences.getInt(sp_lesson_alphabet, 0);
        int numberCount = sharedPreferences.getInt(sp_lesson_number, 0);
        int shapeCount = sharedPreferences.getInt(sp_lesson_shape, 0);
        int colorCount = sharedPreferences.getInt(sp_lesson_color, 0);
        int wordCount = sharedPreferences.getInt(sp_lesson_word, 0);
        int greetingCount = sharedPreferences.getInt(sp_lesson_greeting, 0);
        int sentenceCount = sharedPreferences.getInt(sp_lesson_sentence, 0);
        int quizCount = sharedPreferences.getInt(sp_lesson_quiz, 0);

        int lessonCount = alphabetCount+numberCount+shapeCount+colorCount+wordCount+greetingCount+sentenceCount+quizCount;

        return lessonCount;
    }

    private boolean isPhoneNoChanged() {
        String newPhoneNo = phoneNo.getEditText().getText().toString();
        if (!_phoneNo.equals(newPhoneNo)) {
            reference.child(_username).child("phoneNo").setValue(newPhoneNo);
            _phoneNo = newPhoneNo;
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(sp_phoneNo, newPhoneNo);
            editor.apply();
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailChanged() {
        String newEmail = email.getEditText().getText().toString();
        if (!_email.equals(newEmail)) {
            reference.child(_username).child("email").setValue(newEmail);
            _email = newEmail;
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(sp_email, newEmail);
            editor.apply();
            return true;
        } else {
            return false;
        }
    }

    private boolean isPasswordChanged() {
        String newPassword = password.getEditText().getText().toString();
        if (!_password.equals(newPassword)) {
            reference.child(_username).child("password").setValue(newPassword);
            _password = newPassword;
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(sp_password, newPassword);
            editor.apply();
            return true;
        } else {
            return false;
        }
    }

    private boolean isNameChanged() {
        String newName = fullName.getEditText().getText().toString();
        if (!_fullname.equals(newName)) {
            reference.child(_username).child("name").setValue(newName);
            _fullname = newName;
            fullNameLabel.setText(newName);
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(sp_fullName, newName);
            editor.apply();
            return true;
        } else {
            return false;
        }
    }


    private void getUserData() {

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        _fullname = sharedPreferences.getString(sp_fullName, "");
        _username = sharedPreferences.getString(sp_username, "");
        _email = sharedPreferences.getString(sp_email, "");
        _phoneNo = sharedPreferences.getString(sp_phoneNo, "");
        _password = sharedPreferences.getString(sp_password, "");

        populateUserData(_fullname, _username, _email, _phoneNo, _password);

    }

    private void populateUserData(String user_fullname, String user_username, String user_email, String user_phoneNo, String user_password) {

        fullNameLabel.setText(user_fullname);
        usernameLabel.setText(user_username);
        fullName.getEditText().setText(user_fullname);
        email.getEditText().setText(user_email);
        phoneNo.getEditText().setText(user_phoneNo);
        password.getEditText().setText(user_password);

    }

}
