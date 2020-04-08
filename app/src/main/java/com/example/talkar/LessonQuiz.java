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
import android.widget.TextView;
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

public class LessonQuiz extends AppCompatActivity {

    public String userSpokenText = "";
    public String tutorSpokenText = "";
    private ArFragment arFragment;
    private TextToSpeech textToSpeech;
    Button replay, next;
    int currentQuizCount, quizCompleted;
    String currentModel;
    String[] quizModels, currentQuizOptions, quiz;
    TextView quizText;

    private static final String SHARED_PREFS = "sharedPrefs";
    public static final String sp_lesson_quiz = "QuizCompleted";
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
        setContentView(R.layout.activity_lesson_quiz);

        reference = FirebaseDatabase.getInstance().getReference("lessons");

        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        quizCompleted = sharedPreferences.getInt(sp_lesson_quiz, 0);
        username = sharedPreferences.getString(sp_username, "");

        quizModels = getResources().getStringArray(R.array.modelSentence_array);
        quiz = getResources().getStringArray(R.array.quiz_array);

        quizText = findViewById(R.id.quizText);

        initLesson(quizCompleted);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quiz");
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
                textToSpeech.setLanguage(Locale.GERMAN);
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
        next.setOnClickListener(v -> proceedLesson());

    }

    private void initLesson(int quizCompleted) {
        currentQuizCount = quizCompleted;
        setCurrentSentenceOptions(currentQuizCount);
        currentModel = quizModels[quizCompleted]+".sfb";
        tutorSpokenText = quiz[quizCompleted];
        quizText.setText(quiz[quizCompleted]);
    }

    private void setCurrentSentenceOptions(int currentQuizCount) {
        switch (currentQuizCount){
            case 0:
                currentQuizOptions = getResources().getStringArray(R.array.answerQuiz_0);
                break;
            case 1:
                currentQuizOptions = getResources().getStringArray(R.array.answerQuiz_1);
                break;
            case 2:
                currentQuizOptions = getResources().getStringArray(R.array.answerQuiz_2);
                break;
            case 3:
                currentQuizOptions = getResources().getStringArray(R.array.answerQuiz_3);
                break;
            case 4:
                currentQuizOptions = getResources().getStringArray(R.array.answerQuiz_4);
                break;
        }
    }


    private void proceedLesson() {
        if (verifySpeech()){
            if (currentQuizCount != quizModels.length-1){
                currentQuizCount += 1;
                currentModel = quizModels[currentQuizCount]+".sfb";
                tutorSpokenText = quiz[currentQuizCount];
                textToSpeech.setLanguage(Locale.GERMAN);
//                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak(tutorSpokenText);
                quizText.setText(quiz[currentQuizCount]);
                updateSharedPrefs(currentQuizCount);
                updateDatabase(currentQuizCount);
            }
            else {
                updateSharedPrefs(currentQuizCount+1);
                updateDatabase(currentQuizCount+1);
                tutorSpokenText = "Congratulations on completing the Quiz!";
                textToSpeech.setLanguage(Locale.ENGLISH);
                speak(tutorSpokenText);
                quizText.setText("Congratulations!!");
            }
        }
        else{
            textToSpeech.setLanguage(Locale.ENGLISH);
            speak("Try again!");
        }
    }

    private void updateDatabase(int currentQuizCount) {
        reference.child(username).child("quiz").setValue(currentQuizCount);
    }

    private void updateSharedPrefs(int currentQuizCount) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sp_lesson_quiz, currentQuizCount);
        editor.apply();
    }

    private boolean verifySpeech() {
        if (!userSpokenText.equals("")) {
            if (Arrays.asList(currentQuizOptions).contains(userSpokenText)){
                textToSpeech.setLanguage(Locale.ENGLISH);
                speak("Correct answer!");
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

    // SpeechToText functions
    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.GERMAN);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-DE");
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "nl_NL");

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

    // AR function
    private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }

}
