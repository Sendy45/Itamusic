package com.example.itamusic;

import static com.example.itamusic.MainActivity.applyClickAnimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;
@SuppressWarnings("FieldCanBeLocal")
public class TunerActivity extends AppCompatActivity {
    private TextView tvPitch, tvNote;
    private ImageButton btnGoBack, btnSettingsActivity, btnRecord;
    private ImageView btnProfileActivity;
    private User user = new User();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();
    private boolean RECORDING = false; // If recording for voice type test
    private final ArrayList<Float> recordedPitches = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        // Check for permissions, if not granted, send request for them
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        // SharedPreferences store userID to fetch user out of FB
        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(this);
        String userID = prefs1.getString("user_id", "error");

        // Fetch user data from Firebase
        firebaseHelper.getUserById(userID, fetchedUser -> {
            if (fetchedUser != null) {
                user = fetchedUser;
                Log.d("Activity", "User name: " + user.getUserName());

                // Set profile picture
                Bitmap profilePic = User.base64ToBitmap(user.getProfilePic());
                if (profilePic!=null)
                    btnProfileActivity.setImageBitmap(profilePic);

            } else {
                user = new User();
                Log.d("Activity", "User not found");
            }
        });


        tvPitch = findViewById(R.id.tvPitch);
        tvNote = findViewById(R.id.tvNote);
        btnRecord = findViewById(R.id.btnRecord);
        btnProfileActivity = findViewById(R.id.btnProfileActivity);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnSettingsActivity = findViewById(R.id.btnSettingsActivity);

        // Apply animation for all buttons
        applyClickAnimation(btnProfileActivity);
        applyClickAnimation(btnGoBack);
        applyClickAnimation(btnSettingsActivity);

        // Create AudioDispatcher for real time audio processing
        AudioDispatcher dispatcher =
                AudioDispatcherFactory.fromDefaultMicrophone(
                        44100, // Sample Rate (Hz) - 4410 = standard CD-quality audio
                        2048, // Buffer Size  - num of audio samples processed at once
                        0 // Buffer Overlap - 0 = no over lap
                );

        // Create the PitchDetectionHandler
        PitchDetectionHandler pdh = (res, e) -> {
            final float pitchInHz = res.getPitch();

            // Ensure UI updates are performed on the main thread
            runOnUiThread(() -> {
                if (pitchInHz > 0) {
                    if (RECORDING) // Check if recording, if so, stores pitches
                        recordedPitches.add(pitchInHz);
                    processPitch(pitchInHz);
                } else { // No pitch = Reset TextViews
                    tvNote.setText("");
                    tvPitch.setText("0");
                }
            });
        };

        // Create the PitchProcessor
        AudioProcessor pitchProcessor = new PitchProcessor(
                PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, // Pitch Detection Algorithm - FFT = accurate and known algorithm
                44100, // Sample Rate (Hz) - 4410 = standard CD-quality audio
                2048, // Buffer Size  - num of audio samples processed at once
                pdh
        );
        dispatcher.addAudioProcessor(pitchProcessor);

        // Start the audio thread
        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();

        // Record only when pressed down
        btnRecord.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // Pressed
                    recordedPitches.clear();
                    RECORDING = true; // Start recording
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start(); // Pressed animation
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: // Released
                    RECORDING = false; // Finish recording
                    processRecording(); // Process recorded pitches
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start(); // Release animation
                    break;
            }
            return false;
        });

        // Start profile activity
        btnProfileActivity.setOnClickListener(v -> {
            Intent moveProfile = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(moveProfile);
        });

        // Start settings activity
        btnSettingsActivity.setOnClickListener(v -> {
            Intent moveSettings = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(moveSettings);
        });

        // Return to previous activity
        btnGoBack.setOnClickListener(v -> {
            finish();
        });
    }
    // Process given pitch, find matching note and displaying results
    public void processPitch(float pitchInHz) {
        // A reference frequency for A4 (440 Hz)
        double referenceFrequency = 440.0;

        // Calculate the pitch number using the formula for equal temperament
        double pitchNumber = Math.round(12 * Math.log(pitchInHz / referenceFrequency) / Math.log(2)) + 69;


        // Calculate the note index (0: C, 1: C#, 2: D, ..., 11: B)
        String[] notes = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        int noteIndex = (int) (pitchNumber % 12);  // Ensure the note index wraps around correctly

        // Handle pitch numbers across octaves
        int octave = (int) (pitchNumber / 12) - 1;
        String note = notes[noteIndex] + octave;

        // Convert pitch to int for clean display
        int pitchInHzInt = (int)pitchInHz;

        // Update UI with the detected note
        tvNote.setText(note);
        tvPitch.setText(String.valueOf(pitchInHzInt));
    }
    // Find voice type based on recording and update FB
    private void processRecording() {
        if (!recordedPitches.isEmpty()) {
            float maxPitch = Collections.max(recordedPitches);
            float minPitch = Collections.min(recordedPitches);

            user.setVoiceType(getVoiceType(minPitch, maxPitch));
            firebaseHelper.uploadUserData(user);
        }
    }
    // Return voice type based on min and max pitches
    String getVoiceType(float minPitch, float maxPitch) {
        if (minPitch >= 262 && maxPitch <= 1046) {
            return "Soprano";
        } else if (minPitch >= 175 && maxPitch <= 784) {
            return "Alto";
        } else if (minPitch >= 116 && maxPitch <= 523) {
            return "Tenor";
        } else if (minPitch >= 98 && maxPitch <= 415) {
            return "Baritone";
        } else if (minPitch >= 55 && maxPitch <= 294) {
            return "Bass";
        } else {
            return "Unknown or Mixed Range";
        }
    }
}