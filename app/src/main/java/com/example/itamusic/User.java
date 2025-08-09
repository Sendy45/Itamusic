package com.example.itamusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String name;
    private String id;
    private String userName;
    private String password;
    private Stats stats;
    private String profilePic;
    private String voiceType = ""; // Singing range (Soprano, bass, etc.)
    private ArrayList<ChatNote> chatNotes = new ArrayList<>(); // Saved chat notes
    private DailyStreak dailyStreak = new DailyStreak();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();
    // Constructor
    public User(String name, String id, String userName, String password, Stats stats, String profilePic) {
        this.name = name;
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.stats = stats;
        this.profilePic = profilePic;
    }

    // No-argument constructor required for Firebase deserialization
    public User() {
        // Empty constructor for Firebase
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
    public String getVoiceType() {return voiceType;}
    public void setVoiceType(String voiceType) {this.voiceType = voiceType;}
    public ArrayList<ChatNote> getChatNotes() {return chatNotes;}

    public void setChatNotes(ArrayList<ChatNote> chatNotes) {this.chatNotes = chatNotes;}

    public DailyStreak getDailyStreak() {return dailyStreak;}

    public void setDailyStreak(DailyStreak dailyStreak) {this.dailyStreak = dailyStreak;}
    // Convert bitmap to base64 for storage in Firebase as a String
    public static String bitmapToBase64(Bitmap bitmap) {
        // Create an output stream to hold the compressed image data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Compress the bitmap into PNG format and feed to outputStream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        byte[] byteArray = outputStream.toByteArray(); // Convert to byte array
        return Base64.encodeToString(byteArray, Base64.DEFAULT); // Encode the byte array to a Base64 string
    }

    // Convert a base64 string back into a Bitmap for displaying the image
    public static Bitmap base64ToBitmap(String base64Str) {
        // If the string is null or empty, return null (avoid error)
        if (base64Str == null || base64Str.isEmpty()) {
            return null;
        }
        try {
            byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT); // Decode the Base64 string into a byte array
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length); // Decode the byte array into Bitmap
        } catch (IllegalArgumentException e) { // Failure
            e.printStackTrace();
            return null;
        }
    }

    // Check for new day and update FB
    public void onAppOpened() {
        dailyStreak.checkNewDay();
        firebaseHelper.uploadUserDailyStreak(id, dailyStreak);
    }
    // Mark daily mission done and update FB
    public void onMissionDone(String mission) {
        dailyStreak.markMissionDone(mission);
        firebaseHelper.uploadUserDailyStreak(id, dailyStreak);
    }
    // Add to chatNotes and update FB
    public void addChatNote(ChatNote chatNote) {
        chatNotes.add(chatNote);
        firebaseHelper.uploadUserData(this);
    }
    // Remove from chatNotes on given position and update FB
    public void removeChatNote(int position) {
        chatNotes.remove(position);
        firebaseHelper.uploadUserData(this);
    }
}