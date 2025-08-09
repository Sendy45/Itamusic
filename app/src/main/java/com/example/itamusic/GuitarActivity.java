package com.example.itamusic;

import static com.example.itamusic.MainActivity.applyClickAnimation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
@SuppressWarnings("FieldCanBeLocal")
public class GuitarActivity extends AppCompatActivity {

    private ImageButton btnEm, btnAm, btnDm, btnG, btnC, btnF, btnBb, btnBdim,
            btnGoBack, btnSettingsActivity;
    private ImageView btnProfileActivity;

    private Guitar guitar;
    private User user = new User();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();
    private final String activityDailyMission = "Play the guitar for 5 minutes";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guitar);

        // SharedPreferences store userID to fetch user out of FB
        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(this);
        String userID = prefs1.getString("user_id", "error");

        // Fetch user data from Firebase
        firebaseHelper.getUserById(userID, fetchedUser -> {
            if (fetchedUser != null) {
                user = fetchedUser;
                Log.d("Activity", "User name: " + user.getUserName());

                // Check for dailyStreak, if activityDailyMission exists, starts counting down
                DailyStreak dailyStreak = user.getDailyStreak();
                if (dailyStreak.getMissionsToday().contains(activityDailyMission)) {
                    dailyStreak.startTimer(activityDailyMission, userID);
                }

                // Set profile picture
                Bitmap profilePic = User.base64ToBitmap(user.getProfilePic());
                if (profilePic!=null)
                    btnProfileActivity.setImageBitmap(profilePic);
            } else {
                user = new User();
                Log.d("Activity", "User not found");
            }
        });


        btnEm = findViewById(R.id.btnEm);
        btnAm = findViewById(R.id.btnAm);
        btnDm = findViewById(R.id.btnDm);
        btnG = findViewById(R.id.btnG);
        btnC = findViewById(R.id.btnC);
        btnF = findViewById(R.id.btnF);
        btnBb = findViewById(R.id.btnBb);
        btnBdim = findViewById(R.id.btnBdim);
        btnProfileActivity = findViewById(R.id.btnProfileActivity);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnSettingsActivity = findViewById(R.id.btnSettingsActivity);

        // Apply animation for all buttons
        applyClickAnimation(btnProfileActivity);
        applyClickAnimation(btnGoBack);
        applyClickAnimation(btnSettingsActivity);

        guitar = new Guitar(this);

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

        // Button on click play sound
        btnEm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        guitar.playGuitarChord("Em");
                        break;
                }
                return false;
            }
        });
        btnAm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        guitar.playGuitarChord("Am");
                        break;
                }
                return false;
            }
        });
        btnDm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        guitar.playGuitarChord("Dm");
                        break;
                }
                return false;
            }
        });
        btnG.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        guitar.playGuitarChord("G");
                        break;
                }
                return false;
            }
        });
        btnC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        guitar.playGuitarChord("C");
                        break;
                }
                return false;
            }
        });
        btnF.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        guitar.playGuitarChord("F");
                        break;
                }
                return false;
            }
        });
        btnBb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        guitar.playGuitarChord("Bb");
                        break;
                }
                return false;
            }
        });
        btnBdim.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        guitar.playGuitarChord("Bdim");
                        break;
                }
                return false;
            }
        });
    }
}