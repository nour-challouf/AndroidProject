package com.example.androidproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
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

    private FeedbackActivity feedbackActivity;

    public FeedbackAdapter(FeedbackActivity feedbackActivity, List<Feedback> feedbackList) {
        this.feedbackActivity = feedbackActivity;
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
        holder.feedbackTimestamp.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(feedback.getTimestamp()));
        holder.displayRatingBar.setRating(feedback.getRating());

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
            // Show an input dialog to edit the feedback text and rating
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Edit Feedback");

            // Inflate a custom layout for editing feedback and rating
            View editView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_edit_feedback, null);
            EditText inputText = editView.findViewById(R.id.editFeedbackText);
            RatingBar inputRatingBar = editView.findViewById(R.id.editRatingBar);

            // Set current feedback text and rating
            inputText.setText(feedback.getFeedbackText());
            inputRatingBar.setRating(feedback.getRating());

            builder.setView(editView);

            builder.setPositiveButton("Save", (dialog, which) -> {
                String updatedText = inputText.getText().toString();
                float updatedRating = inputRatingBar.getRating();
                if (!updatedText.isEmpty()) {
                    feedback.setFeedbackText(updatedText);  // Update feedback text
                    feedback.setRating(updatedRating);  // Update feedback rating
                    notifyItemChanged(position);  // Notify adapter about item update
                    feedbackActivity.updateAverageRating(); // Update the average rating display
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
        RatingBar displayRatingBar;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            feedbackText = itemView.findViewById(R.id.feedbackText);
            feedbackTimestamp = itemView.findViewById(R.id.feedbackTimestamp);
            displayRatingBar = itemView.findViewById(R.id.displayRatingBar); // Initialize the rating bar
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
