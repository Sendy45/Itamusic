package com.example.itamusic;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Arrays;

public class Piano extends Instrument{
    private final String name;
    private final ArrayList<Note> notesList;
    private final Note cNote,dNote,eNote,fNote,gNote,aNote,bNote,csNote,dsNote,fsNote,gsNote,asNote,ccNote,ddNote,eeNote,ffNote,ggNote,ccsNote,ddsNote,ffsNote;

    // Constructor
    public Piano(Context context){
        name = "piano";
        cNote = new Note("C4",R.raw.c4,context,0);
        csNote = new Note("C#4",R.raw.c_s4,context,1);
        dNote = new Note("D4",R.raw.d4,context,2);
        dsNote = new Note("D#4",R.raw.d_s4,context,3);
        eNote = new Note("E4",R.raw.e4,context,4);
        fNote = new Note("F4",R.raw.f4,context,5);
        fsNote = new Note("F#4",R.raw.f_s4,context,6);
        gNote = new Note("G4",R.raw.g4,context,7);
        gsNote = new Note("G#4",R.raw.g_s4,context,8);
        aNote = new Note("A4",R.raw.a4,context,9);
        asNote = new Note("A#4",R.raw.a_s4,context,10);
        bNote = new Note("B4",R.raw.b4,context,11);
        ccNote = new Note("C5",R.raw.c5,context,12);
        ccsNote = new Note("C#5",R.raw.c_s5,context,13);
        ddNote = new Note("D5",R.raw.d5,context,14);
        ddsNote = new Note("D#5",R.raw.d_s5,context,15);
        eeNote = new Note("E5",R.raw.e5,context,16);
        ffNote = new Note("F5",R.raw.f5,context,17);
        ffsNote = new Note("F#5",R.raw.f_s5,context,18);
        ggNote = new Note("G5",R.raw.g5,context,19);
        notesList = new ArrayList<>(Arrays.asList(cNote,csNote,dNote,dsNote,eNote,fNote,fsNote,gNote,gsNote,aNote,asNote,bNote,ccNote,ccsNote,ddNote,ddsNote,eeNote,ffNote,ffsNote,ggNote));
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

    // Play key by name
    public void playPianoNote(String noteName)
    {
        int index = getNotesNamesList().indexOf(noteName);
        Note note = notesList.get(index);
        note.play();
    }
}
