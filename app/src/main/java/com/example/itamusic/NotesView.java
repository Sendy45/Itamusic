package com.example.itamusic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
@SuppressWarnings("FieldCanBeLocal")
public class NotesView extends View {
    private final Paint paint = new Paint();
    private final Paint notePaint = new Paint();
    private final Paint textPaint = new Paint();
    private ArrayList<String> key;
    private ArrayList<String> chord;
    private ArrayList<ArrayList<String>> notes;
    // Dynamic sizing
    private float startY;
    private float lineSpacing;
    private int staffWidth;
    private int viewHeight;
    private int viewWidth;
    private float staffMarginHorizontal = 20f;
    private final float noteSpacingFactor = 0.12f; // How much of the width to space notes by
    private final float clefSizeFactor = 0.25f;     // Percentage of height for clef symbol
    private final float noteSizeFactor = 0.23f;     // Percentage of height for note symbol
    private final ArrayList<String> notesNamesListInSharps = new ArrayList<>(Arrays.asList(
            "C4", "C#4", "D4", "D#4", "E4", "F4", "F#4", "G4", "G#4", "A4", "A#4", "B4",
            "C5", "C#5", "D5", "D#5", "E5", "F5", "F#5", "G5", "G#5", "A5", "A#5", "B5",
            "C6", "C#6", "D6", "D#6", "E6", "F6", "F#6", "G6", "G#6", "A6", "A#6", "B6"));
    private final ArrayList<String>notesNamesListInFlats = new ArrayList<>(Arrays.asList(
            "C4", "Db4", "D4", "Eb4", "E4", "F4", "Gb4", "G4", "Ab4", "A4", "Bb4", "B4",
            "C5", "Db5", "D5", "Eb5", "E5", "F5", "Gb5", "G5", "Ab5", "A5", "Bb5", "B5",
            "C6", "Db6", "D6", "Eb6", "E6", "F6", "Gb6", "G6", "Ab6", "A6", "Bb6", "B6"));

    // Custom view constructor - called when the view is created programmatically
    public NotesView(Context context) {
        super(context);       // Call the superclass constructor
        init(context);        // Initialize custom properties
    }

    // Constructor - called when the view is created from XML
    public NotesView(Context context, AttributeSet attrs) {
        super(context, attrs); // Call the superclass constructor with attributes
        init(context);         // Initialize custom properties
    }

    // Initialize custom properties
    private void init(Context context) {
        paint.setStrokeWidth(5);

        // Set default color of paints to black
        paint.setColor(Color.BLACK);
        notePaint.setColor(Color.BLACK);
        textPaint.setColor(Color.BLACK);

        // Load a special font from the assets folder (used for music notation symbols)
        // "bravura.otf" is a standard font for musical symbols like clefs, notes, rests, etc.
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/bravura.otf");
        textPaint.setTypeface(typeface);
    }

