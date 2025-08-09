package com.example.itamusic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
@SuppressWarnings("FieldCanBeLocal")
public class PianoActivity extends AppCompatActivity {

    private Button c4,d4,e4,f4,g4,a4,b4,cs4,ds4,fs4,gs4,as4,c5,d5,e5,f5,g5,cs5,ds5,fs5;
    private ImageButton btnGoBack, btnSettingsActivity;
    private ImageView btnProfileActivity;
    private User user = new User();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();
    private Piano piano;
    private final String activityDailyMission = "Play the piano for 5 minutes";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piano);

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

        // Pianos keys
        c4 = findViewById(R.id.c4);
        d4 = findViewById(R.id.d4);
        e4 = findViewById(R.id.e4);
        f4 = findViewById(R.id.f4);
        g4 = findViewById(R.id.g4);
        a4 = findViewById(R.id.a4);
        b4 = findViewById(R.id.b4);
        cs4 = findViewById(R.id.cs4);
        ds4 = findViewById(R.id.ds4);
        fs4 = findViewById(R.id.fs4);
        gs4 = findViewById(R.id.gs4);
        as4 = findViewById(R.id.as4);
        c5 = findViewById(R.id.c5);
        d5 = findViewById(R.id.d5);
        e5 = findViewById(R.id.e5);
        f5 = findViewById(R.id.f5);
        g5 = findViewById(R.id.g5);
        cs5 = findViewById(R.id.cs5);
        ds5 = findViewById(R.id.ds5);
        fs5 = findViewById(R.id.fs5);
        btnProfileActivity = findViewById(R.id.btnProfileActivity);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnSettingsActivity = findViewById(R.id.btnSettingsActivity);

        piano = new Piano(this);

        // Button on click play sound
        c4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("C4");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        d4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("D4");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        e4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("E4");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        f4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("F4");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        g4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("G4");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        a4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("A4");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        b4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("B4");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        cs4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("C#4");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        ds4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("D#4");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        fs4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("F#4");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        gs4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("G#4");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        as4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("A#4");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        c5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("C5");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        d5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("D5");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        e5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("E5");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        f5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("F5");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        g5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("G5");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        cs5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("C#5");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        ds5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("D#5");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
        });
        fs5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addPressEffect(view);
                    piano.playPianoNote("F#5");
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    releasePressEffect(view); // animate back to normal
                }
                return true; // Must return true to receive ACTION_UP
            }
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
    // Press animation, key gets smaller when pressed,
    // looks like it going down from up view - like a real piano key
    public void addPressEffect(View view) {
        view.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(50)
                .setInterpolator(new DecelerateInterpolator()) // Smoother effect
                .start();
    }
    // Release animation, key returns to full size when released
    public void releasePressEffect(View view) {
        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(100)
                .setInterpolator(new AccelerateInterpolator()) // Smoother release
                .start();
    }

}