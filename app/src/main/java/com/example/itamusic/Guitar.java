package com.example.itamusic;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
@SuppressWarnings("FieldCanBeLocal")
public class Guitar extends Instrument{

    private final String name;
    private final ArrayList<Note> chordsList;
    private final Note EmChord, AmChord, DmChord, GChord, CChord, FChord, BbChord, BdimChord;

    // Constructor
    public Guitar(Context context){
        name = "guitar";
        EmChord = new Note("Em",R.raw.em_chord,context,-1);
        AmChord = new Note("Am",R.raw.am_chord,context,-1);
        DmChord = new Note("Dm",R.raw.dm_chord,context,-1);
        GChord = new Note("G",R.raw.g_chord,context,-1);
        CChord = new Note("C",R.raw.c_chord,context,-1);
        FChord = new Note("F",R.raw.f_chord,context,-1);
        BbChord = new Note("Bb",R.raw.bb_chord,context,-1);
        BdimChord = new Note("Bdim",R.raw.bdim_chord,context,-1);
        chordsList = new ArrayList<>(Arrays.asList(EmChord, AmChord, DmChord, GChord, CChord, FChord, BbChord, BdimChord));
    }

    public ArrayList<Note> getChordsList() {
        return chordsList;
    }

    // Return ArrayList of chords names
    public ArrayList<String> getChordsNamesList() {

        ArrayList<String> chordsNamesList = new ArrayList<>();
        for (Note note : chordsList){
            chordsNamesList.add(note.getName());
        }

        return chordsNamesList;
    }

    // Play chord by name
    public void playGuitarChord(String chordName)
    {
        int index = getChordsNamesList().indexOf(chordName);
        Note note = chordsList.get(index);
        note.play();
    }
}
