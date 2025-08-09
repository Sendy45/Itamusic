package com.example.itamusic;

import static com.example.itamusic.MainActivity.applyClickAnimation;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
@SuppressWarnings("FieldCanBeLocal")
public class DrumsActivity extends AppCompatActivity {

    private ImageButton btnRide, btnCrash, btnHiHats, btnHighTom, btnLowTom, btnFloorTom, btnSnare, btnBass,
            btnGoBack, btnSettingsActivity;
    private ImageView btnProfileActivity;

    private Drums drums;
    private User user = new User();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();
    private final String activityDailyMission = "Play the drums for 5 minutes";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drums);

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

        btnRide = findViewById(R.id.btnRide);
        btnCrash = findViewById(R.id.btnCrash);
        btnHiHats = findViewById(R.id.btnHiHats);
        btnHighTom = findViewById(R.id.btnHighTom);
        btnLowTom = findViewById(R.id.btnLowTom);
        btnFloorTom = findViewById(R.id.btnFloorTom);
        btnSnare = findViewById(R.id.btnSnare);
        btnBass = findViewById(R.id.btnBass);
        btnRide = findViewById(R.id.btnRide);
        btnProfileActivity = findViewById(R.id.btnProfileActivity);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnSettingsActivity = findViewById(R.id.btnSettingsActivity);

        // Apply animation for all buttons
        applyClickAnimation(btnProfileActivity);
        applyClickAnimation(btnGoBack);
        applyClickAnimation(btnSettingsActivity);

        drums = new Drums(this);

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
        btnRide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        drums.playDrumsNote("ride");
                        shakeView(view);
                        break;
                }
                return false;
            }
        });
        btnCrash.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        drums.playDrumsNote("crash");
                        shakeView(view);
                        break;
                }
                return false;
            }
        });
        btnHiHats.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        drums.playDrumsNote("hi hat");
                        shakeView(view);
                        break;
                }
                return false;
            }
        });
        btnHighTom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        drums.playDrumsNote("high tom tom");
                        pulseOnHit(view);
                        break;
                }
                return false;
            }
        });
        btnLowTom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        drums.playDrumsNote("low tom tom");
                        pulseOnHit(view);
                        break;
                }
                return false;
            }
        });
        btnFloorTom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        drums.playDrumsNote("floor tom");
                        pulseOnHit(view);
                        break;
                }
                return false;
            }
        });
        btnSnare.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        drums.playDrumsNote("snare");
                        pulseOnHit(view);
                        break;
                }
                return false;
            }
        });
        btnBass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        drums.playDrumsNote("bass");
                        pulseOnHit(view);
                        break;
                }
                return false;
            }
        });
    }
    // Shake animation for cymbals
    public void shakeView(View view) {
        float currentRotation = view.getRotation();  // Get the current rotation angle

        // Rotates right and left, making it look like it shakes
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(
                view,
                "rotation",
                currentRotation,
                currentRotation + 10f,
                currentRotation - 10f,
                currentRotation + 5f,
                currentRotation - 5f,
                currentRotation
        );

        rotateAnim.setDuration(300);
        rotateAnim.start();
    }
    // Pulse animation for the drums
    public void pulseOnHit(View view) {
        ScaleAnimation pulse = new ScaleAnimation(
                1f, 1.01f, 1f, 1.01f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        pulse.setDuration(100);
        pulse.setRepeatCount(1);
        pulse.setRepeatMode(Animation.REVERSE); // Repeats backwards
        view.startAnimation(pulse);
    }
}