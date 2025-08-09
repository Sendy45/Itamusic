package com.example.itamusic;

import static com.example.itamusic.MainActivity.applyClickAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
@SuppressWarnings("FieldCanBeLocal")
public class IntervalLessonActivity extends AppCompatActivity {

    private ImageButton btnGoBack, btnSettingsActivity;
    private ImageView btnProfileActivity;
    private NotesView notesView;
    private ViewPager2 vpPage;
    private ArrayList<String> pagesText;
    private ArrayList<Object> pagesNotes;
    private User user = new User();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_lesson);

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

        notesView = new NotesView(this);

        btnProfileActivity = findViewById(R.id.btnProfileActivity);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnSettingsActivity = findViewById(R.id.btnSettingsActivity);
        vpPage = findViewById(R.id.vpPage);

        // Apply animation for all buttons
        applyClickAnimation(btnProfileActivity);
        applyClickAnimation(btnGoBack);
        applyClickAnimation(btnSettingsActivity);

        pagesText = new ArrayList<>(Arrays.asList(
                //<---page1--->
                "<u>What is an Interval?</u><br>" +
                        "<b>An interval is the distance between two notes.</b> Intervals are essential in understanding how melodies and harmonies are built.<br><br>" +
                        "They are measured in steps or semitones and named by the number of letter names they span.<br><br>" +
                        "• For example: C to E is called a third because it spans three letter names: C-D-E.",
                //<---page2--->
                "<u>Major and Minor Intervals</u><br>" +
                        "<b>Some intervals can be major or minor.</b><br>" +
                        "• A major interval is one semitone larger than a minor interval.<br><br>" +
                        "<b>For example:</b><br>" +
                        "• C to E = Major third (4 semitones)<br>" +
                        "• C to E♭ = Minor third (3 semitones)<br><br>" +
                        "• Minor intervals often sound sad or dark.<br>" +
                        "• Major intervals sound bright and happy.",
                //<---page3--->
                "<u>Perfect Intervals</u><br>" +
                        "<b>Some intervals are called perfect because they are stable and consonant.</b><br><br>" +
                        "<b>These include:</b><br>" +
                        "• Perfect unison (same note)<br>" +
                        "• Perfect fourth (e.g., C to F)<br>" +
                        "• Perfect fifth (e.g., C to G)<br>" +
                        "• Perfect octave (e.g., C to next C)<br><br>" +
                        "• Perfect intervals are used to build strong harmonies.",
                //<---page4--->
                "<u>Augmented and Diminished</u><br>" +
                        "<b>Intervals can be made larger or smaller:</b><br><br>" +
                        "• Augmented: one semitone larger than perfect or major<br>" +
                        "• Diminished: one semitone smaller than perfect or minor<br><br>" +
                        "<b>Example:</b><br>" +
                        "• C to G = Perfect fifth<br>" +
                        "• C to G♯ = Augmented fifth<br>" +
                        "• C to G♭ = Diminished fifth<br><br>" +
                        "• These intervals add tension and color to music."
        ));

        pagesNotes = new ArrayList<>(Arrays.asList(
                //<---page1--->
                new ArrayList<>(Arrays.asList(
                        new ArrayList<>(Arrays.asList("C4", "E4")) // Third
                )),
                //<---page2--->
                new ArrayList<>(Arrays.asList(
                        new ArrayList<>(Arrays.asList("C4", "E4")), // Major third
                        new ArrayList<>(),
                        new ArrayList<>(Arrays.asList("C4", "Eb4")) // Minor third
                )),
                //<---page3--->
                new ArrayList<>(Arrays.asList(
                        new ArrayList<>(Arrays.asList("C4")), // Perfect unison
                        new ArrayList<>(Arrays.asList("C4", "F4")), // Perfect forth
                        new ArrayList<>(Arrays.asList("C4", "G4")), // Perfect fifth
                        new ArrayList<>(Arrays.asList("C4", "C5")) // Perfect octave
                )),
                //<---page4--->
                new ArrayList<>(Arrays.asList(
                        new ArrayList<>(Arrays.asList("C4", "G4")), // Perfect fifth
                        new ArrayList<>(),
                        new ArrayList<>(Arrays.asList("C4", "G#4")), // Augmented fifth
                        new ArrayList<>(),
                        new ArrayList<>(Arrays.asList("C4", "Gb4")) // Diminished fifth
                ))
        ));


        // Set the ArrayLists to the same length and without nulls
        int numOfPages = Math.min(pagesText.size(), pagesNotes.size());
        pagesText = new ArrayList<>(pagesText.subList(0, numOfPages));
        pagesNotes = new ArrayList<>(pagesNotes.subList(0, numOfPages));

        // Create adapter for ViewPage
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(pagesText, pagesNotes);

        vpPage.setAdapter(viewPagerAdapter);
        vpPage.setClipToPadding(false); // To prevent cut offs of content
        vpPage.setClipChildren(false);
        vpPage.setOffscreenPageLimit(2); // Keeps the memory of surrounding pages to improve smoothness
        vpPage.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER); // Prevent over scroll effect, for esthetics

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
}