package com.example.talkar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Integer.valueOf;

public class ProgressFragment extends Fragment {

    public static final String SHARED_PREFS = "sharedPrefs";
    private static final String sp_lesson_alphabet = "AlphabetsCompleted";
    private static final String sp_lesson_number = "NumbersCompleted";
    private static final String sp_lesson_shape = "ShapesCompleted";
    private static final String sp_lesson_color = "ColorsCompleted";
    private static final String sp_lesson_word = "WordsCompleted";
    private static final String sp_lesson_greeting = "GreetingsCompleted";
    private static final String sp_lesson_sentence = "SentencesCompleted";
    private static final String sp_lesson_quiz = "QuizCompleted";

    private TextView alphabetTextView, numberTextView, shapeTextView, colorTextView, wordTextView, greetingTextView, sentenceTextView, quizTextView;
    private ProgressBar alphabetProgress, numberProgress, shapeProgress, colorProgress, wordProgress, greetingProgress, sentenceProgress, quizProgress;

    private int totalAlphabets, totalNumbers, totalShapes, totalColors, totalWords, totalGreetings, totalSentences, totalQuiz;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getProgressData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        super.onCreate(savedInstanceState);

        totalAlphabets = getResources().getStringArray(R.array.modelAlphabet_array).length;
        totalNumbers = getResources().getStringArray(R.array.modelNumber_array).length;
        totalShapes = getResources().getStringArray(R.array.modelShape_array).length;
        totalColors = getResources().getStringArray(R.array.modelColor_array).length;
        totalWords = getResources().getStringArray(R.array.modelWord_array).length;
        totalGreetings = getResources().getStringArray(R.array.modelGreeting_array).length;
        totalSentences = getResources().getStringArray(R.array.modelSentence_array).length;
        totalQuiz = getResources().getStringArray(R.array.modelQuiz_array).length;

        alphabetTextView = view.findViewById(R.id.alphabetsCompleted);
        numberTextView = view.findViewById(R.id.numbersCompleted);
        shapeTextView = view.findViewById(R.id.shapesCompleted);
        colorTextView = view.findViewById(R.id.colorsCompleted);
        wordTextView = view.findViewById(R.id.wordsCompleted);
        greetingTextView = view.findViewById(R.id.greetingsCompleted);
        sentenceTextView = view.findViewById(R.id.sentencesCompleted);
        quizTextView = view.findViewById(R.id.quizCompleted);

        alphabetProgress = view.findViewById(R.id.alphabetBar);
        numberProgress = view.findViewById(R.id.numberBar);
        shapeProgress = view.findViewById(R.id.shapeBar);
        colorProgress = view.findViewById(R.id.colorBar);
        wordProgress = view.findViewById(R.id.wordBar);
        greetingProgress = view.findViewById(R.id.greetingBar);
        sentenceProgress = view.findViewById(R.id.sentenceBar);
        quizProgress = view.findViewById(R.id.quizBar);

        getProgressData();

        return view;
    }

    private void getProgressData() {
        int alphabetsCompleted, numbersCompleted, shapesCompleted, colorsCompleted, wordsCompleted, greetingsCompleted, sentencesCompleted, quizCompleted;

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        alphabetsCompleted = sharedPreferences.getInt(sp_lesson_alphabet, 0);
        numbersCompleted = sharedPreferences.getInt(sp_lesson_number, 0);
        shapesCompleted = sharedPreferences.getInt(sp_lesson_shape, 0);
        colorsCompleted = sharedPreferences.getInt(sp_lesson_color, 0);
        wordsCompleted = sharedPreferences.getInt(sp_lesson_word, 0);
        greetingsCompleted = sharedPreferences.getInt(sp_lesson_greeting, 0);
        sentencesCompleted = sharedPreferences.getInt(sp_lesson_sentence, 0);
        quizCompleted = sharedPreferences.getInt(sp_lesson_quiz, 0);

        populateProgressData(alphabetsCompleted, numbersCompleted, shapesCompleted, colorsCompleted, wordsCompleted, greetingsCompleted, sentencesCompleted, quizCompleted);
    }

    private void populateProgressData(int alphabetsCompleted, int numbersCompleted, int shapesCompleted, int colorsCompleted, int wordsCompleted, int greetingsCompleted, int sentencesCompleted, int quizCompleted) {
        String outputAlphabetText = alphabetsCompleted +"/"+totalAlphabets;
        String outputNumberText = numbersCompleted +"/"+totalNumbers;
        String outputShapeText = shapesCompleted +"/"+totalShapes;
        String outputColorText = colorsCompleted +"/"+totalColors;
        String outputWordText = wordsCompleted +"/"+totalWords;
        String outputGreetingText = greetingsCompleted +"/"+totalGreetings;
        String outputSentenceText = sentencesCompleted +"/"+totalSentences;
        String outputQuizText = quizCompleted +"/"+totalQuiz;

        alphabetProgress.setProgress(getProgressValue(alphabetsCompleted, totalAlphabets));
        numberProgress.setProgress(getProgressValue(numbersCompleted, totalNumbers));
        shapeProgress.setProgress(getProgressValue(shapesCompleted, totalShapes));
        colorProgress.setProgress(getProgressValue(colorsCompleted, totalColors));
        wordProgress.setProgress(getProgressValue(wordsCompleted, totalWords));
        greetingProgress.setProgress(getProgressValue(greetingsCompleted, totalGreetings));
        sentenceProgress.setProgress(getProgressValue(sentencesCompleted, totalSentences));
        quizProgress.setProgress(getProgressValue(quizCompleted, totalQuiz));

        alphabetTextView.setText(outputAlphabetText);
        numberTextView.setText(outputNumberText);
        shapeTextView.setText(outputShapeText);
        colorTextView.setText(outputColorText);
        wordTextView.setText(outputWordText);
        greetingTextView.setText(outputGreetingText);
        sentenceTextView.setText(outputSentenceText);
        quizTextView.setText(outputQuizText);
    }

    private int getProgressValue(int completedValue, int totalValue) {
        return (int) Math.round(100*((double) completedValue / totalValue));
    }

}
