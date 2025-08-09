package com.example.itamusic;

// ChatNote class for combining title and body of note in one object
public class ChatNote {
    private String title;
    private String body;

    public ChatNote(String title, String body) {
        this.title = title;
        this.body = body;
    }

    // No-argument constructor required for Firebase deserialization
    public ChatNote() {
        // Empty constructor for Firebase
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}