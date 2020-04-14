package com.example.talkar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class LessonFragment extends Fragment {

    public static final String SHARED_PREFS = "sharedPrefs";
    private static final String sp_lesson_alphabet = "AlphabetsCompleted";
    private static final String sp_lesson_number = "NumbersCompleted";
    private static final String sp_lesson_shape = "ShapesCompleted";
    private static final String sp_lesson_color = "ColorsCompleted";
    private static final String sp_lesson_word = "WordsCompleted";
    private static final String sp_lesson_greeting = "GreetingCompleted";
    private static final String sp_lesson_sentence = "SentencesCompleted";
    private static final String sp_lesson_quiz = "QuizCompleted";
    private int alphabetsCompleted, numbersCompleted, shapesCompleted, colorsCompleted, wordsCompleted, greetingsCompleted, sentencesCompleted, quizCompleted;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lesson, container, false);

        CardView alphabets, numbers, shapes, colors, words, greetings, sentences, quiz, dictionary;
        Button btnFeedback;

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        alphabetsCompleted = sharedPreferences.getInt(sp_lesson_alphabet, 0);
        numbersCompleted = sharedPreferences.getInt(sp_lesson_number, 0);
        shapesCompleted = sharedPreferences.getInt(sp_lesson_shape, 0);
        colorsCompleted = sharedPreferences.getInt(sp_lesson_color, 0);
        wordsCompleted = sharedPreferences.getInt(sp_lesson_word, 0);
        greetingsCompleted = sharedPreferences.getInt(sp_lesson_greeting, 0);
        sentencesCompleted = sharedPreferences.getInt(sp_lesson_sentence, 0);
        quizCompleted = sharedPreferences.getInt(sp_lesson_quiz, 0);

        alphabets = view.findViewById(R.id.btnAlphabets);
        numbers = view.findViewById(R.id.btnNumbers);
        shapes = view.findViewById(R.id.btnShapes);
        colors = view.findViewById(R.id.btnColors);
        words = view.findViewById(R.id.btnWords);
        greetings = view.findViewById(R.id.btnGreetings);
        sentences = view.findViewById(R.id.btnSentences);
        quiz = view.findViewById(R.id.btnQuiz);

        btnFeedback = view.findViewById(R.id.btnFeedback);


        btnFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), Feedback.class);
            startActivity(intent);
        });

        alphabets.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), LessonAlphabets.class);
            startActivity(intent);
        });

        numbers.setOnClickListener(v -> {
            if (alphabetsCompleted>0){
                Intent intent = new Intent(this.getContext(), LessonNumbers.class);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Complete previous modules to begin with this", Toast.LENGTH_LONG).show();
            }
        });

        shapes.setOnClickListener(v -> {
            if (alphabetsCompleted>0 && numbersCompleted>0){
                Intent intent = new Intent(this.getContext(), LessonShapes.class);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Complete previous modules to begin with this", Toast.LENGTH_LONG).show();
            }
        });

        colors.setOnClickListener(v -> {
            if (alphabetsCompleted>0 && numbersCompleted>0 && shapesCompleted>0){
                Intent intent = new Intent(this.getContext(), LessonColors.class);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Complete previous modules to begin with this", Toast.LENGTH_LONG).show();
            }
        });
        
        words.setOnClickListener(v -> {
            if (alphabetsCompleted>0 && numbersCompleted>0 && shapesCompleted>0 && colorsCompleted>0){
                Intent intent = new Intent(this.getContext(), LessonWords.class);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Complete previous modules to begin with this", Toast.LENGTH_LONG).show();
            }
        });

        greetings.setOnClickListener(v -> {
            if (alphabetsCompleted>0 && numbersCompleted>0 && shapesCompleted>0 && colorsCompleted>0 && wordsCompleted>0){
                Intent intent = new Intent(this.getContext(), LessonGreetings.class);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Complete previous modules to begin with this", Toast.LENGTH_LONG).show();
            }
        });

        sentences.setOnClickListener(v -> {
            if (alphabetsCompleted>0 && numbersCompleted>0 && shapesCompleted>0 && colorsCompleted>0 && wordsCompleted>0 && greetingsCompleted>0){
                Intent intent = new Intent(this.getContext(), LessonSentences.class);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Complete previous modules to begin with this", Toast.LENGTH_LONG).show();
            }
        });

        quiz.setOnClickListener(v -> {
            if (alphabetsCompleted>0 && numbersCompleted>0 && shapesCompleted>0 && colorsCompleted>0 && wordsCompleted>0 && greetingsCompleted>0 && sentencesCompleted>0){
                Intent intent = new Intent(this.getContext(), LessonQuiz.class);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Complete previous modules to begin with this", Toast.LENGTH_LONG).show();
            }
        });

        dictionary = view.findViewById(R.id.btnDictionary);
        dictionary.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), LessonDictionary.class);
            startActivity(intent);
        });

        return view;
    }
}
