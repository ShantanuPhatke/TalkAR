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

import android.speech.tts.TextToSpeech;

public class LessonSentences extends AppCompatActivity {

    public String userSpokenText = "";
    public String tutorSpokenText = "";
    private ArFragment arFragment;
    private TextToSpeech textToSpeech;
    Button replay, next;
    int currentSentenceCount, sentencesCompleted;
    String currentModel;
    String[] sentenceModels, currentSentenceOptions, sentence;
    TextView sentenceText;

    private static final String SHARED_PREFS = "sharedPrefs";
    public static final String sp_lesson_sentence = "SentencesCompleted";
    private static final String sp_username = "Username";

    private String username;

    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_sentences);

        reference = FirebaseDatabase.getInstance().getReference("lessons");

        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        sentencesCompleted = sharedPreferences.getInt(sp_lesson_sentence, 0);
        username = sharedPreferences.getString(sp_username, "");

        sentenceModels = getResources().getStringArray(R.array.modelSentence_array);
        sentence = getResources().getStringArray(R.array.sentence_array);

        sentenceText = findViewById(R.id.sentenceText);

        initLesson(sentencesCompleted);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sentences");
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

    private void initLesson(int sentencesCompleted) {
        currentSentenceCount = sentencesCompleted;
        setCurrentSentenceOptions(currentSentenceCount);
        currentModel = sentenceModels[sentencesCompleted]+".sfb";
        tutorSpokenText = sentence[sentencesCompleted];
        sentenceText.setText(sentence[sentencesCompleted]);
    }

    private void setCurrentSentenceOptions(int currentSentenceCount) {
        switch (currentSentenceCount){
            case 0:
                currentSentenceOptions = getResources().getStringArray(R.array.answerSentence_0);
                break;
            case 1:
                currentSentenceOptions = getResources().getStringArray(R.array.answerSentence_1);
                break;
            case 2:
                currentSentenceOptions = getResources().getStringArray(R.array.answerSentence_2);
                break;
            case 3:
                currentSentenceOptions = getResources().getStringArray(R.array.answerSentence_3);
                break;
        }
    }


    private void proceedLesson() {
        if (verifySpeech()){
            if (currentSentenceCount != sentenceModels.length-1){
                currentSentenceCount += 1;
                setCurrentSentenceOptions(currentSentenceCount);
                currentModel = sentenceModels[currentSentenceCount]+".sfb";
                tutorSpokenText = sentence[currentSentenceCount];
                textToSpeech.setLanguage(Locale.GERMAN);
//                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak(tutorSpokenText);
                sentenceText.setText(sentence[currentSentenceCount]);
                updateSharedPrefs(currentSentenceCount);
                updateDatabase(currentSentenceCount);
            }
            else {
                updateSharedPrefs(currentSentenceCount+1);
                updateDatabase(currentSentenceCount+1);
                tutorSpokenText = "Congratulations on learning the German sentences!";
                textToSpeech.setLanguage(Locale.ENGLISH);
                speak(tutorSpokenText);
                sentenceText.setText("Congratulations!!");
            }
        }
        else{
            textToSpeech.setLanguage(Locale.ENGLISH);
            speak("Try again!");
        }
    }

    private void updateDatabase(int currentSentenceCount) {
        reference.child(username).child("sentences").setValue(currentSentenceCount);
    }

    private void updateSharedPrefs(int currentSentenceCount) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sp_lesson_sentence, currentSentenceCount);
        editor.apply();
    }

    private boolean verifySpeech() {
        return !userSpokenText.equals("") && Arrays.asList(currentSentenceOptions).contains(userSpokenText);
//        return !userSpokenText.equals("") && userSpokenText.equals(tutorSpokenText);
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
