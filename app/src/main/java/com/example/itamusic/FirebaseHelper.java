package com.example.itamusic;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FirebaseHelper {

    private static final String TAG = "FirebaseHelper";
    private final DatabaseReference database;

    // Constructor
    public FirebaseHelper() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getDatabase() {
        return database;
    }

    // Update user's data
    public void uploadUserData(User user) {
        database.child("users").child(user.getId()).setValue(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User data uploaded successfully"))
                .addOnFailureListener(e -> Log.w(TAG, "User data upload failed", e));
    }
    // Update user's stats
    public void uploadUserStats(String userId, Stats stats) {
        database.child("users").child(userId).child("stats").setValue(stats)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Stats uploaded successfully"))
                .addOnFailureListener(e -> Log.w(TAG, "Stats upload failed", e));
    }
    // Update user's dailyStreak
    public void uploadUserDailyStreak(String userId, DailyStreak dailyStreak) {
        database.child("users").child(userId).child("dailyStreak").setValue(dailyStreak)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DailyStreak uploaded successfully"))
                .addOnFailureListener(e -> Log.w(TAG, "DailyStreak upload failed", e));
    }
    // Interfaces to deal with the a-sync nature of FB
    public interface UserCallback {
        void onUserReceived(User user); // One user
    }

    public interface UsersCallback {
        void onUsersReceived(ArrayList<User> users); // All users
    }

    // Get specific user by his ID
    public void getUserById(String userId, UserCallback callback) {
        database.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // Success
                    callback.onUserReceived(snapshot.getValue(User.class));
                } else { // Failure
                    callback.onUserReceived(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { // Failure
                Log.e(TAG, "Failed to fetch user", error.toException());
                callback.onUserReceived(null);
            }
        });
    }

    // Get all existing users in an arrayList
    public void getAllUsers(UsersCallback callback) {
        database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> allUsers = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot dat : snapshot.getChildren()) {
                        User user = dat.getValue(User.class);
                        allUsers.add(user);
                    }
                }
                callback.onUsersReceived(allUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { // Failure
                Log.e(TAG, "Failed to fetch users", error.toException());
                callback.onUsersReceived(new ArrayList<>());
            }
        });
    }
}


