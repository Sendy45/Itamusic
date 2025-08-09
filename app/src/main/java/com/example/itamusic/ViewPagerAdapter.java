package com.example.itamusic;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private final ArrayList<String> pagesText;
    private final ArrayList<Object> pagesNotes; // 2D or 3D depending on the page

    // Constructor
    public ViewPagerAdapter(ArrayList<String> pagesText, ArrayList<Object> pagesNotes){
        this.pagesText = pagesText;
        this.pagesNotes = pagesNotes;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each page (viewpager_page.xml)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpager_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the text (HTML formatted) for this page
        holder.tvInfo.setText(Html.fromHtml(pagesText.get(position), Html.FROM_HTML_MODE_LEGACY));

        // Get the corresponding notes data for this page
        Object noteData = pagesNotes.get(position);

        // Check if the notes are stored as an ArrayList (either 2D or 3D structure)
        if (noteData instanceof ArrayList) {
            ArrayList<?> innerList = (ArrayList<?>) noteData; // Doesn't know yet if 2D or 3D

            // Check if it's a 2D list (ArrayList<String>) or 3D list (ArrayList<ArrayList<String>>)
            if (!innerList.isEmpty() && innerList.get(0) instanceof String) {
                // Case: 2D - simple list of notes (a key)
                holder.notesView.setKey((ArrayList<String>) innerList);
            } else {
                // Case: 3D - list of multiple note groups (complex musical structure)
                holder.notesView.setNotes((ArrayList<ArrayList<String>>) noteData);
            }
        }
    }
    // Return the number of pages (same as size of text list)
    @Override
    public int getItemCount() {
        return pagesText.size();
    }

    // ViewHolder that holds the views for each page
    public static class ViewHolder extends RecyclerView.ViewHolder {
        NotesView notesView;
        TextView tvInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notesView = itemView.findViewById(R.id.notesView);
            tvInfo = itemView.findViewById(R.id.tvInfo);
        }
    }
}