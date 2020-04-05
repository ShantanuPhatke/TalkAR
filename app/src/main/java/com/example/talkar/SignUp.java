package com.example.talkar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    TextInputLayout regFullname, regUsername, regEmail, regPhoneNo, regPassword;
    Button regBtn, regToLoginBtn;

    FirebaseDatabase rootNode;
    DatabaseReference usersReference, lessonsReference;

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        regFullname = findViewById(R.id.fullname);
        regUsername = findViewById(R.id.username);
        regEmail= findViewById(R.id.email);
        regPhoneNo = findViewById(R.id.phone_no);
        regPassword = findViewById(R.id.password);
        regBtn= findViewById(R.id.sign_up);
        regToLoginBtn= findViewById(R.id.reg_login_btn);

        regToLoginBtn.setOnClickListener(v -> super.onBackPressed());

    }

    private Boolean validateFullname() {
        String val = regFullname.getEditText().getText().toString();

        if (val.isEmpty()) {
            regFullname.setError("Field cannot be empty");
            return false;
        }
        else {
            regFullname.setError(null);
            regFullname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        }
        else if (val.length() >= 15) {
            regUsername.setError("Username too long");
            return false;
        }
        else if (!val.matches(noWhiteSpace)) {
            regUsername.setError("White spaces are not allowed");
            return false;
        }
        else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        }
        else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        }
        else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = regPhoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPhoneNo.setError("Field cannot be empty");
            return false;
        }
        else {
            regPhoneNo.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        }
        else if (!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        }
        else {
            regPassword.setError(null);
            return true;
        }
    }


    public void registerUser(View view) {

        rootNode = FirebaseDatabase.getInstance();
        usersReference = rootNode.getReference("users");
        lessonsReference = rootNode.getReference("lessons");

        if (!validateFullname() | !validateUsername() | !validateEmail() | !validatePhoneNo() | !validatePassword()) {
            return;
        }

        String name = regFullname.getEditText().getText().toString();
        String username = regUsername.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phoneNo = regPhoneNo.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();
        // Creating new user
        UserHelperClass usersHelperClass = new UserHelperClass(name, username, email, phoneNo, password);
        usersReference.child(username).setValue(usersHelperClass);


        int alphabets = 0;
        int numbers = 0;
        int shapes = 0;
        int words = 0;
        int sentences = 0;
        int quiz = 0;
        // Initializing all lesson values as zero in the database
        LessonsHelperClass lessonsHelperClass = new LessonsHelperClass(username, alphabets, numbers, shapes, words, sentences, quiz);
        lessonsReference.child(username).setValue(lessonsHelperClass);

        super.onBackPressed();
    }
}
