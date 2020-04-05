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

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String sp_lesson_alphabet = "AlphabetsCompleted";
    private static final String sp_lesson_number = "NumbersCompleted";
    public static final String sp_lesson_shape = "ShapesCompleted";
    public static final String sp_lesson_word = "WordsCompleted";
    public static final String sp_lesson_sentence = "SentencesCompleted";
    public static final String sp_lesson_quiz = "QuizCompleted";

    private TextView alphabetTextView, numberTextView, shapeTextView, wordTextView, sentenceTextView, quizTextView;
    private ProgressBar alphabetProgress, numberProgress, shapeProgress, wordProgress, sentenceProgress, quizProgress;

    private int totalAlphabets, totalNumbers, totalShapes, totalWords, totalSentences, totalQuiz;

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
        totalWords = getResources().getStringArray(R.array.modelWord_array).length;
        totalSentences = getResources().getStringArray(R.array.modelSentence_array).length;
        totalQuiz = getResources().getStringArray(R.array.modelQuiz_array).length;

        alphabetTextView = view.findViewById(R.id.alphabetsCompleted);
        numberTextView = view.findViewById(R.id.numbersCompleted);
        shapeTextView = view.findViewById(R.id.shapesCompleted);
        wordTextView = view.findViewById(R.id.wordsCompleted);
        sentenceTextView = view.findViewById(R.id.sentencesCompleted);
        quizTextView = view.findViewById(R.id.quizCompleted);

        alphabetProgress = view.findViewById(R.id.alphabetBar);
        numberProgress = view.findViewById(R.id.numberBar);
        shapeProgress = view.findViewById(R.id.shapeBar);
        wordProgress = view.findViewById(R.id.wordBar);
        sentenceProgress = view.findViewById(R.id.sentenceBar);
        quizProgress = view.findViewById(R.id.quizBar);

        getProgressData();

        return view;
    }

    private void getProgressData() {
        int alphabetsCompleted, numbersCompleted, shapesCompleted, wordsCompleted, sentencesCompleted, quizCompleted;

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        alphabetsCompleted = sharedPreferences.getInt(sp_lesson_alphabet, 0);
        numbersCompleted = sharedPreferences.getInt(sp_lesson_number, 0);
        shapesCompleted = sharedPreferences.getInt(sp_lesson_shape, 0);
        wordsCompleted = sharedPreferences.getInt(sp_lesson_word, 0);
        sentencesCompleted = sharedPreferences.getInt(sp_lesson_sentence, 0);
        quizCompleted = sharedPreferences.getInt(sp_lesson_quiz, 0);

        populateProgressData(alphabetsCompleted, numbersCompleted, shapesCompleted, wordsCompleted, sentencesCompleted, quizCompleted);
    }

    private void populateProgressData(int alphabetsCompleted, int numbersCompleted, int shapesCompleted, int wordsCompleted, int sentencesCompleted, int quizCompleted) {
        String outputAlphabetText = alphabetsCompleted +"/"+totalAlphabets;
        String outputNumberText = numbersCompleted +"/"+totalNumbers;
        String outputShapeText = shapesCompleted +"/"+totalShapes;
        String outputWordText = wordsCompleted +"/"+totalWords;
        String outputSentenceText = sentencesCompleted +"/"+totalSentences;
        String outputQuizText = quizCompleted +"/"+totalQuiz;

        alphabetProgress.setProgress(getProgressValue(alphabetsCompleted, totalAlphabets));
        numberProgress.setProgress(getProgressValue(numbersCompleted, totalNumbers));
        shapeProgress.setProgress(getProgressValue(shapesCompleted, totalShapes));
        wordProgress.setProgress(getProgressValue(wordsCompleted, totalWords));
        sentenceProgress.setProgress(getProgressValue(sentencesCompleted, totalSentences));
        quizProgress.setProgress(getProgressValue(quizCompleted, totalQuiz));

        alphabetTextView.setText(outputAlphabetText);
        numberTextView.setText(outputNumberText);
        shapeTextView.setText(outputShapeText);
        wordTextView.setText(outputWordText);
        sentenceTextView.setText(outputSentenceText);
        quizTextView.setText(outputQuizText);
    }

    private int getProgressValue(int completedValue, int totalValue) {
        return (int) Math.round(100*((double) completedValue / totalValue));
    }

}
