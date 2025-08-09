package com.example.itamusic;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatNoteAdapter extends RecyclerView.Adapter<ChatNoteAdapter.ViewHolder> {
    private final ArrayList<ChatNote> chatNotes; // List of chat history
    private final Context context;
    protected int selectedPosition = RecyclerView.NO_POSITION; // No item selected initially
    // Constructor
    public ChatNoteAdapter(Context context, ArrayList<ChatNote> chatNotes) {
        this.context = context;
        this.chatNotes = chatNotes;
    }

    @NonNull
    @Override
    // Inflate layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatNote chatNote = chatNotes.get(position);
        holder.tvTitle.setText(chatNote.getTitle()); // Set chatNote title
        holder.tvBody.setText(chatNote.getBody()); // Set chatNote body

        // Highlight the selected item
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(Color.parseColor("#555555"));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        // Handle normal click to select/unselect the message
        holder.itemView.setOnClickListener(v -> {
            int oldSelected = selectedPosition; // Store old selected position

            if (selectedPosition == holder.getAdapterPosition()) {
                // If clicking the same item again -> unselect
                selectedPosition = RecyclerView.NO_POSITION;
            } else {
                // Otherwise select the new item
                selectedPosition = holder.getAdapterPosition();
            }

            // Refresh both old and new positions
            notifyItemChanged(oldSelected);
            notifyItemChanged(selectedPosition);
        });

        holder.itemView.setOnLongClickListener(v -> {
            copyToClipboard(chatNote.getBody()); // Copy message text on long press
            return true;
        });
    }

    @Override
    // Return number of chat notes
    public int getItemCount() {
        return chatNotes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // TextViews for chat note display
        TextView tvTitle, tvBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBody = itemView.findViewById(R.id.tvBody);
        }
    }

    // Copy body text to clipboard
    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Chat Message", text);
        clipboard.setPrimaryClip(clip);
    }
}