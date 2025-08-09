package com.example.itamusic;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Arrays;

public class Interval {
    private Note note1;
    private Note note2;
    private String name;
    private int halfTones; // Distance between the notes in half tones
    private static final ArrayList<String> intervalsNames = new ArrayList<>(Arrays.asList(
            "Unison",
            "Minor Second",
            "Major Second",
            "Minor Third",
            "Major Third",
            "Perfect Fourth",
            "Tritone", // Augmented Fourth / Diminished Fifth
            "Perfect Fifth",
            "Minor Sixth",
            "Major Sixth",
            "Minor Seventh",
            "Major Seventh",
            "Octave"
    ));
    // Constructor
    public Interval(Note note1, Note note2) {
        this.note1 = note1;
        this.note2 = note2;
        // Calculates Interval length in half tones
        halfTones = Math.abs(note1.getHalfTones()- note2.getHalfTones());
        name = intervalsNames.get(halfTones%12); // index = distance in halftones
    }

    public Note getNote1() {
        return note1;
    }

    public void setNote1(Note note1) {
        this.note1 = note1;
    }

    public Note getNote2() {
        return note2;
    }

    public void setNote2(Note note2) {this.note2 = note2;}
    public int getHalfTones() {
        return halfTones;
    }

    public void setHalfTones(int halfTones) {
        this.halfTones = halfTones;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public ArrayList<String> getIntervalsNames() {return intervalsNames; }

    // Get halfTones and return matching interval name
    public static String getNameByHalfTones(int halfTones) {
        return intervalsNames.get(halfTones);
    }

    // Plays Interval by playing the two notes
    public void play() {
        note1.play();
        note2.play();
    }

    // Plays a given interval for a given length of time
    public void play(int millis)
    {
        //Create a handler
        Handler handler = new Handler();
        // Create a runnable that will be executed after the delay
        Runnable runnable = new Runnable() { @Override public void run() {
            // Code to execute after the delay
            play();
        } };
        // Post the runnable with a delay
        handler.postDelayed(runnable, millis);
    }
}
