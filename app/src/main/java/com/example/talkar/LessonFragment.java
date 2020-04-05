package com.example.talkar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class LessonFragment extends Fragment {

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CardView alphabets, numbers, shapes, words, sentences, quiz, dictionary;

        View view = inflater.inflate(R.layout.fragment_lesson, container, false);

        alphabets = view.findViewById(R.id.btnAlphabets);
        alphabets.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), LessonAlphabets.class);
            startActivity(intent);
        });

        numbers = view.findViewById(R.id.btnNumbers);
        numbers.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), LessonNumbers.class);
            startActivity(intent);
        });

        shapes = view.findViewById(R.id.btnShapes);
        shapes.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), LessonShapes.class);
            startActivity(intent);
        });

        words = view.findViewById(R.id.btnWords);
        words.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), LessonWords.class);
            startActivity(intent);
        });

        sentences = view.findViewById(R.id.btnSentences);
        sentences.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), LessonSentences.class);
            startActivity(intent);
        });

        quiz = view.findViewById(R.id.btnQuiz);
        quiz.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), LessonQuiz.class);
            startActivity(intent);
        });

        dictionary = view.findViewById(R.id.btnDictionary);
        dictionary.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), LessonDictionary.class);
            startActivity(intent);
        });

        return view;
    }
}
