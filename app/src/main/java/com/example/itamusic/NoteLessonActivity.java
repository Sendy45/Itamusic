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
public class NoteLessonActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_note_lesson);

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
                "<u>What is a Note?</u><br>" +
                        "<b>A note is a single musical sound with a specific pitch and duration.</b> Notes are the basic units of music and are used to build melodies, harmonies, and rhythms.<br><br>" +
                        "• Each note is named with a letter from A to G, and the sequence repeats in higher and lower pitches, called octaves.<br>" +
                        "• Notes can be played on any instrument and sung by the human voice.",
                //<---page2--->
                "<u>Sharps and Flats</u><br>" +
                        "<b>There are 12 distinct notes in Western music.</b> Some of these are natural notes (like A, B, C), and some are sharp (♯) or flat (♭) versions.<br><br>" +
                        "• A sharp raises a note by one semitone (half step).<br>" +
                        "• A flat lowers a note by one semitone.<br><br>" +
                        "<b>For example:</b><br>" +
                        "• C♯ is one semitone above C<br>" +
                        "• B♭ is one semitone below B",
                //<---page3--->
                "<u>Enharmonic Notes</u><br>" +
                        "<b>Some notes can be written in more than one way.</b><br>" +
                        "• C♯ and D♭ are the same sound but written differently — these are called enharmonic equivalents.<br><br>" +
                        "<b>Which name is used depends on the musical context and key signature.</b><br><br>" +
                        "• Enharmonic notes are very important when reading and writing music accurately.",
                //<---page4--->
                "<u>Octaves and Pitch</u><br>" +
                        "<b>An octave is the distance between one note and the next note with the same name, either higher or lower.</b><br><br>" +
                        "• For example, C4 and C5 are one octave apart.<br>" +
                        "• The standard tuning note A4 has a frequency of 440 Hz.<br><br>" +
                        "• Notes repeat in octaves but sound higher or lower in pitch."
        ));

        pagesNotes = new ArrayList<>(Arrays.asList(
                //<---page1--->
                new ArrayList<>(Arrays.asList("C4")),
                //<---page2--->
                new ArrayList<>(Arrays.asList("C#4", "Bb4")),
                //<---page3--->
                new ArrayList<>(Arrays.asList("C#4", "Db4")),
                //<---page4--->
                new ArrayList<>(Arrays.asList("C4", "C5"))));

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