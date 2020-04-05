package com.example.talkar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import android.speech.tts.TextToSpeech;


public class LessonAlphabets extends AppCompatActivity {

    public String userSpokenText = "";
    public String tutorSpokenText = "";
    private ArFragment arFragment;
    private TextToSpeech textToSpeech;
    Button replay, next;
    int currentAlphabetCount, alphabetsCompleted;
    String currentModel;
    String[] alphabetModels, currentAlphabetOptions;

    private static final String SHARED_PREFS = "sharedPrefs";
    public static final String sp_lesson_alphabet = "AlphabetsCompleted";
    private static final String sp_username = "Username";

    private String username;

    private DatabaseReference reference;

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_alphabets);

        reference = FirebaseDatabase.getInstance().getReference("lessons");

        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        alphabetsCompleted = sharedPreferences.getInt(sp_lesson_alphabet, 0);
        username = sharedPreferences.getString(sp_username, "");

        alphabetModels = getResources().getStringArray(R.array.modelAlphabet_array);

        initLesson(alphabetsCompleted);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Alphabets");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // AR Environment
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            ModelRenderable.builder()
                    .setSource(this, Uri.parse(currentModel))
                    .build()
                    .thenAccept(modelRenderable -> addModelToScene(anchor, modelRenderable))
                    .exceptionally(throwable -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(throwable.getMessage())
                                .show();
                        return null;
                    });
        });


        // TextToSpeech
        textToSpeech = new TextToSpeech(this, status -> {
            if(status==TextToSpeech.SUCCESS){
//                textToSpeech.setLanguage(Locale.GERMAN);
                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak(tutorSpokenText);
            }
        });

        replay = findViewById(R.id.replay);
        replay.setOnClickListener(v ->{
//            textToSpeech.setLanguage(Locale.GERMAN);
            textToSpeech.setLanguage(new Locale("nl_NL"));
            textToSpeech.speak(tutorSpokenText, TextToSpeech.QUEUE_FLUSH,null, null);
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(v -> proceedLesson());

    }

    private void initLesson(int alphabetsCompleted) {
        currentAlphabetCount = alphabetsCompleted;
        setCurrentAlphabetOptions(currentAlphabetCount);
        currentModel = alphabetModels[alphabetsCompleted]+".sfb";
        tutorSpokenText = alphabetModels[alphabetsCompleted];
    }

    private void setCurrentAlphabetOptions(int currentAlphabetCount) {
        switch (currentAlphabetCount){
            case 0:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_0);
                break;
            case 1:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_1);
                break;
            case 2:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_2);
                break;
            case 3:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_3);
                break;
            case 4:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_4);
                break;
            case 5:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_5);
                break;
            case 6:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_6);
                break;
            case 7:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_7);
                break;
            case 8:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_8);
                break;
            case 9:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_9);
                break;
            case 10:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_10);
                break;
            case 11:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_11);
                break;
            case 12:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_12);
                break;
            case 13:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_13);
                break;
            case 14:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_14);
                break;
            case 15:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_15);
                break;
            case 16:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_16);
                break;
            case 17:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_17);
                break;
            case 18:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_18);
                break;
            case 19:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_19);
                break;
            case 20:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_20);
                break;
            case 21:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_21);
                break;
            case 22:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_22);
                break;
            case 23:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_23);
                break;
            case 24:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_24);
                break;
            case 25:
                currentAlphabetOptions = getResources().getStringArray(R.array.answerAlphabet_25);
                break;
        }
    }


    private void proceedLesson() {
        if (verifySpeech()){
//            System.out.println("VERIFIED TRUE");
//            System.out.println("ALPHABET MODELS LENGTH"+alphabetModels.length);
            if (currentAlphabetCount != alphabetModels.length-1){
                currentAlphabetCount += 1;
                setCurrentAlphabetOptions(currentAlphabetCount);
//                System.out.println("CURRENT ALPHABET COUNT: " +currentAlphabetCount);
                currentModel = alphabetModels[currentAlphabetCount]+".sfb";
                tutorSpokenText = alphabetModels[currentAlphabetCount];
//                textToSpeech.setLanguage(Locale.GERMAN);
                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak(tutorSpokenText);
                updateSharedPrefs(currentAlphabetCount);
                updateDatabase(currentAlphabetCount);
            }
            else {
                updateSharedPrefs(currentAlphabetCount+1);
                updateDatabase(currentAlphabetCount+1);
                tutorSpokenText = "Congratulations on learning the German alphabets!";
                textToSpeech.setLanguage(Locale.ENGLISH);
                speak(tutorSpokenText);
            }
        }
        else{

            System.out.println("VERIFIED FALSE ( NOT VERIFIED )");
            textToSpeech.setLanguage(Locale.ENGLISH);
            speak("Try again!");
        }

    }

    private void updateDatabase(int currentAlphabetCount) {
        reference.child(username).child("alphabets").setValue(currentAlphabetCount);
    }

    private void updateSharedPrefs(int currentAlphabetCount) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sp_lesson_alphabet, currentAlphabetCount);
        editor.apply();
    }

    private boolean verifySpeech() {

        System.out.println("CURRENT ALPHABET OPTIONS: "+ Arrays.toString(currentAlphabetOptions));
        System.out.println("USER SPOKEN TEXT: "+ userSpokenText);
        if (!userSpokenText.equals("")) {
            if (Arrays.asList(currentAlphabetOptions).contains(userSpokenText)){
//                System.out.println("CURRENT ALPHABET OPTIONS: "+ Arrays.toString(currentAlphabetOptions));
//                System.out.println("USER SPOKEN TEXT: "+ userSpokenText);
                return true;
            }
            return false;
        }
        return false;

//        return !userSpokenText.equals("") && Arrays.asList(currentAlphabetOptions).contains(String.valueOf(userSpokenText));
//        return !userSpokenText.equals("") && userSpokenText.equals(tutorSpokenText);

    }

    // AR function
    private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }


    // SpeechToText functions
    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.GERMAN);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-DE");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "nl_NL");

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your device does not support speech input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                textView.setText(result.get(0));
                userSpokenText = result.get(0).toLowerCase();
                proceedLesson();
            }
        }
    }


    // TextToSpeech function
    public void speak(String s){
        textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null, null);
    }


}