    // Adjust view for size changing
    @Override
    protected void onSizeChanged(int w, int h, int wOld, int hOld) {
        super.onSizeChanged(w, h, wOld, hOld);
        this.viewWidth = w;
        this.viewHeight = h;

        // Dynamic spacing based on view height
        lineSpacing = viewHeight / 12f;
        startY = viewHeight / 4.5f;

        // Dynamic staff width
        staffMarginHorizontal = viewWidth * 0.08f;
        staffWidth = viewWidth - (int)(2 * staffMarginHorizontal);

        // Adjust clef size
        textPaint.setTextSize(viewHeight * clefSizeFactor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw 5 staff lines
        for (int i = 0; i < 5; i++) {
            float y = startY + i * lineSpacing;
            canvas.drawLine(staffMarginHorizontal, y, staffMarginHorizontal + staffWidth, y, paint);
        }

        // Draw trebleClef
        drawTrebleSign(canvas);

        // Check what to draw
        if (chord != null) drawChord(chord, canvas);
        if (key != null) drawKey(key, canvas);
        if (notes != null) drawNotes(notes, canvas);
    }
    // Draw treble clef on the left edge
    private void drawTrebleSign(Canvas canvas) {
        String trebleClef = getContext().getString(R.string.treble_clef);
        float x = staffMarginHorizontal + 0.2f;
        float y = startY + lineSpacing * 2;
        canvas.drawText(trebleClef, x, y, textPaint);
    }
    // Draw note from index and name
    private void drawNote(Canvas canvas, String note, int index) {
        // Calculate X,Y based on pitch and index
        float noteX = staffMarginHorizontal + 150 + index * staffWidth * noteSpacingFactor;
        float noteY = startY + lineSpacing * 4 - (getPitchPosition(note) * lineSpacing / 2);

        drawWholeNoteSymbol(canvas, noteX, noteY, note);
    }
    // Draw whole note (4/4) by X,Y and name
    private void drawWholeNoteSymbol(Canvas canvas, float noteX, float noteY, String note) {
        // Adjust size
        textPaint.setTextSize(viewHeight * noteSizeFactor); // Proportional size

        String wholeNote = getContext().getString(R.string.whole_note);
        canvas.drawText(wholeNote, noteX, noteY + lineSpacing, textPaint);

        // Add extra line if needed (Checking in function)
        drawExtraLine(canvas, noteX, noteY);

        // Add sign if needed
        if (note.contains("#")) drawSharpSign(canvas, noteX, noteY);
        if (note.contains("b")) drawFlatSign(canvas, noteX, noteY);
    }
    // Convert pitch to a vertical position (e.g., C4 = 0, E4 = 2, etc.)
    private int getPitchPosition(String pitch) {
        ArrayList<String> notesNames = new ArrayList<>(Arrays.asList(
                "C4", "D4", "E4", "F4", "G4", "A4", "B4",
                "C5", "D5", "E5", "F5", "G5", "A5", "B5",
                "C6", "D6", "E6", "F6", "G6", "A6", "B6"
        ));
        ArrayList<String> sharpNotesNames = new ArrayList<>(Arrays.asList(
                "C#4", "D#4", "E#4", "F#4", "G#4", "A#4", "B#4",
                "C#5", "D#5", "E#5", "F#5", "G#5", "A#5", "B#5",
                "C#6", "D#6", "E#6", "F#6", "G#6", "A#6", "B#6"
        ));
        ArrayList<String> flatNotesNames = new ArrayList<>(Arrays.asList(
                "Cb4", "Db4", "Eb4", "Fb4", "Gb4", "Ab4", "Bb4",
                "Cb5", "Db5", "Eb5", "Fb5", "Gb5", "Ab5", "Bb5",
                "Cb6", "Db6", "Eb6", "Fb6", "Gb6", "Ab6", "Bb6"
        ));
        // Check all lists
        if (notesNames.contains(pitch)) // Normal
            return notesNames.indexOf(pitch);
        if (sharpNotesNames.contains(pitch)) // Sharps
            return sharpNotesNames.indexOf(pitch);
        if (flatNotesNames.contains(pitch)) // Flats
            return flatNotesNames.indexOf(pitch);
        return 0;
    }
    // Draw extra lines for notes outside of the 5 lines
    private void drawExtraLine(Canvas canvas, float noteX, float noteY) {
        float topStaffY = startY;
        float bottomStaffY = startY + lineSpacing * 4;
        float noteHeadWidth = lineSpacing * 1.5f;

        // Above the staff
        if (noteY < topStaffY - noteHeadWidth/2) {
            // Add Lines according to position
            int linesCount = Math.round((topStaffY - noteY - lineSpacing) / (lineSpacing));
            for (int i = 1; i <= linesCount; i++) {
                float y = topStaffY - i * (lineSpacing);
                canvas.drawLine(noteX - noteHeadWidth / 4, y, noteX + noteHeadWidth * 5 /4, y, paint);
            }
        }

        // Below the staff
        if (noteY > bottomStaffY - noteHeadWidth) {
            // Add Lines according to position
            int linesCount = Math.round((noteY + noteHeadWidth - bottomStaffY - lineSpacing ) / (lineSpacing));
            for (int i = 1; i <= linesCount; i++) {
                float y = bottomStaffY + i * (lineSpacing);
                canvas.drawLine(noteX - noteHeadWidth / 4, y, noteX + noteHeadWidth * 5 /4, y, paint);
            }
        }
    }
    // Draw sharp sign next to given note X,Y
    private void drawSharpSign(Canvas canvas, float noteX, float noteY) {
        // Adjust size
        textPaint.setTextSize(viewHeight * 0.15f); // Proportional size

        canvas.drawText("♯", noteX - lineSpacing / 2, noteY + lineSpacing, textPaint);
    }
    // Draw flat sign next to given note X,Y
    private void drawFlatSign(Canvas canvas, float noteX, float noteY) {
        // Adjust size
        textPaint.setTextSize(viewHeight * 0.15f); // Proportional size

        canvas.drawText("♭", noteX - lineSpacing / 2, noteY + lineSpacing, textPaint);
    }
    // Draw current chord
    public void drawChord(ArrayList<String> chord, Canvas canvas)
    {
        // Draw the notes
        for (int i = 0; i < chord.size(); i++) {
            String note = chord.get(i);
            drawNote(canvas, note, 2);  // Removed context here, use getContext() instead
            // index = 2 for vertical stacking
        }
    }
    // Draw current key
    public void drawKey(ArrayList<String> key, Canvas canvas)
    {
        // Draw the notes
        for (int i = 0; i < key.size(); i++) {
            String note = key.get(i);
            drawNote(canvas, note, i);  // Removed context here, use getContext() instead
        }
    }
    // Draw current notes
    public void drawNotes(ArrayList<ArrayList<String>> notes, Canvas canvas)
    {
        // Draw the notes
        for (int i = 0; i < notes.size(); i++) {
            for (int j = 0; j < notes.get(i).size(); j++){
                String note = notes.get(i).get(j);
                drawNote(canvas, note, i+1);  // Removed context here, use getContext() instead
                // i+1 for spacing from Treble Clef symbol
            }
        }
    }
    public void setChord(ArrayList<String> chordGiven)
    {
        chord = chordGiven;
        invalidate();
    }

    public void setKey(ArrayList<String> keyGiven)
    {
        key = keyGiven;
        invalidate();
    }
    public void setNotes(ArrayList<ArrayList<String>> notesGiven)
    {
        notes = notesGiven;
        invalidate();
    }
    // Return chord in noteStructure by given pitch and type (Major, Minor)
    public NotesStructure getChord(int pitch, int type)
    {
        ArrayList<String> chordNotes = new ArrayList<>();
        ArrayList<String> notesNamesList = notesNamesListInSharps; // Default - sharps

        // Check for keys where flats are used
        if ((Arrays.asList(0,5,7,10).contains(pitch) && type==1) ||
                (Arrays.asList(1,3,8,10).contains(pitch) && type==0))
            notesNamesList = notesNamesListInFlats; // Special case for flats

        chordNotes.add(notesNamesList.get(pitch)); // Root note
        chordNotes.add(notesNamesList.get(pitch+7)); // Perfect fifth

        if (type==0)
            chordNotes.add(notesNamesList.get(pitch+4)); // Major chord
        else
            chordNotes.add(notesNamesList.get(pitch+3)); // Minor chord

        String rootNote = notesNamesList.get(pitch); // Get root note for chord name
        rootNote = rootNote.substring(0, rootNote.length() - 1); // Remove the scale number (E4 -> E)

        String chordType = (type == 0) ? "Major" : "Minor"; // (Major if 0)

        String chordName = rootNote + " " + chordType;

        return new NotesStructure(chordName, chordNotes);
    }
    // Return key in noteStructure by given pitch and type (Major, Minor)
    public NotesStructure getKey(int pitch, int type)
    {
        ArrayList<String> keyNotes = new ArrayList<>();
        ArrayList<String> notesNamesList = notesNamesListInSharps; // Default - sharps

        // Check for keys where flats are used
        if ((Arrays.asList(0,2,5,7,10).contains(pitch) && type==1) ||
                (Arrays.asList(1,3,5,8,10).contains(pitch) && type==0))
            notesNamesList = notesNamesListInFlats; // Special case for flats

        // (6,0)# (3,1)# Unique keys, needs extra attention

        // First note
        keyNotes.add(notesNamesList.get(pitch));
        // Second note
        if (pitch==3 && type==1)
            keyNotes.add("E#4");
        else
            keyNotes.add(notesNamesList.get(pitch+2));
        // Third note
        if (type==0)
            keyNotes.add(notesNamesList.get(pitch+4)); // Major key
        else
            keyNotes.add(notesNamesList.get(pitch+3)); // Minor key
        // forth note
        keyNotes.add(notesNamesList.get(pitch+5));
        // Fifth note
        keyNotes.add(notesNamesList.get(pitch+7));
        // Sixth note
        // Seventh note
        if (type==0)
        {
            keyNotes.add(notesNamesList.get(pitch+9)); // Major key
            if (pitch==6)
                keyNotes.add("E#5");
            else
                keyNotes.add(notesNamesList.get(pitch+11));
        }
        else
        {
            keyNotes.add(notesNamesList.get(pitch+8)); // Minor key
            keyNotes.add(notesNamesList.get(pitch+10));
        }

        String rootNote = notesNamesList.get(pitch); // Get root note for key name
        rootNote = rootNote.substring(0, rootNote.length() - 1); // Remove the scale number (E4 -> E)

        String keyType = (type == 0) ? "Major" : "Minor"; // (Major if 0)

        String keyName = rootNote + " " + keyType;

        return new NotesStructure(keyName, keyNotes);
    }
    // Return random chord
    public NotesStructure getRandomChord()
    {
        int pitch = new Random().nextInt(12); // (C - B)
        int type = new Random().nextInt(2); // (Major or Minor)

        return getChord(pitch, type);
    }
    // Return Random Key
    public NotesStructure getRandomKey()
    {
        int pitch = new Random().nextInt(12); // (C - B)
        int type = new Random().nextInt(2); // (Major or Minor)

        return getKey(pitch, type);
    }
}
// Mini class for storing the name and structure of a series of notes
class NotesStructure {
    String name;
    ArrayList<String> notes;

    // Constructor
    NotesStructure(String name, ArrayList<String> notes) {
        this.name = name;
        this.notes = notes;
    }
}