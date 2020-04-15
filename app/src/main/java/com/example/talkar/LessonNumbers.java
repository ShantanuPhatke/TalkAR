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
import java.util.concurrent.TimeUnit;

import android.speech.tts.TextToSpeech;


public class LessonNumbers extends AppCompatActivity {

    public String userSpokenText = "";
    public String tutorSpokenText = "";
    private ArFragment arFragment;
    private TextToSpeech textToSpeech;
    Button replay, next;
    int currentNumberCount, numbersCompleted;
    String currentModel;
    String[] numberModels, currentNumberOptions;

    String[] quizQuestions, quizAnswers;
    int quiz, quizCurrent, quizLength, quizFlag=0;
    public static final String sp_lesson_numbers_quiz = "NumbersQuiz";

    private static final String SHARED_PREFS = "sharedPrefs";
    public static final String sp_lesson_number = "NumbersCompleted";
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
        setContentView(R.layout.activity_lesson_numbers);

        reference = FirebaseDatabase.getInstance().getReference("lessons");

        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        numbersCompleted = sharedPreferences.getInt(sp_lesson_number, 0);
        username = sharedPreferences.getString(sp_username, "");
        quiz = sharedPreferences.getInt(sp_lesson_numbers_quiz, 0);

        numberModels = getResources().getStringArray(R.array.modelNumber_array);

        // Check if quiz for module is completed
        if (quiz == 1) {
            callDialog();
        } else {
            initLesson(numbersCompleted);
        }

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Numbers");
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
//                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak(tutorSpokenText);
            }
        });

        replay = findViewById(R.id.replay);
        replay.setOnClickListener(v ->{
            textToSpeech.setLanguage(Locale.GERMAN);
//            textToSpeech.setLanguage(new Locale("nl_NL"));
            textToSpeech.speak(tutorSpokenText, TextToSpeech.QUEUE_FLUSH,null, null);
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(v -> {
            if (quizFlag == 1) {
                initQuiz();
            } else if (quizFlag == 2 || quizFlag == 3){
                speak("Quiz cannot be skipped!");
            } else {
                proceedLesson();
            }
        });

    }

    private void initLesson(int numbersCompleted) {
        if (numbersCompleted < numberModels.length){
            currentNumberCount = numbersCompleted;
            setCurrentNumberOptions(currentNumberCount);
            currentModel = numberModels[numbersCompleted]+".sfb";
            tutorSpokenText = numberModels[numbersCompleted];
        } else {
            tutorSpokenText = "Congratulation, you've completed the Numbers Lesson. Time for a small quiz! Tap on Next to proceed.";
            quizFlag = 1;
        }
    }

    private void setCurrentNumberOptions(int currentNumberCount) {
        switch (currentNumberCount){
            case 0:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_0);
                break;
            case 1:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_1);
                break;
            case 2:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_2);
                break;
            case 3:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_3);
                break;
            case 4:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_4);
                break;
            case 5:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_5);
                break;
            case 6:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_6);
                break;
            case 7:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_7);
                break;
            case 8:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_8);
                break;
            case 9:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_9);
                break;
            case 10:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_10);
                break;
            case 11:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_11);
                break;
            case 12:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_12);
                break;
            case 13:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_13);
                break;
            case 14:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_14);
                break;
            case 15:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_15);
                break;
            case 16:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_16);
                break;
            case 17:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_17);
                break;
            case 18:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_18);
                break;
            case 19:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_19);
                break;
            case 20:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_20);
                break;
            case 21:
                currentNumberOptions = getResources().getStringArray(R.array.answerNumber_21);
                break;
        }
    }


    private void proceedLesson() {
        if (verifySpeech()){
            if (currentNumberCount != numberModels.length-1){
                currentNumberCount += 1;
                setCurrentNumberOptions(currentNumberCount);
                currentModel = numberModels[currentNumberCount]+".sfb";
                tutorSpokenText = numberModels[currentNumberCount];
                textToSpeech.setLanguage(Locale.GERMAN);
//                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak(tutorSpokenText);
                updateSharedPrefs(currentNumberCount);
                updateDatabase(currentNumberCount);
            }
            else {
                updateSharedPrefs(currentNumberCount+1);
                updateDatabase(currentNumberCount+1);
                tutorSpokenText = "Congratulation, you've completed the Numbers Lesson. Time for a small quiz! Tap on Next to proceed.";
                textToSpeech.setLanguage(Locale.ENGLISH);
                speak(tutorSpokenText);
                quizFlag = 1;
            }
        }
        else{
//            System.out.println("QWERTY: "+userSpokenText);
            textToSpeech.setLanguage(Locale.ENGLISH);
            speak("Try again!");
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateDatabase(int currentNumberCount) {
        reference.child(username).child("numbers").setValue(currentNumberCount);
    }

    private void updateSharedPrefs(int currentNumberCount) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sp_lesson_number, currentNumberCount);
        editor.apply();
    }

    private boolean verifySpeech() {
        if (!userSpokenText.equals("")) {
            if (Arrays.asList(currentNumberOptions).contains(userSpokenText)){
                textToSpeech.setLanguage(Locale.ENGLISH);
                speak("Correct answer!");
                Toast.makeText(this, "Correct answer!", Toast.LENGTH_SHORT).show();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }
        return false;
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
                if (quizFlag == 2) {
                    proceedQuiz();
                } else {
                    proceedLesson();
                }
            }
        }
    }

    private void initQuiz() {

        quizQuestions = getResources().getStringArray(R.array.modelNumberQuiz_array);
        quizAnswers = getResources().getStringArray(R.array.answerNumberQuiz_array);
        quizCurrent = 0;
        quizLength = quizQuestions.length;
        tutorSpokenText = "What are the following numbers called in German?";
        speak(tutorSpokenText);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentModel = quizQuestions[quizCurrent]+".sfb";
        tutorSpokenText = quizQuestions[quizCurrent];
        speak(tutorSpokenText);
        quizFlag = 2;

    }

    private void proceedQuiz() {
        if (verifyQuiz()) {
            if (quizCurrent < quizLength-1) {
                quizCurrent += 1;
                currentModel = quizQuestions[quizCurrent]+".sfb";
                tutorSpokenText = quizQuestions[quizCurrent];
                textToSpeech.setLanguage(Locale.GERMAN);
//                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak(tutorSpokenText);
            } else {
                textToSpeech.setLanguage(Locale.ENGLISH);
                tutorSpokenText = "Great! You've successfully completed the quiz, congratulations!";
                speak(tutorSpokenText);
                quizFlag = 3;
                quiz = 1;

                // Update shared prefs
                SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(sp_lesson_numbers_quiz, 1);
                editor.apply();

                // Update database
                reference.child(username).child("quizNumbers").setValue(1);

                callDialog();
            }
        } else {
            textToSpeech.setLanguage(Locale.ENGLISH);
            speak("Wrong answer, try again.");
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void callDialog() {
        ModuleCompletedDialog moduleCompletedDialog = new ModuleCompletedDialog();
        moduleCompletedDialog.show(getSupportFragmentManager(), "Module completed");
    }

    private boolean verifyQuiz() {
        if (!userSpokenText.equals("")) {
            String []answerOptions = quizAnswers[quizCurrent].split("\\|");
            if (Arrays.asList(answerOptions).contains(userSpokenText)){
                textToSpeech.setLanguage(Locale.ENGLISH);
                speak("Correct answer!");
                Toast.makeText(this, "Correct answer!", Toast.LENGTH_SHORT).show();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }
        return false;
    }


    // TextToSpeech function
    public void speak(String s){
        textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null, null);
    }


}
