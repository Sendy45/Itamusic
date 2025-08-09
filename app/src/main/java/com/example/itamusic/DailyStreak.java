package com.example.itamusic;

import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.TimeZone;

public class DailyStreak {

    private int currentStreak;
    private int bestStreak;
    private String lastDate;
    private ArrayList<String> missionsToday;
    private ArrayList<String> completedMissionsToday;
    private int missionsDoneCount;
    private String dailyTip;

    // Default constructor, used when new user created
    public DailyStreak() {
        currentStreak = 0;
        bestStreak = 0;
        lastDate = null;
        missionsToday = new ArrayList<>();
        completedMissionsToday = new ArrayList<>();
        missionsDoneCount = 0;
        dailyTip = null;
    }
    // Customized constructor, never used
    public DailyStreak(int currentStreak, int bestStreak, String lastDate, ArrayList<String> missionsToday, ArrayList<String> completedMissionsToday, int missionsDoneCount, String dailyTip) {
        this.currentStreak = currentStreak;
        this.bestStreak = bestStreak;
        this.lastDate = lastDate;
        this.missionsToday = missionsToday;
        this.completedMissionsToday = completedMissionsToday;
        this.missionsDoneCount = missionsDoneCount;
        this.dailyTip = dailyTip;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public ArrayList<String> getMissionsToday() {
        return missionsToday;
    }

    public void setMissionsToday(ArrayList<String> missionsToday) {
        this.missionsToday = missionsToday;
    }

    public ArrayList<String> getCompletedMissionsToday() {
        return completedMissionsToday;
    }

    public void setCompletedMissionsToday(ArrayList<String> completedMissionsToday) {
        this.completedMissionsToday = completedMissionsToday;
    }

    public int getMissionsDoneCount() {
        return missionsDoneCount;
    }

    public void setMissionsDoneCount(int missionsDoneCount) {
        this.missionsDoneCount = missionsDoneCount;
    }

    public String getDailyTip() {
        return dailyTip;
    }

    public void setDailyTip(String dailyTip) {
        this.dailyTip = dailyTip;
    }

    // Check if new day arrived and update the dailyStreak accordingly
    public void checkNewDay() {
        String today = getCurrentDay();

        if (lastDate == null || !lastDate.equals(today)) { // If is new day
            if (missionsDoneCount < 3) // If didn't complete dailyTasks
                currentStreak = 0;

            // Update daily based variables
            lastDate = today;
            completedMissionsToday.clear();
            missionsToday = getRandomMissions();
            missionsDoneCount = 0;
            dailyTip = getRandomDailyTip();
        }
    }
    // Return the current date in YEAR-MONTH-DAY format
    public String getCurrentDay(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jerusalem")); // Time in Israel
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // (January = 0)
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return year + "-" + (month + 1) + "-" + day;
    }
    // Check if all missions were done, if so, update dailyStreak
    public void checkNewStreak(){
        if (missionsDoneCount == 3) {
            currentStreak++;
            if (currentStreak > bestStreak)
                bestStreak = currentStreak;
        }
    }
    // Mark mission as done and check for new streak
    public void markMissionDone(String mission) {
        if (missionsToday.contains(mission)) {
            missionsDoneCount++;
            completedMissionsToday.add(mission);
            missionsToday.remove(mission);
            checkNewStreak();
        }
    }
    // Return 3 random missions
    public ArrayList<String> getRandomMissions() {
        ArrayList<String> pool = new ArrayList<>(Arrays.asList(
                "Play the piano for 5 minutes", "Play the guitar for 5 minutes", "Play the drums for 5 minutes",
                "Do 10 scales questions", "Do 10 chords questions", "Do 10 notes questions", "Do 10 intervals questions",
                "Chat with the chatbot"
        ));
        Collections.shuffle(pool);
        return new ArrayList<>(pool.subList(0, 3));
    }
    // Return random daily tip
    public String getRandomDailyTip() {
        ArrayList<String> pool = new ArrayList<>(Arrays.asList(
                "Practice slowly — speed will come naturally.", "Always warm up before a long practice session.", "Listen actively: notice instruments, rhythms, and harmonies.",
                "Train your ear daily — try to sing intervals!", "Learn a little music theory — it makes playing easier.", "Record yourself — hearing your mistakes is powerful.",
                "Memorize small sections, not entire pieces at once.", "Keep a steady rhythm — use a metronome!", "Sing what you play — it strengthens your musical memory.",
                "Don't just practice pieces — improvise and have fun too!"
        ));
        Collections.shuffle(pool);
        return pool.get(0);
    }
    // Start countDownTimer, when finished mark given dailyMission done
    // Time in Milliseconds
    public void startTimer(int timeInMillis, String activityDailyMission, String userID) {
        final DailyStreak thisDailyStreak = this;
        new CountDownTimer(timeInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                markMissionDone(activityDailyMission);
                // Update FB
                FirebaseHelper firebaseHelper = new FirebaseHelper();
                firebaseHelper.uploadUserDailyStreak(userID, thisDailyStreak);
            }
        }.start();
    }
    // Start CountDownTimer in constant duration of 5 minutes
    public void startTimer(String activityDailyMission, String userID){
        startTimer(5*60*1000, activityDailyMission, userID);
    }
}
