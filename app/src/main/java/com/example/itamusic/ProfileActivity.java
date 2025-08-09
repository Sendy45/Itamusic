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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
@SuppressWarnings("FieldCanBeLocal")
public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvNotesStats, tvChordsStats, tvKeysStats, tvIntervalsStats, tvCurrentStreak, tvBestStreak, tvVoiceType;
    private ImageButton btnGoBack, btnSettingsActivity;
    private ImageView ivProfilePic;
    private User user = new User();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
                    ivProfilePic.setImageBitmap(profilePic);

                tvVoiceType.setText(user.getVoiceType());

                // After user is fetched, update the UI
                updateUI();
            } else {
                user = new User();
                Log.d("Activity", "User not found");
            }
        });

        tvUserName = findViewById(R.id.tvUserName);
        tvNotesStats = findViewById(R.id.tvNotesStats);
        tvChordsStats = findViewById(R.id.tvChordsStats);
        tvKeysStats = findViewById(R.id.tvKeysStats);
        tvIntervalsStats = findViewById(R.id.tvIntervalsStats);
        tvCurrentStreak = findViewById(R.id.tvCurrentStreak);
        tvBestStreak = findViewById(R.id.tvBestStreak);
        tvVoiceType = findViewById(R.id.tvVoiceType);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnSettingsActivity = findViewById(R.id.btnSettingsActivity);
        ivProfilePic = findViewById(R.id.ivProfilePic);

        // Apply animation for all buttons
        applyClickAnimation(btnGoBack);
        applyClickAnimation(btnSettingsActivity);

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

    // Method to update UI after user data is fetched
    @SuppressLint("SetTextI18n")
    private void updateUI() {
        if (user != null) {
            Stats stats = user.getStats();
            String userName = user.getUserName();

            // Display info
            tvUserName.setText(userName);

            tvNotesStats.setText("Success Rate: " + (int)stats.getNotesSuccessRate() + "%\n"+
                    "Questions Done: " + stats.getNotesQuestions());
            tvIntervalsStats.setText("Success Rate: " + (int)stats.getIntervalsSuccessRate() + "%\n"+
                    "Questions Done: " + stats.getIntervalsQuestions());
            tvChordsStats.setText("Success Rate: " + (int)stats.getChordsSuccessRate() + "%\n"+
                    "Questions Done: " + stats.getChordsQuestions());
            tvKeysStats.setText("Success Rate: " + (int)stats.getKeysSuccessRate() + "%\n"+
                    "Questions Done: " + stats.getKeysQuestions());

            tvCurrentStreak.setText("" + user.getDailyStreak().getCurrentStreak());
            tvBestStreak.setText("" + user.getDailyStreak().getBestStreak());
        }
    }
}
