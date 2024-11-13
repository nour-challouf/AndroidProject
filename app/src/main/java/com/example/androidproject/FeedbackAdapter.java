package com.example.androidproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import android.widget.Button;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private List<Feedback> feedbackList;

    public FeedbackAdapter(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);
        holder.feedbackText.setText(feedback.getFeedbackText());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.feedbackTimestamp.setText(sdf.format(feedback.getTimestamp()));

        // Set delete button functionality with confirmation dialog
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Feedback")
                    .setMessage("Are you sure you want to delete this feedback?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        feedbackList.remove(position);  // Remove item from list
                        notifyItemRemoved(position);  // Notify adapter about item removal
                        notifyItemRangeChanged(position, feedbackList.size());  // Refresh list to update positions
                        dialog.dismiss();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        // Set edit button functionality with an input dialog
        holder.editButton.setOnClickListener(v -> {
            // Show an input dialog to edit the feedback
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Edit Feedback");

            // Set up the input
            final EditText input = new EditText(v.getContext());
            input.setText(feedback.getFeedbackText());
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("Save", (dialog, which) -> {
                String updatedText = input.getText().toString();
                if (!updatedText.isEmpty()) {
                    feedback.setFeedbackText(updatedText);  // Update feedback text
                    notifyItemChanged(position);  // Notify adapter about item update
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView feedbackText, feedbackTimestamp;
        Button deleteButton, editButton;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            feedbackText = itemView.findViewById(R.id.feedbackText);
            feedbackTimestamp = itemView.findViewById(R.id.feedbackTimestamp);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
