package com.example.itamusic;

import static com.example.itamusic.MainActivity.applyClickAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import android.Manifest;

@SuppressWarnings("FieldCanBeLocal")
public class SettingsActivity extends AppCompatActivity {

    private FrameLayout btnChangePic, btnChangeUserName, btnNotifications, btnResetStats, btnDeleteAccount;
    private ImageButton btnGoBack;
    private ImageView btnProfileActivity;
    private SeekBar sbVolume;
    private EditText etNameInput, etPassword;
    private TextView tvTime;
    private Button btnAccept, btnSetAlarm, btnCancelAlarm, btnPickTime;
    private Dialog deleteAccountDialog, resetStatsDialog, changeNameDialog, notificationsDialog;
    private User user = new User();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();
    private MaterialTimePicker timePicker;
    private Calendar calendar = Calendar.getInstance();
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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

        btnChangePic = findViewById(R.id.btnChangePic);
        btnChangeUserName = findViewById(R.id.btnChangeUserName);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnResetStats = findViewById(R.id.btnResetStats);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnProfileActivity = findViewById(R.id.btnProfileActivity);
        btnGoBack = findViewById(R.id.btnGoBack);
        sbVolume = findViewById(R.id.sbVolume);

        // Apply animation for all buttons
        applyClickAnimation(btnChangePic);
        applyClickAnimation(btnChangeUserName);
        applyClickAnimation(btnNotifications);
        applyClickAnimation(btnResetStats);
        applyClickAnimation(btnDeleteAccount);
        applyClickAnimation(btnProfileActivity);
        applyClickAnimation(btnGoBack);

        // Get the AudioManager system service to control audio settings
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // Get max volume level
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // Get current volume level

        sbVolume.setMax(maxVolume); // Set max for range (0 - maxVolume)
        sbVolume.setProgress(currentVolume); // Set current progression

        createNotificationChannel();

