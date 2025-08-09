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
public class ChordLessonActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_chord_lesson);

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

        pagesText = new ArrayList<>(Arrays.asList( // Texts for each page
                //<---page1--->
                "<u>What is a Chord?</u><br>" +
                        "<b>A chord is a group of notes played together</b>, usually three or more, that create harmony.<br>" +
                        "Chords are the building blocks of harmony in music and are used to support melodies, establish tonality, and create emotion and mood.<br><br>" +
                        "<b>Intervals</b><br>" +
                        "Chords are built from intervals, which are the distances between notes:<br><br>" +
                        "• A third is a key building block (e.g., C to E is a major third).<br>" +
                        "• A fifth is another common interval (e.g., C to G is a perfect fifth).",
                //<---page2--->
                "<u>Major and Minor Chords</u><br>" +
                        "<b>A major chord</b> is made up of a root note, a major third, and a perfect fifth. It sounds bright, stable, and happy.<br>" +
                        "• Example: C major = C, E, G.<br><br>" +
                        "<b>A minor chord</b> uses a root note, a minor third, and a perfect fifth. It has a sad or emotional sound.<br>" +
                        "• Example: A minor = A, C, E.<br><br>" +
                        "• These two chord types are the most common in Western music.",
                //<---page3--->
                "<u>Diminished and Augmented Chords</u><br>" +
                        "<b>A diminished chord</b> includes a root, a minor third, and a diminished fifth.<br>" +
                        "• It sounds tense, eerie, or unstable and is often used for suspense.<br>" +
                        "• Example: B diminished = B, D, F.<br><br>" +
                        "<b>An augmented chord</b> uses a root, a major third, and an augmented fifth.<br>" +
                        "• It has a dreamy, mysterious, or dramatic sound.<br>" +
                        "• Example: C augmented = C, E, G♯.",
                //<---page4--->
                "<u>Seventh Chords</u><br>" +
                        "<b>Seventh chords</b> are built by adding a fourth note — the seventh — on top of a triad.<br><br>" +
                        "• A major seventh chord sounds smooth and jazzy (C, E, G, B).<br>" +
                        "• A dominant seventh chord adds tension (C, E, G, B♭).<br><br>" +
                        "• Seventh chords are widely used in jazz, blues, and pop to add color and complexity.",
                //<---page5--->
                "<u>Chord Inversions</u><br>" +
                        "<b>A chord inversion</b> changes which note is on the bottom.<br><br>" +
                        "• In root position, the root is the lowest note (C, E, G).<br>" +
                        "• In first inversion, the third is on the bottom (E, G, C).<br>" +
                        "• In second inversion, the fifth is lowest (G, C, E).<br><br>" +
                        "• Inversions create smoother voice leading and variety."
        ));

        pagesNotes = new ArrayList<>(Arrays.asList( // Shown notes for each page
                //<---page1--->
                new ArrayList<>(Arrays.asList(
                        new ArrayList<>(Arrays.asList("C4", "E4")), // Major third
                        new ArrayList<>(),
                        new ArrayList<>(Arrays.asList("C4", "G4")) // Perfect fifth
                )),
                //<---page2--->
                new ArrayList<>(Arrays.asList(
                        notesView.getChord(0,0).notes, // C major
                        new ArrayList<>(),
                        notesView.getChord(0,1).notes // C minor
                )),
                //<---page3--->
                new ArrayList<>(Arrays.asList(
                        new ArrayList<>(Arrays.asList("C4", "Eb4", "Gb4")), // C dim
                        new ArrayList<>(),
                        new ArrayList<>(Arrays.asList("C4", "E4", "G#4")) // C aug
                )),
                //<---page4--->
                new ArrayList<>(Arrays.asList(
                        new ArrayList<>(Arrays.asList("C4", "E4", "G4", "B4")), // C7 major
                        new ArrayList<>(),
                        new ArrayList<>(Arrays.asList("C4", "E4", "G4", "Bb4")) // C7
                )),
                //<---page5--->
                new ArrayList<>(Arrays.asList(
                        notesView.getChord(0,0).notes, // C major
                        new ArrayList<>(),
                        new ArrayList<>(Arrays.asList("E4", "G4", "C5")), // C major 1st inversion
                        new ArrayList<>(),
                        new ArrayList<>(Arrays.asList("G4", "C5", "E5")) // C major 2st inversion
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