package com.example.itamusic;

import static com.example.itamusic.MainActivity.applyClickAnimation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
@SuppressWarnings("FieldCanBeLocal")
public class HomeFragment extends Fragment {

    private ImageButton btnChatBotActivity, btnChatNotesActivity;
    private ImageView ivProfilePic;
    private TextView tvCurrentStreak, tvUserName, tvDailyTip;
    private LinearLayout dailyMissionsLayout;
    private User user = new User();
    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnChatBotActivity = view.findViewById(R.id.btnChatBotActivity);
        btnChatNotesActivity = view.findViewById(R.id.btnChatNotesActivity);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvCurrentStreak = view.findViewById(R.id.tvCurrentStreak);
        tvDailyTip = view.findViewById(R.id.tvDailyTip);
        dailyMissionsLayout = view.findViewById(R.id.dailyMissionsLayout);

        // Apply animation for all buttons
        applyClickAnimation(btnChatBotActivity);
        applyClickAnimation(btnChatNotesActivity);

        loadUserDataAndUpdateUI();

        // Open ChatBot activity
        btnChatBotActivity.setOnClickListener(v -> {
            Intent moveChatBot = new Intent(requireContext(), ChatBotActivity.class);
            startActivity(moveChatBot);
        });
        // Open Chat Notes activity
        btnChatNotesActivity.setOnClickListener(v -> {
            Intent moveChatNote = new Intent(requireContext(), ChatNotesActivity.class);
            startActivity(moveChatNote);
        });

        return view;
    }
    // Updates UI when resumed
    @Override
    public void onResume() {
        super.onResume();

        loadUserDataAndUpdateUI();
    }
    // Handle user data, UI updating and FB functions
    private void loadUserDataAndUpdateUI() {
        // SharedPreferences store userID to fetch user out of FB
        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userID = prefs1.getString("user_id", "error");

        firebaseHelper.getUserById(userID, fetchedUser -> {
            if (fetchedUser != null) {
                user = fetchedUser;
                Log.d("HomeFragment", "User name: " + user.getUserName());

                // Check for new day for dailyStreak and dailyTip
                user.onAppOpened();

                tvUserName.setText(String.valueOf(user.getUserName()));
                tvCurrentStreak.setText(String.valueOf(user.getDailyStreak().getCurrentStreak()));
                tvDailyTip.setText(String.valueOf(user.getDailyStreak().getDailyTip()));
                updateDailyMissionsLayout();

                // Set profile picture
                Bitmap profilePic = User.base64ToBitmap(user.getProfilePic());
                if (profilePic != null && ivProfilePic != null) {
                    ivProfilePic.setImageBitmap(profilePic);
                }

            } else {
                Log.d("HomeFragment", "User not found");
            }
        });
    }
    // Showing the daily missions, (dynamic, creating text and image for each mission)
    private void updateDailyMissionsLayout() {

        // Clear layout
        dailyMissionsLayout.removeAllViews();

        ArrayList<String> completedMissions = user.getDailyStreak().getCompletedMissionsToday();
        // Create allMissions list for looping
        ArrayList<String> allMissions = new ArrayList<>(user.getDailyStreak().getMissionsToday());
        allMissions.addAll(completedMissions);

        for (String mission : allMissions) {
            // Create a horizontal LinearLayout for each mission
            LinearLayout missionItem = new LinearLayout(requireContext());
            missionItem.setOrientation(LinearLayout.HORIZONTAL);
            missionItem.setPadding(0, 8, 0, 8); // spacing between items

            // Create the checkbox ImageView
            ImageView checkBox = new ImageView(requireContext());
            if (completedMissions.contains(mission)) { // Mission done
                checkBox.setImageResource(R.drawable.checked_box_ic);
            } else { // Mission undone
                checkBox.setImageResource(R.drawable.unchecked_box_ic);
            }
            LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(64, 64);
            checkBox.setLayoutParams(checkBoxParams);

            // Create the TextView
            TextView missionText = new TextView(requireContext());
            missionText.setText(mission);
            missionText.setTextColor(Color.parseColor("#FFFFFF"));
            missionText.setTextSize(18);
            missionText.setPadding(16, 0, 0, 0); // space between checkbox and text

            // Add ImageView and TextView to missionItem layout
            missionItem.addView(checkBox);
            missionItem.addView(missionText);

            // Add the missionItem to the main missionsLayout
            dailyMissionsLayout.addView(missionItem);
        }

    }

}