package com.example.itamusic;

import java.io.Serializable;

public class Stats implements Serializable {

    private int intervalsQuestions;
    private int intervalsCorrect;
    private int notesQuestions;
    private int notesCorrect;
    private int chordsQuestions;
    private int chordsCorrect;
    private int keysQuestions;
    private int keysCorrect;

    // Constructor
    // resetting all to zero
    public Stats() {
        this.intervalsQuestions = 0;
        this.intervalsCorrect = 0;
        this.notesQuestions = 0;
        this.notesCorrect = 0;
        this.chordsQuestions = 0;
        this.chordsCorrect = 0;
        this.keysQuestions = 0;
        this.keysCorrect = 0;
    }

    // Increment methods
    public void incrementIntervalsQuestions(boolean correct) {
        intervalsQuestions++;
        if (correct) {
            intervalsCorrect++;
        }
    }

    public void incrementNotesQuestions(boolean correct) {
        notesQuestions++;
        if (correct) {
            notesCorrect++;
        }
    }

    public void incrementChordsQuestions(boolean correct) {
        chordsQuestions++;
        if (correct) {
            chordsCorrect++;
        }
    }

    public void incrementKeysQuestions(boolean correct) {
        keysQuestions++;
        if (correct) {
            keysCorrect++;
        }
    }

    // Getters
    public int getIntervalsQuestions() {
        return intervalsQuestions;
    }

    public int getIntervalsCorrect() {
        return intervalsCorrect;
    }

    public int getNotesQuestions() {
        return notesQuestions;
    }

    public int getNotesCorrect() {
        return notesCorrect;
    }

    public int getChordsQuestions() {
        return chordsQuestions;
    }

    public int getChordsCorrect() {
        return chordsCorrect;
    }

    public int getKeysQuestions() {
        return keysQuestions;
    }

    public int getKeysCorrect() {
        return keysCorrect;
    }

    // Calculate success rates
    public double getIntervalsSuccessRate() {
        return calculateSuccessRate(intervalsCorrect, intervalsQuestions);
    }

    public double getNotesSuccessRate() {
        return calculateSuccessRate(notesCorrect, notesQuestions);
    }

    public double getChordsSuccessRate() {
        return calculateSuccessRate(chordsCorrect, chordsQuestions);
    }

    public double getKeysSuccessRate() {
        return calculateSuccessRate(keysCorrect, keysQuestions);
    }

    // Helper method to calculate success rate
    private double calculateSuccessRate(int correct, int total) {
        if (total == 0) {
            return 0.0;
        }
        return (correct / (double) total) * 100.0;
    }
}
