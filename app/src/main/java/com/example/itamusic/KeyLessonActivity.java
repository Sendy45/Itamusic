package com.example.itamusic;

import static com.example.itamusic.MainActivity.applyClickAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
@SuppressWarnings("FieldCanBeLocal")
public class KeyLessonActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_key_lesson);

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
                "<u>What is a Key?</u><br>" +
                        "<b>A key in music is the group of notes that a piece of music is based on.</b><br><br>" +
                        "<b>Each key has a tonic—the main note the music feels \"centered\" on.</b><br>" +
                        "• In the key of C major, the note C is the tonic.<br><br>" +
                        "<b>The key defines which notes and chords will be used most often in the song.</b>",
                //<---page2--->
                "<u>Major and Minor Keys</u><br>" +
                        "<b>There are two main types of keys: major and minor.</b><br><br>" +
                        "• Major keys sound bright and happy.<br><br>" +
                        "• Minor keys sound more sad or emotional.<br><br>" +
                        "<b>Each key has a specific pattern of whole and half steps.</b><br>" +
                        "• C major has no sharps or flats.<br>" +
                        "• A minor is its relative minor and also has no sharps or flats.",
                //<---page3--->
                "<u>Key Signatures</u><br>" +
                        "<b>A key signature shows which notes are sharp or flat in a key.</b><br><br>" +
                        "<b>It appears at the beginning of a musical staff and saves you from writing accidentals every time.</b><br><br>" +
                        "<b>Examples:</b><br>" +
                        "• G major has 1 sharp (F♯)<br>" +
                        "• B♭ major has 2 flats (B♭, E♭)<br><br>" +
                        "<b>Learning key signatures helps you read music faster.</b>",
                //<---page4--->
                "<u>Changing Keys (Modulation)</u><br>" +
                        "<b>Music can modulate, or change keys during a song.</b><br><br>" +
                        "• This is often done to add emotion, excitement, or variety.<br>" +
                        "• Modulation usually moves to closely related keys—like going from C major to G major.<br><br>" +
                        "<b>A smooth modulation feels natural and keeps the listener engaged.</b>"
        ));

        pagesNotes = new ArrayList<>(Arrays.asList(
                //<---page1--->
                notesView.getKey(0, 0).notes, // C major
                //<---page2--->
                notesView.getKey(9,1).notes, // A minor
                //<---page3--->
                notesView.getKey(10,0).notes, // Bb major
                //<---page4--->
                notesView.getKey(7,0).notes)); // G major

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