        // Check seekBar for volume change
        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                // Change the volume accordingly to seekBar progress
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        });
        // Open dialog to change profile pic
        btnChangePic.setOnClickListener(v -> {
            Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePic, 314); // Start camera activityForResult
        });
        // Open dialog to change user name
        btnChangeUserName.setOnClickListener(v -> changeUserName());
        // Open dialog to manage notifications
        btnNotifications.setOnClickListener(v -> manageNotifications());
        // Open dialog to reset user stats
        btnResetStats.setOnClickListener(v -> resetStats());
        // Open dialog to delete current user
        btnDeleteAccount.setOnClickListener(view -> deleteAccount());
        // Start profile activity
        btnProfileActivity.setOnClickListener(v -> {
            Intent moveProfile = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(moveProfile);
        });
        // Return to previous activity
        btnGoBack.setOnClickListener(v -> finish());
    }
    // Create notification channel for creating and handling notifications
    private void createNotificationChannel() {
        // Check if the device is running Android 8.0 (Oreo) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "itamusicReminderChannel"; // Channel name
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH; //(HIGH = sound, heads-up alert)

            NotificationChannel channel = new NotificationChannel("itamusicandorid", name, importance); // Create notification channel

            channel.setDescription(description); // Set description

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // If the device is running Android 13 (TIRAMISU) or higher, check for notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // If the POST_NOTIFICATIONS permission is not granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request the POST_NOTIFICATIONS permission from the user
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 321); // 321 = request code
            }
        }
    }
    // Show a time picker to select time for the alarm
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void showTimePicker() {

        // Create time picker
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12) // Default
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        timePicker.show(getSupportFragmentManager(), "itamusicandorid");

        // Get time picked
        timePicker.addOnPositiveButtonClickListener(v -> {
            tvTime.setText(
                    String.format("%02d", timePicker.getHour())+" : "+String.format("%02d",timePicker.getMinute())
            );

            // Set calendar to time picked
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
            calendar.set(Calendar.MINUTE,timePicker.getMinute());
            calendar.set(Calendar.SECOND,0); // Seconds and Milliseconds not needed
            calendar.set(Calendar.MILLISECOND,0);
        });
    }
    // Set a repeating alarm
    private void setAlarm() {
        // Get the AlarmManager system service
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class); // Intent for AlarmReceiver

        // Wrap the intent in a PendingIntent so AlarmManager can execute it later
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Schedule the alarm to go off at the time specified
        // and repeat approximately once a day (not exact to save battery)
        alarmManager.setInexactRepeating(
                AlarmManager.RTC,                       // Use real-time clock
                calendar.getTimeInMillis(),             // Start time for the alarm
                AlarmManager.INTERVAL_DAY,              // Repeat interval (1 day)
                pendingIntent                           // The action to perform when alarm triggers
        );
    }

    // Cancel the existing alarm if it's set
    private void cancelAlarm() {
        // Create the same Intent and PendingIntent as in setAlarm()
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // If the alarmManager hasn't been initialized yet, initialize it
        if (alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        // Cancel the alarm using the same PendingIntent
        alarmManager.cancel(pendingIntent);
    }
    // Open dialog to change user name
    private void changeUserName() {
        // Initiate dialog
        changeNameDialog = new Dialog(SettingsActivity.this);
        changeNameDialog.setContentView(R.layout.dialog_change_user_name);
        changeNameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        etNameInput = changeNameDialog.findViewById(R.id.tvDisclaimer);
        etPassword = changeNameDialog.findViewById(R.id.etPassword);
        btnAccept = changeNameDialog.findViewById(R.id.btnAccept);

        // Apply animation for button
        applyClickAnimation(btnAccept);

        changeNameDialog.show();

        btnAccept.setOnClickListener(vDialog -> {
            String newName = etNameInput.getText().toString(); // Get new userName from EditText
            String password = etPassword.getText().toString(); // Get password from EditText

            if ((newName.isEmpty()) ||  (password.isEmpty()) || (!user.getPassword().equals(password))) { // Check for missing or incorrect inputs
                Toast.makeText(SettingsActivity.this, "Not all fields are filled or correct", Toast.LENGTH_SHORT).show();
            }
            else { // Change only if password is correct
                user.setUserName(newName);
                // Update FB
                firebaseHelper.uploadUserData(user);
                changeNameDialog.dismiss();
            }
        });
    }
    // Open dialog to reset user stats
    private void resetStats() {
        // Initiate dialog
        resetStatsDialog = new Dialog(SettingsActivity.this);
        resetStatsDialog.setContentView(R.layout.dialog_delete_user_data);
        resetStatsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        etPassword = resetStatsDialog.findViewById(R.id.etPassword);
        btnAccept = resetStatsDialog.findViewById(R.id.btnAccept);

        // Apply animation for button
        applyClickAnimation(btnAccept);

        resetStatsDialog.show();

        btnAccept.setOnClickListener(vDialog -> {
            String password = etPassword.getText().toString(); // Get password from EditText

            if ((password.isEmpty()) || (!user.getPassword().equals(password))) { // Check for missing or incorrect inputs
                Toast.makeText(SettingsActivity.this, "Not all fields are filled or correct", Toast.LENGTH_SHORT).show();
            }
            else { // Reset only if password is correct
                // Reset stats and update FB
                Stats newStats = new Stats();
                firebaseHelper.uploadUserStats(user.getId(), newStats);
                resetStatsDialog.dismiss();
            }
        });
    }
    // Open dialog to delete current user
    private void deleteAccount() {
        // Initiate dialog
        deleteAccountDialog = new Dialog(SettingsActivity.this);
        deleteAccountDialog.setContentView(R.layout.dialog_delete_user_data);
        deleteAccountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        etPassword = deleteAccountDialog.findViewById(R.id.etPassword);
        btnAccept = deleteAccountDialog.findViewById(R.id.btnAccept);

        // Apply animation for button
        applyClickAnimation(btnAccept);

        deleteAccountDialog.show();

        btnAccept.setOnClickListener(vDialog -> {
            String password = etPassword.getText().toString(); // Get password from EditText

            if ((password.isEmpty()) || (!user.getPassword().equals(password))) { // Check for missing or incorrect inputs
                Toast.makeText(SettingsActivity.this, "Not all fields are filled or correct", Toast.LENGTH_SHORT).show();
            }
            else { // Delete only if password is correct
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(user.getId()); // Get FB reference
                // Delete current user
                ref.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) { // Success
                        // User deleted - exit app and return to LogIn screen
                        Intent moveStart = new Intent(SettingsActivity.this, MainActivity.class);
                        moveStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear back stack
                        startActivity(moveStart);
                        Log.d("Activity", "User deleted successfully");
                    } else { // Failure
                        Log.d("Activity", "Failed to delete");
                    }
                });

                deleteAccountDialog.dismiss();
            }
        });
    }
    // Open dialog to manage notifications
    private void manageNotifications() {
        // Initiate dialog
        notificationsDialog = new Dialog(SettingsActivity.this);
        notificationsDialog.setContentView(R.layout.dialog_add_notification);
        notificationsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvTime = notificationsDialog.findViewById(R.id.tvTime);
        btnPickTime = notificationsDialog.findViewById(R.id.btnPickTime);
        btnSetAlarm = notificationsDialog.findViewById(R.id.btnSetAlarm);
        btnCancelAlarm = notificationsDialog.findViewById(R.id.btnCancelAlarm);

        // Apply animation for all buttons
        applyClickAnimation(btnPickTime);
        applyClickAnimation(btnSetAlarm);
        applyClickAnimation(btnCancelAlarm);

        notificationsDialog.show();

        // Show timePicker
        btnPickTime.setOnClickListener(vDialog -> showTimePicker());
        // Set alarm
        btnSetAlarm.setOnClickListener(vDialog -> {
            setAlarm();

            notificationsDialog.dismiss();
        });
        // Cancel alarm
        btnCancelAlarm.setOnClickListener(vDialog -> {
            cancelAlarm();
            notificationsDialog.dismiss();
        });
    }
    // onActivityResult for picture taking
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==314 && resultCode==RESULT_OK){ // Picture got
            if (data!=null) { // Prevent error
                // Retrieve data
                Bundle extras = data.getExtras();
                Bitmap profilePic = (Bitmap)extras.get("data");

                // Set new profile pic
                btnProfileActivity.setImageBitmap(profilePic);

                // update FB
                user.setProfilePic(User.bitmapToBase64(profilePic));
                firebaseHelper.uploadUserData(user);
            }
        }
    }
}