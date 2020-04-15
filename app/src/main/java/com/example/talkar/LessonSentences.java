package com.example.talkar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

    String[] quizQuestionModels, quizQuestions, quizAnswers;
    int quiz, quizCurrent, quizLength, quizFlag=0;
    public static final String sp_lesson_sentence_quiz = "SentencesQuiz";

    private static final String SHARED_PREFS = "sharedPrefs";
    public static final String sp_lesson_sentence = "SentencesCompleted";
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
        setContentView(R.layout.activity_lesson_sentences);

        reference = FirebaseDatabase.getInstance().getReference("lessons");

        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        sentencesCompleted = sharedPreferences.getInt(sp_lesson_sentence, 0);
        username = sharedPreferences.getString(sp_username, "");

        sentenceModels = getResources().getStringArray(R.array.modelSentence_array);
        sentence = getResources().getStringArray(R.array.sentence_array);

        sentenceText = findViewById(R.id.sentenceText);

        quiz = sharedPreferences.getInt(sp_lesson_sentence_quiz, 0);
        // Check if quiz for module is completed
        if (quiz == 1) {
            callDialog();
        } else {
            initLesson(sentencesCompleted);
        }

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
//                textToSpeech.setLanguage(Locale.ENGLISH);
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


    private void initLesson(int sentencesCompleted) {
        if (sentencesCompleted < sentenceModels.length) {
            currentSentenceCount = sentencesCompleted;
            setCurrentSentenceOptions(currentSentenceCount);
            currentModel = sentenceModels[sentencesCompleted]+".sfb";
    //        textToSpeech.setLanguage(Locale.ENGLISH);
            tutorSpokenText = sentence[sentencesCompleted].split("\\|")[0];
            sentenceText.setText(sentence[sentencesCompleted].split("\\|")[0]);

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                textToSpeech.setLanguage(Locale.GERMAN);
                tutorSpokenText = sentence[sentencesCompleted].split("\\|")[1];
                speak(tutorSpokenText);
                sentenceText.setText(sentence[sentencesCompleted].split("\\|")[1]);
            }, 3000);
        } else {
            tutorSpokenText = "Congratulation, you've completed the Sentences Lesson. Time for a small quiz! Tap on Next to proceed.";
            quizFlag = 1;
        }

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
                tutorSpokenText = sentence[currentSentenceCount].split("\\|")[0];
                textToSpeech.setLanguage(Locale.ENGLISH);
//                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak(tutorSpokenText);
                sentenceText.setText(sentence[currentSentenceCount].split("\\|")[0]);

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    textToSpeech.setLanguage(Locale.GERMAN);
                    tutorSpokenText = sentence[currentSentenceCount].split("\\|")[1];
                    speak(tutorSpokenText);
                    sentenceText.setText(sentence[currentSentenceCount].split("\\|")[1]);
                }, 3000);

                updateSharedPrefs(currentSentenceCount);
                updateDatabase(currentSentenceCount);
            }
            else {
                updateSharedPrefs(currentSentenceCount+1);
                updateDatabase(currentSentenceCount+1);
                tutorSpokenText = "Congratulation, you've completed the Sentences Lesson. Time for a small quiz! Tap on Next to proceed.";
                textToSpeech.setLanguage(Locale.ENGLISH);
                speak(tutorSpokenText);
                sentenceText.setText("Congratulations!!");
                quizFlag = 1;
            }
        }
        else{
            textToSpeech.setLanguage(Locale.ENGLISH);
            speak("Try again!");
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
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
        if (!userSpokenText.equals("")) {
            if (Arrays.asList(currentSentenceOptions).contains(userSpokenText)){
                textToSpeech.setLanguage(Locale.ENGLISH);
                speak("Correct answer!");
                Toast.makeText(this, "Correct answer!", Toast.LENGTH_LONG).show();
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

        quizQuestionModels = getResources().getStringArray(R.array.modelSentenceQuiz_array);
        quizQuestions = getResources().getStringArray(R.array.questionSentenceQuiz_array);
        quizAnswers = getResources().getStringArray(R.array.answerSentenceQuiz_array);
        quizCurrent = 0;
        quizLength = quizQuestionModels.length;
        tutorSpokenText = "Translate the following sentences to German?";
        speak(tutorSpokenText);
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentModel = quizQuestionModels[quizCurrent]+".sfb";
        tutorSpokenText = quizQuestions[quizCurrent];
        sentenceText.setText(quizQuestions[quizCurrent]);
        speak(tutorSpokenText);
        quizFlag = 2;

    }

    private void proceedQuiz() {
        if (verifyQuiz()) {
            if (quizCurrent < quizLength-1) {
                quizCurrent += 1;
                currentModel = quizQuestionModels[quizCurrent]+".sfb";
                tutorSpokenText = quizQuestions[quizCurrent];
                sentenceText.setText(quizQuestions[quizCurrent]);
                textToSpeech.setLanguage(Locale.ENGLISH);
//                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak(tutorSpokenText);
            } else {
                tutorSpokenText = "Great! You've successfully completed the quiz, congratulations!";
                speak(tutorSpokenText);
                quizFlag = 3;
                quiz = 1;

                // Update shared prefs
                SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(sp_lesson_sentence_quiz, 1);
                editor.apply();

                // Update database
                reference.child(username).child("quizSentences").setValue(1);

                callDialog();
            }
        } else {
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
                Toast.makeText(this, "Correct answer!", Toast.LENGTH_LONG).show();
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
