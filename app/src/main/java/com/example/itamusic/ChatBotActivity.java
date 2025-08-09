package com.example.itamusic;

import static com.example.itamusic.MainActivity.applyClickAnimation;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
@SuppressWarnings("FieldCanBeLocal")
public class ChatBotActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private final ArrayList<Message> chatMessages = new ArrayList<>();
    private EditText etUserInput, etTitle;
    private Button btnCreate;
    private ImageButton btnGoBack, btnSettingsActivity, btnSend, btnChatNotesActivity, btnSaveChatNote;
    private ImageView btnProfileActivity;
    private Dialog addChatNoteDialog;
    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String API_KEY = "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";
    private User user = new User();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();
    private final String activityDailyMission = "Chat with the chatbot";
    private boolean GOT_DAILY_MISSION = false; // Flag to check if there is daily mission

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

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
                    GOT_DAILY_MISSION = true;
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

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        etUserInput = findViewById(R.id.etUserInput);
        btnSend = findViewById(R.id.btnSend);
        btnChatNotesActivity = findViewById(R.id.btnChatNotesActivity);
        btnSaveChatNote = findViewById(R.id.btnSaveChatNote);
        btnProfileActivity = findViewById(R.id.btnProfileActivity);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnSettingsActivity = findViewById(R.id.btnSettingsActivity);

        // Apply animation for all buttons
        applyClickAnimation(btnChatNotesActivity);
        applyClickAnimation(btnSaveChatNote);
        applyClickAnimation(btnSend);
        applyClickAnimation(btnProfileActivity);
        applyClickAnimation(btnGoBack);
        applyClickAnimation(btnSettingsActivity);

        // Set adapter and chatRecyclerView
        chatAdapter = new ChatAdapter(this, chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Send message to the chatBot
        btnSend.setOnClickListener(v -> {
            String userMessage = etUserInput.getText().toString();

            if (!userMessage.isEmpty()) {
                addMessage(new Message(userMessage, true));
                etUserInput.setText("");
                sendMessageToAPI(userMessage);
            }

            // Check if there is chat bot daily mission, if true, mark as done
            if (GOT_DAILY_MISSION) {
                GOT_DAILY_MISSION = false; // Mission completed, doesn't have daily mission anymore
                user.onMissionDone(activityDailyMission);
            }
        });
        // start chatNotes activity
        btnChatNotesActivity.setOnClickListener(v -> {
            Intent moveChatNote = new Intent(getApplicationContext(), ChatNotesActivity.class);
            startActivity(moveChatNote);
        });
        // Save a highlighted chat note and move it to ChatNoteActivity
        btnSaveChatNote.setOnClickListener(v -> {
            String selectedText = chatAdapter.selectedText; // The highlighted text
            if (selectedText!=null) { // If null, no message was selected
                // Create dialog for adding title and confirming
                addChatNoteDialog = new Dialog(this);
                addChatNoteDialog.setContentView(R.layout.dialog_add_chat_note);
                addChatNoteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                etTitle = addChatNoteDialog.findViewById(R.id.etTitle);
                btnCreate = addChatNoteDialog.findViewById(R.id.btnCreate);

                applyClickAnimation(btnCreate);

                addChatNoteDialog.show();

                // Check for title, then create new chatNote and add to list
                btnCreate.setOnClickListener(view -> {
                    String title = etTitle.getText().toString();

                    if ((title.isEmpty())) { // Check for missing input
                        Toast.makeText(this, "You need a title to create chat note", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Save addition in FB
                        user.addChatNote(new ChatNote(title, selectedText));
                        addChatNoteDialog.dismiss();
                    }
                });
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

    // Add Message and scroll to new position
    private void addMessage(Message message) {
        runOnUiThread(() -> {
            chatMessages.add(message);
            chatAdapter.notifyItemInserted(chatMessages.size() - 1); // Prevent IndexOutOfBoundsException
            chatRecyclerView.scrollToPosition(chatMessages.size() - 1); // Scroll to new message
        });
    }

    // Send message to the AI through API
    private void sendMessageToAPI(String userMessage) {
        JSONObject json = new JSONObject();
        try {
            json.put("model", "mistralai/Mistral-7B-Instruct"); // Model name
            JSONArray messages = new JSONArray();

            // First prompt to tune the AI for music theory
            JSONObject systemMessageObj = new JSONObject();
            systemMessageObj.put("role", "system");
            systemMessageObj.put("content", "You are a music theory expert. Answer all questions related to music and music theory. When asked a question not related to music, say that its not in your area of expertise and don't answer it. Keep your answers short and to the point, without meaningless suggestions if not asked otherwise. Only answer question, don't give examples to things you can do.");
            messages.put(systemMessageObj);

            // Send user query
            JSONObject userMessageObj = new JSONObject();
            userMessageObj.put("role", "user");
            userMessageObj.put("content", userMessage);
            messages.put(userMessageObj);

            json.put("messages", messages);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request from API
        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        // Answer handling
        client.newCall(request).enqueue(new Callback() {
            @Override
            // No response case
            public void onFailure(Call call, IOException e) {
                addMessage(new Message("Chatbot: Failed to get response. Error: " + e.getMessage(), false)); // Send error as message
            }

            @Override
            // Response case
            public void onResponse(Call call, Response response) throws IOException {
                // Response was successful
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray choices = jsonResponse.getJSONArray("choices"); // Model sends a couple of answer choices
                        // Got answers from the model
                        if (choices.length() > 0) {
                            JSONObject firstChoice = choices.getJSONObject(0); // Pick the first answer
                            String botMessage = firstChoice.getJSONObject("message").getString("content");
                            addMessage(new Message(botMessage, false));
                        } else { // No answers got
                            addMessage(new Message("Chatbot: No response.", false));
                        }
                        // handle errors
                    } catch (JSONException e) {
                        e.printStackTrace();
                        addMessage(new Message("Chatbot: JSON parsing error.", false));
                    }
                } else {
                    String errorBody = response.body().string();
                    addMessage(new Message("Chatbot: Error in response. Code: " + response.code() + ", Body: " + errorBody, false));
                }
            }
        });
    }
}
// Message class to work with different types of messages, user and model
class Message {
    String text;
    boolean isUser;

    Message(String text, boolean isUser) {
        this.text = text;
        this.isUser = isUser;
    }
}
