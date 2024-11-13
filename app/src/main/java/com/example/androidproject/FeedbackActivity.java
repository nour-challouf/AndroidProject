package com.example.androidproject;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
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
    private Spinner sortSpinner;
    private TextView averageRatingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackInput = findViewById(R.id.feedbackInput);
        searchInput = findViewById(R.id.searchInput);
        submitButton = findViewById(R.id.submitButton);
        feedbackRecyclerView = findViewById(R.id.feedbackRecyclerView);
        ratingBar = findViewById(R.id.ratingBar);
        sortSpinner = findViewById(R.id.sortSpinner);
        // Reference to the average rating TextView
        averageRatingTextView = findViewById(R.id.averageRatingTextView);

        // Initialize the adapter with the full list
        filteredFeedbackList.addAll(feedbackList);
        feedbackAdapter = new FeedbackAdapter(this, filteredFeedbackList);
        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedbackRecyclerView.setAdapter(feedbackAdapter);
        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedbackRecyclerView.setAdapter(feedbackAdapter);

        // Set up sorting options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Most Recent", "Highest Rating", "Lowest Rating"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        // Spinner listener for sorting
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) parent.getItemAtPosition(position);
                sortFeedbackList(selectedOption); // Sort based on selected option
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Submit button to add feedback
        submitButton.setOnClickListener(view -> {
            String feedbackText = feedbackInput.getText().toString();
            float rating = ratingBar.getRating(); // Get the rating

            if (!feedbackText.isEmpty()) {
                Feedback feedback = new Feedback(feedbackText, System.currentTimeMillis(), rating);
                feedbackList.add(feedback); // Add feedback to the main list
                updateFilteredList(searchInput.getText().toString()); // Update with current search filter
                feedbackInput.setText(""); // Clear input after submission
                ratingBar.setRating(0); // Reset the rating after submission
                updateAverageRating(); // Update the average rating display
                Toast.makeText(this, "Feedback submitted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter feedback", Toast.LENGTH_SHORT).show();
            }
        });


        // TextWatcher for the search input to filter the feedback list
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateFilteredList(s.toString()); // Filter list based on search input
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        updateAverageRating();
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
        String selectedOption = (String) sortSpinner.getSelectedItem();
        sortFeedbackList(selectedOption); // Apply the selected sorting option
        feedbackAdapter.notifyDataSetChanged();
    }


    private void sortFeedbackList(String criteria) {
        switch (criteria) {
            case "Highest Rating":
                filteredFeedbackList.sort((f1, f2) -> Float.compare(f2.getRating(), f1.getRating()));
                break;
            case "Lowest Rating":
                filteredFeedbackList.sort((f1, f2) -> Float.compare(f1.getRating(), f2.getRating()));
                break;
            case "Most Recent":
                filteredFeedbackList.sort((f1, f2) -> Long.compare(f2.getTimestamp(), f1.getTimestamp()));
                break;
        }
        feedbackAdapter.notifyDataSetChanged();
    }

    public void updateAverageRating() {
        float totalRating = 0;
        for (Feedback feedback : feedbackList) {
            totalRating += feedback.getRating();
        }
        float averageRating = feedbackList.size() > 0 ? totalRating / feedbackList.size() : 0;
        averageRatingTextView.setText(String.format("Average Rating: %.1f", averageRating));
    }



}
