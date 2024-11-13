package com.example.androidproject;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackActivity extends AppCompatActivity {

    private EditText feedbackInput;
    private EditText searchInput;
    private Button submitButton;
    private RecyclerView feedbackRecyclerView;
    private FeedbackAdapter feedbackAdapter;

    private static List<Feedback> feedbackList = new ArrayList<>();
    private List<Feedback> filteredFeedbackList = new ArrayList<>(); // List to hold filtered feedback

    private RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackInput = findViewById(R.id.feedbackInput);
        searchInput = findViewById(R.id.searchInput);  // Reference to the search bar
        submitButton = findViewById(R.id.submitButton);
        feedbackRecyclerView = findViewById(R.id.feedbackRecyclerView);

        // Initialize the adapter with the full list
        filteredFeedbackList.addAll(feedbackList);
        feedbackAdapter = new FeedbackAdapter(filteredFeedbackList);
        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedbackRecyclerView.setAdapter(feedbackAdapter);

        //Rarting Inze
        ratingBar = findViewById(R.id.ratingBar);


        submitButton.setOnClickListener(view -> {
            String feedbackText = feedbackInput.getText().toString();
            float rating = ratingBar.getRating(); // Get the rating

            if (!feedbackText.isEmpty()) {
                Feedback feedback = new Feedback(feedbackText, System.currentTimeMillis(), rating);
                feedbackList.add(feedback);  // Add feedback to the main list
                updateFilteredList(""); // Update filtered list to include new feedback
                feedbackInput.setText("");   // Clear input after submission
                ratingBar.setRating(0); // Reset the rating after submission
                Toast.makeText(this, "Feedback submitted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter feedback", Toast.LENGTH_SHORT).show();
            }
        });

        // Add a TextWatcher to the search input to filter the feedback list
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateFilteredList(s.toString()); // Update filtered list based on search text
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Method to filter the list and update the adapter
    private void updateFilteredList(String query) {
        filteredFeedbackList.clear();
        if (query.isEmpty()) {
            filteredFeedbackList.addAll(feedbackList);
        } else {
            filteredFeedbackList.addAll(feedbackList.stream()
                    .filter(feedback -> feedback.getFeedbackText().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        feedbackAdapter.notifyDataSetChanged();
    }
}
