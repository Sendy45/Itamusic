package com.example.itamusic;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

public class Note {
    private String name;
    private int address;
    private float leftVol = 1f;
    private float rightVol = 1f;
    private int priority = 1;
    private int loop = 0;
    private float rate = 1f;
    private final SoundPool mSoundPool = new SoundPool(20, AudioManager.STREAM_MUSIC,0);;
    private Context context;
    private int sound;
    private int halfTones;

    // Shorted constructor for practical uses
    public Note(String name, int address, Context context, int halfTones) {
        this.name = name;
        this.address = address;
        this.context = context;
        this.halfTones = halfTones;
        sound = mSoundPool.load(context,address,1);
    }
    // Longer constructor
    public Note(String name, int address, float leftVol, float rightVol, float rate, Context context, int halfTones) {
        this.name = name;
        this.address = address;
        this.leftVol = leftVol;
        this.rightVol = rightVol;
        this.rate = rate;
        this.context = context;
        this.halfTones = halfTones;
        sound = mSoundPool.load(context,address,1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public float getLeftVol() {
        return leftVol;
    }

    public void setLeftVol(float leftVol) {
        this.leftVol = leftVol;
    }

    public float getRightVol() {
        return rightVol;
    }

    public void setRightVol(float rightVol) {
        this.rightVol = rightVol;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getHalfTones() {
        return halfTones;
    }

    public void setHalfTones(int halfTones) {
        this.halfTones = halfTones;
    }

    // Play sound
    public void play()
    {
        mSoundPool.play(sound,leftVol,rightVol,priority,loop,rate);
    }

    // Plays a given note for a given length of time
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
