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
public class LessonsFragment extends Fragment {

    private ImageButton btnChatBotActivity;
    private FrameLayout btnIntervalsActivity, btnChordsActivity, btnKeysActivity, btnNotesActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);

        btnIntervalsActivity = view.findViewById(R.id.btnIntervalsActivity);
        btnNotesActivity = view.findViewById(R.id.btnNotesActivity);
        btnChordsActivity = view.findViewById(R.id.btnChordsActivity);
        btnKeysActivity = view.findViewById(R.id.btnKeysActivity);
        btnChatBotActivity = view.findViewById(R.id.btnChatBotActivity);

        // Apply animation for all buttons
        applyClickAnimation(btnIntervalsActivity);
        applyClickAnimation(btnNotesActivity);
        applyClickAnimation(btnChordsActivity);
        applyClickAnimation(btnKeysActivity);
        applyClickAnimation(btnChatBotActivity);

        btnIntervalsActivity.setOnClickListener(v -> {
            Intent moveIntervalLesson = new Intent(requireContext(), IntervalLessonActivity.class);
            startActivity(moveIntervalLesson);
        });

        btnNotesActivity.setOnClickListener(v -> {
            Intent moveNoteLesson = new Intent(requireContext(), NoteLessonActivity.class);
            startActivity(moveNoteLesson);
        });

        btnChordsActivity.setOnClickListener(v -> {
            Intent moveChordLesson = new Intent(requireContext(), ChordLessonActivity.class);
            startActivity(moveChordLesson);
        });

        btnKeysActivity.setOnClickListener(v -> {
            Intent moveKeyLesson = new Intent(requireContext(), KeyLessonActivity.class);
            startActivity(moveKeyLesson);
        });

        btnChatBotActivity.setOnClickListener(v -> {
            Intent moveChatBot = new Intent(requireContext(), ChatBotActivity.class);
            startActivity(moveChatBot);
        });

        return view;
    }
}
