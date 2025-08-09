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
public class InstrumentsFragment extends Fragment {

    private FrameLayout btnPianoActivity, btnGuitarActivity, btnDrumsActivity, btnTunerActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_instruments, container, false);

        btnPianoActivity = view.findViewById(R.id.btnPianoActivity);
        btnGuitarActivity = view.findViewById(R.id.btnGuitarActivity);
        btnDrumsActivity = view.findViewById(R.id.btnDrumsActivity);
        btnTunerActivity = view.findViewById(R.id.btnTunerActivity);

        // Apply animation for all buttons
        applyClickAnimation(btnPianoActivity);
        applyClickAnimation(btnGuitarActivity);
        applyClickAnimation(btnDrumsActivity);
        applyClickAnimation(btnTunerActivity);

        // Start Piano activity
        btnPianoActivity.setOnClickListener(v -> {
            Intent movePiano = new Intent(requireContext(), PianoActivity.class);
            startActivity(movePiano);
        });
        // Start Guitar activity
        btnGuitarActivity.setOnClickListener(v -> {
            Intent moveGuitar = new Intent(requireContext(), GuitarActivity.class);
            startActivity(moveGuitar);
        });
        // Start Drums activity
        btnDrumsActivity.setOnClickListener(v -> {
            Intent moveDrums = new Intent(requireContext(), DrumsActivity.class);
            startActivity(moveDrums);
        });
        // Start Tuner activity
        btnTunerActivity.setOnClickListener(v -> {
            Intent moveTuner = new Intent(requireContext(), TunerActivity.class);
            startActivity(moveTuner);
        });

        return view;
    }
}
