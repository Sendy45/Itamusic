package com.example.itamusic;

import static com.example.itamusic.MainActivity.applyClickAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
@SuppressWarnings("FieldCanBeLocal")
public class ChatNotesActivity extends AppCompatActivity {

    private RecyclerView chatNotesRecyclerView;
    private ChatNoteAdapter chatNoteAdapter;
    private ArrayList<ChatNote> chatNotes = new ArrayList<>();
    private ImageButton btnGoBack, btnSettingsActivity, btnDeleteChatNote;
    private ImageView btnProfileActivity;
    private User user = new User();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_notes);

        // SharedPreferences store userID to fetch user out of FB
        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(this);
        String userID = prefs1.getString("user_id", "error");

        // Fetch user data from Firebase
        firebaseHelper.getUserById(userID, fetchedUser -> {
            if (fetchedUser != null) {
                user = fetchedUser;
                Log.d("Activity", "User name: " + user.getUserName());

                // Get chatNotes and displays them
                chatNotes = user.getChatNotes();
                updateRecyclerView();

                // Set profile picture
                Bitmap profilePic = User.base64ToBitmap(user.getProfilePic());
                if (profilePic!=null)
                    btnProfileActivity.setImageBitmap(profilePic);

            } else {
                user = new User();
                Log.d("Activity", "User not found");
            }
        });

        chatNotesRecyclerView = findViewById(R.id.chatNotesRecyclerView);
        btnDeleteChatNote = findViewById(R.id.btnDeleteChatNote);
        btnProfileActivity = findViewById(R.id.btnProfileActivity);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnSettingsActivity = findViewById(R.id.btnSettingsActivity);

        // Apply animation for all buttons
        applyClickAnimation(btnDeleteChatNote);
        applyClickAnimation(btnProfileActivity);
        applyClickAnimation(btnGoBack);
        applyClickAnimation(btnSettingsActivity);

        // Delete highlighted chatNote
        btnDeleteChatNote.setOnClickListener(view -> {
            int position = chatNoteAdapter.selectedPosition;
            if (position!=RecyclerView.NO_POSITION) { // If NO_POSITION, no chatNote was selected
                // Update RecyclerView and FB
                user.removeChatNote(position);
                chatNoteAdapter.notifyItemRemoved(position);
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
    // Update RecyclerView UI when something changes
    private void updateRecyclerView(){
        chatNoteAdapter = new ChatNoteAdapter(this, chatNotes);
        chatNotesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatNotesRecyclerView.setAdapter(chatNoteAdapter);
    }
}