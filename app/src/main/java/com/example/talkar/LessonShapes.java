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

public class LessonShapes extends AppCompatActivity {

    public String userSpokenText = "";
    public String tutorSpokenText = "";
    private ArFragment arFragment;
    private TextToSpeech textToSpeech;
    Button replay, next;
    int currentShapeCount, shapesCompleted;
    String currentModel;
    String[] shapeModels, currentShapeOptions, shape;

    String[] quizQuestions, quizAnswers;
    int quiz, quizCurrent, quizLength, quizFlag=0;
    public static final String sp_lesson_shape_quiz = "ShapesQuiz";

    private static final String SHARED_PREFS = "sharedPrefs";
    public static final String sp_lesson_shape = "ShapesCompleted";
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
        setContentView(R.layout.activity_lesson_shapes);

        reference = FirebaseDatabase.getInstance().getReference("lessons");

        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        shapesCompleted = sharedPreferences.getInt(sp_lesson_shape, 0);
        username = sharedPreferences.getString(sp_username, "");
        quiz = sharedPreferences.getInt(sp_lesson_shape_quiz, 0);

        shapeModels = getResources().getStringArray(R.array.modelShape_array);
        shape = getResources().getStringArray(R.array.shape_array);

        // Check if quiz for module is completed
        if (quiz == 1) {
            callDialog();
        } else {
            initLesson(shapesCompleted);
        }

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Shapes");
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

    private void initLesson(int shapesCompleted) {
        if (shapesCompleted < shapeModels.length) {
            currentShapeCount = shapesCompleted;
            setCurrentShapeOptions(currentShapeCount);
            currentModel = shapeModels[shapesCompleted]+".sfb";
            tutorSpokenText = shape[shapesCompleted];
        } else {
            tutorSpokenText = "Congratulation, you've completed the Shapes Lesson. Time for a small quiz! Tap on Next to proceed.";
            quizFlag = 1;
        }
    }

    private void setCurrentShapeOptions(int currentShapeCount) {
        switch (currentShapeCount){
            case 0:
                currentShapeOptions = getResources().getStringArray(R.array.answerShape_0);
                break;
            case 1:
                currentShapeOptions = getResources().getStringArray(R.array.answerShape_1);
                break;
            case 2:
                currentShapeOptions = getResources().getStringArray(R.array.answerShape_2);
                break;
            case 3:
                currentShapeOptions = getResources().getStringArray(R.array.answerShape_3);
                break;
            case 4:
                currentShapeOptions = getResources().getStringArray(R.array.answerShape_4);
                break;
            case 5:
                currentShapeOptions = getResources().getStringArray(R.array.answerShape_5);
                break;
            case 6:
                currentShapeOptions = getResources().getStringArray(R.array.answerShape_6);
                break;
            case 7:
                currentShapeOptions = getResources().getStringArray(R.array.answerShape_7);
                break;
            case 8:
                currentShapeOptions = getResources().getStringArray(R.array.answerShape_8);
                break;
        }
    }


    private void proceedLesson() {
        if (verifySpeech()){
            if (currentShapeCount != shapeModels.length-1){
                currentShapeCount += 1;
                setCurrentShapeOptions(currentShapeCount);
                currentModel = shapeModels[currentShapeCount]+".sfb";
                tutorSpokenText = shape[currentShapeCount];
                textToSpeech.setLanguage(Locale.GERMAN);
//                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak(tutorSpokenText);
                updateSharedPrefs(currentShapeCount);
                updateDatabase(currentShapeCount);
            }
            else {
                updateSharedPrefs(currentShapeCount+1);
                updateDatabase(currentShapeCount+1);
                tutorSpokenText = "Congratulations on learning the German shapes!";
                textToSpeech.setLanguage(Locale.ENGLISH);
                speak(tutorSpokenText);
            }
        }
        else{
            textToSpeech.setLanguage(Locale.ENGLISH);
            speak("Try again!");
        }
    }

    private void updateDatabase(int currentShapeCount) {
        reference.child(username).child("shapes").setValue(currentShapeCount);
    }

    private void updateSharedPrefs(int currentShapeCount) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sp_lesson_shape, currentShapeCount);
        editor.apply();
    }

    private boolean verifySpeech() {
        if (!userSpokenText.equals("")) {
            if (Arrays.asList(currentShapeOptions).contains(userSpokenText)){
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

        quizQuestions = getResources().getStringArray(R.array.modelShapeQuiz_array);
        quizAnswers = getResources().getStringArray(R.array.answerShapeQuiz_array);
        quizCurrent = 0;
        quizLength = quizQuestions.length;
        tutorSpokenText = "What are the following shapes called in German?";
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
                editor.putInt(sp_lesson_shape_quiz, 1);
                editor.apply();

                // Update database
                reference.child(username).child("quizShapes").setValue(1);

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
