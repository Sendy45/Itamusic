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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private final ArrayList<Message> messages; // List of chat history
    private final Context context;
    private int selectedPosition = RecyclerView.NO_POSITION; // No item selected initially
    protected String selectedText = null;
    // Constructor
    public ChatAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    // Inflate different layouts for user and bot messages
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) { // user message
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_message, parent, false);
        } else { // bot message
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bot_message, parent, false);
        }
        return new ViewHolder(view);
    }
    // Manages an item in the recyclerView by position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageText.setText(message.text); // Set the message text

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
                selectedText = null;
            } else {
                // Otherwise select the new item
                selectedPosition = holder.getAdapterPosition();
                selectedText = message.text;
            }

            // Refresh both old and new positions
            notifyItemChanged(oldSelected);
            notifyItemChanged(selectedPosition);
        });

        // Handle long click to copy the text to clipboard
        holder.itemView.setOnLongClickListener(v -> {
            copyToClipboard(message.text); // Copy the message text when long-pressed
            return true; // Return true to indicate the long click was handled
        });
    }


    @Override
    // Determine message type
    public int getItemViewType(int position) {
        return messages.get(position).isUser ? 0 : 1;
    }

    @Override
    // Return number of messages
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText; // TextView for message display

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText); // Initialize message text view
        }
    }

    // Copy text to clipboard
    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Chat Message", text);
        clipboard.setPrimaryClip(clip);
    }
}
