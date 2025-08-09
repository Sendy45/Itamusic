package com.example.itamusic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
@SuppressWarnings("FieldCanBeLocal")
public class  MainActivity extends AppCompatActivity {

    private Button btnSignUp, btnAddUser, btnProfilePic;
    private ImageButton btnLogIn;
    private EditText etName, etId, etUserName, etPassword, etUserNameLogIn, etPasswordLogIn;
    private Dialog signUpDialog;
    private Bitmap profilePic;
    private ArrayList<User> users = new ArrayList<>();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogIn = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        etUserNameLogIn = findViewById(R.id.etUserNameLogIn);
        etPasswordLogIn = findViewById(R.id.etPasswordLogIn);

        // Get all users to check for user in login
        firebaseHelper.getAllUsers(fetchedUsers -> {
            if (fetchedUsers != null) {
                users = fetchedUsers;
                Log.d("Activity", "Users loaded: " + users.size());
            } else {
                users = new ArrayList<>();
                Log.d("Activity", "No users found");
            }
        });

        // Apply animation for all buttons
        applyClickAnimation(btnLogIn);
        applyClickAnimation(btnSignUp);


        // Button for signing up
        btnSignUp.setOnClickListener(v -> {
            createSignUpDialog(); // Create sign up dialog
        });

        // Button for logging in
        btnLogIn.setOnClickListener(v -> logIn());

    }
    // Create sign up dialog
    public void createSignUpDialog()
    {
        // Initiate dialog
        signUpDialog = new Dialog(MainActivity.this);
        signUpDialog.setContentView(R.layout.dialog_sign_up);
        signUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        etName = signUpDialog.findViewById(R.id.etName);
        etId = signUpDialog.findViewById(R.id.etId);
        etUserName = signUpDialog.findViewById(R.id.etUserName);
        etPassword = signUpDialog.findViewById(R.id.etPassword);
        btnProfilePic = signUpDialog.findViewById(R.id.btnProfilePic);
        btnAddUser = signUpDialog.findViewById(R.id.btnAddUser);

        // Apply animation for all buttons
        applyClickAnimation(btnAddUser);
        applyClickAnimation(btnProfilePic);

        signUpDialog.show();

        // Add profile picture
        btnProfilePic.setOnClickListener(v -> {
            Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePic, 314);
        });

        // Add user when clicked
        btnAddUser.setOnClickListener(v -> {
            // Gather all strings from EditTexts
            String name = etName.getText().toString();
            String userName = etUserName.getText().toString();
            String id = etId.getText().toString();
            String password = etPassword.getText().toString();

            if ((name.isEmpty()) || (userName.isEmpty()) || (password.isEmpty()) || (id.length()!=9) || (profilePic==null)) { // Check for missing or incorrect inputs
                Toast.makeText(MainActivity.this, "Not all fields are filled or correct", Toast.LENGTH_SHORT).show();
            } else if (isUserExistingSignUp(id, userName, password)) { // Check if user exists
                Toast.makeText(MainActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
            }
            else { // Create new user and update FB
                User newUser = new User(name, id, userName, password, new Stats(), User.bitmapToBase64(profilePic));
                firebaseHelper.uploadUserData(newUser);

                // Add newUser to users for LogIn
                users.add(newUser);

                signUpDialog.dismiss();
            }
        });
    }
    // Check if user exits, return true if does, false if doesn't
    private boolean isUserExistingLogIn(String userName, String password) {
        if (users == null || users.isEmpty()) {
            Log.e("Login", "User list is not initialized yet.");
            return false; // Doesn't exist
        }

        for (User user : users) {
            if (user.getUserName().equals(userName) &&  // Is userName correct
                    user.getPassword().equals(password)) { // Is password correct
                return true; // Exist
            }
        }
        return false; // Doesn't exist
    }

    // Check if users ID or user name and password exists, return true if does, false if doesn't
    // If user exists when signing up then the user won't be able to sign up
    public boolean isUserExistingSignUp(String id, String userName, String password)
    {
        // Go over all users and checks for match
        for(int i = 0;i < users.size();i++)
        {
            if(users.get(i).getId().equals(id))
                return true; // User with same ID exist
            if(users.get(i).getUserName().equals(userName) &&
                    users.get(i).getPassword().equals(password))
                return true; // User with same userName and password exist
        }
        return false; // User doesn't exist
    }
    // Get user name and password and return user
    // If user doesn't exists return null, won't happen
    private User getUserByLogIn(String userName, String password) {
        if (users == null || users.isEmpty()) {
            Log.e("Login", "User list is not initialized yet.");
            return null;  // Return null if the user list is empty or not initialized
        }
        // Iterate over users
        for (User user : users) {
            if (user.getUserName().equals(userName) &&
                    user.getPassword().equals(password)) {
                return user;  // Return the user if found
            }
        }

        return null;  // Return null if no matching user is found
    }
    // LogIn if userName and password match an existing user
    private void logIn() {
        String userName = etUserNameLogIn.getText().toString();
        String password = etPasswordLogIn.getText().toString();

        if ((userName.isEmpty()) || (password.isEmpty())) { // Check if one of the fields is empty
            Toast.makeText(MainActivity.this, "Not all fields are filled", Toast.LENGTH_SHORT).show();
        } else if(!isUserExistingLogIn(userName,password)){ // Check if user exists
            Toast.makeText(MainActivity.this, "User does not exists", Toast.LENGTH_SHORT).show();
        } else{ // Logging into the app
            Intent moveHome = new Intent(MainActivity.this, HomeActivity.class);

            // Save user ID with shared preferences to use firebase when needed
            SharedPreferences.Editor editor1 = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
            User user = getUserByLogIn(userName, password); // Get user
            assert user != null; // For user.getId()
            editor1.putString("user_id", user.getId());
            editor1.apply();

            startActivity(moveHome);
        }
    }
    // onActivityResult for picture taking
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==314 && resultCode==RESULT_OK){ // Picture got
            if (data != null) {
                // Retrieve data
                Bundle extras = data.getExtras();
                profilePic = (Bitmap)extras.get("data");
            }
        }
    }
    // Click animation for buttons
    // Make the button smaller when pressed, and back to normal when released
    @SuppressLint("ClickableViewAccessibility")
    public static void applyClickAnimation(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // Pressed
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: // Released
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false;
        });
    }
}