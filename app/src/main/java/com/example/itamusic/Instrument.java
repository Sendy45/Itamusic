package com.example.itamusic;

import java.util.ArrayList;

public abstract class Instrument {
    protected String name;
    protected ArrayList<Note> notesList;

    // Constructor
    public Instrument() {
        this.name = "";
        this.notesList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Note> getNotesList() {
        return notesList;
    }

    public void setNotesList(ArrayList<Note> notesList) {
        this.notesList = notesList;
    }

    // Return ArrayList of notes names
    public ArrayList<String> getNotesNamesList() {

        ArrayList<String> notesNamesList = new ArrayList<>();
        for (Note note : notesList){
            notesNamesList.add(note.getName());
        }

        return notesNamesList;
    }
    // Play note by name
    public void play(String noteName) {}
}
