package com.example.itamusic;

import static com.example.itamusic.MainActivity.applyClickAnimation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
@SuppressWarnings("FieldCanBeLocal")
public class ExercisesFragment extends Fragment {

    private FrameLayout btnIntervalsActivity, btnNotesActivity, btnChordsActivity, btnKeysActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_exercises, container, false);

        btnIntervalsActivity = view.findViewById(R.id.btnIntervalsActivity);
        btnNotesActivity = view.findViewById(R.id.btnNotesActivity);
        btnChordsActivity = view.findViewById(R.id.btnChordsActivity);
        btnKeysActivity = view.findViewById(R.id.btnKeysActivity);

        // Apply animation for all buttons
        applyClickAnimation(btnIntervalsActivity);
        applyClickAnimation(btnNotesActivity);
        applyClickAnimation(btnChordsActivity);
        applyClickAnimation(btnKeysActivity);

        // Start intervals exercises activity
        btnIntervalsActivity.setOnClickListener(v -> {
            Intent moveIntervalTest = new Intent(requireContext(), IntervalTestActivity.class);
            startActivity(moveIntervalTest);
        });
        // Start notes exercises activity
        btnNotesActivity.setOnClickListener(v -> {
            Intent moveNoteTest = new Intent(requireContext(), NoteTestActivity.class);
            startActivity(moveNoteTest);
        });
        // Start chords exercises activity
        btnChordsActivity.setOnClickListener(v -> {
            Intent moveChordTest = new Intent(requireContext(), ChordTestActivity.class);
            startActivity(moveChordTest);
        });
        // Start keys exercises activity
        btnKeysActivity.setOnClickListener(v -> {
            Intent moveKeyTest = new Intent(requireContext(), KeyTestActivity.class);
            startActivity(moveKeyTest);
        });

        return view;
    }
}
