package com.example.talkar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Random;

public class WordDialog extends AppCompatDialogFragment {

    String[] wordOTD_array;
    String[] wordOTD;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wordOTD_array = getResources().getStringArray(R.array.wordOTD_array);

        Random random = new Random();
        int randomNumber = random.nextInt(wordOTD_array.length);

        wordOTD = wordOTD_array[randomNumber].split("\\|");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Word of the day!")
                .setMessage(wordOTD[0]+": "+wordOTD[1])
                .setPositiveButton("Okay", (dialog, which) -> {

                });
        return  builder.create();
    }
}
