package com.example.itamusic;

import static com.example.itamusic.MainActivity.applyClickAnimation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
@SuppressWarnings("FieldCanBeLocal")
public class KeyTestActivity extends AppCompatActivity {

    private ImageButton btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnGoBack, btnSettingsActivity;
    private ImageView btnProfileActivity;

    private TextView tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4, tvScore;
    private NotesView notesView;
    private ArrayList<TextView> textViewList;
    private ArrayList<ImageButton> buttonList;
    private int successCounter = 0, questionCounter = -1; // counter - questions done right, questionCounter - questions done, for dailyTask
    private User user = new User();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();
    private final String activityDailyMission = "Do 10 scales questions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_test);

        // SharedPreferences store userID to fetch user out of FB
        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(this);
        String userID = prefs1.getString("user_id", "error");

        // Fetch user data from Firebase
        firebaseHelper.getUserById(userID, fetchedUser -> {
            if (fetchedUser != null) {
                user = fetchedUser;
                Log.d("Activity", "User name: " + user.getUserName());

                // Check for dailyStreak, if activityDailyMission exists, starts counting
                if (user.getDailyStreak().getMissionsToday().contains(activityDailyMission)) {
                    questionCounter = 10;
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

        notesView = findViewById(R.id.notesView);
        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnAnswer4 = findViewById(R.id.btnAnswer4);
        tvAnswer1 = findViewById(R.id.tvAnswer1);
        tvAnswer2 = findViewById(R.id.tvAnswer2);
        tvAnswer3 = findViewById(R.id.tvAnswer3);
        tvAnswer4 = findViewById(R.id.tvAnswer4);
        tvScore = findViewById(R.id.tvScore);
        btnProfileActivity = findViewById(R.id.btnProfileActivity);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnSettingsActivity = findViewById(R.id.btnSettingsActivity);

        // Apply animation for all buttons
        applyClickAnimation(btnAnswer1);
        applyClickAnimation(btnAnswer2);
        applyClickAnimation(btnAnswer3);
        applyClickAnimation(btnAnswer4);
        applyClickAnimation(btnProfileActivity);
        applyClickAnimation(btnGoBack);
        applyClickAnimation(btnSettingsActivity);

        // Set tv and btn lists
        textViewList = new ArrayList<>(Arrays.asList(tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4));
        buttonList = new ArrayList<>(Arrays.asList(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4));

        // Initiate first question
        nextQuestion();

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
        btnGoBack.setOnClickListener(v -> finish());
    }
    // Creates a question, displays it and waits for an answer
    public void nextQuestion() {
        // Generate a random question and answer
        ArrayList<TextView> textViewAnswerList = new ArrayList<>();
        ArrayList<ImageButton> buttonAnswerList = new ArrayList<>();
        ArrayList<String> answerList = new ArrayList<>();

        NotesStructure key = notesView.getRandomKey();
        notesView.setKey(key.notes);

        answerList.add(key.name); // Add the correct answer

        // Add the right answer button in a random place
        Random r = new Random();
        int ansNum = r.nextInt(4);
        textViewAnswerList.add(textViewList.get(ansNum));
        buttonAnswerList.add(buttonList.get(ansNum));
        textViewAnswerList.get(0).setText(key.name);

        // Add wrong answers
        for (int i = 0; i < 4; i++) {
            // Make sure to not override the right answer button
            if (i != ansNum) {
                String ansWrong = notesView.getRandomKey().name;

                // If answer in answer list, tries again
                while (answerList.contains(ansWrong))
                    ansWrong = notesView.getRandomKey().name;
                answerList.add(ansWrong);
                textViewAnswerList.add(textViewList.get(i));
                buttonAnswerList.add(buttonList.get(i));
                textViewList.get(i).setText(ansWrong);
            }
        }

        // Check for answer
        checkAnswer(buttonAnswerList);
    }

    // Checks the input, updates as needed
    public void checkAnswer(ArrayList<ImageButton> ButtonAnswerList) {
        // Make sure the list is not empty and has at least 4 buttons
        if (ButtonAnswerList.size() < 4) {
            throw new IllegalArgumentException("buttonAnswerList must contain at least 4 buttons.");
        }

        // Use AtomicInteger to track button index
        AtomicInteger index = new AtomicInteger(0);

        // Loop through each button and assign the click listener
        for (ImageButton button : ButtonAnswerList) {
            int buttonIndex = index.getAndIncrement(); // Get the current index, then increment it

            button.setOnClickListener(view -> {
                // Handle click based on button index
                boolean isCorrectAnswer = buttonIndex == 0; // First button is the correct one
                Stats stats = user.getStats();
                // Update counter and stats as needed
                if (isCorrectAnswer) { // Success
                    successCounter++;
                    tvScore.setText(String.valueOf(successCounter));
                    stats.incrementKeysQuestions(true);
                } else { // Failure
                    stats.incrementKeysQuestions(false);
                }

                // Update Firebase with the new stats
                firebaseHelper.uploadUserStats(user.getId(), stats);

                // When questionCounter = 10, counting down to 0,
                // else questionCounter = -1 and counting down to nothing
                questionCounter--;
                if (questionCounter==0){ // DailyTask done
                    user.onMissionDone(activityDailyMission);
                }

                // Move to the next question
                nextQuestion();
            });
        }
    }
}