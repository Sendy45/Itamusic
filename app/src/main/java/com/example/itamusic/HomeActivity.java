package com.example.itamusic;

import static com.example.itamusic.MainActivity.applyClickAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
@SuppressWarnings("FieldCanBeLocal")
public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageButton btnSettingsActivity;
    private ImageView btnProfileActivity;
    private User user = new User();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();
    private int currentFragmentIndex = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        btnProfileActivity = findViewById(R.id.btnProfileActivity);
        btnSettingsActivity = findViewById(R.id.btnSettingsActivity);

        // Set HomeFragment as default
        replaceFragment(new HomeFragment());

        // Apply animation for all buttons
        applyClickAnimation(btnProfileActivity);
        applyClickAnimation(btnSettingsActivity);

        // Prevent unwanted item coloring
        bottomNavigationView.setItemIconTintList(null);

        // Check navigation bar for item selection to open new fragments
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int newIndex = -1; // Keep track on index to preserve animation direction
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home) { // HomeFragment
                newIndex = 0;
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.instruments) { // InstrumentsFragment
                newIndex = 1;
                selectedFragment = new InstrumentsFragment();
            } else if (item.getItemId() == R.id.exercises) { // ExercisesFragment
                newIndex = 2;
                selectedFragment = new ExercisesFragment();
            } else if (item.getItemId() == R.id.lessons) { // LessonsFragment
                newIndex = 3;
                selectedFragment = new LessonsFragment();
            }

            if (selectedFragment != null && newIndex != -1) {
                if (newIndex > currentFragmentIndex) {
                    // Slide from right to left
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                } else if (newIndex < currentFragmentIndex) {
                    // Slide from left to right
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }
                currentFragmentIndex = newIndex; // Update index
            }

            return true; // Finished
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
    }
    // Open new fragment and use sliding animation
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,  // enter
                R.anim.slide_out_left,  // exit
                R.anim.slide_in_left,   // popEnter
                R.anim.slide_out_right  // popExit
        );
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}