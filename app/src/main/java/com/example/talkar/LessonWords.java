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

public class LessonWords extends AppCompatActivity {

    public String userSpokenText = "";
    public String tutorSpokenText = "";
    private ArFragment arFragment;
    private TextToSpeech textToSpeech;
    Button replay, next;
    int currentWordCount, wordsCompleted;
    String currentModel;
    String[] wordModels, currentWordOptions, word;

    String[] quizQuestions, quizAnswers;
    int quiz, quizCurrent, quizLength, quizFlag=0;
    public static final String sp_lesson_word_quiz = "WordsQuiz";

    private static final String SHARED_PREFS = "sharedPrefs";
    public static final String sp_lesson_word = "WordsCompleted";
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
        setContentView(R.layout.activity_lesson_words);

        reference = FirebaseDatabase.getInstance().getReference("lessons");

        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        wordsCompleted = sharedPreferences.getInt(sp_lesson_word, 0);
        username = sharedPreferences.getString(sp_username, "");
        quiz = sharedPreferences.getInt(sp_lesson_word_quiz, 0);

        wordModels = getResources().getStringArray(R.array.modelWord_array);
        word = getResources().getStringArray(R.array.word_array);

        // Check if quiz for module is completed
        if (quiz == 1) {
            callDialog();
        } else {
            initLesson(wordsCompleted);
        }

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Words");
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

    private void initLesson(int wordsCompleted) {
        if (wordsCompleted < wordModels.length) {
                currentWordCount = wordsCompleted;
            setCurrentWordOptions(currentWordCount);
            currentModel = wordModels[wordsCompleted]+".sfb";
            tutorSpokenText = word[wordsCompleted];
        } else {
            tutorSpokenText = "Congratulation, you've completed the Words Lesson. Time for a small quiz! Tap on Next to proceed.";
            quizFlag = 1;
        }
    }

    private void setCurrentWordOptions(int currentWordCount) {
        switch (currentWordCount){
            case 0:
                currentWordOptions = getResources().getStringArray(R.array.answerWord_0);
                break;
            case 1:
                currentWordOptions = getResources().getStringArray(R.array.answerWord_1);
                break;
            case 2:
                currentWordOptions = getResources().getStringArray(R.array.answerWord_2);
                break;
            case 3:
                currentWordOptions = getResources().getStringArray(R.array.answerWord_3);
                break;
            case 4:
                currentWordOptions = getResources().getStringArray(R.array.answerWord_4);
                break;
        }
    }


    private void proceedLesson() {
        if (verifySpeech()){
            if (currentWordCount != wordModels.length-1){
                currentWordCount += 1;
                setCurrentWordOptions(currentWordCount);
                currentModel = wordModels[currentWordCount]+".sfb";
                tutorSpokenText = word[currentWordCount];
                textToSpeech.setLanguage(Locale.GERMAN);
//                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak(tutorSpokenText);
                updateSharedPrefs(currentWordCount);
                updateDatabase(currentWordCount);
            }
            else {
                updateSharedPrefs(currentWordCount+1);
                updateDatabase(currentWordCount+1);
                tutorSpokenText = "Congratulations on learning the German words!";
                textToSpeech.setLanguage(Locale.ENGLISH);
                speak(tutorSpokenText);
            }
        }
        else{
            textToSpeech.setLanguage(Locale.ENGLISH);
            speak("Try again!");
        }
    }

    private void updateDatabase(int currentWordCount) {
        reference.child(username).child("words").setValue(currentWordCount);
    }

    private void updateSharedPrefs(int currentWordCount) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sp_lesson_word, currentWordCount);
        editor.apply();
    }

    private boolean verifySpeech() {
        if (!userSpokenText.equals("")) {
            if (Arrays.asList(currentWordOptions).contains(userSpokenText)){
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
                if (quizFlag == 2) {
                    proceedQuiz();
                } else {
                    proceedLesson();
                }
            }
        }
    }

    private void initQuiz() {

        quizQuestions = getResources().getStringArray(R.array.modelWordQuiz_array);
        quizAnswers = getResources().getStringArray(R.array.answerWordQuiz_array);
        quizCurrent = 0;
        quizLength = quizQuestions.length;
        tutorSpokenText = "What are the following words called in German?";
        speak(tutorSpokenText);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentModel = quizQuestions[quizCurrent]+".sfb";
        tutorSpokenText = quizAnswers[quizCurrent];
        speak(tutorSpokenText);
        quizFlag = 2;

    }

    private void proceedQuiz() {
        if (verifyQuiz()) {
            if (quizCurrent < quizLength-1) {
                quizCurrent += 1;
                currentModel = quizQuestions[quizCurrent]+".sfb";
                tutorSpokenText = quizAnswers[quizCurrent];
//                textToSpeech.setLanguage(Locale.GERMAN);
                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak(tutorSpokenText);
            } else {
                tutorSpokenText = "Woaho! You've successfully completed the quiz, congratulations!";
                quizFlag = 3;
                quiz = 1;

                // Update shared prefs
                SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(sp_lesson_word_quiz, 1);
                editor.apply();

                // Update database
                reference.child(username).child("quizWords").setValue(1);

                callDialog();
            }
        } else {
            speak("Wrong answer, try again.");
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
