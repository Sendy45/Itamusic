package com.example.itamusic;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
@SuppressWarnings("FieldCanBeLocal")
public class Drums extends Instrument{
    private final String name;
    private final ArrayList<Note> notesList;
    private final Note ride, crash, hiHat, highTomTom, lowTomTom, floorTom, snare, bass;

    // Constructor
    public Drums(Context context){
        name = "drums";
        ride = new Note("ride",R.raw.ride,context,-1);
        crash = new Note("crash",R.raw.crash,context,-1);
        hiHat = new Note("hi hat",R.raw.hi_hat,context,-1);
        highTomTom = new Note("high tom tom",R.raw.high_tom_tom,context,-1);
        lowTomTom = new Note("low tom tom",R.raw.low_tom_tom,context,-1);
        floorTom = new Note("floor tom",R.raw.floor_tom,context,-1);
        snare = new Note("snare",R.raw.snare,context,-1);
        bass = new Note("bass",R.raw.bass,context,-1);
        notesList = new ArrayList<>(Arrays.asList(ride, crash, hiHat, highTomTom, lowTomTom, floorTom, snare, bass));
    }

    public ArrayList<Note> getNotesList() {
        return notesList;
    }

    // Return ArrayList of notes names
    public ArrayList<String> getNotesNamesList() {

        ArrayList<String> notesNamesList = new ArrayList<>();
        for (Note note : notesList){
            notesNamesList.add(note.getName());
        }

        return notesNamesList;
    }

    // Play drums by name
    public void playDrumsNote(String noteName)
    {
        int index = getNotesNamesList().indexOf(noteName);
        Note note = notesList.get(index);
        note.play();
    }
}
